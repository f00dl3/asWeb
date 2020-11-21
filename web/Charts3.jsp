<%-- 
    Document   : Anthony
    Created on : 6 Oct 2020
    Updated: 21 Nov 2020
    Author     : astump
--%>

<%@ page import="asWebRest.shared.WebCommon" %>

<%
    
    String headerType = "full";
    String pageTitle = "Charts3";
    String scriptIt = "true";
    String authCheck = "true";
    String action = "";
    String inList = "";
    
    WebCommon wc = new WebCommon();
       
    %>
    
    <script>
        var doAction = "";
    </script>
    
    <%
    if(wc.isSet(request.getParameter("doAction"))) { action = wc.basicInputFilter(request.getParameter("doAction")); }
    if(wc.isSet(request.getParameter("inList"))) { action = wc.basicInputFilter(request.getParameter("inList")); }
    %>
    
    <script>
        doAction = "<% out.print(action); %>";
        console.log("DEBUG VARS: doAction='" + doAction + "', inList='" + inList + "'");
     </script>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<jsp:include page="/inc/Header.jsp?type=full&title=Charts3&scripts=true"></jsp:include>

    <body id="Charts">
    
        <div id="ChartHolder" style="height: 90%; width: 95%;">
        	<canvas id="ChartCanvas" style="height: 100%; width: 100%;"></canvas>
       	</div><br/>
       	<div id="extraDataHolder"></div>
        
    </body>

<jsp:include page="/inc/Footer.jsp"></jsp:include>
