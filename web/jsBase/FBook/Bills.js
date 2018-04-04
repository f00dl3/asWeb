/* 
by Anthony Stump
FBook.js Created: 23 Mar 2018
FBook/Bills.js Split: 4 Apr 2018
 */

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

