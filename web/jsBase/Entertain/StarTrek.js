/* 
by Anthony Stump
Created: 16 Apr 2018
 */

function populateStarTrek(target, stEps) {
    var cols = [ "SDate", "Title" ];
    var rData = "<h3>StarTrek</h3>" +
            "<div class='table'>" +
            "<div class='tr'>";
    for (var i = 0; i < cols.length; i++) { rData += "<span class='td'><strong>" + cols[i] + "</strong></span>"; }
    rData += "</div>";
    stEps.forEach(function (trek) {
        rData += "<div class='tr'>" +
                "<span class='td'><div class='UPOp'>" + trek.StarDate +
                "<div class='UPopO'>" +
                "<strong>Series:</strong> " + trek.Series + "<br/>" +
                "<strong>Season:</strong> " + trek.Season + "<br/>" +
                "<strong>Episode:</strong> " + trek.SeasonEp + "<br/>" + 
                "<strong>Overall:</strong> " + trek.SeriesEp +
                "</div></div></span>" +
                "<span class='td'><div class='UPop'>" + trek.Title +
                "<div class='UPopO'>" +
                "<strong>Aired:</strong> " + trek.AirDate + "<br/>" +
                trek.ProdCode + ": " + trek.Synopsis +
                "</div></div></span>" +
                "</div>";
    });
    rData += "</div>";
    dojo.byId(target).innerHTML = rData;
}


