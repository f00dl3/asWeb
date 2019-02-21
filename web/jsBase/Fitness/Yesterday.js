/* 
by Anthony Stump
Created: 21 Feb 2019
Updated: Created!
 */

function actUpdateYesterdaySubmit(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject("FormUpdateYesterday");
    putUpdateYesterday(thisFormData);
}

function fitnessYesterday(dataIn) {
    if(!isSet(dataIn)) { dataIn = {}; }
    var caloriesBurned, steps, intensityMinutes;
    if(!isSet(dataIn.CaloriesBurned)) { caloriesBurned = ""; } else { caloriesBurned = dataIn.CaloriesBurned; }
    if(!isSet(dataIn.Steps)) { steps = ""; } else { steps = dataIn.Steps; }
    if(!isSet(dataIn.IntensityMinutes)) { intensityMinutes = ""; } else { intensityMinutes = dataIn.IntensityMinutes; }
    var holderData = "<div class='UBox'>Yester" +
            "<div class='UBoxO'>Update Yesterday<br/>" +
            "<form id='FormUpdateYesterday'><button class='UButton' id='MakeUpdatesY' type='submit'>Update</button>";
    var tableData = "<table><tbody>" +
            "<tr><td>Calores Burned</td><td><input type='number' step='1' name='YesterdayCaloriesBurned' value='" + caloriesBurned + "'/></td></tr>" +
            "<tr><td>Steps</td><td><input type='number' step='1' name='YesterdaySteps' value='" + steps + "'/></td></tr>" +
            "<tr><td>Intensity Minutes</td><td><input type='number' step='1' name='YesterdayIntensityMinutes' value='" + intensityMinutes + "'/></td></tr>" +
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