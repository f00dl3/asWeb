/* 
 
 by Anthony Stump
 Created: 25 Jun 2018
 Updated: 27 Aug 2018
 
 WARNING: AS OF 19 AUG 2018 -- STABILITY ISSUES
 AT THE CURRENT STAGE THIS WILL CAUSE MEMORY LEAK ON 32GB+ SYSTEMS
  - UBUNTU BECOMES UNSTABLE DUE TO EXCESSIVE SWAPPING
  - OPERA MOBILE ON ANDROID SIMPLY CRASHES THE TAB
 
 */

var dataRefresh = getRefresh("long");
if(!checkMobile) { dataRefresh = getRefresh("medium"); }
var disableRefresh = false;
var imageLayer;
var jsonDataMerged;
var overlayLayer;
var pointType;
var reportLayer;
var searchDateStart;
var searchDateEnd;
var warnLayer;
var watchLayer;
var wxStations;

var wxDataTypes = [
    { "name": "CAPE/CIN", "matcher": "CAPE" },
    { "name": "Jet Stream Winds", "matcher": "JSWM" },
    { "name": "Lifted Index", "matcher": "LI" },
    { "name": "Low Level Jet", "matcher": "LLJ" },
    { "name": "Precipitable Water", "matcher": "PWAT" },
    { "name": "Surface Dewpoint", "matcher": "SfcD" },
    { "name": "Surface Elevation", "matcher": "SfcE" },
    { "name": "Surface Feels Like", "matcher": "SfcF" },
    { "name": "Surface Humidity", "matcher": "SfcH" },
    { "name": "Surface Pressure", "matcher": "SfcP" },
    { "name": "Surface Temperature", "matcher": "SfcT" },
    { "name": "Surface Winds", "matcher": "SfcW" },
    { "name": "Water Temperature", "matcher": "WatT" },
    { "name": "Water Wave Height", "matcher": "WatW" },
    { "name": "Water Wave Period", "matcher": "WatP" },
    { "name": "Weather Observed", "matcher": "WxOb" }
];

function actOnSubmitModelQuery(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    getJsonWeatherGlob(map, thisFormData.WxDataType, null, null, thisFormData.WxDataHour, true, true, false);
}

function actOnSubmitXmlSearch(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    getJsonWeatherGlob(map, pointType, thisFormData.wxSearchStart, thisFormData.wxSearchEnd, 0, false, true, true);
}

