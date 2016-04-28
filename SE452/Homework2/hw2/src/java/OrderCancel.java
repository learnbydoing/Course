import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class OrderCancel extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        String errmsg = "";
        String orderid = request.getParameter("orderid");
        if (orderid==null||orderid.isEmpty()) {
            errmsg = "Order id is empty!";
        }
        
        if (errmsg.isEmpty()) {        
            HttpSession session = request.getSession();
            OrderList orderlist;
            synchronized(session) {
                orderlist = (OrderList)session.getAttribute("Orders");
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
                                    SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                    errmsg = "The order can only be cancelled within 5 days before delivery date ["+dt1.format(order.getDeliveryDate())+"]"
                                            + "<br><h2 style=\"color:red;\">You can't cancel it now.</h2>";
                                }
                            }
                        }
                    }
                }
                if (errmsg.isEmpty()) {
                    orderlist.removeOrder(orderid);
                    session.setAttribute("Orders", orderlist);
                    errmsg = "Your order ["+orderid+"] has been removed!";
                }
            }
        }
        
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        Helper helper = new Helper(request, pw);
        helper.prepareLayout();
        helper.prepareHeader();
        helper.prepareMenu();
        String content = "<section id='content'>";
        content += "  <div class='cart'>";
        content += "  <h3>Cancel Order</h3>";
        content += "  <h3 style='color:red'>"+errmsg+"</h3>";
        content += "  </div>";
        content += "</section>";
        helper.prepareContent(content);
        helper.prepareSideBar();
        helper.prepareFooter();
        helper.printHtml();   
    }
}
