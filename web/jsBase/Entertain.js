/* 
by Anthony Stump
Created: 25 Mar 2018
Updated: 12 Apr 2018
 */

function naviButtonListenerEt() {
    var btnShowCook = dojo.byId("ShowETCooking");
    var btnShowGames = dojo.byId("ShowETGameAll");
    dojo.connect(btnShowCook, "click", displayCooking);
    dojo.connect(btnShowGames, "click", displayGames);
}


function initEntertain() {
    naviButtonListenerEt();
}

dojo.ready(initEntertain);

