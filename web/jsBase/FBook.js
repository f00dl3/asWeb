/* 
by Anthony Stump
Created: 23 Mar 2018
 */

function genOverviewChecking(cbData) {
    var bubble = "<div class='UBox'>Check<br/><span>$" + (cbData.Balance+367.43).toFixed(2) + "</span></div>";
    dojo.byId("HoldChecking").innerHTML = bubble;
}

function genOverviewMortgage(mortData, amSch) {
    var asCols = [ "DueDate", "Payment", "Extra", "Planned", "Interest", "Balance" ];
    var masDate = getDate("day", 0, "dateOnly");
    var bubble = "<div class='UBox'>Mort<br/><span>$" + (mortData.MBal/1000).toFixed(1) + "K</span>" +
            "<div class='UBoxO'><strong>Amortization</strong><br/>" +
            "<em>Payments after today(" + masDate + ")</em><br/>";
    var bTable = "<table><thead><tr>";
    for(var i = 0; i < asCols.length; i++) { bTable += "<th>" + asCols[i] + "</th>"; }
    bTable += "</tr></thead><tbody>";
    amSch.forEach(function(tams) {
        if(tams.DueDate >= masDate && tams.Balance >= 0.01) {
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
    var svBkCols = [ "STID", "Date", "Description", "Debit", "Credit" ];
    var bubble = "<div class='UBox'>Savings<br/><span>$" + svData.SBal + "</span>" +
            "<div class='UBoxO'><strong>Savings</strong><p>" +
            "<a href='" + doCh("p", "FinSavings", null) + "' target='pChart'>" +
            "<img class='ch_large' src='" + doCh("p", "FinSavings", "Thumb=1") + "'/></a>";
    var bForm = "<form id='SavingsBookForm'>" +
            "<button id='SvBkAddButton' type='submit' name='SvSetAdd'>Add Savings</button>" +
            "<table><thead><tr>";
    for(var i = 0; i < svBkCols.length; i++) { bForm += "<th>" + svBkCols[i] + "</th>"; }
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
    var proCols = [ "3M", "1Y", "5Y", "10Y", "20Y", "30Y", "RET" ];
    var wCols = [ "As of Date", "ENW", "Liq", "Fix", "Life", "Credit", "Debt", "Growth" ];
    var cnw1d = (enw.NetWorth/1000).toFixed(1);
    var mlb1d = (mort.MBal/1000).toFixed(1);
    var nwci = x3nw.Worth;
    var nwga = nwga.GrowthAvg;
    var nwgm = 1+(nwga/100);
    var pro03m = nwci * nwgm;
    var pro01y = nwci * Math.pow(nwgm,(4*1));
    var pro05y = nwci * Math.pow(nwgm,(4*5));
    var pro10y = nwci * Math.pow(nwgm,(4*10));
    var pro20y = nwci * Math.pow(nwgm,(4*20));
    var pro30y = nwci * Math.pow(nwgm,(4*30));
    var proRet = nwci * Math.pow(nwgm,(4*33));
    var projections = [ pro03m, pro01y, pro05y, pro10y, pro20y, pro30y, proRet ];
    var bubble = "<div class='UBox'>EWorth<br/><span>$" + cnw1d + "K</span>" +
            "<div class='UBoxO'><strong>Estimated Net Worth</strong><br/>" +
            "<p><em>3 month avg. growth</em>: " + nwga.GrowthAvg + "%" +
            "<p>em>Projections (starting from last period.)</em>";
    var pTable = "<table><thead><tr>";
    for(var i = 0; i < proCols.length; i++) { pTable += "<th>" + proCols[i] + "</th>"; }
    pTable += "</tr></thead><tbody>";
    for(var i = 0; i < projections.length; i++) { pTable += "<tr><td>" + projections[i].toFixed(1) + "</td></tr>"; }
    pTable += "</tbody></table>";
    bubble += pTable +
            "<p><a href='" + doCh("p", "FinENW", null) + "' target='pChart'>" +
            "<img class='ch_large' src='" + doCh("p", "FinENW", "Thumb=1") + "' /></a><p>";
    var wTable = "<table><thead><tr>";
    for(var i = 0; i < wCols.length; i++) { wTable += "<th>" + wCols[i] + "</th>"; }
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

function putBills(billData) {
    var rData = "<h3>Bills</h3>";
    var bCols = [ "Month", "ELE", "GAS", "WAT", "SWR", "TRA", "WEB", "PHO", "Total" ];
    var bCharts = "<a href='" + doCh("p", "FinBills", null) + "' target='pChart'><img class='ch_large' src='" + doCh("p", "FinBills", "Thumb=1") + "'/></a>";
    rData += bCharts + "<p>";
    var bTable = "<table>" +
            "<thead><tr>";
    for(var i = 0; i < bCols.length; i++) { bTable += "<th>" + bCols[i] + "</th>"; }
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
        if(!isSet(fbcr)) { fbcr = ""; }
        if(!isSet(fbdb)) { fbdb = ""; }
        cbResults += "<div class='tr'>";
        if(isSet(ckBk.Bank) && ckBk.Bank !== "0000-00-00") {
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
        if(ckBk.Debit !== 0) { tDebits++; tDebitsAmt += ckBk.Debit; }
        if(ckBk.Credit !== 0) { tCredits++; tCreditsAmt += ckBk.Credit; }
        var iPushString = (ckBk.Card).toUppercase() + " " +
                ckBk.CTID + " " + ckBk.Bank + " " + ckBk.Date + " " +
                (ckBk.Description).toUppercase();
        items.push(iPushString);
        var scFBC = ""; if(ckBk.Credit === 0) { scFBC = "FBCRZero"; }
        var scFBD = ""; if(ckBk.Credit === 0) { scFBD = "FBDBZero"; }
        var shortDesc = (ckBk.Description).replace("DBT PURCHASE ON ", "").substring(0, 32);
        var cbResult = "<div class='tr'>" +
                "<span class='td'>" + ckBk.Date + "</span>" +
                "<span class='td UPop' style='width: 250px;'>" + shortDesc +
                "<span class-'UPopO'>" + ckBk.Description +
                "<br/><strong>CTID: </strong>" + ckBk.CTID + "</span>" +
                "</span>" +
                "<span class='td'>";
        if(difference >= 0) { cbResult += "<span style='color: green;'>"; } else { cbResult += "<span style='color: red;'>"; }
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
        var x1 = (width-100)-(scale*tbp.x1);
        var x2 = (width-100)-(scale*tbp.x2);
        var y1 = (height-100)-(scale*tbp.y1);
        var y2 = (height-100)-(scale*tbp.y2);
        var tWidth = (scale*tbp.Width);
        var tType = tbp.Type;
        switch(tType) {
            case "Door": brush = "#993300"; seeThru = 2; break;
            case "Wall": brush = "White"; seeThru = 0.6; break;
            case "Window": brush = "#3399ff"; seeThru = 1; break;
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
    var ptoCols = [ "Month", "New", "Taken", "Save", "Balance", "Notes" ];
    var rData = "<h3>Work PTO</h3>";
    var ptoTable = "<table><thead><tr>";
    for(var i = 0; i < ptoCols.length; i++) { ptoTable += "<th>" + ptoCols[i] + "</th>"; }
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

function init(searchString) {
    
}

dojo.ready(init);