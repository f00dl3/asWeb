/* 
by Anthony Stump
Created: 7 Mar 2020
Updated: on creation
 */

const axios = require('axios');
const aLog = require('./../accessLog.js');
const asm = require('./../commmon.js');

module.exports = {

		getWeatherSarcastic: function(msg) { getWeatherSarcastic(msg) }

}

function getWeatherSarcastic(msg) {

	var commandRan = "getWeatherSarcastic(msg)";
	aLog.basicAccessLog(msg, commandRan, "start");

	var rData = "DEBUG: getWeatherSarcastic() did not get data back yet!";
	var url = asm.webUiBase + "Wx";
	var pData = "doWhat=getObsJsonLast";

	axios.post(url, pData).then((res) => { 
		rData = res.data[0].jsonSet,
		respondWeatherSarcastic(rData, msg)
	}).catch((error) => {
		console.log(error)
	});

	aLog.basicAccessLog(msg, commandRan, "stop");

}

function respondWeatherSarcastic(data, msg) {

	var jds = JSON.parse(data);
	var theWeather = jds.Weather.toLowerCase();
	
	var finalMessage = "OMG really bro? Last I checked it is " + jds.Weather;
	
	if(theWeather.includes("rain") || theWeather.includes("snow")) {
		finalMessage += " - I guess you're right!";
	} else {
		finalMessage += " - Maybe you're on something?";
	}
	
	msg.reply(asm.trimForDiscord(finalMessage));

}