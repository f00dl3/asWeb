/* 
by Anthony Stump
Created: 20 Apr 2018
*/

function initEmily() {
    getObsDataMerged("disHolder", "marquee");
    getWebLinks("Emily.php-0", "linkList", "list");
    getWebVersion("versionPlaceholder");
    logButtonListener();
};

dojo.ready(initEmily);