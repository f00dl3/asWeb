/* 
by Anthony Stump
Created: 18 Nov 2020
Updated: 6 Jun 2021
 */

let wLb = [ "*", "R", "L", "X", "?", "C", "D", "Y", "RA", "RE", "T", "B", "S", "O" ];

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
	let aData13 = resultJ.data13;
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
				{ label: wLb[0], borderColor: 'white', backgroundColor: 'grey', data: aData },
				{ label: wLb[1], borderColor: 'yellow', backgroundColor: 'orange', data: aDataA },
				{ label: wLb[2], borderColor: 'green', backgroundColor: 'darkgreen', data: aData2 },
				{ label: wLb[3], borderColor: 'blue', backgroundColor: 'darkblue', data: aData3 },
				{ label: wLb[4], borderColor: 'grey', data: aData4, hidden: true },
				{ label: wLb[5], borderColor: 'brown', data: aData5, hidden: true },
				{ label: wLb[6], borderColor: 'red', backgroundColor: 'darkred', data: aData6 },
				{ label: wLb[7], borderColor: 'pink', backgroundColor: 'purple', data: aData7, hidden: true },
				{ label: wLb[8], borderColor: '#99ee00', backgroundColor: 'olive', data: aData8, hidden: true },
				{ label: wLb[9], borderColor: '#fcae1e', backgroundColor: '#fc6a03', data: aData9, hidden: true },
				{ label: wLb[10], borderColor: 'skyblue', backgroundColor: 'blue', data: aData10, hidden: true },
				{ label: wLb[11], borderColor: 'white', backgroundColor: 'grey', data: aData11, hidden: true },
				{ label: wLb[12], borderColor: 'lightgreen', backgroundColor: 'green', data: aData12, hidden: true },
				{ label: wLb[13], borderColor: '#ed820e', backgroundColor: '#cc5801', data: aData13, hidden: true }
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
	let description = "Net Worth";
	let lbRadius = 1;
	let doLegend = true;
	let doX = true;
	let doPopup = true;
	if(type === "thumb") { 
		description = "TOTL";
		doLegend = false;
		doX = false; 
		lbRadius = 0;
		doPopup = false;
	}
	let timeout = getRefresh("semiRapid");
	let limit = 256;
	let resultJ = JSON.parse(result);
	let aLabels = resultJ.labels.reverse();
	let aData = resultJ.data.reverse();
	let aData2 = resultJ.data2.reverse();
	let aData3 = resultJ.data3.reverse();
	let aData4 = resultJ.data4.reverse();
	let aData5 = resultJ.data5.reverse();
	let aData6 = resultJ.data6.reverse();
	let aData7 = resultJ.data7.reverse();
	let aData8 = resultJ.data8.reverse();
	let aData10 = resultJ.data10.reverse();
	let aData11 = resultJ.data11.reverse();
	let aData13 = resultJ.data13.reverse();
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
	aData8 = trimArray(aData8, limit);
	aData10 = trimArray(aData10, limit);
	aData11 = trimArray(aData11, limit);
	aData13 = trimArray(aData13, limit);
	var ctx = document.getElementById(container).getContext('2d');
	let allDifference = (aData[aData.length-1] - aData[0]).toFixed(2);
	let totalDifference = (aData[aData.length-1] - aData[aData.length-limit]).toFixed(2);
	let lastDifference = (aData[aData.length-1] - aData[aData.length-2]).toFixed(2);
	let extraDataContent = aLabels[aLabels.length-1] + ": " + autoUnits(aData[aData.length-1].toFixed(2)) +
		 " (P " + totalDifference + ", U " + lastDifference + ")";
	let chart = null;
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [
				{ label: wLb[0], borderColor: 'white', backgroundColor: 'grey', data: aData, hidden: true },
				{ label: wLb[1], borderColor: 'yellow', backgroundColor: 'orange', data: aDataA, hidden: true },
				{ label: wLb[2], borderColor: 'green', backgroundColor: 'darkgreen', data: aData2, hidden: true },
				{ label: wLb[3], borderColor: 'blue', backgroundColor: 'darkblue', data: aData3, hidden: true },
				{ label: wLb[4], borderColor: 'grey', data: aData4, hidden: true },
				{ label: wLb[5], borderColor: 'brown', data: aData5, hidden: true },
				{ label: wLb[6], borderColor: 'red', backgroundColor: 'darkred', data: aData6, hidden: true },
				{ label: wLb[7], borderColor: 'pink', backgroundColor: 'purple', data: aData7, hidden: true },
				{ label: wLb[8], borderColor: '#99ee00', backgroundColor: 'olive', data: aData8, hidden: true },
				{ label: wLb[10], borderColor: 'skyblue', backgroundColor: 'blue', data: aData10, hidden: false },
				{ label: wLb[11], borderColor: 'white', backgroundColor: 'grey', data: aData11, hidden: true },
				{ label: wLb[13], borderColor: '#ed820e', backgroundColor: '#cc5801', data: aData13, hidden: true }
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
	let ia = 0;
	setInterval(function() { ch_get_FinENW_All_R_Update(chart, pData, doPopup); console.log("FinENW_All_R Refresh "+ia); ia++; }, timeout);
}

function ch_get_FinENW_All_R(container, type) {
	let pData = { "doWhat": "wRapid" };
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_FinENW_All_R(container, result, type, pData);
  	});
}

