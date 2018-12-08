/* 
by Anthony Stump
Created: 6 Dec 2018
Updated: 7 Dec 2018
 */

function putFlashGames(target) {
    var rData = "Loading flash games...<br/>" +
            "<div id='flashPlayerHolder'>HOLDER</div>";
    dojo.byId(target).innerHTML = rData;
}