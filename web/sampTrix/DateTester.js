/* 
by Anthony Stump
Created: 9 Feb 2018
Updated: 10 Feb 2018
 */

var now = new Date();
var p1d = getDate("day", +1, "js");

var sunset = new Date().sunset(getHomeGeo("lat"), getHomeGeo("lon"));
var sunrise = new Date().sunrise(getHomeGeo("lat"), getHomeGeo("lon"));

dojo.require("dojo.date");
var toSunrise = dojo.date.compare(now, sunrise, "datetime");
var toSunset = dojo.date.compare(p1d, sunset, "datetime");

if(toSunrise === 1 && toSunset === 0) {
    isDaylight = 1;
} else {
    isDaylight = 0;
}

console.log("Is it day?: " + isDaylight);
