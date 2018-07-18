/* 
by Anthony Stump
Created: 27 Mar 2018
Split from WxLive: 23 Apr 2018
Updated: 17 Jul 2018
 */

function actOnShowFeed() {
    getSpcFeeds();
    $("#WxFeeds").toggle();
    $("#WxLive").hide();
    $("#WxQuakes").hide();
    $("#WxNews").hide();
}

function actOnShowLive() {
    $("#WxLive").show();
    $("#WxFeeds").hide();
    $("#WxQuakes").hide();
    $("#WxNews").hide();
}

function actOnShowNews() {
    getNewsEmail();
    $("#WxNews").toggle();;
    $("#WxFeeds").hide();
    $("#WxQuakes").hide();
    $("#WxLive").hide();
}

function actOnShowQuakes() {
    popEarthquakes();
    $("#WxFeeds").hide();
    $("#WxLive").hide();
    $("#WxNews").hide();
    $("#WxQuakes").toggle();
}

function displayWxLive() {
    popLiveContainer();
    $("#WxLiveContainer").show();
    $("#WxLocalModel").hide();
    $("#WxArchive").hide();
    $("#WxCf6").hide();
}

function getLiveLinks3d() {
    aniPreload("on");
    var thePostData = {
        "doWhat": "getLiveLinks",
        "master1": "Weather.php-IRS",
        "master2": "Weather.php-7Day"
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("WebLinks"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    popLiveLinks3d(
                        data.irsLinks,
                        data.df7Links
                    );
                    aniPreload("off");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for 3D Weather Links FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                })
    });
    setTimeout(function () { getLiveLinks3d(); }, getRefresh("medium"));
}

function getLiveWarnings() {
    aniPreload("on");
    var thePostData = {
        "doWhat": "getLiveWarnings",
        "xdt1": getDate("day", -1, "full"),
        "xdt2": getDate("day", 0, "full"),
        "xExp": getDate("day", 0, "full"),
        "limit": "3",
        "stationA": "020091",
        "idMatch": "/"
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Wx"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    popLiveWarnings(data);
                    aniPreload("off");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for Live Warnings FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                })
    });
    setTimeout(function () { getLiveWarnings(); }, getRefresh("medium"));
}

function getNewsEmail() {
    aniPreload("on");
    var thePostData = { "doWhat": "getNewsEmail" };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Wx"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    popNewsEmail(data);
                    aniPreload("off");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for News/Email FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                })
    });
    setTimeout(function () { getNewsEmail(); }, getRefresh("medium"));
}

function getSpcFeeds() {
    aniPreload("on");
    var thePostData = { "doWhat": "getSpcFeed" };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Wx"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    popFeeds(data);
                    aniPreload("off");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for SPC Feeds FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                })
    });
    setTimeout(function () { getNewsEmail(); }, getRefresh("medium"));
}

function popEarthquakes() {
    var rData = "<h4>Earthquakes</h4>" +
            "<strong>See Weather Map for live and historical / archived data!</strong><p>" +
            "<span id='qLinkHolder'></span>";
    var vData = "<div id='vHolder'></div>";
    rData += vData;
    dojo.byId("WxQuakes").innerHTML = rData;
    getWebLinks("Weather.php-EQuake", "qLinkHolder", null);
    populateKilaeuaData();
}

function popFeeds(spcFeedData) {
    var rData = "<h4>Weather Feeds</h4><ul>";
    spcFeedData.forEach(function (spc) {
        rData += "<li><div class='SplashPop'>" + spc.title +
                "<div class='SplashPopO'>" +
                "<h1>" + spc.title + "</h1>" +
                spc.description +
                "</div></div></li>";
    });
    rData += "</ul>";
    dojo.byId("WxFeeds").innerHTML = rData;
}

function popLiveButtonNavi() {
    rData = "<button class='SButton' id='ShWxObs'>Live</button>" +
            "<button class='SButton' id='ShFeeds'>Feeds</button>" +
            "<button class='SButton' id='ShQuake'>Quakes</button>" +
            "<button class='SButton' id='ShNEmail'>News</button>";
    dojo.byId("liveButtonNavi").innerHTML = rData;
    var showLiveButton = dojo.byId("ShWxObs");
    var showFeedButton = dojo.byId("ShFeeds");
    var showQuakesButton = dojo.byId("ShQuake");
    var showNewsButton = dojo.byId("ShNEmail");
    dojo.connect(showLiveButton, "click", actOnShowLive);
    dojo.connect(showFeedButton, "click", actOnShowFeed);
    dojo.connect(showQuakesButton, "click", actOnShowQuakes);
    dojo.connect(showNewsButton, "click", actOnShowNews);
}

