/* 
by Anthony Stump
Created: 16 Jul 2020
Updated: 18 Feb 2021
 */

function actOnCryptoFormSubmit(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    var thisFormDataJ = dojo.formToJson(this.form);
    setCryptoAdd(thisFormData);
}

function actOnETBAFormSubmit(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    var thisFormDataJ = dojo.formToJson(this.form);
    setETBAAdd(thisFormData);
}

function actOnStockFormSubmit(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    var thisFormDataJ = dojo.formToJson(this.form);
    if(isSet(thisFormData.Action)) {
        switch(thisFormData.Action) {
            case "Add": setStockAdd(thisFormData); break;
            case "Update": setStockUpdate(thisFormData); break;
            default: window.alert("No action set!");
        }
    }
}

function displayStocks() {
    getStocks();
    $("#FBStocks").toggle();
    $("#FBCheck").hide();
    $("#FBAuto").hide();
    $("#FBAutoHC").hide();
    $("#FBAuto20").hide();
    $("#FBBills").hide();
    $("#FBBlue").hide();
    $("#FBAsset").hide();
    $("#FBWorkPTO").hide();
    $("#FBUUse").hide();
}

function getStocks() {
	console.log("DEBUG: Attempting Stock Pull...");
    dojo.byId("FBStocks").innerHTML = "LOADING DATA...";
    aniPreload("on");
    var thePostData = { "doWhat": "getStocksAll" };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Stock"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    aniPreload("off");
                    let psObj = data;
                	putStocks(psObj.etba, psObj.stocksA, psObj.crypto);
                    console.log("DEBUG: Stock pull success.");
                },
                function(error) { 
                    aniPreload("off");
                    showNotice("STOCK PULL FAILURE\n " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function putStocks(etbaData, stockData, crypto) {
	let etbaInsert = "<h3>Stock Transactions</h3>" +
		"<div class='table'>" +
		"<form class='tr etbaAddUpdateForm'>" +
		"<span class='td'><input class='C2UETBA' type='checkbox' name='Action' value='Update' /></span>" + 
		"<span class='td'><input type='date' name='etbaDate' id='etbaDate' style='width: 80px;' /></span>" +
        "<span class='td'><input type='number' step='1' name='etbaDebit' value='0' style='width: 70px;' /></span>" +
        "<span class='td'><input type='number' step='1' name='etbaCredit' value='0' style='width: 70px;' /></span>" +
        "</form>";
	etbaData.forEach(function (etb) {
		etbaInsert += "<div class='tr'>" +
			"<span class='td'>" + etb.BTID + "</span>" +
			"<span class='td'>" + etb.Date + "</span>" + 
			"<span class='td'>" + etb.Debit + "</span>" +
			"<span class='td'>" + etb.Credit + "</span>" +
			"</div>";
	});
	etbaInsert += "</div>";
	let crytpoInsert = "<h3>Crypto Transactions</h3>" +
		"<div class='table'>" +
		"<form class='tr cryptoAddUpdateForm'>" +
		"<span class='td'><input class='C2UCR' type='checkbox' name='Action' value='Update' /></span>" + 
		"<span class='td'><input type='date' name='crDate' id='crDate' style='width: 80px;' /></span>" +
        "<span class='td'><input type='number' step='1' name='crDebit' value='0' style='width: 70px;' /></span>" +
        "<span class='td'><input type='number' step='1' name='crCredit' value='0' style='width: 70px;' /></span>" +
        "</form>";
	crypto.forEach(function (crd) {
		crytpoInsert += "<div class='tr'>" +
			"<span class='td'>" + crd.BTID + "</span>" +
			"<span class='td'>" + crd.Date + "</span>" + 
			"<span class='td'>" + crd.Debit + "</span>" +
			"<span class='td'>" + crd.Credit + "</span>" +
			"</div>";
	});
	crytpoInsert += "</div>";
    let rData = "<h3>Stocks & Manged Funds</h3>";
    let managedBalance = 0.0;
    let managedBalanceE = 0.0;
    let myBalance = 0.0;
	let spilloverSavings = 0.0;
    stockData.forEach(function (sd) {
    	if(sd.Managed == 1) {
			if(sd.Holder == 'EJones' || sd.Holder == 'FidelityE') {
				managedBalanceE += (sd.Count * (sd.LastValue * sd.Multiplier));
			} else {
				managedBalance += (sd.Count * (sd.LastValue * sd.Multiplier));
			}
    	} else {
    		myBalance += (sd.Count * (sd.LastValue * sd.Multiplier));
    	}
	if(sd.SpilloverSavings == 1) {
		spilloverSavings += (sd.Count * sd.LastValue);
	}
    });
    rData += "<div class='table'><div class='tr'>" +
		"<span class='td'><strong>Short-term</strong><br/>" +
		"<a href='" + doCh("3", "BrokerageDist", null) + "' target='pChart'><div class='th_sm_med' style='height: 92px;'><canvas id='brkdist'></canvas></div></a><br/>" +
    	"$" + autoUnits(myBalance) +
		"</span>" +
		"<span class='td'><strong>Ret. A</strong><br/>" + 
		"<a href='" + doCh("3", "RetirementDist", null) + "' target='pChart'><div class='th_sm_med' style='height: 92px;'><canvas id='retdist'></canvas></div></a><br/>" +
		"$" + autoUnits(managedBalance) +
		"</span>" +
		"<span class='td'><strong>Ret. E</strong><br/>" + 
		"<a href='" + doCh("3", "RetirementDistE", null) + "' target='pChart'><div class='th_sm_med' style='height: 92px;'><canvas id='retdistE'></canvas></div></a><br/>" +
		"$" + autoUnits(managedBalanceE) +
		"</span>" +
		"</div></div><br/></p>";
	//"<strong>Spillover Savings</strong>: $" + spilloverSavings.toFixed(2) + "</p>";
    let stockResults = "<div class='table'>";
    stockData.forEach(function (sd) {
		let holdingValue = (sd.Count * (sd.LastValue * sd.Multiplier));
		holdingValue = holdingValue.toFixed(2);
        stockResults += "<form class='tr stockAddUpdateForm'>"+
        	"<span class='td'><input class='C2UStock' type='checkbox' name='Action' value='Update' /></span>" +
            "<span class='td'><input type='hidden' name='Symbol' value='" + sd.Symbol + "'/><a href='" + mktUrl + sd.Symbol + "' target='newStock'>" + sd.Symbol + "</a></span>" +
            "<span class='td'><div class='UPop'>" +
            "<input type='number' step='0.001' name='Count' value='" + sd.Count + "' style='width: 80px;' />" +
            "<div class='UPopO'>";
        if(isSet(sd.LastComparedShares)) { stockResults += "<strong>compared:</strong> " + sd.LastComparedShares + "<br/>"; }
        if(isSet(sd.LastUpdated)) { stockResults += "<strong>as of:</strong> " + sd.LastUpdated + "<br/>"; }
        if(isSet(sd.EJTI15)) { stockResults += "<strong>EJTI15:</strong> <input type='number' name='EJTI15' value='" + sd.EJTI15 + "' style='width: 80px;' /><br/>"; }
        if(isSet(sd.EJRI07)) { stockResults += "<strong>EJRI07:</strong> <input type='number' name='EJRI07' value='" + sd.EJRI07 + "' style='width: 80px;' /><br/>"; }
        if(isSet(sd.FI4KAN)) { stockResults += "<strong>FI4KAN:</strong> <input type='number' name='FI4KAN' value='" + sd.FI4KAN + "' style='width: 80px;' /><br/>"; }
        if(isSet(sd.FIRIAN)) { stockResults += "<strong>FIRIAN:</strong> <input type='number' name='FIRIAN' value='" + sd.FIRIAN + "' style='width: 80px;' /><br/>"; }
        if(isSet(sd.FIIBAN)) { stockResults += "<strong>FIIBAN:</strong> <input type='number' name='FIIBAN' value='" + sd.FIIBAN + "' style='width: 80px;' /><br/>"; }
        stockResults += "</div></div></span>" +
            "<span class='td'>" + sd.Description + "</span>" +
            "<span class='td'><div class='UPop'>$" + parseFloat(sd.LastValue).toFixed(2) + 
            "<div class='UPopO'>" +
            "<strong>Multiplier:</strong> " + sd.Multiplier.toFixed(4) + "x" + 
            "</div></div></span>" +
            "<span class='td'><input type='text' name='Holder' value='" + sd.Holder + "' style='width: 60px;' /></span>" +
            "<span class='td'>$" + autoUnits(holdingValue) + "</span>" +
            "<span class='td'>" + sd.Managed + "</span>" +
            "</form>";
    });
    stockResults += "<form class='tr stockAddUpdateForm'>" +
            "<span class='td'><input class='C2UStock' type='checkbox' name='Action' value='Add' /></span>" +
            "<span class='td'><input type='text' name='Symbol' style='width: 60px;' /></span>" +
            "<span class='td'><input type='number' name='Count' style='width: 80px;' /></span>" +
            "<span class='td'><input type='text' name='Description' style='width: 120px;' /></span>" +
            "<span class='td'>(N/A)</span>" + 
            "<span class='td'><input type='text' name='Holder' style='width: 60px;' /></span>" +
            "<span class='td'>(N/A)</span>" + 
            "<span class='td'><input type='number' step='1' name='Managed' style='width: 30px;' /></span>" +
            "</form></div>";
    rData += stockResults +
            "<p><em>Blank space for pop-over</em>";
    dojo.byId("FBStocks").innerHTML = etbaInsert + crytpoInsert + rData;
    dojo.query(".C2UStock").connect("onchange", actOnStockFormSubmit);
    dojo.query(".C2UETBA").connect("onchange", actOnETBAFormSubmit);
    dojo.query(".C2UCR").connect("onchange", actOnCryptoFormSubmit);
	ch_get_BrokerageDist("brkdist", "thumb");
	ch_get_RetirementDist("retdist", "thumb");
	ch_get_RetirementDistE("retdistE", "thumb");
}

function setCryptoAdd(formData) {
    aniPreload("on");
    formData.doWhat = "putCryptoAccountAdd";
    var xhArgs = {
        preventCache: true,
        url: getResource("Stock"),
        postData: formData,
        handleAs: "text",
        timeout: timeOutMilli,
        load: function(data) {
            showNotice("Crypto ledger added!");
            getCheckbook();
            getStocks();
            aniPreload("off");
        },
        error: function(data, iostatus) {
            showNotice("xhrPost for CryptoAdd FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
            aniPreload("off");
        }
    };
    dojo.xhrPost(xhArgs);
}

function setETBAAdd(formData) {
    aniPreload("on");
    formData.doWhat = "putETradeBrokerageAccountAdd";
    var xhArgs = {
        preventCache: true,
        url: getResource("Stock"),
        postData: formData,
        handleAs: "text",
        timeout: timeOutMilli,
        load: function(data) {
            showNotice("Stock ledger added!");
            getCheckbook();
            getStocks();
            aniPreload("off");
        },
        error: function(data, iostatus) {
            showNotice("xhrPost for ETBrokerageAdd FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
            aniPreload("off");
        }
    };
    dojo.xhrPost(xhArgs);
}

function setStockAdd(formData) {
    aniPreload("on");
    formData.doWhat = "putStockAdd";
    var xhArgs = {
        preventCache: true,
        url: getResource("Stock"),
        postData: formData,
        handleAs: "text",
        timeout: timeOutMilli,
        load: function(data) {
            showNotice("Stock added!");
            getStocks();
            aniPreload("off");
        },
        error: function(data, iostatus) {
            window.alert("Stock Add FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
            aniPreload("off");
        }
    };
    dojo.xhrPost(xhArgs);
}

function setStockUpdate(formData) {
    aniPreload("on");
    formData.doWhat = "putStockUpdate";
    var xhArgs = {
        preventCache: true,
        url: getResource("Stock"),
        postData: formData,
        handleAs: "text",
        timeout: timeOutMilli,
        load: function(data) {
            showNotice("Stock updated!");
            getStocks();
            aniPreload("off");
        },
        error: function(data, iostatus) {
            window.alert("Stock Update FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
            aniPreload("off");
        }
    };
    dojo.xhrPost(xhArgs);
}
