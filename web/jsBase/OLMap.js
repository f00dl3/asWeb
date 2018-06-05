/* 
by Anthony Stump
Created: 29 May 2018
Updated: 5 Jun 2018
 */

function generateMapHolder() {
    var mapHeight = "95%";
    var mapWidth = "100%";
    var styleForMap = "<style>.map { height: " + mapHeight + "; width: " + mapWidth + "; }</style>";
    var rData = styleForMap + "<div id='map' class='map'></div><br/>" +
            "<div id='MessageHolder'></div>";
    dojo.byId("OLMapHolder").innerHTML = rData;
    initMap();
}

function initMap() {
    switch(doAction) {
        case "Draw": putDrawPathMap(); break;
        case "Image": renderImage(); break; // pass get data to it
        default: putSimpleMap(); break;
    }
}

function initOLMapPage() {
    generateMapHolder();
}

dojo.ready(initOLMapPage);