/* 
by Anthony Stump
Created: 18 Nov 2020
Updated: on creation
 */

function chart_ObsJSONTempH(result) {
	let limit = 256;
	let resultJ = JSON.parse(result);
	let aLabels = resultJ.labels.reverse();
	let aData = resultJ.data.reverse();
	let aData2 = resultJ.data.reverse();
	aLabels = trimArray(aLabels, limit);
	aData = trimArray(aData, limit);
	aData2 = trimArray(aData2, limit);
	let lastValue_Label = aLabels[aData.length-1];
	let lastValue_Data = aData[aData.length-1];
	let previousValue_Data = aData[aData.length-2];
	let lastValue_Change = lastValue_Data - previousValue_Data;
	let extraDataContent = "As of " + lastValue_Label + ", temperature " + lastValue_Data + "F (changed " + lastValue_Change + "F)";
	var ctx = document.getElementById('ChartCanvas').getContext('2d');
	var chart = null;
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [
				{
					label: 'Temperature',
					backgroundColor: 'darkred',
					borderColor: 'red',
					data: aData
				},
				{
					label: 'Dewpoint',
					backgroundColor: 'darkblue',
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
	$('#extraDataHolder').text(extraDataContent);
}

function get_ObsJSONTempH() {
    var timeout = getRefresh("medium");
	let pData = { "doWhat": "ObsJSONTempH" };
	$.post(getResource("Chart3"), pData, function(result) {
		chart_ObsJSONTempH(result);
  	});
    setTimeout(function () { get_ObsJSONTempH(); }, timeout);
}
