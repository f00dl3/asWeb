/* 
by Anthony Stump
Created: 19 Nov 2020
Updated: 10 Dec 2020
 */

function ch_chart_CalorieRange(container, result, type, pData) {
	let resultJ = JSON.parse(result);
	let doX = true;
	let doLegend = true;
	if(type === "thumb") {
		doX = false; 
		doLegend = false;
	}
	let aLabels = resultJ.labels;
	let aData = resultJ.data;
	let aData2 = resultJ.data2;
	let aData3 = resultJ.data3;
	let aData4 = resultJ.data4;
	let aData5 = resultJ.data5;
	var ctx = document.getElementById(container).getContext('2d');
	var chart = null;
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [
				{ label: 'Calories', borderColor: 'white', data: aData },
				{ label: 'Fat', borderColor: 'red', data: aData2 },
				{ label: 'Protein', borderColor: 'green', data: aData3 },
				{ label: 'Carbs', borderColor: 'yellow', data: aData4 },
				{ label: 'Burned', borderColor: 'blue', data: aData5 }
			]
		},
		options: {
			elements: {
				line: { borderWidth: 1, tension: 0 },
				point: { radius: 0 }
			},
			legend: {
				display: doLegend
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

function ch_get_CalorieRange(container, type, pOpts) {
    let xdt1 = getDate("day", -365, "dateOnly");
    let xdt2 = getDate("day", 0, "dateOnly");
	if(!isEmpty(pOpts)) {
		xdt1 = pOpts.xdt1;
		xdt2 = pOpts.xdt2;
	}
	let pData = { "doWhat": "CalorieRange", "XDT1": xdt1, "XDT2": xdt2 };
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_CalorieRange(container, result, type, pData);
  	});
}

function ch_chart_SleepRange(container, result, type, pData) {
	let resultJ = JSON.parse(result);
	let doX = true;
	let doLegend = true;
	if(type === "thumb") {
		doX = false; 
		doLegend = false 
	}
	let aLabels = resultJ.labels;
	let aData = resultJ.data;
	let aData2 = resultJ.data2;
	let aData3 = resultJ.data3;
	var ctx = document.getElementById(container).getContext('2d');
	var chart = null;
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [
				{ label: 'Sleep', borderColor: 'white', data: aData },
				{ label: 'Gaming', borderColor: 'blue', data: aData2 },
				{ label: 'Exercised', borderColor: 'red', data: aData3 }
			]
		},
		options: {
			elements: {
				line: { borderWidth: 1, tension: 0 },
				point: { radius: 0 }
			},
			legend: {
				display: doLegend
			},
			scales: {
				xAxes: [
					{ ticks: { display: doX } }
				],
				yAxes: [ 
					{ ticks: { callback: function(value, index, values) { return value + " hr"; } } }
				]
			}
		}
	});
}

function ch_get_SleepRange(container, type, pOpts) {
    let xdt1 = getDate("day", -365, "dateOnly");
    let xdt2 = getDate("day", 0, "dateOnly");
	if(!isEmpty(pOpts)) {
		xdt1 = pOpts.xdt1;
		xdt2 = pOpts.xdt2;
	}
	let pData = { "doWhat": "SleepRange", "XDT1": xdt1, "XDT2": xdt2 };
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_SleepRange(container, result, type, pData);
  	});
}

function ch_chart_WeightRange(container, result, type, pData) {
	let timeout = getRefresh("semiRapid");
	let resultJ = JSON.parse(result);
	let doX = true;
	if(type === "thumb") { doX = false; }
	let aLabels = resultJ.labels;
	let aData = resultJ.data;
	var ctx = document.getElementById(container).getContext('2d');
	var chart = null;
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [
				{ label: 'Weight', borderColor: 'yellow', backgroundColor: "gold", data: aData }
			]
		},
		options: {
			elements: {
				line: { borderWidth: 1, tension: 0 },
				point: { radius: 0 }
			},
			legend: {
				display: false
			},
			scales: {
				xAxes: [
					{ ticks: { display: doX } }
				],
				yAxes: [ 
					{ ticks: { callback: function(value, index, values) { return value + " lbs"; } } }
				]
			}
		}
	});
	setInterval(() => { ch_get_WeightRange_Update(chart, pData); }, timeout);
}

function ch_get_WeightRange(container, type, pOpts) {
    let xdt1 = getDate("day", -365, "dateOnly");
    let xdt2 = getDate("day", 0, "dateOnly");
	if(!isEmpty(pOpts)) {
		xdt1 = pOpts.xdt1;
		xdt2 = pOpts.xdt2;
	}
	let pData = { "doWhat": "WeightRange", "XDT1": xdt1, "XDT2": xdt2 };
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_WeightRange(container, result, type, pData);
  	});
}

function ch_get_WeightRange_Update(chart, pData) {
	$.post(getResource("Chart3"), pData, function (result) {
		let resultJ = JSON.parse(result);
		let tLabel = resultJ.labels[0];
		let tData = resultJ.data[0];
		let currentLabel = chart.data.labels[chart.data.labels.length-1];
		if(tLabel === currentLabel) {
		} else {
			chart.data.labels.push(tLabel);
			chart.data.datasets[0].data.push(tData);
			chart.update();
			chart.data.labels.shift();
			chart.data.datasets.forEach((ds) => {
				ds.data.shift();
			})
			chart.update();
		}
	});
}
