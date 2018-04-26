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
    var cpuLoads = [
        snmp.cpu1Load,
        snmp.cpu2Load,
        snmp.cpu3Load,
        snmp.cpu4Load,
        snmp.cpu5Load,
        snmp.cpu6Load,
        snmp.cpu7Load,
        snmp.cpu8Load
    ];
    var cumCpuLoad = 0;
    for (var i = 0; i < cpuLoads.length; i++) {
        cumCpuLoad += cpuLoads[i];
    }
    var cpuAvgLoad = (cumCpuLoad/cpuLoads.length).toFixed(1);
    var svgMult = 2;
    if(checkMobile()) { svgMult = 1; }
    var rData = "",
        loadCond = "";
    var runningProcesses = snmp.runningProcs;
    var usedMemory = snmp.memPhysUsed - (snmp.memBuffUsed + snmp.memCachUsed);
    var eUsedMemory = usedMemory + snmp.memCachUsed;
    var memPhysSize = snmp.memPhysSize;
    var usedStorage = ((snmp.hdd0Used * 4096) / 1024 / 1024 / 1024);
    var usedStorage2 = ((snmp.hdd1Used * 4096) / 1024 / 1024 / 1024);
    var usedStorageComb = usedStorage + usedStorage2;
    var loadIndex = snmp.loadIndex;
    if(isSet(snmp.diskIoTx) && isSet(snmp.diskIoRx)) {
        // build
    }
    var avgCPUColor = autoColorScale(cpuAvgLoad, 100, 0, null);
    var procsColor = autoColorScale(runningProcesses, 500, 0, null);
    var diskSpanColor = autoColorScale((usedStorage/1700)*100, 100, 0, null);
    var disk2SpanColor = autoColorScale((usedStorage2/4400)*100, 100, 0, null);
    //var diskIOColor = autoColorScale(thisDiskIO, 200000, 0, null);
    var loadIndexColor = autoColorScale(loadIndex, 8, 0, null);
    var memChartColor = autoColorScale(usedMemory, memPhysSize, 0, null);
    var tempCase = (0.93*(conv2Tf(snmp.tempCase/1000))).toFixed(1);
    var tempCPU = (0.93*(conv2Tf(snmp.tempCPU/1000))).toFixed(1);
    var thisDiskIO = "NA";
    if (snmp.loadIndex >= 4) { loadCond += " Load @ " + loadCond; }
    rData += "Uptime: " + snmp.uptime +
            " / Amb: <span style='" + styleTemp(tempCase) + "'>" + tempCase + "F</span>" +
            " / CPU: <span style='" + styleTemp(tempCPU) + "'>" + tempCPU + "F</span>" +
            "<br/>" +
            "CPU --" +
            " Avg: <span style='background-color: " + avgCPUColor + "; color: " + autoFontScale(cpuAvgLoad) + ";'>" + cpuAvgLoad + "%</span> " +
            " / Load: <span style='background-color: " + loadIndexColor + "; color: " + autoFontScale((loadIndex/8)*100) + ";'>" + loadIndex + "</span>" +
            " / Procs: <span style='background-color: " + procsColor + "; color: " + autoFontScale(runningProcesses) + ";'>" + runningProcesses + "</span><br/><p>";
    for(var i = 0; i < cpuLoads.length; i++) {
        var thisCPULoad = cpuLoads[i];
        var thisColor = autoColorScale(thisCPULoad, 100, 0, null);
        var thisBorder = "2px solid #333333";
        if(thisCPULoad >= 45) { thisBorder = "2px solid red"; }
        rData += "<svg width='" + (svgMult*37) + "' style='border: " + thisBorder + "; padding: 1px; background-color: #666666;' height='16'><g>" +
                "<rect x='0' y='0' width='" + ((37*svgMult)*(thisCPULoad/100)) + "' height='16' style='fill: " + thisColor + ";'/>" +
                "<text x='0' y='10' fill='" + autoFontScale(thisCPULoad) + "' alignment-baseline='middle'>" + thisCPULoad + "%</text>" +
                "</g></svg>";
    }
    rData += "<br/>" +
            "<svg width='" + (svgMult*75) + "' style='border: 1px solid #333333; padding: 1px; background-color: #666666;' height='16'><g>" +
            "<rect x='0' y='0' width='" + ((75*svgMult)*(usedMemory/memPhysSize)) + "' height='16' fill: " + memChartColor + ";'/>" +
            "<text x='0' y='10' fill='" + autoFontScale((usedMemory/memPhysSize)*100) + "' alignment-baseline='middle'>" + "Mem: " + Math.round(usedMemory/1024/1024) + "G/" + Math.round(eUsedMemory/1024/1024) + "G</text>" +
            "</g></svg>" +
            "<svg width='" + (svgMult*75) + "' style='border: 1px solid #333333; padding: 1px; background-color: #666666;' height='16'><g>" +
            "<rect x='0' y='0' width='" + ((75*svgMult)*(usedStorage/1700)) + "' height='16' style='fill: " + diskSpanColor + ";'/>" +
            "<text x='0' y='10' fill='" + autoFontScale((usedStorage/1700)*100) + "' alignment-baseline='middle'>" + Math.round(usedStorage) + "G/" + autoUnits(thisDiskIO) + "</text>" +
            "</g></svg>" +
            "<svg width='" + (svgMult*75) + "' style='border: 1px solid #333333; padding: 1px; background-color: #666666;' height='16'><g>" +
            "<rect x='0' y='0' width='" + ((75*svgMult)*(usedStorage2/4400)) + "' height='16' style='fill: " + disk2SpanColor + ";'/>" +
            "<text x='0' y='10' fill='" + autoFontScale((usedStorage2/4400)*100) + "' alignment-baseline='middle'>" + Math.round(usedStorage2) + "G</text>" +
            "</g></svg>";
    // Build network part!
    dojo.byId(target).innerHTML = rData;
}

function snmpRapid(target) {
    var timeout = 500;
    if(checkMobile()) { timeout = 1500; }
    setInterval(function () {
        var thePostData = { "doWhat": "RapidSNMP" };
        require(["dojo/request"], function(request) {
            request
                .post(getResource("SNMP"), {
                    data: thePostData,
                    handleAs: "json"
                }).then(
                    function(data) {
                        processSnmpData(data, target);
                    },
                    function(error) { 
                        console.log("request for Rapid SNMP FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                    });
        });
    }, timeout);
}