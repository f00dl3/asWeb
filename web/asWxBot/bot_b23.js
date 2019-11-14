const asm = require('./asModules');
const Discord = require('discord.js');
const client = new Discord.Client();
const auth = require('./auth.json');
const fetch = require('node-fetch');
const FormData = require('form-data');
const axios = require('axios');
process.env["NODE_TLS_REJECT_UNAUTHORIZED"] = 0;

var bBuild = 23;
var bUpdated = "19 OCT 2019";
var webUiBase = "https://localhost:8444/asWeb/r/";
var homeForBot = auth.kcregionalwx;
var maxMessageSize = 256;

function generateHelpMessage(msg) {

	var commandRan = "generateHelpMessage(msg)";
	basicAccessLog(msg, commandRan, "start");
	var apiVer = "UNSYNCH";
	var helpMessageHeader = "asWxBot (f00dl3) - build "+bBuild+" ("+bUpdated+") ==> Commands:";
	var helpMessageBody = "\n\'!ping\': Return \'pong\' reply back to user" +
		"\n\'cf6\': GetCF6 daily climate data. Use: \'cf6 <YYYY-MM>\'" +
		"\n\'find\': Get weather data for given station. Use: find <station> <YYYY-MM-DD>" +
		"\n\'forecast\': Get latest forecast model output for KOJC (Olathe, KS)" +
		"\n\'quote\': Get a random quote" +
		"\n\'radar\': Get latest weather radar loop. No basemap! Default: EAX. Usage: \'radar <SITE>\'" +
		"\n\'search\': Search station inventory to get station code for \'find\' tool" +
		"\n\'server\': Gets latest server status information" +
		"\n\'weather\': Get latest weather data from KOJC (Olathe, KS)";
	
	var returnData = "DEBUG: function getWebVersion()";
	var url = webUiBase + "WebVersion";
	fetch(url).then((res) => {
		returnData = res.data,
		console.log(res),
		console.log("API asWebUI v"+returnData.Version),
		apiVer = returnData.Version,
		helpMessageHeader = "asWxBot (f00dl3) - Build "+bBuild+" ("+bUpdated+") - asWebUI API v"+apiVer+"\n";
	});

	var helpMessage = helpMessageHeader + helpMessageBody;
	msg.reply(helpMessage);

	basicAccessLog(msg, commandRan, "stop");
	
}

function getCf6Data(msg, month) {

	var commandRan = "getCf6Data(msg, "+month+")";
	basicAccessLog(msg, commandRan, "start");

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

	basicAccessLog(msg, commandRan, "stop");

}

function getServerInfo(msg) {

	var commandRan = "getServerInfo(msg)";
	basicAccessLog(msg, commandRan, "start");
	
	var rData = "TODO: BUILD SNMP DATA QUERY NON-CHART SPECIFIC!";
	var url = webUiBase + "SNMP";
	var pData = "doWhat=getMainRecent";

	axios.post(url, pData).then((res) => { 
		rData = res.data,
		respondServerInfo(msg, rData)
	}).catch((error) => {
		console.log(error)
	});

	basicAccessLog(msg, commandRan, "stop");
	
	
}

function getWeatherData(msg, station, date) {

	var commandRan = "getWeatherData(msg, "+station+", "+date+")";
	basicAccessLog(msg, commandRan, "start");
	
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
	
	basicAccessLog(msg, commandRan, "stop");

}

function getWeatherForecast(msg) {

	var commandRan = "getWeatherForecast(msg)";
	basicAccessLog(msg, commandRan, "start");

	var rData = "getWeatherLatest() did not get data back yet!";
	var url = webUiBase + "Wx";
	var pData = "doWhat=getMosData";

	axios.post(url, pData).then((res) => {
		var last = res.data.last[0];
		var hgts = res.data.heights;
		var hours = res.data.hours;
		var runs = res.data.runs;
		var jmd = res.data.jsonModelData;
		respondWeatherForecast(msg, last, hgts, hours, runs, jmd);
	}).catch((error) => {
		console.log(error);
	});

	basicAccessLog(msg, commandRan, "stop");
}


