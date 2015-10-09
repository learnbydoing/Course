/*--------------------------------------------------------
1. Name / Date:
Rong Zhuang
17 Sep, 2015

2. Java version used:
build 1.8.0_60

3. Precise command-line compilation examples / instructions:
> javac JokeServer.java
> javac JokeClient.java
> javac JokeClientAdmin.java

4. Precise examples / instructions to run this program:
In separate shell windows:
> java JokeServer
> java JokeClient
> java JokeClientAdmin

All acceptable commands are displayed on the various consoles.

If the server is running on the different machine with the client, you
need to pass the IP address of the server to the clients. For exmaple,
if the server is running at 140.192.34.32 then you would type:

> java JokeClient 140.192.34.32
> java JokeClientAdmin 140.192.34.32

5. List of files needed for running the program.
 a. Worker.class
 b. JokeServer.class
 c. JokeClient.class
 d. JokeClientAdmin.class
 e. AdminLooper.class
 f. AdminWorker.class

6. Notes:
 a. Server will monitor two ports: 4653 for general joke and proverb service,
    8722 for admin service.
 b. Regarding the general service, server can return random joke or proverb.
    Each user's state will be remembered, even if server restarts from shutdown.
 c. Server can read and write user states from/to disk. The states can be
    re-initialized after the server restart.
 d. The states are stored in file ServerUserStates.txt.
    Format of the state file.
    UUID, Blank Space, 10 digits separate by comma (1-used, 0-not used)
    One user one line, Exmaple:
    2b703f01-e9a4-4b20-a7a7-402009f1ded9 1,1,1,0,1,0,1,1,0,1
 e. To stop the server, type 'SD' in admin client.

----------------------------------------------------------*/
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * An instance of Worker for the server.
 * The Worker handles the joke/proverb service for the server. Its job is to
 * accept requests for the client and send results back.
 */
class Worker extends Thread {
	String FILE_USERSTATES = "ServerUserStates.txt";
	Socket socket;

	/**
	 * Construct
	 * @param s, the socket which is to be monitored
	 * @param mode, server mode
	 * @param list, joke/proverb list
	 * @param ss, user states
	 */
	public Worker (Socket s)
	{
		socket = s;
	}

	/**
	 * Start to work, after being assigned tasks by the server
	 */
	public void run(){
		// Output stream to the client
		PrintStream printer = null;
		// Local reader from the client
		BufferedReader reader = null;

		try{
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			printer = new PrintStream(socket.getOutputStream());
			try{
				String clientRequest = "";
				// Accept the command(user name + user key) from the client
				clientRequest = reader.readLine();

				if (clientRequest.startsWith("GET") && clientRequest.endsWith("HTTP/1.1")) {
					String req = clientRequest.substring(4, clientRequest.length()-9).trim();
					String root = getRootFolder();
					String path = root + "/" + req;
					File f = new File(path);
					if (!f.exists()||!f.isFile()) {
						printer.println("No such resource:" + req);
					}
					else {
						InputStream fs = new FileInputStream(f);
						printHttpHeader(path, f.length(), printer);
						sendFile(fs, printer);
					}
				}
				else {
					printer.println("Unknown request:" + clientRequest);
				}

				// Seek joke/proverb
				//buildResponse(clientRequest, printer);
				// Save states to file
				//saveStates(FILE_USERSTATES, mapStates);
				System.out.println("I'm waiting for new request...");
			}
			catch(IOException ex){
				System.out.println ("Exception occurs, see the below details:");
  			ex.printStackTrace ();
			}
			socket.close();
		}
		catch(IOException ex){
			// Handle the exception
			System.out.println(ex);
		}
	}

	private String getRootFolder() {
		String root = "";
		try{
			File f = new File(".");
			root = f.getCanonicalPath();
		}
		catch(IOException ex){
			ex.printStackTrace();
		}

		return root;
	}

	private static void sendFile(InputStream stream, OutputStream printer)
	{
		try {
			byte[] buffer = new byte[1000];
			while (stream.available()>0)
			printer.write(buffer, 0, stream.read(buffer));
		}
		catch(IOException ex){
			ex.printStackTrace();
		}
	}
	/**
	 * Seek joke or proverb
	 * @param username, the user name
	 * @param userkey, the user key(UUID)
	 * @param mode, Server mode
	 * @param maplist, joke or proverb list
	 * @param statelist, user states
	 * @param printer, the output stream object of the client
	 */
	private void buildResponse(String request, PrintStream printer){
		try{
			String htmlPage = buildHtmlPage(request);
			//printHttpHeader(htmlPage, printer);
			printer.println(htmlPage);

			System.out.println(request);
			//System.out.println(htmlPage);

		}
		catch(Exception ex) {
			//Handle the exception
			printer.println("Failed in attemp to build response!");
		}
	}
	private static String getContentType(String path)
	{
		if (path=="")
			 return "";

		String extension = path.substring(path.lastIndexOf(".") + 1);
		switch(extension) {
			case ".html":
			case ".htm":
				return "text/html";
			case ".txt":
				return "text/plain";
			default:
				return "text/html";
		}
	}
	private void printHttpHeader(String path, long length, PrintStream printer) {
		printer.println("HTTP/1.1 200 OK");
		printer.println("Content-Length: " + length);
		printer.println("Content-Type: "+ getContentType(path));
		printer.println("");
	}

	private String buildHtmlPage(String content) {
		StringBuilder sbHtml = new StringBuilder();
		sbHtml.append("<!DOCTYPE html>");
		sbHtml.append("<html>");
		sbHtml.append("<head>");
		sbHtml.append("<title>My Listener</title>");
		sbHtml.append("</head>");
		sbHtml.append("<body>");
		sbHtml.append("<h1>Server got your request:</h1>");
		sbHtml.append("<h3>" + content + "</h3>");
		sbHtml.append("<p>*This page is returned by Rong Zhuang's Web Server.</p>");
		sbHtml.append("</body>");
		sbHtml.append("</html>");
		return sbHtml.toString();
	}
}


/**
 * A server can create Worker to handle the general joke/proverb requests from
 * the client. It can also create Admin Worker to accept admin commands to
 * switch server mode. The server does nothing but dispatches tasks to Workers
 * or Admin Workers.
 */
public class MyListener {
	private static final String FILE_USERSTATES = "ServerUserStates.txt";
	/**
	 * Start a new AdminListener to monitor and admin service from Admin Client.
	 * Meanwhile, start a new Server Socket to handle general joke/proverb service
	 * from joke client.
	 */
	public static void main(String args[]){
		// The maximum queue length for incoming connection
		int queue_len = 6;
		// Port number for general requests(joke or proverb)
		int port = 2540;
		// A reference of the client socket
		Socket socket;

		try{
			// Setup the server socket
			ServerSocket servsocket = new ServerSocket(port, queue_len);
			System.out.println("Rong Zhuang's My Listener is starting up, listening at port " + port + ".");

			while(true){
				// Make the server socket wait for the next client request
				socket = servsocket.accept();
				// Get states from file
				// Assign task to general work to get joke or proverb
				new Worker(socket).start();
			}
		}
		catch(IOException ex){
			//Handle the exception
			System.out.println(ex);
		}
		finally {
			System.out.println("Server has been shutdown!");
		}
	}

}
