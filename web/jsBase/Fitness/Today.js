/* 
by Anthony Stump
Created: 14 Feb 2018
Fitness/Today.js split: 4 Apr 2018
Updated: 24 Feb 2019
 */

function actUpdateTodaySubmit(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject("FormUpdateToday");
    putUpdateToday(thisFormData);
}

function fitnessToday(dataIn) {
    if(!isSet(dataIn)) { dataIn = {}; }
    var studChecked, commonRouteChecked, runWalk, cycling, rsMile, weight, shoe, mowNotes, xTags, hSleep, xOrgs, exMins;
    studChecked = commonRouteChecked = rsMile = "";
    if(!isSet(dataIn.Cycling)) { cycling = ""; } else { cycling = dataIn.Cycling; }
    if(!isSet(dataIn.Weight)) { weight = ""; } else { weight = dataIn.Weight; }
    if(!isSet(dataIn.RunWalk)) { runWalk = ""; } else { runWalk = dataIn.RunWalk; }
    if(!isSet(dataIn.Shoe)) { shoe = ""; } else { shoe = dataIn.Shoe; }
    if(!isSet(dataIn.MowNotes)) { mowNotes = ""; } else { mowNotes = dataIn.MowNotes; }
    if(!isSet(dataIn.Orgs)) { xOrgs = ""; } else { xOrgs = dataIn.Orgs; }
    if(!isSet(dataIn.EstHoursSleep)) { hSleep = ""; } else { hSleep = dataIn.EstHoursSleep; }
    if(!isSet(dataIn.IntensityMinutes)) { exMins = ""; } else { exMins = dataIn.IntensityMinutes; }
    if(!isSet(dataIn.xTags)) {
        if(dataIn.Vomit === 1) { xTags = "VO"; } else { xTags = ""; }
    } else {
        xTags = dataIn.xTags; if(dataIn.Vomit === 1) { xTags += " VO"; }
    }
    if(dataIn.BkStudT === 1) { studChecked = "checked='checked'"; }
    if(dataIn.CommonRoute === 1) { commonRouteChecked = "checked='checked'"; }
    var holderData = "<div class='UBox'>TD" +
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
            "<tr><td>Sleep</td><td><input type='number' step='0.1' name='TodayEstHoursSleep' value='" + hSleep + "'/></td></tr>" +
            "<tr><td>Exercise Mins</td><td><input type='number' step='1' name='TodayExerciseMinutes' value='" + exMins + "'/></td></tr>" +
            "<tr><td>Other</td><td><input type='text' name='TodayX' value='" + xTags + "'/></td></tr>" +
            "<tr><td>Orgs</td><td><input type='number' step='1' name='TodayXO' value='" + xOrgs + "'/></td></tr>" +
            "</tbody></table>";
    holderData += tableData +
            "</form></div></div>";
    dojo.byId("Today").innerHTML = holderData;
    var formUpdateToday = dojo.byId("FormUpdateToday");
    dojo.connect(formUpdateToday, "onsubmit", actUpdateTodaySubmit);
}

function putUpdateToday(formData) {
    aniPreload("on");
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
            aniPreload("off");
        },
        error: function(data, iostatus) {
            window.alert("xhrPost for UpdateToday FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
            aniPreload("off");
        }
    };
    dojo.xhrPost(xhArgs);
}