/* 
by Anthony Stump
Created: 30 Mar 2018
Updated: 26 Aug 2018
 */
    
var jmwsData;
var jmwsStations;
var myLat;
var myLon;
var sdTimeout = getRefresh("medium");

function externalLink(station) {
    return "<a href='http://weather.gladstonefamily.net/site/" + station + "' target='top'>" + station + "</a>";
}

function getActiveStationData(logXmlObs, regions, autoStations, mobiLoc) {
    var locGeoJSON = JSON.parse(mobiLoc[0].Location);
    if(isSet(locGeoJSON[0]) && isSet(locGeoJSON[1])) {
        myLat = locGeoJSON[1];
        myLon = locGeoJSON[0];
    } else {
        myLat = getHomeGeo("lat");
        myLon = getHomeGeo("lon");
    }
    var lastDuration = Number(logXmlObs[0].Duration);
    var stationCount = jmwsStations.length;
    var unCount = autoStations.length;
    var unStations = "<div class='UPop'><button class='UButton'>Unconfigured: " + unCount + "</button><div class='UPopO'>";
    autoStations.forEach(function (auto) { unStations += externalLink(auto.Station) + ", "; });
    unStations += "</div></div>";
    var rData = "<h4>Active Weather Stations</h4>" +
            "<strong>Last update took " + (lastDuration/60).toFixed(2) + " minutes." +
            " (<a href='" + doCh("j", "WxXml", null) + "' target='charts'>Trend</a>)<br/>" +
            "Location: [ " + myLon + " , " + myLat + " ]<p>";
    var onKeySearch = "<div class='table'>" +
            "<form class='tr' id='WxStationSearchForm'>" +
            "<span class='td'><input type='text' id='SearchBox' name='StationSearchField' onkeyup='searchAheadStations(this.value)' /></span>" +
            "<span class='td'><strong>Search</strong></span>" +
            "</form></div>";
    var searchPopupHolder = "<div class='table' id='searchPopup'></div><p>";
    var hideOnSearch = "<div id='jmwsSearchResults'>" +
            stationCount + " stations are active. Please search for one!<br/>" +
            "To search by state, use S; or to search by region use R<p>" +
            "<div class='UPop'><button class='UButton'>Region Maps</button>" +
            "<div class='UPopO'><div class='table'><div class='tr'>" +
            "<span class='td'><a href='" + getBasePath("ui") + "/img/Regions.png'><img class='th_small' src='" + getBasePath("ui") + "/img/Regions.png'/></a></span>" + 
            "<span class='td'>";
    regions.forEach(function (region) {
        hideOnSearch += "[<a href='" + getBasePath("old") + "/OutMap.php?AllPoints=WxStation&WxReg=" + region.Code + "' target='wxinc'>" + region.Code + "</a>] ";
    });
    hideOnSearch += "</span></div></div></div></div>" + unStations + "<p>" +
            "<div id='StationsNearMe'>Loading...</div><p>";
    rData += onKeySearch + searchPopupHolder + hideOnSearch + "</div>";
    dojo.byId("stationDataHolder").innerHTML = rData;
    stationsNearMe();
}

function getJMWS(xdt1, xdt2) {
    if(isSet(xdt1)) { jmwsStart = xdt1; } else { jmwsStart = getDate("hour", -1, "full"); }
    if(isSet(xdt2)) { jmwsEnd = xdt2; } else { jmwsEnd = getDate("hour", 0, "full"); }
    aniPreload("on");
    var thePostData = {
        "doWhat": "getObsJMWS",
        "startTime": jmwsStart,
        "endTime": jmwsEnd,
        "limit": 1
    };
    require(["dojo/request"], function (request) {
        request
                .post(getResource("Wx"), {
                    data: thePostData,
                    handleAs: "json"
                }).then(
                function (data) {
                    aniPreload("off");
                    jmwsData = data.wxObs;
                    jmwsStations = data.stations;
                    getActiveStationData(
                        data.logs,
                        data.regions,
                        data.autoStations,
                        data.mobiLoc
                    );
                },
                function (error) {
                    aniPreload("off");
                    window.alert("request for JMWS Glob data FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
    setTimeout(function() { getJMWS(xdt1, xdt2); }, sdTimeout);
}

function getJMWSChart() {
    aniPreload("on");
    var thePostData = {
        "doWhat": "WxXml"
    };
    require(["dojo/request"], function (request) {
        request
                .post(getResource("Chart"), {
                    data: thePostData,
                    handleAs: "text"
                }).then(
                function (data) {
                    aniPreload("off");
                },
                function (error) {
                    aniPreload("off");
                    window.alert("request for JMWS Chart FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
    setTimeout(function() { getJMWSChart(); }, sdTimeout);
}

function searchAheadStations(value) {
    if(value.length > 2) {
        var matchingRows = [];
        jmwsStations.forEach(function (sr) {
            if(
                (isSet(sr.City) && (sr.City).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.Station) && (sr.Station).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.Description) && (sr.Description).toLowerCase().includes(value.toLowerCase())) ||
                (value.includes("S:") && isSet(sr.State) && (sr.State).toLowerCase().includes(value.replace("S:", "").toLowerCase())) ||
                (value.includes("R:") && isSet(sr.Region) && (sr.Region).toLowerCase().includes(value.replace("R:", "").toLowerCase()))
            ) { 
                matchingRows.push(sr);
            }
        });
        stationDataTable(matchingRows, "jmwsSearchResults");
        //getKeyedUpStationData(matchingRows);
    }
}

function stationsNearMe() {
    var matchingRows = [];
    jmwsStations.forEach(function (sr) {
        if(isSet(sr.Point)) {
            stationGeoJSON = JSON.parse(sr.Point);
            var stationLat = stationGeoJSON[1];
            var stationLon = stationGeoJSON[0];
            if((Math.abs(stationLat-myLat) <= 1.75) && (Math.abs(stationLon-myLon) <= 1.75)) { 
                matchingRows.push(sr); 
            }
        }
    });
    stationDataTable(matchingRows, "StationsNearMe");
}

function init() {
    getJMWS(null, null);
    getJMWSChart();
}

dojo.ready(init);