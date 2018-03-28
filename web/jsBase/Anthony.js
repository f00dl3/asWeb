/* 
by Anthony Stump
Created: 4 Mar 2018
Updated: 28 Mar 2018
*/

var initAnthony = function(event) {
    getObsData("disHolder", "marquee");
//    getObsDataMerged("disHolder", "marquee");
    getWebLinks("Anthony.php-0", "linkList", null);
    getWebVersion("versionPlaceholder");
};

dojo.ready(initAnthony);