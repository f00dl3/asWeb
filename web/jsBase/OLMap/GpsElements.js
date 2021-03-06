/* 
by Anthony Stump
Created: 31 May 2018
Split off from OLMap/AddElements.js 16 Jun 2018
Updated: 20 Apr 2019
 */

var gActivity;
var gJsonData;
var gInData;
var pu_Altitude = [];
var pu_Cadence = [];
var pu_Dists= [];
var pu_Heart = [];
var pu_Power = [];
var pu_Speed = [];
var pu_Temps = [];
var pu_Times = [];
var overlayLayer;

function actOnPointDrop(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    var metric = thisFormData.GPSPoints;
    if(overlayLayer) {
        window.map.removeLayer(overlayLayer);
        window.map.removeLayer(routeLayer);
        console.log("Removed layers!");
    } else {
        console.log("No layers!");
    }
    addGpsToMap(window.map, null, activity, metric);
}

function addGpsInfo(activity, oaStats, oaSensors, fitToday) {
    var calsBurned = "Unknown";
    var gpsThumbSize = "th_icon";
    var gpsPopScale = 3;
    var gpsPopOClass = "mGPSPopO";
    var labelC = "CA";
    var labelE = "EL";
    var labelF = "PF";
    var labelH = "HR";
    var labelP = "PW";
    var labelQ = "QA";
    var labelS = "SP";
    var labelT = "TM";
    var labelV = "HV";
    if(!checkMobile()) {
        gpsThumbSize = "th_small";
        gpsPopScale = 1;
        gpsPopOClass = "GPSPopO";
        labelC = "Cadence";
        labelE = "Elevat";
        labelF = "Pedal";
        labelH = "Heart";
        labelP = "Power";
        labelQ = "Quadra";
        labelS = "Speed";
        labelT = "Temps";
        labelV = "HRV";
    }
    var myAge = getDate("day", 0, "yearOnly") - 1985;
    var thisDayWeight = fitToday.Weight;
    var thisActivityTimeMins = Math.round(Math.max.apply(Math, pu_Times)/100/60);
    var rData = "<div class='GpsInfo'>" +
            "<div class='table'>" +
            "<div class='tr'>";
    if(pu_Cadence.length !== 0 && Math.max.apply(Math, pu_Cadence) !== 0) {
        rData += "<span class='td'><div class='GPSPop'>" + labelC + "<br/>" +
                "<img class='" + gpsThumbSize + "' src='" + doCh("j", "gpsCadence", "th") + "'/>" +
                "<div class='" + gpsPopOClass + "'><h3>Cadence</h3>" +
                "<a href='" + doCh("j", "gpsCadence", null) + "' target='gpsCh'><img height='" + (540/gpsPopScale) + "' width='" + (960/gpsPopScale) + "' src='" + doCh("j", "gpsCadence", null) + "'/></a><br/>" +
                "<div class='table'>" +
                "<div class='tr'><span class='td'><em>RPM</em></span><span class='td'><strong>This</strong></span><span class='td'><strong>AVG</strong></span><span class='td'><strong>MAX</strong></span></div>" +
                "<div class='tr'><span class='td'><strong>Average</strong></span><span class='td'>" + (getSum(pu_Cadence)/pu_Cadence.length).toFixed(1) + "</span><span class='td'>" + oaSensors.AvgCycCadAvg.toFixed(1) + "</span><span class='td'>" + oaSensors.MaxCycCadAvg.toFixed(1) + "</span></div>" +
                "<div class='tr'><span class='td'><strong>Maximum</strong></span><span class='td'>" + (Math.max.apply(Math, pu_Cadence).toFixed(1)) + "</span><span class='td'>" + oaSensors.AvgCycCadMax.toFixed(1) + "</span><span class='td'>" + oaSensors.MaxCycCadMax.toFixed(1) + "</span></div>" +
                "</div>" +
                "</div></div></span>";
    }
    rData += "<span class='td'><div class='GPSPop'>" + labelE + "<br/>" +
            "<img class='" + gpsThumbSize + "' src='" + doCh("j", "gpsElevation", "th") + "'/>" +
            "<div class='" + gpsPopOClass + "'><h3>Elevation</h3>" +
            "<a href='" + doCh("j", "gpsElevation", null) + "' target='gpsCh'><img height='" + (540/gpsPopScale) + "' width='" + (960/gpsPopScale) + "' src='" + doCh("j", "gpsElevation", null) + "'/></a><br/>" +
            "<strong>Maximum: </strong> " + Math.max.apply(Math, pu_Altitude).toFixed(1) + " ft<br/>" +
            "<strong>Minimum: </strong> " + Math.min(pu_Altitude).toFixed(1) + " ft<br/>" +
            "</div>" +
            "</div></span>";
    if(pu_Heart.length !== 0 && Math.max.apply(Math, pu_Heart) !== 0) {
        rData += "<span class='td'><div class='GPSPop'>" + labelH + "<br/>" +
                "<img class='" + gpsThumbSize + "' src='" + doCh("j", "gpsHeartRate", "th") + "'/>" +
                "<div class='" + gpsPopOClass + "'><h3>Heart Rate</h3>" +
                "<a href='" + doCh("j", "gpsHeartRate", null) + "' target='gpsCh'><img height='" + (540/gpsPopScale) + "' width='" + (960/gpsPopScale) + "' src='" + doCh("j", "gpsHeartRate", null) + "'/></a><br/>" +
                "<strong>Average: </strong> " + (getSum(pu_Heart)/pu_Heart.length).toFixed(1) + " bpm<br/>" +
                "<strong>Maximum: </strong> " + Math.max.apply(Math, pu_Heart).toFixed(1) + " bpm<br/>" +
                "</div>" +
                "</div></span>" +
                "<span class='td'><div class='GPSPop'>" + labelV + "<br/>" +
                "<img class='" + gpsThumbSize + "' src='" + doCh("j", "gpsHrv", "th") + "'/>" +
                "<div class='" + gpsPopOClass + "'><h3>Heart Rate Variability</h3>" +
                "<a href='" + doCh("j", "gpsHrv", null) + "' target='gpsCh'><img height='" + (540/gpsPopScale) + "' width='" + (960/gpsPopScale) + "' src='" + doCh("j", "gpsHrv", null) + "'/></a><br/>" +
                "<strong>Average: </strong> UNIMP!<br/>" +
                "<strong>Maximum: </strong> UNIMP!<br/>" +
                "</div>" +
                "</div></span>";
    }
    if(pu_Power.length !== 0 && Math.max.apply(Math, pu_Power) !== 0) {
        rData += "<span class='td'><div class='GPSPop'>" + labelP + "<br/>" +
                "<img class='" + gpsThumbSize + "' src='" + doCh("j", "gpsPower", "th") + "'/>" +
                "<div class='" + gpsPopOClass + "'><h3>Power</h3>" +
                "<a href='" + doCh("j", "gpsPower", null) + "' target='gpsCh'><img height='" + (540/gpsPopScale) + "' width='" + (960/gpsPopScale) + "' src='" + doCh("j", "gpsPower", null) + "'/></a><br/>" +
                "<div class='table'>" +
                "<div class='tr'><span class='td'><em>RPM</em></span><span class='td'><strong>This</strong></span><span class='td'><strong>AVG</strong></span><span class='td'><strong>MAX</strong></span></div>" +
                "<div class='tr'><span class='td'><strong>Average</strong></span><span class='td'>" + (getSum(pu_Power)/pu_Power.length).toFixed(1) + "</span><span class='td'>" + oaSensors.AvgCycPowerAvg.toFixed(1) + "</span><span class='td'>" + oaSensors.MaxCycPowerAvg.toFixed(1) + "</span></div>" +
                "<div class='tr'><span class='td'><strong>Maximum</strong></span><span class='td'>" + (Math.max.apply(Math, pu_Power).toFixed(1)) + "</span><span class='td'>" + oaSensors.AvgCycPowerMax.toFixed(1) + "</span><span class='td'>" + oaSensors.MaxCycPowerMax.toFixed(1) + "</span></div>" +
                "</div>" +
                "</div></div></span>";
    }
    rData += "<span class='td'><div class='GPSPop'>" + labelS + "<br/>" +
            "<img class='" + gpsThumbSize + "' src='" + doCh("j", "gpsSpeed", "th") + "'/>" +
            "<div class='" + gpsPopOClass + "'><h3>Speed</h3>" +
            "<a href='" + doCh("j", "gpsSpeed", null) + "' target='gpsCh'><img height='" + (540/gpsPopScale) + "' width='" + (960/gpsPopScale) + "' src='" + doCh("j", "gpsSpeed", null) + "'/></a><br/>" +
            "<div class='table'>" +
            "<div class='tr'><span class='td'><em>MPH</em></span><span class='td'><strong>This</strong>";
    if(activity === "Cyc") { rData += "</span><span class='td'><strong>AVG</strong></span><span class='td'><strong>MAX</strong>"; }
    rData += "</span></div>" +
            "<div class='tr'><span class='td'><strong>Average</strong></span><span class='td'>" + (getSum(pu_Speed)/pu_Speed.length).toFixed(1);
    if(activity === "Cyc") { rData += "</span><span class='td'>" + oaStats.AvgCycSpeedAvg.toFixed(1) + "</span><span class='td'>" + oaStats.MaxCycSpeedAvg.toFixed(1); }
    rData += "</span></div>" +
            "<div class='tr'><span class='td'><strong>Maximum</strong></span><span class='td'>" + (Math.max.apply(Math, pu_Speed).toFixed(1));
    if(activity === "Cyc") { rData += "</span><span class='td'>" + oaStats.AvgCycSpeedMax.toFixed(1) + "</span><span class='td'>" + oaStats.MaxCycSpeedMax.toFixed(1); }
    rData += "</span></div>" +
            "</div>" +
            "</div></div></span>" +
            "<span class='td'><div class='GPSPop'>" + labelT + "<br/>" +
            "<img class='" + gpsThumbSize + "' src='" + doCh("j", "gpsTemperature", "th") + "'/>" +   
            "<div class='" + gpsPopOClass + "'><h3>Temperature</h3>" +
            "<a href='" + doCh("j", "gpsTemperature", null) + "' target='gpsCh'><img height='" + (540/gpsPopScale) + "' width='" + (960/gpsPopScale) + "' src='" + doCh("j", "gpsTemperature", null) + "'/></a><br/>" +
            "<strong>Maximum: </strong> " + Math.max.apply(Math, pu_Temps).toFixed(1) + " degrees F<br/>" +
            "<strong>Minimum: </strong> " + Math.min(pu_Temps).toFixed(1) + " degrees F<br/>" +
            "</div>" +
            "</div></span>" +
            "</div>" +
            "</div>" +
            "<strong> " + activity + ", " + (Math.max.apply(Math, pu_Dists).toFixed(1)) + " mi.<br/>" +
            thisActivityTimeMins + " min.";
    switch(activity) {
        case "Cyc":
            if(isSet(pu_Power) && Math.max.apply(Math, pu_Power) !== 0) {
                calsBurned = Math.round(((getSum(pu_Power)/pu_Power.length) * Math.max.apply(Math, pu_Times)/100/60/60) * 9.6);
            } break;
        case "Run":
            if(isSet(pu_Heart) && Math.max.apply(Math, pu_Heart) !== 0) {
                var thisAvgHr = (getSum(pu_Heart)/pu_Heart.length).toFixed(1);
                calsBurned = (((myAge * 0.2017) + (thisDayWeight * 0.09036) + (thisAvgHr * 0.6309) - 55.0969) * thisActivityTimeMins / 4.184).toFixed(1);
            } break;
    }
    rData += "<br/><strong>" + calsBurned + " calories";
            "</div>" +
            "</div>";
    dojo.byId("mapEx").innerHTML += rData;
}

