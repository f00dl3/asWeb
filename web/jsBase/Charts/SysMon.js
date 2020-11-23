/* 
by Anthony Stump
Created: 21 Nov 2020
Updated: 23 Nov 2020
 */

function ch_chart_Rapid_CPU(container, result, type, pData) {
	let timeout = getRefresh("semiRapid");
	let snmp = JSON.parse(result);
	let aLabels = [];
	aLabels.push("0");
	let aData = [];
    var cpuLoads = [
        snmp.cpu1Load,
        snmp.cpu2Load,
        snmp.cpu3Load,
        snmp.cpu4Load,
        snmp.cpu5Load,
        snmp.cpu6Load,
        snmp.cpu7Load,
        snmp.cpu8Load
    ];
    var cumCpuLoad = 0;
    for (var i = 0; i < cpuLoads.length; i++) {
        cumCpuLoad += cpuLoads[i];
    }
    var cpuAvgLoad = (cumCpuLoad/cpuLoads.length).toFixed(1);
	aData.push(cpuAvgLoad);
	var ctx = document.getElementById(container).getContext('2d');
	var chart = null;
	chart = new Chart(ctx, {
		type: 'line',
		data: {
			labels: aLabels,
			datasets: [
				{ label: 'CPU Use', borderColor: 'red', data: aData }
			]
		},
		options: {
			scales: {
				yAxes: [ 
					{ ticks: { callback: function(value, index, values) { return value + "%"; } } }
				]
			}
		}
	});
	setInterval(() => { ch_get_Rapid_CPU_Update(chart, pData); }, timeout);
}

function ch_get_Rapid_CPU(container, type) {
	let pData = { "doWhat": "snmpWalk", "extraDiskID": null };
	$.post(getResource("SNMP"), pData, function(result) {
		ch_chart_Rapid_CPU(container, result, type, pData);
  	});
}

function ch_get_Rapid_CPU_Update(chart, pData) {
	$.post(getResource("SNMP"), pData, function (result) {
		let snmp = JSON.parse(result);
		let tLabel = "0";
		var cpuLoads = [
	        snmp.cpu1Load,
	        snmp.cpu2Load,
	        snmp.cpu3Load,
	        snmp.cpu4Load,
	        snmp.cpu5Load,
	        snmp.cpu6Load,
	        snmp.cpu7Load,
	        snmp.cpu8Load
	    ];
	    var cumCpuLoad = 0;
	    for (var i = 0; i < cpuLoads.length; i++) {
	        cumCpuLoad += cpuLoads[i];
	    }
	    var cpuAvgLoad = (cumCpuLoad/cpuLoads.length).toFixed(1);
		chart.data.labels.push(tLabel);
		chart.data.datasets.forEach((dataset) => { dataset.data.push(cpuAvgLoad); });
		chart.update();
		if(chart.data.labels.length === 128) {
			chart.data.labels.shift();
			chart.data.datasets.forEach((dataset) => { dataset.data.shift(); });
			chart.update();
		}
		console.log("Chart update completed - " + tLabel + " & " + cpuAvgLoad);
	});
}
