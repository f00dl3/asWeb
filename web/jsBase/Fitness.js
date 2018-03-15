/* 
by Anthony Stump
Created: 14 Feb 2018
Updated: 15 Feb 2018
 */

var myHeight = 68;

function colorCalories(tValue) {
    switch(tValue) {
        case inRange(tValue, 0, 1799): return 'FtCL2000';
        case inRange(tValue, 1800, 1999): return 'FtCL2200';
        case inRange(tValue, 2000, 2199): return 'FtCL2400';
        case inRange(tValue, 2200, 2399): return 'FtCL2600';
        case inRange(tValue, 2400, 2599): return 'FtCL2800';
        case inRange(tValue, 2600, 2799): return 'FtCL3000';
        case inRange(tValue, 2800, 9999): return 'FtCG3000';
    }
}

function colorShoeMile(rsm) {
    switch (true) {
        case (rsm <= 29.9): return 'FtSL030';
        case inRange(rsm, 30, 59.9): return 'FtSL060';
        case (rsm >= 60) && (rsm <= 89.9): return 'FtSL090';
        case (rsm >= 90) && (rsm <= 119.9): return 'FtSL120';
        case (rsm >= 120) && (rsm <= 149.9): return 'FtSL150';
        case (rsm >= 150) && (rsm <= 179.9): return 'FtSL180';
        case (rsm >= 180) && (rsm <= 209.9): return 'FtSL210';
        case (rsm >= 210) && (rsm <= 239.9): return 'FtSL240';
        case (rsm >= 240) && (rsm <= 269.9): return 'FtSL270';
        case (rsm >= 270) && (rsm <= 299.9): return 'FtSL300';
        case (rsm >= 300) && (rsm <= 329.9): return 'FtSL330';
        case (rsm >= 330) && (rsm <= 359.9): return 'FtSL360';
        case (rsm >= 360) && (rsm <= 389.9): return 'FtSL390';
        case (rsm >= 390) && (rsm <= 419.9): return 'FtSL420';
        case (rsm >= 420) && (rsm <= 449.9): return 'FtSL450';
        case (rsm >= 450): return 'FtSG450';
    }
}

function colorWeight(tValue) {
    switch(tValue) {
        case inRange(tValue, 0, 144.9): return 'FtWL1475';
        case inRange(tValue, 145.0, 147.4): return 'FtWL1500';
        case inRange(tValue, 147.5, 149.9): return 'FtWL1525';
        case inRange(tValue, 150.0, 152.4): return 'FtWL1550';
        case inRange(tValue, 152.5, 154.9): return 'FtWL1575';
        case inRange(tValue, 155.0, 157.4): return 'FtWL1600';
        case inRange(tValue, 157.5, 159.9): return 'FtWL1625';
        case inRange(tValue, 160.0, 162.4): return 'FtWL1650';
        case inRange(tValue, 162.5, 164.9): return 'FtWG1650';
        case inRange(tValue, 165.0, 999.9): return 'FtCG3000';
    }
}

