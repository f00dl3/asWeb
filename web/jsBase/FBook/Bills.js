/* 
by Anthony Stump
FBook.js Created: 23 Mar 2018
FBook/Bills.js Split: 4 Apr 2018
Updated: 22 Apr 2018
 */

function displayBills() {
    getBills();
    $("#FBBills").toggle();
    $("#FBAuto").hide();
    $("#FBAsset").hide();
    $("#FBBlue").hide();
    $("#FBCheck").hide();
    $("#FBWorkPTO").hide();
    $("#FBUUse").hide();
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
    var bCols = ["Month", "ELE", "GAS", "WAT", "SWR", "TRA", "WEB", "PHO", "GYM", "OTH", "Total"];
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
                "<td>" + bdat.Gym + "</td>" +
                "<td>" + bdat.Other + "</td>" +
                "<td>" + bdat.Total + "</td>" +
                "</tr>";
    });
    bTable += "</tbody></table>";
    rData += bTable;
    dojo.byId("FBBills").innerHTML = rData;
}

