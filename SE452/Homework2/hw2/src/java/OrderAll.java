import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class OrderAll extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        Helper helper = new Helper(request,pw);
        if(!helper.isLoggedin()){
            HttpSession session = request.getSession(true);
            session.setAttribute(helper.SESSION_LOGIN_MSG, "Please login first!");
            response.sendRedirect("Login");
            return;
        }
        
        String usertype = helper.usertype();
        String errmsg = "";
        if (usertype==null || !usertype.equals(UserHashMap.CONST_TYPE_SALESMAN_LOWER)) {
            errmsg = "You have no authorization to manage orders!";
        }
        
        HttpSession session = request.getSession();
        List<Order> orders = null;
        synchronized(session) {
            OrderList allorders = (OrderList)session.getAttribute(helper.SESSION_ORDERS);
            if (allorders == null) {
                errmsg = "There is no order created yet!";
            } else {
                orders = allorders.getOrders(); 
                if (orders == null || orders.size() == 0) {
                    errmsg = "There is no order created yet!";
                }
            }
        }
        helper.prepareLayout();
        helper.prepareHeader();
        helper.prepareMenu();
        String content = "<section id='content'>";
        content += "  <div class='cart'>";
        content += "  <h3>All Orders</h3>";
        if (!errmsg.isEmpty()) {
            content += "<h3 style='color:red'>"+errmsg+"</h3>";
        } else {
            for (Order order: orders) {
                content += "<div class=\"order_box\"><table class=\"order_table\"><tr><td><h5><i>Order Id: </i></h5></td><td>"+order.getId()+"</td>";
                content += "<td><form action=\"OrderCancel\" method=\"Post\">" + 
                   "<input type=\"hidden\" name=\"orderid\"" +
                   "       value=\"" + order.getId() + "\">" +
                   "<input type=\"submit\" value=\"Cancel Order\" class=\"formbutton\" onclick = \"return confirm('Are you sure to cancel this order?')\">"+      
                   "</form></td></tr>";
                content += "<tr><td><h5><i>User Namer: </i></h5></td><td>"+order.getUserName()+"</td><td><td></tr>";
                content += "<tr><td><h5><i>Address Number: </i></h5></td><td>"+order.getAddress()+"</td><td><td></tr>";
                content += "<tr><td><h5><i>Confirmation Number: </i></h5></td><td>"+order.getConfirmation()+"</td><td><td></tr>";
                content += "</table>";
                content += "<table cellspacing='0'>";
                content += "<tr><th>Item No.</th><th>Name</th><th>Price</th><th>Quantity</th><th>SubTotal</th></tr>"; 
                OrderItem orderItem;
                NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "US"));
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
                content += "<tr class='total'><td></td><td></td><td></td><td>Total</td><td>"+formatter.format(total)+"</td></tr>";
                content += "</table></div>";
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