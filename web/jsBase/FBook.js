/* 
 by Anthony Stump
 Created: 23 Mar 2018
 Updated: 28 Mar 2018
 */

function actOnAssetUpdate(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    setAssetUpdate(thisFormData);
}

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

function displayAssets() {
    $("#FBAsset").toggle();
}

function genOverviewChecking(cbData) {
    var bubble = "<div class='UBox'>Check<br/><span>$" + (cbData.Balance + 367.43).toFixed(2) + "</span></div>";
    dojo.byId("HoldChecking").innerHTML = bubble;
}

function genOverviewMortgage(mortData, amSch) {
    var asCols = ["DueDate", "Payment", "Extra", "Planned", "Interest", "Balance"];
    var masDate = getDate("day", 0, "dateOnly");
    var bubble = "<div class='UBox'>Mort<br/><span>$" + (mortData.MBal / 1000).toFixed(1) + "K</span>" +
            "<div class='UBoxO'><strong>Amortization</strong><br/>" +
            "<em>Payments after today(" + masDate + ")</em><br/>";
    var bTable = "<table><thead><tr>";
    for (var i = 0; i < asCols.length; i++) {
        bTable += "<th>" + asCols[i] + "</th>";
    }
    bTable += "</tr></thead><tbody>";
    amSch.forEach(function (tams) {
        if (tams.DueDate >= masDate && tams.Balance >= 0.01) {
            bTable += "<tr>" +
                    "<td>" + tams.DueDate + "</td>" +
                    "<td>" + tams.Payment + "</td>" +
                    "<td>" + tams.Extra + "</td>" +
                    "<td>" + tams.Planned + "</td>" +
                    "<td>" + tams.Interest + "</td>" +
                    "<td>" + tams.Balance + "</td>" +
                    "</tr>";
        }
    });
    bTable += "</tbody></table>";
    bubble += bTable + "</div></div>";
    dojo.byId("HoldMortgage").innerHTML = bubble;
}

function genOverviewSavings(svData, svBk) {
    var svBkCols = ["STID", "Date", "Description", "Debit", "Credit"];
    var bubble = "<div class='UBox'>Savings<br/><span>$" + svData.SBal + "</span>" +
            "<div class='UBoxO'><strong>Savings</strong><p>" +
            "<a href='" + doCh("p", "FinSavings", null) + "' target='pChart'>" +
            "<img class='ch_large' src='" + doCh("p", "FinSavings", "Thumb=1") + "'/></a>";
    var bForm = "<form id='SavingsBookForm'>" +
            "<button id='SvBkAddButton' type='submit' name='SvSetAdd'>Add Savings</button>" +
            "<table><thead><tr>";
    for (var i = 0; i < svBkCols.length; i++) {
        bForm += "<th>" + svBkCols[i] + "</th>";
    }
    bForm += "</tr></thead><tbody><tr>" +
            "<td>Add:</td><td><input type='date' name='ASvDate' style='width: 80px;'/></td>" +
            "<td><input type='text' name='ASvDesc' style='width: 180px;'/></td>" +
            "<td><input type='number' step='0.1' name='ASvDebi' style='width: 70px;'/></td>" +
            "<td><input type='number' step='0.1' name='ASvCred' style='width: 70px;'/></td>" +
            "</tr>";
    svBk.forEach(function (tSvBk) {
        bForm += "<tr>" +
                "<td>" + tSvBk.STID + "</td>" +
                "<td>" + tSvBk.Date + "</td>" +
                "<td>" + tSvBk.Description + "</td>" +
                "<td>" + tSvBk.Debit + "</td>" +
                "<td>" + tSvBk.Credit + "</td>" +
                "</tr>";
    });
    bubble += bForm + "</tbody></table>" +
            "</form></div></div>";
    dojo.byId("HoldSavings").innerHTML = bubble;
}

