<%-- 
    Document   : Anthony
    Created on : 29 May 2018
    Updated: 31 May 2018
    Author     : astump
--%>

<%@ page import="asWebRest.shared.WebCommon" %>

<%
    
    String headerType = "full";
    String pageTitle = "OLMap";
    String scriptIt = "true";
    String authCheck = "true";
    
    WebCommon wc = new WebCommon();
    
    if(wc.isSet(request.getParameter("action")) /* && wc.isSet(request.getParameter("dataInput")) */) {
        String action = wc.basicInputFilter(request.getParameter("action"));
        String postData = "test"; /* wc.basicInputFilter(request.getParameter("dataInput")); */
        %><script>
            var doAction = "<% out.print(action); %>";
            var dataInput = "<% out.print(postData); %>";
        </script><%
    }
    
%>

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