/* 
by Anthony Stump
Created: 17 Apr 2018
 */

var tpSearchableData, tpMatcher;

function actOnDoXTag(event) {
    var target = "?";
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    var thisFormJson = dojo.formToJson(this.form);
    window.alert(thisFormJson);
}

function actOnTpSelect(event) {
    var target = "TPGalleryHolderPElement";
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    var thisFormJson = dojo.formToJson(this.form);
    window.alert(thisFormJson);
    generateGallery("tp", thisFormData.HashPath);
}

function populateImageTagUpdater(target, xTags, tpPic) {
    var imageFile = "" //?
    var rData = "<div class='table'>" +
            "<form class='tr' name='TPUpForm'>" +
            "<span class='td'>" + imageFile + "</span>" +
            "<span class='td'><div class='GPSPop'><input type='text' name='XTags' value='" + tpPic.XTags + "'/>" +
            "<div class='GPSPopO'>" +
            "<strong>Available:</strong><br/>";
    xTags.forEach(function (xt) {
        rData += "<strong>" + xTags.Tag + "</strong>: " + xTags.Description + ", ";
    });
    rData += "</div></div></span>" +
            "<span class='td'><input type='submit' id='DoXTag' name='XTagDo'/></span>" +
            "</form></div>";
    dojo.byId(target).innerHTML = rData;
    var doXTag = dojo.byId("DoXTag");
    dojo.connect(doXTag, "click", actOnDoXTag);
}

function populateGalleryHolder(tpIndexed, tpImgTotal) {
    var percentIndexed = ((tpIndexed.Files/tpImgTotal)*100).toFixed(2);
    var rData = "<em>" + tpIndexed.Files + " of " + tpImgTotal + " indexed. (" + percentIndexed + "% done)</em>" +
            "<p id='TPGalleryHolderPElement'></p>";
    dojo.byId("TPGalleryHolder").innerHTML = rData;
}

function populateQueueSizeHolder(tpQSize) {
    var rData = "Queue size: <strong>" + tpQSize.ToDo + "</strong>";
    dojo.byId("TPQueueSizeHolder").innerHTML = rData;
}

function populateSearchBox(xTags, tpImages) {
    var rData = "<form class='tr' id='TPSearchForm'>" +
            "<span class='td'><input type='text' class='TPSearchBox' name='TPSearchInput' onKeyUp='tpShowHint(this.value)'/></span>" +
            "<span class='td'><strong><div class='UPopNM'>Search" +
            "<div class='UPopNMO'>" +
            "<strong>Tags: </strong><br/>" +
            "<div class='table'>";
    xTags.forEach(function (xt) {
        var xtMatches = 0;
        tpImages.forEach(function (tpi) {
            if((tpi.XTags).includes(xt.Tag)) {
                xtMatches++;
            }
        });
        rData += "<div class='tr'>" +
                "<span class='td'>" + xTags.Tag + "</span>" +
                "<span class='td'>" + xTags.Description + "</span>" +
                "<span class='td'>" + xtMatches + "</span>" +
                "</div>";
    });
    rData += "</div></div></div></strong></span></form>";
    dojo.byId("TPSearchBoxHolder").innerHTML = rData;
}

function populateSearchPopup(searchableData) {
    if(!isSet(searchableData)) { searchableData = tpSearchableData; }
    var done, percentDone, fColor;
    var tpGallery = 0;
    var tpImageTotal = 0;
    var cols = [ "Do", "Image Set", "Count" ];
    var hosTable = "<div class='table HideOnSearch'><div class='tr'>";
    for (var i = 0; i < cols.length; i++) { hosTable += "<span class='td'><strong>" + cols[i] + "</strong></span>"; }
    searchableData.forEach(function (tpData) {
        tpMatcher.forEach(function (tpMatch) {
            if(tpMatch.HashPath === tpData.HashPath) {
                done = tpMatch.Indexed;
                percentDone = Math.round((done/tpData.FinalCount)*100);
            } else {
                done = 0;
                percentDone = 0;
            }
        });
        if(percentDone === 0) { fColor = "gray"; } else { fColor = autoColorScale(percentDone, 100, 0, null); }
        tpGallery++;
        tpImageTotal += (tpImageTotal + tpData.FinalCount);
        hosTable += "<form id='TPFormGallery' class='tr'>" +
                "<span class='td'>";
        if(tpData.OffDisc === 0) { hosTable += "<input type='radio' class='TPSelect' name='TPHash' value='" + tpData.HashPath + "'/>"; } else { rData += "N/A"; }
        hosTable += "</span>" +
                "<span class='td'><div class='UPop' style='color: " + fColor + ";'>" + tpData.ImageSet +
                "<div class='UPopO'>" +
                "<strong>Submitted: </strong>" + tpData.AddedOn + "<br/>" +
                "<strong>Hash Path: </strong>" + tpData.HashPath + "<br/>" +
                "<strong>Description: </strong>" + tpData.Description + "<br/>" + 
                "<strong>Tags: </strong> " + tpData.Tags + "<br/>" +
                "</div></div></span>" +
                "<span class='td'><div class='UPop'>" + tpData.FinalCount +
                "<div class='UPopO'>" +
                "<strong>Original: </strong>" + tpData.ImageCount + "<br/>" +
                "<strong>Indexed: </strong>" + done + "<br/>" +
                "</div></div></span>" +
                "</form>";
    });
    hosTable += "</div>";
    dojo.byId("TPSearchPopupHolder").innerHTML = hosTable;
    dojo.query(".TPSelect").connect("onchange", actOnTpSubmit);
}

function tpShowHint(value) {
    var noticeMessage;
    var matchLimitHit = 0;
    var hitCount = 0;
    if(value.length < 2) {
        var matchingRows = [];
        tpSearchableData.forEach(function (sr) {
            if(
                (isSet(sr.ImageSet) && (sr.ImageSet).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.Description) && (sr.Description).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.Tags) && (sr.Tags).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.Categories) && (sr.Categories).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.HashPath) && (sr.HashPath).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.AddedOn) && (sr.AddedOn).toLowerCase().includes(value.toLowerCase()))
            ) { 
                hitCount++;
                if(matchingRows.length < 99) {
                    matchingRows.push(sr);
                } else {
                   matchLimitHit = 1;
                }
            }
        });
        populateSearchPopup(matchingRows);
    }
}

function init() {
    
}

dojo.ready(init);