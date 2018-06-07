/* 
by Anthony Stump
Created: 29 May 2018
Split off from OLMap.js on 30 May 2018
Updated: 7 Jun 2018
 */

function getGpsFromDatabase(map, date, type) {
    aniPreload("on");
    var thePostData = {
        "doWhat": "getGpsJson",
        "logDate": date,
        "activity": type
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Fitness"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    aniPreload("off");
                    dataToPass = JSON.parse(data[0].gpsLog);
                    switch(type) {
                        case "Run": addGpsToMap(map, dataToPass); break;
                        //case "C": addLineStringToMap(map, JSON.parse(data[0].CycGeoJSON), "Bike ride on " + date); break;
                        //case "A": addLineStringToMap(map, JSON.parse(data[0].AltGeoJSON), "Alt route on " + date); break;
                    }
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for GPS JSON Data FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function getRouteFromDatabase(map, date, type) {
    aniPreload("on");
    var thePostData = {
        "doWhat": "getOnlyFitnessGeoJSON",
        "XDT1": date,
        "XDT2": date
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Fitness"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    aniPreload("off");
                    switch(type) {
                        case "R": addLineStringToMap(map, JSON.parse(data[0].RunGeoJSON), "Run route on " + date); break;
                        case "C": addLineStringToMap(map, JSON.parse(data[0].CycGeoJSON), "Bike ride on " + date); break;
                        case "A": addLineStringToMap(map, JSON.parse(data[0].AltGeoJSON), "Alt route on " + date); break;
                    }
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for Route FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function postProcessOptions(map) {
    var dataInputOverride = [[-94.66923, 38.91598], [-94.66923, 38.91598], [-94.66803, 38.91628]];
    if(isSet(doAction) && isSet(dataInput)) {
        switch(doAction) {
            case "Route": addLineStringToMap(map, dataInputOverride); break;
            case "RouteGeoJSONAlt": getRouteFromDatabase(map, dataInput, "A"); break;
            case "RouteGeoJSONCyc": getRouteFromDatabase(map, dataInput, "C"); break;
            case "RouteGeoJSONRun": getRouteFromDatabase(map, dataInput, "R"); break;
            //https://localhost:8444/asWeb/OLMap.jsp?Action=RouteGPSTest&Input=Test
            case "RouteGPSTest": getGpsFromDatabase(map, "2018-06-06", "Run"); break;
        }
    }
}

function putSimpleMap() {
    var raster = localTiles;
    var map = new ol.Map({
        target: 'map',
        layers: [
            raster
        ],
        view: new ol.View({
            center: wmGeoJson,
            zoom: 15
        })
    });
    postProcessOptions(map);
}

