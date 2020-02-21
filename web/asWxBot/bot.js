//const asModules = require('./asModules/common.js');
const Discord = require('discord.js');
const client = new Discord.Client();
const auth = require('./auth.json');
const fetch = require('node-fetch');
const FormData = require('form-data');
const axios = require('axios');
process.env["NODE_TLS_REJECT_UNAUTHORIZED"] = 0;

var bBuild = 50;
var bUpdated = "20 FEB 2020";
var webUiBase = "https://localhost:8444/asWeb/r/";
var homeForBot = auth.kcregionalwx;
var maxMessageSize = 256;

function autoUnits(tVal) {
    var tSuffix = "";
    if (tVal < 0) { tVal = ""; tSuffix = "Error!"; }
    if (tVal > 1000) { tVal = (tVal/1000).toFixed(1); tSuffix = "K"; }
    if (tVal > 1000) { tVal = (tVal/1000).toFixed(1); tSuffix = "M"; }
    if (tVal > 1000) { tVal = (tVal/1000).toFixed(1); tSuffix = "G"; } 
    if (tVal > 1000) { tVal = (tVal/1000).toFixed(1); tSuffix = "T"; } 
    if (tVal > 1000) { tVal = (tVal/1000).toFixed(1); tSuffix = "P"; } 
    var formatting = tVal + tSuffix;
    return formatting;
}

function basicAccessLog(msg, input, phase) {
	var requestor = "user";
	try { requestor = msg.author.tag; } catch (e) { console.log("ERROR", e); }
	var accessString = "ACCESS: " + requestor + " " + phase + " " + input;
	var url = webUiBase + "Logs";
	var pData = "doWhat=setDiscordAccess" +
			"&attempt=" + accessString;
	axios.post(url, encodeURI(pData)).then((res) => {
		console.log("Success log!");
	}).catch((error) => {
		console.log(error)
	});
}

function conv2Tf(tC) {
	var tF = (tC * 9/5) + 32;
	return tF.toFixed(0);
}

