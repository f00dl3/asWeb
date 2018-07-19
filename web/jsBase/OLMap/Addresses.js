/* 
by Anthony Stump
Created: 18 Jul 2018
 */

function addAddresses(map, data) {
    console.log(data)
}

function getAddresses(map) {
    require(["dojo/request"], function(request) {
        request
            .get(getResource("Addresses"), {
                    handleAs: "json"  
            }).then(
                function(data) {
                    addAddresses(map, data);
                    aniPreload("off");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for Addresses fail, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                })
    });
}