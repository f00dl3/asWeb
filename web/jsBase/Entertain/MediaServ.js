/* 
by Anthony Stump
Created: 19 Mar 2018
Updated: 13 Apr 2018
 */

function getMediaOpts() {
    var tElement = "";
    var hMediaOpts = [ "AO DBX", "AO TP" ];
    var mediaOpts = [
        "Photos",
        "Weather",
        "ZX Goosebumps",
        "ZX Power Rangers",
        "ZX StarTrek",
        "ZX X-Files"
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
	var cPlays;
	switch(pCount) {
		case 0: cPlays = ""; break;
		case 1: cPlays = "color: #999999;"; break;
		case 2: cPlays = "color: #33ffff;"; break;
		case 3: cPlays = "color: #0099cc;"; break;
		case 4: cPlays = "color: #0066ff;"; break;
		case (pCount >= 5): cPlays = "color: #ffff00;"; break;
		default: cPlays = ""; break;
	}
	return cPlays;
}

function playMediaFile(lastPlayedQuery, whatFile, fileExt) {
    var mpo, mediaMime, mediaType;
    mpo = mediaMime = "";
    mediaType = "audio";
    if(checkMobile()) { mpo += "<div class='PlayPop'>" } else { mpo += "<div>"; }
    switch(mediaType) { // fileExt
        case "audio": // mp3
            mediaMime = "audio/mpeg";
            mpo += "<audio controls autoplay loop>" +
                    "<source src='" + whatFile + "' type='" + mediaMime + "'>" +
                    "</audio>";
            break;
    }
    mpo += "</div>";
    dojo.byId("PlayerHolder").innerHTML = mpo;
}

function populateDbx(dbxData) {
    var dbxDoGif, dbxLocation;
    dbxDoGif = dbxLocation = "";
    var dbxContent = "<h3>DBX Index</h3>" +
            "<div class='table'>" +
            "<div class='tr'>" +
            "<div clsas='th'>ZIP</div>" +
            "<div class='th'>File</div>" +
            "</div>";
    dbxData.forEach(function (tDbx) {
        var dAlbumArt = (tDbx.AlbumArt).substr((tDbx.AlbumArt).length - 4);
        if(isSet(tDbx.GIFVer)) {
            dbxDoGif = "<img src='" + getBasePath("tomcatOld") + "/AlbumArt/" + dbxData.AlbumArt + ".pnx' class='th_large' />";
        } else { dbxDoGif = "Review first!"; }
        if(tDbx.Burned === 1) {
            dbxLocation = tDbx.Media + " (" + tDbx.BDate + ")";
        } else { dbxLocation = "Local"; }
        var tdo = "<div class='tr'>" +
                "<span class='td'><div class='UPop'>" + dAlbumArt +
                "<div class='UPopO'>" + dbxDoGif + "</div></div></span>" +
                "<span class='td' style='" + mediaPlayColor(tDbx.PlayCount) + " width: 80%;'>" +
                "<div class='UPop'>" + tDbx.File + "<div class='UPopO'>" +
                "<strong>Size:</strong> " + (tDbx.Size/1024).toFixed(1) + " MB<br/>" +
                "<strong>Duration:</strong> " + (tDbx.DurSec/60).toFixed(1) + " min<br/>" +
                "<strong>Location:</strong> " + dbxLocation + "<br/>";
        if(isSet(tDbx.Artist)) { tdo += "<strong>Source/Artist:</strong> " + tDbx.Artist + "<br/>"; }
        if(isSet(tDbx.XTags)) { tdo += "<strong>Tags:</strong> " + tDbx.XTags + "<br/>"; }
        if(isSet(tDbx.Description)) { tdo += tDbx.Description + "<br/>"; }
        tdo += "</div></div></span>" +
                "</div>";
        dbxContent += tdo;
    });
    dbxContent += "</div>";
    return dbxContent;
}

function populateGoosebumps(gbQ) {
    var rData = "<h3>Goosebumps Books</h3>" +
            "<div class='table'>" +
            "<div class='tr'>" +
            "<span class='th'>Art</span>" +
            "<span class='th'>Code</span>" + 
            "<span class='th'>Title</span>" +
            "<span class='th>>PDF</span>" +
            "</div>";
    gbQ.forEach(function (gbData) {
        var imageLocation = getBasePath("tomcatOld") + "/Goosebumps/Thumbs/" + gbQ.Code + "." + gbQ.CoverImageType;
        var imageThumbLocation = imageLocation.replace("/Thumbs", "");
        rData += "<div class='tr'>" +
                "<span class='td'><a href='" + imageLocation + "' target='new'><img src='" + imageThumbLocation + "'/></a></span>" +
                "<span class='td'>" + gbQ.Code + "</span>" +
                "<span class='td'><div class='UPop'>" + gbQ.Title +
                "<div class='UPopO'>" + gbQ.Plot + "</div></div></span>" +
                "<span class='td'><div class='UPop'>";
        if(gbQ.pdf === 1) { rData += "<a href='" + getBasePath("media") + "/Docs/Goosebumps/" + gbQ.Code + ".pdf' target='new'>"; } else { rData += "N/A"; }
        rData += "<div class='UPopO'>" +
                "Published: " + gbQ.PublishDate + "<br/>" +
                "Pages: " + gbQ.Pages + "<br/>" +
                "ISBN: " + gbQ.ISBN + "</div></div></span>" +
                "</div>";
    });
    rData += "</div></div>";
    return rData;
}

function putFileResults(msData) {
    var thumbSize, mediaCount, mediaCountA, items, itemsTable;
    mediaCount = mediaCountA = 1;
    items = "";
    itemsTable = [];
    if(checkMobile()) { thumbSize = "th_small"; } else { thumbSize = "th_sm_med"; }
    var fileTable = "<div class='table id='FileTable'>" +
            "<div class='tr'>" +
            "<span class='td'><img class='th_icon' src='" + getBasePath("icon") + "/ic_ply.png'/></span>" +
            "<span class='td'><strong>File</strong></span>" +
            "</div>";
    msData.forEach(function (tm) {
        var dbDipInfo, thisAddCheckbox, forceMediaType;
        dbDipInfo = thisAddCheckbox = "";
        var mediaType = (tm.File).substr((tm.File).length - 3).toUpperCase();
        items += (
               tm.Path + " " + tm.Media + " " + tm.File + " " +
               tm.DescriptionLimited + " " + tm.Artist + " " + tm.AlbumArt + " " +
               tm.XTags + " " + tm.TrackListingASON + " "
        ).toUppercase();
        if(isSet(tm.Description)) {
            if(isSet(tm.AlbumArt)) {
                if(
                    (tm.Path).substr(0, 4) === "/DBX" ||
                    tm.Path === "/Adult/Export" ||
                    tm.Path === "/Adult/Other") {
                    dbDipInfo += "<img class='" + thumbSize + "' src='" + getBasePath("tomcatOld") + "/AlbumArt/" + tm.AlbumArt + ".pnx'/><br/>";
                } else {
                    dbDipInfo += "<img class='" + thumbSize + "' src='" + getBasePath("tomcatOld") + "/AlbumArt/" + tm.AlbumArt + ".jpg'/><br/>";
                }
            }
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
                    thisAddCheckbox += "<a href='" + getBasePath("media") + tm.Path + "/" + tm.File + "' target='new'>" +
                            "<img class='arrow' src='" + getBasePath("icon") + "/ar_dn.gif' /></a>";
                    break;
                case "JPG":
                    if(isSet(tm.Archived)) {
                        thisAddCheckbox += "<a href='" + getBasePath("old") + "/Images/Memories/Archived/" + tm.Archived + ".war'>" +
                                "<img src='th_icon' src='" + getBasePath("icon") + "/ic_zip.png' /></a>";
                    } else {
                        var imageAttribs = tm.Resolution.split("x");
                        var imageWidth = imageAttribs[0];
                        var imageHeight = imageAttribs[1];
                        var imageWidthHeightAttribs = "IW=" + imageWidth + "&IH=" + imageHeight + "&";
                        if(tm.WarDeploy === 1) {
                            var thisYear = (tm.Path).substr((tm.Path).length - 2);
                            thisAddCheckbox += "<a href='" + getBasePath("old") + "/OutMap.php?Image=Gallery&" + imageWidthHeightAttribs +
                                    "PicPath=Tomcat/PicsL" + thisYear + "/full/" + tm.File + "' target='photoPop'>" +
                                    "<img class='th_icon' src='" + getBasePath("tomcatOld") + "/PicsL" + thisYear + "/thumb/" + tm.File + "'/>";
                        } else {
                            var thisYear = (tm.Path).substr((tm.Path).length - 4);
                            thisAddCheckbox += "<a href='" + getBasePath("old") + "/OutMap.php?Image=Gallery&" + imageWidthHeightAttribs +
                                    "PicPath=Images/Memories/" + thisYear + "/full/" + tm.File + "' target='photoPop'>" +
                                    "<img class='th_icon' src='" + getBasePath("old") + "/Images/Memories/" + thisYear + "/thumb/" + tm.File + "'/>";
                        }
                        thisAddCheckbox += "</a>";
                    }
                    break;
                default:
                    thisAddCheckbox += "<input class='PlaySong' type='checkbox' name='FilePlay[" + mediaCount + "]' value='Yes' />";
                    break;
            }
            thisAddCheckbox += "</div>";
        } else {
            thisAddCheckbox += "<div class='UPopNM'>" +
                    "<img class='th_icon' src='" + getBasePath("icon") + "/ic_lck.jpeg'/>" +
                    "<div class='UPopNMO'>" +
                    "Viewed: <input class='PlaySong' type='checkbox' name='FilePlay[" + mediaCount + "]' value='Yes'/>" +
                    "</div></div>";
        }
        var thisMsInfoString = "<form class='tr' id='SearchSongForm[" + mediaCount + "]'>" +
                "<span class='td' style='width: 10%;'>" + thisAddCheckbox + "</span>";
        if(isSet(tm.File)) {
            thisMsInfoString += "<span class='td' style='width: 80%; color: #ff3300;'>";
        } else {
            thisMsInfoString += "<span class='td' style='" + mediaPlayColor(tm.PlayCount) + " width=80%;'>";
        }
        if(mediaType === "MP4") { forceMediaType = "m4v"; } else { forceMediaType = mediaType; }
        thisMsInfoString += "<input type='hidden' name='MediaType[" + mediaCount + "]' value = '" + forceMediaType + "' />" +
                "<div class='UPop'>" +
                "<input type='hidden' name='FileName[" + mediaCount + "]' value='" + tm.File + "'/>" + tm.File +
                "<img class='th_th_icon' src='" + getBasePath("icon") + "/ic_tim.png'/>";
        if(isSet(tm.GeoData)) {
            thisMsInfoString += "<a href='" + getBasePath("old") + "/OutMap.php?Title=" + tm.File + "&Point=" + tm.GeoData + "' target='photoGeo'>" +
                    "<img class='th_icon' src='" + getBasePath("icon") + "/ic_gps.png'/></a>";
        }
        thisMsInfoString += "<div class='UPopO'>" +
                "<input type='hidden' name='FilePath[" + mediaCount + "]' value='/MediaServ" + tm.Path + "/" + tm.File + "'/>" +
                dbDipInfo +
                "</div></div>";
        if(isSet(tm.TrackListingASON)) {
            var trackListingArray = (tm.TrackListingASON).split("],[").substring(2, (tm.TrackListingASON).length - 2);
            thisMsInfoString += "<div class='UPop'>" +
                    "<img class='th_icon' src='" + getBasePath("icon") + "/ic_lst.jpeg'/>" +
                    "<div class='UPopO'>";
            trackListingArray.forEach(function (tTrack) {
               var tTrackArray = tTrack.split(",");
               thisMsInfoString += tTrackArray[0] + ": " + tTrackArray[1] + "<br/>";
            });
            thisMsInfoString += "</div></div>";
        }
        thisMsInfoString += "</span></form>";
        itemsTable.push(thisMsInfoString);
        fileTable += thisMsInfoString;
    });
    fileTable += "</div>";
    if(checkMobile()) { fileTable += "<p>Space for player pop-up<p>More space!"; }
    return fileTable;
}

