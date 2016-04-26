/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
public class OrderCancel extends HttpServlet {
   
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
        
        HttpSession session = request.getSession();
        OrderList orderlist;
        synchronized(session) {
            orderlist = (OrderList)session.getAttribute("Orders");
            if (orderlist == null) {
                errmsg = "No order!";
            } else {
                Order order = orderlist.getOrder(orderid);
                if (order == null) {
                    errmsg = "Order ["+orderid+"] is not found!";
                } else {                    
                    List<OrderItem> items = order.getItems();
                    for (OrderItem item: items) {
                        if (item.getItemType() == 4 || item.getItemType() == 5) {
                            Date eventdate = item.getItemEventDate();
                            Calendar c = Calendar.getInstance();
                            c.setTime(eventdate);
                            c.add(Calendar.DATE, -1);
                            Date now = new Date();
                            int comparison = now.compareTo(c.getTime());
                            if (comparison > 0) {
                                SimpleDateFormat dt1 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                                errmsg = "The order contains event ["+item.getItemName()+"] and date ["+dt1.format(item.getItemEventDate())+"]"
                                        + "<br>Current Time:" + dt1.format(now)
                                        + "<br><h2 style=\"color:red;\">You can't cancel it within 24 hours.</h2>";
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
        
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String title = "Cancel Order";
        String docType = "<!DOCTYPE html>";
        out.println(docType +
                    "<html><head><title>"+title+"</title></head><body><h1>"+title+"</h1>");
        out.println("<h2>"+errmsg+"</h2>");
        out.println("</body></html>");         
        //response.sendRedirect("MyOrder");
    }
}
