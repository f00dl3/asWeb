/*
by Anthony Stump
Created: 4 Mar 2019
Updated: 4 Mar 2019
*/

function getPhoneTrackFromDatabase(map, dataInput) {
    aniPreload("on");
    var thePostData = {
        "doWhat": "getPhoneTrack",
        "SearchString": dataInput
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("SNMP"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    aniPreload("off");
                    var tObj = data;
                    var gjArray = [];
                    (tObj.PhoneTrack).forEach(function(ta) {
                    	gjArray.push(ta.Location);
                    });
                    var routeData = (JSON.stringify(gjArray))
                    	.replace(/\"/g, "")
                    	.replace(/\[null\,null\]\,/g, "");
                    console.log(routeData);
                    addLineStringToMap(map, JSON.parse(routeData), tObj.Description);
                    showNotice("Track: " + tObj.Description);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for Phone Track FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}