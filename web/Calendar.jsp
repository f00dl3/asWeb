<%-- 
    Document   : Anthony
    Created on : 16 Dec 2019
    Updated:    22 Dec 2019
    Author     : astump
--%>

<%
    String headerType = "full";
    String pageTitle = "Calendar";
    String scriptIt = "true";
    String authCheck = "true";
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/inc/Header.jsp?type=full&title=Calendar&scripts=true" />

    <body>
    
        <h1><% out.println(pageTitle); %></h1>
            
        <div id='qcHolder'></div><br/>
        <div id='calendarViewHolder'></div>
             
    </body>

<jsp:include page="/inc/Footer.jsp"></jsp:include>