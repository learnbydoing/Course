import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
            session.setAttribute(helper.SESSION_LOGIN_MSG, "Please login first!");
            response.sendRedirect("Login");
            return;
        }
        
        String username = helper.username();
        String address = request.getParameter("address");
        String creditcard = request.getParameter("creditcard");
        
        HttpSession session = request.getSession();
        ShoppingCart cart = null;
        List<CartItem> items = null;
        String errmsg = "";
        synchronized(session) {
            cart = (ShoppingCart)session.getAttribute(helper.SESSION_CART);
            if (cart == null) {
                errmsg = "No item in shopping cart, can't place order!";
            } else {
                items = cart.getItems();
                if (items == null || items.size() == 0) { 
                    errmsg = "No item in shopping cart, can't place order!";
                }                
            }
        }        
        
        
        String orderid = uniqueId();
        String confirmation = username + orderid.substring(orderid.length()-4) + creditcard.substring(creditcard.length() - 4);
        String content = "<section id='content'>";
        content += "  <div class='cart'>";
        content += "  <h3>Order - Confirmation</h3>";        
        
        if (!errmsg.isEmpty()) {
             content += "<h3 style='color:red'>"+errmsg+"</h3>";
        } else {
            content += "<h5>Name: "+username+"</h5>";
            content += "<h5>Address: "+address+"</h5>";
            content += "<table cellspacing='0'>";
            content += "<tr><th>No.</th><th>Product Name</th><th>Price</th><th>Quantity</th><th>SubTotal</th></tr>"; 
            CartItem cartItem;
            NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "US"));
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
            content += "<tr class='total'><td></td><td></td><td></td><td>Total</td><td>"+formatter.format(total)+"</td></tr>";
            content += "<tr><td></td><td></td><td></td><td>Confirmation Number</td><td>"+confirmation+"</td></tr></table>";
        }
        content += "  </div>";
        content += "</section>";

        if (errmsg.isEmpty()) {
            Date now = new Date();
            Calendar c = Calendar.getInstance();
            c.setTime(now);
            c.add(Calendar.DATE, 14); // 2 weeks
             //create order
            Order order = new Order(orderid, helper.username(), address, creditcard, confirmation, c.getTime());
            for (CartItem ob: items) {
                OrderItem item = new OrderItem(ob.getItem());
                item.setQuantity(ob.getQuantity());
                order.addItem(item);
            }

            OrderList orders;
            orders = (OrderList)session.getAttribute(helper.SESSION_ORDERS);
            if (orders == null) {
                orders = new OrderList();
                session.setAttribute(helper.SESSION_ORDERS, orders);
            }
            // create 
            orders.addOrder(order);            
            // remove cart from session
            session.removeAttribute(helper.SESSION_CART); 
        }
        
        helper.prepareLayout();
        helper.prepareHeader();
        helper.prepareMenu();
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

    private String uniqueId() {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
        return ft.format(dNow);
    }
}
