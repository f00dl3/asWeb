/* 
by Anthony Stump
Created: 5 Mar 2018
Updated: 26 Mar 2018

 */

var playIcon = "<img class='th_icon' src='" + getBasePath("icon") + "/ic_ply.png' />";

function getObsData(targetDiv, displayType) {
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
            var lastData;
            var subXhr = dojo.xhrPost(arObsJson);
            subXhr.then(
                function(data) {
                    lastData = JSON.parse(data[0].jsonData).KOJC;
                    switch(displayType) {
                        case "Marquee":
                            processMarqueeData(theData, lastData);
                            $(targetDiv).html(data.WxObsMarq).marquee();
                            break;
                        case "Static":
                            processObservationData(nowObsId, theData, lastData, indoorObs);
                            $(targetDiv).html(data.WxObs);
                            break;
                    }
                },
                function(error) {
                    lastData = "";
                }
            );
        },
        error: function(data, iostatus) {
            window.alert("xhrGet obsJson: FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
        }
    };
    
    var dateOverrideStart = getDate("hour", -1, "full"); 
    var dateOverrideEnd = getDate("hour", 0, "full");
    var obsJsonPostData = "doWhat=getObjsJson" +
        "&startTime=" + dateOverrideStart +
        "&endTime=" + dateOverrideEnd +
        "&limit=1";

    var arObsJson = {
        preventCache: true,
        url: obsJson,
        postData: obsJsonPostData,
        handleAs: "json",
        timeout: timeOutMilli
    };
    dojo.xhrPost(arObsJsonMq);
    console.log(obsJsonPostData);
}

function processMarqueeData(theData, lastData) {
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
        console.log(data);
    } else {
        var diffTemperature = parseInt(theData.Temperature) - parseInt(lastData.Temperature);
        var diffDewpoint = parseInt(theData.Dewpoint) - parseInt(lastData.Dewpoint);
        var diffPressure = parseInt(theData.Pressure) - parseInt(lastData.Pressure);
        console.log(diffTemperature + " " + diffPressure + " " + diffDewpoint);
        var gust = "";
        var shortTime = theData.TimeString.replace(/Last\ Updated\ on\ /g, '');
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
    dojo.byId("disHolder").innerHTML = returnData;
    console.log("returnData: " + returnData);
}

