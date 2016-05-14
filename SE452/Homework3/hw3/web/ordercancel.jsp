<%@page import="java.util.Calendar"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%@page import="Johnny.Beans.OrderList"%>
<%@page import="Johnny.Beans.Order"%>
<%@page import="Johnny.Beans.OrderItem"%>
<%@page import="Johnny.Common.Helper"%>
<jsp:include page="layout_top.jsp" />
<jsp:include page="layout_header.jsp" />
<jsp:include page="layout_menu.jsp" />
<%
    String errmsg = "";
    String orderid = request.getParameter("orderid");
    if (orderid==null||orderid.isEmpty()) {
        errmsg = "Order id is empty!";
    }

    if (errmsg.isEmpty()) {
        OrderList orderlist;
        Helper helper = new Helper(request,response.getWriter());
        synchronized(session) {
            orderlist = (OrderList)session.getAttribute(helper.SESSION_ORDERS);
            if (orderlist == null) {
                errmsg = "You have no order!";
            } else {
                Order order = orderlist.getOrder(orderid);
                if (order == null) {
                    errmsg = "Order ["+orderid+"] is not found!";
                } else {                    
                    List<OrderItem> items = order.getItems();
                    for (OrderItem item: items) {
                        if (item.getItemType() == 4 || item.getItemType() == 5) {
                            Date deliverydate = order.getDeliveryDate();
                            Calendar c = Calendar.getInstance();
                            c.setTime(deliverydate);
                            c.add(Calendar.DATE, -5);
                            Date now = new Date();
                            int comparison = now.compareTo(c.getTime());
                            if (comparison > 0) {
                                errmsg = "The order can only be cancelled within 5 days before delivery date ["+helper.formateDate(order.getDeliveryDate())+"]"
                                        + "<br><h2 style=\"color:red;\">You can't cancel it now.</h2>";
                            }
                        }
                    }
                }
            }
            if (errmsg.isEmpty()) {
                orderlist.removeOrder(orderid);
                session.setAttribute(helper.SESSION_ORDERS, orderlist);
                errmsg = "Your order ["+orderid+"] has been removed!";
            }
        }
    }
%>

<section id='content'>
    <div class='cart'>
        <h3>Cancel Order</h3>
        <h3 style='color:red'><%=errmsg%></h3>
    </div>
</section>
<jsp:include page="layout_sidebar.jsp" />
<jsp:include page="layout_footer.jsp" />