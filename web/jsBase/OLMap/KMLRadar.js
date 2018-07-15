/* 
 by Anthony Stump
 Created: 15 Jul 2018
 */

function generateRadarKml(radarList, mobiLoc) {
    var inBoundsN = 0;
    var inBoundsS = 0;
    var inBoundsE = 0;
    var inBoundsW = 0;
    var opacity = 0;
    var oColor = 0;
    var inBounds = "";
    var kmlData = '<?xml version="1.0" encoding="UTF-8"?>' +
            '<kml xmlns="http://earth.google.com/kml/2.0">' +
            '<Document>';
    radarList.forEach(function (tRad) {
        var bounds = tRad.BoundsNSEW;
        if(mobiLoc[0] < bounds[0] - 2) { inBoundsN = 1; } var nCheck = "[if " + mobiLoc[0] + " vs " + bounds[0] - 2 + "]";
        if(mobiLoc[0] > bounds[1] + 2) { inBoundsS = 1; } var sCheck = "[if " + mobiLoc[0] + " vs " + bounds[1] + 2 + "]";
        if(mobiLoc[1] < bounds[2] - 2) { inBoundsE = 1; } var eCheck = "[if " + mobiLoc[1] + " vs " + bounds[2] - 2 + "]";
        if(mobiLoc[1] > bounds[3] + 2) { inBoundsW = 1; } var wCheck = "[if " + mobiLoc[1] + " vs " + bounds[3] + 2 + "]";
        if(inBoundsN === 1 && inBoundsS === 1 && inBoundsE === 1 && inBoundsW === 1) {
            opacity = 1;
            oColor = 73;
            inBounds = "yes";
        } else {
            opacity = 0.8;
            oColor = 26;
            inBounds = "no";
        }
        var debugString = "(N: " + inBoundsN + " " + nCheck +"," +
                "S: " + inBoundsS + " " + sCheck +"," +
                "E: " + inBoundsE + " " + eCheck +"," +
                "W: " + inBoundsW + " " + wCheck +")";
        if(!checkMobile() && opacity === 1) {
            rData += "<GroundOverlay>" +
                    "<name>" + tRad.Site + " INBOUNDS: " + inBounds + " " + debugString + "</name>" +
                    "<Icon>" +
                    "<href>" + getBasePath("get") + "/Radar/" + tRad.Site + "/_BLoop.gif</href>" +
                    "<refreshMode>onInterval</refreshMode>" +
                    "<refreshInterval>120</refreshInterval>" +
                    "</Icon>" +
                    "<visibility>" + (0.6 * opacity) + "</visibility>" +
                    "<color>" + color + "</color>" + 
                    "<LatLonBox>" +
                    "<north>" + bounds[0] + "</north>" +
                    "<south>" + bounds[1] + "</south>" +
                    "<east>" + bounds[2] + "</east>" +
                    "<west>" + bounds[3] + "</west>" +
                    "</LatLonBox>" +
                    "</GroundOverlay>";
        }
    });
    return kmlData;
}