function getRandomQuotes(msg) {

	var commandRan = "getRandomQuotes(msg)";
	basicAccessLog(msg, commandRan, "start");

	var rData = "DEBUG: getRandomQuotes() did not get data back yet!";
	var url = webUiBase + "Entertainment";
	var pData = "doWhat=getRandomQuotes";

	axios.post(url, pData).then((res) => { 
		rData = res.data,
		respondRandomQuotes(msg, rData)
	}).catch((error) => {
		console.log(error)
	});

	basicAccessLog(msg, commandRan, "stop");

}
function getWeatherLatest(msg) {

	var commandRan = "getWeatherLatest(msg)";
	basicAccessLog(msg, commandRan, "start");

	var rData = "DEBUG: getWeatherLatest() did not get data back yet!";
	var url = webUiBase + "Wx";
	var pData = "doWhat=getObsJsonLast";

	axios.post(url, pData).then((res) => { 
		rData = res.data[0].jsonSet,
		respondWeatherData("KOJC", rData, msg)
	}).catch((error) => {
		console.log(error)
	});

	basicAccessLog(msg, commandRan, "stop");

}

function getWeatherRadar(msg, site) {

	var radarSite = "EAX";
	if(isSet(site)) { radarSite = site.toUpperCase(); }
	var radarFile = "/var/www/Get/Radar/" + radarSite + "/_BLatest.gif";
	msg.reply("Latest radar image for " + radarSite + ":\n", { files :  [ radarFile ] });

}

function getWeatherStations(msg, searchString) {

	var commandRan = "getWeatherStations("+searchString+")";
	basicAccessLog(msg, commandRan, "start");

	var rData = "DEBUG: getWeatherStations() called.";
	var url = webUiBase + "Wx";
	var timeStart= returnTimestamp(-1);
	var timeEnd = returnTimestamp();

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
	
	basicAccessLog(msg, commandRan, "stop");

}

function sendMessageOnStartup(client, myArgs) {

	if(isSet(myArgs) && myArgs != '') {
		var messageToSendOnStartUp = myArgs[0];
		console.log(myArgs);
		try {
			var channel = client.channels.get(homeForBot);
			channel.send(messageToSendOnStartUp);
		} catch(e) {
			console.log("ERROR: ", e);
	 	}
		client.destroy();
	} 
	
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

client.on('ready', async () => {
	
	console.log(`SUCCESS---->\nasWxBot build `+bBuild+` logged in as ${client.user.tag}!\n`);
	var myArgs = process.argv.slice(2);
	sendMessageOnStartup(client, myArgs);

});

client.on('message', msg => {

	var msgArray = (msg.content).split(' ');
	var matchMsg = msgArray[0];
	var ncmsg = (matchMsg).toLowerCase();
	var msgBack = "DEBUG: Message back not set yet!";

	switch(ncmsg) {

		case "!help":
			generateHelpMessage(msg);
			break;
		
		case "!ping":
			msgBack = "pong!";
			msg.reply(msgBack);
			break;
			
		case "!serverInfo":
			getServerInfo(msg);
			break;

		case "!test1":
			msg.reply(trimForDiscord2("Testing"));
			break;
			
		case "!test2":
			msg.reply("Testing image attachment to bot message:", { files : ['../img/DiscordApp/pwned.jpg'] });
			break;

		case "cf6":
			var month = msgArray[1];
			getCf6Data(msg, month);
			break;

		case "find":
			var station = "",
				date = "";
			try { station = msgArray[1].toUpperCase(); } catch (e) { console.log(e); }
			try { date = msgArray[2]; } catch (e) { console.log(e); }
		 	getWeatherData(msg, station, date);
			break;

		case "forecast": 
			getWeatherForecast(msg);
			break;

		case "quote":
			getRandomQuotes(msg);
			break;

		case "radar":
			var site = msgArray[1];
			getWeatherRadar(msg, site);
			break;

		case "search":
			var searchString = "";
			try { searchString = msgArray[1].toLowerCase(); } catch (e) { console.log(e); }
			if(searchString.length < 4) {
				msg.reply("Please enter more characters of the location!");
			} else {
				msg.reply("Finding stations similar to [" + searchString + "]...");
				getWeatherStations(msg, searchString);
			}
			break;

		case "server":
			getServerInfo(msg);
			break;

		case "weather":
			getWeatherLatest(msg);
			break;

	}

});

client.login(auth.token);
