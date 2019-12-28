/* 
by Anthony Stump
Created: 24 Dec 2019
Updated: on Creation
 */

function putDrawPathMapV3() {

	var raster = new ol.layer.Tile({
	  source: new ol.source.OSM()
	});

	var source = new ol.source.Vector({wrapX: false});

	var vector = new ol.layer.Vector({
	  source: source
	});

	var map = new ol.Map({
	  layers: [raster, vector],
	  target: 'map',
	  view: new ol.View({
	    center: [-11000000, 4600000],
	    zoom: 4
	  })
	});

	var typeSelect = "LineString";

	var draw;

	typeSelect.onchange = function() {
	  map.removeInteraction(draw);
	  addInteraction();
	};

	function addInteraction() {
	  var value = typeSelect.value;
	  if (value !== 'None') {
	    draw = new ol.interaction.Draw({
	      source: source,
	      type: typeSelect.value
	    });
	    map.addInteraction(draw);
	  }
	}
	
	addInteraction();

}


