/* 
by Anthony Stump
Created: 27 Mar 2018
Updated: 22 May 2018
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

function processMosData(last, heightsIn, hours, runs, jsonModelData) {
    var tAutoCounter = 0;
    var searchString = last.RunString;
    var runString, dataCMC, dataGFS, dataHRRR, dataHRWA, dataHRWN, dataSRFA, dataSRFN, dataNAM, dataRAP;
    var precipTot = 0.00,
        estimatedFreezingRainTotal = 0.0,
        estimatedSnowTotal = 0.0;
    var mosCols = [ "Forecast", "TA", "TD", "RH", "WND", "P3", "PT", "X3", "PW", "LI", "CAPE", "CIN" ];
    var rData = "<h3>GRIB2 JSON Model Output Data</h3>" +
            "<em>For KOJC / Olathe Johnson County<br/>" +
            "Auto-updated hourly</em><p>" +
            "<a href='" + getBasePath("g2OutOld") + "' target='new'>Automatic model image output</a>";
    var models = [ "CMC", "GFS", "HRRR", "NAM", "RAP", "HRWA", "HRWN", "SRFA", "SRFN" ];
    var heights = [];
    heightsIn.forEach(function (hgt) {
        if(hgt.HeightMb !== 0) {
            heights.push(hgt.HeightMb);
            mosCols.push(hgt.HeightMb);
        }
    });
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
        if(isSet(jmd.CMC)) { dataCMC = JSON.parse(jmd.CMC) }
        if(isSet(jmd.GFS)) { dataGFS = JSON.parse(jmd.GFS); }
        if(isSet(jmd.HRRR)) { dataHRRR = JSON.parse(jmd.HRRR); }
        if(isSet(jmd.HRWA)) { dataHRWA = JSON.parse(jmd.HRWA); }
        if(isSet(jmd.HRWN)) { dataHRWN = JSON.parse(jmd.HRWN); }
        if(isSet(jmd.SRFA)) { dataSRFA = JSON.parse(jmd.SRFA); }
        if(isSet(jmd.SRFN)) { dataSRFN = JSON.parse(jmd.SRFN); }
        if(isSet(jmd.NAM)) { dataNAM = JSON.parse(jmd.NAM); }
        if(isSet(jmd.RAP)) { dataRAP = JSON.parse(jmd.RAP); }
    });
    rData += "</div>";
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
    for(var i = 0; i < mosCols.length; i++) { mosTable += "<span class='td'><strong>" + mosCols[i] + "</strong></span>"; }
    mosTable += "</div>";
    gfsFh.forEach(function (tfhA) {
        var tfh = tfhA.FHour;
        
        if(isSet(tfh)) {
            var tValidTime = runDateTime; // clone it again
            //var dtTValidTime = getDate();
            tValidTime = tValidTime; // modify it + x hours
            var tValidTimeString = tValidTime; // convert to string, again, image format Ymd_H for files
        }
        
        tAutoCounter = 0;
        var tAuto0TF = 0;
        if(isSet(dataCMC) && isSet(dataCMC["T0_" + tfh]) && dataCMC["T0_" + tfh] > -50) { var tModelAu = conv2Tf(dataCMC["T0_" + tfh]); tAuto0TF += tModelAu; tAutoCounter++; }
        if(isSet(dataGFS) && isSet(dataGFS["T0_" + tfh]) && dataGFS["T0_" + tfh] > -50) { var tModelAu = conv2Tf(dataGFS["T0_" + tfh]); tAuto0TF += tModelAu; tAutoCounter++; }
        if(isSet(dataHRRR) && isSet(dataHRRR["T0_" + tfh]) && dataHRRR["T0_" + tfh] > -50) { var tModelAu = conv2Tf(dataHRRR["T0_" + tfh]); tAuto0TF += tModelAu; tAutoCounter++; }
        if(isSet(dataHRWA) && isSet(dataHRWA["T0_" + tfh]) && dataHRWA["T0_" + tfh] > -50) { var tModelAu = conv2Tf(dataHRWA["T0_" + tfh]); tAuto0TF += tModelAu; tAutoCounter++; }
        if(isSet(dataHRWN) && isSet(dataHRWN["T0_" + tfh]) && dataHRWN["T0_" + tfh] > -50) { var tModelAu = conv2Tf(dataHRWN["T0_" + tfh]); tAuto0TF += tModelAu; tAutoCounter++; }
        if(isSet(dataSRFA) && isSet(dataSRFA["T0_" + tfh]) && dataSRFA["T0_" + tfh] > -50) { var tModelAu = conv2Tf(dataSRFA["T0_" + tfh]); tAuto0TF += tModelAu; tAutoCounter++; }
        if(isSet(dataSRFN) && isSet(dataSRFN["T0_" + tfh]) && dataSRFN["T0_" + tfh] > -50) { var tModelAu = conv2Tf(dataSRFN["T0_" + tfh]); tAuto0TF += tModelAu; tAutoCounter++; }
        if(isSet(dataNAM) && isSet(dataNAM["T0_" + tfh]) && dataNAM["T0_" + tfh] > -50) { var tModelAu = conv2Tf(dataNAM["T0_" + tfh]); tAuto0TF += tModelAu; tAutoCounter++; }
        if(isSet(dataRAP) && isSet(dataRAP["T0_" + tfh]) && dataRAP["T0_" + tfh] > -50) { var tModelAu = conv2Tf(dataRAP["T0_" + tfh]); tAuto0TF += tModelAu; tAutoCounter++; }
        tAuto0TF = Math.round(tAuto0TF/tAutoCounter);
        
        tAutoCounter = 0;
        var tAuto900TF = 0;
        if(isSet(dataCMC) && isSet(dataCMC["T900_" + tfh]) && dataCMC["T900_" + tfh] > -50) { var tModelAu = conv2Tf(dataCMC["T900_" + tfh]); tAuto900TF += tModelAu; tAutoCounter++; }
        if(isSet(dataGFS) && isSet(dataGFS["T900_" + tfh]) && dataGFS["T900_" + tfh] > -50) { var tModelAu = conv2Tf(dataGFS["T900_" + tfh]); tAuto900TF += tModelAu; tAutoCounter++; }
        if(isSet(dataHRRR) && isSet(dataHRRR["T900_" + tfh]) && dataHRRR["T900_" + tfh] > -50) { var tModelAu = conv2Tf(dataHRRR["T900_" + tfh]); tAuto900TF += tModelAu; tAutoCounter++; }
        if(isSet(dataHRWA) && isSet(dataHRWA["T900_" + tfh]) && dataHRWA["T900_" + tfh] > -50) { var tModelAu = conv2Tf(dataHRWA["T900_" + tfh]); tAuto900TF += tModelAu; tAutoCounter++; }
        if(isSet(dataHRWN) && isSet(dataHRWN["T900_" + tfh]) && dataHRWN["T900_" + tfh] > -50) { var tModelAu = conv2Tf(dataHRWN["T900_" + tfh]); tAuto900TF += tModelAu; tAutoCounter++; }
        if(isSet(dataSRFA) && isSet(dataSRFA["T900_" + tfh]) && dataSRFA["T900_" + tfh] > -50) { var tModelAu = conv2Tf(dataSRFA["T900_" + tfh]); tAuto900TF += tModelAu; tAutoCounter++; }
        if(isSet(dataSRFN) && isSet(dataSRFN["T900_" + tfh]) && dataSRFN["T900_" + tfh] > -50) { var tModelAu = conv2Tf(dataSRFN["T900_" + tfh]); tAuto900TF += tModelAu; tAutoCounter++; }
        if(isSet(dataNAM) && isSet(dataNAM["T900_" + tfh]) && dataNAM["T900_" + tfh] > -50) { var tModelAu = conv2Tf(dataNAM["T900_" + tfh]); tAuto900TF += tModelAu; tAutoCounter++; }
        if(isSet(dataRAP) && isSet(dataRAP["T900_" + tfh]) && dataRAP["T900_" + tfh] > -50) { var tModelAu = conv2Tf(dataRAP["T900_" + tfh]); tAuto900TF += tModelAu; tAutoCounter++; }
        tAuto900TF = Math.round(tAuto900TF/tAutoCounter);
        
        tAutoCounter = 0;
        var tAuto0DF = 0;
        if(isSet(dataCMC) && isSet(dataCMC["D0_" + tfh]) && dataCMC["D0_" + tfh] > -50) { var tModelAu = conv2Tf(dataCMC["D0_" + tfh]); tAuto0DF += tModelAu; tAutoCounter++; }
        if(isSet(dataGFS) && isSet(dataGFS["D0_" + tfh]) && dataGFS["D0_" + tfh] > -50) { var tModelAu = conv2Tf(dataGFS["D0_" + tfh]); tAuto0DF += tModelAu; tAutoCounter++; }
        if(isSet(dataHRRR) && isSet(dataHRRR["D0_" + tfh]) && dataHRRR["D0_" + tfh] > -50) { var tModelAu = conv2Tf(dataHRRR["D0_" + tfh]); tAuto0DF += tModelAu; tAutoCounter++; }
        if(isSet(dataHRWA) && isSet(dataHRWA["D0_" + tfh]) && dataHRWA["D0_" + tfh] > -50) { var tModelAu = conv2Tf(dataHRWA["D0_" + tfh]); tAuto0DF += tModelAu; tAutoCounter++; }
        if(isSet(dataHRWN) && isSet(dataHRWN["D0_" + tfh]) && dataHRWN["D0_" + tfh] > -50) { var tModelAu = conv2Tf(dataHRWN["D0_" + tfh]); tAuto0DF += tModelAu; tAutoCounter++; }
        if(isSet(dataSRFA) && isSet(dataSRFA["D0_" + tfh]) && dataSRFA["D0_" + tfh] > -50) { var tModelAu = conv2Tf(dataSRFA["D0_" + tfh]); tAuto0DF += tModelAu; tAutoCounter++; }
        if(isSet(dataSRFN) && isSet(dataSRFN["D0_" + tfh]) && dataSRFN["D0_" + tfh] > -50) { var tModelAu = conv2Tf(dataSRFN["D0_" + tfh]); tAuto0DF += tModelAu; tAutoCounter++; }
        if(isSet(dataNAM) && isSet(dataNAM["D0_" + tfh]) && dataNAM["D0_" + tfh] > -50) { var tModelAu = conv2Tf(dataNAM["D0_" + tfh]); tAuto0DF += tModelAu; tAutoCounter++; }
        if(isSet(dataRAP) && isSet(dataRAP["D0_" + tfh]) && dataRAP["D0_" + tfh] > -50) { var tModelAu = conv2Tf(dataRAP["D0_" + tfh]); tAuto0DF += tModelAu; tAutoCounter++; }
        tAuto0DF = Math.round(tAuto0DF/tAutoCounter);        
        
        tAutoCounter = 0;
        var tAuto0WD = 0;
        if(isSet(dataCMC) && isSet(dataCMC["WD0_" + tfh]) && dataCMC["WD0_" + tfh] > -50) { var tModelAu = dataCMC["WD0_" + tfh]; tAuto0WD += tModelAu; tAutoCounter++; }
        if(isSet(dataGFS) && isSet(dataGFS["WD0_" + tfh]) && dataGFS["WD0_" + tfh] > -50) { var tModelAu = dataGFS["WD0_" + tfh]; tAuto0WD += tModelAu; tAutoCounter++; }
        if(isSet(dataHRRR) && isSet(dataHRRR["WD0_" + tfh]) && dataHRRR["WD0_" + tfh] > -50) { var tModelAu = dataHRRR["WD0_" + tfh]; tAuto0WD += tModelAu; tAutoCounter++; }
        if(isSet(dataHRWA) && isSet(dataHRWA["WD0_" + tfh]) && dataHRWA["WD0_" + tfh] > -50) { var tModelAu = dataHRWA["WD0_" + tfh]; tAuto0WD += tModelAu; tAutoCounter++; }
        if(isSet(dataHRWN) && isSet(dataHRWN["WD0_" + tfh]) && dataHRWN["WD0_" + tfh] > -50) { var tModelAu = dataHRWN["WD0_" + tfh]; tAuto0WD += tModelAu; tAutoCounter++; }
        if(isSet(dataSRFA) && isSet(dataSRFA["WD0_" + tfh]) && dataSRFA["WD0_" + tfh] > -50) { var tModelAu = dataSRFA["WD0_" + tfh]; tAuto0WD += tModelAu; tAutoCounter++; }
        if(isSet(dataSRFN) && isSet(dataSRFN["WD0_" + tfh]) && dataSRFN["WD0_" + tfh] > -50) { var tModelAu = dataSRFN["WD0_" + tfh]; tAuto0WD += tModelAu; tAutoCounter++; }
        if(isSet(dataNAM) && isSet(dataNAM["WD0_" + tfh]) && dataNAM["WD0_" + tfh] > -50) { var tModelAu = dataNAM["WD0_" + tfh]; tAuto0WD += tModelAu; tAutoCounter++; }
        if(isSet(dataRAP) && isSet(dataRAP["WD0_" + tfh]) && dataRAP["WD0_" + tfh] > -50) { var tModelAu = dataRAP["WD0_" + tfh]; tAuto0WD += tModelAu; tAutoCounter++; }
        tAuto0WD = Math.round(tAuto0WD/tAutoCounter);
        
        tAutoCounter = 0;
        var tAuto0WS = 0;
        if(isSet(dataCMC) && isSet(dataCMC["WS0_" + tfh]) && dataCMC["WS0_" + tfh] > -50) { var tModelAu = dataCMC["WS0_" + tfh]; tAuto0WS += tModelAu; tAutoCounter++; }
        if(isSet(dataGFS) && isSet(dataGFS["WS0_" + tfh]) && dataGFS["WS0_" + tfh] > -50) { var tModelAu = dataGFS["WS0_" + tfh]; tAuto0WS += tModelAu; tAutoCounter++; }
        if(isSet(dataHRRR) && isSet(dataHRRR["WS0_" + tfh]) && dataHRRR["WS0_" + tfh] > -50) { var tModelAu = dataHRRR["WS0_" + tfh]; tAuto0WS += tModelAu; tAutoCounter++; }
        if(isSet(dataHRWA) && isSet(dataHRWA["WS0_" + tfh]) && dataHRWA["WS0_" + tfh] > -50) { var tModelAu = dataHRWA["WS0_" + tfh]; tAuto0WS += tModelAu; tAutoCounter++; }
        if(isSet(dataHRWN) && isSet(dataHRWN["WS0_" + tfh]) && dataHRWN["WS0_" + tfh] > -50) { var tModelAu = dataHRWN["WS0_" + tfh]; tAuto0WS += tModelAu; tAutoCounter++; }
        if(isSet(dataSRFA) && isSet(dataSRFA["WS0_" + tfh]) && dataSRFA["WS0_" + tfh] > -50) { var tModelAu = dataSRFA["WS0_" + tfh]; tAuto0WS += tModelAu; tAutoCounter++; }
        if(isSet(dataSRFN) && isSet(dataSRFN["WS0_" + tfh]) && dataSRFN["WS0_" + tfh] > -50) { var tModelAu = dataSRFN["WS0_" + tfh]; tAuto0WS += tModelAu; tAutoCounter++; }
        if(isSet(dataNAM) && isSet(dataNAM["WS0_" + tfh]) && dataNAM["WS0_" + tfh] > -50) { var tModelAu = dataNAM["WS0_" + tfh]; tAuto0WS += tModelAu; tAutoCounter++; }
        if(isSet(dataRAP) && isSet(dataRAP["WS0_" + tfh]) && dataRAP["WS0_" + tfh] > -50) { var tModelAu = dataRAP["WS0_" + tfh]; tAuto0WS += tModelAu; tAutoCounter++; }
        tAuto0WS = Math.round(tAuto0WS/tAutoCounter);
                
        tAutoCounter = 0;
        var tAutoPRATE = 0;
        if(isSet(dataCMC) && isSet(dataCMC["PRATE_" + tfh]) && dataCMC["PRATE_" + tfh] > -50) { var tModelAu = dataCMC["PRATE_" + tfh]; tAutoPRATE += tModelAu; tAutoCounter++; }
        if(isSet(dataGFS) && isSet(dataGFS["PRATE_" + tfh]) && dataGFS["PRATE_" + tfh] > -50) { var tModelAu = dataGFS["PRATE_" + tfh]; tAutoPRATE += tModelAu; tAutoCounter++; }
        if(isSet(dataHRRR) && isSet(dataHRRR["PRATE_" + tfh]) && dataHRRR["PRATE_" + tfh] > -50) { var tModelAu = dataHRRR["PRATE_" + tfh]; tAutoPRATE += tModelAu; tAutoCounter++; }
        if(isSet(dataHRWA) && isSet(dataHRWA["PRATE_" + tfh]) && dataHRWA["PRATE_" + tfh] > -50) { var tModelAu = dataHRWA["PRATE_" + tfh]; tAutoPRATE += tModelAu; tAutoCounter++; }
        if(isSet(dataHRWN) && isSet(dataHRWN["PRATE_" + tfh]) && dataHRWN["PRATE_" + tfh] > -50) { var tModelAu = dataHRWN["PRATE_" + tfh]; tAutoPRATE += tModelAu; tAutoCounter++; }
        if(isSet(dataSRFA) && isSet(dataSRFA["PRATE_" + tfh]) && dataSRFA["PRATE_" + tfh] > -50) { var tModelAu = dataSRFA["PRATE_" + tfh]; tAutoPRATE += tModelAu; tAutoCounter++; }
        if(isSet(dataSRFN) && isSet(dataSRFN["PRATE_" + tfh]) && dataSRFN["PRATE_" + tfh] > -50) { var tModelAu = dataSRFN["PRATE_" + tfh]; tAutoPRATE += tModelAu; tAutoCounter++; }
        if(isSet(dataNAM) && isSet(dataNAM["PRATE_" + tfh]) && dataNAM["PRATE_" + tfh] > -50) { var tModelAu = dataNAM["PRATE_" + tfh]; tAutoPRATE += tModelAu; tAutoCounter++; }
        if(isSet(dataRAP) && isSet(dataRAP["PRATE_" + tfh]) && dataRAP["PRATE_" + tfh] > -50) { var tModelAu = dataRAP["PRATE_" + tfh]; tAutoPRATE += tModelAu; tAutoCounter++; }
        if(isSet(tAutoPRATE)) { tAutoPRATE = (tAutoPRATE/tAutoCounter).toFixed(2); preciptTot = precipTot+tAutoPRATE; } else { tAutoPRATE = 0.00; }        
        
        tAutoCounter = 0;
        var tAutoPWAT = 0;
        if(isSet(dataCMC) && isSet(dataCMC["PWAT_" + tfh]) && dataCMC["PWAT_" + tfh] > -50) { var tModelAu = dataCMC["PWAT_" + tfh]; tAutoPWAT += tModelAu; tAutoCounter++; }
        if(isSet(dataGFS) && isSet(dataGFS["PWAT_" + tfh]) && dataGFS["PWAT_" + tfh] > -50) { var tModelAu = dataGFS["PWAT_" + tfh]; tAutoPWAT += tModelAu; tAutoCounter++; }
        if(isSet(dataHRRR) && isSet(dataHRRR["PWAT_" + tfh]) && dataHRRR["PWAT_" + tfh] > -50) { var tModelAu = dataHRRR["PWAT_" + tfh]; tAutoPWAT += tModelAu; tAutoCounter++; }
        if(isSet(dataHRWA) && isSet(dataHRWA["PWAT_" + tfh]) && dataHRWA["PWAT_" + tfh] > -50) { var tModelAu = dataHRWA["PWAT_" + tfh]; tAutoPWAT += tModelAu; tAutoCounter++; }
        if(isSet(dataHRWN) && isSet(dataHRWN["PWAT_" + tfh]) && dataHRWN["PWAT_" + tfh] > -50) { var tModelAu = dataHRWN["PWAT_" + tfh]; tAutoPWAT += tModelAu; tAutoCounter++; }
        if(isSet(dataSRFA) && isSet(dataSRFA["PWAT_" + tfh]) && dataSRFA["PWAT_" + tfh] > -50) { var tModelAu = dataSRFA["PWAT_" + tfh]; tAutoPWAT += tModelAu; tAutoCounter++; }
        if(isSet(dataSRFN) && isSet(dataSRFN["PWAT_" + tfh]) && dataSRFN["PWAT_" + tfh] > -50) { var tModelAu = dataSRFN["PWAT_" + tfh]; tAutoPWAT += tModelAu; tAutoCounter++; }
        if(isSet(dataNAM) && isSet(dataNAM["PWAT_" + tfh]) && dataNAM["PWAT_" + tfh] > -50) { var tModelAu = dataNAM["PWAT_" + tfh]; tAutoPWAT += tModelAu; tAutoCounter++; }
        if(isSet(dataRAP) && isSet(dataRAP["PWAT_" + tfh]) && dataRAP["PWAT_" + tfh] > -50) { var tModelAu = dataRAP["PWAT_" + tfh]; tAutoPWAT += tModelAu; tAutoCounter++; }
        if(isSet(tAutoPWAT)) { tAutoPWAT = (tAutoPWAT/tAutoCounter).toFixed(2); } else { tAutoPWAT = 0.00; }
        
        tAutoCounter = 0;
        var tAutoLI = 0;
        if(isSet(dataCMC) && isSet(dataCMC["LI_" + tfh]) && dataCMC["LI_" + tfh] > -50) { var tModelAu = dataCMC["LI_" + tfh]; tAutoLI += tModelAu; tAutoCounter++; }
        if(isSet(dataGFS) && isSet(dataGFS["LI_" + tfh]) && dataGFS["LI_" + tfh] > -50) { var tModelAu = dataGFS["LI_" + tfh]; tAutoLI += tModelAu; tAutoCounter++; }
        if(isSet(dataHRRR) && isSet(dataHRRR["LI_" + tfh]) && dataHRRR["LI_" + tfh] > -50) { var tModelAu = dataHRRR["LI_" + tfh]; tAutoLI += tModelAu; tAutoCounter++; }
        if(isSet(dataHRWA) && isSet(dataHRWA["LI_" + tfh]) && dataHRWA["LI_" + tfh] > -50) { var tModelAu = dataHRWA["LI_" + tfh]; tAutoLI += tModelAu; tAutoCounter++; }
        if(isSet(dataHRWN) && isSet(dataHRWN["LI_" + tfh]) && dataHRWN["LI_" + tfh] > -50) { var tModelAu = dataHRWN["LI_" + tfh]; tAutoLI += tModelAu; tAutoCounter++; }
        if(isSet(dataSRFA) && isSet(dataSRFA["LI_" + tfh]) && dataSRFA["LI_" + tfh] > -50) { var tModelAu = dataSRFA["LI_" + tfh]; tAutoLI += tModelAu; tAutoCounter++; }
        if(isSet(dataSRFN) && isSet(dataSRFN["LI_" + tfh]) && dataSRFN["LI_" + tfh] > -50) { var tModelAu = dataSRFN["LI_" + tfh]; tAutoLI += tModelAu; tAutoCounter++; }
        if(isSet(dataNAM) && isSet(dataNAM["LI_" + tfh]) && dataNAM["LI_" + tfh] > -50) { var tModelAu = dataNAM["LI_" + tfh]; tAutoLI += tModelAu; tAutoCounter++; }
        if(isSet(dataRAP) && isSet(dataRAP["LI_" + tfh]) && dataRAP["LI_" + tfh] > -50) { var tModelAu = dataRAP["LI_" + tfh]; tAutoLI += tModelAu; tAutoCounter++; }
        tAutoLI = (tAutoLI/tAutoCounter).toFixed(2);
                
        tAutoCounter = 0;
        var tAutoCAPE = 0;
        if(isSet(dataCMC) && isSet(dataCMC["CAPE_" + tfh]) && dataCMC["CAPE_" + tfh] > -50) { var tModelAu = dataCMC["CAPE_" + tfh]; tAutoCAPE += tModelAu; tAutoCounter++; }
        if(isSet(dataGFS) && isSet(dataGFS["CAPE_" + tfh]) && dataGFS["CAPE_" + tfh] > -50) { var tModelAu = dataGFS["CAPE_" + tfh]; tAutoCAPE += tModelAu; tAutoCounter++; }
        if(isSet(dataHRRR) && isSet(dataHRRR["CAPE_" + tfh]) && dataHRRR["CAPE_" + tfh] > -50) { var tModelAu = dataHRRR["CAPE_" + tfh]; tAutoCAPE += tModelAu; tAutoCounter++; }
        if(isSet(dataHRWA) && isSet(dataHRWA["CAPE_" + tfh]) && dataHRWA["CAPE_" + tfh] > -50) { var tModelAu = dataHRWA["CAPE_" + tfh]; tAutoCAPE += tModelAu; tAutoCounter++; }
        if(isSet(dataHRWN) && isSet(dataHRWN["CAPE_" + tfh]) && dataHRWN["CAPE_" + tfh] > -50) { var tModelAu = dataHRWN["CAPE_" + tfh]; tAutoCAPE += tModelAu; tAutoCounter++; }
        if(isSet(dataSRFA) && isSet(dataSRFA["CAPE_" + tfh]) && dataSRFA["CAPE_" + tfh] > -50) { var tModelAu = dataSRFA["CAPE_" + tfh]; tAutoCAPE += tModelAu; tAutoCounter++; }
        if(isSet(dataSRFN) && isSet(dataSRFN["CAPE_" + tfh]) && dataSRFN["CAPE_" + tfh] > -50) { var tModelAu = dataSRFN["CAPE_" + tfh]; tAutoCAPE += tModelAu; tAutoCounter++; }
        if(isSet(dataNAM) && isSet(dataNAM["CAPE_" + tfh]) && dataNAM["CAPE_" + tfh] > -50) { var tModelAu = dataNAM["CAPE_" + tfh]; tAutoCAPE += tModelAu; tAutoCounter++; }
        if(isSet(dataRAP) && isSet(dataRAP["CAPE_" + tfh]) && dataRAP["CAPE_" + tfh] > -50) { var tModelAu = dataRAP["CAPE_" + tfh]; tAutoCAPE += tModelAu; tAutoCounter++; }
        tAutoCAPE = Math.round(tAutoCAPE/tAutoCounter);
                                
        tAutoCounter = 0;
        var tAutoCIN = 0;
        if(isSet(dataCMC) && isSet(dataCMC["CIN_" + tfh]) && dataCMC["CIN_" + tfh] > -50) { var tModelAu = dataCMC["CIN_" + tfh]; tAutoCIN += tModelAu; tAutoCounter++; }
        if(isSet(dataGFS) && isSet(dataGFS["CIN_" + tfh]) && dataGFS["CIN_" + tfh] > -50) { var tModelAu = dataGFS["CIN_" + tfh]; tAutoCIN += tModelAu; tAutoCounter++; }
        if(isSet(dataHRRR) && isSet(dataHRRR["CIN_" + tfh]) && dataHRRR["CIN_" + tfh] > -50) { var tModelAu = dataHRRR["CIN_" + tfh]; tAutoCIN += tModelAu; tAutoCounter++; }
        if(isSet(dataHRWA) && isSet(dataHRWA["CIN_" + tfh]) && dataHRWA["CIN_" + tfh] > -50) { var tModelAu = dataHRWA["CIN_" + tfh]; tAutoCIN += tModelAu; tAutoCounter++; }
        if(isSet(dataHRWN) && isSet(dataHRWN["CIN_" + tfh]) && dataHRWN["CIN_" + tfh] > -50) { var tModelAu = dataHRWN["CIN_" + tfh]; tAutoCIN += tModelAu; tAutoCounter++; }
        if(isSet(dataSRFA) && isSet(dataSRFA["CIN_" + tfh]) && dataSRFA["CIN_" + tfh] > -50) { var tModelAu = dataSRFA["CIN_" + tfh]; tAutoCIN += tModelAu; tAutoCounter++; }
        if(isSet(dataSRFN) && isSet(dataSRFN["CIN_" + tfh]) && dataSRFN["CIN_" + tfh] > -50) { var tModelAu = dataSRFN["CIN_" + tfh]; tAutoCIN += tModelAu; tAutoCounter++; }
        if(isSet(dataNAM) && isSet(dataNAM["CIN_" + tfh]) && dataNAM["CIN_" + tfh] > -50) { var tModelAu = dataNAM["CIN_" + tfh]; tAutoCIN += tModelAu; tAutoCounter++; }
        if(isSet(dataRAP) && isSet(dataRAP["CIN_" + tfh]) && dataRAP["CIN_" + tfh] > -50) { var tModelAu = dataRAP["CIN_" + tfh]; tAutoCIN += tModelAu; tAutoCounter++; }
        tAutoCIN = Math.round(tAutoCIN/tAutoCounter);
                
        var rHumidity = relativeHumidity(tAuto0TF, tAuto0DF);
        
        var estimatedSnow = 0;
        var estimatedFreezingRain = 0;
        
        switch(true) {
            case (tAuto0TF > 34 && tAuto900TF > 32):
                break;
            case (tAuto0TF <= 34 && tAuto0TF > 32 && tAuto900TF <= 32):
                estimatedSnow = (10*(tAutoPRATE*snowRatio(tAutoPRATE,tAuto0TF))).toFixed(1);
                break;
            case (tAuto0TF <= 32):
                if(tAuto900TF > 32) {
                    estimatedFreezingRain = tAutoPRATE;
                } else {
                    estimatedSnow = (10*(tAutoPRATE*snowRatio(tAutoPRATE,tAuto0TF))).toFixed(1);
                }
                break;
        }
        
        estimatedSnowTotal = estimatedSnowTotal + estimatedSnow;
        estimatedFreezingRainTotal = estimatedFreezingRainTotal + estimatedFreezingRain;
        
        if(isSet(tAuto0TF)) {
            mosTable += "<div class='tr'>" +
                    "<span class='td'>" + tValidTime + "</span>" +
                    "<span class='td' style='" + styleTemp(tAuto0TF) + "'><div class='UPop'>" + tAuto0TF +
                    "<div class='UPopO'>";
            if(isSet(dataCMC) && isSet(dataCMC["T0_" + tfh])) { mosTable += "<strong>CMC</strong>: <span style='" + styleTemp(conv2Tf(dataCMC["T0_" + tfh])) + "'>" + conv2Tf(dataCMC["T0_" + tfh]) + "</span><br/>"; }
            if(isSet(dataGFS) && isSet(dataGFS["T0_" + tfh])) { mosTable += "<strong>GFS</strong>: <span style='" + styleTemp(conv2Tf(dataGFS["T0_" + tfh])) + "'>" + conv2Tf(dataGFS["T0_" + tfh]) + "</span><br/>"; }
            if(isSet(dataHRRR) && isSet(dataHRRR["T0_" + tfh])) { mosTable += "<strong>HRRR</strong>: <span style='" + styleTemp(conv2Tf(dataHRRR["T0_" + tfh])) + "'>" + conv2Tf(dataHRRR["T0_" + tfh]) + "</span><br/>"; }
            if(isSet(dataHRWA) && isSet(dataHRWA["T0_" + tfh])) { mosTable += "<strong>HRWA</strong>: <span style='" + styleTemp(conv2Tf(dataHRWA["T0_" + tfh])) + "'>" + conv2Tf(dataHRWA["T0_" + tfh]) + "</span><br/>"; }
            if(isSet(dataHRWN) && isSet(dataHRWN["T0_" + tfh])) { mosTable += "<strong>HRWN</strong>: <span style='" + styleTemp(conv2Tf(dataHRWN["T0_" + tfh])) + "'>" + conv2Tf(dataHRWN["T0_" + tfh]) + "</span><br/>"; }
            if(isSet(dataSRFA) && isSet(dataSRFA["T0_" + tfh])) { mosTable += "<strong>SRFA</strong>: <span style='" + styleTemp(conv2Tf(dataSRFA["T0_" + tfh])) + "'>" + conv2Tf(dataSRFA["T0_" + tfh]) + "</span><br/>"; }
            if(isSet(dataSRFN) && isSet(dataSRFN["T0_" + tfh])) { mosTable += "<strong>SRFN</strong>: <span style='" + styleTemp(conv2Tf(dataSRFN["T0_" + tfh])) + "'>" + conv2Tf(dataSRFN["T0_" + tfh]) + "</span><br/>"; }
            if(isSet(dataNAM) && isSet(dataNAM["T0_" + tfh])) { mosTable += "<strong>NAM</strong>: <span style='" + styleTemp(conv2Tf(dataNAM["T0_" + tfh])) + "'>" + conv2Tf(dataNAM["T0_" + tfh]) + "</span><br/>"; }
            if(isSet(dataRAP) && isSet(dataRAP["T0_" + tfh])) { mosTable += "<strong>RAP</strong>: <span style='" + styleTemp(conv2Tf(dataRAP["T0_" + tfh])) + "'>" + conv2Tf(dataRAP["T0_" + tfh]) + "</span><br/>"; }
            mosTable += "</div></div></span>" +
                    "<span class='td' style='" + styleTemp(tAuto0DF) + "'><div class='UPop'>" + tAuto0DF +
                    "<div class='UPopO'>";
            if(isSet(dataCMC) && isSet(dataCMC["D0_" + tfh])) { mosTable += "<strong>CMC</strong>: <span style='" + styleTemp(conv2Tf(dataCMC["D0_" + tfh])) + "'>" + conv2Tf(dataCMC["D0_" + tfh]) + "</span><br/>"; }
            if(isSet(dataGFS) && isSet(dataGFS["D0_" + tfh])) { mosTable += "<strong>GFS</strong>: <span style='" + styleTemp(conv2Tf(dataGFS["D0_" + tfh])) + "'>" + conv2Tf(dataGFS["D0_" + tfh]) + "</span><br/>"; }
            if(isSet(dataHRRR) && isSet(dataHRRR["D0_" + tfh])) { mosTable += "<strong>HRRR</strong>: <span style='" + styleTemp(conv2Tf(dataHRRR["D0_" + tfh])) + "'>" + conv2Tf(dataHRRR["D0_" + tfh]) + "</span><br/>"; }
            if(isSet(dataHRWA) && isSet(dataHRWA["D0_" + tfh])) { mosTable += "<strong>HRWA</strong>: <span style='" + styleTemp(conv2Tf(dataHRWA["D0_" + tfh])) + "'>" + conv2Tf(dataHRWA["D0_" + tfh]) + "</span><br/>"; }
            if(isSet(dataHRWN) && isSet(dataHRWN["D0_" + tfh])) { mosTable += "<strong>HRWN</strong>: <span style='" + styleTemp(conv2Tf(dataHRWN["D0_" + tfh])) + "'>" + conv2Tf(dataHRWN["D0_" + tfh]) + "</span><br/>"; }
            if(isSet(dataSRFA) && isSet(dataSRFA["D0_" + tfh])) { mosTable += "<strong>SRFA</strong>: <span style='" + styleTemp(conv2Tf(dataSRFA["D0_" + tfh])) + "'>" + conv2Tf(dataSRFA["D0_" + tfh]) + "</span><br/>"; }
            if(isSet(dataSRFN) && isSet(dataSRFN["D0_" + tfh])) { mosTable += "<strong>SRFN</strong>: <span style='" + styleTemp(conv2Tf(dataSRFN["D0_" + tfh])) + "'>" + conv2Tf(dataSRFN["D0_" + tfh]) + "</span><br/>"; }
            if(isSet(dataNAM) && isSet(dataNAM["D0_" + tfh])) { mosTable += "<strong>NAM</strong>: <span style='" + styleTemp(conv2Tf(dataNAM["D0_" + tfh])) + "'>" + conv2Tf(dataNAM["D0_" + tfh]) + "</span><br/>"; }
            if(isSet(dataRAP) && isSet(dataRAP["D0_" + tfh])) { mosTable += "<strong>RAP</strong>: <span style='" + styleTemp(conv2Tf(dataRAP["D0_" + tfh])) + "'>" + conv2Tf(dataRAP["D0_" + tfh]) + "</span><br/>"; }
            mosTable += "</div></div></span>" +
                    "<span class='td' style='" + styleRh(rHumidity) + "'>" + rHumidity + "</span>" +
                    "<span class='td' style='" + styleWind(tAuto0WS) + "'>" + windDirSvg(tAuto0WD) + " (" + tAuto0WS + ")</span>" +
                    "<span class='td' style='" + styleLiquid(tAutoPRATE) + "'><div class='UPop'>" + tAutoPRATE +
                    "<div class='UPopO'>";
            if(isSet(dataCMC) && isSet(dataCMC["PRATE_" + tfh])) { mosTable += "<strong>CMC</strong>: <span style='" + styleLiquid(dataCMC["PRATE_" + tfh]) + "'>" + dataCMC["PRATE_" + tfh].toFixed(2) + "</span><br/>"; }
            if(isSet(dataGFS) && isSet(dataGFS["PRATE_" + tfh])) { mosTable += "<strong>GFS</strong>: <span style='" + styleLiquid(dataGFS["PRATE_" + tfh]) + "'>" + dataGFS["PRATE_" + tfh].toFixed(2) + "</span><br/>"; }
            if(isSet(dataHRRR) && isSet(dataHRRR["PRATE_" + tfh])) { mosTable += "<strong>HRRR</strong>: <span style='" + styleLiquid(dataHRRR["PRATE_" + tfh]) + "'>" + dataHRRR["PRATE_" + tfh].toFixed(2) + "</span><br/>"; }
            if(isSet(dataHRWA) && isSet(dataHRWA["PRATE_" + tfh])) { mosTable += "<strong>HRWA</strong>: <span style='" + styleLiquid(dataHRWA["PRATE_" + tfh]) + "'>" + dataHRWA["PRATE_" + tfh].toFixed(2) + "</span><br/>"; }
            if(isSet(dataHRWN) && isSet(dataHRWN["PRATE_" + tfh])) { mosTable += "<strong>HRWN</strong>: <span style='" + styleLiquid(dataHRWN["PRATE_" + tfh]) + "'>" + dataHRWN["PRATE_" + tfh].toFixed(2) + "</span><br/>"; }
            if(isSet(dataSRFA) && isSet(dataSRFA["PRATE_" + tfh])) { mosTable += "<strong>SRFA</strong>: <span style='" + styleLiquid(dataSRFA["PRATE_" + tfh]) + "'>" + dataSRFA["PRATE_" + tfh].toFixed(2) + "</span><br/>"; }
            if(isSet(dataSRFN) && isSet(dataSRFN["PRATE_" + tfh])) { mosTable += "<strong>SRFN</strong>: <span style='" + styleLiquid(dataSRFN["PRATE_" + tfh]) + "'>" + dataSRFN["PRATE_" + tfh].toFixed(2) + "</span><br/>"; }
            if(isSet(dataNAM) && isSet(dataNAM["PRATE_" + tfh])) { mosTable += "<strong>NAM</strong>: <span style='" + styleLiquid(dataNAM["PRATE_" + tfh]) + "'>" + dataNAM["PRATE_" + tfh].toFixed(2) + "</span><br/>"; }
            if(isSet(dataRAP) && isSet(dataRAP["PRATE_" + tfh])) { mosTable += "<strong>RAP</strong>: <span style='" + styleLiquid(dataRAP["PRATE_" + tfh]) + "'>" + dataRAP["PRATE_" + tfh].toFixed(2) + "</span><br/>"; }
            mosTable += "</div></div></span>" +
                    "<span class='td' style='" + styleLiquid(precipTot) + "'>" + precipTot + "</span>" +
                    "<span class='td " + colorSnow(estimatedSnow) + "'><div class='UPopNM'>" + estimatedSnow + 
                    "<div class='UPopNMO'>" +
                    "<strong>Total snow</strong>: " + estimatedSnowTotal + " in.<br/>" +
                    "<strong>Freezing rain</strong>: " + estimatedFreezingRainTotal + " in.<br/>" +
                    "</div></div></span>" +
                    "<span class='td' style='" + styleLiquid(tAutoPWAT) + "'>" + tAutoPWAT + "</span>" +
                    "<span class='td " + colorLi(tAutoLI) + "'>" + tAutoLI + "</span>" +
                    "<span class='td' style='" + styleCape(tAutoCAPE) + "'>" + tAutoCAPE + "</span>" +
                    "<span class='td " + colorCin(tAutoCIN) + "'>" + tAutoCIN + "</span>";
            heights.forEach(function (hgt) {
                tAutoCounter = 0;
                var tAutoTHgt = 0;
                if(isSet(dataCMC) && isSet(dataCMC["T" + hgt + "_" + tfh]) && dataCMC["T" + hgt + "_" + tfh] > -50) { var tModelAu = conv2Tf(dataCMC["T" + hgt + "_" + tfh]); tAutoTHgt += tModelAu; tAutoCounter++; }
                if(isSet(dataGFS) && isSet(dataGFS["T" + hgt + "_" + tfh]) && dataGFS["T" + hgt + "_" + tfh] > -50) { var tModelAu = conv2Tf(dataGFS["T" + hgt + "_" + tfh]); tAutoTHgt += tModelAu; tAutoCounter++; }
                if(isSet(dataHRRR) && isSet(dataHRRR["T" + hgt + "_" + tfh]) && dataHRRR["T" + hgt + "_" + tfh] > -50) { var tModelAu = conv2Tf(dataHRRR["T" + hgt + "_" + tfh]); tAutoTHgt += tModelAu; tAutoCounter++; }
                if(isSet(dataHRWA) && isSet(dataHRWA["T" + hgt + "_" + tfh]) && dataHRWA["T" + hgt + "_" + tfh] > -50) { var tModelAu = conv2Tf(dataHRWA["T" + hgt + "_" + tfh]); tAutoTHgt += tModelAu; tAutoCounter++; }
                if(isSet(dataHRWN) && isSet(dataHRWN["T" + hgt + "_" + tfh]) && dataHRWN["T" + hgt + "_" + tfh] > -50) { var tModelAu = conv2Tf(dataHRWN["T" + hgt + "_" + tfh]); tAutoTHgt += tModelAu; tAutoCounter++; }
                if(isSet(dataSRFA) && isSet(dataSRFA["T" + hgt + "_" + tfh]) && dataSRFA["T" + hgt + "_" + tfh] > -50) { var tModelAu = conv2Tf(dataSRFA["T" + hgt + "_" + tfh]); tAutoTHgt += tModelAu; tAutoCounter++; }
                if(isSet(dataSRFN) && isSet(dataSRFN["T" + hgt + "_" + tfh]) && dataSRFN["T" + hgt + "_" + tfh] > -50) { var tModelAu = conv2Tf(dataSRFN["T" + hgt + "_" + tfh]); tAutoTHgt += tModelAu; tAutoCounter++; }
                if(isSet(dataNAM) && isSet(dataNAM["T" + hgt + "_" + tfh]) && dataNAM["T" + hgt + "_" + tfh] > -50) { var tModelAu = conv2Tf(dataNAM["T" + hgt + "_" + tfh]); tAutoTHgt += tModelAu; tAutoCounter++; }
                if(isSet(dataRAP) && isSet(dataRAP["T" + hgt + "_" + tfh]) && dataRAP["T" + hgt + "_" + tfh] > -50) { var tModelAu = conv2Tf(dataRAP["T" + hgt + "_" + tfh]); tAutoTHgt += tModelAu; tAutoCounter++; }
                tAutoTHgt = Math.round(tAutoTHgt/tAutoCounter);
                mosTable += "<span class='td' style='" + styleTemp(tAutoTHgt) + "'>" + tAutoTHgt + "</span>";
            });
            mosTable += "</div>";
            
        }
    });
    mosTable += "</div>";
    rData += mosTable;
    dojo.byId("WxLocalModel").innerHTML = rData;
}



