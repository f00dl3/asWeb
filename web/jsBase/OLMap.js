/* 
by Anthony Stump
Created: 29 May 2018
Updated: 7 Jun 2018
 */

function generateMapHolder() {
    var mapHeight = "95%";
    if(checkMobile()) { mapHeight = "100%"; }
    var mapWidth = "100%";
    var styleForMap = "<style>.map { height: " + mapHeight + "; width: " + mapWidth + "; }</style>";
    var rData = styleForMap + "<div id='map' class='map'></div>";
    if(!checkMobile()) {
        rData += "<br/><div id='MessageHolder'></div>";
    } else {
        hideFooter();
    }
    dojo.byId("OLMapHolder").innerHTML = rData;
    initMap();
}

function initMap() {
    switch(doAction) {
        case "Draw": putDrawPathMap(); break;
        case "G16IR": renderImage("G16IR"); break;
        case "G16VIS": renderImage("G16VIS"); break;
        case "Image": renderImage(); break;
        default: putSimpleMap(); break;
    }
}

function initOLMapPage() {
    generateMapHolder();
}

dojo.ready(initOLMapPage);