function genOverviewWorth(enw, mort, x3nw, nwga, enwt) {
    var proCols = ["3M", "1Y", "5Y", "10Y", "20Y", "30Y", "RET"];
    var wCols = ["As of Date", "ENW", "Liq", "Fix", "Life", "Credit", "Debt", "Growth"];
    var cnw1d = (enw.NetWorth / 1000).toFixed(1);
    var mlb1d = (mort.MBal / 1000).toFixed(1);
    var nwci = x3nw.Worth;
    var nwga = nwga.GrowthAvg;
    var nwgm = 1 + (nwga / 100);
    var pro03m = nwci * nwgm;
    var pro01y = nwci * Math.pow(nwgm, (4 * 1));
    var pro05y = nwci * Math.pow(nwgm, (4 * 5));
    var pro10y = nwci * Math.pow(nwgm, (4 * 10));
    var pro20y = nwci * Math.pow(nwgm, (4 * 20));
    var pro30y = nwci * Math.pow(nwgm, (4 * 30));
    var proRet = nwci * Math.pow(nwgm, (4 * 33));
    var projections = [pro03m, pro01y, pro05y, pro10y, pro20y, pro30y, proRet];
    var bubble = "<div class='UBox'>EWorth<br/><span>$" + cnw1d + "K</span>" +
            "<div class='UBoxO'><strong>Estimated Net Worth</strong><br/>" +
            "<p><em>3 month avg. growth</em>: " + nwga.GrowthAvg + "%" +
            "<p>em>Projections (starting from last period.)</em>";
    var pTable = "<table><thead><tr>";
    for (var i = 0; i < proCols.length; i++) {
        pTable += "<th>" + proCols[i] + "</th>";
    }
    pTable += "</tr></thead><tbody>";
    for (var i = 0; i < projections.length; i++) {
        pTable += "<tr><td>" + projections[i].toFixed(1) + "</td></tr>";
    }
    pTable += "</tbody></table>";
    bubble += pTable +
            "<p><a href='" + doCh("p", "FinENW", null) + "' target='pChart'>" +
            "<img class='ch_large' src='" + doCh("p", "FinENW", "Thumb=1") + "' /></a><p>";
    var wTable = "<table><thead><tr>";
    for (var i = 0; i < wCols.length; i++) {
        wTable += "<th>" + wCols[i] + "</th>";
    }
    wTable += "</thead><tbody>";
    enwt.forEach(function (tData) {
        wTable += "<tr>" +
                "<td>" + tData.AsOf + "</td>" +
                "<td>" + tData.Worth + "</td>" +
                "<td>" + tData.AsLiq + "</td>" +
                "<td>" + tData.AsFix + "</td>" +
                "<td>" + tData.Life + "</td>" +
                "<td>" + tData.Credits + "</td>" +
                "<td>" + tData.Debts + "</td>" +
                "<td>" + tData.Growth + "%</td>" +
                "</tr>";
    });
    wTable += "</tbody></table>";
    bubble += wTable + "</div></div>";
    dojo.byId("HoldWorth").innerHTML = bubble;
}

