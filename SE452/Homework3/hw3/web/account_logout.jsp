<%@page import="Johnny.Common.Helper"%>
<%
    Helper helper = new Helper(request, null);
    helper.logout();
    response.sendRedirect("index.jsp");
%>
