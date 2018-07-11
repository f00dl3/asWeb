/* 
 
 by Anthony Stump
 Created: 25 Jun 2018
 Updated: 11 Jul 2018
 POSSIBLE DESKTOP RESOURCE HOG ALERT!
 DEFINTATE MmarkerType, OBILE RESOURCE HOG!
 
 */

var dataRefresh = getRefresh("medium");
var overlayLayer;
var pointType;

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
    var wxIcon = getBasePath("icon") + "/wx/" + wxObs("Icon", stationData.TimeString, null, null, null, thisObsWx) + ".png";
    point.transform('EPSG:4326', 'EPSG:3857');
    var iconFeature = new ol.Feature({
        rawData: stationData,
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
    switch (markerType) {
        case "CAPE":
            if (stationInfo.Priority > 3) { icLabel = ""; icColor = "#000000"; icOpacity = 0; } else {
                icLabel = Math.round(stationData.CAPE);
                icColor = styleCape(stationData.CAPE, true);
                icOpacity = 1;
            } break;
        case "JSWM":
            if (stationInfo.Priority > 3) { icLabel = ""; icColor = "#000000"; icOpacity = 0; } else {
                var uljPoints = [Number(stationData.WS150), Number(stationData.WS200), Number(stationData.WS250), Number(stationData.WS300)];
                var uljMax = Math.max.apply(Math, uljPoints);
                icLabel = "-"; //windDirSvg(Number(stationData.WD200));
                icColor = styleWind(conv2Mph(uljMax), true);
                icOpacity = 1;
            } break;
        case "LI":
            // Does not work properly. 7/11/18
            if (stationInfo.Priority > 3) { icLabel = ""; icColor = "#000000"; icOpacity = 0; } else {
                var lftIndx = Number(stationData.LI);
                icLabel = lftIndx.toFixed(1);
                icColor = colorLi(lftIndx, true);
                icOpacity = 1;
            } break;
        case "LLJM":
            if (stationInfo.Priority > 3) { icLabel = ""; icColor = "#000000"; icOpacity = 0; } else {
                var lljPoints = [ Number(stationData.WS900), Number(stationData.WS850), Number(stationData.WS800) ];
                var lljMax = Math.max.apply(Math, lljPoints);
                icLabel = "-"; //windDirSvg(Number(stationData.WD850));
                icColor = styleWind(conv2Mph(lljMax), true);
                icOpacity = 1;
            } break;
        case "PWAT":
            // Does not work proplerly. 7/11/18. Data undefined.
            if (stationInfo.Priority > 3) { icLabel = ""; icColor = "#000000"; icOpacity = 0; } else {
                icLabel = stationData.PWAT;
                icColor = styleLiquid(Number(stationData.PWAT), true);
                icOpacity = 1;
            } break;
        case "SfcH":
            if(isSet(stationData.Dewpoint) && stationData.Dewpoint !== "") {
                var relHum = relativeHumidity(Number(stationData.Temperature), Number(stationData.Dewpoint));
                icLabel = relHum;
                icColor = styleRh(relHum, true);
                icOpacity = 1;
            } else { icLabel = ""; icColor = "#000000"; icOpacity = 0; } 
            break;
        case "SfcT": default:
            icLabel = Math.round(stationData.Temperature);
            icColor = styleTemp(stationData.Temperature, true);
            icOpacity = 1;
            break;
    }
    iconFeature.setStyle(svgIconStyle("ct", 35, icColor, icOpacity, icLabel, "#000000"));
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
        case "Home":
            shortName = "H";
            break;
        case "Note3":
            shortName = "A";
            //overlay.setPosition(point);
            break;
    }
    iconFeature.setStyle(svgIconStyle("ct", 35, "#ffffff", 1, shortName, "#000000"));
    return iconFeature;
}