function addGpsMarkers(map, jsonData, pointId) {
    var tCoord = [ jsonData.Longitude, jsonData.Latitude ];
    var point = new ol.geom.Point(tCoord);
    point.transform('EPSG:4326', 'EPSG:3857');
    var iconFeature = new ol.Feature({
        altitude: jsonData.AltitudeFt,
        distance: jsonData.DistTotMiles,
        elapsed: jsonData.TrainingTimeTotalSec,
        geometry: point,
        heart: jsonData.HeartRate,
        id: pointId,
        latitude: jsonData.Latitude,
        longitude: jsonData.Longitude,
        power: jsonData.PowerWatts,
        source: jsonData.SpeedSource,
        speed: jsonData.SpeedMPH,
        temperature: jsonData.TemperatureF,
        type: "Coordinate"
    });
    return iconFeature;
}

function addGpsSelectDrop(map, options) {
    var rData = "<div class='GPSTopDrop'>" +
            "<form id='DoGPSPointsForm'>";
    if(checkMobile()) { rData += ""; } else { rData += "<strong>Points: </strong>"; }
    rData += "<select id='GPSPointsDD' name='GPSPoints'>" +
            "<option value=''>Points...</option>";
    for(var i = 0; i < options.length; i++) { rData += "<option value='" + options[i] + "'>" + options[i] + "</option>"; }
    rData += "</select>" +
            "</form>" +
            "</div>";
    dojo.byId("mapEx").innerHTML += rData;
    var pointsDrop = dojo.byId("GPSPointsDD");
    dojo.connect(pointsDrop, "onchange", actOnPointDrop);
}

