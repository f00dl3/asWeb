<%-- 
    Document   : Anthony.jsp
    Created on : Feb 11, 2018, 1:37:57 PM
    Updated:    20 Apr 2018
    Author     : astump
--%>

<%
    String headerType = "full";
    String pageTitle = "Anthony's REST API";
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
        
        <h1 id="mainH1"><% out.println(pageTitle); %></h1>
            
        <span id="versionPlaceholder"></span> <button id='Sh_inLogs' class='UButton'>Logs</button><p>
    
        <div id="sshLinks"></div><br/>
        <div id="linkList"></div><p>
        
        <div id="inLogs"></div>
             
    </body>

<jsp:include page="/inc/Footer.jsp"></jsp:include>