/* 
by Anthony Stump
Created: 2 Apr 2018
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
    }
    
}

function onFadGpsJson(pParams, fadData, overallStats, gpsJsonPu) {
    var activity = pParams.activity;
    var rData = "";
    var puAltitude, puCadence, puDists, puHeart, puPower, puSpeed, puTemps, puTimes;
    var gpsThumbSize, gpsPopScale, gpsPopOClass, labelC, labelE, labelF, labelH, labelP, labelQ, labelS, labelT;
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
    gpsTopDrop += "<option value='Altitude'>Elevation</option>";
    if(isSet(puHeart) && Math.max(puHeart) !== 0) { gpsTopDrop += "<option value='Heart'>Heart Rate</option>"; }
    if(activity === "Cyc" && isSet(puPower) && Math.max(puPower) !== 0) { gpsTopDrop += "<option value='Power'>Power</option>"; }
    gpsTopDrop += "<option value='Speed'>Speed</option></select></form></div>";
    var gpsInfo = "<div class='GPSInfo'><div class='table'><div class='tr'>";
    if(isSet(puCadence) && Math.max(puCadence) !== 0) {
        var sampleCounter = 0;
        gpsInfo += "<span class='td'><div class='GPSPop'>" + labelC + "<br/>" +
                "<img class='" + gpsThumbSize + "' src='" + doCh("p", "GPSJCadence", "Thumb=1&ActDate=" + pParams.actDate) + "' />" +
                "<div class='" + gpsPopOClass + "'><h3>Cadence</h3>" +
                "<a href='" + gpsThumbSize + "' src='" + doCh("p", "GPSJCadence", "ActDate=" + pParams.actDate) + "' target='Map'>" +
                "<img height='" + imgHeight + "' width='" + imgWidth + "' src='" + doCh("p", "GPSJCadence", "ActDate=" + pParams.actDate) + "' />" +
                "</a><br/><div class='table'>" +
                "<div class='tr'>" +
                "<span class='td'><em>RPM</em></span>";
                   // ^ LEFT OFF HERE!
    }
    rData += gpsTopDrop;
    return rData;
    
}