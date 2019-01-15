/* 
Created: 25 Mar 2018
Split off from Entertain.js: 10 Apr 2018
Split off from Games.js: 22 May 2018
Updated: 14 Jan 2019

 */

var ffxivCrafting;
var ffxivItems;
var ffxivMerged;

function actOnFfxivQuestDone(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    switch(thisFormData.Type) {
        case "Achievement": setFfxivAchievementDone(thisFormData); break;
        case "Quest": setFfxivQuestDone(thisFormData); break;
        case "Crafting": setFfxivCraftingDone(thisFormData); break;
        case "Dungeon": setFfxivDungeonDone(thisFormData); break;
        case "Gathering": setFfxivGatheringDone(thisFormData); break;
        case "Hunt": setFfxivHuntingDone(thisFormData); break;
        default: window.alert("TYPE NOT SET!\n" + thisFormData.QuestOrder); break;
    }
}

function actOnFfxivMergedRangeSearch(event) {
    dojo.stopEvent(event);
    var target = "ETGFF14Q";
    var thisFormData = dojo.formToObject(this.form);
    if(isSet(thisFormData.LevelRangeMin) && isSet(thisFormData.LevelRangeMax)) {
        getGameFf14qDataInRange(target, thisFormData);
    } else {
        window.alert("Invalid search range!");
    }
}

function displayGameFf14c() {
    var target = "ETGFF14C";
    getGameFf14c(target);
    $("#ETGHours").hide();
    $("#ETGFF14D").hide();
    $("#ETGFF14I").hide();
    $("#ETGFF14Q").hide();
    $("#ETGIndex").hide();
}

function displayGameFf14d() {
    var target = "ETGFF14D";
    getGameFf14d(target);
    $("#ETGHours").hide();
    $("#ETGFF14C").hide();
    $("#ETGFF14I").hide();
    $("#ETGFF14Q").hide();
    $("#ETGIndex").hide();
}

function displayGameFf14i() {
    var target = "ETGFF14I";
    console.log("Entered displayGameFf14i");
    getGameFf14i(target);
    $("#ETGHours").hide();
    $("#ETGFF14C").hide();
    $("#ETGFF14D").hide();
    $("#ETGFF14Q").hide();
    $("#ETGIndex").hide();
    $("#"+target).show();
}

function displayGameFf14q() {
    var target = "ETGFF14Q";
    getGameFf14q(target);
    $("#ETGHours").hide();
    $("#ETGFF14C").hide();
    $("#ETGFF14D").hide();
    $("#ETGFF14I").hide();
    $("#ETGIndex").hide();
}

function ffxivCraftingHint(value) {
    if(value.length > 2) {
        var hitCount = 0;
        var matchLimitHit = 0;
        var matchingRows = [];
        ffxivCrafting.forEach(function (sr) {
            if(
                (isSet(sr.Recipie) && (sr.Recipie).toLowerCase().includes(value.toLowerCase()))
            ) { 
                hitCount++;
                if(matchingRows.length < 49) {
                    matchingRows.push(sr);
                } else {
                   matchLimitHit = 1;
                }
            }
        });
        putFfxivCraftingList("craftingList", matchingRows);    
    }
}

function ffxivItemHint(value) {
    if(value.length > 2) {
        var hitCount = 0;
        var matchLimitHit = 0;
        var matchingRows = [];
        ffxivItems.forEach(function (sr) {
            if(
                (isSet(sr.Name) && (sr.Name).toLowerCase().includes(value.toLowerCase()))
            ) { 
                hitCount++;
                if(matchingRows.length < 49) {
                    matchingRows.push(sr);
                } else {
                   matchLimitHit = 1;
                }
            }
        });
        putFfxivItemList("itemList", matchingRows);    
    }
}

function ffxivMergedHint(value) {
    if(value.length > 2) {
        var hitCount = 0;
        var matchLimitHit = 0;
        var matchingRows = [];
        ffxivMerged.forEach(function (sr) {
            if(
                (isSet(sr.QuestOrder) && (sr.QuestOrder).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.qcDesc) && (sr.qcDesc).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.MasterType) && (sr.MasterType).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.Zone) && (sr.Zone).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.Stats) && (sr.Stats).toLowerCase().includes(value.toLowerCase())) ||
                (isSet(sr.Name) && (sr.Name).toLowerCase().includes(value.toLowerCase()))
            ) { 
                hitCount++;
                if(matchingRows.length < 49) {
                    matchingRows.push(sr);
                } else {
                   matchLimitHit = 1;
                }
            }
        });
        putFfxivMergedList("mergedList", matchingRows);    
    }
}

