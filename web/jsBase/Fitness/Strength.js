/* 
by Anthony Stump
Created: 14 Jan 2020
Updated: on creation
 */

function fitnessStrength(dataIn) {
    var tableDefs = [ "Day", "Exercise", "Weight", "Reps" ];    
    var container = "<div class='UBox'>ST<div class='UBoxO'>Strength Training<p>";
    var tableData = "<table><thead><tr>";
    tableDefs.forEach(function(def) {
        tableData += "<th>" + def + "</th>";
    });
    tableData += "</tr></thead><tbody>";
    dataIn.forEach(function(str) { 
    	tableData += "<tr>" +
    		"<td>" + str.Day + "</td>" +
    		"<td>" + str.Exercise + "</td>" +
    		"<td>" + str.Weight + "</td>" +
    		"<td>" + str.Reps + "</td>" +
    		"</tr>";
    });
    tableData += "</tbody></table>";
    container += tableData +
    		"</div></div>";    
    dojo.byId("Strength").innerHTML = container;
}