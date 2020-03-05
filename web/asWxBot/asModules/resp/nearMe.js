/* 
by Anthony Stump
Created: 5 Mar 2020
Updated: on creation
 */

const aLog = require('./accessLog.js');

module.exports = {

	getNearMe: function(msg) { getNearMe(msg) }

}

function getNearMe(msg) {
	var commandRan = "getNearMe(msg)";
	msg.reply("What do you think this is, Google?");
	aLog.basicAccessLog(msg, commandRan, "null");
}
