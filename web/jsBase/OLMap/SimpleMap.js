/* 
by Anthony Stump
Created: 29 May 2018
Split off from OLMap.js on 30 May 2018
Updated: 16 Mar 2019
 */

function postProcessOptions(map) {
    var dataInputOverride = [[-94.66923, 38.91598], [-94.66923, 38.91598], [-94.66803, 38.91628]];
    var defaultMetric = "Speed";
    if(isSet(doAction)) {
        switch(doAction) {
            case "Addresses": getAddresses(map); break;
            case "CrashData": getCrashData(map); break;
            case "Homicide": getHomicides(map); break;
            case "Media": getMediaGeoData(map); break;
            case "Point": addPointToMap(map, dataInput); break;
            case "PhoneTrack": getPhoneTrackFromDatabase(map, dataInput); break;
            case "Route": addLineStringToMap(map, dataInputOverride); break;
            case "RouteGeoJSONAlt": getRouteFromDatabase(map, dataInput, "A"); break;
            case "RouteGeoJSONCyc": getRouteFromDatabase(map, dataInput, "C"); break;
            case "RouteGeoJSONRun": getRouteFromDatabase(map, dataInput, "R"); break;
            case "RouteGPSAlt": activity = "Run"; getGpsFromDatabase(map, dataInput, activity, defaultMetric); break;
            case "RouteGPSCyc": activity = "Cyc"; getGpsFromDatabase(map, dataInput, activity, defaultMetric); break;
            case "RouteGPSCyc2": activity = "Cy2"; getGpsFromDatabase(map, dataInput, activity, defaultMetric); break;
            case "RouteGPSCyc3": activity = "Cy3"; getGpsFromDatabase(map, dataInput, activity, defaultMetric); break;
            case "RouteGPSCyc4": activity = "Cy4"; getGpsFromDatabase(map, dataInput, activity, defaultMetric); break;
            case "RouteGPSRun": activity = "Run"; getGpsFromDatabase(map, dataInput, activity, defaultMetric); break;
            case "RouteGPSRun2": activity = "Ru2"; getGpsFromDatabase(map, dataInput, activity, defaultMetric); break;
            case "RouteGPSRun3": activity = "Ru3"; getGpsFromDatabase(map, dataInput, activity, defaultMetric); break;
            case "RouteGPSRun4": activity = "Ru4"; getGpsFromDatabase(map, dataInput, activity, defaultMetric); break;
            case "RouteHistory": getRouteHistoryFromDatabase(map); break;
            case "RoutePlan": getRoutePlanFromDatabase(map, dataInput); break;
            case "RoutePlansAll": getRoutePlansAllFromDatabase(map); break;
            case "Wx": map.getView().setZoom(map.getView().getZoom() - 6); initWxMap(map); break;
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

