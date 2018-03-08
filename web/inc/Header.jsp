<%-- 
    Document   : Header
    Created on : Feb 12, 2018, 7:39:30 AM
    Updated:    7 Mar 2018
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
    String refresh = request.getParameter("refresh");
    
    if(title == null) { title = "asWeb"; }
    if(type == null) { type = "full"; }
    if(scripts == null) { scripts = "false"; }
    
    if(scripts.equals("true")) {
        scripts = "<script src='"+rootPath+"/js/Base/"+title+".js'></script>";
    } else {
        scripts = "";
    }
    
    String theHeader = "";
    String miniHeader = "";
    
%>

    <script>

    if(typeof scLd !== "undefined") {
        if(!scLd("dojo/dojo/dojo.js")) { <% miniHeader += "<script src='"+rootPath+"/js/dojo/dojo/dojo.js'></script>"; %> }
        if(!scLd("jQuery/jquery-3.2.1.min.js")) { <% miniHeader += "<script src='"+rootPath+"/js/jQuery/jquery-3.2.1.min.js'></script>"; %> }
        if(!scLd("Base/Header.js")) { <% miniHeader += "<script src='"+rootPath+"/js/Base/Header.js'></script>"; %> }
        if(!scLd("Base/CookieMgmt.js")) { <% miniHeader += "<script src='"+rootPath+"/js/Base/CookieMgmt.js'></script>"; %> }
    }

    </script>
        
<% 
    
    miniHeader += scripts;
    
    if(refresh != null) {
        miniHeader += "<script>";
        miniHeader += "doRefresh(" + refresh + ");";
        miniHeader += "</script>";
    }
    
    String fullHeader = "<title>asWeb " + title + "</title>"
        + "<meta charset='UTF-8'>"
        + "<meta name='viewport' content='width=device-width, initial-scale=1.0'>"
        + "<link rel='stylesheet' type='text/css' href='"+rootPath+"/css/Master.css'/>"
        + miniHeader;
    
    if(type.equals("full")) {
        theHeader = fullHeader;
    } else {
        theHeader = miniHeader;
    }
    
    out.println(theHeader);
    
%>

