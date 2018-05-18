/* 
by Anthony Stump
Created: 17 May 2018
 */

function populateKilaeuaData() {
    var rData = "<strong>Kilaeua data</strong><br/>" +
            "<a href='https://volcanoes.usgs.gov/observatories/hvo/' target='hvo'>Hawaii Volcano Observatory</a><br/>" +
            "<a href='http://volcanoyt.com' target='volyt'>VolcanoYT Website</a><br/>" +
            "<a href='https://www.youtube.com/live_chat?v=GJ7muLq5O9o' target='vchat'>YT Volcano Chat</a><br/>" +
            "<a href='https://livestormchasing.com/stream/Brandon.Clement' target='live'>Brandon Clement Live Storm Chasing</a><br/>"
            "** Volcano Cam Leeching";
    dojo.byId("vHolder").innerHTML = rData;
}


