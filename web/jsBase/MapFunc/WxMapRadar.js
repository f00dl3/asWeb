/* 
by Anthony Stump
Created: 10 Apr 2018
Updated: 17 Apr 2018
 */

function getWarnBySame() {

}
    
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

function iterateWarnSame(warnSameData, sameCodes) {
    
}

function putWarnings(warnPolys) {
    var wpn = 0;
    var wSame = [];
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
                    getWarnBySame(thisSameCode); // bind thisSameCode to warnSame query and execute       
                    // return warnSameData!
                    if(isSet(sameCodes)) {
                        var tSameArray = warnSameData.split(" ");
                        tSameArray.forEach(function (sameSet) {
                            var tSameSetArray = tSameArray.split(",");
                            if(isSet(tSameSetArray[1] && isSet(tSameSetArray[0]))) {
                                var tWSame = [ tSameSetArray[1], tSameSetArray[0] ];
                                wSame.push(tWSame); 
                                //concat var names
                                var warnPoly_wpn_thisSameCode = L.polygon(wSame) 
                                        .setStyle({
                                            color: thisColor,
                                            fillColor: thisColor,
                                            opacity: 0.5,
                                            fillOpacity: 0.1
                                        }).addTo(map).bindPopup(htmlWarning);
                            }
                        });
                    }
                });
            } else {
                if(isSet(warnPolys.cappolygon)) {
                    var tWPoly = [];
                    var thisArray = (warnPolys.capPolygon).split(" ");
                    thisArray.forEach(function (thisWarnPolySet) {
                        var thisToAdd = [ thisWarnPolySet ];
                        for (var i = 0; i < thisToAdd.length; i++) {
                            if(!thisToAdd[i].contains("-]") || !thisToAdd[i].contains("[,")) {
                                tWPoly.push(thisToAdd);
                            }
                        }
                        // dynamically create array based on wpn
                        var warnPoly_wpn = L.polygon(tWPoly)
                                .setStyle({
                                    color: thisColor,
                                    fillColor: thisColor,
                                    opacity: 0.5,
                                    fillOpacity: 0.1
                                }).addTo(map).bindPopup(htmlWarning);
                    });
                }
                if(!isSet(warnPolys.cappolygon) && isSet(warnPolys.FIPSCodes)) {
                    var wFipsA = [];
                    var fipsCodes = (warnPolys.FIPSCodes).split(",");
                    if(!isSet(fipsCodes)) { fipsCodes = warnPolys.FIPSCodes; }
                    fipsCodes.forEach(function (fips) {
                        getWarnFipsBounds(fips); //exec query_LiveWarnings_FIPSBounds, bind fips code, return wFips
                        var thisFipsArray = (wFips.coords).split(" ");
                        thisFipsArray.forEach(function (fipsSet) {
                            var thisFipsSetArray = fipsSet.split(",");
                            if(isSet(thisFipsSetArray[1]) && iSet(thisFipsSetArray[0])) {
                                var tWFipsA = [ thisFipsSetArray[1], thisFipsSetArray[0] ];
                                wFipsA.push(tWFipsA);
                            }
                            // dynamically create variable
                            var warnPoly_wpn_thisFips = L.polygon(tWFipsA)
                                    .setStyle({
                                        color: thisColor,
                                        fillColor: thisColor,
                                        opacity: 0.5,
                                        fillOpacity: 0.1
                            }).addTo(map).bindPopup(htmlWarning);
                        });
                    });
                }
            }
        }
    });
}

function putWatches() {
    
}