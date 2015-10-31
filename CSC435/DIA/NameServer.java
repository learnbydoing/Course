/*--------------------------------------------------------
1. Name / Date:
Rong Zhuang
3 Oct, 2015

2. Java version used:
build 1.8.0_60

3. Precise command-line compilation examples / instructions:
> javac MyWebServer.java

4. Precise examples / instructions to run this program:
In separate shell windows:
> java MyWebServer

5. List of files needed for running the program.
 a. HttpWorker.class
 b. MyWebServer.class

6. Notes:
 a. Web Server monitors the http request at port: 2540.
 b. Web Server is capable of handling requests simultaneously.
 c. The logs in console will be saved to file MyWebServerLogs.txt. The requests
    for icons will not be displayed, since there are too many such logs when you
    are exploreing the direcotry.
 d. Main functions:
  1) Explore files/directories recursively
  2) Display content of a single file;
  3) Handle dynamical script execution, eg. fake-cgi request.
	4) Logs will be saved each time after a new request is handled.
----------------------------------------------------------*/
import java.io.*;
import java.net.*;
import java.util.*;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

class Server implements Serializable {
	private String ip = "";
	private int port = 0;
	private List<Agent> agents = new ArrayList<Agent>();

	public Server(String ip, int port) {
		this.ip = ip;
		this.port = port;
	}

  public String getIp() {
		return this.ip;
	}

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public int getCounter() {
		return this.agents.size();
	}

	public void addAgent(Agent ag) {
		this.agents.add(ag);
	}

	public void removeAgent(String name) {
		for(Agent ag : this.agents) {
			if (ag.getName().equals(name)) {
				this.agents.remove(ag);
			}
		}
	}
}

class Agent implements Serializable {
	private int id;
	private String name;
	private String ip;
	private int port;
	private Server server;
	private Group group;

