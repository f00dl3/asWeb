<%-- 
    Document   : Anthony
    Created on : 14 Mar 2018
    Updated:    14 Mar 2018
    Author     : astump
--%>

<%
    String headerType = "full";
    String pageTitle = "Fitness";
    String scriptIt = "true";
    String authCheck = "true";
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/inc/Header.jsp?type=full&title=Fitness&scripts=true"></jsp:include>

    <body>
        
        <h1><% out.println(pageTitle); %></h1>
    
        <table><tbody id="fitnessTable"></tbody></table>
             
    </body>

<jsp:include page="/inc/Footer.jsp"></jsp:include>