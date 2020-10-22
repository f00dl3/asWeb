<%-- 
    Document   : Anthony
    Created on : 23 Mar 2018
    Updated: 21 Oct 2020
    Author     : astump
--%>

<%
    String headerType = "full";
    String pageTitle = "Finances";
    String scriptIt = "true";
    String authCheck = "true";
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/inc/Header.jsp?type=full&title=FBook&scripts=true"></jsp:include>

    <body>
        
        <h1><% out.println(pageTitle); %></h1>

        <div id="rLinkHolder"></div><p>
        
        <div id="ButtonNavi">
            <button class="UButton" id="ShowFBAuto20">2</button>
            <button class="UButton" id="ShowFBAsset">A</button>
            <button class="UButton" id="ShowFBBills">B</button>
            <button class="UButton" id="ShowFBCheck">C</button>
            <button class="UButton" id="ShowFBAutoHC">H</button>
            <button class="UButton" id="ShowFBAuto">M</button>
            <button class="UButton" id="ShowFBBlue">P</button>
            <button class="UButton" id="ShowFBStocks">S</button>
            <button class="UButton" id="ShowFBWorkPTO">T</button>
            <button class="UButton" id="ShowFBUUse">U</button>
        </div><p>
    
        <div id="FinOverview">
            <span id="HoldChecking"></span>
            <span id="HoldSavings"></span>
            <span id="HoldStock"></span>
            <span id="HoldMortgage"></span>
            <span id="HoldWorth"></span>
        </div><p>
    
        <div id="FBAsset"></div>
        <div id="FBAuto"></div>
        <div id="FBAutoHC"></div>
        <div id="FBAuto20"></div>
        <div id="FBBills"></div>
        <div id="FBBlue"></div>
        <div id="FBCheck"></div>
        <div id="FBStocks"></div>
        <div id="FBWorkPTO"></div>
        <div id="FBUUse"></div>
             
    </body>

<jsp:include page="/inc/Footer.jsp"></jsp:include>
