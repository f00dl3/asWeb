/* 
by Anthony Stump
Created: 31 May 2018
Updated: 9 Jun 2018
*/

function addGpsMarkersMethod3(map, jsonData) {
    var tCoord = [ jsonData.Longitude, jsonData.Latitude ];
    var point = new ol.geom.Point(tCoord);
    point.transform('EPSG:4326', 'EPSG:3857');
    var iconFeature = new ol.Feature({ geometry: point });
    return iconFeature;
}

function addGpsToMap(map, jsonData) {
    showNotice("TEST ACTIVATED!");
    var keyCount = Object.keys(jsonData).length;
    var aSpeedMPH = [];
    var coords = [];
    var vectorSource = new ol.source.Vector({});
    for(var i = 0; i < keyCount; i++) {
        var tJson = jsonData[i.toString()];
        if(isSet(tJson.SpeedMPH)) { aSpeedMPH.push(Number(tJson.SpeedMPH)); }
    }
    var speedMphMax = Math.max.apply(Math, aSpeedMPH);
    var speedMphMin = Math.min.apply(Math, aSpeedMPH);
    var speedMphAvg = getSum(aSpeedMPH) / aSpeedMPH.length;
    for(var i = 0; i < keyCount; i++) {
        if(i % 5 === 0) {
            var thisColor = 'gray';
            var tCoords = [ tJson.Longitude , tJson.Latitude ];
            var tJson = jsonData[i.toString()];
            if(isSet(tJson.SpeedMPH) && isSet(tCoords[0]) && isSet(tCoords[1])) {
                coords.push(tCoords);
                var tIconFeature = addGpsMarkersMethod3(map, tJson);
                thisColor = autoColorScale(tJson.SpeedMPH, speedMphMax, speedMphMin, speedMphAvg);
                tIconFeature.setStyle(svgIconStyle("c", 10, thisColor, 1));
                vectorSource.addFeature(tIconFeature);
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
            console.log("Clicked valid feature!\n" + feature);   
        }
    });
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
