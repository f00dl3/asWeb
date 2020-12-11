/* 
by Anthony Stump
Created: 18 Nov 2020
Updated: 10 Dec 2020
 */

function ch_chart_ffxivGilWorthByDay(container, result, type, pData) {
	let doLegend = true;
	let doX = true;
	let lbRadius = 1;
	if(type === "thumb") { 
		doLegend = false;
		doX = false; 
		lbRadius = 0;
	}
	let timeout = getRefresh("semiRapid");
	let resultJ = JSON.parse(result);
	let aLabels = resultJ.labels;
	let aData = resultJ.data;
	let aData2 = resultJ.data2;
	var ctx = document.getElementById(container).getContext('2d');
	var chart = null;
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [
				{ label: 'Worth', borderColor: 'yellow', data: aData },
				{ label: 'Gil', borderColor: 'green', backgroundColor: 'darkgreen', data: aData2 },
			]
		},
		options: {
			elements: {
				point: { radius: lbRadius }
			},
			legend: {
				display: doLegend
			},
			plugins: {
				zoom: { zoom: { enabled: doX } }
			},
			scales: {
				xAxes: [
					{ ticks: { display: doX } }
				],
				yAxes: [ 
					{ ticks: { callback: function(value, index, values) { return autoUnits(value); } } }
				]
			}
		}
	});
	setInterval(() => { ch_get_ffxivGilWorthByDay_Update(chart, pData); }, timeout);
}

function ch_get_ffxivGilWorthByDay(container, type) {
	let pData = { "doWhat": "ffxivGilWorthByDay" };
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_ffxivGilWorthByDay(container, result, type, pData);
  	});
}

function ch_get_ffxivGilWorthByDay_Update(chart, pData) {
	$.post(getResource("Chart3"), pData, function (result) {
		let resultJ = JSON.parse(result);
		let tLabel = resultJ.labels.reverse()[0];
		let tData = resultJ.data.reverse()[0];
		let tData2 = resultJ.data2.reverse()[0];
		let currentLabel = chart.data.labels[chart.data.labels.length-1];
		if(tLabel === currentLabel) {
		} else {
			chart.data.labels.push(tLabel);
			chart.data.datasets[0].data.push(tData);
			chart.data.datasets[1].data.push(tData2);
			chart.update();
			chart.data.labels.shift();
			chart.data.datasets.forEach((ds) => {
				ds.data.shift();
			})
			chart.update();
		}
	});
}

function ch_chart_ffxivQuestsByDay(container, result, type, pData) {
	let doLegend = true;
	let doX = true;
	let lbRadius = 1;
	if(type === "thumb") { 
		doLegend = false;
		doX = false; 
		lbRadius = 0;
	}
	let resultJ = JSON.parse(result);	
	let aLabels = resultJ.labels;
	let aData = resultJ.data;
	let aData2 = resultJ.data2;
	let aData3 = resultJ.data3;
	let aData4 = resultJ.data4;
	let aData5 = resultJ.data5;
	let aData6 = resultJ.data6;
	let aData7 = resultJ.data7;
	var ctx = document.getElementById(container).getContext('2d');
	var chart = null;
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [
				{ label: 'Quests', borderColor: 'yellow', data: aData },
				{ label: 'Hunting', borderColor: 'green', data: aData2 },
				{ label: 'Crafting', borderColor: 'blue', data: aData3 },
				{ label: 'Dungeons', borderColor: 'red', data: aData4 },
				{ label: 'Achievements', borderColor: 'orange', data: aData5 },
				{ label: 'Gathering', borderColor: 'magenta', data: aData6 },
				{ label: 'FATEs', borderColor: 'brown', data: aData7 }
			]
		},
		options: {
			legend: {
				display: doLegend
			},
			elements: {
				line: { borderWidth: 1 },
				point: { radius: lbRadius }
			},
			plugins: {
				zoom: { zoom: { enabled: doX } }
			},
			scales: {
				xAxes: [
					{ ticks: { display: doX } }
				],
				yAxes: [ 
					{ ticks: { callback: function(value, index, values) { return value; } } }
				]
			}
		}
	});
}

function ch_get_ffxivQuestsByDay(container, type) {
	let pData = { "doWhat": "ffxivQuestsByDay" };
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_ffxivQuestsByDay(container, result, type, pData);
  	});
}
