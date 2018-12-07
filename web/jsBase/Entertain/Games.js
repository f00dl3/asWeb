/* 
by Anthony Stump
Created: 25 Mar 2018
Split off from Entertain.js: 10 Apr 2018
Updated: 6 Dec 2018
 */

function actOnPlayedGameHours(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject(this.form);
    setGameHours(thisFormData);
}

function displayGames() {
    gameButtonListeners();
    $("#ETGameAll").toggle();
    $("#ETLego").hide();
    $("#ETReddit").hide();
    $("#ETCooking").hide();
    $("#ETStream").hide();
}

function displayGameFlash() {
    var target = "ETGFlash";
    dojo.byId(target).innerHTML = "Flash games to be put here!";
    $("#ETGFF14C").hide();
    $("#ETGFF14D").hide();
    $("#ETGFF14I").hide();
    $("#ETGFF14Q").hide();
    $("#ETGHours").hide();
    $("#ETGIndex").hide();
    $("#ETGHours").hide();
}

function displayGameHours() {
    var target = "ETGHours";
    getGameData(target);
    $("#ETGFF14C").hide();
    $("#ETGFF14D").hide();
    $("#ETGFF14I").hide();
    $("#ETGFF14Q").hide();
    $("#ETGFlash").hide();
    $("#ETGIndex").hide();
}

function displayGameIndex() {
    var target = "ETGIndex";
    getGameIndex(target);
    $("#ETGFF14C").hide();
    $("#ETGFF14D").hide();
    $("#ETGFF14I").hide();
    $("#ETGFF14Q").hide();
    $("#ETGFlash").hide();
    $("#ETGHours").hide();
}

function gameButtonListeners() {
    var btnShowFlash = dojo.byId("ShETGFlash");
    var btnShowHours = dojo.byId("ShETGHours");
    var btnShowIndex = dojo.byId("ShETGIndex");
    var btnShowFF14C = dojo.byId("ShETGFF14C");
    var btnShowFF14D = dojo.byId("ShETGFF14D");
    var btnShowFF14I = dojo.byId("ShETGFF14I");
    var btnShowFF14Q = dojo.byId("ShETGFF14Q");
    dojo.connect(btnShowFlash, "click", displayGameFlash);
    dojo.connect(btnShowHours, "click", displayGameHours);
    dojo.connect(btnShowIndex, "click", displayGameIndex);
    dojo.connect(btnShowFF14C, "click", displayGameFf14c);
    dojo.connect(btnShowFF14D, "click", displayGameFf14d);
    dojo.connect(btnShowFF14I, "click", displayGameFf14i);
    dojo.connect(btnShowFF14Q, "click", displayGameFf14q);
}

function getGameData(target) {
    var timeout = 2 * 60 * 1000;
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
    setTimeout(function () { getGameData(target); }, timeout);
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

function putGameHours(target, gameHoursTotal, latest, gameHours) {
    var ghInWidth = 35;
    var thCols = [ "Do", "Name", "Hours", "New" ];
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
    ghe += "<form class='tr'>" +
            "<span class='td'><input type='checkbox' class='doHours'/></span>" +
            "<span class='td'>" + latest.Name + playing + "<input type='hidden' name='gameName' value='" + latest.Name + "'/></span>" +
            "<span class='td'><div class='UPop'>" + latest.Hours +
            "<div class='UPopO'>Last played: " + latest.Last +
            "</div></div></span>" +
            "<span class='td'>" +
            "<input type='number' step='1' style='width: 35px;' value='' name='playedHours'/>h " +
            "<input type='number' step='1' style='width: 35px;' value='' name='playedMinutes'/>m" +
            "</span>" +
            "</form>";
    rData += ghe + "<div class='UPopNMO'>";
    var standings = 0;
    gameHours.forEach(function (game) {
        standings++;
        var playing = "";
        if(game.Active === 1) { playing = "<strong> (Playing!)</strong>)"; }
        gheA += "<form class='tr'>" +
                "<span class='td'><input type='checkbox' class='doHours'/></span>" +
                "<span class='td'><span style='color: yellow;'>" + standings + ":</span> " + game.Name + playing + "<input type='hidden' name='gameName' value='" + game.Name + "'/></span>" +
                "<span class='td'><div class='UPop'>" + game.Hours +
                "<div class='UPopO'>Last played: " + game.Last +
                "</div></div></span>" +
                "<span class='td'>" +
                "<input type='number' step='1' style='width: " + ghInWidth + "px;' value='' name='playedHours'/>h " +
                "<input type='number' step='1' style='width: " + ghInWidth + "px;' value='' name='playedMinutes'/>" +
                "</span>" +
                "</form>";
    });
    gheA += "</div>";
    rData += gheA + "</div></div></span>";
    dojo.byId(target).innerHTML = rData;
    dojo.query(".doHours").connect("onchange", actOnPlayedGameHours);
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

function setGameHours(formData) {
    var target = "ETGHours";
    aniPreload("on");
    var playedMinutes = 0;
    if(formData.playedMinutes) { playedMinutes = Number(playedMinutes + formData.playedMinutes); }
    if(formData.playedHours) { playedMinutes = Number(playedMinutes + (60 * formData.playedHours)); }
    var thePostData = {
        "doWhat": "setPlayedGameHours",
        "minutes": playedMinutes,
        "game": formData.gameName
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Entertainment"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    aniPreload("off");
                    showNotice(formData.gameName + " for " + (playedMinutes/60).toFixed(1) + " hr!");
                    getGameData(target);
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request to set Update Game Hours FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}
