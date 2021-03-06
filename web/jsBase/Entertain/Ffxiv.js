/* 
by Anthony Stump
Created: 25 Mar 2018
Split off from Entertain.js: 10 Apr 2018
Split off from Games.js: 22 May 2018
Updated: 23 Nov 2020

 */

var ffxivCrafting;
var ffxivItems;
var ffxivMerged;
var levelCap = 80;
var resultLimit = 50;

function actOnFfxivAssetAdd(event) {
	//NEXT TO ADD!
	dojo.stopEvent(event);
	var thisFormData = dojo.formToObject(this.form);
	window.alert("Got here!");
	//setFfxivAssetAdd(thisFormData);
}

function actOnFfxivGil(event) {
	dojo.stopEvent(event);
	var thisFormData = dojo.formToObject(this.form);
	setFfxivGil(thisFormData);
}

function actOnFfxivLevelIncrease(event) {
	dojo.stopEvent(event);
	var thisFormData = dojo.formToObject(this.form);
	setFfxivLevelsIncrease(thisFormData);
}

function actOnFfxivMarketGil(event) {
	dojo.stopEvent(event);
	var thisFormData = dojo.formToObject(this.form);
	setFfxivMarketGil(thisFormData);
}

function actOnFfxivQuestDone(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    switch(thisFormData.Type) {
        case "Achievement": setFfxivAchievementDone(thisFormData); break;
        case "Quest": setFfxivQuestDone(thisFormData); break;
        case "Crafting": setFfxivCraftingDone(thisFormData); break;
        case "Dungeon":
        	if(thisFormData.Completed === "0") { 
        		setFfxivDungeonDone(thisFormData);
        	} else {
        		setFfxivDungeonClear(thisFormData);
        	}
        	break;
        case "FATE": setFfxivFateDone(thisFormData); break;
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
    getGameFf14q(target, true);
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
    	if(value.includes(" ")) {
    		wordArray = value.split(" ");
    		ffxivCrafting.forEach(function (sr) {
        		var wordsHit = 0;
        		wordArray.forEach(function(tWord) {
		            if(
		                (isSet(sr.Recipie) && (sr.Recipie).toLowerCase().includes(tWord.toLowerCase()))
		            ) { 
		            	wordsHit++;
		            }
	    			if (wordsHit === wordArray.length) {
	    				hitCount++;
	                    if(matchingRows.length < (resultLimit-1)) {
	                    	matchingRows.push(sr);
	                    } else {
	                    	matchLimitHit = 1;
	                    }	
	    			}
	        	});
        	});
    	} else {
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
    	}
        putFfxivCraftingList("craftingList", matchingRows);    
    }
}

function ffxivItemHint(value) {
    if(value.length > 2) {
        var hitCount = 0;
        var matchLimitHit = 0;
        var matchingRows = [];
    	if(value.includes(" ")) {
    		wordArray = value.split(" ");
    		ffxivItems.forEach(function (sr) {
        		var wordsHit = 0;
        		wordArray.forEach(function(tWord) {
		            if(
		                (isSet(sr.Name) && (sr.Name).toLowerCase().includes(tWord.toLowerCase()))
		            ) { 
		            	wordsHit++;
		            }
	    			if (wordsHit === wordArray.length) {
	    				hitCount++;
	                    if(matchingRows.length < (resultLimit-1)) {
	                    	matchingRows.push(sr);
	                    } else {
	                    	matchLimitHit = 1;
	                    }	
	    			}
	        	});
        	});
    	} else {
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
    	}
        putFfxivItemList("itemList", matchingRows);    
    }
}

