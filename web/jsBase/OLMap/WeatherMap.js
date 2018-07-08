/* 

by Anthony Stump
Created: 25 Jun 2018
Updated: 8 Jul 2018
POSSIBLE DESKTOP RESOURCE HOG ALERT!
DEFINTATE MOBILE RESOURCE HOG!

*/   

function addObsMarkers(map, stationInfo, stationData) {
    var tCoord = JSON.parse(stationInfo.Point);
    var point = new ol.geom.Point(tCoord);
    var stationDescription = stationInfo.Station;
    var thisObsWx = "Unknown Weather";
    var shortTime = stationData.TimeString;
    if(isSet(stationData.TimeString)) { wxShortTime(stationData.TimeString); }
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
        dewpoint: stationData.Dewpoint,
        geometry: point,
        latitude: tCoord[1],
        longitude: tCoord[0],
        pressureMb: stationData.Pressure,
        pressureIn: stationData.PressureIn,
        stationId: stationInfo.Station,
        stationDescription: stationDescription,
        temperature: stationData.Temperature,
        timeString: shortTime,
        type: "Observation",
        windGusts: stationData.WindGusts,
        windSpeed: stationData.WindSpeed,
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
    if(mobiCoord[0].toFixed(2) !== homeCoord[0].toFixed(2) &&
        mobiCoord[1].toFixed(2) !== homeCoord[1].toFixed(2)) {
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
                    var temp = Number(feature.get("temperature"));
                    eiData = "<table><tr><td colspan='2'>" +
                            feature.get("stationId") + "<br/>" +
                            feature.get("timeString") + "<br/>" +
                            feature.get("stationDescription") +
                            "</td></tr><tr><td>" +
                            "<img class='th_small' src='" + feature.get("wxIcon") + "' /><br/>" +
                            feature.get("wx") +
                            "</td><td>" +
                            "Temp: <span style='" + styleTemp(temp) + "'>" + Math.round(temp) + "F</span><br/>";
                    if(isSet(feature.get("dewpoint"))) {
                        var dewpoint = Number(feature.get("dewpoint")); 
                        eiData += "Dwpt: <span style='" + styleTemp(dewpoint) + "'>" + Math.round(dewpoint) + "F</span><br/>";
                    }
                    if(isSet(feature.get("windSpeed"))) {
                        var windSpeed = Number(feature.get("windSpeed"));
                        eiData += "Wind: <span style='" + styleWind(windSpeed) + "'>" + windSpeed.toFixed(1) + " MPH</span><br/>";
                    }
                    if(isSet(feature.get("windGusts"))) {
                        var windGust = Number(feature.get("windGusts"));
                        eiData += "Gusts: <span style='" + styleWind(windGusts) + "'>" + windGusts.toFixed(1) + " MPH</span><br/>";
                    }
                    if(isSet(feature.get("pressureMb"))) { 
                        eiData += "MSLP: " + Number(feature.get("pressureMb")).toFixed(1) + " mb<br/>";
                    }
                    if(isSet(feature.get("pressureIn"))) {
                        eiData += "Altim: " + Number(feature.get("pressureIn")).toFixed(2) + " in</span><br/>";
                    }
                    eiData += "</td></tr></table>";
                    break;
            }
            content.innerHTML = eiData;
            overlay.setPosition(eCoord);
        }
    });
}