function popLiveContainer() {
    var rData = "<div id='liveButtonNavi'></div><p>" +
            "<div id='WxLive'>" +
            "<h4>Observations/Forecasts</h4>" +
            "<div id='LiveWarnings'>Loading watch/warning/advisories...</div><p>" +
            "<div id='ObsCurrent'>Loading observations...</div><p>" +
            "<div id='ObsLinks3D'>Loading 3D observation links...</div>" +
            "<div id='ObsLinksList'>Loading list of links...</div>" +
            "</div>" +
            "<div id='WxFeeds'></div>" +
            "<div id='WxQuakes'></div>" +
            "<div id='WxNews'></div>";
    dojo.byId("WxLiveContainer").innerHTML = rData;
    getLiveWarnings();
    popLiveButtonNavi();
    popLiveLinksList();
    getLiveLinks3d();
    getObsDataMerged("ObsCurrent", "static");
}

function popLiveLinks3d(irsLinks, df7Links) {
    var cubeRes = "class='th_sm_med'";
    var elementListWx1 = [];
    var irsElems = irsLinks.length;
    irsLinks.forEach(function (irs) {
        var newImgURL = "https://localhost:8444/asWeb" + irs.URL;
        irsBubbleCopy = irs.Bubble.replace("Image=", "Action=");
        var tElem = "";
        if(irs.Bubble === "RadarMode=B") {
            tElem = "<a styleReplace href='" + getBasePath("ui") + "/OLMap.jsp?Action=Wx' target='new'>";
        } else {
            tElem = "<a styleReplace href='" + getBasePath("old") + "/OutMap.php?" + irs.Bubble + "' target='new'>";
                    /* "<a styleReplace href='" + getBasePath("ui") + "/OLMap.jsp?" + irsBubbleCopy + "' target='new'>" + NEED TO FIGURE ANIMATION 6/6/18 */
        }
        tElem += "<img " + cubeRes + " src='" + newImgURL + "'/></a>";
        elementListWx1.push(tElem);
    });
    var df7Elems = df7Links.length;
    df7Links.forEach(function (df7) {
        var tElem = "<a styleReplace hrf='" + df7.URL + "' target='new'>" +
                "<img " + cubeRes + " src='" + df7.URL + "'/></a>";
        elementListWx1.push(tElem);
    });
    var elementPopper1 = "<a styleReplace href='" + getBasePath("ui") + "/Cams.jsp' target='new'>" +
            "<img " + cubeRes + " src='" + getBasePath("getOld") + "/Cams/_Latest.jpeg' alt='Cams'></a>";
    elementListWx1.push(elementPopper1);
    var rData = imageLinks3d(elementListWx1, 25, 200, 1.53);
    dojo.byId("ObsLinks3D").innerHTML = rData;
}

function popLiveLinksList() {
    var codLi = "<li>CoDP Radars: <span id='codBubbles'></span>";
    var modelLi = "<li>Models: <span id='modelBubbles'></span>";
    var obsLinks = "<ul><span id='obsLinks'></span></ul>";
    var rData = codLi + modelLi + obsLinks;
    dojo.byId("ObsLinksList").innerHTML = rData;
    getWebLinks("Weather.php-RadSat-CDRad", "codBubbles", "bubble");
    getWebLinks("Weather.php-FModel", "modelBubbles", "bubble");
    getWebLinks("Weather.php-Observations", "obsLinks", "list");
}

function popLiveWarnings(liveWarnings) {
    var liveWarns = "";
    if(isSet(liveWarnings) && liveWarnings.length !== 0) {
        liveWarnings.forEach(function(lw) {
            liveWarns += "<div class='UPop'>" + lw.title +
                    " (<a href='" + lw.id + "' target='top'>Source</a>)" +
                    "<div class='UPopO'>" + lw.summary + "</div>" +
                    "</div><br/>";
        });
    } else {
        liveWarns = "<em>No active watches/warnings/advsiories found!</em>";
    }
    dojo.byId("LiveWarnings").innerHTML = liveWarns;
}

function popNewsEmail(newsEmailFeeds) {
    var rData = "<h4>News & Email</h4>" +
            "<ul><span id='radioLinks'></span></ul><p>" +
            "<div class='table'>";
    newsEmailFeeds.forEach(function (nem) {
        if(isSet(nem.description)) {
            rData += "<div class='tr'>" +
                "<span class='td'>" + nem.pubDate + "</span>" +
                "<span class='td'><div class='UPop'>" + nem.title +
                "<div class='UPopO'>" + nem.description + "</div>" +
                "</div></span>" +
                "<span class='td'><a href='" + nem.link + "' target='new'>" + nem.Source + "</a></span>" +
                "</div>";
        }
    });
    rData += "</div>";
    dojo.byId("WxNews").innerHTML = rData;
    getWebLinks("WxLive.php-Radio", "radioLinks", "list");
}