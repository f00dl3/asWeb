<%-- 
    Document   : Anthony
    Created on : 27 Mar 2018
    Updated: 25 Apr 2018
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
        
        <h1><% out.println(pageTitle); %></h1>
              
        <div id="WxButtonNavi">
            <button class="UButton" id="ShWxLiveContainer">Live</button>
            <button class="UButton" id="ShWxLocalModel">Model</button>
            <button class="UButton" id="ShWxCf6">CF6</button>
            <button class="UButton" id="ShWxArchive">Arch</button>
        </div>
        
        <div id="WxLiveContainer"></div>
        <div id="WxLocalModel"></div>
        <div id="WxCf6"></div>
        <div id="WxArchive"></div>
            
    </div>
             
    </body>

<jsp:include page="/inc/Footer.jsp"></jsp:include>