function getGameFf14c(target) {
    aniPreload("on");
    var thePostData = { "doWhat": "getFfxivCrafting" }
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Entertainment"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    aniPreload("off");
                    ffxivCrafting = data;
                    putFfxivCrafting(target, data);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for FFXIV Crafting FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
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

function getGameFf14i(target) {
    aniPreload("on");
    var thePostData = { "doWhat": "getFfxivItems" };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Entertainment"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    aniPreload("off");
                    ffxivItems = data;
                    putFfxivItems(target, data);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for FFXIV Items FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
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
                    window.alert("request for FFXIV Quest Graph by Day FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
    setTimeout(function () { getGameFf14q(target); }, timeout);
}

function getGameFf14qData(target) {
    getDivLoadingMessage(target);
    aniPreload("on");
    var thePostData = { "doWhat": "getFfxivMerged" };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Entertainment"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    aniPreload("off");
                    ffxivMerged = data.ffxivMerged;
                    putFfxivMerged(
                            target,
                            data.ffxivMerged,
                            data.ffxivCount,
                            data.ffxivImageMaps,
                            data.ffxivEmotes,
                            data.ffxivAssets
                    );
                    $("#"+target).show();
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for FFXIV Merged FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function getGameFf14qDataInRange(target, formData) {
    getDivLoadingMessage(target);
    aniPreload("on");
    var thePostData = {
        "doWhat": "getFfxivMergedInRange",
        "minLevel": formData.LevelRangeMin,
        "maxLevel": formData.LevelRangeMax
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Entertainment"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                    aniPreload("off");
                    ffxivMerged = data.ffxivMerged;
                    putFfxivMerged(
                            target,
                            data.ffxivMerged,
                            data.ffxivCount,
                            data.ffxivImageMaps,
                            data.ffxivEmotes,
                            data.ffxivAssets
                    );
                    $("#"+target).show();
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for FFXIV Merged IN RANGE FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function putFfxivCrafting(target, craftingData) {
    var rCount = craftingData.length;
    var rData = "<h3>Crafting</h3><strong>Indexed recipies: " + rCount + "<br/>" +
            "<p><div id='rSearchHolder'></div>" +
            "<p><div id='craftingList'></div>";
    dojo.byId(target).innerHTML = rData;
    putFfxivCraftingSearchBox("rSearchHolder");
    putFfxivCraftingList("craftingList", craftingData);
}

function putFfxivCraftingList(target, craftingData) {
    $("#"+target).show();
    var cNum = 0;
    var dCols = [ "Recipie", "Level" ];
    var rData = "<div class='table'><div class='tr'>";
    for (var i = 0; i < dCols.length; i++) { rData += "<span class='td'><strong>" + dCols[i] + "</strong></span>"; }
    rData += "</div>";
    craftingData.forEach(function (ff14c) {
        if(cNum <= 50) {
            var rNameShort = (ff14c.Recipie).substring(4); // work on table without char prefix
            rData += "<div class='tr'>" +
                    "<span class='td'><div class='UPop'>" + rNameShort + "</span>" +
                    "<div class='UPopO'>" +
                    "<strong>Crystals</strong>: " + ff14c.Crystals + "<br/>" +
                    "<strong>Materials</strong>: " + ff14c.Materials + "<br/>" +
                    "<strong>Class</strong>: " + ff14c.Class + "<br/>" +
                    "</div></div></span>" +
                    "<span class='td'><div class='UPop'>" + ff14c.Level +
                    "<div class='UPopO'>" +
                    "<strong>Durability</strong>: " + ff14c.Durability + "<br/>" +
                    "<strong>Max Quality</strong>: " + ff14c.MaxQuality + "<br/>" +
                    "</div></div></span>" +
                    "</div>";
        }
        cNum++;
    });
    rData += "</div>";
    dojo.byId(target).innerHTML = rData;    
}

function putFfxivCraftingSearchBox(target) {
    var rData = "<div class='table'>" +
        "<form class='tr' id='ffxivCraftingSearchForm'>" +
        "<span class='td'><input type='text' id='SearchItems' name='CraftingSearchField' onkeyup='ffxivCraftingHint(this.value)' /></span>" +
        "<span class='td'><strong>Search</strong></span>" +
        "</form>" +
        "</div>";
    dojo.byId(target).innerHTML = rData;
}

function putFfxivDungeonList(target, dungeonData) {
    $("#"+target).show();
    var dCols = [ "Name", "Level", "Rewards" ];
    var rData = "<div class='table'><div class='tr'>";
    for (var i = 0; i < dCols.length; i++) { rData += "<span class='td'><strong>" + dCols[i] + "</strong></span>"; }
    rData += "</div>";
    dungeonData.forEach(function (ff14d) {
        var tomes = "";
        var unlockStyle = "color: red;";
        if(ff14d.Completed === 1) { unlockStyle = "color: white;"; }
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
        if(ff14d.TomesGenesis !== 0) {
            tomes += " <div class='UPop'><img class='th_icon' src='" + getBasePath("image") + "/ffxiv/Genesis.png'/>" +
                    "<div class='UPopO'>Genesis: " + ff14d.TomesGenesis + "</div></div>";
        }
        rData += "<div class='tr'>" +
                "<span class='td'><div class='UPop'><span style='" + unlockStyle + "'>" + ff14d.Name + "</span>" +
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

function putFfxivItemList(target, itemData) {
    $("#"+target).show();
    var iNum = 0;
    var iCols = [ "Name", "ILEV" ];
    var rData = "<div class='table'><div class='tr'>";
    for (var i = 0; i < iCols.length; i++) { rData += "<span class='td'><strong>" + iCols[i] + "</strong></span>"; }
    rData += "</div>";
    itemData.forEach(function (ff14i) {
        if(iNum <= 50) {
            rData += "<div class='tr'>" +
                    "<span class='td'><div class='UPop'>" + ff14i.Name +
                    "<div class='UPopO'>" +
                    "<strong>Category: </strong>" + ff14i.Category + "<br/>" +
                    "<strong>Classes: </strong>" + ff14i.Classes + "<br/>" +
                    "</div></div>" +
                    "</span>" +
                    "<span class='td'><div class='UPop'>" + ff14i.ILEV +
                    "<div class='UPopO'>" +
                    "<strong>Level: </strong>" + ff14i.Level + "<br/>";
            if(isSet(ff14i.Damage)) { rData += "<strong>Damage: </strong>" + ff14i.Damage + " (" + ff14i.DamageType + ")<br/>"; }
            if(isSet(ff14i.Delay)) { rData += "<strong>Delay: </strong>" + ff14i.Delay + "<br/>"; }
            if(isSet(ff14i.AutoAttack)) { rData += "<strong>Auto Attack: </strong>" + ff14i.AutoAttack + "<br/>"; }
            if(isSet(ff14i.Defense)) { rData += "<strong>Defense: </strong>" + ff14i.Defense + "<br/>"; }
            if(isSet(ff14i.MagicDefense)) { rData += "<strong>Magic Def.: </strong>" + ff14i.MagicDefense + "<br/>"; }
            if(isSet(ff14i.MateriaSlots)) { rData += "<strong>Materia Slots: </strong>" + ff14i.MateriaSlots + "<br/>"; }
            if(isSet(ff14i.Stats)) { rData += "<strong>Stats: </strong>" + ff14i.Stats + "<br/>"; }
            rData += "</div></div>" +
                    "</span>" +
                    "</div>";
        }
        iNum++;
    });
    rData += "</div>";
    console.log(target);
    dojo.byId(target).innerHTML = rData;
}

function putFfxivItems(target, itemData) {
    var iCount = ffxivItems.length;
    var rData = "<h3>Items</h3><strong>Indexed items: " + iCount + "<br/>" +
            "<p><div id='iSearchHolder'></div>" +
            "<p><div id='itemList'></div>";
    dojo.byId(target).innerHTML = rData;
    putFfxivItemSearchBox("iSearchHolder");
    putFfxivItemList("itemList", itemData);
}

function putFfxivItemSearchBox(target) {
    var rData = "<div class='table'>" +
        "<form class='tr' id='ffxivItemSearchForm'>" +
        "<span class='td'><input type='text' id='SearchItems' name='ItemSearchField' onkeyup='ffxivItemHint(this.value)' /></span>" +
        "<span class='td'><strong>Search</strong></span>" +
        "</form>" +
        "</div>";
    dojo.byId(target).innerHTML = rData;
}

function putFfxivMergedList(target, questData) {
    var qNum = 0;
    var qCols = [ "Up", "Name", "LV" ];
    var rData = "<div class='table'><div class='tr'>";
    for (var i = 0; i < qCols.length; i++) { rData += "<span class='td'><strong>" + qCols[i] + "</strong></span>"; }
    rData += "</div>";
    questData.forEach(function (ff14q) {
        if(qNum <= 100) {
            var shortName = ff14q.Name;
            if(ff14q.MasterType === 'Crafting') { shortName = shortName.substring(4); }
            var qComplete = "No";
            var fontColor = "Red";
            var updateCheckbox = "<input class='ffxivQuestDone' type='checkbox' name='qUpdate' value='do'/>" +
                    "<input type='hidden' name='Type' value='" + ff14q.MasterType + "'/>" +
                    "<input type='hidden' name='Name' value='" + ff14q.Name + "'/>" +
                    "<input type='hidden' name='QuestOrder' value='" + ff14q.QuestOrder + "'/>" +
                    "</span>";
            if(
                    ff14q.Completed === 1 ||
                    (
                        isSet(ff14q.MasterType) && ff14q.MasterType !== 'Achievement' &&
                        isSet(ff14q.MasterType) && ff14q.MasterType !== 'Quest' &&
                        isSet(ff14q.MasterType) && ff14q.MasterType !== 'Dungeon' &&
                        isSet(ff14q.MasterType) && ff14q.MasterType !== 'Crafting' &&
                        isSet(ff14q.MasterType) && ff14q.MasterType !== 'Gathering' &&
                        isSet(ff14q.MasterType) && ff14q.MasterType !== 'Hunt'
                    )
            ) {
                qComplete = "Yes";
                fontColor = "White";
                updateCheckbox = "NA";
            }
            var tdsStyle = "style='color: " + fontColor + ";'";
            rData += "<form class='tr' id='ffxivQuestSubmitForm'>" +
                    "<span class='td' " + tdsStyle + ">" + updateCheckbox + "</span>" +
                    "<span class='td' " + tdsStyle + "><div class='UPop'>" + shortName;
            if(isSet(ff14q.Type)) {
                switch(ff14q.Type) {
                    case "FT": rData += " <img class='th_icon' src='" + getBasePath("image") + "/ffxiv/qFeat.png'/>"; break;
                    case "MS": rData += " <img class='th_icon' src='" + getBasePath("image") + "/ffxiv/qMain.png'/>"; break;
                    case "LV": rData += " <img class='th_icon' src='" + getBasePath("image") + "/ffxiv/qLeve.png'/>"; break;
                    default: rData += " <img class='th_icon' src='" + getBasePath("image") + "/ffxiv/qSide.png'/>"; break;
                }
            }
            rData += "<div class='UPopO'>";
            if(isSet(ff14q.MasterType)) { rData += "Master Type: " + ff14q.MasterType + "<br/>"; }
            if(isSet(ff14q.Zone)) { rData += "Zone: " + ff14q.Zone + "<br/>"; }
            if(isSet(ff14q.CoordX)) { rData += "Coords: X" + ff14q.CoordX + ", Y" + ff14q.CoordY + "<br/>"; }
            if(isSet(ff14q.qcDesc)) { rData += "Chain: " + ff14q.qcDesc + "<br/>"; }
            if(isSet(ff14q.GivingNPC)) { rData += "Giving NPC: " + ff14q.GivingNPC + "<br/>"; }
            if(isSet(ff14q.Classes)) { rData += "Class/Jobs: " + ff14q.Classes + "<br/>"; }
            if(isSet(ff14q.Version)) { rData += "Patch Level: " + ff14q.Version + "<br/>"; }
            if(isSet(ff14q.Event)) { rData += "Event: " + ff14q.Event + "<br/>"; }
            if(isSet(ff14q.OrigCompDate)) { rData += "1st Completed: " + ff14q.OrigCompDate + "<br/>"; }
            if(isSet(ff14q.Crystals)) { rData += "Crystals: " + ff14q.Crystals + "<br/>"; }
            if(isSet(ff14q.Materials)) { rData += "Materials: " + ff14q.Materials + "<br/>"; }
            if(isSet(ff14q.Durability)) { rData += "Durability: " + ff14q.Durability + "<br/>"; }
            if(isSet(ff14q.MaxQuality)) { rData += "Max. Quality: " + ff14q.MaxQuality + "<br/>"; }
            if(isSet(ff14q.Damage)) { rData += "<strong>Damage: </strong>" + ff14q.Damage + " (" + ff14q.DamageType + ")<br/>"; }
            if(isSet(ff14q.Delay)) { rData += "<strong>Delay: </strong>" + ff14q.Delay + "<br/>"; }
            if(isSet(ff14q.AutoAttack)) { rData += "<strong>Auto Attack: </strong>" + ff14q.AutoAttack + "<br/>"; }
            if(isSet(ff14q.Defense)) { rData += "<strong>Defense: </strong>" + ff14q.Defense + "<br/>"; }
            if(isSet(ff14q.MagicDefense)) { rData += "<strong>Magic Def.: </strong>" + ff14q.MagicDefense + "<br/>"; }
            if(isSet(ff14q.MateriaSlots)) { rData += "<strong>Materia Slots: </strong>" + ff14q.MateriaSlots + "<br/>"; }
            if(isSet(ff14q.Stats)) { rData += "<strong>Stats: </strong>" + ff14q.Stats + "<br/>"; }
            if(isSet(ff14q.QuestOrder)) { rData += "Quest Order: " + ff14q.QuestOrder + "<br/>"; }
            if(isSet(ff14q.Exp)) { rData += "<img class='th_icon' src='" + getBasePath("image") + "/ffxiv/XP.png'/>" + ff14q.Exp; }
            if(isSet(ff14q.Gil)) { rData += "<img class='th_icon' src='" + getBasePath("image") + "/ffxiv/Gil.png'/>" + ff14q.Gil; }
            rData += "</div></div></span>" +
                    "<span class='td' " + tdsStyle + "><div class='UPop'>" + ff14q.MinLevel +
                    "<div class='UPopO'>";
            if(isSet(ff14q.Version)) { "Patch: " + ff14q.Version + "<br/>"; }
            if(isSet(ff14q.Seals)) { "Seals: " + ff14q.Seals + "<br/>"; }
            if(isSet(ff14q.ILEV)) { "ILEV: " + ff14q.ILEV + "<br/>"; }
            rData += "</div></div></span>" +
                    "</form>";
        }
        qNum++;
    });
    rData += "</div>";
    dojo.byId(target).innerHTML = rData;
    dojo.query(".ffxivQuestDone").connect("onchange", actOnFfxivQuestDone);
}

function putFfxivMerged(target, mergedData, countIn, iMaps, emotes, assets) {
    var assetValues = 0;
    assets.forEach(function (asset) { assetValues += asset.Value; });
    var totalValue = (assetValues/1000000).toFixed(2);
    var counts = countIn[0];
    var aCount = counts.Achievements;
    var mCount = ffxivMerged.length;
    var qCount = counts.Quests;
    var dCount = counts.Dungeons;
    var gCount = counts.Gathering;
    var cCount = counts.Crafting;
    var hCount = counts.Hunting;
    var achCounter = 0;
    var compCounter = 0;
    var craftCounter = 0;
    var dungeonCounter = 0;
    var huntCounter = 0;
    var gatherCounter = 0;
    var totalCompletionCount = 0;
    var tCount = hCount + qCount + cCount;
    var availImages = [
        "Brd33", "Brd36", "Brd51", "Brd52", "Brd52a", "Brd52b", "Brd52c",
        "Min1", "Min13",
        "Cnj1", "Cnj4a", "Cnj9"
    ];
    mergedData.forEach(function (ffxq) {
        if(ffxq.Completed === 1 && ffxq.MasterType === "Achievement") { achCounter++; totalCompletionCount++; }
        if(ffxq.Completed === 1 && ffxq.MasterType === "Quest") { compCounter++; totalCompletionCount++; }
        if(ffxq.Completed === 1 && ffxq.MasterType === "Crafting") { craftCounter++; totalCompletionCount++; }
        if(ffxq.Completed === 1 && ffxq.MasterType === "Dungeon") { dungeonCounter++; totalCompletionCount++; }
        if(ffxq.Completed === 1 && ffxq.MasterType === "Gathering") { gatherCounter++; totalCompletionCount++; }
        if(ffxq.Completed === 1 && ffxq.MasterType === "Hunt") { huntCounter++; totalCompletionCount++; }
    });
    var rData = "<span id='charList'></span>" +
            totalValue + "m <img class='th_icon' src='" + getBasePath("image") + "/ffxiv/Gil.png'/>" +
            "-- Mist Ward 1 Plot 39<br/>" +
            " <div class='UPop'><button class='UButton'>Maps</button>" +
            "<div class='UPopO'>" +
            " [<a href='" + getBasePath("image") + "/ffxiv/LaNoscea.jpg' target='ffxivMap'>LAN</a>]" +
            " [<a href='" + getBasePath("image") + "/ffxiv/Shroud.jpg' target='ffxivMap'>SHR</a>]" +
            " [<a href='" + getBasePath("image") + "/ffxiv/Thanalan.jpg' target='ffxivMap'>THA</a>]<br/>";
    iMaps.forEach(function (ffIm) {
        rData += "<a href='" + getBasePath("image") + "/ffxiv/" + ffIm.File + "' target='ffxivMap'>" + ffIm.Description + "</a><br/>";
    });
    rData += "</div></div>" +
            " <div class='UPop'><button class='UButton'>Screens</button>" +
            "<div class='UPopO'>";
    for(var i = 0; i < availImages.length; i++) {
        rData += " [<a href='" + getBasePath("image") + "/ffxiv/" + availImages[i] + ".jpg' target='ffxivMap'>" + availImages[i] + "</a>]";
    }
    rData += "</div></div>" +
            " <div class='UPop'><button class='UButton'>Assets</button>" +
            "<div class='UPopO'>";
    assets.forEach(function (tAs) {
        rData += "<span style='color: lightgreen;'>" + tAs.What + "</span>" +
                " (<em>" + autoUnits(tAs.Value) + "  <img class='th_icon' src='" + getBasePath("image") + "/ffxiv/Gil.png'/></em>)<br/>"; 
    });
    rData += "</div></div>" +
            " <div class='UPop'><button class='UButton'>Emotes</button>" +
            "<div class='UPopO'>";
    emotes.forEach(function (tEm) {
        rData += "<span style='color: lightgreen;'>" + tEm.Command + "</span>";
        if(isSet(tEm.AcquiredBy)) {
            rData += " (<em>" + tEm.AcquiredBy + "</em>)";
        }
        rData += "<br/>";
    });
    rData += "</div></div>" +
            "<h3>Merged FFXIV Data</h3><strong><div class='UPop'>" +
            "Index size: " + mCount + "<br/>" +
            "Completed: " + totalCompletionCount + " (" + ((totalCompletionCount/tCount)*100).toFixed(1) + "%)" +
            "<div class='UPopO'>" +
            "<strong>Achievements:</strong> " + achCounter + " of " + counts.Achievements + " (" + ((achCounter/aCount)*100).toFixed(1) + "%)<br/>" +
            "<strong>Crafting:</strong> " + craftCounter + " of " + counts.Crafting + " (" + ((craftCounter/cCount)*100).toFixed(1) + "%)<br/>" +
            "<strong>Dungeons:</strong> " + dungeonCounter + " of " + counts.Dungeons + " (" + ((dungeonCounter/dCount)*100).toFixed(1) + "%)<br/>" +
            "<strong>Gathering:</strong> " + gatherCounter + " of " + counts.Gathering + " (" + ((gatherCounter/gCount)*100).toFixed(1) + "%)<br/>" +
            "<strong>Hunting:</strong> " + huntCounter + " of " + counts.Hunting + " (" + ((huntCounter/hCount)*100).toFixed(1) + "%)<br/>" +
            "<strong>Quests:</strong> " + compCounter + " of " + qCount + " (" + ((compCounter/qCount)*100).toFixed(1) + "%)<br/>" +
            "<strong>Wearables:</strong> " + counts.Wearables + "<br/>" +
            "<strong>Weapons:</strong> " + counts.Weapons + "<br/>" +
            "</div></div><br/>" +
            "<a href='" + doCh("j", "ffxivQuestsByDay", null) + "' target='qCh'><img class='ch_small' src='" + doCh("j", "ffxivQuestsByDay", "th") + "'/></a>" +
            "<p><div id='mSearchHolder'></div>" +
            "<p><div id='mergedList'></div>";
    dojo.byId(target).innerHTML = rData;
    putFfxivMergedSearchBox("mSearchHolder");
    putFfxivMergedList("mergedList", mergedData);
    getWebLinks("ffxivChars", "charList", "list");
}

function putFfxivMergedSearchBox(target) {
    var rData = "<div class='table'>" +
        "<form class='tr' id='ffxivSearchForm'>" +
        "<span class='td'><div class='UPop'><input type='text' style='width: 128px;' id='SearchBrix' name='StationSearchField' onkeyup='ffxivMergedHint(this.value)' /><div class='UPopO'>Text search (Name, QuestCode, Zone, Code Description)</div></div></span>" +
        "<span class='td'><div class='UPop'><input type='number' style='width: 36px;' id='MinLevel' name='LevelRangeMin'/><div class='UPopO'>Min. Level</div></div></span>" +
        "<span class='td'><div class='UPop'><input type='number' style='width: 36px;'  id='MaxLevel' name='LevelRangeMax'/><div class='UPopO'>Max. Level</div></div></span>" +
        "<span class='td'><button class='UButton' id='LevelRangeSubmit'>Range</button></span>" +
        "</form>" +
        "</div>";
    dojo.byId(target).innerHTML = rData;
    var ffxivRangeButton = dojo.byId("LevelRangeSubmit");
    dojo.connect(ffxivRangeButton, "onclick", actOnFfxivMergedRangeSearch);
}

function setFfxivAchievementDone(formData) {
    var target = "ETGFF14Q";
    aniPreload("on");
    var thePostData = {
        "doWhat": "setFfxivAchievementDone",
        "achievementCode": formData.QuestOrder
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Entertainment"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    aniPreload("off");
                    showNotice("Achievement " + formData.Name + " complete!");
                    getGameFf14q(target);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request to set Achievement Complete FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function setFfxivCraftingDone(formData) {
    var target = "ETGFF14Q";
    aniPreload("on");
    var thePostData = {
        "doWhat": "setFfxivCraftingDone",
        "recipieName": formData.Name
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Entertainment"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    aniPreload("off");
                    showNotice("Crafting " + formData.Name + " complete!");
                    getGameFf14q(target);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request to set Crafting Complete FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function setFfxivDungeonDone(formData) {
    var target = "ETGFF14Q";
    aniPreload("on");
    var thePostData = {
        "doWhat": "setFfxivDungeonDone",
        "dungeonCode": formData.QuestOrder
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Entertainment"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    aniPreload("off");
                    showNotice("Dungeon " + formData.Name + " complete!");
                    getGameFf14q(target);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request to set Dungeon Complete FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function setFfxivGatheringDone(formData) {
    var target = "ETGFF14Q";
    aniPreload("on");
    var thePostData = {
        "doWhat": "setFfxivGatheringDone",
        "gatherCode": formData.QuestOrder
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Entertainment"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    aniPreload("off");
                    showNotice("Gathering " + formData.Name + " complete!");
                    getGameFf14q(target);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request to set Gathering Complete FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function setFfxivHuntingDone(formData) {
    var target = "ETGFF14Q";
    aniPreload("on");
    var thePostData = {
        "doWhat": "setFfxivHuntingDone",
        "huntCode": formData.QuestOrder
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Entertainment"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    aniPreload("off");
                    showNotice("Hunting " + formData.Name + " complete!");
                    getGameFf14q(target);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request to set Hunting Complete FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function setFfxivQuestDone(formData) {
    var target = "ETGFF14Q";
    aniPreload("on");
    var thePostData = {
        "doWhat": "setFfxivQuestDone",
        "questOrder": formData.QuestOrder
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Entertainment"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    aniPreload("off");
                    showNotice("Quest " + formData.Name + " complete!");
                    getGameFf14q(target);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request to set Quest Complete FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}
