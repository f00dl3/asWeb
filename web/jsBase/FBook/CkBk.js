/* 
by Anthony Stump
FBook.js Created: 23 Mar 2018
FBook/CkBk.js Split: 4 Apr 2018
Updated: 19 Sep 2020
 */

var searchableData;
var resultLimit = 100;

function actOnCheckbookFormSubmit(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    var thisFormDataJ = dojo.formToJson(this.form);
    if(isSet(thisFormData.Action)) {
        switch(thisFormData.Action) {
            case "Add": setCheckbookAdd(thisFormData); break;
            case "Update": setCheckbookUpdate(thisFormData); break;
            default: window.alert("No action set!");
        }
    }
}

function actOnDoDiscover(event) {
	dojo.stopEvent(event);
	window.alert("Clicked Discover Button!");
}

function actOnDoOldNavy(event) {
	dojo.stopEvent(event);
	window.alert("Clicked Old Navy Button!");
}

function displayCheckbook() {
    getCheckbook();
    $("#FBCheck").toggle();
    $("#FBAuto").hide();
    $("#FBAutoHC").hide();
    $("#FBBills").hide();
    $("#FBBlue").hide();
    $("#FBAsset").hide();
    $("#FBWorkPTO").hide();
    $("#FBUUse").hide();
    $("#FBStocks").hide();
}

function genOverviewChecking(cbData) {
    var bubble = "<div class='UBox'>Check<br/><span>$" + numComma((cbData.Balance).toFixed(0)) + "</span></div>";
    dojo.byId("HoldChecking").innerHTML = bubble;
}

