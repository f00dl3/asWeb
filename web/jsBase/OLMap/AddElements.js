/* 
by Anthony Stump
Created: 31 May 2018
Updated: 26 Aug 2018
*/

var routeLayer;

function addLineStringToMap(map, pointsToAdd, caption) {
    if(!isSet(caption)) { var caption = pointsToAdd; }
    var polyLine = new ol.geom.LineString(pointsToAdd);
    polyLine.transform('EPSG:4326', 'EPSG:3857');
    var rFeature = new ol.Feature({
        geometry: polyLine,
        captionText: caption,
        type: "Route"
    });
    rFeature.setStyle(routeStyle);
    var vSource = new ol.source.Vector({ features: [rFeature] });
    routeLayer = new ol.layer.Vector({ source: vSource });
    map.addLayer(routeLayer);
    map.getView().fit(vSource.getExtent(), map.getSize());
}

function addPhotoMarker(map, jsonData) {
    var tCoord = JSON.parse(jsonData.GeoData);
    var point = new ol.geom.Point(tCoord);
    point.transform('EPSG:4326', 'EPSG:3857');
    var parentFolder = (jsonData.Path).replace("Pics/20","asWeb/x/PicsL");
    var photoResourceLocation = parentFolder + "/size/" + jsonData.File;
    var photoIcon = new ol.Feature({
        description: jsonData.Description,
        name: jsonData.File,
        type: "Photo",
        geometry: point,
        location: jsonData.GeoData,
        path: jsonData.Path,
        resolution: jsonData.Resolution,
        urlThumb: photoResourceLocation.replace("size", "thumb"),
        urlFull: photoResourceLocation.replace("size", "full").replace("asWeb/x", "asWeb/OLMap.jsp?Action=Image&Input=/asWeb/x") + "&Resolution=" + jsonData.Resolution
    });
    return photoIcon;
}   

function addPointToMap(map, tPoint) {
    var tCoord = JSON.parse(tPoint);
    var point = new ol.geom.Point(tCoord);
    point.transform('EPSG:4326', 'EPSG:3857');
    var iconFeature = new ol.Feature({
        geometry: point,
        type: "Point"
    });
    var icLabel = "+";
    var icColor = "#ffffff";
    var icOpacity = "1";
    var icLabelColor = "#000000";
    iconFeature.setStyle(svgIconStyle("ct", 30, icColor, icOpacity, icLabel, icLabelColor));
    var vectorSource = new ol.source.Vector({});
    vectorSource.addFeature({ iconFeature });
    overlayLayer = new ol.layer.Vector({source: vectorSource});
    map.addLayer(overlayLayer);
    map.getView().fit(vectorSource.getExtent(), map.getSize());
}