/* 
by Anthony Stump
Created: 30 Mar 2018
Updated: 1 Dec 2019
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
    var liveSnapshotImage = getBasePath("oldRoot") + "/Get/Cams/Live.jpeg?ts=" + getDate("minute", 0, "timestamp");
    var rData = "<a href='" + liveSnapshotImage + "'><img src='" + liveSnapshotImage + "' width='100%'/></a>";
    dojo.byId("whereCamsGo").innerHTML = rData;
    setTimeout(function() { displayLiveCams(); }, timeout);
}

function displayVideoLoop() {
    var timeout = 1000 * 90;
    var vObj = "<video id='CamLoop' height='100%' width='100%' autoplay muted controls loop>" +
            "<source src='" + getBasePath("getOld") + "/Cams/_Loop.mp4?ts=" + getDate("minute", 0, "timestamp") + "'></source>" +
            "</video>";
    dojo.byId("whereCamsGo").innerHTML = vObj;
    dojo.byId("CamLoop").play();
    setTimeout(function() { displayVideoLoop(); }, timeout);
}

function popCamHolder() {
    var getCamRawFolder = getServerPath("rawGet") + "/Cams";
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

function init() {
    getObsDataMerged("disHolderCAMS", "marquee");
    popCamHolder();
    snmpRapid("snmpHolder");
}

dojo.ready(init);


