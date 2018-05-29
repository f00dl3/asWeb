/* 
by Anthony Stump
Created: 29 May 2018
 */

function initMap() {
    var map = new ol.Map({
        target: 'map',
        layers: [
            new ol.layer.Tile({
                source: new ol.source.OSM()
            })
        ],
        view: new ol.View({
            center: ol.proj.fromLonLat([37.41, 8.82]),
            zoom: 4
        })
    });
}

function mapHeader() {
    var rData = "<h3>Test Map</h3>";
    dojo.byId("MapTitleSection").innerHTML = rData;
}

function init() {
    mapHeader();
    //initMap();
}

dojo.ready(init);


