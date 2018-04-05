/* 
by Anthony Stump
Created: 14 Feb 2018
Fitness/Plans.js split: 4 Apr 2018
 */

function actOnCommitRoute(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject("RoutePlanForm");
    putRoute(thisFormData);
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