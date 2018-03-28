/* 
by Anthony Stump
Created: 4 Mar 2018
Updated: 27 Mar 2018
*/

var initAnthony = function(event) {
    getObsData("#WxObsMarq", "Marquee");
    getWebLinks("Anthony.php-0", "linkList", null);
    getWebVersion("versionPlaceholder");
};

dojo.ready(initAnthony);