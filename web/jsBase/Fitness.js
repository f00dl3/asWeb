/* 
by Anthony Stump
Created: 14 Feb 2018
Updated: 19 Nov 2020
 */

var myHeight = 67;
var strPlan = 2;

function colorCalories(tValue) {
    switch(tValue) {
        case inRange(tValue, 0, 1799.9): return 'FtCL2000';
        case inRange(tValue, 1800, 1999.9): return 'FtCL2200';
        case inRange(tValue, 2000, 2199.9): return 'FtCL2400';
        case inRange(tValue, 2200, 2399.9): return 'FtCL2600';
        case inRange(tValue, 2400, 2599.9): return 'FtCL2800';
        case inRange(tValue, 2600, 2799.9): return 'FtCL3000';
        case inRange(tValue, 2800, 9999.9): return 'FtCG3000';
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

function actSearchByDateSubmit(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject("SearchByDateForm");
    var xdt1 = thisFormData.FitSearchStart;
    var xdt2 = thisFormData.FitSearchEnd;
    getFitnessAllData(false, xdt1, xdt2);
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
        "Overhauled": bikeStats.LastOverhaul + "<br/>" + autoUnits(bikeStats.MilesOverhaul) + " mi",
        "Chain": bikeStats.LastChain + "<br/>" + autoUnits(bikeStats.MilesChain) + " mi",
        "TireFront": bikeStats.LastTireFront + "<br/>" + autoUnits(bikeStats.MilesTireFront) + " mi",
        "TireRear": bikeStats.LastTireRear + "<br/>" + autoUnits(bikeStats.MilesTireRear) + " mi",
        "StuddedFront": bikeStats.LastTireFrontStudded + "<br/>" + autoUnits(bikeStats.MilesTireFrontStudded) + " mi",
        "StuddedRear": bikeStats.LastTireRearStudded + "<br/>" + autoUnits(bikeStats.MilesTireRearStudded) + " mi",
        "WheelFront": bikeStats.LastWheelFront + "<br/>" + autoUnits(bikeStats.MilesWheelFront) + " mi",
        "WheelRear": bikeStats.LastWheelRear + "<br/>" + autoUnits(bikeStats.MilesWheelRear) + " mi"
    };
    var boxRun = " <div class='UBox'>" +
            "<span>RunWalk</span><br/>" + autoUnits(fitTot.TOTRW) + "<br/><span>miles</span>" +
            "<div class='UBoxO'>" +
            yb0 + ": <strong>" + autoUnits(yearStats.yb0rw) + "</strong> mi<br/>" +
            yb1 + ": <strong>" + autoUnits(yearStats.yb1rw) + "</strong> mi<br/>" +
            yb2 + ": <strong>" + autoUnits(yearStats.yb2rw) + "</strong> mi<br/>" +
            yb3 + ": <strong>" + autoUnits(yearStats.yb3rw) + "</strong> mi<br/>" +
            yb4 + ": <strong>" + autoUnits(yearStats.yb4rw) + "</strong> mi<br/>" +
            "<p>Average pace: <br/><strong>" + avgPace.toFixed(1) + "</strong> min/mile." +
            "<p>Running shoes: <br/><strong>" + crsm.CRSM + "</strong> miles on <strong>" + rshoe.Pair + "</strong>" +
            "<p>Est. steps: <br/><strong>" + estSteps + "</strong> mil." +
            "</div></div>";
    var boxCyc = " <div class='UBox'>" +
            "<span>Cycling</span><br/>" + autoUnits(fitTot.TOTCY) + "<br/><span>miles</span>" +
            "<div class='UBoxO'>" +
            yb0 + ": <strong>" + autoUnits(yearStats.yb0cy) + "</strong> mi<br/>" +
            yb1 + ": <strong>" + autoUnits(yearStats.yb1cy) + "</strong> mi<br/>" +
            yb2 + ": <strong>" + autoUnits(yearStats.yb2cy) + "</strong> mi<br/>" +
            yb3 + ": <strong>" + autoUnits(yearStats.yb3cy) + "</strong> mi<br/>" +
            yb4 + ": <strong>" + autoUnits(yearStats.yb4cy) + "</strong> mi<br/>" +
            "<p><em>Based on $<strong>" + costPerGallon.toFixed(2) + "</strong>/gal<br/>" +
            "and $<strong>" + annMaint.toFixed(2) + "</strong>/yr maint</em><p>" +
            "<div class='table'>";
    for(var key in bikeStats) {
        boxCyc += "<div class='tr'><span class='td'>" + key + "</span><span class='td'>" + bikeStats[key] + "</span></div>";
    }
    boxCyc += "</div>" +
            "</div></div>";
    var boxTot = " <div class='UBox'>" +
            "<span>Combined</span><br/>" + autoUnits(fitTot.TOTOA) + "<br/><span>miles</span>" +
            "<div class='UBoxO'>" +
            yb0 + ": <strong>" + autoUnits(yearStats.yb0oa) + "</strong>mi<br/>" +
            yb1 + ": <strong>" + autoUnits(yearStats.yb1oa) + "</strong> mi<br/>" +
            yb2 + ": <strong>" + autoUnits(yearStats.yb2oa) + "</strong> mi<br/>" +
            yb3 + ": <strong>" + autoUnits(yearStats.yb3oa) + "</strong> mi<br/>" +
            yb4 + ": <strong>" + autoUnits(yearStats.yb4oa) + "</strong> mi<br/>" +
            "<p><a href='" + getBasePath("ui") + "/OLMap.jsp?Action=RouteHistory'>" +
            "<button class='UButton'>All Routes</button></a>" +
            "</div></div>";
    var returnData = boxRun + boxCyc + boxTot;
    dojo.byId("FitBubbleHolder").innerHTML = returnData;
}

function getFitnessAllData(doReload, inXdt1, inXdt2) {
    var timeout = 90 * 1000;
    aniPreload("on");
    var xdt1, xdt2;
    var oYear = getDate("year", 0, "yearOnly");
    if(isSet(inXdt1)) { xdt1 = inXdt1; } else { xdt1 = getDate("day", -365, "dateOnly"); }
    if(isSet(inXdt2)) { xdt2 = inXdt2; } else { xdt2 = getDate("day", 0, "dateOnly"); }
    var thePostData = { 
    		"doWhat": "getAll",
    		"XDT1": xdt1,
    		"XDT2": xdt2,
    		"Bicycle": bicycleUsed,
    		"routine": strPlan,
    		"year": oYear
    }
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Fitness"), {
                data: thePostData,
                handleAs: "json"
            }).then(
                function(data) {
                	processFitnessAll(data.allRecs, data.autoMpg[0]);
	                fitnessCalories(data.calories);
	                fitnessPlans(data.plans);
	                //fitnessStrength(data.strength);
	                fitnessToday(data.today[0]);
	                fitnessYesterday(data.yesterday[0]);
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
	                aniPreload("off");
                },
                function(error) { 
                    aniPreload("off");
                    console.log("xhrGet for All FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
                }
        );
    });
    // Temp disabled check for reload 3/28/19 due to bug
    /* if(doReload) { */ setTimeout(function () { getFitnessAllData(false, inXdt1, inXdt2); }, timeout); /* } */
}

