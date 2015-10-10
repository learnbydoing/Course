/*--------------------------------------------------------
1. Name / Date:
Rong Zhuang
09 Oct, 2015

2. Java version used:
build 1.8.0_60

3. Precise command-line compilation examples / instructions:
> javac MyListener.java

4. Precise examples / instructions to run this program:
In separate shell windows:
> java MyListener

5. List of files needed for running the program.
 a. MyListener.class

6. Notes:
 How to user it?
 1) run java MyListener
 2) open a web browser, input any request to localhost:2540, for example
 http://localhost:2540/elliott/dog.txt

You will see the request both in listener console and your browser.

----------------------------------------------------------*/
import java.io.*;
import java.net.*;
import java.util.*;

/**
 * This listener monitor the port 2540, accept any request from client(web browser).
 * Then, it prints all of the contents of the request to console and return them
 * to the web browser.
 */
public class MyListener {

	public static void main(String args[]){
		// The maximum queue length for incoming connection
		int queue_len = 6;
		// Port number for general requests(joke or proverb)
		int port = 2540;
		// A reference of the client socket
		Socket socket;

		try{
			// Setup the server socket
			ServerSocket servsocket = new ServerSocket(port, queue_len);
			System.out.println("");
			System.out.println("Rong Zhuang's Port Listener has been started up, listening at port " + port + ".");
			System.out.println("");

			while(true){
				// Make the server socket wait for the next client request
				socket = servsocket.accept();
				// Output stream to the client
				PrintStream printer =  new PrintStream(socket.getOutputStream());
				// Local reader from the client
				BufferedReader reader =new BufferedReader(new InputStreamReader(socket.getInputStream()));

				printer.println("Your Request has been accepted by MyListener:");

				String clientRequest = "";
				while ((clientRequest = reader.readLine()) != null) {
					if (clientRequest.equals("")) { // If the end of the http request, stop
						System.out.println("");
						break;
					}
					printer.println(clientRequest);
					System.out.println(clientRequest);
				}

				socket.close();
			}
		}
		catch(IOException ex){
			//Handle the exception
			System.out.println(ex);
		}
		finally {
			System.out.println("Port Listener has been shutdown!");
		}
	}
}
