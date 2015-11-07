/*--------------------------------------------------------
0. Refer to John Reagan's host server. I rewrited it, see the details below.
http://condor.depaul.edu/elliott/435/hw/programs/hostserver/HostServer-Reagan.java.txt

1. Name / Date:
Rong Zhuang
23 Oct, 2015

2. Java version used:
build 1.8.0_60

3. Precise command-line compilation examples / instructions:
> javac HostServer.java

4. Precise examples / instructions to run this program:
 a. In separate shell windows: > java HostServer
 b. Launch a web browser and point it to http://localhost:1565
 c. Three actions to be taken: Refresh, Migrate, New Request

5. List of files needed for running the program.
 a. AgentHolder.class
 b. AgentListener.class
 c. AgentWorker.class
 d. HostServer.class

6. Notes:
 a. Host server monitors the new http request at port: 1565. A new valid port
    will be assigned to handle the follow-on requests.
 b. In the web page, there are three actions you can take. Refresh, which updates
    the state for current port. Migrate, which migrate the state from current
    port to a new valid port. New Request, which opens a new request, mentioned
    in above Note a.
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
 * previous server socket which is used before migration. *
 */
class AgentWorker extends Thread {
	// Use localhost as default server
	String LocalHost = "localhost";
	// Connection to client
	Socket sock;
	// Maintains socket and state counter
	AgentHolder parentAgentHolder;
	// Port being used by current request
	int hostPort;
	int port;
	String name = "";
	String printName = "";

	/**
	 * Constructor
	 * @param s, socket which is to be monitored
	 * @param port, port for current request
	 * @param ah, agentholder which holds to state counter
	 */
	AgentWorker (Socket s, String name, int hostport, int port, AgentHolder ah) {
		this.sock = s;
		this.name = name;
		this.printName = "[" + name + "]";
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
			PrintStream out = new PrintStream(sock.getOutputStream());
			BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

			// Read a line, normally, it's a HTTP GET request
			String req = in.readLine();
			// Check whether there is any invalid input
			if (req == null || req.isEmpty() || req.indexOf("favicon.ico") > -1) {
				//System.out.println("AgentListener> Invalid request.");
				//sock.close();
				return;
			}
			// Print out to console
			//System.out.println(name + "> req: " + req);
			System.out.println("");
			if(req != null && req.toLowerCase().indexOf("migrate") > -1) { // Migrate request
				// Print that we are handling migrationg request
				System.out.println(printName + " Received request for [migration].");
				System.out.println(printName + " Ask Name Server for available host server.");
				// Agent data: host ip + host port + agent ip + agent port
				String udpData = "[RequireHostServer]"+name+"#localhost#" + port;
				String newhost = askForNewHostServer(udpData, "localhost", 48050);
				String[] info;
				if (newhost == null || newhost.length() == 0)  {
					System.out.println(printName + " Fail to get new host server, fail to migrate.");
				}
				else {
					info = newhost.split("#");
					if (info.length != 2) {
						System.out.println(printName + " The feedback returned from Name Server is invalid, fail to migrate.");
					}	else {
						// Create a new socket connected to Host Server at port 1565
						clientSock = new Socket(info[0], Integer.parseInt(info[1]));
						System.out.println(printName + " New Host Server found, IP:" + info[0] + " Port:" + info[1]);
						fromHostServer = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
						// Send a request to port 1565 to receive the next available port
						toHostServer = new PrintStream(clientSock.getOutputStream());
						toHostServer.println("Please send me a new available port for migration! [State=" + parentAgentHolder.agentState + "] [AgentName="+name+"]" + "] [ServerPort="+info[1]+"]");
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
						System.out.println(printName + " Got a new port: " + newPort + " from HostServer: " + info[0] +".");

						// Prepare the specific content
						String content = "<h4>We are migrating to " + LocalHost + " at port: " + newPort + "</h4> \n";
						content = content + "<h4>The state before migration is " + parentAgentHolder.agentState +"</h4> \n";
						// Send migration response to client
						AgentListener.sendResponseToClient(hostPort, LocalHost, newPort, req, content, out);
						// Print that the current socket will be killed
						System.out.println(printName + " Killing parent agent listener.");
						// Grab the socket at the old port(stored in the parentAgentHolder)
						//ServerSocket ss = parentAgentHolder.sock;
						// Close the current socket which is abandoned.
						//ss.close();
					}
				}
			}	else if(req.toLowerCase().indexOf("refresh") > -1) {
				// Print that we are handling refresh request
				System.out.println(name + " Received request for [refresh].");
				// Increment the state counter
				parentAgentHolder.agentState++;
				// Print that we are handling refresh request
				System.out.println(printName + " You have state: " + parentAgentHolder.agentState + " at port: " + port);
				// Prepare the specific content
				String content = "<h4>We are having a conversation with state " + parentAgentHolder.agentState + "</h4>\n";
				// Send refresh response to client
				AgentListener.sendResponseToClient(hostPort, LocalHost, port, req, content, out);
			} else {
				// Unknown request
				System.out.println(printName + " Unknown request, please try again!");
				String content = "<h4>You have not entered a valid request! Please try again!</h4>\n";
				AgentListener.sendResponseToClient(hostPort, LocalHost, port, req, content, out);
			}
			//close the socket anyway
			sock.close();
		} catch (IOException ioe) {
			System.out.println(ioe);
		}
	}

	/**
	 * Send joke or proverb to client with UDP
	 * @param newJokeProverb, joke or proverb
	 * @param port, port for client
	 */
	static String askForNewHostServer(String udpdata, String server, int port) throws IOException {
		// Define package length
		int len = 1024;
		// Define byte array to store original data
		byte[] receiveData = new byte[len];
		// Data from UDP message with actual length
		byte[] data;
		// Define UDP socket
		DatagramSocket clientSocket = new DatagramSocket();
		// Get ip address for localhost
		InetAddress ipAddress = InetAddress.getByName(server);
		// Define default package length
		len = udpdata.getBytes().length;
		// Define byte array for sending data
		byte[] sendData = new byte[len];
		sendData = udpdata.getBytes();
		// Define upd package
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, port);
		// Send to client
		clientSocket.send(sendPacket);
		//System.out.println("Request has been sent to NameServer.");
		// Define receive package
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		// Wait for receiving data from server
		clientSocket.receive(receivePacket);
		// Create array with actual length
		data = new byte[receivePacket.getLength()];
		// Convert original package data with actual length, the blankspace are removed in the tail
		System.arraycopy(receivePacket.getData(), receivePacket.getOffset(), data, 0, receivePacket.getLength());
		// Update the public attribute, so the main process can get the value.
		String feedback = new String(data);
		//System.out.println("Feedback: " + feedback + " returned from NameServer.");
		// Close connection after sending
		clientSocket.close();

		return feedback;
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
 * AgentListener objects monitor individual ports and respond to requests
 * made upon them(in this scenario from a standard web browser); They are created
 * by the hostserver when a new request is made to port 1565.
 */
