<%@page import="Johnny.Common.Constants"%>
<%@page import="Johnny.Dao.UserDao"%>
<%@page import="Johnny.Beans.User"%>
<%@page import="Johnny.Common.Helper"%>
<jsp:include page="layout_top.jsp" />
<jsp:include page="layout_header.jsp" />
<%
    Helper helper = new Helper(request);
    helper.setCurrentPage(Constants.CURRENT_PAGE_USERMNG);
    if(!helper.isLoggedin()){
        session.setAttribute(Constants.SESSION_LOGIN_MSG, "Please login first!");
        response.sendRedirect("account_login.jsp");
        return;
    }
    String currentusertype = helper.usertype();
    String errmsg = "";
    if (currentusertype==null || !currentusertype.equals(Constants.CONST_TYPE_SALESMAN_LOWER)) {
        errmsg = "You have no authorization to manage user!";
    }
    
    String name = "";
    String password = "";
    String usertype = "";
    
    if (errmsg.isEmpty()) {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            name = "Name1";
            password = "123456";
        } else {
            name = request.getParameter("name");
            password = request.getParameter("password");
            usertype = request.getParameter("usertype");

            if(name == null){
                errmsg = "Name can't be empty!";
            }else if(password == null){
                errmsg = "Password can't be empty!";
            }else if(usertype == null){
                errmsg = "User Type can't be empty!";
            }

            if (errmsg.isEmpty()) {
                UserDao dao = UserDao.createInstance();
                if(dao.isExisted(name)) {
                    errmsg = "User ["+name+"] already exist!";
                } else{
                    User user = new User(name, password, usertype);
                    dao.addUser(user);
                    errmsg = "User ["+name+"] is created!";
                }
            }
        }
    }
%>
<jsp:include page="layout_menu.jsp" />
<section id="content">
  <div>
    <h3>Add User</h3>
    <h3 style='color:red'><%=errmsg%></h3>
    <form action="admin_useradd.jsp" method="Post">
      <table style='width:50%'>
        <tr><td><h5>Maker:</h5></td><td><select name='usertype' class='input'><option value='customer' selected>Customer</option><option value='storemanager'>Store Manager</option><option value='salesman'>Salesman</option></select></td></tr>
        <tr><td><h5>Name:</h5></td><td><input type='text' name='name' value='<%=name%>' class='input' required /></td></tr>
        <tr><td><h5>Password:</h5></td><td><input type='text' name='password' value='<%=password%>' class='input' required /></td></tr>
        <tr><td colspan="2"><input name="create" class="formbutton" value="Create" type="submit" /></td></tr>
      </table>
    </form>
  </div>
</section>
<jsp:include page="layout_sidebar.jsp" />
<jsp:include page="layout_footer.jsp" />