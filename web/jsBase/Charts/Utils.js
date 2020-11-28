/* 
by Anthony Stump
Created: 28 Nov 2020
Updated: on creation
 */

function ch_chart_CellData(container, result, type) {
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
	var ctx = document.getElementById(container).getContext('2d');
	let chart = null
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [
				{ label: 'MB', borderColor: 'white', backgroundColor: 'grey', data: aData }
			]
		},
		options: {
			elements: {
				line: { borderWidth: lbRadius, tension: 0 },
				point: { radius: lbRadius }
			},
			legend: {
				display: doLegend
			},
			scales: {
				xAxes: [{
					display: doX
				}],
				yAxes: [{ 
					ticks: { callback: function(value, index, values) { return autoUnits(value*1000000); } } 
				}]
			}
		}
	});
}

function ch_get_CellData(container, type) {
	let pData = { "doWhat": "CellData" };
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_CellData(container, result, type);
  	});
}

function ch_chart_CellMin(container, result, type) {
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
	var ctx = document.getElementById(container).getContext('2d');
	let chart = null
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [
				{ label: 'Minutes', borderColor: 'white', backgroundColor: 'grey', data: aData }
			]
		},
		options: {
			elements: {
				line: { borderWidth: lbRadius, tension: 0 },
				point: { radius: lbRadius }
			},
			legend: {
				display: doLegend
			},
			scales: {
				xAxes: [{
					display: doX
				}],
				yAxes: [{ 
					ticks: { callback: function(value, index, values) { return value; } } 
				}]
			}
		}
	});
}

function ch_get_CellMin(container, type) {
	let pData = { "doWhat": "CellMin" };
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_CellMin(container, result, type);
  	});
}

function ch_chart_CellMMS(container, result, type) {
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
	var ctx = document.getElementById(container).getContext('2d');
	let chart = null
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [
				{ label: 'MMS', borderColor: 'orange', backgroundColor: 'yellow', data: aData }
			]
		},
		options: {
			elements: {
				line: { borderWidth: lbRadius, tension: 0 },
				point: { radius: lbRadius }
			},
			legend: {
				display: doLegend
			},
			scales: {
				xAxes: [{
					display: doX
				}],
				yAxes: [{ 
					ticks: { callback: function(value, index, values) { return value; } } 
				}]
			}
		}
	});
}

function ch_get_CellMMS(container, type) {
	let pData = { "doWhat": "CellMMS" };
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_CellMMS(container, result, type);
  	});
}

function ch_chart_CellText(container, result, type) {
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
	var ctx = document.getElementById(container).getContext('2d');
	let chart = null
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [
				{ label: 'Texts', borderColor: 'red', backgroundColor: 'orange', data: aData }
			]
		},
		options: {
			elements: {
				line: { borderWidth: lbRadius, tension: 0 },
				point: { radius: lbRadius }
			},
			legend: {
				display: doLegend
			},
			scales: {
				xAxes: [{
					display: doX
				}],
				yAxes: [{ 
					ticks: { callback: function(value, index, values) { return value; } } 
				}]
			}
		}
	});
}

function ch_get_CellText(container, type) {
	let pData = { "doWhat": "CellText" };
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_CellText(container, result, type);
  	});
}

function ch_chart_UseElecD(container, result, type) {
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
	var ctx = document.getElementById(container).getContext('2d');
	let chart = null
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [
				{ label: 'kWh', borderColor: 'blue', backgroundColor: 'darkBlue', data: aData }
			]
		},
		options: {
			elements: {
				line: { borderWidth: lbRadius, tension: 0 },
				point: { radius: lbRadius }
			},
			legend: {
				display: doLegend
			},
			scales: {
				xAxes: [{
					display: doX
				}],
				yAxes: [{ 
					ticks: { callback: function(value, index, values) { return value; } } 
				}]
			}
		}
	});
}

function ch_get_UseElecD(container, type) {
	let pData = { "doWhat": "UseElecD" };
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_UseElecD(container, result, type);
  	});
}

function ch_chart_UseGas(container, result, type) {
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
	var ctx = document.getElementById(container).getContext('2d');
	let chart = null
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [
				{ label: 'MCF', borderColor: 'green', backgroundColor: 'darkgreen', data: aData }
			]
		},
		options: {
			elements: {
				line: { borderWidth: lbRadius, tension: 0 },
				point: { radius: lbRadius }
			},
			legend: {
				display: doLegend
			},
			scales: {
				xAxes: [{
					display: doX
				}],
				yAxes: [{ 
					ticks: { callback: function(value, index, values) { return value; } } 
				}]
			}
		}
	});
}

function ch_get_UseGas(container, type) {
	let pData = { "doWhat": "UseGas" };
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_UseGas(container, result, type);
  	});
}

function ch_chart_WebData(container, result, type) {
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
	var ctx = document.getElementById(container).getContext('2d');
	let chart = null
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [
				{ label: 'MB', borderColor: 'white', backgroundColor: 'grey', data: aData }
			]
		},
		options: {
			elements: {
				line: { borderWidth: lbRadius, tension: 0 },
				point: { radius: lbRadius }
			},
			legend: {
				display: doLegend
			},
			scales: {
				xAxes: [{
					display: doX
				}],
				yAxes: [{ 
					ticks: { callback: function(value, index, values) { return autoUnits(value*1000000); } } 
				}]
			}
		}
	});
}

function ch_get_WebData(container, type) {
	let pData = { "doWhat": "WebData" };
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_WebData(container, result, type);
  	});
}