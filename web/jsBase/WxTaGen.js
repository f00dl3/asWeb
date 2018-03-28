/* 
by Anthony Stump
Created: 27 Mar 2018
 */

function genXjTableData(tEle, obsJson) {
    var obsData;
    var theStation = obsJson.Station;
    var sfcFt = 0.0;
    var h2eMap = heights2Elevations("elev", "b2t");
    if(isSet(tEle)) {
        var theElev = tEle;
        sfcFt = Math.round((((1-Math.pow(theElev/1013.25),0.190284))*145366.45)/1000);
    };
    var initHthCols = [ "Timestamp<br/>Elevation", "SFC<br/>" + sfcFt + "K" ];
    var htHead = "<table><thead><tr>";
    for(i = 0; i < initHthCols.length; i++) { htHead += "<th>" + initHthCols[i] + "</th>"; }
    var hit = 0;
    for(var hgt = 1000; hgt >= 100; hgt -= 25) {
        if(!isSet(theElev) || theElev >= hgt) {
            htHead += "<th>" + hgt + "<br/>" + (h2eMap[hit] - sfcFt) + " K</th>";
        }
        hit++;
    }
    htHead += "</thead><tbody>";
    var htFoot = "</tbody></table>";
    // exec Order
    obsJson.forEach(function (stationData) {
        if(!isSet(stationData.Dewpoint)) {
            if(!isSet(stationData.D0)) {
                stationData.Dewpoint = conv2Tf(stationData.D0);
            } else {
                stationData.Dewpoint = stationData.Temperature;
            }
        }
        obsData = parseWxObs(stationData);
    });
    var rData = "<div id='XMLTableHolder'></div>";
    var wxTableT = "<div id='WxTableT'><h3>" + theStation + " Tempeartures</h3>" + htHead;
    obsData.forEach(function (tcr) {
        var shortTime = wxShortTime(tcr.TimeString);
        var srData = "<tr><td>" + shortTime + "</td>" +
                "<td style='" + styleTemp(tcr.SfcT) + "'>" + tcr.SfcT + "</td>";        
        for(var hgt = 1000; hgt >= 100; hgt -= 25) {
            if(!isSet(theElev) || theElev >= hgt) {
                var dyVar = "H" + hgt + "T";
                var dyVarM1 = "H" + (hgt-25) + "T";
                var taData = [ tcr[dyVar], tcr[dyVarM1] ];
                srData += "<td style='" + color2Grad("T", "right", taData) + "'>" + tcr[dyVar] + "</td>";
            }
        }
        wxTableT += srData;
    });
    wxTableT += htFoot + "</div>";
    var wxTableH = "<div id='WxTableH'><h3>" + theStation + " Humidity</h3>" + htHead;   
    obsData.forEach(function (tcr) {
        var shortTime = wxShortTime(tcr.TimeString);
        var srData = "<tr><td>" + shortTime + "</td>" +
                "<td style='" + styleRh(tcr.SfcH) + "'></td>";        
        for(var hgt = 1000; hgt >= 100; hgt -= 25) {
            if(!isSet(theElev) || theElev >= hgt) {
                var dyVar = "H" + hgt + "H";
                var dyVarM1 = "H" + (hgt-25) + "H";
                var taData = [ tcr[dyVar], tcr[dyVarM1] ];
                srData += "<td style='" + color2Grad("H", "right", taData) + "'></td>";
            }
        }
        wxTableH += srData;
    });
    wxTableH += htFoot + "</div>";
    var wxTableW = "<div id='WxTableW'><h3>" + theStation + " Wind Directions & Speed</h3>" + htHead;
    obsData.forEach(function (tcr) {
        var shortTime = wxShortTime(tcr.TimeString);
        var srData = "<tr><td>" + shortTime + "</td>" +
                "<td style='" + styleWind(tcr.SfcWS) + "'>" + tcr.SfcWS + "</td>";        
        for(var hgt = 1000; hgt >= 100; hgt -= 25) {
            if(!isSet(theElev) || theElev >= hgt) {
                var dyVarS = "H" + hgt + "WS";
                var dyVarV = "H" + hgt + "WV";
                var dyVarSM1 = "H" + (hgt-25) + "WS";
                var taData = [ tcr[dyVarS], tcr[dyVarSM1] ];
                srData += "<td style='" + color2Grad("W", "right", taData) + "'>" + tcr[dyVarV] + "<br/>" + tcr[dyVarS] + "</td>";
            }
        }
        wxTableW += srData;
    });
    wxTableW += htFoot + "</div>";
    rData += wxTableT + wxTableH + wxTableW;
    return rData;
}


