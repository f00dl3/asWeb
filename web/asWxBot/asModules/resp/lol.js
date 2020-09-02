/* 
by Anthony Stump
Created: 4 Mar 2020
Updated: 29 Aug 2020
 */

const aLog = require('./../accessLog.js');

module.exports = {

	getF: function(msg) {
		var commandRan = "getF(msg)";
		msg.reply("https://gph.is/g/4bW8kdG");
		aLog.basicAccessLog(msg, commandRan, "null");
	},

	getHello: function(msg) {
		var commandRan = "getHello(msg)";
		msg.reply(":wave: !! OMG Hai !! :wave:");
		aLog.basicAccessLog(msg, commandRan, "null");
	},

	getHeyBot: function(msg) {
		var commandRan = "getHeyBot(msg)";
		msg.reply("Yes? :robot:");
		aLog.basicAccessLog(msg, commandRan, "null");
	},
		
	getLaughing: function(msg) {
		var commandRan = "getLaughing(msg)";
		var randNum = Math.floor(Math.random() * 2);
		switch(randNum) {
			case 0: msg.reply("Was that really that funny?"); break;
			case 1: msg.reply("That was lame. I will moon you :peach:"); break;
		}
		aLog.basicAccessLog(msg, commandRan, "null");	
	},

	getRip: function(msg) {
		var commandRan = "getRip(msg)";
		var randNum = Math.floor(Math.random() * 3);
		switch(randNum) {
			case 0: msg.reply("Whoops ripped my pants :poop:"); break;
			case 1: msg.reply("Phew get some air freshener in here man!"); break;
			case 2: msg.reply("Did you rip your pants?"); break;
		}
		aLog.basicAccessLog(msg, commandRan, "null");
	},

	getSmile: function(msg) {
		var commandRan = "getSmile(msg)";
		msg.reply(":hear_no_evil:");
		aLog.basicAccessLog(msg, commandRan, "null");
	},

	getShocked: function(msg) {
		var commandRan = "getShocked(msg)";
		msg.reply(":hear_no_evil:");
		aLog.basicAccessLog(msg, commandRan, "null");
	}

}
