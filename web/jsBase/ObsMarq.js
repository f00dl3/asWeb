/* 
by Anthony Stump
Created: 5 Mar 2018
Updated: 14 Mar 2018

 (function worker() {
    var obsUrl = "/asWeb/inc/ObsMarq.jsp";
    var marqRefresh = timeMinutes(2.5);
    $.ajax({
       url: obsUrl,
       success: function(data) {
           $('#WxObsMarq').html(data.WxObsMarq).marquee();
       },
       complete: function() {
           setTimeout(worker, marqRefresh);
       }
    });
})();
 */

function getMarqueeData() {
    var obsJson = baseForRestlet+"/Wx";
    var obsJsonLastPostData = "doWhat=getObsJsonLast";
    var arObsJsonMq = {
        preventCache: true,
        url: obsJson,
        postData: obsJsonLastPostData,
        handleAs: "json",
        timeout: timeOutMilli,
        load: function(data) {
            var theData = JSON.parse(data[0].jsonSet);
            var lastData;
            var subXhr = dojo.xhrPost(arObsJson);
            subXhr.then(
                function(data) {
                    lastData = JSON.parse(data[0].jsonData).KOJC;
                    processMarqueeData(theData, lastData);
                    $('#WxObsMarq').html(data.WxObsMarq).marquee();
                },
                function(error) {
                    lastData = "";
                }
            );
        },
        error: function(data, iostatus) {
            window.alert("xhrGet obsJsonLast: FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
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
    if(theData === "") { console.log("ERROR fetching ThisData"); }
    if(lastData === "") { console.log("ERROR fetching LastData"); }
    var returnData = "";
    var theTemperature = theData.Temperature;
    var stationId = theData.Station;
    var getTime = theData.GetTime;
    if(!isSet(theTemperature)) {
        returnData += "<div id='WxObsMarq'>";
        returnData += "<strong>WARNING! " + stationId + " data unavailable!</strong>";
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
            "<img class='th_icon' src='" + baseForUi + "/img/Icons/wx/" + wxObs("Icon", theData.TimeString, null, null, null, theData.Weather) + ".png' /> " + theData.Weather + " " +
            " " + animatedArrow(diffTemperature) + Math.round(theData.Temperature) + "F (" + diffTemperature + "F/hr)" +
            " " + animatedArrow(diffDewpoint) + Math.round(theData.Dewpoint) + "F ( " + diffDewpoint + "F/hr) - " +
            " RH: <span style='" + colorRh(theData.RelativeHumidity) + "'>" + theData.RelativeHumidity + "%</span> - ";
        if(isSet(theData.WindSpeed)) {
            returnData += " Wind: ";
            if(isSet(theData.WindDirection)) { returnData += theData.WindDirection + " at "; }
            returnData += "<span style='" + colorWind(theData.WindSpeed) + "'>" + theData.WindSpeed + " mph</span> " + gust + " - ";
        }
        returnData += " Feel: <span style='" + colorTemp(flTemp) + "'>" + flTemp + "F</span>" +
            " (<span style='" + colorTemp(flTempC) + "'>" + flTempC + "F <img class='th_icon' src='" + baseForUi + "/img/Icons/ic_cyc.jpeg'/></span>) - ";
        if(isSet(theData.CAPE)) {
            returnData += "<span style=" + colorCape(theData.CAPE) + ">" + theData.CAPE + "</span> - ";
        }
        returnData += " MSLP: " + animatedArrow(diffPressure) + theData.Pressure + " <span>mb</span> --- ";
        dojo.byId("disHolder").innerHTML = returnData;
        console.log("returnData: " + returnData);
    }
    returnData += "</div>";
}

var init = function(event) {
    getMarqueeData();
};

dojo.ready(init);
