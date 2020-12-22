/* 
by Anthony Stump
Created: 21 Dec 2020
Updated: 22 Dec 2020
 */

function ch_chart_mSysCPU(container, result, type, pData) {
	let doLegend = true;
	let doX = true;
	let lbRadius = 1;
	if(type === "thumb") { doLegend = false; doX = false; lbRadius = 0; }
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
				{ label: 'CPU', borderColor: 'yellow', backgroundColor: 'gold', data: aData }
			]
		},
		options: {
			elements: { point: { radius: lbRadius } },
			legend: { display: doLegend },
			plugins: {
				zoom: { zoom: { enabled: doX } }
			},
			scales: {
				xAxes: [ { ticks: { display: doX } } ],
				yAxes: [ { ticks: { callback: function(value, index, values) { return value + "%"; } } } ]
			}
		}
	});
	setInterval(() => { ch_get_mSysCPU_Update(chart, pData); }, timeout);
}

function ch_get_mSysCPU(container, type, pOpts) {
	let pData = { "doWhat": "SysMonCharts", "type": "mSysCPU", "step": pOpts.step, "date": pOpts.date };
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_mSysCPU(container, result, type, pData);
  	});
}

function ch_get_mSysCPU_Update(chart, pData) {
	$.post(getResource("Chart3"), pData, function (result) {
		let resultJ = JSON.parse(result);
		let last = resultJ.labels.legnth-1;
		let tLabel = resultJ.labels[last];
		let tData = resultJ.data[last];
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

function ch_chart_mSysLoad(container, result, type, pData) {
	let doLegend = true;
	let doX = true;
	let lbRadius = 1;
	if(type === "thumb") { doLegend = false; doX = false; lbRadius = 0; }
	let timeout = getRefresh("semiRapid");
	let resultJ = JSON.parse(result);
	let aLabels = resultJ.labels;
	let aData = resultJ.data;
	let aData2 = resultJ.data2;
	let aData3 = resultJ.data2;
	var ctx = document.getElementById(container).getContext('2d');
	var chart = null;
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [
				{ label: 'Load', borderColor: 'red', data: aData },
				{ label: '5 min', borderColor: 'green', data: aData2 },
				{ label: '15 min', borderColor: 'blue', data: aData3 }
			]
		},
		options: {
			elements: { point: { radius: lbRadius } },
			legend: { display: doLegend },
			plugins: {
				zoom: { zoom: { enabled: doX } }
			},
			scales: {
				xAxes: [ { ticks: { display: doX } } ],
				yAxes: [ { ticks: { callback: function(value, index, values) { return value; } } } ]
			}
		}
	});
	setInterval(() => { ch_get_mSysLoad_Update(chart, pData); }, timeout);
}

function ch_get_mSysLoad(container, type, pOpts) {
	let pData = { "doWhat": "SysMonCharts", "type": "mSysLoad", "step": pOpts.step, "date": pOpts.date };
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_mSysLoad(container, result, type, pData);
  	});
}

function ch_get_mSysLoad_Update(chart, pData) {
	$.post(getResource("Chart3"), pData, function (result) {
		let resultJ = JSON.parse(result);
		let last = resultJ.labels.legnth-1;
		let tLabel = resultJ.labels[last];
		let tData = resultJ.data[last];
		let tData2 = resultJ.data2[last];
		let tData3 = resultJ.data3[last];
		let currentLabel = chart.data.labels[chart.data.labels.length-1];
		if(tLabel === currentLabel) {
		} else {
			chart.data.labels.push(tLabel);
			chart.data.datasets[0].data.push(tData);
			chart.data.datasets[1].data.push(tData2);
			chart.data.datasets[2].data.push(tData3);
			chart.update();
			chart.data.labels.shift();
			chart.data.datasets.forEach((ds) => {
				ds.data.shift();
			})
			chart.update();
		}
	});
}

