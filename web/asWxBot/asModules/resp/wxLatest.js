/* 
by Anthony Stump
Created: 7 Mar 2020
Updated: 2 Sep 2020
 */

const axios = require('axios');
const aLog = require('./../accessLog.js');
const asm = require('./../common.js');

module.exports = {

		getWeatherChart: function(msg) { getWeatherChart(msg) },
		getWeatherLatest: function(msg) { getWeatherLatest(msg) }

}

function getWeatherChart(msg) {
	var commandRan = "getWeatherChart(msg)";
	aLog.basicAccessLog(msg, commandRan, "start");
	let chartSuccess = "Chart not generated yet!";
	var url = asm.webUiBase + "Chart";
	var graph = "/dev/shm/tomcatShare/cache/ObsJSONTempH.png";
    var pData = {
        "doWhat": "WxObsChartsHome"
    };
    axios.post(url, pData).then((res) => {
    	chartSuccess = "Chart generated!";
		msg.reply(chartSuccess, { files :  [ graph ] });
	}).catch((error) => {
		console.log(error)
	});
	aLog.basicAccessLog(msg, commandRan, "stop");
}

function getWeatherLatest(msg) {

	var commandRan = "getWeatherLatest(msg)";
	aLog.basicAccessLog(msg, commandRan, "start");
	let stationId = "KOJC";
	var rData = "DEBUG: getWeatherLatest() did not get data back yet!";
	var url = asm.webUiBase + "Wx";
	var dateOverrideStart = asm.returnTimestamp();
	var dateOverrideEnd = asm.returnTimestamp(-2);
	console.log(dateOverrideStart + " to " + dateOverrideEnd);
	console.log("DBG: " + url);
	var pData = "doWhat=getObsJsonMergedAndHome" +
        "&startTime=" + dateOverrideEnd +
        "&endTime=" + dateOverrideStart +
        "&order=DESC" +
        "&limit=1" +
        "&stationId=" + stationId;
	axios.post(url, pData).then((res) => { 
        var theData = JSON.parse(res.data.wxObsM1H[0].jsonSet);
        var homeData = JSON.parse(res.data.homeWxObs[0].jsonSet);
		respondWeatherData("KKSLENEX98", theData, msg, homeData);
	}).catch((error) => {
		console.log(error)
	});

	aLog.basicAccessLog(msg, commandRan, "stop");

}

function respondWeatherData(station, data, msg, homeData) {

	var jds = data;
	let jdsHome = homeData;
	var finalMessage = "Station: " + station +
		"\n" + jdsHome.TimeString +
		// "\nWeather: " + jds.Weather +
		// "\nVisibility: " + jds.Visibility + " miles" +
		"\nTemperature: " + jdsHome.Temperature + " F" +
		"\nDewpoint: " + jdsHome.Dewpoint + " F" +
		"\nHumidity: " + jdsHome.RelativeHumidity + "%" +
		"\nPressure: " + jdsHome.PressureIn + "\"" +
		"\nWind Direction: " + jdsHome.WindDegrees + " deg" +
		"\nWind Speed: " + jdsHome.WindSpeed + " mph" +
		"\nDaily Rain: " + jdsHome.DailyRain + "\"" +
		"\nRain Rate: " + jdsHome.RainRate + "\"/hr";
	console.log("\nDBG --> finalMessage = " + finalMessage);
	if(asm.isSet(jdsHome.WindGust)) { finalMessage += "\nWind Gusts: " + jdsHome.WindGust + " mph "; }
	msg.reply(asm.trimForDiscord(finalMessage));

}