	public Agent(int id, String name, String ip, int port, Server server, Group group) {
		this.id = id;
		this.name = name;
		this.ip = ip;
		this.port = port;
		this.server = server;
		this.group = group;
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public String getIp() {
		return this.ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public int getPort() {
		return this.port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Server getServer() {
		return this.server;
	}

	public Group getGroup() {
		return this.group;
	}

	public String toString() {
		return this.name + ":" + this.ip + ":" + this.port;
	}
}

class Group implements Serializable {
	private int id;
	private String name;
	private String color;
	private List<Agent> agents = new ArrayList<Agent>();

	public Group(int id, String name, String color) {
		this.id = id;
		this.name = name;
		this.color = color;
	}

	public int getId() {
		return this.id;
	}

	public String getName() {
		return this.name;
	}

	public String getColor() {
		return this.color;
	}

	public int getCounter() {
		return this.agents.size();
	}

	public void addAgent(Agent ag) {
		this.agents.add(ag);
	}

	public void removeAgent(int id) {
		for(Agent ag : this.agents) {
			if (ag.getId() == id) {
				this.agents.remove(ag);
				break;
			}
		}
	}
}

class LogHelper {
	private static final String FILE_LOG = "NameServerLogs.txt";
	// Use an ArrayList to store the logs from server
	private static ArrayList<String> alLogs = new ArrayList<String>();
	/*
				// Clear list each time for handling new reqeust
				alLogs.clear();
				writeLog("Rong Zhuang's Name Worker is working...");
				// Local reader from the client
				BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			  // Output stream to the client
				PrintStream printer = new PrintStream(socket.getOutputStream());

				writeLog("There are " + agents.size() + " agents and " + servers.size() + " servers.");

				sendResponseToClient(LocalHost, Port, agents, servers, printer);

				writeLog("The current status has been sent back to client.");
				// Save logs to file
				saveLogs(true);
				socket.close();*/


	/**
	 * Write log to local storage list
	 * @param log, the content of the log
	 */
	private static void writeLog(String log) {
		writeLog(log, true);
	}

	/**
	 * Write log to local storage list
	 * @param log, the content of the log
	 * @param print, print to screen
	 */
	private static void writeLog(String log, boolean print) {
		// Store new log
		alLogs.add(log);

		if(print) {
			// Print the log
			System.out.println(log);
		}
	}
	/**
	 * Save logs to the specified file
	 * @param append, ture is append, false is override
	 */
	private static void saveLogs(boolean append) {
		try {
			if (alLogs!=null && alLogs.size()>0) {
				// Convert the ArrayList to string array
				String[] arrLogs = new String[alLogs.size()];
				arrLogs = alLogs.toArray(arrLogs);

				// Open the log
				FileWriter fileWriterLog = new FileWriter(FILE_LOG, append);

				// User BufferedWriter to add new line
				BufferedWriter bufferedWriterLog = new BufferedWriter(fileWriterLog);

				// Go through all the content and write them to the file
				for(int ix=0; ix < arrLogs.length; ix++) {
					// Write the current log
					bufferedWriterLog.write(arrLogs[ix]);
					// One log one line
					bufferedWriterLog.newLine();
				}

				// Always close files.
				bufferedWriterLog.close();
			}
		}
		catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + FILE_LOG + "'");
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
	}
}

/**
 * An instance of HttpWorker for the Web Server.
 * The Worker handles the http requests for the server. Its job is to accept
 * requests for the client and send results back.
 */
class ServerConsumer extends Thread {
	private BlockingQueue<Server> qServer;
	private List<Server> lServer;

	/**
	 * Construct
	 * @param s, the socket which is to be monitored
	 */
	public ServerConsumer (BlockingQueue<Server> qs, List<Server> ls)
	{
		this.qServer = qs;
		this.lServer = ls;
	}

	/**
	 * Start to work, after being assigned tasks by the server
	 */
	public void run(){
		try{
			Server srv;
			//consuming messages until exit message is received
			while ((srv = qServer.take()) != null)
			{
				lServer.add(srv);
				System.out.println("New Host Server(" + srv.getIp() + ":" + srv.getPort() + ") has been registered.");
			}
		}
		catch(Exception ex){
			// Handle the exception
			System.out.println(ex);
		}
	}
}

/**
 * An instance of HttpWorker for the Web Server.
 * The Worker handles the http requests for the server. Its job is to accept
 * requests for the client and send results back.
 */
class AgentConsumer extends Thread {
	private BlockingQueue<Agent> qAgent;
	private List<Agent> lAgent;
	private List<Server> lServer;
	private List<Group> lGroup;

	/**
	 * Construct
	 * @param s, the socket which is to be monitored
	 */
	public AgentConsumer (BlockingQueue<Agent> qa, List<Agent> la, List<Server> ls, List<Group> lg)
	{
		this.qAgent = qa;
		this.lAgent = la;
		this.lServer = ls;
		this.lGroup = lg;
	}

	/**
	 * Start to work, after being assigned tasks by the server
	 */
	public void run(){
		try{
			Agent agt;
			//consuming messages until exit message is received
			while ((agt = qAgent.take()) != null)
			{
				agt.setId(lAgent.size() + 1);
				lAgent.add(agt);
				// Add agent to server
				for(Server srv: lServer) {
					if (srv.getIp().equals(agt.getServer().getIp())
							&& srv.getPort() == agt.getServer().getPort()) {
						srv.addAgent(agt);
						System.out.println("Agent "+agt.getName()+" has been added to server "+srv.getPort());
						//System.out.println("Server "+srv.getPort()+" has " + srv.getCounter() + " agents. ");
						break;
					}
				}
				// Add agent to group
				for(Group grp: lGroup) {
					if (grp.getId() == agt.getGroup().getId()) {
						grp.addAgent(agt);
						System.out.println("Agent "+agt.getName()+" has been added to group "+grp.getName());
						//System.out.println("Group "+grp.getName()+" has " + grp.getCounter() + " agents. ");
						break;
					}
				}
				System.out.println("New Agent(" + agt.toString() + ") has been registered.");
			}
		}
		catch(Exception ex){
			// Handle the exception
			System.out.println(ex);
		}
	}
}

class CommunicationListener extends Thread {
	public String updMessage = "";
	String LocalHost = "localhost";
	int port = 48050;
	private BlockingQueue<Server> qServer;
	private BlockingQueue<Agent> qAgent;
	boolean hostRunning = true;
	private List<Server> lServer;
	private List<Agent> lAgent;
	private List<Group> lGroup;
	Queue<String> nameQueue = new LinkedList<String>();

	/**
	 * Constructor
	 * @param s, the socket which is to be monitored
	 * @param port, the port used to start a new server socket
	 */
	CommunicationListener(BlockingQueue<Server> qs, BlockingQueue<Agent> qa, List<Server> ls, List<Agent> la, List<Group> lg) {
		this.qServer = qs;
		this.qAgent = qa;
		this.lServer = ls;
		this.lAgent = la;
		this.lGroup = lg;
	}

	/**
	 * Start to work, after being assigned tasks by the Host Server
	 */
	public void run() {
		try{
			// Define package length
			int len = 1024;
			// Define byte array to store original data
			byte[] receiveData = new byte[len];
			// Data from UDP message with actual length
			byte[] data;

			// Define UDP socket with specific port
			DatagramSocket serverSocket = new DatagramSocket(port);
			// Define UDP package
			DatagramPacket receivePacket = new DatagramPacket(receiveData, len);

			buildAgentNames();

			while(hostRunning) {
				// Wait for receiving data from server
				serverSocket.receive(receivePacket);
				// Create array with actual length
				data = new byte[receivePacket.getLength()];
				// Convert original package data with actual length, the blankspace are removed in the tail
				System.arraycopy(receivePacket.getData(), receivePacket.getOffset(), data, 0, receivePacket.getLength());
				// Update the public attribute, so the main process can get the value.
				updMessage = new String(data);

				if (updMessage.startsWith("[NewHostServer]")) {
					String hostserver = updMessage.substring(15, updMessage.length()).trim();
					//System.out.println(hostserver);
					String[] info = hostserver.split("#");
					if (info.length == 2) { // ip + port
						qServer.put(new Server(info[0], Integer.parseInt(info[1])));
						System.out.println("\nNew Host Server is found, IP: " + info[0] + ", Port:" + info[1]);
					}
				}
				else if (updMessage.startsWith("[NewAgent]")) {
					String agent = updMessage.substring(10, updMessage.length()).trim();
					//System.out.println(agent);
					String[] info = agent.split("#");
					if (info.length == 4) { // host ip + host port + agent ip + agent port
						Server server = new Server(info[0], Integer.parseInt(info[1]));
						String agentName = getNewAgentName();
						System.out.println("\nNew Agent is found, IP: " + info[2] + " Port:" + info[3]);
						System.out.println("Assigned with name: " + agentName);
						qAgent.put(new Agent(0, agentName, info[2], Integer.parseInt(info[3]), server, getRandomGroup()));
						// send name back to agent
						sendDataViaUpd(agentName, receivePacket.getAddress(), receivePacket.getPort());
					}
				}
				else if (updMessage.startsWith("[RequireHostServer]")) {
					String requirehost = updMessage.substring(19, updMessage.length()).trim();
					//System.out.println(requirehost);
					String[] info = requirehost.split("#");
					if (info.length == 3) { // agent ip + agent port
						System.out.println("\nAgent " + info[0] + "( " + info[1] + ":" + info[2]+") is requesting for new HostServer to migrate.");
						Server server = getRandomServer();
						String udpData = server.getIp() + "#" + server.getPort();
						sendDataViaUpd(udpData, receivePacket.getAddress(), receivePacket.getPort());
					}
				}
				else if (updMessage.startsWith("[AgentMigration]")) {
					String migration = updMessage.substring(16, updMessage.length()).trim();
					//System.out.println(migration);
					String[] info = migration.split("#");
					if (info.length == 4) { // agent name + new ip + new port
						for(Agent agt: lAgent) {
							if (agt.getName().equals(info[0])) {
								agt.setIp(info[2]);
								agt.setPort(Integer.parseInt(info[3]));
								agt.getServer().setPort(Integer.parseInt(info[1]));
								break;
							}
						}
						System.out.println("Agent(" + info[0] + ") has migrated to " + info[1]+": "+info[2]);
					}
				}
			}
			// Close connetion
			serverSocket.close();
		}
		catch (IOException ioe) {
			System.out.println(ioe);
		}
		catch (InterruptedException ire) {
			System.out.println(ire);
		}
		finally {
			//System.out.println("UDP Service is stopped!");
		}
	}

	/**
	 * Send joke or proverb to client with UDP
	 * @param newJokeProverb, joke or proverb
	 * @param port, port for client
	 */
	static void sendDataViaUpd(String udpdata, InetAddress ipAddress, int port) throws IOException {
		// Define UDP socket
		DatagramSocket clientSocket = new DatagramSocket();
		// Define default package length
		int len = udpdata.getBytes().length;
		// Define byte array for sending data
		byte[] sendData = new byte[len];
		sendData = udpdata.getBytes();
		// Define upd package
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, ipAddress, port);
		// Send to client
		clientSocket.send(sendPacket);
		//System.out.println("Send data:" + udpdata + " to hostserver.");
		// Close connection after sending
		clientSocket.close();
	}

	private String getNewAgentName() {
		return nameQueue.poll();
	}

	private void buildAgentNames() {
		nameQueue.offer("Alice");
		nameQueue.offer("Bob");
		nameQueue.offer("Carold");
		nameQueue.offer("Dave");
		nameQueue.offer("Frank");
		nameQueue.offer("Johnny");
		nameQueue.offer("Ted");
		nameQueue.offer("Amei");
		nameQueue.offer("Clark");
		nameQueue.offer("Adam");
		nameQueue.offer("Wade");
		nameQueue.offer("Calvin");
		nameQueue.offer("Eric");
		nameQueue.offer("Galvin");
		nameQueue.offer("Henry");
		nameQueue.offer("Jack");
		nameQueue.offer("Morgan");
		nameQueue.offer("Paul");
		nameQueue.offer("Ross");
		nameQueue.offer("Riley");
	}

	private Group getRandomGroup() {
		int random = (int)(Math.random() * lGroup.size() + 1);
		return lGroup.get(random - 1);
	}

	private Server getRandomServer() {
		int random = (int)(Math.random() * lServer.size() + 1);
		return lServer.get(random - 1);
	}
}
/**
 * This web server can create HttpWorker to handle the http requests from the
 * client(Web browser). The server does nothing but dispatches requests to
 * workers. Each worker starts a new thread to handle the request. So the web
 * server can handle multiple requests simultaneously. The main functions
 * provided by this web server include: explore files/directories in the root
 * folder of the webserver; display content of a single file from server;
 * handle fake-cgi request, eg. add number.
 */
public class NameServer {
	// Define thread-safe list to share data between multiple threads
	static List<Server> servers = Collections.synchronizedList(new ArrayList<Server>());
	static List<Agent> agents = Collections.synchronizedList(new ArrayList<Agent>());
	static List<Group> groups = Collections.synchronizedList(new ArrayList<Group>());
	/**
	 * Start a Server Socket to monitor client requests and dispatches the http
	 * request to HttpWorkers.
	 */
	public static void main(String args[]){
		// The maximum queue length for incoming connection
		int queue_len = 6;
		// Port number for http request
		int port = 48060;
		// A reference of the client socket
		Socket socket;

		try{
			// Setup the server socket
			ServerSocket servsocket = new ServerSocket(port, queue_len);
			System.out.println("Rong Zhuang's Name Server is starting up, listening at port " + port + ".");

			// Build initial group list
			buildGroups();
			//Creating BlockingQueue of size 10
			BlockingQueue<Server> queueServer = new ArrayBlockingQueue<>(10);
			BlockingQueue<Agent> queueAgent = new ArrayBlockingQueue<>(10);
			CommunicationListener producer = new CommunicationListener(queueServer, queueAgent, servers, agents, groups);
			ServerConsumer srvConsumer = new ServerConsumer(queueServer, servers);
			AgentConsumer agtConsumer = new AgentConsumer(queueAgent, agents, servers, groups);
			//starting producer to produce messages in queue
			producer.start();
			//starting consumer to consume messages from queue
			srvConsumer.start();
			agtConsumer.start();
			System.out.println("Producer and Consumer are starting up.");

			while(true){
				// Make the server socket wait for the next client request
				socket = servsocket.accept();
				// Output stream to the client
				PrintStream printer = new PrintStream(socket.getOutputStream());
				String LocalHost = "localhost";
				int Port = 48060;
				sendResponseToClient(LocalHost, Port, agents, servers, printer);
			}
		}
		catch(IOException ex){
			//Handle the exception
			System.out.println(ex);
		}
		finally {
			System.out.println("Name Server has been shutdown!");
		}
	}

