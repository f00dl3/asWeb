/* 
by Anthony Stump
Created: 16 Apr 2018
 */

var fileList;
var tppCallbackData;
var lastWarYear = 2007;

function getFileListing(argsIn) {
    // IN DEV
    // passin [ showListOnly ]
    aniPreload("on");
    var thePostData = {
        "doWhat": "getFileListing",
        "folderPath": argsIn.thisPath
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("MediaServer"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    // Expect full file/path name, short file name, image size, image resolution 
                    generateGallery(argsIn, data);
                    aniPreload("off");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for Picture Index FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function getTpPicsCallback(ffn) {
    aniPreload("on");
    var thePostData = {
        "doWhat": "getTpPics",
        "ffnForHit": ffn
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("TP"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    // Build callback to generateGallery
                    tppCallbackData = data;
                    aniPreload("off");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for TPPic Callback FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function generateGallery(argsIn, data) {
    var thisFFN;
    var rData = argsIn.rData;
    var photoCount = 0;
    var imgBorder = "red";
    fileList.forEach(function (fiData) {
        photoCount++;
        var fileName = fiData.FileName;
        var fullPath = fiData.FullPath;
        var relativePath = "";
        switch(argsIn.flag) {
            case "tp":
                relativePath = fullPath.replace("/var/lib/tomcat8/webapps/TPM#", "/TPM");
                thisFFN = argsIn.params + "/" + fileName;
                getTpPicsCallback(thisFFN);
                // wait for it
                if(isSet(tppCallback.XTags)) { imgBorder = "green"; }
                break;
            case "tc":
                relativePath = fullPath.replace("/var/lib/tomcat8/webapps/ASWebUI#Tomcat#", "/ASWebUI/Tomcat");
                break;
            default:
                relativePath = fullPath.replace("/var/www", "");
                break;
        }
        var imageSize = (fiData.Resolution).split("x");
        var iWidth = imageSize[0], iHeight = imageSize[1];
        var thumbPath = relativePath.replace("/full/", "/thumb/");
        if(!isSet(imgBorder)) { imgBorder = "purple"; }
        rData += "<div class='UPop'>" +
                "<a href='" + getBasePath("old") + "/OutMap.php?" +
                "Image=Gallery&IW=" + iWidth + "&IH=" + iHeight + "&PicPath=" + relativePath + "' target='new'>";
        if(checkMobile()) {
            rData += "<img class='th_small' src='" + thumbPath + "' style='border: 2px solid " + imgBorder + ";'/>";
        } else {
            rData += "<img class='th_sm_med' src='" + thumbPath + "' style='border: 3px solid " + imgBorder + ";'/>";
        }
        rData += "</a><div class='UPopO'>" +
                "<strong>File: </strong>" + fileName + "<br/>" +
                "<strong>Size: </strong>" + (fiData.SizeBytes / 1024).toFixed(1) + "<br/>" +
                "<strong>Path: </strong>" + fiData.FullPath + "<br/>";
        if(isSet(tppCallback.XTags)) { rData += "<strong>Tags: </strong>" + tppCallback.XTags + "<br/>"; }
        rData += "</div></div>";
    });
    rData += "<p><strong>Total photo count: </strong>" + photoCount;
}

function generateYearDropdown() {
    for (var i = new Date().getFullYear(); i >= 2005; i--) {
        var yDropString = "<option value='" + i + "'>" + i + "</option>";
        dojo.byId("YearPicker").innerHTML += yDropString;
    }
}

function initGallery(flagsIn, firstArgIn) {
    var rData, thisPath, path;
    rData = thisPath = path = "";
    var showListOnly = 1;
    if(flagsIn === "photo" && isSet(firstArgIn)) {
        rData = "<h4>Photos from " + firstArgIn + "</h4>";
        if(firstArgIn <= lastWarYear) { flagsIn = "tc"; } else { flagsIn = "none"; }
        showListOnly = 0;
        path = "/Pics/" + firstArgIn;
    }
    switch(flagsIn) {
        case "tc": thisPath = getBasePath("tomcatOld") + "/ASWebUI#Tomcat#PicsL" + firstArgIn.substring(1, firstArgIn.length) + "/full/"; break;
        case "tp": thisPath = getBasePath("tomcatOld") + "/TPM#" + firstArgIn + "/full/"; break;
        case "none": thisPath = getBasePath("old") + "/Images/Memories/" + firstArgIn + "/full/"; break;
        default: window.alert("No flags in!"); break;
    }
    var varToPass = {
        "rData" : rData,
        "flag" : flagsIn,
        "thisPath" : thisPath,
        "arg2Pass" : firstArgIn,
        "showListOnly" : showListOnly
    };
    if(isSet(thisPath)) { // call Tomcat to get files in folder objects via REST call based on thisPath;
        getFileListing(varToPass);
    }
}

function populateGallery() {
    var rData = "<h4>Photos</h4>" +
            "<a href='" + getBasePath("old") + "/OutMap.php?PhotoGeo=true' target='photoGeo'>Map GeoCoded Photos</a><p>" +
            "<strong>Select Year:</strong>" +
            "<form id='PicForm'>" +
            "<select "; // left off here 4-16-18
    dojo.byId("GalleryHolder").innerHTML = rData;
}