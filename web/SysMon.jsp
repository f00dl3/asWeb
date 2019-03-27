<%-- 
    Document   : SysMon.jsp
    Created on : 20 Apr 2018
    Updated: 23 Mar 2019
    Author     : astump
    DESIRED REFRESH 120 SECONDS OR 360 SECONDS IF DATA PULLS ARE HIGH
    ^ TO BE HANDLED IN JAVASCRIPT THE NEW WAY NOW THAT THIS IS RESTFUL!
--%>

<%
    String headerType = "full";
    String pageTitle = "Anthony's SNMP+ Tools";
    String scriptIt = "true";
    String authCheck = "true";
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/coqre" prefix="c" %>
<jsp:include page="/inc/Header.jsp?type=full&title=SysMon&scripts=true"></jsp:include>

    <body>
        
        <h1><% out.println(pageTitle); %></h1>
    
        <div id="snmpDataRapidHolder"></div><br/>
        <div id="databaseLiveHolder"></div>
        <div id="snmpStatusHolder"></div>
        
        <h4>Charts</h4>
        <div id="chartPlacement"></div>
        
        <h4>eDiscovery</h4>
        <div id="eDiscoveryHolder"></div>
        
        <h4>ReliaStump</h4>
        <div id="ReliaStumpHolder"></div>
             
    </body>

<jsp:include page="/inc/Footer.jsp"></jsp:include>