/* 
 
 by Anthony Stump
 Created: 25 Jun 2018
 Updated: 15 Jul 2018
 POSSIBLE DESKTOP RESOURCE HOG ALERT!
 DEFINTATE MOBILE RESOURCE HOG!
 
 */

var dataRefresh = getRefresh("medium");
var imageLayer;
var overlayLayer;
var pointType;
var searchDateStart;
var searchDateEnd;

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
    getJsonWeatherGlob(map, thisFormData.WxDataType);
}

// Working on, 7/11/18
function addWxMapPops(jsonModelLast, gfsFha, stationCount) {
    if(!isSet(searchDateStart)) { searchDateStart = getDate("hour", -1, "full"); }
    if(!isSet(searchDateEnd)) { searchDateEnd = getDate("hour", 0, "full"); }
    var lastString = jsonModelLast[0].RunString;
    var stationCount = stationCount[0].StationCount;
    console.log(lastString);
    var topPop = "<div class='GPSTopDrop'>" +
            "<form id='DoWxModelData'>" +
            "<select id='WxDataHourDrop' name='WxDataHour'>" +
            "<option value=''>Analysis</option>";
    gfsFha.forEach(function (hour) {
        var tFHour2Pass = Number(hour.FHour) + 2;
        topPop += "<option value='" + lastString + "_" + tFHour2Pass + "'>" + "TIMESTRING (+" + hour.FHour + "h)</option>";
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
            "</table></form></div></div>";
    var lowRightPop = "<div class='MapLowRight' style='background: black;'>More<br/>" +
            "<a href='http://weather.cod.edu/satrad/nexrad/index.php?type=EAX-N0Q-0-6' target='newRadar'>CoD Radar</a><br/>" +
            "<a href='" + getResource("Cams") + "' target='newRadar'>Home Cams</a>" +
            "</div>";
    dojo.byId("mapEx").innerHTML = topPop + lowLeftPop + lowRightPop;
    var submitModelQueryButton = dojo.byId("SubmitModelQueryButton");
    dojo.connect(submitModelQueryButton, "onclick", actOnSubmitModelQuery);
}

function addObsMarkers(map, stationInfo, stationData, markerType) {
    var tCoord = JSON.parse(stationInfo.Point);
    var point = new ol.geom.Point(tCoord);
    var stationDescription = stationInfo.Station;
    var thisObsWx = "Unknown Weather";
    var shortTime = stationData.TimeString;
    if (isSet(stationData.TimeString)) {
        shortTime = wxShortTime(stationData.TimeString);
    }
    if (!isSet(stationData.Dewpoint)) {
        if (!isSet(stationData.D0)) {
            stationData.Dewpoint = conv2Tf(stationData.D0);
        } else {
            stationData.Dewpoint = stationData.Temperature;
        }
    }
    if (stationInfo.Priority === 5) {
        stationData.Temperature = conv2Tf(stationData.Temperature);
        stationData.Dewpoint = conv2Tf(stationData.Dewpoint);
    }
    if (isSet(stationData.Weather)) {
        thisObsWx = stationData.Weather;
    }
    if (isSet(stationInfo.Description)) {
        stationDescription = stationInfo.City + ", " + stationInfo.Description;
    } else {
        stationDescription = stationInfo.City + ", " + stationInfo.State;
    }
    var feelsLike = wxObs("Feel", stationData.TimeString, stationData.Temperature, stationData.WindSpeed, stationData.RelativeHumidity, stationData.Weather);
    var wxIcon = getBasePath("icon") + "/wx/" + wxObs("Icon", stationData.TimeString, null, null, null, thisObsWx) + ".png";
    point.transform('EPSG:4326', 'EPSG:3857');
    var iconFeature = new ol.Feature({
        rawData: stationData,
        feelsLike: feelsLike,
        geometry: point,
        latitude: tCoord[1],
        longitude: tCoord[0],
        priority: stationInfo.Priority,
        stationId: stationInfo.Station,
        stationDescription: stationDescription,
        timeString: shortTime,
        type: "Observation",
        wx: thisObsWx,
        wxIcon: wxIcon
    });
    var icLabel = "";
    var icColor = "";
    var icOpacity = "";
    var icLabelColor = "#000000";
    switch (markerType) {
        case "CAPE":
            if (stationInfo.Priority > 3) { icLabel = ""; icColor = "#000000"; icOpacity = 0; } else {
                icLabel = Math.round(stationData.CAPE);
                icColor = styleCape(stationData.CAPE, true);
                icLabelColor = styleCape(stationData.CAPE, "text");
                icOpacity = 1;
            } break;
        case "JSWM":
            if (stationInfo.Priority > 3) { icLabel = ""; icColor = "#000000"; icOpacity = 0; } else {
                var uljPoints = [Number(stationData.WS150), Number(stationData.WS200), Number(stationData.WS250), Number(stationData.WS300)];
                var uljMax = Math.max.apply(Math, uljPoints);
                icLabel = "-"; //windDirSvg(Number(stationData.WD200));
                icColor = styleWind(conv2Mph(uljMax), true);
                icLabelColor = styleWind(conv2Mph(uljMax), "text");
                icOpacity = 1;
            } break;
        case "LI":
            // Does not work properly. 7/11/18
            if (stationInfo.Priority > 3) { icLabel = ""; icColor = "#000000"; icOpacity = 0; } else {
                var lftIndx = Number(stationData.LI);
                icLabel = lftIndx.toFixed(1);
                icColor = colorLi(lftIndx, true);
                icLabelColor = colorLi(lftIndx, "text");
                icOpacity = 1;
            } break;
        case "LLJM":
            if (stationInfo.Priority > 3) { icLabel = ""; icColor = "#000000"; icOpacity = 0; } else {
                var lljPoints = [ Number(stationData.WS900), Number(stationData.WS850), Number(stationData.WS800) ];
                var lljMax = Math.max.apply(Math, lljPoints);
                icLabel = "-"; //windDirSvg(Number(stationData.WD850));
                icColor = styleWind(conv2Mph(lljMax), true);
                icLabelColor = styleWind(conv2Mph(lljMax), "text");
                icOpacity = 1;
            } break;
        case "PWAT":
            // Does not work proplerly. 7/11/18. Data undefined.
            if (stationInfo.Priority > 3) { icLabel = ""; icColor = "#000000"; icOpacity = 0; } else {
                icLabel = stationData.PWAT;
                icColor = styleLiquid(Number(stationData.PWAT), true);
                icLabelolor = styleLiquid(Number(stationData.PWAT), "text");
                icOpacity = 1;
            } break;
        case "SfcD":
            if(!isSet(stationData.Dewpoint) || stationData.Dewpoint < -100) {
                icLabel = ""; icColor = "#000000"; icOpacity = 0;
            } else {
                icLabel = Math.round(stationData.Dewpoint);
                icColor = styleTemp(stationData.Dewpoint, true);
                icLabelColor = styleTemp(stationData.Dewpoint, "text");
                icOpacity = 1;
            }
            break;
        case "SfcE":
            if(isSet(stationInfo.SfcMB)) {
                icLabel = stationInfo.SfcMB;
                icColor = autoColorScale(stationInfo.SfcMB, 1015, 800, null);
                icOpacity = 1;
            } else { icLabel = ""; icColor = "#000000"; icOpacity = 0; } break;
        case "SfcF":
            if(isSet(stationData.Temperature) && isSet(stationData.Dewpoint)) {
                icLabel = feelsLike;
                icColor = styleTemp(feelsLike, true);
                icLabelColor = styleTemp(feelsLike, "text");
                icOpacity = 1;
            } else { icLabel = ""; icColor = "#000000"; icOpacity = 0; } break;
        case "SfcH":
            if(isSet(stationData.Dewpoint) && stationData.Dewpoint !== "") {
                var relHum = relativeHumidity(Number(stationData.Temperature), Number(stationData.Dewpoint));
                icLabel = relHum;
                icColor = styleRh(relHum, true);
                icLabelColor = styleRh(relHum, "text");
                icOpacity = 1;
            } else { icLabel = ""; icColor = "#000000"; icOpacity = 0; } break;
        case "SfcP":
            if(isSet(stationData.Pressure)) {
                icLabel = stationData.Pressure;
                icColor = autoColorScale(stationData.Pressure, 1040, 970, null);
                icOpacity = 1;
            } else { icLabel = ""; icColor = "#000000"; icOpacity = 0; } break;
        case "SfcW":
            if(isSet(stationData.WindSpeed)) {
                var sWindSpeed = Number(stationData.WindSpeed);
                icLabel = Math.round(sWindSpeed); //windDirSvg(stationData.WindDegrees);
                icColor = styleWind(sWindSpeed, true);
                icLabelColor = styleWind(sWindSpeed, "text");
                icOpacity = 1;
            } else { icLabel = ""; icColor = "#000000"; icOpacity = 0; } break;
        case "WatP":
            if(!isSet(stationData.WavePeriodDominant)) { icLabel = ""; icColor = "#000000"; icOpacity = 0; } else {
                icLabel = stationData.WavePeriodDominant;
                icColor = "#ffffff";
                icOpacity = 1;
            } break;
        case "WatT":
            if(!isSet(stationData.WaterTemp)) { icLabel = ""; icColor = "#000000"; icOpacity = 0; } else {
                icLabel = Math.round(Number(stationData.WaterTemp));
                icColor = styleTemp(Number(stationData.WaterTemp), true);
                icLabelColor = styleTemp(Number(stationData.WaterTemp), "text");
                icOpacity = 1;
            } break;
        case "WatW":
            if(!isSet(stationData.WaveHeight)) { icLabel = ""; icColor = "#000000"; icOpacity = 0; } else {
                var waveHeightFt = Number(stationData.WaterTemp)*3.28084;
                icLabel = waveHeightFt.toFixed(1);
                icColor = styleCape((waveHeightFt*100), true);
                icOpacity = 1;
            } break;
        case "WxOb":
            // Does not work, 7/11/18
            if(!isSet(thisObsWx)) { icLabel = ""; icColor = "#000000"; icOpacity = 0; } else {
                icLabel = "<img class='th_icon' src='" + wxIcon + "'/>";
                icColor = "#000000";
                icOpacity = 1;
            } break;
        case "SfcT": default:
            if(!isSet(stationData.Temperature) || stationData.Temperature < -100) {
                icLabel = ""; icColor = "#000000"; icOpacity = 0;
            } else {
                icLabel = Math.round(stationData.Temperature);
                icColor = styleTemp(stationData.Temperature, true);
                icLabelColor = styleTemp(stationData.Temperature, "text");
                icOpacity = 1;
            }
            break;
    }
    iconFeature.setStyle(svgIconStyle("ct", 35, icColor, icOpacity, icLabel, icLabelColor));
    return iconFeature;
}

function addObsLocationMarkers(map, description, tCoord) {
    var shortName = "X";
    var point = new ol.geom.Point(tCoord);
    point.transform('EPSG:4326', 'EPSG:3857');
    var iconFeature = new ol.Feature({
        description: description,
        geometry: point,
        latitude: tCoord[1],
        longitude: tCoord[0],
        type: "Location"
    });
    switch (description) {
        case "Home": shortName = "H"; break;
        case "Note3": shortName = "A"; break;
    }
    iconFeature.setStyle(svgIconStyle("ct", 35, "#ffffff", 1, shortName, "#000000"));
    return iconFeature;
}

function doModelBasemap(map, lmmi) {
    var fixedLmiPath = lmmi.lastFile.replace("/var/www/G2Out", getBasePath("g2OutOld"));
    var extent = ol.extent.applyTransform(
            [-128, 24, -65, 50],
            ol.proj.getTransform('EPSG:4326', 'EPSG:3857')
    );      
    var projection = new ol.proj.Projection({
        code: 'local_image',
        units: 'pixels',
        extent: extent
    });
    imageLayer = new ol.layer.Image({
        opacity: 0.4,
        source: new ol.source.ImageStatic({
            attributions: [ fixedLmiPath ],
            url: fixedLmiPath,
            imageSize: [ lmmi.mediaInfo.imageWidth, lmmi.mediaInfo.imageHeight ],
            projection: map.getView().getProjection(),
            imageExtent: extent
        })
    });
    map.addLayer(imageLayer);
}

function doWeatherOLMap(map, lastModelImage, radarList, wxStations, obsIndoor, obsData, obsDataRapid, mobiLoc, markerType) {
    var timestamp = getDate("hour", 0, "timestamp");
    if (imageLayer) {
        map.removeLayer(imageLayer);
        console.log(timestamp + ": Removed image layer!");
    } else {
        console.log(timestamp + ": No image layer yet!");
    }
    if (overlayLayer) {
        map.removeLayer(overlayLayer);
        console.log(timestamp + ": Removed overlay layer!");
    } else {
        console.log(timestamp + ": No overlay layer yet!");
    }
    var homeCoord = JSON.parse(getHomeGeo("geoJSON"));
    var indoorTemp = Math.round(0.93 * conv2Tf((obsIndoor[0].ExtTemp) / 1000));
    var jsonData = obsData[0].jsonData;
    obsData = false;
    var jsonDataMerged;
    var jsonDataRapid = obsDataRapid[0].jsonData;
    obsDataRapid = false;
    var mobiCoord = JSON.parse(mobiLoc[0].Location);
    var rData = "";
    var vectorSource = new ol.source.Vector({});
    doModelBasemap(map, lastModelImage);
    generateRadarKml(radarList, mobiLoc, timestamp);
    vectorSource.addFeature(addObsLocationMarkers(map, "Home", homeCoord));
    if (
            (isSet(mobiCoord[0]) && isSet(mobiCoord[1])) &&
            mobiCoord[0].toFixed(2) !== homeCoord[0].toFixed(2) &&
            mobiCoord[1].toFixed(2) !== homeCoord[1].toFixed(2)
            ) {
        vectorSource.addFeature(addObsLocationMarkers(map, "Note3", mobiCoord));
    }
    if (isSet(jsonData)) {
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
    overlayLayer = new ol.layer.Vector({source: vectorSource});
    map.addLayer(overlayLayer);
    if(isSet(mobiCoord) && mobiCoord[0] !== 0 && mobiCoord[1] !== 0) {
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
                            eiDataa += "s<br/>";
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
            }
            content.innerHTML = eiData;
            overlay.setPosition(eCoord);
        }
    });
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

function getJsonWeatherGlob(map, lPointType) {
    var baseType;
    if(isSet(lPointType)) { pointType = lPointType; } else { pointType = "SfcT"; }
    switch(pointType) {
        case "CAPE": baseType = "cape"; break;
        case "JSWM": baseType = "wm0500"; break;
        case "LI": baseType = "lftx"; break;
        case "LLJM": baseType = "wm0850"; break;
        case "PWAT": baseType = "pwat"; break;
        case "SfcD": baseType = null; break;
        case "SfcH": baseType = "rh2m"; break;
        case "SfcW": baseType = "wm10m"; break;
        case "WxOb": baseType = "apcp"; break;
        case "SfcT": default: if(checkMobile()) { baseType = "tmp2m"; } else { baseType = "js2tmp"; } break;
    }
    aniPreload("on");
    var thePostData = {
        "doWhat": "getObsJsonGlob", // build out to include also station list
        "startTime": getDate("hour", -1, "full"),
        "endTime": getDate("hour", 0, "full"),
        "limit": 1,
        "moiType": baseType
    };
    require(["dojo/request"], function (request) {
        request
                .post(getResource("Wx"), {
                    data: thePostData,
                    handleAs: "json"
                }).then(
                function (data) {
                    aniPreload("off");
                    doWeatherOLMap(
                            map,
                            data.lmmi,
                            data.radarList,
                            data.stations,
                            data.indoorObs,
                            data.wxObsJson,
                            data.wxObsJsonRapid,
                            data.mobiLoc,
                            pointType
                            );
                },
                function (error) {
                    aniPreload("off");
                    window.alert("request for ObsJson data FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
    setTimeout(function () {
        getJsonWeatherGlob(map);
    }, dataRefresh);
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
    getJsonWeatherGlob(map);
    getModelRunInfo();
}