function getMapLinkString(inDate, inType, inAct, commonFlag, mapType) {
    var longAct, iconBack, typeDesc, letAct, icAct, newAction, gpsAction;
    longAct = iconBack = typeDesc = letAct = icAct = newAction = gpsAction = " "; 
    switch(inAct) {
        case "Alt": longAct = "Alternate"; letAct = "A"; icAct = "Run"; newAction = "RouteGeoJSONAlt"; gpsAction = "RouteGPSRun"; break;
        case "Run": longAct = "Running"; letAct = "R"; icAct = inAct; newAction = "RouteGeoJSONRun"; gpsAction = "RouteGPSRun"; break;
        case "Ru2": longAct = "Running 2"; letAct = "A"; icAct = "Run"; newAction = "RouteGeoJSONAlt"; gpsAction = "RouteGPSRun2"; break;
        case "Ru3": longAct = "Running 3"; letAct = "A"; icAct = "Run"; newAction = "RouteGeoJSONAlt"; gpsAction = "RouteGPSRun3"; break;
        case "Ru4": longAct = "Running 4"; letAct = "A"; icAct = "Run"; newAction = "RouteGeoJSONAlt"; gpsAction = "RouteGPSRun4"; break;
        case "Cyc": longAct = "Cycling"; letAct = "C"; icAct= inAct; newAction = "RouteGeoJSONCyc"; gpsAction = "RouteGPSCyc";  break;
        case "Cy2": longAct = "Cycling 2"; letAct = "A"; icAct = "Cyc"; newAction = "RouteGeoJSONAlt"; gpsAction = "RouteGPSCyc2"; break;
        case "Cy3": longAct = "Cycling 3"; letAct = "A"; icAct = "Cyc"; newAction = "RouteGeoJSONAlt"; gpsAction = "RouteGPSCyc3"; break;
        case "Cy4": longAct = "Cycling 4"; letAct = "A"; icAct = "Cyc"; newAction = "RouteGeoJSONAlt"; gpsAction = "RouteGPSCyc4"; break;
    }
    switch(inType) {
        case "gpsJSON": iconBack = getBasePath("icon") + "/ic_" + icAct.toLowerCase() + "J.jpeg"; typeDesc = "GPS JSON"; letAct = inAct; newAction = gpsAction; break;
        case "Route": iconBack = getBasePath("icon") + "/ic_" + icAct.toLowerCase() + ".jpeg"; typeDesc = "GeoJSON"; break;
    }
    var genString;
    if(isSet(mapType) && mapType === "OL") {
        genString = "<a href='" + getBasePath("ui") + "/OLMap.jsp?Action=" + newAction + "&Input=" + inDate + "' target='new'>" +
            "<div class='UPop'><img class='th_icon' src='" + iconBack + "' />" +
            "<div class='UPopO'>" + longAct + " " + typeDesc + " data." + commonFlag + "</div>" +
            "</div></a> ";
    } else {
        genString = "<a href='" + getBasePath("old") + "/OutMap.php?Title=" + inDate + "&" + inType + "=" + letAct + "' target='new'>" +
            "<div class='UPop'><img class='th_icon' src='" + iconBack + "' />" +
            "<div class='UPopO'>" + longAct + " " + typeDesc + " data." + commonFlag + "</div>" +
            "</div></a> ";
    }
    return genString;
}

