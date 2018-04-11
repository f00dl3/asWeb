/* 
by Anthony Stump
FBook.js Created: 23 Mar 2018
FBook/Overview.js Split: 8 Apr 2018
Updated: 11 Apr 2018
 */

function actOnSavingsSubmit(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    setSavingsAdd(thisFormData);
}

function genOverviewMortgage(mortData, amSch) {
    var asCols = ["DueDate", "Payment", "Extra", "Planned", "Interest", "Balance"];
    var masDate = getDate("day", 0, "dateOnly");
    var bubble = "<div class='UBox'>Mort<br/><span>$" + (mortData[0].MBal / 1000).toFixed(1) + "K</span>" +
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
    var bubble = "<div class='UBox'>Savings<br/><span>$" + Math.round(svData.SBal) + "</span>" +
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
            "<td>Add:</td><td><input type='text' name='ASvDate' style='width: 80px;'/></td>" +
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
    var svButton = dojo.byId("SvBkAddButton");
    dojo.connect(svButton, "onclick", actOnSavingsSubmit);
}

function genOverviewWorth(enw, mort, x3nw, nwga, enwt) {
    var proCols = ["3M", "1Y", "5Y", "10Y", "20Y", "30Y", "RET"];
    var wCols = ["As of Date", "ENW", "Liq", "Fix", "Life", "Credit", "Debt", "Growth"];
    var cnw1d = (enw.NetWorth / 1000).toFixed(1);
    var mlb1d = (mort.MBal / 1000).toFixed(1);
    var nwci = x3nw.Worth;
    var nwgm = 1 + (nwga.GrowthAvg / 100);
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
            "<p><em>Projections (starting from last period.)</em>";
    var pTable = "<table><thead><tr>";
    for (var i = 0; i < proCols.length; i++) {
        pTable += "<th>" + proCols[i] + "</th>";
    }
    pTable += "</tr></thead><tbody><tr>";
    for (var i = 0; i < projections.length; i++) {
        pTable += "<td>" + projections[i].toFixed(1) + "</td>";
    }
    pTable += "</tr></tbody></table>";
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

function getOverviewData() {
    aniPreload("on");
    var thePostData = "doWhat=getOverview";
    var xhArgs = {
        preventCache: true,
        url: getResource("Finance"),
        postData: thePostData,
        handleAs: "json",
        timeout: timeOutMilli,
        load: function (data) {
            putOverview(data);
            aniPreload("off");
        },
        error: function (data, iostatus) {
            aniPreload("off");
            window.alert("xhrGet for Overview FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
        }
    };
    dojo.xhrPost(xhArgs);
}

function putOverview(finGlob) {
    var amSch = finGlob.amSch;
    var cbData = finGlob.checking[0];
    var svData = finGlob.saving[0];
    var svBk = finGlob.svBk;
    var mortData = finGlob.mort;
    var enw = finGlob.enw[0];
    var x3nw = finGlob.x3nw[0];
    var nwga = finGlob.nwga[0];
    var enwt = finGlob.enwt;
    genOverviewChecking(cbData)
    genOverviewSavings(svData, svBk);
    genOverviewMortgage(mortData, amSch);
    genOverviewWorth(enw, mortData, x3nw, nwga, enwt);
}

function setSavingsAdd(formData) {
    aniPreload("on");
    formData.doWhat = "putSavingsAdd";
    var xhArgs = {
        preventCache: true,
        url: getResource("Finance"),
        postData: formData,
        handleAs: "text",
        timeout: timeOutMilli,
        load: function(data) {
            showNotice("Savings ledger added!");
            getOverviewData();
            aniPreload("off");
        },
        error: function(data, iostatus) {
            window.alert("xhrPost for SavingsAdd FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
            aniPreload("off");
        }
    };
    dojo.xhrPost(xhArgs);
}