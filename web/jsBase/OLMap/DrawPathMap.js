/* 
by Anthony Stump
Created: 29 May 2018
Split off from OLMap.js on 30 May 2018
 */

function putDrawPathMap() {
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
    var raster = localTiles;
    var style = {
        'LineString': new ol.style.Style({
            stroke: new ol.style.Stroke({
                color: 'yellow',
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
            console.log("Draw event ended.");
            logFeatures();
        }, this);
    }
    function logFeatures() {
        var writer = new ol.format.GeoJSON();
        var geoJsonStr = writer.writeFeatures(vectorSource.getFeatures());
        dojo.byId("MessageHolder").innerHTML = geoJsonStr;
    }
    addInteraction();
}



