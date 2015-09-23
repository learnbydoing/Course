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
 * An instance of Worker for the server.
 * The Worker's job is to accept requests for the client and send results back.
 */
class Worker extends Thread {
	Socket socket;
	
	/**
     * Construct
     * @param s, the socket which is to be monitored
     */
	public Worker (Socket s)
	{
		socket = s;
	}
	
	/**
     * Start to work, after being assigned tasks by the server
     */
	public void run(){
		PrintStream printer = null; // Output stream to the client
		BufferedReader reader = null; // Local reader from the client
		
		try{
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			printer = new PrintStream(socket.getOutputStream());
			try{
				String hostName = "";
				hostName = reader.readLine(); // Accept the command(hostname) from the client
				System.out.println("Looking up " + hostName);
				printRemoteAddress(hostName, printer); // Print the hostname and ip address to the client
				System.out.println("I'm waiting for new request...");
			}
			catch(IOException ex){
				System.out.println("Server read error"); // Handle the exception
				ex.printStackTrace();
			}
			socket.close();
		}
		catch(IOException ex){
			System.out.println(ex); // Handle the exception
		}
	}
	
	/**
     * Print the result by the givin hostname
     * @param hostname, the host name to be looked up
	 * @param printer, the output stream object of the client
     */
	private void printRemoteAddress(String hostname, PrintStream printer){
		try{
			printer.println("Looking up " + hostname + " ...");
			InetAddress machine = InetAddress.getByName(hostname);
			printer.println("Host name: " + machine.getHostName());
			printer.println("Host IP: " + toText(machine.getAddress()));
		}
		catch(UnknownHostException ex) {
			printer.println("Failed in attemp to look up " + hostname); //Handle the exception
		}
	}
	
	/**
     * Convert the ip address from byte array to human readable text
     * @param ip[], byte array represents the ip address
	 * @return human readable text of the ip address
     */
	private String toText(byte ip[]){
		StringBuffer sb = new StringBuffer();
		for(int ix = 0; ix < ip.length; ix++){
			sb.append(0xff & ip[ix]);
			if (ix != ip.length - 1)
				sb.append(".");
		}
		return sb.toString();
	}
}

/**
 * A server can create multiple Workers to handle the requests from the client.
 * The server does nothing but dispatch tasks to workers.
 */
public class InetServer {
	/**
     * Start a Server Socket and prepare the handle the requests from the client
     */
	public static void main(String args[]){
		int queue_len = 6; // The maximum queue length for incoming connection
		int port = 4653; // Port number
		Socket socket; // A reference of the client socket
		
		try{
			ServerSocket servsocket = new ServerSocket(port, queue_len); // Setup the server socket
			System.out.println("Rong Zhuang's Inet Server is starting up, listening at port " + port + ".");
			System.out.println("I'm waiting for new request...");
			while(true){ // Runs forever, if no exception occurs				
				socket = servsocket.accept(); // Make the server socket wait for the next client command
				new Worker(socket).start(); // Spawn worker to handle the request
			}
		}
		catch(IOException ex){
			System.out.println(ex); //Handle the exception
		}
	}
}