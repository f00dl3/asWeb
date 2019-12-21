/*
by Anthony Stump
Created 18 Dec 2019
Updated 21 Dec 2019
*/

function popupHourly(todaysEvents, tDay) {

    var monthName = prettyMonth(tDay.getMonth());
    var yearFull = tDay.getFullYear();
    
	var rData = "<div class='UPopO'>" +
		"<strong>" + monthName + " " + tDay.getDate() + ", " + yearFull + "</strong><br/>" +
		"<div class='table'>";
	
	for(var i = 0; i < 24; i++) {
		var tTime = tDay.addHours(1);
		var tETime = new Date(tTime);
		tETime.addHours(1);
		rData += "<div class='tr'>" +
			"<span class='td'>" + tTime.getHours() + "</span>" +
			"<span class='td'><ul type='dot'>";

		todaysEvents.forEach(function (tev) {
    		if(
        			(tev.tEventStart).getTime() >= tTime.getTime() &&
        			tev.tEventStart.getTime() < tETime.getTime()
    		) {
        		rData += "<li>" +
        			tev.tEventStart.getHours() + ":" + tev.tEventStart.getMinutes() + " - " +
        			tev.summary +
        			"</li>";
        	}
    	});
		
		rData += "</ul></span>" +
			"<span class='td'><button class='UButton'>+</button></span>" +
			"</div>";
		
		
	}
	
	rData += "</div></div>";
	
	return rData; 
	
}
