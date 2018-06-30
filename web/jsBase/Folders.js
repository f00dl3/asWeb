/* 
by Anthony Stump
Created: 29 Jun 2018
Updated: 30 Jun 2018
 */

var targetDiv = dojo.byId("ffListHolder");

function actOnFolderSelect(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    var thisFormDataJ = dojo.formToJson(this.form);
    window.alert(thisFormDataJ);
}

function folderFileListing(holder, data) {
    var elementData = "<strong>Base:</strong>" + data.InFolder + "<br/>" +
            "<strong>Errors:</strong>" + data.Errors + "<p>" +
            "<ul type='dot'>";
    shortFiles = data.Results.ShortNameFiles;
    shortFolders = data.Results.ShortNameFolders;
    if(shortFolders.length !== 0 || shortFiles.length !== 0) {
        if(shortFolders.length !== 0) {
            shortFolders.forEach(function (folder) {
                elementData += "<li>" +
                        "<form>" +
                        "<input type='hidden' name='folder' value='" + folder + "'/>" +
                        "<div class='folderSelect'><strong>" + folder + "</strong></div>" +
                        "</form>" + 
                        "</li>";
            });
        }
        if(shortFiles.length !== 0) {
            shortFiles.forEach(function (file) {
                elementData += "<li><a href='" + file + "' target='new'>" + file + "</a></li>";
            });
        }
    } else {
        elementData += "<li>No files or folders found!</li>";
    }
    elementData += "</ul>";
    holder.innerHTML = elementData;
    dojo.query(".folderSelect").connect("onclick", actOnFolderSelect);
}

function lukeFolderWalker(pathToScan, divContainer) {
    if(isSet(divContainer)) { targetDiv = divContainer; }
    var elementData = "<strong>RETREIVING FOLDER LIST...</strong>";
    target.innerHTML = elementData;
    var thePostData = {
        "doWhat": "testFolder",
        "scanPath": pathToScan
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Tools"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    elementData = folderFileListing(target, data);
                },
                function(error) { 
                    window.alert("request for Folder Listing FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
                });
    });
}
