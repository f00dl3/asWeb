/* 
by Anthony Stump
Created: 29 May 2018
Split off from OLMap.js on 30 May 2018
Updated: 12 Aug 2018
 */

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
var tilePathLocal = getBasePath("osmTiles") + "{z}/{x}/{y}.png";
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

var routeColorBlack = 'rgba(0,0,0,0.4)';
var routeColorYellow = 'rgba(255,255,0,0.4)';
var routeStyle = new ol.style.Style({
    stroke: new ol.style.Stroke({
        color: routeColorBlack,
        width: 5
    })
});

var tileSource = remoteTiles;