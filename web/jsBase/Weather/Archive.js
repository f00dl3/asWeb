/* 
by Anthony Stump
Created: 24 Apr 2018
Updated: 14 May 2018
 */

var eventData;

if(!isSet(evStart)) { var evStart = getDate("hour", -6, "hourstamp"); }
if(!isSet(evEnd)) { var evEnd = getDate("hour", +6, "hourstamp"); }

function actOnEventSearch(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    var thisFormDataJ = dojo.formToJson(this.form);
    if(
            !isSet(thisFormData.evStartDate) ||
            !isSet(thisFormData.evStartTime) ||
            !isSet(thisFormData.evEndDate) ||
            !isSet(thisFormData.evEndTime)
    ) {
        window.alert("Search time entry error!");
    } else {
        getEventData(thisFormData);
    }
}

function actOnShowEvents(event) {
    dojo.stopEvent(event);
    displayEvents();
}

function displayArchive() {
    getHTrackLast();
    $("#WxLiveContainer").hide();
    $("#WxLocalModel").hide();
    $("#WxArchive").toggle();
    $("#WxCf6").hide();
}

function displayEvents() {
    var layout = "<h3>Event Archive</h3>" +
            "<div id='WxEvSearchHolder'>LOADING EVENT DATA </div><p>" +
            "<div id='WxEvResultHolder'>PLEASE WAIT...</div>";
    dojo.byId("WxArchive").innerHTML = layout;
    getEventData();
}

function getEventData(formData) {
    var timeout = 60*1*1000;
    if(checkMobile()) {
        timeout = 60*2*1000;
    }
    aniPreload("on");
    if(isSet(formData)) {
        if(isSet(formData.evStartDate)) {
            evStart = (formData.evStartDate + formData.evStartTime);
            evStart = basicInputFilter(evStart);
        }
        if(isSet(formData.evEndDate)) {
            evEnd = (formData.evEndDate + formData.evEndTime);
            evEnd = basicInputFilter(evEnd);
        }
    }
    console.log("DEBUG: Feed search from " + evStart + " to " + evEnd);
    var thePostData = {
        "doWhat": "getEventData",
        "eventSearchStart": evStart,
        "eventSearchEnd": evEnd
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Wx"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    aniPreload("off");
                    eventData = data;
                    popEventSearch();
                    popEvents(data);
                },
                function(error) { 
                    aniPreload("off");
                    console.log("request for Events FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
    setTimeout(function () {
        if(!isSet(evStart)) { var evStart = getDate("hour", -12, "hourstamp"); }
        if(!isSet(evEnd)) { var evEnd = getDate("hour", 0, "hourstamp"); }
        getEventData(formData);
    }, timeout);
}

function getHTrackLast() {
    aniPreload("on");
    var thePostData = { "doWhat": "getHTrackLast" };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Wx"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    aniPreload("off");
                    popArchive(data[0]);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for HTrack Last FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function hintEvent(value) {
    if(value.length > 2) {
        var hitCount = 0;
        var matchLimitHit = 0;
        var matchingRows = [];
        eventData.forEach(function (sr) {
            if(
                (isSet(sr.Type) && (sr.Type).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.Magnitude) && (sr.Magnitude).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.Comments) && (sr.Comments).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.County) && (sr.County).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.State) && (sr.State).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.Location) && (sr.Location).toLowerCase().includes(value.toLowerCase()))
            ) { 
                hitCount++;
                if(matchingRows.length < 49) {
                    matchingRows.push(sr);
                } else {
                   matchLimitHit = 1;
                }
            }
        });
        popEvents(matchingRows, matchLimitHit);    
    }
}

