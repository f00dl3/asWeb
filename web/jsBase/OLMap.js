/* 
by Anthony Stump
Created: 29 May 2018
Updated: 27 Jun 2019
 */

function generateMapHolder() {
    var mapHeight = "95%";
    if(checkMobile()) { mapHeight = "100%"; }
    var mapWidth = "100%";
    var styleForMap = "<style>.map { height: " + mapHeight + "; width: " + mapWidth + "; }</style>";
    var rData = styleForMap + 
            "<div id='MarkerHolder'></div>" +
            "<div id='map' class='map'></div>" +
            "<div id='mapEx'></div>" +
            "<div id='popup' class='ol-popup'>" +
            "<a href='#' id='popup-closer' class='ol-popup-closer'></a>" +
            "<div id='popup-content'></div>" +
            "<div id='animationElement'></div>" +
            "</div>";
    if(!checkMobile()) {
        rData += "<br/><div id='MessageHolder'></div>";
    } else {
        hideFooter();
    }
    dojo.byId("OLMapHolder").innerHTML = rData;
    container = dojo.byId("popup");
    content = dojo.byId("popup-content");
    closer = dojo.byId("popup-closer");
    /* FOR FUTURE LOCATIONT TRACKING! ***
    showNotice("<button class='UButton' id='trackMe'>Track me!</button>");
    var consentTracking = dojo.byId("trackMe");
    dojo.connect(consentTracking, "click", actOnTrackMe); */
    initMap();
}

function initMap() {
    switch(doAction) {
    	case "Draw": putDrawPathMap(); break;
    	case "Draw2": putDrawPathMapV2(); break;
        case "G16IR": renderImage("G16IR"); break;
        case "G16VIS": renderImage("G16VIS"); break;
        case "Image": renderImage(); break;
        case "PointClick": putPointClickMap(); break;
        default: putSimpleMap(); break;
    }
}

function initOLMapPage() {
    generateMapHolder();
}

dojo.ready(initOLMapPage);