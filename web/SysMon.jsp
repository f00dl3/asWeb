<%-- 
    Document   : SysMon.jsp
    Created on : 20 Apr 2018
    Author     : astump
    DESIRED REFRESH 120 SECONDS OR 360 SECONDS IF DATA PULLS ARE HIGH
--%>

<%
    String headerType = "full";
    String pageTitle = "Anthony's jSNMP+ Tools";
    String scriptIt = "true";
    String authCheck = "true";
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/inc/Header.jsp?type=full&title=SysMon&scripts=true"></jsp:include>

    <body>
        
        <h1><% out.println(pageTitle); %></h1>
    
        <div id="snmpDataRapidHolder"></div><br/>
        <div id="snmpStatusHolder"></div>
        
        <h4>Charts</h4>
        <div id="chartPlacement"></div>
        
        <h4>eDiscovery</h4>
        <div id="eDiscoveryHolder"></div>
        
        <h4>ReliaStump</h4>
        <div id="ReliaStumpHolder"></div>
             
    </body>

<jsp:include page="/inc/Footer.jsp"></jsp:include>