/* 
by Anthony Stump
Created: 19 Mar 2018
Split to Dbx.js: 15 Apr 2018
 */

function getDbx() {
    var isMobile = "no";
    aniPreload("on");
    if(checkMobile()) { isMobile = "yes"; }
    var thePostData = { "doWhat": "getDbx", "isMobile": isMobile };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("MediaServer"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    populateDbx(data);
                    aniPreload("off");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for DBX fail! " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function populateDbx(dbxData) {
    var cols = [ "ZIP", "File" ];
    var dbxDoGif, dbxLocation;
    dbxDoGif = dbxLocation = "";
    var dbxContent = "<h3>DBX Index</h3>" +
            "<div class='table'>" +
            "<div class='tr'>";
    for(var i = 0; i > cols.length; i++) { dbxContent += "<span class='td'><strong>" + cols[i] + "</strong></span>"; }
    dbxContent += "</div>";
    dbxData.forEach(function (tDbx) {
        var dAlbumArt = (tDbx.AlbumArt).substr((tDbx.AlbumArt).length - 4);
        if(isSet(tDbx.GIFVer)) {
            dbxDoGif = "<img src='" + getBasePath("tomcatOld") + "/AlbumArt/" + tDbx.AlbumArt + ".pnx' class='th_large' />";
        } else { dbxDoGif = "Review first!"; }
        if(tDbx.Burned === 1) {
            dbxLocation = tDbx.Media + " (" + tDbx.BDate + ")";
        } else { dbxLocation = "Local"; }
        var tdo = "<div class='tr'>" +
                "<span class='td'><div class='UPop'>" + dAlbumArt +
                "<div class='UPopO'>" + dbxDoGif + "</div></div></span>" +
                "<span class='td' style='" + mediaPlayColor(tDbx.PlayCount) + " width: 80%;'>" +
                "<div class='UPop'>" + tDbx.File + "<div class='UPopO'>" +
                "<strong>Size:</strong> " + (tDbx.Size/1024).toFixed(1) + " MB<br/>" +
                "<strong>Duration:</strong> " + (tDbx.DurSec/60).toFixed(1) + " min<br/>" +
                "<strong>Location:</strong> " + dbxLocation + "<br/>";
        if(isSet(tDbx.Artist)) { tdo += "<strong>Source/Artist:</strong> " + tDbx.Artist + "<br/>"; }
        if(isSet(tDbx.XTags)) { tdo += "<strong>Tags:</strong> " + tDbx.XTags + "<br/>"; }
        if(isSet(tDbx.Description)) { tdo += tDbx.Description + "<br/>"; }
        tdo += "</div></div></span>" +
                "</div>";
        dbxContent += tdo;
    });
    dbxContent += "</div>";
    dojo.byId("ETSResults").innerHTML = dbxContent;
}


