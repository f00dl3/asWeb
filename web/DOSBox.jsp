<%-- 
    Document   : DOSBox.jsp
    Created on : 24 Aug 2019
    Author     : astump
--%>

<%
    String headerType = "full";
    String pageTitle = "DOS Box";
    String scriptIt = "true";
    String authCheck = "true";
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/inc/Header.jsp?type=full&title=DOSBox&scripts=true"></jsp:include>

    <body>
        
        <h1><% out.println(pageTitle); %></h1>
		  <canvas id="jsdos"></canvas>
	
	</body>

<jsp:include page="/inc/Footer.jsp"></jsp:include>