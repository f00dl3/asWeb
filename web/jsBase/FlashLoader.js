/* 
By Anthony Stump
Created: 9 Dec 2018
 */

function initFlash() {
    var swfFile = getBasePath("media") + "/Flash/" + flashFile;
    $('#flashFileHolder').flash({
        swf: swfFile,
        width: 1024,
        height: 768
    });
}

dojo.ready(initFlash);