function putSearchBox(msOverview) {
    var subTableData = "<div class='table'>" +
        "<div class='tr'><span class='td'>Records</span><span class='td'>" + msOverview.TotalRecords + "</div></div>" +
        "<div class='tr'><span class='td'>Plays</span><span class='td'>" + msOverview.TotalPlays + "</div></div>" +
        "<div class='tr'><span class='td'>Hours</span><span class='td'>" + ((msOverview.TotalDurSec / 60) / 60).toFixed(1) + "</span></div>" +
        "<div class='tr'><span class='td'>Size GB</span><span class='td'>" + ((msOverview.TotalBlocks / 1024) / 1024).toFixed(1) + "</span></div>";
    var mainTableElement = "<div class='table'>" +
            "<div class='tr'>" +
            "<span class='td'><form id='GenreSelectForm'><select name='Genre' id='GenreSelect'>" + getMediaOpts() + "</select></form></span>" +
            "<span class='td'>" +
            "<div class='UPop'><img src='" + getBasePath("ui") + "/img/Icons/ic_lst.jpeg' class='th_icon'/>" +
            "<div class='UPopO'>" + subTableData + "</div>" + 
            "</div>";
    var liveSearchField = "<form class='tr'>" +
            "<span class='td'><input type='text' class='msSearchBox' name'MediaSearch' onKeyUp='msShowHint(this.value)' /></span>" +
            "<span class='td'>" +
            "<div class='UPop'><img class='th_icon' src='" + getBasePath("ui") + "/img/Icons/ic_map.jpeg' />" +
            "<div class='UPopO'><storng>Searches on: </strong>" +
            "Media, Path, File, Description, ContentDate, AlbumArt, XTags, and TrackListingASON" +
            " from the Media Server database.</div>" +
            "</div></span></form>";
    var rData = mainTableElement + liveSearchField;
    dojo.byId("MSSearh").innerHTML = rData;
}

