/* 
by Anthony Stump
Created: 4 Apr 2018
Updated: 22 Apr 2018
 */

function displayAuto() {
    getAuto();
    $("#FBAuto").toggle();
    $("#FBAsset").hide();
    $("#FBBills").hide();
    $("#FBBlue").hide();
    $("#FBCheck").hide();
    $("#FBWorkPTO").hide();
    $("#FBUUse").hide();
}

function getAuto() {
    aniPreload("on");
    var thePostData = "doWhat=getAuto";
    var xhArgs = {
        preventCache: true,
        url: getResource("Finance"),
        postData: thePostData,
        handleAs: "json",
        timeout: timeOutMilli,
        load: function (data) {
            putAuto(
                data.autoMpg,
                data.billSum[0],
                data.amrData
            );
            aniPreload("off");
        },
        error: function (data, iostatus) {
            aniPreload("off");
            window.alert("xhrGet for Bills FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
        }
    };
    dojo.xhrPost(xhArgs);
}

function putAuto(autoMpgData, billSum, amrData) {
    var autoMpgCols = [ "Date", "Total Miles", "Cost/Gallon", "Gallons" ];
    var rData = "<h3>Auto Maintenance</h3>";
    var fuelLog = "<h4>Fuel Log/MPG</h4>" +
            "<div class='table'><div class='tr'>";
    for (var i = 0; i < autoMpgCols.length; i++) { fuelLog += "<span class='td'><strong>" + autoMpgCols[i] + "</strong></span>"; }
    fuelLog += "</div>";
    autoMpgData.forEach(function (mpg) { 
        fuelLog += "<div class='tr'>" +
                "<span class='td'>" + mpg.Date + "</span>" +
                "<span class='td'>" + mpg.TotMiles + "</span>" +
                "<span class='td'>" + mpg.CostPG + "</span>" +
                "<span class='td'>" + mpg.Gallons + "</span>" +
                "</div>";
    });
    fuelLog += "</div>";
    rData += fuelLog + "<p>";
    var maintCols = [ "Mileage", "Date", "Flags", "Services", "Bill", "Location" ];
    var maintRecs = "<h4>Maintenance Records</h4>" +
            "<div class='UBox'>Upkeep Cost<br/>'08 Mazda 6<br/>$" + (billSum.BillSum).toFixed(2) + "</div><p>" +
            "<div class='table'><div class='tr'>";
    for (var i = 0; i < maintCols.length; i++) { maintRecs += "<span class='td'><strong>" + maintCols[i] + "</strong></span>"; }
    maintRecs += "</div>";
    amrData.forEach(function (amr) {
        var icons = "";
        if(amr.OilCh === 1) { icons += "<img class='th_icon' src='" + getBasePath("icon") + "/ic_oil.jpeg'/>"; }
        if(amr.TireRotation === 1) { icons += "<img class='th_icon' src='" + getBasePath("icon") + "/ic_tir.jpeg'/>"; }
        maintRecs += "<div class='tr'>" +
                "<span class='td'>" + amr.Miles + "</span>" +
                "<span class='td'>" + amr.Date + "</span>" +
                "<span class='td'>" + icons + "</span>" +
                "<span class='td'>" + amr.Services + "</span>" +
                "<span class='td'>$" + (amr.Bill).toFixed(2) + "</span>" +
                "<span class='td'>" + amr.Location + "</span>" +
                "</div>";
    });
    maintRecs += "</div>";
    rData += maintRecs;
    dojo.byId("FBAuto").innerHTML = rData;
}

