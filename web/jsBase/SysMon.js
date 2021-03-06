/* 
by Anthony Stump
Created: 20 Apr 2018
Updated: 6 Feb 2021
*/

var chartArray;
var dateIn;
var iconClass = "rThumbMedium";
if(checkMobile()) { iconClass = "rThumbSmall"; }
var lastWalks;
var stepIn;

if(!isSet(stepIn)) { stepIn = 1; }
if(!isSet(dateIn)) { dateIn = getDate("day", 0, "yyyyMMdd"); }

function actOnChartStepSelect(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    stepIn = thisFormData.chStep;
    getCharts(chartArray, stepIn, dateIn);
}

function actOnChartDateSelect(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    dateIn = thisFormData.chDate;
    getCharts(chartArray, stepIn, dateIn);
}

function getCharts(chartArray, timestamp) {
    aniPreload("on");
    var thePostData = {
        "doWhat": "SysMonCharts",
        "step": stepIn,
        "date": dateIn
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Chart"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    getEDiscovery("eDiscoveryHolder");
                    populateChartHolders(chartArray, timestamp);
                    aniPreload("off");
                },
                function(error) { 
                    aniPreload("off");
                    console.log("request for SysMonCharts FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
    for(var i = 0; i < chartArray.length; i++) {
        dojo.byId("CHART_" + chartArray[i]).innerHTML = chartArray[i] + " ";
    }
}

function getEDiscovery(target) {
    var timeout = 90*1000;
    var thePostData = { "doWhat": "getLastSSH" };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("SNMP"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    populateEDiscovery(data);
                },
                function(error) { 
                    console.log("request for LastSSH FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
    setTimeout(function () { getEDiscovery("eDiscoveryHolder"); }, timeout);
}

function getLastWalk(target) {
    var timeout = 90*1000;
    var thePostData = { "doWhat": "getLastWalk" };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("SNMP"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    populateStatusHolder(target, data);
                },
                function(error) { 
                    console.log("request for LastWalk FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
    setTimeout(function () { getLastWalk("snmpStatusHolder"); }, timeout);
}

function getLiveRowCount(target) {
    aniPreload("on");
    var thePostData = { "doWhat": "getLiveRowCount" };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("DBInfo"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    aniPreload("off");
                    populateLiveDatabaseHolder(target, data);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for LiveRowCount FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                }
            );
    });
    setTimeout(function () { getLiveRowCount(target); }, getRefresh("short"));
}

function nodeState(state, label) {
    var btn = "";
    switch(state) {
        case "on": btn = "<button style='background-color: yellow; color: black;'>" + label + "</button> "; break;
        case "off": btn = "<button style='background-color: #666666; color: white;'>" + label + "</button> "; break;
    }
    return btn;
}

function onCheck(timestamp, node) {
    var staleTime = getDate("minute", -5, "timestamp"); 
    var state = "off";
    if(isSet(timestamp) && staleTime.valueOf() <= timestamp.valueOf()) { state = "on"; }
    switch(node) {
        case "Main": return nodeState(state, "D"); break;
        case "Router": return nodeState(state, "R"); break;
        case "Pi": return nodeState(state, "I"); break;
        case "Pi2": return nodeState(state, "J"); break;
        case "Phone": return nodeState(state, "P"); break;
        case "PhoneE": return nodeState(state, "E"); break;
        case "UVM": return nodeState(state, "S"); break;
    }
}

function populateChartHolders(chartArray, timestamp) {
    for(var i = 0; i < chartArray.length; i++) {
        var thisChartObject = "<a href='" + getBasePath("chartCache") + "/" + chartArray[i] + ".png' target='xChart'>" +
	        "<img class='" + iconClass + "' src='" + getBasePath("chartCache") + "/th_" + chartArray[i] + ".png?ts=" + timestamp + "'/>" +
	        "</a>";
        dojo.byId("CHART_"+chartArray[i]).innerHTML = thisChartObject;
    }
}

function populateCharts() {
    var timestamp = getDate("day", 0, "timestamp");
    var rData = "<div class='rWrapper'>"; // implement fContainer class
    var chartList1 = [
        //"mSysLoad", // done 4/29/18
        //"mSysCPU", // done 4/29/18
        //"mSysTemp", // done 4/29/18
        //"mSysMemory", // done 4/29/18
        //"mSysStorage", // done 4/29/18
        //"mSysDiskIO", // done 4/29/18 <<<< not converted over, not working 12/22/20
        //"mSysNet", // done 4/29/18
        //"mSysMySQLSize", // done 5/13/18
        //"mSysNumUsers", // done 4/29/18
        //"mSysFans", // done 4/30/18
        "mSysUPSLoad", // done 4/30/18
        "mSysUPSTimeLeft", // done 4/30/18
        "mSysTomcatDeploy", // issues 4/30/18
        "mSysVolt", // done 4/30/18
        "mCellBattCPU", // done 5/13/18
        "mCellTemp", // done 5/13/18
        "mCellNet", // done 4/30/18
        "mCellSig", // done 4/30/18
    ];
    var chartList2 = [
        "mRouterCPU", // done 4/30/18
        "mRouterNet", // data issues 4/30/18
        "mRouterMemory", // data issues 4/30/10
        "mPiCPU", // done 4/30/18
        "mPiLoad", // done 4/30/18
        "mPiMemory", // axis label issues 4/30/18
        "mPiTemp", // done 4/30/18
        "mPi2CPU", // done 4/30/18
        "mPi2Memory", // data issues 4/30/18
        "mPi2Temp", // data issues 4/30/18
        "mPi2Load", // done 4/30/18
        "mPi2Light", // data issues 4/30/18
        "mJavaCodeLines", // done 4/29/18
        "mSysNvUtilization", // new 10/16/18
        "mUvm2CPU", //new 5/19/19
        "mUvm2Memory", //new 5/19/19
        "mUvm2Load" //new 5/19/19
    ];
    var UNIMP_chartList = [
        "mPiAmb", // done 4/30/18 - decommed 5/19/19
        "mCellTempRapid", // json iteration not known 4/30/18
        "mPi2GPSSpeed", // data issues 4/30/18
        //"mSysCams", // there, unimplemented anymore 4/30/18
        "mSysMySQLWeb",
    ];
    var numElements = chartList1.length;
    var numElements2 = chartList2.length;
    chartArray = chartList1.concat(chartList2);
    for (var i = 0; i < chartArray.length; i++) {
        rData += "<span id='CHART_" + chartArray[i] + "'></span>";
    }
    rData += "<a href='" + getBasePath("ui") + "/OLMap.jsp?Action=Wx' target='nChartR'>" +
            "<img class='" + iconClass + "' src='" + getBasePath("get2") + "/Radar/EAX/_BLoop.gif?ts=" + timestamp + "'/></a>" +
            "<a href='" + getResource("Cams") + "' target='nChartC'>" +
            "<img class='" + iconClass + "' src='" + getBasePath("get2") + "/Cams/_Latest.jpeg?ts=" + timestamp + "'/></a>" +
            "</div>"; 
    dojo.byId("chartPlacement").innerHTML = rData;
    getCharts(chartArray, stepIn, dateIn);
    setTimeout(function () {  populateCharts(); }, getRefresh("veryLong"));
}

function populateCharts3() {
	let pOpts = { "step" : stepIn, "date": dateIn };
	let pOptsStr = pOpts.step + "," + pOpts.date;
	let c2do = [
		"mSysLoad",
		"mSysCPU",
		"mSysTemp",
		"mSysMemory",
		"mSysNet",
		"mSysStorage",
		"mSysMySQLSize",
		"mSysNumUsers",
		"mSysFans"
	];
	let divOpts = "class='th_sm_med' style='height: 92px;'";
	let rData = "<div class='rWrapper'><div class='table'><div class='tr'>";
	let j = 0;
	for(let i = 0; i < c2do.length; i++) {
		if(j <= 5) {
			rData += "<span class='td'>" +
				"<a href='" + doCh("3", c2do[i], pOptsStr) + "' target='nChHolder'>" + c2do[i] + "<br/>"
				"<div " + divOpts + " >" +
				"<canvas id='" + c2do[i] + "_Holder'></canvas>" +
				"</div>" + 
				"</a></span>";
			j++;
			if(j == 5) { 
				rData += "</div>" +
					"<div class='tr'>";
				j = 0; 
			}
		}
	}
	rData += "</div></div></div>"; 
    dojo.byId("chartPlacement3").innerHTML = rData;
	ch_get_mSysLoad("mSysLoad_Holder", "thumb", pOpts);
	ch_get_mSysCPU("mSysCPU_Holder", "thumb", pOpts);
	ch_get_mSysTemp("mSysTemp_Holder", "thumb", pOpts);
	ch_get_mSysMemory("mSysMemory_Holder", "thumb", pOpts);
	ch_get_mSysNet("mSysNet_Holder", "thumb", pOpts);
	ch_get_mSysStorage("mSysStorage_Holder", "thumb", pOpts);
	ch_get_mSysMySQLSize("mSysMySQLSize_Holder", "thumb", pOpts);
	ch_get_mSysNumUsers("mSysNumUsers_Holder", "thumb", pOpts);
	ch_get_mSysFans("mSysFans_Holder", "thumb", pOpts);
}

function populateEDiscovery(lastSsh) {
    var rData = "<strong>SSH Clients</strong>: ";
    if(isSet(lastSsh)) {
        switch(lastSsh.length) {
            case 0: rData += "<strong>NO SSH SESSIONS ACTIVE!</strong>"; break;
            default: 
                for (var i = 0; i < lastSsh.length; i++) {
                    if(lastSsh.SSHClientIP = "") {
                        rData += "UnknownIP";
                    } else {
                        rData += lastSsh[i].SSHClientIP;
                    }
                }
                break;
        }
    } else {
        rData += "ERROR FETCHING FROM DATABASE!";
    }
    rData += "<p>";
    dojo.byId("eDiscoveryHolder").innerHTML = rData;
}

function populateLiveDatabaseHolder(target, dbInfo) {
	var rData = "MySQL database rowcount: ";
	var cumDbRows = 0;
	dbInfo.forEach(function (tdbi) {
		cumDbRows += tdbi.RowCount;
	});
	rData += "<strong>" +
		"<div class='UPop'>" + cumDbRows + "<div class='UPopO'>"
		"<div class='table'>";
	dbInfo.forEach(function (tdbi) {
		rData += "<div class='tr'>" +
			"<span class='td'>" + tdbi.dbTableName + "</span>" +
			"<span class='td'>" + tdbi.RowCount + "</span>" +
			"</div>";
	});
	rData += "</div></div></div></strong><br/>";
	dojo.byId(target).innerHTML = rData;
}

function populateStatusHolder(target, stateData) {
    var indoorTemp = Math.round(0.93 * conv2Tf(stateData.mergedTemps[0].ExtTemp/1000));
    var garageTemp = Math.round(stateData.mergedTemps[1].ExtTemp);
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
    var nodes = [ "Main", "UVM", "Router", "Pi", "Pi2", "Phone", "PhoneE" ];
    var rData = "<div class='UPopNM'>" +
            "<button style='" + styleTemp(indoorTemp) + "'>" + indoorTemp + "F</button> " +
            "<button style='" + styleTemp(garageTemp) + "'>" + garageTemp + "G</button> " +
            "<div class='UPopNMO'>" +
            "<div class='table'>" +
            "<form class='tr' id='StepForm'>" +
            "<span class='td'>Interval</span>" +
            "<span class='td'>" +
            "<select name='chStep' id='chartStepForm'><option value='1'>Select...</option>";
    for(var key in intervals) { rData += "<option value='" + key + "'>" + intervals[key] + "</option>"; };
    rData += "</select></span>" +
            "</form></div><br/>" +
            "<a href='" + getBasePath("ui") + "/Images/Topology.jpg'>Topology Map</a><br/>" +
            "<a href='" + getBasePath("ui") + "/OLMap.jsp?Action=PhoneTrack&Input=Note3'>Phone: Note 3</a><br/>" +
// unused   "<a href='" + getBasePath("ui") + "/OLMap.jsp?Action=PhoneTrack&Input=Note3R'>Phone: Note 3 (Rapid)</a><br/>" +
// unused   "<a href='" + getBasePath("ui") + "/OLMap.jsp?Action=PhoneTrack&Input=EmS4'>Phone: Emily S4</a><br/>" +
// unused   "<a href='" + getBasePath("ui") + "/OLMap.jsp?Action=PhoneTrack&Input=RasPi2'>Rasbperry Pi 2</a><br/>" +
//  old     "<a href='" + getBasePath("old") + "/OutMap.php?PhoneTrack=Note3'>Phone: Note 3</a><br/>" +
//  old     "<a href='" + getBasePath("old") + "/OutMap.php?PhoneTrack=Note3Rapid'>Phone: Note 3 (Rapid!)</a><br/>" +
//  old     "<a href='" + getBasePath("old") + "/OutMap.php?PhoneTrack=EmS4'>Phone: Emily S4</a><br/>" +
//  old     "<a href='" + getBasePath("old") + "/OutMap.php?PhoneTrack=RasPi2'>Raspberry Pi 2</a>" +
            "</div></div>";
    var dateForm = "<form id='chDateForm' class='tr'>" +
            "<span class='td'>Date</span>" +
            "<span class='td'><input name='chDate' type='date' style='width: 75px;'/>" +
            "<button class='UButton' id='subDateForm'>Go</button></span>" +
            "</form>";
    for (var i = 0; i < nodes.length; i++) {
        var entityToCheck = nodes[i];
        var timestampToCheck = stateData.lastWalk[0][nodes[i]];
        rData += onCheck(timestampToCheck, entityToCheck);
    }
    rData += "<br/>" + dateForm;
    dojo.byId(target).innerHTML = rData;
    var stepSelect = dojo.byId("chartStepForm");
    var dateSelect = dojo.byId("subDateForm");
    dojo.connect(stepSelect, "change", actOnChartStepSelect);
    dojo.connect(dateSelect, "click", actOnChartDateSelect);
}

function initSysMon() {
    snmpRapid("snmpDataRapidHolder");
    getLastWalk("snmpStatusHolder");
    getLiveRowCount("databaseLiveHolder");
    populateCharts3();
    populateCharts();
    getEDiscovery();
    populateReliaStump();
};

dojo.ready(initSysMon);