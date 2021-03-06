/*--------------------------------------------------------
1. Name / Date:
Rong Zhuang
26 Oct, 2015

2. Java version used:
build 1.8.0_60

3. Precise command-line compilation examples / instructions:
> javac AsyncJokeServer.java

4. Precise examples / instructions to run this program:
In separate shell windows:
> java AsyncJokeServer

5. List of files needed for running the program.
 a. AsyncJokeServer.class
 b. Worker.class
 c. AdminWorker.class
 d. AdminListener.class

6. Notes:
 a. Server will monitor two ports: 4653 for general joke and proverb service,
    8722 for admin service.
 b. Regarding the general service, server can return random joke or proverb.
    Each user's state will be remembered, even if server restarts from shutdown.
 c. Server can read and write user states from/to disk. The states can be
    re-initialized after the server restart.
 d. The states are stored in file AsyncServerUserStates.txt.
    Format of the state file.
    UUID, Blank Space, 10 digits separate by comma (1-used, 0-not used)
    One user one line, Exmaple:
    2b703f01-e9a4-4b20-a7a7-402009f1ded9 1,1,1,0,1,0,1,1,0,1
 e. To stop the server, type 'SD' in admin client.
----------------------------------------------------------*/
/*--------------------------------------------------------
Enhancements for AsyncJokeServer
1. Added a new method 'sendJokeProverbByUDP' to Worker class. This method is used
   to send UPD message back to client.
2. Enhanced the method 'seekJokeProverb', make thread sleep 40 seconds before
   calling method 'sendJokeProverbByUDP'.
----------------------------------------------------------*/
import java.io.*;
import java.net.*;
import java.util.*;
/**
 * An interface for server mode listener
 */
interface ServerModeListener {
	// Mode changed method
	void modeChanaged(AsyncJokeServer.ServerMode mode);
}

/**
 * An instance of Worker for the server.
 * The Worker handles the joke/proverb service for the server. Its job is to
 * accept requests for the client and send results back.
 */
class Worker extends Thread {
	// File stores the user state, including the uer id and joke/proverb status
	String FILE_USERSTATES = "AsyncServerUserStates.txt";
	// Define the duration before sending joke/proverb back to client
	int SLEEP_IN_SECONDS = 40;
	// Socket
	Socket socket;
	// Server Mode
	AsyncJokeServer.ServerMode serverMode = AsyncJokeServer.ServerMode.JOKE;
	// Joke or Proverb list
	HashMap<Integer, String> mapJokesProverbs = new HashMap<Integer, String>();
	// User state, key is UUID, value is states list
	HashMap<String, int[]> mapStates = new HashMap<String, int[]>();
	// Port for client monitoring udp connection
	int port;

