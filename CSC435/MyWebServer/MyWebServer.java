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

/**
 * An instance of HttpWorker for the Web Server.
 * The Worker handles the http requests for the server. Its job is to accept
 * requests for the client and send results back.
 */
class HttpWorker extends Thread {
	private static final String FILE_LOG = "MyWebServerLogs.txt";
	// Use an ArrayList to store the logs from server
	private static ArrayList<String> alLogs = new ArrayList<String>();
	Socket socket;
	String clientRequest;

	/**
	 * Construct
	 * @param s, the socket which is to be monitored
	 */
	public HttpWorker (String req, Socket s)
	{
		socket = s;
		clientRequest = req;
	}

	/**
	 * Start to work, after being assigned tasks by the server
	 */
	public void run(){
		try{
			// Clear list each time for handling new reqeust
			alLogs.clear();
			// Local reader from the client
			BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		  // Output stream to the client
			PrintStream printer = new PrintStream(socket.getOutputStream());

			if (!clientRequest.startsWith("GET") || clientRequest.length() < 14 ||
					!(clientRequest.endsWith("HTTP/1.0") || clientRequest.endsWith("HTTP/1.1"))) {
				// bad request
				writeLog("");
				writeLog("Rong Zhuang's Web Server is working...");
				writeLog("400(Bad Request): " + clientRequest);
				String errorPage = buildErrorPage("400", "Bad Request", "Your browser sent a request that this server could not understand.");
				printer.println(errorPage);
			}
			else {
				String req = clientRequest.substring(4, clientRequest.length()-9).trim();
				if (req.indexOf("..") > -1 || req.indexOf("/.ht") > -1 || req.endsWith("~")) {
					// hack attack
					writeLog("");
					writeLog("Rong Zhuang's Web Server is working...");
					writeLog("403(Forbidden): " + req);
					String errorPage = buildErrorPage("403", "Forbidden", "You don't have permission to access the requested URL.");
					printer.println(errorPage);
				}
				else {
					if (!req.startsWith("/images/") && !req.endsWith("favicon.ico")) {
						// Avoid printing messages/logs for icon requests
						writeLog("");
						writeLog("Rong Zhuang's Web Server is working...");
						// Accept the http get request from the client
						//String clientRequest = reader.readLine();
						writeLog("> New request received: " + clientRequest);
					}
					// Decode url, eg. New%20folder -> New folder
					req = URLDecoder.decode(req, "UTF-8");
					// Remove the last slash if exists
					if (req.endsWith("/")) {
						req = req.substring(0, req.length() - 1);
					}
					// Handle requests
					if (req.indexOf(".")>-1) { // Request for signle file
						if (req.indexOf(".fake-cgi")>-1) { // CGI request
							writeLog("> This is a [CGI] request..");
							handleCgiRequest(req, printer);
						}
						else { // Single file request
							if (!req.startsWith("/images/")&&!req.startsWith("/favicon.ico")) {
								writeLog("> This is a [SINGLE FILE] request..");
							}
							handleFileRequest(req, printer);
						}
					}
					else { // Request for directory
						writeLog("> This is a [DIRECTORY EXPLORE] request..");
						handleExploreRequest(req, printer);
					}
				}
			}
			// Save logs to file
			saveLogs(true);
			socket.close();
		}
		catch(IOException ex){
			// Handle the exception
			System.out.println(ex);
		}
	}

	/**
	 * Handle CGI(fake) request
	 * @param req, get request from client
 	 * @param printer, output printer
	 */
	private void handleCgiRequest(String req, PrintStream printer) throws UnsupportedEncodingException {
		// Parse the url to key-value pair
		Map<String, String> params = parseUrlParams(req);

		// Try to convert num1 and num2 to integer
		Integer number1 = tryParse(params.get("num1"));
		Integer number2 = tryParse(params.get("num2"));

		// Validate the input params
		if (number1 == null || number2 == null) {
			printer.println("Invalid parameter: " + params.get("num1") + " or " + params.get("num2") + ", both must be integer!");
			writeLog(">> Invalid parameter: " + params.get("num1") + " or " + params.get("num2") + ", both must be integer!");
		}
		else {
			writeLog(">> " + number1 + " + " + number2 + " = " + (number1+number2));
			StringBuilder sbContent = new StringBuilder();
			sbContent.append("Dear " + params.get("person") + ", the sum of ");
			sbContent.append(params.get("num1") + " and " + params.get("num2") + " is ");
			sbContent.append(number1+number2);
			sbContent.append(".");
			String htmlPage = buildHtmlPage(sbContent.toString(), "Fake-CGI: AddNumber");
			String htmlHeader = buildHttpHeader("aa.html", htmlPage.length());
			printer.println(htmlHeader);
			printer.println(htmlPage);
		}
	}

