<%-- 
    Document   : TPGallery.jsp
    Created on : Apr 17, 2018
	Updated: 23 Mar 2019
    Author     : Anthony Stump
--%>

<%
    String headerType = "full";
    String pageTitle = "TP Gallery";
    String scriptIt = "true";
    String authCheck = "true";
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/inc/Header.jsp?type=full&title=TPGallery&scripts=true"></jsp:include>

<body>
    
    <h1><% out.println(pageTitle); %></h1>
    <tt>Best viewed at 1080p resolution or better.</tt><p>
    
    <span id="TPQueueSizeHolder"></span>
    <span id="TPNimsDataHolder"></span><p>
    
    <div id="TPSearchBoxHolder"></div><br/>
        
    <div id="TPLayoutHolder"></div>
    
</body>

<jsp:include page="/inc/Footer.jsp"></jsp:include>
