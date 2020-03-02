/* 
by Anthony Stump
FBook.js Created: 23 Mar 2018
FBook/Bills.js Split: 4 Apr 2018
Updated: 1 Mar 2020
 */

function displayBills() {
    getBills();
    $("#FBBills").toggle();
    $("#FBAuto").hide();
    $("#FBAutoHC").hide();
    $("#FBAsset").hide();
    $("#FBBlue").hide();
    $("#FBCheck").hide();
    $("#FBWorkPTO").hide();
    $("#FBUUse").hide();
}

function getBillChart() {
    var billChData = "<a href='" + doCh("j", "FinBills", null) + "' target='pChart'><img class='ch_large' src='" + doCh("j", "FinBills", "th") + "'/></a>";
    var thePostData = {
        "doWhat": "FinanceBills"
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Chart"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    aniPreload("off");
                    dojo.byId("billChHolder").innerHTML = billChData;
                },
                function(error) { 
                    aniPreload("off");
                    console.log("request for Bill Chart FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });    
}

function getBills() {
    aniPreload("on");
    var thePostData = "doWhat=getBills";
    var xhArgs = {
        preventCache: true,
        url: getResource("Finance"),
        postData: thePostData,
        handleAs: "json",
        timeout: timeOutMilli,
        load: function (data) {
            putBills(data);
            aniPreload("off");
        },
        error: function (data, iostatus) {
            aniPreload("off");
            window.alert("xhrGet for Bills FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
        }
    };
    dojo.xhrPost(xhArgs);
}

function putBills(billData) {
    var rData = "<h3>Bills</h3>";
    var bCols = [
        "Month",
        "ELE",
        "GAS",
        "WAT",
        "SWR",
        "TRA",
        "WEB",
        "PHO",
        "GYM",
        "OTH",
        "Total"
    ];
    var bCharts = "<div id='billChHolder'>Loading Chart...</div>";
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
                "<td>" + bdat.Gym + "</td>" +
                "<td>" + bdat.Other + "</td>" +
                "<td>" + bdat.Total + "</td>" +
                "</tr>";
    });
    bTable += "</tbody></table>";
    rData += bTable;
    dojo.byId("FBBills").innerHTML = rData;
    getBillChart();
}

