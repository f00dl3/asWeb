/* 
by Anthony Stump
Created: 6 Oct 2020
Updated: 18 Nov 2020
Based off samples! https://canvasjs.com/javascript-charts/
For Dojo samples: https://dojotoolkit.org/documentation/tutorials/1.10/charting/
 */

function getStockRapidData() {
	let pData = { "doWhat": "testData" };
	$.post(getResource("Chart3"), pData, function(result) {
		testChartBasicCjs_RENDER(result);
  	});
}

function testChartBasicCjs() {
	getStockRapidData();
}

function testChartBasicCjs_RENDER(result) {
	let resultJ = JSON.parse(result);
	var ctx = document.getElementById('ChartCanvas').getContext('2d');
	var chart = new Chart(ctx, {
		// The type of chart we want to create
		type: 'line',

		// The data for our dataset
		data: {
			labels: resultJ.labels.reverse(),
			datasets: [{
				label: 'Worth Rapid',
				backgroundColor: 'rgb(255, 99, 132)',
				borderColor: 'rgb(255, 99, 132)',
				data: result.data.reverse()
			}]
		},

		// Configuration options go here
		options: {}
	});
}

function testChartBasic() {
	
	console.log("testChartBasic called");

	var chart = new CanvasJS.Chart("ChartHolder", {
		animationEnabled: true,
		theme: "light2",
		title:{
			text: "Site Traffic"
		},
		axisX:{
			valueFormatString: "DD MMM",
			crosshair: {
				enabled: true,
				snapToDataPoint: true
			}
		},
		axisY: {
			title: "Number of Visits",
			includeZero: true,
			crosshair: {
				enabled: true
			}
		},
		toolTip:{
			shared:true
		},  
		legend:{
			cursor:"pointer",
			verticalAlign: "bottom",
			horizontalAlign: "left",
			dockInsidePlotArea: true,
			itemclick: toogleDataSeries
		},
		data: [{
			type: "line",
			showInLegend: true,
			name: "Total Visit",
			markerType: "square",
			xValueFormatString: "DD MMM, YYYY",
			color: "#F08080",
			dataPoints: [
				{ x: new Date(2017, 0, 3), y: 650 },
				{ x: new Date(2017, 0, 4), y: 700 },
				{ x: new Date(2017, 0, 5), y: 710 },
				{ x: new Date(2017, 0, 6), y: 658 },
				{ x: new Date(2017, 0, 7), y: 734 },
				{ x: new Date(2017, 0, 8), y: 963 },
				{ x: new Date(2017, 0, 9), y: 847 },
				{ x: new Date(2017, 0, 10), y: 853 },
				{ x: new Date(2017, 0, 11), y: 869 },
				{ x: new Date(2017, 0, 12), y: 943 },
				{ x: new Date(2017, 0, 13), y: 970 },
				{ x: new Date(2017, 0, 14), y: 869 },
				{ x: new Date(2017, 0, 15), y: 890 },
				{ x: new Date(2017, 0, 16), y: 930 }
			]
		},
		{
			type: "line",
			showInLegend: true,
			name: "Unique Visit",
			lineDashType: "dash",
			dataPoints: [
				{ x: new Date(2017, 0, 3), y: 510 },
				{ x: new Date(2017, 0, 4), y: 560 },
				{ x: new Date(2017, 0, 5), y: 540 },
				{ x: new Date(2017, 0, 6), y: 558 },
				{ x: new Date(2017, 0, 7), y: 544 },
				{ x: new Date(2017, 0, 8), y: 693 },
				{ x: new Date(2017, 0, 9), y: 657 },
				{ x: new Date(2017, 0, 10), y: 663 },
				{ x: new Date(2017, 0, 11), y: 639 },
				{ x: new Date(2017, 0, 12), y: 673 },
				{ x: new Date(2017, 0, 13), y: 660 },
				{ x: new Date(2017, 0, 14), y: 562 },
				{ x: new Date(2017, 0, 15), y: 643 },
				{ x: new Date(2017, 0, 16), y: 570 }
			]
		}]
	});
	chart.render();

	function toogleDataSeries(e){
		if (typeof(e.dataSeries.visible) === "undefined" || e.dataSeries.visible) {
			e.dataSeries.visible = false;
		} else{
			e.dataSeries.visible = true;
		}
		chart.render();
	}

}

function testChartBasicDojo() {

	console.log("testChartBasicDojo called");
	
	require([
		"dojox/charting/Chart",
		"dojox/charting/themes/Claro",
		"dojox/charting/plot2d/Pie",
		"dojox/charting/action2d/Tooltip",
		"dojox/charting/action2d/MoveSlice",
		"dojox/charting/plot2d/Markers",
		"dojox/charting/axis2d/Default",
		"dojo/domReady!"
	], function(Chart, theme, Pie, Tooltip, MoveSlice) {

		var chartData = [10000,9200,11811,12000,7662,13887,14200,12222,12000,10009,11288,12099];

		var chart = new Chart("ChartHolder");

		chart.setTheme(theme);

		chart.addPlot("default", {
			type: Pie,
			markers: true,
			radius:170
		});

		chart.addAxis("x");
		chart.addAxis("y", { min: 5000, max: 30000, vertical: true, fixLower: "major", fixUpper: "major" });

		chart.addSeries("Monthly Sales - 2010",chartData);

		var tip = new Tooltip(chart,"default");

		var mag = new MoveSlice(chart,"default");

		chart.render();

	});
}

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