function addGpsToMap(map, inData, activity, metric) {
    if(isSet(inData)) { gInData = inData; }
    jsonData = JSON.parse(gInData.gpsLog)[0].gpsLog;
    if(isSet(jsonData)) { gJsonData = jsonData; }
    var oaStats = JSON.parse(gInData.oaStats)[0];
    var oaSensors = JSON.parse(gInData.oaSensors)[0];
    var fitToday = JSON.parse(gInData.fitToday)[0];
    var photoRelations = gInData.relatedPhotos;
    gActivity = activity;
    var keyCount = Object.keys(gJsonData).length;
    var tMetrics = [];
    var coords = [];
    var vectorSource = new ol.source.Vector({});
    var photoVector = new ol.source.Vector({});
    var j = 0;
    switch(metric) {
        case "Altitude":
            for(var i = 0; i < keyCount; i++) {
                var tJson = gJsonData[i.toString()];
                try {
                    if(isSet(tJson.AltitudeFt)) { tMetrics.push(Number(tJson.AltitudeFt)); } else { tMetrics.push(0); }
                } catch (err) { 
                    err.message;
                }
            }
            break;
        case "Cadence":
            for(var i = 0; i < keyCount; i++) {
                var tJson = gJsonData[i.toString()];
                try {
                    if(isSet(tJson.Cadence)) { tMetrics.push(Number(tJson.Cadence)); } else { tMetrics.push(0); }   
                } catch (err) { 
                    err.message;
                }
            }
            break;
        case "HeartRate":
            for(var i = 0; i < keyCount; i++) {
                var tJson = gJsonData[i.toString()];
                try {
                    if(isSet(tJson.HeartRate)) { tMetrics.push(Number(tJson.HeartRate)); } else { tMetrics.push(0); }   
                } catch (err) { 
                    err.message;
                }
            }
            break;
        case "Power":
            for(var i = 0; i < keyCount; i++) {
                var tJson = gJsonData[i.toString()];
                try {
                    if(isSet(tJson.PowerWatts)) { tMetrics.push(Number(tJson.PowerWatts)); } else { tMetrics.push(0); }   
                } catch (err) { 
                    err.message;
                }
            }
            break;
        case "Temperature":
            for (var i = 0; i < keyCount; i++) {
                var tJson = gJsonData[i.toString()];
                try {
                    if(isSet(tJson.TemperatureF)) { tMetrics.push(Number(tJson.TemperatureF)); } else { tMetrics.push(0); }
                } catch (err) { 
                    err.message;
                }
            }
            break;
        case "Speed": default: 
            for(var i = 0; i < keyCount; i++) {
                var tJson = gJsonData[i.toString()];
                try {
                    if(isSet(tJson.SpeedMPH)) { tMetrics.push(Number(tJson.SpeedMPH)); } else { tMetrics.push(0); }
                } catch (err) { 
                    err.message;
                }
            }
            break;
    }
    var tMetricsMax = Math.max.apply(Math, tMetrics);
    var tMetricsMin = Math.min.apply(Math, tMetrics);
    var tMetricsAvg = getSum(tMetrics) / tMetrics.length;
    if(isSet(photoRelations)) {
        photoRelations.forEach(function (photoRelation) {
            var tPhotoIcon = addPhotoMarker(map, photoRelation);
            thisColor = "#ffffff";
            tPhotoIcon.setStyle(svgIconStyle("c", 30, thisColor, 1, null, null));
            photoVector.addFeature(tPhotoIcon);
            console.log(tPhotoIcon);
        });
    }
    var step = 2;
    for(var i = 0; i < keyCount; i++) {
        if(i % step === 0) {
            var t2Metric;
            var thisColor = 'gray';
            var tJson = gJsonData[i.toString()];
            try { 
                var tCoords = [ tJson.Longitude , tJson.Latitude ];
                if(isSet(tJson.AltitudeFt)) { pu_Altitude.push(tJson.AltitudeFt); } else { pu_Altitude.push(0); }
                if(isSet(tJson.Cadence)) { pu_Cadence.push(tJson.Cadence); } else { pu_Cadence.push(0);  }
                if(isSet(tJson.DistTotMiles)) { pu_Dists.push(tJson.DistTotMiles); } else { pu_Dists.push(0); }
                if(isSet(tJson.HeartRate)) { pu_Heart.push(tJson.HeartRate); } else { pu_Heart.push(0); }
                if(isSet(tJson.PowerWatts)) { pu_Power.push(tJson.PowerWatts); } else { pu_Power.push(0); }
                if(isSet(tJson.SpeedMPH)) { pu_Speed.push(tJson.SpeedMPH); } else { pu_Speed.push(0); }
                if(isSet(tJson.TemperatureF)) { pu_Temps.push(tJson.TemperatureF); } else { pu_Temps.push(0); }
                if(isSet(tJson.TrainingTimeTotalSec)) { pu_Times.push(tJson.TrainingTimeTotalSec); } else { pu_Times.push(0); }
                switch(metric) {
                    case "Altitude": t2Metric = tJson.AltitudeFt; break;
                    case "Cadence": t2Metric = tJson.Cadence; break;
                    case "HeartRate": t2Metric = tJson.HeartRate; break;
                    case "Power": t2Metric = tJson.PowerWatts; break;
                    case "Temperature": t2Metric = tJson.TemperatureF; break;
                    case "Speed": default: t2Metric = tJson.SpeedMPH; break;
                }
                if(isSet(t2Metric) && isSet(tCoords[0]) && isSet(tCoords[1])) {
                    try {
                        coords.push(tCoords);
                        var tIconFeature = addGpsMarkers(map, tJson, j);
                        if(metric === "Temperature") {
                            thisColor = styleTemp(t2Metric, true);
                        } else {
                            thisColor = autoColorScale(t2Metric, tMetricsMax, tMetricsMin, tMetricsAvg);
                        }
                        tIconFeature.setStyle(svgIconStyle("c", 15, thisColor, 1, null, null));
                        vectorSource.addFeature(tIconFeature);
                    } catch (err) {
                        console.log(
                            "Error on " + j + "/~" + Math.round(keyCount/step) + " ---> " +
                            err.message + "(" + t2Metric + " @ " + tCoords[0] + "," + tCoords[1] + ")"
                        );
                    }
                    j++;
                }
            } catch (err) {
                err.message;
            }
        }
    }
    overlayLayer = new ol.layer.Vector({ source: vectorSource });
    var photoLayer = new ol.layer.Vector({ source: photoVector });
    addLineStringToMap(map, coords, "Route");
    map.addLayer(overlayLayer);
    map.addLayer(photoLayer);
    map.on('click', function(evt) {
        var feature = map.forEachFeatureAtPixel(evt.pixel, function(feature, layer) {
            return feature;
        });
        if(feature) {
            $("#popup").toggle();
            var eCoord = evt.coordinate;
            switch(feature.get("type")) {
                case "Coordinate":
                    content.innerHTML = "<strong>Point ID: </strong> " + feature.get("id") + "<br/>" +
                        "<strong>Source:</strong> " + feature.get("source") + "<br/>" +
                        "<strong>Elapsed:</strong> " + (feature.get("elapsed")/100/60).toFixed(1) + " min<br/>" +
                        "<strong>Distance:</strong> " + (feature.get("distance").toFixed(2)) + " mi<br/>" +
                        "<strong>Speed:</strong> " + feature.get("speed").toFixed(2) + " MPH<br/>" +
                        "<strong>Temperature:</strong> <span style='" + styleTemp(feature.get("temperature")) + "'>" + feature.get("temperature").toFixed(1) + "F</span><br/>" +
                        "<strong>Altitude:</strong> " + feature.get("altitude").toFixed(1) + " ft<br/>" +
                        "<strong>Longitude:</strong> " + feature.get("longitude").toFixed(4) + "<br/>" +
                        "<strong>Latitude:</strong> " + feature.get("latitude").toFixed(4);
                    if(isSet(feature.get("heart")) && feature.get("heart") !== 0) {
                        content.innerHTML += "<br/><strong>Heart Rate:</strong>" + feature.get("heart").toFixed(1) + " bpm";
                    }
                    if(isSet(feature.get("power")) && feature.get("power") !== 0) {
                        content.innerHTML += "<br/><strong>Power:</strong>" + feature.get("power").toFixed(1) + " wt";
                    }
                    break;
                case "Photo":
                    content.innerHTML = "<a href='" + feature.get("urlFull") + "' target='newPhotoPop'><img class='th_small' src='" + feature.get("urlThumb") + "'/></a><br/>" + feature.get("name");
                    break;
                case "Route":
                    content.innerHTML = "<strong>" + feature.get("captionText") + "</strong>";
                    break;
            }
            overlay.setPosition(eCoord);
        }
    });
    if(isSet(inData)) {
        var dropOptions = [ "Speed", "Altitude", "Temperature" ];
        if(isSet(pu_Heart) && pu_Heart.length !== 0 && Math.max.apply(Math, pu_Heart) !== 0) { console.log(pu_Heart.length); dropOptions.push("HeartRate"); }
        switch(activity) {
            case "Cyc":
                if(isSet(pu_Cadence) && pu_Cadence.length !== 0 && Math.max.apply(Math, pu_Cadence) !== 0) { dropOptions.push("Cadence"); }
                if(isSet(pu_Power) && pu_Power.length !== 0 && Math.max.apply(Math, pu_Power) !== 0) { dropOptions.push("Power"); }
                break;
            case "Run":
                //Stub incase next unit has run only features.
                break;
        }
        addGpsInfo(activity, oaStats, oaSensors, fitToday);
        addGpsSelectDrop(map, dropOptions);
    }    
}

