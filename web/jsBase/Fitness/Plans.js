/* 
by Anthony Stump
Created: 14 Feb 2018
Fitness/Plans.js split: 4 Apr 2018
Updated: 13 May 2019
 */

function actOnCommitRoute(event) {
    dojo.stopEvent(event);
    var thisFormData = dojo.formToObject("RoutePlanForm");
    putRoute(thisFormData);
}

function actOnProcessGps(event) {
    dojo.stopEvent(event);
    processGpsTracks();
}

function fileUploadGenerator() {
    // Stub for future upload selector - for now pass through to process GPS.
    var rData = "<form id='UploadFile' name='UploadGPS'>" +
            "<input type='file' name='gpsFile' id='gpsFile'/>" +
            "<button class='"
            "</form>";
    //dojo.byId("gpsUpHold").innerHTML = rData;
    //$("#gpsUpHold").show();
    (event);
}

function fitnessPlans(dataIn) {
	var gcURL = getGarminUrl();
    var container = "<div class='UBox'>PL<div class='UBoxO'>Planned Routes<p>" +
    		"<a href='" + getBasePath("ui") + "/OLMap.jsp?Action=RoutePlansAll'><button class='UButton'>All Plans</button></a>" +
    		"<a href='" + gcURL + "' target='gcws'><button class='UButton'>Garmin</button></a>" +
            "<form id='RoutePlanForm'>" +
            "<button class='UButton' id='CommitRouteButton' name='CommitRoutePlan' value='submit'>Completed</button>" +
            "<button class='UButton' id='UploadGPS' name='UploadGPS' value='Yes'>Process GPS</button>" +
            "<span id='gpsUpHold'></span>" +
            "<p>";
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
        var rpMap = "<a href='" + getBasePath("ui") + "/OLMap.jsp?Action=RoutePlan&Input=" + tData.Description + "'>Mapped</a>";
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
    var uploadGpsButton = dojo.byId("UploadGPS");
    dojo.connect(commitRouteButton, "click", actOnCommitRoute);
    dojo.connect(uploadGpsButton, "click", actOnProcessGps);
}

function processGpsTracks() {
    var thePostData = {
        "doWhat": "processGpsTracks"
    };
    require(["dojo/request"], function(request) {
        request
            .post(getResource("Fitness"), {
                data: thePostData,
                handleAs: "text"
            }).then(
                function(data) {
                    aniPreload("off");
                    showNotice("Processed GPS tracks!");
                    getFitnessAllData();
                },
                function(error) { 
                    aniPreload("off");
                    window.alert("request to FitnessResource/processGpsTracks FAIL!, STATUS: " + iostatus.xhr.status + " (" + data + ")");
                });
    });
}