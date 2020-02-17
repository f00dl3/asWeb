//const asModules = require('./asModules/common.js');
const Discord = require('discord.js');
const client = new Discord.Client();
const auth = require('./auth.json');

var bBuild = "TEST";
var bUpdated = "16 FEB 2020";
var homeForBot = auth.bottesting;

function isSet(what) {
	if(typeof what !== 'undefined' && what) {
		return true;
	} else {
		return false;
	}
}

function sendMessageOnStartup(client, myArgs) {

    if(isSet(myArgs) && myArgs != '') {
            var messageToSendOnStartUp = myArgs[0];
            var channel = client.channels.get(homeForBot);
            if(isSet(myArgs[1]) && myArgs[1] != '') {
                    let tFile = myArgs[1];
                    return new Promise(resolve => {
                            channel.send(messageToSendOnStartUp, new Discord.Attachment(tFile));
                    }).then(value => {
                            client.destroy();
                            console.log("Good");
                    }).catch(error => {
                            client.destroy();
                            console.log("Closed");
                    });
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
