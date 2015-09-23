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
import java.util.*; // Import the util libraries, including HashMap, ArrayList, ...

/**
 * An interface for server mode listener
 */
interface ServerModeListener {
	// Mode changed method
    void modeChanaged(JokeServer.ServerMode mode);
}

/**
 * An instance of Worker for the server.
 * The Worker's job is to accept requests for the client and send results back.
 */
class Worker extends Thread {
	Socket socket;
	JokeServer.ServerMode servermode = JokeServer.ServerMode.JOKE;
	// Joke or Proverb list
	HashMap<Integer, String> maplist = new HashMap<Integer, String>();
	// User state, UUID and states list
	HashMap<String, int[]> states = new HashMap<String, int[]>();	
	 
	/**
     * Construct
     * @param s, the socket which is to be monitored
	 * @param mode, server mode
	 * @param list, joke/proverb list
	 * @param ss, user states
     */
	public Worker (Socket s, JokeServer.ServerMode mode, HashMap<Integer, String> list, HashMap<String, int[]> ss)
	{
		socket = s;	
		servermode = mode;
		maplist = list;
		states = ss;
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
				String clientId, userName, userKey = "";
				clientId = reader.readLine(); // Accept the command(hostname) from the client	
				userName =  clientId.split(" ")[0];
				userKey =  clientId.split(" ")[1];
				switch (servermode) {
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
				// Seek joke or proverb according to the mode and user state
				seekJokeProverb(userName, userKey, servermode, maplist, states, printer); 
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
     * @param username, the user name
	 * @param userkey, the user key(UUID)
     * @param mode, Server mode
	 * @param maplist, joke or proverb list
	 * @param statelist, user states
	 * @param printer, the output stream object of the client
     */
	private void seekJokeProverb(String username, String userkey, JokeServer.ServerMode mode, HashMap<Integer, String> maplist, HashMap<String, int[]> statelist, PrintStream printer){
		try{
			printer.println("Server is working for [" + username + "] in {" + servermode + "} mode ...");		
			
			// The remaining joke/proverb list
			HashMap<Integer, Integer> mapRemaining = new HashMap<Integer, Integer>();
			// The state for the current user
			int[] stateArray = statelist.get(userkey);			
			
			if (stateArray == null){ // A new user
				// Initial the state for new user
				stateArray = new int[maplist.size()];		
			}
			else { // A existing user	
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
			String newJokeProverb = maplist.get(actualIndex);
			// Replace the content with current user name
			newJokeProverb = newJokeProverb.replace("[Xname]", username);
			switch (mode) {
				case JOKE:
					printer.println("Here is the new joke: ");
					break;
				case PROVERB:
					printer.println("Here is the new proverb: "); 
					break;
				default:
					System.out.println("Wrong parameter, either joke or proverb mode, please.");	
			}
			// Output the result to client
			printer.println(newJokeProverb);
			
			// The current worker is working on the last valid joke/perverb
			if (mapRemaining.size() == 1) {
				// Reset the state if all jokes have been used
				for(int ix=0; ix < stateArray.length; ix++) { 
					stateArray[ix] = 0;
				}
			}
			else { // There are still some jokes/proverbs have been used				
				stateArray[actualIndex] = 1; // Mark the current joke/perverb has been used
			}
	
			// Update the states for the current user
			statelist.put(userkey, stateArray);
		}
		catch(Exception ex) {
			printer.println("Failed in attemp to seek joke/proverb for [" + username +"]"); //Handle the exception
		}
	}
}

/**
 * An instance of Worker for the server.
 * The Worker's job is to accept requests for the client and send results back.
 */
class AdminWorker extends Thread {
	// Define the listener list
	private List<ServerModeListener> listeners = new ArrayList<ServerModeListener>();
	Socket socket;
	 
	/**
     * Construct
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
		PrintStream printer = null; // Output stream to the client
		BufferedReader reader = null; // Local reader from the client
		JokeServer.ServerMode mode = JokeServer.ServerMode.JOKE;
		
		try{
			reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			printer = new PrintStream(socket.getOutputStream());
			try{
				String adminCommand = "";
				// Read command 
				adminCommand = reader.readLine();
				// Switch the server mode accordingly
				switch (adminCommand) {
					case "joke-mode":
						mode = JokeServer.ServerMode.JOKE;
						break;
					case "proverb-mode":
						mode = JokeServer.ServerMode.PROVERB;
						break;
					case "maintenance-mode":
						mode = JokeServer.ServerMode.MAINTENANCE;
						break;
					default:
						printer.println("Unknown command, please try again! Input joke-mode, proverb-mode or maintenance-mode.");
						socket.close();
						return;
				}
				
				// Fire the mode changed event to the subscribers
				for (ServerModeListener sml : listeners)
					sml.modeChanaged(mode);
				
				// Print to the Admin Client
				printer.println("Server mode has been switched to: [" + adminCommand + "].");
				// Print to the Server
				System.out.println("Server mode has been switched to: [" + adminCommand + "].");
				
			}
			catch(IOException ex){
				System.out.println("Server read error"); // Handle the exception
			}
			socket.close();
		}
		catch(IOException ex){
			System.out.println(ex); // Handle the exception
		}
	}
}

/**
 * An AdminLooper can create Admin Worker to handle the command from the client.
 * The AdminLooper uses a different port number to accept only admin commands.
 * It dispatches the task to Admin worker and update the mode.
 */
class AdminLooper implements Runnable, ServerModeListener {
	public static boolean adminControlSwitch = true;
	// Public server mode which can be changed by the call back method from Admin worker
	// and be accessed by Server.
	public JokeServer.ServerMode servermode = JokeServer.ServerMode.JOKE;

	public void run(){ // RUNning the Admin listen loop
		int queue_len = 6; // The maximum queue length for incoming connection
		int port = 8722;  // We are listening at a different port for Admin clients
		Socket sock;

		try{
			ServerSocket servsock = new ServerSocket(port, queue_len);
			System.out.println("Rong Zhuang's Joke Server Admin is starting up, listening at port " + port + ".");
			
			while (adminControlSwitch) {
				// Wait for the next ADMIN client connection:
				sock = servsock.accept();
				AdminWorker admWorker = new AdminWorker(sock);
				// Add listen to admin worker to monitor any change in mode
				admWorker.addListener(this);
				admWorker.start(); 
			}
		}
		catch (IOException ioe) {
			System.out.println(ioe);
		}
	}
	
	/**
     * Call back method from admin worker to update the mode status
     * @param mode, server mode
     */
    public void modeChanaged(JokeServer.ServerMode mode) {
		// Update server mode
        servermode = mode;
    }
}

/**
 * A server can create general Worker to handle the requests from the client.
 * This server can also create Admin Worker to accept command to switch server mode.
 * The server does nothing but dispatch tasks to workers(admin workers).
 */
public class JokeServer {
	// Define an enum with three server mode
	public enum ServerMode { JOKE, PROVERB, MAINTENANCE };
	/**
     * Start a Server Socket and prepare the handle the requests from the client
     */
	public static void main(String args[]){
		int queue_len = 6; // The maximum queue length for incoming connection
		int port = 4653; // Port number for general requests(joke or proverb)
		Socket socket; // A reference of the client socket
		HashMap<String, int[]> mapStates = new HashMap<String, int[]>(); // The first is user key(UUID) and the second is state list
		ServerMode currentMode = ServerMode.JOKE;
		
		try{
			AdminLooper AL = new AdminLooper(); // create a DIFFERENT thread for admin purpose
			Thread t = new Thread(AL);
			t.start();  // ...and start it, waiting for administration input
			
			ServerSocket servsocket = new ServerSocket(port, queue_len); // Setup the server socket
			System.out.println("Rong Zhuang's Joke Server is starting up, listening at port " + port + ".");
			while(true){ // Runs forever, if no exception occurs				
				socket = servsocket.accept(); // Make the server socket wait for the next client command
				if (currentMode != AL.servermode) {
					//mapStates.clear();
					// Update the latest mode
					currentMode = AL.servermode;
				}
				if (currentMode == ServerMode.MAINTENANCE) {
					// No service can be provided in maintenance mode
					PrintStream printer = new PrintStream(socket.getOutputStream());
					printer.println("The server is temporarily unavailable -- check-back shortly.");
					socket.close();
				}
				else {
					// Assign task to general work to get joke or proverb
					new Worker(socket, AL.servermode, createList(AL.servermode), mapStates).start(); 
				}
			}
		}
		catch(IOException ex){
			System.out.println(ex); //Handle the exception
		}
	}
	
	/**
     * Create joke/proverb list according to the server mode
     * @param mode, server mode
     */
	private static HashMap<Integer, String> createList(ServerMode mode) {
		//Joke or proverb list, the first is index and the second is the joke/proverb content
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
		}
		return list;		
	}	
	
}