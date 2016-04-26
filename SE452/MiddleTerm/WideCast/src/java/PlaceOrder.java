/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author Johnny
 */
public class PlaceOrder extends HttpServlet {
  
    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String address = request.getParameter("address");
        String creditcard = request.getParameter("creditcard");
        
        HttpSession session = request.getSession();
        ShoppingCart cart;
        String errmsg = "";
        synchronized(session) {
            cart = (ShoppingCart)session.getAttribute("Cart");
            if (cart == null) {
                errmsg = "No item in shopping cart, cann't place order!";
            }            
        }
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String title = "Place Order";
        String docType = "<!DOCTYPE html>\n";
        out.println(docType +
                    "<html><head><title>"+title+"</title></head><body><h1>Order - Confirmation</h1>");
        synchronized(session) {
            String confirmation = "";
            List<CartItem> items = cart.getItems();
            if (items.size() == 0 || !errmsg.isEmpty()) {
                out.println("<h2><i>"+errmsg+"</i></h2>");
            } else {
                out.println("<h2>Name:"+name+"</h2>");
                out.println("<h2>Address:"+address+"</h2>");
                out.println
                  ("<table style='width:50%' border='1px'>" +
                   "<tr><th>No.</th><th>Product Name</th><th>Unit Price</th><th>Quantity</th><th>Subtotal</th></tr>");
                CartItem order;
                NumberFormat formatter = NumberFormat.getCurrencyInstance();
                double total = 0;
                confirmation = name.substring(0,3) + creditcard.substring(creditcard.length() - 4);
                for(int i = 0; i < items.size(); i++) {
                    order = items.get(i);
                    out.println
                      ("<tr>" +
                       "  <td>" + (i + 1) + "</td>" +
                       "  <td>" + order.getItemName() + "</td>" +
                       "  <td>" + formatter.format(order.getUnitPrice())+ "</td>" +
                       "  <td>" + order.getQuantity()+ "</td>" +
                       "  <td>" + formatter.format(order.getTotalCost())+ "</td>");
                    total += order.getTotalCost();
                }
                out.println("<tr><td></td><td></td><td></td><td>Total</td><td>"+formatter.format(total)+"</td></tr>");
                out.println("<tr><td></td><td></td><td></td><td>Confirmation Number</td><td>"+confirmation+"</td></tr>");
                out.println("</table>");
            }
            out.println("</body></html>");
                         
            //create order
            Order order = new Order(uniqueId(), "johnny", confirmation);
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
    
    private String uniqueId() {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
        return ft.format(dNow);
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
        doGet(request, response);
    }
}
