/* 
by Anthony Stump
Created: 7 Mar 2020
Updated: 8 Mar 2020
 */

const axios = require('axios');
const aLog = require('./../accessLog.js');
const asm = require('./../common.js');

module.exports = {

		getWeatherData: function(msg, station, date) { getWeatherData(msg, station, date) }

}

function getWeatherData(msg, station, date) {

	var commandRan = "getWeatherData(msg, "+station+", "+date+")";
	aLog.basicAccessLog(msg, commandRan, "start");
	
	var rData = "DEBUG: function getWeatherData("+station+")";
	var url = asm.webUiBase + "Wx";
	var dateOverrideStart = asm.returnTimestamp(-24);
	var dateOverrideEnd = asm.returnTimestamp();
	if(asm.isSet(date)) { 
		dateOverrideStart = date + " 00:00:00";
		dateOverrideEnd = date + " 23:59:59";	
	}
	var pData = "doWhat=getObsJsonMerged" +
		"&startTime=" + dateOverrideStart +
		"&endTime=" + dateOverrideEnd +
		"&stationId=" + station +
		"&limit=500" +
		"&order=DESC";

	axios.post(url, encodeURI(pData)).then((res) => {
		rDataA = res.data,
		respondWeatherDataTable(station, rDataA, msg)
	}).catch((error) => {
		console.log(error)
	});
	
	aLog.basicAccessLog(msg, commandRan, "stop");

}

function respondWeatherDataTable(station, data, msg) {
	
	var lastMessage = "Station: " + station;
	msg.reply(asm.trimForDiscord(lastMessage));

	if(station === "KOJC") {
		var jds = data.wxObsM1H;
		jds.forEach(function(jd) {
			var message = tabulateWeatherData(jd);
			if(message !== lastMessage) { msg.reply("RECENT => " + message); }
			lastMessage = message;
		});
	}
	var jdt = data.wxObsNow;
	jdt.forEach(function(jd) {
		var message = tabulateWeatherData(jd);
		if(message !== lastMessage) {
			msg.reply(message);
		} else {
			//msg.reply(message+"*");
		}
		lastMessage = message;
	});
	
}

function tabulateWeatherData(jd) {

	var jsd = JSON.parse(jd.jsonSet);
	var shortTimeString = (jsd.TimeString).replace("Last Updated on ", "");

	var dataBack = "\n" + shortTimeString + " | " +
		jsd.Weather + " | " +
		jsd.Temperature + " | " + jsd.Dewpoint + " | " +
		jsd.WindDirection + " | " + jsd.WindSpeed;
	asm.trimForDiscord(dataBack);

	return dataBack;

}