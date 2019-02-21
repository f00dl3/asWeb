/* 
by Anthony Stump
Created: 17 Apr 2018
Updated: 21 Feb 2019
 */

var maxListing = 250;
var tpSearchableData, tpIndexedImages, tpMatcher;

function actOnDoXTag(event) {
    var target = "?";
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    var thisFormJson = dojo.formToJson(this.form);
}

function actOnTpSelect(event) {
    var target = "TPGalleryHolderPElement";
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    switch(thisFormData.TPIndexedMS) {
    	case "1": initGallery("tpi", thisFormData.TPHash, thisFormData.TPGlob); break;
    	case "0": default: initGallery("tp", thisFormData.TPHash, thisFormData.TPGlob); break;
    }
}

function actOnTpiUpdate(event) {
	dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    var thisFormJson = dojo.formToJson(this.form);
    putUpdateTpi(thisFormData);
}

function checkTpi(ffn, iRes, fileSizeKB, hashPath) {
    aniPreload("on");
    var thePostData = { "doWhat": "checkTpi", "ffn": ffn };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("TP"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                	rCount = data[0].Count;
                	if(rCount === 1) {
                		//console.log(ffn + " is indexed!");
        				require(["dojo/dom-style"], function(domStyle){
                				domStyle.set(ffn, "border", "2px solid green");
            			});
        				genUpdateTpiForm(data[0], iRes, ffn, fileSizeKB, hashPath);
                	} else {
                		console.log(ffn + " is not indexed!");
        				genUpdateTpiForm(null, iRes, ffn, fileSizeKB, hashPath);
                	}
                    aniPreload("off");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request to check TPI status failed!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function genLayout() {
    var rData = "<div class='table'>" +
                "<div class='tr'>";
    if(!checkMobile()) {
        rData = 
                "<span class='td' style='width: 25%;'>" +
                "<div id='TPSearchPopupHolder'></div>" +
                "</span>" +
                "<span class='td' style='width: 75%;'>";
    } else {
        rData = "<span class='td'>" +
                "<div class='UPop'><button class='UButton'>Selector...</button>" +
                "<div class='UPopO'>" +
                "<div id='TPSearchPopupHolder'></div>" +
                "</div></div><br/>";
    }
    rData += "<div id='TPGalleryHolder'></div>";
                "</span>" +
                "</div>";
    dojo.byId("TPLayoutHolder").innerHTML = rData;
    getSearchableData();
    getQueueSize();
    populateGalleryHolder();
}

function genUpdateTpiForm(existingData, iRes, ffn, fileSizeKB, hashPath) {
	var existingDescription = "";
	var existingTags = "";
	if(existingData && isSet(existingData.Description)) { existingDescription = existingData.Description; }
	if(existingData && isSet(existingData.XTags)) { existingTags = existingData.XTags; }
	console.log(iRes + " " + ffn + " " + fileSizeKB);
	var fileSizeKB_int = parseFloat(fileSizeKB).toFixed(0);
	rData = "<form>" +
		"<strong>Desc: </strong><textarea name='TpiDesc' rows='4' columns='50'>" + existingDescription + "</textarea><br/>" +
		"<strong>Tags: </strong><input type='text' name='TpiTags' style='width: 256px;' value='" + existingTags + "'></input><br/>" +
		"<input type='hidden' name='TpiSize' value='" + fileSizeKB_int + "'></input>" +
		"<input type='hidden' name='TpiFile' value='" + ffn + "'></input>" +
		"<input type='hidden' name='TPiHash' value='" + hashPath + "'></input>" +
		"<input type='hidden' name='TpiRes' value='" + iRes + "'></input><br/>" +
		"<button class='UButton tpSubmit' id='MakeUpdates' type='submit'>Add TPI</button>";
		"</form>";
	dojo.byId("crossDataHolder_" + ffn).innerHTML = rData;
	//var updateTpiForm = dojo.byId("UpdateTpiForm_" + ffn);
    dojo.query(".tpSubmit").connect("click", actOnTpiUpdate);
}

function getSearchableData() {
    aniPreload("on");
    var thePostData = { "doWhat": "getTpIndex" };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("TP"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    tpSearchableData = data.Searchable;
                    tpIndexedImages = data.IndexedImages;
                    populateSearchBox();
                    populateSearchPopup(data.Searchable);
                    aniPreload("off");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for TP Index FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function getQueueSize() {
    getDivLoadingMessage("TPSearchBoxHolder");
    aniPreload("on");
    var thePostData = { "doWhat": "getQueueSizeCombined" };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("TP"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    var regularQueue = data.regular.length;
                    var archiveQueue = data.archive.length;
                    var combinedLength = regularQueue + archiveQueue;
                    populateQueueSizeHolder(combinedLength);
                    aniPreload("off");
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for Queue Size FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function populateImageTagUpdater(target, xTags, tpPic) {
    var imageFile = ""; //?
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

function populateGalleryHolder(/* tpIndexed, tpImgTotal */) {
    /* var percentIndexed = ((tpIndexed.Files/tpImgTotal)*100).toFixed(2); */
    var rData = /* "<em>" + tpIndexed.Files + " of " + tpImgTotal + " indexed. (" + percentIndexed + "% done)</em>" + */
            "<p id='GalleryHolderInside'></p>";
    dojo.byId("TPGalleryHolder").innerHTML = rData;
}

function populateQueueSizeHolder(tpQSize) {
    var rData = "Queue size: <strong>" + tpQSize + "</strong>";
    dojo.byId("TPQueueSizeHolder").innerHTML = rData;
}

function populateSearchBox() {
    var rData = "<form class='tr' id='TPSearchForm'>" +
            "<span class='td'><input type='text' class='TPSearchBox' name='TPSearchInput' onKeyUp='tpShowHint(this.value)'/></span>" +
            "<span class='td'><strong>Search</strong></span></form>";
    dojo.byId("TPSearchBoxHolder").innerHTML = rData;
}

function populateSearchPopup(searchableData) {
    var done, percentDone, fColor;
    var tpGallery = 0;
    var tpImageTotal = 0;
    var cols = [ "Do", "Image Set", "Count" ];
    var hosTable = "<div class='table HideOnSearch'><div class='tr'>";
    for (var i = 0; i < cols.length; i++) { hosTable += "<span class='td'><strong>" + cols[i] + "</strong></span>"; }
    hosTable += "</div>";
    var tpGalleryResults = searchableData.length;
    searchableData.forEach(function (tpData) {
        tpGallery++;
        if(tpGallery < maxListing) {
            /* tpMatcher.forEach(function (tpMatch) {
                if(tpMatch.HashPath === tpData.HashPath) {
                    done = tpMatch.Indexed;
                    percentDone = Math.round((done/tpData.FinalCount)*100);
                } else {
                    done = 0;
                    percentDone = 0;
                }
            }); */
            if(percentDone === 0) { fColor = "gray"; } else { fColor = autoColorScale(percentDone, 100, 0, null); }
            tpImageTotal += (tpImageTotal + tpData.FinalCount);
            hosTable += "<form id='TPFormGallery' class='tr'>" +
                    "<span class='td'>";
            if(tpData.OffDisc === 0) {
                hosTable += "<input type='radio' class='TPSelect' name='TPHash' value='" + tpData.HashPath + "'/>" +
                        "<input type='hidden' name='TPGlob' value='" + tpData.Glob + "'/>" +
                        "<input type='hidden' name='TPIndexedMS' value='" + tpData.MSIndexed + "'/>";
            } else {
                hosTable += "N/A";
            }
            hosTable += "</span>" +
                    "<span class='td'>" +
                    "<div class='UPop' style='color: " + fColor + ";'>" + tpData.ImageSet;
            if(!checkMobile()) {
                hosTable += "<div class='UPopO'>" +
                        "<strong>Submitted: </strong>" + tpData.AddedOn + "<br/>" +
                        "<strong>Hash Path: </strong>" + tpData.HashPath + "<br/>" +
                        "<strong>Description: </strong>" + tpData.Description + "<br/>" + 
                        "<strong>Tags: </strong> " + tpData.Tags + "<br/>" +
                        "</div>";
            }
            hosTable += "</div></span>" +
                    "<span class='td'><div class='UPop'>" + tpData.FinalCount;
            if(!checkMobile()) {
                hosTable += "<div class='UPopO'>" +
                        "<strong>Original: </strong>" + tpData.ImageCount + "<br/>" +
                        "<strong>Indexed: </strong>" + done + "<br/>" +
                        "</div>";
            }
            hosTable += "</div></span>" +
                        "</form>";
        }
    });
    if(tpGallery < maxListing) { showNotice(tpGallery + " results returned!") } else { showNotice("Displaying " + maxListing + " of " + tpGalleryResults + " results!"); }
    hosTable += "</div>";
    dojo.byId("TPSearchPopupHolder").innerHTML = hosTable;
    dojo.query(".TPSelect").connect("onchange", actOnTpSelect);
}

function putUpdateTpi(formData) {
    aniPreload("on");
    formData.doWhat = "setTpi";
    var xhArgs = {
        preventCache: true,
        url: getResource("TP"),
        postData: formData,
        handleAs: "text",
        timeout: timeOutMilli,
        load: function(data) {
            showNotice("Updated TPI data!");
            aniPreload("off");
        },
        error: function(data, iostatus) {
            window.alert("xhrPost for UpdateTpi FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
            aniPreload("off");
        }
    };
    dojo.xhrPost(xhArgs);
}

function tpShowHint(value) {
    var matchLimitHit = 0;
    var hitCount = 0;
    if(value.length > 2) {
        console.log(value);
        var matchingRows = [];
        tpSearchableData.forEach(function (sr) {
            if(
                (isSet(sr.ImageSet) && (sr.ImageSet).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.Description) && (sr.Description).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.Tags) && (sr.Tags).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.Categories) && (sr.Categories).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.HashPath) && (sr.HashPath).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.AddedOn) && (sr.AddedOn).includes(value.toLowerCase()))
            ) { 
                hitCount++;
                if(matchingRows.length < maxListing) {
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
	genLayout();
}

dojo.ready(init);