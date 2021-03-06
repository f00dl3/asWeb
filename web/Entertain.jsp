<%-- 
    Document   : MediaServ
    Created on : Mar 19, 2018, 7:35:15 AM
    Updated: 9 Dec 2018
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
            <button class="UButton" id="HShowETReddit">Reddit</button>
        </div><p>

        <div id="ETCooking"></div>
        <div id="ETGameAll">
            <button class="SButton" id="ShETGHours">Hours</button>
            <button class="SButton" id="ShETGIndex">Index</button>
            <div class="UPop">
                <button class="SButton">FFXIV</button>
                <div class="UPopO">
                    <button class="SButton" id="ShETGFF14Q">All</button>
                    <button class="SButton" id="ShETGFF14C">Craft</button>
                    <button class="SButton" id="ShETGFF14D">Duties</button>
                    <button class="SButton" id="ShETGFF14I">Items</button>
                </div>
            </div>
            <p>
            <div id="ETGHours"></div>
            <div id="ETGIndex"></div>
            <div id="ETGFF14C"></div>
            <div id="ETGFF14D"></div>
            <div id="ETGFF14I"></div>
            <div id="ETGFF14Q"></div>
        </div>
        <div id="ETLego"></div>
        <div id="ETStream">
            <div id="ETSSearch"></div><br/>
            <div id="ETSResults"></div><br/>
            <div id="ETSPlayer"></div>
        </div>
        <div id="ETReddit"></div>
        
</body>

<jsp:include page="/inc/Footer.jsp"></jsp:include>
