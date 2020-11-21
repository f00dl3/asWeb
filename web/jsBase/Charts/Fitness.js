/* 
by Anthony Stump
Created: 19 Nov 2020
Updated: on creation
 */

/* Figure out posting data range - needs dojo */

function chart_WeightRange(container, result) {
	let resultJ = JSON.parse(result);
	let aLabels = resultJ.labels.reverse();
	let aData = resultJ.data.reverse();
	var ctx = document.getElementById(container).getContext('2d');
	var chart = null;
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [
				{
					label: 'Weight',
					borderColor: 'white',
					backgroundColor: "grey",
					data: aData
				}
			]
		},
		options: {
			scales: {
				yAxes: [ 
					{ ticks: { callback: function(value, index, values) { return value + " lbs"; } } }
				]
			}
		}
	});
}

function get_WeightRange(container) {
    let xdt1 = getDate("day", -365, "dateOnly");
    let xdt2 = getDate("day", 0, "dateOnly");
	let pData = { "doWhat": "WeightRange", "XDT1": xdt1, "XDT2": xdt2 };
	$.post(getResource("Chart3"), pData, function(result) {
		chart_WeightRange(container, result);
  	});
}
