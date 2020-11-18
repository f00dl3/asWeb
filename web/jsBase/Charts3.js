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
		default: initCharts3(); break;
	}	
});
