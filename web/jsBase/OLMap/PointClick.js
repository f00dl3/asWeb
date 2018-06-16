/* 
by Anthony Stump
Created: 16 Jun 2018
 */

function putPointClickMap() {
    var container = dojo.byId("popup");
    var content = dojo.byId("popup-content");
    var closer = dojo.byId("popup-closer");
    var overlay = new ol.Overlay({
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
        var coordinate = evt.coordinate;
        console.log("EVENT CLICK: " + coordinate);
        var hdms = ol.coordinate.toStringHDMS(ol.proj.transform(coordinate, 'ESPG:3857', 'ESPG:4326'));
        content.innerHTML = '<p>You clicked: </p><code>' + hdms + '</code>';
        overlay.setPosition(coordinate);
    });
}

