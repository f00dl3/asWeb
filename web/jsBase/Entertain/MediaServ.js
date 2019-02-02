/* 
by Anthony Stump
Created: 19 Mar 2018
Updated: 2 Feb 2019
 */

var msIndex;

function actOnNonMedia(event) {
    var target = "ETSResults";
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    var thisFormJson = dojo.formToJson(this.form);
    switch(thisFormData.Genre) {
        case "AO_TP": window.location.href = getBasePath("ui") + "/TPGallery.jsp"; break;
        case "AO_DBX": getDbx(target); break;
        case "ChicagoSeries": getChicagoSeries(target); break;
        case "Goosebumps": getGoosebumps(target); break;
        case "Photos": populateGallery(target); break;
        case "PowerRangers": getPowerRangers(target); break;
        case "StarTrek": getStarTrek(target); break;
        case "TrueBlood": getTrueBlood(target); break;
        case "XFiles": getXFiles(target); break;
        default: window.alert("Invalid / unbuilt option! (" + thisFormData.Genre + ")"); break;
    }
}

function actOnPlayMedia(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    var thisFormDataJ = dojo.formToJson(this.form);
    setPlayMedia(thisFormData);
    if(isSet(thisFormData.dbxRawFile)) {
        playDbxFile(thisFormData);
    } else {
        playMediaFile(thisFormData.FilePath);
    }
}

function displayMediaServer() {
    var target = "ETSSearch";
    getIndex(target, false);
    $("#ETStream").toggle();
    $("#ETCooking").hide();
    $("#ETLego").hide();
    $("#ETReddit").hide();
    $("#ETGameAll").hide();
}

