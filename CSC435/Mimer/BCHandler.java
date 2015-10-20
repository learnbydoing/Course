/*--------------------------------------------------------
1. Name / Date:
Rong Zhuang
15 Oct, 2015

2. Java version used:
build 1.8.0_60

3. Precise command-line compilation examples / instructions:
> jbch.bat
rem javac -cp "%JAVA_HOME%\lib\xstream-1.2.1.jar;%JAVA_HOME%\lib\xpp3_min-1.1.3.4.O.jar" BCHandler.java

Requires the Xstream libraries contained in .jar files to compile, AND to run.
http://condor.depaul.edu/elliott/435/hw/programs/mimer/xstream-1.2.1.jar
http://condor.depaul.edu/elliott/435/hw/programs/mimer/xpp3_min-1.1.3.4.O.jar

For more details, see: http://www.studytrails.com/java/xml/xstream/xstream-xml-to-java.jsp

4. Precise examples / instructions to run this program:
It is invoked by shim.bat with providing file path to it. If everything is
configured well, you can open link http://localhost:2540/mimer-data.xyz to see
the result.

5. List of files needed for running the program.
 a. shim.bat
 b. BCHandler.class

6. Notes:
 a. This BCHandler will open the specified file and convert the content to xml
    format and send to back channel of server at port: 2540.
----------------------------------------------------------*/
import java.io.*;
import java.net.*;
import java.util.*;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

/**
 * MyDataArray acts as a data model which is used to store data. Here, it stores
 * the content of a file.
 */
class MyDataArray {
	int num_lines = 0;
	String[] lines = new String[8]; // size is 8 by default
}

/**
 * BCHandler is the default program to handle '.xyf' files. Firstly, it openes
 * the file with the given path, then convert it to XML format, print to screen;
 * secondly, it sends the xml content to the back channel of MyWebServer.
 */
public class BCHandler {
	private static final String XMLfileName = "C:\\temp\\mimer.output";
	private static final int PORT_NUMBER = 2570;

	public static void main (String args[]) {
		String serverName;
		String filePath = ""; // Assigned by shim.bat, param: firstarg
		if (args.length < 1) {
			serverName = "localhost";
		}
		else {
			serverName = args[0];
		}

		System.out.println("\n> Rong Zhuang's Back Channel Handler is working...");

		try {
			// Get environment variable for file path
			Properties p = new Properties(System.getProperties());
			filePath = p.getProperty("firstarg");
			System.out.println("The file path is: " + filePath);

			// Create the data model
			MyDataArray da = new MyDataArray();

			// Read lines one by one from file, maximum 8 lines
			int i = 0;
			String strLine = "";
			BufferedReader fromMimeDataFile = new BufferedReader(new FileReader(filePath));
			while(((strLine = fromMimeDataFile.readLine())!= null) && i < 8){
				da.lines[i] = strLine;
				System.out.println("Data is: " + da.lines[i]);
				i++;
			}
			da.num_lines = i;
			System.out.println("Totoally " + da.num_lines + " lines in the file.");

			// Create XStream to work with xml
			XStream xstream = new XStream();
			// Serialize object to xml
			String xml = xstream.toXML(da);

			System.out.println("\n> Here is the XML content:");
			// Print the xml content
			System.out.print(xml);

			System.out.print("\n\n> Write mimer data to local disk:" + XMLfileName);
			// Try to delete the file if exists
			File xmlFile = new File(XMLfileName);
			if (xmlFile.exists() == true && xmlFile.delete() == false) {
				throw (IOException) new IOException("XML file delete failed.");
			}
			// Try to create the file
			xmlFile = new File(XMLfileName);
			if (xmlFile.createNewFile() == false) { // Fail to create
				throw (IOException) new IOException("XML file creation failed.");
			}
			else { //Success to create
				PrintWriter toXmlOutputFile = new PrintWriter(new BufferedWriter(new FileWriter(XMLfileName)));
				// Write content to file
				toXmlOutputFile.println("First arg to Handler is: " + XMLfileName + "\n");
				toXmlOutputFile.println(xml);
				toXmlOutputFile.close();
			}

			// Send to server and wait for acknowledgment
			System.out.println("\n\n> Send to server...");
			sendToServer(xml, serverName, PORT_NUMBER);

		}
		catch (IOException x) {
			x.printStackTrace ();
		}
	}

	static void sendToServer (String xmldata, String serverName, int port){
		try{
			// Open connection to connect Back Channel on server at port 2570
			Socket sock = new Socket(serverName, port);
			PrintStream toServer = new PrintStream(sock.getOutputStream());
			BufferedReader fromServer = new BufferedReader(new InputStreamReader(sock.getInputStream()));
			System.out.println("Create connection to server: " + serverName + " at Port: " + port);

			// Send xml data
			toServer.println(xmldata);
			// Send end indicator
			toServer.println("end_of_xml");
			toServer.flush();

			System.out.println("Xml data has been sent to Back Channel, waiting acknowledgment from server... ");
			// Wait response from the server, and block while synchronously waiting
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
