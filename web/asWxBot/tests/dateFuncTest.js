function parseDate(oH) {

	var di = new Date();
	if(typeof oH !== 'undefined') { di = new Date(di.setHours(di.getHours()- oH)); }
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

console.log("DEBUG datePre: " + JSON.stringify(parseDate(10)));
console.log("DEBUG dateNow: " + JSON.stringify(parseDate()));
