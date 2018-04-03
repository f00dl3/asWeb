/* 
by Anthony Stump
Created: 30 Mar 2018
Updated: 2 Apr 2018
 */

function getFitnessAllData(inXdt1, inXdt2) {
    aniPreload("on");
    var xdt1, xdt2;
    var oYear = getDate("year", 0, "yearOnly");
    if(isSet(inXdt1)) { xdt1 = inXdt1; } else { xdt1 = getDate("day", -365, "dateOnly"); }
    if(isSet(inXdt2)) { xdt2 = inXdt2; } else { xdt2 = getDate("day", 0, "dateOnly"); }
    var thePostData = "doWhat=getAll&XDT1=" + xdt1 + "&XDT2=" + xdt2 + "&Bicycle=" + bicycleUsed + "&year=" + oYear;
    var xhArgs = {
        preventCache: true,
        url: getResource("Fitness"),
        postData: thePostData,
        handleAs: "json",
        timeout: timeOutMilli,
        load: function(data) {
            onRouteDataLoaded(data);
            aniPreload("off");
        },
        error: function(data, iostatus) {
            aniPreload("off");
            window.alert("xhrGet for All FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
        }
    };
    dojo.xhrPost(xhArgs);
}

function getGpsJsonData(activityType, logDate) {
    aniPreload("on");
    if(!isSet(activityType)) { activityType = "Run"; }
    var thePostData = "doWhat=getGpsJson" +
            "&ActType=" + activityType +
            "&LogDate=" + logDate;
    var xhArgs = {
        preventCache: true,
        url: getResource("Fitness"),
        postData: thePostData,
        handleAs: "json",
        timeout: timeOutMilli,
        load: function(data) {
            onGpsJsonDataLoaded(data);
            aniPreload("off");
        },
        error: function(data, iostatus) {
            aniPreload("off");
            window.alert("xhrGet for GpsJSONData FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
        }
    };
    dojo.xhrPost(xhArgs);
}

function initMaps() {
    var title = "";
    var doMobiLoc = false;
    var doRelatedPhotos = false;
    var resizeFactor, doAniMarker;
    var tilesLocal = getBasePath("osmTiles");
    var tilesRemote = "http://a.tile.openstreetmap.org/";
    var tileServer = tilesLocal;
    var lineColor = "#000000";
    var lineOpacity = 0.75;
    if(tileServer === tilesLocal) { lineColor = "#ffff00"; lineOpacity = 0.4; }
    if(checkMobile()) { resizeFactor = 1; doAniMarker = 0; } else { resizeFactor = 1.5; doAniMarker = 1; }
    if(isSet(getGetParams("AllRoutes"))) { /* unset session 'radarMode' and 'gpsJSON' */ }
    if(isSet(getGetParams("gpsJSON"))) {
        // unset session 'radarMode'
        // Build further
        mapPop("gpsJSON", title);
    }
    if(isSet(getGetParams("Route"))) {
        // unset 'gpsJSON', 'radaraMode' sessvar
        getFitnessRouteData();
    }
    if(isSet(getGetParams("Point"))) {
        // unset 'gpsJSON', 'radaraMode' sessvar
        var params2Pass = {
            "doCoords": true,
            "doRelatedPhotos": true,
            "title": getGetParams("Title"),
            "coord_LON_LAT": getGetParams("Point"),
            "mapType": "Point"
        };
    }
    if(isSet(getGetParams("Image"))) {
        // unset sesvar 'gpsJSON', 'radarMode'
        var image = getGetParams("Image");
        var refresh = 0;
        var iWidth, iHeight;
        var imageUrl = "";
        var tpUpDiv = null;
        switch(image) {
            case "Cams":
                imageUrl = getBasePath("getOldGet") + "/Cams/_Loop.gif";
                refresh = 180;
                iWidth = (281 * resizeFactor);
                iHeight = (280 * resizeFactor);
                break;
            case "Gallery":
                imageUrl = getGetParams("PicPath");
                iWidth = getGetParams("IW");
                iHeight = getGetParmas("IH");
                if(imageUrl.includes("/TPM")) { tpUpDiv = "<div id='TPUpDIV' class='GPSInfo'></div>"; }
                break;
            case "G16IR":
                imageUrl = getBasePath("getOldGet") + "/G16/codCentPlainsWV/Latest/Loop.gif";
                refresh = 100;
                iWidth = (1600 * resizeFactor);
                iHeight = (900 * resizeFactor);
                break;
            case "G16VIS":
                imageUrl = getBasePath("getOldGet") + "/G16/codCentPlainsVI/Latest/Loop.gif";
                refresh = 100;
                iWidth = (1600 * resizeFactor);
                iHeight = (900 * resizeFactor);
                break;
            case "SfcFrnt":
                imageUrl = getBasePath("getOldGet") + "/SfcFrt/L/_Loop.gif";
                refresh = 600;
                iWidth = (1000 * resizeFactor);
                iHeight = (750 * resizeFactor);
                break;
            case "TempMap":
                imageUrl = getBasePath("getOldGet") + "/Temp/L/_Loop.gif";
                refresh = 600;
                iWidth = (770 * resizeFactor);
                iHeight = (440 * resizeFactor);
                break;
        }
        var params2Pass = {
            "title": "Image Map",
            "imageUrl": imageUrl,
            "refresh": refresh,
            "iWidth": iWidth,
            "iHeight": iHeight,
            "extraDiv": tpUpDiv
        };
    }
    if(isSet(getGetParams("RadarMode"))) {
        // unset 'gpsJSON'
        // call mapPop functions
        // check if is set session times
        // xdt1 = getDate("hour", -1, "full")
        // xdt2 = getDate("hour", 0, "full")
        // xexp = xdt2
        // xdt3 = getDate("hour", -8, "full")
    }
    if(isSet(getGetParams("gpsJSON"))) {
        var activity = ""; //gpsJSON sessvar
        var logDate = getDate("day", 0, "dateOnly");
        var paramForQuery;
        switch(activity) {
            case "Cyc": paramForQuery = "Cyc"; break; // prepare query for jsonLogCyc, etc
            case "Cy2": paramForQuery = "Cy2"; break;
            case "Run": paramForQuery = "Run"; break;
            case "Ru2": paramForQuery = "Ru2"; break;
        }
        getGpsJsonData(paramForQuery, logDate);
        // call query pass paramForQuery as post var
        params2Pass = {
            "title": "GPS JSON Data for ", // sesvar Title
            "doRelatedPhotos": true,
            "mapType": "LineString"
        };
    }
}

function onGpsJsonDataLoaded(gpsData) {
    gpsData.forEach(function (gps) {
        gpsData = JSON.parse(gps.gpsLog);
        var coord_LAT_LON = "[";
        var coord_LON_LAT = "]";
        Object.keys(gpsData).forEach(function (key) {
            if(isSet(gpsData.Latitude) && isSet(gpsData.Longitude)) {
                coord_LAT_LON += "[" + gpsData.Latitude + "," + gpsData.Longitude + "],";
                coord_LON_LAT += "[" + gpsData.Longitude + "," + gpsData.Latitude + "],";
            }
        });
        coord_LAT_LON = coord_LAT_LON.replace("[[", "[").replace("],]","]");
    });
    // include MapPop
}

function onRouteDataLoaded(allData) {
    var coord_LON_LAT;
    if(getGetParams("Route") === "A" || getGetParams("Route") === "C" || getGetParams("Route") === "R") {
        allData.forEach(function (data) {
            switch(getGetParams("Route")) {
                case "A": coord_LON_LAT = data.AltGeoJSON; break;
                case "C": coord_LON_LAT = data.CycGeoJSON; break;
                case "R": coord_LON_LAT = data.RunGeoJSON; break;
            }
        });
    } else {
        coord_LON_LAT = getGetParams("Route");
    }
    var params2Pass = {
        "title": getGetParams("Title"),
        "doRelatedPhotos": true,
        "doMobiLoc": true,
        "coord_LON_LAT": coord_LON_LAT,
        "mapType": "LineString"
    };
}

function init() {
    initMaps();
}

dojo.ready(init);