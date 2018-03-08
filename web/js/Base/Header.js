/* 
by Anthony Stump
Created: 4 Mar 2018
Updated: 7 Mar 2018
 */

var baseForUi = "/asWeb";
var shortBaseForRestlet = baseForUi + "/r";
var fullBaseForRestlet = self.location.protocol + "//" + self.location.host + shortBaseForRestlet;
var baseForRestlet = fullBaseForRestlet;
var timeOutMilli = (20*1000);

function checkMobile() {
    if(/Android|webOS|iPhone|iPad|iPod|BlackBerry|IEMobile|Opera Mini/i.test(navigator.userAgent)) {
        return true;
    } else {
        return false;
    }
}

function doRefresh(period) {
    var dateString = getDate("day", 0);
    var returnString = "<meta http-equiv='refresh' content='" + period + "'>";
    returnString += "<div class='Notice'>Refreshed " + dateString + "</div>";
    return returnString;
}

function getDate(intType, intInput) {
    require(["dojo/date"], function(date) {
        var thisDate = new Date();
        return date.add(thisDate, intType, intInput);
    });
}

function isSet(varIn) {
    if(typeof varIn !== 'undefined') { return true; } else { return false; }
}

function timeMinutes(inMin) {
    return 1000*60*inMin;
}

function scLd(scriptName) {
    if (!scriptName) scriptName = baseForUi+"/js/"+scriptName+".js";
    var scripts = document.getElementsByTagName('script');
    for (var i = scripts.length; i--;) {
        if (scripts[i].src === scriptName) return true;
    }
    return false;
}
    