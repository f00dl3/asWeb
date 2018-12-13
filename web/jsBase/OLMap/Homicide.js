/* 
by Anthony Stump
Created: 18 Jul 2018
Updated: 12 Dec 2018
 */

function addHomicideMarker(map, tHomicide) {
    var currentYear = getDate("hour", 0, "yearOnly");
    var tCoord = JSON.parse(tHomicide.Point);
    var point = new ol.geom.Point(tCoord);
    point.transform('EPSG:4326', 'EPSG:3857');
    var iconFeature = new ol.Feature({
        geometry: point,
        homDate: tHomicide.Date,
        homSynop: tHomicide.Description,
        homId: tHomicide.HomID,
        type: "Homicide",
        victAge: tHomicide.Age,
        victName: tHomicide.Victim,
    });
    var icLabel = "+";
    var icColor = "#000000";
    var icOpacity = "0.5";
    var icLabelColor = "#000000";
    if((tHomicide.Date).includes(curYear+"-")) {
        icColor = "#FF0000";
        icLabelColor = "#FF0000";
        icOpacity = "1.0";
    } else if((tHomicide.Date).includes((curYear-1)+"-")) {
        icColor = "#0000FF";
        icLabelColor = "#0000FF";
        icOpacity = "0.9";
    } else if((tHomicide.Date).includes((curYear-2)+"-")) {
        icColor = "#00FF00";
        icLabelColor = "#00FF00";
        icOpacity = "0.8";
    }
    iconFeature.setStyle(svgIconStyle("ct", 20, icColor, icOpacity, icLabel, icLabelColor));
    return iconFeature;
}

function addHomicides(map, homicideData) {
    console.log(homicideData);
    var vectorSource = new ol.source.Vector({});
    homicideData.forEach(function (tHomicide) {
        if(isSet(tHomicide.Point)) {
            vectorSource.addFeature(addHomicideMarker(map, tHomicide));
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
                case "Homicide":
                    eiData = "<strong>" + feature.get("homDate") + "</strong><p>";
                    if(isSet(feature.get("victName"))) { eiData += "<strong>Victim</strong>: " + feature.get("victName") + "<br/>"; }
                    if(isSet(feature.get("victAge"))) { eiData += "<strong>Victim Age</strong>: " + feature.get("victAge") + "<br/>"; }
                    if(isSet(feature.get("homSynop"))) { eiData += feature.get("homSynop"); }
                    eiData += "<p><em>HomID: " + feature.get("homId") + "</em>";
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
            .get(getResource("Homicide"), {
                    handleAs: "json"  
            }).then(
                function(data) {
                    addHomicides(map, data);
                    aniPreload("off");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for Homicides!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                })
    });
}

