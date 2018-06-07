/* 
by Anthony Stump
Created: 29 May 2018
Split off from OLMap.js on 30 May 2018
Updated: 7 Jun 2018
 */

function getOSMAttribution() {
    var currentYear = getDate("day", 0, "yearOnly");
    var rString = "Anthony Stump, 2015-" + currentYear;
    return rString;
}

function svgIconStyle(type, size, color, opacity) {
    var svgData = "";
    switch(type) {
        case "c":
            var svgSize = size * 2;
            svgData += '<svg width="' + svgSize + '" height="' + svgSize + '" version="1.1" xmlns="http://www.w3.org/2000/svg">' +
                    '<circle cx="' + size + '" cy="' + size + '" r="' + size + '" fill="' + color + '"/>' +
                    '</svg>';
    }
    var genSvgUrl = "data:image/svg+xml;utf8," + svgData;
    //console.log(genSvgUrl);
    var style = new ol.style.Style({
        image: new ol.style.Icon({
            opacity: opacity,
            src: genSvgUrl,
            scale: 0.3
        })
    });
    return style;
}

var map;
var remoteTiles = new ol.layer.Tile({ source: new ol.source.OSM() });
var tilePathLocal = getBasePath("osmTiles") + "{z}/{x}/{y}.png";
var wmGeoJson = ol.proj.fromLonLat(getHomeGeo("geoJsonRaw"));


var routeStyle = new ol.style.Style({
    stroke: new ol.style.Stroke({
        color: 'rgba(255,255,0,0.4)',
        width: 5
    })
});

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