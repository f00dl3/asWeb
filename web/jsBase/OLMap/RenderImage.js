/* 
by Anthony Stump
Created: 5 Jun 2018
Updated: 6 Jun 2018
 */

function renderImage(arguments) {
    var imageFile = getBasePath("image") + "/ffxivLyse.png";
    var resolution = [ 1920, 1080 ];
    if(isSet(iRes)) { resolution = iRes.split("x"); } else { console.log("Defaulting [iRes]"); }
    if(isSet(dataInput)) { imageFile = dataInput; } else { console.log("Defaulting [imageFile]"); }
    if(isSet(arguments)) {
        switch(arguments) {
            case "G16IR": imageFile = getBasePath("getOldGet") + "/G16/codCentPlainsWV/Latest/Loop.gif"; iRes = [ 1600, 900 ]; break;
            case "G16VIS": imageFile = getBasePath("getOldGet") + "/G16/codCentPlainsVI/Latest/Loop.gif"; iRes = [ 1600, 900 ]; break;
        }
    }
    var extent = [ 0, 0, resolution[0], resolution[1] ];
    var projection = new ol.proj.Projection({
        code: 'local_image',
        units: 'pixels',
        extent: extent
    });
    map = new ol.Map({
        target: 'map',
        view: new ol.View({
            projection: projection,
            center: ol.extent.getCenter(extent),
            resolution: 1
        })
    });
    var imageLayer = new ol.layer.Image({
        source: new ol.source.ImageStatic({
            attributions: [
                imageFile + '<br/>',
                'Resolution: ' + iRes
            ],
            url: imageFile,
            projection: projection,
            imageExtent: extent
        })
    });
    map.addLayer(imageLayer);
    map.getView().fit(extent, map.getSize());
}


