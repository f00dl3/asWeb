<%-- 
    Document   : login
    Created on : Feb 10, 2018, 1:09:23 PM
    Author     : astump

session.setAttribute("userLoggedIn", user);

--%>

<%
    String headerType = "full";
    String pageTitle = "asWeb (Java)";
    String scriptIt = "true";
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/inc/Header.jsp?type=full&title=login&scripts=true"></jsp:include>

    <body>
        
        <h1><% out.println(pageTitle); %></h1>
        
        <% 
            String remoteIpAddr = request.getRemoteAddr();
            String userAgent = request.getHeader("user-agent");
        %>
        
        <form id="loginForm" method="post">
            <div class="table">
                <div class="tr"><span class="td">User</span><span class="td"><input type="text" name="asUser"></input></span></div>
                <div class="tr"><span class="td">Password</span><span class="td"><input type="password" name="asPass"></input></span></div>
            </div>
            <p><button class="UButton" id="btnLogin" type="submit">Login!</button>
        </form>
        
        <strong>Version: </strong><span id="webVersion"></span><br/>
        <strong>Your IP: </strong><% out.println(remoteIpAddr); %><br/>
        <strong>Browser: </strong><% out.println(userAgent); %><br/>
        Last login from <span id="lastUser"></span> at <span id="lastTime"></span> from <span id="lastIP"></span><br/>       
        <strong>Debug 1: </strong>${loginCheck}<br/>
        <strong>Debug 2: </strong><span id="debug2"></span><br/>
        <strong>Debug 3: </strong><span id="debug3"></span><br/>
        
    </body>

<jsp:include page="/inc/Footer.jsp"></jsp:include>