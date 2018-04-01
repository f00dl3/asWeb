<%-- 
    Document   : Anthony
    Created on : 27 Mar 2018
    Updated: 1 Apr 2018
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
            <button class="UButton" id="SwxLive">Live</button>
            <button class="UButton" id="SwxFeeds">Feeds</button>
            <button class="UButton" id="SwxQuakes">Quakes</button>
            <button class="UButton" id="SwxNews">News</button>
        </div>
        
        <div id="wxObsFcst">
            <h4>Observations/Forecasts</h4>
                <div id="LiveWarnings"></div><p>
                <div id="ObsCurrent"></div>
                <div id="ObsLinks3D"></div>
                <div id="ObsLinksList"></div>
        </div>
        <div id="wxFeeds">
            <h4>Weather Feeds</h4>
                <div id="WeatherFeeds"></div>
        </div>
        <div id="wxEarthquakes">
            <h4>Earthquakes</h4>
                <div id="Earthquakes"></div>
        </div>
        <div id="wxNewsEmail">
            <h4>News & Email</h4>
                <div id="NewsEmail"></div>
        </div>
             
    </body>

<jsp:include page="/inc/Footer.jsp"></jsp:include>