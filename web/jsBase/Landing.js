/* 
by Anthony Stump
Created: 12 Feb 2018
Updated: 17 Apr 2018

  $(window).width or $(window).height for JS
 @media (width) @media (height) for CSS window props
 
 **/

var loggedIn = false;

function getWebLogs() {
    var xhrLogArgs = {
        url: getResource("Landing"),
        handleAs: "json",
        timeout: timeOutMilli,
        load: function(data) {
            var theData = data[0];
            dojo.byId('lastUser').innerHTML = theData.User;
            dojo.byId('lastTime').innerHTML = theData.LoginTime;
            dojo.byId('lastIP').innerHTML = theData.RemoteIP;
        },
        error: function(data, iostatus) {
            window.alert("xhrGet WebAccessLogs: FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
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
            window.alert("xhrGet WebVersion: FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
        }
    };
    dojo.xhrGet(xhrWebVersionArgs);
}

function isLoggedIn(userName) {
    if(getCookie("ValidLogin") === true) {
        window.location.href = "/asWeb/"+userName+".jsp";
    } else {
        window.alert("Not logged in. Please log in!\nAttempt redirect to ["+getBasePath("rest")+"/"+userName+".jsp]");
        window.location.href = "/asWeb/"+userName+".jsp";
    }
}

function sendLogin() {
    var loginForm = dojo.byId('loginForm');
    dojo.connect(loginForm, "onsubmit", loginSend);
    function loginSend(event) {
        dojo.stopEvent(event);
        var xhrArgs = {
            form: dojo.byId('loginForm'),
            url: getBasePath("rest")+"/Login",
            timeout: timeOutMilli,
            handleAs: "json",
            load: function(data) {
                window.alert("Login form xhrPost: OK!\nValid?: "+data.isValidLogin);
                setCookie("ValidLogin", data.isValidLogin);
                console.log("Cookies After Login.js loginSend(): \n" + listCookies());
            },
            error: function(data) {
                window.alert("Login form xhrPost: FAIL!");
            }
        };
        window.alert("Login form xhrPost: LOADING...");
        var deferred = dojo.xhrPost(xhrArgs);
    }   
}

var init = function(event) {
    getWebLogs();
    getWebVersion();
    sendLogin();
    getSessionVariables();
};

dojo.ready(init);