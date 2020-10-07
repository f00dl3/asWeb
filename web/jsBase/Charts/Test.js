/* 
by Anthony Stump
Created: 6 Oct 2020
Updated: 7 Oct 2020
Based off samples! https://canvasjs.com/javascript-charts/dynamic-live-line-chart/
 */

function testChart() {	

	console.log("DEBG: testChart called");
	
	var dataPoints = [];
	
	var chart = new CanvasJS.Chart("ChartHolder", {
		theme: "light2",
		title: {
			text: "Live Data"
		},
		data: [{
			type: "line",
			dataPoints: dataPoints
		}]
	});
	updateData();
	
	// Initial Values
	var xValue = 0;
	var yValue = 10;
	var newDataCount = 6;

	function addData(data) {
		if(newDataCount != 1) {
			$.each(data, function(key, value) {
				dataPoints.push({x: value[0], y: parseInt(value[1])});
				xValue++;
				yValue = parseInt(value[1]);
			});
		} else {
			//dataPoints.shift();
			dataPoints.push({x: data[0][0], y: parseInt(data[0][1])});
			xValue++;
			yValue = parseInt(data[0][1]);
		}	
		newDataCount = 1;
		chart.render();
		setTimeout(updateData, 1500);
	}

	function updateData() {
		
		console.log("DEBUG: updateData called");
	    dojo.byId("ChartHolder").innerHTML = "LOADING DATA...";
	    aniPreload("on");
	    var thePostData = { "doWhat": "TestData" };
	    require(["dojo/request"], function(request) {
	        request
	            .post(getResource("Chart3"), {
	                data: thePostData,
	                handleAs: "json"
	            }).then(
	                function(data) {
	                    aniPreload("off");
	                    addData(data);
	                    console.log("DEBUG: Test data pull success.");
	                },
	                function(error) { 
	                    aniPreload("off");
	                    console.log("DEBUG: Test data pull failure: " + iostatus.xhr.status + " (" + data + ")");
	                });
	    });
	}
	
}

