/* 
 * by Anthony Stump
 * Created: 3 Oct 2019
 * Updated: see bUpdated
 */

const Discord = require('discord.js');
const client = new Discord.Client();
const auth = require('./auth.json');
const fetch = require('node-fetch');
const FormData = require('form-data');
const axios = require('axios');
const asm = require('./asModules/common.js');
const wxs = require('./asModules/wxShare.js');
const aLog = require('./asModules/accessLog.js');
const resp = require('./asModules/responses.js');
process.env["NODE_TLS_REJECT_UNAUTHORIZED"] = 0;

var bBuild = 72;
var bUpdated = "31 MAR 2020";
var homeForBot = auth.kcregionalwx;
var alertChan = auth.wxalerts;
var maxMessageSize = asm.maxMessageSize;
var webUiBase = asm.webUiBase;

function sendMessageOnStartup(client, myArgs) {

        if(asm.isSet(myArgs) && myArgs != '') {
                var messageToSendOnStartUp = myArgs[0];
                var channel = client.channels.get(alertChan);
                /* if(messageToSendOnStartup.toLowerCase().includes("reddit")) {
                	channel = clent.channels.get(homeForBot); 
            	} */
                if(asm.isSet(myArgs[1]) && myArgs[1] != '') {
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

client.on('message', msg => {

	if((msg.content).includes("coming down")) {
		
		resp.getWeatherSarcastic(msg);
		
	} else {
		
		var msgArray = (msg.content).split(' ');
		var matchMsg = msgArray[0];
		var ncmsg = (matchMsg).toLowerCase();
		var msgBack = "DEBUG: Message back not set yet!";
	
		switch(ncmsg) {
	
			case "!help":
				resp.generateHelpMessage(msg);
				break;
			
			case "!ping":
				msgBack = "pong!";
				msg.reply(msgBack);
				break;
				
			case "!serverInfo":
				resp.getServerInfo(msg);
				break;
	
			case "!test1":
				msg.reply(asm.trimForDiscord2("Testing"));
				break;
				
			case "!test2":
				msg.reply("Testing image attachment to bot message:", { files : ['../img/DiscordApp/pwned.jpg'] });
				break;
				
			case "cam":
				resp.getWeatherCameras(msg);
				break;
				
			case "camloop":
				resp.getWeatherCameraLoop(msg);
				break;
	
			case "cf6":
				var month = msgArray[1];
				resp.getCf6Data(msg, month);
				break;
	
			case "find":
				var station = "",
					date = "";
				try { station = msgArray[1].toUpperCase(); } catch (e) { console.log(e); }
				try { date = msgArray[2]; } catch (e) { console.log(e); }
			 	resp.getWeatherData(msg, station, date);
				break;
	
			case "forecast": 
				resp.getWeatherForecast(msg);
				break;
			
			case "lol":
				resp.lol(msg);
				break;
				
			case "nearby":
				resp.getNearMe(msg);
				break;
		
			case "pho":
				resp.pho(msg);
				break;
	
			case "quote":
				resp.getRandomQuotes(msg);
				break;
	
			case "radar":
				var site = msgArray[1];
				resp.getWeatherRadar(msg, site);
				break;
	
			case "search":
				var searchString = "";
				try { searchString = msgArray[1].toLowerCase(); } catch (e) { console.log(e); }
				if(searchString.length < 4) {
					msg.reply("Please enter more characters of the location!");
				} else {
					msg.reply("Finding stations similar to [" + searchString + "]...");
					resp.getWeatherStations(msg, searchString);
				}
				break;
	
			case "server":
				resp.getServerInfo(msg);
				break;

			case "stock":
				var ticker = "";
				try { ticker = msgArray[1].toUpperCase(); } catch (e) { console.log(e); }
			 	resp.getStocks(msg, ticker);
				break;	
	
			case "weather":
				resp.getWeatherLatest(msg);
				break;

		}
	}

});

client.login(auth.token);
