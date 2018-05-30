/* 
by Anthony Stump
Created: 29 May 2018
Updated: 30 May 2018
 */

function generateMapHolder(action) {
    var mapHeight = "95%";
    var mapWidth = "100%";
    var styleForMap = "<style>.map { height: " + mapHeight + "; width: " + mapWidth + "; }</style>";
    var rData = styleForMap + "<div id='map' class='map'></div>";
    dojo.byId("OLMapHolder").innerHTML = rData;
    initMap(action);
}

function initMap(action) {
    switch(action) {
        case "drawPath": putDrawPathMap(); break;
        default: putSimpleMap(); break;
    }
}

function initOLMapPage() {
    generateMapHolder("drawPath");
}

dojo.ready(initOLMapPage);