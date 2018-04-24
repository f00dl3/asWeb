<%-- 
    Document   : Address.jsp
    Created on : 22 Apr 2018
    Updated: 23 Apr 2018
    Author     : astump
--%>

<%
    String headerType = "full";
    String pageTitle = "Address Book";
    String scriptIt = "true";
    String authCheck = "true";
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<jsp:include page="/inc/Header.jsp?type=full&title=Address&scripts=true"></jsp:include>

    <body>
              
        <h1>Address Book</h1>
        
        <div id="HeadHolder"></div><br/>
        <div id="SearchHolder"></div><p>
        <div class='table' id="ResultHolder"></div><br/>
        <div id="CountHolder"></div>
             
    </body>

<jsp:include page="/inc/Footer.jsp"></jsp:include>