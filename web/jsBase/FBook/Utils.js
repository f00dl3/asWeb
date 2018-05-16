/* 
by Anthony Stump
Created: 4 Apr 2018
Updated: 16 May 2018
 */

function displayUtils() {
    getUtils();
    $("#FBUUse").toggle();
    $("#FBAuto").hide();
    $("#FBBills").hide();
    $("#FBBlue").hide();
    $("#FBCheck").hide();
    $("#FBWorkPTO").hide();
    $("#FBAsset").hide();
}

function getUtils() {
    var thePostData = {
        "doWhat": "Utilities"
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Chart"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    aniPreload("off");
                    getUtilsData();
                },
                function(error) { 
                    aniPreload("off");
                    console.log("request for Util Charts FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });    
}

function getUtilsData() {
    aniPreload("on");
    var uuMonthLast = getDate("day", 0, "yyyy-MM");
    var thePostData = "doWhat=getUtils&tMonth="+uuMonthLast;
    var xhArgs = {
        preventCache: true,
        url: getResource("Finance"),
        postData: thePostData,
        handleAs: "json",
        timeout: timeOutMilli,
        load: function (data) {
            putUtils(
                data.uuRel,
                data.settingC,
                data.settingH,
                data.uuData
            );
            aniPreload("off");
        },
        error: function (data, iostatus) {
            aniPreload("off");
            window.alert("xhrGet for Overview FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
        }
    };
    dojo.xhrPost(xhArgs);
}

function putUtils(uuRelations, settingC, settingH, uuData) {
    var rData = "<h3>Utility Use</h3>";
    rData += "<a href='" + doCh("j", "UseElecD", null) + "' target='newCh'><img class='ch_small' src='" + doCh("j", "UseElecD", "th") + "'/></a>" +
            "<a href='" + doCh("j", "UseGas", null) + "' target='newCh'><img class='ch_small' src='" + doCh("j", "UseGas", "th") + "'/></a>";
    var rTable = "<table><thead><tr><th>Month<br/>(A. Tmp.)</th>" +
            "<th><div class='UPop'>Elec<br/>kWh<div class='UPopO'>";
    var coolTable = "<table><thead><tr>" +
            "<th>Cooling<br/>Schedule</th>" +
            "<th>Cooling<br/>Setting</th>" +
            "</tr></thead><tbody>";
    settingC.forEach(function (setC) {
        coolTable += "<tr>" +
                "<td>" + setC.Day + " " + setC.Time + "</td>" +
                "<td>" + setC.Temp + "</td>" +
                "</tr>";
    });
    coolTable += "</tbody></table>";
    rTable += coolTable + "</div></div></th>" +
            "<th><div class='UPop'>Gas<br/>MCF<div class='UPopO'>";
    var heatTable = "<table><thead><tr>" +
            "<th>Heating<br/>Schedule</th>" +
            "<th>Heating<br/>Setting</th>" +
            "</tr></thead><tbody>";
    settingH.forEach(function (setH) {
        heatTable += "<tr>" +
                "<td>" + setH.Day + " " + setH.Time + "</td>" +
                "<td>" + setH.Temp + "</td>" +
                "</tr>";
    });
    heatTable += "</tbody></table>";
    rTable += heatTable + "</div></div></th>" +
            "<th><span class='UChart'>Cell<br/>MIN<div class='UChartO'><a href='" + doCh("p", "CellMin", null) + "'><img class='ch_large' src='" + doCh("p", "CellMin", "Thumb=1") + "' /></a></div></span></th>" +
            "<th><span class='UChart'>Cell<br/>Txt<div class='UChartO'><a href='" + doCh("p", "CellTxt", null) + "'><img class='ch_large' src='" + doCh("p", "CellTxt", "Thumb=1") + "' /></a></div></span></th>" +
            "<th><span class='UChart'>Cell<br/>MMS<div class='UChartO'><a href='" + doCh("p", "CellMMS", null) + "'><img class='ch_large' src='" + doCh("p", "CellMMS", "Thumb=1") + "' /></a></div></span></th>" +
            "<th><span class='UChart'>Cell<br/>Data<div class='UChartO'><a href='" + doCh("p", "CellData", null) + "'><img class='ch_large' src='" + doCh("p", "CellData", "Thumb=1") + "' /></a></div></span></th>" +
            "<th><span class='UChart'>Web<br/>Data<div class='UChartO'><a href='" + doCh("p", "WebData", null) + "'><img class='ch_large' src='" + doCh("p", "WebData", "Thumb=1") + "' /></a></div></span></th>" +
            "</tr></thead><tbody>";
    Object.keys(uuData).sort().reverse().forEach(function (mo) {
        var dayEleCost = (uuData[mo].kWh_Avg * elecCost);
        rTable += "<tr>" +
                "<td><div class='UPop'>" + mo + "<div class='UPopO'>Avg. temperature: " + (uuData[mo].wxAvgTF).toFixed(1) + " F</div></div></td>" +
                "<td><div class='UPop'>" + uuData[mo].kWh_Avg + "<div class='UPopO'>" +
                "<strong>Est. cost per-day:</strong> $" + dayEleCost.toFixed(2) + "<br/>" +
                "<strong>Est. 30 day bill:</strong> $" + (dayEleCost*30).toFixed(2) + "</div></div></td>" +
                "<td>" + uuData[mo].TotalMCF + "</td>" +
                "<td>" + uuData[mo].Minutes + "</td>" +
                "<td>" + uuData[mo].Texts + "</td>" + 
                "<td>" + uuData[mo].MMS + "</td>" + 
                "<td>" + autoUnits(uuData[mo].CData*1000000) + "</td>" +
                "<td>" + autoUnits(uuData[mo].WData*1000000) + "</td>" +
                "</tr>";
    });
    rTable += "</tbody></table>";
    rData += rTable;
    dojo.byId("FBUUse").innerHTML = rData;
}