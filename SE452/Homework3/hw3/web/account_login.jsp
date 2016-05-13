<%@page import="Johnny.Dao.UserDao"%>
<%@page import="Johnny.Beans.User"%>
<%@page import="Johnny.Common.Constants"%>
<%@page import="Johnny.Common.Helper"%>

<jsp:include page="layout_top.jsp" />
<jsp:include page="layout_header.jsp" />
<jsp:include page="layout_menu.jsp" />
<%
    String error_msg = "";
    Helper helper = new Helper(request, response.getWriter());
    
    if ("GET".equalsIgnoreCase(request.getMethod())) {
        
    } else {         
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String usertype = request.getParameter("usertype");

        UserDao dao = new UserDao();
        User user = dao.getUser(username);
        if(user!=null){
            String user_password = user.getPassword();
            String user_usertype = user.getUsertype();
            if (password.equals(user_password)&&usertype.equals(user_usertype)) {
                HttpSession session2 = request.getSession(true);
                session2.setAttribute(helper.SESSION_USERNAME, user.getName());
                session2.setAttribute(helper.SESSION_USERTYPE, user.getUsertype());
                response.sendRedirect("index.jsp");
                return;
            }
        } else {
            error_msg = "<h3 style='color:red'>Login failed! <br>Please check your username, password and user type!</h3>";
        }
    }
    
    String errmsg = error_msg;
    HttpSession session2 = request.getSession(true);
    if(session.getAttribute(helper.SESSION_LOGIN_MSG)!=null){
        errmsg = "<h3 style='color:red'>" + session.getAttribute(helper.SESSION_LOGIN_MSG) + "</h3>";
        session.removeAttribute(helper.SESSION_LOGIN_MSG);
    }
%>
<section id="content">
  <div class="post">
    <%=errmsg %>
    <form action="account_login.jsp" method="Post">
      <table style='width:50%'>
        <tr><td><h5>User Name:</h5></td><td><input type='text' name='username' value='' class='input' required /></td></tr>
        <tr><td><h5>Password:</h5></td><td><input type='password' name='password' value='' class='input' required /></td></tr>
        <tr><td><h5>User Type</h5></td><td><select name='usertype' class='input'><option value='customer' selected>Customer</option><option value='storemanager'>Store Manager</option><option value='salesman'>Salesman</option></select></td></tr>
        <tr><td colspan="2"><input name="login" class="formbutton" value="Login" type="submit" /></td></tr>
        <tr><td colspan="2"><strong><a class='' href='account_register.jsp'>New User? Register here!</a></strong></td></tr>
      </table>
    </form>
  </div>
</section>

<jsp:include page="layout_footer.jsp" />