/* 
by Anthony Stump
Created: 29 May 2018
Split off from OLMap.js on 30 May 2018
Updated: 28 Jun 2018
 */

function getOSMAttribution() {
    var currentYear = getDate("day", 0, "yearOnly");
    var rString = "Anthony Stump, 2015-" + currentYear;
    return rString;
}

function getOLPopup() {
    var popup = new ol.Overlay.Popup;
    popup.setOffset([0, -55]);
    return popup;
}

function svgIconStyle(type, size, thisColor, opacity, label, labelColor) {
    var svgData = "";
    var svgSize = size * 2;
    switch(type) {
        case "c":
            svgData += '<svg width="' + svgSize + '" height="' + svgSize + '">' +
                    '<circle cx="' + size + '" cy="' + size + '" r="' + size + '" fill="' + thisColor + '"/>' +
                    '</svg>';
            break;
        case "s":
            svgData += '<svg width="' + svgSize + '" height="' + svgSize + '">' +
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

var container, content, closer;
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
    zoom: 15,
    maxZoom: 16
});

var routeStyle = new ol.style.Style({
    stroke: new ol.style.Stroke({
        color: 'rgba(255,255,0,0.4)',
        width: 5
    })
});

var tileSource = localTiles;