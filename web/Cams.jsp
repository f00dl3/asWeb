<%-- 
    Document   : Anthony
    Created on : 30 Mar 2018
    Updated:    21 Nov 2020
    Author     : astump
--%>

<%
    String headerType = "full";
    String pageTitle = "Cams";
    String scriptIt = "true";
    String authCheck = "true";
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/inc/Header.jsp?type=full&title=Cams&scripts=true" />

    <body>
        
        <div id='MarqPH'>
            <div id='disHolderCAMS'></div>
        </div><p>
        <div id="finOverviewHolder"></div>
        <div id="camHolder"></div><p>        
        <div id="snmpHolder"></div>
        
            
             
    </body>

<jsp:include page="/inc/Footer.jsp"></jsp:include>