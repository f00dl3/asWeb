/* 
Created: 25 Mar 2018
Split off from Entertain.js: 10 Apr 2018
Split off from Games.js: 22 May 2018
Updated: 23 May 2018
 */

var ffxivQuests;

function actOnFfxivQuestDone(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    var thisFormDataJ = dojo.formToJson(this.form);
    setFfxivQuestDone(thisFormData);
}

function displayGameFf14q() {
    var target = "ETGFF14Q";
    getGameFf14q(target);
    $("#ETGHours").hide();
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

function getGameFf14q(target) {
    var timeout = 2 * 60 * 1000;
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
    setTimeout(function () { getGameFf14q(target); }, timeout);
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
    var qCount = ffxivQuests.length;
    var compCounter = 0;
    ffxivQuests.forEach(function (ffxq) {
        if(ffxq.Completed === 1) { compCounter++; }
    });
    var rData = "<a href='" + charProfLink + "' target='new'>Foodle Faddle</a>" +
            "<h3>Quests</h3><strong>Indexed quests: " + qCount + "<br/>" +
            "Completed quests: " + compCounter + " (" + ((compCounter/qCount)*100).toFixed(2) + "%)" +
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
