/* 
by Anthony Stump
Created: 30 Mar 2018
Updated: 25 Apr 2018
 */

function checkIfSnmpIsUp(state) {
    switch(state) {
        case true: doSnmpWidget(); break;
        case false: break;
    }
}

function snmpRapid() {
    aniPreload("on");
    var thePostData = { "doWhat": "RapidSNMP" };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("SNMP"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    console.log(data);
                    aniPreload("off");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for Media Server Index FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}