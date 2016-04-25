import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/UserEdit")
public class UserEdit extends HttpServlet {
	private String error_msg = "";
	private HashMap<String, Console> consoles;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		error_msg = "";
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		displayUser(request, response, pw, false);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		error_msg = "";
		response.setContentType("text/html");
		PrintWriter pw = response.getWriter();
		Helper helper = new Helper(request, pw);

		String usertype = request.getParameter("usertype");
		String name = request.getParameter("name");
		String password = request.getParameter("password");

		if(name == null){
			error_msg = "Name can't be empty!";
		}else	if(password == null){
			error_msg = "Password can't be empty!";
		}else	if(usertype == null){
			error_msg = "User Type can't be empty!";
		}

		if (!error_msg.isEmpty()) {
			displayUser(request, response, pw, true);
			return;
		}

		HashMap<String, User> userlist = helper.getUsers(usertype);
		User userobj = userlist.get(name);
		if (userobj == null) {
			error_msg = "User ["+name+"] does not exist!";
		}	else{
			userobj.setPassword(password);
			userobj.setUsertype(usertype);
			error_msg = "User ["+name+"] is updated!";
			System.out.println(error_msg);
		}

		displayUser(request, response, pw, true);
	}

	protected void displayUser(HttpServletRequest request,
			HttpServletResponse response, PrintWriter pw, boolean error)
			throws ServletException, IOException {

		Helper helper = new Helper(request,pw);
		if(!helper.isLoggedin()){
			HttpSession session = request.getSession(true);
			session.setAttribute("login_msg", "Please login to manage user!");
			response.sendRedirect("Login");
			return;
		}
		String sessionusertype = helper.usertype();
		String errmsg = "";
		if (sessionusertype==null || !sessionusertype.equals(UserHashMap.CONST_TYPE_SALESMAN)) {
			errmsg = "You have no authorization to manage user!";
		}

		String usertype = request.getParameter("usertype");
		String name = request.getParameter("name");

		if (usertype==null||name==null) {
			usertype="";
			name="";
			errmsg = "<h3 style='color:red'>Invalid Paramters!</h3>";
		}
		String[][] arr = new String[3][2];
		arr[0][0] = "customer";
		arr[0][1] = "Customer";
		arr[1][0] = "storemanager";
		arr[1][1] = "Store Manager";
		arr[2][0] = "salesman";
		arr[2][1] = "Salesman";
		String strOptionlist = "<tr><td><h5>User Type:</h5></td><td><select name='usertype' class='input' disabled>";
		for (int i = 0; i < arr.length; i++) {
			if (arr[i][0].equals(usertype.toLowerCase())) {
				strOptionlist += "<option value='"+arr[i][0]+"' selected>"+arr[i][1]+"</option>";
			} else {
				strOptionlist += "<option value='"+arr[i][0]+"'>"+arr[i][1]+"</option>";
			}
		}
		strOptionlist += "</select></td></tr>";

		User userobj = helper.getUser(usertype, name);
		helper.prepareLayout();
		helper.prepareHeader();
		helper.prepareMenu();
		if (error) {
			errmsg = "<h3 style='color:red'>"+error_msg+"</h3>";
		}
		String template = helper.getTemplate("user_edit.html");
		template = template.replace("$errmsg$", errmsg);
		template = template.replace("$option$", strOptionlist);
		if (userobj != null) {
			template = template.replace("$usertype$", usertype).
													replace("$name$", name)
													.replace("$password$", userobj.getPassword());
		}
		helper.prepareContent(template);
		helper.prepareSideBar();
		helper.prepareFooter();
		helper.printHtml();
	}
}