function getWeightChart(inXdt1, inXdt2) {
    aniPreload("on");
    var xdt1, xdt2;
    var oYear = getDate("year", 0, "yearOnly");
    if(isSet(inXdt1)) { xdt1 = inXdt1; } else { xdt1 = getDate("day", -365, "dateOnly"); }
    if(isSet(inXdt2)) { xdt2 = inXdt2; } else { xdt2 = getDate("day", 0, "dateOnly"); }
    var thePostData = "doWhat=FitnessCharts&XDT1=" + xdt1 + "&XDT2=" + xdt2;
    var xhArgs = {
        preventCache: true,
        url: getResource("Chart"),
        postData: thePostData,
        handleAs: "text",
        timeout: timeOutMilli,
        load: function(data) {
            populateFitnessChart("jFree");
            aniPreload("off");
        },
        error: function(data, iostatus) {
            aniPreload("off");
            showNotice("xhrGet for Fitness Charts FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
        }
    };
    dojo.xhrPost(xhArgs);
}

function processFitnessAll(dataIn, autoMpg) {
    var costPerMileTco = 14000/((autoMpg.EndMiles - 47500) + 60000);
    var costPerMile = costPerMileTco + cpmNoMpg + (autoMpg.AvgCost / ((autoMpg.EndMiles - autoMpg.StartMiles) / autoMpg.Gallons));
    var fitTableDefs = [
        "<div class='UPop'>Date<div class='UPopO'><span id='SleepChartHolder'></span></div></div>",
        "Weight",
        "RunWalk",
        "Cycling",
        "Cals",
        "Extra"
    ];
    var rData = "<tr>";
    fitTableDefs.forEach(function(thisDef) {
        if(thisDef === "Cals") {
            rData += "<th><div class='UPop'>" + thisDef + "<div class='UPopO'><div id='CalorieChartHolder'></div></div></th>";
        } else {
            rData += "<th>" + thisDef + "</th>";
        }
    });
    rData += "</tr>";
    for(var i=0; i<dataIn.length; i++) {
        var tData = dataIn[i];
        var bkStuds, gym, reelMow, commonRouteFlag, cycMap, runMap, altMap, sleepHours;
        bkStuds = gym = reelMow = commonRouteFlag = cycMap = runMap = altMap = sleepHours = " ";
        var bmi = 'BMI of ' + ((tData.Weight*703)/(myHeight*myHeight)).toFixed(1);
        if(!tData.RunWalk) { tData.RunWalk = " "; }
        if(!tData.Cycling) { tData.Cycling = " "; }
        if(!tData.Calories) { tData.Calories = " "; }
        if(isSet(tData.EstHoursSleep)) { sleepHours = "<strong>Sleep</strong>: " + tData.EstHoursSleep + " hrs<br/>"; }
        if(tData.BkStudT === 1) { bkStuds = " style='background: blue; color: white;'"; }
        if(tData.Gym === 1) {
            gym = "<div class='UPop'><img class='th_icon' src='" + getBasePath("ui") + "/img/Icons/ic_lst.jpeg'/>" +
                    "<div class='UPopO'>" + tData.GymWorkout + "</div></div> ";
        }
        if(tData.ReelMow === 1) {
            reelMow = "<div class='UPop'><img class='th_icon' src='" + getBasePath("ui") + "/img/Icons/ic_mow.jpg'>" +
                    "<div class='UPopO'>" + tData.MowNotes + "</div></div> ";
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
        if(isSet(tData.CycGeoJSON) /* && tData.CommonRoute === 0 */) { cycMap += getMapLinkString(tData.Date, "Route", "Cyc", commonRouteFlag, "OL"); }
        if(tData.isGPSCycJSON === true) { cycMap = getMapLinkString(tData.Date, "gpsJSON", "Cyc", commonRouteFlag, "OL"); }
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
        if(isSet(tData.RunGeoJSON) /* && tData.CommonRoute === 0 */) { runMap += getMapLinkString(tData.Date, "Route", "Run", commonRouteFlag, "OL"); }
        if(tData.isGPSRunJSON === true) { runMap = getMapLinkString(tData.Date, "gpsJSON", "Run", commonRouteFlag, "OL"); }
        var altMap = "";
        if(isSet(tData.AltGeoJSON) /* && tData.CommonRoute === 0 */) { altMap += getMapLinkString(tData.Date, "Route", "Alt", commonRouteFlag, "OL"); }
        if(tData.isGPSCyc2JSON === true) { altMap += getMapLinkString(tData.Date, "gpsJSON", "Cy2", commonRouteFlag, "OL") + " "; }
        if(tData.isGPSCyc3JSON === true) { altMap += getMapLinkString(tData.Date, "gpsJSON", "Cy3", commonRouteFlag, "OL") + " "; }
        if(tData.isGPSCyc4JSON === true) { altMap += getMapLinkString(tData.Date, "gpsJSON", "Cy4", commonRouteFlag, "OL") + " "; }
        if(tData.isGPSRun2JSON === true) { altMap += getMapLinkString(tData.Date, "gpsJSON", "Ru2", commonRouteFlag, "OL") + " "; }
        if(tData.isGPSRun3JSON === true) { altMap += getMapLinkString(tData.Date, "gpsJSON", "Ru3", commonRouteFlag, "OL") + " "; }
        if(tData.isGPSRun4JSON === true) { altMap += getMapLinkString(tData.Date, "gpsJSON", "Ru4", commonRouteFlag, "OL") + " "; }
        var nutriBreakdown = "Breakdown unavailable!";
        
        var shoeClass = "";
        if(isSet(tData.RSMile)) { shoeClass = colorShoeMile(tData.RSMile); }
        if(isSet(tData.Fat)) {
            nutriBreakdown = "<strong>Burned: " + tData.CaloriesBurned + "<br/>" +
            		"<table><tbody><tr><td>" +
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
                    "<strong>Puked:</strong> " + tData.Vomit + "<br/>" +
                    "<strong>LS Used:</strong> " + tData.LSTypes + "<br/>" +
                   "<strong>Orgs:</strong> " + tData.Orgs + "<br/>" +
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
                sleepHours +
                "High: <span style='" + styleTemp(tData.High) + "'>" + tData.High + "</span><br/>" +
                "Low: <span style='" + styleTemp(tData.Low) + "'>" + tData.Low + "</span><br/>" +
                "Average: <span style='" + styleTemp(tData.Average) + "'>" + tData.Average + "</span><br/>" +
                "Gaming: " + (tData.HoursGaming).toFixed(1) + " hrs<br/>" +
                "Steps: " + tData.Steps + "<br/>" +
                "Int Mins: " + tData.IntensityMinutes + 
                "</div></div></td>" +
                "<td align='center'><div class='UPop'><div class='" + colorWeight(tData.Weight) + "'>" + tData.Weight + "</div>" +
                "<div class='UPopO'>" + 
                bmi +
                "</div>" +
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
    populateCalorieChart();
    populateSleepChart();
}

function populateCalorieChart() {
    var rData = "<a href='" + doCh("j", "CalorieRange", null) + "' target='pChart'><img class='ch_large' src='" + doCh("j", "CalorieRange", "th") + "'/></a>";
    dojo.byId("CalorieChartHolder").innerHTML = rData;
}

function populateFitnessChart(chartSource) {
    var timestamp = getDate("day", 0, "timestamp");
    var tElement = "";
    switch(chartSource) {
        
        case "pChart":
            tElement = "<div class='trafcam'>" +
                    "<div class='UPopNM'>" +
                    "<img class='ch_large' src='" + getBasePath("old") + "/pChart/ch_Dynamic.php?Thumb=1&DynVar=FitWeight'/>" +
                    "<div class='UPopNMO'>" +
                    "<strong>Chart Type</strong><br/>" +
                    "<a href='" + doCh("3", "WeightRange", null) + "' target='pChart'><button class='UButton'>Range</button></a>" +
                    "<a href='" + getBasePath("old") + "/pChart/ch_Dynamic.php?DynVar=FitWeightAll' target='pChart'><button class='UButton'>Full</button></a>" +
                    "</div></div>" +
                    "</div>";
            break;
            
        case "jFree":
            tElement = "<div class='trafcam'>" +
                    "<div class='UPopNM'>" +
                    "<img class='ch_large' src='" + getBasePath("chartCache") + "/th_WeightRange.png?ts=" + timestamp + "'/>" +
                    "<div class='UPopNMO'>" +
                    "<strong>Chart Type</strong><br/>" +
                    "<a href=''" + doCh("3", "WeightRange", null) + "' target='pChart'><button class='UButton'>Range</button></a>" +
                    //"<a href='" + getBasePath("old") + "/pChart/ch_Dynamic.php?DynVar=FitWeightAll' target='pChart'><button class='UButton'>Full</button></a>" +
                    "</div></div>" +
                    "</div>";
    }
    
    dojo.byId("WeightChartHolder").innerHTML = tElement;
}

function populateSleepChart() {
    var rData = "<a href='" + doCh("j", "SleepRange", null) + "' target='pChart'><img class='ch_large' src='" + doCh("j", "SleepRange", "th") + "'/></a>";
    dojo.byId("SleepChartHolder").innerHTML = rData;
}

function populateSearchBox() {
    var tElement = "<div class='UBox'><form id='SearchByDateForm'>" +
            "<span><button class='UButton' type='Submit' name='DoFitSearch'>Search</button> back to 2007-06-27</span>" +
            " [<a href='" + getResource("Physicals") + "' target='physSet'>Physicals</a>]" +
            "<br/>" +
            "<span>Start: </span><input type='date' name='FitSearchStart' value='' style='width: 120px;'/>";
    if(!checkMobile()) { tElement += " | "; } else { tElement += "<br/>"; }
    tElement += "<span>End: </span><input type='date' name='FitSearchEnd' value='' style='width: 120px;'/><br/>" +
            "</form></div><br/>";
    dojo.byId("FitDateRangeSearch").innerHTML = tElement;
    var searchByDateForm = dojo.byId("SearchByDateForm");
    dojo.connect(searchByDateForm, "onsubmit", actSearchByDateSubmit);
}

function putRoute(formData) {
    aniPreload("on");
    formData.doWhat = "putRoute";
    var xhArgs = {
        preventCache: true,
        url: getResource("Fitness"),
        postData: formData,
        handleAs: "json",
        timeout: timeOutMilli,
        load: function(data) {
            showNotice(data.routesDone[0] + " done!");
            getFitnessAllData(false);
            aniPreload("off");
        },
        error: function(data, iostatus) {
            aniPreload("off");
            showNotice("xhrPost for Route FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
        }
    };
    dojo.xhrPost(xhArgs);
}

var initFitness = function(event) {
    getWeightChart();
    populateSearchBox();
    getFitnessAllData(true, null, null);
};

dojo.ready(initFitness);
