<%-- 
    Document   : Folders.jsp
    Created on : 30 Jun 2018
    Updated:    1 Jul 2018
    Author     : astump
--%>

<%@page import="asWebRest.shared.WebCommon"%>
<%
    String headerType = "full";
    String pageTitle = "Folders and Files";
    String scriptIt = "true";
    String authCheck = "true";
    String folderOverride = "/";
    WebCommon wc = new WebCommon();
    if(wc.isSet(request.getParameter("folderInput"))) { folderOverride = request.getParameter("folderInput"); }
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<script>var folderOverride = "<% out.print(folderOverride); %>";</script>

<jsp:include page="/inc/Header.jsp?type=full&title=Folders&scripts=true"></jsp:include>

    <body>
        
        <h1><% out.println(pageTitle); %></h1>
        <div id="ffListHolder"></div>
             
    </body>

<jsp:include page="/inc/Footer.jsp"></jsp:include>