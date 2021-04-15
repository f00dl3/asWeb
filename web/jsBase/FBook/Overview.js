/* 
by Anthony Stump
FBook.js Created: 23 Mar 2018
FBook/Overview.js Split: 8 Apr 2018
Updated: 13 Apr 2021
 */

function actOnSavingsSubmit(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    setSavingsAdd(thisFormData);
}

function genOverviewMortgage(mortData, amSch, mdfbal, svbal) {
    var svCushion = 8000;
    var svCushionMdt = 10000;
    var outstandingAdditional = 808;
    var actualMortgageBalance = mortData[0].MBal + outstandingAdditional;
    var bubble = "";
    if(actualMortgageBalance > 0) {
        var asCols = ["DueDate", "Payment", "Extra", "Planned", "Interest", "Balance"];
        var easyPay = actualMortgageBalance - mdfbal.Value;
        var fastPay = actualMortgageBalance - mdfbal.Value - (svbal-svCushion);
        var midPay = actualMortgageBalance - mdfbal.Value - (svbal-svCushionMdt);
        var masDate = getDate("day", 0, "dateOnly");
    	bubble = "<div class='UBox'>Mort<br/><span>$" + (actualMortgageBalance / 1000).toFixed(1) + "K</span>" +
	            "<div class='UBoxO'>" +
	            "to payoff: <strong>$" + (easyPay/1000).toFixed(1) + "K</strong><br/>" +
	            "moderate: <strong>$" + (midPay/1000).toFixed(1) + "K</strong><br/>" +
	            "aggressive: <strong>$" + (fastPay/1000).toFixed(1) + "K</strong><br/>" +
	            "(assuming sv xfer of $" + (svbal-svCushion).toFixed(0) + ")<br/>" +
	            "<p><strong>Amortization</strong><br/>" +
	            "<em>Payments after today (" + masDate + ")</em><br/>";
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
    }
    dojo.byId("HoldMortgage").innerHTML = bubble;
}

function genOverviewSavings(svData, svBk, stockData) {
	let spilloverSavings = 0.0;
	stockData.forEach(function (sd) {
		if(sd.SpilloverSavings == 1) {
			spilloverSavings += (sd.LastValue * (sd.Count - sd.Unvested));
		}			
	});
	savingsWithSpillover = svData.SBal + spilloverSavings;
    var svBkCols = ["STID", "Date", "Description", "Debit", "Credit"];
    var bubble = "<div class='UBox'>Savings<br/><span>$" + autoUnits(svData.SBal) + "</span>" +
            "<div class='UBoxO'><strong>Saving</strong><br/>" +
		"Liquidity: <strong>$" + Math.round(savingsWithSpillover) + "</strong>"; /* <br/> +
		" (Stocks: <strong>$" + Math.round(spilloverSavings) + "</strong>)<p/>" +
            "<a href='" + doCh("j", "FinSavings", null) + "' target='pChart'>" +
            "<img class='ch_large' src='" + doCh("j", "FinSavings", "th") + "'/></a>" */
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
        var sCredit, sDebit;
        if(isSet(tSvBk.Credit)) { sCredit = tSvBk.Credit.toFixed(2); } else { sCredit = ""; }
        if(isSet(tSvBk.Debit)) { sDebit = tSvBk.Debit.toFixed(2); } else { sDebit = ""; }
        bForm += "<tr>" +
                "<td>" + tSvBk.STID + "</td>" +
                "<td>" + tSvBk.Date + "</td>" +
                "<td>" + tSvBk.Description + "</td>" +
                "<td>" + sDebit + "</td>" +
                "<td>" + sCredit + "</td>" +
                "</tr>";
    });
    bubble += bForm + "</tbody></table>" +
            "</form></div></div>";
    dojo.byId("HoldSavings").innerHTML = bubble;
    var svButton = dojo.byId("SvBkAddButton");
    dojo.connect(svButton, "onclick", actOnSavingsSubmit);
}

