/* 
by Anthony Stump
Created: 7 Mar 2020
Updated: 8 Mar 2020
 */

const axios = require('axios');
const aLog = require('./../accessLog.js');
const asm = require('./../common.js');

module.exports = {

		getWeatherStations: function(msg, searchString) { getWeatherStations(msg, searchString) }

}

function getWeatherStations(msg, searchString) {

	var commandRan = "getWeatherStations("+searchString+")";
	aLog.basicAccessLog(msg, commandRan, "start");

	var rData = "DEBUG: getWeatherStations() called.";
	var url = asm.webUiBase + "Wx";
	var timeStart= asm.returnTimestamp(-1);
	var timeEnd = asm.returnTimestamp();

	var pData = "doWhat=getObsJMWS" +
		"&startTime=" + timeStart +
		"&endTime=" + timeEnd +
		"&limit=1";

	msg.reply("\nPlease wait a few moments to query databse. This could take up to 15 seconds.");

	axios.post(url, pData).then((res) => {
		jmwsData = res.data.wxObs,
		jmwsData = jmwsData.concat(res.data.wxObsR),
		jmwsStations = res.data.stations,
		respondStationSearch(msg, searchString, jmwsStations, jmwsData)
	}).catch((error) => {
		console.log(error)
	});
	
	aLog.basicAccessLog(msg, commandRan, "stop");

}

function respondStationSearch(msg, searchString, jmwsStations, jmwsData) {

	var matchingRows = [];
	var wordArray = [];
	var resultLimit = 30;

	if(searchString.includes(" ")) {
		wordArray = value.split(" ");
		jmwsStations.forEach(function(sr) {
			var wordsHit = 0;
			wordArray.forEach(function(tWord) {
				if(
					(asm.isSet(sr.City)) && ((sr.City).toLowerCase().includes(tWord.toLowerCase())) ||
					(asm.isSet(sr.Station)) && ((sr.Station).toLowerCase().includes(tWord.toLowerCase())) ||
					(asm.isSet(sr.Description)) && ((sr.Description).toLowerCase().includes(tWord.toLowerCase())) ||
					tWord.includes("S: ") && (asm.isSet(sr.State)) && ((sr.State).toLowerCase().includes(tWord.toLowerCase())) 
				) {
					wordsHit++;
				}
				if(wordsHit == wordArray.length) {
					hitCount++;
					if(matchingRows.length < (resultLimit-1)) {
						matchingRows.push(sr);
					} else {
						console.log("Match limit hit!");
					}
				}
			});
		});
	} else {
		jmwsStations.forEach(function(sr) {
			var tWord = searchString;
			if(
				(asm.isSet(sr.City)) && ((sr.City).toLowerCase().includes(tWord.toLowerCase())) ||
				(asm.isSet(sr.Station)) && ((sr.Station).toLowerCase().includes(tWord.toLowerCase())) ||
				(asm.isSet(sr.Description)) && ((sr.Description).toLowerCase().includes(tWord.toLowerCase())) ||
				tWord.includes("S: ") && (asm.isSet(sr.State)) && ((sr.State).toLowerCase().includes(tWord.toLowerCase()))
			) {
				if(matchingRows.length < (resultLimit - 1)) {
					matchingRows.push(sr);
				} else {
					console.log("Match limit hit!");
				}
			}
		});
		
	}

	var results = "Results:" +
		"\nStationID | Location";

	matchingRows.forEach(function(mr) {

		results += "\n" + mr.Station + " | " +
			mr.City + "," + mr.State;

	});			

	msg.reply(results);

}
