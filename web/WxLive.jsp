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
        
        <div class="jqAcc">
            <h4>Observations/Forecasts</h4>
                <div id="LiveWarnings"></div><p>
                <div id="ObsCurrent"></div>
                <div id="ObsLinks3D"></div>
                <div id="ObsLinksList"></div>
            <h4>Weather Feeds</h4>
                <div id="WeatherFeeds"></div>
            <h4>Earthquakes</h4>
                <div id="Earthquakes"></div>
            <h4>News & Email</h4>
                <div id="NewsEmail"></div>
        </div>
             
    </body>

<jsp:include page="/inc/Footer.jsp"></jsp:include>