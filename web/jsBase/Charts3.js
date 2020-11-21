/* 
by Anthony Stump
Created: 6 Oct 2020
Updated: 21 Nov 2020
 */

console.log("DBG: loaded charts3.js"); 

function initCharts3() {
	console.log("DBG: init called for charts3");
	//testChart();
	//testChartBasic();
	testChartBasicCjs();
	//testChartBasicDojo();
}

$(document).ready(function() { 
	console.log("DBG received action: " + doAction);
	let container = "ChartCanvas";
	let type = "full";
	switch(doAction) {
		case "FinENW_All_A.png": ch_get_FinENW_All_A(container, type); break;
		case "FinENW_All_R.png": ch_get_FinENW_All_R(container, type); break;
		case "FinENW_Year_F.png": ch_get_FinENW_Year_A(container, type); break;
		case "FinENW_Year_L.png": ch_get_FinENW_Year_A(container, type); break;
		case "FinENW_Year_T.png": ch_get_FinENW_Year_A(container, type); break;
		case "ObsJSONTempH.png": ch_get_ObsJSONTempH(container, type); break;
		case "WeightRange.png": ch_get_WeightRange(container, type); break;
		default: initCharts3(); break;
	}	
});
