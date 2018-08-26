<%-- 
    Document   : WxStation.jsp
    Created on : 22 Apr 2018
    Updated:    26 Aug 2018
    Author     : astump
--%>

<%
    String headerType = "full";
    String pageTitle = "Weather Station Inventory";
    String scriptIt = "true";
    String authCheck = "true";
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/inc/Header.jsp?type=full&title=WxStation&scripts=true"></jsp:include>

    <body>
              
        <h1>Weather Station Inventory Search</h1>
        
        <div id="stationDataHolder">LOADING... <em>(~2 MB Compressed JSON)</em></div><p>
             
    </body>

<jsp:include page="/inc/Footer.jsp"></jsp:include>