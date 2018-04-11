/* 
by Anthony Stump
Created: 9 Apr 2018
Updated: 10 Apr 2018
 */

function doWeatherMap(wxStations, obsIndoor, obsData, obsDataRapid) {
    var rData = "";
    var indoorTemp = Math.round(0.93 * conv2Tf((obsIndoor.TempCase)/1000));
    var jsonData = obsData.jsonSet; obsData = false;
    var jsonDataRapid = obsDataRapid.jsonSet; obsDataRapid = false;
    var jsonDataMerged;
    if(isSet(jsonData)) {
        jsonDataMerged = jsonData.concat(jsonDataRapid);
        jsonData = jsonDataRapid = false;
        wxStations.forEach(function (station) {
            //if(station.Priority !== 1) { continue; } --- for testing
            var stationId = wxStations.StationId;
            var thisObsWx = "Unknown Weather";
            if(checkMobile()) {
                var skipper = Math.rand(0, 3);
                if(skipper !== 0 && station.Priority < 2) {
                    return false;
                }
            }
            // loop through if key matches, data = stationData
            var stationData = obsData[stationId];
            var wxIcon = getBasePath("icon") + "/wx/" + wxObs("Icon", stationData.TimeString, null, null, null, thisObsWx) + ".png";
            if(!isSet(stationData.Temperature) || stationData.Temperature < -100) { return false; }
            if(!isSet(stationData.Dewpoint)) {
                if(!isSet(stationData.D0)) {
                    stationData.Dewpoint = conv2Tf(stationData.D0);
                } else {
                    stationData.Dewpoint = stationData.Temperature;
                }
            }
            if(wxStations.Priority === 5) {
                stationData.Temperature = conv2Tf(stationData.Temperature);
                stationData.Dewpoint = conv2Tf(stationData.Dewpoint);
            }
            if(isSet(stationData.Weather)) { thisObsWx = stationData.Weather; }
            if(isSet(wxStations.Point)) {
                var shortTime = wxShortTime(stationData.TimeString);
                var content = "";
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
                    if(isSet(wxStations.Description)) { content += wxStations.Description; } else { content += wxStations.State; }
                    content += "<br/>" + shortTime + "<br/>" +
                            "<table>" +
                            "<tr><td><a href='" + doCh("p", "WxXML", "TLev=SFC&Station="+theStation) + "' target='new'>" +
                            "<img class='th_small' src='" + wxIcon + "' />" +
                            "</a></td>" +
                            "<td>" + thisObsWx + "<br/>";
                    if(isSet(stationData.Temperature)) { content += "<strong>Temp</strong>: <span style='" + styleTemp(stationData.Temperature) + "'>" + (stationData.Temperature).toFixed(1) + "F</span><br/>"; }
                    if(isSet(stationData.Dewpoint)) { content += "<strong>Dwpt</strong>: <span style='" + styleTemp(stationData.Dewpoint) + "'>" + (stationData.Dewpoint).toFixed(1) + "F</span><br/>"; }
                    if(isSet(stationData.WindSpeed)) { content += "<strong>Wind</strong>: <span style='" + styleWind(stationData.WindSpeed) + "'>" + (stationData.WindSpeed).toFixed(1) + " MPH</span><br/>"; }
                    if(isSet(stationData.WindGust)) { content += "<strong>Gusts</strong>: <span style='" + styleWind(stationData.WindGust) + "'>" + (stationData.WindGust).toFixed(1) + " MPH</span><br/>"; }
                    if(isSet(stationData.Pressure)) {
                        content += "<strong>MSLP</strong>: " + (stationData.Pressure).toFixed(1) + " mb</span><br/>";
                    } else {
                        if(isSet(stationData.PressureIn)) {
                            content += "<strong>Altim HG</strong>: " + (stationData.PressureIn).toFixed(2) + " in</span><br/>";
                        }
                    }
                    if(wxStations.Priority === 6 || wxStations.Priority === 7) {
                        if(isSet(stationData.WaterColumnHeight)) { content += "<strong>Column</strong>: " + stationData.WaterColumnHeight + " m</span><br/>"; }
                        if(isSet(stationData.WaterTemp)) { content += "<strong>Water</strong>: <span style='" + styleTemp(stationData.WaterTemp) + "'>" + (stationData.WaterTemp).toFixed(1) + "F</span><br/>"; }
                        if(isSet(stationData.WaveHeight)) { content += "<strong>Waves</strong>: " + (stationData.WaveHeight * 3.28084).toFixed(1) + " ft<br/>"; }
                        if(isSet(stationData.WavePeriodDominant)) {
                            content += "<strong>Period</strong>: " + Math.round(stationData.WavePeriodDominant) +
                                    " (" + (stationData.WavePeriodAverage).toFixed(1) + "s)<br/>";
                        }
                        if(isSet(stationData.WaveDirection)) { content += "<strong>Wave Dir.</strong>: " + stationData.WaveDirection + "<br/>"; }
                        content += "</table>";
                        if(wxStations.Priority < 4) {
                            content += "<button id='Sh" + theStation + "TableT' class='UButton'>TMP</button>" +
                                    "<button id='Sh" + theStation + "TableH' class='UButton'>HUM</button>" +
                                    "<button id='Sh" + theStation + "TableW' class='UButton'>WND</button>";
                            // do sounding min trim() on doSoundingMin.replace("/\s\s+/", "");
                        }
                        var station = theStation;
                        rData += content + "</script>";
                    }
                    var obsSpan = smallDivs(wxDataType /* GET FROM POST? */, station, stationData) + "</script>";
                    if(isSet(loopNo)) {
                        $(function() {
                            var els = $("span[id^="+theStation+"_]").hide().toArray();
                            if(!els.length) return;
                            $(els[0]).show();
                            setInterval(function() {
                                $(els[0]).hide();
                                els.push(els.shift());
                                $(els[0]).fadeIn();
                            }, 5);
                        });
                    }
                    var obsIcon = L.divIcon({
                        iconSize: new L.point(16, 16),
                        html: obsSpan
                    });
                }
            }
        });
        generateIcons();
    } else {
        rData = "<div class='Notice'>No data!</div>";
    }
    return rData;
}

