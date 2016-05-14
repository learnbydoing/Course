<%@page import="Johnny.Common.Constants"%>
<%@page import="Johnny.Dao.ConsoleDao"%>
<%@page import="Johnny.Beans.Console"%>
<%@page import="Johnny.Beans.Accessory"%>
<%@page import="Johnny.Common.Helper"%>
<jsp:include page="layout_top.jsp" />
<jsp:include page="layout_header.jsp" />
<%
    Helper helper = new Helper(request);
    if(!helper.isLoggedin()){
        session.setAttribute(helper.SESSION_LOGIN_MSG, "Please login first!");
        response.sendRedirect("account_login.jsp");
        return;
    }
    String usertype = helper.usertype();
    String errmsg = "";
    if (usertype==null || !usertype.equals(Constants.CONST_TYPE_STOREMANAGER_LOWER)) {
        errmsg = "You have no authorization to manage accessary!";
    }
    
    String consolekey = "";
    String name = "";
    String price = "";
    String image = "";
    String retailer = "";
    String condition = "";
    String discount = "";
    
    if (errmsg.isEmpty()) {
         consolekey = request.getParameter("consolekey");
        if ("GET".equalsIgnoreCase(request.getMethod())) {           
            name = "Controller";
            price = "99.99";
            image = "consoles/xbox360.jpg";
            retailer = "Johnny Corp";
            condition = "New";
            discount = "30";
        } else {
            name = request.getParameter("name");
            price = request.getParameter("price");
            image = request.getParameter("image");
            retailer = request.getParameter("retailer");
            condition = request.getParameter("condition");
            discount = request.getParameter("discount");

            if(consolekey == null){
                errmsg = "Console can't be empty!";
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
                ConsoleDao dao = ConsoleDao.createInstance();
                if(!dao.isConsoleExisted(consolekey)) {
                    errmsg = "No such console ["+consolekey+"] !";
                } else{
                    String key = consolekey + "_" + name.trim().toLowerCase();
                    if (dao.isAccessoryExisted(consolekey, key)) {
                        errmsg = "Accessory ["+name+"] already exists!";
                    } else {
                        Accessory accessory = new Accessory(key, consolekey, name, dprice, image, retailer,condition,ddiscount);
                        dao.addAccessory(consolekey, accessory);
                        errmsg = "Accessory ["+name+"] is created!";
                    }
                }
            }
        }
    }
%>
<jsp:include page="layout_menu.jsp" />
<section id="content">
  <div>
    <h3>Add Accessory</h3>
    <h3 style='color:red'><%=errmsg%></h3>
    <form action="admin_accessoryadd.jsp" method="Post">
      <table style='width:50%'>
        <tr><td><h5>Console:</h5></td><td><select name='consolekey' class='input'><option value='xboxone' selected>Microsoft-Xbox One</option><option value='xbox360'>Microsoft-Xbox 360</option><option value='ps3'>Sony-PS3</option><option value='ps4'>Sony-PS4</option><option value='wii'>Nintendo-Wii</option><option value='wiiu'>Nintendo-WiiU</option></select></td></tr>
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