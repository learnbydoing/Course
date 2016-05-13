<%@ page import="Johnny.Common.Constants" %>
<%@ page import="Johnny.Common.Helper" %>
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
        
        Helper helper = new Helper(request, null);
        String usermenu = "<ul>";
        if (session.getAttribute(Constants.SESSION_USERTYPE)!=null){
            String usertype = session.getAttribute(Constants.SESSION_USERTYPE).toString();
            if (usertype.toLowerCase().equals(Constants.CONST_TYPE_STOREMANAGER_LOWER)) {
                if (Constants.CURRENT_PAGE_ACCMNG.equals(Constants.CURRENT_PAGE_HOME)) {
                    usermenu += "<li class=\"selected\">";
                } else {
                    usermenu += "<li>";
                }
                usermenu += "<a href='AccessoryMgn'>Accessory</a></li>";
                if (Constants.CURRENT_PAGE_GAMEMNG.equals(Constants.CURRENT_PAGE_HOME)) {
                    usermenu += "<li class=\"selected\">";
                } else {
                    usermenu += "<li>";
                }
                usermenu += "<a href='GameMgn'>Game</a></li>";
            } else if (usertype.toLowerCase().equals(Constants.CONST_TYPE_SALESMAN_LOWER)) {
                if (Constants.CURRENT_PAGE_ALLORDERS.equals(Constants.CURRENT_PAGE_HOME)) {
                    usermenu += "<li class=\"selected\">";
                } else {
                    usermenu += "<li>";
                }
                usermenu += "<a href='OrderAll'>All Order("+"AllOrderCount()"+")</a></li>";
                if (Constants.CURRENT_PAGE_USERS.equals(Constants.CURRENT_PAGE_HOME)) {
                    usermenu += "<li class=\"selected\">";
                } else {
                    usermenu += "<li>";
                }
                usermenu += "<a href='UserMgn'>User</a></li>";
            }
        }
        if (Constants.CURRENT_PAGE_MYORDER.equals(Constants.CURRENT_PAGE_HOME)) {
            usermenu += "<li class=\"selected\">";
        } else {
            usermenu += "<li>";
        }
        usermenu += "<a href='MyOrder'>My Order("+helper.OrderCount()+")</a></li>";
        if (Constants.CURRENT_PAGE_CART.equals(Constants.CURRENT_PAGE_HOME)) {
            usermenu += "<li class=\"selected\">";
        } else {
            usermenu += "<li>";
        }
        usermenu += "<a href='mycart.jsp'>Cart("+helper.CartCount()+")</a></li>";
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