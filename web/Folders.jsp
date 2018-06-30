<%-- 
    Document   : Folders.jsp
    Created on : 30 Jun 2018
    Author     : astump
--%>

<%
    String headerType = "full";
    String pageTitle = "Folders and Files";
    String scriptIt = "true";
    String authCheck = "true";
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/inc/Header.jsp?type=full&title=Folders&scripts=true"></jsp:include>

    <body>
        
        <h1><% out.println(pageTitle); %></h1>
            
        <div id="ffListHolder"></div><br/>
             
    </body>

<jsp:include page="/inc/Footer.jsp"></jsp:include>