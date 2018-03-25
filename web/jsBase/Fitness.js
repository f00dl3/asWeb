/* 
by Anthony Stump
Created: 14 Feb 2018
Updated: 25 Feb 2018
 */

var bicycleUsed = "A16";
var myHeight = 68;

function actOnCaloriesSubmit(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject("CaloriesForm");
    putCalories(thisFormData);
}

function actOnCommitRoute(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject("RoutePlanForm");
    window.alert("Commit Route button pressed!\n" + dojo.formToJson("RoutePlanForm"));
    putRoute(thisFormData);
}

function actSearchByDateSubmit(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject("SearchByDateForm");
    var xdt1 = thisFormData.FitSearchStart;
    var xdt2 = thisFormData.FitSearchEnd;
    getFitnessAllData(xdt1, xdt2);
}

function actUpdateTodaySubmit(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject("FormUpdateToday");
    putUpdateToday(thisFormData);
}

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

function fitnessBubbles(bikeStats, overallStats, fitTot, crsm, rshoe, autoMpg, bikeInfo, yearStats) {
    var yb0 = getDate("year", 0, "yearOnly");
    var yb1 = yb0 - 1;
    var yb2 = yb0 - 2;
    var yb3 = yb0 - 3;
    var yb4 = yb0 - 4;
    var avgPace = (overallStats.TT / overallStats.TD);
    var costPerMileTco = 14000/((autoMpg.EndMiles - 47500) + 60000);
    var costPerMile = costPerMileTco + cpmNoMpg + (autoMpg.AvgCost / ((autoMpg.EndMiles - autoMpg.StartMiles) / autoMpg.Gallons));
    var costPerGallon = autoMpg.AvgCost;
    var estSteps = ((fitTot.TOTRW * 1508.57)/1000000).toFixed(2);
    var bikeStats = {
        "Bicycle": bicycleUsed + "<br/>" + bikeInfo.Purchased + "<br/>" + bikeStats.MilesBike + " mi",
        "Saved": "$" + (costPerMile * fitTot.TOTCY).toFixed(2),
        "Cleaned": bikeStats.LastCleaned,
        "LastFlat": bikeStats.LastFlat,
        "Overhauled": bikeStats.LastOverhaul + "<br/>" + bikeStats.MilesOverhaul + " mi",
        "Chain": bikeStats.LastChain + "<br/>" + bikeStats.MilesChain + " mi",
        "TireFront": bikeStats.LastTireFront + "<br/>" + bikeStats.MilesTireFront + " mi",
        "TireRear": bikeStats.LastTireRear + "<br/>" + bikeStats.MilesTireRear + " mi",
        "StuddedFront": bikeStats.LastTireFrontStudded + "<br/>" + bikeStats.MilesTireFrontStudded + " mi",
        "StuddedRear": bikeStats.LastTireRearStudded + "<br/>" + bikeStats.MilesTireRearStudded + " mi",
        "WheelFront": bikeStats.LastWheelFront + "<br/>" + bikeStats.MilesWheelFront + " mi",
        "WheelRear": bikeStats.LastWheelRear + "<br/>" + bikeStats.MilesWheelRear + " mi"
    };
    var boxRun = " <div class='UBox'>" +
            "<span>RunWalk</span><br/>" + fitTot.TOTRW + "<br/><span>miles</span>" +
            "<div class='UBoxO'>" +
            yb0 + ": <strong>" + yearStats.yb0rw + "</strong> mi<br/>" +
            yb1 + ": <strong>" + yearStats.yb1rw + "</strong> mi<br/>" +
            yb2 + ": <strong>" + yearStats.yb2rw + "</strong> mi<br/>" +
            yb3 + ": <strong>" + yearStats.yb3rw + "</strong> mi<br/>" +
            yb4 + ": <strong>" + yearStats.yb4rw + "</strong> mi<br/>" +
            "<p>Average pace: <br/><strong>" + avgPace.toFixed(1) + "</strong> min/mile." +
            "<p>Running shoes: <br/><strong>" + crsm.CRSM + "</strong> miles on <strong>" + rshoe.Pair + "</strong>" +
            "<p>Est. steps: <br/><strong>" + estSteps + "</strong> mil." +
            "</div></div>";
    var boxCyc = " <div class='UBox'>" +
            "<span>Cycling</span><br/>" + fitTot.TOTCY + "<br/><span>miles</span>" +
            "<div class='UBoxO'>" +
            yb0 + ": <strong>" + yearStats.yb0cy + "</strong> mi<br/>" +
            yb1 + ": <strong>" + yearStats.yb1cy + "</strong> mi<br/>" +
            yb2 + ": <strong>" + yearStats.yb2cy + "</strong> mi<br/>" +
            yb3 + ": <strong>" + yearStats.yb3cy + "</strong> mi<br/>" +
            yb4 + ": <strong>" + yearStats.yb4cy + "</strong> mi<br/>" +
            "<p><em>Based on $<strong>" + costPerGallon.toFixed(2) + "</strong>/gal<br/>" +
            "and $<strong>" + annMaint.toFixed(2) + "</strong>/yr maint</em><p>" +
            "<div class='table'>";
    for(var key in bikeStats) {
        boxCyc += "<div class='tr'><span class='td'>" + key + "</span><span class='td'>" + bikeStats[key] + "</span></div>";
    }
    boxCyc += "</div>" +
            "</div></div>";
    var boxTot = " <div class='UBox'>" +
            "<span>Combined</span><br/>" + fitTot.TOTOA + "<br/><span>miles</span>" +
            "<div class='UBoxO'>" +
            yb0 + ": <strong>" + yearStats.yb0oa + "</strong>mi<br/>" +
            yb1 + ": <strong>" + yearStats.yb1oa + "</strong> mi<br/>" +
            yb2 + ": <strong>" + yearStats.yb2oa + "</strong> mi<br/>" +
            yb3 + ": <strong>" + yearStats.yb3oa + "</strong> mi<br/>" +
            yb4 + ": <strong>" + yearStats.yb4oa + "</strong> mi<br/>" +
            "<p><a href='" + getBasePath("old") + "/OutMap.php?AllRoutes=1'>" +
            "<button class='UButton'>All Routes</button></a>" +
            "</div></div>";
    var returnData = boxRun + boxCyc + boxTot;
    dojo.byId("FitBubbleHolder").innerHTML = returnData;
}

