/* 
by Anthony Stump
Created: 7 Mar 2020
Updated: 2 Jan 2021
 */

const aLog = require('./../accessLog.js');
const asm = require('./../common.js');

module.exports = {
		
		helpMessage: function(msg) { helpMessage(msg) }

}

function helpMessage(msg) {

	var commandRan = "generateHelpMessage(msg)";
	aLog.basicAccessLog(msg, commandRan, "start");
	var apiVer = "UNSYNCH";
	var helpMessageHeader = "asWxBot (f00dl3) ==> Commands:";
	var helpMessageBody = "\n\'!ping\': Return \'pong\' reply back to user" +
		"\n\'c2f\': Convert Celsius to Farenheit. Use: \'c2f 15\'" +
		"\n\'cam\': Get bot\'s cameras!" +
		"\n\'camloop\': Get bot\'s camera video loop!" +
		"\n\'cf6\': GetCF6 daily climate data. Use: \'cf6 <YYYY-MM>\'" +
		"\n\'f2c\': Convert Farenheit to Celsius. Use: \'f2c 88\'" +
		"\n\'find\': Get weather data for given station. Use: find <station> <YYYY-MM-DD>" +
		"\n\'forecast\': Get latest forecast model output for KOJC (Olathe, KS)" +
		"\n\'quote\': Get a random quote" +
		"\n\'radar\': Get latest weather radar loop. No basemap! Default: EAX. Usage: \'radar <SITE>\'" +
		"\n\'search\': Search station inventory to get station code for \'find\' tool" +
		"\n\'server\': Gets latest server status information" +
		//"\n\'stocks\': Get stock quote for desired stock" + 
		"\n\'weather\': Get latest weather data from KOJC (Olathe, KS)";
	
/*

	var returnData = "DEBUG: function getWebVersion()";
	var url = asm.webUiBase + "WebVersion";
	fetch(url).then((res) => {
		returnData = res.data,
		console.log(res),
		console.log("API asWebUI v"+returnData.Version),
		apiVer = returnData.Version,
		helpMessageHeader = "asWxBot (f00dl3) - API v"+apiVer+"\n";
	});
	
*/
	
	var helpMessage = helpMessageHeader + helpMessageBody;
	msg.reply(helpMessage);

	aLog.basicAccessLog(msg, commandRan, "stop");
	
}
