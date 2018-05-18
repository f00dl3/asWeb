/* 
by Anthony Stump
Created: 17 May 2018
 */

function popKilaeuaCam() {
    var timeout = 1000 * 90;
    var cObj = "<a href='" + getBasePath("getOld") + "/Kilaeua/KICam_Latest.jpg' target='kcam'>" +
            "<img class='th_medium' src='" + getBasePath("getOld") + "/Kilaeua/KICam_Latest.jpg?ts=" + getDate("minute", 0, "timestamp") + "'>" +
            "</a>";
    dojo.byId("kCamHolder").innerHTML = cObj;
    setTimeout(function() { popKilaeuaCam(); }, timeout);
}

function populateKilaeuaData() {
    var rData = "<strong>Kilaeua data</strong><br/>" +
            "<div id='kCamHolder'></div><br/>" +
            "<span id='vLinkHolder'></span><br/>" +
            "** Volcano Cam Leeching<p>";
    dojo.byId("vHolder").innerHTML = rData;
    getWebLinks("Volcano", "vLinkHolder", null);
    popKilaeuaCam();
}


