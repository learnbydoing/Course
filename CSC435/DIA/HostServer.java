/*--------------------------------------------------------
1. Name / Date:
Rong Zhuang
2 Nov, 2015

2. Java version used:
build 1.8.0_60

3. Precise command-line compilation examples / instructions:
> Run DIA.bat

4. Precise examples / instructions to run this program:
> Run DIA.bat

5. Notes:
 a. Host server monitors the new http request at port: 45050. Each time, it
    creates a new agent.
 b. In the web page of agent, there are three actions you can take. Refresh,
    which updates the state for current port. Migrate, which migrate the state
    from currentport to a new host server and valid port. New Request, which
    opens a new request, mentioned in above Note a.
----------------------------------------------------------*/
import java.io.*;
import java.net.*;

/**
 * AgentWorker objects are created by AgentListeners. It handles two kinds of
 * request. First, it monitors the 'Refresh' request, which maintains a state
 * value. The AgentWorker increment the state and send back to client. Second,
 * it checkes whether the request is asking for migration. The AgentWorker create
 * a connection to Host Server to require a new available port. Then, sends the
 * current state to client with the new port(migrate). At last, it closes the
 * previous server socket which is used before migration.
 */
class AgentWorker extends Thread {
	// logger
	LogHelper logger;
	// Connection to client
	Socket sock;
	// Maintains socket and state counter
	AgentHolder parentAgentHolder;
	// host server ip
	String hostIP;
	// host server port
	int hostPort;
	// Port of the agent
	int port;
	// Agent name
	String name = "";
	// Agent print name
	String printName = "";

	/**
	 * Constructor
	 * @param s, socket which is to be monitored
	 * @param port, port for current request
	 * @param ah, agentholder which holds to state counter
	 */
	AgentWorker (LogHelper lgr, Socket s, String name, String hostip, int hostport, int port, AgentHolder ah) {
		this.logger = lgr;
		this.sock = s;
		this.name = name;
		this.printName = "[" + name + "]";
		this.hostIP = hostip;
		this.hostPort = hostport;
		this.port = port;
		this.parentAgentHolder = ah;
	}

