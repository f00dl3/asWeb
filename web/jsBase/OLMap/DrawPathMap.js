/* 
by Anthony Stump
Created: 29 May 2018
Split off from OLMap.js on 30 May 2018
Updated: 28 Dec 2019
 */

var mAdjustment = 0.740586;
var mapProjection = "EPSG:3857";
var goodProjection = "EPSG:4326";
var mapProjectionObject = ol.proj.get(mapProjection);
var lineLength = "Not started yet!";

function putDrawPathMap() {
    var lineColor = "black";
    window.app = {};
    var app = window.app;
    app.exportJson = function(opt_options) {
        var options = opt_options || {};
        var button = document.createElement('button');
        button.innerHTML = "X";
        var this_ = this;
        var handleExportJson = function() {
            window.alert("Button clicked!");
        };
        button.addEventListener('click', handleExportJson, false);
        var element = document.createElement('div');
        element.className = 'ol-unselectable ol-control';
        element.appendChild(button);
        ol.control.Control.call(this, {
            element: element,
            target: options.target
        });
    };
    ol.inherits(app.exportJson, ol.control.Control);
    var jsonDrawn = {
        'type': 'FeatureCollection',
        'crs': { 'type': 'name' },
        'features': []
    };
    
    var draw;
    var wmGeoJson = ol.proj.fromLonLat(getHomeGeo("geoJsonRaw"));
    var features = (new ol.format.GeoJSON()).readFeatures(jsonDrawn);
    var raster = remoteTiles;
    var style = {
        'LineString': new ol.style.Style({
            stroke: new ol.style.Stroke({
                color: lineColor,
                width: 5
            })
        })
    };
    var styleFunction = function(feature) {
        return style[feature.getGeometry().getType()];
    };
    var vectorSource = new ol.source.Vector({
        features: features,
        format: new ol.format.GeoJSON()
    });
    var vectorLayer = new ol.layer.Vector({
        source: vectorSource,
        style: styleFunction
    });
    var map = new ol.Map({
        controls: ol.control.defaults({
            attributionOptions: {
                collapsible: true
            }
        }).extend([
            new app.exportJson()
        ]),
        projection: mapProjection,
        target: 'map',
        layers: [
            raster, vectorLayer
        ],
        view: new ol.View({
            center: wmGeoJson,
            zoom: 14
        })
    });
    function addInteraction() {
    	var refLineLength = "N/A";
        var typeOfDraw = "LineString";
        draw = new ol.interaction.Draw({
            source: vectorSource,
            type: typeOfDraw
        });
        map.addInteraction(draw);   
        vectorSource.addFeatures(draw);
        draw.on('drawstart', function(evt) {
        	evt.feature.on("change", function (evt) {
            	var measurement = evt.target.getGeometry().getLength();
            	var measurementFixed = measurement * mAdjustment;
            	var distanceKm = (measurementFixed / 1000).toFixed(2);
            	var distanceMi = (distanceKm * 0.621371).toFixed(2);
            	lineLength = refLineLength = distanceKm;
            	var noticeString = "Distance: " + distanceKm + " km (" + distanceMi + " mi)";
            	showNotice(noticeString);
            });
        });
        draw.on('drawend', function(evt) {
            map.removeInteraction(draw);
            showNotice("Draw event ended!");
            var feature = evt.feature;
            var features = vectorSource.getFeatures();
            features = features.concat(feature);
            distanceMi = (refLineLength * 0.621371).toFixed(2);
            fullRefLineLength = refLineLength + " km (" + distanceMi + " mi)";
            logFeatures(features, fullRefLineLength);
        }, this);
    }
    
    function logFeatures(features, refLineLength) {
	    var writer = new ol.format.GeoJSON();
	    var geojsonStr = writer.writeFeatures(features, {featureProjection: 'EPSG:3857'});
        dojo.byId("MessageHolder").innerHTML = refLineLength;
        if(!checkMobile()) {
        	generateDownload(geojsonStr, "points.geojson");   
        } else {
        	dojo.byId("OLMapHolder").innerHTML = geojsonStr;
        }
    }
    
    addInteraction();
    
}


