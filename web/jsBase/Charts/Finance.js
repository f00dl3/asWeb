/* 
by Anthony Stump
Created: 18 Nov 2020
Updated: 20 Nov 2020
 */


function chart_FinENW_All_A(container, result) {
	var timeFormat = 'YYYY-MM-DD';
	let resultJ = JSON.parse(result);
	let aLabels = resultJ.labels;
	let aData = resultJ.data;
	let aData2 = resultJ.data2;
	let aData3 = resultJ.data3;
	let aData4 = resultJ.data4;
	let aData5 = resultJ.data5;
	let aData6 = resultJ.data6;
	var ctx = document.getElementById(container).getContext('2d');
	var chart = null;
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [
				{
					label: 'Total',
					borderColor: 'white',
					data: aData
				},
				{
					label: 'Liquid',
					borderColor: 'green',
					data: aData2
				},
				{
					label: 'Fixed',
					borderColor: 'blue',
					data: aData3
				},
				{
					label: 'Insurance',
					borderColor: 'grey',
					data: aData4
				},
				{
					label: 'Credits',
					borderColor: 'yellow',
					data: aData5
				},
				{
					label: 'Debts',
					borderColor: 'red',
					data: aData6
				},
			]
		},
		options: {
			scales: {
				yAxes: [ 
					{ ticks: { callback: function(value, index, values) { return "$" + value + "K"; } } }
				]
			}
		}
	});
	$('#extraDataHolder').text(extraDataContent);
}

function get_FinENW_All_A(container) {
	let pData = { "doWhat": "FinENW_All_A" };
	$.post(getResource("Chart3"), pData, function(result) {
		chart_FinENW_All_A(container, result);
  	});
}

function chart_FinENW_All_R(container, result) {
	let limit = 128;
	let resultJ = JSON.parse(result);
	let aLabels = resultJ.labels.reverse();
	let aData = resultJ.data.reverse();
	aLabels = trimArray(aLabels, limit);
	aData = trimArray(aData, limit);
	let lastValue_Label = aLabels[aData.length-1];
	let lastValue_Data = aData[aData.length-1];
	let previousValue_Data = aData[aData.length-2];
	let lastValue_Change = lastValue_Data - previousValue_Data;
	let extraDataContent = "As of " + lastValue_Label + ", net worth is $" + autoUnits(lastValue_Data) + " (changed $" + lastValue_Change + ")";
	var ctx = document.getElementById(container).getContext('2d');
	var chart = null;
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [{
				label: 'Rapid Estimated Net Worth',
				backgroundColor: 'grey',
				borderColor: 'white',
				data: aData
			}]
		},
		options: {
			scales: {
				yAxes: [ 
					{ ticks: { callback: function(value, index, values) { return "$" + autoUnits(value); } } }
				]
			}
		}
	});
	$('#extraDataHolder').text(extraDataContent);
}

function get_FinENW_All_R(container) {
    var timeout = getRefresh("medium");
	let pData = { "doWhat": "wRapid" };
	$.post(getResource("Chart3"), pData, function(result) {
		chart_FinENW_All_R(container, result);
  	});
    setTimeout(function () { get_FinENW_All_R(container); }, timeout);
}

function chart_FinENW_Year_A(container, result) {
	let resultJ = JSON.parse(result);
	let aLabels = resultJ.labels;
	let aData = resultJ.data;
	let aData2 = resultJ.data2;
	let aData3 = resultJ.data3;
	let aData4 = resultJ.data4;
	let aData5 = resultJ.data5;
	let aData6 = resultJ.data6;
	let lastValue_Label = aLabels[aData.length-1];
	let lastValue_Data = aData[aData.length-1];
	let previousValue_Data = aData[aData.length-2];
	let lastValue_Change = lastValue_Data - previousValue_Data;
	let extraDataContent = "As of " + lastValue_Label + ", total net worth is $" + autoUnits(lastValue_Data) + " (changed $" + lastValue_Change + ")";
	var ctx = document.getElementById(container).getContext('2d');
	var chart = null;
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [
				{
					label: 'Total',
					borderColor: 'white',
					data: aData
				},
				{
					label: 'Liquid',
					borderColor: 'green',
					data: aData2
				},
				{
					label: 'Fixed',
					borderColor: 'blue',
					data: aData3
				},
				{
					label: 'Insurance',
					borderColor: 'grey',
					data: aData4
				},
				{
					label: 'Credits',
					borderColor: 'yellow',
					data: aData5
				},
				{
					label: 'Debts',
					borderColor: 'red',
					data: aData6
				}
			]
		},
		options: {
			scales: {
				yAxes: [ 
					{ ticks: { callback: function(value, index, values) { return "$" + autoUnits(value); } } }
				]
			}
		}
	});
	$('#extraDataHolder').text(extraDataContent);
}

function get_FinENW_Year_A(container) {
	let pData = { "doWhat": "FinENW_Year_A" };
	$.post(getResource("Chart3"), pData, function(result) {
		chart_FinENW_Year_A(container, result);
  	});
}