// Working on, 7/11/18
function addWxMapPops(jsonModelLast, gfsFha, stationCount) {
    if(!isSet(searchDateStart)) { searchDateStart = getDate("hour", -1, "full"); }
    if(!isSet(searchDateEnd)) { searchDateEnd = getDate("hour", 0, "full"); }
    var lastString = jsonModelLast[0].RunString;
    var lastStringSanitized = lastString.replace("_"," ").replace("Z","");
    var djLastString = "test";
    require(["dojo/date/locale"], function(locale) {
        djLastString = locale.parse(lastStringSanitized, {
            datePattern: 'yyyyMMdd HH',
            timePattern: 'HH',
            selector: 'date'
        });
    });
    var stationCount = stationCount[0].StationCount;
    var topPop = "<div class='GPSTopDrop'>" +
            "<form id='DoWxModelData'>" +
            "<select id='WxDataHourDrop' name='WxDataHour'>" +
            "<option value=''>Analysis</option>";
    gfsFha.forEach(function (hour) {
        var tFHour2Pass = Number(hour.FHour) + 2;
        var thisDjLastString = dojo.date.add(djLastString, "hour", tFHour2Pass);
        var validForecastTime = formatDate(thisDjLastString, "yyyy-MM-dd HH");
        var paddedNumber = formatNumber(tFHour2Pass, 4);
        topPop += "<option value='" + paddedNumber + "'>" + validForecastTime + "z (+" + hour.FHour + "h)</option>";
    });
    topPop += "</select><br/>" +
            "<button name='SubmitModelQuery' id='SubmitModelQueryButton'>Go!</button>" +
            "<select id='WxDataTypeDrop' name='WxDataType'>" +
            "<option value='SfcT'>Select...</option>";
    wxDataTypes.forEach(function (xdType) {
        topPop += "<option value='" + xdType.matcher + "'>" + xdType.name + "</option>";
    });
    topPop += "</select>" +
            "</form>" +
            "</div>";
    var lowLeftPop = "<div class='MapLowLeftPop'><img src='" + getBasePath("icon") + "/ic_map.jpeg' class=th_icon'/>Search data..." +
            "<div class='MapLowLeftPopO'>" +
            "<form><table><th align='center' colspan='2'>Data Range - <a href='" + getResource("WxStation") + "' target='new'>" + stationCount + " stations</th></thead>" +
            "<tr><td>Start</td><td><input type='text' name='wxSearchStart' value='" + searchDateStart + "'/></td></tr>" +
            "<tr><td>End</td><td><input type='text' name='wxSearchEnd' value='" + searchDateEnd + "'/></td></tr>" +
            "<input type='hidden' name='DoObsSearch' value='Yes'/>" +
            "<tr><td align='center' colspan='2'><input id='xmlRangeButton' type='submit' name='DoObsSearch'></td></tr>" +
            "</table></form><br/>" +
            "<div id='SDIHolder'>Loading SDI...</div>" +
            "</div></div>";
    var lowRightPop = "<div class='MapLowRight' style='background: black;'>More<br/>" +
            "<a href='http://weather.cod.edu/satrad/nexrad/index.php?type=EAX-N0Q-0-6' target='newRadar'>CoD Radar</a><br/>" +
            "<a href='" + getResource("Cams") + "' target='newRadar'>Home Cams</a>" +
            "</div>";
    dojo.byId("mapEx").innerHTML = topPop + lowLeftPop + lowRightPop;
    var submitModelQueryButton = dojo.byId("SubmitModelQueryButton");
    var xmlRangeButton = dojo.byId("xmlRangeButton");
    dojo.connect(submitModelQueryButton, "onclick", actOnSubmitModelQuery);
    dojo.connect(xmlRangeButton, "onclick", actOnSubmitXmlSearch);
}

