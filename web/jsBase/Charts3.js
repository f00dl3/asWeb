/* 
by Anthony Stump
Created: 6 Oct 2020
Updated: 24 Nov 2020
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
		case "CalorieRange.png": ch_get_CalorieRange(container, type); break;
		case "ffxivGilWorthByDay.png": ch_get_ffxivGilWorthByDay(container, type); break;
		case "ffxivQuestsByDay.png": ch_get_ffxivQuestsByDay(container, type); break;
		case "FinENW_All_A.png": ch_get_FinENW_All_A(container, type); break;
		case "FinENW_All_R.png": ch_get_FinENW_All_R(container, type); break;
		case "FinENW_Year_F.png": ch_get_FinENW_Year_A(container, type); break;
		case "FinENW_Year_L.png": ch_get_FinENW_Year_A(container, type); break;
		case "FinENW_Year_T.png": ch_get_FinENW_Year_A(container, type); break;
		case "msByDate.png": ch_get_msByDate(container, type); break;
		case "Rapid_CPU.png": ch_get_Rapid_CPU(container, type); break;
		case "SleepRange.png": ch_get_SleepRange(container, type); break;
		case "ObsJSONHumidityH.png": ch_get_ObsJSONHumidityH(container, type); break;
		case "ObsJSONPrecipRateH.png": ch_get_ObsJSONPrecipRateH(container, type); break;
		case "ObsJSONPressureH.png": ch_get_ObsJSONPressureH(container, type); break;
		case "ObsJSONTempH.png": ch_get_ObsJSONTempH(container, type); break;
		case "ObsJSONWindH.png": ch_get_ObsJSONWindH(container, type); break;
		case "WeightRange.png": ch_get_WeightRange(container, type); break;
		default: initCharts3(); break;
	}	
});
