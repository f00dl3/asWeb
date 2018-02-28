<%-- 
    Document   : Header
    Created on : Feb 12, 2018, 7:39:30 AM
    Author     : astump
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns=http://www.w3.org/1999/xhtml">
    
<head>

<% 
    
    String rootPath = "/asWeb";
    String title = request.getParameter("title");
    String type = request.getParameter("type");
    String scripts = request.getParameter("scripts");
    
    if(title == null) { title = "asWeb"; }
    if(type == null) { type = "full"; }
    if(scripts == null) { scripts = "false"; }
    
    if(scripts.equals("true")) {
        scripts = "<script src='"+rootPath+"/js/Base/"+title+".js'></script>";
    } else {
        scripts = "";
    }
    
    String theHeader = "";
    String miniHeader = "<script src='"+rootPath+"/js/dojo/dojo/dojo.js'></script>" +
            "<script src='"+rootPath+"/js/jQuery/jquery-3.2.1.min.js'></script>";
    String fullHeader = "<title>" + title + "</title>"
        + "<meta charset='UTF-8'>"
        + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
        + "<link rel='stylesheet' type='text/css' href='"+rootPath+"/css/Master.css'/>"
        + miniHeader + scripts;
    
    if(type.equals("full")) {
        theHeader = fullHeader;
    } else {
        theHeader = miniHeader;
    }
    
    out.println(theHeader);
    
%>

