/* 
by Anthony Stump
Created: 30 Mar 2018
Updated: 26 Apr 2018
 */

var testCounter = 0;
var diskIoLastRead = 0;
var diskIoLastWrite = 0;
var eth0LastIn = 0;
var eth0LastOut = 0;
var lastNotableEthUse = 0;

function checkIfSnmpIsUp(state) {
    switch(state) {
        case true: doSnmpWidget(); break;
        case false: break;
    }
}

function processSnmpData(snmp, target) {
    var checkInt = 0.5;
    if(checkMobile()) { checkInt = 1; }
    testCounter++;
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
    var thisDiskIoRead = snmp.diskIoRx;
    var thisDiskIoWrite = snmp.diskIoTx;
    var thisDiskIoReadDiff = 0;
    var thisDiskIoWriteDiff = 0;
    if(isSet(diskIoLastRead)) { thisDiskIoReadDiff = thisDiskIoRead - diskIoLastRead; }
    if(isSet(diskIoLastWrite)) { thisDiskIoWriteDiff = thisDiskIoWrite - diskIoLastWrite; }
    diskIoLastRead = thisDiskIoRead;
    diskIoLastWrite = thisDiskIoWrite;
    var thisDiskIo = (thisDiskIoReadDiff + thisDiskIoWriteDiff)/checkInt;
    var avgCPUColor = autoColorScale(cpuAvgLoad, 100, 0, null);
    var procsColor = autoColorScale(runningProcesses, 500, 0, null);
    var diskSpanColor = autoColorScale((usedStorage/1700)*100, 100, 0, null);
    var disk2SpanColor = autoColorScale((usedStorage2/4400)*100, 100, 0, null);
    var diskIOColor = autoColorScale(thisDiskIo, 200000, 0, null);
    var loadIndexColor = autoColorScale(loadIndex, 8, 0, null);
    var memChartColor = autoColorScale(usedMemory, memPhysSize, 0, null);
    var tempCase = (0.93*(conv2Tf(snmp.tempCase/1000))).toFixed(1);
    var tempCPU = (0.93*(conv2Tf(snmp.tempCPU/1000))).toFixed(1);
    var diskPopIn = " ";
    if(thisDiskIo !== 0) { diskPopIn = " +" + autoUnits(thisDiskIo); }
    if (snmp.loadIndex >= 4) { loadCond += " Load @ " + loadCond; }
    rData += "Uptime: " + snmp.uptime +
            " / Amb: <span style='" + styleTemp(tempCase) + "'>" + Math.round(tempCase) + "F</span>" +
            " / CPU: <span style='" + styleTemp(tempCPU) + "'>" + Math.round(tempCPU) + "F</span>" +
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
            "<rect x='0' y='0' width='" + ((75*svgMult)*(usedMemory/memPhysSize)) + "' height='16' style='fill: " + memChartColor + ";'/>" +
            "<text x='0' y='10' fill='" + autoFontScale((usedMemory/memPhysSize)*100) + "' alignment-baseline='middle'>" + "Mem: " + Math.round(usedMemory/1024/1024) + "G/" + Math.round(eUsedMemory/1024/1024) + "G</text>" +
            "</g></svg>" +
            "<svg width='" + (svgMult*75) + "' style='border: 1px solid #333333; padding: 1px; background-color: #666666;' height='16'><g>" +
            "<rect x='0' y='0' width='" + ((75*svgMult)*(usedStorage/1700)) + "' height='16' style='fill: " + diskSpanColor + ";'/>" +
            "<text x='0' y='10' fill='" + autoFontScale((usedStorage/1700)*100) + "' alignment-baseline='middle'>" + Math.round(usedStorage) + "G" + diskPopIn + "</text>" +
            "</g></svg>" +
            "<svg width='" + (svgMult*75) + "' style='border: 1px solid #333333; padding: 1px; background-color: #666666;' height='16'><g>" +
            "<rect x='0' y='0' width='" + ((75*svgMult)*(usedStorage2/4400)) + "' height='16' style='fill: " + disk2SpanColor + ";'/>" +
            "<text x='0' y='10' fill='" + autoFontScale((usedStorage2/4400)*100) + "' alignment-baseline='middle'>" + Math.round(usedStorage2) + "G</text>" +
            "</g></svg>";
    var thisEth0In = snmp.eth0In;
    var thisEth0Out = snmp.eth0Out;
    var thisEth0InDiff = 0;
    var thisEth0OutDiff = 0;
    if(eth0LastIn !== 0) { thisEth0InDiff = thisEth0In - eth0LastIn; }
    if(eth0LastOut !== 0) { thisEth0OutDiff = thisEth0Out - eth0LastOut; }
    eth0LastIn = thisEth0In;
    eth0LastOut = thisEth0Out;
    var thisEth0IntDiff = (thisEth0OutDiff + thisEth0InDiff)/checkInt;
    if(thisEth0IntDiff !== 0) { lastNotableEthUse = thisEth0IntDiff; }
    var xBps = autoUnits(lastNotableEthUse);
    var eth0Use = (lastNotableEthUse/1000000000)*(svgMult*100);
    var eth0Color = autoColorScale(eth0Use, 100, 0, null);
    rData += "<svg width='" + (svgMult*75) + "' style='border: 1px solid #333333; padding: 1px; background-color: #666666;' height='16'><g>" +
            "<rect x='0' y='0' width='" + eth0Use + "' height='16' style='fill: " + eth0Color + ";'/>" +
            "<text x='0' y='10' fill='" + autoFontScale(eth0Use) + "' alignment-baseline='middle'>e0: " + xBps + "b</text>" +
            "</g></svg><br/>" +
            "<strong>SNMPv3 Poll Counter: " + testCounter;
    dojo.byId(target).innerHTML = rData;
}

function snmpRapid(target) {
    var timeout = 500;
    if(checkMobile()) { timeout = 1500; }
    var thePostData = { "doWhat": "snmpWalk" };
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
    setTimeout(function () { snmpRapid(target); }, timeout);
}