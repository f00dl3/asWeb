/* 
by Anthony Stump
Created: 12 Feb 2018
Updated: 24 Feb 2018
*/


/*
 *  $(window).width or $(window).height for JS
 * @media (width) @media (height) for CSS window props
 */

var shortBaseForRestlet = "/asWeb/r";
var fullBaseForRestlet = self.location.protocol + "//" + self.location.host + shortBaseForRestlet;
var baseForRestlet = fullBaseForRestlet;
var timeOutMilli = (20*1000);
var loggedIn = false;

$(document).ready(function() {
    $.ajax({
        url: baseForRestlet+"/WebVersion",
        dataType: 'json',
        type : 'GET',
        success: function(data){
            window.alert(data);
        },
        error: function(jqXHR, textStatus, ajaxOptions, errorThrown, result) { 
            window.alert(" jqXHR: "+jqXHR+"\n textStatus: "+textStatus+"\n errorThrown: "+errorThrown+"\n ajaxOptions: "+ajaxOptions+"\n result: "+result);
            console.log(jqXHR.status);
        }
    }); 

});
/*
var init = function(event) {
    dojo.xhrGet(xhrLogArgs);
    dojo.xhrGet(xhrWebVersionArgs);
    var loginForm = dojo.byId('loginForm');
    dojo.connect(loginForm, "onsubmit", loginSend);
};

var xhrLogArgs = {
    url: baseForRestlet+"/Login",
    handleAs: "json",
    timeout: timeOutMilli,
    load: function(data) {
        event.preventDefault();
        alert(data);
        for(var i = 0; i < data.length; i++) {
            var jsonData = data;
            dojo.byId('lastUser').innerHTML = jsonData['User'];
            dojo.byId('lastTime').innerHTML = jsonData['LoginTime'];
            dojo.byId('lastIP').innerHTML = jsonData['RemoteIP'];
            //dojo.byId('debug2').innerHTML = "xhrGet WebAccessLogs: OK! ("+data+")";
        }
    },
    error: function(data, iostatus) {
        dojo.byId('debug2').innerHTML = "xhrGet WebAccessLogs: FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")";
    }
};

var xhrWebVersionArgs = {
    url: baseForRestlet+"/WebVersion",
    handleAs: "text",
    timeout: timeOutMilli,
    load: function(data) {
        event.preventDefault();
        for(var i = 0; i < data.length; i++) {
            var jsonData = data;
            dojo.byId('webVersion').innerHTML = jsonData['Version'];
            dojo.byId('debug3').innerHTML = "xhrGet WebVersion: OK! ("+data+")";
        }
    },
    error: function(data, iostatus) {
        dojo.byId('debug3').innerHTML = "xhrGet WebVersion: FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")";
    }
};

function loginSend(event) {
    dojo.stopEvent(event);
    var xhrArgs = {
        form: dojo.byId('loginForm'),
        url: baseForRestlet+"/Login",
	timeout: timeOutMilli,
        handleAs: "text",
        load: function(data) {
            dojo.byId('debug2').innerHTML = "Login form xhrPost: OK!";
        },
        error: function(data) {
            dojo.byId('debug2').innerHTML = "Login form xhrPost: FAIL!";
        }
    }
    dojo.byId('debug2').innerHTML = "Login form xhrPost: LOADING...";
    var deferred = dojo.xhrPost(xhrArgs);
}

dojo.ready(init);
*/