/* 
by Anthony Stump
Created: 25 Mar 2018
Updated: 27 Mar 2018
 */

function putCooking(cookingData) {
    var recCols = [ "Recipie", "Guide" ];
    var rData = "<table><thead><tr>";
    for (var i = 0; i < recCols.length; i++) { rData += "<th>" + recCols[i] + "</th>"; }
    rData += "</thead><tbody>";
    cookingData.forEach(function (cook) {
        var ddIngredients = (cook.Ingredients).replace("- ", "<br/>");
        var ddInstructions = (cook.Instructions).replace("- ", "<br/>");
        rData += "<tr>" +
                "<td>" + cook.Description + "</td>" +
                "<td><div class='UPop'><img class='th_icon' src='" + getBasePath("icon") + "/ic_cup.png'/>" +
                "<div class='UPopO'>" + ddIngredients + "</div></div>" +
                "<div class='UPop'><img class='th_icon' src='" + getBasePath("icon") + "/ic_lst.jpeg'/>" +
                "<div class='UPopO'>" + ddInstructions + 
                "<p>Bake at " + cook.CookTemp + "F for " + cook.CookTime + " mins.</div></div>" +
                "</td></tr>";
    });
    rData += "</tbody></table>";
    dojo.byId("ETCooking").innerHTML = rData;
}

function putFfxivQuests(ffxivQuests) {
    var charProfLink = "https://na.finalfantasyxiv.com/loadstone/character/20659030";
    var qCount = ffxivQuests.length;
    var qCols = [ "Up", "Name", "LV" ];
    var rData = "<a href='" + charProfLink + "' target='new'>Foodle Faddle</a>" +
            "<h3>Quests</h3><strong>Indexed quests: " + qCount + 
            "<p><div class='table'><div class='tr'>";
    for (var i = 0; i < qCols.length; i++) { rData += "<span class='th'>" + qCols[i] + "</span>"; }
    rData += "</div>";
    ffxivQuests.forEach(function (ff14q) {
        var qComplete = "No";
        var fontColor = "White";
        var tdsStyle = "style='color: " + fontColor + ";'";
        var updateCheckbox = "<input class='ffxivQuestDone' type='checkbo' name='qUpdate' value='" + ff14q.QuestOrder + "'/></span>";
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
    });
    rData += "</div>";
    dojo.byId("FFXIVQuests").putHTML = rData;
}

function putGameHours(gameHoursTotal, gameHoursLatest, gameHours) {
    var thCols = [ "Name", "Hours" ];
    var ghe, gheA;
    ghe = gheA = "<div class='table'><div class='tr'>";
    var totalHoursDiv = "<div class='UPop'>Total Hours<br/><span>" + gameHoursTotal.TotalHours + "</span></div>";
    var rData = totalHoursDiv +
            "<strong>" +
            "<a href='" + getBasePath("old") + "/Download/GameLauncher.zip'>Launcher (Linux/Bash)</a></strong>" +
            "<span class='UPopNM'>" +
            "<p>Most recent:</b>";
    
    for (var i = 0; i < thCols.length; i++) {
        ghe += "<span class='th'>" + thCols[i] + "</span>";
        gheA += "<span class='th'>" + thCols[i] + "</span>";
    }
    ghe += "</div>";
    gameHoursLatest.forEach(function (latest) {
        var playing = "";
        if(latest.Active === 1) { playing = "<strong> (Playing!)</strong>)"; }
        ghe += "<div class='tr'>" +
                "<span class='td'>" + latest.Name + playing + "</span>" +
                "<span class='td'><div class='UPop'>" + latest.Hours +
                "<div class='UPopO'>Last played: " + latest.Last +
                "</div></div></span>" +
                "</div>";
    });
    rData += ghe + "<div class='UPopNMO'><p>All:";
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
    dojo.byId("GameHours").innerHTML = rData;
}

function putGameIndex(gameIndex) {
    var giCols = [ "Title", "Size<br/>GiB", "Linux<br/>Tested" ];
    var giTable = "<div class='table'><div class='tr'>";
    for(var i = 0; i < giCols.length; i++) { giTable += "<span class='th'>" + giCols[i] + "</span>"; }
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
}

function callMediaServ() {
    var rData = "<div id='SearchTableHolder'></div>";
    return rData;
}

function initEntertain() {
    
}

dojo.ready(initEntertain);

