/* 
by Anthony Stump
Created: 31 May 2018
Updated: 6 Jun 2018
*/

function addGpsToMap(map, jsonData) {
    showNotice("TEST ACTIVATED!");
    var coords = [];
    for(var i = 0; i < Object.keys(jsonData).length; i++) {
        var sk = i.toString();
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
