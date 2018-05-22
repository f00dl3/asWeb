/* 
by Anthony Stump
Created: 25 Mar 2018
Split off from Entertain.js: 10 Apr 2018
Updated: 22 May 2018
 */

var ffxivQuests;

function actOnFfxivQuestDone(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    var thisFormDataJ = dojo.formToJson(this.form);
    setFfxivQuestDone(thisFormData);
}

function displayGames() {
    gameButtonListeners();
    $("#ETGameAll").toggle();
    $("#ETLego").hide();
    $("#ETReddit").hide();
    $("#ETCooking").hide();
    $("#ETStream").hide();
}

function displayGameFf14q() {
    var target = "ETGFF14Q";
    getGameFf14q(target);
    $("#ETGHours").hide();
    $("#ETGIndex").hide();
}

function displayGameHours() {
    var target = "ETGHours";
    getGameData(target);
    $("#ETGFF14Q").hide();
    $("#ETGIndex").hide();
}

function displayGameIndex() {
    var target = "ETGIndex";
    getGameIndex(target);
    $("#ETGFF14Q").hide();
    $("#ETGHours").hide();
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
                if(matchingRows.length < 24) {
                    matchingRows.push(sr);
                } else {
                   matchLimitHit = 1;
                }
            }
        });
        putFfxivQuestList("questList", matchingRows);    
    }
}

function getGameData(target) {
    getDivLoadingMessage(target);
    aniPreload("on");
    var thePostData = { "doWhat": "getGameData" };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Entertainment"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    aniPreload("off");
                    putGameHours(
                        target,
                        data.gameHoursTotal[0],
                        data.gameHoursLatest[0],
                        data.gameHours
                    );
                    $("#"+target).toggle();
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for Game Data FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function getGameFf14q(target) {
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
                    $("#"+target).toggle();
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for FFXIV Quests FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function getGameIndex(target) {
    getDivLoadingMessage(target);
    aniPreload("on");
    var thePostData = { "doWhat": "getGameIndex" };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Entertainment"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    aniPreload("off");
                    putGameIndex(target, data);
                    $("#"+target).toggle();
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for Game Index FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
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

function putFfxivQuestList(target, questData) {
    var qNum = 0;
    var qCols = [ "Up", "Name", "LV" ];
    var rData = "<div class='table'><div class='tr'>";
    for (var i = 0; i < qCols.length; i++) { rData += "<span class='td'><strong>" + qCols[i] + "</strong></span>"; }
    rData += "</div>";
    questData.forEach(function (ff14q) {
        if(qNum <= 24) {
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
    var qCount = ffxivQuests.length;
    var rData = "<a href='" + charProfLink + "' target='new'>Foodle Faddle</a>" +
            "<h3>Quests</h3><strong>Indexed quests: " + qCount + 
            "<p><div id='qSearchHolder'></div>" +
            "<p><div id='questList'></div>";
    dojo.byId(target).innerHTML = rData;
    putFfxivQuestSearchBox("qSearchHolder");
    putFfxivQuestList("questList", questData);
}

function putGameHours(target, gameHoursTotal, latest, gameHours) {
    var thCols = [ "Name", "Hours" ];
    var ghe, gheA;
    ghe = gheA = "<div class='table'><div class='tr'>";
    var totalHoursDiv = "<div class='UBox'>Total Hours<br/><span>" + (gameHoursTotal.TotalHours).toFixed(1) + "</span></div>";
    var rData = totalHoursDiv +
            "<strong>" +
            " <a href='" + getBasePath("downloads") + "/GameLauncher.zip'>Launcher (Linux/Bash)</a></strong>" +
            "<span class='UPopNM'>" +
            "<p>Most recent:</b>";    
    for (var i = 0; i < thCols.length; i++) {
        ghe += "<span class='td'><strong>" + thCols[i] + "</strong></span>";
        gheA += "<span class='td'><strong>" + thCols[i] + "</strong></span>";
    }
    ghe += "</div>";
    gheA += "</div>";
    var playing = "";
    if(latest.Active === 1) { playing = "<strong> (Playing!)</strong>)"; }
    ghe += "<div class='tr'>" +
            "<span class='td'>" + latest.Name + playing + "</span>" +
            "<span class='td'><div class='UPop'>" + latest.Hours +
            "<div class='UPopO'>Last played: " + latest.Last +
            "</div></div></span>" +
            "</div>";
    rData += ghe + "<div class='UPopNMO'>";
    gameHours.forEach(function (game) {
        var playing = "";
        if(game.Active === 1) { playing = "<strong> (Playing!)</strong>)"; }
        gheA += "<div class='tr'>" +
                "<span class='td'>" + game.Name + playing + "</span>" +
                "<span class='td'><div class='UPop'>" + game.Hours +
                "<div class='UPopO'>Last played: " + game.Last +
                "</div></div></span>" +
                "</div>";
    });
    gheA += "</div>";
    rData += gheA + "</div></div></span>";
    dojo.byId(target).innerHTML = rData;
}

function putGameIndex(target, gameIndex) {
    var rData = "";
    var giCols = [ "Title", "Size<br/>GiB", "Linux<br/>Tested" ];
    var giTable = "<div class='table'><div class='tr'>";
    for(var i = 0; i < giCols.length; i++) { giTable += "<span class='td'><strong>" + giCols[i] + "</strong></span>"; }
    giTable += "</div>";
    gameIndex.forEach(function (gix) {
        giTable += "<div class='tr'>" +
                "<span class='td'><div class='UPop'>" + gix.GameName +
                "<div class='UPopO'>" +
                "<strong>File: </strong>" + gix.FoF + "<br/>" +
                "<strong>Volume: </strong>" + gix.Volume + "<br/>" +
                "<strong>Last Burned: </strong>" + gix.BurnDate + "<br/>" +
                "<strong>Last Updated: </strong>" + gix.LastUpdate + "<br/>" +
                "<strong>Pending Burn?: </strong>" + gix.PendingBurn + "<br/>" +
                "</div></div></span>" +
                "<span class='td'>" + gix.SizeG + "</span>" +
                "<span class='td'>" + gix.LinuxTested + "</span>" +
                "</div>";
    });
    giTable += "</div>";
    rData += giTable;
    dojo.byId(target).innerHTML = rData;
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

function gameButtonListeners() {
    var btnShowHours = dojo.byId("ShETGHours");
    var btnShowIndex = dojo.byId("ShETGIndex");
    var btnShowFF14Q = dojo.byId("ShETGFF14Q");
    dojo.connect(btnShowHours, "click", displayGameHours);
    dojo.connect(btnShowIndex, "click", displayGameIndex);
    dojo.connect(btnShowFF14Q, "click", displayGameFf14q);
}