function ffxivMergedHint(value) {
    if(value.length > 2) {
        var hitCount = 0;
        var matchLimitHit = 0;
        var matchingRows = [];
    	if(value.includes(" ")) {
    		wordArray = value.split(" ");
    		ffxivMerged.forEach(function (sr) {
        		var wordsHit = 0;
        		wordArray.forEach(function(tWord) {
		            if(
		                (isSet(sr.QuestOrder) && (sr.QuestOrder).toLowerCase().includes(tWord.toLowerCase())) ||
		                (isSet(sr.qcDesc) && (sr.qcDesc).toLowerCase().includes(tWord.toLowerCase())) ||
		                (isSet(sr.MasterType) && (sr.MasterType).toLowerCase().includes(tWord.toLowerCase())) ||
		                (isSet(sr.Zone) && (sr.Zone).toLowerCase().includes(tWord.toLowerCase())) ||
		                (isSet(sr.Stats) && (sr.Stats).toLowerCase().includes(tWord.toLowerCase())) ||
		                (isSet(sr.Name) && (sr.Name).toLowerCase().includes(tWord.toLowerCase()))
		            ) { 
		            	wordsHit++;
		            }
	    			if (wordsHit === wordArray.length) {
	    				hitCount++;
	                    if(matchingRows.length < (resultLimit-1)) {
	                    	matchingRows.push(sr);
	                    } else {
	                    	matchLimitHit = 1;
	                    }	
	    			}
	        	});
        	});
    	} else {
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
	                if(matchingRows.length < (resultLimit -1)) {
	                    matchingRows.push(sr);
	                } else {
	                   matchLimitHit = 1;
	                }
	            }
	        });
    	}
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
            .post(getResource("FFXIV"), {
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
            .post(getResource("FFXIV"), {
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

function getGameFf14q(target, refresh) {
    var timeout = getRefresh("long");
    /* aniPreload("on");
    var thePostData = { "doWhat": "EntertainmentFfxivQuestsByDate" };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Chart"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    aniPreload("off"); */
                    getGameFf14qData(target);
                /* },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for FFXIV Quest Graph by Day FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    }); */
    if(refresh) { setTimeout(function () { getGameFf14q(target, false); }, timeout); }
}

function getGameFf14qData(target) {
    getDivLoadingMessage(target);
    aniPreload("on");
    var thePostData = { "doWhat": "getFfxivMerged" };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("FFXIV"), {
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
                            data.ffxivAssets,
                            data.ffxivLevels
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
            .post(getResource("FFXIV"), {
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
                            data.ffxivAssets,
                            data.ffxivLevels
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
                    "<input type='hidden' name='Completed' value='" + ff14q.Completed + "'/>" +
                    "</span>";
            if(isSet(ff14q.MasterType) && ff14q.MasterType === 'Dungeon') {
            	if (ff14q.Completed === 1) {
	                qComplete = "Yes";
	                fontColor = "White";            		
            	}
            } else {
	            if(
	                    ff14q.Completed === 1 ||
	                    (
	                        isSet(ff14q.MasterType) && ff14q.MasterType !== 'Achievement' &&
	                        isSet(ff14q.MasterType) && ff14q.MasterType !== 'Quest' &&
	                        isSet(ff14q.MasterType) && ff14q.MasterType !== 'Crafting' &&
	                        isSet(ff14q.MasterType) && ff14q.MasterType !== 'Dungeon' &&
	                        isSet(ff14q.MasterType) && ff14q.MasterType !== 'Gathering' &&
	                        isSet(ff14q.MasterType) && ff14q.MasterType !== 'FATE' &&
	                        isSet(ff14q.MasterType) && ff14q.MasterType !== 'Hunt'
	                    )
	            ) {
	                qComplete = "Yes";
	                fontColor = "White";
	                updateCheckbox = "NA";
	            }
            }
            var tdsStyle = "style='color: " + fontColor + ";'";
            rData += "<form class='tr' id='ffxivQuestSubmitForm'>" +
                    "<span class='td' " + tdsStyle + ">" + updateCheckbox + "</span>" +
                    "<span class='td' " + tdsStyle + "><div class='UPop'>" + shortName;
            if(ff14q.MasterType === 'Quest') {
                if(isSet(ff14q.Type)) {
                    switch(ff14q.Type) {
                    	case "DA": rData += " <img class='th_icon' src='" + getBasePath("image") + "/ffxiv/qDaily.png'/>"; break;
                        case "FT": rData += " <img class='th_icon' src='" + getBasePath("image") + "/ffxiv/qFeat.png'/>"; break;
                        case "MS": rData += " <img class='th_icon' src='" + getBasePath("image") + "/ffxiv/qMain.png'/>"; break;
                        case "LV": rData += " <img class='th_icon' src='" + getBasePath("image") + "/ffxiv/qLeve.png'/>"; break;
                        default: rData += " <img class='th_icon' src='" + getBasePath("image") + "/ffxiv/qSide.png'/>"; break;
                    }
                } else {
                    rData += "<img class='th_icon' src='" + getBasePath("image") + "/ffxiv/qSide.png'/>";
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
            if(isSet(ff14q.Clears)) { rData += "Times Cleared: " + ff14q.Clears + "<br/>"; }
            if(isSet(ff14q.Journal)) { rData += "Journal: " + ff14q.Journal + "<br/>"; }
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

function putFfxivMerged(target, mergedData, countIn, iMaps, emotes, assets, levelInfo) {
    var assetValues = 0;
    var accurateAssetValues = 0;
    var currentGil = 0;
    assets.forEach(function (asset) {
    	assetValues += asset.Value;
    	if(asset.WriteOff == 0) {
    		accurateAssetValues += asset.Value;
    	}
    	if(asset.What == "Gil") {
    		currentGil = asset.Value;
    	}
    });
    var totalValue = assetValues;
    var totalAccurateValue = accurateAssetValues;
    var counts = countIn[0];
    var aCount = counts.Achievements;
    var mCount = ffxivMerged.length;
    var qCount = counts.Quests;
    var dCount = counts.Dungeons;
    var fCount = counts.FATEs;
    var gCount = counts.Gathering;
    var cCount = counts.Crafting;
    var hCount = counts.Hunting;
    var achCounter = 0;
    var compCounter = 0;
    var craftCounter = 0;
    var dungeonCounter = 0;
    var fateCounter = 0;
    var gatherCounter = 0;
    var huntCounter = 0;
    var lData = "";
    var totalCompletionCount = 0;
    var tCount = hCount + qCount + cCount;
    var availImages = [
        "Brd33", "Brd36", "Brd51", "Brd52", "Brd52a", "Brd52b", "Brd52c",
        "Min1", "Min13", "Min43", "Fsh15",
        "Cnj1", "Cnj4a", "Cnj9",
        "Whm34"
    ];
    mergedData.forEach(function (ffxq) {
        if(ffxq.Completed === 1 && ffxq.MasterType === "Achievement") { achCounter++; totalCompletionCount++; }
        if(ffxq.Completed === 1 && ffxq.MasterType === "Quest") { compCounter++; totalCompletionCount++; }
        if(ffxq.Completed === 1 && ffxq.MasterType === "Crafting") { craftCounter++; totalCompletionCount++; }
        if(ffxq.Completed === 1 && ffxq.MasterType === "Dungeon") { dungeonCounter++; totalCompletionCount++; }
        if(ffxq.Completed === 1 && ffxq.MasterType === "FATE") { fateCounter++; totalCompletionCount++; }
        if(ffxq.Completed === 1 && ffxq.MasterType === "Gathering") { gatherCounter++; totalCompletionCount++; }
        if(ffxq.Completed === 1 && ffxq.MasterType === "Hunt") { huntCounter++; totalCompletionCount++; }
    });
    var lData = "<div class='table'><div class='tr'>";
    var ltrIter = 0;
	levelInfo.forEach(function(li) {
		if(ltrIter == 5) {
			ltrIter = 0;
			lData += "</div><div class='tr'>";
		}
		var cIcon = getBasePath("image") + "/ffxiv/" + li.ClassCode + ".png";
		var tbBorder = "red";
		if(li.Level !== levelCap) {
			tbBorder = "green";
		}
		lData += "<span class='td' style='border: 1px solid " + tbBorder + ";'><form>" +
			"<input type='hidden' name='ClassCode' value='" + li.ClassCode + "'/>" +
			"<input type='hidden' name='CurrentLevel' value='" + li.Level + "'/>" +
			"<button class='doLevelIncrease'><img class='th_icon' src='" + cIcon + "'/><br/>" +
			li.Level + "</button></form>" +
			"</span>";
		ltrIter++;
	});
	lData += "</div></div>";
    var rData = "<span id='charList'></span>" +
    		"<div class='UPop'>[CL]<div class='UPopO'>" + lData + "</div></div> " +
            "<div class='UPop'>" +
            autoUnits(totalValue) + " (" + autoUnits(totalAccurateValue) + ") <img class='th_icon' src='" + getBasePath("image") + "/ffxiv/Gil.png'/>" +
            "-- Mist Ward 1 Plot 39<br/><div class='UPopO'>" +
            "<form id='gilForm'><img class='th_icon' src='" + getBasePath("image") + "/ffxiv/Gil.png'/> " +
            " <input name='Gil' type='number' step='0' value='" + currentGil + "' style='width: 168px;'/>" +
            " <button id='gilUpdateButton' type='submit'>gil</button>" +
            "</form> " +
            "<form id='gilMarketForm'><img class='th_icon' src='" + getBasePath("image") + "/ffxiv/Gil.png'/> " +
            " <input name='MarketGil' type='number' step='0' value='0' style='width: 168px;'/>" +
            " <button id='gilMarketUpdateButton' type='submit'>mkt</button>" +
            "</form>" +
//            "<a href='" + doCh("3", "ffxivGilWorthByDay", null) + "' target='qCh'><img class='ch_small' src='" + doCh("j", "ffxivGilByDay", "th") + "'/></a><br/>" +
			"<div class='ch_small'><canvas id='ffGilHolder'></canvas></div>" +
            "[<a href='" + doCh("3", "ffxivGilWorthByDay", null) + "' target='qCh'>Chart</a>]" +
            "</div></div>" +
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
            "<strong>FATEs:</strong> " + fateCounter + " of " + counts.FATEs + " (" + ((fateCounter/fCount)*100).toFixed(1) + "%)<br/>" +
            "<strong>Gathering:</strong> " + gatherCounter + " of " + counts.Gathering + " (" + ((gatherCounter/gCount)*100).toFixed(1) + "%)<br/>" +
            "<strong>Hunting:</strong> " + huntCounter + " of " + counts.Hunting + " (" + ((huntCounter/hCount)*100).toFixed(1) + "%)<br/>" +
            "<strong>Items:</strong> " + counts.Items + "<br/>" +
            "<strong>Quests:</strong> " + compCounter + " of " + qCount + " (" + ((compCounter/qCount)*100).toFixed(1) + "%)<br/>" +
//			"<strong>Weapons:</strong> " + counts.Weapons + "<br/>" +
//			"<strong>Wearables:</strong> " + counts.Wearables + "<br/>" +
            "</div></div><br/>" +
//			"<img class='ch_small' src='" + doCh("j", "ffxivQuestsByDay", "th") + "'/></a>" +
			"<div class='ch_small'><canvas id='qbdHolder'></canvas></div>" +
            "[<a href='" + doCh("3", "ffxivQuestsByDay", null) + "' target='qCh'>Chart</a>]" +
            "<p><div id='mSearchHolder'></div>" +
            "<p><div id='mergedList'></div>";
    dojo.byId(target).innerHTML = rData;
    putFfxivMergedSearchBox("mSearchHolder");
    putFfxivMergedList("mergedList", mergedData);
    getWebLinks("ffxivChars", "charList", "list");
    var setGilButton = dojo.byId("gilUpdateButton");
    var setMarketGilButton = dojo.byId("gilMarketUpdateButton");
    dojo.connect(setGilButton, "onclick", actOnFfxivGil);
    dojo.connect(setMarketGilButton, "onclick", actOnFfxivMarketGil);
    dojo.query(".doLevelIncrease").connect("onclick", actOnFfxivLevelIncrease);
	ch_get_ffxivQuestsByDay("qbdHolder", "thumb");
	ch_get_ffxivGilWorthByDay("ffGilHolder", "thumb");
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
            .post(getResource("FFXIV"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    aniPreload("off");
                    showNotice("Achievement " + formData.Name + " complete!");
                    getGameFf14q(target, false);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request to set Achievement Complete FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function setFfxivAssetAdd(formData) {
    var target = "ETGFF14Q";
    aniPreload("on");
    var thePostData = {
        "doWhat": "setFfxivAssetAdd",
        "assetName": formData.DEFINE,
        "assetValue": formData.DEFINE,
        "assetWriteOff": formData.DEFINE
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("FFXIV"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    aniPreload("off");
                    showNotice("Asset " + formData.Name + " added!");
                    getGameFf14q(target, false);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request to set Asset FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
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
            .post(getResource("FFXIV"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    aniPreload("off");
                    showNotice("Crafting " + formData.Name + " complete!");
                    getGameFf14q(target, false);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request to set Crafting Complete FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function setFfxivDungeonClear(formData) {
    var target = "ETGFF14Q";
    aniPreload("on");
    var thePostData = {
        "doWhat": "setFfxivDungeonClear",
        "dungeonCode": formData.QuestOrder
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("FFXIV"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    aniPreload("off");
                    showNotice("Dungeon " + formData.Name + " cleared!");
                    getGameFf14q(target, false);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request to set Dungeon Clear FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
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
            .post(getResource("FFXIV"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    aniPreload("off");
                    showNotice("Dungeon " + formData.Name + " complete!");
                    getGameFf14q(target, false);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request to set Dungeon Complete FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function setFfxivFateDone(formData) {
    var target = "ETGFF14Q";
    aniPreload("on");
    var thePostData = {
        "doWhat": "setFfxivFateDone",
        "fateCode": formData.QuestOrder
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("FFXIV"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    aniPreload("off");
                    showNotice("FATE " + formData.Name + " complete!");
                    getGameFf14q(target, false);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request to set FATE Complete FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
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
            .post(getResource("FFXIV"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    aniPreload("off");
                    showNotice("Gathering " + formData.Name + " complete!");
                    getGameFf14q(target, false);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request to set Gathering Complete FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function setFfxivGil(formData) {
    var target = "ETGFF14Q";
    aniPreload("on");
    var thePostData = {
        "doWhat": "setFfxivGil",
        "gil": formData.Gil
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("FFXIV"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    aniPreload("off");
                    showNotice("Gil " + formData.Name + " updated!");
                    getGameFf14q(target, false);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request to set Gil FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
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
            .post(getResource("FFXIV"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    aniPreload("off");
                    showNotice("Hunting " + formData.Name + " complete!");
                    getGameFf14q(target, false);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request to set Hunting Complete FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function setFfxivLevelsIncrease(formData) {
    var target = "ETGFF14Q";
    aniPreload("on");
    var thePostData = {
        "doWhat": "setFfxivLevelsIncrease",
        "classCode": formData.ClassCode
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("FFXIV"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    aniPreload("off");
                    showNotice("Level " + formData.ClassCode + " increased by 1!");
                    getGameFf14q(target, false);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request to set Level Inrease FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}

function setFfxivMarketGil(formData) {
    var target = "ETGFF14Q";
    aniPreload("on");
    var thePostData = {
        "doWhat": "setFfxivMarketGil",
        "gil": formData.MarketGil
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("FFXIV"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    aniPreload("off");
                    showNotice("Market " + formData.Name + " updated!");
                    getGameFf14q(target, false);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request to set Market Gil FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
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
            .post(getResource("FFXIV"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    aniPreload("off");
                    showNotice("Quest " + formData.Name + " complete!");
                    getGameFf14q(target, false);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request to set Quest Complete FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}
