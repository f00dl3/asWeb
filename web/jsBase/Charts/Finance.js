/* 
by Anthony Stump
Created: 18 Nov 2020
Updated: on creation
 */

function chart_FinENW_All_R(result) {
	let resultJ = JSON.parse(result);
	let aLabels = resultJ.labels.reverse();
	let aData = resultJ.data.reverse();
	aLabels = trimArray(aLabels, 72);
	aData = trimArray(aData, 72);
	var ctx = document.getElementById('ChartCanvas').getContext('2d');
	var chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [{
				label: 'Rapid Estimated Net Worth',
				backgroundColor: 'rgb(255, 99, 132)',
				borderColor: 'rgb(255, 99, 132)',
				data: aData
			}]
		},
		options: {}
	});
}

function get_FinENW_All_R() {
    var timeout = getRefresh("medium");
	let pData = { "doWhat": "rapidWorth" };
	$.post(getResource("Chart3"), pData, function(result) {
		chart_FinENW_All_R(result);
  	});
    setTimeout(function () { get_FinENW_All_R(); }, timeout);
}
