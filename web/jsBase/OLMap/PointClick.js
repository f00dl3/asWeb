/* 
by Anthony Stump
Created: 16 Jun 2018
Updated: 17 Jun 2018
 */

function putPointClickMap() {
    overlay = new ol.Overlay({
        element: container,
        autoPan: true,
        autoPanAnimation: {
            duration: 250
        }
    });
    closer.onclick = function() {
        overlay.setPosition(undefined);
        closer.blur();
        return false;
    };
    var map = new ol.Map({
        layers: [ tileSource ],
        overlays: [ overlay ],
        target: 'map',
        view: homeView
    });
    map.on('singleclick', function(evt) {
        $("#popup").toggle();
        var coord = evt.coordinate;
        var xfCoord = ol.proj.transform(coord, 'EPSG:3857', 'EPSG:4326');
        var xfcm = xfCoord;
        var tca = xfcm.splice(",");
        var tLon = tca[0].toFixed(4);
        var tLat = tca[1].toFixed(4);
        content.innerHTML = '<p>You clicked: </p>Lat: ' + tLon + '<br>Lon: ' + tLat + '</code>';
        overlay.setPosition(coord);
    });
}

