/* 
Created: 25 Mar 2018
Split off from Entertain.js: 10 Apr 2018
Split off from Games.js: 22 May 2018
Updated: 19 Aug 2018
 */

var ffxivQuests;

function actOnFfxivQuestDone(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    setFfxivQuestDone(thisFormData);
}

function displayGameFf14d() {
    var target = "ETGFF14D";
    getGameFf14d(target);
    $("#ETGHours").hide();
    $("#ETGFF14Q").hide();
    $("#ETGIndex").hide();
}

function displayGameFf14q() {
    var target = "ETGFF14Q";
    getGameFf14q(target);
    $("#ETGHours").hide();
    $("#ETGFF14D").hide();
    $("#ETGIndex").hide();
}

function ffxivQuestHint(value) {
    if(value.length > 2) {
        var hitCount = 0;
        var matchLimitHit = 0;
        var matchingRows = [];
        ffxivQuests.forEach(function (sr) {
            if(
                (isSet(sr.QuestOrder) && (sr.QuestOrder).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.Name) && (sr.Name).toLowerCase().includes(value.toLowerCase()))
            ) { 
                hitCount++;
                if(matchingRows.length < 249) {
                    matchingRows.push(sr);
                } else {
                   matchLimitHit = 1;
                }
            }
        });
        putFfxivQuestList("questList", matchingRows);    
    }
}

function putFfxivQuestSearchBox(target) {
    var rData = "<div class='table'>" +
        "<form class='tr' id='ffxivSearchForm'>" +
        "<span class='td'><input type='text' id='SearchBrix' name='StationSearchField' onkeyup='ffxivQuestHint(this.value)' /></span>" +
        "<span class='td'><strong>Search</strong></span>" +
        "</form>" +
        "</div>";
    dojo.byId(target).innerHTML = rData;
}

