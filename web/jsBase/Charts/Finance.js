/* 
by Anthony Stump
Created: 18 Nov 2020
Updated: on creation
 */

function chart_FinENW_All_R(result) {
	let limit = 72;
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
	var ctx = document.getElementById('ChartCanvas').getContext('2d');
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

function get_FinENW_All_R() {
    var timeout = getRefresh("medium");
	let pData = { "doWhat": "wRapid" };
	$.post(getResource("Chart3"), pData, function(result) {
		chart_FinENW_All_R(result);
  	});
    setTimeout(function () { get_FinENW_All_R(); }, timeout);
}

function chart_FinENW_Year_F(result) {
	let resultJ = JSON.parse(result);
	let aLabels = resultJ.labels;
	let aData = resultJ.data;
	let lastValue_Label = aLabels[aData.length-1];
	let lastValue_Data = aData[aData.length-1];
	let previousValue_Data = aData[aData.length-2];
	let lastValue_Change = lastValue_Data - previousValue_Data;
	let extraDataContent = "As of " + lastValue_Label + ", fixed net worth is $" + autoUnits(lastValue_Data) + " (changed $" + lastValue_Change + ")";
	var ctx = document.getElementById('ChartCanvas').getContext('2d');
	var chart = null;
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [{
				label: 'Fixed Net Worth',
				backgroundColor: 'darkblue',
				borderColor: 'blue',
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

function get_FinENW_Year_F() {
    var timeout = getRefresh("medium");
	let pData = { "doWhat": "wFixed" };
	$.post(getResource("Chart3"), pData, function(result) {
		chart_FinENW_Year_F(result);
  	});
}

function chart_FinENW_Year_L(result) {
	let resultJ = JSON.parse(result);
	let aLabels = resultJ.labels;
	let aData = resultJ.data;
	let lastValue_Label = aLabels[aData.length-1];
	let lastValue_Data = aData[aData.length-1];
	let previousValue_Data = aData[aData.length-2];
	let lastValue_Change = lastValue_Data - previousValue_Data;
	let extraDataContent = "As of " + lastValue_Label + ", liquid net worth is $" + autoUnits(lastValue_Data) + " (changed $" + lastValue_Change + ")";
	var ctx = document.getElementById('ChartCanvas').getContext('2d');
	var chart = null;
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [{
				label: 'Liquid Net Worth',
				backgroundColor: 'darkgreen',
				borderColor: 'green',
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

function get_FinENW_Year_L() {
    var timeout = getRefresh("medium");
	let pData = { "doWhat": "wLiquid" };
	$.post(getResource("Chart3"), pData, function(result) {
		chart_FinENW_Year_L(result);
  	});
}

function chart_FinENW_Year_T(result) {
	let resultJ = JSON.parse(result);
	let aLabels = resultJ.labels;
	let aData = resultJ.data;
	let lastValue_Label = aLabels[aData.length-1];
	let lastValue_Data = aData[aData.length-1];
	let previousValue_Data = aData[aData.length-2];
	let lastValue_Change = lastValue_Data - previousValue_Data;
	let extraDataContent = "As of " + lastValue_Label + ", total net worth is $" + autoUnits(lastValue_Data) + " (changed $" + lastValue_Change + ")";
	var ctx = document.getElementById('ChartCanvas').getContext('2d');
	var chart = null;
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [{
				label: 'Total Net Worth',
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

function get_FinENW_Year_T() {
    var timeout = getRefresh("medium");
	let pData = { "doWhat": "wTotal" };
	$.post(getResource("Chart3"), pData, function(result) {
		chart_FinENW_Year_T(result);
  	});
}
