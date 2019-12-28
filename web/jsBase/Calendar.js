/* 
by Anthony Stump
Created: 16 Dec 2019
Updated: 23 Dec 2019
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

function actOnCalendarSubmit2(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    var thisFormDataJ = dojo.formToJson(this.form);
    if(isSet(thisFormData.dSu) || isSet(thisFormData.dMo) || isSet(thisFormData.dTo) || isSet(thisFormData.dWe) || 
    		isSet(thisFormData.dTh) || isSet(thisFormData.dFr) || isSet(thisFormData.dSa)) {
    	thisFormData.repeatingEvent = "true";
    } else {
    	thisFormData.repeatingEvent = "false";
    }
    if(isSet(thisFormData.QuickStart) && isSet(thisFormData.QuickStartTime) && isSet(thisFormData.QuickTitle)) {
        putQuickCalendarEntry2(thisFormData);
    } else {
        window.alert("Incomplete data!\nCheck input and try again!\n" + thisFormDataJ);
    }
}

function populateCalendarViewHolder(type) {
	
	var target = "calendarViewHolder";
	var quickCalHolder = "qcHolder";
	
	switch(type) {
		case "byMonth":
			monthlyCalendar(target);
			quickCalendar2(quickCalHolder);
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

function putQuickCalendarEntry2(formData) {
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
        },
        error: function(data, iostatus) {
            aniPreload("off");
            window.alert("request for QuickCal Entry 2 FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
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

function quickCalendar2(target) {	
	var quickWebCalEntryForm = "<span>Quick Calendar Entry</span>" +
		    "<div class='table'><form class='tr' id='QuickCalFormTr'>" +
		    "<span class='td'><input name='QuickStart' type='date' value='' style='width: " + dateEntryWidth + "px;'/></span>" +
		    "<span class='td'><input name='QuickStartTime' type='time' value='' style='width: " + timeEntryWidth + "px'/></span>" +
		    "<span class='td'><input name='QuickTitle' type='text' value='' style='width: 100px;' />" +
		    "<input name='doWhat' type='hidden' value='setQuickCalEntry2'/></span>" +
		    "<span class='td'>" +
		    "<input type='checkbox' name='dSu' value='ySu'/>S " +
		    "<input type='checkbox' name='dMo' value='yMo'/>M " +
		    "<input type='checkbox' name='dTu' value='yTu'/>T " +
		    "<input type='checkbox' name='dWe' value='yWe'/>W " +
		    "<input type='checkbox' name='dTh' value='yTh'/>T " +
		    "<input type='checkbox' name='dFr' value='yFr'/>F " +
		    "<input type='checkbox' name='dSa' value='ySa'/>S " +
		    "<button id='QuickCalBtn2' name='QuickCalendar2' class='UButton'>Go</button>" +
		    "</span></form></div>";
	dojo.byId(target).innerHTML = quickWebCalEntryForm;
    var quickCalButton2 = dojo.byId("QuickCalBtn2");
    dojo.connect(quickCalButton2, "click", actOnCalendarSubmit2);
}

function init() {
	getEvents();
}

dojo.ready(init);


