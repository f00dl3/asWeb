/* 
by Anthony Stump
Created: 29 May 2018
Split off from OLMap.js on 30 May 2018
Updated: 28 Jun 2018
 */

function postProcessOptions(map) {
    var dataInputOverride = [[-94.66923, 38.91598], [-94.66923, 38.91598], [-94.66803, 38.91628]];
    var defaultMetric = "Speed";
    if(isSet(doAction)) {
        switch(doAction) {
            case "Route": addLineStringToMap(map, dataInputOverride); break;
            case "RouteGeoJSONAlt": getRouteFromDatabase(map, dataInput, "A"); break;
            case "RouteGeoJSONCyc": getRouteFromDatabase(map, dataInput, "C"); break;
            case "RouteGeoJSONRun": getRouteFromDatabase(map, dataInput, "R"); break;
            case "RouteGPSAlt": activity = "Run"; getGpsFromDatabase(map, dataInput, activity, defaultMetric); break;
            case "RouteGPSCyc": activity = "Cyc"; getGpsFromDatabase(map, dataInput, activity, defaultMetric); break;
            case "RouteGPSRun": activity = "Run"; getGpsFromDatabase(map, dataInput, activity, defaultMetric); break;
            case "RouteHistory": getRouteHistoryFromDatabase(map); break;
            case "RoutePlan": getRoutePlanFromDatabase(map, dataInput); break;
        }
    }
}

function putSimpleMap(doOverride) {
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
    var raster = tileSource;
    dojo.byId("map").innerHTML = "";
    map = new ol.Map({
        target: 'map',
        layers: [ raster ],
        overlays: [ overlay ],
        view: homeView
    });
    console.log("OL Map Initiated!");
    if(!isSet(doOverride)) { postProcessOptions(map); }
}

