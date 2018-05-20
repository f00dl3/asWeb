/* 
by Anthony Stump
Created: 27 Mar 2018
Updated: 20 May 2018
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
    var tAutoCounter = 0;
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
        dataHRRR = JSON.parse(jmd.HRRR);
        dataHRWA = jmd.HRWA;
        dataHRWN = jmd.HRWN;
        dataSRFA = jmd.SRFA;
        dataSRFN = jmd.SRFN;
        dataNAM = jmd.NAM;
        dataRAP = jmd.RAP;
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
                "</div>";
        
    });
    mosTable += "</div>";
    rData += mosTable;
    dojo.byId("WxLocalModel").innerHTML = rData;
}