function doWeatherMapFromOldStuff(map, wxStations, obsIndoor, obsData, obsDataRapid) {
    var wxDataType = "SfcT";
    var rData = "";
    var indoorTemp = Math.round(0.93 * conv2Tf((obsIndoor[0].ExtTemp)/1000));
    var jsonData = obsData[0].jsonData; obsData = false;
    var jsonDataRapid = obsDataRapid[0].jsonData; obsDataRapid = false;
    var jsonDataMerged;
    if(isSet(jsonData)) {
        jsonDataMerged = Object.assign({}, jsonData, jsonDataRapid);        
        jsonData = jsonDataRapid = false;
        wxStations.forEach(function (thisWxStation) {
            if(thisWxStation.Priority === 1) {
                // check mobile filter to 1:3 stations if performance issues for non Priority 1 stations
                var stationId = thisWxStation.Station;
                var stationData = jsonDataMerged[stationId];
                if(!isSet(stationData.Temperature) || stationData.Temperature < -100) { return false; }

                if(isSet(thisWxStation.Point)) {
                    var content = "";
                    var theStation = stationId;
                    var bElevMb = thisWxStation.SfcMB;
                    if(thisWxStation.Priority < 4) {
                        var tarEleTemp = "#Sh"+theStation+"TableT";
                        var tarEleHumi = "#Sh"+theStation+"TableH";
                        var tarEleWind = "#Sh"+theStation+"TableW";
                        /* dojo.connect(tarEleTemp, "onclick", showTableTemp(stationId));
                        dojo.connect(tarEleHumi, "onclick", showTableHumi(stationId));
                        dojo.connect(tarEleWind, "onclick", showTableWind(stationId)); */
                        content += processUpperAirData(null, stationData) +
                                "<strong>" + theStation + "</strong><br/>";
                        content += "<br/>" + shortTime + "<br/>" +
                                "<table>" +
                                "<tr><td><a href='" + doCh("p", "WxXML", "TLev=SFC&Station="+theStation) + "' target='new'>" +
                                "</a></td>" +
                                "<td>" + thisObsWx + "<br/>";
                        if(isSet(stationData.Temperature)) { content += "<strong>Temp</strong>: <span style='" + styleTemp(stationData.Temperature) + "'>" + Number(stationData.Temperature).toFixed(1) + "F</span><br/>"; }
                        if(isSet(stationData.Dewpoint)) { content += "<strong>Dwpt</strong>: <span style='" + styleTemp(stationData.Dewpoint) + "'>" + Number(stationData.Dewpoint).toFixed(1) + "F</span><br/>"; }
                        if(isSet(stationData.WindSpeed)) { content += "<strong>Wind</strong>: <span style='" + styleWind(stationData.WindSpeed) + "'>" + Number(stationData.WindSpeed).toFixed(1) + " MPH</span><br/>"; }
                        if(isSet(stationData.WindGust)) { content += "<strong>Gusts</strong>: <span style='" + styleWind(stationData.WindGust) + "'>" + Number(stationData.WindGust).toFixed(1) + " MPH</span><br/>"; }
                        if(isSet(stationData.Pressure)) {
                            content += "<strong>MSLP</strong>: " + Number(stationData.Pressure).toFixed(1) + " mb</span><br/>";
                        } else {
                            if(isSet(stationData.PressureIn)) {
                                content += "<strong>Altim HG</strong>: " + Number(stationData.PressureIn).toFixed(2) + " in</span><br/>";
                            }
                        }
                        if(thisWxStation.Priority === 6 || thisWxStation.Priority === 7) {
                            if(isSet(stationData.WaterColumnHeight)) { content += "<strong>Column</strong>: " + stationData.WaterColumnHeight + " m</span><br/>"; }
                            if(isSet(stationData.WaterTemp)) { content += "<strong>Water</strong>: <span style='" + styleTemp(stationData.WaterTemp) + "'>" + Number(stationData.WaterTemp).toFixed(1) + "F</span><br/>"; }
                            if(isSet(stationData.WaveHeight)) { content += "<strong>Waves</strong>: " + Number(stationData.WaveHeight * 3.28084).toFixed(1) + " ft<br/>"; }
                            if(isSet(stationData.WavePeriodDominant)) {
                                content += "<strong>Period</strong>: " + Math.round(stationData.WavePeriodDominant) +
                                        " (" + (stationData.WavePeriodAverage).toFixed(1) + "s)<br/>";
                            }
                            if(isSet(stationData.WaveDirection)) { content += "<strong>Wave Dir.</strong>: " + stationData.WaveDirection + "<br/>"; }
                            content += "</table>";
                            if(thisWxStation.Priority < 4) {
                                content += "<button id='Sh" + theStation + "TableT' class='UButton'>TMP</button>" +
                                        "<button id='Sh" + theStation + "TableH' class='UButton'>HUM</button>" +
                                        "<button id='Sh" + theStation + "TableW' class='UButton'>WND</button>";
                                // do sounding min trim() on doSoundingMin.replace("/\s\s+/", "");
                            }
                            var station = theStation;
                            rData += content + "</script>";
                        }
                        var obsSpan = smallDivs(wxDataType, station, stationData) + "</script>";
                        /* if(isSet(loopNo)) {
                            $(function() {
                                var els = $("span[id^="+theStation+"_]").hide().toArray();
                                if(!els.length) return;
                                $(els[0]).show();
                                setInterval(function() {
                                    $(els[0]).hide();
                                    els.push(els.shift());
                                    $(els[0]).fadeIn();
                                }, 5);
                            });
                        } */
                        var obsIcon = "";
                    }
                }
                //generateIcons(); 
            }
        });
    } else {
        rData = "<div class='Notice'>No data!</div>";
    }
    return rData;
}

/* Convert to OL
function generateIcons() {
    var homeIcon = L.icon({
        iconUrl: getBasePath("icon") + "/gKML/icon48.png",
        iconSize: [20, 20],
        iconAnchor: [10, 10]
    });
    var mobiIcon = L.icon({
        iconUrl: getBasePath("icon") + "/gKML/icon46.png",
        iconSize: [20, 20],
        iconAnchor: [10, 10]
    });
    var homePopup = "Home<br/><a href='" + getBasePath("ui") + "/Cams.jsp' target='new'>" +
            "<video class='th_medium' autoplay loop>" +
            "<source src='" + getBasePath("getOldGet") + "/Cams/_Loop.mp4?ts=" + getDate("minute", 0, "timestamp") + "></source>" +
            "</video></a><br/>" +
            "<strong>Indoors</strong>: <span style='" + styleTemp(indoorTemp) + "'>" + indoorTemp + "F</span>";
    L.marker(getHomeGeo("geoJSONr"), {icon: homeIcon}).addTo(map).bindPopup(homePopup);
}
*/

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