function fitnessCalories(calQ) {
    var foods = 1;
    var tableRows = [ "Food", "Servings", "Today", "Serving", "Calories" ];
    var dataBack = "<div class='UBox'>Food<div class='UBoxO'>Calorie Tracker" +
            "<form id='CaloriesForm'>" +
            "<button class='UButton' id='CalSubmitButton' type='submit' name='SubmitServings'>Nom nom nom!</button><p>";
    var tableElement = "<table><thead><tr>";
    for (var i=0; i < tableRows.lenght; i++) {
        tableElement += "<th>" + tableRows[i] + "</th>";
    }
    tableElement += "</tr></thead><tbody>";
    calQ.forEach(function (cDat) {
        var slSet = "";
        if(isSet(cDat.ThisServingsLast)) { slSet = cDat.ThisServingsLast; }
        tableElement += "<tr><input type='hidden' name='FoodID[" + foods + "]' value='" + foods + "' />" +
                "<td><input type='hidden' name='FoodDescription[" + foods + "]' value='" + cDat.Food + "' />" +
                "<div class='U2Pop'>" + cDat.Food + "<div class='UPopO'>Last consumed: " + cDat.Last + "</div></div></td>" +
                "<td><input type='number' step='0.1' name='Quantity[" + foods + "]' value = '" + slSet + "' style='width: 34px;'/></td>" +
                "<td>" + cDat.Serving + "</td>" +
                "<td><input type='hidden' name='Calories[" + foods + "]' value='" + cDat.Calories + "'/>" + cDat.Calories + "</td>" +
                "<input type='hidden' name='Fat[" + foods + "]' value='" + cDat.Fat + "' />" +
                "<input type='hidden' name='Carbs[" + foods + "]' value='" + cDat.Carbs + "' />" +
                "<input type='hidden' name='Protein[" + foods + "]' value='" + cDat.Protein + "' />" +
                "<input type='hidden' name='Sodium[" + foods + "]' value='" + cDat.Sodium + "' />" +
                "<input type='hidden' name='Cholest[" + foods + "]' value='" + cDat.Cholest + "' />" +
                "<input type='hidden' name='Sugar[" + foods + "]' value='" + cDat.Sugar + "' />" +
                "<input type='hidden' name='Fiber[" + foods + "]' value='" + cDat.Fiber + "' />" +
                "<input type='hidden' name='Water[" + foods + "]' value='" + cDat.Water + "' />" +
                "<input type='hidden' name='FruitVeggie[" + foods + "]' value='" + cDat.FruitVeggie + "' />" +
                "</tr>";
                foods++;
    });
    dataBack += tableElement + "</tbody></table>" +
            "</form>" +
            "</div></div>";
    dojo.byId("Calories").innerHTML = dataBack;
    var calSubmitButton = dojo.byId("CalSubmitButton");
    dojo.connect(calSubmitButton, "onclick", actOnCaloriesSubmit);
}

