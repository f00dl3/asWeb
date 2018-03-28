/* 
by Anthony Stump
Created: 27 Mar 2018
 */

function popEarthquakes() {
    var rData = "<strong>See Weather Map for lie and historical / archived data!</strong><p>" +
            "<ul>";
    var eqData = getWebLinks("Weather.php-EQuake", null, null);
    eqData.forEach(function (quake) {
        rData += "<li><a href='" + quake.URL + "' target='new'>" + quake.Description + "</a></li>";
    });
    rData += "</ul>";
    dojo.byId("Earthquakes").innerHTML = rData;
}

function popFeeds(spcFeedData) {
    var rData = "<ul>";
    spcFeedData.forEach(function (spc) {
        var spcLinkThis = getBasePath("old") + "/Include/SPCFeed.php?Type=" + spc.Type + "&Time=" + spc.GetTime;
        rData += "<li><a href='" + spcLinkThis + "' target='new'>" +
                spc.title + "</a></li>";
    });
    rData += "</ul>";
    dojo.byId("WeatherFeeds").innerHTML = rData;
}

function popLiveLinks3d() {
    var cubeRes = "class='th_sm_med'";
    var elementListWx1 = [];
    var irsLinks = getWebLinks("Weather.php-IRS", null, null);
    var irsElems = irsLinks.length;
    irsLinks.forEach(function (irs) {
        var tElem = "<a styleReplace href='" + getBasePath("old") + "/OutMap.php?" + irs.Bubble + "' target='new'>" +
                "<img " + cubeRes + " src='" + irs.URL + "/></a>";
        elementListWx1.push(tElem);
    });
    var df7Links = getWebLinks("Weather.php-7Day", null, null);
    var df7Elems = df7Links.length;
    df7Links.forEach(function (df7) {
        var tElem = "<a styleReplace hrf='" + df7.URL + "' target='new'>" +
                "<img " + cubeRes + " src='" + df7.URL + "'/></a>";
        elementListWx1.push(tElem);
    });
    var elementPopper1 = "<a styleReplace href='" + getBasePath("old") + "/Cams.php' target='new'>" +
            "<img " + cubeRes + " src='" + getBasePath("getOld") + "/Cams/_Latest.jpeg' alt='Cams'></a>";
    elementListWx1.push(elementPopper1);
    var rData = imageLinks3d(elementListWx1, 25, 200, 1.53);
    dojo.byId("ObsLinks3D").innerHTML = rData;
}

function popLiveLinksList() {
    var rData = "<ul>";
    var codLi = "<li>CoDP Radars: ";
    var codLinks = getWebLinks("Weather.php-RadSat-CDRad", null, null);
    codLinks.forEach(function (cod) {
        codLi += "<a href='" + cod.URL + "' target='new'><button class='UButton'>" + cod.Bubble + "</button></a>";
    });
    codLi += "</li>";
    var modelLi = "<li>Models: ";
    var modelLinks = getWebLinks("Weather.php-FModel", null, null);
    modelLinks.forEach(function (model) {
        modelLi += "<a href='" + model.URL + "' target='new'><button class='UButton'>" + model.Bubble + "</bubble></a>";
    });
    modelLi += "</li>";
    rData += codLi + modelLi;
    var obsLinks = getWebLinks("Weather.php-Observations", null, null);
    obsLinks.forEach(function (obs) {
        rData += "<li><a href='" + obs.URL + "' target='new'>" + obs.Description + "</a></li>";
    });
    rData += "</ul>";
    dojo.byId("ObsLinksList").innerHTML = rData;
}

function putLiveWarnings(liveWarnings) {
    var liveWarns = "";
    liveWarnings.forEach(function(lw) {
        liveWarns += "<div class='UPop'>" +
                "<a href='" + lw.id + "' target='top'>" + lw.title + "</a>" +
                "<div class='UPopO'>" + lw.summary + "</div>" +
                "</div><br/>";
    });
    dojo.byId("LiveWarnings").innerHTML = liveWarns;
}

function popNewsEmail(newsEmailFeeds) {
    var rData = "<ul>";
    var radioLinks = getWebLinks("WxLive.php-Radio", null, null);
    radioLinks.forEach(function (radio) {
        rData += "<li><a href='" + radio.URL + "' target='new'>" + radio.Description + "</a></li>";
    });
    rData += "</ul><p>" +
            "<div class='table'>";
    newsEmailFeeds.forEach(function (nem) {
        rData += "<div class='tr'>" +
                "<span class='td'>" + nem.pubDate + "</span>" +
                "<span class='td'><div class='UPop'>" + nem.title +
                "<div class='UPopO'>" + stripTags(nem.Description) + "</div>" +
                "</div></span>" +
                "<span class='td'><a href='" + nem.link + "' target='new'>" + nem.Source + "</a></span>" +
                "</div>";
    });
    rData += "</div>";
    dojo.byId("NewsEmail").innerHTML = rData;
}

function init() {
    getObsData("ObsCurrent", "static");
    popLiveLinks3d();
    popLiveLinksList();
    popFeeds();
    popEarthquakes();
    popNewsEmail();
}

dojo.ready(init);