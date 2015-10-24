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
 a. The joke client uses 4653 as the port number for general joke and proverb
    service.
 b. The first time you run this client, you need to provide your name. After
    that, you just press enter(no name required), a new joke/proverb will be
    returned.
 c. This client also supports multiple users. You can switch between users by
    provideing a different name when you trying to get a new joke/proverb. If
    you just click the enter key without any name provided, the latest user
    will be remembered and sent to server to get a new random joke/proverb.
 d. The users in this client will be persistent to disk, stored in file
    "ClientUsers.txt" which locates in the same folder fo JokeClient.class. When
    you stop the client and restart again, you need to provide a name first, and
    the server will response as the same person, just like you haven't shutdonw
    the client.
 e. Format of the user file. One user one line.
    Name, Blank Space, UUID
    Exmaple:
    johnny 9094926a-d32b-4b73-a32a-8a0ae6d14e1f
 f. The running logs are stored to file ClientLogs.txt
 g. Type 'quit' to stop the admin client.

----------------------------------------------------------*/
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * A client can be setup in the same machine or different machine with the
 * server. The joke client sends a name to the server and get the random
 * joke/proverb from it.
 */
public class AsyncJokeClient{
	private static final int PORT_NUMBER = 4653;
	private static final String FILE_USERS = "AsyncClientUsers.txt";
	private static final String FILE_LOG = "AsyncClientLogs.txt";
	// Use an ArrayList to store the logs from server
	private static ArrayList<String> alLogs = new ArrayList<String>();

	/**
	 * The joke client just needs user to provide the name once, and it will
	 * connect to the joke server to get a new joke or proverb (depends on the
	 * current server mode). Type 'quit' to stop the client, otherwise, it will
	 * always be waiting for the new request.
	 */
	public static void main(String args[]) {
		String serverName;
		// Users in the same client, which are persistent to disk
		HashMap<String, String> users = new HashMap<String, String>();

		// If no server name is specified, means client and server are setup in the
		// same machine
		if (args.length < 1) {
			// The local machine(127.0.0.1) will be used as server.
			serverName = "localhost";
		}
		else {
			// Use the specified server
			serverName = args[0];
		}

		writeLog("Rong Zhuang's Async Joke Client is starting...");
		writeLog("Using server: " + serverName + ", Port: " + PORT_NUMBER);
		saveLogs(false);

		// Read user list from file
		//System.out.println("Read file from disk to get user list...");
		users = getUsers(FILE_USERS);

		BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
		try{
			String currentUserName = ""; // Store the user name for next use
			String userName = ""; // To use the service, a user name must be provided first
			String userKey = ""; // A unique key for a specific user
			do {
				// Clear temp log list
				alLogs.clear();
				writeLog("Enter your name to get a new joke/proverb, or 'quit' to exit this client...");
				System.out.flush();

				// Receive the name from end user's input
				userName = reader.readLine();
				writeLog("> " + userName, false);

				if(userName == null || userName.isEmpty()){ // If no use name is provided
					if (userKey == null || userKey.isEmpty()){ // And no valid key exists
						// Warn the end user to provide a name to continue
						writeLog("Please input your name first!");
					}
					else {
						// The user key already exists, just get the joke or proverb
						getJokeOrProverb(currentUserName, userKey, serverName, PORT_NUMBER);
					}
				}
				else {
					if (userName.equals("quit")){
						// Shutdown the client
						break;
					}
					else {
						// Try to find the user in the current user list
						userKey = users.get(userName);
						//System.out.print("userKey: " +  userKey);
						// If no key found, means this is a new user
						if(userKey == null || userKey.isEmpty()){
							// Generate a random UUID
							UUID uuid = UUID.randomUUID();
							// Convert the UUID to string and use it as user key
							userKey = uuid.toString();
							// Store the new user to list
							users.put(userName, userKey);
							// Meanwhile, save the new user to file
							addUser(FILE_USERS, userName, userKey);
						}
						// Remember the user for the next request
						currentUserName = userName;
						// Get new joke/proverb for the new user
						getJokeOrProverb(currentUserName, userKey, serverName, PORT_NUMBER);
					}
				}

				// Save logs to file
				saveLogs(true);

			} while (!userName.equals("quit")); // End the client if 'quit' is an input

			// Clear temp log list
			alLogs.clear();
			writeLog("Async Joke Client has been shut down!");
			writeLog("--Input 'java AsyncJokeClient' to start a new one.--");
			saveLogs(true);
		}
		catch(IOException ex)
		{
			//Handle the exception
			ex.printStackTrace();
		}
	}