function fitnessPlans(dataIn) {
    var container = "<div class='UBox'>Plans<div class='UBoxO'>Planned Routes<p>" +
            "<form id='RoutePlanForm'><button class='UButton' id='CommitRouteButton' name='CommitRoutePlan' value='submit'>Completed</button><p>";
    var routeOptions = [ "RunGeoJSON", "CycGeoJSON" ];
    var routeId = 1;
    var routeDoneFlag;
    var tableData = "<table><thead><tr>";
    var tableDefs = [ "Do", "Description", "Link", "Type", "Done", "Dist" ];
    tableDefs.forEach(function(def) {
        tableData += "<th>" + def + "</th>";
    });
    tableData += "</tr></thead><tbody>";
    dataIn.forEach(function(tData) {
        var routeDistMi = (tData.DistKm * 0.621371).toFixed(1);
        var pRoute = tData.GeoJSON;
        var rpMap = "<a href='" + getBasePath("old") + "/OutMap.php?Title=" + tData.Description + "&Route=" + pRoute + "&KML=true'>Mapped</a>";
        if(tData.Done === 1) { routeDoneFlag = "Yes"; } else { routeDoneFlag = "No"; }
        tableData += "<tr><input type='hidden' name='RouteID[" + routeId + "]' value=" + routeId + ">" +
                "<td><input type='checkbox' name='RouteSetCommit[" + routeId + "]' value='Yes'/></td>" +
                "<td><input type='hidden' name='RouteDescription[" + routeId + "]' value='" + tData.Description + "'/>" + tData.Description + "</td>" +
                "<td>" + rpMap + "</td>" +
                "<td><select name='RouteType[" + routeId + "]'>";
        routeOptions.forEach(function(tType) {
            tableData += "<option value='" + tType + "'>" + tType.substring(0, 3) + "</option>";
        });
        tableData += "</select></td>" +
                "<td>" + routeDoneFlag + "</td>" +
                "<td>" + routeDistMi + "</td>" +
                "</tr>";
        routeId++;
    });
    tableData += "</tbody></table>";
    container += tableData + "</form></div></div>";
    dojo.byId("Plans").innerHTML = container;
    var commitRouteButton = dojo.byId("CommitRouteButton");
    dojo.connect(commitRouteButton, "onclick", actOnCommitRoute);
}

