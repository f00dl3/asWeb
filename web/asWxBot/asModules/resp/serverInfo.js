/* 
by Anthony Stump
Created: 7 Mar 2020
Updated: on creation
 */

const axios = require('axios');
const aLog = require('./../accessLog.js');
const asm = require('./../commmon.js');

module.exports = {

		getServerInfo: function(msg) { getServerInfo(msg) }

}

function getServerInfo(msg) {

	var commandRan = "getServerInfo(msg)";
	aLog.basicAccessLog(msg, commandRan, "start");
	
	var rData = "TODO: BUILD SNMP DATA QUERY NON-CHART SPECIFIC!";
	var url = webUiBase + "SNMP";
	var pData = "doWhat=getMainRecent";

	axios.post(url, pData).then((res) => { 
		rData = res.data,
		respondServerInfo(msg, rData)
	}).catch((error) => {
		console.log(error)
	});

	aLog.basicAccessLog(msg, commandRan, "stop");
		
}

function respondServerInfo(msg, dataIn) {
	
	console.log(dataIn[0]);
	var td = dataIn[0];
	var xj = td.dtExpandedJSONData;
	
	var cpuAverage = ((
			td.dtCPULoad1 +
			td.dtCPULoad2 +  
			td.dtCPULoad3 + 
			td.dtCPULoad4 + 
			td.dtCPULoad5 + 
			td.dtCPULoad6 + 
			td.dtCPULoad7 + 
			td.dtCPULoad8) / 8).toFixed(2);
	
	//var memUse = ();
	var networkUse = (td.dtOctestIn + td.dtOctetsOut);
	var dbRows = (td.dtMySQLRowsCore + td.dtMySQLRowsFeeds + td.dtMySQLRowsWxObs + td.dtMySQLRowsNetSNMP);	
	var linesCode = (
			xj.LOC_aswjCss + 
			xj.LOC_asUtilsJava + 
			xj.LOC_aswjJava + 
			xj.LOC_aswjJs +
			xj.LOC_aswjJsp); 
	
	msg.reply("Latest Server Info:");
	msg.reply("\nData Timestamp:" + td.WalkTime);
	msg.reply("\nCPU Utilization: " + cpuAverage + "%");
	msg.reply("\nLoad Index (1 min): " + td.dtLoadIndex1);
	/* msg.reply("\nDisk Use 1: " + asm.autoUnits(td.dtK4RootU) + "/" + asm.autoUnits(td.dtK4Root) +
			" (" + (td.dtK4RootU/td.dtK4Root) + "%)");
	msg.reply("\nDisk Use 2: " + asm.autoUnits(xj.dtK4Extra1U) + "/" + asm.autoUnits(xj.dtK4Extra1) +
			" (" + (xj.dtK4Extra1U/xj.dtK4Extra1) + "%)"); */
	msg.reply("\nNetwork Counters: " + asm.autoUnits(networkUse));
	msg.reply("\nUPS Stats: " + td.dtUPSLoad + "% (" + td.dtUPSTimeLeft + " mins)");
	msg.reply("\nDatabase Rows: " + asm.autoUnits(dbRows));
	msg.reply("\nLines of Code: " + asm.autoUnits(linesCode));
		
}
