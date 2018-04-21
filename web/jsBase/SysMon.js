/* 
by Anthony Stump
Created: 20 Apr 2018
*/

var lastWalks;

function actOnAlarmFilterSelect(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    var thisFormDataJ = dojo.formToJson(this.form);
    window.alert(thisFormDataJ);
    //post alarm filter, return data to div
}

function alarmSeverityButton(bgColor, textColor, text) {
    return "<button class='UButton' style='width: 60px; background-color: " + bgColor + "; color: " + textColor + ";'>" + text + "</button>"
}

function getSnmpOverviewData() {
    //.then
    //lastWalks = lastWalks;
    //populateStatusHolder(indoorTemp, garageTemp);
}

function nodeState(state, label) {
    var btn = "";
    switch(state) {
        case "on": btn = "<button class='UButton' style='background-color: yellow;'>" + label + "</button>";
        case "off": btn = "<button class='UButton'>" + label + "</button>";
    }
    return btn;
}

function onCheck(node, state) {
    // if time stamp less than 2 minutes then node = on, else node is off.
    switch(node) {
        case "Main": nodeState(state, "D"); break;
        case "Router": nodeState(state, "R"); break;
        case "Pi": nodeState(state, "I"); break;
        case "Pi2": nodeState(state, "J"); break;
        case "Phone": nodeState(state, "P"); break;
        case "PhoneE": nodeState(state, "E"); break;
    }
}

function populateAlarmTable(alarmData) {
    var cols = [ "Time", "Severity", "Action", "Status", "Ticket", "Host", "Alarm Text" ];
    var rData = "<strong>Alarm List View:</strong><br/><div class='table'><div class='tr'>";
    for (var i = 0; i < cols.length; i++) { rData += "<span class='td'><strong>" + cols[i] + "</strong></span>"; }
    rData += "</div>";
    alarmData.forEach(function (alarm) {
        var asButton, alarmText;
        switch(alarm.severity) {
            case "0": asButton = alarmSeverityButton("grey", "white", "Critical"); break;
            case "1": asButton = alarmSeverityButton("red", "white", "Major"); break;
            case "2": asButton = alarmSeverityButton("orange", "black", "Major"); break;
            case "3": asButton = alarmSeveirtyButton("yellow", "black", "Minor"); break;
            case "4": asButton = alarmSeverityButton("lightblue", "black", "FYI"); break;
            default: asButton = alarmSeverityButton("lightblue", "black", "UNK"); break;
        }
        if(isSet(alarm.shortAlarmText)) { alarmText = alarm.shortAlarmText; } else { alarmText = alarm.alarmText; }
        /* LEFT OFF HERE 4/20/18 -- will need to rebuild Alarm fetcher too! */
    });
    dojo.byId("AlarmTableHolder").innerHTML = rData;
}

function populateCharts() {
    // stepIn = get Step post var
    // dateIn = get Date post var
    var chartDefs = "Step=" + stepIn +
            "&Date=" + dateIn;
    var rData = "";
    var chartList1 = [
        "mSysLoad",
        "mSysCPU",
        "mSysTemp",
        "mSysMemory",
        "mSysStorage",
        "mSysDiskIO",
        "mSysNet",
        "mSysMySQLSize",
        "mSysNumUsers",
        "mSysFans",
        "mSysUPSLoad",
        "mSysUPSTimeLeft",
        "mSysTomcatDeploy"
    ];
    var chartList2 = [
        "mSysVolt",
        "mCellBattCPU",
        "mCellTempRapid",
        "mCellNet",
        "mCellSig",
        "mRouterCPU",
        "mRouterNet",
        "mPiAmp",
        "mPiCPU",
        "mPiMemory",
        "mPiTemp"
    ];
    var numElements = chartList1.length;
    var numElements2 = chartList2.length;
    if(checkMobile()) {
        for (var i = 0; i < numElements.length; i++) {
            rData += "<a href='" + doCh("p", chartList1[i], chartDefs) + "' target='nChart" + i + "'>" +
                    "<img class='th_small' src='" + doCh("p", chartList1[i], "Thumb=1&" + chartDefs) + "'/></a>";
        }
        for (var j = 0; j < numElements2.length; j++) {
            rData += "<a href='" + doCh("p", chartList2[j], chartDefs) + "' target='nChart" + i + "'>" +
                    "<img class='th_small' src='" + doCh("p", chartList2[j], "Thumb=1&" + chartDefs) + "'/></a>";
        }
        rData += "<a href='" + getBasePath("old") + "/OutMap.php?RadarMode=B' target='nChartR'>" +
                "<img class='th_small' src='" + getBasePath("getOldGet") + "/Radar/EAX/_BLoop.gif'/></a>" +
                "<a href='" + getResource("Cams") + "' target='nChartC'>" +
                "<img class='th_small' src='" + getBasePath("getOldGet") + "/Cams/_Latest.jpeg'/></a>";
    } else {
        var elementList1 = [];
        var elementList2 = [];
        for (var i = 0; i < numElements.length; i++) {
            var thisElement = "<a styleReplace href='" + doCh("p", chartList1[i], chartDefs) + "' target='nChart" + i + "'>" +
                    "<img class='th_small' src='" + doCh("p", chartList1[i], "Thumb=1&" + chartDefs) + "'/></a>";
            elementList1.push(thisElement);
        }
        for (var j = 0; j < numElements2.length; j++) {
            var thisElement = "<a styleReplace href='" + doCh("p", chartList2[j], chartDefs) + "' target='nChart" + i + "'>" +
                    "<img class='th_small' src='" + doCh("p", chartList2[j], "Thumb=1&" + chartDefs) + "'/></a>";
            elementList2.push(thisElement);
        }
        var elementPopper1 = "<a styleReplace href='" + getBasePath("old") + "/OutMap.php?RadarMode=B' target='nChartR'>" +
                "<img class='th_small' src='" + getBasePath("getOldGet") + "/Radar/EAX/_BLoop.gif'/></a>";
        var elementPopper2 = "<a styleReplace href='" + getResource("Cams") + "' target='nChartC'>" +
                "<img class='th_small' src='" + getBasePath("getOldGet") + "/Cams/_Latest.jpeg'/></a>";
        elementList2
            .push(elementPopper1)
            .push(elementPopper2);
        rData += "<p>" + imageLinks3d(elementList1, 25, 100, 2.15) + "<p>" +
                "<div style='min-height: 64px;'></div>" + imageLinks3d(elementList2, 25, 100, 2.15);
    }
}

