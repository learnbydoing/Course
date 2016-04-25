/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author RZHUANG
 */
public class Guess extends HttpServlet {
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
        request.setAttribute("errmsg", "");
        request.setAttribute("random", 0);
        request.setAttribute("guess", 0);
        request.setAttribute("times", 0);
        request.setAttribute("reward", 0);
        request.getRequestDispatcher("/WEB-INF/guess.jsp").forward(request, response);
        //processRequest(request, response);
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
        response.setContentType("text/html;charset=UTF-8");
        
        String number = request.getParameter("number");
        
        int guess = 0;
        String errmsg = "";
        if (number==null||number.isEmpty()){
            errmsg = "Please input a number!";
        } else {            
            try {
                guess = Integer.parseInt(number);
            } catch (NumberFormatException nfe) {                
                errmsg = "Your guess must be number!";
            }
        }
        
        int times = 0;
        int random = 0;
        HttpSession session = request.getSession(true);
        if (session.getAttribute("times")!=null) {
            times = (int)session.getAttribute("times");
        }
        if (!errmsg.isEmpty()) {
            request.setAttribute("random", "NULL");
            request.setAttribute("guess", number);
            request.setAttribute("errmsg", errmsg);
        } else {            
            Random rand = new Random();
            random = rand.nextInt(3) + 1;            
            if (random == guess) {                
                times++;
                session.setAttribute("times", times);
            }
            request.setAttribute("guess", guess);  
            request.setAttribute("random", random);  
        }        
       
        int reward = 0;
        switch(times) {
            case 0:
                reward = 0;
                break;
            case 1:
                reward = 50;
                break;
            case 2:
                reward = 100;
                break;
            case 3:
                reward = 500;
                break;
            default:
                reward = 500 + (times-3)*100;
                break;
        }
              
        request.setAttribute("times", times);
        request.setAttribute("reward", reward);
        request.getRequestDispatcher("/WEB-INF/guess.jsp").forward(request, response);
    }    

}
