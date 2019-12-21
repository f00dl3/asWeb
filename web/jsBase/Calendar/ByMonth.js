/*
by Anthony Stump
Created 16 Dec 2019
Updated 21 Dec 2019
*/

var todayDate = new Date();
var defaultSelection = todayDate.getFullYear() + "-" + (todayDate.getMonth()+1);

function actOnMonthSelector(event) {
    dojo.stopEvent(event);
    var tform = dojo.formToObject(this.form); 
    var dateInNew = tform.Year + "-" + tform.Month;
    var target = dojo.byId(tform.TargetDIV);
    defaultSelection = dateInNew;
    calendarMonthCreator(dateInNew, target);
}

function calendarMonthCreator(dateIn, target) {
    var ymdIn = dateIn.split("-");
    var tYear = ymdIn[0];
    var tMonth = ymdIn[1];
    var firstDayOfMonth = new Date(dateIn + "-01");
    firstDayOfMonth.addHours(6);
    var daysInMonth = new Date(tYear, tMonth, 0).getDate();
    var startPosition = firstDayOfMonth.getDay();    
    console.log("DEBUG: var startPosition: " + startPosition);
    var iterator = 0;
    var requiredWeeks = Math.ceil((startPosition + daysInMonth)/7);
    var thisWeek = 1;
    var dayOfMonth = 0;
    var monthName = prettyMonth(firstDayOfMonth.getMonth());
    var yearFull = firstDayOfMonth.getFullYear();
    var lastDayOfMonth = new Date(firstDayOfMonth.addDays(daysInMonth));
    
    var rData = "<h4>" + monthName + " " + yearFull + "</h4>" +
    	"<div class='cal_mo_table'>";
    
    var tMonthEvents = [];
    
    console.log("DEBUG: Range = " + firstDayOfMonth + " to " + lastDayOfMonth);
    
    wcEventData.forEach(function (cev) {
    	if(
    			(cev.tEventStart).getTime() >= firstDayOfMonth.getTime() &&
    			cev.tEventStart.getTime() <= lastDayOfMonth.getTime()
		) {
    		console.log("DEBUG: Event " + cev.summary + " is in range for start of " + firstDayOfMonth + " !");
    		tMonthEvents.push(cev);
    	}
    });
    
    console.log(JSON.stringify(tMonthEvents));
    
    while(thisWeek <= requiredWeeks) {
        
        rData += "<div class='cal_mo_tr'>";
        
        for(tdow = 0; tdow <= 6; tdow++) {
            
            if(iterator === startPosition) { dayOfMonth = 1; }
            if(dayOfMonth === (daysInMonth + 1)) { dayOfMonth = 0; }
            
            rData += "<span class='cal_mo_td'><div class='UPop'>";
            if(!checkMobile()) { rData += "<span><ul type='dot'>"; }
            
            if(dayOfMonth !== 0) {

            	var todaysEvents = [];
            	var todayEventCount = 0;
            	var thisDate = new Date(firstDayOfMonth).addDays(dayOfMonth-1);
            	var thisEndConstraint = new Date(thisDate.addDays(2));
            	rData += prettyDayOfMonthPopper(getDayPosition(tdow), dayOfMonth);
            	
            	tMonthEvents.forEach(function (tev) {
            		if(
                			(tev.tEventStart).getTime() >= thisDate.getTime() &&
                			tev.tEventStart.getTime() < thisEndConstraint.getTime()
            		) {
                		if(!checkMobile()) {
                			rData += "<li>" + tev.summary + "</li>";
                		}
                		todayEventCount++;
                		todaysEvents.push(tev);
                	}
            	});
            	
            		
            	rData += popupHourly(todaysEvents, thisDate);
                
            	dayOfMonth++;
                
                
            }

        	if(!checkMobile()) { rData += "</ul></span>"; }
        	if(checkMobile() && todayEventCount !== 0) { rData += "(" + todayEventCount + ")"; }
        	
            rData += "</div></span>";
            
            iterator++;
        }
        
        rData += "</div>";
        
        thisWeek++;
    }
    
    rData += "</div>";
    
    target.innerHTML = rData;

}

function calendarMonthSelector(defaultIn, target) {
	console.log("DEBUG: defaultIn = " + defaultIn);
    var ySelector = "<select name='Year' id='YearSelector'>";
    for(var i = 2007; i < 2100; i++) { 
        if(i === defaultIn.split("-")[0]) { i = "selected"; }
        ySelector += "<option value='" + i + "'>" + i + "</option>"; 
    }
    ySelector += "</select>";
    var mSelector = "<select name='Month' id='MonthSelector'>";
    for(var j = 0; j < 12; j++) {         
        if(j === defaultIn.split("-")[1]) { j = "selected"; }
        mSelector += "<option value='" + j + "'>" + prettyMonth(j) + "</option>";
    }
    mSelector += "</select>";
    var rData = "<form id='SelectMonthYear' name='SelectMonthYear'>" +
            "<input type='hidden' name='TargetDIV' value='calds'>" +
            "<span>" + mSelector + "</span><span>" + ySelector + "</span>" +
            "<span><button class='UButton' id='YMDoIt'>Fetch</button></span>" + 
            "</form>";
    target.innerHTML = rData;
    var mySelector = dojo.byId("YMDoIt");
    dojo.connect(mySelector, "onclick", actOnMonthSelector);
}

function monthlyCalendar(target) {  
    var rData = "<div id='calms'></div><br/>" +
            "<div id='calds'></div>";
    dojo.byId(target).innerHTML = rData;
    var selectorTarget = dojo.byId("calms");
    var dataTarget = dojo.byId("calds");
    calendarMonthSelector(defaultSelection, selectorTarget);
    calendarMonthCreator(defaultSelection, dataTarget);
}
