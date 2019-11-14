
class Responders {

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

}

module.export = Responders;

