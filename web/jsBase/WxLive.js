/* 
by Anthony Stump
Created: 27 Mar 2018
Updated: 9 May 2018
 */

function actOnShowArchive() {
    displayArchive();
}

function actOnShowCf6() {
    displayCf6();
}

function actOnShowLocalModel() {
}

function actOnShowLocalModel() {
    displayModelData();
}

function actOnShowWxLive() {
    displayWxLive();
}

function buttonListeners() {
    var showLiveContainerButton = dojo.byId("ShWxLiveContainer");
    var showLocalModelButton = dojo.byId("ShWxLocalModel");
    var showCf6Button = dojo.byId("ShWxCf6");
    var showArchiveButton = dojo.byId("ShWxArchive");
    dojo.connect(showLiveContainerButton, "click", actOnShowWxLive);
    dojo.connect(showLocalModelButton, "click", actOnShowLocalModel);
    dojo.connect(showCf6Button, "click", actOnShowCf6);
    dojo.connect(showArchiveButton, "click", actOnShowArchive);
}

function init() {
    buttonListeners();
    actOnShowLive();
}

dojo.ready(init);