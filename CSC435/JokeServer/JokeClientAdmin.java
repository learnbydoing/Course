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
I hard-code the port number to 4653 both in JokeServer and JokeClient.
If any exception occurs and the server hangs, kill server and restart it.
The clients are not necessarily to restart, they will find the server 
again when a request is made.

----------------------------------------------------------*/
import java.io.*; // Import the io libraries
import java.net.*; // Import the networking libraries

/**
 * A Admin Client can be setup in the same machine or different machine with the server.
 * The client sends the admin command to the server and make the server switch the mode
 * between joke, proverb or maintenance.
 */
public class JokeClientAdmin{
	private static final int PORT_NUMBER = 8722; // Use a different port with the general joke/proverb service.
	/**
     * The client will start a Socket, wait for user's commands,
	 * then send them to the server, and switch the server mode. Type 'quit' to
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

		System.out.println("Rong Zhuang's Joke Client Admin.");
		System.out.println("Using server: " + serverName + ", Port: " + PORT_NUMBER); 
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try{
			String command; // The target host to be searched
			do {
				System.out.print("Enter command to switch the server mode, (quit) to end: ");
				System.out.flush();
				command = reader.readLine(); // Receive the command from end user's input
				if (!command.equals("quit")){ // Not the quit command
					sendCommand(command, serverName); // Connect to server and work on the command
				}
			} while (!command.equals("quit")); // End the client if 'quit' is an input
			
			System.out.println ("Client Admin has been shut down!");
			System.out.println ("--Input 'java JokeClientAdmin' to start a new one.--");
		} 
		catch(IOException ex) 
		{
			ex.printStackTrace(); //Handle the exception
		}
	}	

	/**
     * Connect to the server, send command and switch the server mode accordingly
     * @param command, the command for switching mode
	 * @param servername, the server name to be connected to
     */
	private static void sendCommand(String command, String servername){
		Socket socket;
		BufferedReader fromServer;
		PrintStream toServer;
		String textFromServer;
		try{
			// Open a new socket connection to the server with the specified port number
			socket = new Socket(servername, PORT_NUMBER);
			fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			toServer = new PrintStream(socket.getOutputStream());
			// Send command to server
			toServer.println(command); 
			toServer.flush();
			// Read multiple lines of response from the server, and block while synchronously waiting:
			for (int i = 1; i <=50; i++){ // There is nothing special for the number(50) of iteration times
				textFromServer = fromServer.readLine();
				if (textFromServer == null){
					break;
				}
				else {
					System.out.println(textFromServer);
				} 
			}
			socket.close(); // Close the socket
		} 
		catch(IOException ex) {
			System.out.println ("Socket error."); //Handle the exception
			ex.printStackTrace ();
		}
	}
}