function popArchive(htTrackLast) {
    var rData = "<h3>Data Archive</h3>" +
            "<em>Sciences Archive in process of being blended in with Media Server!</em>" +
            "<ul type='circle'>" +
            "<li><strong>Hurricane Tracks</strong>" +
            "<form id='HurriForm'><select name='HTrack'><option value=''>(Choose)</option>";
    for (var i = 2015; i >= 1860; i--) { rData += "<option value='" + i + "'>" + i + "</option>"; }
    rData += "</select></form>(Last addition: " + htTrackLast.StormID + ")</li>" +
            "<li><button class='UButton' id='FeedButton'>Feeds</button> - data search by Hour</a></li>" +
            "<li>(Reanalysis MP4s all on BluRay!)</li>" +
            "<li><a href='" + getBasePath("download") + "/Sciences.zip'>Legacy Science Archive</a></li>";
    dojo.byId("WxArchive").innerHTML = rData;
    var feedButton = dojo.byId("FeedButton");
    dojo.connect(feedButton, "click", actOnShowEvents);
}

function popEvents(contextEventData, matchLimitHit) {
    if(isSet(matchLimitHit)) { showNotice("Match limit hit - filtering!"); }
    var eCounter = 0;
    var rData = "<div class='table'>";
    contextEventData.forEach(function (feed) {
        eCounter++;
        if(eCounter <= 50) {
            rData += "<div class='tr'>" +
                    "<span class='td'>" + feed.Date + " @ " + feed.Time + "</span>" +
                    "<span class='td'><div class='UPop'>" + feed.Type;
            if(isSet(feed.Magnitude)) { rData += ", " + feed.Magnitude; }
            rData += "<div class='UPopO'>" + feed.Comments + "</div>" +
                    "</div></span>" +
                    "<span class='td'>";
            if(isSet(feed.Lat)) {
                rData += "<a href='" + getBasePath("old") + "/OutMap.php?Title=" + feed.Date + "&Point=" + 
                        "[" + feed.Lon + "," + feed.Last + "]' target='new'>";
            }
            if(isSet(feed.Location)) { rData += feed.Location; } else { rData += feed.County; }
            if(isSet(feed.State)) { rData += ", " + feed.State; }
            if(isSet(feed.Lat)) { rData += "</a>"; }
            rData += "</span></div>";
        }
    });
    rData += "</div>";
    dojo.byId("WxEvResultHolder").innerHTML = rData;
    if(eCounter > 50) { showNotice("Over 50 events! (" + eCounter + ")"); }
}

function popEventSearch() {
    var searchForm = "<div class='UBox' id='SearchEvents'><span>Event Search Form</span>" +
        "<tt>Please keep in mind that until fixed, search time is in UTC (+5 hours from CDT)</tt><br/>"
        "<div class='table' id='EventSearchFormTable'><form class='tr'>" +
        "<span class='td'><div class='table'><div class='tr'>" +
        "<span class='td'><input name='evStartDate' type='date' value='' style='width: " + dateEntryWidth + "px;'/></span>" +
        "<span class='td'><input name='evStartTime' type='time' value='' style='width: " + timeEntryWidth + "px'/></span>" +
        "</div></div></span>" +
        "<span class='td'><div class='table'><div class='tr'>" +
        "<span class='td'><input name='evEndDate' type='date' value='' style='width: " + dateEntryWidth + "px;'/></span>" +
        "<span class='td'><input name='evEndTime' type='time' value='' style='width: " + timeEntryWidth + "px'/></span>" +
        "</div></div></span>" +
        "<span class='td'><input name='QuickSearch' id='eventTextSearchbox' type='text' value='' onKeyUp='hintEvent(this.value)' style='width: 100px;' />" +
        "<input name='doWhat' type='hidden' value='setQuickEventSearch'/>" +
        "<button id='QuickEventSearch' name='QuickEventSearch' class='UButton'>Go</button>" +
        "</span></form></div></div>";
    dojo.byId("WxEvSearchHolder").innerHTML = searchForm;
    var submitSearch = dojo.byId("QuickEventSearch");
    dojo.connect(submitSearch, "click", actOnEventSearch);
}