	/**
	 * Start to work, after being assigned tasks by the AgentListener
	 */
	public void run() {
		Socket clientSock;
		BufferedReader fromHostServer;
		PrintStream toHostServer;

		try {
			PrintStream printer = new PrintStream(sock.getOutputStream());
			BufferedReader reader = new BufferedReader(new InputStreamReader(sock.getInputStream()));

			// Read a line, normally, it's a HTTP GET request
			String req = reader.readLine();
			// Check whether there is any invalid input
			if (req == null || req.isEmpty() || req.indexOf("favicon.ico") > -1) {
				return;
			}
			// Print out to console
			logger.write("");
			if(req != null && req.toLowerCase().indexOf("migrate") > -1) { // Migrate request
				// Print that we are handling migration request
				logger.write(printName + " Received request for [MIGRATION].");
				logger.write(printName + " Ask Name Server for available host server.");
				// udp data: agent name + agent ip + agent port
				String udpData = "[RequireHostServer]" + name + "#" + hostIP + "#" + port;
				String newhost = UdpHelper.sendAndReceiveUDP(udpData, hostIP, 48050);
				String[] info;
				if (newhost == null || newhost.length() == 0)  {
					//System.out.println(printName + " Fail to get new host server, fail to migrate.");
					logger.write(printName + " Fail to get new host server, fail to migrate.");
				}
				else {
					info = newhost.split("#");
					if (info.length != 2) { // new host server ip + new host server port
						logger.write(printName + " The feedback returned from Name Server is invalid, fail to migrate.");
					}	else {
						// Create a new socket connected to Host Server at port 1565
						clientSock = new Socket(info[0], Integer.parseInt(info[1]));
						logger.write(printName + " New Host Server found, IP:" + info[0] + " Port:" + info[1]);
						fromHostServer = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
						// Send a request to the new host server for a new port
						toHostServer = new PrintStream(clientSock.getOutputStream());
						toHostServer.println("Require a new port for migration! [State=" + parentAgentHolder.agentState + "] [AgentName=" + name + "] [ServerPort="+info[1]+"]");
						toHostServer.flush();

						// Get the new port number
						String buf = "";
						while ((buf = fromHostServer.readLine()) != null) {
							if (buf.indexOf("[Port=") > -1) { // New port found
								break;
							}
						}

						// Extract the port number
						String strPort = buf.substring(buf.indexOf("[Port=") + 6, buf.indexOf("]", buf.indexOf("[Port=")));
						// Convert to int
						int newPort= Integer.parseInt(strPort);
						//log it to the server console
						logger.write(printName + " Got a new port: " + newPort + " from HostServer: " + info[0] +".");
						// Prepare the specific content
						String content = "<h4>We are migrating to " + info[0] + " at port: " + newPort + "</h4> \n";
						content = content + "<h4>The state before migration is " + parentAgentHolder.agentState +"</h4> \n";
						// Send migration response to client
						AgentListener.sendResponseToClient(hostIP, hostPort, name, newPort, req, content, printer);
						// Print that the current socket will be killed
						logger.write(printName + " Killing old agent (" + hostIP + ":" + port + ").");
						// Grab the socket at the old port(stored in the parentAgentHolder)
						ServerSocket ss = parentAgentHolder.sock;
						// Close the current socket which is abandoned.
						ss.close();
					}
				}
			}	else if(req.toLowerCase().indexOf("refresh") > -1) {
				// Print that we are handling refresh request
				logger.write(printName + " Received request for [REFRESH].");
				// Increment the state counter
				parentAgentHolder.agentState++;
				// Print that we are handling refresh request
				logger.write(printName + " You have state: " + parentAgentHolder.agentState + " at port: " + port);
				// Prepare the specific content
				String content = "<h4>We are having a conversation with state " + parentAgentHolder.agentState + "</h4>\n";
				// Send refresh response to client
				AgentListener.sendResponseToClient(hostIP, hostPort, name, port, req, content, printer);
			}	else if(req.toLowerCase().indexOf("kill") > -1) {
				// Print that we are handling kill request
				logger.write(printName + " Received request for [KILL].");
				// Send response to client
				AgentListener.sendErrorPageToClient(hostIP, hostPort, name, printer);
				// Print that the current agent will be killed
				logger.write(printName + " has been killed.");
				// Udp data: host ip + host port + agent name
				String udpData = "[AgentKilled]" + hostIP + "#" + hostPort + "#" + name;
				// Nofify name server
				UdpHelper.sendUDP(udpData, hostIP, 48050);
				// Grab the socket at the old port(stored in the parentAgentHolder)
				ServerSocket ss = parentAgentHolder.sock;
				// Close the current socket which is abandoned.
				ss.close();
			} else {
				// Unknown request
				logger.write(printName + " Unknown request, please try again!");
				String content = "<h4>You have not entered a valid request! Please try again!</h4>\n";
				AgentListener.sendResponseToClient(hostIP, hostPort, name, port, req, content, printer);
			}
			// Save log
			logger.save();
			// Close the socket anyway
			sock.close();
		} catch (IOException ioe) {
			//System.out.println(ioe);
		}
	}
}

/**
 * Agent holder object. Maintains socket and state, so we can track the
 * agentState and pass it between ports.
 */
class AgentHolder {
	// Active serversocket object
	ServerSocket sock;
	// AgentState
	int agentState;

	/**
	 * Constructor
	 * @param s, server socket
	 */
	AgentHolder(ServerSocket s) { sock = s; }
}

/**
 * An AgentListener handles the requests for the host server and creates agents.
 */
class AgentListener extends Thread {
	// logger
	LogHelper logger;
	// A reference of the client socket
	Socket sock;
	// Server
	String hostIP;
	// Port of the host server
	int hostPort;
	// Agent name
	String agentName = "";

