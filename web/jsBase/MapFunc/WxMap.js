/* 
by Anthony Stump
Created: 9 Apr 2018
 */

function doWeatherMap(wxStations, obsIndoor, obsData, obsDataRapid) {
    var indoorTemp = Math.round(0.93 * conv2Tf((obsIndoor.TempCase)/1000));
    var obsPlotEmpty = "<img class='th_icon' src='" + getBasePath("icon") + "/wx/xx.png'/>";
    var jsonData = obsData.jsonSet; obsData = false;
    var jsonDataRapid = obsDataRapid.jsonSet; obsDataRapid = false;
    var jsonDataMerged;
    if(isSet(jsonData)) {
        jsonDataMerged = jsonData.concat(jsonDataRapid);
        jsonData = jsonDataRapid = false;
        wxStations.forEach(function (station) {
            //if(station.Priority !== 1) { continue; } --- for testing
            var stationId = wxStations.StationId;
            if(checkMobile()) {
                var skipper = Math.rand(0, 3);
                if(skipper !== 0 && wxStation.Priority < 2) {
                    return false;
                }
                // loop through if key matches, data = stationData
                var stationData = obsData[stationId];
                if(!isSet(stationData.Temperature) || stationData.Temperature < -100) { return false; }
                if(!isSet(stationData.Dewpoint)) {
                    if(!isSet(stationData.D0)) {
                        stationData.Dewpoint = conv2Tf(stationData.D0);
                    } else {
                        stationData.Dewpoint = stationData.Temperature;
                    }
                }
                if(isSet(wxStations.Point)) {
                    var content = "";
                    var thisObsWx = "Unknown Weather";
                    var theStation = stationId;
                    var bElevMb = wxStations.SfcMB;
                    if(wxStations.Priority < 4) {
                        var tarEleTemp = "#Sh"+theStation+"TableT";
                        var tarEleHumi = "#Sh"+theStation+"TableH";
                        var tarEleWind = "#Sh"+theStation+"TableW";
                        dojo.connect(tarEleTemp, "onclick", showTableTemp(stationId));
                        dojo.connect(tarEleHumi, "onclick", showTableHumi(stationId));
                        dojo.connect(tarEleWind, "onclick", showTableWind(stationId));
                        content += processUpperAirData(null, stationData) +
                                "<strong>" + theStation + "</strong><br/>";
                        // LEFT OFF HERE 4/9/18
                    }
                    if(wxStations.Priority === 5) {
                        stationData.Temperature = conv2Tf(stationData.Temperature);
                        stationData.Dewpoint = conv2Tf(stationData.Dewpoint);
                    }
                    if(isSet(stationData.Weather)) { thisObsWx = stationData.Weather; }
                    var shortTime = wxShortTime(stationData.TimeString);
                }
            }
        });
    } else {
        mapData = "<div class='Notice'>No data!</div>";
    }
    
    
}

function showTableHumi(stationId) {
    //xhrRequest to wxTableGen table
}

function showTableTemp(stationId) {
    //xhrRequest to wxTableGen table
}

function showTableWind(stationId) {
    //xhrRequest to wxTableGen table
}