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
}

function getRedditData() {
    redditData = "Back from function!";
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
            "<div id='RedditSearchHolder'></div><br/>" +
            "<div id='RedditResultHolder'></div>";
    dojo.byId("ETReddit").innerHTML = rData;
    popSearchReddit();
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
                "<span class='td'><div class='UPopNM'>" + reddit.Title + "<div class='UPopNMO'>" + reddit.Content + "</div></div></span>" +
                "</div>";
    });
    rData += "</div>";
    dojo.byId("RedditResultHolder").innerHTML = rData;
}