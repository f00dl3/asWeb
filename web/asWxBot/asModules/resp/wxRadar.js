/* 
by Anthony Stump
Created: 5 Mar 2020
Updated: 6 Mar 2020
 */

const aLog = require('./../accessLog.js');
const asm = require('./../commmon.js');

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
