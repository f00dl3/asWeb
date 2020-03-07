/* 
by Anthony Stump
Created: 4 Mar 2020
Updated: 6 Mar 2020
 */

const aLog = require('./../accessLog.js');

module.exports = {

	getPho: function (msg) {
		var commandRan = "getPho(msg)";
		var randInt = Math.floor(Math.random() * 6);
		var randReply = "phone";
		switch(randInt) {
			case 0: break;
			case 1: randReply = "5"; break;
			case 2: randReply = "restaurants"; break;
			case 3: randReply = "food"; break;
			case 4: randReply = "fish"; break;
			case 5: randReply = "fools"; break
		}
		msg.reply("Searching for " + randReply);
		aLog.basicAccessLog(msg, commandRan, "null");
	}

}
