/* 
by Anthony Stump
Created: 18 Jul 2018
 */

function addHomicides(map, data) {
    console.log(data)
}

function getHomicides(map) {
    require(["dojo/request"], function(request) {
        request
            .get(getResource("Homicide"), {
                    handleAs: "json"  
            }).then(
                function(data) {
                    addHomicides(map, data);
                    aniPreload("off");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for Homicides!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                })
    });
}

