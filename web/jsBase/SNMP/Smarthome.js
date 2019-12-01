/*
by Anthony Stump
Created: 1 Dec 2019
Updated: on creation
*/

function actOnArmAway(event) {
    dojo.stopEvent(event);
    showNotice("Armed Away");
}

function actOnArmStay(event) {
    dojo.stopEvent(event);
    showNotice("Armed Stay");
}

function actOnDisarm2(event) {
    dojo.stopEvent(event);
    showNotice("Disarmed (2h)");
}

function showMiniSmartController(target) {	
	var rData = "<div class='UPop'>" +
		"<button class='UButton'>Smarthome</button>" +
		"<div class='UPopO'>" +
		"<button class='UButton' id='disarm2'>Disarm for 2h</button><br/>" +
		"<button class='UButton' id='armStay'>Arm (Stay)</button><br/>" +
		"<button class='UButton' id='armAway'>Arm (Away)</button><br/>" +
		"</div></div>";
	dojo.byId(target).innerHTML = rData;
	var buttonDisarm2 = dojo.byId('disarm2');
	var buttonArmStay = dojo.byId('armStay');
	var buttonArmAway = dojo.byId('armAway');
	dojo.connect(buttonDisarm2, "onclick", actOnDisarm2);
	dojo.connect(buttonArmStay, "onclick", actOnArmStay);
	dojo.connect(buttonArmAway, "onclick", actOnArmAway);	
}