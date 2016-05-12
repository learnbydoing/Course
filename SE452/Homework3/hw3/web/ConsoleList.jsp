<%@page import="java.util.List"%>
<%@page import="Johnny.Dao.ConsoleDao"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="Johnny.Beans.Console"%>
<jsp:include page="Layout_Top.jsp" />
<jsp:include page="Layout_Header.jsp" />
<jsp:include page="Layout_Menu.jsp" />
<%@ page import="Johnny.Common.*" %>
    <%
        String makerName = request.getParameter("maker");
        makerName = makerName == null ? "" : makerName;

        ConsoleDao dao = new ConsoleDao();
        List<Console> list = dao.getConsoleList(makerName); 
        int i = 0;
        i= 3;
        for (int j = 0; j < list.size(); j++) {
            i++;
        }
    %>

    <section id='content'>
        <h3><%= makerName %> Consoles</h3>
        <c:set var="counter" value="0" scope="page" />
        <c:forEach var="console" items="${list}">   
            <c:if test="${counter%3 == 1}">
                <div class='special_grid_row'>     
            </c:if>
            <div class="special_grid_col">
              <div class="special_box">
                <img src="images/" class="img-responsive" alt=""/>
                <h5><c:out  value="${console.Name}"/></h5>
                <div class="grid_1">
                  <div class="special_item_price">
                    <span class="price-old"><c:out  value="${console.Price}"/></span><span class="price-new"><c:out  value="${console.DiscountedPrice}"/></span>
                  </div>
                  <div class="special_item_add">
                    <ul>
                      <li>
                        <form method='post' action='Cart'>
                          <input type='hidden' name='id' value='<c:out value="${console.Key}"/>'>
                          <input type='hidden' name='name' value='<c:out value="${console.Name}"/>'>
                          <input type='hidden' name='type' value='1'>
                          <input type='hidden' name='maker' value='<c:out  value="${console.Retailer}"/>'>
                          <input type='hidden' name='access' value=''>
                          <input type='submit' class='formbutton' value='Add to Cart'>
                        </form>
                      </li>
                      <li><a class='button' href='AccessoryList?maker=$maker$&console=$id$'>View Accessories</a></li>
                      <li><a class='button' href='Review?productkey=$id$'>Reviews</a></li>
                    </ul>
                  </div>
                </div>
              </div>
            </div>
            <c:if test="${counter%3 == 0 || counter == list.size()}">
                </div>    
            </c:if>
            <c:set var="counter" value="${counter + 1}" scope="page"/>
        </c:forEach>
        <div class='clear'>
    </section>
<jsp:include page="Layout_SideBar.jsp" />
<jsp:include page="Layout_Footer.jsp" />