/* 
by Anthony Stump
Created: 10 Jun 2018
Updated: 14 Jun 2018
 */

function afterCountryDatastore(cdsData) {
    showNotice("Datstore fetched!");
    var rData = cdsData;
    dojo.byId("datastoreTreeTester").innerHTML = rData;
}

function genLayout() {
    var rData = "<h3>Dojo DataStore IFRS/IFWS tests</h3>" +
            //"<div id='datastoreTreeTester'>...Loading data...</div><br/>" +
            //"<div id='jsonRestTest'>... JSON Rest Test ...</div><br/>" +
            "<div id='treeTest2'>... Tree Test II ...</div>";
    dojo.byId("HeaderHolder").innerHTML = rData;
    treeTest2();
}

function gdsFitness() {
    aniPreload("on");
    var thePostData = { "doWhat": "getFitnessAsStore" };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Fitness"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    showNotice("Fitness Store Fetched!");
                    aniPreload("off");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for Fitness Store FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
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

function treeTest2() {
    showNotice("treeTest2 Called!");
    require([
        "dijit/Tree",
        "dojo/data/ItemFileReadStore",
        "dijit/tree/ForestStoreModel",
        "dojo/domReady!"
    ], function(Tree, ItemFileReadStore, ForestStoreModel) {
        var store = new ItemFileReadStore({
           url: "fitnessPull.djds" 
        });
        var treeModel = new ForestStoreModel({
            store: store,
            //query:
            rootId: "root",
            rootLabel: "Test",
            childrenAttrs: [ "children" ]
        });
        var myTree = new Tree({
            model: treeModel
        }, "treeTest2");
        myTree.startup();
    });
}

function init() {
    genLayout();
}   

dojo.ready(init);