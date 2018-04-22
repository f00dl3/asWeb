<%-- 
    Document   : Clock.jsp
    Adapted on : Apr 7, 2018
    Author     : astump
--%>

<%@ page import="java.io.*, java.util.*" %>

<%

	response.setIntHeader("Refresh", 1);
	Calendar calendar = new GregorianCalendar();
	String am_pm;
	int hour = calendar.get(Calendar.HOUR);
	int minute = calendar.get(Calendar.MINUTE);
	int second = calendar.get(Calendar.SECOND);
	if(calendar.get(Calendar.AM_PM) == 0) am_pm = "AM";
	else am_pm = "PM";
	String CT = hour + ":" + minute + ":" + second + " " + am_pm;
	out.println("Time is " + CT);
	
%>
