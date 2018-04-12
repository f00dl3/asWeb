<%-- 
    Document   : Anthony
    Created on : 23 Mar 2018
    Updated: 12 Apr 2018
    Author     : astump
--%>

<%
    String headerType = "full";
    String pageTitle = "Finance Book";
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
            <button class="UButton" id="ShowFBAsset">AS</button>
            <button class="UButton" id="ShowFBAuto">AU</button>
            <button class="UButton" id="ShowFBBills">BI</button>
            <button class="UButton" id="ShowFBBlue">BP</button>
            <button class="UButton" id="ShowFBCheck">CK</button>
            <button class="UButton" id="ShowFBWorkPTO">TO</button>
            <button class="UButton" id="ShowFBUUse">UT</button>
        </div><p>
    
        <div id="FinOverview">
            <span id="HoldChecking"></span>
            <span id="HoldSavings"></span>
            <span id="HoldMortgage"></span>
            <span id="HoldWorth"></span>
        </div><p>
    
        <div id="FBAsset"></div>
        <div id="FBAuto"></div>
        <div id="FBBills"></div>
        <div id="FBBlue"></div>
        <div id="FBCheck"></div>
        <div id="FBWorkPTO"></div>
        <div id="FBUUse"></div>
             
    </body>

<jsp:include page="/inc/Footer.jsp"></jsp:include>