function getGameFf14d(target) {
    aniPreload("on");
    var thePostData = { "doWhat": "getFfxivDungeons" };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Entertainment"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    aniPreload("off");
                    putFfxivDungeonList(target, data);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for FFXIV Dungeons FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function getGameFf14q(target) {
    var timeout = 2 * 60 * 1000;
    aniPreload("on");
    var thePostData = { "doWhat": "EntertainmentFfxivQuestsByDate" };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Chart"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    aniPreload("off");
                    getGameFf14qData(target);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for FFXIV Quests by Day FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
    setTimeout(function () { getGameFf14q(target); }, timeout);
}

function getGaemFf14dData(target) {
    // Build!
}

function getGameFf14qData(target) {
    getDivLoadingMessage(target);
    aniPreload("on");
    var thePostData = { "doWhat": "getFfxivQuests" };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Entertainment"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    aniPreload("off");
                    ffxivQuests = data;
                    putFfxivQuests(target, data);
                    $("#"+target).show();
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for FFXIV Quests FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function putFfxivDungeonList(target, dungeonData) {
    var dCols = [ "Name", "Level", "Rewards" ];
    var rData = "<div class='table'><div class='tr'>";
    for (var i = 0; i < dCols.length; i++) { rData += "<span class='td'><strong>" + dCols[i] + "</strong></span>"; }
    rData += "</div>";
    dungeonData.forEach(function (ff14d) {
        var tomes = "";
        if(ff14d.TomesPoetics !== 0) {
            tomes += " <div class='UPop'><img class='th_icon' src='" + getBasePath("image") + "/ffxiv/Poetics_icon1.png'/>" +
                    "<div class='UPopO'>Poetics: " + ff14d.TomesPoetics + "</div></div>";
        }
        if(ff14d.TomesCreation !== 0) {
            tomes += " <div class='UPop'><img class='th_icon' src='" + getBasePath("image") + "/ffxiv/Creation_icon1.png'/>" +
                    "<div class='UPopO'>Creation: " + ff14d.TomesCreation + "</div></div>";
        }
        if(ff14d.TomesMendacity !== 0) {
            tomes += " <div class='UPop'><img class='th_icon' src='" + getBasePath("image") + "/ffxiv/Mendacity.png'/>" +
                    "<div class='UPopO'>Mendacity: " + ff14d.TomesMendacity + "</div></div>";
        }
        rData += "<div class='tr'>" +
                "<span class='td'><div class='UPop'>" + ff14d.Name +
                "<div class='UPopO'>" +
                "<strong>Players</strong>: " + ff14d.PartySize + "<br/>" +
                "<strong>Unlocking</strong>: " + ff14d.UnlockQuest + "<br/>" +
                "<strong>Roulette</strong>: " + ff14d.Roulette + "<br/>" +
                "</div></div></span>" +
                "<span class='td'><div class='UPop'>" + ff14d.MinLevel +
                "<div class='UPopO'>" +
                "<strong>Min. ILEV</strong>: " + ff14d.MinItemLevel + "<br/>" +
                "<strong>Synch ILEV</strong>: " + ff14d.MaxItemLevel + "<br/>" +
                "</div></div></span>" +
                "<span class='td'>" + tomes + "</span>" +
                "</div>";
    });
    rData += "</div>";
    dojo.byId(target).innerHTML = rData;    
}

function putFfxivQuestList(target, questData) {
    var qNum = 0;
    var qCols = [ "Up", "Name", "LV" ];
    var rData = "<div class='table'><div class='tr'>";
    for (var i = 0; i < qCols.length; i++) { rData += "<span class='td'><strong>" + qCols[i] + "</strong></span>"; }
    rData += "</div>";
    questData.forEach(function (ff14q) {
        if(qNum <= 249) {
            var qComplete = "No";
            var fontColor = "White";
            var tdsStyle = "style='color: " + fontColor + ";'";
            var updateCheckbox = "<input class='ffxivQuestDone' type='checkbox' name='qUpdate' value='" + ff14q.QuestOrder + "'/></span>";
            if(ff14q.Completed === 1) {
                qComplete = "Yes";
                fontColor = "Gray";
                updateCheckbox = "NA";
            }
            rData += "<form class='tr' id='ffxivQuestSubmitForm'>" +
                    "<span class='td' " + tdsStyle + ">" + updateCheckbox + "</span>" +
                    "<span class='td' " + tdsStyle + "><div class='UPop'>" + ff14q.Name +
                    "<div class='UPopO'>" +
                    "Zone: " + ff14q.Zone + "<br/>" +
                    "Coords: X" + ff14q.CoordX + ", Y" + ff14q.CoordY + "<br/>";
            if(isSet(ff14q.GivingNPC)) { rData += "Giving NPC: " + ff14q.GivingNPC + "<br/>"; }
            if(isSet(ff14q.Classes)) { rData += "Class/Jobs: " + ff14q.Classes + "<br/>"; }
            if(isSet(ff14q.Version)) { rData += "Patch Level: " + ff14q.Version + "<br/>"; }
            if(isSet(ff14q.OrigCompDate)) { rData += "1st Completed: " + ff14q.OrigCompDate + "<br/>"; }
            rData += "Quest Order: " + ff14q.QuestOrder + "<br/>" +
                    "</div></div></span>" +
                    "<span class='td' " + tdsStyle + "><div class='UPop'>" + ff14q.MinLevel +
                    "<div class='UPopO'>";
            if(isSet(ff14q.Exp)) { "XP: " + ff14q.Exp + "<br/>"; }
            if(isSet(ff14q.Gil)) { "Gil: " + ff14q.Gil + "<br/>"; }
            if(isSet(ff14q.Seals)) { "Seals: " + ff14q.Seals + "<br/>"; }
            rData += "</div></div></span>" +
                    "</form>";
        }
        qNum++;
    });
    rData += "</div>";
    dojo.byId(target).innerHTML = rData;
    dojo.query(".ffxivQuestDone").connect("onchange", actOnFfxivQuestDone);
}

function putFfxivQuests(target, questData) {
    var charProfLink = "https://na.finalfantasyxiv.com/loadstone/character/20659030";
    var charProfLink2 = "https://na.finalfantasyxiv.com/lodestone/character/20659030/";
    var qCount = ffxivQuests.length;
    var compCounter = 0;
    ffxivQuests.forEach(function (ffxq) {
        if(ffxq.Completed === 1) { compCounter++; }
    });
    var rData = "<a href='" + charProfLink2 + "' target='new'>Foodle Faddle</a><br/>" +
            "<strong>Maps</strong>:" +
            " [<a href='" + getBasePath("image") + "/ffxiv/LaNoscea.jpg' target='ffxivMap'>LAN</a>]" +
            " [<a href='" + getBasePath("image") + "/ffxiv/Shroud.jpg' target='ffxivMap'>SHR</a>]" +
            " [<a href='" + getBasePath("image") + "/ffxiv/Thanalan.jpg' target='ffxivMap'>THA</a>]<br/>" +
            "<strong>Screens</strong>:" +
            " [<a href='" + getBasePath("image") + "/ffxiv/Arc33.jpg' target='ffxivMap'>ARC-33</a>]" +
            " [<a href='" + getBasePath("image") + "/ffxiv/Arc36.jpg' target='ffxivMap'>ARC-36</a>]" +
            " [<a href='" + getBasePath("image") + "/ffxiv/Min1.jpg' target='ffxivMap'>MIN-1</a>]" +
            " [<a href='" + getBasePath("image") + "/ffxiv/Min13.jpg' target='ffxivMap'>MIN-13</a>]" +
            "<h3>Quests</h3><strong>Indexed quests: " + qCount + "<br/>" +
            "Completed quests: " + compCounter + " (" + ((compCounter/qCount)*100).toFixed(2) + "%)<br/>" +
            "<a href='" + doCh("j", "ffxivQuestsByDay", null) + "' target='qCh'><img class='ch_small' src='" + doCh("j", "ffxivQuestsByDay", "th") + "'/></a>" +
            "<p><div id='qSearchHolder'></div>" +
            "<p><div id='questList'></div>";
    dojo.byId(target).innerHTML = rData;
    putFfxivQuestSearchBox("qSearchHolder");
    putFfxivQuestList("questList", questData);
}

function setFfxivQuestDone(formData) {
    var target = "ETGFF14Q";
    aniPreload("on");
    var thePostData = {
        "doWhat": "setFfxivQuestDone",
        "questOrder": formData.qUpdate
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Entertainment"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    aniPreload("off");
                    showNotice("Quest " + formData.qUpdate + " complete!");
                    getGameFf14q(target);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request to set Quest Complete FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}