function ch_get_FinENW_All_R_Update(chart, pData, doPopup) {
	$.post(getResource("Chart3"), pData, function (result) {
		let limit = 256;
		let resultJ = JSON.parse(result);
		let tLabel = resultJ.labels[0];
		let tData = resultJ.data[0];
		let tData2 = resultJ.data2[0];
		let tData3 = resultJ.data3[0];
		let tData4 = resultJ.data4[0];
		let tData5 = resultJ.data5[0];
		let tData6 = resultJ.data6[0];
		let tData7 = resultJ.data7[0];
		let tData8 = resultJ.data8[0];
		let tData10 = resultJ.data10[0];
		let tData11 = resultJ.data11[0];
		let tData13 = resultJ.data13[0];
		let tDataA = tData - tData4;
		let aData = resultJ.data.reverse();
		let allDifference = (aData[aData.length-1] - aData[0]).toFixed(2);
	        let totalDifference = (aData[aData.length-1] - aData[aData.length-limit]).toFixed(2);
        	let lastDifference = (aData[aData.length-1] - aData[aData.length-2]).toFixed(2);
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
			chart.data.datasets[8].data.push(tData8);
			chart.data.datasets[9].data.push(tData10);
			chart.data.datasets[10].data.push(tData11);
			chart.data.datasets[11].data.push(tData13);
			chart.update();
			chart.data.labels.shift();
			chart.data.datasets.forEach((dataset) => { dataset.data.shift(); });
			chart.update();
		        let extraDataContent = tLabel + ": " + autoUnits(tData.toFixed(2)) + 
				" (P " + totalDifference + ", U " + lastDifference + ")";
			if(doPopup) { showNotice("Update change: " + lastDifference); }
//			let extraDataContent = tLabel + ": " + autoUnits(tData.toFixed(2));
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
	let aData13 = resultJ.data13;
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
				{ label: wLb[0], borderColor: 'white', backgroundColor: 'grey', data: aData, hidden: true },
				{ label: wLb[1], borderColor: 'yellow', backgroundColor: 'orange', data: aDataA, hidden: false },
				{ label: wLb[2], borderColor: 'green', backgroundColor: 'darkgreen', data: aData2, hidden: true },
				{ label: wLb[3], borderColor: 'blue', backgroundColor: 'darkblue', data: aData3, hidden: true },
				{ label: wLb[4], borderColor: 'grey', data: aData4, hidden: true },
				{ label: wLb[5], borderColor: 'brown', data: aData5, hidden: true },
				{ label: wLb[6], borderColor: 'red', backgroundColor: 'darkred', data: aData6, hidden: true },
				{ label: wLb[7], borderColor: 'pink', backgroundColor: 'purple', data: aData7, hidden: true },
				{ label: wLb[8], borderColor: "#99ee00", backgroundColor: 'olive', data: aData8, hidden: true },
				{ label: wLb[9], borderColor: '#fcae1e', backgroundColor: '#fc6a03', data: aData9, hidden: true },
				{ label: wLb[10], borderColor: 'skyblue', backgroundColor: 'blue', data: aData10, hidden: true },
				{ label: wLb[11], borderColor: 'white', backgroundColor: 'grey', data: aData11, hidden: true },
				{ label: wLb[12], borderColor: 'lightgreen', backgroundColor: 'green', data: aData12, hidden: true },
				{ label: wLb[13], borderColor: '#ed820e', backgroundColor: '#cc5801', data: aData13, hidden: true }
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
	$('#extraDataHolder').text(extraDataContent);
}

function ch_get_FinENW_Year_A(container, type) {
	let pData = { "doWhat": "FinENW_Year_A" };
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_FinENW_Year_A(container, result, type);
  	});
}

function ch_chart_LiquidDist(container, result, type) {
	let doLegend = true;
	let doX = true;
	let lbRadius = 0;
	if(type === "thumb") { 
		doLegend = false;
		doX = false; 
	}
	let resultJ = JSON.parse(result);
	let aData3 = resultJ.data3.reverse();
	let aData4 = resultJ.data4.reverse();
	let aData5 = resultJ.data5.reverse();
	let aData8 = resultJ.data8.reverse();
	let aData9 = resultJ.data9.reverse();
	let aData10 = resultJ.data10.reverse();
	let aData11 = resultJ.data11.reverse();
	let aData12 = resultJ.data12.reverse();
	let tDatasetData = [ aData3[0], aData4[0], aData5[0], aData8[0], aData9[0], aData10[0], aData11[0], aData12[0] ];
	let tDatasetColors = [ "darkblue", "white", "yellow", "#99ee00", "#fc6a03", "blue", "grey", "green" ];
	var ctx = document.getElementById(container).getContext('2d');
	let chart = null
	chart = new Chart(ctx, {
		type: 'pie',
		data: {
			labels: [ wLb[3], wLb[4], wLb[5], wLb[8], wLb[9], wLb[10], wLb[11], wLb[12] ],
			datasets: [ {
				data: tDatasetData,
				backgroundColor: tDatasetColors,
				label: "Wealth"
			} ]
		},
		options: {
			legend: {
				display: doLegend
			}
		}
	});
}

function ch_get_LiquidDist(container, type) {
	let pData = { "doWhat": "FinENW_Year_A" };
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_LiquidDist(container, result, type);
  	});
}

function returnFormatted(numberIn) {
	let sstyle = "lightgreen";
	if(numberIn < 0) { sstyle = "red"; }
	if(numberIn == 0) { sstyle = "white"; }
	let rData = "<font color=" + sstyle + ">" + numberIn + "</font>";
	return rData;
}
