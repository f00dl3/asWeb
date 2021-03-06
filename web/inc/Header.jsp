<%-- 
    Document   : Header.jsp
    Created on : Feb 12, 2018, 7:39:30 AM
    Updated:    16 Apr 2021
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
    
    String fullBasePack = "<script src='"+rootPath+"/jsLib/jQuery/jquery-3.5.1.min.js'></script>" +
		"<script src='"+rootPath+"/jsLib/eruda/src/eruda.js'></script>" +
            "<script src='"+rootPath+"/jsLib/jQuery/jquery.marquee.min.js'></script>" +
			"<script src='"+rootPath+"'/jsLib/hammer.min.js'></script>" +
            "<script src='"+rootPath+"/jsLib/moment/moment.min.js'></script>" +
			"<script src='"+rootPath+"/jsLib/chart.js/2.9.4/Chart.min.js'></script>" +
            "<script src='"+rootPath+"/jsLib/chart.js/chartjs-plugin-zoom.min.js'></script>" +
            "<script src='"+rootPath+"/jsLib/dojo/1.16.3/dojo/dojo.js'></script>" +
            "<script src='"+rootPath+"/jsBase/Tools/comSec.js'></script>" +
            "<script src='"+rootPath+"/jsBase/Tools/Routing.js'></script>" +
            "<script src='"+rootPath+"/jsBase/Tools/Session.js'></script>" +
            "<script src='"+rootPath+"/jsBase/Header.js'></script>";

    String[] calScripts = {
   		"Calendar/ByDay",
   		"Calendar/ByMonth",
   		"Calendar/Shared"
    };
    
    String[] chart3Scripts = {
     	"Charts/Bills",
   		"Charts/Entertainment",
   		"Charts/Ffxiv",
   		"Charts/Finance",
		"Charts/Fitness",
		"Charts/SNMP",
		"Charts/Stocks",
		"Charts/SysMonDesktop",
   		"Charts/Test",
   		"Charts/Utils",
   		"Charts/Weather"
    };
    
    String[] etScripts = {
    	"Charts/Entertainment",
    	"Charts/Ffxiv",
        "Entertain/ChicagoSeries",
        "Entertain/Cooking",
        "Entertain/Dbx",
        "Entertain/Ffxiv",
        "Entertain/Games",
        "Entertain/Gallery",
        "Entertain/Goosebumps",
        "Entertain/Lego",
        "Entertain/MediaServ",
        "Entertain/PowerRangers",
        "Entertain/Reddit",
        "Entertain/StarTrek",
        "Entertain/TrueBlood",
        "Entertain/XFiles"
    };
    
    String[] finScripts = {
        "Charts/Bills",
   		"Charts/Finance",
		"Charts/Stocks",
   		"Charts/Utils",
        "FBook/Overview",
        "FBook/Assets",
        "FBook/Auto",
        "FBook/AutoHC",
        "FBook/Auto20",
        "FBook/Bills",
        "FBook/Blue",
        "FBook/CkBk",
        "FBook/Pto",
        "FBook/Stock",
        "FBook/Utils"
    };
    
    String[] fitScripts = {
   		"Charts/Fitness",
        "Fitness/Calories",
        "Fitness/GarminConnectApiSynch",
        "Fitness/Plans",
        "Fitness/Strength",
        "Fitness/Today",
        "Fitness/Yesterday"
    };
    
    String[] flashScripts = {
        "FlashLoader"
    };
    
    String[] folScripts = {
    };
    
    String[] olMapScripts = {
        "OLMap/AddElements",
        "OLMap/Addresses",
        "OLMap/CommonStuff",
        "OLMap/CrashData",
        "OLMap/DrawPathMap",
        "OLMap/GpsElements",
        "OLMap/Homicide",
        "OLMap/KMLRadar",
        "OLMap/LocationListener",
        "OLMap/MediaGeo",
        "OLMap/PhoneTracker",
        "OLMap/PointClick",
        "OLMap/RenderImage",
        "OLMap/SimpleMap",
        "OLMap/WeatherGeometry",
        "OLMap/WeatherMap",
        "Weather/ObsFeed",
        "Weather/WxFunctions"
    };
    
    String[] snmpScripts = {
   		"SNMP/Smarthome",
        "SNMP/SnmpData",
        "Weather/WxFunctions"
    };
    
    String [] sysMonScripts = {
     	"Charts/SNMP",
  		"Charts/SysMonDesktop",
        "SNMP/ReliaStump"
    };
    
    String[] tpScripts = {
	"Charts/Entertainment",
        "Entertain/Gallery"
    };
    
    String[] wxScripts = {
   		"Charts/Weather",
        "Weather/Archive",
        "Weather/Cf6Data",
        "Weather/Kilaeua",
        "Weather/MosData",
        "Weather/WxFunctions",
        "Weather/WxLiveInside",
        "Weather/ObsFeed"
    };
    
    String[] css2do = {
        "Master",
        "Colors",
        "3DTransforms",
        "Preloader",
        "Responsive",
        "OLFeatures",
        "StumpCal"
    };
    
    if(title == null) { title = "asWeb"; }
    if(type == null) { type = "full"; }
    if(scripts == null) { scripts = "false"; }
    
    if(type.equals("full")) { scripts2Load += fullBasePack; }
    
    String preloadElement = "<div class='preload'><img src='"+rootPath+"/img/Preload/5-1.gif'/><br>" +
            " <strong><span id='preloadSize'>Loading...</span></strong></div>";
            
    String cssFiles = "";
    for(int i=0; i < css2do.length; i++) {
        cssFiles += "<link rel='stylesheet' type='text/css' href='"+rootPath+"/css/"+css2do[i]+".css'/>";
    }
    
    String chart3ScriptPack = "";
    for(int i=0; i < chart3Scripts.length; i++) {
    	chart3ScriptPack += "<script src='"+rootPath+"/jsBase/"+chart3Scripts[i]+".js'></script>";
    }
    
    String calScriptPack = "";
    for(int i=0; i < calScripts.length; i++) {
        calScriptPack += "<script src='"+rootPath+"/jsBase/"+calScripts[i]+".js'></script>";
    }
    
    String etScriptPack = "";
    for(int i=0; i < etScripts.length; i++) {
        etScriptPack += "<script src='"+rootPath+"/jsBase/"+etScripts[i]+".js'></script>";
    }
    
    String fbScriptPack = "";
    for(int i=0; i < finScripts.length; i++) {
        fbScriptPack += "<script src='"+rootPath+"/jsBase/"+finScripts[i]+".js'></script>";
    }
    
    String fitScriptPack = "";
    for(int i=0; i < fitScripts.length; i++) {
        fitScriptPack += "<script src='"+rootPath+"/jsBase/"+fitScripts[i]+".js'></script>";
    }
    
    String flashScriptPack = "";
    for(int i=0; i < flashScripts.length; i++) {
        flashScriptPack += "<script src='"+rootPath+"/jsBase/"+flashScripts[i]+".js'></script>";
    }
    
    String folScriptPack = "";
    for(int i=0; i < folScripts.length; i++) {
        folScriptPack += "<script src='"+rootPath+"/jsBase/"+folScripts[i]+".js'></script>";
    }
    
    String olMapScriptPack = "";
    olMapScriptPack += "<script src='"+rootPath+"/jsLib/sun-js-master/sun.js'></script>";
    for(int i=0; i < olMapScripts.length; i++) {
        olMapScriptPack += "<script src='"+rootPath+"/jsBase/"+olMapScripts[i]+".js'></script>";
    }
        
    String snmpScriptPack = "";
    for(int i=0; i < snmpScripts.length; i++) {
        snmpScriptPack += "<script src='"+rootPath+"/jsBase/"+snmpScripts[i]+".js'></script>";
    }
    
    String sysMonScriptPack = "";
    for(int i=0; i < sysMonScripts.length; i++) {
        sysMonScriptPack += "<script src='"+rootPath+"/jsBase/"+sysMonScripts[i]+".js'></script>";
    }
    
    String tpScriptPack = "";
    for(int i=0; i < tpScripts.length; i++) {
        tpScriptPack += "<script src='"+rootPath+"/jsBase/"+tpScripts[i]+".js'></script>";
    }
    
    String wxScriptPack = "<script src='"+rootPath+"/jsLib/sun-js-master/sun.js'></script>";
    for(int i=0; i < wxScripts.length; i++) {
        wxScriptPack += "<script src='"+rootPath+"/jsBase/"+wxScripts[i]+".js'></script>";
    }

    String mapHelperScripts = "<script src='"+rootPath+"/jsBase/MapFunc/MapInit.js'></script>";
    
    if(scripts.equals("true")) {
        switch(title) {
            case "Anthony":
                scripts2Load += "<script src='"+rootPath+"/jsBase/Calendar.js'>" +
            			calScriptPack + wxScriptPack;
                break;
            case "Cams":
            	//fullTitle = "Smarthome Interface";
                scripts2Load += fbScriptPack + wxScriptPack + snmpScriptPack;
                break;
            case "Calendar":
            	fullTitle = "WebCal Anthony Fork";
                scripts2Load += calScriptPack;
                break;
            case "Charts3":
            	fullTitle = "Dynamic Charts v3";
            	scripts2Load += chart3ScriptPack;
            	break;
            case "DBInfo":
                fullTitle = "Database Info";
                break;
            case "DOSBox":
            	fullTitle = "DOS Box Emulator";
                scripts2Load += "<script src='"+rootPath+"/DOSBox/js-dos.js'>";
                break;            	
            case "Entertain":
                fullTitle = "Entertainment";
                scripts2Load += etScriptPack;
                break;
            case "FBook":
                fullTitle = "Finances";
                scripts2Load += fbScriptPack;
                break;
            case "Fitness":
                scripts2Load += wxScriptPack + fitScriptPack;
                break;
            case "FlashLoader":
                scripts2Load = scripts2Load.replace("jquery-3.3.1.min.js","jquery-2.2.4.min.js");
                scripts2Load += "<script src='"+rootPath+"/jsLib/jQuery/jquery.swfobject.1-1-1.min.js'>" +
                        flashScriptPack;
                fullTitle = "Flash Loader";
                break;
            case "Folders":
                fullTitle = "Folders and Files";
                scripts2Load += folScriptPack;
                break;
            case "OLMap":
                fullTitle = "Maps OL";
                scripts2Load += olMapScriptPack;
                break;
            case "OutMap":
                fullTitle = "Maps";
                scripts2Load += mapHelperScripts;
                break;
            case "Physicals":
                fullTitle = "Physicals";
                break;
            case "Smarthome":
            	fullTitle = "Smart Home";
            	break;
            case "SysMon":
                fullTitle = "SNMP+ Tools";
                scripts2Load += snmpScriptPack + sysMonScriptPack;
                break;
            case "TPGallery":
                fullTitle = "TP Gallery";
                scripts2Load += tpScriptPack;
                break;
            case "WxLive":
                fullTitle = "Weather Live";
                scripts2Load += wxScriptPack;
                break;
            case "WxStation":
                fullTitle = "Weather Stations";
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
    	" <link rel='shortcut icon' src='"+rootPath+"/asWeb/img/asWebREST.png' />" +
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
