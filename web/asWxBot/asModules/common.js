/* 
by Anthony Stump
Created: 17 Oct 2019
Updated: 7 Mar 2020
 */

var maxMessageSize = 256;

module.exports = {

	maxMessageSize: maxMessageSize,

	autoUnits: function (tVal) {
    		var tSuffix = "";
    		if (tVal < 0) { tVal = ""; tSuffix = "Error!"; }
    		if (tVal > 1000) { tVal = (tVal/1000).toFixed(1); tSuffix = "K"; }
    		if (tVal > 1000) { tVal = (tVal/1000).toFixed(1); tSuffix = "M"; }
    		if (tVal > 1000) { tVal = (tVal/1000).toFixed(1); tSuffix = "G"; } 
    		if (tVal > 1000) { tVal = (tVal/1000).toFixed(1); tSuffix = "T"; } 
    		if (tVal > 1000) { tVal = (tVal/1000).toFixed(1); tSuffix = "P"; } 
    		var formatting = tVal + tSuffix;
    		return formatting;
	},

	isSet: function (what) {
		if(typeof what !== 'undefined' && what) {
			return true;
		} else {
			return false;
		}
	},
	
	parseDate: function(oH) {

		var di = new Date();
		if(isSet(oH)) { di = new Date(di.setHours(di.getHours()- oH)); }
		var dY = di.getFullYear();
		var dM = ("0" + (di.getMonth()+1)).slice(-2);
		var dD = ("0" + (di.getDate())).slice(-2);
		var dh = di.getHours();
		var dm = di.getMinutes();
		var ds = di.getSeconds();
		var jsonBack = {
			"dY": dY, "dM": dM, "dD": dD,
			"dh": dh, "dm": dm, "ds": ds
		};

		return jsonBack;

	},

	returnTimestamp: function(oH) {
		var tJ = parseDate(oH);
		return tJ.dY + "-" + tJ.dM + "-" + tJ.dD + " " + tJ.dh + ":" + tJ.dm + ":" + tJ.ds;
	},

	trimForDiscord: function(message) { 
		return message.substring(0, maxMessageSize);
	},

	webUiBase: "https://localhost:8444/asWeb/r/"

}
