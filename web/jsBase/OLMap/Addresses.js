/* 
by Anthony Stump
Created: 18 Jul 2018
Updated: 19 Jul 2018
 */

function addAddressMarker(map, thisAddress) {
    var tCoord = JSON.parse(thisAddress.Point);
    var point = new ol.geom.Point(tCoord);
    point.transform('EPSG:4326', 'EPSG:3857');
    if(isSet(thisAddress.LastName) || isSet(thisAddress.FirstName)) {
        if(isSet(thisAddress.LastName)) {
            pointName = thisAddress.LastName + ", " + thisAddress.FirstName;
        } else {
            pointName = thisAddress.FirstName;
        }
    } else {
        pointName = thisAddress.Business;
    }
    var fullAddress = thisAddress.Address + ", " + thisAddress.City + ", " + thisAddress.State + " " + thisAddress.Zip;
    var iconFeature = new ol.Feature({
        rawData: thisAddress,
        fullAddress: fullAddress,
        geometry: point,
        name: pointName,
        type: "Address"
    });
    var icLabel = "A";
    var icColor = "#ffffff";
    var icOpacity = "1";
    var icLabelColor = "#000000";
    iconFeature.setStyle(svgIconStyle("ct", 30, icColor, icOpacity, icLabel, icLabelColor));
    return iconFeature;
}

function addAddresses(map, addressData) {
    console.log(addressData);
    var vectorSource = new ol.source.Vector({});
    addressData.forEach(function (tAddy) {
        if(isSet(tAddy.Point)) {
            vectorSource.addFeature(addAddressMarker(map, tAddy));
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
                case "Address":
                    eiData = "<strong>" + feature.get("name") + "</strong><br/>" +
                            "<strong>Address</strong>: " + feature.get("addressFull") + "<br/>";                    
                    break;
            }
            content.innerHTML = eiData;
            overlay.setPosition(eCoord);
        }
    });
}

function getAddresses(map) {
    require(["dojo/request"], function(request) {
        request
            .get(getResource("Addresses"), {
                    handleAs: "json"  
            }).then(
                function(data) {
                    addAddresses(map, data);
                    aniPreload("off");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for Addresses fail, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                })
    });
}