/* 
by Anthony Stump
Created: 31 May 2018
Updated: 17 Jun 2018
*/

var gActivity;
var gJsonData;
var pu_Altitude = [];
var pu_Cadence = [];
var pu_Dists= [];
var pu_Heart = [];
var pu_Power = [];
var pu_Speed = [];
var pu_Temps = [];
var pu_Times = [];

function actOnPointDrop(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    window.alert("To build out!");
}

function addGpsInfo(activity) {
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
    }
    var rData = "<div class='GpsInfo'>TEST<br/>" +
            "<div class='table'>" +
            "<div class='tr'>" +
            "<span class='td'><div class='GPSPop'>" + labelS + "<br/>" +
            "<img class='" + gpsThumbSize + "' src='" + doCh("j", "gpsSpeed", "th") + "'/>" +
            "<div class='" + gpsPopOClass + "'><h3>Speed</h3>" +
            "<a href='" + doCh("j", "gpsSpeed", null) + "' target='gpsCh'><img height='" + (540/gpsPopScale) + "' width='" + (960/gpsPopScale) + "' src='" + doCh("j", "gpsSpeed", null) + "'/></a><br/>" +
            "<div class='table'>" +
            "<div class='tr'><span class='td'><em>MPH</em></span><span class='td'><strong>This</strong>";
    if(activity == "Cyc") { rData += "</span><span class='td'><strong>AVG</strong></span><span class='td'><strong>MAX</strong>"; }
    rData += "</span></div>" +
            "<div class='tr'><span class='td'><strong>Average</strong><span class='td'>" + (getSum(pu_Speed)/pu_Speed.length).toFixed(1) +
            "</span></div>" +
            "</div>" +
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
        temperature: jsonData.TemperatureF
    });
    return iconFeature;
}

