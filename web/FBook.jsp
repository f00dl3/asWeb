<%-- 
    Document   : Anthony
    Created on : 23 Mar 2018
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

        <div id="ButtonNavi">
            <button class="UButton" id="ShowFBAsset">AS</button>
            <button class="UButton" id="ShowFBAuto">AU</button>
            <button class="UButton" id="ShowFBBills">BI</button>
            <button class="UButton" id="ShowFBBlue">BP</button>
            <button class="UButton" id="ShowFBCheck">CK</button>
            <button class="UButton" id="ShowFBWorkPTO">TO</button>
            <button class="UButton" id="ShoFBUtUse">UT</button>
        </div>
    
        <div id="FinOverview">
            <div id="HoldChecking"></div>
            <div id="HoldSavings"></div>
            <div id="HoldMortgage"></div>
            <div id="HoldWorth"></div>
        </div>
    
        <div id="FBAsset"></div>
        <div id="FBAuto"></div>
        <div id="FBBills"></div>
        <div id="FBBlue"></div>
        <div id="FBCheck"></div>
        <div id="FBWorkPTO"></div>
        <div id="FBUtilUse"></div>
             
    </body>

<jsp:include page="/inc/Footer.jsp"></jsp:include>