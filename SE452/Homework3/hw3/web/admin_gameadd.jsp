<%@page import="Johnny.Common.Constants"%>
<%@page import="Johnny.Dao.GameDao"%>
<%@page import="Johnny.Beans.Game"%>
<%@page import="Johnny.Common.Helper"%>
<jsp:include page="layout_top.jsp" />
<jsp:include page="layout_header.jsp" />
<jsp:include page="layout_menu.jsp" />
<%
    Helper helper = new Helper(request,response.getWriter());
    if(!helper.isLoggedin()){
        session.setAttribute(helper.SESSION_LOGIN_MSG, "Please login first!");
        response.sendRedirect("account_login.jsp");
        return;
    }
    String usertype = helper.usertype();
    String errmsg = "";
    if (usertype==null || !usertype.equals(Constants.CONST_TYPE_STOREMANAGER_LOWER)) {
        errmsg = "You have no authorization to manage game!";
    }
    
    String maker = "";
    String name = "";
    String price = "";
    String image = "";
    String retailer = "";
    String condition = "";
    String discount = "";
    
    if (errmsg.isEmpty()) {
        if ("GET".equalsIgnoreCase(request.getMethod())) {
            name = "Game1";
            price = "59.99";
            image = "games/activision_cod.jpg";
            retailer = "Activision";
            condition = "New";
            discount = "15";
        } else {
            maker = request.getParameter("maker");
            name = request.getParameter("name");
            price = request.getParameter("price");
            image = request.getParameter("image");
            retailer = request.getParameter("retailer");
            condition = request.getParameter("condition");
            discount = request.getParameter("discount");

            if(maker == null){
                errmsg = "Maker can't be empty!";
            }else if(name == null){
                errmsg = "Name can't be empty!";
            }else if(price == null){
                errmsg = "Price can't be empty!";
            }else if(image == null){
                errmsg = "Image can't be empty!";
            }else if(retailer == null){
                errmsg = "Retailer can't be empty!";
            }else if(condition == null){
                errmsg = "Condition can't be empty!";
            }else if(discount == null){
                errmsg = "Discount can't be empty!";
            }
            
            double dprice = 0.0;
            if (errmsg.isEmpty()) {
                try {
                    dprice = Double.parseDouble(price);
                } catch (NumberFormatException nfe) {
                    errmsg = "Price must be number!";
                }
            }
            double ddiscount = 0.0;
            if (errmsg.isEmpty()) {
                try {
                    ddiscount = Double.parseDouble(discount);
                } catch (NumberFormatException nfe) {
                    errmsg = "Diccount must be number!";
                }
                if (errmsg.isEmpty() && (ddiscount < 0 || ddiscount > 100)) {
                    errmsg = "Diccount must be between 0 and 100!";
                }
            }

            if (errmsg.isEmpty()) {
                GameDao dao = GameDao.createInstance();
                if(dao.isExisted(name)) {
                    errmsg = "Game ["+name+"] already exist!";
                } else{
                    String key = name.trim().toLowerCase();
                    Game gm = new Game(key, maker, name, dprice, image, retailer,condition,ddiscount);
                    dao.addGame(gm);
                    errmsg = "Game ["+name+"] is created!";
                }
            }
        }
    }
%>
<section id="content">
  <div>
    <h3>Add Game</h3>
    <h3 style='color:red'><%=errmsg%></h3>
    <form action="admin_gameadd.jsp" method="Post">
      <table style='width:50%'>
        <tr><td><h5>Maker:</h5></td><td><select name='maker' class='input'><option value='electronicarts' selected>Electronic Arts</option><option value='activision'>Activision</option><option value='taketwointeractive'>Take-Two Interactive</option></select></td></tr>
        <tr><td><h5>Name:</h5></td><td><input type='text' name='name' value='<%=name%>' class='input' required /></td></tr>
        <tr><td><h5>Price:</h5></td><td><input type='text' name='price' value='<%=price%>' class='input' required /></td></tr>
        <tr><td><h5>Image:</h5></td><td><input type='text' name='image' value='<%=image%>' class='input' required /></td></tr>
        <tr><td><h5>Retailer:</h5></td><td><input type='text' name='retailer' value='<%=retailer%>' class='input' required /></td></tr>
        <tr><td><h5>Condition:</h5></td><td><input type='text' name='condition' value='<%=condition%>' class='input' required /></td></tr>
        <tr><td><h5>Discount:</h5></td><td><input type='text' name='discount' value='<%=discount%>' class='input' required /></td></tr>
        <tr><td colspan="2"><input name="create" class="formbutton" value="Create" type="submit" /></td></tr>
      </table>
    </form>
  </div>
</section>
<jsp:include page="layout_sidebar.jsp" />
<jsp:include page="layout_footer.jsp" />