	/**
	 * Handle single file request
	 * @param req, get request from client
	 * @param printer, output printer
	 */
	private void handleFileRequest(String req, PrintStream printer) throws FileNotFoundException, IOException {
		// Get the root folder of the webserver
		String rootDir = getRootFolder();
		// Get the real file path
		String path = Paths.get(rootDir, req).toString();
		// Try to open the file
		File f = new File(path);
		if (!f.exists() || !f.isFile()) { // If not exists or not a file
			printer.println("No such resource:" + req);
			writeLog(">> No such resource:" + req);
		}
		else { // It's a file
			if (!req.startsWith("/images/")&&!req.startsWith("/favicon.ico")) {
				writeLog(">> Seek the content of file: " + f.getName());
			}
			// Print header
			String htmlHeader = buildHttpHeader(path, f.length());
			printer.println(htmlHeader);

			// Open file to input stream
			InputStream fs = new FileInputStream(f);
			byte[] buffer = new byte[1000];
			while (fs.available()>0) {
				printer.write(buffer, 0, fs.read(buffer));
			}
		}
	}

	/**
	 * Handle file and directory explore request
	 * @param req, get request from client
	 * @param printer, output printer
	 */
	private void handleExploreRequest(String req, PrintStream printer)  {
		// Get the root folder of the webserver
		String rootDir = getRootFolder();
		// Get the real file path
		String path = Paths.get(rootDir, req).toString();
		// Try to open the diretcory
		File f = new File (path) ;
		if (!f.exists()) { // If the directory does not exist
			printer.println("No such resource:" + req);
			writeLog(">> No such resource:" + req);
		}
		else { // If exists
			writeLog(">> Explore the content under folder: " + f.getName());
			// Get all the files and directory under current diretcory
			File[] strFilesDirs = f.listFiles();
			// Build file/diretcory structure in html format
			StringBuilder sbDirHtml = new StringBuilder();
			// Title line
			sbDirHtml.append("<table>");
			sbDirHtml.append("<tr>");
			sbDirHtml.append("  <th>Name</th>");
			sbDirHtml.append("  <th>Last Modified</th>");
			sbDirHtml.append("  <th>Size(Bytes)</th>");
			sbDirHtml.append("</tr>");

			// Parent folder, show it if current diretcory is not root
			if (!path.equals(rootDir)) {
				String parent = path.substring(0, path.lastIndexOf("\\"));
				if (parent.equals(rootDir)) { // The first level
					parent = "../";
				}
				else { // The second or deeper levels
					parent = parent.replace(rootDir, "");
				}
				// Replace backslash to slash
				parent = parent.replace("\\", "/");
				// Parent line
				sbDirHtml.append("<tr>");
				sbDirHtml.append("  <td><img src=\""+buildImageLink(req,"images/folder.png")+"\"></img><a href=\"" + parent +"\">../</a></td>");
				sbDirHtml.append("  <td></td>");
				sbDirHtml.append("  <td></td>");
				sbDirHtml.append("</tr>");
			}

			// Build lines for directories
			ArrayList<File> folders = getFileByType(strFilesDirs, true);
			for (File folder: folders) {
				writeLog(">>> Directory: " + folder.getName());
				sbDirHtml.append("<tr>");
				sbDirHtml.append("  <td><img src=\""+buildImageLink(req,"images/folder.png")+"\"></img><a href=\""+buildRelativeLink(req, folder.getName())+"\">"+folder.getName()+"</a></td>");
				sbDirHtml.append("  <td>" + getFormattedDate(folder.lastModified()) + "</td>");
				sbDirHtml.append("  <td></td>");
				sbDirHtml.append("</tr>");
			}
			// Build lines for files
			ArrayList<File> files = getFileByType(strFilesDirs, false);
			for (File file: files) {
				writeLog(">>> File: " + file.getName());
				sbDirHtml.append("<tr>");
				sbDirHtml.append("  <td><img src=\""+buildImageLink(req, getFileImage(file.getName()))+"\" width=\"16\"></img><a href=\""+buildRelativeLink(req, file.getName())+"\">"+file.getName()+"</a></td>");
				sbDirHtml.append("  <td>" + getFormattedDate(file.lastModified()) + "</td>");
				sbDirHtml.append("  <td>" + file.length() + "</td>");
				sbDirHtml.append("</tr>");
			}

			sbDirHtml.append("</table>");
			String htmlPage = buildHtmlPage(sbDirHtml.toString(), "");
			String htmlHeader = buildHttpHeader(path, htmlPage.length());
			printer.println(htmlHeader);
			printer.println(htmlPage);
		}
	}

