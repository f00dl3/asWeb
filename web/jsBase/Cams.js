/* 
by Anthony Stump
Created: 30 Mar 2018
Updated: 1 Apr 2018
 */

function popCamHolder() {
    var rData = "<h1><img src='" + getBasePath("icon") + "/DeusEx.png' class='th_icon'/>Anthony's Survailance</h1>";
    var vObj = "<video id='CamLoop' height='100%' width='100%' autoplay controls loop>" +
            "<source src='" + getBasePath("getOld") + "/Cams/_Loop.mp4?ts=" + getDate("minute", 0, "timestamp") + "'></source>" +
            "</video>";
    rData += vObj + "<p>" +
            "<a href='" + getBasePath("getOld") + "/Cams' target='new'>/GetCams</a> Directory Listing (" +
            "<a href='" + getGDrivePath("Cams") + "' target='new'>Google Drive</a>)";
    dojo.byId("camHolder").innerHTML = rData;
}

function init() {
    getObsDataMerged("disHolderCAMS", "marquee");
    popCamHolder();
    // popSnmpHolder();
}

dojo.ready(init);

