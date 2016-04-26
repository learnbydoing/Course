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
public class MyOrder extends HttpServlet {

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
        OrderList orderlist;
        String errmsg = "";
        synchronized(session) {
            orderlist = (OrderList)session.getAttribute("Orders");
            if (orderlist == null) {
                errmsg = "No order!";
            }            
        }
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String title = "My Orders";
        String docType = "<!DOCTYPE html>";
        out.println(docType +
                    "<html><head><title>"+title+"</title></head><body><h1>"+title+"</h1>");
        synchronized(session) {
            List<Order> orders = orderlist.getOrders("johnny");
            if (orders.size() == 0 || !errmsg.isEmpty()) {
                out.println("<h2><i>"+errmsg+"</i></h2>");
            } else {
                for (Order order: orders) {
                    out.println("<h2><i>Order Id: "+order.getId()+"</i></h2>");
                    out.println("<form action=\"OrderCancel\" method=\"Post\">" + 
                       "<input type=\"hidden\" name=\"orderid\"" +
                       "       value=\"" + order.getId() + "\">" +
                       "<input type=\"submit\" value=\"Cancel Order\">"+      
                       "</form>");
                    out.println
                      ("<table style='width:50%' border='1px'>" +
                       "<tr><th>No.</th><th>Product Name</th><th>Unit Price</th><th>Quantity</th><th>Subtotal</th></tr>");
                    OrderItem orderitem;
                    NumberFormat formatter = NumberFormat.getCurrencyInstance();
                    double total = 0;
                    for(int i = 0; i < order.getItems().size(); i++) {
                        orderitem = (OrderItem)order.getItems().get(i);
                        out.println
                          ("<tr>" +
                           "  <td>" + (i + 1) + "</td>" +
                           "  <td>" + orderitem.getItemName() + "</td>" +
                           "  <td>" + formatter.format(orderitem.getUnitPrice())+ "</td>" +
                           "  <td>" + orderitem.getQuantity()+ "</td>" +
                           "  <td>" + formatter.format(orderitem.getTotalCost())+ "</td>");
                        total += orderitem.getTotalCost();
                    }
                    out.println("<tr><td></td><td></td><td></td><td>Total</td><td>"+formatter.format(total)+"</td></tr>");
                    out.println("<tr><td></td><td></td><td></td><td>Confirmation Number</td><td>"+order.getConfirmation()+"</td></tr>");
                    out.println("</table>");
                }
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
