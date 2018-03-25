<%-- 
    Document   : MediaServ
    Created on : Mar 19, 2018, 7:35:15 AM
    Updated: 25 Mar 2018
    Author     : Anthony Stump
--%>

<%
    String headerType = "full";
    String pageTitle = "Entertainment";
    String scriptIt = "true";
    String authCheck = "true";
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/inc/Header.jsp?type=full&title=Entertain&scripts=true"></jsp:include>

<body>
    
    <h1><% out.println(pageTitle); %></h1>
    
    <p>
    
    
</body>

<jsp:include page="/inc/Footer.jsp"></jsp:include>
