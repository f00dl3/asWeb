/* 
by Anthony Stump
Created: 30 Mar 2018
 */

function initMaps() {
    var tilesLocal = getBasePath("osmTiles");
    var tilesRemote = "http://a.tile.openstreetmap.org/";
    var tileServer = tilesLocal;
    var lineColor = "#000000";
    var lineOpacity = 0.75;
    if(tileServer === tilesLocal) {
        lineColor = "#ffff00";
        lineOpacity = 0.4;
    }
}

