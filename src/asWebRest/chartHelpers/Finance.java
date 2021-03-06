/*
by Anthony Stump
Created: 13 May 2018
Updated: 8 Jan 2021
 */

package asWebRest.chartHelpers;

import org.json.JSONArray;
import org.json.JSONObject;

public class Finance {

    private JSONObject billCh(JSONArray dataIn) {
        String bill_Name = "Bills";
        JSONObject bill_Glob = new JSONObject();
        JSONObject bill_Props = new JSONObject();
        JSONArray bill_Labels = new JSONArray();
        JSONArray bill_Data = new JSONArray();
        JSONArray bill_Data2 = new JSONArray();
        JSONArray bill_Data3 = new JSONArray();
        JSONArray bill_Data4 = new JSONArray();
        JSONArray bill_Data5 = new JSONArray();
        JSONArray bill_Data6 = new JSONArray();
        JSONArray bill_ele = new JSONArray();
        JSONArray bill_gas = new JSONArray();
        JSONArray bill_wat = new JSONArray();
        JSONArray bill_swr = new JSONArray();
        JSONArray bill_web = new JSONArray();
        JSONArray bill_pho = new JSONArray();
        bill_Props
                .put("dateFormat", "yyyy-MM")
                .put("chartName", bill_Name).put("chartFileName", "FinBills")
                .put("sName", "Total").put("sColor", "White")
                .put("s2Name", "Elect+Gas").put("s2Color", "Yellow")
                .put("s3Name", "Water+Sewer").put("s3Color", "Blue")
                .put("s4Name", "Trash").put("s4Color", "Orange")
                .put("s5Name", "Web+Phone").put("s5Color", "Red")
                .put("s6Name", "Gym/Other").put("s6Color", "Gray")
                .put("xLabel", "Month").put("yLabel", "USD");
        for (int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            bill_Labels.put(thisObject.getString("Month"));
            bill_Data.put(thisObject.getDouble("Total"));
            bill_Data2.put(thisObject.getDouble("ELE") + thisObject.getDouble("GAS"));
            bill_Data3.put(thisObject.getDouble("WAT") + thisObject.getDouble("SWR"));
            bill_Data4.put(thisObject.getDouble("TRA"));
            bill_Data5.put(thisObject.getDouble("WEB") + thisObject.getDouble("PHO"));
            bill_Data6.put(thisObject.getDouble("Gym") + thisObject.getDouble("Other"));
            bill_ele.put(thisObject.getDouble("ELE"));
            bill_gas.put(thisObject.getDouble("GAS"));
            bill_wat.put(thisObject.getDouble("WAT"));
            bill_swr.put(thisObject.getDouble("SWR"));
            bill_web.put(thisObject.getDouble("WEB"));
            bill_pho.put(thisObject.getDouble("PHO"));
        }
        bill_Glob
                .put("labels", bill_Labels)
                .put("data", bill_Data)
                .put("data2", bill_Data2)
                .put("data3", bill_Data3)
                .put("data4", bill_Data4)
                .put("data5", bill_Data5)
                .put("data6", bill_Data6)
                .put("ele", bill_ele)
                .put("gas", bill_gas)
                .put("wat", bill_wat)
                .put("swr", bill_swr)
                .put("web", bill_web)
                .put("pho", bill_pho)
                .put("props", bill_Props);
        return bill_Glob;
    }