function addHistoryArrayToMap(map, arrayIn) {
    var ic = 0;
    var rFeatures = [];
    arrayIn.forEach(function (sgj) {
        var thisCaption = "#" + ic + ": " + sgj.Date + " (" + sgj.Type + ")";
        var fixedGeoJson = sgj.GeoJSON.replace(/\[0\, 0\]\,/g,'');
        var pointsToAdd = JSON.parse(fixedGeoJson);
        var polyLine = new ol.geom.LineString(pointsToAdd);
        polyLine.transform('EPSG:4326', 'EPSG:3857');
        var rFeature = new ol.Feature({ geometry: polyLine, name: thisCaption });
        rFeature.setStyle(routeStyle);
        rFeatures.push(rFeature);
        ic++;
    });
    var vSource = new ol.source.Vector({ features: rFeatures });
    var vLayer = new ol.layer.Vector({ source: vSource });
    map.addLayer(vLayer);
}

function getGpsFromDatabase(map, date, type) {
    aniPreload("on");
    var thePostData = {
        "doWhat": "GpsCharts",
        "logDate": date,
        "activity": type
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Chart"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    aniPreload("off");
                    getGpsFromDatabasePart2(map, date, type);
                },
                function(error) { 
                    aniPreload("off");
                    getGpsFromDatabasePart2(map, date, type);
                });
    });
}

