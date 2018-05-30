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
}

