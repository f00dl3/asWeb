/* 
by Anthony Stump
Created: 16 Apr 2018
Updated: 22 Apr 2018
 */

function getXFiles(target) {
    aniPreload("on");
    var thePostData = { "doWhat": "getXFiles" };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Entertainment"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    populateXFiles(target, data);
                    aniPreload("off");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for XFiles FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function populateXFiles(target, xfData) {
    var cols = [ "Num", "IC", "Title", "*" ];
    var rData = "<h3>X-Files (TV)</h3>" +
            "<div class='table'><div class='tr'>";
    for (var i = 0; i < cols.length; i++) { rData += "<span class='td'><strong>" + cols[i] + "</strong></span>"; }
    rData += "</div>";
    xfData.forEach(function (xf) {
        var mythEp = "";
        var epNo = (xf.AlbumArt).replace("XFiles/", "");
        var epTitleRaw = (xf.File).split("."); 
        var epTitle = epTitleRaw[0];
        var thisAlbumArtImage = getBasePath("image") + "/AlbumArt/" + xf.AlbumArt + ".jpg";
        if((xf.Description).includes("XFilesTV Myth")) {
            mythEp = "<img src='" + getBasePath("image") + "/AlbumArt/XFiles/x.gif' class='th_icon'/>";
        }
        rData += "<div class='tr'>" +
                "<span class='td'>" + epNo + "</span>" +
                "<span class='td'><div class='UPop'><img class='th_icon' src='" + thisAlbumArtImage + "'/>" +
                "<div class='UPopO'><img src='" + thisAlbumArtImage + "'/></div>" +
                "</div></span>" +
                "<span class='td' style='" + mediaPlayColor(xf.PlayCount) + "'><div class='UPop'>" + epTitle +
                "<div class='UPopO'>" + xf.Description + "</div></div></span>" +
                "<span class='td'>" + mythEp + "</span>" +
                "</div>";
    });
    rData += "</div>";
    dojo.byId(target).innerHTML = rData;
}

