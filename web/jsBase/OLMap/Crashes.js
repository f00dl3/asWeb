/* 
by Anthony Stump
Created: 16 Dec 2018
 */

function addCrashMarker(map, tCrash) {
    var tCoord = JSON.parse(tHomicide.Point);
    var point = new ol.geom.Point(tCoord);
    point.transform('EPSG:4326', 'EPSG:3857');
    var iconFeature = new ol.Feature({
        geometry: point,
        crashDate: tCrash.Date,
        crashTime: tCrash.Time,
        crashSynop: tCrash.Synopsis,
        crashId: tCrash.Incident,
        crashLocation: tCrash.Location,
        crashVehicles: tCrash.Damage,
        crashInjuries: tCrash.InjuryTypes,
        type: "Crash"
    });
    var icLabel = "+";
    var icColor = "#000000";
    var icOpacity = "0.5";
    var icLabelColor = "#000000";
    iconFeature.setStyle(svgIconStyle("ct", 20, icColor, icOpacity, icLabel, icLabelColor));
    return iconFeature;
}

function addCrashes(map, crashData) {
    var vectorSource = new ol.source.Vector({});
    crashData.forEach(function (tHomicide) {
        if(isSet(tHomicide.Point)) {
            vectorSource.addFeature(addCrashMarker(map, tHomicide));
        }
    });
    overlayLayer = new ol.layer.Vector({source: vectorSource});
    map.addLayer(overlayLayer);
    map.on('click', function (evt) {
        var feature = map.forEachFeatureAtPixel(evt.pixel, function (feature, layer) {
            return feature;
        });
        if (feature) {
            $("#popup").toggle();
            var eCoord = evt.coordinate;
            var eiData = "";
            switch (feature.get("type")) {
                case "Crash":
                    eiData = "<strong>" + feature.get("crashDate") + " @ " + feature.get("crashTime") + "</strong><p>";
                    if(isSet(feature.get("crashLocation"))) { eiData += "<strong>Location</strong>: " + feature.get("crashLocation") + "<br/>"; }
                    if(isSet(feature.get("crashVehicles"))) { eiData += "<strong>Vehicles</strong>: " + feature.get("crashVehicles") + "<br/>"; }
                    if(isSet(feature.get("crashInjuries"))) { eiData += "<strong>Injuries</strong>: " + feature.get("crashInjuries") + "<br/>"; }
                    if(isSet(feature.get("crashSynopsis"))) { eiData += feature.get("crashSynopsis") + "<br/>"; }
                    eiData += "<p><em>ID: " + feature.get("crashId") + "</em>";
                    break;
            }
            content.innerHTML = eiData;
            overlay.setPosition(eCoord);
        }
    });
}

function getHomicides(map) {
    require(["dojo/request"], function(request) {
        request
            .get(getResource("CrashData"), {
                    handleAs: "json"  
            }).then(
                function(data) {
                    addHomicides(map, data);
                    aniPreload("off");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for Crash Data Fail!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                })
    });
}