	static void buildGroups() {
		groups.add(new Group(1, "A", "#842DCE"));
		groups.add(new Group(2, "B", "#00FF00"));
		groups.add(new Group(3, "C", "#FF0000"));
		groups.add(new Group(4, "D", "#00FFFF"));
	}

	/**
	 * Build content in html format, and send back to client
	 * @param host, host server, eg. localhost
	 * @param port, port number for the current connection
	 * @param req, http request from client
	 * @param content, response content to the request
	 * @param printer, output printer
	 */
	static void sendResponseToClient(String host, int port, List<Agent> agents, List<Server> servers, PrintStream printer) {
		String htmlPage = buildHtmlPage(host, port, agents, servers);
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
	static String buildHtmlPage(String host, int port, List<Agent> agents, List<Server> servers) {
		StringBuilder sbHtml = new StringBuilder();
		sbHtml.append("<!DOCTYPE html>\n");
		sbHtml.append("<html>\n");
		sbHtml.append("  <head>\n");
		sbHtml.append("    <title>Name Server</title>\n");
		sbHtml.append("    <link rel=\"icon\" href=\"http://www.google.com/favicon.ico\" type=\"image/x-icon\" />");
		sbHtml.append("  </head>\n");
		sbHtml.append("  <body>\n");
		sbHtml.append("    <center> <h1> Agent Name Server </h1> </center>\n");
		sbHtml.append("    <h2> Agents </h2>\n");
		sbHtml.append("    <table style=\"background-color:white\" cellspacing=\"0\" cellpadding=\"3\" border=\"1\" width=\"100%\"> \n");
		sbHtml.append("      <tr>\n");
		sbHtml.append("          <th width=\"15%\"> Agent Name and Hot Link </th>\n");
		sbHtml.append("          <th width=\"15%\"> Current Location </th>\n");
		sbHtml.append("          <th width=\"15%\"> Host Server </th>\n");
		sbHtml.append("          <th width=\"10%\"> Group </th>\n");
		sbHtml.append("          <th width=\"10%\"> Group ID </th>\n");
		sbHtml.append("          <th width=\"10%\"> Kill the Agent </th>\n");
		sbHtml.append("      </tr>\n");
		for (Agent agt: agents) {
			sbHtml.append("      <tr>\n");
			sbHtml.append("        <td> <a href=\"http://" + agt.getIp() + ":" + agt.getPort() + "\">" + agt.getName() + "</a> </td>\n");
			sbHtml.append("        <td> IP: " + agt.getIp() + " Port: " + agt.getPort() + "</td>\n");
			sbHtml.append("        <td> IP: " + agt.getServer().getIp() + " Port: " + agt.getServer().getPort() + "</td>\n");
			sbHtml.append("        <td bgcolor=\"" + agt.getGroup().getColor() + "\"></td>\n");
			sbHtml.append("        <td>" + agt.getGroup().getName() + "</td>\n");
			sbHtml.append("        <td> <a href=\"http://127.0.0.1:4560/dienow\"> Kill! </a> </td>\n");
			sbHtml.append("      </tr>\n");
		}
		sbHtml.append("    </table>\n");
		sbHtml.append("    <h2> HostServers </h2>\n");
		sbHtml.append("    <table style=\"background-color:white\" cellspacing=\"0\" cellpadding=\"3\" border=\"1\" width=\"100%\"> \n");
		sbHtml.append("      <tr>\n");
		sbHtml.append("          <th width=\"15%\"> HostServer Location </th>\n");
		sbHtml.append("          <th width=\"15%\"> Other Information? </th>\n");
		sbHtml.append("      </tr>\n");
		for (Server svr: servers) {
			sbHtml.append("      <tr>\n");
			sbHtml.append("        <td> <a href=\"http://" + svr.getIp() + ":" + svr.getPort() + "\"> IP: " + svr.getIp() +" Port: " + svr.getPort() + " </a> </td>\n");
			sbHtml.append("        <td> Now hosting: " + svr.getCounter() + " agents </td>\n");
			sbHtml.append("      </tr>\n");
		}
		sbHtml.append("    </table>\n");
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
