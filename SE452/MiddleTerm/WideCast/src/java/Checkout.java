/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author Johnny
 */
public class Checkout extends HttpServlet {
   
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
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        String title = "Provide your address and credit card";
        out.println("<!DOCTYPE html><html><head><title>Place Order</title></head><body>");
        out.println("<div>"+title+"</div>");
        out.println("    <form action=\"PlaceOrder\" method=\"Post\">");
        out.println("        <table style='width:50%'>");
        out.println("            <tr><td>Full Name:</td><td><input type='text' name='name' value='johnny' required /></td></tr>");
        out.println("            <tr><td>Address:</td><td><input type='text' name='address' value='1st Jackson Ave,Chicago,IL' required /></td></tr>");
        out.println("            <tr><td>Credit Card Number</td><td><input type='text' name='creditcard' value='31011220332874983274' required /></td></tr>");
        out.println("            <tr><td colspan=\"2\"><input name=\"create\" value=\"Place Order\" type=\"submit\" /></td></tr>");
        out.println("        </table>");
        out.println("    </form>");
        out.println("</body></html>");        
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
