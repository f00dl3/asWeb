/* 
by Anthony Stump
FBook.js Created: 23 Mar 2018
FBook/Blue.js Split: 4 Apr 2018
Updated: 16 Jul 2020
 */

function displayBlue() {
    getBlue("Upper");
    $("#FBBlue").toggle();
    $("#FBAuto").hide();
    $("#FBAutoHC").hide();
    $("#FBAsset").hide();
    $("#FBBills").hide();
    $("#FBCheck").hide();
    $("#FBWorkPTO").hide();
    $("#FBUUse").hide();
    $("#FBStocks").hide();
}

function getBlue(level) {
    aniPreload("on");
    var thePostData = "doWhat=getMeasure" +
            "&level="+level;
    var xhArgs = {
        preventCache: true,
        url: getResource("Home"),
        postData: thePostData,
        handleAs: "json",
        timeout: timeOutMilli,
        load: function (data) {
            putBlueprint(data);
            aniPreload("off");
        },
        error: function (data, iostatus) {
            aniPreload("off");
            window.alert("xhrGet for WorkPTO FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
        }
    };
    dojo.xhrPost(xhArgs);
}

function putBlueprint(blueData) {
    var scale = 2, width = 1280, height = 800;
    var rData = "<h3>House diagrams</h3>" +
            "<h4>Work in progress</h4>" +
            "<em>SVG is drawn from database. Image height/width set in JavaScript page, dimensions" +
            " subtracted from height/width maximum. Wall values in database are entered using" +
            " offset from max (or northeast corner of the house) in inches.</em>" +
            "<p><strong>WARNING: This project has been dead since June 16th, 2014!<strong>" +
            "<br/><em>Scale: " + scale + " pixels = 1 inch.</em>";
    var ulData = "<h4>Upper Level</h4>" +
            "<svg width=" + width + " height=" + height + ">";
    blueData.forEach(function (tbp) {
        var brush, seeThru;
        brush = seeThru = "";
        var x1 = (width - 100) - (scale * tbp.x1);
        var x2 = (width - 100) - (scale * tbp.x2);
        var y1 = (height - 100) - (scale * tbp.y1);
        var y2 = (height - 100) - (scale * tbp.y2);
        var tWidth = (scale * tbp.Width);
        var tType = tbp.Type;
        switch (tType) {
            case "Door":
                brush = "#993300";
                seeThru = 2;
                break;
            case "Wall":
                brush = "White";
                seeThru = 0.6;
                break;
            case "Window":
                brush = "#3399ff";
                seeThru = 1;
                break;
        }
        var tSvg = "<line " +
                " x1=" + x1 +
                " y1=" + y1 +
                " x2=" + x2 +
                " y2=" + y2 +
                " style='" +
                " stroke: " + brush + ";" +
                " stroke-opacity:" + seeThru + ";" +
                " stroke-width:" + tWidth + ";'/>";
        ulData += tSvg;
    });
    rData += ulData + "</svg>";
    dojo.byId("FBBlue").innerHTML = rData;
}
