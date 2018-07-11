/* 

by Anthony Stump
Created: 25 Jun 2018
Updated: 11 Jul 2018
POSSIBLE DESKTOP RESOURCE HOG ALERT!
DEFINTATE MOBILE RESOURCE HOG!

*/   

var overlayLayer;

function addObsMarkers(map, stationInfo, stationData) {
    var tCoord = JSON.parse(stationInfo.Point);
    var point = new ol.geom.Point(tCoord);
    var stationDescription = stationInfo.Station;
    var thisObsWx = "Unknown Weather";
    var shortTime = stationData.TimeString;
    if(isSet(stationData.TimeString)) { shortTime = wxShortTime(stationData.TimeString); }
    if(!isSet(stationData.Dewpoint)) {
        if(!isSet(stationData.D0)) {
            stationData.Dewpoint = conv2Tf(stationData.D0);
        } else {
            stationData.Dewpoint = stationData.Temperature;
        }
    }
    if(stationInfo.Priority === 5) {
        stationData.Temperature = conv2Tf(stationData.Temperature);
        stationData.Dewpoint = conv2Tf(stationData.Dewpoint);
    }
    if(isSet(stationData.Weather)) { thisObsWx = stationData.Weather; }
    if(isSet(stationInfo.Description)) {
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
    var icLabelTemp = Math.round(stationData.Temperature);
    var icColorTemp = styleTemp(stationData.Temperature, true);
    iconFeature.setStyle(svgIconStyle("ct", 35, icColorTemp, 1, icLabelTemp, "#000000"));
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
    switch(description) {
        case "Home": shortName = "H"; break;
        case "Note3":
            shortName = "A";
            //overlay.setPosition(point);
            break;
    }
    iconFeature.setStyle(svgIconStyle("ct", 35, "#ffffff", 1, shortName, "#000000"));
    return iconFeature;
}

function doWeatherOLMap(map, wxStations, obsIndoor, obsData, obsDataRapid, mobiLoc) {
    var homeCoord = JSON.parse(getHomeGeo("geoJSON"));
    var indoorTemp = Math.round(0.93 * conv2Tf((obsIndoor[0].ExtTemp)/1000));
    var jsonData = obsData[0].jsonData; obsData = false;
    var jsonDataMerged;
    var jsonDataRapid = obsDataRapid[0].jsonData; obsDataRapid = false;
    var mobiCoord = JSON.parse(mobiLoc[0].Location);
    var rData = "";
    var vectorSource = new ol.source.Vector({});
    var wxDataType = "SfcT";
    vectorSource.addFeature(addObsLocationMarkers(map, "Home", homeCoord));
    if(
        (isSet(mobiCoord[0]) && isSet(mobiCoord[1])) &&
        mobiCoord[0].toFixed(2) !== homeCoord[0].toFixed(2) &&
        mobiCoord[1].toFixed(2) !== homeCoord[1].toFixed(2)
    ) {
        vectorSource.addFeature(addObsLocationMarkers(map, "Note3", mobiCoord));
        map.getView().setCenter(ol.proj.transform(mobiCoord, 'EPSG:4326', 'EPSG:3857'));
    }
    if(isSet(jsonData)) {
        jsonDataMerged = Object.assign({}, jsonData, jsonDataRapid);        
        jsonData = jsonDataRapid = false;
        wxStations.forEach(function (thisWxStation) {
            if(thisWxStation.Priority !== 0) {
                var stationId = thisWxStation.Station;
                var stationData = jsonDataMerged[stationId];
                if(isSet(stationData) && isSet(stationData.Temperature)) {
                    var tIconFeature = addObsMarkers(map, thisWxStation, stationData); 
                    vectorSource.addFeature(tIconFeature);
                }
            }
        });
    }
    overlayLayer = new ol.layer.Vector({ source: vectorSource });
    map.addLayer(overlayLayer);
    map.on('click', function(evt) {
        var feature = map.forEachFeatureAtPixel(evt.pixel, function(feature, layer) {
            return feature;
        });
        if(feature) {
            $("#popup").toggle();
            var eCoord = evt.coordinate;
            var eiData = "";
            switch(feature.get("type")) {
                case "Location":
                    var description = feature.get("description");
                    eiData = "<strong>" + description + "</strong><br/>";
                    if(description === "Home") {
                        var rawIndoorTemp = obsIndoor[0].ExtTemp;
                        var actualGarageTemp = obsIndoor[1].ExtTemp;
                        var actualIndoorTemp = conv2Tf(rawIndoorTemp/1000);
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
                    if(isSet(passedData.Dewpoint)) {
                        var dewpoint = Number(passedData.Dewpoint); 
                        eiData += "Dwpt: <span style='" + styleTemp(dewpoint) + "'>" + Math.round(dewpoint) + "F</span><br/>";
                    }
                    if(isSet(passedData.WindSpeed)) {
                        var windSpeed = Number(passedData.WindSpeed);
                        eiData += "Wind: <span style='" + styleWind(windSpeed) + "'>" + windSpeed.toFixed(1) + " MPH</span><br/>";
                    }
                    if(isSet(passedData.WindGusts)) {
                        var windGust = Number(passedData.WindGusts);
                        eiData += "Gusts: <span style='" + styleWind(windGusts) + "'>" + windGusts.toFixed(1) + " MPH</span><br/>";
                    }
                    if(isSet(passedData.Pressure)) { eiData += "MSLP: " + Math.round(Number(passedData.Pressure)) + " mb<br/>"; }
                    if(isSet(passedData.PressureIn)) { eiData += "Altim: " + Number(passedData.PressureIn).toFixed(2) + " in</span><br/>"; }
                    if(isSet(passedData.WaterColumnHeight)) { eiData += "Column: " + passedData.WaterColumnHeight + "m<br/>"; }
                    if(isSet(passedData.WaterTemp)) {
                        var waterTemp = Number(passedData.WaterTemp);
                        eiData += "Water: <span style='" + styleTemp(waterTemp) + "'>" + Math.round(waterTemp) + "F</span><br/>";
                    }
                    if(isSet(feature.get("waveHeight"))) {
                        var waveHeightFt = Number(stationData.WaveHeight * 3.28084).toFixed(1);
                        eiData += "Waves: " + waveHeightFt + " ft<br/>";
                    }
                    if(isSet(passedData.WavePeriodDominant)) {
                        var wavePeriodDominant = Number(passedData.WavePeriodDominant);
                        eiData += "<strong>Period</strong>: " + Math.round(wavePeriodDominant);
                        if(isSet(passedData.WavePeriodAverage)) {
                            var wavePeriodAverage = Number(passedData.WavePeriodAverage);
                            eiData += " (" + wavePeriodAverage.toFixed(1) + "s)<br/>";
                        } else {
                            eiDataa += "s<br/>";
                        }
                    }
                    if(isSet(passedData.WaveDirection)) { eiData += "Wave Dir: " + passedData.WaveDirection + "<br/>"; }
                    eiData += "</td></tr></table>";
                    if(feature.get("priority") < 4) {
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
    var gfHour; if(isSet(sessVars.wxDataHour)) { gFHour = sessVars.gfHour; } else { gFHour = false; }
    var gImgType; if(isSet(sessVars.gImgType)) { gImgType = sessVars.gImgType; } else { gImgType = "tmp2m"; }
    var sfcT, sfcD, obsPlotData, obsSpan;
    sfcT = sfcD = obsPlotData = obsSpan = "";
    var theStation = stationData.Station;
    var obsPlotEmpty = "<img class='th_icon' src='" + getBasePath("icon") + "/wx/xx.png'/>";
    var defBlock100 = "display: inline-block; width: 100%; ";
    if(!isSet(stationData.Temperature)) {
        obsPlotData = obsPlotEmpty;
    } else {
        sfcT = Math.round(stationData.Temperature);
        sfcD = Math.round(stationData.Dewpoint);
        obsPlotData = sfcT;
    }
    var obsPlotStyle = "display: inline-block; width: 100%; ";
    if(isSet(stationData.Temperature)) {
        var passArray = { "v1": sfcT, "v2": sfcD };
        obsPlotStyle += color2Grad("T", "right", passArray);
        obsSpan = "<span class='StationSpan' id='" + theStation + "' style='" + obsPlotStyle + "'>" + obsPlotData + "</span>";
    } else {
        switch(dataType) {
            case "CAPE":
                setSessionVariable("gImgType", "cape");
                if(wxStations.Priority > 3) { obsPlotData = obsPlotEmpty; }
                else { obsPlotData = stationData.CAPE + "<br/><span style='" + styleLi(stationData.CIN) + "'>" + stationData.CIN + "</span>"; }
                obsPlotStyle = styleCape(stationData.CAPE);
                obsSpan = "<span class='StationSpan' id='" + theStation + "' style='" + obsPlotStyle + "'>" + obsPlotData + "</span>";
                break;
            case "JSWM":
                setSessionVariable("gImgType", "wm0500");
                var uljMax = 0;
                if(wxStations.Priority > 3) { obsPlotData = obsPlotEmpty; }
                else {
                    var uljPoints = [ stationData.WS150, stationData.WS200, stationData.WS250, stationData.WS300 ];
                    uljMax = Math.max(uljPoints);
                    obsPlotData = windDirSvg(stationData.WD200);
                }
                obsPlotStyle = defBlock100 + styleWind(conv2Mph(uljMax));
                obsSpan = "<span class='StationSpan' id='" + theStation + "' style='" + obsPlotStyle + "'>" + obsPlotData + "</span>";
                break;
            case "LI":
                setSessionVariable("gImgType", "lftx");
                if(wxStations.Priority > 3) { obsPlotData = obsPlotEmpty; }
                else { obsPlotData = (stationData.LI).toFixed(1); }
                obsPlotStyle = styleLi(stationData.LI);
                obsSpan = "<span class='StationSpan' id='" + theStation + "' style='" + obsPlotStyle + "'>" + obsPlotData + "</span>";
                break;
            case "LLJM":
                setSessionVariable("gImgType", "wm0900");
                var lljMax = 0;
                if(wxStations.Priority > 3) { obsPlotData = obsPlotEmpty; }
                else {
                    var lljPoints = [ stationData.WS900, stationData.WS850, stationData.WS800 ];
                    lljMax = Math.max(lljPoints);
                    obsPlotData = windDirSvg(stationData.WD0800);
                }
                obsPlotStyle = defBlock100 + styleWind(conv2Mph(lljMax));
                obsSpan = "<span class='StationSpan' id='" + theStation + "' style='" + obsPlotStyle + "'>" + obsPlotData + "</span>";
                break;
            case "PWAT":
                setSessionVariable("gImgType", "pwat");
                if(wxStations.Priority > 3) { obsPlotData = obsPlotEmpty; }
                else { obsPlotData = (stationData.PWAT).toFixed(1); }
                obsPlotStyle = defBlock100 + styleLiquid(stationData.PWAT);
                obsSpan = "<span class='StationSpan' id='" + theStation + "' style='" + obsPlotStyle + "'>" + obsPlotData + "</span>";
                break;
            case "SfcH":
                setSessionVariable("gImgType", "rh2m");
                obsPlotData = relativeHumidity(stationData.Temperature, stationData.Dewpoint);
                obsPlotStyle = defBlock100 + styleRh(obsPlotData);
                obsSpan = "<span class='StationSpan' id='" + theStation + "' style='" + obsPlotStyle + "'>" + obsPlotData + "</span>";
                break;
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
                if(wxStations.Priority < 4) { obsPlotData = obsPlotEmpty; }
                if(!isSet(stationData.WavePeriodDominant)) { obsPlotData = obsPlotEmpty; } else { obsPlotData = stationData.WavePeriodDominant; }
                obsPlotStyle = defBlock100;
                obsSpan = "<span class='StationSpan' id='" + theStation + "' style='" + obsPlotStyle + "'>" + obsPlotData + "</span>";
                break;
            case "WatT":
                setSessionVariable("gImgType", "tmp2m");
                if(wxStations.Priority < 4) { obsPlotData = obsPlotEmpty; }
                if(!isSet(stationData.WaterTemp)) {
                    obsPlotStyle = defBlock100;
                    obsPlotData = obsPlotEmpty; 
                } else {
                    obsPlotData = (stationData.WaterTemp).toFixed(1);
                    obsPlotStyle = defBlock100 + styleTemp(stationData.WaterTemp);
                }
                obsSpan = "<span class='StationSpan' id='" + theStation + "' style='" + obsPlotStyle + "'>" + obsPlotData + "</span>";
                break;
            case "WatW":
                if(wxStations.Priority < 4) { obsPlotData = obsPlotEmpty; }
                obsPlotStyle = defBlock100;
                if(!isSet(stationData.WaveHeight)) {
                    obsPlotData = obsPlotEmpty; 
                } else {
                    obsPlotData = (stationData.WaveHeight * 3.28084).toFixed(1);
                    obsPlotStyle += styleCape((stationData.WaveHeight*3.28084*100));
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
    if(overlayLayer) {
        map.removeLayer(overlayLayer);
        console.log("Removed layers!");
    } else {
        console.log("No layers!");
    }
    var dataRefresh = getRefresh("medium");
    var jsonDataTimeout = getRefresh("medium");
    var thePostData = {
        "doWhat": "getObsJsonGlob", // build out to include also station list
        "startTime": getDate("hour", -1, "full"),
        "endTime": getDate("hour", 0, "full"),
        "limit": 1 
    };
    // if can get dynamic updating divs to work, could increase limit. This would be major resource hog.
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Wx"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    aniPreload("off");
                    doWeatherOLMap(
                            map,
                            data.stations,
                            data.indoorObs,
                            data.wxObsJson,
                            data.wxObsJsonRapid,
                            data.mobiLoc
                    );
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for ObsJson data FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
    setTimeout(function () { getJsonWeatherGlob(map); }, dataRefresh);
}

function initWxMap(map) {
    getJsonWeatherGlob(map);
}