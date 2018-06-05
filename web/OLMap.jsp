<%-- 
    Document   : Anthony
    Created on : 29 May 2018
    Updated: 5 Jun 2018
    Author     : astump
--%>

<%@ page import="asWebRest.shared.WebCommon" %>

<%
    
    String headerType = "full";
    String pageTitle = "OLMap";
    String scriptIt = "true";
    String authCheck = "true";
    String action = "";
    String postData = "";
    String resolution = "";
    
    WebCommon wc = new WebCommon();
       
    %>
    <script>
        var doAction, dataInput, iRes;
    </script>
    <%
    if(wc.isSet(request.getParameter("Action"))) { action = wc.basicInputFilter(request.getParameter("Action")); }
    if(wc.isSet(request.getParameter("Input"))) { postData = wc.basicInputFilter(request.getParameter("Input")); }
    if(wc.isSet(request.getParameter("Resolution"))) { resolution = wc.basicInputFilter(request.getParameter("Resolution")); }
    %>
    <script>
        doAction = "<% out.print(action); %>";
        dataInput = "<% out.print(postData); %>";
        iRes = "<% out.print(resolution); %>";
        console.log("DEBUG VARS: doAction='" + doAction + "', dataInput='" + dataInput + "', iRes='" + iRes + "'");
    </script>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

    <link rel="stylesheet" type='text/css' href="/asWeb/jsLib/OpenLayers/ol.css"/>
    <script src="/asWeb/jsLib/OpenLayers/ol.js"></script>
    <!-- <script src="/asWeb/jsLib/OpenLayers/ol-debug.js"></script> -->
    
<jsp:include page="/inc/Header.jsp?type=full&title=OLMap&scripts=true"></jsp:include>

    <body>
    
        <div id="OLMapHolder"></div>
        
    </body>

<jsp:include page="/inc/Footer.jsp"></jsp:include>