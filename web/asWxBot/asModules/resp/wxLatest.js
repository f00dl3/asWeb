/* 
by Anthony Stump
Created: 7 Mar 2020
Updated: 11 Mar 2021
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
	let lenexaWestData = "";
	let raymoreData = "";
	//var lenexaWestData = JSON.parse(res.data.lenexaWestWxObs[0].jsonSet);
        //var raymoreData = JSON.parse(res.data.raymoreWxObs[0].jsonSet);
		respondWeatherData("KKSLENEX98", theData, msg, homeData, lenexaWestData, raymoreData);
        	 var camSnap = "/dev/shm/tomcatShare/cache/CamLive_Public.jpeg";
	         msg.reply("Camera Snapshot", { files :  [ camSnap ] });

	}).catch((error) => {
		console.log(error)
	});

	aLog.basicAccessLog(msg, commandRan, "stop");

}

function respondWeatherData(station, data, msg, homeData, lenexaWestData, raymoreData) {

	var jds = data;
	let jdsHome = homeData;
	let jdsRaymore = raymoreData;
	let jdsLenexaWest = lenexaWestData;
	var finalMessage = "Lenexa/Old Town @ " + jdsHome.TimeString +
		"\nTemp/Dwpt/Humid: " + jdsHome.Temperature + "F" + "/" + jdsHome.Dewpoint + "F (" + jdsHome.RelativeHumidity + "%), Press: " + jdsHome.PressureIn + "\"" +
		"\nWind Dir: " + jdsHome.WindDegrees + " deg, Rain: " + jdsHome.DailyRain + "\", rate " + jdsHome.RainRate + "\"/hr";
	/*
	var finalMessageLenexaWest = "Lenexa West @ " + jdsLenexaWest.TimeString + 
		"\nTemp/Dwpt/Humid: " + jdsLenexaWest.Temperature + "F/" + jdsLenexaWest.Dewpoint + "F (" + jdsLenexaWest.RelativeHumidity + "%), Press: " + jdsLenexaWest.PressureIn + "\"" +
		"\nWind Dir: " + jdsLenexaWest.WindDegrees + " deg, Rain: " + jdsLenexaWest.DailyRain + "\", rate " + jdsLenexaWest.RainRate + "\"/hr";
	var finalMessageRaymore = "Raymore @ " + jdsRaymore.TimeString +
		"\nTemp/Dwpt/Humid: " + jdsRaymore.Temperature + "F/" + jdsRaymore.Dewpoint + "F (" + jdsRaymore.RelativeHumidity + "%), Press: " + jdsRaymore.PressureIn + "\"" +
		"\nWind Dir: " + jdsRaymore.WindDegrees + " deg, Rain: " + jdsRaymore.DailyRain + "\", rate " + jdsRaymore.RainRate + "\"/hr";
	*/
	console.log("\nDBG --> finalMessage = " + finalMessage);
	msg.reply(asm.trimForDiscord(finalMessage));
	//msg.reply(asm.trimForDiscord(finalMessageLenexaWest));
	//msg.reply(asm.trimForDiscord(finalMessageRaymore));

}
