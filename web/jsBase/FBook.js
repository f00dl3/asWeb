/* 
 by Anthony Stump
 Created: 23 Mar 2018
 Updated: 9 Apr 2018
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
    var btnShowCheck = dojo.byId("ShowFBCheck");
    var btnShowPto = dojo.byId("ShowFBWorkPTO");
    var btnShowUtils = dojo.byId("ShowFBUUse");
    dojo.connect(btnShowAsset, "click", displayAssets);
    dojo.connect(btnShowAuto, "click", displayAuto);
    dojo.connect(btnShowBills, "click", displayBills);
    dojo.connect(btnShowBlue, "click", displayBlue);
    dojo.connect(btnShowCheck, "click", displayCheckbook);
    dojo.connect(btnShowPto, "click", displayWorkPTO);
    dojo.connect(btnShowUtils, "click", displayUtils);
}

function initFinance() {
    getWebLinks("FBook.php-0", "rLinkHolder", null);
    getOverviewData();
    naviButtonListener();
}

dojo.ready(initFinance);