	/**
	 * Constructor
	 * @param s, the socket which is to be monitored
	 * @param mode, server mode
	 * @param list, joke/proverb list
	 * @param ss, user states
	 * @param prt, udp port
	 */
	public Worker (Socket s, AsyncJokeServer.ServerMode mode, HashMap<Integer, String> list, HashMap<String, int[]> ss, int prt)
	{
		socket = s;
		serverMode = mode;
		mapJokesProverbs = list;
		mapStates = ss;
		port = prt;
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
				String clientId, userName, userKey = "";
				// Accept the command(user name + user key) from the client
				clientId = reader.readLine();
				userName = clientId.split(" ")[0];
				userKey = clientId.split(" ")[1];

				// Check the server mode
				switch (serverMode) {
					case JOKE:
						System.out.println(userName + " is seeking for a new joke...");
						break;
					case PROVERB:
						System.out.println(userName + " is seeking for a new proverb...");
						break;
					default:
						System.out.println("Wrong mode, can't continue.");
						socket.close();
						return;
				}
				socket.close();
				System.out.println("TCP connection has been broken.");
				// Seek joke/proverb
				seekJokeProverb(userName, userKey, serverMode, mapJokesProverbs, mapStates, port, SLEEP_IN_SECONDS);
				// Save states to file
				saveStates(FILE_USERSTATES, mapStates);
				System.out.println("I'm waiting for new request...");
			}
			catch(IOException ex){
				System.out.println ("Exception occurs, see the below details:");
  			ex.printStackTrace ();
			}
		}
		catch(IOException ex){
			// Handle the exception
			System.out.println(ex);
		}
	}

	/**
	 * Seek joke or proverb
	 * @param username, the user name
	 * @param userkey, the user key(UUID)
	 * @param mode, Server mode
	 * @param mapjokesproverbs, joke or proverb list
	 * @param mapstates, user states
	 * @param port, udp port
	 * @param sleep, set sleep time before sending udp message
	 */
	private void seekJokeProverb(String username, String userkey,
		AsyncJokeServer.ServerMode mode, HashMap<Integer, String> mapjokesproverbs,
		HashMap<String, int[]> mapstates, int port, int sleep){
		try{
			System.out.println("Server is working for [" + username + "] in {" + mode + "} mode ...");

			// The remaining joke/proverb list
			HashMap<Integer, Integer> mapRemaining = new HashMap<Integer, Integer>();
			// The state for the current user
			int[] stateArray = getStates(mode, mapstates, userkey);
			//System.out.println("userkey:"+ userkey);
			//System.out.println("stateArray:" + Arrays.toString(stateArray));

			if (stateArray == null){ // A new user
				// Initial the state for new user
				stateArray = new int[mapjokesproverbs.size()];
			}
			else { // An existing user
				int index = 0;
				// Find the remaining valid jokes/proverbs
				for(int ix=0; ix < stateArray.length; ix++) {
					if(stateArray[ix] == 0) { //not used joke
						mapRemaining.put(index, ix);
						index++;
					}
				}
			}

			//if a new user or all jokes/proverbs are used, reset all can be used.
			if (mapRemaining.size() == 0){
				for(int ix=0; ix < stateArray.length; ix++) {
					mapRemaining.put(ix, ix);
				}
			}

			// Define a random object
			Random rand = new Random();
			// Generate a random number for the valid joke/proverb
			int remainingIndex = rand.nextInt(mapRemaining.size());
			// Mapping to the actual index in joke/proverb list
			int actualIndex = mapRemaining.get(remainingIndex);
			// Get joke/proverb content
			String newJokeProverb = mapjokesproverbs.get(actualIndex);
			// Replace the content with current user name
			newJokeProverb = newJokeProverb.replace("[Xname]", username);
			switch (mode) {
				case JOKE:
					System.out.println("Here is the new joke: ");
					break;
				case PROVERB:
					System.out.println("Here is the new proverb: ");
					break;
				default:
					System.out.println("Wrong parameter, either joke or proverb mode, please.");
			}

			System.out.println(newJokeProverb);
			System.out.println("Sleep for " + sleep + " seconds, zzzzzz...");
			Thread.sleep(sleep * 1000);
			switch (mode) {
				case JOKE:
					System.out.println("I'm waking up! Sending joke back to client with UDP.");
					break;
				case PROVERB:
					System.out.println("I'm waking up! Sending proverb back to client with UDP.");
					break;
			}

			// Send joke/proverb back to client via UDP
			sendJokeProverbByUDP(newJokeProverb, port);

			// The current worker is working on the last valid joke/perverb
			if (mapRemaining.size() == 1) {
				// Reset the state if all jokes/proverbs have been used
				for(int ix=0; ix < stateArray.length; ix++) {
					stateArray[ix] = 0;
				}
			}
			else { // There are still some jokes/proverbs have not been used
				// Mark the current joke/perverb has been used
				stateArray[actualIndex] = 1;
			}

			// Update the states for the current user
			updateStates(mode, mapstates, userkey, stateArray);
		}
		catch(Exception ex) {
			//Handle the exception
			System.out.println("Failed in attemp to seek joke/proverb for [" + username +"]");
		}
	}

	/**
	 * Send joke or proverb to client with UDP
	 * @param newJokeProverb, joke or proverb
	 * @param port, port for client
	 */
	private void sendJokeProverbByUDP(String newJokeProverb, int port) throws IOException {
		// Define UDP socket
		DatagramSocket clientSocket = new DatagramSocket();
		// Get ip address for localhost, same machine
		InetAddress IPAddress = InetAddress.getByName("localhost");
		// Define default package length
		int len = 1024;
		// Check the actual length
		if (newJokeProverb.getBytes().length > len) {
			// Update with the actual lenght of content
			len = newJokeProverb.getBytes().length;
		}
		// Define byte array for sending data
		byte[] sendData = new byte[len];
		sendData = newJokeProverb.getBytes();
		// Define upd package
		DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
		// Send to client
		clientSocket.send(sendPacket);
		System.out.println("Joke/proverb has been sent to " + IPAddress + " at port:" + port);
		System.out.println("\n");
		// Close connection after sending
		clientSocket.close();
	}

	/**
	 * Get the joke or proverb states from the whole list
	 * @param mode, Server mode
	 * @param mapstates, user states(whole)
	 * @return, user states(partial)
	 */
	private int[] getStates(AsyncJokeServer.ServerMode mode,
		HashMap<String, int[]> mapstates, String key) {
		// Define the return list
		int[] retStates = new int[5];
		// Get states for current user
		int[] states = mapstates.get(key);
		if (states == null)
			 return null;

		// Filter the state, joke or proverb
		switch (mode) {
			case JOKE:
				for (int ix = 0; ix < 5; ix++){
					retStates[ix] = states[ix];
				}
				break;
			case PROVERB:
				for (int ix = 5; ix < states.length; ix++){
					retStates[ix-5] = states[ix];
				}
				break;
			default:
				break;
		}

		return retStates;
	}

	/**
	 * Update original whole state list
	 * @param mode, Server mode
	 * @param mapstates, user states(whole)
	 * @param partialstates, user states(partial, joke or proverb)
	 */
	private static void updateStates(AsyncJokeServer.ServerMode mode,
		HashMap<String, int[]> mapstates, String key, int[] partialstate) {

		int[] states = mapstates.get(key);
		if (states == null || states.length == 0) { // New user
			states = new int[10];
			if (mode == AsyncJokeServer.ServerMode.JOKE) {
				for (int ix = 0; ix < states.length; ix++){
					if (ix < partialstate.length)
						states[ix] = partialstate[ix];
					else
						states[ix] = 0;
				}
			}
			else if (mode == AsyncJokeServer.ServerMode.PROVERB) {
				for (int ix = 0; ix < states.length; ix++){
					if (ix < partialstate.length)
						states[ix] = 0;
					else
						states[ix] = partialstate[ix - 5];
				}
			}
		}
		else { // Existing user
			if (mode == AsyncJokeServer.ServerMode.JOKE) {
				for (int ix = 0; ix < partialstate.length; ix++){
					states[ix] = partialstate[ix];
				}
			}
			else if (mode == AsyncJokeServer.ServerMode.PROVERB) {
				for (int ix = 0; ix < partialstate.length; ix++){
					states[ix + 5] = partialstate[ix];
				}
			}
		}
		mapstates.put(key, states);
	}

	/**
	 * Store whole states to the specified file
	 * @param filename, the name of the file
	 * @param statelist, user states(whole)
	 */
	private static void saveStates(String filename, HashMap<String, int[]> statelist) {
		try {
			// Open the state file, create new one if not exists
			FileWriter fileWriterState = new FileWriter(filename);
			// User BufferedWriter to add new line
			BufferedWriter bufferedWriterdState = new BufferedWriter(fileWriterState);
			//System.out.println("Size of statelist: " + statelist.size());
			for (Map.Entry<String, int[]> entry : statelist.entrySet()) {
				String uuid = entry.getKey();
				int[] states = entry.getValue();
				StringBuilder sb = new StringBuilder();
				for(int ix = 0; ix < states.length; ix++) {
					sb.append(states[ix]);
					if (ix != states.length - 1)
						sb.append(",");
				}

				// Concatenate user name and key with a space
				bufferedWriterdState.write(uuid + " " + sb.toString());
				// One user one line
				bufferedWriterdState.newLine();
			}

			// Always close files.
			bufferedWriterdState.close();
		}
		catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + filename + "'");
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
	}
}

