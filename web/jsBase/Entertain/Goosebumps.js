/* 
by Anthony Stump
Created: 19 Mar 2018
Split to Goosebumps.js: 15 Apr 2018
Updated: 22 Apr 2018
 */

function getGoosebumps(target) {
    aniPreload("on");
    var thePostData = { "doWhat": "getGoosebumps" };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Entertainment"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    populateGoosebumps(target, data);
                    aniPreload("off");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for Goosebumps FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function populateGoosebumps(target, gbQ) {
    var cols = [ "Art", "Code", "Title", "PDF" ];
    var rData = "<h3>Goosebumps Books</h3>" +
            "<div class='table'>" +
            "<div class='tr'>";
    for(var i = 0; i < cols.length; i++) { rData += "<span class='td'><strong>" + cols[i] + "</strong></span>"; }
    rData += "</div>";
    gbQ.forEach(function (gbData) {
        var imageThumbLocation = getBasePath("image") + "/Goosebumps/Thumbs/" + gbData.Code + "." + gbData.CoverImageType;
        var imageLocation = imageThumbLocation.replace("/Thumbs", "");
        rData += "<div class='tr'>" +
                "<span class='td'><a href='" + imageLocation + "' target='new'><img class='th_icon' src='" + imageThumbLocation + "'/></a></span>" +
                "<span class='td'>" + gbData.Code + "</span>" +
                "<span class='td'><div class='UPop'>" + gbData.Title +
                "<div class='UPopO'>" + gbData.Plot + "</div></div></span>" +
                "<span class='td'><div class='UPop'>";
        if(gbData.pdf === 1) { rData += "<a href='" + getBasePath("media") + "/Docs/Goosebumps/" + gbData.Code + ".pdf' target='new'>"; } else { rData += "N/A"; }
        rData += "<div class='UPopO'>" +
                "Published: " + gbData.PublishDate + "<br/>" +
                "Pages: " + gbData.Pages + "<br/>" +
                "ISBN: " + gbData.ISBN + "</div></div></span>" +
                "</div>";
    });
    rData += "</div></div>";
    dojo.byId(target).innerHTML = rData;
}
