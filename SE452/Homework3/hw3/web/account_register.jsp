<%@page import="Johnny.Dao.UserDao"%>
<%@page import="Johnny.Beans.User"%>
<%@page import="Johnny.Common.Constants"%>
<%@page import="Johnny.Common.Helper"%>

<jsp:include page="layout_top.jsp" />
<jsp:include page="layout_header.jsp" />
<jsp:include page="layout_menu.jsp" />
<section id="content">
  <div class="post">
    <%
        String error_msg = null;
        
        if ("GET".equalsIgnoreCase(request.getMethod())) {

        } else { 
            Helper helper = new Helper(request, response.getWriter());
            String username = request.getParameter("username");
            String password = request.getParameter("password");
            String repassword = request.getParameter("repassword");
            String usertype = Constants.CONST_TYPE_CUSTOMER;
            if(!helper.isLoggedin()) {
                usertype = request.getParameter("usertype");
            }

            if(!password.equals(repassword)){
                error_msg = "Password and Re-Password doesn't match!";
            } else {
                UserDao dao = new UserDao();
                User user = dao.getUser(username);
                if(user!=null) {
                    error_msg = "Username ["+user.getName()+"] already exist as " + user.getUsertype();
                } else {
                    User newuser = new User(username,password,usertype);
                    dao.addUser(newuser);
                    HttpSession session2 = request.getSession(true);
                    session2.setAttribute(helper.SESSION_LOGIN_MSG, "Your "+usertype+" account has been created. Please login");
                    if(!helper.isLoggedin()){
                        response.sendRedirect("account_login.jsp"); return;
                    } else {
                        response.sendRedirect("index.jsp"); return;
                    }
                }
            }
        }
        String errmsg = "";
        if (error_msg != null && !error_msg.isEmpty()) {
            errmsg = "<h3 style='color:red'>"+error_msg+"</h3>";
        }
    %>
    <%= errmsg %>
    <form action="account_register.jsp" method="Post">
      <table style='width:50%'>
        <tr><td><h5>User Name:</h5></td><td><input type='text' name='username' value='' class='input' required /></td></tr>
        <tr><td><h5>Password:</h5></td><td><input type='password' name='password' value='' class='input' required /></td></tr>
        <tr><td><h5>Re-Password:</h5></td><td><input type='password' name='repassword' value='' class='input' required /></td></tr>
        <tr><td><h5>User Type</h5></td><td><select name='usertype' class='input'><option value='customer' selected>Customer</option><option value='storemanager'>Store Manager</option><option value='salesman'>Salesman</option></select></td></tr>
        <tr><td colspan="2"><input name="register" class="formbutton" value="Register" type="submit" /></td></tr>
      </table>
    </form>
  </div>
</section>

<jsp:include page="layout_footer.jsp" />