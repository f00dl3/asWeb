//const asModules = require('./asModules/common.js');
const Discord = require('discord.js');
const client = new Discord.Client();
const auth = require('./auth.json');
const fetch = require('node-fetch');
const FormData = require('form-data');
const axios = require('axios');
process.env["NODE_TLS_REJECT_UNAUTHORIZED"] = 0;

var bBuild = 19;
var bUpdated = "12 OCT 2019";
var helpMessage = "asWxBot (f00dl3) - Build "+bBuild+" ("+bUpdated+")\n" +
	"Bot options: \n" +
	"\n\'!ping\': Return \'pong\' reply back to user." +
	"\n\'cf6\': GetCF6 daily climate data. Use: \'cf6 <YYYY-MM>\'" +
	"\n\'find\': Get weather data for given station. Use: find <station> <YYYY-MM-DD>" +
//	"\n\'search\': Search station inventory to get station code for \'find\' tool." +
	"\n\'weather\': Get latest weather data from KOJC (Olathe, KS).";
var webUiBase = "https://localhost:8444/asWeb/r/";

var maxMessageSize = 256;

function basicAccessLog(input, phase) {
	console.log("Access " + phase + ": A user executed [" + input + "]");
}

function getCf6Data(msg, month) {

	var commandRan = "getCf6Data(msg, "+month+")";
	basicAccessLog(commandRan, "start");

	var rData = "CF6 Function";
	var url = webUiBase + "Wx";
	var pData = "doWhat=getCf6Data" +
		"&CF6Search1="+month+"-01" +
		"&CF6Search2="+month+"-31";

	axios.post(url, pData).then((res) => {
		rData = res.data,
		respondCf6Data(msg, rData)
	}).catch((error) => {
		console.log(error)
	});	

	basicAccessLog(commandRan, "stop");

}

