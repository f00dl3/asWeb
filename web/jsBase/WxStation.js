/* 
by Anthony Stump
Created: 30 Mar 2018
Updated: 26 Aug 2018
 */
    
var jmwsData;
var jmwsStations;

function externalLink(station) {
    return "<a href='http://weather.gladstonefamily.net/site/" + station + "' target='top'>" + station + "</a>";
}

function getActiveStationData(logXmlObs, regions, autoStations) {
    var lastDuration = Number(logXmlObs[0].Duration);
    var stationCount = jmwsStations.length;
    var unCount = autoStations.length;
    var rData = "<h4>Active Weather Stations</h4>" +
            "<strong>Last update took " + (lastDuration/60).toFixed(2) + " minutes." +
            " (<a href='" + doCh("j", "WxXml", null) + "' target='charts'>Trend</a>)<p>";
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
}

function populateAfterSearch(overrideData) {
    var jsonGlob = jmwsData.jsonData;
    console.log(jsonGlob);
    var tCols = [ "Station", "Location" ];
    var rData = "<div class='table'><div class='tr'>";
    for(var i = 0; i < tCols.length; i++) { rData += "<span class='td'><strong>" + tCols[i] + "</strong></span>"; }
    rData += "</div>";
    var j = 0;
    overrideData.forEach(function (od) {
        if(j <= 100) {
            rData += "<div class='tr'>" +
                    "<span class='td'>" + od.Station + "</span>" +
                    "<span class='td'>" + od.City + ", " + od.Description + "</span>" +
                    "</div>";
        }
        j++;
    });
    rData += "</div>";
    dojo.byId("jmwsSearchResults").innerHTML = rData;
    if(j > 100) { showNotice("Over 100 results found (" + j + ")!"); }
}


function getKeyedUpStationData(wxStations) {
    // add keyUpMatcherFunction
    var wxTableGen = ""; //SET ME UP!
    var terms = basicInputFilter(input);
    var tsWeather, tsTemperature, tsDewpoint, tsGradient, doTable;
    tsGradient = "";
    var doWindBarb = "Missing";
    var items, itemsTable;
    items = itemsTable = [];
    // map the data. Line 53 in WxStations.php stationData as key-val
    wxStations.forEach(function (station) {
        if(!isSet(stationData.Weather)) { tsWeather = "Missing"; } else { tsWeather = stationData.Weather; }
        if(isSet(stationData.Temperature)) {
            tsTemperature = (stationData.Temperature).toFixed(1);
            if(isSet(stationData.Dewpoint)) {
                tsDewpoint = (stationData.Dewpoint).toFixed(1);
                var c2gJson = [ stationData.Temperature, stationData.Dewpoint ];
                tsGradient = color2Grad("T", "right", c2gJson);
            } else {
                tsDewpoint = "MM";
            }
        } else {
            tsTemperature = "MM";
        }
        if(isSet(stationData.WindDegrees) && isSet(stationData.WindSpeed)) {
            doWindBarb = windDirSvg(stationData.WindDegrees) + " at " + stationData.WindSpeed;
        }
        if(wxStations.Priority < 4) {
            // pass params Station and tElevation if needed
            doTable = "<a href='" + wxTableGen + "'>T</>";
        }
        var coordArrayTemp = (wxStations.point).substring(1,((wxStations.point).length)-1);
        var coordArray = coordArrayTemp.split(",");
        var pushableItems = (wxStations.City).toUppercase() + " S:" + wxStations.State + " " +
                wxStations.Station + ", R:" + wxStations.Region + " " + wxStations.Description;
        items.push(pushableItems);
        var pushableItemsTable = "<div class='tr'>" +
                "<span class='td'>" + wxStations.Station + "</span>" +
                "<span class='td'><div class='UPop'>" + wxStations.City + ", " + wxStations.State +
                "<div class='UPopO'>" +
                "<strong>Coords</strong>: " + coordArray[1] + ", " + coordArray[0] + "<br/>" +
                "<strong>Priority</strong>: " + wxStations.Priority + "<br/>" +
                "<strong>Region</strong>: " + wxStations.Region + "<br/>" + 
                "<strong>Elevation Mb</strong>: " + wxStations.SfcMB +
                "</div></div></span>" +
                "<span class='td' style='" + tsGradient + "'><div class='UPopO'>" + tsTemperature +
                "<div class='UPopO'>" +
                "<strong>Updated</strong>: " + stationData.TimeString + "<br/>" +
                "<strong>Dewpoint</strong>: " + tsDewpoint + "<br/>" +
                "<strong>Weather</strong>: " + tsWeather + "<br/>" +
                "<strong>Winds</strong>: " + doWindBarb +
                "</div></div></span>" +
                "<span class='td'>" +
                "<a href='" + getBasePath("old") + "/OutMap.php?Title=" + wxStations.Station + "&Point=" + wxStations.Point + "' target='wxinc'>M</a>" +
                "<a href='" + doCh("p", "WxXML", "TLev=SFC&Station=" + wxStations.Station) + "' target='wxinc'>G</a>" + doTable + "</span>" +
                "</div>";
        itemsTable.push(pushableItemsTable);
    });
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
                        data.autoStations
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
    console.log(value);
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

function init() {
    getJMWS(null, null);
    getJMWSChart();
}

dojo.ready(init);