/*
by Anthony Stump
Created 18 Dec 2019
Updated on Creation
*/

function popupHourly(tDay) {

    var monthName = prettyMonth(tDay.getMonth());
    var yearFull = tDay.getFullYear();
    
	var rData = "<div class='UPopO'>" +
		"<strong>" + monthName + " " + tDay.getDate() + ", " + yearFull + "</strong><br/>" +
		"<div class='table'>";
	
	for(var i = 0; i < 24; i++) {
		var tTime = tDay.addHours(1);
		rData += "<div class='tr'>" +
			"<span class='td'>" + tTime.getHours() + "</span>" +
			"<span class='td'></span>" +
			"<span class='td'><button class='UButton'>+</button></span>" +
			"</div>";
	}
	
	rData += "</div></div>";
	
	return rData; 
	
}