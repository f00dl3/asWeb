<%-- 
    Document   : Anthony
    Created on : 29 May 2018
    Updated: 23 Apr 2019
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
    String legacyPath = "";
    
    WebCommon wc = new WebCommon();
       
    %>
    
    <script>
        var doAction,
            dataInput,
            iRes,
            legacyPath;
    </script>
    
    <%
    if(wc.isSet(request.getParameter("Action"))) { action = wc.basicInputFilter(request.getParameter("Action")); }
    if(wc.isSet(request.getParameter("Input"))) { postData = wc.basicInputFilter(request.getParameter("Input")); }
    if(wc.isSet(request.getParameter("Resolution"))) { resolution = wc.basicInputFilter(request.getParameter("Resolution")); }
    if(wc.isSet(request.getParameter("LegacyPath"))) { legacyPath = wc.basicInputFilter(request.getParameter("LegacyPath")); }
    %>
    
    <script>
        doAction = "<% out.print(action); %>";
        dataInput = "<% out.print(postData); %>";
        iRes = "<% out.print(resolution); %>";
        legacyPath = "<% out.print(legacyPath); %>";
        console.log("DEBUG VARS: doAction='" + doAction + "', dataInput='" + dataInput + "', iRes='" + iRes + "', legacyPath='" + legacyPath + "'");
    </script>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

    <link rel="stylesheet" type='text/css' href="/asWeb/jsLib/OpenLayers/v5.3.0-dist/ol.css"/>
    <!-- <link rel="stylesheet" type='text/css' href="/asWeb/jsLib/OpenLayers/ol-popup.css"/> -->
    <script src="/asWeb/jsLib/OpenLayers/v5.3.0-dist/ol.js"></script>
    <!-- <script src="/asWeb/jsLib/OpenLayers/ol-popup.js"></script> -->
    <!-- <script src="/asWeb/jsLib/OpenLayers/ol-debug.js"></script> -->
    
<jsp:include page="/inc/Header.jsp?type=full&title=OLMap&scripts=true"></jsp:include>

    <body id="MapPage">
    
        <div id="OLMapHolder"></div>
        
    </body>

<jsp:include page="/inc/Footer.jsp"></jsp:include>