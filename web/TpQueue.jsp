<%-- 
    Document   : TPQueue
    Adapted on : Apr 7, 2018, 8:58:02 PM
    Author     : astump
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><c:forEach items="${tpQueues}" var="tpQueue">${tpQueue.webLink} </c:forEach>