<%-- 
    Document   : FlashLoader.jsp
    Created on : 9 Dec 2018
    Author     : astump
--%>

<%@page import="asWebRest.shared.WebCommon"%>
<%
    String headerType = "full";
    String pageTitle = "Flash Loader";
    String scriptIt = "true";
    String authCheck = "true";
    String flashFile = "NONE";
    WebCommon wc = new WebCommon();
    if(wc.isSet(request.getParameter("ff"))) {
        flashFile = request.getParameter("ff");
        pageTitle = pageTitle + " (" + flashFile + ")";
    }
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/inc/Header.jsp?type=full&title=FlashLoader&scripts=true"></jsp:include>

    <script>var flashFile = "<% out.print(flashFile); %>";</script>

    <body>
        
        <h1><% out.println(pageTitle); %></h1>
        <div id="flashFileHolder">Flash failed to load!</div>
             
    </body>

<jsp:include page="/inc/Footer.jsp"></jsp:include>