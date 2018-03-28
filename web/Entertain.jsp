<%-- 
    Document   : MediaServ
    Created on : Mar 19, 2018, 7:35:15 AM
    Updated: 27 Mar 2018
    Author     : Anthony Stump
--%>

<%
    String headerType = "full";
    String pageTitle = "Entertainment";
    String scriptIt = "true";
    String authCheck = "true";
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/inc/Header.jsp?type=full&title=Entertain&scripts=true"></jsp:include>

<body>
    
    <h1><% out.println(pageTitle); %></h1>
    
        <div id="ButtonNavi">
            <button class="UButton" id="ShowETCooking">Cooking</button>
            <button class="UButton" id="ShowETGameAll">Games</button>
            <button class="UButton" id="ShowETLego">Lego</button>
            <button class="UButton" id="ShowETStream">Media</button>
            <!-- if not hidden <button class="UButton" id="ShowETReddit">Reddit</button> -->
        </div>

        <div id="ETCooking"></div>
        <div id="ETGameAll">
            <div class="jqAcc">
                <h4>Game Hours</h4>
                    <div id="GameHours"></div>
                <h4>Game Index</h4>
                    <div id="GameIndex"></div>
                <h4>Final Fantasy 14 (FFXIV)</h4>
                    <div id="FFXIVQuests"></div>    
            </div>
        </div>
        <div id="ETLego"></div>
        <div id="ETStream"></div>
        <div id="ETReddit"></div>
        
</body>

<jsp:include page="/inc/Footer.jsp"></jsp:include>
