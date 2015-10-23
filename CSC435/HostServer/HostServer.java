/* 2012-05-20 Version 2.0

Thanks John Reagan for this well-running code which repairs the original
obsolete code for Elliott's HostServer program. I've made a few additional
changes to John's code, so blame Elliott if something is not running.

-----------------------------------------------------------------------

Play with this code. Add your own comments to it before you turn it in.

-----------------------------------------------------------------------
NOTE: This is NOT a suggested implementation for your agent platform,
but rather a running example of something that might serve some of
your needs, or provide a way to start thinking about what YOU would like to do.
You may freely use this code as long as you improve it and write your own comments.

-----------------------------------------------------------------------

TO EXECUTE:

1. Start the HostServer in some shell. >> java HostServer

1. start a web browser and point it to http://localhost:1565. Enter some text and press
the submit button to simulate a state-maintained conversation.

2. start a second web browser, also pointed to http://localhost:1565 and do the same. Note
that the two agents do not interfere with one another.

3. To suggest to an agent that it migrate, enter the string "migrate"
in the text box and submit. The agent will migrate to a new port, but keep its old state.

During migration, stop at each step and view the source of the web page to see how the
server informs the client where it will be going in this stateless environment.

-----------------------------------------------------------------------------------

COMMENTS:

This is a simple framework for hosting agents that can migrate from
one server and port, to another server and port. For the example, the
server is always localhost, but the code would work the same on
different, and multiple, hosts.

State is implemented simply as an integer that is incremented. This represents the state
of some arbitrary conversation.

The example uses a standard, default, HostListener port of 1565.

-----------------------------------------------------------------------------------

DESIGN OVERVIEW

Here is the high-level design, more or less:

HOST SERVER
  Runs on some machine
  Port counter is just a global integer incrememented after each assignment
  Loop:
    Accept connection with a request for hosting
    Spawn an Agent Looper/Listener with the new, unique, port

AGENT LOOPER/LISTENER
  Make an initial state, or accept an existing state if this is a migration
  Get an available port from this host server
  Set the port number back to the client which now knows IP address and port of its
         new home.
  Loop:
    Accept connections from web client(s)
    Spawn an agent worker, and pass it the state and the parent socket blocked in this loop

AGENT WORKER
  If normal interaction, just update the state, and pretend to play the animal game
  (Migration should be decided autonomously by the agent, but we instigate it here with client)
  If Migration:
    Select a new host
    Send server a request for hosting, along with its state
    Get back a new port where it is now already living in its next incarnation
    Send HTML FORM to web client pointing to the new host/port.
    Wake up and kill the Parent AgentLooper/Listener by closing the socket
    Die

WEB CLIENT
  Just a standard web browser pointing to http://localhost:1565 to start.

  -------------------------------------------------------------------------------*/


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
/**
 * HostServer Notes: This went pretty smoothly for me, although I did have to edit the HTML functions
 * to get an accurate content length so things would be compatible with browsers other than IE. I also modified
 * things to eliminate inaccurate state numbers based on fav.ico requests. If the string person wasnt found,
 * the requests was ignored
 */

/**
 * AgentWorker
 *
 * AgentWorker objects are created by AgentListeners and process requests made at the various
 * active ports occupied by agentlistener objects. They take a request and look for the string
 * migrate in that request(supplied from a get parameter via an html form). If migrate is found,
 * the worker finds the next availabel port and switches teh client to it.
 *
 * I made a small modification because my browser kept requesting fav.ico. So I verified that we receive
 * a person attribute before processing the request as valid(and incrementing agent state)
 *
 */
class AgentWorker extends Thread {
	//server is hardcoded in, only acceptable for this basic implementation
	String LocalHost = "localhost";
	//port the main worker will run on
	int HostMainPort = 1565;
	Socket sock; //connection to client
	AgentHolder parentAgentHolder; //maintains agentstate holding socket and state counter
	int localPort; //port being used by this request

	// Constructor
	AgentWorker (Socket s, int port, AgentHolder ah) {
		sock = s;
		localPort = port;
		parentAgentHolder = ah;
	}

