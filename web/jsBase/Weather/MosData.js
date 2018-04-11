/* 
by Anthony Stump
Created: 27 Mar 2018
Updated: 10 Apr 2018
 */

function processMosData(last, heights, hours, runs, jsonModelData) {
    var searchString = last.RunString;
    var runString, dataCMC, dataGFS, dataHRRR, dataHRWA, dataHRWN, dataSRFA, dataSRFN, dataNAM, dataRAP;
    var mosCols = [ "Forecast", "TA", "TD", "RH", "WND", "P3", "PT", "X3", "PW", "LI", "CAPE", "CIN" ];
    var rData = "<h3>GRIB2 JSON Model Output Data</h3>" +
            "<em>For KOJC / Olathe Johnson County<br/>" +
            "Auto-updated hourly</em>" +
            "<a href='" + getBasePath("g2OutOld") + "' target='new'>Automatic model image output</a>";
    var models = [ "CMC", "GFS", "HRRR", "NAM", "RAP", "HRWA", "HRWN", "SRFA", "SRFN" ];
    var heights = []; heights.forEach(function (hgt) { heights.push(hgt); mosCols.push(hgt) });
    var gfsFh = []; hours.forEach(function (tfh) { gfsFh.push(tfh); });
    var reportingModels = "";
    var estZRTot, estSnowTot, precipTot, mosi;
    estZRTot = estSnowTot = precipTot = mosi = 0;
    var mrForm = "<form id='ModelRunForm'><select id='RunSearch' name='RunSearch'><option value=''>Select...</option>";
    runs.forEach(function (run) { mrForm += "<option value='" + run.RunString + "'>" + run.RunString + "</option>"; });
    mrForm += "</select>";
    rData += mrForm + "<p>";
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
        dataCMC = JSON.parse(jmd.CMC);
        dataGFS = JSON.parse(jmd.GFS);
        dataHRRR = JSON.parse(jmd.HRRR);
        dataHRWA = JSON.parse(jmd.HRWA);
        dataHRWN = JSON.parse(jmd.HRWN);
        dataSRFA = JSON.parse(jmd.SRFA);
        dataSRFN = JSON.parse(jmd.SRFN);
        dataNAM = JSON.parse(jmd.NAM);
        dataRAP = JSON.parse(jmd.RAP);
    });
    if(isSet(dataCMC.T0_3)) { reportingModels += " CMC"; }
    if(isSet(dataGFS.T0_3)) { reportingModels += " GMC"; }
    if(isSet(dataHRRR.T0_3)) { reportingModels += " HRRR"; }
    if(isSet(dataHRWA.T0_3)) { reportingModels += " HRWA"; }
    if(isSet(dataHRWN.T0_3)) { reportingModels += " HRWN"; }
    if(isSet(dataSRFA.T0_3)) { reportingModels += " SRFA"; }
    if(isSet(dataSRFN.T0_3)) { reportingModels += " SRFN"; }
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
                isSet(dataCMC["T0_" + tfh]) &&
                isSet(dataGFS["T0_" + tfh]) &&
                isSet(dataHRRR["T0_" + tfh]) &&
                isSet(dataHRWA["T0_" + tfh]) &&
                isSet(dataHRWN["T0_" + tfh]) &&
                isSet(dataSRFA["T0_" + tfh]) &&
                isSet(dataSRFN["T0_" + tfh]) &&
                isSet(dataNAM["T0_" + tfh]) &&
                isSet(dataRAP["T0_" + tfh])
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
    return rData;
}



