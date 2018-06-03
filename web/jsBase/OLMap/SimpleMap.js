/* 
by Anthony Stump
Created: 29 May 2018
Split off from OLMap.js on 30 May 2018
Updated: 3 Jun 2018
 */

function processLineStringFromLatLon(dataInput) {
    var linestring_feature = new ol.Feature({
        geometry: new ol.geom.LineString(dataInput)
    });
    features = [linestring_feature];
    features.forEach(transform_geometry);
    var vector_layer = new ol.layer.Vector({
        source: new ol.source.Vector({
            features: features
        })
    })
    return vector_layer;
}

function putSimpleMap() {
    var raster = localTiles;
    var map = new ol.Map({
        target: 'map',
        layers: [
            raster
        ],
        view: new ol.View({
            center: wmGeoJson,
            zoom: 14
        })
    });
    if(isSet(doAction) /* && isSet(dataInput) */) {
        switch(doAction) {
            case "Route":
                var dataInputOverride = [[-94.66923, 38.91598], [-94.66923, 38.91598], [-94.66803, 38.91628]];
                console.log(dataInputOverride);
                var pointsToAdd = dataInputOverride; //dataInput when all is working.
                map.addLayer(processLineStringFromLatLon(pointsToAdd));
                dojo.byId("MessageHolder").innerHTML = pointsToAdd;
                break;
        }
    } 
}