	/**
	 * Constructor
	 * @param lgr, logger
	 * @param s, the socket which is to be monitored
	 * @param port, the port used to start a new agent
	 */
	AgentListener(LogHelper log, Socket s, String hostip, int port) {
		this.logger = log;
		this.sock = s;
		this.hostIP = hostip;
		this.hostPort = port;
	}

	/**
	 * Start to work, after being assigned tasks by the Host Server
	 */
	public void run() {
		try {
			PrintStream printer = new PrintStream(sock.getOutputStream());
			BufferedReader reader =  new BufferedReader(new InputStreamReader(sock.getInputStream()));

			// Read a line, normally, it's a HTTP GET request
			String req = reader.readLine();
			
			// Check whether there is any invalid input
			if (req == null || req.isEmpty() || req.indexOf("favicon.ico") > -1) {
				return;
			}
			// Print out to console
			logger.write("\nAgentListener> req: " + req);

			boolean isMigration = false;
			// If [State=?] is found, it comes from migration, parse the request and store it
			if(req != null && req.indexOf("[State=") > -1 && req.indexOf("[AgentName=") > -1) {
				isMigration = true;
			}

			int currentState = 0;
			if (isMigration) {
				// Extract the state from the read line
				String strState = req.substring(req.indexOf("[State=")+7, req.indexOf("]", req.indexOf("[State=")));
				// Parse to int
				currentState = Integer.parseInt(strState);
				// Print to console
				logger.write("AgentListener> Current State is: " + currentState);
			}

			// Setup a new server but at different port
			ServerSocket servsock = new ServerSocket(0, 6);
			// Get a new valid port
			int newPort = servsock.getLocalPort();

			String serverPort = "";
			if (!isMigration) {
				// Agent data: host ip + host port + agent ip + agent port
				String agentData = "[NewAgent]" + hostIP + "#" + hostPort + "#" + hostIP + "#" + newPort;
				logger.write("AgentListener> Register new Agent(" + hostIP + ":" + newPort +") to NameServer.");
				agentName = UdpHelper.sendAndReceiveUDP(agentData, hostIP, 48050);
				logger.write("AgentListener> Registration succeed! Agent name: " + agentName);
			}
			else {
				// Extract the state from the read line
				agentName = req.substring(req.indexOf("[AgentName=")+11, req.indexOf("]", req.indexOf("[AgentName=")));
				serverPort = req.substring(req.indexOf("[ServerPort=")+12, req.indexOf("]", req.indexOf("[ServerPort=")));
				String agentData = "[AgentMigration]" + agentName + "#" + serverPort + "#" + hostIP + "#" + newPort;
				UdpHelper.sendUDP(agentData, hostIP, 48050);
				logger.write("AgentListener> [" + agentName+ "] has migrated to " + hostIP+": " + newPort);
			}

			// Send response page to client browser
			sendResponseToClient(hostIP, hostPort, agentName, newPort, req, "", printer);
			// Close to avoid hangon
			sock.close();
			// Save log
			logger.save();
			// Use a agentholder to store the socket and state, so server can 'remember'
			// the current state, even after migration.
			AgentHolder agenthold = new AgentHolder(servsock);
			// Update with the latest state, may comes from refresh or migrate
			agenthold.agentState = currentState;
			// Prepare for the upcoming requests
			while(true) {
				// Wait for requests
				sock = servsock.accept();
				// Create new agentworker object and start it up.
				new AgentWorker(logger, sock, agentName, hostIP, hostPort, newPort, agenthold).start();
			}

		}
		catch(IOException ioe) {
			//System.out.println("\nAgentListener> Exception occurs!\n");
			//System.out.println(ioe);
		}
		finally {
			//System.out.println("Agent [" + agentName +"] has been shutdown!");
		}
	}

	/**
	 * Build content in html format, and send back to client
	 * @param hostip, host server, eg. localhost
	 * @param hostport, port number for the host server
	 * @param agentname, agent name
	 * @param port, agent port
	 * @param req, http request from client
	 * @param content, response content to the request
 	 * @param printer, output printer
	 */
	static void sendResponseToClient(String hostip, int hostport, String agentname, int port, String req, String content, PrintStream printer) {
		String htmlPage = buildHtmlPage(hostip, hostport, agentname, port, req, content);
		String htmlHeader = buildHttpHeader(htmlPage.length());
		printer.println(htmlHeader);
		printer.println(htmlPage);
	}

