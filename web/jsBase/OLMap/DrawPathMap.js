/* 
by Anthony Stump
Created: 29 May 2018
Split off from OLMap.js on 30 May 2018
Updated: 27 Jun 2019
 */

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
        projection: "EPSG:900913",
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
        var typeOfDraw = "LineString";
        draw = new ol.interaction.Draw({
            source: vectorSource,
            type: typeOfDraw
        });
        map.addInteraction(draw);   
        vectorSource.addFeatures(draw);
        draw.on('drawend', function(evt) {
            map.removeInteraction(draw);
            showNotice("Draw event ended!");
            var feature = evt.feature;
            var features = vectorSource.getFeatures();
            features = features.concat(feature);
            logFeatures(features);
        }, this);
    }
    function logFeatures(features) {
        var writer = new ol.format.GeoJSON();
        var drawnData = [];
        features.forEach(function(ft) {
        	var tGeo = ft.getGeometry().getCoordinates();
        	//tGeo = ol.proj.transform(tGeo, 'EPSG:3857', 'EPSG:4326');
        	drawnData.push(tGeo);
        });
        console.log("DRAWN DATA: " + drawnData);
        var line = new ol.geom.LineString(drawnData);
        var lineLength = Math.round(line.getLength() * 100) / 100
        console.log("LINE LENGHT: " + lineLength + " METERS");
        dojo.byId("MessageHolder").innerHTML = JSON.stringify(drawnData); // Returns empty
    }
    addInteraction();
}


