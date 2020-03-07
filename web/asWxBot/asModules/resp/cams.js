/* 
by Anthony Stump
Created: 7 Mar 2020
Updated: on Creation
 */

const aLog = require('./../accessLog.js');
const asm = require('./../commmon.js');

module.exports = {

		getWeatherCameras: function(msg) { getWeatherCameras(msg) },
		getWeatherCameraLoop: function(msg) { getWeatherCameraLoop(msg) }

}

function getWeatherCameras(msg) {
	var commandRan = "getWeatherCameras(msg)";
	aLog.basicAccessLog(msg, commandRan, "start");
	var camSnap = "/dev/shm/tomcatShare/cache/CamLive_Public.jpeg";
	msg.reply("Camera Snapshot", { files :  [ camSnap ] });
	aLog.basicAccessLog(msg, commandRan, "stop");
}

function getWeatherCameraLoop(msg) {
	var commandRan = "getWeatherCamLoop(msg)";
	aLog.basicAccessLog(msg, commandRan, "start");
	var camLoop = "/dev/shm/tomcatShare/cache/CamLoopPublic.mp4";
	msg.reply("Camera Loop", { files :  [ camLoop ] });
	aLog.basicAccessLog(msg, commandRan, "stop");
}