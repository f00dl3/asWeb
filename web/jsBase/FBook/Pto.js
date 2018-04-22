/* 
by Anthony Stump
FBook.js Created: 23 Mar 2018
FBook/Pto.js Split: 4 Apr 2018
Updated: 22 Apr 2018
 */

function displayWorkPTO() {
    getWorkPTO();
    $("#FBWorkPTO").toggle();
    $("#FBAuto").hide();
    $("#FBBills").hide();
    $("#FBBlue").hide();
    $("#FBCheck").hide();
    $("#FBAsset").hide();
    $("#FBUUse").hide();
}

function getWorkPTO() {
    aniPreload("on");
    var thePostData = "doWhat=getWorkPTO";
    var xhArgs = {
        preventCache: true,
        url: getResource("Pto"),
        postData: thePostData,
        handleAs: "json",
        timeout: timeOutMilli,
        load: function (data) {
            putWorkPTO(data);
            aniPreload("off");
        },
        error: function (data, iostatus) {
            aniPreload("off");
            window.alert("xhrGet for WorkPTO FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
        }
    };
    dojo.xhrPost(xhArgs);
}

function putWorkPTO(ptoData) {
    var ptoCols = ["Month", "New", "Taken", "Save", "Balance", "Notes"];
    var rData = "<h3>Work PTO</h3>";
    var ptoTable = "<table><thead><tr>";
    for (var i = 0; i < ptoCols.length; i++) {
        ptoTable += "<th>" + ptoCols[i] + "</th>";
    }
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