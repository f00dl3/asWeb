/* 
by Anthony Stump
Created: 18 Nov 2020
Updated: 21 Nov 2020
 */

function ch_chart_ObsJSONTempH(container, result, type, pData) {
	let timeout = getRefresh("semiRapid");
	let limit = 512;
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
				{
					label: 'Temperature',
					borderColor: 'red',
					data: aData
				},
				{
					label: 'Dewpoint',
					borderColor: 'blue',
					data: aData2
				},
			]
		},
		options: {
			scales: {
				yAxes: [ 
					{ ticks: { callback: function(value, index, values) { return value + "F"; } } }
				]
			}
		}
	});
	setInterval(() => { ch_get_FinENW_All_R_Update(chart, pData); }, timeout);
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
			console.log("Skipping update - duplicate data!");
		} else {
			chart.data.labels.push(tLabel);
			chart.data.datasets.forEach((dataset) => { dataset.data.push(tData); dataset.data2.push(tData2); });
			chart.update();
			chart.data.labels.shift();
			chart.data.datasets.forEach((dataset) => { dataset.data.shift(); dataset.data2.shift(); });
			chart.update();
			console.log("Chart update completed - " + tLabel + " & " + tData);
		}
	});
}