function ch_chart_mSysMemory(container, result, type, pData) {
	let doLegend = true;
	let doX = true;
	let lbRadius = 1;
	if(type === "thumb") { doLegend = false; doX = false; lbRadius = 0; }
	let timeout = getRefresh("semiRapid");
	let resultJ = JSON.parse(result);
	let aLabels = resultJ.labels;
	let aData = resultJ.data;
	let aData2 = resultJ.data2;
	let aData3 = resultJ.data3;
	let aData4 = resultJ.data4;
	var ctx = document.getElementById(container).getContext('2d');
	var chart = null;
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [
				{ label: 'Used', borderColor: 'yellow', backgroundColor: 'gold', data: aData },
				{ label: 'Swap', borderColor: 'red', backgroundColor: 'darkred', data: aData2 },
				{ label: 'Buffers', borderColor: 'green', backgroundColor: 'darkgreen', data: aData4 },
				{ label: 'Cached', borderColor: 'blue', backgroundColor: 'darkblue', data: aData3 }
			]
		},
		options: {
			elements: { point: { radius: lbRadius } },
			legend: { display: doLegend },
			plugins: {
				zoom: { zoom: { enabled: doX } }
			},
			scales: {
				xAxes: [ { ticks: { display: doX } } ],
				yAxes: [ { ticks: { callback: function(value, index, values) { return autoUnits(value.toFixed(2)); } } } ]
			}
		}
	});
	setInterval(() => { ch_get_mSysMemory_Update(chart, pData); }, timeout);
}

function ch_get_mSysMemory(container, type, pOpts) {
	let pData = { "doWhat": "SysMonCharts", "type": "mSysMemory", "step": pOpts.step, "date": pOpts.date };
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_mSysMemory(container, result, type, pData);
  	});
}

function ch_get_mSysMemory_Update(chart, pData) {
	$.post(getResource("Chart3"), pData, function (result) {
		let resultJ = JSON.parse(result);
		let last = resultJ.labels.legnth-1;
		let tLabel = resultJ.labels[last];
		let tData = resultJ.data[last];
		let tData2 = resultJ.data2[last];
		let tData3 = resultJ.data3[last];
		let tData4 = resultJ.data4[last];
		let currentLabel = chart.data.labels[chart.data.labels.length-1];
		if(tLabel === currentLabel) {
		} else {
			chart.data.labels.push(tLabel);
			chart.data.datasets[0].data.push(tData);
			chart.data.datasets[1].data.push(tData2);
			chart.data.datasets[2].data.push(tData3);
			chart.data.datasets[3].data.push(tData4);
			chart.update();
			chart.data.labels.shift();
			chart.data.datasets.forEach((ds) => {
				ds.data.shift();
			})
			chart.update();
		}
	});
}

function ch_chart_mSysNet(container, result, type, pData) {
	let doLegend = true;
	let doX = true;
	let lbRadius = 1;
	if(type === "thumb") { doLegend = false; doX = false; lbRadius = 0; }
	let timeout = getRefresh("semiRapid");
	let resultJ = JSON.parse(result);
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
				{ label: 'Total', borderColor: 'yellow', backgroundColor: 'gold', data: aData },
				{ label: 'RX', borderColor: 'green', data: aData2 },
				{ label: 'TX', borderColor: 'red', data: aData3 }
			]
		},
		options: {
			elements: { point: { radius: lbRadius } },
			legend: { display: doLegend },
			plugins: {
				zoom: { zoom: { enabled: doX } }
			},
			scales: {
				xAxes: [ { ticks: { display: doX } } ],
				yAxes: [ { ticks: { callback: function(value, index, values) { return autoUnits(value.toFixed(2)); } } } ]
			}
		}
	});
	setInterval(() => { ch_get_mSysMemory_Update(chart, pData); }, timeout);
}

function ch_get_mSysNet(container, type, pOpts) {
	let pData = { "doWhat": "SysMonCharts", "type": "mSysNet", "step": pOpts.step, "date": pOpts.date };
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_mSysNet(container, result, type, pData);
  	});
}

function ch_get_mSysNet_Update(chart, pData) {
	$.post(getResource("Chart3"), pData, function (result) {
		let resultJ = JSON.parse(result);
		let last = resultJ.labels.legnth-1;
		let tLabel = resultJ.labels[last];
		let tData = resultJ.data[last];
		let tData2 = resultJ.data2[last];
		let tData3 = resultJ.data3[last];
		let currentLabel = chart.data.labels[chart.data.labels.length-1];
		if(tLabel === currentLabel) {
		} else {
			chart.data.labels.push(tLabel);
			chart.data.datasets[0].data.push(tData);
			chart.data.datasets[1].data.push(tData2);
			chart.data.datasets[2].data.push(tData3);
			chart.update();
			chart.data.labels.shift();
			chart.data.datasets.forEach((ds) => {
				ds.data.shift();
			})
			chart.update();
		}
	});
}

