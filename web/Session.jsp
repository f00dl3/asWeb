<%-- 
    Document   : Session.jsp
    Created on : 17 Apr 2018
    Author     : astump
--%>

<%
    String sessionVariables = (String) request.getAttribute("sessionVariables");
    out.println(sessionVariables);
%>