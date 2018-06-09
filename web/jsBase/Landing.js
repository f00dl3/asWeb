/* 
by Anthony Stump
Created: 12 Feb 2018
Updated: 9 Jun 2018
 **/

var loggedIn = false;

function actOnLogin(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    window.alert("Attempted login!");
    var concattedUserAndPass = thisFormData.User + "::" + thisFormData.Pass;
    setSessionVariable("userAndPass", concattedUserAndPass);
    setLogLogin(thisFormData);
}
    
function getWebLogs() {
    var xhrLogArgs = {
        url: getResource("Login"),
        handleAs: "json",
        timeout: timeOutMilli,
        load: function(data) {
            var theData = data[0];
            dojo.byId('lastUser').innerHTML = theData.User;
            dojo.byId('lastTime').innerHTML = theData.LoginTime;
            dojo.byId('lastIP').innerHTML = theData.RemoteIP;
        },
        error: function(data, iostatus) {
            console.log("xhrGet WebAccessLogs: FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
        }
    };
    dojo.xhrGet(xhrLogArgs);
}

function getWebVersion() {
    var firstXhrUrl = getResource("WebVersion");
    var xhrWebVersionArgs = {
        url: firstXhrUrl,
        handleAs: "json",
        timeout: timeOutMilli,
        load: function(data) {
            var theData = data[0];
            var thisDiv = "<div class='UPop'>" + theData.Version + " (Updated: " + theData.Date + ")";
            thisDiv += "<div class='UPopO'>" + theData.Changes + "</div></div>";
            dojo.byId('webVersion').innerHTML = thisDiv;
        },
        error: function(data, iostatus) {
            console.log("xhrGet WebVersion: FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
        }
    };
    dojo.xhrGet(xhrWebVersionArgs);
}

function populateLogin() {
    var rData = "<form id='loginForm'>" +
            "<div class='table'>" +
            "<div class='tr'><span class='td'>User</span><span class='td'><input type='text' name='User'></input></span></div>" +
            "<div class='tr'><span class='td'>Password</span><span class='td'><input type='password' name='Pass'></input></span></div>" +
            "</div>" +
            "<p><button class='UButton' id='btnLogin' type='submit'>Login!</button>" +
            "</form>";
    dojo.byId("loginPlaceholder").innerHTML = rData;
    var btnLogin = dojo.byId('btnLogin');
    dojo.connect(btnLogin, "click", actOnLogin);
}

function setLogLogin(thisFormData) {
    getSessionVariables();
    aniPreload("on");
    var clientIp = "";
    var clientBrowser = "";
    if(isSet(sessionVars)) {
        clientIp = sessionVars.clientIp;
        clientBrowser = sessionVars.clientBrowser;
    }
    var thePostData = {
        "doWhat": "LogLogin",
        "UserName": thisFormData.User,
        "RemoteIP": clientIp,
        "UserAgent": clientBrowser
    };
    console.log(thePostData);
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Logs"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    aniPreload("off");
                    window.location.href = getResource("Landing");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request to update Access Log FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

var init = function(event) {
    getWebLogs();
    getWebVersion();
    populateLogin();
};

dojo.ready(init);