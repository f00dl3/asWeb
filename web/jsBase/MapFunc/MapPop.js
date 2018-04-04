/* 
by Anthony Stump
Created: 2 Apr 2018
Updated: 3 Apr 2018
 */

function getFadGpsJson(pParams) {
    aniPreload("on");
    var xdt1, xdt2;
    var oYear = getDate("year", 0, "yearOnly");
    if(isSet(pParams.tGPSDate)) {
        xdt1 = pParams.tGPSDate;
        xdt2 = pParams.tGPSDate
    } else {
        xdt1 = getDate("day", -365, "dateOnly");
        xdt2 = getDate("day", 0, "dateOnly");
    }
    var thePostData = "doWhat=getFadGpsJson&XDT1=" + xdt1 + "&XDT2=" + xdt2;
    // build the activity PU switch in the javacode.
    var xhArgs = {
        preventCache: true,
        url: getResource("Fitness"),
        postData: thePostData,
        handleAs: "json",
        timeout: timeOutMilli,
        load: function(data) {
            onFadGpsJson(
                pParams,
                data.fadData, // only need previous weight reading.
                data.overallStats,
                data.gpsJsonPu
            );
            aniPreload("off");
        },
        error: function(data, iostatus) {
            aniPreload("off");
            window.alert("xhrGet for FAD GPS JSON FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
        }
    };
    dojo.xhrPost(xhArgs);
}

function getRadarModelData(pParams) {
    aniPreload("on");
    var obsJson = getResource("Wx");
    var obsJsonPostData = "doWhat=getRadarMapData";
    var arObsJsonMq = {
        preventCache: true,
        url: obsJson,
        postData: obsJsonPostData,
        handleAs: "json",
        timeout: timeOutMilli,
        load: function(data) {
            // Build this: query to JSONModelLast hooks to GFSFHA as parameter
            var mrsLast = data.jsonModelLast;
            var hours = data.hours;
            var xmlGeo = data.obsXmlGeo;
            onRadarModelData(mrsLast, hours, xmlGeo);
            aniPreload("off");
        },
        error: function(data, iostatus) {
            aniPreload("off");
            window.alert("xhrGet obsJson: FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
        }
    };
    dojo.xhrPost(arObsJsonMq);
}

function mapPop(mapType, title) {
    var tGPSDate;
    var activity;
    var actDate;
    switch(mapType) {
        case "gpsJSON":
            tGPSDate = title;
            activity = mapType;
            actDate = activity + "-" + tGPSDate;
            var passedParams = {
                "tGPSDate": tGPSDate,
                "activity": activity,
                "actDate": actDate,
                "passedAct": activity,
                "passedDate": tGPSDate
            };
            getFadGpsJson(passedParams);
            break;
        case "radarMode":
            getRadarModelData();
    }
    
}

function onFadGpsJson(pParams, fadData, overallSensors, gpsJsonPu) {
    var activity = pParams.activity;
    var rData = "";
    var puAltitude, puCadence, puDists, puHeart, puPower, puSpeed, puTemps, puTimes;
    var gpsInfoC, gpsInfoE, gpsInfoH, gpsInfoP, gpsInfoQ, gpsInfoS, gpsInfoT, gpsInfoD;
    var gpsThumbSize, gpsPopScale, gpsPopOClass, labelC, labelE, labelF, labelH, labelP, labelQ, labelS, labelT;
    gpsInfoC = gpsInfoE = gpsInfoH = gpsInfoP = gpsInfoQ = gpsInfoS = gpsInfoT = gpsInfoD = "";
    puAltitude = puCadence = puDists = puHeart = puPower = puSpeed = puTemps = puTimes = [];
    var imgWidth = (960/gpsPopScale);
    var imgHeight = (540/gpsPopScale);
    gpsJsonPu.forEach(function (gjpu) {
        var gpsLog = JSON.parse(gjpu.gpsLog);
        Object.keys(gpsLog).forEach(function (glk) {
            if(isSet(glk.AltitudeFt)) { puAltitude.put(glk.AltitudeFt); } else { puAltitude.put(0); }
            if(isSet(glk.Cadence)) { puCadence.put(glk.Cadence); } else { puCadence.put(0); }
            if(isSet(glk.DistTotMiles)) { puDists.put(glk.DistTotMiles); } else { puDists.put(0); }
            if(isSet(glk.HeartRate)) { puHeart.put(glk.HeartRate); } else { puHeart.put(0); }
            if(isSet(glk.PowerWatts)) { puPower.put(glk.PowerWatts); } else { puPower.put(0); }
            if(isSet(glk.SpeedMPH)) { puSpeed.put(glk.SpeedMPH); } else { puSpeed.put(0); }
            if(isSet(glk.TemperatureF)) { puTemps.put(glk.TemperatureF); } else { puTemps.put(0); }
        });
    });
    var thisActivityTimeMinutes = (Math.max(puTimes)/100/60).toFixed(1);
    if(checkMobile()) {
        gpsThumbSize = 'th_icon';
        gpsPopScale = 3;
        gpsPopOClass = 'mGPSPopO';
        labelC = "CA";
        labelE = "EL";
        labelF = "PF";
        labelH = "HR";
        labelP = "PW";
        labelQ = "QA";
        labelS = "SP";
        labelT = "TM";
    } else {
        gpsThumbSize = 'th_small';
        gpsPopScale = 1;
        gpsPopOClass = 'GPSPopO';
        labelC = "Cadence";
        labelE = "Elvat";
        labelF = "Pedal";
        labelH = "Heart";
        labelP = "Power";
        labelQ = "Quadra";
        labelS = "Speed";
        labelT = "Temps";
    }
    if(activity === "Run") {
        var thisDayWeight = fadData.Weight;
        var thisAvgHeartRate = (getSum(puHeart)/puHeart.length).toFixed(1);
    }
    var gpsTopDrop = "<div class='GPSTopDrop'><form id=DoGPSPointsForm'>";
    if(checkMobile()) { gpsTopDrop += ""; } else { gpsTopDrop += "<strong>Points: </strong>"; }
    gpsTopDrop += "<select id='GPSPointsDD' name='GPSPoints'><option value=''>Points...</option>";
    if(activity === "Cyc" && isSet(puCadence) && Math.max(puCadence) !== 0) { gpsTopDrop += "<option value='Cadence'>Cadence</option>"; }
    if(isSet(puAltitude) && Math.max(puAltitude) != 0) { gpsTopDrop += "<option value='Altitude'>Elevation</option>"; }
    if(isSet(puHeart) && Math.max(puHeart) !== 0) { gpsTopDrop += "<option value='Heart'>Heart Rate</option>"; }
    if(activity === "Cyc" && isSet(puPower) && Math.max(puPower) !== 0) { gpsTopDrop += "<option value='Power'>Power</option>"; }
    gpsTopDrop += "<option value='Speed'>Speed</option></select></form></div>";
    if(isSet(puCadence) && Math.max(puCadence) !== 0) {
        gpsInfoC += "<div class='GPSInfo'><div class='table'><div class='tr'>";
        var sampleCounter = 0;
        var gpsInfoCCols = [ "RPM", "This", "AVG", "MAX" ];
        gpsInfoC += "<span class='td'><div class='GPSPop'>" + labelC + "<br/>" +
                "<img class='" + gpsThumbSize + "' src='" + doCh("p", "GPSJCadence", "Thumb=1&ActDate=" + pParams.actDate) + "' />" +
                "<div class='" + gpsPopOClass + "'><h3>Cadence</h3>" +
                "<a href='" + doCh("p", "GPSJCadence", "ActDate=" + pParams.actDate) + "' target='Map'>" +
                "<img height='" + imgHeight + "' width='" + imgWidth + "' src='" + doCh("p", "GPSJCadence", "ActDate=" + pParams.actDate) + "' />" +
                "</a><br/><div class='table'>" +
                "<div class='tr'>";
        for(i = 0; i < gpsInfoCCols.length; i++) { gpsInfoC += "<span class='td'><strong>" + gpsInfoCCols[i] + "</strong></span>"; }
        gpsInfoC += "</div><div class='tr'><span class='td'><strong>Average</strong></span>" +
                "<span class='td'>" + (getSum(puCadence)/puCadence.length).toFixed(1) + "</span>" +
                "<span class='td'>" + (overallSensors.AvgCycCadAvg).toFixed(1) + "</span>" +
                "<span class='td'>" + (overallSensors.MaxCycCadAvg).toFixed(1) + "</span>" +
                "</div><div class='tr'><span class='td'><strong>Maximum</strong></span>" +
                "<span class='td'>" + (Math.max(puCadence)).toFixed(1) + "</span>" +
                "<span class='td'>" + (overallSensors.AvgCycCadMax).toFixed(1) + "</span>" +
                "<span class='td'>" + Math.round(overallSensors.MaxCycCadMax) + "</span>" +
                "</div></div></span>";
    }
    if(isSet(puAltitude) && Math.max(puCadence) !== 0) {
        gpsInfoE += "<span class='td'><div class='GPSPop'>" + labelE + "<br/>" +
                "<img class='" + gpsThumbSize + "' src='" + doCh("p", "GPSJElev", "Thumb=1&ActDate=" + pParams.actDate) + "' />" +
                "<div class='" + gpsPopOClass + "'><h3>Elevation</h3>" +
                "<a href='" + doCh("p", "GPSJElev", "ActDate=" + pParams.actDate) + "' target='Map'>" +
                "<img height='" + imgHeight + "' width='" + imgWidth + "' src='" + doCh("p", "GPSJElev", "ActDate=" + pParams.actDate) + "' />" +
                "</a><br/>" +
                "<strong>Maximum:</strong> " + Math.max(puAltitude) + " feet.<br/>" +
                "<strong>Minimum:</strong> " + Math.min(puAltitude) + " feet." +
                "</div></div></span>";
    }
    if(isSet(puHeart) && Math.max(puHeart) !== 0) {
        gpsInfoH += "<span class='td'><div class='GPSPop'>" + labelH + "<br/>" +
                "<img class='" + gpsThumbSize + "' src='" + doCh("p", "GPSJHeart", "Thumb=1&ActDate=" + pParams.actDate) + "' />" +
                "<div class='" + gpsPopOClass + "'><h3>Heart Rate</h3>" +
                "<a href='" + doCh("p", "GPSJHeart", "ActDate=" + pParams.actDate) + "' target='Map'>" +
                "<img height='" + imgHeight + "' width='" + imgWidth + "' src='" + doCh("p", "GPSJHeart", "ActDate=" + pParams.actDate) + "' />" +
                "</a><br/>" +
                "<strong>Average:</strong> " + (getSum(puHeart)/puHeart.length).toFixed(1) + " bpm.<br/>" +
                "<strong>Maximum:</strong> " + Math.max(puHeart) + " bpm." +
                "</div></div></span>";
    }           
    if(isSet(puPower) && Math.max(puPower) !== 0) {
        var gpsInfoPCols = [ "Watts", "This", "AVG", "MAX" ];
        gpsInfoP += "<span class='td'><div class='GPSPop'>" + labelP + "<br/>";
                "<img class='" + gpsThumbSize + "' src='" + doCh("p", "GPSJPower", "Thumb=1&ActDate=" + pParams.actDate) + "' />" +
                "<div class='" + gpsPopOClass + "'><h3>Power</h3>" +
                "<a href='" + doCh("p", "GPSJPower", "ActDate=" + pParams.actDate) + "' target='Map'>" +
                "<img height='" + imgHeight + "' width='" + imgWidth + "' src='" + doCh("p", "GPSJPower", "ActDate=" + pParams.actDate) + "' />" +
                "</a><br/><div class='table'><div class='tr'>";
        for(var i = 0; i < gpsInfoPCols.length; i++) { gpsInfoP += "<span class='td'><strong>" + gpsInfoPCols[i] + "</strong></span>"; }
        gpsInfoP += "</div><div class='tr'><span class='td'><strong>Average</strong></span>" +
                "<span class='td'>" + (getSum(puPower)/puPower.length).toFixed(1) + "</span>" +
                "<span class='td'>" + (overallSensors.AvgCycPowerAvg).toFixed(1) + "</span>" +
                "<span class='td'>" + (overallSensors.MaxCycPowerAvg).toFixed(1) + "</span>" +
                "</div><div class='tr'><span class='td'><strong>Maximum</strong></span>" +
                "<span class='td'>" + Math.max(puPower) + "</span>" +
                "<span class='td'>" + (overallSensors.AvgCycPowerMax).toFixed(1) + "</span>" +
                "<span class='td'>" + (overallSensors.MaxCycPowerMax).toFixed(1) + "</span>" +
                "</div></div></div></span>";
    }
    if(isSet(puCadence) && Math.max(puCadence) !== 0) {
        gpsInfoQ += "<span class='td'><div class='GPSPop'>" + labelQ + "<br/>" +
                "<img class='" + gpsThumbSize + "' src='" + doCh("p", "GPSJQuad", "Thumb=1&ActDate=" + pParams.actDate) + "' />" +
                "<div class='" + gpsPopOClass + "'><h3>Quadrant Analisys</h3>" +
                "<a href='" + doCh("p", "GPSJQuad", "ActDate=" + pParams.actDate) + "' target='Map'>" +
                "</a></div></div></span>";
    }
    if(isSet(puSpeed) && Math.max(puSpeed) !== 0) {
        var gpsInfoSCols = [ "MPH", "This" ];
        var gpsInfoSCCols = [ "AVG", "MAX" ];
        gpsInfoS += "<span class='td'><div class='GPSPop'>" + labelS + "<br/>" +
                "<img class='" + gpsThumbSize + "' src='" + doCh("p", "GPSJSpeed", "Thumb=1&ActDate=" + pParams.actDate) + "' />" +
                "<div class='" + gpsPopOClass + "'><h3>Speed</h3>" +
                "<a href='" + doCh("p", "GPSJSpeed", "ActDate=" + pParams.actDate) + "' target='Map'>" +
                "</a><br/><div class='table'><div class='tr'>";
        for(var i = 0; i < gpsInfoSCols.length; i++) { gpsInfoS += "<span class='td'><strong>" + gpsInfoSCols[i] + "</strong></span>"; }
        if(activity === "Cyc") {
            for(var i = 0; i < gpsInfoSCCols.length; i++) { gpsInfoS += "<span class='td'><strong>" + gpsInfoSCCols[i] + "</strong></span>"; }
        }
        gpsInfoS += "</div><div class='tr'><span class='td'><strong>Average</strong></span>" +
                "<span class='td'>" + (getSum(puSpeed)/puSpeed.length).toFixed(1) + "</span>";
        if(activity === "Cyc") {
            gpsInfoS += "<span class='td'>" + (overallSensors.AvgCycSpeedAvg).toFixed(1) + "</span>" +
                    "<span class='td'>" + (overallSensors.MaxCycSpeedAvg).toFixed(1) + "</span>";
        }
        gpsInfoS += "</div><div class='tr'><span class='td'><strong>Maximum</strong></span>" +
                "<span class='td'>" + (Math.max(puSpeed).toFixed(1)) + "</span>";
        if(activity === "Cyc") {
            gpsInfoS += "<span class='td'>" + (overallSensors.MaxCycSpeedAvg).toFixed(1) + "</span>" +
                    "<span class='td'>" + (overallSensors.MaxCycSpeedMax).toFixed(1) + "</span>";
        }
        gpsInfoS += "</div></div></div></div></span>";
    }
    if(isSet(puTemps) && Math.max(puTemps) !== 0) {
        gpsInfoT += "<span class='td'><div class='GPSPop'>" + labelT + "<br/>" +
                "<img class='" + gpsThumbSize + "' src='" + doCh("p", "GPSJTemp", "Thumb=1&ActDate=" + pParams.actDate) + "' />" +
                "<div class='" + gpsPopOClass + "'><h3>Temperature</h3>" +
                "<a href='" + doCh("p", "GPSJTemp", "ActDate=" + pParams.actDate) + "' target='Map'>" +
                "</a><br/><strong>Maximum: </strong>" + Math.max(puTemps) + "<br/>" +
                "<br/><strong>Minimum: </strong>" + Math.min(puTemps) + "</div></div></span>";
    }
    gpsTopDrop += gpsInfoC + gpsInfoE + gpsInfoH + gpsInfoP + gpsInfoQ + gpsInfoS + gpsInfoT + gpsInfoD + "</div></div>" +
            "<strong>" + activity + ", " + (Math.max(puDists)).toFixed(1) + " mi<br/>" +
            thisActivityTimeMinutes + " min,</strong>";
    if(activity === "Cyc") { 
        if(isSet(puPower) && Math.max(puPower) !== 0) {
            gpsTopDrop += "<strong>" + Math.round(((getSum(puPower)/puPower.length) * (Mathmax(puTimes)/60/60))*3.6) + " calories</strong><br/>";
        } else {
            gpsTopDrop += "<strong>Unknown calories</strong>";
        }
    }
    if(activity === "Run") {
        if(isSet(puHeart) && Math.max(puHeart) !== 0) {
            gpsTopDrop += Math.round(((myAge*0.2017)+(thisDayWeight*0.09036)+(thisAvgHr*0.6309)-55.0969)*thisActivityTimeMinutes/4.184) + " calories</strong><br/>";
        } else {
            gpsTopDrop += "<strong>Unknown calories</strong>";
        }
    }
    gpsTopDrop += "</div>";
    rData += gpsTopDrop;
    return rData;
}

function onRadarModelData(mrsLast, hours, xmlGeo) {
    var rData = "";
    var codRadarLink = "http://weather.cod.edu/satrad/nexrad/index.php?type=EAX-N0Q-0-6";
    var lastString = mrsLast.RunString;
    var runDateTime = ""; // create DateTime from format 'Ymd\_HT', lastStrin
    var radarDefMap = {
        "B": getBasePath("getOldGet") + "/Radar/EAX/_BLoop.gif",
        "V": getBasePath("getOldGet") + "/Radar/EAX/_VLoop.gif"
    };
    var wxDataTypeMap = {
            "CAPE": "CAPE/CIN",
            "JSWM": "Jet Stream Winds",
            "LI": "Lifted Index",
            "LLJM": "Low Level Jet",
            "PWAT": "Precipitable Water",
            "SfcE": "Surface Elevation",
            "SfcF": "Surface Feels Like",
            "SfcH": "Surface Humidity",
            "SfcP": "Surface Precipitation",
            "SfcT": "Surface Temperature",
            "SfcW": "Surface Winds",
            "WatT": "Water Temperature",
            "WatW": "Water Wave Height",
            "WatP": "Water Wave Period",
            "WxOb": "Weather Observed"
    };
    var topDrop = "<div class='GPSTopDrop'>" +
            "<form id='DoWxModelData'>" +
            "<select id='WxDataHourDrop' name='WxDataHour'>" +
            "<option value=''>Analysis</option>";
    hours.forEach(function (tFh) {
        var tFh2Pass = tFh.FHour + 2;
        var tValidTime = "" // clone runDateTime & modify + 2 hours
        var tValidTimeStr = ""; // tValidTime to String "Y-m-d H"
        topDrop += "<option value='" + mrsLast.RunString + "_" + tFh2Pass + "'>" + tValidTimeStr + " (+" + tFh2Pass + "h)</option>";
    });
    topDrop += "</select><br/>" +
            "<button name='SubmitModelQuery'>Go!</button>" +
            "<select id='WxDataTypeDrop' name='WxDataType'>";
    Object.keys(wxDataTypeMap).forEach(function (wxType) {
        topDrop += "<option value='" + wxType + "'>" + wxDataTypeMap[wxType] + "</option>";
    });
    topDrop += "</form></div>";
    var mapLowRight = "<div class='MapLowRight' style='background: black;'>More<br/>";
    Object.keys(radarDefMap).forEach(function (radar) {
        mapLowRight += "<a href='" + getBasePath("old") + "/OutMap.php?RadarMode=" + radar + "'>" +
                "<img class='th_small' src='" + radarDefMap[radar] + "'/></a>";
    })
    mapLowRight += "<br/><a href='" + codRadarLink + "' target='newRadar'>CoD Radar</a><br/>" +
            "<a href='" + getBasePath("ui") + "/Cams.jsp' target='newRadar'>Home Cams</a>" +
            "<br/>Leaflet Attributions</div>";
    var mapLowLeft = "<div class='MapLowLeftPop'><img src='" + getBasePath("icon") + "/ic_map.jpeg' class='th_icon'>Search data..." +
            "<div class='MapLowLeftPopO'>" +
            "<form id='MapLowLeftSearchForm'>" +
            "<table><thead><th align='center' colspan=2>Data Range -" +
            " <a href='" + getBasePath("old") + "/WxStation.php' target='new'>" + /* stations.length */ + "<br/>";
    // ^ LEFT OFF HERE ON 4/3/18!
    rData += topDrop + mapLowRight;
    return rData;
}