	/**
	 * Build http header
	 * @param path, path of the request
	 * @param length, length of the content
	 * @return, header text
	 */
	private String buildHttpHeader(String path, long length) {
		StringBuilder sbHtml = new StringBuilder();
		sbHtml.append("HTTP/1.1 200 OK");
		sbHtml.append("\r\n");
		sbHtml.append("Content-Length: " + length);
		sbHtml.append("\r\n");
		sbHtml.append("Content-Type: "+ getContentType(path));
		sbHtml.append("\r\n");
		return sbHtml.toString();
	}

	/**
	 * Build http page
	 * @param content, content of the page
	 * @param header1, h1 content
	 * @return, page text
	 */
	private String buildHtmlPage(String content, String header1) {
		StringBuilder sbHtml = new StringBuilder();
		sbHtml.append("<!DOCTYPE html>");
		sbHtml.append("<html>");
		sbHtml.append("<head>");
		sbHtml.append("<style>");
		sbHtml.append("	table { width:50%; } ");
		sbHtml.append("	th, td { padding: 3px; text-align: left; }");
		sbHtml.append("</style>");
		sbHtml.append("<title>My Web Server</title>");
		sbHtml.append("</head>");
		sbHtml.append("<body>");
		if (header1 != null && !header1.isEmpty()) {
			sbHtml.append("<h1>" + header1 + "</h1>");
		}
		else {
			sbHtml.append("<h1>File Explorer in Rong's Zhuang Web Server </h1>");
		}
		sbHtml.append(content);
		sbHtml.append("<hr>");
		sbHtml.append("<p>*This page is returned by Rong Zhuang's Web Server.</p>");
		sbHtml.append("</body>");
		sbHtml.append("</html>");
		return sbHtml.toString();
	}

	/**
	 * Build error page for bad request
	 * @param code, http cde: 400, 301, 200
	 * @param title, page title
	 * @param msg, error message
	 * @return, page text
	 */
	private String buildErrorPage(String code, String title, String msg) {
		StringBuilder sbHtml = new StringBuilder();
		sbHtml.append("HTTP/1.1 " + code + " " + title + "\r\n\r\n");
		sbHtml.append("<!DOCTYPE html>");
		sbHtml.append("<html>");
		sbHtml.append("<head>");
		sbHtml.append("<title>" + code + " " + title + "</title>");
		sbHtml.append("</head>");
		sbHtml.append("<body>");
		sbHtml.append("<h1>" + code + " " + title + "</h1>");
		sbHtml.append("<p>" + msg + "</p>");
		sbHtml.append("<hr>");
		sbHtml.append("<p>*This page is returned by Rong Zhuang's Web Server.</p>");
		sbHtml.append("</body>");
		sbHtml.append("</html>");
		return sbHtml.toString();
	}

	/**
	 * Get file or directory list
	 * @param filelist, original file/directory list
	 * @param isfolder, flag indicates looking for file or directory list
	 * @return, file/directory list
	 */
	private ArrayList<File> getFileByType(File[] filelist, boolean isfolder) {
		ArrayList<File> files = new ArrayList<File>();
		if (filelist == null || filelist.length == 0) {
			return files;
		}

		for(int ix = 0; ix < filelist.length; ix++) {
			if (filelist[ix].isDirectory() && isfolder) {
				files.add(filelist[ix]);
			}
			else if (filelist[ix].isFile() && !isfolder) {
				files.add(filelist[ix]);
			}
		}
		return files;
	}

