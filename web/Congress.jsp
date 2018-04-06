<%-- 
    Document   : Anthony
    Created on : 6 Apr 2018
    Author     : astump
--%>

<%
    String headerType = "full";
    String pageTitle = "Congress";
    String scriptIt = "true";
    String authCheck = "true";
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/inc/Header.jsp?type=full&title=Congress&scripts=true"></jsp:include>

    <body>
        
        <div id="CongressHolder"></div>
             
    </body>

<jsp:include page="/inc/Footer.jsp"></jsp:include>