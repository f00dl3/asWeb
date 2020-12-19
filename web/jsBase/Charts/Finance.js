/* 
by Anthony Stump
Created: 18 Nov 2020
Updated: 19 Dec 2020
 */

function ch_chart_FinENW_All_A(container, result, type) {
	let timeFormat = 'YYYY-MM-DD';
	let doLegend = true;
	let doX = true;
	let lbRadius = 0;
	let doZoom = true;
	if(type === "thumb") { 
		doLegend = false;
		doX = false; 
		//lbRadius = 0;
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
	let aData7 = resultJ.data7;
	let aData8 = resultJ.data8;
	let aData9 = resultJ.data9;
	let aData10 = resultJ.data10;
	let aData11 = resultJ.data11;
	let aData12 = resultJ.data12;
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
				{ label: 'Insurance', borderColor: 'grey', data: aData4, hidden: true },
				{ label: 'Credits', borderColor: 'yellow', data: aData5, hidden: true },
				{ label: 'Debts', borderColor: 'red', data: aData6 },
				{ label: 'Liquidity', borderColor: 'pink', data: aData7, hidden: true },
				{ label: 'Fidelity', borderColor: 'green', data: aData8, hidden: true },
				{ label: 'EJones', borderColor: 'green', data: aData9, hidden: true },
				{ label: 'ETrade', borderColor: 'green', data: aData10, hidden: true },
				{ label: 'Checking', borderColor: 'green', data: aData11, hidden: true },
				{ label: 'Savings', borderColor: 'green', data: aData12, hidden: true }
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
	let aData2 = resultJ.data2.reverse();
	let aData3 = resultJ.data3.reverse();
	let aData4 = resultJ.data4.reverse();
	let aData5 = resultJ.data5.reverse();
	let aData6 = resultJ.data6.reverse();
	let aData7 = resultJ.data7.reverse();
	let aData10 = resultJ.data10.reverse();
	let aData11 = resultJ.data11.reverse();
	let aData12 = resultJ.data12.reverse();
	let aDataA = [];
	let i = 0;
	aData.forEach(function(ad) {
		aDataA.push(ad-aData4[i]);
		i++;
	});
	aLabels = trimArray(aLabels, limit);
	aData = trimArray(aData, limit);
	aDataA = trimArray(aDataA, limit);
	aData2 = trimArray(aData2, limit);
	aData3 = trimArray(aData3, limit);
	aData4 = trimArray(aData4, limit);
	aData5 = trimArray(aData5, limit);
	aData6 = trimArray(aData6, limit);
	aData7 = trimArray(aData7, limit);
	aData10 = trimArray(aData10, limit);
	aData11 = trimArray(aData11, limit);
	aData12 = trimArray(aData12, limit);
	var ctx = document.getElementById(container).getContext('2d');
	let extraDataContent = aLabels[0] + ": " + autoUnits(aData[0].toFixed(2));
	let chart = null;
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [
				{ label: description, backgroundColor: 'grey', borderColor: 'white', data: aData },
				{ label: 'Reportable', borderColor: 'orange', data: aDataAm, hidden: true },
				{ label: 'Liquid', borderColor: 'green', data: aData2, hidden: true },
				{ label: 'Fixed', borderColor: 'blue', data: aData3, hidden: true },
				{ label: 'Insurance', borderColor: 'grey', data: aData4, hidden: true },
				{ label: 'Credits', borderColor: 'yellow', data: aData5, hidden: true },
				{ label: 'Debts', borderColor: 'red', data: aData6, hidden: true },
				{ label: 'Liquidity', borderColor: 'pink', data: aData7, hidden: true },
				{ label: 'ETrade', borderColor: 'green', data: aData10, hidden: true },
				{ label: 'Checking', borderColor: 'green', data: aData11, hidden: true },
				{ label: 'Savings', borderColor: 'green', data: aData12, hidden: true }
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
				yAxes: [ 
					{ ticks: { callback: function(value, index, values) { return "$" + autoUnits(value.toFixed(0)); } } }
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
		let tData2 = resultJ.data2[0];
		let tData3 = resultJ.data3[0];
		let tData4 = resultJ.data4[0];
		let tData5 = resultJ.data5[0];
		let tData6 = resultJ.data6[0];
		let tData7 = resultJ.data7[0];
		let tData10 = resultJ.data10[0];
		let tData11 = resultJ.data11[0];
		let tData12 = resultJ.data12[0];
		let tDataA = tData - tData4;
		let currentLabel = chart.data.labels[chart.data.labels.length-1];
		if(tLabel === currentLabel) {
			//console.log("Skipping update - duplicate data!");
		} else {
			chart.data.labels.push(tLabel);
			chart.data.datasets[0].data.push(tData);
			chart.data.datasets[1].data.push(tDataA);
			chart.data.datasets[2].data.push(tData2);
			chart.data.datasets[3].data.push(tData3);
			chart.data.datasets[4].data.push(tData4);
			chart.data.datasets[5].data.push(tData5);
			chart.data.datasets[6].data.push(tData6);
			chart.data.datasets[7].data.push(tData7);
			chart.data.datasets[8].data.push(tData10);
			chart.data.datasets[9].data.push(tData11);
			chart.data.datasets[10].data.push(tData12);
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
	let lbRadius = 0;
	let doZoom = true;
	if(type === "thumb") { 
		doLegend = false;
		doX = false; 
		//lbRadius = 0;
		doZoom = false;
	}
	let aLabels = resultJ.labels;
	let aData = resultJ.data;
	let aData2 = resultJ.data2;
	let aData3 = resultJ.data3;
	let aData4 = resultJ.data4;
	let aData5 = resultJ.data5;
	let aData6 = resultJ.data6;
	let aData7 = resultJ.data7;
	let aData8 = resultJ.data8;
	let aData9 = resultJ.data9;
	let aData10 = resultJ.data10;
	let aData11 = resultJ.data11;
	let aData12 = resultJ.data12;
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
				{ label: 'Insurance', borderColor: 'grey', data: aData4, hidden: true },
				{ label: 'Credits', borderColor: 'yellow', data: aData5, hidden: true },
				{ label: 'Debts', borderColor: 'red', data: aData6 },
				{ label: 'Liquidity', borderColor: 'pink', data: aData7, hidden: true },
				{ label: 'Fidelity', borderColor: 'green', data: aData8, hidden: true },
				{ label: 'EJones', borderColor: 'green', data: aData9, hidden: true },
				{ label: 'ETrade', borderColor: 'green', data: aData10, hidden: true },
				{ label: 'Checking', borderColor: 'green', data: aData11, hidden: true },
				{ label: 'Savings', borderColor: 'green', data: aData12, hidden: true }
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
