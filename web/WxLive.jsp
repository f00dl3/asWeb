<%-- 
    Document   : Anthony
    Created on : 27 Mar 2018
    Author     : astump
--%>

<%
    String headerType = "full";
    String pageTitle = "Weather Live";
    String scriptIt = "true";
    String authCheck = "true";
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/inc/Header.jsp?type=full&title=WxLive&scripts=true"></jsp:include>

    <body>
              
        <div id="ButtonNavi">
            <button class="UButton" id="ShWxObs">Live</button>
            <button class="UButton" id="ShFeeds">Feeds</button>
            <button class="UButton" id="ShQuake">Quakes</button>
            <button class="UButton" id="ShNEmail">News</button>
        </div><p>
        
        <div id="WxLive">
            <h4>Observations/Forecasts</h4>
            <div id="LiveWarnings"></div><p>
            <div id="ObsCurrent"></div>
            <div id="ObsLinks3D"></div>
            <div id="ObsLinksList"></div>
        </div>
            
        <div id="WxFeeds"></div>
        <div id="WxQuakes"></div>
        <div id="WxNews"></div>
            
    </div>
             
    </body>

<jsp:include page="/inc/Footer.jsp"></jsp:include>