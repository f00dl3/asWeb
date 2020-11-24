/* 
by Anthony Stump
Created: 18 Nov 2020
Updated: 23 Nov 2020
 */

function ch_chart_FinENW_All_A(container, result, type) {
	let timeFormat = 'YYYY-MM-DD';
	let doLegend = true;
	let doX = true;
	let lbRadius = 1;
	let doZoom = true;
	if(type === "thumb") { 
		doLegend = false;
		doX = false; 
		lbRadius = 0;
		doZoom = false;
	}
	let resultJ = JSON.parse(result);
	let aLabels_orig = resultJ.labels;
	let aLabels = [];
	aLabels_orig.forEach(function (aL) {
		let mTime = moment(aL, timeFormat);
		aLabels.push(mTime);
	});
	let aData = resultJ.data;
	let aData2 = resultJ.data2;
	let aData3 = resultJ.data3;
	let aData4 = resultJ.data4;
	let aData5 = resultJ.data5;
	let aData6 = resultJ.data6;
	let aDataA = [];
	let i = 0;
	aData.forEach(function(ad) {
		aDataA.push(ad-aData4[i]);
		i++;
	});
	var ctx = document.getElementById(container).getContext('2d');
	let chart = null
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [
				{ label: 'Total', borderColor: 'white', data: aData },
				{ label: 'Reportable', borderColor: 'orange', data: aDataA },
				{ label: 'Liquid', borderColor: 'green', data: aData2 },
				{ label: 'Fixed', borderColor: 'blue', data: aData3 },
				{ label: 'Insurance', borderColor: 'grey', data: aData4 },
				{ label: 'Credits', borderColor: 'yellow', data: aData5 },
				{ label: 'Debts', borderColor: 'red', data: aData6 }
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
					display: doX,
					type: 'time',
					time: { unit: 'month' }
				}],
				yAxes: [{ 
					ticks: { callback: function(value, index, values) { return "$" + value + "K"; } } 
				}]
			},
			plugins: {
				zoom: {
					zoom: {
						enabled: doZoom
					}
				}
			}
		}
	});
}

function ch_get_FinENW_All_A(container, type) {
	let pData = { "doWhat": "FinENW_All_A" };
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_FinENW_All_A(container, result, type);
  	});
}

function ch_chart_FinENW_All_R(container, result, type, pData) {
	let description = "Rapid Estimated Net Worth";
	let lbRadius = 1;
	let doLegend = true;
	let doX = true;
	if(type === "thumb") { 
		description = "RENW";
		doLegend = false;
		doX = false; 
		lbRadius = 0;
	}
	let timeout = getRefresh("semiRapid");
	let limit = 128;
	let resultJ = JSON.parse(result);
	let aLabels = resultJ.labels.reverse();
	let aData = resultJ.data.reverse();
	aLabels = trimArray(aLabels, limit);
	aData = trimArray(aData, limit);
	var ctx = document.getElementById(container).getContext('2d');
	let extraDataContent = aLabels[0] + ": " + autoUnits(aData[0].toFixed(2));
	let chart = null;
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [
				{ label: description, backgroundColor: 'grey', borderColor: 'white', data: aData }
			]
		},
		options: {
			elements: {
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
					{ ticks: { callback: function(value, index, values) { return "$" + autoUnits(value); } } }
				]
			}
		}
	});
	$('#extraDataHolder').text(extraDataContent);
	setInterval(() => { ch_get_FinENW_All_R_Update(chart, pData); }, timeout);
}

function ch_get_FinENW_All_R(container, type) {
	let pData = { "doWhat": "wRapid" };
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_FinENW_All_R(container, result, type, pData);
  	});
}

function ch_get_FinENW_All_R_Update(chart, pData) {
	$.post(getResource("Chart3"), pData, function (result) {
		let resultJ = JSON.parse(result);
		let tLabel = resultJ.labels[0];
		let tData = resultJ.data[0];
		let currentLabel = chart.data.labels[chart.data.labels.length-1];
		if(tLabel === currentLabel) {
			//console.log("Skipping update - duplicate data!");
		} else {
			chart.data.labels.push(tLabel);
			chart.data.datasets.forEach((dataset) => { dataset.data.push(tData); });
			chart.update();
			chart.data.labels.shift();
			chart.data.datasets.forEach((dataset) => { dataset.data.shift(); });
			chart.update();
			let extraDataContent = tLabel + ": " + autoUnits(tData.toFixed(2));
			$('#extraDataHolder').text(extraDataContent);
		}
	});
}

function ch_chart_FinENW_Year_A(container, result, type) {
	let resultJ = JSON.parse(result);
	let doLegend = true;
	let doX = true;
	let lbRadius = 1;
	let doZoom = true;
	if(type === "thumb") { 
		doLegend = false;
		doX = false; 
		lbRadius = 0;
		doZoom = false;
	}
	let aLabels = resultJ.labels;
	let aData = resultJ.data;
	let aData2 = resultJ.data2;
	let aData3 = resultJ.data3;
	let aData4 = resultJ.data4;
	let aData5 = resultJ.data5;
	let aData6 = resultJ.data6;
	let aDataA = [];
	let i = 0;
	aData.forEach((ad) => {
		aDataA.push(ad-aData4[i]);
		i++;
	});
	let lastValue_Label = aLabels[aData.length-1];
	let lastValue_Data = aData[aData.length-1];
	let previousValue_Data = aData[aData.length-2];
	let lastValue_Change = lastValue_Data - previousValue_Data;
	let extraDataContent = "As of " + lastValue_Label + ", total net worth is $" + autoUnits(lastValue_Data) + " (changed $" + lastValue_Change.toFixed(2) + ")";
	var ctx = document.getElementById(container).getContext('2d');
	let chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [
				{ label: 'Total', borderColor: 'white', data: aData },
				{ label: 'Reportable', borderColor: 'orange', data: aDataA },
				{ label: 'Liquid', borderColor: 'green', data: aData2 },
				{ label: 'Fixed', borderColor: 'blue', data: aData3 },
				{ label: 'Insurance', borderColor: 'grey', data: aData4 },
				{ label: 'Credits', borderColor: 'yellow', data: aData5 },
				{ label: 'Debts', borderColor: 'red', data: aData6 }
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
				xAxes: [
					{ ticks: { display: doX } }
				],
				yAxes: [{
					ticks: { callback: function(value, index, values) { return "$" + autoUnits(value); } } 
				}]
			},
			plugins: {
				zoom: {
					zoom: {
						enabled: doZoom
					}
				}
			}
		}
	});
	$('#extraDataHolder').text(extraDataContent);
}

function ch_get_FinENW_Year_A(container, type) {
	let pData = { "doWhat": "FinENW_Year_A" };
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_FinENW_Year_A(container, result, type);
  	});
}
