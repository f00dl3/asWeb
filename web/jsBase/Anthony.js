/* 
by Anthony Stump
Created: 4 Mar 2018
Updated: 26 Mar 2018
*/

var initAnthony = function(event) {
    getObsData("#WxObsMarq", "Marquee");
    getWeblinks("linkList");
    getWebVersion("versionPlaceholder");
};

dojo.ready(initAnthony);