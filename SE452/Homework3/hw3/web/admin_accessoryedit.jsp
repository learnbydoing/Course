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
        errmsg = "You have no authorization to manage accessory!";
    }
    
    String consolekey = "";
    String accessorykey = "";
    String gamekey = "";
    String name = "";
    String price = "";
    String image = "";
    String retailer = "";
    String condition = "";
    String discount = "";
    
    if (errmsg.isEmpty()) {
        consolekey = request.getParameter("consolekey");
        accessorykey = request.getParameter("accessorykey");
        if (consolekey == null || consolekey.isEmpty() || accessorykey == null || accessorykey.isEmpty()) {
            errmsg = "Invalida parameter. Cannot find the accessory with key: " + accessorykey;
        } else {
            if ("GET".equalsIgnoreCase(request.getMethod())) {            
                ConsoleDao dao = ConsoleDao.createInstance();
                Accessory accessory = dao.getAccessory(consolekey, accessorykey);
                name = accessory.getName();
                price = String.valueOf(accessory.getPrice());
                image = accessory.getImage();
                retailer = accessory.getRetailer();
                condition = accessory.getCondition();
                discount = String.valueOf(accessory.getDiscount());
            } else {
                name = request.getParameter("name");
                price = request.getParameter("price");
                image = request.getParameter("image");
                retailer = request.getParameter("retailer");
                condition = request.getParameter("condition");
                discount = request.getParameter("discount");
                
                if(name == null){
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
                        Accessory accessory = dao.getAccessory(consolekey, accessorykey);
                        if (accessory == null) {
                            errmsg = "Accessory ["+name+"] does not exist!";
                        } else {
                            accessory.setName(name);
                            accessory.setPrice(dprice);
                            accessory.setImage(image);
                            accessory.setRetailer(retailer);
                            accessory.setCondition(condition);
                            accessory.setDiscount(ddiscount);
                            dao.updateAccessory();
                            errmsg = "Accessory ["+accessory.getName()+"] is updated!";
                        }                        
                    }
                }
            }
        }
    }
    
    String[][] arr = new String[6][2];
    arr[0][0] = "xboxone";
    arr[0][1] = "Microsoft-Xbox One";
    arr[1][0] = "xbox360";
    arr[1][1] = "Microsoft-Xbox 360";
    arr[2][0] = "ps3";
    arr[2][1] = "Sony-PS3";
    arr[3][0] = "ps4";
    arr[3][1] = "Sony-PS4";
    arr[4][0] = "wii";
    arr[4][1] = "Nintendo-Wii";
    arr[5][0] = "wiiu";
    arr[5][1] = "Nintendo-WiiU";
    String selector = "<select name='facconsole' class='input' disabled>";
    for (int i = 0; i < arr.length; i++) {
        if (arr[i][0].equalsIgnoreCase(consolekey)) {
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
    <h3>Edit Accessory</h3>
    <h3 style='color:red'><%=errmsg%></h3>
    <form action="admin_accessoryedit.jsp" method="Post">
      <input type='hidden' name='consolekey' value='<%=consolekey%>'>
      <input type='hidden' name='accessorykey' value='<%=accessorykey%>'>
      <table style='width:50%'>
        <tr><td><h5>Console:</h5></td><td><%=selector%></td></tr>
        <tr><td><h5>Name:</h5></td><td><input type='text' name='name' value='<%=name%>' class='input' required /></td></tr>
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