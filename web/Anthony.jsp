<%-- 
    Document   : Anthony
    Created on : Feb 11, 2018, 1:37:57 PM
    Updated:    26 Mar 2018
    Author     : astump
--%>

<%
    String headerType = "full";
    String pageTitle = "Anthony's REST UI";
    String scriptIt = "true";
    String authCheck = "true";
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/inc/Header.jsp?type=full&title=Anthony&scripts=true"></jsp:include>

    <body>
        
        <div id='MarqPH'>
            <div id='disHolder'></div>
        </div>
        
        <p/>
        
        <h1><% out.println(pageTitle); %></h1>
        <div id="versionPlaceholder"></div><button id='Sh_inLogs' class='UButton'>Logs</button>
    
        <div id='linkList'></div>
             
    </body>

<jsp:include page="/inc/Footer.jsp"></jsp:include>