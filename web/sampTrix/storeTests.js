/* 
by Anthony Stump
Created: 10 Jun 2018
Updated: 13 Jun 2018
 */

function afterCountryDatastore(cdsData) {
    showNotice("Datstore fetched!");
    var rData = cdsData;
    dojo.byId("datastoreTreeTester").innerHTML = rData;
}

function genLayout() {
    var rData = "<h3>Dojo DataStore IFRS/IFWS tests</h3>" +
            "<div id='datastoreTreeTester'>...Loading data...</div>" +
            "<div id='jsonRestTest'>... JSON Rest Test ...</div>";
    dojo.byId("HeaderHolder").innerHTML = rData;
    getDatastoreCountries();
    gdsJsonRest();
}

function gdsJsonRest() {
    var rData;
    require(["dojo/store/JsonRest"], function(JsonRest) {
        var store = new JsonRest({ target: "std_Countries.djds" });
        rData = store;
        dojo.byId("jsonRestTest").innerHTML = rData;
    });
}

function getDatastoreCountries() {
    aniPreload("on");
    require(["dojo/request"], function(request) {
        request
            .get("std_Countries.djds", {
                handleAs: "text"
            })
            .then(
                function(data) {
                    aniPreload("off");
                    afterCountryDatastore(data);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request Countries datastore FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
            });
    });
}

function init() {
    genLayout();
}   

dojo.ready(init);