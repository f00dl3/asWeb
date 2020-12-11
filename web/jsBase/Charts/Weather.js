/* 
by Anthony Stump
Created: 18 Nov 2020
Updated: 10 Dec 2020
 */

let limit = 1200;
let sLimit = 256;

function ch_chart_CF6Depart(container, result, type, pData) {
	let doLegend = true;
	let doX = true;
	let lbRadius = 1;
	if(type === "thumb") { doLegend = false; doX = false; lbRadius = 0; }
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
				{ label: 'Departure', borderColor: 'yellow', data: aData }
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
}

function ch_get_CF6Depart(container, type, pOpts) {
    let dateOverrideStart = getDate("day", -365, "dateOnly");
   	let dateOverrideEnd = getDate("day", 0, "dateOnly");
	if(!isEmpty(pOpts)) {
		dateOverrideStart = pOpts.dateStart;
		dateOverrideEnd = pOpts.dateEnd;
	}
	let pData = { 
		"doWhat": "WxObsChartsCF6",
        "dateStart": dateOverrideStart,
        "dateEnd": dateOverrideEnd,
        "type": "depart"
	};
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_CF6Depart(container, result, type, pData);
  	});
}

function ch_chart_CF6Temps(container, result, type, pData) {
	let doLegend = true;
	let doX = true;
	let lbRadius = 1;
	if(type === "thumb") { doLegend = false; doX = false; lbRadius = 0; }
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
				{ label: 'High', borderColor: 'red', data: aData },
				{ label: 'Low', borderColor: 'blue', data: aData2 },
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
}

function ch_get_CF6Temps(container, type, pOpts) {
    let dateOverrideStart = getDate("day", -365, "dateOnly");
   	let dateOverrideEnd = getDate("day", 0, "dateOnly");
	if(!isEmpty(pOpts)) {
		dateOverrideStart = pOpts.dateStart;
		dateOverrideEnd = pOpts.dateEnd;
	}
	let pData = { 
		"doWhat": "WxObsChartsCF6",
        "dateStart": dateOverrideStart,
        "dateEnd": dateOverrideEnd,
        "type": "temps"
	};
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_CF6Temps(container, result, type, pData);
  	});
}

function ch_chart_ObsJSONHumidity(container, result, type, pData) {
	let doLegend = true;
	let doX = true;
	let lbRadius = 1;
	if(type === "thumb") { doLegend = false; doX = false; lbRadius = 0; }
	let resultJ = JSON.parse(result);
	let aLabels = resultJ.labels.reverse();
	let aData = resultJ.data.reverse();
	aLabels = trimArray(aLabels, limit);
	aData = trimArray(aData, limit);
	var ctx = document.getElementById(container).getContext('2d');
	var chart = null;
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [
				{ label: 'Humidity', borderColor: 'lightlbue', backgroundColor: "blue", data: aData }
			]
		},
		options: {
			elements: { point: { radius: lbRadius } },
			legend: { display: doLegend },
			scales: {
				xAxes: [ { ticks: { display: doX } } ],
				yAxes: [ { ticks: { callback: function(value, index, values) { return value; } } } ]
			}
		}
	});
}

function ch_get_ObsJSONHumidity(container, type, pOpts) {
    let dateOverrideStart = getDate("hour", -72, "full"); 
   	let dateOverrideEnd = getDate("hour", 0, "full");
	let stationId = "KOJC";
	if(!isEmpty(pOpts)) {
		dateOverrideStart = pOpts.dateStart;
		dateOverrideEnd = pOpts.dateEnd;
		stationId = pOpts.station;
	}
	let pData = { 
		"doWhat": "WxObsCharts",
        "startTime": dateOverrideStart,
        "endTime": dateOverrideEnd,
        "order": "DESC",
        "limit": sLimit,
        "stationId": stationId,
		"type": "humidity"
	};
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_ObsJSONHumidity(container, result, type, pData);
  	});
}

function ch_chart_ObsJSONHumidityH(container, result, type, pData) {
	let doLegend = true;
	let doX = true;
	let lbRadius = 1;
	if(type === "thumb") { doLegend = false; doX = false; lbRadius = 0; }
	let resultJ = JSON.parse(result);
	let aLabels = resultJ.labels.reverse();
	let aData = resultJ.data.reverse();
	aLabels = trimArray(aLabels, limit);
	aData = trimArray(aData, limit);
	var ctx = document.getElementById(container).getContext('2d');
	var chart = null;
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [
				{ label: 'Humidity', borderColor: 'lightlbue', backgroundColor: "blue", data: aData }
			]
		},
		options: {
			elements: { point: { radius: lbRadius } },
			legend: { display: doLegend },
			scales: {
				xAxes: [ { ticks: { display: doX } } ],
				yAxes: [ { ticks: { callback: function(value, index, values) { return value; } } } ]
			}
		}
	});
}

function ch_get_ObsJSONHumidityH(container, type) {
	let pData = { "doWhat": "ObsJSONHumidityH" };
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_ObsJSONHumidityH(container, result, type, pData);
  	});
}

