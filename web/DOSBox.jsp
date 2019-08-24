<%-- 
    Document   : DOSBox.jsp
    Created on : 24 Aug 2019
    Author     : astump
--%>

<%@page import="asWebRest.shared.WebCommon"%>
<%
    String headerType = "full";
    String pageTitle = "DOS Box Loader";
    String scriptIt = "true";
    String authCheck = "true";
    WebCommon wc = new WebCommon();
%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<jsp:include page="/inc/Header.jsp?type=full&title=DOSBox&scripts=true"></jsp:include>

    <body>
        
        <h1><% out.println(pageTitle); %></h1>
		  <canvas id="jsdos"></canvas>
		  <script>
		    Dos(document.getElementById("jsdos"), { 
		        wdosboxUrl: "/asWeb/DOSBox/wdosbox.js" 
		    }).ready((fs, main) => {
		      fs.extract("/asWeb/DOSBox/wf/dagger.zip").then(() => {
		        main(["-c", "CD DAGGER", "-c","DFALL.BAT"])
		      });
		    });
		  </script>
	
	</body>

<jsp:include page="/inc/Footer.jsp"></jsp:include>