function getFinanceData() {
    aniPreload("on");
    var thePostData = "doWhat=getFinanceData";
    var xhArgs = {
        preventCache: true,
        url: getResource("Finance"),
        postData: thePostData,
        handleAs: "json",
        timeout: timeOutMilli,
        load: function (data) {
            putAssets(
                    data.qBGames[0],
                    data.bGames,
                    data.qBooks[0],
                    data.books,
                    data.qdTools[0],
                    data.dTools,
                    data.qLicenses[0],
                    data.licenses,
                    data.assets,
                    data.qMedia[0]
                    );
            naviButtonListener();
            aniPreload("off");
        },
        error: function (data, iostatus) {
            aniPreload("off");
            window.alert("xhrGet for FinanceData FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
        }
    };
    dojo.xhrPost(xhArgs);
}

function naviButtonListener() {
    var btnShowFBAsset = dojo.byId("ShowFBAsset");
    dojo.connect(btnShowFBAsset, "click", displayAssets);
}

function onlineAssetSearch(searchTerms) {
    var onlineSearchPrefix = "https://www.google.com/search?q=eBay+";
    return onlineSearchPrefix + searchTerms;
}

function putAssets(qBGames, bGames, qBooks, books, qdTools, dTools, qLicenses, licenses, assets, qMedia) {
    var abgCols = ["Title", "Quantity"];
    var dtCols = ["Description", "UD", "Quantity", "Location", "Checked"];
    var liCols = ["Title", "Type"];
    var asCols = ["Description", "CAT", "UD", "Value", "Checked", "Notes", "DT"];
    var dtCounter = 1;
    var assCounter = 0;
    var rData = "<div id='FBAssetInner'><h3>Asset Tracker</h3>";
    var abgBubble = "<div class='UBox'>BGames<br/><span>" + qBGames.TotQty + "</span><div class='UBoxO'><table><thead><tr>";
    for (var i = 0; i < abgCols.lenght; i++) {
        abgBubble += "<th>" + abgCols[i] + "</th>";
    }
    abgBubble += "</thead><tbody>";
    bGames.forEach(function (bg) {
        abgBubble += "<tr><td>" + bg.Title + "</td><td>" + bg.Quantity + "</td></tr>";
    });
    abgBubble += "</tbody></table></div></div>";
    var abkBubble = "<div class='UBox'>Books<br/><span>" + qBooks.TotQty + "</span><div class='UBoxO'><table><thead><tr>";
    for (var i = 0; i < abgCols.length; i++) {
        abkBubble += "<th>" + abgCols[i] + "</th>";
    }
    abkBubble += "</thead><tbody>";
    books.forEach(function (bk) {
        abkBubble += "<tr><td>" + bk.Title + "</td><td>" + bk.Quantity + "</td></tr>";
    });
    abkBubble += "</tbody></table></div></div>";
    var adtBubble = "<div class='UBox'>DecTools<br/><span>" + qdTools.TotQty + "</span><div class='UBoxO'>" +
            "<form id='DTUpdateForm'><input id='DTUpdateButton' type='submit' name='DTUpdate' /><p>" +
            "<table><thead><tr>";
    for (var i = 0; i < dtCols.length; i++) {
        adtBubble += "<th>" + dtCols[i] + "</td>";
    }
    adtBubble += "<tbody>";
    dTools.forEach(function (dt) {
        var dtAge = dt.Checked;
        var cDtAge = checkTransactionAge(dtAge);
        adtBubble += "<tr><input type='hidden' name='DTID[" + dtCounter + "]' value='" + dtCounter + "' />" +
                "<td><input type='hidden' name='DTDescription[" + dtCounter + "]' value='" + dt.Description + "' />" + dt.Description + "</td>" +
                "<td><input type='checkbox' name='DTSetUpdate[" + dtCounter + "]' value='Yes' /></td>" +
                "<td><input type='number' name='DTQuantity[" + dtCounter + "]' value='" + dt.Quantity + "' style='width: 30px;' /></td>" +
                "<td><input type='text' name='DTLocation[" + dtCounter + "]' value='" + dt.Location + "' style='width: 66px;' /></td>" +
                "<td class='" + cDtAge + "'>" + dtAge + "</td>" +
                "</tr>";
        dtCounter++;
    });
    adtBubble += "</tbody></table></div></div>";
    var aliBundle = "<div class='UBox'>Licenses<br/><span>" + qLicenses.TotQty + "</span>" +
            "<div class='UBoxO'><table><thead><tr>";
    for (var i = 0; i < liCols.length; i++) {
        aliBundle += "<th>" + liCols[i] + "</th>";
    }
    aliBundle += "</thead><tbody>";
    licenses.forEach(function (lic) {
        aliBundle += "<tr>" +
                "<td>" + lic.Title + "</td>" +
                "<td>" + lic.Type + "</td>" +
                "</tr>";
    });
    aliBundle += "</tbody></table></div></div>";
    var ameBundle = "<div class='UBox'>Media<br/><span>" + qMedia.TotQty + "</span></div>";
    var asTable = "<div class='table'><div class='tr'>";
    for (var i = 0; i < asCols.length; i++) {
        asTable += "<span class='td'><strong>" + asCols[i] + "</strong></span>";
    }
    asTable += "</div>";
    assets.forEach(function (ass) {
        assCounter++;
        var cAge = checkTransactionAge(ass.Checked);
        var ifDetails, ifDetailsComb;
        ifDetails = ifDetailsComb = "";
        var upRelatedCheckbox = "<input type='checkbox' class='Check2UpdateAssets' name='AssetSetUpdate' />";
        if (isSet(ass.Serial)) {
            ifDetailsComb += "<strong>Serial: </strong>" + ass.Serial + "<br/>";
        }
        if (isSet(ass.UPC)) {
            ifDetailsComb += "<strong>UPC: </strong>" + ass.UPC + "<br/>";
        }
        if (isSet(ass.Related)) {
            ifDetailsComb += "<strong>Related: </strong>" + ass.Related + "<br/>";
            upRelatedCheckbox = "N/A";
        }
        if (isSet(ass.Location)) {
            ifDetailsComb += "<strong>Location: </strong>" + ass.Location + "<br/>";
        }
        if (isSet(ifDetailsComb)) {
            ifDetails = "<div class='UPop'><img class='th_icon' src='" + getBasePath("icon") + "/ic_lst.jpeg'/>" +
                    "<div class='UPopO'>" + ifDetailsComb + "</div></div>";
        }
        var assUpForm = "<form class='tr auFormTr' id='AssetUpdateForm'>" +
                "<input type='hidden' name='AssetID' value='" + assCounter + "' />" +
                "<span class='td'>" +
                "<input type='hidden' name='AssetDescription' value='" + ass.Description + "'/>" +
                "<a href='" + onlineAssetSearch(ass.Description) + "' target='_new_AssetSearch' />" +
                ass.Description + "</a></span>" +
                "<span class='td'>" + ass.Type + " - " + ass.Category + "</span>" +
                "<span class='td'>" + upRelatedCheckbox + "</span>" +
                "<span class='td'><input type='number' name='AssetValue' value='" + ass.Value + "' style='width: 75px;' /></span>" +
                "<span class='" + cAge + "'>" + ass.Checked + "</span>" +
                "<span class='td'><input type='text' name='AssetNotes' value='" + ass.Notes + "' style='width: 140px;' /></span>" +
                "<span class='td'>" + ifDetails + "</span>" +
                "</form>";
        asTable += assUpForm;
    });
    rData += abgBubble + abkBubble + adtBubble + aliBundle + ameBundle + "<p>" +
            asTable + "</div></div><p><em>Space intentionally left blank for pop-over</em>";
    dojo.byId("FBAsset").innerHTML = rData;
    dojo.query(".Check2UpdateAssets").connect("onclick", actOnAssetUpdate);
}

function putBills(billData) {
    var rData = "<h3>Bills</h3>";
    var bCols = ["Month", "ELE", "GAS", "WAT", "SWR", "TRA", "WEB", "PHO", "Total"];
    var bCharts = "<a href='" + doCh("p", "FinBills", null) + "' target='pChart'><img class='ch_large' src='" + doCh("p", "FinBills", "Thumb=1") + "'/></a>";
    rData += bCharts + "<p>";
    var bTable = "<table>" +
            "<thead><tr>";
    for (var i = 0; i < bCols.length; i++) {
        bTable += "<th>" + bCols[i] + "</th>";
    }
    bTable += "</tr></thead><tbody>";
    billData.forEach(function (bdat) {
        bTable += "<tr>" +
                "<td>" + bdat.Month + "</td>" +
                "<td>" + bdat.ELE + "</td>" +
                "<td>" + bdat.GAS + "</td>" +
                "<td>" + bdat.WAT + "</td>" +
                "<td>" + bdat.SWR + "</td>" +
                "<td>" + bdat.TRA + "</td>" +
                "<td>" + bdat.WEB + "</td>" +
                "<td>" + bdat.PHO + "</td>" +
                "<td>" + bdat.Total + "</td>" +
                "</tr>";
    });
    bTable += "</tbody></table>";
    rData += bTable;
    dojo.byId("FBBills").innerHTML = rData;
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

function putBlueprint(blueData) {
    var scale = 2, width = 1280, height = 800;
    var rData = "<h3>House diagrams</h3>" +
            "<h4>Work in progress</h4>" +
            "<em>SVG is drawn from database. Image height/width set in JavaScript page, dimensions" +
            " subtracted from height/width maximum. Wall values in database are entered using" +
            " offset from max (or northeast corner of the house) in inches.</em>" +
            "<p><strong>WARNING: This project has been dead since June 16th, 2014!<strong>" +
            "<br/><em>Scale: " + scale + " pixels = 1 inch.</em>";
    var ulData = "<h4>Upper Level</h4>" +
            "<svg width=" + width + " height=" + height + ">";
    blueData.forEach(function (tbp) {
        var brush, seeThru;
        brush = seeThru = "";
        var x1 = (width - 100) - (scale * tbp.x1);
        var x2 = (width - 100) - (scale * tbp.x2);
        var y1 = (height - 100) - (scale * tbp.y1);
        var y2 = (height - 100) - (scale * tbp.y2);
        var tWidth = (scale * tbp.Width);
        var tType = tbp.Type;
        switch (tType) {
            case "Door":
                brush = "#993300";
                seeThru = 2;
                break;
            case "Wall":
                brush = "White";
                seeThru = 0.6;
                break;
            case "Window":
                brush = "#3399ff";
                seeThru = 1;
                break;
        }
        var tSvg = "<line " +
                " x1=" + x1 +
                " y1=" + y1 +
                " x2=" + x2 +
                " y2=" + y2 +
                " style='" +
                " stroke: " + brush + ";" +
                " stroke-opacity:" + seeThru + ";" +
                " stroke-width:" + tWidth + ";'/>";
        ulData += tSvg;
    });
    rData += ulData + "</svg>";
    dojo.byId("FBBlue").innerHTML = rData;

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

function putWorkPTO(ptoData) {
    var ptoCols = ["Month", "New", "Taken", "Save", "Balance", "Notes"];
    var rData = "<h3>Work PTO</h3>";
    var ptoTable = "<table><thead><tr>";
    for (var i = 0; i < ptoCols.length; i++) {
        ptoTable += "<th>" + ptoCols[i] + "</th>";
    }
    ptoTable += "</thead><tbody>";
    ptoData.forEach(function (pto) {
        ptoTable += "<tr>" +
                "<td>" + pto.Month + "</td>" +
                "<td>" + pto.New + "</td>" +
                "<td>" + pto.Taken + "</td>" +
                "<td>" + pto.Save + "</td>" +
                "<td>" + pto.Balance + "</td>" +
                "<td>" + pto.Notes + "</td>" +
                "</tr>";
    });
    ptoTable += "</tbody></table>";
    rData += ptoTable;
    dojo.byId("FBWorkPTO").innerHTML = rData;
}

function searchAheadCheckbook() {

}

function setAssetUpdate(formData) {
    aniPreload("on");
    formData.doWhat = "putAssetTrackUpdate";
    var xhArgs = {
        preventCache: true,
        url: getResource("Finance"),
        postData: formData,
        handleAs: "text",
        timeout: timeOutMilli,
        load: function (data) {
            showNotice(formData.AssetDescription + " updated!");
            getFinanceData();
            aniPreload("off");
        },
        error: function (data, iostatus) { 
            aniPreload("off");
            window.alert("xhrPost for AssetTrackUpdate FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
        }
    };
    dojo.xhrPost(xhArgs);
}

function initFinance() {
    getFinanceData();
}

dojo.ready(initFinance);