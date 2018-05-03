/* 
by Anthony Stump
Created: 23 Mar 2018
Updated: 3 May 2018
 */

function displayCf6() {
    generateCf6Layout();
    $("#WxLiveContainer").hide();
    $("#WxLocalModel").hide();
    $("#WxArchive").hide();
    $("#WxCf6").toggle();
}

function generateCf6Layout() {
    var rData = "<h3>CF6 KMCI/CPC Data</h3>" +
            "<div id='cf6SearchHolder'></div><br/>" +
            "<div id='cf6ResultHolder'></div>";
    dojo.byId("WxCf6").innerHTML = rData;
    var cf6SearchHolder = dojo.byId("cf6SearchHolder");
    getInitialCf6Data();
}

function getCf6Data(dateInStart, dateInEnd) {
    var dateStart, dateEnd;
    if(isSet(dateInStart)) { dateStart = dateInStart; } else { dateStart = getDate("day", -365, "dateOnly"); }
    if(isSet(dateInEnd)) { dateEnd = dateInEnd; } else { dateEnd = getDate("day", 0, "dateOnly"); }
}

function getInitialCf6Data(target) {
    getDivLoadingMessage("cf6SearchHolder");
    aniPreload("on");
    var thePostData = { "doWhat": "getCf6Initial" };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Wx"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    aniPreload("off");
                    popCf6Search(data.almanac[0]);
                    popStatTable(data.almanac[0]);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for CF6 Initial FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function hfWxIconStr(icref, desc) {
    return "<div class='UPop'><img class='th_icon' src='" + getBasePath("icon") + "/wx/" + icref + ".png'/><div class='UPopO'>" + desc + "</div></div>"
}

function popCf6Results(cf6Data, dateStart, dateEnd) {
    var rData = "<h3>CF6 Search Results</h3>";
    var cf6Graphs = "<p>" + dateStart + " to " + dateEnd + " graphed.<p>" +
            "<a href='" + doCh("p", "WxTemp", null) + "' target='pChart'><img class='th_sm_med' src='" + doCh("p", "WxTmp", "Thumb=1") + "'/></a>" +
            "<a href='" + doCh("p", "WxTempDFN", null) + "' target='pChart'><img class='th_sm_med' src='" + doCh("p", "WxTmpDFN", "Thumb=1") + "'/></a>";
    var cf6TH = [ "Date", "High", "Low", "DFN", "DDs", "Liq", "Snow", "SDepth", "WMax", "CC%", "Weather" ];
    var cf6Table = "<table><thead><tr>";
    for (var i = 0; i < cf6TH.length; i++) { cf6Table += "<th>" + cf6TH[i] + "</th>"; }
    cf6Table += "</tr></thead><tbody>";
    cf6Data.forEach(function (cf6) {
        var weather = cf6.Weather;
        var wxCo1 = [ "1", "2", "3", "4", "5", "6", "7", "8", "9","X" ];
        var wxCoA = [ "FG", "DF", "TH", "IP", "HA", "ZR", "DS", "HZ", "BS", "TO" ];
        var wxCoI = [
            hfWxIconStr("fg", "Fog"),
            hfWxIconStr("df", "Dense Fog"),
            hfWxIconStr("ts", "Thunderstorm"),
            hfWxIconStr("ip", "Ice Pellets"),
            hfWxIconStr("ha", "Hail"),
            hfWxIconStr("zr", "Freezing Rain"),
            hfWxIconStr("sm", "Smoke"),
            hfWxIconStr("du", "Haze"),
            hfWxIconStr("cd", "Blowing Snow"),
            hfWxIconStr("to", "Funnel Cloud")
        ];
        var newWxTmp = weather.replace(wxCo1, wxCoA);
        var newWx = newWxTmp.replace(wxCoA, wxCoI);
        var spread = (cf6.High - cf6.Low);
        cf6Table += "<tr>" +
                "<td class='C6DATE'><div class='UPop'>" + cf6.Date + "<div class='UPopO'>" +
                "<strong>CPC AO:</strong> " + cf6.AO + "<br/>" +
                "<strong>CPC AAO:</strong> " + cf6.AAO + "<br/>" +
                "<strong>CPC NAO:</strong> " + cf6.NAO + "<br/>" +
                "<strong>CPC PNA:</strong> " + cf6.PNA + "<br/>" +
                "<strong>Run/Walk:</strong> " + cf6.RunWalk + "<br/>" +
                "<strong>Cycling:</strong> " + cf6.Cycling +
                "</div></div></td>" +
                "<td><div style='" + styleTemp(cf6.High) + "'>" + cf6.High + "</div></td>" +
                "<td><div style='" + styleTemp(cf6.Low) + "'>" + cf6.Low + "</div></td>" +
                "<td><div class='UPop'><span class='" + colorTempDiff(cf6.DFNorm) + "'>" + cf6.DFNorm + "</span>" +
                "<div class='UPopO'>" +
                "<strong>Average:</strong><span style='" + styleTemp(cf6.Average) + "'>" + cf6.Average + "</span><br/>" +
                "<strong>Spread:</strong><span style='" + styleWind(spread) + "'>" + spread + "</span>" +
                "</div></div></td>";
        switch(true) {
            case (cf6.HDD !== 0): cf6Table += "<td style='" + styleWind(cf6.HDD) + "'><div class='UPop'>H" + cf6.HDD; break;
            case (cf6.CDD !== 0): cf6Table += "<td style='" + styleWind(cf6.CDD) + "'><div class='UPop'>C" + cf6.CDD; break;
            default: cf6Table += "<td><div class='UPop'>None"; break;
        }
        cf6Table += "<div class='UPopO'><strong>Electricity Use: </strong>" + cf6.kWh + "</div></div></td>" +
                "<td style='" + styleLiquid(cf6.Liquid) + "'>" + cf6.Liquid;
        if(isSet(cf6.HomePrecip)) {
            cf6Table += "<div class='UPOp'>" +
                    "<img class='th_icon' src='" + getBasePath("icon") + "/ic_hom.gif'/>" + 
                    "<div class='UPopO'>Home Liquid Precip: <span style='" + styleLiquid(cf6.HomePrecip) + "'>" + 
                    cf6.HomePrecip +
                    "</div></div>";
        }
        cf6Table += "</td>" +
                "<td><div class='" + colorSnow(cf6.Snow) + "'>" + cf6.Snow + "</div></td>" +
                "<td><div class='" + colorSnow(cf6.SDepth) + "'>" + cf6.SDepth + "</div></td>" +
                "<td style='" + styleWind(cf6.WMax) + "'><div class='UPop'>" + cf6.WMax +
                "<div class='UPopO'><strong>Average:</strong><span style='" + styleWind(cf6.WAvg) + "'>" + cf6.WAvg + "</span></div>" +
                "</div></td>" +
                "<td><div class='" + colorClouds(cf6.Clouds) + "'>" + cf6.Clouds + "</div></td>" +
                "<td class='C6WXTY'>" + newWx + "</td>" +
                "</tr>";
    });
    cf6Table += "</tbody></table>";
    rData += cf6Graphs + cf6Table;
    dojo.byId("cf6ResultHolder").innerHTML = rData;
}

function popCf6Search(amDat) {
    var rData = "<strong>" + amDat.FDay + "</strong> to <strong>" + amDat.LDay + "</strong>(" + amDat.Days + " days of data)" +
            "<br/>Default values search for the last year. Search & graph up to 25 years." +
            "<br/>Automatically updated 6 AM daily, with second attempt around Noon." +
            "<br/>CPC data updated on the 7th of each month.";
    var searchForm = "<form id='cf6SearchForm'>" +
            "<table><thead><th align='center' colspan=2>CF6 Search</th></thead><tbody>" +
            "<tr><td>Start</td><td><input type='date' name='CF6Search1' value=''/></td></tr>" +
            "<tr><td>End</td><td><input type='date' name='CF6Search2' value=''/></td></tr>" +
            "<input type='hidden' name='DoCf6Search' value='Yes'/>" +
            "<tr><td colspan=2 align='center'><button class='UButton' id='Cf6SearchButton' type='submit' name='DoCf6Search'>Search</button></td></tr>" +
            "</table></form>";            
    dojo.byId("cf6SearchHolder").innerHTML = rData;
}

function popLastYearGraphed() {
    var rData = "<p>Last year graphed:<p>" +
            "<a href='" + doCh("p", "WxTemp", null) + "' target='pChart'><img class='th_sm_med' src='" + doCh("p", "WxTemp", "Thumb=1") + "'/></a>" +
            "<a href='" + doCh("p", "WxTempDFN", null) + "' target='pChart'><img class='th_sm_med' src='" + doCh("p", "WxTempDFN", "Thumb=1") + "'/></a>" +
            "<a href='" + doCh("p", "WxCPC", null) + "' target='pChart'><img class='th_sm_med' src='" + doCh("p", "WxCPC", "Thumb=1") + "'/></a>";
    dojo.byId("cf6OverviewGraphs").innerHTML = rData;
}

function popStatTable(alm) {
    var wxArr = {
        "Fog/Mist":{"FG":"1"},
        "Dense Fog":{"FD":"2"},
        "Thunder":{"TH":"3"},
        "Ice Pellets":{"IP":"4"},
        "Hail":{"HL":"5"},
        "Freezing Rain":{"ZR":"6"},
        "Dust/Sand Storm":{"DS":"7"},
        "Haze/Smoke":{"HZ":"8"},
        "Blowing Snow":{"BS":"9"},
        "Tornado":{"FC":"X"}
    };
    var rData = "<br/><div id='cf6OverviewGraphs'></div>" +
            "<h4>Statistics</h4>";
    var rTable = "<table><tbody>" +
            "<tr><td>Average temperature: " + alm.TAvg_Avg + "F" +
            "<br/>Average high temperature: " + alm.High_Avg + "F" +
            "<br/>Average low temperature: " + alm.Low_Avg + "F" +
            "<br/>Overall temperature trend: " + alm.DFNorm_Avg + "F" +
            "<br/>" +
            "<br/>Maximum high temperature: " + alm.High_Max + "F" +
            "<br/>Minimum low temperature: " + alm.Low_Min + "F" +
            "<br/>Minimum high temperature: " + alm.High_Min + "F" +
            "<br/>Maximum low temperature: " + alm.Low_Max + "F" +
            "<br/>" +
            "<br/>" + alm.HotDays + " days with highs at or above 95F" +
            "<br/>" + alm.Freezing + " days with lows at or below freezing" +
            "<br/>" + alm.ColdNigts + " days with lows at or below 12F" +
            "<br/>" +
            "<br/>" + alm.HDD_Total + " total heating degree days" +
            "<br/>" + alm.CDD_Total + " total cooling degree days" +
            "</td><td>" +
            "Maximum 24 hour precipitation: " + alm.LP_Max + " in" +
            "<br/>Maximum 24 hour snowfall: " + alm.SP_Max + " in" +
            "<br/>" +
            "<br/>Total liquid precipitation: " + alm.LP_Total + " in" +
            "<br/>Total liquid precipitation home: " + alm.HomePrecip + " in" +
            "<br/>Total snowfall: " + alm.SP_Total + " in" +
            "<br/>Maximum snow depth: " + alm.SDepth_Max + " in" +
            "<br/>" +
            "<br/>Days with precipitation: " + alm.PDays +
            "<br/>Days with 0.1 in or more of precipitation: " + alm.PL01 +
            "<br/>Days with 1.0 in or more of precipitation: " + alm.PL10 +
            "<br/>Days with 2.5 in or more of precipitation: " + alm.PL25 +
            "<br/>" +
            "<br/>Days with snowfall: " + alm.PS_Any +
            "<br/>Days with 1 in or more of snowfall: " + alm.PS1 +
            "<br/>Days with 3 in or more of snowfall: " + alm.PS3 +
            "<br/>Days with 6 in or more of snowfall: " + alm.PS6 +
            "<br/>Days with 8 in or more of snowfall: " + alm.PS8 +
            "<br/>Days with 12 in or more of snowfall: " + alm.PS12 +
            "</td></tr><tr><td>" +
            // Loop through wx types on Java backend?
            "</td><td>" +
            "Average skycover: " + alm.CC_Avg + "%" +
            "<br/>" +
            "<br/>" + alm.CL + " clear days" +
            "<br/>" + alm.PC + " partly cludy days" +
            "<br/>" + alm.OC + " overcase days" +
            "<br/>" +
            "<br/>Average daily wind speed: " + alm.WAvg_Avg + " MPH" +
            "<br/>Lowest average wind speed: " + alm.WAvg_Min + " MPH" +
            "<br/>Highest average wind speed: " + alm.WAvg_Max + " MPH" +
            "<br/>" +
            "<br/>Average daily maximum gust speed: " + alm.WMax_Avg + " MPH" +
            "<br/>Lowest daily maximum gust speed: " + alm.WMax_Min + " MPH" +
            "<br/>Highest daily maximum gust speed: " + alm.WMax_Max + " MPH" +
            "</td></tr></tbody></table>";
    var dataDisclaimer = "<br/><em>Data from home only goes back to Jun 3rd, 2015.</em>" +
            "<br/><em>Complete data only goes back to May 6th, 2005.</em>" +
            "<br/>Data from Oct 1972 to Now is from Kansas City International Airport" +
            "<br/>Data from Jan 1934 to Oct 1972 is from Kansas City Downtown Airport" + 
            "<br/>Data from Jan 1900 to Jan 1934 is from Kansas City.";
    rData += rTable + dataDisclaimer;
    dojo.byId("cf6ResultHolder").innerHTML = rData;               
    popLastYearGraphed();
}

function popTempDistTable(trDat) {
    var rData = "<h4>Temperatures</h4>";
    var tempDistTable = "<table><thead><tr><th>What?</th>";
    for (var i = 110; i >= -20; i = i-10) { tempDistTable += "<th>" + i + "s</th>"; }
    //Dynamically generate the query from the Java code to do this.
    tempDistTable += "</tr></thead><tbody><tr><td>Highs</td>";
    for (var i = 110; i >= -20; i = i-10) { tempDistTable += "<td>" + i + "s</td>"; }
    tempDistTable += "</tr><tr><td>Lows</td>";
    for (var i = 110; i >= -20; i = i-10) { tempDistTable += "<td>" + i + "s</td>"; }
    tempDistTable += "</tr></tbody></table>";
    dojo.byId("tempDistHolder").innerHTML = rData;
}