function generateIcons() {
    var homeIcon = L.icon({
        iconUrl: getBasePath("icon") + "/gKML/icon48.png",
        iconSize: [20, 20],
        iconAnchor: [10, 10]
    });
    var mobiIcon = L.icon({
        iconUrl: getBasePath("icon") + "/gKML/icon46.png",
        iconSize: [20, 20],
        iconAnchor: [10, 10]
    });
    var homePopup = "Home<br/><a href='" + getBasePath("ui") + "/Cams.jsp' target='new'>" +
            "<video class='th_medium' autoplay loop>" +
            "<source src='" + getBasePath("getOldGet") + "/Cams/_Loop.mp4?ts=" + getDate("minute", 0, "timestamp") + "></source>" +
            "</video></a><br/>" +
            "<strong>Indoors</strong>: <span style='" + styleTemp(indoorTemp) + "'>" + indoorTemp + "F</span>";
    L.marker(getHomeGeo("geoJSONr"), {icon: homeIcon}).addTo(map).bindPopup(homePopup);
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

function smallDivs(dataType, wxStations, stationData) {
    // if posted wxDataHour set session gfHour = wxDataHour else unset gFHour
    // if not posted wxDataType then session gImgType = tmp2m
    var sfcT, sfcD, obsPlotData, obsSpan;
    sfcT = sfcD = obsPlotData = obsSpan = "";
    var theStation = stationData.Station;
    var obsPlotEmpty = "<img class='th_icon' src='" + getBasePath("icon") + "/wx/xx.png'/>";
    var defBlock100 = "display: inline-block; width: 100%; ";
    if(!isSet(stationData.Temperature)) {
        obsPlotData = obsPlotEmpty;
    } else {
        sfcT = Math.round(stationData.Temperature);
        sfcD = Math.round(stationData.Dewpoint);
        obsPlotData = sfcT;
    }
    var obsPlotStyle = "display: inline-block; width: 100%; ";
    if(isSet(stationData.Temperature)) {
        var passArray = { "v1": sfcT, "v2": sfcD };
        obsPlotStyle += color2Grad("T", "right", passArray);
        obsSpan = "<span class='StationSpan' id='" + theStation + "' style='" + obsPlotStyle + "'>" + obsPlotData + "</span>";
    } else {
        switch(dataType) {
            case "CAPE":
                //set session gImgType = "cape";
                if(wxStations.Priority > 3) { obsPlotData = obsPlotEmpty; }
                else { obsPlotData = stationData.CAPE + "<br/><span style='" + styleLi(stationData.CIN) + "'>" + stationData.CIN + "</span>"; }
                obsPlotStyle = styleCape(stationData.CAPE);
                obsSpan = "<span class='StationSpan' id='" + theStation + "' style='" + obsPlotStyle + "'>" + obsPlotData + "</span>";
                break;
            case "JSWM":
                //set session gImgType = "wm0500";
                var uljMax = 0;
                if(wxStations.Priority > 3) { obsPlotData = obsPlotEmpty; }
                else {
                    var uljPoints = [ stationData.WS150, stationData.WS200, stationData.WS250, stationData.WS300 ];
                    uljMax = Math.max(uljPoints);
                    obsPlotData = windDirSvg(stationData.WD200);
                }
                obsPlotStyle = defBlock100 + styleWind(conv2Mph(uljMax));
                obsSpan = "<span class='StationSpan' id='" + theStation + "' style='" + obsPlotStyle + "'>" + obsPlotData + "</span>";
                break;
            case "LI":
                //set session gImgType = "lftx";
                if(wxStations.Priority > 3) { obsPlotData = obsPlotEmpty; }
                else { obsPlotData = (stationData.LI).toFixed(1); }
                obsPlotStyle = styleLi(stationData.LI);
                obsSpan = "<span class='StationSpan' id='" + theStation + "' style='" + obsPlotStyle + "'>" + obsPlotData + "</span>";
                break;
            case "LLJM":
                //set session gImgType = "wm0850";
                var lljMax = 0;
                if(wxStations.Priority > 3) { obsPlotData = obsPlotEmpty; }
                else {
                    var lljPoints = [ stationData.WS900, stationData.WS850, stationData.WS800 ];
                    lljMax = Math.max(lljPoints);
                    obsPlotData = windDirSvg(stationData.WD0800);
                }
                obsPlotStyle = defBlock100 + styleWind(conv2Mph(lljMax));
                obsSpan = "<span class='StationSpan' id='" + theStation + "' style='" + obsPlotStyle + "'>" + obsPlotData + "</span>";
                break;
            case "PWAT":
                //set session gImgType = "pwat";
                if(wxStations.Priority > 3) { obsPlotData = obsPlotEmpty; }
                else { obsPlotData = (stationData.PWAT).toFixed(1); }
                obsPlotStyle = defBlock100 + styleLiquid(stationData.PWAT);
                obsSpan = "<span class='StationSpan' id='" + theStation + "' style='" + obsPlotStyle + "'>" + obsPlotData + "</span>";
                break;
            case "SfcH":
                //set session gImgType = "rh2m";
                obsPlotData = relativeHumidity(stationData.Temperature, stationData.Dewpoint);
                obsPlotStyle = defBlock100 + styleRh(obsPlotData);
                obsSpan = "<span class='StationSpan' id='" + theStation + "' style='" + obsPlotStyle + "'>" + obsPlotData + "</span>";
                break;
            case "SfcE":
                obsPlotData = wxStations.SfcMB;
                obsPlotStyle = "width: 100%; background-color: " + autoColorScale(wxStations.SfcMB, 1015, 800, null);
                obsSpan = "<span class='StationSpan' id='" + theStation + "' style='" + obsPlotStyle + "'>" + obsPlotData + "</span>";
                break;
            case "SfcF":
                //set session gImgType = "tmp2m";
                obsPlotData = wxObs("Feel", stationData.TimeString, stationData.Temperature, stationData.WindSpeed, stationData.RelativeHumidity, stationData.Weather);
                obsPlotStyle = defBlock100 + styleTemp(obsPlotData);
                obsSpan = "<span class='StationSpan' id='" + theStation + "' style='" + obsPlotStyle + "'>" + obsPlotData + "</span>";
                break;
            case "SfcP":
                obsPlotData = stationData.Pressure;
                obsPlotStyle = "width: 100%; background-color: " + autoColorScale(stationData.Pressure, 1040, 970, null);
                obsSpan = "<span class='StationSpan' id='" + theStation + "' style='" + obsPlotStyle + "'>" + obsPlotData + "</span>";
                break;
            case "SfcW":
                //set session gImgType = "wm10m";
                obsPlotData = windDirSvg(stationData.WindDegrees);
                obsPlotStyle = defBlock100 + styleWind(stationData.WindSpeed);
                obsSpan = "<span class='StationSpan' id='" + theStation + "' style='" + obsPlotStyle + "'>" + obsPlotData + "</span>";
                break;
            case "WatP":
                if(wxStations.Priority < 4) { obsPlotData = obsPlotEmpty; }
                if(!isSet(stationData.WavePeriodDominant)) { obsPlotData = obsPlotEmpty; } else { obsPlotData = stationData.WavePeriodDominant; }
                obsPlotStyle = defBlock100;
                obsSpan = "<span class='StationSpan' id='" + theStation + "' style='" + obsPlotStyle + "'>" + obsPlotData + "</span>";
                break;
            case "WatT":
                //set session gImgType = "tmp2m";
                if(wxStations.Priority < 4) { obsPlotData = obsPlotEmpty; }
                if(!isSet(stationData.WaterTemp)) {
                    obsPlotStyle = defBlock100;
                    obsPlotData = obsPlotEmpty; 
                } else {
                    obsPlotData = (stationData.WaterTemp).toFixed(1);
                    obsPlotStyle = defBlock100 + styleTemp(stationData.WaterTemp);
                }
                obsSpan = "<span class='StationSpan' id='" + theStation + "' style='" + obsPlotStyle + "'>" + obsPlotData + "</span>";
                break;
            case "WatW":
                if(wxStations.Priority < 4) { obsPlotData = obsPlotEmpty; }
                obsPlotStyle = defBlock100;
                if(!isSet(stationData.WaveHeight)) {
                    obsPlotData = obsPlotEmpty; 
                } else {
                    obsPlotData = (stationData.WaveHeight * 3.28084).toFixed(1);
                    obsPlotStyle += styleCape((stationData.WaveHeight*3.28084*100));
                }
                obsSpan = "<span class='StationSpan' id='" + theStation + "' style='" + obsPlotStyle + "'>" + obsPlotData + "</span>";
                break;
            case "WxOb":
                //set session gImgType = "radar";
                obsPlotData = "<img class='th_icon' src='" + wxIcon + "'/>";
                obsPlotStyle = defBlock100;
                obsSpan = "<span class='StationSpan' id='" + theStation + "' style='" + obsPlotStyle + "'>" + obsPlotData + "</span>";
                break;
        }
    }
    return obsSpan;
}