function generateHelpMessage(msg) {

	var commandRan = "generateHelpMessage(msg)";
	basicAccessLog(msg, commandRan, "start");
	var apiVer = "UNSYNCH";
	var helpMessageHeader = "asWxBot (f00dl3) - build "+bBuild+" ("+bUpdated+") ==> Commands:";
	var helpMessageBody = "\n\'!ping\': Return \'pong\' reply back to user" +
		"\n\'cam\': Get bot\'s cameras!" +
		"\n\'camloop\': Get bot\'s camera video loop!" +
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

function getLaughing(msg) {
	var commandRan = "getLaughing(msg)";
	msg.reply("Was that really that funny?");
	basicAccessLog(msg, commandRan, "null");	
}

function getNearMe(msg) {
	var commandRan = "getNearMe(msg)";
	msg.reply("What do you think this is, Google?");
	basicAccessLog(msg, commandRan, "null");
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
	if(isSet(date)) { 
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

function getWeatherCameras(msg) {
	var commandRan = "getWeatherCameras(msg)";
	basicAccessLog(msg, commandRan, "start");
	var camSnap = "/dev/shm/tomcatShare/cache/CamLive_Public.jpeg";
	msg.reply("Camera Snapshot", { files :  [ camSnap ] });
	basicAccessLog(msg, commandRan, "stop");
}

function getWeatherCameraLoop(msg) {
	var commandRan = "getWeatherCamLoop(msg)";
	basicAccessLog(msg, commandRan, "start");
	var camLoop = "/dev/shm/tomcatShare/cache/CamLoopPublic.mp4";
	msg.reply("Camera Loop", { files :  [ camLoop ] });
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
	var radarFileNew = "/media/sf_SharePoint/Get/Radar/" + radarSite + "/_BLatest.gif";
	msg.reply("Latest radar image for " + radarSite + ":\n", { files :  [ radarFileNew ] });

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

function isSet(what) {
	if(typeof what !== 'undefined' && what) {
		return true;
	} else {
		return false;
	}
}

function parseDate(oH) {

	var di = new Date();
	if(isSet(oH)) { di = new Date(di.setHours(di.getHours()- oH)); }
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

function respondRandomQuotes(msg, rqDataIn) {
	var quotesHere = rqDataIn.length;
	var rI = (Math.random() * (quotesHere-1)).toFixed(0);
	console.log("DEBUG: Random number = " + rI + " - total quotes: " + quotesHere);
	var thisQuote = rqDataIn[rI];
	msg.reply(thisQuote.Quote + " -" + thisQuote.Author);
}

function respondServerInfo(msg, dataIn) {
	
	console.log(dataIn[0]);
	var td = dataIn[0];
	var xj = td.dtExpandedJSONData;
	
	var cpuAverage = ((
			td.dtCPULoad1 +
			td.dtCPULoad2 +  
			td.dtCPULoad3 + 
			td.dtCPULoad4 + 
			td.dtCPULoad5 + 
			td.dtCPULoad6 + 
			td.dtCPULoad7 + 
			td.dtCPULoad8) / 8).toFixed(2);
	
	//var memUse = ();
	var networkUse = (td.dtOctestIn + td.dtOctetsOut);
	var dbRows = (td.dtMySQLRowsCore + td.dtMySQLRowsFeeds + td.dtMySQLRowsWxObs + td.dtMySQLRowsNetSNMP);	
	var linesCode = (
			xj.LOC_aswjCss + 
			xj.LOC_asUtilsJava + 
			xj.LOC_aswjJava + 
			xj.LOC_aswjJs +
			xj.LOC_aswjJsp); 
	
	msg.reply("Latest Server Info:");
	msg.reply("\nData Timestamp:" + td.WalkTime);
	msg.reply("\nCPU Utilization: " + cpuAverage + "%");
	msg.reply("\nLoad Index (1 min): " + td.dtLoadIndex1);
	/* msg.reply("\nDisk Use 1: " + autoUnits(td.dtK4RootU) + "/" + autoUnits(td.dtK4Root) +
			" (" + (td.dtK4RootU/td.dtK4Root) + "%)");
	msg.reply("\nDisk Use 2: " + autoUnits(xj.dtK4Extra1U) + "/" + autoUnits(xj.dtK4Extra1) +
			" (" + (xj.dtK4Extra1U/xj.dtK4Extra1) + "%)"); */
	msg.reply("\nNetwork Counters: " + autoUnits(networkUse));
	msg.reply("\nUPS Stats: " + td.dtUPSLoad + "% (" + td.dtUPSTimeLeft + " mins)");
	msg.reply("\nDatabase Rows: " + autoUnits(dbRows));
	msg.reply("\nLines of Code: " + autoUnits(linesCode));
		
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
					(isSet(sr.City)) && ((sr.City).toLowerCase().includes(tWord.toLowerCase())) ||
					(isSet(sr.Station)) && ((sr.Station).toLowerCase().includes(tWord.toLowerCase())) ||
					(isSet(sr.Description)) && ((sr.Description).toLowerCase().includes(tWord.toLowerCase())) ||
					tWord.includes("S: ") && (isSet(sr.State)) && ((sr.State).toLowerCase().includes(tWord.toLowerCase())) 
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
				(isSet(sr.City)) && ((sr.City).toLowerCase().includes(tWord.toLowerCase())) ||
				(isSet(sr.Station)) && ((sr.Station).toLowerCase().includes(tWord.toLowerCase())) ||
				(isSet(sr.Description)) && ((sr.Description).toLowerCase().includes(tWord.toLowerCase())) ||
				tWord.includes("S: ") && (isSet(sr.State)) && ((sr.State).toLowerCase().includes(tWord.toLowerCase()))
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
	if(isSet(jds.WindGust)) { finalMessage += "\nGusts: " + jds.WindGust + " mph"; }
	msg.reply(trimForDiscord(finalMessage));

}

function respondWeatherDataTable(station, data, msg) {
	
	var lastMessage = "Station: " + station;
	msg.reply(trimForDiscord(lastMessage));

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

function respondWeatherForecast(msg, last, heightsIn, hours, runs, jmd) {

	var tAutoCounter = 0;
	var searchString = last.RunString;
	var runString, dataHRRR;
	var precipTot = 0.00;
	var rData = "GRIB2 HRRR JSON Model Output Data for KOJC";
	var heights = [];
	heightsIn.forEach(function (hgt) {
		if(hgt.HeightMb !== 0) {
			heights.push(hgt.HeightMb);
		}			
	});
	var gfsFh = [];
	hours.forEach(function(tFh) { gfsFh.push(tFh); });
	var reportingModels = "HRRR";
	jmd.forEach(function(jd) {
		runString = jd.RunString;
		if(isSet(jd.HRRR)) { dataHRRR = JSON.parse(jd.HRRR); }
	});
	// Build function to convert UTC to Central Time
	rData += "\nModel run: " + runString +
		"\nHour | T | D | Precip";
	msg.reply(trimForDiscord(rData));
	gfsFh.forEach(function(tfhA) {
		var tfh = tfhA.FHour;
		if(isSet(tfh)) {
			if(isSet(dataHRRR["T0_"+tfh])) {
				rData = "\n+" + tfh + " | " +
					conv2Tf(dataHRRR["T0_"+tfh]) + " | " +
					conv2Tf(dataHRRR["D0_"+tfh]) + " | " +
					dataHRRR["PRATE_"+tfh];
				msg.reply(trimForDiscord(rData));
			}
		}
	});

}

function returnTimestamp(oH) {
	var tJ = parseDate(oH);
	return tJ.dY + "-" + tJ.dM + "-" + tJ.dD + " " + tJ.dh + ":" + tJ.dm + ":" + tJ.ds;
}

function sendMessageOnStartup(client, myArgs) {

        if(isSet(myArgs) && myArgs != '') {
                var messageToSendOnStartUp = myArgs[0];
                var channel = client.channels.get(homeForBot);
                if(isSet(myArgs[1]) && myArgs[1] != '') {
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
	trimForDiscord(dataBack);

	return dataBack;

}

function trimForDiscord(message) { 

	var maxMessageSize = 256;
	return message.substring(0, maxMessageSize);

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
			
		case "cam":
			getWeatherCameras(msg);
			break;
			
		case "camloop":
			getWeatherCameraLoop(msg);
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
		
		case "lol":
			getLaughing(msg);
			break;
			
		case "nearby":
			if(msgArray)
			getNearMe(msg);
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