	/**
	 * Connect to the server, send user name and unique key(UUID) and get a random
	 * joke or proverb.
	 * @param username, the user name who asks for a joke or proverb
	 * @param userkey, the user key which is used to identify the unique user
	 * @param servername, the server name to be connected to
	 * @param port, the port for the socket
	 */
	private static void getJokeOrProverb(String username, String userkey, String servername, int port){
		Socket socket;
		BufferedReader fromServer;
		PrintStream toServer;

		try{
			// Open a new socket connection to the server with the specified port number
			socket = new Socket(servername, port);
			fromServer = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			toServer = new PrintStream(socket.getOutputStream());
			// Send user name and user key to server
			toServer.println(username + " " + userkey);
			toServer.flush();

			setupUDPServer(port);

			// There is nothing special for the number(50) of iteration times
			/*String strOutput;
			for (int ix = 1; ix <= 50; ix++){
				strOutput = fromServer.readLine();
				if (strOutput == null){  // There is no more messages
					break;
				}
				else {
					// Write log and print
					writeLog(strOutput);
				}
			}*/

			// Close the socket
			socket.close();
		}
		catch(IOException ex) {
			writeLog("Exception occurs, see the below details:");
			ex.printStackTrace ();
		}
	}

	private static void setupUDPServer(int udpPort) throws IOException {
		int len = 1024;
		byte[] receiveData = new byte[len];
		byte[] data;

		System.out.println("Setup UDP server at port: " + udpPort);
		DatagramSocket serverSocket = new DatagramSocket(udpPort);
		DatagramPacket receivePacket = new DatagramPacket(receiveData, len);
		serverSocket.receive(receivePacket);
		data = new byte[receivePacket.getLength()];
		System.arraycopy(receivePacket.getData(), receivePacket.getOffset(), data, 0, receivePacket.getLength());
		String sentence = new String(data);
		System.out.println("Received UDP data: " + sentence);
		serverSocket.close();
	}

	/**
	 * Get the user list from the specified file
	 * @param filename, the name of the file
	 * @return, user list
	 */
	private static HashMap<String, String> getUsers(String filename) {
		// Use hashmap to store the user list, the key is user name and the value is UUID
		HashMap<String, String> mapUsers = new HashMap<String, String>();
		// This will reference one line at a time
		String lineUser = null;

		try {
			// Check file exists
			File userfile = new File(filename);
			if(!userfile.exists() || userfile.isDirectory()) {
				return mapUsers;
			}
			// Open the user file
			FileReader fileReader = new FileReader(filename);
			// User bufferedReader to go through the file
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			// Read the file in lines one by one
			while((lineUser = bufferedReader.readLine()) != null) {
				String[] namekey = lineUser.split(" "); // Split the name and key
				if (namekey.length != 2) { // Validate the data
					System.out.println("Invalid line: '" + lineUser + "' in '" + filename + "'");
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
			// Open the user file, create new one if not exists
			FileWriter fileWriterUser = new FileWriter(filename, true);
			// User BufferedWriter to add new line
			BufferedWriter bufferedWriterdUser = new BufferedWriter(fileWriterUser);

			// Concatenate user name and key with a space
			bufferedWriterdUser.write(username + " " + userkey);
			// One user one line
			bufferedWriterdUser.newLine();

			// Always close files.
			bufferedWriterdUser.close();
		}
		catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + filename + "'");
		}
		catch(IOException ex) {
			ex.printStackTrace();
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
