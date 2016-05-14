<%@page import="Johnny.Dao.UserDao"%>
<%@page import="Johnny.Beans.User"%>
<%@page import="Johnny.Common.Constants"%>
<%@page import="Johnny.Common.Helper"%>

<jsp:include page="layout_top.jsp" />
<jsp:include page="layout_header.jsp" />
<%
    String errmsg = "";
    
    String username = "";
    String password = "";
    String usertype = "";
        
    Helper helper = new Helper(request);
    
    if ("GET".equalsIgnoreCase(request.getMethod())) {
        
    } else {
        username = request.getParameter("username");
        password = request.getParameter("password");
        usertype = request.getParameter("usertype");

        UserDao dao = UserDao.createInstance();
        User user = dao.getUser(username);
        if(user!=null){
            String user_password = user.getPassword();
            String user_usertype = user.getUsertype();
            if (password.equals(user_password)&&usertype.equals(user_usertype)) {
                session.setAttribute(helper.SESSION_USERNAME, user.getName());
                session.setAttribute(helper.SESSION_USERTYPE, user.getUsertype());
                response.sendRedirect("index.jsp");
                return;
            }
        } 
        errmsg = "Login failed! <br>Please check your username, password and user type!";
    }
    
    if (errmsg.isEmpty()) {
        if(session.getAttribute(helper.SESSION_LOGIN_MSG)!=null){
            errmsg = session.getAttribute(helper.SESSION_LOGIN_MSG) + "";
            session.removeAttribute(helper.SESSION_LOGIN_MSG);
        }
    }
%>
<jsp:include page="layout_menu.jsp" />
<section id="content">
  <div class="post">
    <h3 style='color:red'><%=errmsg %></h3>   
    <form action="account_login.jsp" method="Post">
      <table style='width:50%'>
        <tr><td><h5>User Name:</h5></td><td><input type='text' name='username' value='<%=username%>' class='input' required /></td></tr>
        <tr><td><h5>Password:</h5></td><td><input type='password' name='password' value='<%=password%>' class='input' required /></td></tr>
        <tr><td><h5>User Type</h5></td><td><select name='usertype' class='input'><option value='customer' selected>Customer</option><option value='storemanager'>Store Manager</option><option value='salesman'>Salesman</option></select></td></tr>
        <tr><td colspan="2"><input name="login" class="formbutton" value="Login" type="submit" /></td></tr>
        <tr><td colspan="2"><strong><a class='' href='account_register.jsp'>New User? Register here!</a></strong></td></tr>
      </table>
    </form>
  </div>
</section>
<jsp:include page="layout_footer.jsp" />