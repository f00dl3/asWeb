/* 
by Anthony Stump
Created: 31 May 2018
Updated: 6 Jun 2018
http://openlayersbook.github.io/
*/

function addGpsIconFeatures(map, jsonData) {
    var tCoord = [ jsonData.Longitude, jsonData.Latitude ];
    var point = new ol.geom.Point(tCoord);
    point.transform('EPSG:4326', 'EPSG:3857');
    var iconFeature = new ol.Feature({
        geometry: point,
        name: 'Point: ' + tCoord[0] + ", " + tCoord[1]
    });
    console.log("icon feature added");
    return iconFeature;
}

function addGpsToMap(map, jsonData) {
    showNotice("TEST ACTIVATED!");
    var coords = [];
    var iconFeatures = [];
    for(var i = 0; i < Object.keys(jsonData).length; i++) {
        var sk = i.toString();
        iconFeatures.push(addGpsIconFeatures(map, jsonData[sk]));
        coords.push([ jsonData[sk].Longitude , jsonData[sk].Latitude ]);
    }
    addLineStringToMap(map, coords, null);
    var iconVector = new ol.source.Vector({ features: iconFeatures });
    var iconStyle = new ol.style.Style({
        image: new ol.style.Icon({
            anchor: [0.5, 5],
            anchorXUnits: 'fraction',
            anchorYUnits: 'pixels',
            opacity: 0.75,
            src: getBasePath('icon') + '/ic_run.png'
        })
    });
    console.log(iconFeatures);
    var iconLayer = new ol.layer.Vector({
        source: iconVector,
        style: iconStyle
    });
    map.addLayer(iconLayer);
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
