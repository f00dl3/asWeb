/* 
by Anthony Stump
Created: 5 Mar 2018
Updated: 7 Mar 2018
 */

/* 
  
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

var obsJson = baseForRestlet+"/Wx";
window.alert(getDate("hour", -1));
var dateOverrideStart = "2018-03-07 13:45:00"; //getDate("hour", -1); 
var dateOverrideEnd = "2018-03-07 14:45:00"; // getDate("hour", 0);
var obsJsonLastPostData = "doWhat=getObsJsonLast";
var obsJsonPostData = "doWhat=getObjsJson" +
        "&startTime=" + dateOverrideStart +
        "&endTime=" + dateOverrideEnd +
        "&limit=1";

var arObsJson = {
    url: obsJson,
    postData: obsJsonPostData,
    handleAs: "json",
    timeout: timeOutMilli,
    load: function(data) {
        var returnData;
        var theData = JSON.parse(data[0].KOJC);
        var theTemperature = theData.Temperature;
        var stationId = theData.Station;
        var getTime = theData.GetTime;
        if(!isset(theTemperature)) {
            returnData = "<div id='LWObs'><strong>WARNING! " + stationId + " data unavailable!</strong>";
            returnData += "<br/>Fetch timestamp: " + getTime + "</div>";
            window.alert(data);
        } else {
            if(!isSet(theData.Dewpoint)) { theData.Dewpoint = theData.Temperature; }
            window.alert(data);
        }
    },
    error: function(data, iostatus) {
        window.alert("xhrPost obsJson: FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
    }
};

var arObsJsonMq = {
    url: obsJson,
    postData: obsJsonLastPostData,
    handleAs: "json",
    timeout: timeOutMilli,
    load: function(data) {
        var theData = JSON.parse(data[0].jsonSet);
        window.alert("Temperature: " + theData.Temperature)
    },
    error: function(data, iostatus) {
        window.alert("xhrGet obsJsonLast: FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
    }
};

var init = function(event) {
    dojo.xhrPost(arObsJsonMq);
    //dojo.xhrPost(arObsJson);
};

dojo.ready(init);
