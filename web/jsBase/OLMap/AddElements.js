/* 
by Anthony Stump
Created: 31 May 2018
Updated: 21 Jun 2018
*/

function addLineStringToMap(map, pointsToAdd, caption, doCenter) {
    if(!isSet(caption)) { var caption = pointsToAdd; }
    var polyLine = new ol.geom.LineString(pointsToAdd);
    polyLine.transform('EPSG:4326', 'EPSG:3857');
    var rFeature = new ol.Feature({ geometry: polyLine });
    rFeature.setStyle(routeStyle);
    var vSource = new ol.source.Vector({ features: [rFeature] });
    var vLayer = new ol.layer.Vector({ source: vSource });
    map.addLayer(vLayer);
    if(doCenter === true) { map.getView().fit(vSource.getExtent(), map.getSize()); }
}
