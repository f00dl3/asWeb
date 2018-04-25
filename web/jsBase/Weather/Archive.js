/* 
by Anthony Stump
Created: 24 Apr 2018
 */

function actOnShowEvents() {
    displayEvents();
}

function displayArchive() {
    popArchive();
    $("#WxLiveContainer").hide();
    $("#WxLocalModel").hide();
    $("#WxArchive").toggle();
    $("#WxCf6").hide();
}

function displayEvents() {
    var layout = "<h3>Event Archive</h3>" +
            "<div id='WxEvSearchHolder'></div>" +
            "<div id='WxEvResultHolder'></div>";
    dojo.byId("WxArchive").innerHTML = layout;
    getEventData();
}

function getEventData() {
    
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

function popEvents(feedEvents) {
    var rData = "";
    feedEvents.forEach(function (feed) {
        rData += "<div class='tr'>" +
                "<span class='td'>" + feed.Date + " @ " + feed.Time + "</span>" +
                "<span class='td'><div class='UPop'>" + feed.Type;
        if(isSet(feed.Magnitude)) { rData += ", " + feed.Magnitude; }
        
    });
    dojo.byId("WxEvResultHolder").innerHTML = rData;
}

function popEventSearch() {
    
}