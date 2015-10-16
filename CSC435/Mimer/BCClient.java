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
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

class myDataArray {
  int num_lines = 0;
  String[] lines = new String[8];
}

public class BCClient{
  private static String XMLfileName = "C:\\temp\\mimer.output";
  private static PrintWriter      toXmlOutputFile;
  private static File             xmlFile;
  
  public static void main (String args[]) {
    String serverName;
    String argOne = "WillBeFileName";
    if (args.length < 1) serverName = "localhost";
    else serverName = args[0];
    XStream xstream = new XStream();
    String[] testLines = new String[4];  int i;
    myDataArray da = new myDataArray();
    myDataArray daTest = new myDataArray();
    
    System.out.println("Clark Elliott's back channel Client.\n");
    System.out.println("Using server: " + serverName + ", Port: 2540 / 2570");
    BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
    try {
      String userData;
      do {
	System.out.print
	  ("Enter a string to send to back channel of webserver, (quit) to end: ");
	System.out.flush ();
	userData = in.readLine ();
	da.lines[0] = "You "; da.lines[1] = "typed "; da.lines[2] = userData;
	da.num_lines = 3;
	String xml = xstream.toXML(da);
	if (userData.indexOf("quit") < 0){
	  sendToBC(xml, serverName);

	  System.out.println("\n\nHere is the XML version:");
	  System.out.print(xml);
	  
	  daTest = (myDataArray) xstream.fromXML(xml); // deserialize data
	  System.out.println("\n\nHere is the deserialized data: ");
	  for(i=0; i < daTest.num_lines; i++){System.out.println(daTest.lines[i]);}
	  System.out.println("\n");

	  xmlFile = new File(XMLfileName);
	  if (xmlFile.exists() == true && xmlFile.delete() == false){
	    throw (IOException) new IOException("XML file delete failed.");
	  }
	  xmlFile = new File(XMLfileName);
	  if (xmlFile.createNewFile() == false){
	    throw (IOException) new IOException("XML file creation failed.");
	  }
	  else{
	    toXmlOutputFile = 
	      new PrintWriter(new BufferedWriter(new FileWriter(XMLfileName)));
	    toXmlOutputFile.println("First arg to Handler is: " + argOne + "\n");
	    toXmlOutputFile.println(xml);
	    toXmlOutputFile.close();
	  }
	}
      } while (userData.indexOf("quit") < 0);
      System.out.println ("Cancelled by user request.");
      

    } catch (IOException x) {x.printStackTrace ();}
  }


  
  static void sendToBC (String sendData, String serverName){
    Socket sock;
    BufferedReader fromServer;
    PrintStream toServer;
    String textFromServer;
    try{
      // Open our connection Back Channel on server:
      sock = new Socket(serverName, 2570);
      toServer   = new PrintStream(sock.getOutputStream());
      // Will be blocking until we get ACK from server that data sent
      fromServer = 
	new  BufferedReader(new InputStreamReader(sock.getInputStream()));
      
      toServer.println(sendData);
      toServer.println("end_of_xml");
      toServer.flush(); 
      // Read two or three lines of response from the server,
      // and block while synchronously waiting:
      System.out.println("Blocking on acknowledgment from Server... ");
      textFromServer = fromServer.readLine();
      if (textFromServer != null){System.out.println(textFromServer);}
      sock.close();
    } catch (IOException x) {
      System.out.println ("Socket error.");
      x.printStackTrace ();
    }
  }
}
