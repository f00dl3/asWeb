/* 
by Anthony Stump
Created: 29 May 2018
Split off from OLMap.js on 30 May 2018
Updated: 7 Jun 2018
 */

function genDivMarkers(type, kCount) {
    rData = "<div style='display: none;'>";
    for(var i=0; i < kCount; i++) {
        switch(type) {
            case "olCirc":
                rData += "<div class='olCircMarker' id=" + type + i + "'>" + i + "</div>";
                break;
        }
    }
    rData += "</div>";
    return rData;
}

function genSquareMarker(color, radius) {
    console.log("genSquareMarker(" + color + ", " + radius + ") called!");
    var stroke = new ol.style.Stroke({
        color: 'black',
        width: 1
    });
    var fill = new ol.style.Fill({
        color: color
    });
    var square = new ol.style.Style({
        image: new ol.style.RegularShape({
            fill: fill,
            stroke: stroke,
            points: 4,
            radius: radius,
            angle: Math.PI / 4
        })
    });
    return square;
}


function genSvgMarker(color) {
    var size = 5;
    var symbol = [[0,0], [4, 2], [6, 0], [10, 5], [6, 3], [4, 5], [0, 0]];
    var stroke = new ol.style.Stroke({
        color: 'black',
        width: 1
    });
    var fill = new ol.style.Fill({
        color: color
    });
    var canvas = document.createElement('canvas');
    var vectorContext = ol.render.toContext(
            canvas.getContext('2d'), {
                size: [ size, size ],
                pixelRatio: 1
            }
    );
    vectorContext.setStyle(new ol.style.Style({
        fill: fill,
        stroke: stroke
    }));
    vectorContext.drawGeometry(new ol.geom.Polygon([symbol.map()]));
    style = new ol.style.Style({
        image: new ol.style.Icon({
            img: canvas,
            imgSize: [ size, size ],
            rotation: 0
        })
    });
    return style;
    console.log("genSvgMarker(" + color + ") called!");
}

function getOSMAttribution() {
    var currentYear = getDate("day", 0, "yearOnly");
    var rString = "Anthony Stump, 2015-" + currentYear;
    return rString;
}

function reverseGeodata(pointsToAdd) {
    var resortedPoints = [];
    for(var i = 0; i < pointsToAdd.length; i++) { 
        var thisPointSet = pointsToAdd[i];
        resortedPoints.push([ thisPointSet[1], thisPointSet[0] ]);
    }
    return resortedPoints;
}

function svgIconStyle(type, size, color, opacity) {
    var svgData = "";
    switch(type) {
        case "c":
            var svgSize = size * 2;
            svgData += '<svg width="' + svgSize + '" height="' + svgSize + '" version="1.1" xmlns="http://www.w3.org/2000/svg">' +
                    '<circle r="' + size + '" fill="' + color + '"/>' +
                    '</svg>';
    }
    var genSvgUrl = "data:image/svg+xml;utf8," + svgData;
    console.log(genSvgUrl);
    var style = new ol.style.Style({
        image: new ol.style.Icon({
            opacity: opacity,
            src: genSvgUrl,
            scale: 0.3
        })
    });
    return style;
}

function transform_geometry(element) {
    var current_projection = new ol.proj.Projection({code: "EPSG:4326"});
    var new_projection = localTiles.getSource().getProjection();
    element.getGeometry().transform(current_projection, new_projection);
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