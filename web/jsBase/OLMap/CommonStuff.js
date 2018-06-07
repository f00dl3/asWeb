/* 
by Anthony Stump
Created: 29 May 2018
Split off from OLMap.js on 30 May 2018
Updated: 7 Jun 2018
 */

function genDivMarker(type) {
    switch(type) {
        case "circ": return "<div class='olCircMarker'></div>";
    }
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