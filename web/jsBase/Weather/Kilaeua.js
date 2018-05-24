/* 
by Anthony Stump
Created: 17 May 2018
Updated: 24 May 2018
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
    var spectLink = "http://st-rau.de/img/seismo.jpg";
    var seisLink = "https://volcanoes.usgs.gov/vsc/captures/kilauea/PAUD-6h.png";
    var spectHolder = "<a href='" + spectLink + "' target='specKil'><img class='th_small' src='" + spectLink + "?" + timestamp + "'></a>" +
            "<a href='" + seisLink + "' target='seiKil'><img class='th_small' src='" + seisLink + "?" + timestamp + "'></a>";
    dojo.byId("kSpectHolder").innerHTML= spectHolder;
    setTimeout(function() { popKilaueaSpectro(); }, timeout);
}

function populateKilaeuaData() {
    var rData = "<strong>Kilaeua data</strong><br/>" +
            "<span id='kCamHolder'></span>" +
            "<span id='kSpectHolder'></span><br/>" +
            "<span id='vLinkHolder'></span><br/>" +
            "<a href='" + getBasePath("oldGet") + "/Kilaeua' target='kca'>Kilauea Cam Archives</a><br/>";
    dojo.byId("vHolder").innerHTML = rData;
    getWebLinks("Volcano", "vLinkHolder", null);
    popKilaeuaCam();
    popKilaueaSpectro();
}