function genOverviewStock(stockData, eTrade, crypto) {
	let cryptoBalance = crypto.Balance.toFixed(0);
	let cryptoVested = crypto.Contributions;
	var etaBalance = eTrade.Balance;
	let eTradeVested = eTrade.Contributions;
	var stockWorth = 0;
	let cryptoWorth = 0;
	//var vestDiff = etaBalance - eTradeVested;
	let cryptoDiff = cryptoBalance - cryptoVested;
	//let perChgVest = ((vestDiff/eTradeVested)*100).toFixed(1);
	let perChgCrypto = ((cryptoDiff/cryptoVested)*100).toFixed(1);
	stockData.forEach(function(sd) { 
		if (sd.FIIBAN != 0 && sd.Symbol != 'TMUS') { 
			stockWorth += ((sd.FIIBAN - sd.Unvested) * parseFloat(sd.LastValue)); 
			//console.log("\nCumulation: " + sd.Symbol + " - " + sd.LastValue + " - TSW=" + stockWorth);
			} 
		});
	let vestDiff = stockWorth - eTradeVested;
	let perChgVest = ((vestDiff/eTradeVested)*100).toFixed(1);
    var sCols = ["Symbol", "Description", "Shares", "Value", "Worth" /*, "Day" */ ];
    var bubble = "<div class='UBox'>Extend<br/><span>$" + autoUnits(etaBalance+parseFloat(cryptoBalance)) + "</span>" +
            "<div class='UBoxO'>" +
            "Brokerage: <strong>$" + autoUnits(etaBalance) + "</strong> (<strong>$" + autoUnits(eTradeVested) + "</strong>)<br/>" +
            "Change*: <strong>$" + vestDiff.toFixed(0) + "</strong> (<strong>" + perChgVest + "%</strong>)<br/>" +
		"<em>*Does not include free company stock</em><p/>" +
            "Crypto: <strong>$" + autoUnits(cryptoBalance) + "</strong> (<strong>$" + autoUnits(cryptoVested) + "</strong>)<br/>" +
            "Change: <strong>$" + cryptoDiff.toFixed(0) + "</strong> (<strong>" + perChgCrypto + "%</strong>)<br/>";
    bubble += "<br/><strong>Watching</strong><br/>";
    var bTable = "<table><thead><tr>";
    for (var i = 0; i < sCols.length; i++) {
        bTable += "<th>" + sCols[i] + "</th>";
    }
    bTable += "</tr></thead><tbody>";
    stockData.forEach(function (sd) {
    	if(sd.Managed != 1) {
    		let change = (parseFloat(sd.LastValue) - parseFloat(sd.PreviousClose)).toFixed(2);
    		let txtColor = "yellow";
		if(sd.SpilloverSavings === 1) { txtColor = "orange"; }
	    	if(sd.Count === 0) { txtColor = "white"; }
	        bTable += "<tr>" +
	                    "<td style='color: " + txtColor + "'><a href='" + mktUrl + sd.Symbol + "' target='stonew'>" + sd.Symbol + "</a></td>" +
	                    "<td style='color: " + txtColor + "'>" + sd.Description + "</td>" +
	                    "<td style='color: " + txtColor + "'>" + sd.Count + "</td>" +
	                    "<td style='color: " + txtColor + "'>" + parseFloat(sd.LastValue).toFixed(2) + "</td>" +
	                    "<td style='color: " + txtColor + "'>" + autoUnits(parseFloat(sd.LastValue * sd.Count).toFixed(2)) + "</td>" +
	                    //"<td style='color: " + txtColor + "'>" + change + "</td>" +
	                    //"<td><a href='" + doCh("j", "FinStock_"+sd.Symbol, null) + "' target='pChart'><img class='th_icon' src='" + doCh("j", "FinStock_"+sd.Symbol, "th") + "' /></a>" +
	                    "</tr>";
	        }
    	}
    );
    bTable += "</tbody></table>";
    bubble += bTable + "</div></div>";
    dojo.byId("HoldStock").innerHTML = bubble;
}

