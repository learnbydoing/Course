import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class PlaceOrder extends HttpServlet {    
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
        
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String creditcard = request.getParameter("creditcard");
        
        HttpSession session = request.getSession();
        ShoppingCart cart = null;
        List<CartItem> items = null;
        String errmsg = "";
        synchronized(session) {
            cart = (ShoppingCart)session.getAttribute("Cart");
            if (cart == null) {
                errmsg = "No item in shopping cart, can't place order!";
            } else {
                items = cart.getItems();
                if (items == null || items.size() == 0) { 
                    errmsg = "No item in shopping cart, can't place order!";
                }                
            }
        }        
        
        helper.prepareLayout();
        helper.prepareHeader();
        helper.prepareMenu();
        String confirmation = name.substring(0,3) + creditcard.substring(creditcard.length() - 4);
        String content = "<section id='content'>";
        content += "  <div class='cart'>";
        content += "  <h3>Order - Confirmation</h3>";        
        
        if (!errmsg.isEmpty()) {
             content += "<h3 style='color:red'>"+errmsg+"</h3>";
        } else {
            content += "<h2>Name:"+name+"</h2>";
            content += "<h2>Address:"+address+"</h2>";
            content += "<table cellspacing='0'>";
            content += "<tr><th>No.</th><th>Product Name</th><th>Price</th><th>Quantity</th><th>SubTotal</th></tr>"; 
            CartItem cartItem;
            NumberFormat formatter = NumberFormat.getCurrencyInstance();
            double total = 0;
            for(int i = 0; i < items.size(); i++) {
                cartItem = items.get(i);
                content += "<tr>" +
                   "  <td>" + (i + 1) + "</td>" +
                   "  <td>" + cartItem.getItemName() + "</td>" +
                   "  <td>" + formatter.format(cartItem.getUnitPrice())+ "</td>" +
                   "  <td>" + cartItem.getQuantity()+ "</td>" +
                   "  <td>" + formatter.format(cartItem.getTotalCost())+ "</td>";
                content += "</tr>";
                total = total +cartItem.getTotalCost();
            }
            content += "<tr class='total'><td></td><td></td><td></td><td>Total</td><td>$"+total+"</td></tr>";
            content += "<tr><td></td><td></td><td></td><td>Confirmation Number</td><td>"+confirmation+"</td></tr></table>";
        }
        content += "  </div>";
        content += "</section>";

        helper.prepareContent(content);
        helper.prepareSideBar();
        helper.prepareFooter();
        helper.printHtml();

        if (errmsg.isEmpty()) {
            Date now = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(now);
            c.add(Calendar.DATE, 14); // 2 weeks
             //create order
            Order order = new Order(uniqueId(), helper.username(), confirmation, c.getTime());
            for (CartItem ob: items) {
                OrderItem item = new OrderItem(ob.getItem());
                item.setQuantity(ob.getQuantity());
                order.addItem(item);
            }

            OrderList orders;
            orders = (OrderList)session.getAttribute("Orders");
            if (orders == null) {
                orders = new OrderList();
                session.setAttribute("Orders", orders);
            }
            // create 
            orders.addOrder(order);            
            // remove cart from session
            session.removeAttribute("Cart"); 
        }             
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }

    private String uniqueId() {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
        return ft.format(dNow);
    }
}