function getGpsFromDatabasePart2(map, date, type) {
    var metric = "Speed";
    aniPreload("on");
    var thePostData = {
        "doWhat": "getGpsJson",
        "logDate": date,
        "activity": type
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Fitness"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    aniPreload("off");
                    addGpsToMap(map, data, type, metric);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for GPS JSON Data FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function getRouteHistoryFromDatabase(map) {
    aniPreload("on");
    var thePostData = {
        "doWhat": "getRouteHistory"
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Fitness"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    aniPreload("off");
                    addHistoryArrayToMap(map, data);
                    showNotice("Data pull success!");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for Route History FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function getRouteFromDatabase(map, date, type) {
    aniPreload("on");
    var thePostData = {
        "doWhat": "getOnlyFitnessGeoJSON",
        "XDT1": date,
        "XDT2": date
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Fitness"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    aniPreload("off");
                    gjd = JSON.parse(data.foj)[0];
                    var photoRelations = data.relatedPhotos;
                    switch(type) {
                        case "R": addLineStringToMap(map, JSON.parse(gjd.RunGeoJSON), "Run route on " + date); break;
                        case "C": addLineStringToMap(map, JSON.parse(gjd.CycGeoJSON), "Bike ride on " + date); break;
                        case "A": addLineStringToMap(map, JSON.parse(gjd.AltGeoJSON), "Alt route on " + date); break;
                    }
                    if(isSet(photoRelations)) {
                        var photoVector = new ol.source.Vector({});
                        photoRelations.forEach(function (photoRelation) {
                            var tPhotoIcon = addPhotoMarker(map, photoRelation);
                            console.log(tPhotoIcon.get("geometry"));
                            thisColor = "#ffffff";
                            tPhotoIcon.setStyle(svgIconStyle("c", 30, thisColor, 1, null, null));
                            photoVector.addFeature(tPhotoIcon);
                        });
                        var photoLayer = new ol.layer.Vector({ source: photoVector });
                        map.addLayer(photoLayer);
                    }
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for Route FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function getRoutePlanFromDatabase(map, dataInput) {
    aniPreload("on");
    var thePostData = {
        "doWhat": "getRoutePlan",
        "SearchString": dataInput
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Fitness"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    aniPreload("off");
                    var tObj = data[0];
                    var routeData = JSON.parse(tObj.GeoJSON);
                    addLineStringToMap(map, routeData, tObj.Description);
                    showNotice("Plan: " + tObj.Description + " (" + tObj.DistKm + "km)");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for Route Plan FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function getRoutePlansAllFromDatabase(map) {
    aniPreload("on");
    var thePostData = {
        "doWhat": "getRoutePlansAll"
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Fitness"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    aniPreload("off");
                    addHistoryArrayToMap(map, data);
                    showNotice("Data pull success!");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for Route Plans All FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}