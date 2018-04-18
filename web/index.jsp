<%-- 
    Document   : index.jsp
    Created on : Feb 10, 2018, 1:09:23 PM
    Updated:    17 Apr 2018
    Author     : astump


--%>

<%
    String headerType = "full";
    String pageTitle = "asWeb (Java REST)";
    String scriptIt = "true";
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/inc/Header.jsp?type=full&title=Landing&scripts=true"></jsp:include>

    <body>
        <h1><% out.println(pageTitle); %></h1>
        
        <% 
            String remoteIpAddr = request.getRemoteAddr();
            String userAgent = request.getHeader("user-agent");
        %>
        
        <div id="loginPlaceholder"></div>
        
        <strong>Version: </strong><span id="webVersion"></span><br/>
        <strong>Your IP: </strong><% out.println(remoteIpAddr); %><br/>
        <strong>Browser: </strong><% out.println(userAgent); %><br/>
        Last login from <span id="lastUser"></span> at <span id="lastTime"></span> from <span id="lastIP"></span><br/>
    </body>

<jsp:include page="/inc/Footer.jsp"></jsp:include>