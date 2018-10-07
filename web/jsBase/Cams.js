/* 
by Anthony Stump
Created: 30 Mar 2018
Updated: 7 Oct 2018
 */

function actOnDoLive(event) {
    dojo.stopEvent(event);
    displayLiveCams();
}

function actOnDoVideo(event) {
    dojo.stopEvent(event);
    displayVideoLoop();
}

function displayLiveCams() {
    var liveSnapshotImage = getBasePath("oldRoot") + "/Get/Cams/Live.jpeg";
    var rData = "(URL: <a href='" + liveSnapshotImage + "'>" + liveSnapshotImage + "</a>)<br/>" +
            "<a href='" + liveSnapshotImage + "'><img src='" + liveSnapshotImage + "'/></a>";
    dojo.byId("whereCamsGo").innerHTML = rData;
}

function displayVideoLoop() {
    var timeout = 1000 * 90;
    var vObj = "<video id='CamLoop' height='100%' width='100%' autoplay muted controls loop>" +
            "<source src='" + getBasePath("getOld") + "/Cams/_Loop.mp4?ts=" + getDate("minute", 0, "timestamp") + "'></source>" +
            "</video>";
    dojo.byId("whereCamsGo").innerHTML = vObj;
    dojo.byId("CamLoop").play();
    setTimeout(function() { videoObject(); }, timeout);
}

function popCamHolder() {
    var getCamRawFolder = getServerPath("rawGet") + "/Cams";
    var rData = "<h1><img src='" + getBasePath("icon") + "/DeusEx.png' class='th_icon'/>Anthony's Survailance</h1>" +
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
    displayVideoLoop();    
}

function init() {
    getObsDataMerged("disHolderCAMS", "marquee");
    popCamHolder();
    snmpRapid("snmpHolder");
}

dojo.ready(init);


