/*--------------------------------------------------------
1. Name / Date:
Rong Zhuang
11 Sep, 2015

2. Java version used: 
build 1.8.0_60

3. Precise command-line compilation examples / instructions:
> javac InetServer.java
> javac InetClient.java

4. Precise examples / instructions to run this program:
In separate shell windows:
> java InetServer
> java InetClient

All acceptable commands are displayed on the various consoles.

If the server is running on the different machine with the client, you 
need to pass the IP address of the server to the clients. For exmaple, 
if the server is running at 140.192.34.32 then you would type:

> java InetClient 140.192.34.32

5. List of files needed for running the program.
 a. Worker.class
 b. InetServer.class
 c. InetClient.class

6. Notes:
I hard-code the port number to 4653 both in InetClient and InetServer.
If any exception occurs and the server hangs, kill server and restart it.
The clients are not necessarily to restart, they will find the server 
again when a request is made.

----------------------------------------------------------*/
import java.io.*; // Import the io libraries
import java.net.*; // Import the networking libraries

/**
 * A client can be setup in the same machine or different machine with the server.
 * The client sends the request to the server and get the result from it.
 */
public class InetClient{
	private static final int PORT_NUMBER = 4653;
	/**
     * The client will start a Socket, wait for user's inputs,
	 * then send them to the server, and print out the results. Type 'quit' to
	 * stop the client, otherwise, it will always be waiting for the new request.
     */
	public static void main(String args[]) {
		String serverName;
		if (args.length < 1) { // If no server name is specified, means client and server are setup in the same machine
			serverName = "localhost"; // The local machine(127.0.0.1) will be used as server.
		}
		else {
			serverName = args[0];
		}

		System.out.println("Rong Zhuang's Inet Client.");
		System.out.println("Using server: " + serverName + ", Port: " + PORT_NUMBER); // Same port number with the server
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try{
			String hostName; // The target host to be searched
			do {
				System.out.print("Enter a hostname or an IP address, (quit) to end: ");
				System.out.flush();
				hostName = reader.readLine(); // Receive the request from end user's input
				if (!hostName.equals("quit")){ // Not the quit command
					getRemoteAddress(hostName, serverName); // Connect to server and work on the request
				}
			} while (!hostName.equals("quit")); // End the client if 'quit' is an input
			
			System.out.println ("Client has been shut down!");
			System.out.println ("--Input 'java InetClient' to start a new one.--");
		} 
		catch(IOException ex) 
		{
			ex.printStackTrace(); //Handle the exception
		}
	}	

	/**
     * Connect to the server, send requests and get requests
     * @param hostname, the host name to be looked up
	 * @param servername, the server name to be connected to
     */
	private static void getRemoteAddress(String hostname, String servername){
		Socket socket;
		BufferedReader fromServer;
		PrintStream toServer;
		String textFromServer;
		try{
			// Open a new socket connection to the server with the specified port number
			socket = new Socket(servername, PORT_NUMBER);
			fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			toServer = new PrintStream(socket.getOutputStream());
			// Send machine name or IP address to server
			toServer.println(hostname); 
			toServer.flush();
			// Read two or three lines of response from the server,
			// and block while synchronously waiting:
			for (int i = 1; i <=3; i++){ // Three lines for "Look up...", "Host name" and "Host IP"
				textFromServer = fromServer.readLine();
				if (textFromServer != null){
					System.out.println(textFromServer);
				} 
			}
			socket.close();
		} 
		catch(IOException ex) {
			System.out.println ("Socket error."); //Handle the exception
			ex.printStackTrace ();
		}
	}
}