/* 
 by Anthony Stump
 Created: 23 Mar 2018
 Updated: 4 Apr 2018
 */

function checkTransactionAge(dtAge) {
    var cDtAge = "";
    switch (true) {
        case (dtAge >= getDate("day", - 14, "dateOnly")): cDtAge = 'FBCAN'; break;
        case (dtAge >= getDate("month", - 1, "dateOnly")): cDtAge = 'FBCA1'; break;
        case (dtAge >= getDate("month", - 6, "dateOnly")): cDtAge = 'FBCA3'; break;
        default: cDtAge = 'FBCA6'; break;
    }
    return cDtAge;
}

function naviButtonListener() {
    var btnShowAsset = dojo.byId("ShowFBAsset");
    var btnShowAuto = dojo.byId("ShowFBAuto");
    var btnShowBills = dojo.byId("ShowFBBills");
    var btnShowBlue = dojo.byId("ShowFBBlue");
    var btnShowPto = dojo.byId("ShowFBWorkPTO");
    var btnShowUtils = dojo.byId("ShowFBUUse");
    dojo.connect(btnShowAsset, "click", displayAssets);
    dojo.connect(btnShowAuto, "click", displayAuto);
    dojo.connect(btnShowBills, "click", displayBills);
    dojo.connect(btnShowBlue, "click", displayBlue);
    dojo.connect(btnShowPto, "click", displayWorkPTO);
    dojo.connect(btnShowUtils, "click", displayUtils);
}

function putOverview(finGlob) { // Get from db and maybe call in get not separate function
    var amSch = finGlob.amSch;
    var cbData = finGlob.checking[0];
    var svData = finGlob.saving[0];
    var svBk = finGlob.svBk;
    var mortData = finGlob.mort;
    var enw = finGlob.enw;
    var x3nw = finGlob.x3nw;
    var nwga = finGlob.nwga;
    var enwt = finGlob.enwt;
    genOverviewChecking(cbData)
    genOverviewSavings(svData, svBk);
    genOverviewMortgage(mortData, amSch);
    genOverviewWorth(enw, mortData, x3nw, nwga, enwt);
}

function initFinance() {
    naviButtonListener();
}

dojo.ready(initFinance);