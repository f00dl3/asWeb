/* 
by Anthony Stump
Created: 6 Oct 2020
Updated: 18 Nov 2020
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
	switch(doAction) {
		case "FinENW_All_R.png": get_FinENW_All_R(); break;
		case "FinENW_Year_F.png": get_FinENW_Year_F(); break;
		case "FinENW_Year_L.png": get_FinENW_Year_L(); break;
		case "FinENW_Year_T.png": get_FinENW_Year_T(); break;
		case "ObsJSONTempH.png": get_ObsJSONTempH(); break;
		default: initCharts3(); break;
	}	
});
