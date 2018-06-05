/* 
by Anthony Stump
Created: 5 Jun 2018
 */

function renderImage() {
    var imageFile = getBasePath("image") + "/ffxivLyse.png";
    var resolution = [ 1920, 1080 ];
    if(isSet(iRes)) { resolution = iRes.split("x"); } else { console.log("Defaulting [iRes]"); }
    if(isSet(dataInput)) { imageFile = dataInput; } else { console.log("Defaulting [imageFile]"); }
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
            url: imageFile,
            projection: projection,
            imageExtent: extent
        })
    });
    map.addLayer(imageLayer);
    dojo.byId("MAIN_FOOTER").innerHTML += " --> <strong>" + imageFile + " (" + iRes + ")</strong>";
}


