<%@page import="Johnny.Common.Constants"%>
<%@page import="Johnny.Dao.UserDao"%>
<%@page import="Johnny.Beans.User"%>
<%@page import="Johnny.Common.Helper"%>
<jsp:include page="layout_top.jsp" />
<jsp:include page="layout_header.jsp" />
<jsp:include page="layout_menu.jsp" />
<%
    Helper helper = new Helper(request,response.getWriter());
    if(!helper.isLoggedin()){
        session.setAttribute(helper.SESSION_LOGIN_MSG, "Please login first!");
        response.sendRedirect("account_login.jsp");
        return;
    }
    String currentusertype = helper.usertype();
    String errmsg = "";
    if (currentusertype==null || !currentusertype.equals(Constants.CONST_TYPE_SALESMAN_LOWER)) {
        errmsg = "You have no authorization to manage user!";
    }
    
    String name = request.getParameter("name");
    String usertype = "";
    String password = "";
    
    if (name==null||name.isEmpty()) {
        errmsg = "<h3 style='color:red'>Invalid Paramters!</h3>";
    }
    
    String[][] arr = new String[3][2];
    arr[0][0] = Constants.CONST_TYPE_CUSTOMER_LOWER;
    arr[0][1] = Constants.CONST_TYPE_CUSTOMER;;
    arr[1][0] = Constants.CONST_TYPE_STOREMANAGER_LOWER;
    arr[1][1] = Constants.CONST_TYPE_STOREMANAGER;
    arr[2][0] = Constants.CONST_TYPE_SALESMAN_LOWER;
    arr[2][1] = Constants.CONST_TYPE_SALESMAN;
    String selector = "<select name='usertype' class='input' disabled>";
    for (int i = 0; i < arr.length; i++) {
        if (usertype != null && arr[i][0].equals(usertype.toLowerCase())) {
            selector += "<option value='"+arr[i][0]+"' selected>"+arr[i][1]+"</option>";
        } else {
            selector += "<option value='"+arr[i][0]+"'>"+arr[i][1]+"</option>";
        }
    }
    selector += "</select>";
    
    if (errmsg.isEmpty()) {
        UserDao dao = new UserDao();
        User userobj = dao.getUser(name);
        
        if (userobj == null) {
            errmsg = "User ["+name+"] does not exist!";
        } else {
            usertype = userobj.getUsertype();
            if ("GET".equalsIgnoreCase(request.getMethod())) {                
                password = userobj.getPassword();
            } else {
                password = request.getParameter("password");
                if(password == null||password.isEmpty()){
                    errmsg = "Password can't be empty!";
                }
                if (errmsg.isEmpty()) {
                    userobj.setPassword(password);
                    errmsg = "User ["+name+"] is updated!";
                }
            }
        }
    }
%>
<section id="content">
  <div>
    <h3>Edit User</h3>
    <h3 style='color:red'><%=errmsg%></h3>
    <form action="admin_useredit.jsp" method="Post">
      <input type='hidden' name='usertype' value='<%=usertype%>'>
      <input type='hidden' name='name' value='<%=name%>'>
      <table style='width:50%'>
        <tr><td><h5>User Type:</h5></td><td><%=selector%></td></tr>
        <tr><td><h5>Name:</h5></td><td><input type='text' name='name' value='<%=name%>' class='input' required disabled/></td></tr>
        <tr><td><h5>Password:</h5></td><td><input type='text' name='password' value='<%=password%>' class='input' required /></td></tr>
        <tr><td colspan="2"><input name="create" class="formbutton" value="Save" type="submit" /></td></tr>
      </table>
    </form>
  </div>
</section>
<jsp:include page="layout_sidebar.jsp" />
<jsp:include page="layout_footer.jsp" />