	public void run() {
		Socket clientSock;
		BufferedReader fromHostServer;
		PrintStream toHostServer;

		try {
			PrintStream out = new PrintStream(sock.getOutputStream());
			BufferedReader in = new BufferedReader(new InputStreamReader(sock.getInputStream()));

			//read a line from the client
			String inLine = in.readLine();
			//to allow for usage on non-ie browsers, I had to accurately determine the content
			//length and as a result need to build the html response so i can determine its length.
			//log a request
			System.out.println();
			System.out.println("Request line: " + inLine);

			if(inLine.toLowerCase().indexOf("migrate") > -1) {
				//the supplied request contains migrate, switch the user to a new port

				//create a new socket with the main server waiting on 1565
				clientSock = new Socket(LocalHost, HostMainPort);
				fromHostServer = new BufferedReader(new InputStreamReader(clientSock.getInputStream()));
				//send a request to port 1565 to receive the next open port
				toHostServer = new PrintStream(clientSock.getOutputStream());
				toHostServer.println("Please host me. Send my port! [State=" + parentAgentHolder.agentState + "]");
				toHostServer.flush();

				//wait for the response and read a response until we find what should be a port
				String buf = "";
				for(;;) {
					//read the line and check it for what looks to be a valid port
					buf = fromHostServer.readLine();
					if(buf.indexOf("[Port=") > -1) {
						break;
					}
				}

				//extract the port by leveraging the format of the port response
				String tempbuf = buf.substring( buf.indexOf("[Port=")+6, buf.indexOf("]", buf.indexOf("[Port=")) );
				//parse the response for the integer containing the new port
				int newPort= Integer.parseInt(tempbuf);
				//log it to the server console
				System.out.println("newPort is: " + newPort);

				String content = "<h3>We are migrating to host " + newPort + "</h3> \n";
				content = content + "<h3>View the source of this page to see how the client is informed of the new location.</h3> \n";
				AgentListener.sendResponseToClient(LocalHost, newPort, inLine, content, out);

				//StringBuilder htmlString = new StringBuilder();
				//prepare the html response to send the user
				//htmlString.append(AgentListener.sendHTMLheader(newPort, NewHost, inLine));
				//inform the user that the migration request was received
				//htmlString.append("<h3>We are migrating to host " + newPort + "</h3> \n");
				//htmlString.append("<h3>View the source of this page to see how the client is informed of the new location.</h3> \n");
				//finish html
				//htmlString.append(AgentListener.sendHTMLsubmit());

				//log that we are killing the waiting server at the port
				System.out.println("Killing parent listening loop.");
				//grab the socket at the old port(stored in the parentAgentHolder)
				ServerSocket ss = parentAgentHolder.sock;
				//close the port
				ss.close();


			} else if(inLine.toLowerCase().indexOf("refresh") > -1) {
				//increment the state int to reflect an event occuring in the 'game'
				parentAgentHolder.agentState++;
				String content = "<h3>We are having a conversation with state   " + parentAgentHolder.agentState + "</h3>\n";
				AgentListener.sendResponseToClient(LocalHost, localPort, inLine, content, out);
				//send the html back to the user displaying the agent state and form
				//htmlString.append(AgentListener.sendHTMLheader(localPort, NewHost, inLine));
				//htmlString.append("<h3>We are having a conversation with state   " + parentAgentHolder.agentState + "</h3>\n");
				//htmlString.append(AgentListener.sendHTMLsubmit());

			} else {
				//System.out.println("Unknow request:" + inLine);
				//we couldnt find a person variable, so we probably are looking at a fav.ico request
				//tell the user it was invalid
				String content = "You have not entered a valid request!\n";
				AgentListener.sendResponseToClient(LocalHost, localPort, inLine, content, out);
				//htmlString.append(AgentListener.sendHTMLheader(localPort, NewHost, inLine));
				//htmlString.append("You have not entered a valid request!\n");
				//htmlString.append(AgentListener.sendHTMLsubmit());


			}
			//output the html
			//AgentListener.sendHTMLtoStream(htmlString.toString(), out);

			//close the socket
			sock.close();


		} catch (IOException ioe) {
			System.out.println(ioe);
		}
	}

}
/**
 * Basic agent holder object. Holds state info/resources
 * so we can track the agentState and pass it between ports
 */
class AgentHolder {
	//active serversocket object
	ServerSocket sock;
	//basic agentState var
	int agentState;

	//basic constructor
	AgentHolder(ServerSocket s) { sock = s;}
}

/**
 * AgentListener objects monitor individual ports and respond to requests
 * made upon them(in this scenario from a standard web browser); They are created
 * by the hostserver when a new request is made to port 1565.
 */
class AgentListener extends Thread {
	Socket sock;
	int localPort;

	// Constructor
	AgentListener(Socket s, int port) {
		sock = s;
		localPort = port;
	}
	//set a default agent state of 0
	int agentState = 0;
	String NewHost = "localhost";