/**
 * An instance of Admin Worker for the server.
 * The Admin Worker handles the admin services for the server. Its job is to
 * accept admin commands from the admin client and inform the server to switch
 * mode accordingly.
 */
class AdminWorker extends Thread {
	// Define the listener list
	private List<ServerModeListener> listeners = new ArrayList<ServerModeListener>();
	Socket socket;

	/**
	 * Constructor
	 * @param s, the socket which is to be monitored
	 */
	public AdminWorker (Socket s)
	{
		socket = s;
	}

	/**
	 * Allow external objects to subscribe the mode change event
	 * @param lsn, object which has implements ServerModeListener interface
	 */
	public void addListener(ServerModeListener lsn) {
		// Add the object to current listener list
		listeners.add(lsn);
	}

	/**
	 * Start to work, after being assigned tasks by the server
	 */
	public void run(){
		// Output stream to the client
		PrintStream printer = null;
		// Local reader from the client
		BufferedReader reader = null;
		// Server Mode, Joke mode by default
		AsyncJokeServer.ServerMode mode = AsyncJokeServer.ServerMode.JOKE;

		try{
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			printer = new PrintStream(socket.getOutputStream());
			try{
				// Admin command
				String adminCommand = "";
				// Read command
				adminCommand = reader.readLine();
				// Switch the server mode accordingly
				switch (adminCommand.toUpperCase()) {
					case "J":
						mode = AsyncJokeServer.ServerMode.JOKE;
						break;
					case "P":
						mode = AsyncJokeServer.ServerMode.PROVERB;
						break;
					case "M":
						mode = AsyncJokeServer.ServerMode.MAINTENANCE;
						break;
					case "SD":
						mode = AsyncJokeServer.ServerMode.SHUTDOWN;
						break;
					default:
						printer.println("Unknown command! Please try again!");
						socket.close();
						return;
				}

				// Print to the Admin Client
				printer.println("Server mode has been switched to: [" + mode + "].");
				// Print to the Server
				System.out.println("Server mode has been switched to: [" + mode + "].");

			}
			catch(IOException ex){
				// Handle the exception
				System.out.println("Server read error");
			}
			//Close all
			reader.close();
			printer.close();
			socket.close();

			// Fire the mode changed event to the subscribers
			for (ServerModeListener sml : listeners)
				sml.modeChanaged(mode);
		}
		catch(IOException ex){
			// Handle the exception
			System.out.println(ex);
		}
	}
}

