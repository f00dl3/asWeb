/* 
by Anthony Stump
Created: 16 Apr 2018
Updated: 21 Feb 2019
 */

var tppCallback;
var lastWarYear = getDate("day", 0, "yearOnly");

function actOnYearPicker(event) {
    var target = "GalleryHolder";
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    initGallery("photo", thisFormData.YearPicker);
}

function getFolderListing(argsIn) {
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
                    if(argsIn.args2Pass === "archive") {
                        populateArchiveHolder(argsIn, data);
                    } else {
                        generateGallery(argsIn, data);
                    }
                    aniPreload("off");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for File Listing FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
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

function generateGallery(argsIn, fileList) {
    console.log(argsIn + ", " + fileList);
    var thisFFN;
    var rData = argsIn.rData;
    var photoCount = 0;
    var imgBorder = "red";
    var iWidth = 1920;
    var iHeight = 1280;
    Object.keys(fileList).forEach(function (tFile) {
        photoCount++;
        var fileName = tFile;
        var fullPath = fileList[tFile].Path;
        var relativePath = "";
        var iRes = "";
        switch(argsIn.flagOut) {
            case "tp": case "tpi":
                relativePath = fullPath.replace(getServerPath("mediaServer"), getBasePath("media"));
                thisFFN = relativePath.replace("/asWeb/MediaServer/Adult", "").replace("/TP/", "/").replace("/TPi/", "/").substr(1);
                checkTpi(thisFFN);
                //getTpPicsCallback(thisFFN);
                // wait for it
                //if(isSet(tppCallback) && isSet(tppCallback.XTags)) { imgBorder = "green"; }
                break;
            case "tc":
                relativePath = getBasePath("ui") + fullPath.replace("/var/lib/tomcat8/webapps/asWeb#x#", "/x/");
                break;
            default:
                relativePath = getBasePath("old") + fullPath.replace("/var/www/ASWebUI", "");
                break;
        }
        if(isSet(fileList[tFile].Width) && isSet(fileList[tFile].Height)) {
            iWidth = fileList[tFile].Width;
            iHeight = fileList[tFile].Height;
            iRes = iWidth + "x" + iHeight;
        }
        var thumbPath = relativePath.replace("/full/", "/thumb/");
        if(!isSet(imgBorder)) { imgBorder = "purple"; }
        rData += "<div class='UPop'>" +
                /* "<a href='" + leafletMapImageLink(relativePath, iWidth, iHeight) + "' target='new'>";
                "<a href='" + relativePath + "' target='new'>"; */
                "<a href='" + olMapImageLink(relativePath, iRes) + "' target='tpPic'>";
        if(checkMobile()) {
            rData += "<img class='th_small' id='" + thisFFN + "' src='" + thumbPath + "' style='border: 2px solid " + imgBorder + ";'/>";
        } else {
            rData += "<img class='th_sm_med' id='" + thisFFN + "' src='" + thumbPath + "' style='border: 2px solid " + imgBorder + ";'/>";
        }
        rData += "</a><div class='UPopO'>" +
                "<strong>File: </strong>" + fileName + "<br/>" +
                "<strong>Size: </strong>" + (fileList[tFile].Size / 1024).toFixed(1) + "<br/>" +
                "<strong>Path: </strong>" + fileList[tFile].Path + "<br/>";
        if(argsIn.flagsOut == "tp" || argsIn.flagsOut == "tpi") {
        	rData += genUpdateTpiForm(iRes, thisFFN);
        }
        //if(isSet(tppCallback) && isSet(tppCallback.XTags)) { rData += "<strong>Tags: </strong>" + tppCallback.XTags + "<br/>"; }
        rData += "</div></div>";
    });
    rData += "<p><strong>Total photo count: </strong>" + photoCount;
    dojo.byId("GalleryHolderInside").innerHTML = rData;
}

function initGallery(flagsIn, firstArgIn, tpGlob) {
    var flagsOut;
    var rData, thisPath, path;
    rData = thisPath = path = "";
    var showListOnly = 1;
    if(flagsIn === "photo" && isSet(firstArgIn)) {
        rData = "<h4>Photos from " + firstArgIn + "</h4>";
        if(firstArgIn.valueOf() <= lastWarYear) { flagsOut = "tc"; } else { flagsOut = "none"; }
        showListOnly = 0;
        path = "/Pics/" + firstArgIn;
    } else if (flagsIn === "tp") {
        flagsOut = "tp";
    } else if (flagsIn === "tpi") {
    	flagsOut = "tpi";
    }
 
    switch(flagsOut) {
        case "tc":
            thisPath = getServerPath("tomcat") + "/asWeb#x#PicsL" + firstArgIn.substring(2, firstArgIn.length) + "/full/";
            break;
        case "tp":
            thisPath = getServerPath("mediaServer") + "/Adult/TP/" + firstArgIn + "/full/";
            break;
        case "tpi":
            thisPath = getServerPath("mediaServer") + "/Adult/TPi/" + firstArgIn + "/full/";
            break;
        case "none":
            thisPath = getServerPath("apache2") + "/ASWebUI/Images/Memories/" + firstArgIn + "/full/";
            break;
        default:
            window.alert("No flags in!");
            break;
    }
    var varToPass = {
        "rData" : rData,
        "flag" : flagsIn,
        "flagOut": flagsOut,
        "thisPath" : thisPath,
        "arg2Pass" : firstArgIn,
        "showListOnly" : showListOnly
    };
    if(isSet(thisPath)) {
        getFolderListing(varToPass);
    }
    dojo.byId("GalleryHolderInside").innerHTML = rData;
}

function populateArchiveHolder(varsToPass, fileList) {
    var rData = "<h4>Archives</h4>";
    var fileCount = 0;
    var linkArray = [];
    Object.keys(fileList).forEach(function (tFile) {
        fileCount++;
        var fileName = tFile;
        var thisFullPath = fileList[tFile].Path;
        var thisFileSizeFriendly = (fileList[tFile].Size/1024/1024).toFixed(1);
        var thisLinkString = "<a href='" + varsToPass.relativeUrlPath + "/" + fileName + "' target='new'>" + fileName + "</a> (" + thisFileSizeFriendly + " MBs)";
        rData += thisLinkString + "<br/>";
    });
    dojo.byId("GalleryHolderInside").innerHTML = rData;
}

function populateGallery(target) {
    var varsToPass = {};
    var subPath = "/Images/Memories/Archived";
    varsToPass.thisPath = getServerPath("apache2") + "/ASWebUI" + subPath;
    varsToPass.relativeUrlPath = getBasePath("old") + subPath;
    varsToPass.args2Pass = "archive";
    var rData = "<h4>Photos</h4>" +
            "<a href='" + getResource("Map.Media") + "' target='photoGeo'>Map GeoCoded Photos</a><p>" +
            "<strong>Select Year:</strong>" +
            "<form id='PicForm'>" +
            "<select name='YearPicker' id='YearPicker'>" +
            "<option value=''>Select...</option>";
    if(isSet(hiddenFeatures)) { rData += "<option value='Unsorted'>X</option>"; }
    for (var i = new Date().getFullYear(); i >= 2005; i--) {
        rData += "<option value='" + i + "'>" + i + "</option>";
    }
    rData += "</select></form><p>" +
            "<div id='Photos'>";
    
    rData += 
            "<div id='GalleryHolderInside'></div>" +
            "</div>";
    dojo.byId(target).innerHTML = rData;
    var yearPickerSelector = dojo.byId("YearPicker");
    getFolderListing(varsToPass);
    dojo.connect(yearPickerSelector, "onchange", actOnYearPicker);
}