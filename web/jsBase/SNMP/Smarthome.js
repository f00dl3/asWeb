/*
by Anthony Stump
Created: 1 Dec 2019
Updated: 15 Dec 2019
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

function actOnSmartplug(event) {
    dojo.stopEvent(event);
	var thisFormData = dojo.formToObject(this.form);
	setSmartplug(thisFormData);
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
                    populateSmartStuff(target, data);
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

function setSmartplug(formData) {
    aniPreload("on");
    var thePostData = {
        "doWhat": "doSmartplug",
        "target": formData.Device,
        "command": "cycle"
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Smarthome"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    aniPreload("off");
                    showNotice("Sent " + thePostData.command + " to " + formData.Device);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request to set Smartplug FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function populateSmartStuff(target, eventData) {
	var eventLog = JSON.parse(eventData);
	var counter = 0;
	var doorData = "<div class='UPop'>" +
		"<button class='UButton'>Alarm</button>" +
		"<div class='UPopO'>" +
		"<form><input type='hidden' name='ArmType' value='Disarm2H'><button class='UButton' id='disarm2'>Disarm for 2h</button></form>" +
		"<form><input type='hidden' name='ArmType' value='ArmStay'><button class='UButton' id='armStay'>Arm Stay</button></form>" +
		"<form><input type='hidden' name='ArmType' value='ArmAway'><button class='UButton' id='armAway'>Arm Away</button></form>" +
		"<strong>Last 5:</strong><br/>" +
		"<div class='table'>";
	eventLog.forEach(function(el) {
		if(counter < 5) {
			doorData += "<div class='tr'>" +
				"<span class='td'>" + el.EventID + "</span>" +
				"<span class='td'>" + el.ReceivedTimestamp + "</span>" + 
				"<span class='td'>" + el.DoorLocation + "</span>" + 
				"</div>";
			counter++;
		}
	});
	doorData += "</div>" + 
		"</div></div>";
	var smartData = "<div class='UPop'>" +
		"<button class='UButton'>Plugs</button>" +
		"<div class='UPopO'>" +
		"<form><input type='hidden' name='Device' value='DCamera'><button class='UButton' id='spDCamera'>SPARE</button></form>" +
		"<form><input type='hidden' name='Device' value='Router'><button class='UButton' id='spRouter'>Router</button></form>" +
		"<form><input type='hidden' name='Device' value='Desktop'><button class='UButton' id='spDesktop'>Desktop</button></form>" +
		"</div>" + 
		"</div>";
	var rData = doorData + smartData;
	dojo.byId(target).innerHTML = rData;
	var buttonDisarm2 = dojo.byId('disarm2');
	var buttonArmStay = dojo.byId('armStay');
	var buttonArmAway = dojo.byId('armAway');
	var buttonDCamera = dojo.byId('spDCamera');
	var buttonSpDesktop = dojo.byId('spDesktop');
	var buttonSpRouter = dojo.byId('spRouter');
	dojo.connect(buttonDisarm2, "onclick", actOnDisarm2);
	dojo.connect(buttonArmStay, "onclick", actOnArmStay);
	dojo.connect(buttonArmAway, "onclick", actOnArmAway);
	dojo.connect(buttonDCamera, "onclick", actOnSmartplug);	
	dojo.connect(buttonSpDesktop, "onclick", actOnSmartplug);
	dojo.connect(buttonSpRouter, "onclick", actOnSmartplug);		
}

function showMiniSmartController(target) {	
	getDoorEvents(target);
}

