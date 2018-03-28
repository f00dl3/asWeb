<%-- 
    Document   : Header
    Created on : Feb 12, 2018, 7:39:30 AM
    Updated:    28 Mar 2018
    Author     : astump
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<% 
    
    String rootPath = "/asWeb";
    String title = request.getParameter("title");
    String type = request.getParameter("type");
    String scripts = request.getParameter("scripts");
    String scripts2Load = "";
    String refresh = request.getParameter("refresh");
    
    if(title == null) { title = "asWeb"; }
    if(type == null) { type = "full"; }
    if(scripts == null) { scripts = "false"; }
    
    if(type.equals("full")) { 
        scripts2Load += "<script src='"+rootPath+"/jsLib/dojo/dojo/dojo.js'></script>" +
        "<script src='"+rootPath+"/jsLib/jQuery/jquery-3.2.1.min.js'></script>" +
        "<script src='"+rootPath+"/jsLib/jQuery/jquery.marquee.min.js'></script>" +
        "<script src='"+rootPath+"/jsBase/Header.js'></script>" +
        "<script src='"+rootPath+"/jsBase/comSec.js'></script>" +
        "<script src='"+rootPath+"/jsBase/CookieMgmt.js'></script>";
    }
    
    if(scripts.equals("true")) {
        switch(title) {
            case "Anthony":
                scripts2Load += "<script src='"+rootPath+"/jsLib/sun-js-master/sun.js'></script>" +
                        "<script src='"+rootPath+"/jsBase/WxFunctions.js'></script>" +
                        "<script src='"+rootPath+"/jsBase/ObsFeed.js'></script>";
                break;
            case "Entertain":
                scripts2Load += "<script src='"+rootPath+"/jsBase/MediaServ.js'></script>";
                break;
            case "Fitness":
                scripts2Load += "<script src='"+rootPath+"/jsBase/WxFunctions.js'></script>";
                break;
            case "WxLive":
                scripts2Load += "<script src='"+rootPath+"/jsBase/ObsFeed.js'></script>" +
                        "<script src='"+rootPath+"/jsLib/sun-js-master/sun.js'></script>" +
                        "<script src='"+rootPath+"/jsBase/WxFunctions.js'></script>";
                break;
        }
        scripts2Load += "<script src='"+rootPath+"/jsBase/"+title+".js'></script>";
    } 

    String theHeader = "";
    String miniHeader = "";
    
    miniHeader += scripts2Load;
    
    if(refresh != null) {
        miniHeader += "<script>";
        miniHeader += "doRefresh(" + refresh + ");";
        miniHeader += "</script>";
    }
    
    String fullHeader = "<!DOCTYPE html PUBLIC '-//W3C//DTD XHTML 1.0 Transitional//EN' 'http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd'>" +
        " <html xmlns='http://www.w3.org/1999/xhtml'>" +    
        " <head><title>asWeb " + title + "</title>" +
        " <meta charset='UTF-8'>" +
        " <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
        " <link rel='stylesheet' type='text/css' href='"+rootPath+"/css/Master.css'/>" +
        " <link rel='stylesheet' type='text/css' href='"+rootPath+"/css/Colors.css'/>" +
        " <link rel='stylesheet' type='text/css' href='"+rootPath+"/css/3DTransforms.css'/>" +
        " <link rel='stylesheet' type='text/css' href='"+rootPath+"/css/Preloader.css'/>" +
        " <div class='preload'><img src='"+rootPath+"/img/Preload/5-1.gif'/><br>" +
        " <strong><span id='preloadSize'></span></strong></div>" +
        " <div id='NoticeHolder'></div>" +
        " <div id='NaviHolder'></div>" +
        miniHeader;
    
    if(type.equals("full")) {
        theHeader = fullHeader;
    } else {
        theHeader = miniHeader;
    }
    
    out.println(theHeader);
    
%>

