/* 
by Anthony Stump
Created: 5 Jun 2018
Updated: 26 Jun 2018
 */

function renderImage(arguments) {
    var animation = false;
    var imageFile = getBasePath("image") + "/ffxivLyse.png";
    var resolution = [ 1920, 1080 ];
    if(isSet(iRes)) { resolution = iRes.split("x"); } else { console.log("Defaulting [iRes]"); }
    if(isSet(dataInput)) { imageFile = dataInput; } else { console.log("Defaulting [imageFile]"); }
    if(isSet(arguments)) {
        switch(arguments) {
            case "G16IR": 
                imageFile = getBasePath("getOldGet") + "/G16/codCentPlainsWV/Latest/Loop.gif";
                animation = true;
                iRes = [ 1600, 900 ];
                break;
            case "G16VIS": 
                imageFile = getBasePath("getOldGet") + "/G16/codCentPlainsVI/Latest/Loop.gif";
                animation = true;
                iRes = [ 1600, 900 ];
                break;
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
    if(animation) {
        // Does not work - 6/25/18
        dojo.byId("animationElement").innerHTML = "<img id='innerAnimation' src='" + imageFile + "'/>";
        $("#innerAnimation").show();
        //var posA = Math.round(-Math.abs(resolution[0]/2));
        //var posB = Math.round(Math.abs(resolution[1]/2));
        var posA = 0, posB = 0;
        var pos = ol.proj.fromLonLat([ posA, posB ]);
        var marker = new ol.Overlay({
            position: pos,
            positioning: 'center-center',
            element: dojo.byId("innerAnimation"),
            stopEvent: false
        });
        map.addOverlay(marker);
    } else {
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
    }
    map.getView().fit(extent, map.getSize());
}


