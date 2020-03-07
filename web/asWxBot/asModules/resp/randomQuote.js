/* 
by Anthony Stump
Created: 5 Mar 2020
Updated: 6 Mar 2020
 */

const axios = require('axios');
const aLog = require('./../accessLog.js');
const asm = require('./../common.js');

module.exports = {

		getRandomQuotes: function(msg) {

			var commandRan = "getRandomQuotes(msg)";
			aLog.basicAccessLog(msg, commandRan, "start");

			var rData = "DEBUG: getRandomQuotes() did not get data back yet!";
			var url = asm.webUiBase + "Entertainment";
			var pData = "doWhat=getRandomQuotes";

			axios.post(url, pData).then((res) => { 
				rData = res.data,
				respondRandomQuotes(msg, rData)
			}).catch((error) => {
				console.log(error)
			});

			aLog.basicAccessLog(msg, commandRan, "stop");

		}
		

}

function respondRandomQuotes(msg, rqDataIn) {
	
	var quotesHere = rqDataIn.length;
	var rI = (Math.random() * (quotesHere-1)).toFixed(0);
	console.log("DEBUG: Random number = " + rI + " - total quotes: " + quotesHere);
	var thisQuote = rqDataIn[rI];
	msg.reply(thisQuote.Quote + " -" + thisQuote.Author);
	
}
