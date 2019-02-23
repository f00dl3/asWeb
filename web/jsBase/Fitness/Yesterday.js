/* 
by Anthony Stump
Created: 21 Feb 2019
Updated: 22 Feb 2019
 */

function actUpdateYesterdaySubmit(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject("FormUpdateYesterday");
    putUpdateYesterday(thisFormData);
}

function fitnessYesterday(dataIn) {
    if(!isSet(dataIn)) { dataIn = {}; }
    var yCaloriesBurned, yCalories, ySteps, yExerciseMinutes, yXTags, yOrgs;
    if(!isSet(dataIn.Calories)) { yCalories = ""; } else { yCalories = dataIn.Calories; }
    if(!isSet(dataIn.CaloriesBurned)) { yCaloriesBurned = ""; } else { yCaloriesBurned = dataIn.CaloriesBurned; }
    if(!isSet(dataIn.Steps)) { ySteps = ""; } else { ySteps = dataIn.Steps; }
    if(!isSet(dataIn.IntensityMinutes)) { yExerciseMinutes = ""; } else { yExerciseMinutes = dataIn.IntensityMinutes; }
    if(!isSet(dataIn.XTags)) { yXTags = ""; } else { yXTags = dataIn.XTags; }
    if(!isSet(dataIn.Orgs)) { yOrgs = ""; } else { yOrgs = dataIn.Orgs; }
    var holderData = "<div class='UBox'>YD" +
            "<div class='UBoxO'>Update Yesterday<br/>" +
            "<form id='FormUpdateYesterday'><button class='UButton' id='MakeUpdatesY' type='submit'>Update</button>";
    var tableData = "<table><tbody>" +
            "<tr><td>Burned Cals</td><td><input type='number' step='1' name='YesterdayCaloriesBurned' value='" + yCaloriesBurned + "'/></td></tr>" +
            "<tr><td>Calores</td><td><input type='number' step='1' name='YesterdayCalories' value='" + yCalories + "'/></td></tr>" +
            "<tr><td>Steps<br/><em>Base 2000</em></td><td><input type='number' step='1' name='YesterdaySteps' value='" + ySteps + "'/></td></tr>" +
            "<tr><td>Exercise Minutes</td><td><input type='number' step='1' name='YesterdayIntensityMinutes' value='" + yExerciseMinutes + "'/></td></tr>" +
            "<tr><td>XTags</td><td><input type='text' name='YesterdayX' value='" + yXTags + "'/></td></tr>" +
            "<tr><td>Orgs</td><td><input type='number' step='1' name='YesterdayXO' value='" + yOrgs + "'/></td></tr>" +
            "</tbody></table>";
    holderData += tableData +
            "</form></div></div>";
    dojo.byId("Yesterday").innerHTML = holderData;
    var formUpdateYesterday = dojo.byId("FormUpdateYesterday");
    dojo.connect(formUpdateYesterday, "onsubmit", actUpdateYesterdaySubmit);
}

function putUpdateYesterday(formData) {
    aniPreload("on");
    formData.doWhat = "putYesterday";
    var xhArgs = {
        preventCache: true,
        url: getResource("Fitness"),
        postData: formData,
        handleAs: "text",
        timeout: timeOutMilli,
        load: function(data) {
            showNotice("Updated yesterday's activites!");
            getFitnessAllData();
            aniPreload("off");
        },
        error: function(data, iostatus) {
            window.alert("xhrPost for UpdateYesterday FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
            aniPreload("off");
        }
    };
    dojo.xhrPost(xhArgs);
}