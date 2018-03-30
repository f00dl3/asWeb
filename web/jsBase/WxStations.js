/* 
by Anthony Stump
Created: 30 Mar 2018
 */
    
function externalLink(station) {
    return "<a href='http://weather.gladstonefamily.net/site/" + station + "' target='top'>" + station + "</a>";
}

function getActiveStationData(logXmlObs, regions, autoStations) {
    var stationCount = 0;
    var rData = "<h4>Active Weather Stations</h4>" +
            "<strong>Last update took " + (logXmlObs.Duration/60).toFixed(2) + " minutes." +
            " (<a href='" + doCh("p", "WxLog", null) + "' target='charts'>Trend</a>)<p>";
    var onKeySearch = "<div class='table'>" +
            "<form class='tr' id='WxStationSearchForm'>" +
            "<span class='td'><input type='text' id='SearchBox' name='StationSearchField' onkeyup='showHint(this.value)' /></span>" +
            "<span class='td'><strong>Search</strong></span>" +
            "</form></div>";
    var searchPopupHolder = "<div class='table' id='searchPopup'></div><p>";
    var hideOnSearch = "<div class='HideOnSearch'>" +
            stationCount + " stations are active. Please search for one!<br/>" +
            "To search by state, use S; or to search by region use R<p>" +
            "<strong>Region Maps</strong><br/>" +
            "<div class='table'><div class='tr'>" +
            "<span class='td'><a href='" + getBasePath("ui") + "/img/Regions.png'><img clsas='th_small' src='" + getBasePath("ui") + "/img/Regions.png'/></a></span>" + 
            "<span class='td'>";
    regions.forEach(function (region) {
        hideOnSearch += "[<a href='" + getBasePath("old") + "/OutMap.php?AllPoints=WxStation&WxReg=" + region.Code + "' target='wxinc'>" + region.Code + "</a>] ";
    });
    hideOnSearch += "</span></div></div></div><p>";
    var unCount = autoStations.length;
    var unStations = "<h3>Unconfigured stations: " + unCount + "</h3>";
    autoStations.forEach(function (auto) { unStations += externalLink(auto.Station) + ", "; });
    rData += onKeySearch + searchPopupHolder + hideOnSearch + unStations;
    return rData;
}

function getKeyedUpStationData(inputKeyUp, wxStations, stationData) {
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

function init() {
    
}

dojo.ready(init);