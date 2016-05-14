<%@page import="Johnny.Beans.ShoppingCart"%>
<%@page import="Johnny.Dao.UserDao"%>
<%@page import="Johnny.Dao.OrderDao"%>
<%@page import="Johnny.Common.Constants" %>
<%@page import="Johnny.Common.Helper" %>
<div>
  <nav>    
    <%
        String uri = request.getRequestURI();
        String pageName = uri.substring(uri.lastIndexOf("/")+1);
        String[][] sitemenus = new String[5][2];
        sitemenus[0][0] = Constants.CURRENT_PAGE_HOME;
        sitemenus[0][1] = "index.jsp";
        sitemenus[1][0] = Constants.CURRENT_PAGE_CONSOLES;
        sitemenus[1][1] = "consolelist.jsp";
        sitemenus[2][0] = Constants.CURRENT_PAGE_ACCESSORIES;
        sitemenus[2][1] = "accessorylist.jsp";
        sitemenus[3][0] = Constants.CURRENT_PAGE_GAMES;
        sitemenus[3][1] = "gamelist.jsp";
        sitemenus[4][0] = Constants.CURRENT_PAGE_TABLETS;
        sitemenus[4][1] = "tabletlist.jsp";
        String sitemenu = "<ul>";
        for (int i = 0; i < sitemenus.length; i++) {
            if (sitemenus[i][1].equals(pageName)) {
                sitemenu += "<li class=\"selected\">";
            } else {
                sitemenu += "<li>";
            }
            sitemenu += "<a href='"+sitemenus[i][1]+"'>"+sitemenus[i][0]+"</a></li>";
        }
        sitemenu += "</ul>";
        
        Helper helper = new Helper(request);
        String usermenu = "<ul>";
        if (session.getAttribute(Constants.SESSION_USERTYPE)!=null){
            String usertype = session.getAttribute(Constants.SESSION_USERTYPE).toString();
            if (usertype.toLowerCase().equals(Constants.CONST_TYPE_STOREMANAGER_LOWER)) {
                if (Constants.CURRENT_PAGE_ACCMNG.equals(Constants.CURRENT_PAGE_HOME)) {
                    usermenu += "<li class=\"selected\">";
                } else {
                    usermenu += "<li>";
                }
                usermenu += "<a href='admin_accessorylist.jsp'>Accessory</a></li>";
                if (Constants.CURRENT_PAGE_GAMEMNG.equals(Constants.CURRENT_PAGE_HOME)) {
                    usermenu += "<li class=\"selected\">";
                } else {
                    usermenu += "<li>";
                }
                usermenu += "<a href='admin_gamelist.jsp'>Game</a></li>";
            } else if (usertype.toLowerCase().equals(Constants.CONST_TYPE_SALESMAN_LOWER)) {
                if (Constants.CURRENT_PAGE_ALLORDERS.equals(Constants.CURRENT_PAGE_HOME)) {
                    usermenu += "<li class=\"selected\">";
                } else {
                    usermenu += "<li>";
                }
                OrderDao dao = OrderDao.createInstance();
                usermenu += "<a href='admin_orderlist.jsp'>All Order("+dao.getOrders().size()+")</a></li>";
                if (Constants.CURRENT_PAGE_USERS.equals(Constants.CURRENT_PAGE_HOME)) {
                    usermenu += "<li class=\"selected\">";
                } else {
                    usermenu += "<li>";
                }
                UserDao userdao = UserDao.createInstance();
                usermenu += "<a href='admin_userlist.jsp'>User("+userdao.getUserCount()+")</a></li>";
            }
        }
        if (Constants.CURRENT_PAGE_MYORDER.equals(Constants.CURRENT_PAGE_HOME)) {
            usermenu += "<li class=\"selected\">";
        } else {
            usermenu += "<li>";
        }
        int ordercount = 0;
        if (helper.isLoggedin()) {
            OrderDao dao = OrderDao.createInstance();
            ordercount = dao.getOrders(helper.username()).size();
        }
        usermenu += "<a href='myorder.jsp'>My Order("+ordercount+")</a></li>";
        if (Constants.CURRENT_PAGE_CART.equals(Constants.CURRENT_PAGE_HOME)) {
            usermenu += "<li class=\"selected\">";
        } else {
            usermenu += "<li>";
        }
        int cartcount = 0;
        if (helper.isLoggedin()) {
            ShoppingCart cart = (ShoppingCart)session.getAttribute(Constants.SESSION_CART);
            if (cart != null) {
                cartcount = cart.getItems().size();
            }            
        }
        usermenu += "<a href='mycart.jsp'>Cart("+cartcount+")</a></li>";
        usermenu += "</ul>";
    %>
    <div style="float: left; ">
      <%= sitemenu %>
    </div>
    <div id="menu" style="float: right;">
      <%= usermenu %>
    </div>
  </nav>
</div>
<div id="body">