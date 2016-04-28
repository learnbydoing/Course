

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/Cart")
public class Cart extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        Helper helper = new Helper(request, pw);
        String name = request.getParameter("name");
        String type = request.getParameter("type");
        String maker = request.getParameter("maker");
        String access = request.getParameter("access");

        helper.storeProduct(name, type, maker, access);

        displayCart(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        Helper helper = new Helper(request, pw);

        displayCart(request, response);
    }

    protected void displayCart(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter pw = response.getWriter();
        Helper helper = new Helper(request,pw);
        if(!helper.isLoggedin()){
            HttpSession session = request.getSession(true);
            session.setAttribute("login_msg", "Please Login to add items to cart");
            response.sendRedirect("Login");
            return;
        }

        helper.prepareLayout();
        helper.prepareHeader();
        helper.prepareMenu();
        String content = "<section id='content'>";
        content += "  <div class='cart'>";
        content += "  <h3>My Cart</h3>";
        if(helper.CartCount()>0){
            content += "<table cellspacing='0'>";
            content += "<tr><th>Item No.</th><th>Name</th><th>Price</th></tr>";
            int i = 1;
            double total = 0;
            for (OrderItem oi : helper.getCustomerOrders()) {
                content += "<tr>";
                content += "<td>"+i+".</td><td>"+oi.getName()+"</td><td>$"+oi.getPrice()+"</td>";
                content += "</tr>";
                total = total +oi.getPrice();
                i++;
            }
            content += "<tr class='total'><td></td><td>Total</td><td>$"+total+"</td></tr>";
            content += "<tr><td></td><td></td><td><a href='CheckOut' class='button'>Check Out</a></td></tr></table>";
        } else {
            content += "<h4 style='color:red'>Your Cart is empty</h4>";
        }
        content += "  </div>";
        content += "</section>";
        helper.prepareContent(content);
        helper.prepareSideBar();
        helper.prepareFooter();
        helper.printHtml();
    }
}