function getCheckbook() {
    aniPreload("on");
    var thePostData = "doWhat=getChecking";
    var xhArgs = {
        preventCache: true,
        url: getResource("Finance"),
        postData: thePostData,
        handleAs: "json",
        timeout: timeOutMilli,
        load: function (data) {
            putCheckbook(data);
            aniPreload("off");
        },
        error: function (data, iostatus) {
            aniPreload("off");
            window.alert("xhrGet for Checkbook FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
        }
    };
    dojo.xhrPost(xhArgs);
    //setTimeout(function() { getCheckbook(); }, getRefresh("medium"));
}

function getCheckbookDeep() {
	console.log("DEBUG: Attempting deep checkbook pull...");
    dojo.byId("cbSearchHolder").innerHTML = "LOADING DATA...";
    aniPreload("on");
    var thePostData = { "doWhat": "getCheckingDeep" };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Finance"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    aniPreload("off");
                    searchableData = data;
                    console.log("DEBUG: Deep checkbook pull success.");
                    putCheckbookSearchBox();
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for Deep Checkbook FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function putCheckbookSearchBox() {
    var cbSearchInner = "<div class='table'><form class='tr' id='CkBkSearch'>" +
    		/* "<span class='td'>" +
    		"<button class='UButton' id='doDiscover'>DI</button> " +
    		"<button class='UButton' id='doOldNavy'>ON</button> " +
    		"</span>" + */
            "<span class='td'><input type='text' id='SearchBoxText' name='CBSearchField' onKeyUp='searchAheadCheckbook(this.value)'/></span>" +
            "<span class='td'><strong>Search</strong></span>" +
            "</form></div><p>";
    // dojo.byId("doDiscover").connect("click", actOnDoDiscover);
    // dojo.byId("doOldNavy").connect("click", actOnDoOldNavy);
    dojo.byId("cbSearchHolder").innerHTML = cbSearchInner;
}

function putCheckbook(ckBkData) {
    var results = 0;
    var rData = "<h3>Checkbook</h3>";
    var cbSearch = "<div id='cbSearchHolder'></div>";
    var cbRange = "<div class='table' id='cbSearchResults'></div><p>";
    var cbResults = "<div class='table HideOnSearch'>";
    ckBkData.forEach(function (ckBk) {
        results++;
        var shortDesc = "";
        var replacePattern = "DBT PURCHASE ON ";
        if((ckBk.Description).includes(replacePattern)) { shortDesc = (ckBk.Description).replace(replacePattern, ""); } else { shortDesc = ckBk.Description; }
        shortDesc = shortDesc.substring(0, 32);
        var fbcr = (ckBk.Credit).toFixed(2); if (!isSet(fbcr) || fbcr == 0.00) { fbcr = ""; }
        var fbdb = (ckBk.Debit).toFixed(2); if (!isSet(fbdb) || fbdb == 0.00) { fbdb = ""; }
        cbResults += "<form class='tr cbAddUpdateForm'>";
        if (isSet(ckBk.Bank) && ckBk.Bank !== "0000-00-00") {
            cbResults += "<span class='td'> </span>" +
                    "<span class='td'>" + ckBk.Bank + "</span>" +
                    "<span class='td'>" + ckBk.Date + "</span>" +
                    "<span class='td UPop' style='width: 250px;'>" + shortDesc +
                    "<span class='UPopO'>" + ckBk.Description + "<br/><strong>CTID:</strong> " + ckBk.CTID + "</span>" +
                    "</span>" +
                    "<span class='td'>" + fbdb + "</span>" +
                    "<span class='td'>" + fbcr + "</span>" +
                    "<span class='td'>" + (ckBk.Balance).toFixed(2) + "</span>";
        } else {
            cbResults += "<span class='td'><input class='C2UCBook' type='checkbox' name='Action' value='Update' /></span>" +
                    "<span class='td'><input type='date' name='CkBkBank' value='" + ckBk.Bank + "' style='width: 80px;' /></span>" +
                    "<span class='td'><input type='date' name='CkBkDate' value='" + ckBk.Date + "' style='width: 80px;' /></span>" +
                    "<span class='td UPop'><input type='text' name='CkBkDesc' value='" + ckBk.Description + "' />" +
                    "<span class='UPopO'><input type='hidden' name='CkBkID' value='" + ckBk.CTID + "' />" +
                    "<strong>CTID:</strong> " + ckBk.CTID + "</span></span>" +
                    "<span class='td'><input type='number' step='0.01' name='CkBkDebi' value='" + fbdb + "' style='width: 70px;' /></span>" +
                    "<span class='td'><input type='number' step='0.01' name='CkBkCred' value='" + fbcr + "' style='width: 70px;' /></span>" +
                    "<span class='td'>" + (ckBk.Balance).toFixed(2) + "</span>";
        }
        cbResults += "</form>";
    });
    cbResults += "<form class='tr ckAddUpdateForm'>" +
            "<span class='td'><input class='C2UCBook' type='checkbox' name='Action' value='Add' /></span>" +
            "<span class='td'><input type='date' name='ACkBank' style='width: 80px;' /></span>" +
            "<span class='td'><input type='date' name='ACkDate' style='width: 80px;' /></span>" +
            "<span class='td'><input type='text' name='ACkDesc' /></span>" +
            "<span class='td'><input type='number' step='0.1' name='ACkDebi' style='width: 70px;' /></span>" +
            "<span class='td'><input type='number' step='0.1' name='ACkCred' style='width: 70px;' /></span>" +
            "<span class='td'>(N/A)</span>" +
            "</form></div>";
    rData += cbSearch + cbRange + cbResults + "Results returned: " + results +
            "<p><em>Blank space for pop-over</em>";
    dojo.byId("FBCheck").innerHTML = rData;
    dojo.query(".C2UCBook").connect("onchange", actOnCheckbookFormSubmit);
    getCheckbookDeep();
}

function putCheckbookSearchResults(matchingRows) {
    var rData = "";
    var tDebits, tDebitsAmt, tCredits, tCreditsAmt;
    tDebits = tDebitsAmt = tCredits = tCreditsAmt = 0;
    matchingRows.forEach(function (ckBk) {
        var difference = ckBk.Credit - ckBk.Debit;
        if (ckBk.Debit !== 0) {
            tDebits++;
            tDebitsAmt += ckBk.Debit;
        }
        if (ckBk.Credit !== 0) {
            tCredits++;
            tCreditsAmt += ckBk.Credit;
        }
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
                "<span class='UPopO'>" + ckBk.Description +
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
        rData += cbResult;;
    });
    dojo.byId("cbSearchResults").innerHTML = rData;
    $(".HideOnSearch").hide();
}

function searchAheadCheckbook(value) {
    if(value.length > 2) {
        var hitCount = 0;
        var matchLimitHit = 0;
        var matchingRows = [];
    	if(value.includes(" ")) {
    		wordArray = value.split(" ");
    		searchableData.forEach(function (sr) {
        		var wordsHit = 0;
        		wordArray.forEach(function(tWord) {
		            if(
	                    (isSet(sr.Card) && (sr.Card).toLowerCase().includes(tWord.toLowerCase())) ||
	                    (isSet(sr.CTID) && (sr.CTID).toLowerCase().includes(tWord.toLowerCase())) ||
	                    (isSet(sr.Bank) && (sr.Bank).toLowerCase().includes(tWord.toLowerCase())) ||
	                    (isSet(sr.Date) && (sr.Date).toLowerCase().includes(tWord.toLowerCase())) ||
	                    (isSet(sr.Description) && (sr.Description).toLowerCase().includes(tWord.toLowerCase()))
		            ) { 
		            	wordsHit++;
		            }
	    			if (wordsHit === wordArray.length) {
	    				hitCount++;
	                    if(matchingRows.length < (resultLimit-1)) {
	                    	matchingRows.push(sr);
	                    } else {
	                    	matchLimitHit = 1;
	                    }	
	    			}
	        	});
        	});
    	} else {
    		searchableData.forEach(function (sr) {
	            if(
                    (isSet(sr.Card) && (sr.Card).toLowerCase().includes(value.toLowerCase())) ||
                    (isSet(sr.CTID) && (sr.CTID).toLowerCase().includes(value.toLowerCase())) ||
                    (isSet(sr.Bank) && (sr.Bank).toLowerCase().includes(value.toLowerCase())) ||
                    (isSet(sr.Date) && (sr.Date).toLowerCase().includes(value.toLowerCase())) ||
                    (isSet(sr.Description) && (sr.Description).toLowerCase().includes(value.toLowerCase()))
	            ) { 
	                hitCount++;
	                if(matchingRows.length < (resultLimit-1)) {
	                    matchingRows.push(sr);
	                } else {
	                	matchLimitHit = 1;
	                }
	            }
	        });
        }
        putCheckbookSearchResults(matchingRows);
    }
}

function setCheckbookAdd(formData) {
    aniPreload("on");
    formData.doWhat = "putCheckbookAdd";
    var xhArgs = {
        preventCache: true,
        url: getResource("Finance"),
        postData: formData,
        handleAs: "text",
        timeout: timeOutMilli,
        load: function(data) {
            showNotice("Checkbook ledger added!");
            getCheckbook();
            aniPreload("off");
        },
        error: function(data, iostatus) {
            window.alert("xhrPost for CheckbookAdd FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
            aniPreload("off");
        }
    };
    dojo.xhrPost(xhArgs);
}

function setCheckbookUpdate(formData) {
    aniPreload("on");
    formData.doWhat = "putCheckbookUpdate";
    var xhArgs = {
        preventCache: true,
        url: getResource("Finance"),
        postData: formData,
        handleAs: "text",
        timeout: timeOutMilli,
        load: function(data) {
            showNotice("Checkbook ledger updated!");
            getCheckbook();
            aniPreload("off");
        },
        error: function(data, iostatus) {
            window.alert("xhrPost for CheckbookUpdate FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
            aniPreload("off");
        }
    };
    dojo.xhrPost(xhArgs);
}
