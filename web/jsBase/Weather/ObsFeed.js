/* 
by Anthony Stump
Created: 5 Mar 2018
Updated: 12 Jul 2018
 */

function getObsData(targetDiv, displayType) {
    aniPreload("on");
    var obsJson = getResource("Wx");
    var obsJsonLastPostData = "doWhat=getObsJsonLast";
    var arObsJsonMq = {
        preventCache: true,
        url: obsJson,
        postData: obsJsonLastPostData,
        handleAs: "json",
        timeout: timeOutMilli,
        load: function(data) {
            var theData = JSON.parse(data[0].jsonSet);
            var nowObsId = data[0].ObsID;
            var indoorObs = data.indoorObs;
            var subXhr = dojo.xhrPost(arObsJson);
            subXhr.then(
                function(data) {
                    lastData = JSON.parse(data[0].jsonData).KOJC;
                    switch(displayType) {
                        case "marquee":
                            processMarqueeData(theData, lastData, targetDiv);
                            break;
                        case "static":
                            processObservationData(nowObsId, theData, lastData, indoorObs);
                            $(targetDiv).html(data.WxObs);
                            break;
                    }
                },
                function(error) {
                    lastData = "";
                }
            );
            aniPreload("off");
        },
        error: function(data, iostatus) {
            aniPreload("off");
            console.log("xhrGet obsJson: FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
        }
    };
    
    var dateOverrideStart = getDate("hour", -2, "full"); 
    var dateOverrideEnd = getDate("hour", 0, "full");
    var obsJsonPostData = "doWhat=getObjsJson" +
        "&startTime=" + dateOverrideStart +
        "&endTime=" + dateOverrideEnd +
        "&order=DESC" +
        "&limit=1" +
        "&stationId=KOJC";

    var arObsJson = {
        preventCache: true,
        url: obsJson,
        postData: obsJsonPostData,
        handleAs: "json",
        timeout: timeOutMilli
    };
    dojo.xhrPost(arObsJsonMq);
}


function getObsDataMerged(targetDiv, displayType) {
    var timeout = getRefresh("medium");
    aniPreload("on");
    var dateOverrideStart = getDate("hour", -1, "full"); 
    var dateOverrideEnd = getDate("hour", 0, "full");
    var obsJson = getResource("Wx");
    var obsJsonPostData = "doWhat=getObsJsonMerged" +
        "&startTime=" + dateOverrideStart +
        "&endTime=" + dateOverrideEnd +
        "&order=DESC" +
        "&limit=1" +
        "&stationId=KOJC";
    var arObsJsonMq = {
        preventCache: true,
        url: obsJson,
        postData: obsJsonPostData,
        handleAs: "json",
        timeout: timeOutMilli,
        load: function(data) {
            var lastData;
            var theData = JSON.parse(data.wxObsNow[0].jsonSet);
            var nowObsId = data.wxObsNow[0].ObsID;
            if(isSet(data.wxObsM1H[0])) { lastData = JSON.parse(data.wxObsM1H[0].jsonSet); } else { lastData = theData; }
            var indoorObs = data.indoorObs;
            switch(displayType) {
                case "marquee":
                    processMarqueeData(theData, lastData, targetDiv);
                    break;
                case "static":
                    processObservationData(nowObsId, theData, lastData, indoorObs, targetDiv);
                    $(targetDiv).html(data.WxObs);
                    break;
            }
            aniPreload("off");
        },
        error: function(data, iostatus) {
            aniPreload("off");
            console.log("xhrGet obsJson: FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
        }
    };
    dojo.xhrPost(arObsJsonMq);
    setTimeout(function() { getObsDataMerged(targetDiv, displayType); }, timeout);
}

function processMarqueeData(theData, lastData, targetDiv) {
    if(theData === "") { console.log("ERROR fetching ThisMarqData"); }
    if(lastData === "") { console.log("ERROR fetching LastMarqData"); }
    var returnData = "";
    var theTemperature = theData.Temperature;
    var stationId = theData.Station;
    var getTime = theData.GetTime;
    if(!isSet(theTemperature)) {
        returnData += "<div id='WxObsMarq'>";
        returnData += "<strong>WARNING! " + stationId + " [Marq] data unavailable!</strong>";
        returnData += "<br/>Fetch timestamp: " + getTime;
    } else {
        var diffTemperature = parseInt(theData.Temperature) - parseInt(lastData.Temperature);
        var diffDewpoint = parseInt(theData.Dewpoint) - parseInt(lastData.Dewpoint);
        var diffPressure = parseInt(theData.Pressure) - parseInt(lastData.Pressure);
        var gust = "";
        var shortTime = wxShortTime(theData.TimeString);
        if(!isSet(theData.Dewpoint)) { theData.Dewpoint = theData.Temperature; }
        if(isSet(theData.WindGust)) { gust = ", gusting to <span style=''>" + theData.WindGust + " mph</span>"; }
        var mqJson = {'v1':theData.Temperature, 'v2':theData.Dewpoint};
        var marqStyle = color2Grad("T", "right", mqJson);
        var rSpeed = parseInt(theData.WindSpeed) + 5;
        var cSpeed = parseInt(theData.WindSpeed) + 13;
        var flTemp = wxObs("Feel", theData.TimeString, theData.Temperature, theData.WindSpeed, theData.RelativeHumidity, theData.Weather);
        var flTempR = wxObs("Feel", theData.TimeString, theData.Temperature, rSpeed, theData.RelativeHumidity, theData.Weather);
        var flTempC = wxObs("Feel", theData.TimeString, theData.Temperature, cSpeed, theData.RelativeHumidity, theData.Weather);
        returnData += "<div id='WxObsMarq' style='" + marqStyle + "'>" +
            shortTime +
            "<img class='th_icon' src='" + getBasePath("icon") + "/wx/" + wxObs("Icon", theData.TimeString, null, null, null, theData.Weather) + ".png' /> " + theData.Weather + " " +
            " " + animatedArrow(diffTemperature) + Math.round(theData.Temperature) + "F (" + diffTemperature + "F/hr)" +
            " " + animatedArrow(diffDewpoint) + Math.round(theData.Dewpoint) + "F ( " + diffDewpoint + "F/hr) - " +
            " RH: <span style='" + styleRh(theData.RelativeHumidity) + "'>" + theData.RelativeHumidity + "%</span> - ";
        if(isSet(theData.WindSpeed)) {
            returnData += " Wind: ";
            if(isSet(theData.WindDirection)) { returnData += theData.WindDirection + " at "; }
            returnData += "<span style='" + styleWind(theData.WindSpeed) + "'>" + theData.WindSpeed + " mph</span> " + gust + " - ";
        }
        returnData += " Feel: <span style='" + styleTemp(flTemp) + "'>" + flTemp + "F</span>" +
            " (<span style='" + styleTemp(flTempC) + "'>" + flTempC + "F <img class='th_icon' src='" + getBasePath("icon") + "/ic_cyc.jpeg'/></span>) - ";
        if(isSet(theData.CAPE)) {
            returnData += "<span style=" + styleCape(theData.CAPE) + ">" + theData.CAPE + "</span> - ";
        }
        returnData += " MSLP: " + animatedArrow(diffPressure) + theData.Pressure + " <span>mb</span> --- ";
    }
    returnData += "</div>";
    dojo.byId(targetDiv).innerHTML = returnData;
    $("#WxObsMarq").marquee();
}

function processObservationData(nowObsId, theData, lastData, indoorObs, targetDiv) {
    if(theData === "") { console.log("ERROR fetching ThisObsData"); }
    if(lastData === "") { console.log("ERROR fetching LastObsData"); }
    var returnData = "";
    var theTemperature = theData.Temperature;
    var stationId = "KOJC"; // forced
    var getTime = theData.GetTime;
    var indoorTemp = Math.round(0.93 * conv2Tf(indoorObs[0].ExtTemp/1000));
    var indoorPiTemp = Math.round(indoorObs[1].ExtTemp);
    var indoorPi2Temp = Math.round(indoorObs[2].ExtTemp);
    if(!isSet(theTemperature)) {
        returnData += "<div id='LWObs'>";
        returnData += "<strong>WARNING! " + stationId + " [Obs] data unavailable!</strong>";
        returnData += "<br/>Fetch timestamp: " + getTime;
    } else {
        var diffTemperature = parseInt(theData.Temperature) - parseInt(lastData.Temperature);
        var diffDewpoint = parseInt(theData.Dewpoint) - parseInt(lastData.Dewpoint);
        var diffPressure = parseInt(theData.Pressure) - parseInt(lastData.Pressure);
        var gustLine = "";
        var shortTime = wxShortTime(theData.TimeString);
        if(!isSet(theData.Dewpoint)) { theData.Dewpoint = theData.Temperature; }
        if(isSet(theData.WindGust)) { gustLine = "<br/>Winds gusting to <span style='" + styleWind(theData.WindGust) + "'>" + theData.WindGust + " mph</span>"; }
        var passJson = {'v1':theData.Temperature, 'v2':theData.Dewpoint};
        var oDivStyle = color2Grad("T", "right", passJson);
        var rSpeed = parseInt(theData.WindSpeed) + 5;
        var cSpeed = parseInt(theData.WindSpeed) + 13;
        var flTemp = wxObs("Feel", theData.TimeString, theData.Temperature, theData.WindSpeed, theData.RelativeHumidity, theData.Weather);
        var flTempR = wxObs("Feel", theData.TimeString, theData.Temperature, rSpeed, theData.RelativeHumidity, theData.Weather);
        var flTempC = wxObs("Feel", theData.TimeString, theData.Temperature, cSpeed, theData.RelativeHumidity, theData.Weather);
        returnData += "<div id='LWObs' style='" + oDivStyle + "'>" +
            "<div class='UPopNM'><strong>" + shortTime + "</strong>" +
            "<div class='UPopNMO'>" +
            "<a href='" + getBasePath("ui") + "/WxStation.jsp' target='new'>JSON</a><br/>" +
            "Obs #: " + nowObsId + " station " + stationId + "<br/>" +
            "Loaded: " + getDate("minute", 0, "full") + "</div></div>" +
            "<br/><div class='UPopNM'>" +
            "<img class='th_small' src='" + getBasePath("icon") + "/wx/" + wxObs("Icon", theData.TimeString, null, null, null, theData.Weather) + ".png' />" +
            processUpperAirData(null, theData) + "</div><br/>" +
            "<div class='UPop'>" + theData.Weather +
            "<div class='UPopO'>" +
            "Visibility: " + theData.Visibility + " mi.<br/>" +
            "Pressure: " + animatedArrow(diffPressure) + Math.round(theData.Pressure) + " mb." +
            "</div></div><br/>" +
            "<div class='UPop'>" + animatedArrow(diffTemperature) + 
            "<span style='" + styleTemp(theData.Temperature) + "'>" + Math.round(theData.Temperature) + "F</span>" +
            "<div class='UPopO'>(" + diffTemperature + "F/hr)</div></div>/" +
            "<div class='UPop'>" + animatedArrow(diffDewpoint) + 
            "<span style='" + styleTemp(theData.Dewpoint) + "'>" + Math.round(theData.Dewpoint) + "F</span>" +
            "<div class='UPopO'>(" + diffDewpoint + "F/hr)</div></div>" +
            "<br/>RH: <span style='" + styleRh(theData.RelativeHumidity) + "'>" + theData.RelativeHumidity + "%</span>" +
            " (<div class='UPop'><span style='" + styleTemp(flTemp) + "'>" + flTemp + "F</span>" +
            "<div class='UPopO'>" +
            "<button style='" + styleTemp(indoorTemp) + "'><img class='th_icon' src='" + getBasePath("icon") + "/ic_home.gif'/>" + indoorTemp + "F</button><br/>" +
            "<button style='" + styleTemp(indoorPiTemp) + "'><img class='th_icon' src='" + getBasePath("icon") + "/ic_gar.png'/>" + indoorPiTemp + "F</button><br/>" +
            "<button style='" + styleTemp(indoorPi2Temp) + "'><img class='th_icon' src='" + getBasePath("icon") + "/ic_off.jpeg'/>" + indoorPi2Temp + "F</button><br/>" +
            "<button style='" + styleTemp(flTempR) + "'><img class='th_icon' src='" + getBasePath("icon") + "/ic_run.jpeg'/>" + flTempR + "F</button><br/>" +
            "<button style='" + styleTemp(flTempC) + "'><img class='th_icon' src='" + getBasePath("icon") + "/ic_cyc.jpeg'/>" + flTempC + "F</button><br/>" +
            "<br/>As of: " + indoorObs[0].WalkTime + "</div></div>)<br/>"; 
        if(isSet(theData.WindSpeed)) {
            returnData += "Wind: "; if(isSet(theData.WindDirection)) { returnData += theData.WindDirection + " at "; }
            returnData += "<span style='" + styleWind(theData.WindSpeed) + "'>" + theData.WindSpeed + " mph</span>" + gustLine + "<br/>";
        }
        if(isSet(theData.CAPE)) { returnData += "CAPE: <span style=" + styleCape(theData.CAPE) + ">" + theData.CAPE + "</span><br/>"; }
    }
    returnData += "</div>";        
    dojo.byId(targetDiv).innerHTML = returnData;
}

function processUpperAirData(baseEle, stationData, noWrappingDiv) {
    var stId = stationData.Station;
    var sfcFt = 0.0;
    var h2eMap = heights2Elevations("elev", "b2t");
    var heights = heights2Elevations("height", "b2t");
    var theLastT, theLastD, theLastW, theLastH, doSounding;
    if(isSet(baseEle)) {
        sfcFt = ((((1-Math.pow(baseEle/1013.25), 0.190284))*145366.45)/1000).toFixed(1);
    } else {
        baseEle = 1013;
    }
    if(isSet(stationData.T950)) {
        if(!isSet(stationData.T975)) { 
            stationData.T975 = stationData.T1000;
        }
        var obsData = parseWxObs(stationData);
        var doSoundingMin = "<table>";
        if(isSet(obsData.H900T)) {
            var tA100 = [ obsData.H100T ]; var dA100 = [ obsData.H100D ];
            var hA100 = [ obsData.H100H ]; var wA100 = [ obsData.H100WS ];
            var hA150 = [ obsData.H100H, obsData.H125H, obsData.H150H ]; var wA150 = [ obsData.H100WS, obsData.H125WS, obsData.H150WS ];
            var tA150 = [ obsData.H100T, obsData.H125T, obsData.H150T ]; var dA150 = [ obsData.H100D, obsData.H125D, obsData.H150D ];
            var hA150 = [ obsData.H100H, obsData.H125H, obsData.H150H ]; var wA150 = [ obsData.H100WS, obsData.H125WS, obsData.H150WS ];
            var tA200 = [ obsData.H150T, obsData.H175T, obsData.H200T ]; var dA200 = [ obsData.H150D, obsData.H175D, obsData.H200D ];
            var hA200 = [ obsData.H150H, obsData.H175H, obsData.H200H ]; var wA200 = [ obsData.H150WS, obsData.H175WS, obsData.H200WS ];
            var tA250 = [ obsData.H200T, obsData.H225T, obsData.H250T ]; var dA250 = [ obsData.H200D, obsData.H225D, obsData.H250D ];
            var hA250 = [ obsData.H200H, obsData.H225H, obsData.H250H ]; var wA250 = [ obsData.H200WS, obsData.H225WS, obsData.H250WS ];
            var tA300 = [ obsData.H250T, obsData.H275T, obsData.H300T ]; var dA300 = [ obsData.H250D, obsData.H275D, obsData.H300D ];
            var hA300 = [ obsData.H250H, obsData.H275H, obsData.H300H ]; var wA300 = [ obsData.H250WS, obsData.H275WS, obsData.H300WS ];
            var tA350 = [ obsData.H300T, obsData.H325T, obsData.H350T ]; var dA350 = [ obsData.H300D, obsData.H325D, obsData.H350D ];
            var hA350 = [ obsData.H300H, obsData.H325H, obsData.H350H ]; var wA350 = [ obsData.H300WS, obsData.H325WS, obsData.H350WS ];
            var tA400 = [ obsData.H350T, obsData.H375T, obsData.H400T ]; var dA400 = [ obsData.H350D, obsData.H375D, obsData.H400D ];
            var hA400 = [ obsData.H350H, obsData.H375H, obsData.H400H ]; var wA400 = [ obsData.H350WS, obsData.H375WS, obsData.H400WS ];
            var tA450 = [ obsData.H400T, obsData.H425T, obsData.H450T ]; var dA450 = [ obsData.H400D, obsData.H425D, obsData.H450D ];
            var hA450 = [ obsData.H400H, obsData.H425H, obsData.H450H ]; var wA450 = [ obsData.H400WS, obsData.H425WS, obsData.H450WS ];
            var tA500 = [ obsData.H450T, obsData.H475T, obsData.H500T ]; var dA500 = [ obsData.H450D, obsData.H475D, obsData.H500D ];
            var hA500 = [ obsData.H450H, obsData.H475H, obsData.H500H ]; var wA500 = [ obsData.H450WS, obsData.H475WS, obsData.H500WS ];
            var tA550 = [ obsData.H500T, obsData.H525T, obsData.H550T ]; var dA550 = [ obsData.H500D, obsData.H525D, obsData.H550D ];
            var hA550 = [ obsData.H500H, obsData.H525H, obsData.H550H ]; var wA550 = [ obsData.H500WS, obsData.H525WS, obsData.H550WS ];
            var tA600 = [ obsData.H550T, obsData.H575T, obsData.H600T ]; var dA600 = [ obsData.H550D, obsData.H575D, obsData.H600D ];
            var hA600 = [ obsData.H550H, obsData.H575H, obsData.H600H ]; var wA600 = [ obsData.H550WS, obsData.H575WS, obsData.H600WS ];
            var tA650 = [ obsData.H600T, obsData.H625T, obsData.H650T ]; var dA650 = [ obsData.H600D, obsData.H625D, obsData.H650D ];
            var hA650 = [ obsData.H600H, obsData.H625H, obsData.H650H ]; var wA650 = [ obsData.H600WS, obsData.H625WS, obsData.H650WS ];
            var tA700 = [ obsData.H650T, obsData.H675T, obsData.H700T ]; var dA700 = [ obsData.H650D, obsData.H675D, obsData.H700D ];
            var hA700 = [ obsData.H650H, obsData.H675H, obsData.H700H ]; var wA700 = [ obsData.H650WS, obsData.H675WS, obsData.H700WS ];
            var tA750 = [ obsData.H700T, obsData.H725T, obsData.H750T ]; var dA750 = [ obsData.H700D, obsData.H725D, obsData.H750D ];
            var hA750 = [ obsData.H700H, obsData.H725H, obsData.H750H ]; var wA750 = [ obsData.H700WS, obsData.H725WS, obsData.H750WS ];
            var tA800 = [ obsData.H750T, obsData.H775T, obsData.H800T ]; var dA800 = [ obsData.H750D, obsData.H775D, obsData.H800D ];
            var hA800 = [ obsData.H750H, obsData.H775H, obsData.H800H ]; var wA800 = [ obsData.H750WS, obsData.H975WS, obsData.H800WS ];
            var tA850 = [ obsData.H800T, obsData.H825T, obsData.H850T ]; var dA850 = [ obsData.H800D, obsData.H825D, obsData.H850D ];
            var hA850 = [ obsData.H800H, obsData.H825H, obsData.H850H ]; var wA850 = [ obsData.H800WS, obsData.H825WS, obsData.H850WS ];
            var tA900 = [ obsData.H850T, obsData.H875T, obsData.H700T ]; var dA900 = [ obsData.H850D, obsData.H875D, obsData.H900D ];
            var hA900 = [ obsData.H850H, obsData.H875H, obsData.H900H ]; var wA900 = [ obsData.H850WS, obsData.H875WS, obsData.H900WS ];
            var tA950 = [ obsData.H900T, obsData.H925T, obsData.H950T ]; var dA950 = [ obsData.H900D, obsData.H925D, obsData.H950D ];
            var hA950 = [ obsData.H900H, obsData.H925H, obsData.H950H ]; var wA950 = [ obsData.H900WS, obsData.H925WS, obsData.H950WS ];
            var tA1000 = [ obsData.H950T, obsData.H975T, obsData.H1000T ]; var dA1000 = [ obsData.H950D, obsData.H975D, obsData.H1000D ];
            var hA1000 = [ obsData.H950H, obsData.H975H, obsData.H1000H ]; var wA1000 = [ obsData.H950WS, obsData.H975WS, obsData.H1000WS ];
            for(var i = h2eMap.length; i >= 0; i -= 1) {
                if(isSet(heights[i])) {
                    if(isSet(baseEle) || baseEle >= heights[i]) {
                        var dht = "H" + heights[i] + "T";
                        var dhd = "H" + heights[i] + "D";
                        var dhh = "H" + heights[i] + "H";
                        var dhws = "H" + heights[i] + "WS";
                        var dhwv = "H" + heights[i] + "WV";
                        doSoundingMin += "<tr>" +
                                "<td>+" + (h2eMap[i] - sfcFt) + " K</td>" +
                                "<td style='" + styleTemp(obsData[dht]) + "'>" + obsData[dht] + "</td>" + 
                                "<td style='" + styleTemp(obsData[dhd]) + "'>" + obsData[dhd] + "</td>" + 
                                "<td style='" + styleRh(obsData[dhh]) + "'>" + obsData[dhh] + "</td>" + 
                                "<td style='" + styleWind(obsData[dhws]) + "'>" + obsData[dhwv] + " " + obsData[dhws] + "</td>" + 
                                "</tr>";
                        theLastT = obsData[dht]; theLastD = obsData[dhd]; theLastW = obsData[dhws]; theLastH = obsData[dhh];
                    }
                }
            }
            if(!isSet(theLastT)) { theLastT = obsData.SfcT; }
            if(!isSet(theLastD)) { theLastD = obsData.SfcD; }
            if(!isSet(theLastH)) { theLastH = obsData.SfcH; }
            if(!isSet(theLastW)) { theLastW = obsData.SfcWS; }
            if(isSet(theLastT) && isSet(theLastD) && isSet(theLastH) && isSet(theLastW)) {
                var tASurface = [ theLastT, obsData.SfcT ]; var dASurface = [ theLastD, obsData.SfcD ];
                var hASurface = [ theLastH, obsData.SfcH ]; var wASurface = [ theLastW, obsData.SfcWS ];
                doSoundingMin += "<tr><td>SFC</td>" + // Not fully functional
                    "<td style='" + styleTemp(obsData.SfcT) + "'>" + obsData.SfcT + "</td>" +
                    "<td style='" + styleTemp(obsData.SfcD) + "'>" + obsData.SfcD + "</td>" +
                    "<td style='" + styleRh(obsData.SfcH) + "'>" + obsData.SfcH + "</td>" +
                    "<td style='" + styleWind(obsData.SfcWS) + "'>" + obsData.SfcWV + " " + obsData.SfcWS + "</td>" +
                    "</tr>";
            }
            if(isSet(stationData.CAPE)) {
                doSoundingMin += "</table><table>" +
                        shAtParAdd("CAPE", "style", styleCape(stationData.CAPE), stationData.CAPE + " J/Kg", stId, "CAPE") +
                        shAtParAdd("CIN Height", "class", colorCin(stationData.CIN), stationData.CIN + " J/Kg", stId, "CIN") +
                        shAtParAdd("Lift Cond Lvl", "class", colorLcl(stationData.SLCL), Math.round(stationData.SLCL) + " m", stId, "SLCL");
                if(isSet(stationData.FZLV)) { doSoundingMin += shAtParAdd("Freezing Level", null, null, (stationData.FZLV/1000).toFixed(1) + " K Ft", stId, "FZL"); }
                if(isSet(stationData.WZLV)) { doSoundingMin += shAtParAdd("Freezing WBulb", null, null, (stationData.WZLV/1000).toFixed(1) + " K Ft", stId, "WZL"); }
                if(isSet(stationData.LI)) { doSoundingMin += shAtParAdd("Lifted Index", "style", styleLi(stationData.LI), stationData.LI, stId, "LI"); }
                if(isSet(stationData.CCL)) { doSoundingMin += shAtParAdd("Conv Cond Lvl", null, null, Math.round(stationData.CCL) + " m", stId, "CCL"); }
                if(isSet(stationData.PWAT)) { doSoundingMin += shAtParAdd("Precip Water", "style", styleLiquid(stationData.PWAT), stationData.PWAT + " in", stId, "PWAT"); }
                doSoundingMin += "</table><a href='" + doCh("p", "WxLevel", "Station="+stId) + " target='new'>Height Levels</a>";
            }
        }
        doSounding = doSoundingMin;
        if(!isSet(noWrappingDiv)) { doSounding = "<div class='UPopNMO'>" + doSounding + "</div>"; }
    } else {
        doSounding = "DATA ERROR FOR HOUR";
        if(!isSet(noWrappingDiv)) { doSounding = "<div class='UPopNMO'>" + doSounding + "</div>"; }
    }
    return doSounding;
}

function shAtParAdd(longName, sType, styling, dataIn, stId, param) {
    var tdStyling = "";
    if(isSet(styling)) { tdStyling = " " + sType + "='" + styling + "'"; }
    var rData = "<tr><td>" + longName + "</td><td" + tdStyling + ">" + dataIn + "</td>" +
            "<td><a href='" + doCh("p", "WxXML", "Station=" + stId + "&Param=" + param) + "' target='new'>" + playIcon + "</a></td></tr>";
    return rData;
}