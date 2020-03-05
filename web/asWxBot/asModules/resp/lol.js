/* 
by Anthony Stump
Created: 4 Mar 2020
Updated: on creation
 */

const aLog = require('./accessLog.js');

module.exports = {

	getLaughing: function (msg) {
		var commandRan = "getLaughing(msg)";
		msg.reply("Was that really that funny?");
		aLog.basicAccessLog(msg, commandRan, "null");	
	}

}
