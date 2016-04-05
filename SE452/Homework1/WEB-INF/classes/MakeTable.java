import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class MakeTable extends HttpServlet {
	public void doGet(HttpServletRequest request,
		HttpServletResponse response)
		throws ServletException, IOException {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			String docType =
				"<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 " +
				"Transitional//EN\">\n";
				out.println(docType +
				"<HTML>\n" +
				"<HEAD><TITLE>Make Table</TITLE></HEAD>\n" +
				"<BODY>\n" +
				"<H1>Make Table</H1>\n" +
				getTable(25, 3) + "\n" +
				"</BODY></HTML>");
	}

	private String getTable(int row, int col) {
		StringBuilder sb = new StringBuilder();
		sb.append("<table border=\"1\">");
		for (int i = 0; i < row; i++) {
			sb.append("<tr>");
			for (int j = 0; j < col; j++) {
				sb.append("<td>Row " + String.valueOf(i + 1) + ", Column " + String.valueOf(j + 1) + "</td>");
			}
			sb.append("</tr>");
		}
		sb.append("</table>");
		return sb.toString();
	}
}
