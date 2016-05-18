<%@page import="java.util.List"%>
<%@page import="Johnny.Beans.Order"%>
<%@page import="Johnny.Common.Helper"%>
<%@page import="Johnny.Common.Constants"%>
<%@page import="Johnny.Dao.OrderDao"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="layout_top.jsp" />
<jsp:include page="layout_header.jsp" />
<%
    Helper helper = new Helper(request);
    helper.setCurrentPage(Constants.CURRENT_PAGE_ALLORDERS);
    if(!helper.isLoggedin()){
        session.setAttribute(Constants.SESSION_LOGIN_MSG, "Please login first!");
        response.sendRedirect("account_login.jsp");
        return;
    }
    
    String usertype = helper.usertype();
    String errmsg = "";
    if (usertype==null || !usertype.equals(Constants.CONST_TYPE_SALESMAN_LOWER)) {
        errmsg = "You have no authorization to manage order!";
    }

    OrderDao dao = OrderDao.createInstance();
    List<Order> orders = dao.getOrders();
    if (orders == null || orders.size() == 0) {
        errmsg = "There is no order yet!";
    } else {
        String orderid = request.getParameter("orderid");
        String itemid = request.getParameter("itemid");
        String strtype = request.getParameter("type");
        String strQuantity = request.getParameter("quantity");
        if (orderid!=null && itemid != null && strtype != null && strQuantity != null) {
            int type = 0;
            try {
                type = Integer.parseInt(strtype);
            } catch (NumberFormatException nfe) {

            }            
            int quantity;
            try {
                quantity = Integer.parseInt(strQuantity);
            } catch(NumberFormatException nfe) {
                quantity = 1;
            }
            dao.setItemQuantity(orderid, itemid, type, quantity);
        }
    }
    pageContext.setAttribute("errmsg", errmsg);
    pageContext.setAttribute("orders", orders);
%>
<jsp:include page="layout_menu.jsp" />
<section id='content'>
    <div class='cart'>
        <h3>All Orders</h3>
        <c:choose>
            <c:when test="${not empty errmsg}">
                <h3 style='color:red'><%=errmsg%></h3>    
            </c:when>
            <c:otherwise>
                <c:forEach var="order" items="${orders}">
                    <div class="order_box">
                        <table class="order_table">
                            <tr><td><h5><i>Order Id: </i></h5></td><td><c:out value="${order.id}"/></td>
                                <td>
                                    <form action="ordercancel.jsp" method="Post"> 
                                        <input type="hidden" name="orderid" value="<c:out value="${order.id}"/>">
                                        <input type="submit" value="Cancel Order" class="formbutton" onclick = "return confirm('Are you sure to cancel this order?')">     
                                    </form>
                                </td>
                            </tr>
                            <tr><td><h5><i>Customer Name: </i></h5></td><td><c:out value="${order.userName}"/></td><td></td></tr>
                            <tr><td><h5><i>Address: </i></h5></td><td><c:out value="${order.address}"/></td><td></td></tr>
                            <tr><td><h5><i>Confirmation Number: </i></h5></td><td><c:out value="${order.confirmation}"/></td><td></td></tr>
                            <tr><td><h5><i>Delivery Date: </i></h5></td><td><c:out value="${order.formatDeliveryDate}"/></td><td></td></tr>
                        </table>
                        <table cellspacing='0'>
                            <tr><th>No.</th><th>Name</th><th>Price</th><th>Quantity</th><th>SubTotal</th><th>Management</th></tr>
                            <c:set var="total" value="0" scope="page" />
                            <c:set var="counter" value="0" scope="page" />
                            <c:forEach var="orderitem" items="${order.getItems()}">                                
                                <tr>
                                    <td><c:out value="${counter + 1}"/></td>
                                    <td><c:out value="${orderitem.itemName}"/></td>
                                    <td><fmt:setLocale value="en_US"/><fmt:formatNumber value="${orderitem.unitPrice}" type="currency"/></td>
                                    <td>
                                        <form>
                                            <input type="hidden" name="orderid" value="<c:out value="${order.id}"/>">
                                            <input type="hidden" name="itemid" value="<c:out value="${orderitem.itemId}"/>">
                                            <input type="hidden" name="type" value="<c:out value="${orderitem.itemType}"/>">
                                            <input type="text" name="quantity" size=3 value="<c:out value="${orderitem.quantity}"/>">
                                        <input type="submit" class="formbutton2" value="Update">      
                                        </form>
                                    </td>
                                    <td><fmt:setLocale value="en_US"/><fmt:formatNumber value="${orderitem.totalCost}" type="currency"/></td>
                                    <td>
                                        <span><a href='admin_orderlist.jsp?orderid=<c:out value="${order.id}"/>&itemid=<c:out value="${orderitem.itemId}"/>&type=<c:out value="${orderitem.itemType}"/>&quantity=0' class='button3' onclick = "return confirm('Are you sure to delete this product?')">Delete</a></span>
                                    </td>
                                </tr>
                                <c:set var="total" value="${total + orderitem.getTotalCost()}" scope="page"/>
                                <c:set var="counter" value="${counter + 1}" scope="page"/>
                            </c:forEach>
                            <tr class='total'><td></td><td></td><td></td><td>Total</td><td><fmt:setLocale value="en_US"/><fmt:formatNumber value="${total}" type="currency"/></td><td></td></tr>
                        </table>
                    </div>
                </c:forEach>
            </c:otherwise>
        </c:choose>
    </div>
</section>    
<jsp:include page="layout_sidebar.jsp" />
<jsp:include page="layout_footer.jsp" />