/* 
by Anthony Stump
Created: 28 Nov 2020
Updated: on creation
 */

function ch_chart_Bills(container, result, type) {
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
	let aLabels = resultJ.labels.reverse();
	let aData = resultJ.data.reverse();
	let aData3 = resultJ.data3.reverse();
	let aData4 = resultJ.data4.reverse();
	let aData6 = resultJ.data6.reverse();
	let ele = resultJ.ele.reverse();
	let gas = resultJ.gas.reverse();
	let web = resultJ.web.reverse();
	let pho = resultJ.pho.reverse();
	var ctx = document.getElementById(container).getContext('2d');
	let chart = null
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [
				{ label: 'Total', borderColor: 'white', data: aData },
				{ label: 'Electric', borderColor: 'lightblue', data: ele },
				{ label: 'Gas', borderColor: 'orange', data: gas },
				{ label: 'Wat/Swr', borderColor: 'blue', data: aData3 },
				{ label: 'Trash', borderColor: 'pink', data: aData4 },
				{ label: 'Web', borderColor: 'brown', data: web },
				{ label: 'Phone', borderColor: 'red', data: pho },
				{ label: 'Gym/Other', borderColor: 'grey', data: aData6 }
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
					display: doX
				}],
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
}

function ch_get_Bills(container, type) {
	let pData = { "doWhat": "Bills" };
	$.post(getResource("Chart3"), pData, function(result) {
		ch_chart_Bills(container, result, type);
  	});
}