/*--------------------------------------------------------
1. Name / Date:
Rong Zhuang
09 Oct, 2015

2. Java version used:
build 1.8.0_60

3. Precise command-line compilation examples / instructions:
> javac MyTelnet.java

4. Precise examples / instructions to run this program:
In separate shell windows:
> java MyTelnet condor.depaul.edu

5. List of files needed for running the program.
 a. MyTelnet.class

6. Notes:
This tool is used to see what Http response is returned from server after sending
out an http Get request. Run it as follows:
> java MyTelnet condor.depaul.edu
> GET /elliott/cat.html HTTP/1.1
> GET /elliott/dog.txt HTTP/1.1
> GET /elliott/cat.html HTTP/1.0
> GET /elliott/dog.txt HTTP/1.0

Running logs are stored in http-streams.txt

----------------------------------------------------------*/
import java.io.*;
import java.net.*;

/**
 * Telnet client sends http request to server and receive response from it.
 * Launch it by providing the hostname or IP address. Port is fixed to 80.
 */
public class MyTelnet{
	private static final int PORT_NUMBER = 80;

	public static void main(String args[]) {
		String hostName;
		if (args.length < 1) {
			hostName = "localhost"; // The local machine(127.0.0.1) will be used as server.
		}
		else {
			hostName = args[0];
		}

		System.out.println("Rong Zhuang's Telnet Client is starting up.");
		System.out.println("Will connect host: " + hostName + " at port: " + PORT_NUMBER);

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try{
			String getRequest; // The request contain Get method
			do {
				System.out.print("Enter Http Get request, (quit) to end: ");
				System.out.flush();
				getRequest = reader.readLine(); // Receive the request from end user's input
				if (!getRequest.equals("quit")){ // Not the quit command
					sendHttpGetRequest(getRequest, hostName, PORT_NUMBER);
				}
			} while (!getRequest.equals("quit")); // End the client if 'quit' is an input

			System.out.println ("Telnet Client has been shut down!");
			System.out.println ("--Input 'java MyTelnet' to start a new one.--");
		}
		catch(IOException ex)
		{
			ex.printStackTrace(); //Handle the exception
		}
	}

	/**
	 * Send request to host, support both HTTP1.0 and HTTP1.1
	 * @param getrequest, the get request from user input
	 * @param hostname, the host name to be connected
	 * @param port, default port for http: 80
	 */
	private static void sendHttpGetRequest(String getrequest, String hostname, int port){
		Socket socket;
		BufferedReader fromServer;
		PrintStream toServer;
		String textFromServer;
		try{
			// Open a new socket connection to the host with 80 port number
			socket = new Socket(hostname, port);
			fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			toServer = new PrintStream(socket.getOutputStream());
			// Check the http standard 1.0 or 1.1
			if (getrequest.endsWith("HTTP/1.1")) { //HTTP1.1, need to add host
				System.out.println(getrequest + "\r\nHost:" + hostname + "\r\n");
				toServer.println(getrequest + "\r\nHost:" + hostname + "\r\n");
			}
			else { //HTTP1.0, not need to append host info
				System.out.println(getrequest + "\r\n");
				toServer.println(getrequest + "\r\n");
			}
			toServer.flush();

			while ((textFromServer = fromServer.readLine()) != null) {
				/*if (textFromServer.equals("")) {
					break;
				}*/
				System.out.println(textFromServer);
			}
			socket.close();
		}
		catch(IOException ex) {
			System.out.println ("Socket error."); //Handle the exception
			ex.printStackTrace ();
		}
	}
}
