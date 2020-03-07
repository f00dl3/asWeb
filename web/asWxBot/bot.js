/* 
 * by Anthony Stump
 * Created: 3 Oct 2019
 * Updated: see bUpdated
 */

const Discord = require('discord.js');
const client = new Discord.Client();
const auth = require('./auth.json');
const fetch = require('node-fetch');
const FormData = require('form-data');
const axios = require('axios');
const asm = require('./asModules/common.js');
const wxs = require('./asModules/wxShare.js');
const aLog = require('./asModules/accessLog.js');
const resp = require('./asModules/responses.js');
process.env["NODE_TLS_REJECT_UNAUTHORIZED"] = 0;

var bBuild = 65;
var bUpdated = "7 MAR 2020";
var homeForBot = auth.kcregionalwx;
var maxMessageSize = asm.maxMessageSize;
var webUiBase = asm.webUiBase;

function getWeatherData(msg, station, date) {

	var commandRan = "getWeatherData(msg, "+station+", "+date+")";
	aLog.basicAccessLog(msg, commandRan, "start");
	
	var rData = "DEBUG: function getWeatherData("+station+")";
	var url = webUiBase + "Wx";
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

function getWeatherStations(msg, searchString) {

	var commandRan = "getWeatherStations("+searchString+")";
	aLog.basicAccessLog(msg, commandRan, "start");

	var rData = "DEBUG: getWeatherStations() called.";
	var url = webUiBase + "Wx";
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

function sendMessageOnStartup(client, myArgs) {

        if(asm.isSet(myArgs) && myArgs != '') {
                var messageToSendOnStartUp = myArgs[0];
                var channel = client.channels.get(homeForBot);
                if(asm.isSet(myArgs[1]) && myArgs[1] != '') {
                        let tFile = myArgs[1];
                        return new Promise(resolve => {
                                channel.send(messageToSendOnStartUp, new Discord.Attachment(tFile));
                        }).then(value => {
                                client.destroy();
                                console.log("Good");
                        }).catch(error => {
                                client.destroy();
                                console.log("Closed");
                        });
                } else {
                        try {
                                channel.send(messageToSendOnStartUp);
                        } catch(e) {
                                console.log("ERROR: ", e);
                        }
                }
                console.log(myArgs);
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
	asm.trimForDiscord(dataBack);

	return dataBack;

}

client.on('ready', async () => {
	
	console.log(`SUCCESS---->\nasWxBot build `+bBuild+` logged in as ${client.user.tag}!\n`);
	var myArgs = process.argv.slice(2);
	sendMessageOnStartup(client, myArgs);

});

client.on('message', msg => {

	if((msg.content).includes("coming down")) {
		
		resp.getWeatherSarcastic(msg);
		
	} else {
		
		var msgArray = (msg.content).split(' ');
		var matchMsg = msgArray[0];
		var ncmsg = (matchMsg).toLowerCase();
		var msgBack = "DEBUG: Message back not set yet!";
	
		switch(ncmsg) {
	
			case "!help":
				resp.generateHelpMessage(msg);
				break;
			
			case "!ping":
				msgBack = "pong!";
				msg.reply(msgBack);
				break;
				
			case "!serverInfo":
				resp.getServerInfo(msg);
				break;
	
			case "!test1":
				msg.reply(asm.trimForDiscord2("Testing"));
				break;
				
			case "!test2":
				msg.reply("Testing image attachment to bot message:", { files : ['../img/DiscordApp/pwned.jpg'] });
				break;
				
			case "cam":
				resp.getWeatherCameras(msg);
				break;
				
			case "camloop":
				resp.getWeatherCameraLoop(msg);
				break;
	
			case "cf6":
				var month = msgArray[1];
				resp.getCf6Data(msg, month);
				break;
	
			case "find":
				var station = "",
					date = "";
				try { station = msgArray[1].toUpperCase(); } catch (e) { console.log(e); }
				try { date = msgArray[2]; } catch (e) { console.log(e); }
			 	getWeatherData(msg, station, date);
				break;
	
			case "forecast": 
				resp.getWeatherForecast(msg);
				break;
			
			case "lol":
				resp.lol(msg);
				break;
				
			case "nearby":
				resp.getNearMe(msg);
				break;
		
			case "pho":
				resp.pho(msg);
				break;
	
			case "quote":
				resp.getRandomQuotes(msg);
				break;
	
			case "radar":
				var site = msgArray[1];
				resp.getWeatherRadar(msg, site);
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
				resp.getServerInfo(msg);
				break;
	
			case "weather":
				resp.getWeatherLatest(msg);
				break;

		}
	}

});

client.login(auth.token);
