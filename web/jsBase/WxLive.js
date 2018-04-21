/* 
by Anthony Stump
Created: 27 Mar 2018
Updated: 20 Apr 2018
 */

function actOnShowFeed() {
    getSpcFeeds();
}

function actOnShowLive() {
    getObsDataMerged("ObsCurrent", "static");
    getLiveLinks3d();
    popLiveLinksList();
    $("#WxLive").toggle();
}

function actOnShowNews() {
    getNewsEmail();
}

function actOnShowQuakes() {
    popEarthquakes();
    $("#WxQuakes").toggle();
}

function buttonListeners() {
    var showLiveButton = dojo.byId("ShWxObs");
    var showFeedButton = dojo.byId("ShFeeds");
    var showQuakesButton = dojo.byId("ShQuake");
    var showNewsButton = dojo.byId("ShNEmail");
    dojo.connect(showLiveButton, "click", actOnShowLive);
    dojo.connect(showFeedButton, "click", actOnShowFeed);
    dojo.connect(showQuakesButton, "click", actOnShowQuakes);
    dojo.connect(showNewsButton, "click", actOnShowNews);
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
                }),
                $("#WxNews").toggle();;
    });
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
                }),
                $("#WxNews").toggle();;
    });
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
                }),
                $("#WxFeeds").toggle();;
    });
}

function popEarthquakes() {
    var rData = "<h4>Earthquakes</h4>" +
            "<strong>See Weather Map for live and historical / archived data!</strong><p>" +
            "<span id='qLinkHolder'></span>";
    dojo.byId("WxQuakes").innerHTML = rData;
    getWebLinks("Weather.php-EQuake", "qLinkHolder", null);
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

function popLiveLinks3d(irsLinks, df7Links) {
    var cubeRes = "class='th_sm_med'";
    var elementListWx1 = [];
    var irsElems = irsLinks.length;
    irsLinks.forEach(function (irs) {
        var tElem = "<a styleReplace href='" + getBasePath("old") + "/OutMap.php?" + irs.Bubble + "' target='new'>" +
                "<img " + cubeRes + " src='" + irs.URL + "/></a>";
        elementListWx1.push(tElem);
    });
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
    var codLi = "<li>CoDP Radars: <span id='codBubbles'></span>";
    var modelLi = "<li>Models: <span id='modelBubbles'></span>";
    var obsLinks = "<ul><span id='obsLinks'></span></ul>";
    var rData = codLi + modelLi + obsLinks;
    dojo.byId("ObsLinksList").innerHTML = rData;
    getWebLinks("Weather.php-RadSat-CDRad", "codBubbles", "bubble");
    getWebLinks("Weather.php-FModel", "modelBubbles", "bubble");
    getWebLinks("Weather.php-Observations", "obsLinks", "list");
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
    var rData = "<h4>News & Email</h4>" +
            "<ul><span id='radioLinks'></span></ul><p>" +
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
    dojo.byId("News").innerHTML = rData;
    getWebLinks("WxLive.php-Radio", "radioLinks", "list");
}

function init() {
    buttonListeners();
    actOnShowLive();
}

dojo.ready(init);