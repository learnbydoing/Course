/* file is: BCClient.java   5-5-07  1.0

For use with webserver back channel. Written for Windows.

This program may contain bugs. Note: version 1.0.

To compile:

rem jcxclient.bat
rem java compile BCClient.java with xml libraries...
rem Here are two possible ways to compile. Uncomment one of them:
rem set classpath=%classpath%C:\dp\435\java\mime-xml\;c:\Program Files\Java\jdk1.5.0_05\lib\xstream-1.2.1.jar;c:\Program Files\Java\jdk1.5.0_05\lib\xpp3_min-1.1.3.4.O.jar;
rem javac -cp "c:\Program Files\Java\jdk1.5.0_05\lib\xstream-1.2.1.jar;c:\Program Files\Java\jdk1.5.0_05\lib\xpp3_min-1.1.3.4.O.jar" BCClient.java

Note that both classpath mechanisms are included. One should work for you.

Requires the Xstream libraries contained in .jar files to compile, AND to run.
See: http://xstream.codehaus.org/tutorial.html


To run:

rem rxclient.bat
rem java run BCClient.java with xml libraries:
set classpath=%classpath%C:\dp\435\java\mime-xml\;c:\Program Files\Java\jdk1.5.0_05\lib\xstream-1.2.1.jar;c:\Program Files\Java\jdk1.5.0_05\lib\xpp3_min-1.1.3.4.O.jar;
java BCClient

This is a standalone program to connect with MyWebServer.java through a
back channel maintaining a server socket at port 2570.

----------------------------------------------------------------------*/
import java.io.*;  // Get the Input Output libraries
import java.net.*; // Get the Java networking libraries
import java.util.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

class MyDataArray {
	int num_lines = 0;
	String[] lines = new String[8];
}

public class BCHandler {
	private static final int PORT_NUMBER = 2570;
	private static final String XMLfileName = "C:\\temp\\mimer.output";

	public static void main (String args[]) {
		String serverName;
		String argOne = "WillBeFileName";
		if (args.length < 1) {
			serverName = "localhost";
		}
		else {
			serverName = args[0];
		}

		System.out.println("Rong Zhuang's Back Channel Handler.\n");
		System.out.println("Using server: " + serverName + ", Port: " + PORT_NUMBER);
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));

		try {
			Properties p = new Properties(System.getProperties());
			argOne = p.getProperty("firstarg");
			System.out.println("First var is: " + argOne);

			int i = 0;
			MyDataArray da = new MyDataArray();
			BufferedReader fromMimeDataFile = new BufferedReader(new FileReader(argOne));
			// Only allows for five lines of data in input file plus safety:
			while(((da.lines[i++] = fromMimeDataFile.readLine())!= null) && i < 8){
				System.out.println("Data is: " + da.lines[i-1]);
			}
			da.num_lines = i - 1;
			System.out.println("i is: " + i);

			XStream xstream = new XStream();
			// Serialize object to xml
			String xml = xstream.toXML(da);

			System.out.println("\n\nHere is the XML content:");
			// Print the xml content
			System.out.print(xml);

			System.out.println("Send to server...");
			// Send to server and wait for acknowledgment
			sendToServer(xml, serverName);
		}
		catch (IOException x) {
			x.printStackTrace ();
		}
	}

	static void sendToServer (String sendData, String serverName){
		Socket sock;
		BufferedReader fromServer;
		PrintStream toServer;

		try{
			// Open connection Back Channel on server at port 2570:
			sock = new Socket(serverName, PORT_NUMBER);
			toServer = new PrintStream(sock.getOutputStream());
			fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));

			// Send data
			toServer.println(sendData);
			// Send end indicator
			toServer.println("end_of_xml");
			toServer.flush();

			System.out.println("Blocking on acknowledgment from Server... ");
			// Wait response from the server, and block while synchronously waiting:
			String textFromServer = fromServer.readLine();
			if (textFromServer != null) {
				// Print the acknowledgment from server
				System.out.println(textFromServer);
			}
			sock.close();
		} catch (IOException x) {
			System.out.println ("Socket error.");
			x.printStackTrace ();
		}
	}
}
