/* 
by Anthony Stump
Created: 17 May 2018
Updated: 3 Jun 2018
 */

function popKilaeuaCam() {
    var timeout = 1000 * 90;
    var timestamp = getDate("minute", 0, "timestamp");
    var camLink = getBasePath("getOld") + "/Kilaeua/KICam_Latest.jpg";
    var cObj = "<a href='" + camLink + "' target='kcam'>" +
            "<img class='th_small' src='" + camLink + "?ts=" + timestamp + "'>" +
            "</a>";
    dojo.byId("kCamHolder").innerHTML = cObj;
    setTimeout(function() { popKilaeuaCam(); }, timeout);
}

function popKilaueaSpectro() {
    var timeout = 1000 * 10;
    var timestamp = getDate("minute", 0, "timestamp");
    var spectHolder = "";
    var volcLinks = [
        "https://volcanoes.usgs.gov/vsc/captures/kilauea/summit_uwe_tilt_2weeks.png",
        "https://volcanoes.usgs.gov/vsc/captures/kilauea/PAUD-6h.png",
        "https://volcanoes.usgs.gov/vsc/captures/kilauea/UWB-6h.png"
    ];
    for (var i = 0; i < volcLinks.length; i++) {
        spectHolder += "<a href='" + volcLinks[i] + "' target='specKil'><img class='th_small' src='" + volcLinks[i] + "?" + timestamp + "'></a>";
    }
    dojo.byId("kSpectHolder").innerHTML= spectHolder;
    setTimeout(function() { popKilaueaSpectro(); }, timeout);
}

function populateKilaeuaData() {
    var rData = "<strong>Kilaeua data</strong><br/>" +
            "<span id='kCamHolder'></span>" +
            "<span id='kSpectHolder'></span><br/>" +
            "<span id='vLinkHolder'></span><br/>" +
            "<a href='" + getBasePath("ui") + "/Folders.jsp?folderInput=" + getServerPath("rawGet") + "/Kilaeua' target='kca'>Kilauea Cam Archives</a><br/>";
    dojo.byId("vHolder").innerHTML = rData;
    getWebLinks("Volcano", "vLinkHolder", null);
    popKilaeuaCam();
    popKilaueaSpectro();
}


