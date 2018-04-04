/* 
by Anthony Stump
Created: 4 Apr 2018
 */

function displayUtils() {
    getUtils();
    $("#FBUUse").toggle();
}

function getUtils() {
    var uuMonthLast = getDate("day", 0, "yearMonth");
    putUtils();
}

function putUtils(uuRelations, settingC, settingH, uuData) {
    var rData = "<h3>Utility Use</h3>";
    uuRelations.forEach(function (rel) {
        // Migrate to jFreeChart
        rData += "<a href='" + rel.URL + "'><img class='ch_small' src='" + rel.URL + "&Thumb=1'/></a>";
    });
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
    uuData.forEach(function (uu) {
        var dayEleCost = (uu.kwhAvg * elecCost);
        rTable += "<tr>" +
                "<td><div class='UPop'>" + uu.Month + "<div class='UPopO'>Average temperature: " + uu.ATF + " F</div></div></td>" +
                "<td><div class='UPop'>" + uu.kwhAvg + "<div class='UPopO'>" +
                "<strong>Est. cost per-day:</strong> $" + dayEleCost.toFixed(2) + "<br/>" +
                "<strong>Est. 30 day bill:</strong> $" + (dayEleCost*30).toFixed(2) + "</div></div></td>" +
                "<td>" + uu.TotalMcf + "</td>" +
                "<td>" + uu.CMin + "</td>" +
                "<td>" + uu.CTxt + "</td>" + 
                "<td>" + autoUnits(uu.CData*1000000) + "</td>" +
                "<td>" + autoUnits(uu.WData*1000000) + "</td>" +
                "</tr>";
    });
    rTable += "</tbody></table>";
    rData += rTable;
    dojo.byId("FBUUse").innerHTML = rData;
}