function searchAheadOld(dataArray, thisQuery, matchLimit) { 
    var itemMatchLimitHit, matchedItems, ti, dataBack, noticeBack, objectBack;
    itemMatchLimitHit = matchedItems = ti = 0;
    objectBack = {};
    var thisHint = "";
    if(thisQuery !== "") {
        var thisQueryLength = thisQuery.length;
        thisQuery.forEach(function (item) {
           if(dataArray.indexOf(thisQuery) >= 0) {
               matchedItems++;
               if(thisHint === "") {
                   thisHint = dataArray[ti];
               } else if (matchedItems > matchLimit) {
                   itemMatchLimitHit = 1;
                   thisHint = "";
               } else {
                   thisHint += dataArray[ti];
               }
           }
           ti++;
        });
    }
    if(isSet(thisHint)) {
        if(isSet(itemMatchLimitHit)) {
            noticeBack = "<div class='Notice' style='background-color: red; color: white;'>Showing " + matchLimit + " of " + matchedItems + " results!</div>";
        } else {
            noticeBack = "<div class='Notice'>" + matchedItems + " results found!</div>";
            dataBack = thisHint ;
        }
    } else {
        noticeBack = "</div><div class='Notice'>Unable to find anything!</div>";
    }
    objectBack.noticeBack = noticeBack;
    objectBack.dataBack = dataBack;
    return objectBack;
}

function initMediaServer() {
    getMediaOpts();
    putSearchBox();
}