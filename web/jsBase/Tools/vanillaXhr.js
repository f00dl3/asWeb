/* 
by Anthony Stump
Created: 14 Feb 2018
*/

function vanillaXhrGet(xhrArgs, target) {
    var xhr = new XMLHttpRequest();
    xhr.open("GET", xhrArgs, true);
    xhr.onload = function(e) {
        if(xhr.readyState === 4 && xhr.status === 200) {
            dojo.byId(target).innerHTML = xhr.responseText;
        } else {
            dojo.byId(target).innerHTML = "Error on Vanilla XHR!: RS="+xhr.readyState+", ST="+xhr.status+", Text: "+xhr.statusText;
        }
    };
    xhr.onerror = function(e) { dojo.byId(target).innerHTML = "Error on Vanilla XHR!: RS="+xhr.readyState+", ST="+xhr.status+", Text: "+xhr.statusText; }
    xhr.send(null);
}