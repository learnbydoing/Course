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
import java.util.*;
//import java.util.HashMap;

/**
 * A client can be setup in the same machine or different machine with the server.
 * The client sends the request to the server and get the result from it.
 */
public class JokeClient{
	private static final int PORT_NUMBER = 4653;
	/**
     * The client will start a Socket, wait for user's inputs,
	 * then send them to the server, and print out the results. Type 'quit' to
	 * stop the client, otherwise, it will always be waiting for the new request.
     */
	public static void main(String args[]) {
		String serverName;
		HashMap<String, String> users = new HashMap<String, String>();
		
		if (args.length < 1) { // If no server name is specified, means client and server are setup in the same machine
			serverName = "localhost"; // The local machine(127.0.0.1) will be used as server.
		}
		else {
			serverName = args[0];
		}

		System.out.println("Rong Zhuang's Joke Client.");
		System.out.println("Using server: " + serverName + ", Port: " + PORT_NUMBER); // Same port number with the server
		
		users = getUsers("users.txt"); // Read user list from file
		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try{
			String currentUserName = ""; // Store the user name for next use
			String userName = ""; // To use the service, a user name must be provided first
			String userKey = ""; // A unique key for a specific user
			do {
				System.out.print("Enter your name to get a joke/proverb, (quit) to end: ");
				System.out.flush();
				userName = reader.readLine(); // Receive the request from end user's input
				if(userName == null || userName.isEmpty()){ // If no use name is provided					
					if (userKey == null || userKey.isEmpty()){ // And no valid key exists
						// Warn the end user to provide the user name to continue
						System.out.println ("Please input your name first!");						
					}
					else {
						// The user key already exists, just get the joke or proverb
						getJokeOrProverb(currentUserName, userKey, serverName); // Connect to server and work on the request
					}
				}
				else {
					if (userName.equals("quit")) // Shutdown the client
						break;
					userKey = users.get(userName); // Try to find the user in the current user list
					//System.out.print("userKey: " +  userKey);
					if(userKey == null || userKey.isEmpty()){ // If no key found, means this is a new user
						UUID uuid = UUID.randomUUID(); // Generate a random UUID
						userKey = uuid.toString(); // Convert the UUID to string and use it as user key
						users.put(userName, userKey); // Store the pair to the list
						addUser("users.txt", userName, userKey); // Meanwhile, save the new user to file
					}
					currentUserName = userName;
					getJokeOrProverb(currentUserName, userKey, serverName); // Connect to server and work on the request
				}
			} while (!userName.equals("quit")); // End the client if 'quit' is an input
			
			System.out.println ("Client has been shut down!");
			System.out.println ("--Input 'java JokeClient' to start a new one.--");
		} 
		catch(IOException ex) 
		{
			ex.printStackTrace(); //Handle the exception
		}
	}	

	/**
     * Connect to the server, send requests and get the result
     * @param username, the user name who asks for a joke or proverb
	 * @param userkey, the user key which is used to identify the unique user
	 * @param servername, the server name to be connected to
     */
	private static void getJokeOrProverb(String username, String userkey, String servername){
		Socket socket;
		BufferedReader fromServer;
		PrintStream toServer;
		String textFromServer;		
		
		try{
			// Open a new socket connection to the server with the specified port number
			socket = new Socket(servername, PORT_NUMBER);
			fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			toServer = new PrintStream(socket.getOutputStream());
			// Send user name and user key to server			
			toServer.println(username + " " + userkey); 
			toServer.flush();
			// Read multiple lines of response from the server, and block while synchronously waiting:
			ArrayList<String> alOutput = new ArrayList<String>();
			for (int i = 1; i <= 50; i++){ // There is nothing special for the number(50) of iteration times
				textFromServer = fromServer.readLine();
				if (textFromServer == null){ // If no more outputs from the server, then stop the loop
					break;
				}
				else {
					alOutput.add(textFromServer);
					System.out.println(textFromServer);
				} 
			}
			socket.close(); // Close the socket
			String[] arrLogs = new String[alOutput.size()];
			arrLogs = alOutput.toArray(arrLogs);
			writeLogs("output.txt", arrLogs, true); // Write log to file
		} 
		catch(IOException ex) {
			System.out.println ("Socket error."); //Handle the exception
			ex.printStackTrace ();
		}
	}
	
	/**
     * Get the user list from the specified file
     * @param filename, the name of the file
	 * @return, user list
     */
	private static HashMap<String, String> getUsers(String filename) {
		// Use hashmap to store the user list, the first is user name and the second is UUID key
		HashMap<String, String> mapUsers = new HashMap<String, String>();
		// This will reference one line at a time
        String line = null;

        try {
            // FileReader reads text files in the default encoding.
            FileReader fileReader = new FileReader(filename);

            // Always wrap FileReader in BufferedReader.
            BufferedReader bufferedReader = new BufferedReader(fileReader);
		
			// Read the file in lines one by one
            while((line = bufferedReader.readLine()) != null) {
				String[] namekey = line.split(" "); // Split the name and key
				if (namekey.length != 2) { // Validate the data
					System.out.println("Invalid line: '" + line + "' in '" + filename + "'");      
					return mapUsers;
				}
				// Name + Key(UUID)
                mapUsers.put(namekey[0], namekey[1]);
            }  

            // Always close files.
            bufferedReader.close();         
        }
        catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + filename + "'");                
        }
        catch(IOException ex) {
            ex.printStackTrace();
        }
		finally {
			return mapUsers;
		}		
	}
	
	/**
     * Store new user to the specified file
     * @param filename, the name of the file
	 * @param username, the name of the user
	 * @param userkey, the key(UUID)
     */
	private static void addUser(String filename, String username, String userkey) {
        try {
            // Assume default encoding.
            FileWriter fileWriter = new FileWriter(filename, true);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

  			bufferedWriter.write(username + " " + userkey); // Concatenate user name and key with a blank space
			bufferedWriter.newLine(); // One user one line

            // Always close files.
            bufferedWriter.close();
        }
		catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + filename + "'");                
        }
        catch(IOException ex) {            
            ex.printStackTrace();
        }
	}
	
	/**
     * Save logs to the specified file
     * @param logfile, the file name
     * @param content, the log content in array format, one line one log
     * @param append, ture is append, false is override
     */
	private static void writeLogs(String logfile, String[] content, boolean append) {
        try {
            // Assume default encoding.
            FileWriter fileWriter = new FileWriter(logfile, append);

            // Always wrap FileWriter in BufferedWriter.
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

			// Go through all the content and write them to the file
			for(int ix=0; ix < content.length; ix++) {
				bufferedWriter.write(content[ix]);
				bufferedWriter.newLine();
			}

            // Always close files.
            bufferedWriter.close();
        }
		catch(FileNotFoundException ex) {
            System.out.println("Unable to open file '" + logfile + "'");                
        }
        catch(IOException ex) {            
            ex.printStackTrace();
        }
	}
}