/* Handler.java 1.0
Clark Elliott MIMER shim java example

Capture Environment Variables passed from .bat file through java.exe.

Assuming the first argument is a valid file name, read five lines
of data from the file, and display the data on the console.

Note that NO XML is used in this preliminary program, although some
variable names refer to XML for consistency with other programs
in this assignment.

Here is the DOS .bat file to run this Java program:
rem This is shim.bat
rem Have to set classpath in batch, passing as arg does not work:
set classpath=%classpath%;c:/dp/435/java/mime-xml/
rem Pass the name of the first argument to java:
java -Dfirstarg="%1" Handler

To run:

> shim mimer-data.xyz

...where mimer-data.xyz has five lines of ascii data in it.

*/

import java.io.*;
import java.util.*;

class MyDataArray {
	int num_lines = 0;
	String[] lines = new String[8];
}

public class Handler {
	private static String XMLfileName = "C:\\temp\\mimer.output";
	private static PrintWriter      toXmlOutputFile;
	private static File             xmlFile;
	private static BufferedReader   fromMimeDataFile;

	public static void main(String args[]) {
		int i = 0;
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		MyDataArray da = new MyDataArray();

		try {
			System.out.println("Executing the java application.");
			System.out.flush();

			Properties p = new Properties(System.getProperties());

			String argOne = p.getProperty("firstarg");
			System.out.println("First var is: " + argOne);

			fromMimeDataFile = new BufferedReader(new FileReader(argOne));
			// Only allows for five lines of data in input file plus safety:
			while(((da.lines[i++] = fromMimeDataFile.readLine())!= null) && i < 8){
				System.out.println("Data is: " + da.lines[i-1]);
			}
			da.num_lines = i - 1;
			System.out.println("i is: " + i);

			xmlFile = new File(XMLfileName);
			if (xmlFile.exists() == true && xmlFile.delete() == false){
				throw (IOException) new IOException("XML file delete failed.");
			}
			xmlFile = new File(XMLfileName);
			if (xmlFile.createNewFile() == false){
				throw (IOException) new IOException("XML file creation failed.");
			}
			else{
				toXmlOutputFile = new PrintWriter(new BufferedWriter(new FileWriter(XMLfileName)));
				toXmlOutputFile.println("First arg to Handler is: " + argOne + "\n");
				toXmlOutputFile.println("<This where the persistent XML will go>");
				toXmlOutputFile.close();
			}
		}
		catch (Throwable e) {
			e.printStackTrace();
		}
	}
}
