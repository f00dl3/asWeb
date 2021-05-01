/* 
by Anthony Stump
Created: 6 Oct 2020
Updated: 16 Apr 2021
 */

let pOpts = {};

let inOpts = inList.split(",");

console.log("DBG: loaded charts3.js"); 

function initCharts3() {
	console.log("DBG: init called for charts3");
	//eruda.init();
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
		case "Bills.png": ch_get_Bills(container, type); break;
		case "BrokerageDist.png": ch_get_BrokerageDist(container, type); break;
		case "CalorieRange.png": ch_get_CalorieRange(container, type, { xdt1: inOpts[0], xdt2: inOpts[1] } ); break;
		case "CellData.png": ch_get_CellData(container, type); break;
		case "CellMin.png": ch_get_CellMin(container, type); break;
		case "CellMMS.png": ch_get_CellMMS(container, type); break;
		case "CellText.png": ch_get_CellText(container, type); break;
		case "cf6Temps.png": ch_get_CF6Temps(container, type, { dateStart: inOpts[0], dateEnd: inOpts[1] }); break;
		case "cf6Depart.png": ch_get_CF6Depart(container, type, { dateStart: inOpts[0], dateEnd: inOpts[1] }); break;
		case "CryptoDist.png": ch_get_CryptoDist(container, type); break;
		case "ffxivGilWorthByDay.png": ch_get_ffxivGilWorthByDay(container, type); break;
		case "ffxivQuestsByDay.png": ch_get_ffxivQuestsByDay(container, type); break;
		case "FinENW_All_A.png": ch_get_FinENW_All_A(container, type); break;
		case "FinENW_All_R.png": ch_get_FinENW_All_R(container, type); break;
		case "FinENW_Year_F.png": ch_get_FinENW_Year_A(container, type); break;
		case "FinENW_Year_L.png": ch_get_FinENW_Year_A(container, type); break;
		case "FinENW_Year_T.png": ch_get_FinENW_Year_A(container, type); break;
		case "LiquidDist.png": ch_get_LiquidDist(container, type); break;
		case "msByDate.png": ch_get_msByDate(container, type); break;
		case "mSysCPU.png": ch_get_mSysCPU(container, type, { step: inOpts[0], date: inOpts[1] }); break;
		case "mSysFans.png": ch_get_mSysFans(container, type, { step: inOpts[0], date: inOpts[1] }); break;
		case "mSysLoad.png": ch_get_mSysLoad(container, type, { step: inOpts[0], date: inOpts[1] }); break;
		case "mSysMemory.png": ch_get_mSysMemory(container, type, { step: inOpts[0], date: inOpts[1] }); break;
		case "mSysMySQLSize.png": ch_get_mSysMemory(container, type, { step: inOpts[0], date: inOpts[1] }); break;
		case "mSysNet.png": ch_get_mSysNet(container, type, { step: inOpts[0], date: inOpts[1] }); break;
		case "mSysNumUsers.png": ch_get_mSysNumUsers(container, type, { step: inOpts[0], date: inOpts[1] }); break;
		case "mSysStorage.png": ch_get_mSysStorage(container, type, { step: inOpts[0], date: inOpts[1] }); break;
		case "mSysTemp.png": ch_get_mSysTemp(container, type, { step: inOpts[0], date: inOpts[1] }); break;
		case "Rapid_CPU.png": ch_get_Rapid_CPU(container, type); break;
		case "RetirementDist.png": ch_get_RetirementDist(container, type); break;
		case "RetirementDistE.png": ch_get_RetirementDistE(container, type); break;
		case "SleepRange.png": ch_get_SleepRange(container, type, { xdt1: inOpts[0], xdt2: inOpts[1] }); break;
		case "ObsJSONHumidity.png": ch_get_ObsJSONHumidity(container, type, { dateStart: inOpts[0], dateEnd: inOpts[1], station: inOpts[2] }); break;
		case "ObsJSONHumidityH.png": ch_get_ObsJSONHumidityH(container, type); break;
		case "ObsJSONPrecipRateH.png": ch_get_ObsJSONPrecipRateH(container, type); break;
		case "ObsJSONPressure.png": ch_get_ObsJSONPressure(container, type, { dateStart: inOpts[0], dateEnd: inOpts[1], station: inOpts[2] }); break;
		case "ObsJSONPressureH.png": ch_get_ObsJSONPressureH(container, type); break;
		case "ObsJSONTemp.png": ch_get_ObsJSONTemp(container, type, { dateStart: inOpts[0], dateEnd: inOpts[1], station: inOpts[2] }); break;
		case "ObsJSONTempH.png": ch_get_ObsJSONTempH(container, type); break;
		case "ObsJSONWind.png": ch_get_ObsJSONWind(container, type, { dateStart: inOpts[0], dateEnd: inOpts[1], station: inOpts[2] }); break;
		case "ObsJSONWindH.png": ch_get_ObsJSONWindH(container, type); break;
		case "UseElecD.png": ch_get_UseElecD(container, type); break;
		case "UseGas.png": ch_get_UseGas(container, type); break;
		case "WebData.png": ch_get_WebData(container, type); break;
		case "WeightRange.png": ch_get_WeightRange(container, type, { xdt1 : inOpts[0], xdt2: inOpts[1] }); break;
		default: initCharts3(); break;
	}	
});
