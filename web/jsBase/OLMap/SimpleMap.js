/* 
by Anthony Stump
Created: 29 May 2018
Split off from OLMap.js on 30 May 2018
 */

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
    if(isSet(dataInput) && isSet(doAction)) {
        switch(doAction) {
            case "Route":
                var pointsToAdd = dataInput;
                var featureLine = new ol.Feature({ geometry: new ol.geom.LineString(pointsToAdd) });
                var sourceLine = new ol.source.Vector({ features: [featureLine] });
                var vectorLine = new ol.layer.Vector({ source: sourceLine });
                map.addLayer(vectorLine);
                dojo.byId("MessageHolder").innerHTML = dataInput;
                break;
        }
    } 
}