	public void run() {

		System.out.println("> In AgentListener Thread");
		try {
			PrintStream out = new PrintStream(sock.getOutputStream());
			BufferedReader in =  new BufferedReader(new InputStreamReader(sock.getInputStream()));

			// Read a line, normally, it's a HTTP GET request
			String req = in.readLine();
			/*String clientRequest = "";
			while ((clientRequest = in.readLine()) != null) {
				if (req.equals("")) {
					req  = clientRequest;
				}
				if (clientRequest.equals("")) { // If the end of the http request, stop
					break;
				}
			}*/

			// Print out to console
			System.out.println(req);

			if (req == null || req.isEmpty() || req.indexOf("favicon.ico") > -1) {
				return;
			}

			//if we have a state, it comes from migration, parse the request and store it
			if(req != null && req.indexOf("[State=") > -1) {
				//extract the state from the read line
				String tempbuf = req.substring(req.indexOf("[State=")+7, req.indexOf("]", req.indexOf("[State=")));
				//parse it
				agentState = Integer.parseInt(tempbuf);
				//log to server console
				System.out.println("agentState is: " + agentState);
			}

			// Send Response page to client
			sendResponseToClient(NewHost, localPort, req, "", out);
			sock.close();

			//now open a connection at the port
			ServerSocket servsock = new ServerSocket(localPort, 2);
			//create a new agentholder and store the socket and agentState
			AgentHolder agenthold = new AgentHolder(servsock);
			agenthold.agentState = agentState;

			//wait for connections.

			while(true) {
				System.out.println("AgentListener is wating for request at port " + localPort);
				sock = servsock.accept();
				//log a received connection
				System.out.println("Got a connection to agent at port " + localPort);
				//connection received. create new agentworker object and start it up!
				new AgentWorker(sock, localPort, agenthold).start();
			}

		} catch(IOException ioe) {
			//this happens when an error occurs OR when we switch port
			System.out.println("Either connection failed, or just killed listener loop for agent at port " + localPort);
			System.out.println(ioe);
		}
	}

	static void sendResponseToClient(String host, int port, String request, String content, PrintStream printer) {
		String htmlPage = buildHtmlPage(host, port, request, content);
		String htmlHeader = buildHttpHeader(htmlPage.length());
		printer.println(htmlHeader);
		printer.println(htmlPage);
/*
		//string builder to hold the html response
		StringBuilder htmlResponse = new StringBuilder();
		//output first request html to user
		//show the port and display the form. we know agentstate is 0 since game hasnt been started
		htmlResponse.append(buildHtmlPage(NewHost, localPort, buf));
		//htmlResponse.append("Now in Agent Looper starting Agent Listening Loop\n<br />\n");
		//htmlResponse.append("[Port="+localPort+"]<br/>\n");
		htmlResponse.append(sendHTMLsubmit());
		//display it
		sendHTMLtoStream(htmlResponse.toString(), out);*/
	}
	//send the html header but NOT the response header
	//otherwise same as original implementation. Load html, load form,
	//add port to action attribute so the next request goes back to the port
	//or goes to the new one we are listening on
	static String buildHtmlPage(String host, int port, String request, String content) {

		StringBuilder sbHtml = new StringBuilder();
		sbHtml.append("<!DOCTYPE html>\n");
		sbHtml.append("<html>\n");
		sbHtml.append("  <head>\n");
		sbHtml.append("    <title>Host Server</title>\n");
		sbHtml.append("    <link rel=\"icon\" href=\"http://www.google.com/favicon.ico\" type=\"image/x-icon\" />");
		sbHtml.append("  </head>\n");
		sbHtml.append("  <body>\n");
		sbHtml.append("    <input type=\"hidden\" name=\"Port\" value=\"" + port + "\">");
		sbHtml.append("[Port="+port+"]<br/>\n");
		sbHtml.append("    <h1>This is for submission to PORT " + port + " on " + host + " </h1>\n");
		sbHtml.append("    <h3>You sent: " + request + " </h3>\n");
		sbHtml.append("    <p>Click 'Refresh' button to get the new state; Or, click the 'Migrate' button to migrate to another port.</p>\n");
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
		sbHtml.append("          <a href=\"http://" + host + ":1565\" target=\"_blank\">New Request</a>\n");
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

	//finish off the html started by sendHTMLheader
	static String sendHTMLsubmit() {
		return "<input type=\"submit\" value=\"Submit\"" + "</p>\n</form></body></html>\n";
	}
	//send the response headers and calculate the content length so we play nicer with all browsers,
	//and can actually work with non ie browser
	static void sendHTMLtoStream(String html, PrintStream out) {

		out.println("HTTP/1.1 200 OK");
		out.println("Content-Length: " + html.length());
		out.println("Content-Type: text/html");
		out.println("");
		out.println(html);
	}

}

/**
 * A HostServer listens on port 1565 for requests. When a new request comes, it
 * increase NextPort by one and assign this request to a new AgentListener. Then,
 * HostServer continues to monitor requests at port 1565. Each request will get
 * a independent port for continous access.
 */
public class HostServer {
	// Define a initial value for port number
	public static int NextPort = 3000;

	public static void main(String[] a) throws IOException {
		int q_len = 50;
		int port = 1565; // Port for first request
		Socket sock;

		ServerSocket servsock = new ServerSocket(port, q_len);
		System.out.println("Rong Zhuang's Host Server has started at port " + port + ".");
		System.out.println("Connect from 1 to 3 browsers using \"http:\\\\localhost:" + port + "\"\n");

		while(true) {
			// Increment nextport for each request
			NextPort = NextPort + 1;
			// Open socket for requests
			sock = servsock.accept();

			System.out.println("Starting AgentListener at port " + NextPort);
			//create new agent listener at this port to wait for requests
			new AgentListener(sock, NextPort).start();
		}
	}
}
