/* 
by Anthony Stump
Created: 5 Feb 2018
Updated: 14 Feb 2018
 */

function getWeblinks() {
    
    var urlXhr1 = baseForRestlet+"/WebLinks";

    var arXhr1 = {
        url: urlXhr1,
        handleAs: "json",
        postData: "master=Anthony.php-0",
        timeout: timeOutMilli,
        load: function(data) {
            var placeholder;
            if(checkMobile() || !checkMobile()) {
                placeholder = "<ul>";
                for (var i = 0; i < data.length; i++) {
                    var theData = data[i];
                    if(checkMobile() && isSet(theData.DesktopLink)) {
                        var theLink = theData.DesktopLink;
                    } else {
                        var theLink = theData.URL;
                    }
                    placeholder += "<li><a href='"+theLink+"'>";
                    placeholder += theData.Description;
                    placeholder += "</a></li>";
                }
                placeholder += "</ul>";
                dojo.byId('linkList').innerHTML = placeholder;
            }

        },
        error: function(data, iostatus) {
            window.alert("xhrGet arXhr1: FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
        }
    };

    dojo.xhrPost(arXhr1);
    
}

function getWebVersion() {
    var firstXhrUrl = baseForRestlet+"/WebVersion";
    var xhrWebVersionArgs = {
        url: firstXhrUrl,
        handleAs: "json",
        timeout: timeOutMilli,
        load: function(data) {
            var theData = data[0];
            var thisDiv = "<div class='UPop'>v" + theData.Version + " (Updated: " + theData.Date + ")";
            thisDiv += "<div class='UPopO'>" + theData.Changes + "</div></div>";
            dojo.byId('versionPlaceholder').innerHTML = thisDiv;
        },
        error: function(data, iostatus) {
            window.alert("xhrGet WebVersion: FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
        }
    };
    dojo.xhrGet(xhrWebVersionArgs);
}

var init = function(event) {
    getWeblinks();
    getWebVersion();
};

dojo.ready(init);




