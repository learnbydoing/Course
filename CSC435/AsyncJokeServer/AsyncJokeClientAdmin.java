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
 a. The admin client uses 8722 as the port number for admin service.
 b. Four modes are supported:
    J - for Joke Mode
    P - for Proverb Mode
    M - for Maintenance Mode
    SD - to shutdown the server
 c. Type 'quit' to stop the admin client.

----------------------------------------------------------*/
import java.io.*;
import java.net.*;

/**
 * A Admin Client can be setup in the same machine or different machine with the
 * server. This admin client sends command to the server and make the server
 * switch the mode between joke, proverb or maintenance. Besides, it can
 * shutdown the server by sending SD command.
 */
public class AsyncJokeClientAdmin{

	// Use a different port for admin service.
	private static final int PORT_NUMBER = 8722;
	/**
	 * The admin client will start a Socket, wait for user's commands. Only the
	 * pre-defined commands are acceptable. The server will response to these
	 * commands and switch the server mode accordingly. Type 'quit' to stop the
	 * admin client, otherwise, it will always be waiting for the new command.
	 */
	public static void main(String args[]) {
		String serverName;
		// If no server name is specified, means client and server are setup in the
		// same machine
		if (args.length < 1) {
			// The local machine(127.0.0.1) will be used as server.
			serverName = "localhost";
		}
		else {
			serverName = args[0];
		}

		System.out.println("Rong Zhuang's Async Joke Client Admin is starting...");
		System.out.println("Using server: " + serverName + ", Port: " + PORT_NUMBER);

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try{
			String command; // The command for maintenance mode
			do {
				System.out.println("Switch server mode: J for joke-mode, P for proverb-mode, M for maintenance-mode, SD to shutdown the server, or 'quit' to exit this admin client...");
				System.out.flush();
				// Receive the command from end user's input
				command = reader.readLine();
				if (!command.equals("quit")){
					// Connect to server and work on the command
					sendCommand(command, serverName, PORT_NUMBER);
				}
			} while (!command.equals("quit")); // End the client if 'quit' is an input

			System.out.println ("Async Joke Client Admin has been shut down!");
			System.out.println ("--Input 'java AsyncJokeClientAdmin' to start a new one.--");
		}
		catch(IOException ex)
		{
			//Handle the exception
			ex.printStackTrace();
		}
	}

	/**
	 * Connect to the server, send command and get the feedback
	 * @param command, the command for maintenance mode
	 * @param servername, the server to be connected
	 * @param port, the port for the socket
	 */
	private static void sendCommand(String command, String servername, int port){
		Socket socket;
		BufferedReader fromServer;
		PrintStream toServer;

		try{
			// Open a new socket connection to the server with the specified port number
			socket = new Socket(servername, port);
			fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			toServer = new PrintStream(socket.getOutputStream());
			// Send command to server
			toServer.println(command);
			toServer.flush();
			// Read multiple lines of response from the server, and block while synchronously waiting:
			// There is nothing special for the number(50) of iteration times, just to
			// make sure we don't miss any messages from server.
			String strOutput = "";
			for (int ix = 1; ix <= 50; ix++){
				strOutput = fromServer.readLine();
				if (strOutput == null){ // There is no more messages
					break;
				}
				else {
					System.out.println(strOutput);
				}
			}
			socket.close(); // Close the socket
		}
		catch(IOException ex) {
			System.out.println ("Exception occurs, see the below details:");
			ex.printStackTrace ();
		}
	}
}
