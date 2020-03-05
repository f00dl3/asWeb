/* 
by Anthony Stump
Created: 4 Mar 2020
Updated: on creation
 */

const axios = require('axios');
const asm = require('./common.js');

module.exports = {

	basicAccessLog: function(msg, input, phase) {
		var requestor = "user";
		try { requestor = msg.author.tag; } catch (e) { console.log("ERROR", e); }
		var accessString = "ACCESS: " + requestor + " " + phase + " " + input;
		var url = asm.webUiBase + "Logs";
		var pData = "doWhat=setDiscordAccess" +
				"&attempt=" + accessString;
		axios.post(url, encodeURI(pData)).then((res) => {
			console.log("Success log!");
		}).catch((error) => {
			console.log(error)
		});
	}
}
