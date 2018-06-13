/* 
by Anthony Stump
Created: 16 Jun 2018
 */

function getTrueBlood(target) {
    aniPreload("on");
    var thePostData = { "doWhat": "getTrueBlood" };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Entertainment"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    populateTrueBlood(target, data);
                    aniPreload("off");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for TrueBlood FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function populateTrueBlood(target, tbEps) {
    var cols = [ "Episode", "Title" ];
    var rData = "<h3>TrueBlood</h3>" +
            "<div class='table'>" +
            "<div class='tr'>";
    for (var i = 0; i < cols.length; i++) { rData += "<span class='td'><strong>" + cols[i] + "</strong></span>"; }
    rData += "</div>";
    tbEps.forEach(function (tb) {
        rData += "<div class='tr'>" +
                "<span class='td'><div class='UPop'>" + tb.OverallNo +
                "<div class='UPopO'>" +
                "<strong>Season Episode:</strong> " + tb.SeasonNo + "<br/>" +
                "</div></div></span>" +
                "<span class='td'><div class='UPop'>" + tb.Title +
                "<div class='UPopO'>" +
                "<strong>Aired:</strong> " + tb.AirDate + "<br/>" +
                tb.Synopsis +
                "</div></div></span>" +
                "</div>";
    });
    rData += "</div>";
    dojo.byId(target).innerHTML = rData;
}