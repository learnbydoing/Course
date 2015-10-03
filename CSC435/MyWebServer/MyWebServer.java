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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLDecoder;
import java.util.Map;
import java.util.HashMap;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * An instance of Worker for the server.
 * The Worker handles the joke/proverb service for the server. Its job is to
 * accept requests for the client and send results back.
 */
class HttpWorker extends Thread {
	String FILE_USERSTATES = "ServerUserStates.txt";
	Socket socket;

	/**
	 * Construct
	 * @param s, the socket which is to be monitored
	 * @param mode, server mode
	 * @param list, joke/proverb list
	 * @param ss, user states
	 */
	public HttpWorker (Socket s)
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
				String rootDir = "";
				String path = "";
				// Accept the command(user name + user key) from the client
				clientRequest = reader.readLine();

				if (clientRequest!=null&&clientRequest.startsWith("GET") && clientRequest.endsWith("HTTP/1.1")) {
					String req = clientRequest.substring(4, clientRequest.length()-9).trim();
					if (req.indexOf(".")>-1) {// Request for file
						if (req.indexOf(".fake-cgi")>-1) { // CGI request
							Map<String, String> params = parseUrlParams(req);
							StringBuilder sbCGIHtml = new StringBuilder();
							sbCGIHtml.append("Dear " + params.get("person") + ", the sum of ");
							sbCGIHtml.append(params.get("num1") + " and " + params.get("num2") + " is ");
							int number1 = Integer.parseInt(params.get("num1"));
							int number2 = Integer.parseInt(params.get("num2"));

							sbCGIHtml.append(number1+number2);
							sbCGIHtml.append(".");
							String htmlPage = buildHtmlPage(sbCGIHtml.toString());
							printHttpHeader("aa.html", htmlPage.length(), printer);
							printer.println(htmlPage);
						}
						else { // General file request
							rootDir = getRootFolder();
							path = Paths.get(rootDir, req).toString();
							//printer.println("path:" + path);
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
					}
					else { // Request for directory
						String filedir ;
						rootDir = getRootFolder();
						path = Paths.get(rootDir, req).toString();
						//printer.println("path:" + path);
						File f = new File (path) ;
						if (!f.exists()) {
							printer.println("No such resource:" + req);
						}
						else {
							//System.out.println("path:"+path);
							// Get all the files and directory under your diretcory
							File[] strFilesDirs = f.listFiles();
							StringBuilder sbDirHtml = new StringBuilder();

							// Parent folder

							String parent = path.substring(0, path.lastIndexOf("\\"));
							System.out.println("parent:"+parent);
							System.out.println("rootDir:"+rootDir);
							if (parent.equals(rootDir)) {
								parent = "../";
							}
							else {
								parent = parent.replace(rootDir, "");
							}
							System.out.println("parent:"+parent);
							//System.out.println("parent:"+parent);
							sbDirHtml.append("<a href=\""+parent+"\">Parent Directory/</a><br>");

							for (int ix = 0; ix < strFilesDirs.length; ix++) {
								//System.out.println("strFilesDirs[ix]:"+strFilesDirs[ix]);
								if (strFilesDirs[ix].isDirectory()) {
									sbDirHtml.append("<a href=\""+strFilesDirs[ix].getName()+"/\">"+strFilesDirs[ix].getName()+"/</a><br>");
								}
								else if (strFilesDirs[ix].isFile()) {
									sbDirHtml.append("<a href=\""+strFilesDirs[ix].getName()+"\">"+strFilesDirs[ix].getName()+"</a><br>");
								}
							}

							String htmlPage = buildHtmlPage(sbDirHtml.toString());
							printHttpHeader(path, htmlPage.length(), printer);
							printer.println(htmlPage);

						}
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

	private Map<String, String> parseUrlParams(String url)  throws UnsupportedEncodingException {
		HashMap<String, String> mapParams = new HashMap<String, String>();
		if (url.indexOf("?")<0)
			return mapParams;

		url = url.substring(url.indexOf("?") + 1);
		String[] pairs = url.split("&");
		for (String pair : pairs) {
			int index = pair.indexOf("=");
			mapParams.put(URLDecoder.decode(pair.substring(0, index), "UTF-8"), URLDecoder.decode(pair.substring(index + 1), "UTF-8"));
		}
		return mapParams;
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
			return "text/html";
		if (path.lastIndexOf(".") < 0)
	 		return "text/html";

		String extension = path.substring(path.lastIndexOf("."));
		//System.out.println("extension:"+extension);
		switch(extension) {
			case ".html":
			case ".htm":
				return "text/html";
			case ".txt":
				return "text/plain";
			case ".ico":
				return "image/x-icon .ico";
			default:
				return "text/plain";
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
public class MyWebServer {
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
			System.out.println("Rong Zhuang's Web Server is starting up, listening at port " + port + ".");

			while(true){
				// Make the server socket wait for the next client request
				socket = servsocket.accept();
				// Get states from file
				// Assign task to general work to get joke or proverb
				new HttpWorker(socket).start();
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
