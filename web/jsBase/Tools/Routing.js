/* 
by Anthony Stump
Created: 4 Mar 2018
Split from Header.js 7 Oct 2020
Updated: 7 Oct 2020
 */

function getBasePath(opt) {
    var base = self.location.protocol + "//" + self.location.host;
    var baseForUi = "/asWeb";
    var shortBaseForRestlet = baseForUi + "/r";
    var baseForRestlet = base + baseForUi + "/r";
    var baseForServlet = base + baseForUi + "/s";
    var tBase = "";
    tBase = base.split(":")[1];
    if(checkMobile()) { tBase += ":8082"; }
    tBase = "https:" + tBase;
    switch(opt) {
        case "chartCache": tBase = baseForUi + "/cache"; break;
        case "congress": tBase = baseForUi + "/img/CongressHack"; break;
        case "downloads": tBase = baseForUi + "/Download"; break;
        case "get2": tBase = baseForUi + "/Get2"; break;
        case "g2OutOld": case "g2Out": tBase = baseForUi + "/G2Out"; break;
        case "icon": tBase = baseForUi + "/img/Icons"; break;
        case "image": tBase = baseForUi + "/img"; break;
        case "media": tBase = baseForUi + "/MediaServer"; break;
        case "rest": tBase = baseForRestlet; break;
        case "root": tBase = base; break;
        case "serv": tBase = baseForServlet; break;
        case "old": tBase += "/ASWebUI"; break;
        case "oldGet": case "getOld": case "getOldGet": case "get": tBase = baseForUi + "/Get"; break;
        case "oldRoot": tBase = tBase; break;
        case "osmTiles": tBase += "/osm_tiles/"; break;
        case "pageSnaps": tBase = baseForUi + "/img/PageSnaps"; break;
        case "tomcatOld": tBase = baseForUi + "/x"; break;
        case "ui": tBase = baseForUi; break;
    }
    return tBase;
}

function getResource(what) {
    switch(what) {
        case "Addresses": return getBasePath("rest") + "/Addresses"; break;
        case "Anthony": case "f00dl3": return getBasePath("ui") + "/Anthony.jsp"; break;
        case "Automotive": return getBasePath("rest") + "/Automotive"; break;
        case "Cams": return getBasePath("ui") + "/Cams.jsp"; break;
        case "Chart": return getBasePath("rest") + "/Chart"; break;
        case "Chart3": return getBasePath("rest") + "/Chart3"; break;
        case "Congress": return getBasePath("rest") + "/Congress"; break;
        case "Cooking": return getBasePath("rest") + "/Cooking"; break;
        case "CrashData": return getBasePath("rest") + "/CrashData"; break;
        case "DBInfo": return getBasePath("rest") + "/DBInfo"; break;
        case "Download": return getBasePath("serv") + "/Download"; break;
        case "Emily": return getBasePath("ui") + "/Emily.jsp"; break;
        case "Entertainment": return getBasePath("rest") + "/Entertainment"; break;
        case "FFXIV": return getBasePath("rest") + "/FFXIV"; break;
        case "Fitness": return getBasePath("rest") + "/Fitness"; break;
        case "Finance": return getBasePath("rest") + "/Finance"; break;
        case "Homicide": return getBasePath("rest") + "/Homicide"; break;
        case "Home": return getBasePath("rest") + "/Home"; break;
        case "Landing": return getBasePath("ui"); break;
        case "Login": return getBasePath("rest") + "/Login"; break;
        case "Logs": return getBasePath("rest") + "/Logs"; break;
        case "Map.Addresses": return getBasePath("ui") + "/OLMap.jsp?Action=Addresses"; break;
        case "Map.Homicide": return getBasePath("ui") + "/OLMap.jsp?Action=Homicide"; break;
        case "Map.Media": return getBasePath("ui") + "/OLMap.jsp?Action=Media"; break;
        case "Map.Point": return getBasePath("ui") + "/OLMap.jsp?Action=Point"; break;
        case "Map.Wx": return getBasePath("ui") + "/OLMap.jsp?Action=Wx"; break;
        case "MediaServer": return getBasePath("rest") + "/MediaServer"; break;
        case "NewsFeed": return getBasePath("rest") + "/NewsFeed"; break;
        case "Physicals": return getBasePath("ui") + "/Physicals.jsp"; break;
        case "Pto": return getBasePath("rest") + "/PTO"; break;
        case "Session": return getBasePath("serv") + "/Session"; break;
        case "Smarthome": return getBasePath("rest") + "/Smarthome"; break;
        case "SNMP": return getBasePath("rest") + "/SNMP"; break;
        case "Stock": return getBasePath("rest") + "/Stock"; break;
        case "Test": return getBasePath("rest") + "/Test"; break;
        case "Tools": return getBasePath("rest") + "/Tools"; break;
        case "TP": return getBasePath("rest") + "/TP"; break;
        case "Upload": return getBasePath("rest") + "/Upload"; break;
        case "WebCal": return getBasePath("rest") + "/WebCal"; break;
        case "WebLinks": return getBasePath("rest") + "/WebLinks"; break;
        case "WebVersion": return getBasePath("rest") + "/WebVersion"; break;
        case "Wx": return getBasePath("rest") + "/Wx"; break;
        case "WxStation": return getBasePath("ui") + "/WxStation.jsp"; break;
    }
}

function getServerPath(what) {
    switch(what) {
        case "apache2": return "/var/www"; break;
        case "mediaServer": return "/extra1/MediaServer"; break;
        case "rawGet": return "/var/www/Get"; break;
        case "rawGet2": return "/media/sf_SharePoint/Get"; break;
        case "tomcat": return "/var/lib/tomcat8/webapps"; break;
        case "tomcat9": return "/var/lib/tomcat9/webapps"; break;
    }
}