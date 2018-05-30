/* 
by Anthony Stump
Created: 29 May 2018
Updated: 30 May 2018
 */

function generateMapHolder(action) {
    var mapHeight = "98%";
    var mapWidth = "98%";
    var styleForMap = "<style>.map { height: " + mapHeight + "; width: " + mapWidth + "; }</style>";
    var rData = styleForMap + "<div id='map' class='map'></div>";
    dojo.byId("OLMapHolder").innerHTML = rData;
    initMap(action);
}

function initMap(action) {
    var draw;
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
    var raster = localTiles;
    var source = new ol.source.Vector({ wrapX: false });
    var vector = new ol.layer.Vector({ source: source });
    var map = new ol.Map({
        target: 'map',
        layers: [
            raster, vector
        ],
        view: new ol.View({
            center: wmGeoJson,
            zoom: 14
        })
    });
    if(isSet(action)) {
        switch(action) {
            case "draw":
                function addInteraction() {
                    var value = "LineString";
                    draw = new ol.interaction.Draw({
                        source: source,
                        type: value
                    });
                    map.addInteraction(draw);
                }
                addInteraction();
                break;
        }
    }
}

function initOLMapPage() {
    generateMapHolder("draw");
}

dojo.ready(initOLMapPage);