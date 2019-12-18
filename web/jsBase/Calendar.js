/* 
by Anthony Stump
Created: 16 Dec 2019
Updated: 18 Dec 2019
 */

var wcEventData;

function actOnCalendarSubmit(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    var thisFormDataJ = dojo.formToJson(this.form);
    if(isSet(thisFormData.QuickStart) && isSet(thisFormData.QuickStartTime) && isSet(thisFormData.QuickTitle)) {
        putQuickCalendarEntry(thisFormData);
    } else {
        window.alert("Incomplete data!\nCheck input and try again!\n" + thisFormDataJ);
    }
}

function populateCalendarViewHolder(type) {
	
	var target = "calendarViewHolder";
	
	switch(type) {
		case "byMonth":
			monthlyCalendar(target);
			break;
	}
	
}

function putQuickCalendarEntry(formData) {
    aniPreload("on");
    var xhArgs = {
        preventCache: true,
        url: getResource("WebCal"),
        postData: formData,
        handleAs: "json",
        timeout: timeOutMilli,
        load: function(data) {
            aniPreload("off");
            showNotice("Calendar entry " + data.EntryID + " added!");
            console.log(data.ErrorLog);
            getAnthonyOverviewData();
        },
        error: function(data, iostatus) {
            aniPreload("off");
            window.alert("request for QuickCal Entry FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
        }
    };
    dojo.xhrPost(xhArgs);
}

function quickCalendar(target) {	
	var quickWebCalEntryForm = "<span>Quick Calendar Entry</span>" +
		    "<div class='table'><form class='tr' id='QuickCalFormTr'>" +
		    "<span class='td'><input name='QuickStart' type='date' value='' style='width: " + dateEntryWidth + "px;'/></span>" +
		    "<span class='td'><input name='QuickStartTime' type='time' value='' style='width: " + timeEntryWidth + "px'/></span>" +
		    "<span class='td'><input name='QuickTitle' type='text' value='' style='width: 100px;' />" +
		    "<input name='doWhat' type='hidden' value='setQuickCalEntry'/>" +
		    "<button id='QuickCalBtn' name='QuickCalendar' class='UButton'>Go</button>" +
		    "</span></form></div>";
	dojo.byId(target).innerHTML = quickWebCalEntryForm;
    var quickCalButton = dojo.byId("QuickCalBtn");
    dojo.connect(quickCalButton, "click", actOnCalendarSubmit);
}

function init() {
	getEvents();
}

dojo.ready(init);


