/* 
by Anthony Stump
Created: 25 Mar 2018
Split off: 10 Apr 2018
Updated: 12 Apr 2018
 */

function getCooking() {
    aniPreload("on");
    require(["dojo/request"], function(request) {
        request
            .get(getResource("Cooking").then(
                function(data) {
                    aniPreload("off");
                    putCooking(cookingData)
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request for Cooking FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                })
            );
    });
}

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