function processObservationData(nowObsId, theData, lastData, indoorObs) {
    if(theData === "") { console.log("ERROR fetching ThisObsData"); }
    if(lastData === "") { console.log("ERROR fetching LastObsData"); }
    var returnData = "";
    var theTemperature = theData.Temperature;
    var stationId = theData.Station;
    var getTime = theData.GetTime;
    var indoorTemp = Math.round(0.93 * conv2Tf(indoorObs.TempCase/1000));
    var indoorPiTemp = Math.round(indoorObs.PiExtTemp);
    var indoorPi2Temp = Math.round(indoorObs.Pi2ExtTemp);
    if(!isSet(theTemperature)) {
        returnData += "<div id='LWObs'>";
        returnData += "<strong>WARNING! " + stationId + " [Obs] data unavailable!</strong>";
        returnData += "<br/>Fetch timestamp: " + getTime;
        console.log(data);
    } else {
        var diffTemperature = parseInt(theData.Temperature) - parseInt(lastData.Temperature);
        var diffDewpoint = parseInt(theData.Dewpoint) - parseInt(lastData.Dewpoint);
        var diffPressure = parseInt(theData.Pressure) - parseInt(lastData.Pressure);
        var gustLine = "";
        var shortTime = theData.TimeString.replace(/Last\ Updated\ on\ /g, '');
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
            "<a href='" + getBasePath("old") + "/WxStation.php' target='new'>XML JSON</a><br/>" +
            "Obs #: " + nowObsId + " station " + stationId + "<br/>" +
            "Loaded: " + getDate("minute", 0, "full") + "</div></div>" +
            "<br/><div class='UPopNM'>" +
            "<img class='ic_small' src='" + getBasePath("icon") + "/wx/" + wxObs("Icon", theData.TimeString, null, null, null, theData.Weather) + ".png' />" +
            processUpperAirData(null, theData, stationId) + "</div><br/>" +
            "<div class='UPop'>" + theData.Weather +
            "<div class='UPopO'>" +
            "Visibility: " + theData.Visibility + " mi.<br/>" +
            "Pressure: " + animatedArrow(diffPressure) + Math.round(theData.Pressure) + " mb." +
            "</div></div><br/>" +
            "<div class='UPop'>" + animatedArrow(diffTemperature) + Math.round(theData.Temperature) + "F" +
            "<div class='UPopO'>(" + diffTemperature + "F/hr)</div></div>/" +
            "<div class='UPop'>" + animatedArrow(diffDewpoint) + Math.round(theData.Dewpoint) + "F +( " + diffDewpoint + "F/hr - " +
            "<div class='UPopO'>(" + diffDewpoint + "F/hr) "
            "<br/>RH: <span style='" + styleRh(theData.RelativeHumidity) + "'>" + theData.RelativeHumidity + "%</span>" +
            "</div></div> (<div class='UPop'><span style='" + styleTemp(flTemp) + "'>" + flTemp + "F</span>" +
            "<div class='UPopO'>" +
            "<button style='" + styleTemp(indoorTemp) + "'><img class='th_icon' src='" + getBasePath("icon") + "/ic_home.gif'/>" + indoorTemp + "F</button>" +
            "<button style='" + styleTemp(indoorPiTemp) + "'><img class='th_icon' src='" + getBasePath("icon") + "/ic_gar.png'/>" + indoorPiTemp + "F</button>" +
            "<button style='" + styleTemp(indoorPi2Temp) + "'><img class='th_icon' src='" + getBasePath("icon") + "/ic_off.jpeg'/>" + indoorPi2Temp + "F</button>" +
            "<button style='" + styleTemp(flTempR) + "'><img class='th_icon' src='" + getBasePath("icon") + "/ic_run.jpeg'/>" + flTempR + "F</button>" +
            "<button style='" + styleTemp(flTempC) + "'><img class='th_icon' src='" + getBasePath("icon") + "/ic_cyc.jpeg'/>" + flTempC + "F</button>" +
            "<br/>As of: " + indoorObs.WalkTime + "</div></div>)<br/>"; 
        if(isSet(theData.WindSpeed)) {
            returnData += "Wind: "; if(isSet(theData.WindDirection)) { returnData += theData.WindDirection + " at "; }
            returnData += "<span style='" + styleWind(theData.WindSpeed) + "'>" + theData.WindSpeed + " mph<br/>" + gustLine + "<br/>";
        }
        if(isSet(theData.CAPE)) { returnData += "CAPE: <span style=" + styleCape(theData.CAPE) + ">" + theData.CAPE + "</span><br/>"; }
    }
    returnData += "</div>";        
    dojo.byId("obsHolder").innerHTML = returnData;
    console.log("returnData: " + returnData);
}

