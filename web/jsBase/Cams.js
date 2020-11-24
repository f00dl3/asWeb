/* 
by Anthony Stump
Created: 30 Mar 2018
Updated: 24 Nov 2020
 */

function actOnDoLive(event) {
    delete displayVideoLoop;
    dojo.stopEvent(event);
    displayLiveCams();
}

function actOnDoVideo(event) {
    delete displayLiveCams;
    dojo.stopEvent(event);
    displayVideoLoop();
}

function displayLiveCams() {
    var timeout = getRefresh("rapid");
    if(checkMobile()) { timeout = getRefresh("semiRapid"); }
    var liveSnapshotImage = getBasePath("chartCache") + "/CamLive.jpeg?ts=" + getDate("minute", 0, "timestamp");
    var rData = "<a href='" + liveSnapshotImage + "'><img src='" + liveSnapshotImage + "' width='100%'/></a>";
    dojo.byId("whereCamsGo").innerHTML = rData;
    setTimeout(function() { displayLiveCams(); }, timeout);
}

function displayVideoLoop() {
    var timeout = 1000 * 90;
    var vObj = "<video id='CamLoop' height='100%' width='100%' autoplay muted controls loop>" +
            "<source src='" + getBasePath("get2") + "/Cams/_Loop.mp4?ts=" + getDate("minute", 0, "timestamp") + "'></source>" +
            "</video>";
    dojo.byId("whereCamsGo").innerHTML = vObj;
    dojo.byId("CamLoop").play();
    setTimeout(function() { displayVideoLoop(); }, timeout);
}

function popCamHolder() {
    var getCamRawFolder = getBasePath("get2") + "/Cams";
    var rData = "<h1><img src='" + getBasePath("icon") + "/DeusEx.png' class='th_icon'/>Anthony's Survailance</h1>" +
    		"<span id='miniSmartControllerHolder'></span>" +
            "<button class='UButton' id='doLive'>Live</button>" + 
            "<button class='UButton' id='doLoop'>Loop</button><br/>" +
            "<div id='whereCamsGo'></div><p>" +
            "<a href='" + getBasePath("ui") + "/Folders.jsp?folderInput=" + getCamRawFolder + "' target='new'>/GetCams</a> Directory Listing (" +
            "<a href='" + getGDrivePath("Cams") + "' target='new'>Google Drive</a>)";
    dojo.byId("camHolder").innerHTML = rData;
    var buttonLive = dojo.byId("doLive");
    var buttonVideo = dojo.byId("doLoop");
    dojo.connect(buttonLive, "onclick", actOnDoLive);
    dojo.connect(buttonVideo, "onclick", actOnDoVideo);
    showMiniSmartController("miniSmartControllerHolder");
    displayVideoLoop();    
}

function putOverviewSmall(holder) {
	let rData = "<div class='UPop'><button class='UButton'>Worth</button>" +
		"<div class='UPopO'>" +
		"<div class='ch_small'><canvas id='rwcChart'></canvas></div>" +
		"<div id='extraDataHolder'></div>" +
		"</div></div>" +
		"<div class='UPop'><button class='UButton'>Weather</button>" +
		"<div class='UPopO'>" +
		"<div class='ch_small'><canvas id='hTempChart'></canvas></div>" +
		"<div class='ch_small'><canvas id='hPrecipChart'></canvas></div>" +
		"</div></div>";
	dojo.byId(holder).innerHTML = rData;
	ch_get_FinENW_All_R("rwcChart", "thumb");
	ch_get_ObsJSONTempH("hTempChart", "thumb");
	ch_get_ObsJSONPrecipRateH("hPrecipChart", "thumb");
}

function initCams() {
    getObsDataMergedAndHome("disHolderCAMS", "marquee");
    popCamHolder();
    snmpRapid("snmpHolder");
	putOverviewSmall("finOverviewHolder");
}

require(["dojo/ready"], function(ready){
  	ready(function(){
		initCams();
  	});
});