/* 
by Anthony Stump
Created: 20 Jun 2018
Updated: 16 Jul 2018
SOME FEATURES FOR LIVE TRACKING IN TESTING - not a priority yet!
 */

function actOnTrackMe() {
    showNotice("Location tracking toggled!");
    geolocation.setTracking(this.checked);
    geolocation.on('change', function() {
        showNotice("Location: " + geolocation.getLongitude() + ", " + geolocation.getLatitude());
    });
    plotLocation();
}

function addObsLocationMarkers(map, description, tCoord) {
    var shortName = "X";
    var point = new ol.geom.Point(tCoord);
    point.transform('EPSG:4326', 'EPSG:3857');
    var iconFeature = new ol.Feature({
        description: description,
        geometry: point,
        latitude: tCoord[1],
        longitude: tCoord[0],
        type: "Location"
    });
    switch (description) {
        case "Home": shortName = "H"; break;
        case "Note3": shortName = "A"; break;
    }
    iconFeature.setStyle(svgIconStyle("ct", 35, "#ffffff", 1, shortName, "#000000"));
    return iconFeature;
}

function plotLocation() {
    var accuracyFeature = new ol.Feature();
    var positionFeature = new ol.Feature();
    geolocation.on('change:accuracyGeometry', function() {
        accuracyFeature.setGeometry(geolocation.getAccuracyGeometry());
    });
    positionFeature.setStyle(new ol.style.Style({
        image: new ol.style.Circle({
            radius: 6,
            fill: new ol.style.Fill({ color: '#3399CC' }),
            stroke: new ol.style.Stroke({ color: '#fff', width: 2 })
        })
    }));
    geolocation.on('change:position', function() {
        var coordinates = geolocation.getPosition();
        positionFeature.setGeometry(coordinates ? new ol.geom.Point(coordinates) : null);
    });
    locationVector = new ol.layer.Vector({
        map: map,
        source: new ol.source.Vector({ features: [ accuracyFeature, positionFeature ] })
    });
    map.addVector(locationVector);
}

var geolocation = new ol.Geolocation({ projection: view.getProjection() });

