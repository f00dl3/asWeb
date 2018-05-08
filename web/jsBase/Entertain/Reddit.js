/* 
by Anthony Stump
Created: 15 Apr 2018
Updated: 7 May 2018
 */

var redditData; 

function displayReddit() {
    $("#ETReddit").toggle();
    $("#ETCooking").hide();
    $("#ETLego").hide();
    $("#ETGameAll").hide();
    $("#ETStream").hide();
    layoutReddit();
    getRedditData();
}

function getRedditData() {
    var thePostData = {
        "doWhat": "getReddit",
        "searchDate": "2018-05-07%"
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("NewsFeed"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    redditData = data;
                    popSearchReddit();
                    popRedditResults(data);
                    aniPreload("off");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for Reddit FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function hintReddit(value) {
    if(value.length > 2) {
        var hitCount = 0;
        var matchLimitHit = 0;
        var contextualData = [];
        redditData.forEach(function (sr) {
            if(
                (isSet(sr.Title) && (sr.Title).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.Content) && (sr.Content).toLowerCase().includes(value.toLowerCase()))
            ) { 
                hitCount++;
                if(contextualData.length < 49) {
                    contextualData.push(sr);
                } else {
                   matchLimitHit = 1;
                }
            }
        });
        popRedditResults(contextualData, matchLimitHit);    
    }
}

function layoutReddit() {
    var rData = "<h4>Reddit Database</h4>" +
            "<a href='" + getBasePath("rOut") + "' target='top'>Auto-fetched Tarballs</a><p>" +
            "<div id='RedditSearchHolder'>LOADING REDDIT FEED DATA...</div><br/>" +
            "<div id='RedditResultHolder'></div>";
    dojo.byId("ETReddit").innerHTML = rData;
}

function popSearchReddit() {
    var rData = "<form id='RedditSearch'>Search: " +
            "<input type='date' name='RedditDate' id='RedditDate' value='' style='width: 80px;' />" +
            "</form><p>";
    dojo.byId("RedditSearchHolder").innerHTML = rData;
}

function popRedditResults(contextualData) {
    var rData = "<div class='table'>";
    contextualData.forEach(function (reddit) {
        rData += "<div class='tr'>" +
                "<span class='td'>" + reddit.GetTime + "</span>" +
                "<span class='td'><div class='UPopNM'>" + reddit.title + "<div class='UPopNMO'>" + reddit.content + "</div></div></span>" +
                "</div>";
    });
    rData += "</div>";
    dojo.byId("RedditResultHolder").innerHTML = rData;
}