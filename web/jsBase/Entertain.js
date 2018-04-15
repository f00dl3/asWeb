/* 
by Anthony Stump
Created: 25 Mar 2018
Updated: 15 Apr 2018
 */

function naviButtonListenerEt() {
    var btnShowCook = dojo.byId("ShowETCooking");
    var btnShowGames = dojo.byId("ShowETGameAll");
    var btnShowLego = dojo.byId("ShowETLego");
    var btnShowMediaServer = dojo.byId("ShowETStream");
    var btnShowReddit = dojo.byId("HShowETReddit");
    dojo.connect(btnShowCook, "click", displayCooking);
    dojo.connect(btnShowGames, "click", displayGames);
    dojo.connect(btnShowLego, "click", displayLego)
    dojo.connect(btnShowMediaServer, "click", displayMediaServer);
    dojo.connect(btnShowReddit, "click", displayReddit);
}


function initEntertain() {
    if(isSet(hiddenFeatures)) {
        $("#HShowETReddit").toggle();
    }
    naviButtonListenerEt();
}

dojo.ready(initEntertain);