	/**
	 * Build html page
	 * @param hostip, host server, eg. localhost
	 * @param hostport, port number for the host server
	 * @param agentname, agent name
	 * @param port, agent port
	 * @param req, http request from client
	 * @param content, content of the page
	 * @return, page text
	 */
	static String buildHtmlPage(String hostip, int hostport, String agentname, int port, String req, String content) {
		StringBuilder sbHtml = new StringBuilder();
		sbHtml.append("<!DOCTYPE html>\n");
		sbHtml.append("<html>\n");
		sbHtml.append("  <head>\n");
		sbHtml.append("    <title>Agent(" + agentname + ")</title>\n");
		sbHtml.append("    <link rel=\"icon\" href=\"http://www.google.com/favicon.ico\" type=\"image/x-icon\" />");
		sbHtml.append("  </head>\n");
		sbHtml.append("  <body>\n");
		sbHtml.append("    <input type=\"hidden\" name=\"Port\" value=\"[Port=" + port + "]\">");
		sbHtml.append("    <h1>This is Agent [" + agentname + "] at " + port + " on " + hostip + " </h1>\n");
		sbHtml.append("    <h2>You sent: " + req + " </h2>\n");
		sbHtml.append("    <h3>What can you do next?</h3>\n");
		sbHtml.append("    <p>1. Click 'Refresh' button to get a new state(each time increment 1) for the current port.\n");
		sbHtml.append("    <p>2. Click 'Migrate' button to migrate current state to another available host server with new port.</p>\n");
		sbHtml.append("    <p>3. Click 'New Request' link button to ask Host Server for a new available port to handle your upcoming requests.</p>\n");
		sbHtml.append("    <table>\n");
		sbHtml.append("      <tr>\n");
		sbHtml.append("        <td>\n");
		sbHtml.append("          <form method=\"GET\" action=\"http://" + hostip + ":" + port + "/action/refresh\">\n");
		sbHtml.append("            <input type=\"submit\" value=\"Refresh\">\n");
		sbHtml.append("          </form>\n");
		sbHtml.append("        </td>\n");
		sbHtml.append("        <td>\n");
		sbHtml.append("          <form method=\"GET\" action=\"http://" + hostip + ":" + port + "/action/migrate\">\n");
		sbHtml.append("            <input type=\"submit\" value=\"Migrate\">\n");
		sbHtml.append("          </form>\n");
		sbHtml.append("        </td>\n");
		sbHtml.append("        <td>\n");
		sbHtml.append("          <a href=\"http://" + hostip + ":" + hostport + "\" target=\"_blank\">New Request</a>\n");
		sbHtml.append("        </td>\n");
		sbHtml.append("      </tr>\n");
		sbHtml.append("    </table>\n");
		sbHtml.append(content);
		sbHtml.append("    <hr>\n");
		sbHtml.append("    <p>*This page is returned by " + hostip + " at port " + port + ".</p>\n");
		sbHtml.append("    <p>*This agent is hosted by by " + hostip + " at port " + hostport + ".</p>\n");
		sbHtml.append("  </body>\n");
		sbHtml.append("</html>\n");
		return sbHtml.toString();
	}

	/**
	 * Build http header
	 * @param length, length of the content
	 * @return, header text
	 */
	static String buildHttpHeader(long length) {
		StringBuilder sbHtml = new StringBuilder();
		sbHtml.append("HTTP/1.1 200 OK\r\n");
		sbHtml.append("Content-Length: " + length + "\r\n");
		sbHtml.append("Content-Type: text/html\r\n");
		return sbHtml.toString();
	}

