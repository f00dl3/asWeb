/* 
by Anthony Stump
Created: 15 Apr 2018
Altered off: 8 May 2018
Updated: 8 May 2018
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
        "desiredDataType": "dataStore",
        "searchDate": "2018-05-07%"
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("NewsFeed"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    popRedditResults(data);
                    aniPreload("off");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for RedditDataStore FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
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

// work on getting HTML images to show? safely!
function popRedditResults(contextualData) {
    var rData = "<div class='table'>";
    require(["dojo/data/ItemFileReadStore"], function(ItemFileReadStore, contextualData) {
        var redditStore = new ItemFileReadStore({ data: contextualData });
        redditStore.fetch({
            query: { GetTime: "*" },
            queryOptions: { ignoreCase: true, deep: true },
            onError: function(error, request) { console.log(error); },
            onComplete: function(items, request) {
                for(var i = 0; i < items.length; i++) {
                    var item = items[i];
                    rData += "<div class='tr'>" +
                        "<span class='td'>" + contextualData.getValue(item, "GetTime") + "</span>" +
                        "<span class='td'><div class='UPopNM'>" + contextualData.getValue(item, "title") + "<div class='UPopNMO'>" + contextualData.getValue(item, "content") + "</div></div></span>" +
                        "</div>";
                }
            }
        });
    });
    rData += "</div>";
    dojo.byId("RedditResultHolder").innerHTML = rData;
}