function ch_chart_ObsJSONPrecipRateH(container, result, type, pData) {
	let doLegend = true;
	let doX = true;
	let lbRadius = 1;
	if(type === "thumb") { doLegend = false; doX = false; lbRadius = 0; }
	let timeout = getRefresh("semiRapid");
	let resultJ = JSON.parse(result);
	let aLabels = resultJ.labels.reverse();
	let aData = resultJ.data.reverse();
	let aData2 = resultJ.data2.reverse();
	aLabels = trimArray(aLabels, limit);
	aData = trimArray(aData, limit);
	aData2 = trimArray(aData2, limit);
	var ctx = document.getElementById(container).getContext('2d');
	var chart = null;
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [
				{ label: 'Rate', borderColor: 'green', backgroundColor: 'darkgreen', data: aData },
				{ label: 'Daily', borderColor: 'lightblue',  data: aData2 }
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
				yAxes: [ { ticks: { callback: function(value, index, values) { return value + "\"/h"; } } } ]
			}
		}
	});
	setInterval(() => { ch_get_ObsJSONPrecipRateH_Update(chart, pData); }, timeout);
}

function ch_get_ObsJSONPrecipRateH(container, type) {
	let pData = { "doWhat": "ObsJSONPrecipRateH" };
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_ObsJSONPrecipRateH(container, result, type, pData);
  	});
}

function ch_get_ObsJSONPrecipRateH_Update(chart, pData) {
	$.post(getResource("Chart3"), pData, function (result) {
		let resultJ = JSON.parse(result);
		let tLabel = resultJ.labels[0];
		let tData = resultJ.data[0];
		let tData2 = resultJ.data2[0];
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

function ch_chart_ObsJSONPressure(container, result, type, pData) {
	let doLegend = true;
	let doX = true;
	let lbRadius = 1;
	if(type === "thumb") { doLegend = false; doX = false; lbRadius = 0; }
	let resultJ = JSON.parse(result);
	let aLabels = resultJ.labels.reverse();
	let aData = resultJ.data.reverse();
	aLabels = trimArray(aLabels, limit);
	aData = trimArray(aData, limit);
	var ctx = document.getElementById(container).getContext('2d');
	var chart = null;
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [
				{ label: 'Pressure', borderColor: 'orange', backgroundColor: "red", data: aData }
			]
		},
		options: {
			elements: { point: { radius: lbRadius } },
			legend: { display: doLegend },
			scales: {
				xAxes: [ { ticks: { display: doX } } ],
				yAxes: [ { ticks: { callback: function(value, index, values) { return value; } } } ]
			}
		}
	});
}

function ch_get_ObsJSONPressure(container, type, pOpts) {
    let dateOverrideStart = getDate("hour", -72, "full"); 
   	let dateOverrideEnd = getDate("hour", 0, "full");
	let stationId = "KOJC";
	if(!isEmpty(pOpts)) {
		dateOverrideStart = pOpts.dateStart;
		dateOverrideEnd = pOpts.dateEnd;
		stationId = pOpts.station;
	}
	let pData = { 
		"doWhat": "WxObsCharts",
        "startTime": dateOverrideStart,
        "endTime": dateOverrideEnd,
        "order": "DESC",
        "limit": sLimit,
        "stationId": stationId,
		"type": "pressure"
	};
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_ObsJSONPressure(container, result, type, pData);
  	});
}

function ch_chart_ObsJSONPressureH(container, result, type, pData) {
	let doLegend = true;
	let doX = true;
	let lbRadius = 1;
	if(type === "thumb") { doLegend = false; doX = false; lbRadius = 0; }
	let resultJ = JSON.parse(result);
	let aLabels = resultJ.labels.reverse();
	let aData2 = resultJ.data2.reverse();
	aLabels = trimArray(aLabels, limit);
	aData2 = trimArray(aData2, limit);
	var ctx = document.getElementById(container).getContext('2d');
	var chart = null;
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [
				{ label: 'Pressure', borderColor: 'orange', backgroundColor: "red", data: aData2 }
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
}

function ch_get_ObsJSONPressureH(container, type) {
	let pData = { "doWhat": "ObsJSONPressureH" };
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_ObsJSONPressureH(container, result, type, pData);
  	});
}

function ch_chart_ObsJSONTemp(container, result, type, pData) {
	let doLegend = true;
	let doX = true;
	let lbRadius = 1;
	if(type === "thumb") { doLegend = false; doX = false; lbRadius = 0; }
	let resultJ = JSON.parse(result);
	let aLabels = resultJ.labels.reverse();
	let aData = resultJ.data.reverse();
	let aData2 = resultJ.data2.reverse();
	aLabels = trimArray(aLabels, limit);
	aData = trimArray(aData, limit);
	aData2 = trimArray(aData2, limit);
	var ctx = document.getElementById(container).getContext('2d');
	var chart = null;
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [
				{ label: 'Temperature', borderColor: 'red', data: aData },
				{ label: 'Dewpoint', borderColor: 'blue', data: aData2 },
			]
		},
		options: {
			elements: { point: { radius: lbRadius } },
			legend: { display: doLegend },
			scales: {
				xAxes: [ { ticks: { display: doX } } ],
				yAxes: [ { ticks: { callback: function(value, index, values) { return value + "F"; } } } ]
			}
		}
	});
}

