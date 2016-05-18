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
    HttpServletRequest req;
    HttpSession session;

    public Helper(HttpServletRequest req) {
        this.req = req;
        this.session = req.getSession(true);
    }

    public boolean isLoggedin(){
        if (session.getAttribute(Constants.SESSION_USERNAME)==null)
            return false;
        return true;
    }

    public String username(){
        if (session.getAttribute(Constants.SESSION_USERNAME)!=null)
            return session.getAttribute(Constants.SESSION_USERNAME).toString();
        return "";
    }

    public String usertype(){
        if (session.getAttribute(Constants.SESSION_USERTYPE)!=null)
            return session.getAttribute(Constants.SESSION_USERTYPE).toString();
        return null;
    }
    
    public String getCurrentPage() {
        if (session.getAttribute(Constants.SESSION_CURRENTPAGE)!=null)
            return session.getAttribute(Constants.SESSION_CURRENTPAGE).toString();
        return null;
    }
    
    public void setCurrentPage(String page) {
        session.setAttribute(Constants.SESSION_CURRENTPAGE, page);
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