/**
 * An AdminListener can create Admin Worker to handle the command from the client.
 * The AdminListener uses a different port number to accept only admin commands.
 * It dispatches the task to Admin worker and update the mode.
 */
class AdminListener implements Runnable, ServerModeListener {
	// Flag indicates whether the Admin listener needs to continue work
	public volatile boolean adminListenerRunning = true;
	// Public server mode which can be changed by the call back method from
	// Admin worker and be accessed by Server.
	public AsyncJokeServer.ServerMode servermode = AsyncJokeServer.ServerMode.JOKE;
	// Sever socket
	ServerSocket adminServerSock;
	// Socket to listen to the Admin Client
	Socket adminSock;

	public void run(){ // Running the Admin listener
		// The maximum queue length for incoming connection
		int queue_len = 6;
		// We are listening at a different port for Admin clients
		int port = 8722;

		try{
			adminServerSock = new ServerSocket(port, queue_len);
			System.out.println("Rong Zhuang's Async Joke Admin Server is starting up, listening at port " + port + ".");

			while (adminListenerRunning) {
				// Wait for the next Admin client connection:
				adminSock = adminServerSock.accept();

				// Create a new admin worker
				AdminWorker admWorker = new AdminWorker(adminSock);
				// Add listen to admin worker to monitor any mode change
				admWorker.addListener(this);
				// Start admin worker in new thread
				admWorker.start();
			}
		}
		catch (IOException ioe) {
			System.out.println(ioe);
		}
		finally {
			System.out.println("Admin Service is stopped!");
		}
	}