function genOverviewWorth(enw, mort, x3nw, nwga, enwt, mdfbal) {
    var proCols = ["3M", "1Y", "5Y", "10Y", "20Y", "30Y", "RET"];
    var wCols = ["As of Date", "ENW", "Liq", "Fix", "Life", "Credit", "Debt", "Growth"];
    var cnw1d = (enw.NetWorth / 1000).toFixed(1);
    var cnw1d_L = (enw.NetWorth).toFixed(2);
    var mlb1d = (mort.MBal / 1000).toFixed(1);
    var nwci = x3nw.Worth;
    var nwgm = (nwga.GrowthAvg / 100);
    var pro03m = nwci * (1 + nwgm);
    var pro01y = fnwGrow(nwci, nwgm, 1);
    var pro05y = fnwGrow(nwci, nwgm, 5);
    var pro10y = fnwGrow(nwci, nwgm, 10);
    var pro20y = fnwGrow(nwci, nwgm, 20);
    var pro30y = fnwGrow(nwci, nwgm, 30);
    var proRet = fnwGrow(nwci, nwgm, 32);
    var projections = [pro03m, pro01y, pro05y, pro10y, pro20y, pro30y, proRet];
    var bubble = "<div class='UBox'>Worth<br/><span>$" + autoUnits(enw.NetWorth) + "</span>" +
            "<div class='UBoxO'><strong>Estimated Net Worth</strong><br/>" +
            "Current estimate: <strong>$" + cnw1d_L + "</strong>" +
            "<br/><em>Now WMQY growth</em>: " +
            	"<div class='UPop'>" + nwga.p7day.toFixed(1) + "<div class='UPopO'>Change: $" + nwga.g7day.toFixed(1) + "K</div></div>/" + 
            	"<div class='UPop'>" + nwga.p30day.toFixed(1) + "<div class='UPopO'>Change: $" + nwga.g30day.toFixed(1) + "K</div></div>/" + 
            	"<div class='UPop'>" + nwga.p90day.toFixed(1) + "<div class='UPopO'>Change: $" + nwga.g90day.toFixed(1) + "K</div></div>/" + 
            	"<div class='UPop'>" + nwga.p1year.toFixed(1) + "<div class='UPopO'>Change: $" + nwga.g1year.toFixed(1) + "K</div></div>%" +
            "<br/><em>3 month avg. growth</em>: " + nwga.GrowthAvg + "%"
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
    bubble += pTable + "<p>" +
		"<div id='tmphld'></div>" +
		"<div class='table'><div class='tr'>" +
            "<span class='td'><a href='" + doCh("3", "FinENW_All_A", null) + "' target='pChart'><div class='th_sm_med' style='height: 92px;'><canvas id='enwAllHolder'></canvas></div></a></span>" +
            "<span class='td'><a href='" + doCh("3", "LiquidDist", null) + "' target='pChart'><div class='th_sm_med' style='height: 92px;'><canvas id='wdist'></canvas></div></a></span>" +
		"</div><div class='tr'>" +
	    "<span class='td'><a href='" + doCh("3", "FinENW_Year_T", null) + "' target='pChart'><div class='th_sm_med' style='height: 92px;'><canvas id='fwY'></canvas></div></a></span>" +
            "<span class='td'><a href='" + doCh("3", "FinENW_All_R", null) + "' target='pChart'><div class='th_sm_med' style='height: 92px;'><canvas id='fwR'></canvas></div></a></span>" +
		"</div></div>" + 
                       "<p>";
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
	ch_get_FinENW_All_A("enwAllHolder", "thumb");
	ch_get_FinENW_Year_A("fwY", "thumb");
	ch_get_FinENW_All_R("fwR", "thumb");
	ch_get_LiquidDist("wdist", "thumb");
}

function getOverviewData() {
    var timeout = getRefresh("medium");
    aniPreload("on");
    var thePostData = { "doWhat": "FinanceOverviewCharts" };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Chart"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    getOverviewData2();
                    aniPreload("off");
                },
                function(error) { 
                    aniPreload("off");
                    console.log("request for FOverCharts FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
    setTimeout(function () { getOverviewData(); }, timeout);
}

function getOverviewData2() {
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
            showNotice("xhrGet for Overview FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
        }
    };
    dojo.xhrPost(xhArgs);
}

function fnwGrow(nwci, nwgm, quarters) {
	return nwci * Math.pow((1 + nwgm), (4 * quarters));
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
    var mdfbal = finGlob.mdfbal[0];
    var svbal = svData.SBal;
    var eTrade = finGlob.eTrade[0];
    var crypto = finGlob.crypto[0];
    var stockData = finGlob.stock;
    genOverviewChecking(cbData);
    //genOverviewSavings(svData, svBk, stockData);
    genOverviewStock(stockData, eTrade, crypto);
    //genOverviewMortgage(mortData, amSch, mdfbal, svbal);
    genOverviewWorth(enw, mortData, x3nw, nwga, enwt, mdfbal);
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
            showNotice("xhrPost for SavingsAdd FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
            aniPreload("off");
        }
    };
    dojo.xhrPost(xhArgs);
}
