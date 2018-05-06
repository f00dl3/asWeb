/* 
by Anthony Stump
Created: 15 Apr 2018
Updated: 6 May 2018
 */

function displayReddit() {
    $("#ETReddit").toggle();
    $("#ETCooking").hide();
    $("#ETLego").hide();
    $("#ETGameAll").hide();
    $("#ETStream").hide();
    layoutReddit();
}


function layoutReddit() {
    rData = "<h4>Reddit Database</h4>" +
            "<a href='" + getBasePath("rOut") + "' target='top'>Auto-fetched tarballs</a><p>";
    dojo.byId("ETReddit").innerHTML = rData;
}