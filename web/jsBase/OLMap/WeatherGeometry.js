/* 
by Anthony Stump
Created: 25 Jun 2018
Split off from OLMap/WeatherMap.js 16 Jul 2018
 */

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
    iconFeature.setStyle(svgIconStyle("ct", 30, icColor, icOpacity, icLabel, icLabelColor));
    return iconFeature;
}

function addQuakes(quakes) {
    
}

function addWarnPolys(liveWarns) {
    var rFeatures = [];
    liveWarns.forEach(function (warn) {
        if(isSet(warn.cap12polygon)) {
            var geoJSON = JSON.parse(warn.cap12polygon);
            var polyLine = new ol.geom.LineString(geoJSON);
            var colors = "255 255 255";
            if(isSet(warn.ColorRGB)) { colors = warn.ColorRGB; }
            colors = colors.split(" ");
            polyLine.transform('EPSG:4326', 'EPSG:3857');
            var rFeature = new ol.Feature({
                event: warn.capevent,
                geometry: polyLine,
                summary: warn.summary,
                warnId: warn.id,
                title: warn.title,
                type: "WarnPoly"
            });
            var wpStyle = new ol.style.Style({
                stroke: new ol.style.Stroke({
                    color: 'rgba(' + colors[0] + ',' + colors[1] + ',' + colors[2] + ',0.4)',
                    width: 4
                })
            });
            rFeature.setStyle(wpStyle);
            rFeatures.push(rFeature);
        } else if(isSet(warn.cap12same)) {
            var arrayOfBounds = warn.tSameBounds;
            arrayOfBounds.forEach(function (cgj) {
                if(isSet(cgj[0]) && isSet(cgj[0].coords) && cgj[0].coords.length > 2) {
                    var bounds = cgj[0].coords.slice(0, -1);
                    var polyLine = new ol.geom.LineString(bounds);
                    var colors = "255 255 255";
                    if(isSet(warn.ColorRGB)) { colors = warn.ColorRGB; }
                    colors = colors.split(" ");
                    polyLine.transform('EPSG:4326', 'EPSG:3857');
                    var rFeature = new ol.Feature({
                        event: warn.capevent,
                        geometry: polyLine,
                        summary: warn.summary,
                        warnId: warn.id,
                        title: warn.title,
                        type: "WarnPoly"
                    });
                    var wpStyle = new ol.style.Style({
                        stroke: new ol.style.Stroke({
                            color: 'rgba(' + colors[0] + ',' + colors[1] + ',' + colors[2] + ',0.4)',
                            width: 3
                        })
                    });
                    rFeature.setStyle(wpStyle);
                    rFeatures.push(rFeature);
                }
            });
        } else {
            console.log(warn.id + ": No polygon or SAME data!");
        }
    });
    var vSource = new ol.source.Vector({ features: rFeatures });
    var vLayer = new ol.layer.Vector({ source: vSource });
    return vLayer;
}

function getSameBounds(sameCode) {
    aniPreload("on");
    var thePostData = {
        "doWhat": "getSameBounds",
        "sameCode": sameCode
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
                    window.alert("request for SameBounds data FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}
