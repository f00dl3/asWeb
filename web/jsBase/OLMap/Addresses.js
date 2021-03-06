/* 
by Anthony Stump
Created: 18 Jul 2018
Updated: 21 Jan 2019
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
    var fullAddress = thisAddress.Address + "<br/>" + thisAddress.City + ", " + thisAddress.State + " " + thisAddress.Zip;
    var iconFeature = new ol.Feature({
        asOf: thisAddress.AsOf,
        fullAddress: fullAddress,
        geometry: point,
        name: pointName,
        phoneBusiness: thisAddress.P_Business,
        phoneCell: thisAddress.P_Cell,
        phoneCell2: thisAddress.P_Cell2,
        phoneHome: thisAddress.P_Home,
        type: "Address"
    });
    var icLabel = "+";
    var icColor = "#000000";
    var icOpacity = "1";
    var icLabelColor = "#ffffff";
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
                    eiData = "<strong>" + feature.get("name") + "</strong><p>" +
                            "<strong>Address</strong>: " + feature.get("fullAddress") + "<br/>";                    
                    if(isSet(feature.get("phoneBusiness"))) { eiData += "<strong>Business</strong>: <a href='tel:" + feature.get("phoneBusiness") + "'>" + feature.get("phoneBusiness") + "</a><br/>"; }
                    if(isSet(feature.get("phoneHome"))) { eiData += "<strong>Home</strong>: <a href='tel:" + feature.get("phoneHome") + "'>" + feature.get("phoneHome") + "</a><br/>"; }
                    if(isSet(feature.get("phoneCell"))) { eiData += "<strong>Cell</strong>: <a href='tel:" + feature.get("phoneCell") + "'>" + feature.get("phoneCell") + "</a><br/>"; }
                    if(isSet(feature.get("phoneCell2"))) { eiData += "<strong>Cell 2</strong>: <a href='tel:" + feature.get("phoneCell2") + "'>" + feature.get("phoneCell2") + "</a><br/>"; }
                    eiData += "<p><em>As of " + feature.get("asOf") + "</em>";
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