function getIndex(target, updateFlag) {
    var isMobile = "no";
    var aContent = 0;
    var estimatedLoadSize = "Desktop compressed JSON size under 1 MB.";
    if(checkMobile()) {
        isMobile = "yes";
        estimatedLoadSize = "Mobile compressed JSON size under 1 MB.";
    }
    if(!updateFlag) { dojo.byId("ETSResults").innerHTML = "Loading Media Server Index...<p>" + estimatedLoadSize; }
    aniPreload("on");
    if(isSet(hiddenFeatures)) { aContent = 1; }
    var thePostData = {
        "doWhat": "getIndexed",
        "isMobile": isMobile,
        "aContent": aContent
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("MediaServer"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    msIndex = data.Index;
                    putSearchBox(target, data.Overview[0], updateFlag);
                    if(!updateFlag) { dojo.byId("ETSResults").innerHTML = "<p>Ready. " + data.Index.length + " filtered items."; }
                    aniPreload("off");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for Media Server Index FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function mediaOpts() {
    var tElement = "";
    var hMediaOpts = [ "AO_DBX" , "AO_TP" ];
    var mediaOpts = [
        "Photos",
        "ChicagoSeries",
        "Goosebumps",
        "PowerRangers",
        "StarTrek",
        "TrueBlood",
        "XFiles"
    ];
    if(isSet(hiddenFeatures)) {
        hMediaOpts.forEach(function(tOpt) {
           tElement += "<option value='" + tOpt + "'>" + tOpt + "</option>"; 
        });
    }
    mediaOpts.forEach(function(tOpt) {
        tElement += "<option value='" + tOpt + "'>" + tOpt + "</option>"; 
    });
    return tElement;
}

function mediaPlayColor(pCount) {
    var cPlays = "#000000";
    switch(pCount) {
        case "0": cPlays = "#ffffff"; break;
        case "1": cPlays = "#999999;"; break;
        case "2": cPlays = "#33ffff;"; break;
        case "3": cPlays = "#0099cc;"; break;
        case "4": cPlays = "#0066ff;"; break;
        default: cPlays = "#ffff00"; break;
    }
    return "color: " + cPlays + ";";
}

function playMediaFile(whatFile, dbxFlag) {
    var wfa = whatFile.split(".");
    var mediaType = wfa[wfa.length-1].toLowerCase();
    var mpo, mediaMime, mediaType, filePath;
    mpo = mediaMime = "";
    if(checkMobile()) { mpo += "<div class='PlayPop'>"; } else { mpo += "<div>"; }
    if(isSet(dbxFlag)) { 
        filePath = getBasePath("chartCache") + "/" + whatFile;
    } else {
        filePath = getBasePath("ui") + whatFile;
    }
    switch(mediaType) {
        case "mp3":
            mediaMime = "audio/mpeg";
            mpo += "<audio id='mediaServerAudoPlayer' controls autoplay loop>" +
                    "<source src='" + filePath + "' type='" + mediaMime + "'>" +
                    "</audio>";
            break;
        default:
            window.location.href = filePath;
            break;
    }
    mpo += "</div>";
    dojo.byId("ETSPlayer").innerHTML = mpo;
    dojo.byId("mediaServerAudoPlayer").play();
}

function playDbxFile(formData) {
    var thePostData = {
        "doWhat": "viewDbx",
        "rawFilePath": formData.dbxRawFile
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("MediaServer"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    playMediaFile(formData.unpackedDestination, true);
                    aniPreload("off");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for viewDbx FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function putFileResults(msData, hitCount, matchLimitHit) {
    var noticeMessage = "";
    var thumbSize = "";
    var firstThumbSize = "";
    if(checkMobile()) {
        thumbSize = "th_small";
        firstThumbSize = "th_icon";
    } else {
        thumbSize = "th_sm_med";
        firstThumbSize = "th_sm_med";
    }
    var fileTable = "<div class='table id='FileTable'>" +
            "<div class='tr'>" +
            "<span class='td'><img class='th_icon' src='" + getBasePath("icon") + "/ic_ply.png'/></span>" +
            "<span class='td'><strong>File</strong></span>" +
            "</div>";
    msData.forEach(function (tm) {
        var aaTag = (tm.Path).substr(0, 4);
        var spanColorVal = mediaPlayColor(tm.PlayCount);
        var dbDipInfo = "",
            thisAddCheckbox = "";
        var fileProps = (tm.File).split(".");
        var mediaType = fileProps[fileProps.length-1].toUpperCase();
        var forceMediaType = mediaType;
        var mediaDownloader = "<a href='" + getBasePath("media") + tm.Path + "/" + tm.File + "' target='new'>" +
                            "<img class='arrow' src='" + getBasePath("icon") + "/ar_dn.gif' /></a>";
        thisAddCheckbox += "<input type='hidden' name='origPath' value='" + tm.Path + "'/><input type='hidden' name='aaTag' value='" + aaTag + "'/>";
        if(isSet(tm.AlbumArt)) {
            if(aaTag === "/DBX") {
                var albumArtStripped = (tm.AlbumArt).split("/")[1];
                thisAddCheckbox += "<input type='hidden' name='dbxRawFile' value='" + tm.Path + "/" + albumArtStripped + ".raw'/>" +
                        "<input type='hidden' name='unpackedDestination' value='" + tm.File + "'/>";
            }
            if(
                aaTag === "/DBX" ||
                tm.Path === "/Adult/Export" ||
                tm.Path === "/Adult/Other") {
                dbDipInfo += "<img class='" + thumbSize + "' src='" + getBasePath("image") + "/AlbumArt/" + tm.AlbumArt + ".pnx'/><br/>";
            } else {
                dbDipInfo += "<img class='" + thumbSize + "' src='" + getBasePath("image") + "/AlbumArt/" + tm.AlbumArt + ".jpg'/><br/>";
            }
        }
        if(isSet(tm.Description)) {            
            if(isSet(tm.Burned)) { if(tm.Burned === 1) { dbDipInfo += "Burned to " + tm.Media + " on " + tm.BDate + "<br/>"; } }
            if(isSet(tm.ContentDate)) { dbDipInfo += "Created/Released: " + tm.ContentDate + "<br/>"; }
            if(isSet(tm.Artist)) { dbDipInfo += "Artist: " + tm.Artist + "<br/>"; }
            if(isSet(tm.LastSelected)) { dbDipInfo += "Last played on " + tm.LastSelected + ", " + tm.PlayCount + " total requests.<br/>"; }
            dbDipInfo += "Duration " + (tm.DurSec/60).toFixed(1) + " minutes, " + tm.Size + " blocks.<br/>" +
                    "Description: " + tm.Description + "<br/>";
            if(isSet(tm.BitRate)) {
                dbDipInfo += "Quality " + tm.BitRate + "kbps"; 
                if(isSet(tm.Hz)) { dbDipInfo += ", " + tm.Hz + "Hz, " + tm.Channels + " channels"; }
                if(isSet(tm.Resolution)) { dbDipInfo += ", resolution " + tm.Resolution; }
                dbDipInfo += "<br/>";
            }
            if(isSet(tm.MPAA)) {
                dbDipInfo += "Rated " + tm.MPAA;
                if(isSet(tm.MPAAContent)) { dbDipInfo += " for " + tm.MPAAContent; }
                dbDipInfo += "<br/>";
            }
            if(isSet(tm.XTags)) { dbDipInfo += "Tags: " + tm.XTags + "<br/>"; }
        } else { dbDipInfo = "No metadata available for this content!"; }
        if(tm.Working === 1 && tm.OffDisk === 0) {
            thisAddCheckbox += "<div class='UPop'>";
            switch(mediaType) {
                case "DOC": case "CHM": case "PDF": case "TXT": case "LSX": case "GIF": case "ZIP":
                    thisAddCheckbox += mediaDownloader;
                    break;
                case "SWF":
                    thisAddCheckbox += "<a href='" + getBasePath("ui") + "/FlashLoader.jsp?ff=" + tm.File + "' target='new'>" +
                            "<img class='arrow' src='" + getBasePath("icon") + "/ic_ply.png' /></a>";
                    break;
                case "JPG": case "JPEG": case "PNG":
                    if(isSet(tm.Archived)) {
                        thisAddCheckbox += "<a href='" + getBasePath("old") + "/Images/Memories/Archived/" + tm.Archived + ".war'>" +
                                "<img src='" + firstThumbSize + "' src='" + getBasePath("icon") + "/ic_zip.png' /></a>";
                    } else {
                        var imageAttribs = tm.Resolution.split("x");
                        var imageWidth = imageAttribs[0];
                        var imageHeight = imageAttribs[1];
                        //var imageWidthHeightAttribs = "IW=" + imageWidth + "&IH=" + imageHeight + "&";
                        var olResolution = imageAttribs[0] + "x" + imageAttribs[1];
                        var olPicPath, thisYear;
                        if(tm.WarDeploy === 1) {
                            thisYear = (tm.Path).substr((tm.Path).length - 2);
                            olPicPath = "/asWeb/x/PicsL" + thisYear + "/full/" + tm.File;
                            thisAddCheckbox += /* "<a href='" + leafletMapImageLink(olPicPath, imageWidth, imageHeight) + "' target='photoPop'>" + 
                                    "<a href='/asWeb/x/PicsL" + thisYear + "/full/" + tm.File + "' target='new'>" + */
                                    "<a href='" + olMapImageLink(olPicPath, olResolution) + "' target='photoPop'>" +
                                    "<img class='" + firstThumbSize + "' src='" + olPicPath + "'/>";
                        } else {
                            thisYear = (tm.Path).substr((tm.Path).length - 4);
                            olPicPath = "/asWeb/x/PicsL" + thisYear + "/full/" + tm.File;
                            thisAddCheckbox += "<a href='" + leafletMapImageLink(olPicPath, imageWidth, imageHeight) + "' target='photoPop'>" +
                                    /* "<a href='" + olMapImageLink(olPicPath, olResolution) + "' target='photoPop'>" + */
                                    "<img class='" + firstThumbSize + "' src='" + getBasePath("old") + "/Images/Memories/" + thisYear + "/thumb/" + tm.File + "'/>";
                        }
                        thisAddCheckbox += "</a>";
                    }
                    break;
                default:
                    thisAddCheckbox += "<input class='PlaySong' type='checkbox' name='FilePlay' value='Yes' />";
                    break;
            }
            thisAddCheckbox += "</div>";
        } else {
            thisAddCheckbox += "<div class='UPopNM'>" +
                    "<img class='" + firstThumbSize + "' src='" + getBasePath("icon") + "/ic_lck.jpeg'/>" +
                    "<div class='UPopNMO'>" +
                    "Viewed: <input class='PlaySong' type='checkbox' name='FilePlay' value='Yes'/>" +
                    "</div></div>";
        }
        var thisMsInfoString = "<form class='tr'>" +
                "<span class='td' style='width: 10%;'>" + thisAddCheckbox + "</span>";
        if(!isSet(tm.File)) {
            thisMsInfoString += "<span class='td' style='width: 80%; color: #ff3300;'>";
        } else {
            thisMsInfoString += "<span class='td' style='" + spanColorVal + "' width=80%;'>";
        }
        if(mediaType === "MP4") { forceMediaType = "m4v"; }
        thisMsInfoString += "<input type='hidden' name='MediaType' value = '" + forceMediaType + "' />" +
                "<div class='UPop'>" +
                "<input type='hidden' name='FileName' value='" + tm.File + "'/>" + tm.File;
        if(tm.PlayCount > 1) { thisMsInfoString += " <tt>(" + tm.PlayCount + ")</tt> "; }
        thisMsInfoString += "<img class='th_icon' src='" + getBasePath("icon") + "/ic_tim.png'/>";
        if(thisAddCheckbox != mediaDownloader) {
            thisMsInfoString += mediaDownloader;
        }
        if(isSet(tm.GeoData)) {
            thisMsInfoString += "<a href='" + getBasePath("old") + "/OutMap.php?Title=" + tm.File + "&Point=" + tm.GeoData + "' target='photoGeo'>" +
                    "<img class='th_icon' src='" + getBasePath("icon") + "/ic_gps.png'/></a>";
        }
        thisMsInfoString += "<div class='UPopO'>" +
                "<input type='hidden' name='FilePath' value='/MediaServer" + tm.Path + "/" + tm.File + "'/>" +
                dbDipInfo +
                "</div></div>";
        if(isSet(tm.TrackListingASON)) {
            var subAson = (tm.TrackListingASON).replace("[[", "").replace("]]", "").split("],[");
            thisMsInfoString += "<div class='UPop'>" +
                    "<img class='th_icon' src='" + getBasePath("icon") + "/ic_lst.jpeg'/>" +
                    "<div class='UPopO'>";
            for (var i = 0; i < subAson.length; i++) {
                var tTrackArray = subAson[i].split(",");
                thisMsInfoString += tTrackArray[0] + ": " + tTrackArray[1] + "<br/>";
            }
            thisMsInfoString += "</div></div>";
        }
        thisMsInfoString += "</span></form>";
        fileTable += thisMsInfoString;
    });
    fileTable += "</div>";
    if(matchLimitHit === 0) {
        noticeMessage = "<div class='Notice'>" + hitCount + " results found!</div>";
    } else {
        noticeMessage = "<div class='Notice' style='background-color: red; color: white;'>Showing 250 of " + hitCount + " results!</div>";
    }
    if(checkMobile()) { fileTable += "<p>Space for player pop-up<p>More space!" + noticeMessage; }
    dojo.byId("ETSResults").innerHTML = fileTable;
    dojo.query(".PlaySong").connect("onchange", actOnPlayMedia);
}

function putSearchBox(target, msOverview, updateFlag) {
    var subTableData = "<div class='table'>" +
        "<div class='tr'><span class='td'>Records</span><span class='td'>" + msOverview.TotalRecords + "</span></div>" +
        "<div class='tr'><span class='td'>Plays</span><span class='td'>" + msOverview.PlayCount + "</span></div>" +
        "<div class='tr'><span class='td'>Hours</span><span class='td'>" + ((msOverview.TotalDurSec / 60) / 60).toFixed(1) + "</span></div>" +
        "<div class='tr'><span class='td'>Size GB</span><span class='td'>" + ((msOverview.TotalBlocks / 1024) / 1024).toFixed(1) + "</span></div>" +
        "</div>";
    var mainTableElement = "<div class='table'>" +
            "<div class='tr'>" +
            "<span class='td'><form id='GenreSelectForm'>" +
            "<select name='Genre' id='GenreSelect'>" +
            "<option value=''>Select...</option>" +
            mediaOpts() +
            "</select></form></span>" +
            "<span class='td'>" +
            "<div class='UPop'><img src='" + getBasePath("ui") + "/img/Icons/ic_lst.jpeg' class='th_icon'/>" +
            "<div class='UPopO'>" + subTableData + "</div></div></span>" + 
            "</div>";
    var liveSearchField = "<form class='tr'>" +
            "<span class='td'><input type='text' class='msSearchBox' name'MediaSearch' onKeyUp='searchAheadMediaServer(this.value)' /></span>" +
            "<span class='td'>" +
            "<div class='UPop'><img class='th_icon' src='" + getBasePath("ui") + "/img/Icons/ic_map.jpeg' />" +
            "<div class='UPopO'><storng>Searches on: </strong>" +
            "Media, Path, File, Description, ContentDate, AlbumArt, XTags, and TrackListingASON" +
            " from the Media Server database.</div>" +
            "</div></span></form>";
    var rData = mainTableElement + liveSearchField;
    if(!updateFlag) {
        dojo.byId(target).innerHTML = rData;
        var genreSelect = dojo.byId("GenreSelect");
        dojo.connect(genreSelect, "onchange", actOnNonMedia);
    }
}

function searchAheadMediaServer(value) {
    var noticeMessage;
    var matchLimitHit = 0;
    var hitCount = 0;
    if(value.length > 2) {
        var matchingRows = [];
        msIndex.forEach(function (sr) {
            if(
                (isSet(sr.File) && (sr.File).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.Path) && (sr.Path).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.Media) && (sr.Media).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.Description) && (sr.Description).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.ContentDate) && (sr.ContentDate).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.DateIndexed) && (sr.DateIndexed).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.AlbumArt) && (sr.AlbumArt).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.Artist) && (sr.Artist).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.XTags) && (sr.XTags).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.Resolution) && (sr.Resolution).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.TrackListingASON) && (sr.TrackListingASON).toLowerCase().includes(value.toLowerCase()))
            ) { 
                hitCount++;
                if(matchingRows.length < 249) {
                    matchingRows.push(sr);
                } else {
                   matchLimitHit = 1;
                }
            }
        });
        putFileResults(matchingRows, hitCount, matchLimitHit);    
    }
}

function setPlayMedia(formData) {
    aniPreload("on");
    formData.doWhat = "setPlayed";
    var xhArgs = {
        preventCache: true,
        url: getResource("MediaServer"),
        postData: formData,
        handleAs: "text",
        timeout: timeOutMilli,
        load: function(data) {
            aniPreload("off");
            getIndex("ETSSearch", true);
        },
        error: function(data, iostatus) {
            window.alert("xhrPost for SetPlayMedia FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
            aniPreload("off");
        }
    };
    dojo.xhrPost(xhArgs);
}
