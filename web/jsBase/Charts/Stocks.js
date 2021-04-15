/* 
by Anthony Stump
Created: 9 Jan 2021
Updated: 31 Mar 2021
 */

function ch_chart_BrokerageDist(container, result, type) {
	let doLegend = true;
	let doX = true;
	let lbRadius = 0;
	if(type === "thumb") { 
		doLegend = false;
		doX = false; 
	}
	let resultJ = JSON.parse(result);
	let subData = resultJ.stocksA;
	let datasetDataValues = [];
	let datasetDataColors = [];
	let datasetLabels = [];
	subData.forEach(function (sd) {
		if(sd.Managed === 0 && sd.FIIBAN !== 0) {
			datasetDataValues.push(((sd.LastValue * sd.Multiplier) * (sd.FIIBAN-sd.Unvested)).toFixed(2));
			datasetDataColors.push(randomColor());
			datasetLabels.push(sd.Symbol);
		}
	});
	var ctx = document.getElementById(container).getContext('2d');
	let chart = null
	chart = new Chart(ctx, {
		type: 'pie',
		data: {
			datasets: [{
				data: datasetDataValues,
				color: datasetDataColors,
				backgroundColor: datasetDataColors,
				label: 'FIIBAN'
			}],
			labels: datasetLabels
		},
		options: {
			legend: {
				display: doLegend
			}
		}
	});
}

function ch_get_BrokerageDist(container, type) {
	let pData = { "doWhat": "getStocksAll" };
	$.post(getResource("Stock"), pData, function(result) {
		ch_chart_BrokerageDist(container, result, type);
  	});
}

function ch_chart_CryptoDist(container, result, type) {
	let doLegend = true;
	let doX = true;
	let lbRadius = 0;
	if(type === "thumb") { 
		doLegend = false;
		doX = false; 
	}
	let resultJ = JSON.parse(result);
	let subData = resultJ.stocksA;
	let datasetDataValues = [];
	let datasetDataColors = [];
	let datasetLabels = [];
	subData.forEach(function (sd) {
		if(sd.Managed === 0 && sd.Holder === "Crypto") {
			datasetDataValues.push(((sd.LastValue * sd.Multiplier) * (sd.Count)).toFixed(2));
			datasetDataColors.push(randomColor());
			datasetLabels.push(sd.Symbol);
		}
	});
	var ctx = document.getElementById(container).getContext('2d');
	let chart = null
	chart = new Chart(ctx, {
		type: 'pie',
		data: {
			datasets: [{
				data: datasetDataValues,
				color: datasetDataColors,
				backgroundColor: datasetDataColors,
				label: 'Crypto'
			}],
			labels: datasetLabels
		},
		options: {
			legend: {
				display: doLegend
			}
		}
	});
}

function ch_get_CryptoDist(container, type) {
	let pData = { "doWhat": "getStocksAll" };
	$.post(getResource("Stock"), pData, function(result) {
		ch_chart_CryptoDist(container, result, type);
  	});
}

function ch_chart_RetirementDist(container, result, type) {
	let doLegend = true;
	let doX = true;
	let lbRadius = 0;
	if(type === "thumb") { 
		doLegend = false;
		doX = false; 
	}
	let resultJ = JSON.parse(result);
	let subData = resultJ.stocksA;
	let datasetDataValues = [];
	let datasetDataColors = [];
	let datasetLabels = [];
	subData.forEach(function (sd) {
		if((sd.FI4KAN !== 0 || sd.FIRIAN !== 0) && sd.Holder !== 'EJones' && sd.Holder !== 'FidelityE') {
			datasetDataValues.push(((sd.LastValue * sd.Multiplier) * (sd.FI4KAN + sd.FIRIAN)).toFixed(2));
			datasetDataColors.push(randomColor());
			datasetLabels.push(sd.Symbol);
		}
	});
	var ctx = document.getElementById(container).getContext('2d');
	let chart = null
	chart = new Chart(ctx, {
		type: 'pie',
		data: {
			datasets: [{
				data: datasetDataValues,
				color: datasetDataColors,
				backgroundColor: datasetDataColors,
				label: 'Retirement'
			}],
			labels: datasetLabels
		},
		options: {
			legend: {
				display: doLegend
			}
		}
	});
}

function ch_get_RetirementDist(container, type) {
	let pData = { "doWhat": "getStocksAll" };
	$.post(getResource("Stock"), pData, function(result) {
		ch_chart_RetirementDist(container, result, type);
  	});
}

function ch_chart_RetirementDistE(container, result, type) {
	let doLegend = true;
	let doX = true;
	let lbRadius = 0;
	if(type === "thumb") { 
		doLegend = false;
		doX = false; 
	}
	let resultJ = JSON.parse(result);
	let subData = resultJ.stocksA;
	let datasetDataValues = [];
	let datasetDataColors = [];
	let datasetLabels = [];
	subData.forEach(function (sd) {
		if(sd.Managed === 1 && sd.Count !== 0 && (sd.Holder == 'EJones' || sd.Holder == 'FidelityE')) {
			datasetDataValues.push(((sd.LastValue * sd.Multiplier) * sd.Count).toFixed(2));
			datasetDataColors.push(randomColor());
			datasetLabels.push(sd.Symbol);
		}
	});
	var ctx = document.getElementById(container).getContext('2d');
	let chart = null
	chart = new Chart(ctx, {
		type: 'pie',
		data: {
			datasets: [{
				data: datasetDataValues,
				color: datasetDataColors,
				backgroundColor: datasetDataColors,
				label: 'RetirementE'
			}],
			labels: datasetLabels
		},
		options: {
			legend: {
				display: doLegend
			}
		}
	});
}

function ch_get_RetirementDistE(container, type) {
	let pData = { "doWhat": "getStocksAll" };
	$.post(getResource("Stock"), pData, function(result) {
		ch_chart_RetirementDistE(container, result, type);
  	});
}