    private JSONObject finEnw(JSONArray dataIn, String periodLength, String dataSelection) {
        String enw_Name = "Estimated Net Worth (" + periodLength + " - " + dataSelection + ")";
        JSONObject enw_Glob = new JSONObject();
        JSONObject enw_Props = new JSONObject();
        JSONArray enw_Labels = new JSONArray();
        JSONArray enw_Data = new JSONArray();
        JSONArray enw_Data2 = new JSONArray();
        JSONArray enw_Data3 = new JSONArray();
        JSONArray enw_Data4 = new JSONArray();
        JSONArray enw_Data5 = new JSONArray();
        JSONArray enw_Data6 = new JSONArray();
        JSONArray enw_Data7 = new JSONArray();
        JSONArray enw_Data8 = new JSONArray();
        JSONArray enw_Data9 = new JSONArray();
        JSONArray enw_Data10 = new JSONArray();
        JSONArray enw_Data11 = new JSONArray();
        JSONArray enw_Data12 = new JSONArray();
	JSONArray enw_Data13 = new JSONArray();
        if(dataSelection.equals("R")) {
        	enw_Props.put("dateFormat", "yyyy-MM-dd HH:mm:ss");
        } else {
        	enw_Props.put("dateFormat", "yyyy-MM-dd");
        }
        enw_Props
                .put("chartName", enw_Name).put("chartFileName", "FinENW_" + periodLength + "_" + dataSelection)
                .put("xLabel", "Date").put("yLabel", "(K) USD");
        switch(dataSelection) {
	        case "A": default:
		        enw_Props
		        	.put("sName", "Worth").put("sColor", "White")
			        .put("s2Name", "Liquid").put("s2Color", "Green")
			        .put("s3Name", "Fixed").put("s3Color", "Blue")
			        .put("s4Name", "Insurance").put("s4Color", "Gray")
			        .put("s5Name", "Credits").put("s5Color", "Yellow")
			        .put("s6Name", "Debt").put("s6Color", "Red")
			        .put("s7Name", "Liquidity").put("s7Color", "Pink")
			        .put("s8Name", "Ret_A").put("s8Color", "Brown")
			        .put("s9Name", "Ret_E").put("s9Color", "Brown")
			        .put("s10Name", "Stocks").put("s10Color", "Brown")
			        .put("s11Name", "CFCK01").put("s11Color", "Brown")
			        .put("s12Name", "CFSV59").put("s12Color", "Brown")
				.put("s13Name", "Crypto").put("s13Color", "Brown");
		        break;
	        case "T": case "R":
		        enw_Props.put("sName", "Worth").put("sColor", "White");
	        	break;
	        case "L":
		        enw_Props.put("sName", "Liquid").put("sColor", "Green");
	        	break;
	        case "F":
		        enw_Props.put("sName", "Fixed").put("sColor", "Blue");
	        	break;
	        case "D": 
		        enw_Props.put("sName", "Debt").put("sColor", "Red");
	        	break;
        }
        for (int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            enw_Labels.put(thisObject.getString("AsOf"));
            enw_Data.put(thisObject.getDouble("Worth"));
            enw_Data2.put(thisObject.getDouble("AsLiq"));
            enw_Data3.put(thisObject.getDouble("AsFix"));
            enw_Data4.put(thisObject.getDouble("Life"));
            enw_Data5.put(thisObject.getDouble("Credits"));
            enw_Data6.put(thisObject.getDouble("Debts"));
            enw_Data7.put(thisObject.getDouble("Liquidity"));
            enw_Data8.put(thisObject.getDouble("AsLiq_FidA") + thisObject.getDouble("AsLiq_FidARI") + thisObject.getDouble("AsLiq_EJRI23"));
            enw_Data9.put(thisObject.getDouble("AsLiq_EJTI15") + thisObject.getDouble("AsLiq_EJRI07") + thisObject.getDouble("AsLiq_FidE"));
            enw_Data10.put(thisObject.getDouble("AsLiq_ETra") + thisObject.getDouble("AsLiq_FidAB") + thisObject.getDouble("AsLiq_Crypto"));
            enw_Data11.put(thisObject.getDouble("AsLiq_FBCFCK01"));
            enw_Data12.put(thisObject.getDouble("AsLiq_FBCFSV59"));
		enw_Data13.put(thisObject.getDouble("AsLiq_Crypto"));
       }
        enw_Glob
                .put("labels", enw_Labels)
                .put("props", enw_Props);
        switch(dataSelection) {
	        case "A": case "R": default:
	    		enw_Glob
		            .put("data", enw_Data)
		            .put("data2", enw_Data2)
		            .put("data3", enw_Data3)
		            .put("data4", enw_Data4)
		            .put("data5", enw_Data5)
		            .put("data6", enw_Data6)
		            .put("data7", enw_Data7)
		            .put("data8", enw_Data8)
		            .put("data9", enw_Data9)
		            .put("data10", enw_Data10)
		            .put("data11", enw_Data11)
		            .put("data12", enw_Data12)
				.put("data13", enw_Data13);
	            break;
	        case "T": 
	    		enw_Glob.put("data", enw_Data);
	        	break;
	        case "L":
	    		enw_Glob.put("data", enw_Data2);
	        	break;
	        case "F":
	    		enw_Glob.put("data", enw_Data3);
	        	break;
	        case "D": 
	    		enw_Glob.put("data", enw_Data6);
	        	break;
        }
        return enw_Glob;
    }
    