function addGpsSelectDrop() {
    var rData = "<div class='GPSTopDrop'>" +
            "<form id='DoGPSPointsForm'>";
    if(checkMobile()) { rData += ""; } else { rData += "<strong>Points: </strong>"; }
    var options = [ "Speed", "Altitude" ];
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

function addGpsToMap(map, jsonData, activity, metric) {
    gActivity = activity;
    showNotice(gActivity + " test activated!");
    if(isSet(jsonData)) { gJsonData = jsonData; }
    var keyCount = Object.keys(gJsonData).length;
    var tMetrics = [];
    var coords = [];
    var vectorSource = new ol.source.Vector({});
    var j = 0;
    switch(metric) {
        case "Altitude":
            for(var i = 0; i < keyCount; i++) {
                var tJson = gJsonData[i.toString()];
                if(isSet(tJson.AltitudeFt)) { tMetrics.push(Number(tJson.AltitudeFt)); }
            }
            break;
        case "Speed": default: 
            for(var i = 0; i < keyCount; i++) {
                var tJson = gJsonData[i.toString()];
                if(isSet(tJson.SpeedMPH)) { tMetrics.push(Number(tJson.SpeedMPH)); }
            }
            break;
    }
    var tMetricsMax = Math.max.apply(Math, tMetrics);
    var tMetricsMin = Math.min.apply(Math, tMetrics);
    var tMetricsAvg = getSum(tMetrics) / tMetrics.length;
    for(var i = 0; i < keyCount; i++) {
        if(i % 5 === 0) {
            var t2Metric;
            var thisColor = 'gray';
            var tCoords = [ tJson.Longitude , tJson.Latitude ];
            var tJson = gJsonData[i.toString()];
            if(isSet(tJson.AltitudeFt)) { pu_Altitude.push(tJson.AltitudeFt); } else { pu_Altitude.push(0); }
            if(isSet(tJson.Cadence)) { pu_Cadence.push(tJson.Cadence); } else { pu_Cadence.push(0); }
            if(isSet(tJson.DistTotMiles)) { pu_Dists.push(tJson.DistTotMiles); } else { pu_Dists.push(0); }
            if(isSet(tJson.HeartRate)) { pu_Heart.push(tJson.HeartRate); } else { pu_Heart.push(0); }
            if(isSet(tJson.PowerWatts)) { pu_Power.push(tJson.PowerWatts); } else { pu_Power.push(0); }
            if(isSet(tJson.SpeedMPH)) { pu_Speed.push(tJson.SpeedMPH); } else { pu_Speed.push(0); }
            if(isSet(tJson.TemperatureF)) { pu_Temps.push(tJson.TemperatureF); } else { pu_Temps.push(0); }
            if(isSet(tJson.TrainingTimeTotalSec)) { pu_Times.push(tJson.TrainingTimeTotalSec); } else { pu_Times.push(0); }
            switch(metric) {
                case "Altitude": t2Metric = tJson.AltitudeFt; break;
                case "Speed": default: t2Metric = tJson.SpeedMPH; break;
            }
            if(isSet(t2Metric) && isSet(tCoords[0]) && isSet(tCoords[1])) {
                coords.push(tCoords);
                var tIconFeature = addGpsMarkers(map, tJson, j);
                thisColor = autoColorScale(t2Metric, tMetricsMax, tMetricsMin, tMetricsAvg);
                tIconFeature.setStyle(svgIconStyle("c", 10, thisColor, 1));
                vectorSource.addFeature(tIconFeature);
                j++;
            } else {
                console.log("ERROR @ " + tCoords);
            }
        }
    }
    var vectorLayer = new ol.layer.Vector({ source: vectorSource });
    addLineStringToMap(map, coords, null);
    map.addLayer(vectorLayer);
    map.on('click', function(evt) {
        var feature = map.forEachFeatureAtPixel(evt.pixel, function(feature, layer) {
            return feature;
        });
        if(feature) {
            $("#popup").toggle();
            var eCoord = evt.coordinate;
            console.log("Clicked valid feature!");
            content.innerHTML = "<strong>Point ID: </strong> " + feature.get("id") + "<br/>" +
                    "<strong>Source:</strong> " + feature.get("source") + "<br/>" +
                    "<strong>Elapsed:</strong> " + (feature.get("elapsed")/100/60).toFixed(1) + " min<br/>" +
                    "<strong>Distance:</strong> " + (feature.get("distance").toFixed(2)) + " mi<br/>" +
                    "<strong>Speed:</strong> " + feature.get("speed").toFixed(2) + " MPH<br/>" +
                    "<strong>Temperature:</strong> " + feature.get("temperature").toFixed(1) + " F<br/>" +
                    "<strong>Altitude:</strong> " + feature.get("altitude") + " ft<br/>" +
                    "<strong>Longitude:</strong> " + feature.get("longitude") + "<br/>" +
                    "<strong>Latitude:</strong> " + feature.get("latitude");
            if(isSet(feature.get("heart")) && feature.get("heart") !== 0) {
                content.innerHTML += "<br/><strong>Heart Rate:</strong>" + feature.get("heart").toFixed(1) + " bpm";
            }
            if(isSet(feature.get("power")) && feature.get("power") !== 0) {
                content.innerHTML += "<br/><strong>Power:</strong>" + feature.get("power").toFixed(1) + " wt";
            }
            overlay.setPosition(eCoord);
        }
    });
    addGpsInfo(activity);
    addGpsSelectDrop();
}

function addLineStringToMap(map, pointsToAdd, caption) {
    if(!isSet(caption)) { var caption = pointsToAdd; }
    var polyLine = new ol.geom.LineString(pointsToAdd);
    polyLine.transform('EPSG:4326', 'EPSG:3857');
    var rFeature = new ol.Feature({ geometry: polyLine });
    rFeature.setStyle(routeStyle);
    var vSource = new ol.source.Vector({ features: [rFeature] });
    var vLayer = new ol.layer.Vector({ source: vSource });
    map.addLayer(vLayer);
    map.getView().fit(vSource.getExtent(), map.getSize());
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
                    window.alert("request for GPS Charts FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
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
                    dataToPass = data[0].gpsLog;
                    addGpsToMap(map, dataToPass, type, metric);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for GPS JSON Data FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}