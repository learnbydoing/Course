/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author RZHUANG
 */
public class Order extends HttpServlet {   
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
        String error_msg = "";
        
        String item1 = request.getParameter("item1");
        String item2 = request.getParameter("item2");
        String item3 = request.getParameter("item3");
        
        String price1 = request.getParameter("price1");
        String price2 = request.getParameter("price2");
        String price3 = request.getParameter("price3");
        
        double dprice1 = 0.0;
        try {
            dprice1 = Double.parseDouble(price1);
        } catch (NumberFormatException nfe) {
            error_msg = "Price1 is not number!";
        }
        double dprice2 = 0.0;
        try {
            dprice2 = Double.parseDouble(price2);
        } catch (NumberFormatException nfe) {
            error_msg = "Price2 is not number!";
        }
        double dprice3 = 0.0;
        try {
            dprice3 = Double.parseDouble(price3);
        } catch (NumberFormatException nfe) {
            error_msg = "Price3 is not number!";
        }
        double sum= (dprice1 + dprice2 + dprice3);
        double tax = sum * 0.05;
        double total = sum + tax;
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String title = "Items Purchased";
        String docType =
          "<!DOCTYPE html>\n";
        out.println(docType +
                    "<HTML>\n" +
                    "<HEAD><TITLE>" + title + "</TITLE></HEAD>\n" +
                    "<BODY BGCOLOR=\"#FDF5E6\">\n" +
                    "<H1>" + title + "</H1>");
         out.println("<UL>");
         out.println("  <LI>" + item1 + "</li>");
         out.println("  <LI>" + item2 + "</li>");
         out.println("  <LI>" + item3 + "</li>");
         out.println("</UL>"); 
         out.println("<h3>Sum:"+sum+"</h3>"); 
          out.println("<h3>Tax:"+tax+"</h3>"); 
         out.println("<h3>Total:"+total+"</h3>"); 
        out.println("</BODY></HTML>");
    }    
}
