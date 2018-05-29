<%-- 
    Document   : Anthony
    Created on : 29 May 2018
    Author     : astump
--%>

<%
    String headerType = "full";
    String pageTitle = "OLMap";
    String scriptIt = "true";
    String authCheck = "true";
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/inc/Header.jsp?type=full&title=OLMap&scripts=true"></jsp:include>

    <link rel="stylesheet" href="/asWeb/jsLib/OpenLayers/ol.css">
    <script src="/asWeb/jsLib/OpenLayers/ol.js">
        
    <body>
    
        <span id="MapTitleSection"></span><br/>
        <div id="map" class="map"></div>
        
    </body>

<jsp:include page="/inc/Footer.jsp"></jsp:include>