function getWeatherData(msg, station, date) {

	var commandRan = "getWeatherData(msg, "+station+", "+date+")";
	basicAccessLog(commandRan, "start");
	
	var rData = "DEBUG: function getWeatherData("+station+")";
	var url = webUiBase + "Wx";
	var dateOverrideStart = returnTimestamp(-24);
	var dateOverrideEnd = returnTimestamp();
	if(date) {
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
	
	basicAccessLog(commandRan, "stop");

}

function getWeatherLatest(msg) {

	var commandRan = "getWeatherLatest(msg)";
	basicAccessLog(commandRan, "start");

	var rData = "DEBUG: getWeatherLatest() did not get data back yet!";
	var url = webUiBase + "Wx";
	var pData = "doWhat=getObsJsonLast";

	axios.post(url, pData).then((res) => { 
		rData = res.data[0].jsonSet,
		respondWeatherData("KOJC", rData, msg);
	}).catch((error) => {
		console.log(error)
	});

	basicAccessLog(commandRan, "stop");

}

function getWeatherStations(searchString) {

	var commandRan = "getWeatherStations("+searchString+")";
	basicAccessLog(commandRan, "start");

	var rData = "DEBUG: getWeatherStations() called.";
	var url = webUiBase + "Wx";
	var timeStart= returnTimestamp(-1);
	var timeEnd = returnTimestamp();

	var pData = "doWhat=getObsJMWS" +
		"&startTime=" + timeStart +
		"&endTime=" + timeEnd +
		"&limit=1";

	axios.post(url, pData).then((res) => {
		jmwsData = data.wxObs,
		jmwsData = jmwsData.concat(data.wxObsR),
		jmwsStations = data.stations,
		respondStationSearch(searchString, jmwsStations, jmwsData)
	}).catch((error) => {
		console.log(error)
	});
	
	basicAccessLog(commandRan, "stop");

}

function getWebVersion() {

	var returnData = "DEBUG: function getWebVersion()";
	var url = webUiBase + "WebVersion";
	fetch(url).then((res) => {
		returnData = res.data[0],
		console.log("API asWebUI v"+returnData.Version)
	});
	return returnData;

}

function parseDate(oH) {

	var di = new Date();
	if(typeof oH !== 'undefined') { di = new Date(di.setHours(di.getHours()- oH)); }
	var dY = di.getFullYear();
	var dM = ("0" + (di.getMonth()+1)).slice(-2);
	var dD = ("0" + (di.getDate())).slice(-2);
	var dh = di.getHours();
	var dm = di.getMinutes();
	var ds = di.getSeconds();
	var jsonBack = {
		"dY": dY, "dM": dM, "dD": dD,
		"dh": dh, "dm": dm, "ds": ds
	};

	return jsonBack;

}

function respondCf6Data(msg, cf6in) {

	var litr = 0;
	var headerMessage = "F6 Climate Data for KCI airport (KMCI)" +
		"\nDate | Hi | Lw | Pcp | Sn | Wnd | Cld | Wx";
	msg.reply(trimForDiscord(headerMessage));

	cf6in.forEach(function(cf6) {
		var newWx;
		var weather = cf6.Weather;
		if(typeof weather !== 'undefined') {
			newWx = weather
				.replace("1", "F")
				.replace("2", "D")
				.replace("3", "T")
				.replace("4", "I")	
				.replace("5", "H")	
				.replace("6", "Z")
				.replace("7", "S")
				.replace("8", "D")
				.replace("9", "B");
		}
		var returnString = "\n" + cf6.Date + " | " +
			cf6.High + " | " + cf6.Low + " | " +
			cf6.Liquid + " | " + cf6.Snow + " | " +
			cf6.WMax + " | " + cf6.Clouds + " | " +
			newWx;
		msg.reply(trimForDiscord(returnString));
	});

}

function respondStationSearch(searchString, jmwsStations, jmwsData) {

	var matchingRows = [];
	var wordArray = [];
	var resultLimit = 30;

	if(searchString.includes(" ")) {
		wordArray = value.split(" ");
		jmwsStations.forEach(function(sr) {
			var wordsHit = 0;
			wordArray.forEach(function(tWord) {
				if(
					(typeof sr.City !== 'undefined') && ((sr.City).toLowerCase.includes(tWord.toLowerCase())) ||
					(typeof sr.Station !== 'undefined') && ((sr.Station).toLowerCase.includes(tWord.toLowerCase())) ||
					(typeof sr.Description !== 'undefined') && ((sr.Description).toLowerCase.includes(tWord.toLowerCase())) ||
					tWord.includes("S: ") && (typeof sr.State !== 'undefined') && ((sr.State).toLowerCase.includes(tWord.toLowerCase())) 
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
		jmwsStations.forEach(function(jws) {
			if(
				(typeof sr.City !== 'undefined') && ((sr.City).toLowerCase.includes(tWord.toLowerCase())) ||
				(typeof sr.Station !== 'undefined') && ((sr.Station).toLowerCase.includes(tWord.toLowerCase())) ||
				(typeof sr.Description !== 'undefined') && ((sr.Description).toLowerCase.includes(tWord.toLowerCase())) ||
				tWord.includes("S: ") && (typeof sr.State !== 'undefined') && ((sr.State).toLowerCase.includes(tWord.toLowerCase()))
			) {
				if(matchingRows.length < (resultLimit - 1)) {
					matchingRows.push(sr);
				} else {
					console.log("Match limit hit!");
				}
			}
		});
		
	}

	matchingRows.forEach(function(mr) {

		console.log(mr);

	});			

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
	msg.reply(trimForDiscord(finalMessage));

}

function respondWeatherDataTable(station, data, msg) {
	
	var lastMessage = "Station: " + station;
	msg.reply(trimForDiscord(lastMessage));

	var jds = data.wxObsM1H;
	jds.forEach(function(jd) {
		var message = tabulateWeatherData(jd);
		if(message != lastMessage) { msg.reply("RECENT => " + message); }
		lastMessage = message;
	});

	var jdt = data.wxObsNow;
	jdt.forEach(function(jd) {
		var message = tabulateWeatherData(jd);
		if(message != lastMessage) { msg.reply(message); }
		lastMessage = message;
	});

	console.log(dataBack);

	return dataBack;
	
}

function returnTimestamp(oH) {
	var tJ = parseDate(oH);
	return tJ.dY + "-" + tJ.dM + "-" + tJ.dD + " " + tJ.dh + ":" + tJ.dm + ":" + tJ.ds;
}

function tabulateWeatherData(jd) {

	var jsd = JSON.parse(jd.jsonSet);
	var shortTimeString = (jsd.TimeString).replace("Last Updated on ", "");

	var dataBack = "\n" + shortTimeString + " | " +
		jsd.Weather + " | " +
		jsd.Temperature + " | " + jsd.Dewpoint + " | " +
		jsd.WindDirection + " | " + jsd.WindSpeed;
	trimForDiscord(dataBack);

	return dataBack;

}

function trimForDiscord(message) { 

	var maxMessageSize = 256;
	return message.substring(0, maxMessageSize);

}

client.on('ready', () => {
	var webVersion = getWebVersion();
	console.log(`Logged in as ${client.user.tag}!\n`+webVersion);
});

client.on('message', msg => {

	var msgArray = (msg.content).split(' ');
	var matchMsg = msgArray[0];
	var ncmsg = (matchMsg).toLowerCase();
	var msgBack = "DEBUG: Message back not set yet!";

	switch(ncmsg) {

		case "!help":
			msg.reply(helpMessage);
			break;
		
		case "!ping":
			msgBack = "pong!";
			msg.reply(msgBack);
			break;

		case "!test1":
			msg.reply(trimForDiscord2("Testing"));
			break;

		case "cf6":
			var month = msgArray[1];
			getCf6Data(msg, month);
			break;

		case "find":
			var station = msgArray[1].toUpperCase();
			var date = msgArray[2];
		 	getWeatherData(msg, station, date);
			break;

		case "search":
			var searchString = msgArray[1].toLowerCase();
			if(searchString.length < 4) {
				msg.reply("Please enter more characters of the location!");
			} else {
				msg.reply("Finding stations similar to [" + searchString + "]...");
				getWeatherStations(searchString);
			}

		case "weather":
			getWeatherLatest(msg);
			break;

	}

});

client.login(auth.token);
