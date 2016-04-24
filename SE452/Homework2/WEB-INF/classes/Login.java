import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Login")
public class Login extends HttpServlet {

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();

		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String usertype = request.getParameter("usertype");

		HashMap<String, User> hm = new HashMap<String, User>();
		if (usertype.equals("customer")) {
			hm.putAll(UserHashMap.customer);
		} else if (usertype.equals("storemanager")) {
			hm.putAll(UserHashMap.storemanager);
		} else if (usertype.equals("salesman")) {
			hm.putAll(UserHashMap.salesman);
		}
		User user = hm.get(username);
		if(user!=null){
			String user_password = user.getPassword();
			if (password.equals(user_password)) {
				HttpSession session = request.getSession(true);
				session.setAttribute("username", user.getName());
				session.setAttribute("usertype", user.getUsertype());
				response.sendRedirect("Home");
				return;
			}
		}
		displayLogin(request, response, pw, true);
	}

	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		displayLogin(request, response, pw, false);
	}

	protected void displayLogin(HttpServletRequest request,
			HttpServletResponse response, PrintWriter pw, boolean error)
			throws ServletException, IOException {

		Helper helper = new Helper(request,pw);
		helper.prepareLayout();
		helper.prepareHeader();
		helper.prepareMenu();
		String errmsg = "";
		if (error) {
			errmsg = "<h3 style='color:red'>Please check your username, password and user type!</h3>";
		}
		HttpSession session = request.getSession(true);
		if(session.getAttribute("login_msg")!=null){
			errmsg = "<h3 style='color:red'>" + session.getAttribute("login_msg") + "</h3>";
			session.removeAttribute("login_msg");
		}
		String template = helper.getTemplate("account_login.html");
		template = template.replace("$errmsg$", errmsg);
		helper.prepareContent(template);
		//helper.prepareSideBar();
		helper.prepareFooter();
		helper.printHtml();
	}
}
