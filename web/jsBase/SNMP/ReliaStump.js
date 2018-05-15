/* 
by Anthony Stump
Created: 20 Apr 2018
Split off: 14 May 2018
Updated: 14 May 2018
*/

function actOnAlarmFilterSelect(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    var thisFormDataJ = dojo.formToJson(this.form);
    window.alert(thisFormDataJ);
    //post alarm filter, return data to div
}

function alarmSeverityButton(bgColor, textColor, text) {
    return "<button class='UButton' style='width: 60px; background-color: " + bgColor + "; color: " + textColor + ";'>" + text + "</button>"
}

function populateAlarmTable(alarmData) {
    var cols = [ "Time", "Severity", "Action", "Status", "Ticket", "Host", "Alarm Text" ];
    var rData = "<strong>Alarm List View:</strong><br/><div class='table'><div class='tr'>";
    for (var i = 0; i < cols.length; i++) { rData += "<span class='td'><strong>" + cols[i] + "</strong></span>"; }
    rData += "</div>";
    alarmData.forEach(function (alarm) {
        var asButton, alarmText, aaButton;
        switch(alarm.severity) {
            case "0": asButton = alarmSeverityButton("grey", "white", "Critical"); break;
            case "1": asButton = alarmSeverityButton("red", "white", "Major"); break;
            case "2": asButton = alarmSeverityButton("orange", "black", "Major"); break;
            case "3": asButton = alarmSeveirtyButton("yellow", "black", "Minor"); break;
            case "4": asButton = alarmSeverityButton("lightblue", "black", "FYI"); break;
            default: asButton = alarmSeverityButton("lightblue", "black", "UNK"); break;
        }
        switch(alarm.status) {
            case "NEW": aaButton = "ACK"; break;
            case "ACK": aaButton = "CLR"; break;
            default: aaButton = ""; break;
        }
        if(isSet(alarm.shortAlarmText)) { alarmText = alarm.shortAlarmText; } else { alarmText = alarm.alarmText; }
        rData += "<form class='tr'>" +
                "<input name='AlarmID' type='hidden' value='" + alarm.alarmId + "/>" +
                "<span class='td'>" + alarm.alarmTime + "</span>" +
                "<span class='td'>" + asButton + "</span>" +
                "<span class='td'>";
        if(isSet(aaButton)) {
            rData += "<input name='DoUpdate' class='UButton' type='checkbox' alue='" + aaButton + "' />";
        }
        rData += "</span>" +
                "<span class='td'><div class='UPop'>" + alarm.status +
                "<div class='UPopO'>";
        if(isSet(alarm.ackBy)) { rData += "<strong>Acknowledged</strong> by: " + alarm.ackBy + "<br/>at " + alarm.ackTime; }
        if(isSet(alarm.clrBy)) { rData += "<strong>Cleared</strong> by: " + alarm.clrBy + "<br/>at " + alrm.clrTime; }
        rData += "</div></div></span>" +
                "<span class='td'>";
        if(isSet(aaButton)) { rData += "<input type'text' name='AlarmTicket' value='" + alarm.ticket + "/>"; }
        rData += "</span>" +
                "<span class=td'><div class='UPop'>" + alarm.hostname +
                "<div class='UPopO'>";
        if(isSet(alarm.owner)) {
            rData += "<strong>IP Address</strong>: " + alarm.sourceIp + "<br/>" +
                "<strong>Owner</strong>: " + alarm.owner + "<br/>" +
                "<strong>Contact</strong>: " + alarm.contact + "<br/>" +
                "<strong>Workgroup</strong>: " + alarm.workgroup + "<br/>" +
                "<strong>Description</strong>: " + alarm.hostDescription + "<br/>";
        }
        rData += "</div></div></span>" +
                "<span class='td'><div class='UPop'>" + thisAlarmText.substring(0, 30) +
                "<div class='UPopO'>" + thisAlarmText;
        
    });
    dojo.byId("AlarmTableHolder").innerHTML = rData;
}

function populateReliaStump() {
    var rData = "<tt>IN DEVELOPMENT...</tt><p>" +
            "<form id='AlarmFilterForm'>" +
            "<select id='AlarmFilter' name='AlarmFilter'>" +
            "<option value=''>Select...</option>" +        
            "<option value='Active'>Active</option>" +
            "<option value='All'>All</option>" +
            "</select><p>" +
            "<div id='AlarmTableHolder'></div>";
    dojo.byId("ReliaStumpHolder").innerHTML = rData;
    var alarmFilterSelector = dojo.byId("AlarmFilter");
    dojo.connect(alarmFilterSelector, "change", actOnAlarmFilterSelect);
}