	/**
	 * Build content in html format, and send back to client
	 * @param hostip, host server, eg. localhost
	 * @param hostport, port number for the host server
	 * @param agentname, agent name
 	 * @param printer, output printer
	 */
	static void sendErrorPageToClient(String hostip, int hostport, String agentname, PrintStream printer) {
		String htmlPage = buildErrorPage(hostip, hostport, agentname);
		String htmlHeader = buildHttpHeader(htmlPage.length());
		printer.println(htmlHeader);
		printer.println(htmlPage);
	}

	/**
	 * Build html page
	 * @param hostip, host server, eg. localhost
	 * @param hostport, port number for the host server
	 * @param agentname, agent name
	 * @return, page text
	 */
	static String buildErrorPage(String hostip, int hostport, String agentname) {
		StringBuilder sbHtml = new StringBuilder();
		sbHtml.append("<!DOCTYPE html>\n");
		sbHtml.append("<html>\n");
		sbHtml.append("  <head>\n");
		sbHtml.append("    <title>Agent(" + agentname + ")</title>\n");
		sbHtml.append("    <link rel=\"icon\" href=\"http://www.google.com/favicon.ico\" type=\"image/x-icon\" />");
		sbHtml.append("  </head>\n");
		sbHtml.append("  <body>\n");
		sbHtml.append("    <h1>Agent [" + agentname + "] has been killed. </h1>\n");
		sbHtml.append("    <h3><a href=\"http://" + hostip + ":" + hostport + "\" target=\"_blank\">Host Server</a></h3>\n");
		sbHtml.append("    <h3><a href=\"http://" + hostip + ":48060\" target=\"_blank\">Name Server</a></h3>\n");
		sbHtml.append("  </body>\n");
		sbHtml.append("</html>\n");
		return sbHtml.toString();
	}
}

/**
 * A HostServer listens on specific port for requests. The default port number
 * is 45050. It can be specified when setting up the host server. When the host
 * server receives a new request, it assigns the request to a new AgentListener.
 * Then, the HostServer continues to monitor new requests.
 */
public class HostServer {
	// Use localhost as default server
	static String LocalHost = "localhost";
	// Define log file for each host server
	static String FILE_LOG_Host_SERVER = "Log_HostServer_{0}.txt";
	/**
	 * Start a Server Socket to monitor client requests and dispatches the request
	 * to AgentListener.
	 */
	public static void main(String[] args) {
		// The maximum queue length for incoming connection
		int queue_len = 6;
		// Port number for http request
		int port = 45050;
		// A reference of the client socket
		Socket sock;

		try{
			if (args.length == 1) { // Port number is specified
				try {
					// Convert it number
					port = Integer.parseInt(args[0]);
				}
				catch(NumberFormatException Ex) {
					System.out.println(args[0] + " is not an Integer");
					System.exit(5);
				}
			}
			// Create log instance, each has an independent log file, separated by port number
			LogHelper logger = new LogHelper(FILE_LOG_Host_SERVER.replace("{0}", String.valueOf(port)));
			logger.clear();

			//LocalHost = InetAddress.getLocalHost().getHostAddress();

			// Setup the server socket
			ServerSocket servsock = new ServerSocket(port, queue_len);
			logger.write("Rong Zhuang's Host Server is starting up, listening at port " + port + ".");
			// Sleep for a while, make sure name server is setup before sending registeration request to it
			Thread.sleep(3000);
			logger.write("Register this host server to name server...");
			// Send request to name server for regiseration
			UdpHelper.sendUDP("[NewHostServer]" + LocalHost + "#" + port, LocalHost, 48050);
			logger.write("Host Server info has been sent to NameServer.");
			// Save log
			logger.save();

			while(true) {
				// Open socket for requests
				sock = servsock.accept();
				// Create new agent listener at this port to wait for requests
				new AgentListener(logger, sock, LocalHost, port).start();
			}
		}
		catch(IOException ex){
			//Handle the exception
			System.out.println(ex);
		}
		catch(InterruptedException irx){
			//Handle the exception
			System.out.println(irx);
		}
		finally {
			System.out.println("Host Server has been shutdown!");
		}
	}
}
