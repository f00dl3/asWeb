/* 
by Anthony Stump
Created: 7 Mar 2020
Updated: 8 Mar 2020
 */

const axios = require('axios');
const aLog = require('./../accessLog.js');
const asm = require('./../common.js');

module.exports = {

		getWeatherLatest: function(msg) { getWeatherLatest(msg) }

}

function getWeatherLatest(msg) {

	var commandRan = "getWeatherLatest(msg)";
	aLog.basicAccessLog(msg, commandRan, "start");

	var rData = "DEBUG: getWeatherLatest() did not get data back yet!";
	var url = asm.webUiBase + "Wx";
	var pData = "doWhat=getObsJsonLast";

	axios.post(url, pData).then((res) => { 
		rData = res.data[0].jsonSet,
		respondWeatherData("KOJC", rData, msg)
	}).catch((error) => {
		console.log(error)
	});

	aLog.basicAccessLog(msg, commandRan, "stop");

}

function respondWeatherData(station, data, msg) {

	var jds = JSON.parse(data);
	var finalMessage = "Station: " + station +
		"\n" + jds.TimeString +
		"\nWeather: " + jds.Weather +
		"\nVisibility: " + jds.Visibility + " miles" +
		"\nTemperature: " + jds.Temperature + " F" +
		"\nDewpoint: " + jds.Dewpoint + " F" +
		"\nHumidity: " + jds.RelativeHumidity + "%" +
		"\nPressure: " + jds.Pressure + " mb" +
		"\nWind: " + jds.WindDirection + " at " + jds.WindSpeed + " mph";
	if(asm.isSet(jds.WindGust)) { finalMessage += "\nGusts: " + jds.WindGust + " mph"; }
	msg.reply(asm.trimForDiscord(finalMessage));

}