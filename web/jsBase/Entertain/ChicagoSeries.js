/* 
by Anthony Stump
Created: 16 Dec 2018
 */

function getChicagoSeries(target) {
    aniPreload("on");
    var thePostData = { "doWhat": "getChicagoSeries" };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Entertainment"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    populateChicagoSeries(target, data);
                    aniPreload("off");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for ChicagoSeries FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function populateChicagoSeries(target, chicagoEps) {
    var cols = [ "Overall", "Sea/Ep", "Title" ];
    var rData = "<h3>Chicago Series</h3>" +
            "<div class='table'>" +
            "<div class='tr'>";
    for (var i = 0; i < cols.length; i++) { rData += "<span class='td'><strong>" + cols[i] + "</strong></span>"; }
    rData += "</div>";
    chicagoEps.forEach(function (chs) {
        rData += "<div class='tr'>" +
                "<span class='td'>" + chs.OverallNo + "</span>" +
                "<span class='td'>" + chs.Season + "/" + chs.SeasonNo + "</span>" +
                "<span class='td'><div class='UPop'>" + chs.Title +
                "<div class='UPopO'>" +
                "<strong>Aired:</strong> " + chs.AirDate + "<br/>" +
                chs.Synopsis +
                "</div></div></span>" +
                "</div>";
    });
    rData += "</div>";
    dojo.byId(target).innerHTML = rData;
}