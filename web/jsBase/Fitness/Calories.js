/* 
by Anthony Stump
Created: 14 Feb 2018
Fitness/Calories.js split: 4 Apr 2018
Updated: 22 Feb 2019
 */

function actOnCaloriesSubmit(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject("CaloriesForm");
    putCalories(thisFormData);
}

function fitnessCalories(calQ) {
    var foods = 1;
    var tableRows = [ "Food", "Servings", "Today", "Serving", "Calories" ];
    var dataBack = "<div class='UBox'>FD<div class='UBoxO'>Calorie Tracker" +
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

function putCalories(formData) {
    aniPreload("on");
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
            aniPreload("off");
        },
        error: function(data, iostatus) {
            aniPreload("off");
            window.alert("xhrPost for Calories FAIL!, STATUS: " + iostatus.xhr.status + " ("+data+")");
        }
    };
    dojo.xhrPost(xhArgs);
}