function doWeatherOLMap(
        map, lastModelImage, radarList,
        obsIndoor, obsData, obsDataRapid,
        liveWarns, liveWatches, liveReports,
        mobiLoc, markerType,
        fHour4Digit, baseType, lastModelRunString,
        hideElements, disableImageOverlays
    ) {   
    var timestamp = getDate("hour", 0, "timestamp");
    removeLayers(map, timestamp);
    var homeCoord = JSON.parse(getHomeGeo("geoJSON"));
    var jsonData = obsData[0].jsonData;
    obsData = false;
    var jsonDataRapid = obsDataRapid[0].jsonData;
    obsDataRapid = false;
    var mobiCoord = JSON.parse(mobiLoc[0].Location);
    if(!disableImageOverlays) {
        doModelBasemap(map, lastModelImage, fHour4Digit, baseType, lastModelRunString);
    }
    generateRadarKml(radarList, mobiLoc, timestamp, disableImageOverlays);
    if(!hideElements) {
        var vectorSource = new ol.source.Vector({});
        var vectorSourceReports = new ol.source.Vector({});
        vectorSource.addFeature(addObsLocationMarkers(map, "Home", homeCoord));
        if (
                (isSet(mobiCoord[0]) && isSet(mobiCoord[1])) &&
                mobiCoord[0].toFixed(2) !== homeCoord[0].toFixed(2) &&
                mobiCoord[1].toFixed(2) !== homeCoord[1].toFixed(2)
                ) {
            vectorSource.addFeature(addObsLocationMarkers(map, "Note3", mobiCoord));
        }
        if(isSet(jsonData)) {
            jsonDataMerged = Object.assign({}, jsonData, jsonDataRapid);
            jsonData = jsonDataRapid = false;
            wxStations.forEach(function (thisWxStation) {
                if (thisWxStation.Priority !== 0) {
                    var stationId = thisWxStation.Station;
                    var stationData = jsonDataMerged[stationId];
                    if (isSet(stationData) && isSet(stationData.Temperature)) {
                        var tIconFeature = addObsMarkers(map, thisWxStation, stationData, markerType);
                        vectorSource.addFeature(tIconFeature);
                    }
                }
            });
        }
        liveReports.forEach(function (report) {
            if(report.Type === "Q" && isSet(report.Lat) && isSet(report.Lon)) {
                var tQuakeIconFeature = addQuakeMarkers(map, report);
                vectorSourceReports.addFeature(tQuakeIconFeature);
            }
        });
        if(isSet(liveWarns)) {
            warnLayer = addWarnPolys(liveWarns);
            map.addLayer(warnLayer);
        }
        if(isSet(liveWatches)) {
            watchLayer = addWatchPolys(liveWatches);
            map.addLayer(watchLayer);
        }
        overlayLayer = new ol.layer.Vector({source: vectorSource});
        reportLayer = new ol.layer.Vector({source: vectorSourceReports});
        map.addLayer(overlayLayer);
        map.addLayer(reportLayer);
    }
    if(
            isSet(mobiCoord) &&
            mobiCoord[0] !== 0 &&
            mobiCoord[1] !== 0 &&
            mobiCoord[0] !== null &&
            mobiCoord[1] !== null
    ) {
        map.getView().setCenter(ol.proj.transform(mobiCoord, 'EPSG:4326', 'EPSG:3857'));
    }
    map.on('click', function (evt) {
        var feature = map.forEachFeatureAtPixel(evt.pixel, function (feature, layer) {
            return feature;
        });
        if (feature) {
            $("#popup").toggle();
            var eCoord = evt.coordinate;
            var eiData = "";
            switch (feature.get("type")) {
                case "Location":
                    var description = feature.get("description");
                    eiData = "<strong>" + description + "</strong><br/>";
                    if (description === "Home") {
                        var rawIndoorTemp = obsIndoor[0].ExtTemp;
                        var actualGarageTemp = obsIndoor[1].ExtTemp;
                        var actualIndoorTemp = conv2Tf(rawIndoorTemp / 1000);
                        eiData += "<table><tr>" +
                                "<td><a href='" + getResource("Cams") + "' target='cams'>" +
                                "<img class='th_sm_med' src='" + getBasePath("getOldGet") + "/Cams/_Latest.jpeg'/></a></td>" +
                                "<td><strong>Indoors</strong><br/>" +
                                "Desktop: <span style='" + styleTemp(actualIndoorTemp) + "'>" + Math.round(actualIndoorTemp) + "F</span><br/>" +
                                "Garage: <span style='" + styleTemp(actualGarageTemp) + "'>" + Math.round(actualGarageTemp) + "F</span>" +
                                "</td></tr></table>";
                    }
                    break;
                case "Observation":
                    var passedData = feature.get("rawData");
                    var temp = Number(passedData.Temperature);
                    eiData = "<table><tr><td colspan='2'>" +
                            feature.get("stationId") + "<br/>" +
                            feature.get("stationDescription") + "<br/>" +
                            feature.get("timeString") +
                            "</td></tr><tr><td width='40%'>" +
                            "<img class='th_small' src='" + feature.get("wxIcon") + "' /><br/>" +
                            feature.get("wx") +
                            "</td><td width='60%'>" +
                            "Temp: <span style='" + styleTemp(temp) + "'>" + Math.round(temp) + "F</span><br/>";
                    if (isSet(passedData.Dewpoint)) {
                        var dewpoint = Number(passedData.Dewpoint);
                        eiData += "Dwpt: <span style='" + styleTemp(dewpoint) + "'>" + Math.round(dewpoint) + "F</span><br/>";
                    }
                    if (isSet(feature.get("feelsLike"))) {
                        var feel = feature.get("feelsLike");
                        eiData += "Feel: <span style='" + styleTemp(feel) + "'>" + feel + "F</span><br/>";
                    }
                    if (isSet(passedData.WindSpeed)) {
                        var windSpeed = Number(passedData.WindSpeed);
                        eiData += "Wind: <span style='" + styleWind(windSpeed) + "'>" + windSpeed.toFixed(1) + " MPH</span><br/>";
                    }
                    if (isSet(passedData.WindGusts)) {
                        var windGust = Number(passedData.WindGusts);
                        eiData += "Gusts: <span style='" + styleWind(windGusts) + "'>" + windGusts.toFixed(1) + " MPH</span><br/>";
                    }
                    if (isSet(passedData.Pressure)) {
                        eiData += "MSLP: " + Math.round(Number(passedData.Pressure)) + " mb<br/>";
                    }
                    if (isSet(passedData.PressureIn)) {
                        eiData += "Altim: " + Number(passedData.PressureIn).toFixed(2) + " in</span><br/>";
                    }
                    if (isSet(passedData.WaterColumnHeight)) {
                        eiData += "Column: " + passedData.WaterColumnHeight + "m<br/>";
                    }
                    if (isSet(passedData.WaterTemp)) {
                        var waterTemp = Number(passedData.WaterTemp);
                        eiData += "Water: <span style='" + styleTemp(waterTemp) + "'>" + Math.round(waterTemp) + "F</span><br/>";
                    }
                    if (isSet(feature.get("waveHeight"))) {
                        var waveHeightFt = (Number(stationData.WaveHeight)*3.28084).toFixed(1);
                        eiData += "Waves: " + waveHeightFt + " ft<br/>";
                    }
                    if (isSet(passedData.WavePeriodDominant)) {
                        var wavePeriodDominant = Number(passedData.WavePeriodDominant);
                        eiData += "<strong>Period</strong>: " + Math.round(wavePeriodDominant);
                        if (isSet(passedData.WavePeriodAverage)) {
                            var wavePeriodAverage = Number(passedData.WavePeriodAverage);
                            eiData += " (" + wavePeriodAverage.toFixed(1) + "s)<br/>";
                        } else {
                            eiData += "s<br/>";
                        }
                    }
                    if (isSet(passedData.WaveDirection)) {
                        eiData += "Wave Dir: " + passedData.WaveDirection + "<br/>";
                    }
                    eiData += "</td></tr></table>";
                    if (feature.get("priority") < 4) {
                        var upperAirData = processUpperAirData(null, passedData, true).replace("/\s\s+/", "");
                        eiData += "<button id='Sh" + feature.get("stationId") + "TableT' class='UButton'>TMP</button>" +
                                "<button id='Sh" + feature.get("stationId") + "TableH' class='UButton'>HUM</button>" +
                                "<button id='Sh" + feature.get("stationId") + "TableW' class='UButton'>WND</button><br/>" +
                                upperAirData;
                    }
                    break;
                case "Quake":
                    eiData += "Earthquake: <strong>" + feature.get("magnitude") + "</strong></p>" +
                            "<strong>Time</strong>: " + feature.get("friendlyTime") + "<br/>" +
                            "<strong>Longitude</strong>: " + feature.get("longitude") + "<br/>" +
                            "<strong>Latitude</strong>: " + feature.get("latitude") + "<p>" +
                            feature.get("friendlyLocation");
                    break;
                case "WarnPoly": 
                    eiData += "<strong>" + feature.get("event") + "</strong><p>" +
                            feature.get("summary");
                    break;
            }
            content.innerHTML = eiData;
            overlay.setPosition(eCoord);
        }
    });
    stationDataIndexGen(mobiLoc);
}

