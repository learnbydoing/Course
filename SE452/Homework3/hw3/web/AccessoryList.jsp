<%@page import="java.util.List"%>
<%@page import="Johnny.Dao.ConsoleDao"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="Johnny.Beans.Console"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<jsp:include page="Layout_Top.jsp" />
<jsp:include page="Layout_Header.jsp" />
<jsp:include page="Layout_Menu.jsp" />
<%@ page import="Johnny.Common.*" %>
    <%
        String makerName = request.getParameter("maker");
        makerName = makerName == null ? "" : makerName;

        ConsoleDao dao = new ConsoleDao();
        List<Console> list = dao.getConsoleList(makerName); 
        pageContext.setAttribute("list", list);
    %>    
    <section id='content'>
         <c:forEach var="console" items="${list}">            
            <h3><c:out value="${console.name}"/>: Accessories</h3>
            <c:set var="counter" value="0" scope="page" />
            <c:forEach var="accessory" items="${console.accessories}">   
                <c:if test="${(counter + 1)%3 == 1}">
                    <div class='special_grid_row'>     
                </c:if>
                <div class="special_grid_col">
                  <div class="special_box">
                    <img src="images/<c:out value="${accessory.image}"/>" class="img-responsive" alt=""/>
                    <h5><c:out value="${accessory.name}"/></h5>
                    <div class="grid_1">
                      <div class="special_item_price">
                        <span class="price-old"><fmt:setLocale value="en_US"/><fmt:formatNumber value="${accessory.price}" type="currency"/></span><span class="price-new"><fmt:setLocale value="en_US"/><fmt:formatNumber value="${accessory.discountedPrice}" type="currency"/></span>
                      </div>
                      <div class="special_item_add">
                        <ul>
                          <li>
                            <form method='post' action='Cart'>
                              <input type='hidden' name='id' value='<c:out value="${accessory.key}"/>'>
                              <input type='hidden' name='name' value='<c:out value="${accessory.name}"/>'>
                              <input type='hidden' name='type' value='1'>
                              <input type='hidden' name='maker' value='<c:out value="${accessory.retailer}"/>'>
                              <input type='hidden' name='access' value=''>
                              <input type='submit' class='formbutton' value='Add to Cart'>
                            </form>
                          </li>
                          <li><a class='button' href='Review?productkey=$id$'>Reviews</a></li>
                        </ul>
                      </div>
                    </div>
                  </div>
                </div>
                <c:if test="${(counter + 1)%3 == 0 || counter == list.size()}">
                    </div>    
                </c:if>
                <c:set var="counter" value="${counter + 1}" scope="page"/>
            </c:forEach>
        <div class='clear'>
        </c:forEach>
    </section>        
<jsp:include page="Layout_SideBar.jsp" />
<jsp:include page="Layout_Footer.jsp" />