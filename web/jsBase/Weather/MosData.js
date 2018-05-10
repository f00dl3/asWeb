/* 
by Anthony Stump
Created: 27 Mar 2018
Updated: 9 May 2018
 */

function displayModelData() {
    $("#WxLiveContainer").hide();
    $("#WxLocalModel").toggle();
    $("#WxArchive").hide();
    $("#WxCf6").hide();
    getModelData();
}

function getModelData() {
    var thePostData = {
        "doWhat": "getMosData"
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Wx"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    processMosData(
                        data.last[0], // returns undefined!
                        data.heights,
                        data.hours,
                        data.runs,
                        data.jsonModelData
                    );
                    aniPreload("off");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for getMosData FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function processMosData(last, heights, hours, runs, jsonModelData) {
    var searchString = last.RunString;
    var runString, dataCMC, dataGFS, dataHRRR, dataHRWA, dataHRWN, dataSRFA, dataSRFN, dataNAM, dataRAP;
    var mosCols = [ "Forecast", "TA", "TD", "RH", "WND", "P3", "PT", "X3", "PW", "LI", "CAPE", "CIN" ];
    var rData = "<h3>GRIB2 JSON Model Output Data</h3>" +
            "<em>For KOJC / Olathe Johnson County<br/>" +
            "Auto-updated hourly</em><p>" +
            "<a href='" + getBasePath("g2OutOld") + "' target='new'>Automatic model image output</a>";
    var models = [ "CMC", "GFS", "HRRR", "NAM", "RAP", "HRWA", "HRWN", "SRFA", "SRFN" ];
    var heights = []; heights.forEach(function (hgt) { heights.push(hgt); mosCols.push(hgt) });
    var gfsFh = []; hours.forEach(function (tfh) { gfsFh.push(tfh); });
    var reportingModels = "";
    var estZRTot, estSnowTot, precipTot, mosi;
    estZRTot = estSnowTot = precipTot = mosi = 0;
    var mrForm = "<form id='ModelRunForm'><select id='RunSearch' name='RunSearch'><option value=''>Select...</option>";
    runs.forEach(function (run) { mrForm += "<option value='" + run.RunString + "'>" + run.RunString + "</option>"; });
    mrForm += "</select></form>";
    var mrDateForm = "<form id='DateSelectForm'><input type='date' id='SelectModelRunDate' value='' /></form>";
    rData += "<div class='table'><div class='tr'>" +
            "<span class='td'>Select run:<br/>" + mrForm + "</span>" +
            "<span class='td'>" + mrDateForm + "<br/>" +
            "<button id='SubmitDateRun' class='UButton'><img class='th_icon' src='" + getBasePath("icon") + "/transparent-arrow-left-hi.png' /> By date:</button></span>" +
            "</div></div><p>";
    var mroTable = "<div class='table'>" +
            "<span class='td'>" +
            "<a href='" + getBasePath("g2OutOld") + "/MergedJ/Loops/FOCUS_Loop.mp4' target='new'>" +
            "<video id='FocusLoop' class='th_sm_med' autoplay controls loop>" +
            "<source src='" + getBasePath("g2OutOld") + "/MergedJ/Loops/FOCUS_Loop.mp4?ts=" + getDate("day", 0, "full") + "'></source>" +
            "</video></a><br/>Focus</span>" +
            "<span class='td'>" +
            "<a href='" + doCh("p", "WxMOSTemp", "RunString=" + searchString) + "' target='pChart'>" +
            "<img class='th_sm_med' src='" + doCh("p", "WxMOSTemp", "RunString=" + searchString + "&Thumb=1") + "'/>" +
            "</a><br/>Temperature</span>" +
            "<span class='td'>" +
            "<a href='" + doCh("p", "WxMOSWind", "RunString=" + searchString) + "' target='pChart'>" +
            "<img class='th_sm_med' src='" + doCh("p", "WxMOSWind", "RunString=" + searchString + "&Thumb=1") + "'/>" +
            "</a><br/>Wind Speed</span>" +
            "</div>";
    rData += mroTable;
    jsonModelData.forEach(function (jmd) {
        runString = jmd.RunString;
        dataCMC = jmd.CMC;
        dataGFS = jmd.GFS;
        dataHRRR = jmd.HRRR;
        dataHRWA = jmd.HRWA;
        dataHRWN = jmd.HRWN;
        dataSRFA = jmd.SRFA;
        dataSRFN = jmd.SRFN;
        dataNAM = jmd.NAM;
        dataRAP = jmd.RAP;
    });
    if(isSet(dataCMC) && isSet(dataCMC.T0_3)) { reportingModels += " CMC"; }
    if(isSet(dataGFS) && isSet(dataGFS.T0_3)) { reportingModels += " GMC"; }
    if(isSet(dataHRRR) && isSet(dataHRRR.T0_3)) { reportingModels += " HRRR"; }
    if(isSet(dataHRWA) && isSet(dataHRWA.T0_3)) { reportingModels += " HRWA"; }
    if(isSet(dataHRWN) && isSet(dataHRWN.T0_3)) { reportingModels += " HRWN"; }
    if(isSet(dataSRFA) && isSet(dataSRFA.T0_3)) { reportingModels += " SRFA"; }
    if(isSet(dataSRFN) && isSet(dataSRFN.T0_3)) { reportingModels += " SRFN"; }
    if(isSet(dataNAM)) { reportingModels += " NAM"; }
    if(isSet(dataRAP)) { reportingModels += " RAP"; }
    var runDateTime = runString; // Create date time from format and put how I want it here!
    var urlPrefix = runDateTime; // clone it
    urlPrefix = urlPrefix; // format to Ymd_H for image files!
    var mosTable = "<div class='table'><div class='tr'>";
    for(var i = 0; i < mosCols.length; i++) { mosTable += "<span class='th'>" + mosCols[i] + "</span>"; }
    mosTable += "</div>";
    gfsFh.forEach(function (tfh) {
        if(isSet(tfh)) {
            var tValidTime = runDateTime; // clone it again
            tValidTime = tValidTime; // modify it + x hours
            var tValidTimeString = tValidTime; // convert to string, again, image format Ymd_H for files
        }
        if(
                isSet(dataCMC) && isSet(dataCMC["T0_" + tfh]) &&
                isSet(dataGFS) && isSet(dataGFS["T0_" + tfh]) &&
                isSet(dataHRRR) && isSet(dataHRRR["T0_" + tfh]) &&
                isSet(dataHRWA) && isSet(dataHRWA["T0_" + tfh]) &&
                isSet(dataHRWN) && isSet(dataHRWN["T0_" + tfh]) &&
                isSet(dataSRFA) && isSet(dataSRFA["T0_" + tfh]) &&
                isSet(dataSRFN) && isSet(dataSRFN["T0_" + tfh]) &&
                isSet(dataNAM) && isSet(dataNAM["T0_" + tfh]) &&
                isSet(dataRAP) && isSet(dataRAP["T0_" + tfh])
        ) {
            var tAutoCounter = 0;
            var tAuto0TF = 0;
            if(isSet(dataCMC["T0_" + tfh]) && dataCMC["T0_" + tfh] > -50) {
                var tModelAu = conv2Tf(dataCMC["T0_" + tfh]); tAuto0TF += tModelAu; tAutoCounter++;
            }
            tAuto0TF = Math.round(tAuto0TF/tAutoCounter);
            tAutoCounter = 0;
            var tAuto900TF = 0;
        }
    }); 
    dojo.byId("WxLocalModel").innerHTML = rData;
}