function populateEDiscovery(obsIndoor) {
    var rData = "<strong>SSH Client</strong>: " + obsIndoor.SSHClientIP + "<p>";
    dojo.byId("eDiscoveryHolder").innerHTML = rData;
}

function populateReliaStump() {
    var rData = "<tt>IN DEVELOPMENT...</tt><p>" +
            "<form id='AlarmFilterForm'>" +
            "<select id='AlarmFilter' name='AlarmFilter'>" +
            "<option value=''>Select...</option>" +        
            "<option value='Active'>Active</option>" +
            "<option value='All'>All</option>" +
            "</select><p>" +
            "<div id='AlarmTableHolder'></div>";
    dojo.byId("ReliaStumpHolder").innerHTML = rData;
    var alarmFilterSelector = dojo.byId("AlarmFilter");
    dojo.connect(alarmFilterSelector, "change", actOnAlarmFilterSelect);
}

function populateStatusHolder(indoorTemp, garageTemp) {
    var intervals = {
        "1": "2 min* (Day)",
        "2": "4 min",
        "7": "14 min",
        "15": "30 min",
        "30": "1 hr (Month)",
        "90": "3 hr",
        "180": "6 hr",
        "360": "12 hr (Year)",
        "680": "1 day",
        "4760": "1 week"
    };
    var nodes = [ "Main", "Router", "Pi", "Pi2", "Phone", "PhoneE" ];
    var rData = "<div class='UPopNM'>" +
            "<button style='" + styleTemp(indoorTemp) + "'>" + indoorTemp + "F</button>" +
            "<button style='" + styleTemp(garageTemp) + "'>" + garageTemp + "G</button>" +
            "<div class='UPopNMO'><div class='tr'><span class='td'>Interval</span>" +
            "<span class='td'><form id='StepForm'>" +
            "<select name='chStep' id='chartStepForm'><option value='1'>Select...</option>";
    Object(intervals).keys.forEach(function (int) { rData += "<option value='" + int + "'>" + intervals["int"] + "</option>"; });
    rData += "</select></form></div>" +
            "<div class='tr'>" +
            "<span class='td'>Date</span>" +
            "<span class='td'>" +
            "<form id='chDateForm'>" +
            "<input name='chDate' type='date' style='width: 75px;'/>" +
            "</form></span></div></div><br/>" +
            "<a href='" + getBasePath("ui") + "/Images/Topology.jpg'>Topology Map</a><br/>" +
            "<a href='" + getBasePath("old") + "/OutMap.php?PhoneTrack=Note3'>Phone: Note 3</a><br/>" +
            "<a href='" + getBasePath("old") + "/OutMap.php?PhoneTrack=Note3Rapid'>Phone: Note 3 (Rapid!)</a><br/>" +
            "<a href='" + getBasePath("old") + "/OutMap.php?PhoneTrack=EmS4'>Phone: Emily S4</a><br/>" +
            "<a href='" + getBasePath("old") + "/OutMap.php?PhoneTrack=RasPi2'>Raspberry Pi 2</a>" +
            "</div></div>";
    for (var i = 0; i < nodes.length; i++) { rData += onCheck(nodes[i]); }
    dojo.byId("snmpStatusHolder").innerHTML = rData;
}

function initSysMon() {
    
};

dojo.ready(initSysMon);