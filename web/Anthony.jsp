<%-- 
    Document   : Anthony
    Created on : Feb 11, 2018, 1:37:57 PM
    Author     : astump
--%>

<%
    String headerType = "full";
    String pageTitle = "Anthony's RESTful WebUI";
    String scriptIt = "true";
    String authCheck = "true";
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/inc/Header.jsp?type=full&title=Anthony&scripts=false"></jsp:include>

    <body>
        
        <div id='MarqPH'><jsp:include page="/inc/ObsMarq.jsp"></jsp:include></div>
        
        <p/>
        
        <h1><% out.println(pageTitle); %></h1>
             
    </body>

<jsp:include page="/inc/Footer.jsp"></jsp:include>