/* 
by Anthony Stump
Created: 29 May 2018
Updated: 30 May 2018
 */

function generateMapHolder(mapTitle) {
    var mapHeight = "93%";
    var mapWidth = "95%";
    var styleForMap = "<style>.map { height: " + mapHeight + "; width: " + mapWidth + "; }</style>";
    var rData = styleForMap + "<strong>" + mapTitle + "</strong><br/>" +
        "<div id='map' class='map'></div>";
    dojo.byId("OLMapHolder").innerHTML = rData;
    initMap();
}

function initMap() {
    var tilePathLocal = getBasePath("osmTiles") + "{z}/{x}/{y}.png";
    var wmGeoJson = ol.proj.fromLonLat(getHomeGeo("geoJsonRaw"));
    var remoteTiles = new ol.layer.Tile({
        source: new ol.source.OSM()
    });
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
    var map = new ol.Map({
        target: 'map',
        layers: [
            localTiles
        ],
        view: new ol.View({
            center: wmGeoJson,
            zoom: 14
        })
    });
}

function initOLMapPage() {
    generateMapHolder("Local OpenLayers Test Map");
}

dojo.ready(initOLMapPage);