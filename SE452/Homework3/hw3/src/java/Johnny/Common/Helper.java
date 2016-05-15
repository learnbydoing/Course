/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Johnny.Common;

/**
 *
 * @author RZHUANG
 */
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class Helper {
    // Session
    public final String SESSION_USERNAME = "username";
    public final String SESSION_USERTYPE = "usertype";
    public final String SESSION_CART = "cart";
    public final String SESSION_ORDERS = "orders";
    public final String SESSION_LOGIN_MSG = "login_msg";
    
    // Page
    public final String CURRENT_PAGE_HOME = "Home";
    public final String CURRENT_PAGE_CONSOLES = "Consoles";
    public final String CURRENT_PAGE_ACCESSORIES = "Accessories";
    public final String CURRENT_PAGE_GAMES = "Games";
    public final String CURRENT_PAGE_TABLETS = "Tablets";
    public final String CURRENT_PAGE_ACCMNG = "Accessory Management";
    public final String CURRENT_PAGE_GAMEMNG = "Game Management";
    public final String CURRENT_PAGE_USERS = "Users";
    public final String CURRENT_PAGE_ALLORDERS = "All Orders";
    public final String CURRENT_PAGE_MYORDER = "My Order";
    public final String CURRENT_PAGE_CART = "Cart";

    HttpServletRequest req;
    HttpSession session;

    public Helper(HttpServletRequest req) {
        this.req = req;
        this.session = req.getSession(true);
    }

    public boolean isLoggedin(){
        if (session.getAttribute(SESSION_USERNAME)==null)
            return false;
        return true;
    }

    public String username(){
        if (session.getAttribute(SESSION_USERNAME)!=null)
            return session.getAttribute(SESSION_USERNAME).toString();
        return "";
    }

    public String usertype(){
        if (session.getAttribute(SESSION_USERTYPE)!=null)
            return session.getAttribute(SESSION_USERTYPE).toString();
        return null;
    }   

    public String currentDate(){
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/YYYY");
        Date date = new Date();
        return dateFormat.format(date).toString();
    }
    
    public String formatCurrency(double price) {        
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("en", "US"));
        return formatter.format(price);
    }
    
    public String formateDate(Date date) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        return sdf.format(date);
    }
    
    public String generateUniqueId() {
        Date dNow = new Date();
        SimpleDateFormat ft = new SimpleDateFormat("yyMMddhhmmssMs");
        return ft.format(dNow);
    }
}

