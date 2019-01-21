/* 
by Anthony Stump
Created: 19 Jul 2018
Updated: 21 Jan 2019
 */

function addMediaMarker(map, tMedia) {
    try {
        var tCoord = JSON.parse(tMedia.GeoData);
        var point = new ol.geom.Point(tCoord);
        point.transform('EPSG:4326', 'EPSG:3857');
        var iconFeature = new ol.Feature({
            fileName: tMedia.Path + "/" + tMedia.File,
            geometry: point,
            type: "Photo"
        });
        var icLabel = "+";
        var icColor = "#000000";
        var icOpacity = "1";
        var icLabelColor = "#ffffff";
        iconFeature.setStyle(svgIconStyle("ct", 25, icColor, icOpacity, icLabel, icLabelColor));
        return iconFeature;
    } catch (error) { }
}

function addMedia(map, mediaData) {
    console.log(mediaData);
    var vectorSource = new ol.source.Vector({});
    mediaData.forEach(function (tMedia) {
        if(isSet(tMedia.GeoData)) {
            try {
                vectorSource.addFeature(addMediaMarker(map, tMedia));
            } catch (error) {}
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
                case "Photo":
                    eiData = "<strong>" + feature.get("fileName") + "</strong><p>";
                    break;
            }
            content.innerHTML = eiData;
            overlay.setPosition(eCoord);
        }
    });
}

function getMediaGeoData(map) {
    aniPreload("on");
    var isMobile = "no";
    var aContent = 0;
    if(checkMobile()) {
        isMobile = "yes";
    }
    if(isSet(hiddenFeatures)) { aContent = 1; }
    var thePostData = {
        "doWhat": "getIndexed",
        "isMobile": isMobile,
        "aContent": aContent
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("MediaServer"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    addMedia(map, data.Index);
                    aniPreload("off");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for Media Server Index FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

