/* 
by Anthony Stump
FBook.js Created: 23 Mar 2018
FBook/Assets.js Split: 4 Apr 2018
Updated: 15 Apr 2018
 */

function actOnAssetUpdate(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    setAssetUpdate(thisFormData);
}

function actOnDecorToolsUpdate(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    setDecorToolsUpdate(thisFormData);
}

function displayAssets() {
    var target = "FBAsset";
    getAssetData(target);
}

function getAssetData(target) {
    if(isSet(target)) { getDivLoadingMessage(target); }
    aniPreload("on");
    var thePostData = "doWhat=getAssetData";
    var xhArgs = {
        preventCache: true,
        url: getResource("Finance"),
        postData: thePostData,
        handleAs: "json",
        timeout: timeOutMilli,
        load: function (data) {
            putAssets(
                    data.qMerged[0],
                    data.bGames,
                    data.books,
                    data.dTools,
                    data.licenses,
                    data.assets
            );
            aniPreload("off");
            if(isSet(target)) { $("#" + target).toggle(); }
        },
        error: function (data, iostatus) {
            aniPreload("off");
            window.alert("xhrGet for FinanceData FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
        }
    };
    dojo.xhrPost(xhArgs);
}

function onlineAssetSearch(searchTerms) {
    var onlineSearchPrefix = "https://www.google.com/search?q=eBay+";
    return onlineSearchPrefix + searchTerms;
}

function putAssets(qMerged, bGames, books, dTools, licenses, assets) {
    var abgCols = ["Title", "Quantity"];
    var dtCols = ["Description", "UD", "Quantity", "Location", "Checked"];
    var liCols = ["Title", "Type"];
    var asCols = ["Description", "CAT", "UD", "Value", "Checked", "Notes", "DT"];
    var dtCounter = 1;
    var assCounter = 0;
    var rData = "<div id='FBAssetInner'><h3>Asset Tracker</h3>";
    var abgBubble = "<div class='UBox'>BGames<br/><span>" + qMerged.qBGames + "</span><div class='UBoxO'><table><thead><tr>";
    for (var i = 0; i < abgCols.lenght; i++) {
        abgBubble += "<th>" + abgCols[i] + "</th>";
    }
    abgBubble += "</thead><tbody>";
    bGames.forEach(function (bg) {
        abgBubble += "<tr><td>" + bg.Title + "</td><td>" + bg.Quantity + "</td></tr>";
    });
    abgBubble += "</tbody></table></div></div>";
    var abkBubble = "<div class='UBox'>Books<br/><span>" + qMerged.qBooks + "</span><div class='UBoxO'><table><thead><tr>";
    for (var i = 0; i < abgCols.length; i++) {
        abkBubble += "<th>" + abgCols[i] + "</th>";
    }
    abkBubble += "</thead><tbody>";
    books.forEach(function (bk) {
        abkBubble += "<tr><td>" + bk.Title + "</td><td>" + bk.Quantity + "</td></tr>";
    });
    abkBubble += "</tbody></table></div></div>";
    var adtBubble = "<div class='UBox'>DecTools<br/><span>" + qMerged.qDTools + "</span><div class='UBoxO'>" +
            "<form id='DTUpdateForm'>" +
            "<div class='table'><div class='tr'>";
    for (var i = 0; i < dtCols.length; i++) {
        adtBubble += "<span class='td'><strong>" + dtCols[i] + "</strong></span>";
    }
    adtBubble += "</div>";
    dTools.forEach(function (dt) {
        var dtAge = dt.Checked;
        var cDtAge = checkTransactionAge(dtAge);
        adtBubble += "<form class='tr dtUpdateForm'><input type='hidden' name='DTID' value='" + dtCounter + "' />" +
                "<span class='td'><input type='hidden' name='DTDescription' value='" + dt.Description + "' />" + dt.Description + "</span>" +
                "<span class='td'><input type='checkbox' class='Check2UpdateDT' name='DTSetUpdate' value='Yes' /></span>" +
                "<span class='td'><input type='number' name='DTQuantity' value='" + dt.Quantity + "' style='width: 30px;' /></span>" +
                "<span class='td'><input type='text' name='DTLocation' value='" + dt.Location + "' style='width: 66px;' /></span>" +
                "<span class='td " + cDtAge + "'>" + dtAge + "</span>" +
                "</form>";
        dtCounter++;
    });
    adtBubble += "</div></div></div>";
    var aliBundle = "<div class='UBox'>Licenses<br/><span>" + qMerged.qLicenses + "</span>" +
            "<div class='UBoxO'><table><thead><tr>";
    for (var i = 0; i < liCols.length; i++) {
        aliBundle += "<th>" + liCols[i] + "</th>";
    }
    aliBundle += "</thead><tbody>";
    licenses.forEach(function (lic) {
        aliBundle += "<tr>" +
                "<td>" + lic.Title + "</td>" +
                "<td>" + lic.Type + "</td>" +
                "</tr>";
    });
    aliBundle += "</tbody></table></div></div>";
    var ameBundle = "<div class='UBox'>Media<br/><span>" + qMerged.qMedia + "</span></div>";
    var asTable = "<div class='table'><div class='tr'>";
    for (var i = 0; i < asCols.length; i++) {
        asTable += "<span class='td'><strong>" + asCols[i] + "</strong></span>";
    }
    asTable += "</div>";
    assets.forEach(function (ass) {
        assCounter++;
        var cAge = checkTransactionAge(ass.Checked);
        var ifDetails, ifDetailsComb;
        ifDetails = ifDetailsComb = "";
        var upRelatedCheckbox = "<input type='checkbox' class='Check2UpdateAssets' name='AssetSetUpdate' />";
        if (isSet(ass.Serial)) {
            ifDetailsComb += "<strong>Serial: </strong>" + ass.Serial + "<br/>";
        }
        if (isSet(ass.UPC)) {
            ifDetailsComb += "<strong>UPC: </strong>" + ass.UPC + "<br/>";
        }
        if (isSet(ass.Related)) {
            ifDetailsComb += "<strong>Related: </strong>" + ass.Related + "<br/>";
            upRelatedCheckbox = "N/A";
        }
        if (isSet(ass.Location)) {
            ifDetailsComb += "<strong>Location: </strong>" + ass.Location + "<br/>";
        }
        if (isSet(ifDetailsComb)) {
            ifDetails = "<div class='UPop'><img class='th_icon' src='" + getBasePath("icon") + "/ic_lst.jpeg'/>" +
                    "<div class='UPopO'>" + ifDetailsComb + "</div></div>";
        }
        var assUpForm = "<form class='tr auFormTr' id='AssetUpdateForm'>" +
                "<input type='hidden' name='AssetID' value='" + assCounter + "' />" +
                "<span class='td'>" +
                "<input type='hidden' name='AssetDescription' value='" + ass.Description + "'/>" +
                "<a href='" + onlineAssetSearch(ass.Description) + "' target='_new_AssetSearch' />" +
                ass.Description + "</a></span>" +
                "<span class='td'>" + ass.Type + " - " + ass.Category + "</span>" +
                "<span class='td'>" + upRelatedCheckbox + "</span>" +
                "<span class='td'><input type='number' name='AssetValue' value='" + ass.Value + "' style='width: 75px;' /></span>" +
                "<span class='" + cAge + "'>" + ass.Checked + "</span>" +
                "<span class='td'><input type='text' name='AssetNotes' value='" + ass.Notes + "' style='width: 140px;' /></span>" +
                "<span class='td'>" + ifDetails + "</span>" +
                "</form>";
        asTable += assUpForm;
    });
    rData += abgBubble + abkBubble + adtBubble + aliBundle + ameBundle + "<p>" +
            asTable + "</div></div><p><em>Space intentionally left blank for pop-over</em>";
    dojo.byId("FBAsset").innerHTML = rData;
    dojo.query(".Check2UpdateAssets").connect("onchange", actOnAssetUpdate);
    dojo.query(".Check2UpdateDT").connect("onchange", actOnDecorToolsUpdate);
}

function setAssetUpdate(formData) {
    aniPreload("on");
    formData.doWhat = "putAssetTrackUpdate";
    var xhArgs = {
        preventCache: true,
        url: getResource("Finance"),
        postData: formData,
        handleAs: "text",
        timeout: timeOutMilli,
        load: function (data) {
            showNotice(formData.AssetDescription + " updated!");
            getAssetData();
            getOverviewData();
            aniPreload("off");
        },
        error: function (data, iostatus) { 
            aniPreload("off");
            window.alert("xhrPost for AssetTrackUpdate FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
        }
    };
    dojo.xhrPost(xhArgs);
}

function setDecorToolsUpdate(formData) {
    aniPreload("on");
    formData.doWhat = "putDecorToolsUpdate";
    var xhArgs = {
        preventCache: true,
        url: getResource("Finance"),
        postData: formData,
        handleAs: "text",
        timeout: timeOutMilli,
        load: function (data) {
            showNotice(formData.AssetDescription + " updated!");
            getAssetData();
            getOverviewData();
            aniPreload("off");
        },
        error: function (data, iostatus) { 
            aniPreload("off");
            window.alert("xhrPost for DecorToolsUpdate FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
        }
    };
    dojo.xhrPost(xhArgs);
}