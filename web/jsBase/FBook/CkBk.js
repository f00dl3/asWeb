/* 
by Anthony Stump
FBook.js Created: 23 Mar 2018
FBook/CkBk.js Split: 4 Apr 2018
 */

function genOverviewChecking(cbData) {
    var bubble = "<div class='UBox'>Check<br/><span>$" + (cbData.Balance + 367.43).toFixed(2) + "</span></div>";
    dojo.byId("HoldChecking").innerHTML = bubble;
}

function putCheckbook(ckBkData) {
    var results = 0;
    var rData = "<h3>Checkbook</h3>";
    var cbSearch = "<div class='table'><form class='tr' id='CkBkSearch'>" +
            "<span class='td'><input type='text' id='SearchBoxText' name='CBSearchField' onKeyUp='searchAheadCheckbook(this.value)'/></span>" +
            "<span class='td'><strong>Search</strong></span>" +
            "</form></div><p>";
    var cbRange = "<div class='table' id='cbSearchResults'></div><p>";
    var cbResults = "<div class='table HideOnSearch'>" +
            "<form id='CheckbookForm'>";
    ckBkData.forEach(function (ckBk) {
        results++;
        var shortDesc = (ckBk.Description).replace("DBT PURCHASE ON ", "").substring(0, 32);
        var fbcr = ckBk.Credit;
        var fbdb = ckBk.Debit;
        if (!isSet(fbcr)) {
            fbcr = "";
        }
        if (!isSet(fbdb)) {
            fbdb = "";
        }
        cbResults += "<div class='tr'>";
        if (isSet(ckBk.Bank) && ckBk.Bank !== "0000-00-00") {
            cbResults += "<span class='td'> </span>" +
                    "<span class='td'>" + ckBk.Bank + "</span>" +
                    "<span class='td'>" + ckBk.Date + "</span>" +
                    "<span class='td UPop' style='width: 250px;'>" + shortDesc +
                    "<span class='UPopO'>" + ckBk.Description + "<br/><strong>CTID:</strong> " + ckBk.CTID + "</span>" +
                    "</span>" +
                    "<span class='td'>" + fbdb + "</span>" +
                    "<span class='td'>" + fbcr + "</span>" +
                    "<span class='td'>" + ckBk.Balance + "</span>" +
                    "</div>";
        } else {
            cbResults += "<span class='td'><input class='C2UCBook' type='checkbox' name='CkSetUpdate[" + ckBk.CTID + "]' value='Yes' /></span>" +
                    "<span class='td'><input type='date' name='CkBkBank[" + ckBk.CTID + "]' value='" + ckBk.Bank + "' style='width: 80px;' /></span>" +
                    "<span class='td'><input type='date' name='CkBkDate[" + ckBk.CTID + "]' value='" + ckBk.Date + "' style='width: 80px;' /></span>" +
                    "<span class='td UPop'><input type='text' name='CkBkDesc[" + ckBk.CTID + "]' value='" + ckBk.Description + "' />" +
                    "<span class='UPopO'><input type='hidden' name='CkBkID[" + ckBk.CTID + "]' value='" + ckBk.CTID + "' />" +
                    "<strong>CTID:</strong> " + ckBk.CTID + "</span></span>" +
                    "<span class='td'><input type='number' step='0.1' name='CkBkDebi[" + ckBk.CTID + "]' value='" + fbdb + "' style='width: 70px;' /></span>" +
                    "<span class='td'><input type='number' step='0.1' name='CkBkCred[" + ckBk.CTID + "]' value='" + fbcr + "' style='width: 70px;' /></span>" +
                    "<span class='td'>" + ckBk.Balance + "</span>";
        }
        cbResults += "</div>";
    });
    cbResults += "<div class='tr'>" +
            "<span class='td'><input class='C2UCBook' type='checkbox' name='CkSetAdd' value='Yes' /></span>" +
            "<span class='td'><input type='date' name='ACkBank' style='width: 80px;' /></span>" +
            "<span class='td'><input type='date' name='ACkDate' style='width: 80px;' /></span>" +
            "<span class='td'><input type='text' name='ACkDesc' /></span>" +
            "<span class='td'><input type='number' step='0.1' name='ACkDebi' style='width: 70px;' /></span>" +
            "<span class='td'><input type='number' step='0.1' name='ACkCred' style='width: 70px;' /></span>" +
            "<span class='td'>(N/A)</span>" +
            "</div>";
    rData += cbSearch + cbRange + cbResults + "Results returned: " + results +
            "<p><em>Blank space for pop-over</em>" +
            "</div>";
    dojo.byId("FBCheck").innerHTML = rData;
    // on done bind putCheckbookRange
}

function putCheckbookRange(ckBkRange) {
    // NEEDS ALOT OF WORK!
    var rData = "";
    var items, itemsTable, tDebits, tDebitsAmt, tCredits, tCreditsAmt;
    items = itemsTable = [];
    tDebits = tDebitsAmt = tCredits = tCreditsAmt = 0;
    ckBkRange.forEach(function (ckBk) {
        var difference = ckBk.Credit - ckBk.Debit;
        if (ckBk.Debit !== 0) {
            tDebits++;
            tDebitsAmt += ckBk.Debit;
        }
        if (ckBk.Credit !== 0) {
            tCredits++;
            tCreditsAmt += ckBk.Credit;
        }
        var iPushString = (ckBk.Card).toUppercase() + " " +
                ckBk.CTID + " " + ckBk.Bank + " " + ckBk.Date + " " +
                (ckBk.Description).toUppercase();
        items.push(iPushString);
        var scFBC = "";
        if (ckBk.Credit === 0) {
            scFBC = "FBCRZero";
        }
        var scFBD = "";
        if (ckBk.Credit === 0) {
            scFBD = "FBDBZero";
        }
        var shortDesc = (ckBk.Description).replace("DBT PURCHASE ON ", "").substring(0, 32);
        var cbResult = "<div class='tr'>" +
                "<span class='td'>" + ckBk.Date + "</span>" +
                "<span class='td UPop' style='width: 250px;'>" + shortDesc +
                "<span class-'UPopO'>" + ckBk.Description +
                "<br/><strong>CTID: </strong>" + ckBk.CTID + "</span>" +
                "</span>" +
                "<span class='td'>";
        if (difference >= 0) {
            cbResult += "<span style='color: green;'>";
        } else {
            cbResult += "<span style='color: red;'>";
        }
        cbResult += difference + "</span></span>" +
                "</div>";
        itemsTable.push(cbResult);
    });
    dojo.byId("cbSearchResults").innerHTML = rData;
}

function searchAheadCheckbook() {

}
