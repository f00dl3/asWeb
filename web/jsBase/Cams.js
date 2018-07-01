/* 
by Anthony Stump
Created: 30 Mar 2018
Updated: 1 Jul 2018
 */

function popCamHolder() {
    var getCamRawFolder = getServerPath("rawGet") + "/Cams";
    var timeout = 1000 * 90;
    var rData = "<h1><img src='" + getBasePath("icon") + "/DeusEx.png' class='th_icon'/>Anthony's Survailance</h1>";
    var vObj = "<video id='CamLoop' height='100%' width='100%' autoplay controls loop>" +
            "<source src='" + getBasePath("getOld") + "/Cams/_Loop.mp4?ts=" + getDate("minute", 0, "timestamp") + "'></source>" +
            "</video>";
    rData += vObj + "<p>" +
            "<a href='" + getBasePath("ui") + "/Folders.jsp?folderInput=" + getCamRawFolder + "' target='new'>/GetCams</a> Directory Listing (" +
            "<a href='" + getGDrivePath("Cams") + "' target='new'>Google Drive</a>)";
    dojo.byId("camHolder").innerHTML = rData;
    setTimeout(function() { popCamHolder(); }, timeout);
}

function init() {
    getObsDataMerged("disHolderCAMS", "marquee");
    popCamHolder();
    snmpRapid("snmpHolder");
}

dojo.ready(init);


