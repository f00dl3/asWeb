/* 
by Anthony Stump
Created: 24 Nov 2020
Updated: on creation
 */

function ch_chart_msByDate(container, result, type, pData) {
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
	var ctx = document.getElementById(container).getContext('2d');
	var chart = null;
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [
				{ label: 'Adds', borderColor: 'pink', backgroundColor: "purple", data: aData },
			]
		},
		options: {
			elements: {
				line: { borderWidth: lbRadius },
				point: { radius: lbRadius }
			},
			legend: {
				display: doLegend
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
	//setInterval(() => { ch_get_msByDate_Update(chart, pData); }, timeout);
}

function ch_get_msByDate(container, type) {
	let pData = { "doWhat": "msByDate" };
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_msByDate(container, result, type, pData);
  	});
}

function ch_get_msByDate_Update(chart, pData) {
	$.post(getResource("Chart3"), pData, function (result) {
		let resultJ = JSON.parse(result);
		let tLabel = resultJ.labels.reverse()[0];
		let tData = resultJ.data.reverse()[0];
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
