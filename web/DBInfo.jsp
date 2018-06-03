<%-- 
    Document   : DBInfo.jsp
    Created on : 3 Jun 2018
    Author     : astump
--%>

<%
    String headerType = "full";
    String pageTitle = "Database Info";
    String scriptIt = "true";
    String authCheck = "true";
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/inc/Header.jsp?type=full&title=DBInfo&scripts=true"></jsp:include>

    <body>
              
        <h1>MySQL Details</h1>
        
        <div id="dbOverviewHolder"></div>
        <div id="dbNoteHolder"></div>
        <div id="thisDbNote"></div>
             
    </body>

<jsp:include page="/inc/Footer.jsp"></jsp:include>