import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;

public class ShowMessage extends HttpServlet {
  protected String message;
	public void init() {
		message = getInitParameter("message");
	}

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
				"<HEAD><TITLE>Show Message</TITLE></HEAD>\n" +
				"<BODY>\n" +
				"<H1>The ShowMessage Servlet</H1>\n" +
				getMessage(20, message) + "\n" +
				"</BODY></HTML>");
	}

	private String getMessage(int count, String message) {
		if (message == null || message.isEmpty()) {
			return "No Message.";
		}
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < count; i++) {
			sb.append("<p>");
			sb.append(message);
			sb.append("</p>");
		}
		return sb.toString();
	}
}
