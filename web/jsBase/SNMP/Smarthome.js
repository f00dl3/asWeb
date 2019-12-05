/*
by Anthony Stump
Created: 1 Dec 2019
Updated: 5 Dec 2019
*/

function actOnArmAway(event) {
    dojo.stopEvent(event);
	var thisFormData = dojo.formToObject(this.form);
	setArmDisarm(thisFormData);
}

function actOnArmStay(event) {
    dojo.stopEvent(event);
	var thisFormData = dojo.formToObject(this.form);
	setArmDisarm(thisFormData);
}

function actOnDisarm2(event) {
    dojo.stopEvent(event);
	var thisFormData = dojo.formToObject(this.form);
	setArmDisarm(thisFormData);
}



function getDoorEvents(target) {
    aniPreload("on");
    var thePostData = {
        "doWhat": "getDoorEvents"
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Smarthome"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    aniPreload("off");
                    populateDoorEvents(target, data);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request to get Door Events FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function setArmDisarm(formData) {
    aniPreload("on");
    var thePostData = {
        "doWhat": "setArmDisarm",
        "ArmType": formData.ArmType
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Smarthome"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    aniPreload("off");
                    showNotice("System set " + formData.ArmType);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request to set ArmDisarm FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function populateDoorEvents(target, eventData) {
	var eventLog = JSON.parse(eventData);
	var counter = 0;
	var rData = "<div class='UPop'>" +
		"<button class='UButton'>Alarm</button>" +
		"<div class='UPopO'>" +
		"<form><input type='hidden' name='ArmType' value='Disarm2H'><button class='UButton' id='disarm2'>Disarm for 2h</button></form>" +
		"<form><input type='hidden' name='ArmType' value='ArmStay'><button class='UButton' id='armStay'>Arm Stay</button></form>" +
		"<form><input type='hidden' name='ArmType' value='ArmAway'><button class='UButton' id='armAway'>Arm Away</button></form>" +
		"<strong>Last 5:</strong><br/>" +
		"<div class='table'>";
	eventLog.forEach(function(el) {
		if(counter < 5) {
			rData += "<div class='tr'>" +
				"<span class='td'>" + el.EventID + "</span>" +
				"<span class='td'>" + el.ReceivedTimestamp + "</span>" + 
				"<span class='td'>" + el.DoorLocation + "</span>" + 
				"</div>";
			counter++;
		}
	});
	rData += "</div>" + 
		"</div></div>";
	dojo.byId(target).innerHTML = rData;
	var buttonDisarm2 = dojo.byId('disarm2');
	var buttonArmStay = dojo.byId('armStay');
	var buttonArmAway = dojo.byId('armAway');
	dojo.connect(buttonDisarm2, "onclick", actOnDisarm2);
	dojo.connect(buttonArmStay, "onclick", actOnArmStay);
	dojo.connect(buttonArmAway, "onclick", actOnArmAway);		
}

function showMiniSmartController(target) {	
	getDoorEvents(target);
}

