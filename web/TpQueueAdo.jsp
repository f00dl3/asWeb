<%-- 
    Document   : TpQueueAdo
    Adapted on : Apr 7, 2018
    Author     : astump
--%>

<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %><c:forEach items="${tpQueuesADO}" var="tpQueueADO">${tpQueueADO.galName} </c:forEach>