function getAllFitnessData() {
    var fitnessResource = getBasePath("rest") + "/Fitness";
    var dateOverrideStart = getDate("day", -365, "dateOnly"); 
    var dateOverrideEnd = getDate("day", 0, "dateOnly");
    var thePostData = "doWhat=getAll" +
        "&XDT1=" + dateOverrideStart +
        "&XDT2=" + dateOverrideEnd;
    var arGetAll = {
        preventCache: true,
        url: fitnessResource,
        postData: thePostData,
        handleAs: "json",
        timeout: timeOutMilli,
        load: function(data) {
            processFitnessAll(data);
        },
        error: function(data, iostatus) {
            window.alert("xhrGet for arGetAll: FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
        }
    };
    dojo.xhrPost(arGetAll);
}

function getMapLinkString(inDate, inType, inAct, commonFlag) {
    var longAct, iconBack, typeDesc, letAct, icAct;
    longAct = iconBack = typeDesc = letAct = icAct = " "; 
    switch(inAct) {
        case "Alt": longAct = "Alternate"; letAct = "A"; icAct = "Run"; break;
        case "Run": longAct = "Running"; letAct = "R"; icAct = inAct; break;
        case "Ru2": longAct = "Running 2"; letAct = "A"; icAct = "Run"; break;
        case "Cyc": longAct = "Cycling"; letAct = "C"; icAct= inAct; break;
        case "Cy2": longAct = "Cycling 2"; letAct = "A"; icAct = "Cyc"; break;
    }
    switch(inType) {
        case "gpsJSON": iconBack = getBasePath("ui") + "/img/Icons/ic_" + icAct.toLowerCase() + "J.jpeg"; typeDesc = "GPS JSON"; letAct = inAct; break;
        case "Route": iconBack = getBasePath("ui") + "/img/Icons/ic_" + icAct.toLowerCase() + ".jpeg"; typeDesc = "GeoJSON"; break;
    }
    var genString = "<a href='" + getBasePath("old") + "/OutMap.php?Title=" + inDate + "&" + inType + "=" + letAct + "' target='new'>" +
        "<div class='UPop'><img class='th_icon' src='" + iconBack + "' />" +
        "<div class='UPopO'>" + longAct + " " + typeDesc + " data." + commonFlag + "</div>" +
        "</div></a>";
    return genString;
}

function processFitnessAll(dataIn) {
        var fitTableDefs = [
        "Date",
        "Weight",
        "RunWalk",
        "Cycling",
        "Cals",
        "Extra"
    ];
    var rData = "<tr>";
    fitTableDefs.forEach(function(thisDef) {
       rData += "<th>" + thisDef + "</th>";
    });
    rData += "</tr>";
    for(var i=0; i<dataIn.length; i++) {
        var tData = dataIn[i];
        var bkStuds, gym, reelMow, commonRouteFlag, cycMap, runMap, altMap;
        bkStuds = gym = reelMow = commonRouteFlag = cycMap = runMap = altMap = " ";
        var bmi = 'BMI of' + ((tData.Weight*703)/(myHeight*myHeight)).toFixed(1);
        if(!tData.RunWalk) { tData.RunWalk = " "; }
        if(!tData.Cycling) { tData.Cycling = " "; }
        if(!tData.Calories) { tData.Calories = " "; }
        if(tData.BkStudT === 1) { bkStuds = " style='background: blue; color: white;'"; }
        if(tData.Gym === 1) {
            gym = "<div class='UPop'><img class='th_icon' src='" + getBasePath("ui") + "/img/Icons/ic_lst.jpeg'/>" +
                    "<div class='UPop'>" + tData.GymWorkout + "</div></div>";
        }
        if(tData.ReelMow === 1) {
            reelMow = "<div class='UPop'><img class='th_icon' src='" + getBasePath("ui") + "/img/Icons/ic_mow.jpg'>" +
                    "<div class='UPopO'>" + tData.MowNotes + "</div></div>";
        }
        if(tData.CommonRoute === 1) { commonRouteFlag = "<br/><strong>Common Route!</strong>"; }
        var cycDiv = "<div class='table'>" +
            "<div class='tr'><span class='td'>Bicycle</span><span class='td'>" + tData.Bicycle + "</span></div>" +
            "<div class='tr'><span class='td'>Saved $</span><span class='td'>" + (tData.Cycling * costPerMile).toFixed(2) + "</span></div>";
        if(isSet(tData.CycSpeedAvg)) {
            cycDiv += "<div class='tr'>" +
                    "<span class='td'>Speed<br/>mph</span>" +
                    "<span class='td'>" + tData.CycSpeedAvg.toFixed(1) + "<br/>" +
                    tData.CycSpeedMax.toFixed(1) + "</span>" +
                    "</div>";
        }
        cycDiv += "</div>";
        if(isSet(tData.CycGeoJSON) && tData.CommonRoute === 0) { cycMap += getMapLinkString(tData.Date, "Route", "Cyc", tData.CommonFlag); }
        if(tData.isGPSCycJSON === true) { cycMap = getMapLinkString(tData.Date, "gpsJSON", "Cyc", tData.CommonFlag); }
        var runDiv = "Wearing " + tData.Shoe;
        if(isSet(tData.RSMile)) { runDiv = tData.RSMile + " miles wearing " + tData.Shoe; }
        if(isSet(tData.RunGeoJSON) && tData.CommonRoute === 0) { runMap += getMapLinkString(tData.Date, "Route", "Run", tData.CommonFlag); }
        if(tData.isGPSRunJSON === true) { runMap = getMapLinkString(tData.Date, "gpsJSON", "Run", tData.CommonFlag); }
        var altMap = " ";
        if(isSet(tData.AltGeoJSON) && tData.CommonRoute === 0) { altMap += getMapLinkString(tData.Date, "Route", "Alt", tData.CommonFlag); }
        if(tData.isGPSCyc2JSON === true) { altMap = getMapLinkString(tData.Date, "gpsJSON", "Cy2", tData.CommonFlag); }
        if(tData.isGPSRun2JSON === true) { altMap = getMapLinkString(tData.Date, "gpsJSON", "Ru2", tData.CommonFlag); }
        var nutriBreakdown = "Breakdown unavailable!";
        var shoeClass = "";
        if(isSet(tData.RSMile)) { shoeClass = colorShoeMile(tData.RSMile); }
        if(isSet(tData.Fat)) {
            nutriBreakdown = "<table><tbody><tr><td>" +
                    "<table><thead><tr><th colspan=3>Consumption</th></tr></thead>" +
                    "<tbody>" +
                    "<tr><td>Fat</td><td>" + tData.Fat + "</td><td>" + Math.round((tData.Fat/(65*1.25) * 100)) + "%</td></tr>" +
                    "<tr><td>Cholest</td><td>" + tData.Cholest + "</td><td>" + Math.round((tData.Cholest/(300*1.25)) * 100) + "%</td></tr>" +
                    "<tr><td>Sodium</td><td>" + tData.Sodium + "</td><td>" + Math.round((tData.Sodium/(2400*1.25)) * 100) + "%</td></tr>" +
                    "<tr><td>Carbs</td><td>" + tData.Carbs + "</td><td>" + Math.round((tData.Carbs/(300*1.25)) * 100) + "%</td></tr>" +
                    "<tr><td>Fiber</td><td>" + tData.Fiber + "</td><td>" + Math.round((tData.Fiber/(25*1.25)) * 100) + "%</td></tr>" +
                    "<tr><td>Sugar</td><td>" + tData.Sugar + "</td><td>" + Math.round((tData.Sugar/200) * 100) + "%</td></tr>" +
                    "<tr><td>Protein</td><td>" + tData.Protein + "</td><td>" + Math.round((tData.Protein/(tData.Weight * 0.8)) * 100) + "%*</td></tr>" +
                    "</tbody></table>" +
                    "</td><td>" +
                    "<table><thead><tr><th colspan=3>Calories from...</th></thead><tbody>" +
                    "<tr><td>Fat</td><td>" + (tData.Fat * 9) + "</td><td>~" + Math.round(((tData.Fat * 9)/tData.Calories) * 100) + "%</td></tr>" +
                    "<tr><td>Carbs</td><td>" + (tData.Carbs * 4) + "</td><td>~" + Math.round(((tData.Carbs * 4)/tData.Calories) * 100) + "%</td></tr>" +
                    "<tr><td>Protein</td><td>" + (tData.Protein * 4) + "</td><td>~" + Math.round(((tData.Protein * 4)/tData.Calories) * 100) + "%</td></tr>" +
                    "</tbody></table><p>" +
                    "<strong>Liquids:</strong> " + tData.Water + "<br/>" +
                    "<strong>Fruits/Veggies:</strong> " + tData.FruitsVeggies + "<p>" +
                    "<em>Based off 2500 cals</em></td></tr></tbody></table>";
        }
        var extra = " ";
        if(isSet(tData.xTags)) {
            extra = "<div class='UPop'><img class='th_icon' src='" + getBasePath("ui") + "/img/Icons/cfa.x'/>" +
                    "<div class='UPopO'>" + tData.xTags + "</div>" +
                    "</div>";
        }
        rData += "<tr>";
        rData += "<td><div class='UPop'>" + tData.Date +
                "<div class='UPopO'>" +
                "High: <span class='" + colorTemp(tData.High) + "'>" + tData.High + "</span><br/>" +
                "Low: <span class='" + colorTemp(tData.Low) + "'>" + tData.Low + "</span><br/>" +
                "Average: <span class='" + colorTemp(tData.Average) + "'>" + tData.Average + "</span>" +
                "</div></div></td>" +
                "<td align='center'><div class='UPop'><div class='" + colorWeight(tData.Weight) + "'>" + tData.Weight + "</div>" +
                "<div class='UPopO'>" + bmi + "</div>" +
                "</div></td>" +
                "<td align='center' class='" + shoeClass + "'><div class='UPop'>" + tData.RunWalk +
                "<div class='UPopO'>" + runDiv + "</div></div></td>" +
                "<td align='center' " + bkStuds + "><div class='UPop'>" + tData.Cycling +
                "<div class='UPopO'>" + cycDiv + "</div></div></td>" +
                "<td align='center'><div class='UPop'><div class='" + colorCalories(tData.Calories) + "'>" + tData.Calories + "</div>" + 
                "<div class='UPopO'>" + nutriBreakdown + "</div></div></td>" +
                "<td>" + cycMap + runMap + altMap + gym + reelMow;
        if(isSet(hiddenFeatures)) { rData += extra; }
        rData += "</td></tr>";
    }
    dojo.byId("fitnessTable").innerHTML = rData;
}

var init = function(event) {
    getAllFitnessData();
};

dojo.ready(init);