/* 
by Anthony Stump
Created: 5 Mar 2020
Updated: on creation
 */

const aLog = require('./accessLog.js');

module.exports = {

		getWeatherRadar: function(msg, site) {
			
			let commandRan = "getWeatherRadar(msg, site)";
			
			aLog.basicAccessLog(msg, commandRan, "start");
			
			var radarSite = "EAX";
			if(asm.isSet(site)) { radarSite = site.toUpperCase(); }
			var radarFileNew = "/media/sf_SharePoint/Get/Radar/" + radarSite + "/_BLatest.jpg";
			msg.reply("Latest radar image for " + radarSite + ":\n", { files :  [ radarFileNew ] });
			
			aLog.basicAccessLog(msg, commandRan, "stop");

		}

}
