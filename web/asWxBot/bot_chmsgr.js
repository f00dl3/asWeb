//const asModules = require('./asModules/common.js');
const Discord = require('discord.js');
const client = new Discord.Client();
const auth = require('./auth.json');

var bBuild = "TEST";
var bUpdated = "15 FEB 2020";
var homeForBot = auth.kcregionalwx;

function isSet(what) {
	if(typeof what !== 'undefined' && what) {
		return true;
	} else {
		return false;
	}
}

function sendMessageOnStartup(client, myArgs) {

	if(isSet(myArgs) && myArgs != '') {
		client.login(auth.token);
		var messageToSendOnStartUp = myArgs[0];
		var channel = client.channels.get(homeForBot);
		if(isSet(myArgs[1]) && myArgs[1] != '') {
			try {
				// THIS PART BROKEN - 15 FEB 2020
				var fileToSend = myArgs[1];
				channel.send(messageToSendOnStartUp); // THIS WORKS FINE! Message is broacast
                channel.send(messageToSendOnStartUp, new Discord.Attachment(fileToSend)).catch(console.error);
                		/* THIS FAILS:
                		 * (node:19833) UnhandledPromiseRejectionWarning: Error: Request to use token, but token was unavailable to the client.
						    at APIRequest.getAuth (/var/lib/tomcat9/webapps/asWeb##200215.1754/asWxBot/node_modules/discord.js/src/client/rest/APIRequest.js:33:11)
						    at APIRequest.gen (/var/lib/tomcat9/webapps/asWeb##200215.1754/asWxBot/node_modules/discord.js/src/client/rest/APIRequest.js:39:54)
						    at resolve (/var/lib/tomcat9/webapps/asWeb##200215.1754/asWxBot/node_modules/discord.js/src/client/rest/RequestHandlers/Sequential.js:59:20)
						    at new Promise (<anonymous>)
						    at SequentialRequestHandler.execute (/var/lib/tomcat9/webapps/asWeb##200215.1754/asWxBot/node_modules/discord.js/src/client/rest/RequestHandlers/Sequential.js:58:12)
						    at SequentialRequestHandler.handle (/var/lib/tomcat9/webapps/asWeb##200215.1754/asWxBot/node_modules/discord.js/src/client/rest/RequestHandlers/Sequential.js:125:10)
						    at execute.then (/var/lib/tomcat9/webapps/asWeb##200215.1754/asWxBot/node_modules/discord.js/src/client/rest/RequestHandlers/Sequential.js:127:12)
						    at <anonymous>
						    at process._tickCallback (internal/process/next_tick.js:188:7)
						(node:19833) UnhandledPromiseRejectionWarning: Unhandled promise rejection. This error originated either by throwing inside of an async function without a catch block, or by rejecting a promise which was not handled with .catch(). (rejection id: 2)
						(node:19833) [DEP0018] DeprecationWarning: Unhandled promise rejections are deprecated. In the future, promise rejections that are not handled will terminate the Node.js process with a non-zero exit code.

*/
			} catch(e) {
				console.log("ERROR: ", er);
		 	}
		} else {
			try {
				channel.send(messageToSendOnStartUp);
			} catch(e) {
				console.log("ERROR: ", e);
		 	}
		}
		console.log(myArgs);
		client.destroy();
	} 
	
}

client.on('ready', async () => {
	
	console.log(`SUCCESS---->\nasWxBot build `+bBuild+` logged in as ${client.user.tag}!\n`);
	var myArgs = process.argv.slice(2);
	sendMessageOnStartup(client, myArgs);

});

client.login(auth.token);
