<%@page import="Johnny.Common.Helper"%>
<jsp:include page="layout_top.jsp" />
<jsp:include page="layout_header.jsp" />
<%
    String err_msg = "";
    Helper helper = new Helper(request);
    
    if ("GET".equalsIgnoreCase(request.getMethod())) {
        
    } else {         
        String address = request.getParameter("address");
        String creditcard = request.getParameter("creditcard");
        if (address == null || address.isEmpty()) {
            err_msg = "Address can't be empty!";
        } else if (creditcard == null || creditcard.length() != 16) {
            err_msg = "Credit card can't be empty and must be 16 numbers length!";
        }

        if (err_msg.isEmpty()) {
            request.setAttribute("address", address);
            request.setAttribute("creditcard", creditcard);
            RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher("/orderplace.jsp");
            dispatcher.forward(request, response);
        }
    }
%>
<jsp:include page="layout_menu.jsp" />
<section id="content">
  <div class="post">
    <h3>Provide your address and credit card</h3>
    <%=err_msg%>
    <form action="checkout.jsp" method="Post">
      <table style='width:55%'>
          <tr><td>Full Name:</td><td><input type='text' name='username' value='<%=helper.username()%>' required disabled/></td></tr>
        <tr><td>Address:</td><td><input type='text' name='address' value='1st Jackson Ave,Chicago,IL' required /></td></tr>
        <tr><td>Credit Card Number</td><td><input type='text' name='creditcard' value='3101122033287498' required /></td></tr>
        <tr><td><a href='mycart.jsp' class='button2'>Back to cart</a></td><td><input name="create" value="Place Order" type="submit" class="formbutton" /></td></tr>
      </table>	  
    </form>
  </div>
</section>
<jsp:include page="layout_sidebar.jsp" />
<jsp:include page="layout_footer.jsp" />