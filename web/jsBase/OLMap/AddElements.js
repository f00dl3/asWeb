/* 
by Anthony Stump
Created: 31 May 2018
Updated: 7 Jun 2018
*/

function addGpsMarkersMethod1(map, jsonData) {
    console.log("addGpsMarkersMethod1(map, jsonData) called!");
    var tCoord = [ jsonData.Longitude, jsonData.Latitude ];
    var point = new ol.geom.Point(tCoord);
    point.transform('EPSG:4326', 'EPSG:3857');
    var feature = new ol.Feature(point);
    feature.setStyle(genSquareMarker("red", 5));
    var source = new ol.source.Vector({ feature: feature });
    var vectorLayer = new ol.layer.Vector({ source: source });
    map.addLayer(vectorLayer);
}

function addGpsMarkersMethod2(map, jsonData, iter, markerType) {
    var tCoord = [ jsonData.Longitude, jsonData.Latitude ];
    var point = new ol.geom.Point(tCoord);
    point.transform('EPSG:4326', 'EPSG:3857');
    var pos = ol.proj.fromLonLat(tCoord);
    var marker = new ol.Overlay({
        position: pos,
        positioning: 'center-center',
        element: document.getElementById(markerType + iter),
        stopEvent: false
    });
    map.addOverlay(marker);
    console.log("addGpsMarkersMethod2(map, jsonData, " + iter + ", " + markerType + ") called! --> " + tCoord);
}

function addGpsToMap(map, jsonData) {
    showNotice("TEST ACTIVATED!");
    var markerType = "olCirc";
    var coords = [];
    var keyCount = Object.keys(jsonData).length;
    dojo.byId("MarkerHolder").innerHTML = genDivMarkers(markerType, keyCount);
    for(var i = 0; i < keyCount; i++) {
        var sk = i.toString();
        //addGpsMarkersMethod2(map, jsonData[sk], i, markerType);
        genSvgMarker("red");
        coords.push([ jsonData[sk].Longitude , jsonData[sk].Latitude ]);
    }
    addLineStringToMap(map, coords, null);

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
