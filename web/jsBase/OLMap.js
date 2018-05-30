/* 
by Anthony Stump
Created: 29 May 2018
 */

function generateMapHolder() {
    var mapHeight = "100%";
    var mapWidth = "100%";
    var styleForMap = "<style>.map { height: " + mapHeight + "; width: " + mapWidth + "; }</style>";
    var rData = styleForMap + "<h3>Test Map</h3><br/>" +
        "<div id='map' class='map'></div>";
    dojo.byId("OLMapHolder").innerHTML = rData;
    initMap();
}

function initMap() {
    var tilePathLocal = getBasePath("osmTiles") + '{z}/{x}/{y}.png';
    console.log(tilePathLocal);
    var localTiles = new ol.layer.Tile({
        source: new ol.source.OSM({
            attributions: [
                'Anthony Stump 2018',
                ol.source.OSM.ATTRIBUTION
            ],
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
            center: ol.proj.fromLonLat([getHomeGeo("lat"), getHomeGeo("lon")]),
            zoom: 4
        })
    });
}

function initOLMapPage() {
    generateMapHolder();
}

dojo.ready(initOLMapPage);