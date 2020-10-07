<%-- 
    Document   : Anthony
    Created on : 6 Oct 2020
    Updated: 7 Oct 2020
    Author     : astump
--%>

<%@ page import="asWebRest.shared.WebCommon" %>

<%
    
    String headerType = "full";
    String pageTitle = "Charts3";
    String scriptIt = "true";
    String authCheck = "true";
    String action = "";
    String postData = "";
    String resolution = "";
    
    WebCommon wc = new WebCommon();
       
    %>
    
    <script>
        var doAction,
            dataInput,
            iRes,
            legacyPath;
    </script>
    
    <%
    if(wc.isSet(request.getParameter("Action"))) { action = wc.basicInputFilter(request.getParameter("Action")); }
    if(wc.isSet(request.getParameter("Input"))) { postData = wc.basicInputFilter(request.getParameter("Input")); }
    if(wc.isSet(request.getParameter("Resolution"))) { resolution = wc.basicInputFilter(request.getParameter("Resolution")); }
    %>
    
    <script>
        doAction = "<% out.print(action); %>";
        dataInput = "<% out.print(postData); %>";
        iRes = "<% out.print(resolution); %>";
        console.log("DEBUG VARS: doAction='" + doAction + "', dataInput='" + dataInput + "', iRes='" + iRes);
    </script>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
    
<jsp:include page="/inc/Header.jsp?type=full&title=Charts3&scripts=true"></jsp:include>

    <body id="Charts">
    
        <div id="ChartHolder" style="height: 80%; width: 95%;">Load not called!</div>
        
    </body>

<jsp:include page="/inc/Footer.jsp"></jsp:include>