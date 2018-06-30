/* 
by Anthony Stump
Created: 29 Jun 2018
Updated: 30 Jun 2018
 */

var targetDiv = dojo.byId("ffListHolder");

function actOnFolderSelect(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this);
    var thisFormDataJ = dojo.formToJson(this);
    lukeFolderWalker(thisFormData.folder, targetDiv);
}

function actOnResourceSelect(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this);
    var thisFormDataJ = dojo.formToJson(this);
    window.alert(thisFormDataJ);
}

function folderFileListing(holder, data) {
    var elementData = "<strong>Base:</strong> " + data.InFolder + "<br/>" +
            "<strong>Errors:</strong> " + data.Errors + "<p>" +
            "<form class='folderSelect'><input type='hidden' name='folder' value='../'/><strong>[Parent]</strong></form><p>";
            "<ul type='dot'>";
    shortFiles = data.Results.ShortNameFiles;
    fullFiles = data.Results.FullPathsFiles;
    shortFolders = data.Results.ShortNameFolders;
    fullFolders = data.Results.FullPathsFolders;
    var i = 0;
    var j = 0;
    if(shortFolders.length !== 0 || shortFiles.length !== 0) {
        if(shortFolders.length !== 0) {
            shortFolders.forEach(function (folder) {
                thisFullFolder = fullFolders[i];
                elementData += "<li><form class='folderSelect'><input type='hidden' name='folder' value='" + thisFullFolder + "'/><strong>" + folder + "</strong></form></li>";
                i++;
            });
        }
        if(shortFiles.length !== 0) {
            shortFiles.forEach(function (file) {
                thisFullFile = fullFolders[j];
                elementData += "<li><form class='resourceSelect'><input type='hidden' name='fileToRequest' value='" + thisFullFile + "'/>" + file + "</form></li>";
                j++;
            });
        }
    } else {
        elementData += "<li>No files or folders found!</li>";
    }
    elementData += "</ul>";
    holder.innerHTML = elementData;
    dojo.query(".folderSelect").connect("onclick", actOnFolderSelect);
    dojo.query(".resourceSelect").connect("onclick", actOnResourceSelect);
}

function lukeFolderWalker(pathToScan, divContainer) {
    if(isSet(divContainer)) { targetDiv = dojo.byId(divContainer); }
    var elementData = "<strong>RETREIVING FOLDER LIST...</strong>";
    targetDiv.innerHTML = elementData;
    var thePostData = {
        "doWhat": "LukeFolderWalker",
        "scanPath": pathToScan
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Tools"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    elementData = folderFileListing(targetDiv, data);
                },
                function(error) { 
                    window.alert("request for Folder Listing FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
                });
    });
}

function requestResource(item) {
    var thePostData = {
        "doWhat": "RequestResource",
        "fileToTransfer": item
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Tools"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    showNotice("Requested: " + item);
                },
                function(error) { 
                    window.alert("request for Resource FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
                });
    });
}

function init() {
    lukeFolderWalker("/", "ffListHolder");
}

dojo.ready(init);