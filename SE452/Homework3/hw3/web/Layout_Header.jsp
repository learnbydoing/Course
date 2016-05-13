<header>
  <div class="header_logo">
    <table>
      <tr>
        <td><img src="images/site/GameON.jpg" alt="" /></td>
        <td><h1><a href="Home">Game <span>Speed</span></a></h1></td>
      </tr>
    </table>
  </div>
  <div class="header_right">
    <div class="account">
      <%@ page import="Johnny.Common.Constants" %>
      <%
        String menuitem = "";
        if (session.getAttribute(Constants.SESSION_USERNAME)!=null){
            String username = session.getAttribute(Constants.SESSION_USERNAME).toString();
            username = Character.toUpperCase(username.charAt(0)) + username.substring(1);
            menuitem += "<ul>"
                      + "  <li>Hello, "+username+"</li>"
                      + "  <li><a href='account_logout.jsp'>Logout</a></li>"
                      + "</ul>";
        } else {
            menuitem += "<ul>"
                      + "  <li><a href='account_register.jsp'>Register</a></li>"
                      + "  <li><a href='account_login.jsp'>Login</a></li>"
                      + "</ul>";
        }
       %>
       <%= menuitem %>
    </div>
    <div class="clear"></div>
    <div class="header_search">
      <form action="SearchProduct" method="Post">
        <input type="text" id="search" name="productname" value="" placeholder="search...">
        <input type="submit" value="">
      </form>
    </div>
  </div>
</header>
<div class="clear"></div>