function fitnessToday(dataIn) {
    var studChecked, commonRouteChecked, runWalk, cycling, rsMile, weight, shoe, mowNotes, xTags;
    studChecked = commonRouteChecked = rsMile = "";
    if(!isSet(dataIn.Cycling)) { cycling = ""; } else { cycling = dataIn.Cycling; }
    if(!isSet(dataIn.Weight)) { weight = ""; } else { weight = dataIn.Weight; }
    if(!isSet(dataIn.RunWalk)) { runWalk = ""; } else { runWalk = dataIn.RunWalk; }
    if(!isSet(dataIn.Shoe)) { shoe = ""; } else { shoe = dataIn.Shoe; }
    if(!isSet(dataIn.MowNotes)) { mowNotes = ""; } else { mowNotes = dataIn.MowNotes; }
    if(!isSet(dataIn.xTags)) { xTags = ""; } else { xTags = dataIn.xTags; }
    if(dataIn.BkStudT === 1) { studChecked = "checked='checked'"; }
    if(dataIn.CommonRoute === 1) { commonRouteChecked = "checked='checked'"; }
    var holderData = "<div class='UBox'>Today" +
            "<div class='UBoxO'>Update Today<br/>" +
            "<form id='FormUpdateToday'><button class='UButton' id='MakeUpdates' type='submit'>Update</button>";
    var tableData = "<table><tbody>" +
            "<tr><td>Weight</td><td><input type='number' step='0.1' name='TodayWeight' value='" + weight + "'/></td></tr>" +
            "<tr><td>RunWalk</td><td><input type='number' step='0.1' name='TodayRunWalk' value='" + runWalk + "'/></td></tr>" +
            "<tr><td>Shoe</td><td><input type='text' name='TodayShoe' value='" + shoe + "'/></td></tr>" +
            "<tr><td>RSMile</td><td><input type='number' step='0.1' name='TodayRSMile' value='" + rsMile + "'/></td></tr>" +
            "<tr><td>Cycling</td><td><input type='number' step='0.1' name='TodayCycling' value='" + cycling + "'/><br/>" +
            "S<input type='checkbox' style='width: 15px;' name='TodayBkStudT' " + studChecked + "/>" +
            "C<input type='checkbox' style='width: 15px;' name='TodayCommonRoute' " + commonRouteChecked + "/></td></tr>" +
            "<input type='hidden' name='TodayBicycle' value='" + bicycleUsed + "'/>" +
            "<tr><td>Mowing</td><td><input type='text' name='TodayMowNotes' value='" + mowNotes + "'/></td></tr>" +
            "<tr><td>Other</td><td><input type='text' name='TodayX' value='" + xTags + "'/></td></tr>" +
            "</tbody></table>";
    holderData += tableData +
            "</form></div></div>";
    dojo.byId("Today").innerHTML = holderData;
    var formUpdateToday = dojo.byId("FormUpdateToday");
    dojo.connect(formUpdateToday, "onsubmit", actUpdateTodaySubmit);
}

