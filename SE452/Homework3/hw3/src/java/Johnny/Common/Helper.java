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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import Johnny.Beans.ShoppingCart;
import Johnny.Beans.OrderList;
import Johnny.Beans.Accessory;
import Johnny.Beans.ProductItem;
import Johnny.Beans.ProductList;
import Johnny.Beans.Review;
import Johnny.Beans.ShoppingCart;
import Johnny.Beans.ShoppingCart;
import Johnny.Beans.ShoppingCart;
import Johnny.Beans.ShoppingCart;
import Johnny.Dao.ReviewDao;

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
    PrintWriter pw;
    String url;
    HttpSession session;
    String _layout;
    String _header;
    String _menu;
    String _content;
    String _sidebar;
    String _footer;

    public Helper(HttpServletRequest req, PrintWriter pw) {
        this.req = req;
        this.pw = pw;
        this.url = this.getFullURL();
        this.session = req.getSession(true);
    }

    public void prepareLayout() {
        _layout = HtmlToString("_layout.html");
        _header = "";
        _menu = "";
        _content = "";
        _sidebar = "";
        _footer = "";
    }
    public void prepareHeader() {
        _header = HtmlToString("site_header.html");
        String menuitem = "";
        if (session.getAttribute(SESSION_USERNAME)!=null){
            String username = session.getAttribute(SESSION_USERNAME).toString();
            username = Character.toUpperCase(username.charAt(0)) + username.substring(1);
            menuitem += "<ul>"
                      + "  <li>Hello, "+username+"</li>"
                      + "  <li><a href='Logout'>Logout</a></li>"
                      + "</ul>";
        } else {
            menuitem += "<ul>"
                      + "  <li><a href='Registration'>Register</a></li>"
                      + "  <li><a href='Login'>Login</a></li>"
                      + "</ul>";
        }
        _header = _header.replace("$menuitem$", menuitem);
    }
    public void prepareMenu(String page) {
        
    }
    public void prepareContent(String content) {
        _content = content;
    }
    public void prepareSideBar() {
        _sidebar = HtmlToString("site_sidebar.html");
    }
    public void prepareFooter() {
        _footer = HtmlToString("site_footer.html");
    }
    public void printHtml() {
        _layout =  _layout.replace("$header$", _header)
                          .replace("$menu$", _menu)
                          .replace("$content$", _content)
                          .replace("$sidebar$", _sidebar)
                          .replace("$footer$", _footer);
        pw.print(_layout);
    }
    public String getTemplate(String file) {
        return HtmlToString(file);
    }
    public String getFullURL() {
        String scheme = req.getScheme();
        String serverName = req.getServerName();
        int serverPort = req.getServerPort();
        String contextPath = req.getContextPath();
        StringBuffer url = new StringBuffer();
        url.append(scheme).append("://").append(serverName);

        if ((serverPort != 80) && (serverPort != 443)) {
                url.append(":").append(serverPort);
        }
        url.append(contextPath);
        url.append("/");
        return url.toString();
    }

    public String HtmlToString(String file) {
        String result = null;
        try {
            String webPage = url + file;
            URL url = new URL(webPage);
            URLConnection urlConnection = url.openConnection();
            InputStream is = urlConnection.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);

            int numCharsRead;
            char[] charArray = new char[1024];
            StringBuffer sb = new StringBuffer();
            while ((numCharsRead = isr.read(charArray)) > 0) {
                    sb.append(charArray, 0, numCharsRead);
            }
            result = sb.toString();
        } catch (Exception e) {
        }
        return result;
    }

    public void logout(){
        session.removeAttribute(SESSION_USERNAME);
        session.removeAttribute(SESSION_USERTYPE);
        session.removeAttribute(SESSION_CART);
        //session.removeAttribute(SESSION_ORDERS);
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
    
    public int CartCount(){
        if(!isLoggedin()) {
            return 0;
        }
        
        ShoppingCart cart = (ShoppingCart)session.getAttribute(SESSION_CART);
        if (cart == null) {
            return 0;
        }
        return cart.getItems().size();           
    } 
    public int AllOrderCount(){
        if(!isLoggedin()) {
            return 0;
        }
        
        OrderList orders = (OrderList)session.getAttribute(SESSION_ORDERS);
        if (orders == null) {
            return 0;
        }
        return orders.getOrders().size();           
    } 
    
    public int OrderCount(){
        if(!isLoggedin()) {
            return 0;
        }
        
        OrderList orders = (OrderList)session.getAttribute(SESSION_ORDERS);
        if (orders == null) {
            return 0;
        }
        return orders.getOrders(username()).size();           
    } 

    public String currentDate(){
        DateFormat dateFormat = new SimpleDateFormat("MM/dd/YYYY");
        Date date = new Date();
        return dateFormat.format(date).toString();
    }
    /*
    public Accessory getAccessory(String manufacturer, String console, String accessory){
        HashMap<String, Console> hm = getConsoles(manufacturer);
        if (hm == null) {
            return null;
        }
        Console conobj = hm.get(console);
        if (conobj==null) {
            return null;
        }

        HashMap<String, Accessory> map = conobj.getAccessories();
        if (map == null || map.size()==0) {
            return null;
        }
        return map.get(accessory);
    }

    public Console getConsole(String manufacturer, String console){
        HashMap<String, Console> hmconsole = getConsoles(manufacturer);
        if (hmconsole==null || hmconsole.size()==0) {
            return null;
        } else {
            return hmconsole.get(console);
        }
    }

    public HashMap<String, Console> getConsoles(String manufacturer){
        HashMap<String, Console> hm = new HashMap<String, Console>();
        if (manufacturer == null || manufacturer.isEmpty()) {
            hm = getConsoles();
        } else {
            switch(manufacturer.toLowerCase()) {
                case ConsoleHashMap.CONST_MICROSOFT_LOWER:
                    hm.putAll(ConsoleHashMap.Microsoft);
                    break;
                case ConsoleHashMap.CONST_SONY_LOWER:
                    hm.putAll(ConsoleHashMap.Sony);
                    break;
                case ConsoleHashMap.CONST_NINTENDO_LOWER:
                    hm.putAll(ConsoleHashMap.Nintendo);
                    break;
            }
        }
        return hm;
    }

    public HashMap<String, Console> getConsoles(){
        HashMap<String, Console> hm = new HashMap<String, Console>();
        hm.putAll(ConsoleHashMap.Microsoft);
        hm.putAll(ConsoleHashMap.Sony);
        hm.putAll(ConsoleHashMap.Nintendo);
        return hm;
    }

    public Game getGame(String maker, String key) {
        HashMap<String, Game> hm = getGames(maker);
        if (hm==null||hm.size()==0){
            return null;
        } else {
            return hm.get(key);
        }
    }
    public HashMap<String, Game> getGames(String maker){
        HashMap<String, Game> hm = new HashMap<String, Game>();
        if (maker == null || maker.isEmpty()) {
            hm = getGames();
        } else {
            switch(maker.toLowerCase()) {
                case GameHashMap.CONST_ELECTRONICARTS_LOWER:
                    hm = GameHashMap.ElectronicArts;
                    break;
                case GameHashMap.CONST_ACTIVISION_LOWER:
                    hm = GameHashMap.Activision;
                    break;
                case GameHashMap.CONST_TAKETWOINTERACTIVE_LOWER:
                    hm = GameHashMap.TakeTwoInteractive;
                    break;
            }
        }
        return hm;
    }

    public HashMap<String, Game> getGames(){
        HashMap<String, Game> hm = new HashMap<String, Game>();
        hm.putAll(GameHashMap.ElectronicArts);
        hm.putAll(GameHashMap.Activision);
        hm.putAll(GameHashMap.TakeTwoInteractive);
        return hm;
    }
    
    public HashMap<String, Tablet> getTablets(String maker){
        HashMap<String, Tablet> hm = new HashMap<String, Tablet>();
        if (maker == null || maker.isEmpty()) {
            hm = getTablets();
        } else {
            switch(maker.toLowerCase()) {
                case TabletHashMap.CONST_APPLE_LOWER:
                    hm = TabletHashMap.Apple;
                    break;
                case TabletHashMap.CONST_MICROSOFT_LOWER:
                    hm = TabletHashMap.Microsoft;
                    break;
                case TabletHashMap.CONST_SAMSUNG_LOWER:
                    hm = TabletHashMap.Samsung;
                    break;
            }
        }
        return hm;
    }

    public HashMap<String, Tablet> getTablets(){
        HashMap<String, Tablet> hm = new HashMap<String, Tablet>();
        hm.putAll(TabletHashMap.Apple);
        hm.putAll(TabletHashMap.Microsoft);
        hm.putAll(TabletHashMap.Samsung);
        return hm;
    }

    public ArrayList<String> getProducts(){
        ArrayList<String> ar = new ArrayList<String>();
        for(Map.Entry<String, Console> entry : getConsoles().entrySet()){
            ar.add(entry.getValue().getName());
        }

        return ar;
    }

    public ArrayList<String> getProductsGame(){
        ArrayList<String> ar = new ArrayList<String>();
        for(Map.Entry<String, Game> entry : getGames().entrySet()){
            ar.add(entry.getValue().getName());
        }
        return ar;
    }

    public ArrayList<String> getProductsTablets(){
        ArrayList<String> ar = new ArrayList<String>();
        for(Map.Entry<String, Tablet> entry : getTablets().entrySet()){
            ar.add(entry.getValue().getName());
        }
        return ar;
    }
    */
    public ArrayList<Review> getReviews(String productkey){
        if (productkey==null||productkey.isEmpty()) {
            return new ArrayList<Review>();
        }
        if (!ReviewDao.reviews.containsKey(productkey)) {
            ProductItem product = ProductList.getProduct(productkey);
            if (product != null) {
                // create a new key for it
                ReviewDao.reviews.put(productkey, new ArrayList<Review>());
            }
        }
        return ReviewDao.reviews.get(productkey);
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
