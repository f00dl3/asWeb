/* 
by Anthony Stump
Created: 29 May 2018
Split off from OLMap.js on 30 May 2018
Updated: 27 Dec 2019
 */
    

function putDrawPathMap4() {

	var mapProjection = "EPSG:3857";
	var goodProjection = "EPSG:4326";
	var mapProjectionObject = ol.proj.get(mapProjection);

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
        	//projection: mapProjection,
            center: wmGeoJson,
            zoom: 14
        })
    });

	var sketch;
	var helpTooltipElement;
	var helpTooltip;
	var measureTooltipElement;
	var measureTooltip;
	var continueLineMsg = 'Click to continue drawing the line';

	function createHelpTooltip() {
		  if (helpTooltipElement) {
		    helpTooltipElement.parentNode.removeChild(helpTooltipElement);
		  }
		  helpTooltipElement = document.createElement('div');
		  helpTooltipElement.className = 'od2-tooltip hidden';
		  helpTooltip = new ol.Overlay({
		    element: helpTooltipElement,
		    offset: [15, 0],
		    positioning: 'center-left'
		  });
		  map.addOverlay(helpTooltip);
	}

	function createMeasureTooltip() {
		  if (measureTooltipElement) {
		    measureTooltipElement.parentNode.removeChild(measureTooltipElement);
		  }
		  measureTooltipElement = document.createElement('div');
		  measureTooltipElement.className = 'od2-tooltip od2-tooltip-measure';
		  measureTooltip = new ol.Overlay({
		    element: measureTooltipElement,
		    offset: [0, -15],
		    positioning: 'bottom-center'
		  });
		  map.addOverlay(measureTooltip);
	}

    var pointerMoveHandler = function(evt) {
    	  if (evt.dragging) {
    	    return;
    	  }
    	  var helpMsg = 'Click to start drawing';
    	  if (draw) {
    	    var geom = sketch.getGeometry();
    	    if (geom instanceof Polygon) {
    	      helpMsg = continuePolygonMsg;
    	    } else if (geom instanceof LineString) {
    	      helpMsg = continueLineMsg;
    	    }
    	  }

    	  helpTooltipElement.innerHTML = helpMsg;
    	  helpTooltip.setPosition(evt.coordinate);

    	  helpTooltipElement.classList.remove('hidden');
	};
	
    function addInteraction() {
        var typeOfDraw = "LineString";
        draw = new ol.interaction.Draw({
            source: vectorSource,
            type: typeOfDraw
        });
        map.addInteraction(draw);   
        vectorSource.addFeatures(draw);
        createMeasureTooltip();
        createHelpTooltip();

        var listener;
        
        draw.on('drawstart', function(evt) {
            sketch = evt.feature;
            var tooltipCoord = evt.coordinate;
            listener = sketch.getGeometry().on('change', function(evt) {
              var geom = evt.target;
              var output;
              if (geom instanceof ol.geom.Polygon) {
                output = formatArea(geom);
                tooltipCoord = geom.getInteriorPoint().getCoordinates();
              } else if (geom instanceof ol.geom.LineString) {
                output = formatLength(geom);
                tooltipCoord = geom.getLastCoordinate();
              }
              measureTooltipElement.innerHTML = output;
              measureTooltip.setPosition(tooltipCoord);
            });
        });
        
        draw.on('drawend', function(evt) {   
            measureTooltipElement.className = 'od2-tooltip od2-tooltip-static';
            measureTooltip.setOffset([0, -7]);
            sketch = null;
            measureTooltipElement = null;
            createMeasureTooltip();
            map.removeInteraction(draw);
            showNotice("Draw event ended!");
            var feature = evt.feature;
            var features = vectorSource.getFeatures();
            features = features.concat(feature);
            logFeatures(features);
            unByKey(listener);
        }, this);


        map.on('pointermove', pointerMoveHandler);

        map.getViewport().addEventListener('mouseout', function() {
          helpTooltipElement.classList.add('hidden');
        });

    }
    
    function logFeatures(features, lineLength) {
	    var writer = new ol.format.GeoJSON();
	    var geojsonStr = writer.writeFeatures(features, {featureProjection: 'EPSG:3857'});
        //var lineLength = features.getGeometry().getLength();
	    //console.log(JSON.parse(features));
        //var line = new ol.geom.LineString(features);
        //console.log(line.toString());
        //var lineLength = Math.round(line.getLength() * 100) / 100
        //console.log("LINE LENGHT: " + lineLength + " METERS");
        dojo.byId("MessageHolder").innerHTML = lineLength; // Returns empty
        generateDynamicDownload(geojsonStr, "output.geojson");   
    }
    	
    addInteraction();
}


