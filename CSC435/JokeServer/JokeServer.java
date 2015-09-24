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
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
/**
 * An interface for server mode listener
 */
interface ServerModeListener {
	// Mode changed method
  void modeChanaged(JokeServer.ServerMode mode);
}

/**
 * An instance of Worker for the server.
 * The Worker handles the joke/proverb service for the server. Its job is to
 * accept requests for the client and send results back.
 */
class Worker extends Thread {
	Socket socket;
  // Server Mode
	JokeServer.ServerMode servermode = JokeServer.ServerMode.JOKE;
	// Joke or Proverb list
	HashMap<Integer, String> maplist = new HashMap<Integer, String>();
	// User state, key is UUID, value is states list
	HashMap<String, int[]> jokeStates = new HashMap<String, int[]>();
  HashMap<String, int[]> proverbStates = new HashMap<String, int[]>();

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
    switch (servermode) {
      case JOKE:
        jokeStates = ss;
        break;
      case PROVERB:
        proverbStates = ss;
        break;
      default:
        break;
    }
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
				userName =  clientId.split(" ")[0];
				userKey =  clientId.split(" ")[1];
        // Check the server mode
        switch (servermode) {
          case JOKE:
            System.out.println(userName + " is seeking for a new joke...");
            // Seek joke
            seekJokeProverb(userName, userKey, servermode, maplist, jokeStates, printer);
            break;
          case PROVERB:
            System.out.println(userName + " is seeking for a new proverb...");
            // Seek proverb
            seekJokeProverb(userName, userKey, servermode, maplist, proverbStates, printer);
            break;
          default:
            System.out.println("Wrong mode, can't continue.");
            socket.close();
            return;
        }
				System.out.println("I'm waiting for new request...");
			}
			catch(IOException ex){
        System.out.println ("Exception occurs, see the below details:");
  			ex.printStackTrace ();
			}
			socket.close();
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
	 * @param maplist, joke or proverb list
	 * @param statelist, user states
	 * @param printer, the output stream object of the client
     */
	private void seekJokeProverb(String username, String userkey,
              JokeServer.ServerMode mode, HashMap<Integer, String> maplist,
              HashMap<String, int[]> statelist, PrintStream printer){
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
        // Mark the current joke/perverb has been used
				stateArray[actualIndex] = 1;
			}

			// Update the states for the current user
			statelist.put(userkey, stateArray);
		}
		catch(Exception ex) {
      //Handle the exception
			printer.println("Failed in attemp to seek joke/proverb for [" + username +"]");
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
    // Output stream to the client
		PrintStream printer = null;
    // Local reader from the client
		BufferedReader reader = null;
    // Server Mode, Joke mode by default
		JokeServer.ServerMode mode = JokeServer.ServerMode.JOKE;

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
						mode = JokeServer.ServerMode.JOKE;
						break;
					case "P":
						mode = JokeServer.ServerMode.PROVERB;
						break;
					case "M":
						mode = JokeServer.ServerMode.MAINTENANCE;
						break;
					case "SD":
						mode = JokeServer.ServerMode.SHUTDOWN;
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
	public JokeServer.ServerMode servermode = JokeServer.ServerMode.JOKE;
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
			System.out.println("Rong Zhuang's Joke Admin Server is starting up, listening at port " + port + ".");

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
  public void modeChanaged(JokeServer.ServerMode mode) {
    // Update server mode
    servermode = mode;

    if (servermode == JokeServer.ServerMode.SHUTDOWN) {
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
public class JokeServer {
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
    HashMap<String, int[]> jokeStates = new HashMap<String, int[]>();
    HashMap<String, int[]> proverbStates = new HashMap<String, int[]>();

		try{
      // create a new AdminListener instance for admin purpose
			AdminListener adminLnr = new AdminListener();
			Thread t = new Thread(adminLnr);
      // Start it for waiting admin command
			t.start();

      // Setup the server socket
			ServerSocket servsocket = new ServerSocket(port, queue_len);
			System.out.println("Rong Zhuang's Joke Server is starting up, listening at port " + port + ".");

			while(serverRunning){
        // Make the server socket wait for the next client request
				socket = servsocket.accept();
				if (currentMode != adminLnr.servermode) {
					//mapStates.clear();
					// Update the latest mode
					currentMode = adminLnr.servermode;
				}
				switch(currentMode) {
					case JOKE:
            // Assign task to general work to get jokes
            new Worker(socket, currentMode, createList(currentMode), jokeStates).start();
            break;
					case PROVERB:
						// Assign task to general work to get proverbs
						new Worker(socket, currentMode, createList(currentMode), proverbStates).start();
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
            socket.close();         
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

}
