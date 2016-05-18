<%@page import="Johnny.Common.Constants"%>
<%@page import="Johnny.Dao.GameDao"%>
<%@page import="Johnny.Beans.Game"%>
<%@page import="Johnny.Common.Helper"%>
<jsp:include page="layout_top.jsp" />
<jsp:include page="layout_header.jsp" />
<%
    Helper helper = new Helper(request);
    helper.setCurrentPage(Constants.CURRENT_PAGE_GAMEMNG);
    
    if(!helper.isLoggedin()){
        session.setAttribute(Constants.SESSION_LOGIN_MSG, "Please login first!");
        response.sendRedirect("account_login.jsp");
        return;
    }
    String usertype = helper.usertype();
    String errmsg = "";
    if (usertype==null || !usertype.equals(Constants.CONST_TYPE_STOREMANAGER_LOWER)) {
        errmsg = "You have no authorization to manage game!";
    }
    
    String gamekey = "";
    String maker = "";
    String name = "";
    String price = "";
    String image = "";
    String retailer = "";
    String condition = "";
    String discount = "";
    
    if (errmsg.isEmpty()) {
        gamekey = request.getParameter("gamekey");
        if (gamekey == null || gamekey.isEmpty()) {
            errmsg = "Invalida parameter. Cannot find the product with key: " + gamekey;
        } else {
            if ("GET".equalsIgnoreCase(request.getMethod())) {            
                GameDao dao = GameDao.createInstance();
                Game game = dao.getGame(gamekey);
                maker = game.getMaker();
                name = game.getName();
                price = String.valueOf(game.getPrice());
                image = game.getImage();
                retailer = game.getRetailer();
                condition = game.getCondition();
                discount = String.valueOf(game.getDiscount());
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
                        errmsg = "Discount must be number!";
                    }
                    if (errmsg.isEmpty() && (ddiscount < 0 || ddiscount > 100)) {
                        errmsg = "Discount must be between 0 and 100!";
                    }
                }

                if (errmsg.isEmpty()) {
                    GameDao dao = GameDao.createInstance();
                    if(!dao.isExisted(gamekey)) {
                        errmsg = "Game ["+gamekey+"] does not exist!";
                    } else{
                        Game game = dao.getGame(gamekey);
                        game.setMaker(maker);
                        game.setPrice(dprice);
                        game.setImage(image);
                        game.setRetailer(retailer);
                        game.setCondition(condition);
                        game.setDiscount(ddiscount);
                        dao.updateGame();
                        errmsg = "Game ["+game.getName()+"] is updated!";
                    }
                }
            }
        }
    }
    
    String[][] arr = new String[3][2];
    arr[0][0] = Constants.CONST_ELECTRONICARTS_LOWER;
    arr[0][1] = Constants.CONST_ELECTRONICARTS;
    arr[1][0] = Constants.CONST_ACTIVISION_LOWER;
    arr[1][1] = Constants.CONST_ACTIVISION;
    arr[2][0] = Constants.CONST_TAKETWOINTERACTIVE_LOWER;
    arr[2][1] = Constants.CONST_TAKETWOINTERACTIVE;
    String selector = "<select name='maker' class='input'>";
    for (int i = 0; i < arr.length; i++) {
        if (arr[i][0].equals(maker.toLowerCase())) {
            selector += "<option value='"+arr[i][0]+"' selected>"+arr[i][1]+"</option>";
        } else {
            selector += "<option value='"+arr[i][0]+"'>"+arr[i][1]+"</option>";
        }
    }
    selector += "</select>";
%>
<jsp:include page="layout_menu.jsp" />
<section id="content">
  <div>
    <h3>Edit Game</h3>
    <h3 style='color:red'><%=errmsg%></h3>
    <form action="admin_gameedit.jsp" method="Post">
      <input type='hidden' name='gamekey' value='<%=gamekey%>'>
      <input type='hidden' name='name' value='<%=name%>'>
      <table style='width:50%'>
        <tr><td><h5>Maker:</h5></td><td><%=selector%></td></tr>
        <tr><td><h5>Name:</h5></td><td><input type='text' name='name' value='<%=name%>' class='input' required disabled/></td></tr>
        <tr><td><h5>Price:</h5></td><td><input type='text' name='price' value='<%=price%>' class='input' required /></td></tr>
        <tr><td><h5>Image:</h5></td><td><input type='text' name='image' value='<%=image%>' class='input' required /></td></tr>
        <tr><td><h5>Retailer:</h5></td><td><input type='text' name='retailer' value='<%=retailer%>' class='input' required /></td></tr>
        <tr><td><h5>Condition:</h5></td><td><input type='text' name='condition' value='<%=condition%>' class='input' required /></td></tr>
        <tr><td><h5>Discount:</h5></td><td><input type='text' name='discount' value='<%=discount%>' class='input' required /></td></tr>
        <tr><td colspan="2"><input name="create" class="formbutton" value="Save" type="submit" /></td></tr>
      </table>
    </form>
  </div>
</section>
<jsp:include page="layout_sidebar.jsp" />
<jsp:include page="layout_footer.jsp" />