function processUpperAirData(baseEle, stationData) {
    var stId = stationData.Station;
    var sfcFt = 0.0;
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
        var obsData = {
            "SfcT": stationData.Temperature, "H1000T": stationData.T1000,
            "H975T": conv2Tf(stationData.T975), "H950T": conv2Tf(stationData.T950),
            "H925T": conv2Tf(stationData.T925), "H900T": conv2Tf(stationData.T900),
            "H875T": conv2Tf(stationData.T975), "H850T": conv2Tf(stationData.T950),
            "H825T": conv2Tf(stationData.T825), "H800T": conv2Tf(stationData.T800),
            "H775T": conv2Tf(stationData.T975), "H750T": conv2Tf(stationData.T950),
            "H725T": conv2Tf(stationData.T725), "H700T": conv2Tf(stationData.T700),
            "H675T": conv2Tf(stationData.T975), "H650T": conv2Tf(stationData.T950),
            "H625T": conv2Tf(stationData.T625), "H600T": conv2Tf(stationData.T600),
            "H575T": conv2Tf(stationData.T975), "H550T": conv2Tf(stationData.T950),
            "H525T": conv2Tf(stationData.T525), "H500T": conv2Tf(stationData.T500),
            "H475T": conv2Tf(stationData.T975), "H450T": conv2Tf(stationData.T950),
            "H425T": conv2Tf(stationData.T425), "H400T": conv2Tf(stationData.T400),
            "H375T": conv2Tf(stationData.T975), "H350T": conv2Tf(stationData.T950),
            "H325T": conv2Tf(stationData.T325), "H300T": conv2Tf(stationData.T300),
            "H275T": conv2Tf(stationData.T975), "H250T": conv2Tf(stationData.T950),
            "H225T": conv2Tf(stationData.T225), "H200T": conv2Tf(stationData.T200),
            "H175T": conv2Tf(stationData.T975), "H150T": conv2Tf(stationData.T950),
            "H125T": conv2Tf(stationData.T125), "H100T": conv2Tf(stationData.T100),
            "SfcWS": stationData.WindSpeed, "H1000WS": conv2Mph(stationData.WS1000),
            "H975WS": conv2Mph(stationData.WS975),"H950WS": conv2Mph(stationData.WS950),
            "H925WS": conv2Mph(stationData.WS925), "H900WS": conv2Mph(stationData.WS900),
            "H875WS": conv2Mph(stationData.WS875), "H850WS": conv2Mph(stationData.WS850),
            "H825WS": conv2Mph(stationData.WS825), "H800WS": conv2Mph(stationData.WS800),
            "H775WS": conv2Mph(stationData.WS75), "H750WS": conv2Mph(stationData.WS950),
            "H725WS": conv2Mph(stationData.WS725), "H700WS": conv2Mph(stationData.WS700),
            "H675WS": conv2Mph(stationData.WS675), "H650WS": conv2Mph(stationData.WS650),
            "H625WS": conv2Mph(stationData.WS625), "H600WS": conv2Mph(stationData.WS600),
            "H575WS": conv2Mph(stationData.WS575), "H550WS": conv2Mph(stationData.WS550),
            "H525WS": conv2Mph(stationData.WS525), "H500WS": conv2Mph(stationData.WS500),
            "H475WS": conv2Mph(stationData.WS475), "H450WS": conv2Mph(stationData.WS450),
            "H425WS": conv2Mph(stationData.WS425), "H400WS": conv2Mph(stationData.WS400),
            "H375WS": conv2Mph(stationData.WS375), "H350WS": conv2Mph(stationData.WS350),
            "H325WS": conv2Mph(stationData.WS325), "H300WS": conv2Mph(stationData.WS300),
            "H275WS": conv2Mph(stationData.WS275), "H250WS": conv2Mph(stationData.WS250),
            "H225WS": conv2Mph(stationData.WS225), "H200WS": conv2Mph(stationData.WS200),
            "H175WS": conv2Mph(stationData.WS175), "H150WS": conv2Mph(stationData.WS150),
            "H125WS": conv2Mph(stationData.WS125), "H100WS": conv2Mph(stationData.WS100),
            "SfcWV": windDirSvg(stationData.WindDegrees), "H1000WV": windDirSvg(stationData.WD1000),
            "H975WV": windDirSvg(stationData.WD975), "H950WV": windDirSvg(stationData.WD950),
            "H925WV": windDirSvg(stationData.WD925), "H900WV": windDirSvg(stationData.WD900),
            "H875WV": windDirSvg(stationData.WD875), "H850WV": windDirSvg(stationData.WD850),
            "H825WV": windDirSvg(stationData.WD825), "H800WV": windDirSvg(stationData.WD800),
            "H775WV": windDirSvg(stationData.WD775), "H750WV": windDirSvg(stationData.WD750),
            "H725WV": windDirSvg(stationData.WD725), "H700WV": windDirSvg(stationData.WD700),
            "H675WV": windDirSvg(stationData.WD675), "H650WV": windDirSvg(stationData.WD650),
            "H625WV": windDirSvg(stationData.WD625), "H600WV": windDirSvg(stationData.WD600),
            "H575WV": windDirSvg(stationData.WD575), "H550WV": windDirSvg(stationData.WD550),
            "H525WV": windDirSvg(stationData.WD525), "H500WV": windDirSvg(stationData.WD500),
            "H475WV": windDirSvg(stationData.WD475), "H450WV": windDirSvg(stationData.WD450),
            "H425WV": windDirSvg(stationData.WD425), "H400WV": windDirSvg(stationData.WD400),
            "H375WV": windDirSvg(stationData.WD375), "H350WV": windDirSvg(stationData.WD350),
            "H325WV": windDirSvg(stationData.WD325), "H300WV": windDirSvg(stationData.WD300),
            "H275WV": windDirSvg(stationData.WD275), "H250WV": windDirSvg(stationData.WD250),
            "H225WV": windDirSvg(stationData.WD225), "H200WV": windDirSvg(stationData.WD200),
            "H175WV": windDirSvg(stationData.WD175), "H150WV": windDirSvg(stationData.WD150),
            "H125WV": windDirSvg(stationData.WD125), "H100WV": windDirSvg(stationData.WD100),
            "SfcH": relativeHumdiity(stationData.Temperature, stationData.Dewpoint), "H1000H": relativeHumidity(conv2Tf(stationData.T1000), conv2Tf(stationData.D1000)),
            "H975H": relativeHumidity(conv2Tf(stationData.T975), conv2Tf(stationData.D975)), "H950H": relativeHumidity(conv2Tf(stationData.T950), conv2Tf(stationData.D950)),
            "H925H": relativeHumidity(conv2Tf(stationData.T925), conv2Tf(stationData.D925)), "H900H": relativeHumidity(conv2Tf(stationData.T900), conv2Tf(stationData.D900)),
            "H875H": relativeHumidity(conv2Tf(stationData.T875), conv2Tf(stationData.D875)), "H850H": relativeHumidity(conv2Tf(stationData.T850), conv2Tf(stationData.D850)),
            "H825H": relativeHumidity(conv2Tf(stationData.T825), conv2Tf(stationData.D825)), "H800H": relativeHumidity(conv2Tf(stationData.T800), conv2Tf(stationData.D800)),
            "H775H": relativeHumidity(conv2Tf(stationData.T775), conv2Tf(stationData.D775)), "H750H": relativeHumidity(conv2Tf(stationData.T750), conv2Tf(stationData.D750)),
            "H725H": relativeHumidity(conv2Tf(stationData.T725), conv2Tf(stationData.D725)), "H700H": relativeHumidity(conv2Tf(stationData.T700), conv2Tf(stationData.D700)),
            "H675H": relativeHumidity(conv2Tf(stationData.T675), conv2Tf(stationData.D675)), "H650H": relativeHumidity(conv2Tf(stationData.T650), conv2Tf(stationData.D650)),
            "H625H": relativeHumidity(conv2Tf(stationData.T625), conv2Tf(stationData.D625)), "H600H": relativeHumidity(conv2Tf(stationData.T600), conv2Tf(stationData.D600)),
            "H575H": relativeHumidity(conv2Tf(stationData.T575), conv2Tf(stationData.D575)), "H550H": relativeHumidity(conv2Tf(stationData.T550), conv2Tf(stationData.D550)),
            "H525H": relativeHumidity(conv2Tf(stationData.T525), conv2Tf(stationData.D525)), "H500H": relativeHumidity(conv2Tf(stationData.T500), conv2Tf(stationData.D500)),
            "H475H": relativeHumidity(conv2Tf(stationData.T475), conv2Tf(stationData.D475)), "H450H": relativeHumidity(conv2Tf(stationData.T450), conv2Tf(stationData.D450)),
            "H425H": relativeHumidity(conv2Tf(stationData.T425), conv2Tf(stationData.D425)), "H400H": relativeHumidity(conv2Tf(stationData.T400), conv2Tf(stationData.D400)),
            "H375H": relativeHumidity(conv2Tf(stationData.T375), conv2Tf(stationData.D375)), "H350H": relativeHumidity(conv2Tf(stationData.T350), conv2Tf(stationData.D350)),
            "H325H": relativeHumidity(conv2Tf(stationData.T325), conv2Tf(stationData.D325)), "H300H": relativeHumidity(conv2Tf(stationData.T300), conv2Tf(stationData.D300)),
            "H275H": relativeHumidity(conv2Tf(stationData.T275), conv2Tf(stationData.D275)), "H250H": relativeHumidity(conv2Tf(stationData.T250), conv2Tf(stationData.D250)),
            "H225H": relativeHumidity(conv2Tf(stationData.T225), conv2Tf(stationData.D225)), "H200H": relativeHumidity(conv2Tf(stationData.T200), conv2Tf(stationData.D200)),
            "H175H": relativeHumidity(conv2Tf(stationData.T175), conv2Tf(stationData.D175)), "H150H": relativeHumidity(conv2Tf(stationData.T150), conv2Tf(stationData.D150)),
            "H125H": relativeHumidity(conv2Tf(stationData.T125), conv2Tf(stationData.D125)), "H100H": relativeHumidity(conv2Tf(stationData.T100), conv2Tf(stationData.D100)),
            "SfcD": stationData.Dewpoint, "H1000D": conv2Tf(stationData.D1000),
            "H975D": conv2Tf(stationData.D975), "H950D": conv2Tf(stationData.D950),
            "H925D": conv2Tf(stationData.D925), "H900D": conv2Tf(stationData.D900),
            "H875D": conv2Tf(stationData.D875), "H850D": conv2Tf(stationData.D850),
            "H825D": conv2Tf(stationData.D825), "H800D": conv2Tf(stationData.D800),
            "H775D": conv2Tf(stationData.D775), "H750D": conv2Tf(stationData.D750),
            "H725D": conv2Tf(stationData.D725), "H700D": conv2Tf(stationData.D700),
            "H675D": conv2Tf(stationData.D675), "H650D": conv2Tf(stationData.D650),
            "H625D": conv2Tf(stationData.D625), "H600D": conv2Tf(stationData.D600),
            "H575D": conv2Tf(stationData.D575), "H550D": conv2Tf(stationData.D550),
            "H525D": conv2Tf(stationData.D525), "H500D": conv2Tf(stationData.D500),
            "H475D": conv2Tf(stationData.D475), "H450D": conv2Tf(stationData.D450),
            "H425D": conv2Tf(stationData.D425), "H400D": conv2Tf(stationData.D400),
            "H375D": conv2Tf(stationData.D375), "H350D": conv2Tf(stationData.D350),
            "H325D": conv2Tf(stationData.D325), "H300D": conv2Tf(stationData.D300),
            "H275D": conv2Tf(stationData.D275), "H250D": conv2Tf(stationData.D250),
            "H225D": conv2Tf(stationData.D225), "H200D": conv2Tf(stationData.D200),
            "H175D": conv2Tf(stationData.D175), "H150D": conv2Tf(stationData.D150),
            "H125D": conv2Tf(stationData.D125), "H100D": conv2Tf(stationData.D100)
        };
        var doSoundingMin = "<table>";
        if(isSet(obsData.H900T)) {
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
            doSoundingMin += "<tr>" +
                    "<td>+" + (51.8 - sfcFt) + " K</td>" +
                    "<td style='" + styleTemp(obsData.H100T) + "'>" + obsData.H100T + "</td>" +
                    "<td style='" + styleTemp(obsData.H100D) + "'>" + obsData.H100D + "</td>" +
                    "<td style='" + styleRh(obsData.H100H) + "'>" + obsData.H100H + "</td>" +
                    "<td style='" + styleWind(obsData.H100WS) + "'>" + obsData.H100WV + " " + obsData.H100WS + "</td>" +
                    "</tr><tr>" +
                    "<td>+" + (44.3 - sfcFt) + " K</td>" +
                    "<td style='" + color2Grad("T", "bottom", tA150) + "'>" + obsData.H150T + "</td>" +
                    "<td style='" + color2Grad("T", "bottom", dA150) + "'>" + obsData.D150D + "</td>" +
                    "<td style='" + color2Grad("H", "bottom", hA150) + "'>" + obsData.H150H + "</td>" +
                    "<td style='" + color2Grad("W", "bottom", wA150) + "'>" + obsData.H150WV + " " + obsData.H150WS + "</td>" +
                    "</tr><tr>" +
                    "<td>+" + (38.6 - sfcFt) + " K</td>" +
                    "<td style='" + color2Grad("T", "bottom", tA200) + "'>" + obsData.H200T + "</td>" +
                    "<td style='" + color2Grad("T", "bottom", dA200) + "'>" + obsData.D200D + "</td>" +
                    "<td style='" + color2Grad("H", "bottom", hA200) + "'>" + obsData.H200H + "</td>" +
                    "<td style='" + color2Grad("W", "bottom", wA200) + "'>" + obsData.H200WV + " " + obsData.H200WS + "</td>" +
                    "</tr><tr>" +
                    "<td>+" + (34.0 - sfcFt) + " K</td>" +
                    "<td style='" + color2Grad("T", "bottom", tA250) + "'>" + obsData.H250T + "</td>" +
                    "<td style='" + color2Grad("T", "bottom", dA250) + "'>" + obsData.D250D + "</td>" +
                    "<td style='" + color2Grad("H", "bottom", hA250) + "'>" + obsData.H250H + "</td>" +
                    "<td style='" + color2Grad("W", "bottom", wA250) + "'>" + obsData.H250WV + " " + obsData.H250WS + "</td>" +
                    "</tr><tr>" +
                    "<td>+" + (30.1 - sfcFt) + " K</td>" +
                    "<td style='" + color2Grad("T", "bottom", tA300) + "'>" + obsData.H300T + "</td>" +
                    "<td style='" + color2Grad("T", "bottom", dA300) + "'>" + obsData.D300D + "</td>" +
                    "<td style='" + color2Grad("H", "bottom", hA300) + "'>" + obsData.H300H + "</td>" +
                    "<td style='" + color2Grad("W", "bottom", wA300) + "'>" + obsData.H300WV + " " + obsData.H300WS + "</td>" +
                    "</tr><tr>" +
                    "<td>+" + (26.6 - sfcFt) + " K</td>" +
                    "<td style='" + color2Grad("T", "bottom", tA350) + "'>" + obsData.H350T + "</td>" +
                    "<td style='" + color2Grad("T", "bottom", dA350) + "'>" + obsData.D350D + "</td>" +
                    "<td style='" + color2Grad("H", "bottom", hA350) + "'>" + obsData.H350H + "</td>" +
                    "<td style='" + color2Grad("W", "bottom", wA350) + "'>" + obsData.H350WV + " " + obsData.H350WS + "</td>" +
                    "</tr><tr>" +
                    "<td>+" + (23.6 - sfcFt) + " K</td>" +
                    "<td style='" + color2Grad("T", "bottom", tA400) + "'>" + obsData.H400T + "</td>" +
                    "<td style='" + color2Grad("T", "bottom", dA400) + "'>" + obsData.D400D + "</td>" +
                    "<td style='" + color2Grad("H", "bottom", hA400) + "'>" + obsData.H400H + "</td>" +
                    "<td style='" + color2Grad("W", "bottom", wA400) + "'>" + obsData.H400WV + " " + obsData.H400WS + "</td>" +
                    "</td>"
                    "<td>+" + (20.8 - sfcFt) + " K</td>" +
                    "<td style='" + color2Grad("T", "bottom", tA450) + "'>" + obsData.H450T + "</td>" +
                    "<td style='" + color2Grad("T", "bottom", dA450) + "'>" + obsData.D450D + "</td>" +
                    "<td style='" + color2Grad("H", "bottom", hA450) + "'>" + obsData.H450H + "</td>" +
                    "<td style='" + color2Grad("W", "bottom", wA450) + "'>" + obsData.H450WV + " " + obsData.H450WS + "</td>" +
                    "</tr><tr>" +
                    "<td>+" + (18.2 - sfcFt) + " K</td>" +
                    "<td style='" + color2Grad("T", "bottom", tA500) + "'>" + obsData.H500T + "</td>" +
                    "<td style='" + color2Grad("T", "bottom", dA500) + "'>" + obsData.D500D + "</td>" +
                    "<td style='" + color2Grad("H", "bottom", hA500) + "'>" + obsData.H500H + "</td>" +
                    "<td style='" + color2Grad("W", "bottom", wA500) + "'>" + obsData.H500WV + " " + obsData.H500WS + "</td>" +
                    "</td><tr>" +
                    "<td>+" + (16.0 - sfcFt) + " K</td>" +
                    "<td style='" + color2Grad("T", "bottom", tA550) + "'>" + obsData.H550T + "</td>" +
                    "<td style='" + color2Grad("T", "bottom", dA550) + "'>" + obsData.D550D + "</td>" +
                    "<td style='" + color2Grad("H", "bottom", hA550) + "'>" + obsData.H550H + "</td>" +
                    "<td style='" + color2Grad("W", "bottom", wA550) + "'>" + obsData.H550WV + " " + obsData.H550WS + "</td>" +
                    "</tr><tr>" +
                    "<td>+" + (13.8 - sfcFt) + " K</td>" +
                    "<td style='" + color2Grad("T", "bottom", tA600) + "'>" + obsData.H600T + "</td>" +
                    "<td style='" + color2Grad("T", "bottom", dA600) + "'>" + obsData.D600D + "</td>" +
                    "<td style='" + color2Grad("H", "bottom", hA600) + "'>" + obsData.H600H + "</td>" +
                    "<td style='" + color2Grad("W", "bottom", wA600) + "'>" + obsData.H600WV + " " + obsData.H600WS + "</td>" +
                    "</tr>";
            if(!isSet(baseEle) || baseEle >= 650) {
                doSoundingMin += "<tr><td>+" + (11.8 - sfcFt) + " K</td>" +
                    "<td style='" + color2Grad("T", "bottom", tA650) + "'>" + obsData.H650T + "</td>" +
                    "<td style='" + color2Grad("T", "bottom", dA650) + "'>" + obsData.D650D + "</td>" +
                    "<td style='" + color2Grad("H", "bottom", hA650) + "'>" + obsData.H650H + "</td>" +
                    "<td style='" + color2Grad("W", "bottom", wA650) + "'>" + obsData.H650WV + " " + obsData.H650WS + "</td>" +
                    "</tr>";
                    theLastT = obsData.H650T; theLastD = obsData.H650D; theLastW = obsData.H650WS; theLastH = obsData.H650H;
            }
            if(!isSet(baseEle) || baseEle >= 700) {
                doSoundingMin += "<tr><td>+" + (9.9 - sfcFt) + " K</td>" +
                    "<td style='" + color2Grad("T", "bottom", tA700) + "'>" + obsData.H700T + "</td>" +
                    "<td style='" + color2Grad("T", "bottom", dA700) + "'>" + obsData.D700D + "</td>" +
                    "<td style='" + color2Grad("H", "bottom", hA700) + "'>" + obsData.H700H + "</td>" +
                    "<td style='" + color2Grad("W", "bottom", wA700) + "'>" + obsData.H700WV + " " + obsData.H700WS + "</td>" +
                    "</tr>";
                    theLastT = obsData.H700T; theLastD = obsData.H700D; theLastW = obsData.H700WS; theLastH = obsData.H700H;
            }
            if(!isSet(baseEle) || baseEle >= 750) {
                doSoundingMin += "<tr><td>+" + (8.1 - sfcFt) + " K</td>" +
                    "<td style='" + color2Grad("T", "bottom", tA750) + "'>" + obsData.H750T + "</td>" +
                    "<td style='" + color2Grad("T", "bottom", dA750) + "'>" + obsData.D750D + "</td>" +
                    "<td style='" + color2Grad("H", "bottom", hA750) + "'>" + obsData.H750H + "</td>" +
                    "<td style='" + color2Grad("W", "bottom", wA750) + "'>" + obsData.H750WV + " " + obsData.H750WS + "</td>" +
                    "</tr>";
                    theLastT = obsData.H750T; theLastD = obsData.H750D; theLastW = obsData.H750WS; theLastH = obsData.H750H;
            }
            if(!isSet(baseEle) || baseEle >= 800) {
                doSoundingMin += "<tr><td>+" + (6.4 - sfcFt) + " K</td>" +
                    "<td style='" + color2Grad("T", "bottom", tA800) + "'>" + obsData.H800T + "</td>" +
                    "<td style='" + color2Grad("T", "bottom", dA800) + "'>" + obsData.D800D + "</td>" +
                    "<td style='" + color2Grad("H", "bottom", hA800) + "'>" + obsData.H800H + "</td>" +
                    "<td style='" + color2Grad("W", "bottom", wA800) + "'>" + obsData.H800WV + " " + obsData.H800WS + "</td>" +
                    "</tr>";
                    theLastT = obsData.H800T; theLastD = obsData.H800D; theLastW = obsData.H800WS; theLastH = obsData.H800H;
            }
            if(!isSet(baseEle) || baseEle >= 850) {
                doSoundingMin += "<tr><td>+" + (4.8 - sfcFt) + " K</td>" +
                    "<td style='" + color2Grad("T", "bottom", tA850) + "'>" + obsData.H850T + "</td>" +
                    "<td style='" + color2Grad("T", "bottom", dA850) + "'>" + obsData.D850D + "</td>" +
                    "<td style='" + color2Grad("H", "bottom", hA850) + "'>" + obsData.H850H + "</td>" +
                    "<td style='" + color2Grad("W", "bottom", wA850) + "'>" + obsData.H850WV + " " + obsData.H850WS + "</td>" +
                    "</tr>";
                    theLastT = obsData.H850T; theLastD = obsData.H850D; theLastW = obsData.H850WS; theLastH = obsData.H850H;
            }
            if(!isSet(baseEle) || baseEle >= 900) {
                doSoundingMin += "<tr><td>+" + (3.2 - sfcFt) + " K</td>" +
                    "<td style='" + color2Grad("T", "bottom", tA900) + "'>" + obsData.H900T + "</td>" +
                    "<td style='" + color2Grad("T", "bottom", dA900) + "'>" + obsData.D900D + "</td>" +
                    "<td style='" + color2Grad("H", "bottom", hA900) + "'>" + obsData.H900H + "</td>" +
                    "<td style='" + color2Grad("W", "bottom", wA900) + "'>" + obsData.H900WV + " " + obsData.H900WS + "</td>" +
                    "</tr>";
                    theLastT = obsData.H900T; theLastD = obsData.H900D; theLastW = obsData.H900WS; theLastH = obsData.H900H;
            }
            if(!isSet(baseEle) || baseEle >= 950) {
                doSoundingMin += "<tr><td>+" + (1.7 - sfcFt) + " K</td>" +
                    "<td style='" + color2Grad("T", "bottom", tA950) + "'>" + obsData.H950T + "</td>" +
                    "<td style='" + color2Grad("T", "bottom", dA950) + "'>" + obsData.D950D + "</td>" +
                    "<td style='" + color2Grad("H", "bottom", hA950) + "'>" + obsData.H950H + "</td>" +
                    "<td style='" + color2Grad("W", "bottom", wA950) + "'>" + obsData.H950WV + " " + obsData.H950WS + "</td>" +
                    "</tr>";
                    theLastT = obsData.H950T; theLastD = obsData.H950D; theLastW = obsData.H950WS; theLastH = obsData.H950H;
            }
            if(!isSet(baseEle) || baseEle >= 1000) {
                doSoundingMin += "<tr><td>+" + (0.4 - sfcFt) + " K</td>" +
                    "<td style='" + color2Grad("T", "bottom", tA1000) + "'>" + obsData.H1000T + "</td>" +
                    "<td style='" + color2Grad("T", "bottom", dA1000) + "'>" + obsData.D1000D + "</td>" +
                    "<td style='" + color2Grad("H", "bottom", hA1000) + "'>" + obsData.H1000H + "</td>" +
                    "<td style='" + color2Grad("W", "bottom", wA1000) + "'>" + obsData.H1000WV + " " + obsData.H1000WS + "</td>" +
                    "</tr>";
                    theLastT = obsData.H1000T; theLastD = obsData.H1000D; theLastW = obsData.H1000WS; theLastH = obsData.H1000H;
            }
            if(!isSet(theLastT)) { theLastT = obsData.SfcT; }
            if(!isSet(theLastD)) { theLastD = obsData.SfcD; }
            if(!isSet(theLastH)) { theLastH = obsData.SfcH; }
            if(!isSet(theLastW)) { theLastW = obsData.SfcWS; }
            if(isSet(theLastT) && isSet(theLastD) && isSet(theLastH) && isSet(theLastW)) {
                var tASurface = [ theLastT, obsData.SfcT ]; var dASurface = [ theLastD, obsData.SfcD ];
                var hASurface = [ theLastH, obsData.SfcH ]; var wASurface = [ theLastW, obsData.SfcWS ];
                doSoundingMin += "<tr><td>SFC</td>" +
                    "<td style='" + color2Grad("T", "bottom", tASurface) + "'>" + obsData.SfcT + "</td>" +
                    "<td style='" + color2Grad("T", "bottom", dASurface) + "'>" + obsData.SfcD + "</td>" +
                    "<td style='" + color2Grad("H", "bottom", hASurface) + "'>" + obsData.SfcH + "</td>" +
                    "<td style='" + color2Grad("W", "bottom", wASurface) + "'>" + obsData.SfcWV + " " + obsData.SfcWS + "</td>" +
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
        doSounding = "<div class='UPopNMO'>" + doSoundingMin + "</div>";
    } else {
        doSounding = "<div class='UPopNMO'>DATA ERROR FOR HOUR</div>";
    }
}

function shAtParAdd(longName, sType, styling, dataIn, stId, param) {
    var tdStyling = "";
    if(isSet(styling)) { tdStyling = " " + sType + "='" + styling + "'"; }
    var rData = "<tr><td>" + longName + "</td><td" + tdStyling + ">" + dataIn + "</td>" +
            "<td><a href='" + doCh("p", "WxXML", "Station=" + stId + "&Param=" + param) + "' target='new'>" + playIcon + "</a></td></tr>";
    return rData;
}