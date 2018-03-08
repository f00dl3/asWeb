/* 
by Anthony Stump
Created: 5 Feb 2018
Updated: 7 Feb 2018
 */

var urlXhr1 = baseForRestlet+"/WebLinks";

var init = function(event) {
    dojo.xhrPost(arXhr1);
};

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

dojo.ready(init);




