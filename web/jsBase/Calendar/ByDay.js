/*
by Anthony Stump
Created 18 Dec 2019
Updated 27 Dec 2019
*/

function addCalendarEventByHour() {
	dojo.stopEvent(event);
	var thisFormData = dojo.formToObject(this.form);
	window.alert("NOT BUILT " + thisFormData.dateTime);
}

function popupHourly(todaysEvents, tDay) {

    var monthName = prettyMonth(tDay.getMonth());
    var yearFull = tDay.getFullYear();
    
	var rData = "<div class='UPopO'>" +
		"<strong>" + monthName + " " + tDay.getDate() + ", " + yearFull + "</strong><br/>" +
		"<div class='table'>";
	
	todaysEvents.forEach(function (tev) {    
		var eventFontColor = "white";
		rData += "<li><form>" +
			"<input type='hidden' name='eventId' value='" + parseInt(tev.eventId, 10) + "'/>" + 
			tev.tEventStart.getHours() + ":" + tev.tEventStart.getMinutes() + " - ";
			if(tev.cal_summary == "A") { 
					eventFontColor = "yellow";
    				rData += " <button class='UButton deleteCal'>X</button>";
			}
			rData += "<span style='color: " + eventFontColor + ";'>" + tev.summary + "</span>" +
				"</form></li>";
	});
		
	rData += "</div></div>";
	

	return rData; 
	
}