function doWeatherOLMap(map, wxStations, obsIndoor, obsData, obsDataRapid, mobiLoc, markerType) {
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
    var wxDataType = "SfcT";
    vectorSource.addFeature(addObsLocationMarkers(map, "Home", homeCoord));
    if (
            (isSet(mobiCoord[0]) && isSet(mobiCoord[1])) &&
            mobiCoord[0].toFixed(2) !== homeCoord[0].toFixed(2) &&
            mobiCoord[1].toFixed(2) !== homeCoord[1].toFixed(2)
            ) {
        vectorSource.addFeature(addObsLocationMarkers(map, "Note3", mobiCoord));
        map.getView().setCenter(ol.proj.transform(mobiCoord, 'EPSG:4326', 'EPSG:3857'));
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
                            "</td></tr><tr><td>" +
                            "<img class='th_small' src='" + feature.get("wxIcon") + "' /><br/>" +
                            feature.get("wx") +
                            "</td><td>" +
                            "Temp: <span style='" + styleTemp(temp) + "'>" + Math.round(temp) + "F</span><br/>";
                    if (isSet(passedData.Dewpoint)) {
                        var dewpoint = Number(passedData.Dewpoint);
                        eiData += "Dwpt: <span style='" + styleTemp(dewpoint) + "'>" + Math.round(dewpoint) + "F</span><br/>";
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
                        var waveHeightFt = Number(stationData.WaveHeight * 3.28084).toFixed(1);
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

function smallDivs(dataType, wxStations, stationData) {
    var sessVars = window.localStorage.getItem("sessionVars");
    var gfHour;
    if (isSet(sessVars.wxDataHour)) {
        gFHour = sessVars.gfHour;
    } else {
        gFHour = false;
    }
    var gImgType;
    if (isSet(sessVars.gImgType)) {
        gImgType = sessVars.gImgType;
    } else {
        gImgType = "tmp2m";
    }
    var sfcT, sfcD, obsPlotData, obsSpan;
    sfcT = sfcD = obsPlotData = obsSpan = "";
    var theStation = stationData.Station;
    var obsPlotEmpty = "<img class='th_icon' src='" + getBasePath("icon") + "/wx/xx.png'/>";
    var defBlock100 = "display: inline-block; width: 100%; ";
    if (!isSet(stationData.Temperature)) {
        obsPlotData = obsPlotEmpty;
    } else {
        sfcT = Math.round(stationData.Temperature);
        sfcD = Math.round(stationData.Dewpoint);
        obsPlotData = sfcT;
    }
    var obsPlotStyle = "display: inline-block; width: 100%; ";
    if (isSet(stationData.Temperature)) {
        var passArray = {"v1": sfcT, "v2": sfcD};
        obsPlotStyle += color2Grad("T", "right", passArray);
        obsSpan = "<span class='StationSpan' id='" + theStation + "' style='" + obsPlotStyle + "'>" + obsPlotData + "</span>";
    } else {
        switch (dataType) {
            case "SfcE":
                setSessionVariable("gImgType", "sfce");
                obsPlotData = wxStations.SfcMB;
                obsPlotStyle = "width: 100%; background-color: " + autoColorScale(wxStations.SfcMB, 1015, 800, null);
                obsSpan = "<span class='StationSpan' id='" + theStation + "' style='" + obsPlotStyle + "'>" + obsPlotData + "</span>";
                break;
            case "SfcF":
                setSessionVariable("gImgType", "tmp2m");
                obsPlotData = wxObs("Feel", stationData.TimeString, stationData.Temperature, stationData.WindSpeed, stationData.RelativeHumidity, stationData.Weather);
                obsPlotStyle = defBlock100 + styleTemp(obsPlotData);
                obsSpan = "<span class='StationSpan' id='" + theStation + "' style='" + obsPlotStyle + "'>" + obsPlotData + "</span>";
                break;
            case "SfcP":
                setSessionVariable("gImgType", "mslp");
                obsPlotData = stationData.Pressure;
                obsPlotStyle = "width: 100%; background-color: " + autoColorScale(stationData.Pressure, 1040, 970, null);
                obsSpan = "<span class='StationSpan' id='" + theStation + "' style='" + obsPlotStyle + "'>" + obsPlotData + "</span>";
                break;
            case "SfcW":
                setSessionVariable("gImgType", "wm10m");
                obsPlotData = windDirSvg(stationData.WindDegrees);
                obsPlotStyle = defBlock100 + styleWind(stationData.WindSpeed);
                obsSpan = "<span class='StationSpan' id='" + theStation + "' style='" + obsPlotStyle + "'>" + obsPlotData + "</span>";
                break;
            case "WatP":
                if (wxStations.Priority < 4) {
                    obsPlotData = obsPlotEmpty;
                }
                if (!isSet(stationData.WavePeriodDominant)) {
                    obsPlotData = obsPlotEmpty;
                } else {
                    obsPlotData = stationData.WavePeriodDominant;
                }
                obsPlotStyle = defBlock100;
                obsSpan = "<span class='StationSpan' id='" + theStation + "' style='" + obsPlotStyle + "'>" + obsPlotData + "</span>";
                break;
            case "WatT":
                setSessionVariable("gImgType", "tmp2m");
                if (wxStations.Priority < 4) {
                    obsPlotData = obsPlotEmpty;
                }
                if (!isSet(stationData.WaterTemp)) {
                    obsPlotStyle = defBlock100;
                    obsPlotData = obsPlotEmpty;
                } else {
                    obsPlotData = (stationData.WaterTemp).toFixed(1);
                    obsPlotStyle = defBlock100 + styleTemp(stationData.WaterTemp);
                }
                obsSpan = "<span class='StationSpan' id='" + theStation + "' style='" + obsPlotStyle + "'>" + obsPlotData + "</span>";
                break;
            case "WatW":
                if (wxStations.Priority < 4) {
                    obsPlotData = obsPlotEmpty;
                }
                obsPlotStyle = defBlock100;
                if (!isSet(stationData.WaveHeight)) {
                    obsPlotData = obsPlotEmpty;
                } else {
                    obsPlotData = (stationData.WaveHeight * 3.28084).toFixed(1);
                    obsPlotStyle += styleCape((stationData.WaveHeight * 3.28084 * 100));
                }
                obsSpan = "<span class='StationSpan' id='" + theStation + "' style='" + obsPlotStyle + "'>" + obsPlotData + "</span>";
                break;
            case "WxOb":
                setSessionVariable("gImgType", "radar");
                obsPlotData = "<img class='th_icon' src='" + wxIcon + "'/>";
                obsPlotStyle = defBlock100;
                obsSpan = "<span class='StationSpan' id='" + theStation + "' style='" + obsPlotStyle + "'>" + obsPlotData + "</span>";
                break;
        }
    }
    return obsSpan;
}

function getJsonWeatherGlob(map) {
    aniPreload("on");
    if (overlayLayer) {
        map.removeLayer(overlayLayer);
        console.log("Removed layers!");
    } else {
        console.log("No layers!");
    }
    var thePostData = {
        "doWhat": "getObsJsonGlob", // build out to include also station list
        "startTime": getDate("hour", -1, "full"),
        "endTime": getDate("hour", 0, "full"),
        "limit": 1
    };
    // if can get dynamic updating divs to work, could increase limit. This would be major resource hog.
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
                            data.stations,
                            data.indoorObs,
                            data.wxObsJson,
                            data.wxObsJsonRapid,
                            data.mobiLoc,
                            "SfcH"
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

function initWxMap(map) {
    getJsonWeatherGlob(map);
}