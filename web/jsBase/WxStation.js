/* 
by Anthony Stump
Created: 30 Mar 2018
Updated: 26 Aug 2018
 */
    
var jmwsData;
var jmwsStations;
var myLat;
var myLon;

function externalLink(station) {
    return "<a href='http://weather.gladstonefamily.net/site/" + station + "' target='top'>" + station + "</a>";
}

function getActiveStationData(logXmlObs, regions, autoStations, mobiLoc) {
    var locGeoJSON = JSON.parse(mobiLoc[0].Location);
    myLat = locGeoJSON[1];
    myLon = locGeoJSON[0];
    var lastDuration = Number(logXmlObs[0].Duration);
    var stationCount = jmwsStations.length;
    var unCount = autoStations.length;
    var rData = "<h4>Active Weather Stations</h4>" +
            "<strong>Last update took " + (lastDuration/60).toFixed(2) + " minutes." +
            " (<a href='" + doCh("j", "WxXml", null) + "' target='charts'>Trend</a>)<br/>" +
            "Location: [ " + myLon + " , " + myLat + " ] <button class='UButton' id='localStations'>Nearby</button><p>";
    var onKeySearch = "<div class='table'>" +
            "<form class='tr' id='WxStationSearchForm'>" +
            "<span class='td'><input type='text' id='SearchBox' name='StationSearchField' onkeyup='searchAheadStations(this.value)' /></span>" +
            "<span class='td'><strong>Search</strong></span>" +
            "</form></div>";
    var searchPopupHolder = "<div class='table' id='searchPopup'></div><p>";
    var hideOnSearch = "<div id='jmwsSearchResults'>" +
            stationCount + " stations are active. Please search for one!<br/>" +
            "To search by state, use S; or to search by region use R<p>" +
            "<strong>Region Maps</strong><br/>" +
            "<div class='table'><div class='tr'>" +
            "<span class='td'><a href='" + getBasePath("ui") + "/img/Regions.png'><img class='th_small' src='" + getBasePath("ui") + "/img/Regions.png'/></a></span>" + 
            "<span class='td'>";
    regions.forEach(function (region) {
        hideOnSearch += "[<a href='" + getBasePath("old") + "/OutMap.php?AllPoints=WxStation&WxReg=" + region.Code + "' target='wxinc'>" + region.Code + "</a>] ";
    });
    hideOnSearch += "</span></div></div><p>";
    var unStations = "<h3>Unconfigured stations: " + unCount + "</h3>";
    autoStations.forEach(function (auto) { unStations += externalLink(auto.Station) + ", "; });
    rData += onKeySearch + searchPopupHolder + hideOnSearch + unStations + "</div>";
    dojo.byId("stationDataHolder").innerHTML = rData;
    var localStationButton = dojo.byId("localStations");
    dojo.connect(localStationButton, "onclick", stationsNearMe);
}

function populateAfterSearch(overrideData) {
    var jsonGlob = jmwsData[0].jsonData;
    var tCols = [ "Station", "Location", "Temp" ];
    var rData = "<div class='table'><div class='tr'>";
    for(var i = 0; i < tCols.length; i++) { rData += "<span class='td'><strong>" + tCols[i] + "</strong></span>"; }
    rData += "</div>";
    var j = 0;
    overrideData.forEach(function (od) {
        if(jsonGlob[od.Station]) {
            if(j <= 100) {
                var tJson = jsonGlob[od.Station];
                var tTemp = tJson.Temperature;
                var tDPTemp = tJson.Dewpoint;
                var shortTime = wxShortTime(tJson.TimeString);
                var tsWeather;
                if(!isSet(tJson.Weather)) { tsWeather = "Missing"; } else { tsWeather = tJson.Weather; }
                if(od.Priority === 5) {
                    tTemp = conv2Tf(tTemp);
                    tDPTemp = conv2Tf(tDPTemp);
                }
                rData += "<div class='tr'>" +
                        "<span class='td'>" + od.Station + "</span>" +
                        "<span class='td'><div class='UPop'>" + od.City + ", " + od.Description +
                        "<div class='UPopO'>" + 
                        "<strong>Priority: </strong>" + od.Priority + "<br/>" +
                        "<strong>Point: </strong><a href='" + getResource("Map.Point") + "&Input=" + od.Point + "' target='pMap'>" + od.Point + "</a><br/>" +
                        "</div></div>" +
                        "</span>" +
                        "<span class='td' style='" + styleTemp(tTemp) + "'><div class='UPop'>" + Math.round(tTemp) + " / " + 
                        " <span style='" + styleTemp(tDPTemp) + "'>" + Math.round(tDPTemp) + "</span>" +
                        " <img class='th_icon' src='" + getBasePath("icon") + "/wx/" + wxObs("Icon", tJson.TimeString, null, null, null, tJson.Weather) + ".png' />" +
                        "<div class='UPopO'>" +
                        "<strong>" + shortTime + "</strong><br/>" +
                        "<img class='th_small' src='" + getBasePath("icon") + "/wx/" + wxObs("Icon", tJson.TimeString, null, null, null, tJson.Weather) + ".png' /><br/>" +
                        "<strong>Weather:</strong> " + tsWeather + "</br>" +
                        "<strong>Tempterature:</strong> <span style='" + styleTemp(tTemp) + "'>" + Math.round(tTemp) + "</span><br/>" +
                        "<strong>Dewpoint:</strong> <span style='" + styleTemp(tDPTemp) + "'>" + Math.round(tDPTemp) + "</span><br/>" +
                        "<strong>Wind:</strong> <span style='" + styleWind(tJson.WindSpeed) + "'>" + tJson.WindSpeed + " mph</span><br/>";
                if(isSet(tJson.WindGust)) {
                        "<strong>Gusting:</strong> <span style='" + styleWind(tJson.WindGust) + "'>" + tJson.WindGust + " mph</span><br/>";
                    
                }
                rData += "</div></div>" +
                        "</span>" +
                        "</div>";
            }
            j++;
        }
    });
    rData += "</div>";
    dojo.byId("jmwsSearchResults").innerHTML = rData;
    if(j > 100) { showNotice("Over 100 results found (" + j + ")!"); }
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
        populateAfterSearch(matchingRows);
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
            if((Math.abs(stationLat-myLat) <= 2.5) && (Math.abs(stationLon-myLon) <= 2.5)) { 
                matchingRows.push(sr); 
            }
        }
    });
    populateAfterSearch(matchingRows);
}

function init() {
    getJMWS(null, null);
    getJMWSChart();
}

dojo.ready(init);