function ch_get_ObsJSONTemp(container, type, pOpts) {
    let dateOverrideStart = getDate("hour", -72, "full"); 
   	let dateOverrideEnd = getDate("hour", 0, "full");
	let stationId = "KOJC";
	if(!isEmpty(pOpts)) {
		dateOverrideStart = pOpts.dateStart;
		dateOverrideEnd = pOpts.dateEnd;
		stationId = pOpts.station;
	}
	let pData = { 
		"doWhat": "WxObsCharts",
        "startTime": dateOverrideStart,
        "endTime": dateOverrideEnd,
        "order": "DESC",
        "limit": sLimit,
        "stationId": stationId,
		"type": "temp"
	};
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_ObsJSONTemp(container, result, type, pData);
  	});
}

function ch_chart_ObsJSONTempH(container, result, type, pData) {
	let doLegend = true;
	let doX = true;
	let lbRadius = 1;
	if(type === "thumb") { doLegend = false; doX = false; lbRadius = 0; }
	let timeout = getRefresh("semiRapid");
	let resultJ = JSON.parse(result);
	let aLabels = resultJ.labels.reverse();
	let aData = resultJ.data.reverse();
	let aData2 = resultJ.data2.reverse();
	aLabels = trimArray(aLabels, limit);
	aData = trimArray(aData, limit);
	aData2 = trimArray(aData2, limit);
	var ctx = document.getElementById(container).getContext('2d');
	var chart = null;
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [
				{ label: 'Temperature', borderColor: 'red', data: aData },
				{ label: 'Dewpoint', borderColor: 'blue', data: aData2 },
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
	setInterval(() => { ch_get_ObsJSONTempH_Update(chart, pData); }, timeout);
}

function ch_get_ObsJSONTempH(container, type) {
	let pData = { "doWhat": "ObsJSONTempH" };
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_ObsJSONTempH(container, result, type, pData);
  	});
}

function ch_get_ObsJSONTempH_Update(chart, pData) {
	$.post(getResource("Chart3"), pData, function (result) {
		let resultJ = JSON.parse(result);
		let tLabel = resultJ.labels[0];
		let tData = resultJ.data[0];
		let tData2 = resultJ.data2[0];
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

function ch_chart_ObsJSONWind(container, result, type, pData) {
	let doLegend = true;
	let doX = true;
	let lbRadius = 1;
	if(type === "thumb") { doLegend = false; doX = false; lbRadius = 0; }
	let resultJ = JSON.parse(result);
	let aLabels = resultJ.labels.reverse();
	let aData = resultJ.data.reverse();
	let aData2 = resultJ.data2.reverse();
	aLabels = trimArray(aLabels, limit);
	aData = trimArray(aData, limit);
	aData2 = trimArray(aData2, limit);
	var ctx = document.getElementById(container).getContext('2d');
	var chart = null;
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [
				{ label: 'Speed', borderColor: 'yellow', backgroundColor: "gold", data: aData },
				{ label: 'Gusts', borderColor: 'orange', data: aData2 },
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
}

function ch_get_ObsJSONWind(container, type, pOpts) {
    let dateOverrideStart = getDate("hour", -72, "full"); 
   	let dateOverrideEnd = getDate("hour", 0, "full");
	let stationId = "KOJC";
	if(!isEmpty(pOpts)) {
		dateOverrideStart = pOpts.dateStart;
		dateOverrideEnd = pOpts.dateEnd;
		stationId = pOpts.station;
	}
	let pData = { 
		"doWhat": "WxObsCharts",
        "startTime": dateOverrideStart,
        "endTime": dateOverrideEnd,
        "order": "DESC",
        "limit": sLimit,
        "stationId": stationId,
		"type": "wind"
	};
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_ObsJSONWind(container, result, type, pData);
  	});
}

function ch_chart_ObsJSONWindH(container, result, type, pData) {
	let doLegend = true;
	let doX = true;
	let lbRadius = 1;
	if(type === "thumb") { doLegend = false; doX = false; lbRadius = 0; }
	let resultJ = JSON.parse(result);
	let aLabels = resultJ.labels.reverse();
	let aData = resultJ.data.reverse();
	let aData2 = resultJ.data2.reverse();
	aLabels = trimArray(aLabels, limit);
	aData = trimArray(aData, limit);
	aData2 = trimArray(aData2, limit);
	var ctx = document.getElementById(container).getContext('2d');
	var chart = null;
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [
				{ label: 'Speed', borderColor: 'yellow', backgroundColor: "gold", data: aData },
				{ label: 'Gusts', borderColor: 'orange', data: aData2 },
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
}

function ch_get_ObsJSONWindH(container, type) {
	let pData = { "doWhat": "ObsJSONWindH" };
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_ObsJSONWindH(container, result, type, pData);
  	});
}
