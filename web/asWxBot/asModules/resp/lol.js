/* 
by Anthony Stump
Created: 4 Mar 2020
Updated: 6 Mar 2020
 */

const aLog = require('./../accessLog.js');

module.exports = {

	getLaughing: function (msg) {
		var commandRan = "getLaughing(msg)";
		msg.reply("Was that really that funny?");
		aLog.basicAccessLog(msg, commandRan, "null");	
	}

}
