/* 
by Anthony Stump
Created: 6 Dec 2018
Updated: 9 Dec 2018
 */

function putFlashGames(target) {
    window.alert("Flash clicked!");
    $("#"+target).toggle();
    var rData = "Loading flash games...<br/>" +
            "<div id='flashPlayerHolder'>HOLDER</div>" +
            "<p>";
    dojo.byId(target).innerHTML = rData;
}