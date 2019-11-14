class Common {

	function autoUnits(tVal) {
	    var tSuffix = "";
	    if (tVal < 0) { tVal = ""; tSuffix = "Error!"; }
	    if (tVal > 1000) { tVal = (tVal/1000).toFixed(1); tSuffix = "K"; }
	    if (tVal > 1000) { tVal = (tVal/1000).toFixed(1); tSuffix = "M"; }
	    if (tVal > 1000) { tVal = (tVal/1000).toFixed(1); tSuffix = "G"; } 
	    if (tVal > 1000) { tVal = (tVal/1000).toFixed(1); tSuffix = "T"; } 
	    if (tVal > 1000) { tVal = (tVal/1000).toFixed(1); tSuffix = "P"; } 
	    var formatting = tVal + tSuffix;
	    return formatting;
	}

	function basicAccessLog(msg, input, phase) {
		var requestor = "user";
		try { requestor = msg.author.tag; } catch (e) { console.log("ERROR", e); }
		console.log("\nACCESS: " + requestor + " " + phase + " " + input);
	}

	function conv2Tf(tC) {
		var tF = (tC * 9/5) + 32;
		return tF.toFixed(0);
	}

	function parseDate(oH) {

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

	}

	function returnTimestamp(oH) {
		var tJ = parseDate(oH);
		return tJ.dY + "-" + tJ.dM + "-" + tJ.dD + " " + tJ.dh + ":" + tJ.dm + ":" + tJ.ds;
	}

	function trimForDiscord(message) { 

		var maxMessageSize = 256;
		return message.substring(0, maxMessageSize);

	}

}

module.exports = Common;