    private JSONObject savingsOpt(JSONArray dataIn) {
        String svChart_Name = "Savings Balance";
        JSONObject svChart_Glob = new JSONObject();
        JSONObject svChart_Props = new JSONObject();
        JSONArray svChart_Labels = new JSONArray();
        JSONArray svChart_Data = new JSONArray();
        svChart_Props
                .put("dateFormat", "yyyy-MM-dd")
                .put("chartName", svChart_Name).put("chartFileName", "FinSavings")
                .put("sName", "Savings").put("sColor", "Red")
                .put("xLabel", "Date").put("yLabel", "USD");
        for (int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            svChart_Labels.put(thisObject.getString("Date"));
            svChart_Data.put(thisObject.getDouble("Value"));
        }
        svChart_Glob
                .put("labels", svChart_Labels)
                .put("data", svChart_Data)
                .put("props", svChart_Props);
        return svChart_Glob;
    }
    
    private JSONObject stockChart(JSONArray dataIn, String symbol) {
    	String ch_Name = "Stock Value: " + symbol;
    	JSONObject ch_Glob = new JSONObject();
    	JSONObject ch_Props = new JSONObject();
    	JSONArray ch_Labels = new JSONArray();
    	JSONArray ch_Data = new JSONArray();
    	ch_Props
		        .put("dateFormat", "yyyy-MM-dd HH:mm:ss")
		        .put("chartName", ch_Name).put("chartFileName", "FinStock_"+symbol)
		        .put("sName", "Value").put("sColor", "Red")
		        .put("xLabel", "Date").put("yLabel", "USD");
		for (int i = 0; i < dataIn.length(); i++) {
		    try { 
		    	JSONObject thisObject = dataIn.getJSONObject(i);
			    JSONObject tSubData = new JSONObject(thisObject.getString("jsonData"));
			    JSONObject tTicker = tSubData.getJSONObject(symbol);
			    String thisValue = tTicker.getString("valueEach");
			    ch_Labels.put(thisObject.getString("AsOf"));
			    ch_Data.put(thisValue);			
		    } catch (Exception e) { }
		}
		ch_Glob
		        .put("labels", ch_Labels)
		        .put("data", ch_Data)
		        .put("props", ch_Props);
		return ch_Glob;
    	
    }
    
    private JSONObject wealthGrowth(JSONArray dataIn) {
    	String ch_Name = "Wealth Growth";
    	JSONObject ch_Glob = new JSONObject();
    	JSONObject ch_Props = new JSONObject();
    	JSONArray ch_Labels = new JSONArray();
    	JSONArray ch_Data = new JSONArray();
    	JSONArray ch_Data2 = new JSONArray();
    	JSONArray ch_Data3 = new JSONArray();
    	JSONArray ch_Data4 = new JSONArray();
    	ch_Props
		        .put("dateFormat", "yyyy-MM-dd")
		        .put("chartName", ch_Name).put("chartFileName", "FinGrowth")
		        .put("sName", "Week").put("sColor", "Green")
		        .put("s2Name", "Month").put("s2Color", "Yellow")
		        .put("s3Name", "Quarter").put("s3Color", "Red")
		        .put("s4Name", "Year").put("s4Color", "White")
		        .put("xLabel", "Date").put("yLabel", "USD");
        for (int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            ch_Labels.put(thisObject.getString("asOf"));
            ch_Data.put(thisObject.getInt("mW"));
            ch_Data2.put(thisObject.getInt("mM"));
            ch_Data3.put(thisObject.getInt("mQ"));
            ch_Data4.put(thisObject.getInt("mY"));
        }
		ch_Glob
		        .put("labels", ch_Labels)
		        .put("data", ch_Data)
		        .put("data2", ch_Data2)
		        .put("data3", ch_Data3)
		        .put("data4", ch_Data4)
		        .put("props", ch_Props);
		return ch_Glob;
    	
    }
    
    public JSONObject getBillCh(JSONArray dataIn) { return billCh(dataIn); }
    public JSONObject getFinEnw(JSONArray dataIn, String periodLength, String dataSelection) { return finEnw(dataIn, periodLength, dataSelection); }
    public JSONObject getSavingsOpt(JSONArray dataIn) { return savingsOpt(dataIn); }
    public JSONObject getStockChart(JSONArray dataIn, String symbol) { return stockChart(dataIn, symbol); }
    public JSONObject getWealthGrowth(JSONArray dataIn) { return wealthGrowth(dataIn); }

}
