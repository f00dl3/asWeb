/* 
by Anthony Stump
Created: 29 May 2018
Split off from OLMap.js on 30 May 2018
 */

var tilePathLocal = getBasePath("osmTiles") + "{z}/{x}/{y}.png";
var wmGeoJson = ol.proj.fromLonLat(getHomeGeo("geoJsonRaw"));

var remoteTiles = new ol.layer.Tile({ source: new ol.source.OSM() });

var localTiles = new ol.layer.Tile({
    source: new ol.source.OSM({
        attributions: [
            'Anthony Stump 2018',
            ol.source.OSM.ATTRIBUTION
        ],
        crossOrigin: null,
        opaque: false,
        url: tilePathLocal
    })
});