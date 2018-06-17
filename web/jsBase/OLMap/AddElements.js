/* 
by Anthony Stump
Created: 31 May 2018
Updated: 17 Jun 2018
*/

var gActivity;
var gJsonData;

function actOnPointDrop(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    window.alert("To build out!");
}

function addGpsInfo() {
    var rData = "<div class='GpsInfo'>" +
            "<div class='table'>" +
            "<div class='tr'>" +
            "<span class='td'>" + gActivity + " Test</span>" +
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
            switch(metric) {
                case "Altitude": t2Metric = tJson.AltitudeFt; break;
                case "Speed": default: t2Metric = tJson.SpeedMPH; break;
            }
            //if(j === 0) { console.log(tJson); }
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
                    "<strong>Longitude:</strong> " + feature.get("longitude") + "<br/>" +
                    "<strong>Latitude:</strong> " + feature.get("latitude") + "<br/>" +
                    "<strong>Speed:</strong> " + feature.get("speed").toFixed(2) + " MPH<br/>" +
                    "<strong>Temperature:</strong> " + feature.get("temperature").toFixed(1) + " F";
            if(isSet(feature.get("heart")) && feature.get("heart") !== 0) {
                content.innerHTML += "<br/><strong>Heart Rate:</strong>" + feature.get("heart").toFixed(1) + " bpm";
            }
            if(isSet(feature.get("power")) && feature.get("power") !== 0) {
                content.innerHTML += "<br/><strong>Power:</strong>" + feature.get("power").toFixed(1) + " wt";
            }
            overlay.setPosition(eCoord);
        }
    });
    addGpsInfo();
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
