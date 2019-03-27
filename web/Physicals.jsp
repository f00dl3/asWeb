<%-- 
    Document   : Anthony
    Created on : 27 Mar 2019
    Updated:	on Creation
    Author     : astump
--%>

<%
    String headerType = "full";
    String pageTitle = "Physicals";
    String scriptIt = "true";
    String authCheck = "true";
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/inc/Header.jsp?type=full&title=Physicals&scripts=true"></jsp:include>

    <body>
        
        <h1><% out.println(pageTitle); %></h1>
    
        <div id="physDataPlaceholder"></div>
             
    </body>

<jsp:include page="/inc/Footer.jsp"></jsp:include>