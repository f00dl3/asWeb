/* 
 * by Anthony Stump
 * Created: 7 Jun 2018
 * this is the graveyard for non-working OpenLayers methods,
 * saving just incase I ever need them again.
 */

function addGpsMarkersMethod1(map, jsonData) {
    console.log("addGpsMarkersMethod1(map, jsonData) called!");
    var tCoord = [ jsonData.Longitude, jsonData.Latitude ];
    var point = new ol.geom.Point(tCoord);
    point.transform('EPSG:4326', 'EPSG:3857');
    var feature = new ol.Feature(point);
    feature.setStyle(genSquareMarker("red", 5));
    var source = new ol.source.Vector({ feature: feature });
    var vectorLayer = new ol.layer.Vector({ source: source });
    map.addLayer(vectorLayer);
}

function addGpsMarkersMethod2(map, jsonData, iter, markerType) {
    var tCoord = [ jsonData.Longitude, jsonData.Latitude ];
    var point = new ol.geom.Point(tCoord);
    point.transform('EPSG:4326', 'EPSG:3857');
    var pos = ol.proj.fromLonLat(tCoord);
    var marker = new ol.Overlay({
        position: pos,
        positioning: 'center-center',
        element: document.getElementById(markerType + iter),
        stopEvent: false
    });
    map.addOverlay(marker);
    console.log("addGpsMarkersMethod2(map, jsonData, " + iter + ", " + markerType + ") called! --> " + tCoord);
}

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