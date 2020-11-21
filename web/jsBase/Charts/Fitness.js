/* 
by Anthony Stump
Created: 19 Nov 2020
Updated: 21 Nov 2020
 */

/* Figure out posting data range - needs dojo */

function ch_chart_WeightRange(container, result, type) {
	let resultJ = JSON.parse(result);
	let aLabels = resultJ.labels;
	let aData = resultJ.data;
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
			elements: {
				line: { borderWidth: 1, tension: 0 },
				point: { radius: 0 }
			},
			scales: {
				yAxes: [ 
					{ ticks: { callback: function(value, index, values) { return value + " lbs"; } } }
				]
			}
		}
	});
}

function ch_get_WeightRange(container, type) {
    let xdt1 = getDate("day", -365, "dateOnly");
    let xdt2 = getDate("day", 0, "dateOnly");
	let pData = { "doWhat": "WeightRange", "XDT1": xdt1, "XDT2": xdt2 };
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_WeightRange(container, result, type);
  	});
}