	/**
	 * Call back method from admin worker to update the mode status
	 * @param mode, server mode
	 */
	public void modeChanaged(AsyncJokeServer.ServerMode mode) {
		// Update server mode
		servermode = mode;

		if (servermode == AsyncJokeServer.ServerMode.SHUTDOWN) {
			adminListenerRunning = false;
			try {
				// Close the Admin server socket
				if(!adminServerSock.isClosed())
					adminServerSock.close();
			}
			catch (IOException ioe) {
				System.out.println(ioe);
			}
		}
	}
}

/**
 * A server can create Worker to handle the general joke/proverb requests from
 * the client. It can also create Admin Worker to accept admin commands to
 * switch server mode. The server does nothing but dispatches tasks to Workers
 * or Admin Workers.
 */
public class AsyncJokeServer {
	// File stores the user state, including the uer id and joke/proverb status
	private static final String FILE_USERSTATES = "AsyncServerUserStates.txt";
	// Define an enum with four server mode
	public enum ServerMode { JOKE, PROVERB, MAINTENANCE, SHUTDOWN };
	// Current server mode
	static ServerMode currentMode = ServerMode.JOKE;
	// Flag indicates whether the Server needs to continue work
	static boolean serverRunning = true;
	/**
	 * Start a new AdminListener to monitor and admin service from Admin Client.
	 * Meanwhile, start a new Server Socket to handle general joke/proverb service
	 * from joke client.
	 */
	public static void main(String args[]){
		// The maximum queue length for incoming connection
		int queue_len = 6;
		// Port number for general requests(joke or proverb)
		int port = 4653;
		// A reference of the client socket
		Socket socket;
		// User states, the key is user key(UUID) and the value is state list
		HashMap<String, int[]> stateList = new HashMap<String, int[]>();

		try{
			// create a new AdminListener instance for admin purpose
			AdminListener adminLnr = new AdminListener();
			Thread t = new Thread(adminLnr);
			// Start it for waiting admin command
			t.start();

			// Setup the server socket
			ServerSocket servsocket = new ServerSocket(port, queue_len);
			System.out.println("Rong Zhuang's Async Joke Server is starting up, listening at port " + port + ".");

			while(serverRunning){
				// Make the server socket wait for the next client request
				socket = servsocket.accept();
				if (currentMode != adminLnr.servermode) {
					// Update the latest mode
					currentMode = adminLnr.servermode;
				}
				// Get states from file
				stateList = getStates(FILE_USERSTATES);
				// Work on different server mode
				switch(currentMode) {
					case JOKE:
					case PROVERB:
						// Assign task to general work to get joke or proverb
						new Worker(socket, currentMode, createList(currentMode), stateList, port).start();
						break;
					case SHUTDOWN:
						// Stop the current general service
						serverRunning = false;
						break;
					case MAINTENANCE:
						// No service can be provided in maintenance mode
						PrintStream printer = new PrintStream(socket.getOutputStream());
						printer.println("The server is temporarily unavailable -- check-back shortly.");
						printer.flush();
						printer.close();
						break;
					default:
						break;
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

	/**
	 * Create joke/proverb list according to the server mode
	 * @param mode, server mode
	 * @return, joke/proverb list
	 */
	private static HashMap<Integer, String> createList(ServerMode mode) {
		//Joke or proverb list, the key is index and the value is the joke/proverb content
		HashMap<Integer, String> list = new HashMap<Integer, String>();

		switch (mode) {
			case JOKE:
				// Jokes from http://www.greatcleanjokes.com/
				list.put(0, "A. [Xname]'s new joke: Today a man knocked on my door and asked for a small donation towards the local swimming pool. I gave him a glass of water.");
				list.put(1, "B. [Xname]'s new joke: When my grandson asked me how old I was, I teasingly replied, 'I'm not sure'. 'Look in your underwear, Grandma,' he advised, 'mine says I'm 4 to 6.'");
				list.put(2, "C. [Xname]'s new joke: Doctor: 'I am not exactly sure of the cause. I think it could be due to alcohol.' Patient: 'That's OK. I will come back when you are sober.'");
				list.put(3, "D. [Xname]'s new joke: Wife: You hate my relatives! Husband: No, I don't! In fact, I like your mother-in-law more than I like mine.");
				list.put(4, "E. [Xname]'s new joke: What is Bruce Lee's favorite drink? Wataaaaah!");
				break;
			case PROVERB:
				// Proverbs from http://www.phrasemix.com/collections/the-50-most-important-english-proverbs
				list.put(0, "A. [Xname]'s new proverb: Two wrongs don't make a right.");
				list.put(1, "B. [Xname]'s new proverb: When in Rome, do as the Romans.");
				list.put(2, "C. [Xname]'s new proverb: When the going gets tough, the tough get going.");
				list.put(3, "D. [Xname]'s new proverb: No man is an island.");
				list.put(4, "E. [Xname]'s new proverb: Hope for the best, but prepare for the worst.");
				break;
			default:
				System.out.println("Wrong mode parameter, must be either joke or proverb mode.");
				break;
		}
		return list;
	}

	/**
	 * Get the user states from the specified file
	 * @param filename, the name of the file
	 * @return, user states
	 */
	private static HashMap<String, int[]> getStates(String filename) {
		// Use hashmap to store the user list, the key is UUID and the value is statelist
		HashMap<String, int[]> mapStates = new HashMap<String, int[]>();
		// This will reference one line at a time
		String lineState = null;
		try {
			//check file exists
			File statefile = new File(filename);
			if(!statefile.exists() || statefile.isDirectory()) {
				return mapStates;
			}
			// Open the state file
			FileReader fileReaderState = new FileReader(filename);
			// User bufferedReader to go through the file
			BufferedReader bufferedReaderState = new BufferedReader(fileReaderState);
			// Read the file in lines one by one
			while((lineState = bufferedReaderState.readLine()) != null) {
				// Split the key(UUID) and state list
				String[] keyState = lineState.split(" ");
				if (keyState.length != 2) { // Validate the data
					System.out.println("Invalid line: '" + lineState + "' in '" + filename + "'");
					return mapStates;
				}
				// Split the state list(5 for joke, 5 for proverb)
				String[] arrStates = keyState[1].split(",");
				int[] intStates = new int[arrStates.length];
				for (int ix = 0; ix < arrStates.length; ix++){
					intStates[ix] = Integer.parseInt(arrStates[ix]);
				}
				// Key(UUID) + State list
				mapStates.put(keyState[0], intStates);
				//System.out.println("keyState[0]:" + keyState[0]);
				//System.out.println("intStates:" + Arrays.toString(intStates));
			}
			// Always close files.
			bufferedReaderState.close();
		}
		catch(FileNotFoundException ex) {
			System.out.println("Unable to open file '" + filename + "'");
		}
		catch(IOException ex) {
			ex.printStackTrace();
		}
		finally {
			return mapStates;
		}
	}
}
