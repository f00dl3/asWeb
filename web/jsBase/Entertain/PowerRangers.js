/* 
by Anthony Stump
Created: 16 Apr 2018
 */

function populateRangers(target, prData) {
    var cols = [ "SSE", "Title", "Link", "Overall" ];
    var rData = "<h3>Power Rangers (TV)</h3>" +
            "<div class='table'><div class='tr'>";
    for (var i = 0; i < cols.length; i++) { rData += "<span class='td'><strong>" + cols[i] + "</strong></span>"; }
    rData += "</div>";
    prData.forEach(function (pr) {
        var link = "N/A";
        if(pr.MediaServer === 1) { link = "<a href='" + getBasePath("media") + "/Movies/MMPR/" + pr.Series + "_" + pr.Season + "_" + pr.SeasonEp + ".mp4'>View</a>"; }
        rData += "<div class='tr'>" +
                "<span class='td'>" + pr.Series + "/" + pr.Season + "/" + pr.SeasonEp + "</span>" +
                "<span class='td'><div class='UPop'>" + pr.Title + 
                "<div class='UPopO'>" +
                "<storng>Aired: </strong>" + pr.AirDate + "<br/>" +
                pr.ProdCode + ": " + pr.Synopsis +
                "</div></div></span>" +
                "<span class='td'>" + link + "</span>" +
                "<span class='td'>" + pr.SeriesEp + "</span>" +
                "</div>";
    });
    rData += "</div>";
    dojo.byId(target).innerHTML = rData;
}