class AgentListener extends Thread {
	String LocalHost = "localhost";
	Socket sock;
	int hostPort;

	/**
	 * Constructor
	 * @param s, the socket which is to be monitored
	 * @param port, the port used to start a new server socket
	 */
	AgentListener(Socket s, int port) {
		this.sock = s;
		this.hostPort = port;
	}

	/**
	 * Start to work, after being assigned tasks by the Host Server
	 */
	public void run() {
		try {
			PrintStream out = new PrintStream(sock.getOutputStream());
			BufferedReader in =  new BufferedReader(new InputStreamReader(sock.getInputStream()));

			// Read a line, normally, it's a HTTP GET request
			String req = in.readLine();
			// Print out to console
			System.out.println("\nAgentListener> req: " + req);

			// Check whether there is any invalid input
			if (req == null || req.isEmpty() || req.indexOf("favicon.ico") > -1) {
				//System.out.println("AgentListener> Invalid request.");
				//sock.close();
				return;
			}

			// If [State=?] is found, it comes from migration, parse the request and store it
			boolean isMigration = false;
			if(req != null && req.indexOf("[State=") > -1 && req.indexOf("[AgentName=") > -1) {
				isMigration = true;
			}

			int agentState = 0;
			if (isMigration) {
				// Extract the state from the read line
				String strState = req.substring(req.indexOf("[State=")+7, req.indexOf("]", req.indexOf("[State=")));
				// Parse to int
				agentState = Integer.parseInt(strState);
				// Print to console
				System.out.println("AgentListener> agentState is: " + agentState);
			}

			// Setup a new server but at different port
			ServerSocket servsock = new ServerSocket(0, 6);
			int port = servsock.getLocalPort();

			// Send response page to client browser
			sendResponseToClient(hostPort, LocalHost, port, req, "", out);
			// Close to avoid hangon
			sock.close();

			String agentName = "";
			String serverPort = "";
			if (!isMigration) {
				// Agent data: host ip + host port + agent ip + agent port
				String agentData = "[NewAgent]" + LocalHost + "#" + hostPort + "#localhost#" + port;
				System.out.println("AgentListener> Register new Agent(localhost:" + port +") to NameServer.");
				agentName = registerAgentToNameServer(agentData, "localhost", 48050);
				System.out.println("AgentListener> Registration succeed! Agent name: " + agentName);
			}
			else {
				// Extract the state from the read line
				agentName = req.substring(req.indexOf("[AgentName=")+11, req.indexOf("]", req.indexOf("[AgentName=")));
				serverPort = req.substring(req.indexOf("[ServerPort=")+12, req.indexOf("]", req.indexOf("[ServerPort=")));
				String agentData = "[AgentMigration]" + agentName + "#"+serverPort+"#localhost#" + port;
				notifyMigrationToNameServer(agentData, "localhost", 48050);
			}

			// Use a agentholder to store the socket and state, so server can 'remember'
			// the current state, even after migration.
			AgentHolder agenthold = new AgentHolder(servsock);
			// Update with the latest state, may comes from refresh or migrate
			agenthold.agentState = agentState;

			// Prepare for the upcoming requests
			while(true) {
				//System.out.println("["+agentName+"] is wating for request at port " + port + " ...");
				sock = servsock.accept();
				// Create new agentworker object and start it up.
				new AgentWorker(sock, agentName, hostPort, port, agenthold).start();
			}

		}
		catch(IOException ioe) {
			System.out.println("\nAgentListener> Exception occurs!\n");
			System.out.println(ioe);
		}
	}

