/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
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
public class Cart extends HttpServlet {    

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
        HttpSession session = request.getSession();
        ShoppingCart cart;
        synchronized(session) {
            cart = (ShoppingCart)session.getAttribute("Cart");
            if (cart == null) {
                cart = new ShoppingCart();
                session.setAttribute("Cart", cart);
            }
            String id = request.getParameter("id");
            if (id != null && !id.isEmpty()) {
                String strQuantity = request.getParameter("quantity");
                if (strQuantity == null) {                  
                    cart.addItem(id);
                } else {                  
                    int quantity;
                    try {
                        quantity = Integer.parseInt(strQuantity);
                    } catch(NumberFormatException nfe) {
                        quantity = 1;
                    }
                    cart.setItemQuantity(id, quantity);
                }
            }
        }
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String title = "Your Shopping Cart";
        String docType = "<!DOCTYPE html>";
        out.println(docType +
                    "<html><head><title>" + title + "</title></head><body>"+
                    "<h1 align=\"center\">" + title + "</h1>");
        synchronized(session) {
            List<CartItem> items = cart.getItems();
            if (items.size() == 0) {
                out.println("<h2><i>No items in your cart...</i></h2>");
            } else {
                out.println
                  ("<table border=1 align=\"center\">" +
                   "<tr><th>Id</th><th>Name</th><th>Unit Cost</th><th>Quantity</th><th>Sub Total</th>");
                CartItem order;
                NumberFormat formatter = NumberFormat.getCurrencyInstance();               
                for(int i = 0; i < items.size(); i++) {
                    order = items.get(i);
                    out.println
                      ("<tr>" +
                       "  <td>" + order.getItemId() + "</td>" +
                       "  <td>" + order.getItemName() + "</td>" +
                       "  <td>" + formatter.format(order.getUnitPrice()) + "</td>" +
                       "  <td>" +
                       "<form>" +  // Submit to current URL
                       "<input type=\"hidden\" name=\"id\"" +
                       "       value=\"" + order.getItemId() + "\">" +
                       "<input type=\"text\" name=\"quantity\" size=3 value=\"" + 
                       order.getQuantity() + "\">\n" +
                       "<input type=\"submit\" value=\"Update\">"+      
                       "</form></td>" +
                       "  <td>" +  formatter.format(order.getTotalCost())+
                       "</td></tr>");
                }
                out.println
                  ("</table>" +
                   "<form action=\"Checkout\">" +
                   "<center><input type=\"submit\" value=\"Checkout\">" +
                   "</center></form>");
            }
            out.println("</body></html>");
        }
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
