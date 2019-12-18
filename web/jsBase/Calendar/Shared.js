/*
by Anthony Stump
Created: 17 Dec 2019
Updated: 18 Dec 2019
*/

Date.prototype.addDays = function(days) {
    var date = new Date(this.valueOf());
    date.setDate(date.getDate() + days);
    return date;
}

Date.prototype.addHours = function(h) {
  this.setTime(this.getTime() + (h*60*60*1000));
  return this;
}

function addEventDateTimes(eds) {
	eds.forEach(function (tEvent) {
		var tEventStart = new Date(tEvent.eventStart);
		var tEventEnd = new Date(tEvent.eventEnd);
		var tLastModified = new Date(tEvent.lastModified);
		tEvent.tEventStart = tEventStart;
		tEvent.tEventEnd = tEventEnd;
		tEvent.tLastModified = tLastModified;
	});
	console.log(JSON.stringify(eds));
	return eds;
}

function getDayPosition(dayString) {
    var dayPos = 0;
    switch(dayString) {
        case 0: dayPos = "SUN"; break;
        case 1: dayPos = "MON"; break;
        case 2: dayPos = "TUE"; break;
        case 3: dayPos = "WED"; break;
        case 4: dayPos = "THU"; break;
        case 5: dayPos = "FRI"; break;
        case 6: dayPos = "SAT"; break;
        case "SUN": dayPos = 0; break;
        case "MON": dayPos = 1; break;
        case "TUE": dayPos = 2; break;
        case "WED": dayPos = 3; break;
        case "THU": dayPos = 4; break;
        case "FRI": dayPos = 5; break;
        case "SAT": dayPos = 6; break;
    }
    return dayPos;
}

function getEvents() {
    aniPreload("on");
    var thePostData = { "doWhat": "getEventsBasic" };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("WebCal"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    aniPreload("off");
                    wcEventData = addEventDateTimes(data);
                    console.log("DEBUG: Fetched events to wcEventData");
                    populateCalendarViewHolder("byMonth");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request to get Calendar Events failed!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function prettyDayOfMonthPopper(dow, dom) {
    var formatted;
    switch(dom) {
        case 0: 
            formatted = "";
            break;
        default: 
            formatted = "<span style='color: red;'>" +
                    "<strong>" + dow + "<br/>" + dom + "</strong>" +
                    "</span>"; 
            break;
    }
    return formatted;
}

function prettyMonth(numMo) {
    var months = [ 
        "January", "February", "March", "April",
        "May", "June", "July", "August",
        "September", "October", "November", "December"
    ];
    return months[numMo];
}

