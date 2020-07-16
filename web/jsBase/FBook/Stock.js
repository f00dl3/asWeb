/* 
by Anthony Stump
Created: 16 Jul 2020
Updated: 16 Jul 2020
 */

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
            .post(getResource("Finance"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    aniPreload("off");
                	putStocks(data);
                    console.log("DEBUG: Stock pull success.");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("STOCK PULL FAILURE\n " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function putStocks(stockData) {
    let rData = "<h3>Stocks & Manged Funds</h3>";
    let stockResults = "<div class='table'>";
    stockData.forEach(function (sd) {
        stockResults += "<form class='tr stockAddUpdateForm'>"+
        	"<span class='td'><input class='C2UStock' type='checkbox' name='Action' value='Update' /></span>" +
            "<span class='td'><input type='hidden' name='Symbol' value='" + sd.Symbol + "'/>" + sd.Symbol + "</span>" +
            "<span class='td'><input type='number' step='0.001' name='Count' value='" + sd.Count + "' style='width: 80px;' /></span>" +
            "<span class='td'>" + sd.Description + "</span>" +
            "<span class='td'>$" + parseFloat(sd.LastValue).toFixed(2) + "</span>" +
            "<span class='td'><input type='text' name='Holder' value='" + sd.Holder + "' style='width: 60px;' /></span>" +
            "<span class='td'>$" + (sd.Count * sd.LastValue).toFixed(2) + "</span>" +
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
    dojo.byId("FBStocks").innerHTML = rData;
    dojo.query(".C2UStock").connect("onchange", actOnStockFormSubmit);
}

function setStockAdd(formData) {
    aniPreload("on");
    formData.doWhat = "putStockAdd";
    var xhArgs = {
        preventCache: true,
        url: getResource("Finance"),
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
        url: getResource("Finance"),
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
