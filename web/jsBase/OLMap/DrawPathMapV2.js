/*

DrawPathMapV2.js
by Anthony Stump
Created: 27 Jun 2019
Updated: on Creation

sample from https://openlayers.org/en/latest/examples/measure.html


import Map from 'ol/Map.js';
import {unByKey} from 'ol/Observable.js';
import Overlay from 'ol/Overlay.js';
import {getArea, getLength} from 'ol/sphere.js';
import View from 'ol/View.js';
import {LineString, Polygon} from 'ol/geom.js';
import Draw from 'ol/interaction/Draw.js';
import {Tile as TileLayer, Vector as VectorLayer} from 'ol/layer.js';
import {OSM, Vector as VectorSource} from 'ol/source.js';
import {Circle as CircleStyle, Fill, Stroke, Style} from 'ol/style.js';
*/
function putDrawPathMapV2() {
	
	var raster = new ol.layer.Tile({ source: new ol.source.OSM() });
	var source = new ol.source.Vector();
	
	var vector = new ol.layer.Vector({
		source: source,
		style: new ol.style.Style({
			fill: new ol.style.Fill({ color: 'rgba(255 ,255, 255, 0.2)' }),
			stroke: new ol.style.Stroke({
				color: '#ffcc33',
				width: 2
			}),
			image: new ol.style.Circle({
				radius: 7,
				fill: new ol.style.Fill({ color: '#ffcc33' })
			})
		})
	});
	
	var sketch;
	var helpTooltipElement;
	var measureTooltipElement;
	var measureTooltip;
	var continuePolygonMsg = 'Click to continue drawing the polygon';
	var continueLineMsg = 'Click to continue drawing the line';
	
	var pointerMoveHandler = function(evt) {
		
		if(evt.dragging) { return; }
		
		var helpMsg = 'Click to start drawing';
		
		if(sketch) {
			var geom = (sketch.getGeometry());
			if(geom instanceof ol.geom.Polygon) {
				helpMsg = continuePolygonMsg; 
			} else if (geom instanceof ol.geom.LineString) {
				helpMsg = continueLineMsg;
			}
		}
		
		helpTooltipElement.innerHTML = helpMsg;
		helpTooltip.setPosition(evt.coordinate);
		helpToolTipElement.classList.remove('hidden');
		
	};
	
	var map = new ol.Map({
		layers: [ raster, vector ],
		target: 'map',
		view: new ol.View({
			center: [ -11000000, 4600000 ],
			zoom: 15
		})
	});
	
	map.on('pointermove', pointerMoveHandler);
	
	map.getViewport().addEventListener('mouseout', function() {
		helpTooltipElement.classList.add('hidden');
	});
	
	var typeSelect = document.getElementById('type');
	var draw;
	
	var formatLength = function(line) {
		var length = ol.sphere.getLength(line);
		var output;
		if (length > 100) { 
			output = (Math.round(length / 1000 * 100) / 100) + ' ' + 'km';
		} else {
			output = (Math.round(length * 100) / 100) + ' ' + 'm';
		}
		return output;
	};
	
	var formatArea = function(polygon) {
		var area = ol.sphere.getArea(polygon);
		var output;
		if (area > 10000) {
			output = (Math.round(area / 1000000 * 100) / 100) + ' ' + 'km<sup>2</sup>';
		} else {
			output = (Math.round(area / 100) / 100) + ' ' + 'm<sup>2</sup>';
		}
		return output;
	};
	
	function addInteraction() {
		
		var type = (typeSelect.value == 'area' ? 'Polygon' : 'LineString');
		
		draw = new ol.interaction.Draw({
			source: source,
			type: type,
			style: new ol.style.Style({
				fill: new ol.style.Fill({ color: 'rgba(255, 255, 255, 0.2)' }),
				stroke: new ol.style.Stroke({
					color: 'rgba(0, 0, 0, 0.5)',
					lineDash: [10, 10],
					width: 2
				}),
				image: new ol.style.Circle({
					radius: 5,
					stroke: new ol.style.Stroke({ color: 'rgba(0, 0, 0, 0.7)' }),
					fill: new ol.style.Fill({ color: 'rgba(255, 255, 255, 0.2)' })
				})
			})

		});
		map.addInteraction(draw);
		
		createMeasureTooltip();
		createHelpTooltip();
		var listener;
		
		draw.on('drawstart', function(evt) {
			sketch = evt.feature;
			var tooltipCoord = evt.coordinate;
			listener = sketch.getGeometry().on('change', function(evt) {
				var geom = evt.target;
				var output;
				if(geom instanceof ol.geom.Polygon) {
					output = formatArea(geom);
					tooltipCoord = geom.getInteriorPoint().getCoordinates();
				} else if (geom instanceof ol.geom.LineString) {
					output = formatLength(geom);
					tooltipCoord = geom.getLastCoordinate();
				}
				measureTooltipElement.innerHTML = output;
				measureTooltip.setPosition(tooltipCoord);
			});
		}, this);
		
		draw.on('drawend', function() {
			measureTooltipElement.className = 'od2-tooltip od2-tooltip-static';
			measureTooltip.setOffset([ 0, -7 ]);
			sketch = null;
			measureTooltipElement = null;
			createMeasureTooltip();
			unByKey(listener);
		}, this);
		
	}
	
	function createHelpTooltip() {
		if(helpTooltipElement) { helpTooltipElement.parentNode.removeChild(helpTooltipElement); }
		helpTooltipElement = document.createElement('div');
		helpTooltipElement.className = 'od2-tooltip hidden';
		helpTooltip = new ol.overlay.Overlay({
			element: helpTooltipElement,
			offset: [ 15, 0 ],
			positioning: 'center-left'
		});
		map.addOverlay(helpTooltip);
	}
	
	function createMeasureTooltip() {
		if(measureTooltipElement) { measureTooltipElement.parentNode.removeChild(measureTooltipElement); }
		measureTooltipElement = document.createElement('div');
		measureTooltipElement.className = 'od2-tooltip od2-tooltip-measure';
		measureTooltip = new ol.overlay.Overlay({
			element: measureTooltipElement,
			offset: [ 0, -15 ],
			positioning: 'bottom-center'
		});
		map.addOverlay(measureTooltip);
	}
	
	typeSelect.onchange = function() {
		map.removeInteraction(draw);
		addInteraction();
	};
	
	addInteraction();
	
}