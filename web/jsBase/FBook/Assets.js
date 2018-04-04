/* 
by Anthony Stump
FBook.js Created: 23 Mar 2018
FBook/Assets.js Split: 4 Apr 2018
 */

function actOnAssetUpdate(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    setAssetUpdate(thisFormData);
}

function actOnDecorToolsUpdate(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    setDecorToolsUpdate(thisFormData);
}

function displayAssets() {
    $("#FBAsset").toggle();
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

function onlineAssetSearch(searchTerms) {
    var onlineSearchPrefix = "https://www.google.com/search?q=eBay+";
    return onlineSearchPrefix + searchTerms;
}

function putAssets(qMerged, bGames, books, dTools, licenses, assets) {
    var abgCols = ["Title", "Quantity"];
    var dtCols = ["Description", "UD", "Quantity", "Location", "Checked"];
    var liCols = ["Title", "Type"];
    var asCols = ["Description", "CAT", "UD", "Value", "Checked", "Notes", "DT"];
    var dtCounter = 1;
    var assCounter = 0;
    var rData = "<div id='FBAssetInner'><h3>Asset Tracker</h3>";
    var abgBubble = "<div class='UBox'>BGames<br/><span>" + qMerged.qBGames + "</span><div class='UBoxO'><table><thead><tr>";
    for (var i = 0; i < abgCols.lenght; i++) {
        abgBubble += "<th>" + abgCols[i] + "</th>";
    }
    abgBubble += "</thead><tbody>";
    bGames.forEach(function (bg) {
        abgBubble += "<tr><td>" + bg.Title + "</td><td>" + bg.Quantity + "</td></tr>";
    });
    abgBubble += "</tbody></table></div></div>";
    var abkBubble = "<div class='UBox'>Books<br/><span>" + qMerged.qBooks + "</span><div class='UBoxO'><table><thead><tr>";
    for (var i = 0; i < abgCols.length; i++) {
        abkBubble += "<th>" + abgCols[i] + "</th>";
    }
    abkBubble += "</thead><tbody>";
    books.forEach(function (bk) {
        abkBubble += "<tr><td>" + bk.Title + "</td><td>" + bk.Quantity + "</td></tr>";
    });
    abkBubble += "</tbody></table></div></div>";
    var adtBubble = "<div class='UBox'>DecTools<br/><span>" + qMerged.qDTools + "</span><div class='UBoxO'>" +
            "<form id='DTUpdateForm'>" +
            "<div class='table'><div class='tr'>";
    for (var i = 0; i < dtCols.length; i++) {
        adtBubble += "<span class='td'><strong>" + dtCols[i] + "</strong></span>";
    }
    adtBubble += "</div>";
    dTools.forEach(function (dt) {
        var dtAge = dt.Checked;
        var cDtAge = checkTransactionAge(dtAge);
        adtBubble += "<form class='tr dtUpdateForm'><input type='hidden' name='DTID' value='" + dtCounter + "' />" +
                "<span class='td'><input type='hidden' name='DTDescription' value='" + dt.Description + "' />" + dt.Description + "</span>" +
                "<span class='td'><input type='checkbox' class='Check2UpdateDT' name='DTSetUpdate' value='Yes' /></span>" +
                "<span class='td'><input type='number' name='DTQuantity' value='" + dt.Quantity + "' style='width: 30px;' /></span>" +
                "<span class='td'><input type='text' name='DTLocation' value='" + dt.Location + "' style='width: 66px;' /></span>" +
                "<span class='td " + cDtAge + "'>" + dtAge + "</span>" +
                "</form>";
        dtCounter++;
    });
    adtBubble += "</div></div></div>";
    var aliBundle = "<div class='UBox'>Licenses<br/><span>" + qMerged.qLicenses + "</span>" +
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
    var ameBundle = "<div class='UBox'>Media<br/><span>" + qMerged.qMedia + "</span></div>";
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
    dojo.query(".Check2UpdateAssets").connect("onchange", actOnAssetUpdate);
    dojo.query(".Check2UpdateDT").connect("onchange", actOnDecorToolsUpdate);
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

function setDecorToolsUpdate(formData) {
    aniPreload("on");
    formData.doWhat = "putDecorToolsUpdate";
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
            window.alert("xhrPost for DecorToolsUpdate FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
        }
    };
    dojo.xhrPost(xhArgs);
}

