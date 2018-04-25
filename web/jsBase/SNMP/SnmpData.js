/* 
by Anthony Stump
Created: 30 Mar 2018
Updated: 25 Apr 2018
 */

function checkIfSnmpIsUp(state) {
    switch(state) {
        case true: doSnmpWidget(); break;
        case false: break;
    }
}

function processSnmpData(snmp, target) {
    var cpuAvgLoad = ((snmp.cpuLoad1 + snmp.cpuLoad2 + snmp.cpuLoad3 + snmp.cpuLoad4 + 
            snmp.cpuLoad5 + snmp.cpuLoad6 + snmp.cpuLoad7 + snmp.cpuLoad8) / 8);
    var svgMult = 2;
    if(checkMobile()) { svgMult = 1; }
    var rData = "",
        loadCond = "";
    var runningProcesses = snmp.runningProcs;
    var usedMemory = snmp.memPhysUsed - (snmp.memBuffUsed + snmp.memCachUsed);
    var eUsedMemory = usedMemory + snmp.memCachUsed;
    var usedStorage = ((snmp.hdd0Used * 4096) / 1024 / 1024 / 1024);
    var usedStorage2 = ((snmp.hdd1Used * 4096) / 1024 / 1024 / 1024);
    var usedStorageComb = usedStorage + usedStorage2;
    if(isSet(snmp.diskIoTx) && isSet(snmp.diskIoRx)) {
        
    }
    var avgCPUColor = autoColorScale(cpuAvgLoad, 100, 0, null);
    var procsColor = autoColorScale(runningProcesses, 500, 0, null);
    var diskSpanColor = autoColorScale((usedStorage/1700)*100, 100, 0, null);
    var disk2SpanColor = autoColorScale((usedStorage2/4400)*100, 100, 0, null);
    //var diskIOColor = autoColorScale(thisDiskIO, 200000, 0, null);
    //var loadIndexColor = autoColorScale(loadIndex, 8, 0, null);
    var memChartColor = autoColorScale(usedMemory, snmp.memPhysSize, 0, null);
    if (snmp.loadIndex >= 4) { loadCond += " Load @ " + loadCond; }
    rData += "Uptime: " + snmp.Uptime +
            " / Amb: <span style='" + styleTemp(snmp.tempCase) + "'>" + snmp.tempCase + "F</span>";
    dojo.byId(target).innerHTML = rData;
}

function snmpRapid(target) {
    aniPreload("on");
    var thePostData = { "doWhat": "RapidSNMP" };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("SNMP"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    processSnmpData(data, target);
                    aniPreload("off");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for Rapid SNMP Index FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}