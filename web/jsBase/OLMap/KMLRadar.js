/* 
by Anthony Stump
Created: 15 Jul 2018
Updated: 14 Aug 2018
 */

var radarImage = [];

function doModelBasemap(map, lmmi, fHour, baseType, lastRunString) {
    // do not delete Images files for HRRR model output! That breaks this! Will have to modify Model Image Worker.
    var fixedLmiPath = lmmi.lastFile.replace("/var/www/G2Out", getBasePath("g2OutOld"));
    if(fHour !== "0000") {
        // Replace to make the image under Images folder. If 4x day run image, use that!
        fixedLmiPath = fixedLmiPath.replace("xsOut", "ImagesJ/Images");
    }
    console.log(fixedLmiPath); // for debug
    var extent = ol.extent.applyTransform(
            [-128, 24, -65, 50],
            ol.proj.getTransform('EPSG:4326', 'EPSG:3857')
    );      
    var projection = new ol.proj.Projection({
        code: 'local_image',
        units: 'pixels',
        extent: extent
    });
    imageLayer = new ol.layer.Image({
        opacity: 0.35,
        source: new ol.source.ImageStatic({
            attributions: [ fixedLmiPath ],
            url: fixedLmiPath,
            imageSize: [ lmmi.mediaInfo.imageWidth, lmmi.mediaInfo.imageHeight ],
            projection: map.getView().getProjection(),
            imageExtent: extent
        })
    });
    map.addLayer(imageLayer);
}

function generateRadarKml(radarList, mobiLocObj, timestamp) {
    if(radarImage) {
        for(var i = 0; i < radarImage.length; i++) {
            if(radarImage[i]) {
                map.removeLayer(radarImage[i]);
                console.log(timestamp + ": Removed radar image " + i + "!");
            } else {
                console.log(timestamp + ": No radar image layer " + i + " exists!");
            }
        }
    } else {
        console.log(timestamp + ": No radar image layer array exists!");
    }
    mobiLoc = JSON.parse(mobiLocObj[0].Location);
    var mobLon = Number(mobiLoc[0]);
    var mobLat = Number(mobiLoc[1]);
    var j = 0;
    radarList.forEach(function (tRad) {
        var inBoundsN = 0;
        var inBoundsS = 0;
        var inBoundsE = 0;
        var inBoundsW = 0;
        var opacity = 0.2;
        var inBounds = "";
        var bounds = JSON.parse(tRad.BoundsNSEW);
        bounds0 = Number(bounds[0]);
        bounds1 = Number(bounds[1]);
        bounds2 = Number(bounds[2]);
        bounds3 = Number(bounds[3]);
        if(mobLat < (bounds0)) { inBoundsN = 1; } var nCheck = "[if " + mobLat + " < " + (bounds0) + "]";
        if(mobLat > (bounds1)) { inBoundsS = 1; } var sCheck = "[if " + mobLat + " > " + (bounds1) + "]";
        if(mobLon < (bounds2)) { inBoundsE = 1; } var eCheck = "[if " + mobLon + " < " + (bounds2) + "]";
        if(mobLon > (bounds3)) { inBoundsW = 1; } var wCheck = "[if " + mobLon + " > " + (bounds3) + "]";
        if(inBoundsN === 1 && inBoundsS === 1 && inBoundsE === 1 && inBoundsW === 1) {
            inBounds = "yes";
            console.log("IN BOUNDS FOR [" + tRad.Site + "]!");
        } else {
            inBounds = "no";
        }
        var debugString = "(N: " + inBoundsN + " " + nCheck +"," +
                "S: " + inBoundsS + " " + sCheck +"," +
                "E: " + inBoundsE + " " + eCheck +"," +
                "W: " + inBoundsW + " " + wCheck +")";
        //if(!checkMobile() || opacity === 0.2) {
        /* Can get away with this now - OL performance much better than Leaflet! */ if(1 === 1) {
            var imageSource = getBasePath("get") + "/Radar/" + tRad.Site + "/_BLatest.gif";
            var extent = ol.extent.applyTransform(
                    [bounds[3], bounds[1], bounds[2], bounds[0]],
                    ol.proj.getTransform('EPSG:4326', 'EPSG:3857')
            );      
            var projection = new ol.proj.Projection({
                code: 'local_image',
                units: 'pixels',
                extent: extent
            });
            radarImage[j] = new ol.layer.Image({
                opacity: opacity,
                source: new ol.source.ImageStatic({
                    attributions: [ imageSource ],
                    url: imageSource,
                    imageSize: [ 600, 550 ],
                    projection: map.getView().getProjection(),
                    imageExtent: extent
                })
            });
            map.addLayer(radarImage[j]);
            j++;
        }
    });
}