function getFitnessAllData(inXdt1, inXdt2) {
    var xdt1, xdt2;
    var oYear = getDate("year", 0, "yearOnly");
    if(isSet(inXdt1)) { xdt1 = inXdt1; } else { xdt1 = getDate("day", -365, "dateOnly"); }
    if(isSet(inXdt2)) { xdt2 = inXdt2; } else { xdt2 = getDate("day", 0, "dateOnly"); }
    var thePostData = "doWhat=getAll&XDT1=" + xdt1 + "&XDT2=" + xdt2 + "&Bicycle=" + bicycleUsed + "&year=" + oYear;
    var xhArgs = {
        preventCache: true,
        url: getResource("Fitness"),
        postData: thePostData,
        handleAs: "json",
        timeout: timeOutMilli,
        load: function(data) {
            processFitnessAll(data.allRecs);
            fitnessCalories(data.calories);
            fitnessPlans(data.plans);
            fitnessToday(data.today[0]);
            fitnessBubbles(
                data.bkStats[0],
                data.overall[0],
                data.tot[0],
                data.crsm[0],
                data.rShoe[0],
                data.autoMpg[0],
                data.bkInf[0],
                data.yData[0]
            );
        },
        error: function(data, iostatus) {
            window.alert("xhrGet for All FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
        }
    };
    dojo.xhrPost(xhArgs);
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
        case "gpsJSON": iconBack = getBasePath("icon") + "/ic_" + icAct.toLowerCase() + "J.jpeg"; typeDesc = "GPS JSON"; letAct = inAct; break;
        case "Route": iconBack = getBasePath("icon") + "/ic_" + icAct.toLowerCase() + ".jpeg"; typeDesc = "GeoJSON"; break;
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
        if(isSet(tData.TrackedTime)) {
            var timeMins = tData.TrackedTime/60;
            var pace = (timeMins/tData.TrackedDist);
            runDiv += "<br/>" + tData.TrackedDist + " mi./" + timeMins.toFixed(1) + " min." +
                    "<br/>" + pace.toFixed(1) + " min/mile pace.";
        }
        runDiv += "<br/>" + Math.round(tData.RunWalk * 1508.57) + " est. steps";
        var runMap = "";
        if(isSet(tData.RunGeoJSON) && tData.CommonRoute === 0) { runMap += getMapLinkString(tData.Date, "Route", "Run", tData.CommonFlag); }
        if(tData.isGPSRunJSON === true) { runMap = getMapLinkString(tData.Date, "gpsJSON", "Run", tData.CommonFlag); }
        var altMap = "";
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
                "High: <span style='" + styleTemp(tData.High) + "'>" + tData.High + "</span><br/>" +
                "Low: <span style='" + styleTemp(tData.Low) + "'>" + tData.Low + "</span><br/>" +
                "Average: <span style='" + styleTemp(tData.Average) + "'>" + tData.Average + "</span>" +
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

function populateFitnessChart() {
    var tElement = "<div class='trafcam'>" +
            "<div class='UPopNM'>" +
            "<img class='ch_large' src='" + getBasePath("old") + "/pChart/ch_Dynamic.php?Thumb=1&DynVar=FitWeight'/>" +
            "<div class='UPopNMO'>" +
            "<strong>Chart Type</strong><br/>" +
            "<a href='" + getBasePath("old") + "/pChart/ch_Dynamic.php?DynVar=FitWeight' target='pChart'><button class='UButton'>Range</button></a>" +
            "<a href='" + getBasePath("old") + "/pChart/ch_Dynamic.php?DynVar=FitWeightAll' target='pChart'><button class='UButton'>Full</button></a>" +
            "</div></div>" +
            "</div>";
    dojo.byId("WeightChartHolder").innerHTML = tElement;
}

function populateSearchBox() {
    var tElement = "<div class='UBox'><form id='SearchByDateForm'>" +
            "<span><button class='UButton' type='Submit' name='DoFitSearch'>Search</button> back to 2007-06-27</span><br/>" +
            "<span>Start: </span><input type='date' name='FitSearchStart' value='' style='width: 120px;'/> | " +
            "<span>End: </span><input type='date' name='FitSearchEnd' value='' style='width: 120px;'/><br/>" +
            "</form></div><br/>";
    dojo.byId("FitDateRangeSearch").innerHTML = tElement;
    var searchByDateForm = dojo.byId("SearchByDateForm");
    dojo.connect(searchByDateForm, "onsubmit", actSearchByDateSubmit);
}

function putCalories(formData) {
    formData.doWhat = "putCalories";
    var xhArgs = {
        preventCache: true,
        url: getResource("Fitness"),
        postData: formData,
        handleAs: "json",
        timeout: timeOutMilli,
        load: function(data) {
            showNotice(data.callbackData.totCal + " calories added!");
            getFitnessAllData();
        },
        error: function(data, iostatus) {
            window.alert("xhrPost for Calories FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
        }
    };
    dojo.xhrPost(xhArgs);
}

function putRoute(formData) {
    formData.doWhat = "putRoute";
    var xhArgs = {
        preventCache: true,
        url: getResource("Fitness"),
        postData: formData,
        handleAs: "json",
        timeout: timeOutMilli,
        load: function(data) {
            showNotice(data.routesDone[0] + " done!");
            getFitnessAllData();
        },
        error: function(data, iostatus) {
            window.alert("xhrPost for Route FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
        }
    };
    dojo.xhrPost(xhArgs);
}

function putUpdateToday(formData) {
    formData.doWhat = "putToday";
    var xhArgs = {
        preventCache: true,
        url: getResource("Fitness"),
        postData: formData,
        handleAs: "text",
        timeout: timeOutMilli,
        load: function(data) {
            showNotice("Updated today's activites!");
            getFitnessAllData();
        },
        error: function(data, iostatus) {
            window.alert("xhrPost for UpdateToday FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
        }
    };
    dojo.xhrPost(xhArgs);
}

var init = function(event) {
    populateFitnessChart();
    populateSearchBox();
    getFitnessAllData();
};

dojo.ready(init);