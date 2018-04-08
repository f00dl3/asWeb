<%-- 
    Document   : Header
    Created on : Feb 12, 2018, 7:39:30 AM
    Updated:    8 Apr 2018
    Author     : astump
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<% 
    
    String rootPath = "/asWeb";
    String title = request.getParameter("title");
    String fullTitle = title;
    String type = request.getParameter("type");
    String scripts = request.getParameter("scripts");
    String scripts2Load = "";
    String refresh = request.getParameter("refresh");
    String[] finScripts = { "FBook/Overview", "FBook/Assets", "FBook/Auto", "FBook/Bills", "FBook/Blue", "FBook/CkBk", "FBook/Pto", "FBook/Utils" };
    String[] fitScripts = { "Fitness/Plans", "Fitness/Calories", "Fitness/Today" };
    String[] wxScripts = { "Weather/WxFunctions", "Weather/ObsFeed" };
    String[] css2do = { "Master", "Colors", "3DTransforms", "Preloader" };
    
    if(title == null) { title = "asWeb"; }
    if(type == null) { type = "full"; }
    if(scripts == null) { scripts = "false"; }
    
    if(type.equals("full")) { 
        scripts2Load += "<script src='"+rootPath+"/jsLib/dojo/dojo/dojo.js'></script>" +
                "<script src='"+rootPath+"/jsLib/jQuery/jquery-3.3.1.min.js'></script>" +
                "<script src='"+rootPath+"/jsLib/jQuery/jquery.marquee.min.js'></script>" +
                "<script src='"+rootPath+"/jsBase/Header.js'></script>" +
                "<script src='"+rootPath+"/jsBase/comSec.js'></script>" +
                "<script src='"+rootPath+"/jsBase/Tools/CookieMgmt.js'></script>";
    }
    
    String preloadElement = "<div class='preload'><img src='"+rootPath+"/img/Preload/5-1.gif'/><br>" +
            " <strong><span id='preloadSize'>Loading...</span></strong></div>";
    
    String cssFiles = "";
    for(int i=0; i < css2do.length; i++) {
        cssFiles += "<link rel='stylesheet' type='text/css' href='"+rootPath+"/css/"+css2do[i]+".css'/>";
    }
    
    String fbScriptPack = "";
    for(int i=0; i < finScripts.length; i++) {
        fbScriptPack += "<script src='"+rootPath+"/jsBase/"+finScripts[i]+".js'></script>";
    }
    
    String fitScriptPack = "";
    for(int i=0; i < fitScripts.length; i++) {
        fitScriptPack += "<script src='"+rootPath+"/jsBase/"+fitScripts[i]+".js'></script>";
    }
    
    String wxScriptPack = "<script src='"+rootPath+"/jsLib/sun-js-master/sun.js'></script>";
    for(int i=0; i < wxScripts.length; i++) {
        wxScriptPack += "<script src='"+rootPath+"/jsBase/"+wxScripts[i]+".js'></script>";
    }

    String mapHelperScripts = "<script src='"+rootPath+"/jsBase/MapFunc/MapInit.js'></script>";
    
    if(scripts.equals("true")) {
        switch(title) {
            case "Anthony":
                scripts2Load += wxScriptPack;
                break;
            case "Cams":
                scripts2Load += wxScriptPack;
                break;
            case "Entertain":
                fullTitle = "Entertainment";
                scripts2Load += "<script src='"+rootPath+"/jsBase/MediaServ.js'></script>";
                break;
            case "FBook":
                fullTitle = "Finance Book";
                scripts2Load += fbScriptPack;
                break;
            case "Fitness":
                scripts2Load += "<script src='"+rootPath+"/jsBase/Weather/WxFunctions.js'></script>" +
                        fitScriptPack;
                break;
            case "OutMap":
                fullTitle = "Maps";
                scripts2Load += mapHelperScripts;
            case "WxLive":
                fullTitle = "Weather Live";
                scripts2Load += wxScriptPack;
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
        " <head><title>asWeb " + fullTitle + "</title>" +
        " <meta charset='UTF-8'>" +
        " <meta name='viewport' content='width=device-width, initial-scale=1.0'>" +
        " " + cssFiles + " " + preloadElement +
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

