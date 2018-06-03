/* 
by Anthony Stump
Created: 29 May 2018
Split off from OLMap.js on 30 May 2018
Updated: 2 Jun 2018
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
    if(isSet(doAction) /* && isSet(dataInput) */) {
        switch(doAction) {
            case "Route":
                
                var dataInputOverride = [[-94.66923, 38.91598], [-94.66923, 38.91598], [-94.66803, 38.91628]];
                console.log(dataInputOverride);
                var pointsToAdd = dataInputOverride; //dataInput when all is working.
                var resortedPoints = [];
                for(var i = 0; i < pointsToAdd.length; i++) { 
                    var thisPointSet = pointsToAdd[i];
                    resortedPoints.push([ thisPointSet[1], thisPointSet[0] ]);
                }
                console.log(resortedPoints);
                var vectorLine = new ol.layer.Vector({
                    source: new ol.source.Vector({
                        features: [new ol.Feature({
                                geometry: new ol.geom.LineString(pointsToAdd),
                                name: 'Line'
                        })]
                    })
                });
                map.addLayer(vectorLine);
                dojo.byId("MessageHolder").innerHTML = dataInputOverride;
                break;
        }
    } 
}

