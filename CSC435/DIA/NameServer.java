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
	private String name;
	private int id;
	private String ip;
	private int port;
	private Group group;

	public Agent(String name, int id, String ip, int port, Group group) {
		this.name = name;
		this.id = id;
		this.ip = ip;
		this.port = port;
		this.group = group;
	}

	public String getName() {
		return this.name;
	}

	public int getId() {
		return this.id;
	}

	public String getIp() {
		return this.ip;
	}

	public int getPort() {
		return this.port;
	}

	public Group getGroup() {
		return this.group;
	}
}

class Group implements Serializable {
	private String name;
	private String color;

	public Group(String name, String color) {
		this.name = name;
		this.color = color;
	}

	public String getName() {
		return this.name;
	}

	public String getColor() {
		return this.color;
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
	public List<Server> servers = new ArrayList<Server>();

	/**
	 * Construct
	 * @param s, the socket which is to be monitored
	 */
	public ServerConsumer (BlockingQueue<Server> qs)
	{
		this.qServer = qs;
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
				servers.add(srv);
				System.out.println("ServerConsumer> New Host Server is consumed, IP: " + srv.getIp() + ", Port: " + srv.getPort());
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
	public List<Agent> agents = new ArrayList<Agent>();

	/**
	 * Construct
	 * @param s, the socket which is to be monitored
	 */
	public AgentConsumer (BlockingQueue<Agent> qa)
	{
		this.qAgent = qa;
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
				agents.add(agt);
				System.out.println("Agent Consumed: " + agt.getName() + ", " + agt.getIp());
			}
		}
		catch(Exception ex){
			// Handle the exception
			System.out.println(ex);
		}
	}
}

class HostListener extends Thread {
	public String updMessage = "";
	String LocalHost = "localhost";
	int port = 48050;
	private BlockingQueue<Server> qServer;
	private BlockingQueue<Agent> qAgent;
	boolean hostRunning = true;
	List<Group> groups = new ArrayList<Group>();

	/**
	 * Constructor
	 * @param s, the socket which is to be monitored
	 * @param port, the port used to start a new server socket
	 */
	HostListener(BlockingQueue<Server> qs, BlockingQueue<Agent> qa) {
		this.qServer = qs;
		this.qAgent = qa;
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

			buildGroups();

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
					if (info.length == 2) {
						qServer.put(new Server(info[0], Integer.parseInt(info[1])));
						System.out.println("\nProducer> New Host Server is registered, IP: " + info[0] + ", Port:" + info[1]);
					}
				}
				else if (updMessage.startsWith("[NewAgent]")) {
					String agent = updMessage.substring(10, updMessage.length()).trim();
					System.out.println(agent);
					String[] info = agent.split("#");
					if (info.length == 5) {
						qAgent.put(new Agent(info[0], 1, info[2], Integer.parseInt(info[3]), getGroup()));
						System.out.println("Producer> New Agent registered, IP: " + info[2] + " Port:" + info[3]);
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

	private void buildGroups() {
		groups.add(new Group("A", "#842DCE"));
		groups.add(new Group("B", "#00FF00"));
		groups.add(new Group("C", "#FF0000"));
		groups.add(new Group("D", "#00FFFF"));
	}

	private Group getGroup() {
		int random = (int)(Math.random() * 4 + 1);
		return groups.get(random - 1);
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
	static List<Group> groups = new ArrayList<Group>();
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

			//Creating BlockingQueue of size 10
			BlockingQueue<Server> queueServer = new ArrayBlockingQueue<>(10);
			BlockingQueue<Agent> queueAgent = new ArrayBlockingQueue<>(10);
			HostListener producer = new HostListener(queueServer, queueAgent);
			ServerConsumer srvConsumer = new ServerConsumer(queueServer);
			AgentConsumer agtConsumer = new AgentConsumer(queueAgent);
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
				sendResponseToClient(LocalHost, Port, agtConsumer.agents, srvConsumer.servers, printer);
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
		sbHtml.append("          <th width=\"15%\"> Group </th>\n");
		sbHtml.append("          <th width=\"15%\"> Group ID </th>\n");
		sbHtml.append("          <th width=\"10%\"> Kill the Agent </th>\n");
		sbHtml.append("      </tr>\n");
		for (Agent agt: agents) {
			sbHtml.append("      <tr>\n");
			sbHtml.append("        <td> <a href=\"http://" + agt.getIp() + ":" + agt.getPort() + "\">" + agt.getName() + "</a> </td>\n");
			sbHtml.append("        <td> IP: " + agt.getIp() + " Port: " + agt.getPort() + "</td>\n");
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