	/**
	 * Send joke or proverb to client with UDP
	 * @param newJokeProverb, joke or proverb
	 * @param port, port for client
	 */
	static String registerAgentToNameServer(String udpdata, String server, int port) throws IOException {
		// Define package length
		int len = 1024;
		// Define byte array to store original data
		byte[] receiveData = new byte[len];
		// Data from UDP message with actual length
		byte[] data;
		// Define UDP socket
		DatagramSocket clientSocket = new DatagramSocket();
		// Get ip address for localhost
		InetAddress ipAddress = InetAddress.getByName(server);
		// Define default package length
		len = udpdata.getBytes().length;
		// Define byte array for sending data
		byte[] sendData = new byte[len];
		sendData = udpdata.getBytes();
		// Define upd package
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, port);
		// Send to client
		clientSocket.send(sendPacket);
		//System.out.println("Registration request has been sent to NameServer.");
		// Define receive package
		DatagramPacket receivePacket = new DatagramPacket(receiveData, receiveData.length);
		// Wait for receiving data from server
		clientSocket.receive(receivePacket);
		// Create array with actual length
		data = new byte[receivePacket.getLength()];
		// Convert original package data with actual length, the blankspace are removed in the tail
		System.arraycopy(receivePacket.getData(), receivePacket.getOffset(), data, 0, receivePacket.getLength());
		// Update the public attribute, so the main process can get the value.
		String feedback = new String(data);
		//System.out.println("New Agent Name: " + feedback + " returned from NameServer.");
		// Close connection after sending
		clientSocket.close();