	/**
	 * Parse parameter from url to key value pair
	 * @param url, url from client
	 * @return, pair list
	 */
	private Map<String, String> parseUrlParams(String url) throws UnsupportedEncodingException {
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

	/**
	 * Get root path
	 * @return, path of the current location
	 */
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

	/**
	 * Convert date to specified format
	 * @param lastmodified, long value represents date
	 * @return, formatted date in string
	 */
	private String getFormattedDate(long lastmodified) {
		if (lastmodified < 0) {
			return "";
		}

		Date lm = new Date(lastmodified);
		String lasmod = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(lm);
		return lasmod;
	}

	/**
	 * Build relative link
	 * @param current, current request
	* @param filename, file name
	 * @return, formatted file name
	 */
	private String buildRelativeLink(String current, String filename) {
		if (current == null || current.equals("") || current.equals("/")) {
			return filename;
		}
		else {
			return current + "/" +filename;
		}
	}

	/**
	 * Build image link for icons
	 * @param current, current request
	 * @param filename, file name
	 * @return, formatted file name
	 */
	private String buildImageLink(String current, String filename) {
		if (current == null || current.equals("") || current.equals("/")) {
			return filename;
		}
		else {
			String imageLink = filename;
			for(int ix = 0; ix < current.length(); ix++) {
				if (current.charAt(ix) == '/') {
					// For each downstairs level, need a upstairs level path
					imageLink = "../" + imageLink;
				}
			}
			return imageLink;
		}
	}

	/**
	 * Get file icon according to its extension
	 * @param path, file path
	 * @return, icon path
	 */
	private static String getFileImage(String path)	{
		if (path == null || path.equals("") || path.lastIndexOf(".") < 0) {
			return "images/file.png";
		}

		String extension = path.substring(path.lastIndexOf("."));
		switch(extension) {
			case ".class":
				return "images/class.png";
			case ".html":
				return "images/html.png";
			case ".java":
				return "images/java.png";
			case ".txt":
				return "images/text.png";
			case ".xml":
				return "images/xml.png";
			default:
				return "images/file.png";
		}
	}

	/**
	 * Get MIME type according to file extension
	 * @param path, file path
	 * @return, MIME type
	 */
	private static String getContentType(String path)	{
		if (path == null || path.equals("") || path.lastIndexOf(".") < 0) {
	 		return "text/html";
		}

		String extension = path.substring(path.lastIndexOf("."));
		switch(extension) {
			case ".html":
			case ".htm":
				return "text/html";
			case ".txt":
				return "text/plain";
			case ".ico":
				return "image/x-icon .ico";
			case ".wml":
				return "text/html"; //text/vnd.wap.wml
			default:
				return "text/plain";
		}
	}

	/**
	 * Parse string to integer, return null if unable to convert
	 * @param text, string value
	 * @return, integer value
	 */
	private Integer tryParse(String text) {
		try {
			return Integer.parseInt(text);
		} catch (NumberFormatException e) {
			return null;
		}
	}

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
 * This web server can create HttpWorker to handle the http requests from the
 * client(Web browser). The server does nothing but dispatches requests to
 * workers. Each worker starts a new thread to handle the request. So the web
 * server can handle multiple requests simultaneously. The main functions
 * provided by this web server include: explore files/directories in the root
 * folder of the webserver; display content of a single file from server;
 * handle fake-cgi request, eg. add number.
 */
public class MyWebServer {
	/**
	 * Start a Server Socket to monitor client requests and dispatches the http
	 * request to HttpWorkers.
	 */
	public static void main(String args[]){
		// The maximum queue length for incoming connection
		int queue_len = 6;
		// Port number for http request
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
				// Local reader from the client
				BufferedReader reader =new BufferedReader(new InputStreamReader(socket.getInputStream()));

				// Assign http requests to HttpWorker
				String req = "";
				String clientRequest = "";
				while ((clientRequest = reader.readLine()) != null) {
					if (req.equals("")) {
						req  = clientRequest;
					}
					if (clientRequest.equals("")) { // If the end of the http request, stop
						break;
					}
				}

				if (req != null && !req.equals("")) {
					new HttpWorker(req, socket).start();
				}
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
