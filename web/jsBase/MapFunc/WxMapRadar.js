/* 
by Anthony Stump
Created: 10 Apr 2018
 */

function doRadarMode(map, radarMode) {
    var radar;
    switch(doRadarMode) {
        case "B": radar = new L.KML(getBasePath("ui") + "/RadarX.jsp", { async: true }); break;
        case "V": radar = new L.KML(getBasePath("old") + "/Maps/Overlay/RadarV.kml", { async: true }); break;
    }
    map.addLayer(radar);
}

function getWarnPolys(xdt1, xdt2, xexp, stationA, idMatch) {
    var limit = 8192;
    if(checkMobile()) { limit = 1024; }
}

function putWarnings(warnPolys) {
    var wpn = 0;
    var wp2newJson = {};
    warnPolys.forEach(function (wp) {
        var thisColor = "#" + wp.ColorHEX;
        var htmlWarning = "<strong>(CAP v" + (wp.capVersion).toFixed(1) + ") " + wp.capevent + "</strong><br/>" +
                "<em>" + wp.title + "</em><p>" +
                basicInputFilter(wp.briefSummary) + "<p>" +
                "Full statement: <a href='" + getBasePath("old") + "/Include/ReadWarning.php?WID=" + wp.id + "' target='warnWindow'>Link</a>";
        wpn++;
        if(wp.capVersion === 1.20) {
            if(isSet(wp.cap12polygon)) {
                var fixedJson = (wp.cap12polygon).replace("[[[", "[[").replace("]]]", "]]");
                if(isSet(fixedJson)) {
                    fixedJson.forEach(function (thisCoord) { wp2newJson.put(thisCoord[1], thisCoord[0]); });
                } else {
                    wp2newJson.put("", "");
                }
                // DYnamic variable based on wpn
                var warnPoly_i = L.polygon(wp2newJson)
                        .setStyle({ color: thisColor, fillColor: thisColor, opacity: 0.5, fillOpacity: 0.1 })
                        .addTo(map).bindPopup(htmlWarning);        
            }
            if(!isSet(wp.cap12polygon && !isSet(wp.cap12same))) {
                var sameCodes = wp.cap12same;
                sameCodes.forEach(function (thisSameCode) {
                    thisSameCode = thisSameCode.replace("\"", "");
                    // bind thisSameCode to warnSame query and execute
                        // LEFT OFF HERE 4/10/18
                });
            }
        }
    });
}