		return feedback;
	}

	static void notifyMigrationToNameServer(String udpdata, String server, int port) throws IOException {
		// Define UDP socket
		DatagramSocket clientSocket = new DatagramSocket();
		// Get ip address for localhost
		InetAddress ipAddress = InetAddress.getByName(server);
		// Define default package length
		int len = udpdata.getBytes().length;
		// Define byte array for sending data
		byte[] sendData = new byte[len];
		sendData = udpdata.getBytes();
		// Define upd package
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, port);
		// Send to client
		clientSocket.send(sendPacket);
		//System.out.println("Migration notification: " + udpdata + " has been sent to NameServer.");
		// Close connection after sending
		clientSocket.close();
	}

	/**
	 * Build content in html format, and send back to client
	 * @param host, host server, eg. localhost
	 * @param port, port number for the current connection
	 * @param req, http request from client
	 * @param content, response content to the request
 	 * @param printer, output printer
	 */
	static void sendResponseToClient(int hostport, String host, int port, String req, String content, PrintStream printer) {
		String htmlPage = buildHtmlPage(hostport, host, port, req, content);
		String htmlHeader = buildHttpHeader(htmlPage.length());
		printer.println(htmlHeader);
		printer.println(htmlPage);
	}

	/**
	 * Build html page
	 * @param host, host server, eg. localhost
	 * @param port, port number for the current connection
	 * @param req, http request from client
	 * @param content, content of the page
	 * @return, page text
	 */
	static String buildHtmlPage(int hostport, String host, int port, String req, String content) {
		StringBuilder sbHtml = new StringBuilder();
		sbHtml.append("<!DOCTYPE html>\n");
		sbHtml.append("<html>\n");
		sbHtml.append("  <head>\n");
		sbHtml.append("    <title>Host Server</title>\n");
		sbHtml.append("    <link rel=\"icon\" href=\"http://www.google.com/favicon.ico\" type=\"image/x-icon\" />");
		sbHtml.append("  </head>\n");
		sbHtml.append("  <body>\n");
		sbHtml.append("    <input type=\"hidden\" name=\"Port\" value=\"[Port=" + port + "]\">");
		sbHtml.append("    <h1>This is for submission to PORT " + port + " on " + host + " </h1>\n");
		sbHtml.append("    <h2>You sent: " + req + " </h2>\n");
		sbHtml.append("    <h3>What can you do next?</h3>\n");
		sbHtml.append("    <p>1. Click 'Refresh' button to get a new state(each time increment 1) for the current port.\n");
		sbHtml.append("    <p>2. Click 'Migrate' button to migrate current state to another available port.</p>\n");
		sbHtml.append("    <p>3. Click 'New Request' link button to ask Host Server for a new available port to handle your upcoming requests.</p>\n");
		sbHtml.append("    <table>\n");
		sbHtml.append("      <tr>\n");
		sbHtml.append("        <td>\n");
		sbHtml.append("          <form method=\"GET\" action=\"http://" + host + ":" + port + "/action/refresh\">\n");
		sbHtml.append("            <input type=\"submit\" value=\"Refresh\">\n");
		sbHtml.append("          </form>\n");
		sbHtml.append("        </td>\n");
		sbHtml.append("        <td>\n");
		sbHtml.append("          <form method=\"GET\" action=\"http://" + host + ":" + port + "/action/migrate\">\n");
		sbHtml.append("            <input type=\"submit\" value=\"Migrate\">\n");
		sbHtml.append("          </form>\n");
		sbHtml.append("        </td>\n");
		sbHtml.append("        <td>\n");
		sbHtml.append("          <a href=\"http://" + host + ":" + hostport + "\" target=\"_blank\">New Request</a>\n");
		sbHtml.append("        </td>\n");
		sbHtml.append("      </tr>\n");
		sbHtml.append("    </table>\n");
		sbHtml.append(content);
		sbHtml.append("    <hr>\n");
		sbHtml.append("    <p>*This page is returned by " + host + " at port " + port + ".</p>\n");
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
}

/**
 * A HostServer listens on port 1565 for requests. When a new request comes, it
 * increase NextPort by one and assign this request to a new AgentListener. Then,
 * HostServer continues to monitor requests at port 1565. Each request will get
 * a independent port for continous access.
 */
public class HostServer {
	/**
	 * Start a Server Socket to monitor client requests and dispatches the request
	 * to AgentListener.
	 */
	public static void main(String[] args) {
		int q_len = 6;
		int port = 45050; // Port for first request
		Socket sock;

		try{
			if (args.length == 1) {
				try { // Have an argument, so use it
					port = Integer.parseInt(args[0]);
				}
				catch(NumberFormatException Ex) {
					/* Have checked that a[0] is actually a number */
					System.out.println(args[0] + " is not an Integer");
					System.exit(5);
				}
			}

			ServerSocket servsock = new ServerSocket(port, q_len);
			System.out.println("HostServer> Rong Zhuang's Host Server has started at port " + port + ".");

			String serverData = "[NewHostServer]localhost#" + port;
			Thread.sleep(3000);
			sendServerInfoToNameServer(serverData, "localhost", 48050);

			while(true) {
				// Open socket for requests
				sock = servsock.accept();
				//create new agent listener at this port to wait for requests
				new AgentListener(sock, port).start();
			}
		}
		catch(IOException ex){
			//Handle the exception
			System.out.println(ex);
		}
		catch(InterruptedException ex){
			//Handle the exception
			System.out.println(ex);
		}
		finally {
			System.out.println("Name Server has been shutdown!");
		}
	}

	/**
	 * Send joke or proverb to client with UDP
	 * @param newJokeProverb, joke or proverb
	 * @param port, port for client
	 */
	static void sendServerInfoToNameServer(String hostinfo, String server, int port) throws IOException {
		// Define UDP socket
		DatagramSocket clientSocket = new DatagramSocket();
		// Get ip address for localhost
		InetAddress ipAddress = InetAddress.getByName(server);
		// Define default package length
		int len = hostinfo.getBytes().length;
		// Define byte array for sending data
		byte[] sendData = new byte[len];
		sendData = hostinfo.getBytes();
		// Define upd package
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, port);
		// Send to client
		clientSocket.send(sendPacket);
		System.out.println("Host Server info has been sent to NameServer.");
		// Close connection after sending
		clientSocket.close();
	}
}
