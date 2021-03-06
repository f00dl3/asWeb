/* 
by Anthony Stump
Created: 29 May 2018
Split off from OLMap.js on 30 May 2018
Updated: 27 Dec 2019
 */

// SET TILE SOURCE HERE! == R for REMOTE, L for LOCAL
var tileSourceFlag = "R";

var formatArea = function(polygon) {
  var area = getArea(polygon);
  var output;
  if (area > 10000) {
    output = (Math.round(area / 1000000 * 100) / 100) +
        ' ' + 'km<sup>2</sup>';
  } else {
    output = (Math.round(area * 100) / 100) +
        ' ' + 'm<sup>2</sup>';
  }
  return output;
};

var formatLength = function(line) {
	  var length = getLength(line);
	  var output;
	  if (length > 100) {
	    output = (Math.round(length / 1000 * 100) / 100) +
	        ' ' + 'km';
	  } else {
	    output = (Math.round(length * 100) / 100) +
	        ' ' + 'm';
	  }
	  return output;
};
	
function getOSMAttribution() {
    var currentYear = getDate("day", 0, "yearOnly");
    var rString = "Anthony Stump, 2015-" + currentYear + "<br/>" +
            "GeoServer Docs: <a href='/geoserver-doc/user' target='gsDoc'>USR</a> <a href='/geoserver-doc/developer' target='gsDoc'>DEV</a>";
    return rString;
}

function getOLPopup() {
    var popup = new ol.Overlay.Popup;
    popup.setOffset([0, -55]);
    return popup;
}

function svgIconStyle(type, size, thisColor, opacity, label, labelColor) {
    var svgData = "";
    switch(type) {
        case "c":
            var svgSize = size * 2;
            svgData += '<svg width="' + svgSize + '" height="' + svgSize + '" version="1.1" xmlns="http://www.w3.org/2000/svg">' +
                    '<circle cx="' + size + '" cy="' + size + '" r="' + size + '" fill="' + thisColor + '"/>' +
                    '</svg>';
            break;
        case "ct":
            var svgSize = size * 2;
            var fontSize = size;
            svgData += '<svg width="' + svgSize + '" height="' + svgSize + '" version="1.1" xmlns="http://www.w3.org/2000/svg">' +
                    '<circle cx="' + size + '" cy="' + size + '" r="' + size + '" fill="' + thisColor + '"/>' +
                    '<text x="' + svgSize/2 + '" y="' + svgSize/2 + '" fill="' + labelColor + '" text-anchor="middle" alignment-baseline="middle" style="font-size:' + fontSize + 'px;">' + label + '</text>' +
                    '</svg>';
            break;
        case "s":
            svgData += '<svg width="' + svgSize + '" height="' + svgSize + '" version="1.1" xmlns="http://www.w3.org/2000/svg">' +
                    '<rect x="' + size + '" y="' + size + '" width="' + svgSize + '" height="' + svgSize + '" style="fill:' + thisColor + ';stroke:' + thisColor + ';stroke-width:0;fill-opacity:' + opacity + ';stoke-opacity: 1"/>' +
                    '<text x="' + svgSize/2 + '" y="' + svgSize/2 + '" fill="' + labelColor + '" text-anchor="middle" alignment-baseline="middle">' + label + '</text>' +
                    '</svg>';
            break;
    }
    var genSvgUrl = "data:image/svg+xml;utf8," + encodeURIComponent(svgData);
    var style = new ol.style.Style({
        image: new ol.style.Icon({
            opacity: opacity,
            src: genSvgUrl,
            scale: 0.3
        })
    });
    return style;
}

var activity;
var container;
var content;
var content2;
var closer;
var map;
var overlay;
var routeColorBlack = 'rgba(0,0,0,0.4)';
var routeColorYellow = 'rgba(255,255,0,0.4)';
var tileSuffix = "{z}/{x}/{y}.png";
var tilePathLocal = getBasePath("osmTiles") + tileSuffix;
//var tilePathRemote_190626 = "https://tile.openstreetmap.fr/" + tileSuffix;
var wmGeoJson = ol.proj.fromLonLat(getHomeGeo("geoJsonRaw"));

var localTiles = new ol.layer.Tile({
    source: new ol.source.OSM({
        attributions: [
            getOSMAttribution() + '<br/>',
            ol.source.OSM.ATTRIBUTION
        ],
        crossOrigin: null,
        opaque: false,
        url: tilePathLocal
    })
});

var remoteTiles = new ol.layer.Tile({ source: new ol.source.OSM() });

var homeView = new ol.View({
    center: wmGeoJson,
    zoom: 14,
    maxZoom: 16
});

switch(tileSourceFlag) {
    case "R": 
        tileSource = remoteTiles;
        routeColor = routeColorBlack;
        break;
    case "L":
        tileSource = localTiles;
        routeColor = routeColorYellow;
        break;
}

var routeStyle = new ol.style.Style({
    stroke: new ol.style.Stroke({
        color: routeColor,
        width: 5
    })
});
