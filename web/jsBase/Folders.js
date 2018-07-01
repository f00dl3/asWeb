/* 
by Anthony Stump
Created: 29 Jun 2018
Updated: 1 Jul 2018
 */

var targetDiv = dojo.byId("ffListHolder");

function actOnFolderSelect(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this);
    var thisFormDataJ = dojo.formToJson(this);
    lukeFolderWalker2(thisFormData.folder, targetDiv);
}

function actOnResourceSelect(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this);
    requestResource(thisFormData.fileToRequest);
}

function actOnSftpUpload(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    uploadResource(thisFormData);
}

function folderFileListing(holder, data) {
    var elementData = "<strong>Current path:</strong> " + data.Results.ScanFolder + "<br/>" +
            "<strong>Errors:</strong> " + data.Errors + "<p>";
    if(data.Results.ScanFolder !== "/") {
        var pathElements = (data.Results.ScanFolder).split("/");
        var parentFolder = "/";
        for(i = 0; i < (pathElements.length-1); i++) {
            parentFolder += pathElements[i] + "/";
        }
        elementData += "<form class='folderSelect'><input type='hidden' name='folder' value='" + parentFolder + "'/><strong>[Parent]</strong></form><p>";
    }
    shortFiles = (data.Results.ShortNameFiles).sort();
    fullFiles = (data.Results.FullPathsFiles).sort();
    shortFolders = (data.Results.ShortNameFolders).sort();
    fullFolders = (data.Results.FullPathsFolders).sort();
    var i = 0;
    var j = 0;
    if(shortFolders.length !== 0 || shortFiles.length !== 0) {
        if(shortFolders.length !== 0) {
            shortFolders.forEach(function (folder) {
                thisFullFolder = fullFolders[i];
                elementData += "<form class='folderSelect'>" +
                        "<input type='hidden' name='folder' value='" + thisFullFolder + "'/>" +
                        "<strong>" + folder + "</strong>" +
                        "</form><br/>";
                i++;
            });
        }
        if(shortFiles.length !== 0) {
            shortFiles.forEach(function (file) {
                thisFullFile = fullFiles[j];
                elementData += "<form class='resourceSelect'>" +
                        "<input type='hidden' name='fileToRequest' value='" + thisFullFile + "'/>" +
                        file +
                        "</form><br/>";
                j++;
            });
        }
    } else {
        elementData += "<strong>No files or folders found!</strong>";
    }
    holder.innerHTML = elementData;
    dojo.query(".folderSelect").connect("onclick", actOnFolderSelect);
    dojo.query(".resourceSelect").connect("onclick", actOnResourceSelect);
}

function folderFileListing2(holder, data) {
    var elementData = "<h4>" + data.Results.Folder + "</h4>";
    if(isSet(data.Errors)) { elementData += "<br/><strong>Errors:</strong> " + data.Errors; }
    if(data.Results.ScanFolder !== "/") {
        var pathElements = (data.Results.Folder).split("/");
        var parentFolder = "";
        for(i = 0; i < (pathElements.length-1); i++) {
            parentFolder += pathElements[i] + "/";
        }
        elementData += "<div class='table'><div class='tr'>" +
                "<span class='td'>" +
                "<form class='folderSelect'>" +
                "<input type='hidden' name='folder' value='" + parentFolder + "'/>" +
                "<button class='UButton'>UP</button>" + 
                "</form>" +
                "</span><span class='td'><strong>Size</strong><br/>" +
                "<span id='folderSizeHolder'></span>" +
                "</span></div></div><br/>" +
                "<form action='upload' method='post' enctype='multipart/form-data'>" +
                "<input type='hidden' name='path' value='" + data.Results.Folder + "'/>" +
                "<input id='sftpUpload' type='file' name='file' style='width: 240px;' /><br/>" +
                "</form>";
    }
    elementData += "<p>";
    var dirObj = (data.Results.InnerChildren);
    var totalFolderSize = 0;
    elementData += "<div class='table'>";
    Object.keys(dirObj).forEach(function (k) {
        if(dirObj[k].type === "folder") {
            elementData += "<form class='tr folderSelect'>" +
                    "<input type='hidden' name='folder' value='" + dirObj[k].path + "'/>" +
                    "<span class='td'><strong>" + k + "</strong></span>" +
                    "<span class='td'>-</span>" +
                    "</form>";
        } else {
            elementData += "<form class='tr resourceSelect'>" +
                        "<input type='hidden' name='fileToRequest' value='" + dirObj[k].path + "'/>" +
                        "<span class='td'>" + k + "</span>" +
                        "<span class='td'>" + autoUnits(dirObj[k].size) + "</span>" +
                        "</form>";
            totalFolderSize += Number(dirObj[k].size);
        }
    });
    elementData += "</div>";
    holder.innerHTML = elementData;
    dojo.query(".folderSelect").connect("onclick", actOnFolderSelect);
    dojo.query(".resourceSelect").connect("onclick", actOnResourceSelect);
    dojo.byId("folderSizeHolder").innerHTML = autoUnits(totalFolderSize);
    var sftpUp = dojo.byId("sftpUpload");
    dojo.connect(sftpUp, "change", actOnSftpUpload);
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

function lukeFolderWalker2(pathToScan, divContainer) {
    var lsTimeout = getRefresh("medium");
    if(isSet(divContainer)) { targetDiv = dojo.byId(divContainer); }
    var elementData = "<strong>RETREIVING FOLDER LIST V2...</strong>";
    targetDiv.innerHTML = elementData;
    var thePostData = {
        "doWhat": "LukeFolderWalker2",
        "scanPath": pathToScan
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Tools"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    elementData = folderFileListing2(targetDiv, data);
                },
                function(error) { 
                    window.alert("request for Folder Listing V2 FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
                });
    });
    setTimeout(function () { lukeFolderWalker2(pathToScan, divContainer); }, lsTimeout);
}

function requestResource(item) {
    window.location.href = getResource("Download") + "?fileToDownload=" + item;
}

function uploadResource(item) {
    window.alert("NOT BUILT YET - TO DO / NICE TO HAVE!");
    //window.location.href = getResource("Upload") + "?fileToDownload=" + item;
}

function init() {
    lukeFolderWalker2(folderOverride, "ffListHolder");
}

dojo.ready(init);