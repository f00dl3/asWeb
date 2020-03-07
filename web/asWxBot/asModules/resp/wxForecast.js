/* 
by Anthony Stump
Created: 7 Mar 2020
Updated: on creation
 */

const axios = require('axios');
const aLog = require('./../accessLog.js');
const asm = require('./../commmon.js');

module.exports = {

		getWeatherForecast: function(msg) { getWeatherForecast(msg) }

}

function getWeatherForecast(msg) {

	var commandRan = "getWeatherForecast(msg)";
	aLog.basicAccessLog(msg, commandRan, "start");

	var rData = "getWeatherLatest() did not get data back yet!";
	var url = asm.webUiBase + "Wx";
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

	aLog.basicAccessLog(msg, commandRan, "stop");
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
		if(asm.isSet(jd.HRRR)) { dataHRRR = JSON.parse(jd.HRRR); }
	});
	// Build function to convert UTC to Central Time
	rData += "\nModel run: " + runString +
		"\nHour | T | D | Precip";
	msg.reply(asm.trimForDiscord(rData));
	gfsFh.forEach(function(tfhA) {
		var tfh = tfhA.FHour;
		if(asm.isSet(tfh)) {
			if(asm.isSet(dataHRRR["T0_"+tfh])) {
				rData = "\n+" + tfh + " | " +
					wxs.conv2Tf(dataHRRR["T0_"+tfh]) + " | " +
					wxs.conv2Tf(dataHRRR["D0_"+tfh]) + " | " +
					dataHRRR["PRATE_"+tfh];
				msg.reply(asm.trimForDiscord(rData));
			}
		}
	});

}