function ch_chart_mSysStorage(container, result, type, pData) {
	let doLegend = true;
	let doX = true;
	let lbRadius = 1;
	if(type === "thumb") { doLegend = false; doX = false; lbRadius = 0; }
	let timeout = getRefresh("semiRapid");
	let resultJ = JSON.parse(result);
	let aLabels = resultJ.labels;
	let aData = resultJ.data;
	let aData2 = resultJ.data2;
	let aData3 = resultJ.data3;
	let aData4 = resultJ.data4;
	var ctx = document.getElementById(container).getContext('2d');
	var chart = null;
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [
				{ label: 'Used', borderColor: 'yellow', backgroundColor: 'gold', data: aData },
				{ label: 'Swap', borderColor: 'red', backgroundColor: 'darkred', data: aData2 },
				{ label: 'Buffers', borderColor: 'green', backgroundColor: 'darkgreen', data: aData4 },
				{ label: 'Cached', borderColor: 'blue', backgroundColor: 'darkblue', data: aData3 }
			]
		},
		options: {
			elements: { point: { radius: lbRadius } },
			legend: { display: doLegend },
			plugins: {
				zoom: { zoom: { enabled: doX } }
			},
			scales: {
				xAxes: [ { ticks: { display: doX } } ],
				yAxes: [ { ticks: { callback: function(value, index, values) { return value + "%"; } } } ]
			}
		}
	});
	setInterval(() => { ch_get_mSysMemory_Update(chart, pData); }, timeout);
}

function ch_get_mSysStorage(container, type, pOpts) {
	let pData = { "doWhat": "SysMonCharts", "type": "mSysStorage", "step": pOpts.step, "date": pOpts.date };
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_mSysStorage(container, result, type, pData);
  	});
}

function ch_get_mSysStorage_Update(chart, pData) {
	$.post(getResource("Chart3"), pData, function (result) {
		let resultJ = JSON.parse(result);
		let last = resultJ.labels.legnth-1;
		let tLabel = resultJ.labels[last];
		let tData = resultJ.data[last];
		let tData2 = resultJ.data2[last];
		let tData3 = resultJ.data3[last];
		let tData4 = resultJ.data4[last];
		let currentLabel = chart.data.labels[chart.data.labels.length-1];
		if(tLabel === currentLabel) {
		} else {
			chart.data.labels.push(tLabel);
			chart.data.datasets[0].data.push(tData);
			chart.data.datasets[1].data.push(tData2);
			chart.data.datasets[2].data.push(tData3);
			chart.data.datasets[3].data.push(tData4);
			chart.update();
			chart.data.labels.shift();
			chart.data.datasets.forEach((ds) => {
				ds.data.shift();
			})
			chart.update();
		}
	});
}

function ch_chart_mSysTemp(container, result, type, pData) {
	let doLegend = true;
	let doX = true;
	let lbRadius = 1;
	if(type === "thumb") { doLegend = false; doX = false; lbRadius = 0; }
	let timeout = getRefresh("semiRapid");
	let resultJ = JSON.parse(result);
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
				{ label: 'CPU', borderColor: 'yellow', backgroundColor: 'gold', data: aData },
				{ label: 'Case', borderColor: 'red', backgroundColor: 'darkred', data: aData2 },
				{ label: 'NVIDIA GPU', borderColor: 'green', backgroundColor: 'darkgreen', data: aData3 }
			]
		},
		options: {
			elements: { point: { radius: lbRadius } },
			legend: { display: doLegend },
			plugins: {
				zoom: { zoom: { enabled: doX } }
			},
			scales: {
				xAxes: [ { ticks: { display: doX } } ],
				yAxes: [ { ticks: { callback: function(value, index, values) { return value + "F"; } } } ]
			}
		}
	});
	setInterval(() => { ch_get_mSysTemp_Update(chart, pData); }, timeout);
}

function ch_get_mSysTemp(container, type, pOpts) {
	let pData = { "doWhat": "SysMonCharts", "type": "mSysTemp", "step": pOpts.step, "date": pOpts.date };
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_mSysTemp(container, result, type, pData);
  	});
}

function ch_get_mSysTemp_Update(chart, pData) {
	$.post(getResource("Chart3"), pData, function (result) {
		let resultJ = JSON.parse(result);
		let last = resultJ.labels.legnth-1;
		let tLabel = resultJ.labels[last];
		let tData = resultJ.data[last];
		let tData2 = resultJ.data2[last];
		let tData3 = resultJ.data3[last];
		let currentLabel = chart.data.labels[chart.data.labels.length-1];
		if(tLabel === currentLabel) {
		} else {
			chart.data.labels.push(tLabel);
			chart.data.datasets[0].data.push(tData);
			chart.data.datasets[1].data.push(tData2);
			chart.data.datasets[2].data.push(tData3);
			chart.update();
			chart.data.labels.shift();
			chart.data.datasets.forEach((ds) => {
				ds.data.shift();
			})
			chart.update();
		}
	});
}
