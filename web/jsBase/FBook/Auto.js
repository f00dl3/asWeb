/* 
by Anthony Stump
Created: 4 Apr 2018
Updated: 16 Jul 2020
 */

function actOnMpgEntry(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    var thisFormDataJ = dojo.formToJson(this.form);
    if(
        isSet(thisFormData.mpgDate) &&
        isSet(thisFormData.mpgMiles) &&
        isSet(thisFormData.mpgPrice) &&
        isSet(thisFormData.mpgGallons)
    ) {
        setAddAutoMpg(thisFormData);
    } else {
        window.alert("You missed something!\n" + thisFormDataJ)
    }
}

function displayAuto() {
    getAuto();
    $("#FBAuto").toggle();
    $("#FBAutoHC").hide();
    $("#FBAsset").hide();
    $("#FBBills").hide();
    $("#FBBlue").hide();
    $("#FBCheck").hide();
    $("#FBWorkPTO").hide();
    $("#FBUUse").hide();
    $("#FBStocks").hide();
}

function getAuto() {
    aniPreload("on");
    var thePostData = "doWhat=getAuto";
    var xhArgs = {
        preventCache: true,
        url: getResource("Automotive"),
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
            window.alert("xhrGet for AutoMPG FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
        }
    };
    dojo.xhrPost(xhArgs);
}

function putAuto(autoMpgData, billSum, amrData) {
    var autoMpgCols = [ "Date", "Total Miles", "Cost/Gallon", "Gallons", "MPG" ];
    var rData = "<h3>Auto Maintenance: '08 Mazda 6</h3>";
    var fuelLog = "<h4>Fuel Log/MPG</h4>" +
            "<div id='mpgEntryForm'></div><br/>" +
            "<div class='table'><div class='tr'>";
    for (var i = 0; i < autoMpgCols.length; i++) { fuelLog += "<span class='td'><strong>" + autoMpgCols[i] + "</strong></span>"; }
    fuelLog += "</div>";
    var prevMiles = 0;
    autoMpgData.forEach(function (mpg) { 
        var thisMiles = mpg.TotMiles;
        var thisMpg = "N/A";
        if(prevMiles !== "N/A") {
            // Provides incorrect data due to data being sorted ascending.
            var milesSinceLastFillUp = mpg.TotMiles - prevMiles;
            console.log(prevMiles + " - " + mpg.TotMiles + " = " + milesSinceLastFillUp);
            thisMpg = (Math.abs(milesSinceLastFillUp)/mpg.Gallons).toFixed(1);
        }
        fuelLog += "<div class='tr'>" +
                "<span class='td'>" + mpg.Date + "</span>" +
                "<span class='td'>" + mpg.TotMiles + "</span>" +
                "<span class='td'>" + mpg.CostPG + "</span>" +
                "<span class='td'>" + mpg.Gallons + "</span>" +
                "<span class='td'>" + thisMpg + "</span>" +
                "</div>";
        prevMiles = mpg.TotMiles;
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
    popAutoMpgEntryForm();
}

function popAutoMpgEntryForm() {
    var rData = "<div class='table'><form class='tr'>" +
            "<span class='td'><input name='mpgDate' type='date' id='mpgDate' value='' style='width: 100px;' /><br/>Date</span>" +
            "<span class='td'><input name='mpgMiles' id='mpgMiles' type='number' step='1' value='' style='width: 75px;' /><br/>Miles</span>" +
            "<span class='td'><input name='mpgPrice' id='mpgPrice' type='number' step='0.001' value='' style='width: 50px;' /><br/>Price</span>" +
            "<span class='td'><input name='mpgGallons' id='mpgGallons' type='number' step='0.001' value='' style='width: 50px;' /><br/>Gallons</span>" +
            "<span class='td'><button class='UButton' name='mpgSubmit' id='mpgSubmit'>Fuel!</button></span>" +
            "</form></div>";
    dojo.byId("mpgEntryForm").innerHTML = rData;
    var enterMpgButton = dojo.byId("mpgSubmit");
    dojo.connect(enterMpgButton, "click", actOnMpgEntry);
}

function setAddAutoMpg(formData) {
    var thePostData = {
        "doWhat": "putAutoMpgAdd",
        "mpgDate": formData.mpgDate,
        "mpgMiles": formData.mpgMiles,
        "mpgPrice": formData.mpgPrice,
        "mpgGallons": formData.mpgGallons
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Automotive"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    aniPreload("off");
                    getAuto();
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for AutoMPG Entry Add FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}