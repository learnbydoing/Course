<%@page import="Johnny.Dao.UserDao"%>
<%@page import="Johnny.Common.Constants"%>
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
    String usertype = helper.usertype();
    String errmsg = "";
    if (usertype==null || !usertype.equals(Constants.CONST_TYPE_SALESMAN_LOWER)) {
        errmsg = "You have no authorization to manage user!";
    }
    
    if (errmsg.isEmpty()) {
        String username = request.getParameter("username");

        UserDao dao = UserDao.createInstance();
        if (dao.isExisted(username)) {
            dao.deleteUser(username);
            response.sendRedirect("admin_userlist.jsp");
        } else {
            errmsg = "No User found!";
        }
    }
    pageContext.setAttribute("errmsg", errmsg);
%>
<jsp:include page="layout_menu.jsp" />
<section id="content">
  <div>
    <h3>Delete User</h3>
    <h3 style='color:red'>${errmsg}</h3>    
  </div>
</section>
<jsp:include page="layout_sidebar.jsp" />
<jsp:include page="layout_footer.jsp" />
