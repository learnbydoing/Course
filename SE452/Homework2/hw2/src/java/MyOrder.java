import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class MyOrder extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        Helper helper = new Helper(request,pw);
        if(!helper.isLoggedin()){
            HttpSession session = request.getSession(true);
            session.setAttribute("login_msg", "Please Login to add items to cart");
            response.sendRedirect("Login");
            return;
        }        
        
        HttpSession session = request.getSession();
        String errmsg = "";
        List<Order> orders = null;
        synchronized(session) {
            OrderList allorders = (OrderList)session.getAttribute("Orders");
            if (allorders == null) {
                errmsg = "You have no order yet!";
            } else {
                orders = allorders.getOrders(helper.username()); 
                if (orders == null || orders.size() == 0) {
                    errmsg = "You have no order yet!";
                }
            }
        }
        helper.prepareLayout();
        helper.prepareHeader();
        helper.prepareMenu();
        String content = "<section id='content'>";
        content += "  <div class='cart'>";
        content += "  <h3>My Orders</h3>";
        if (!errmsg.isEmpty()) {
            content += "<h3 style='color:red'>"+errmsg+"</h3>";
        } else {
            for (Order order: orders) {
                content += "<h2><i>Order Id: "+order.getId()+"</i></h2>";
                content += "<form action=\"OrderCancel\" method=\"Post\">" + 
                   "<input type=\"hidden\" name=\"orderid\"" +
                   "       value=\"" + order.getId() + "\">" +
                   "<input type=\"submit\" value=\"Cancel Order\">"+      
                   "</form>";
                content += "<table cellspacing='0'>";
                content += "<tr><th>Item No.</th><th>Name</th><th>Price</th><th>Quantity</th><th>SubTotal</th></tr>"; 
                OrderItem orderItem;
                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                double total = 0;
                for(int i = 0; i < order.getItems().size(); i++) {
                    orderItem = (OrderItem)order.getItems().get(i);
                    content += "<tr>";
                    content += "<td>"+(i+1)+"</td><td>"+orderItem.getItemName()+"</td><td>"+formatter.format(orderItem.getUnitPrice())+"</td>";
                    content += "<td>" + orderItem.getQuantity()+"</td>";
                    content += "<td>" +  formatter.format(orderItem.getTotalCost())+ "</td>";
                    content += "</tr>";
                    total = total +orderItem.getTotalCost();
                }
                content += "<tr class='total'><td></td><td></td><td></td><td>Total</td><td>$"+total+"</td></tr>";
                content += "</table>";
            }
        }
        content += "  </div>";
        content += "</section>";
        helper.prepareContent(content);
        helper.prepareSideBar();
        helper.prepareFooter();
        helper.printHtml();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
