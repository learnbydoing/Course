/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author RZHUANG
 */
public class Calc extends HttpServlet {

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
        PrintWriter out = response.getWriter();
        
        String sentence = request.getParameter("sentence");               
        
        if(sentence==null||sentence.isEmpty()) {            
            String docType = "<!DOCTYPE html>\n";
            out.println(docType +
                        "<HTML>\n" +
                        "<HEAD><TITLE>Search Result</TITLE></HEAD>\n" +
                        "<BODY BGCOLOR=\"#FDF5E6\">\n" +
                        "<H1>" + "Please input a sentence and try again" + "</H1>");
            out.println("</BODY></HTML>");
        } else {            
            HashMap<Character, Integer> map = new HashMap<Character, Integer>();

            for (int i = 0; i < sentence.length(); i++) {
                char c = sentence.charAt(i); 
                if(!map.containsKey(c)){
                    map.put(c, 1);
                } else {
                    map.put(c, map.get(c) + 1);
                }
            }
            String docType = "<!DOCTYPE html>\n";
            out.println(docType +
                        "<HTML>\n" +
                        "<HEAD><TITLE>Search Result</TITLE></HEAD>\n" +
                        "<BODY BGCOLOR=\"#FDF5E6\">\n" +
                        "<H1>Occurrences</H1>");             
            out.println("<table border='1px'>"); 
            for(Map.Entry<Character, Integer> entry : map.entrySet()){
                if (entry.getValue() != 0) {
                    out.println("<tr><td>"+entry.getKey()+"</td><td>"+entry.getValue()+"</td></tr>"); 
                }
            }
            out.println("</table>");             
            out.println("</BODY></HTML>");
        }   
    }
}
