/* 
by Anthony Stump
Created: 7 Feb 2018
Updated: 17 Apr 2018
 */

function add2Cookie(attrib, value) {
    var theCookie = document.cookie;
    theCookie += ";" + attrib + "=" + value;
    document.cookie = theCookie;
}

function getCookie(attrib) {
    var name = attrib + "=";
    var ca = document.cookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) === ' ') {
            c = c.substring(1);
        }
        if(c.indexOf(name) === 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

function getSessionVariables() {
    aniPreload("on");
    var thePostData = { "doWhat": "getSessionVariables" };
    require(["dojo/request"], function(request) {
        request
            .get(getResource("Session"), {
                handleAs: "text"
            }).then(
                function(data) {
                    console.log(data);
                    window.localStorage.setItem("sessionVars", data);
                    aniPreload("off");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for Session Variables FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function setCookie(attrib, value) {
    var d = new Date();
    d.setTime(d.getTime() + (3 * 60 * 60 * 1000));
    var expires = "expires="+d.toUTCString();
    document.cookie = attrib + "=" + value + ";" + expires + ";path=/";
}

function setSessionVariable(varName, varValue) {
    aniPreload("on");
    var thePostData = {
        paramName: varName,
        paramValue: varValue
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Session"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    aniPreload("off");
                    window.localStorage.setItem("sessionVars", data);
                    console.log("Session variable [ " + varName + " set to " + varValue + " ]\n" + data);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request to set Session Variable FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function listCookies() {
    var theCookie = document.cookie.split(';');
    var aString = '';
    for (var i = 1; i < theCookie.length; i++) {
        aString += i + ' ' + theCookie[i-1] + "\n";
    }
    return aString;
}

/* setCookie("CookieExists", "true");
console.log("Cookies from CookieMgmt.js: \n" + listCookies()); */