/* 
by Anthony Stump
Created: 7 Mar 2020
Updated: 8 Mar 2020
 */

const axios = require('axios');
const aLog = require('./../accessLog.js');
const asm = require('./../common.js');

module.exports = {

		getCf6Data: function(msg, month) { getCf6Data(msg, month) }

}

function getCf6Data(msg, month) {

	var commandRan = "getCf6Data(msg, "+month+")";
	aLog.basicAccessLog(msg, commandRan, "start");

	var rData = "CF6 Function";
	var url = asm.webUiBase + "Wx";
	var pData = "doWhat=getCf6Data" +
		"&CF6Search1="+month+"-01" +
		"&CF6Search2="+month+"-31";

	axios.post(url, pData).then((res) => {
		rData = res.data,
		respondCf6Data(msg, rData)
	}).catch((error) => {
		console.log(error)
	});	

	aLog.basicAccessLog(msg, commandRan, "stop");

}

function respondCf6Data(msg, cf6in) {

	var litr = 0;
	var headerMessage = "F6 Climate Data for KCI airport (KMCI)" +
		"\nDate | Hi | Lw | Pcp | Sn | Wnd | Cld | Wx";
	msg.reply(asm.trimForDiscord(headerMessage));

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
		msg.reply(asm.trimForDiscord(returnString));
	});

}
