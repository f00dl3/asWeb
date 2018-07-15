/* 
 by Anthony Stump
 Created: 15 Jul 2018
 */

function generateRadarKml(radarList, mobiLoc) {
    var inBoundsN = 0;
    var inBoundsS = 0;
    var inBoundsE = 0;
    var inBoundsW = 0;
    var opacity = 0;
    var oColor = 0;
    var inBounds = "";
    radarList.forEach(function (tRad) {
        var bounds = JSON.parse(tRad.BoundsNSEW);
        if(mobiLoc[0] < bounds[0] - 2) { inBoundsN = 1; } var nCheck = "[if " + mobiLoc[0] + " vs " + bounds[0] - 2 + "]";
        if(mobiLoc[0] > bounds[1] + 2) { inBoundsS = 1; } var sCheck = "[if " + mobiLoc[0] + " vs " + bounds[1] + 2 + "]";
        if(mobiLoc[1] < bounds[2] - 2) { inBoundsE = 1; } var eCheck = "[if " + mobiLoc[1] + " vs " + bounds[2] - 2 + "]";
        if(mobiLoc[1] > bounds[3] + 2) { inBoundsW = 1; } var wCheck = "[if " + mobiLoc[1] + " vs " + bounds[3] + 2 + "]";
        if(inBoundsN === 1 && inBoundsS === 1 && inBoundsE === 1 && inBoundsW === 1) {
            opacity = 1;
            oColor = 73;
            inBounds = "yes";
        } else {
            opacity = 0.8;
            oColor = 26;
            inBounds = "no";
        }
        var debugString = "(N: " + inBoundsN + " " + nCheck +"," +
                "S: " + inBoundsS + " " + sCheck +"," +
                "E: " + inBoundsE + " " + eCheck +"," +
                "W: " + inBoundsW + " " + wCheck +")";
        if(tRad.Site === "EAX") {
            console.log(tRad.Site + " @ " + tRad.BoundsNSEW);
            var imageSource = getBasePath("get") + "/Radar/" + tRad.Site + "/_BLoop.gif";
            console.log(imageSource);
            console.log(debugString);
            var extent = ol.extent.applyTransform(
                    [bounds[3], bounds[1], bounds[2], bounds[0]],
                    ol.proj.getTransform('EPSG:4326', 'EPSG:3857')
            );      
            var projection = new ol.proj.Projection({
                code: 'local_image',
                units: 'pixels',
                extent: extent
            });
            radarImage = new ol.layer.Image({
                opacity: 0.5,
                source: new ol.source.ImageStatic({
                    attributions: [ imageSource ],
                    url: imageSource,
                    imageSize: [ 600, 550 ],
                    projection: map.getView().getProjection(),
                    imageExtent: extent
                })
            });
            map.addLayer(radarImage);
        }
    });
}