function getJsonWeatherGlob(map, lPointType, xdt1, xdt2, fHour, hideElements, setDisableRefresh, disableImageOverlays) {
    var disableImageOverlays = false;
    disableRefresh = setDisableRefresh;
    if(isSet(xdt1)) { searchDateStart = xdt1; } else { searchDateStart = getDate("hour", -1, "full"); }
    if(isSet(xdt2)) { searchDateEnd = xdt2; } else { searchDateEnd = getDate("hour", 0, "full"); }
    var fHour4Digit = formatNumber(fHour, 4);
    var baseType;
    pointType = lPointType;
    switch(pointType) {
        case "CAPE": baseType = "cape"; break;
        case "JSWM": baseType = "wm0500"; break;
        case "LI": baseType = "lftx"; break;
        case "LLJ": case "LLJM": baseType = "wm0850"; break;
        case "PWAT": baseType = "pwat"; break;
        case "SfcD": case "SfcH": baseType = "rh2m"; break;
        case "SfcW": baseType = "wm10m"; break;
        case "WxOb": baseType = "apcp"; break;
        case "SfcT": default: if(checkMobile()) { baseType = "tmp2m"; } else { baseType = "js2tmp"; } break;
    }
    aniPreload("on");
    var wpLimit = 1024;
    if(!checkMobile()) { wpLimit = 8192; }
    var thePostData = {
        "doWhat": "getObsJsonGlob", // build out to include also station list
        "startTime": searchDateStart,
        "endTime": searchDateEnd,
        "limit": 1,
        "moiType": baseType,
        "wpLimit": wpLimit,
        "watchStartTime": getDate("hour", -10, "full")
    };
    require(["dojo/request"], function (request) {
        request
                .post(getResource("Wx"), {
                    data: thePostData,
                    handleAs: "json"
                }).then(
                function (data) {
                    aniPreload("off");
                    wxStations = data.stations;
                    doWeatherOLMap(
                            map,
                            data.lmmi,
                            data.radarList,
                            data.indoorObs,
                            data.wxObsJson,
                            data.wxObsJsonRapid,
                            data.liveWarns,
                            data.liveWatches,
                            data.liveReports,
                            data.mobiLoc,
                            pointType,
                            fHour4Digit,
                            baseType,
                            data.lastRun,
                            hideElements,
                            disableImageOverlays
                            );
                },
                function (error) {
                    aniPreload("off");
                    window.alert("request for ObsJson data FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
    if(!disableRefresh) {
        setTimeout(function () {
        getJsonWeatherGlob(map, "SfcT", null, null, 0, false, false, disableImageOverlays);
        }, dataRefresh);
    } else {
        showNotice("Refresh disabled!");
    }
}

function getModelRunInfo() {
    aniPreload("on");
    var thePostData = {
        "doWhat": "getMosData"
    };
    require(["dojo/request"], function (request) {
        request
                .post(getResource("Wx"), {
                    data: thePostData,
                    handleAs: "json"
                }).then(
                function (data) {
                    aniPreload("off");
                    addWxMapPops(data.last, data.hours, data.stationCount);
                },
                function (error) {
                    aniPreload("off");
                    window.alert("request for ModelRunInfo data FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function initWxMap(map) {
    getJsonWeatherGlob(map, "SfcT", null, null, 0, false, false, false);
    getModelRunInfo();
}

function removeLayers(map, timestamp) {
    if (imageLayer) {
        map.removeLayer(imageLayer);
        imageLayer = null;
        console.log(timestamp + ": Removed image layer!");
    } else {
        console.log(timestamp + ": No image layer yet!");
    }
    if (overlayLayer) {
        map.removeLayer(overlayLayer);
        overlayLayer = null;
        console.log(timestamp + ": Removed overlay layer!");
    } else { 
        console.log(timestamp + ": No overlay layer yet!");
    }
    if (reportLayer) {
        map.removeLayer(reportLayer);
        reportLayer = null;
        console.log(timestamp + ": Removed report layer!");
    } else { 
        console.log(timestamp + ": No report layer yet!");
    }
    if (warnLayer) {
        map.removeLayer(warnLayer);
        warnLayer = null;
        console.log(timestamp + ": Removed warn layer!");
    } else {
        console.log(timestamp + ": No warn layer yet!");
    }
    if (watchLayer) {
        map.removeLayer(watchLayer);
        watchLayer = null;
        console.log(timestamp + ": Removed watch layer!");
    } else {
        console.log(timestamp + ": No watch layer yet!");
    }
}

function showTableHumi(stationId) {
    //xhrRequest to wxTableGen table
}

function showTableTemp(stationId) {
    //xhrRequest to wxTableGen table
}

function showTableWind(stationId) {
    //xhrRequest to wxTableGen table
}

function searchAheadStations(value) {
    if(value.length > 2) {
        var matchingRows = [];
        wxStations.forEach(function (sr) {
            if(
                (isSet(sr.City) && (sr.City).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.Station) && (sr.Station).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.Description) && (sr.Description).toLowerCase().includes(value.toLowerCase())) ||
                (value.includes("S:") && isSet(sr.State) && (sr.State).toLowerCase().includes(value.replace("S:", "").toLowerCase())) ||
                (value.includes("R:") && isSet(sr.Region) && (sr.Region).toLowerCase().includes(value.replace("R:", "").toLowerCase()))
            ) { 
                matchingRows.push(sr);
            }
        });
        stationDataTableMini(matchingRows, "jmwsMiniSearchResults");
    }
}

function stationDataIndexGen(mobiLoc) {
    var locGeoJSON = JSON.parse(mobiLoc[0].Location);
    if(isSet(locGeoJSON[0]) && isSet(locGeoJSON[1])) {
        myLat = locGeoJSON[1];
        myLon = locGeoJSON[0];
    } else {
        myLat = getHomeGeo("lat");
        myLon = getHomeGeo("lon");
    }
    var onKeySearch = "<div class='table'>" +
            "<form class='tr' id='WxStationSearchForm'>" +
            "<span class='td'><input type='text' id='SearchBox' name='StationSearchField' onkeyup='searchAheadStations(this.value)' /></span>" +
            "<span class='td'><strong>Search</strong></span>" +
            "</form></div>";
    var hideOnSearch = "<div id='jmwsMiniSearchResults'><div id='StationsNearMe'>Loading... Part 1<br/>" + myLon + ", " + myLat + "</div></div>";
    var rData = onKeySearch + hideOnSearch;
    dojo.byId("SDIHolder").innerHTML = rData;
    stationsNearMeMM();
}

function stationDataTableMini(overrideData, target) {
    dojo.byId(target).innerHTML = "<div id='" + target + "'>Loading... Part 3<br/>" + myLat + ", " + myLon + "</div>";
    overrideData.forEach(function (odps) {
        var stationGeoJSON = JSON.parse(odps.Point);
        var stationLat = stationGeoJSON[1];
        var stationLon = stationGeoJSON[0];
        odps.DistanceFromMe = (Math.abs(myLat-stationLat) + Math.abs(myLon-stationLon));
    });
    overrideData.sort(function(a, b) {
        return a.DistanceFromMe - b.DistanceFromMe;
    });
    var tCols = [ "Station Location", "Observations" ];
    var rData = "<div class='table'><div class='tr'>";
    for(var i = 0; i < tCols.length; i++) { rData += "<span class='td'><strong>" + tCols[i] + "</strong></span>"; }
    rData += "</div>";
    var j = 0;
    overrideData.forEach(function (od) {
        if(jsonDataMerged[od.Station]) {
            if(j <= 100) {
                var tJson = jsonDataMerged[od.Station];
                var tTemp = tJson.Temperature;
                var tDPTemp = tJson.Dewpoint;
                var tsWeather;
                var shortTime = wxShortTime(tJson.TimeString);
                if(!isSet(tJson.Weather)) { tsWeather = "Missing"; } else { tsWeather = tJson.Weather; }
                if(od.Priority === 5) {
                    tTemp = conv2Tf(tTemp);
                    tDPTemp = conv2Tf(tDPTemp);
                }
                rData += "<div class='tr'>" +
                        "<span class='td'><div class='UPop'>" + od.City + ", " + od.Description +
                        "<div class='UPopO'>" + 
                        "<strong><a href='" + getResource("Map.Point") + "&Input=" + od.Point + "' target='pMap'>Map it!</a></strong><br/>" +
                        "<strong>Station ID: </strong>" + od.Station + "<br/>" +
                        "<strong>Priority: </strong>" + od.Priority + "<br/>" +
                        "<strong>Distance: </strong>" + (od.DistanceFromMe).toFixed(2) + "d<br/>" +
                        "<strong>Point:</strong> " + od.Point + "</a><br/>" +
                        "</div></div>" +
                        "</span>" +
                        "<span class='td' style='" + styleTemp(tTemp) + "'><div class='UPop'>" + Math.round(tTemp) + " / " + 
                        " <span style='" + styleTemp(tDPTemp) + "'>" + Math.round(tDPTemp) + "</span>" +
                        " <img class='th_icon' src='" + getBasePath("icon") + "/wx/" + wxObs("Icon", tJson.TimeString, null, null, null, tJson.Weather) + ".png' />" +
                        "<div class='UPopO'>" +
                        "<strong>" + shortTime + "</strong><br/>" +
                        "<img class='th_small' src='" + getBasePath("icon") + "/wx/" + wxObs("Icon", tJson.TimeString, null, null, null, tJson.Weather) + ".png' /><br/>" +
                        "<strong>Weather:</strong> " + tsWeather + "</br>" +
                        "<strong>Tempterature:</strong> <span style='" + styleTemp(tTemp) + "'>" + Math.round(tTemp) + "</span><br/>" +
                        "<strong>Dewpoint:</strong> <span style='" + styleTemp(tDPTemp) + "'>" + Math.round(tDPTemp) + "</span><br/>" +
                        "<strong>Wind:</strong> <span style='" + styleWind(tJson.WindSpeed) + "'>" + tJson.WindSpeed + " mph</span><br/>";
                if(isSet(tJson.WindGust)) {
                        "<strong>Gusting:</strong> <span style='" + styleWind(tJson.WindGust) + "'>" + tJson.WindGust + " mph</span><br/>";
                    
                }
                rData += "</div></div>" +
                        "</span>" +
                        "</div>";
            }
            j++;
        }
    });
    rData += "</div>";
    dojo.byId(target).innerHTML = rData;
    if(j > 100) { showNotice("Over 100 nearby stations! (" + j + ")"); }
}

function stationsNearMeMM() {
    //dojo.byId("StationsNearMe").innerHTML = "<div id='StationsNearMe'>Loading... Part 2<br/>" + myLon + ", " + myLat + "</div>";
    var matchingRows = [];
    if(wxStations) {
        //dojo.byId("StationsNearMe").innerHTML = "<div id='StationsNearMe'>Loading... Part 2<br/>" + myLon + ", " + myLat + "<br/>Station list loop!</div>";
        wxStations.forEach(function (sr) {
            if(isSet(sr.Point)) {
                stationGeoJSON = JSON.parse(sr.Point);
                var stationLat = stationGeoJSON[1];
                var stationLon = stationGeoJSON[0];
                if((Math.abs(stationLat-myLat) + Math.abs(stationLon-myLon)) <= 2.5) { 
                    matchingRows.push(sr);
                }
            }
        });
    } else {
        dojo.byId("StationsNearMe").innerHTML = "ERROR! Station list empty!";
    }
    stationDataTableMini(matchingRows, "StationsNearMe");
}
