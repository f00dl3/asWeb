/* 
by Anthony Stump
Created: 4 Mar 2020
Updated: 4 Jul 2020
 */

const aLog = require('./../accessLog.js');

module.exports = {

	getLaughing: function (msg) {
		var commandRan = "getLaughing(msg)";
		var randNum = Math.floor(Math.random() * 2);
		switch(randNum) {
			case 0: msg.reply("Was that really that funny?"); break;
			case 1: msg.reply("That was lame. I will moon you :peach:"); break;
		}
		aLog.basicAccessLog(msg, commandRan, "null");	
	}

}
