/*
by Anthony Stump
Created: 13 May 2018
Updated: 25 Jul 2018
 */

package asWebRest.chartHelpers;

import org.json.JSONArray;
import org.json.JSONObject;

public class Finance {

    private static JSONObject billCh(JSONArray dataIn) {
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
        }
        bill_Glob
                .put("labels", bill_Labels)
                .put("data", bill_Data)
                .put("data2", bill_Data2)
                .put("data3", bill_Data3)
                .put("data4", bill_Data4)
                .put("data5", bill_Data5)
                .put("data6", bill_Data6)
                .put("props", bill_Props);
        return bill_Glob;
    }

    private static JSONObject finEnw(JSONArray dataIn) {
        String enw_Name = "Estimated Net Worth";
        JSONObject enw_Glob = new JSONObject();
        JSONObject enw_Props = new JSONObject();
        JSONArray enw_Labels = new JSONArray();
        JSONArray enw_Data = new JSONArray();
        JSONArray enw_Data2 = new JSONArray();
        JSONArray enw_Data3 = new JSONArray();
        JSONArray enw_Data4 = new JSONArray();
        JSONArray enw_Data5 = new JSONArray();
        JSONArray enw_Data6 = new JSONArray();
        enw_Props
                .put("dateFormat", "yyyy-MM-dd")
                .put("chartName", enw_Name).put("chartFileName", "FinENW")
                .put("sName", "Worth").put("sColor", "White")
                .put("s2Name", "Liquid").put("s2Color", "Green")
                .put("s3Name", "Fixed").put("s3Color", "Blue")
                .put("s4Name", "Insurance").put("s4Color", "Gray")
                .put("s5Name", "Credits").put("s5Color", "Yellow")
                .put("s6Name", "Debt").put("s6Color", "Red")
                .put("xLabel", "Date").put("yLabel", "K USD");
        for (int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            enw_Labels.put(thisObject.getString("AsOf"));
            enw_Data.put(thisObject.getDouble("Worth"));
            enw_Data2.put(thisObject.getDouble("AsLiq"));
            enw_Data3.put(thisObject.getDouble("AsFix"));
            enw_Data4.put(thisObject.getDouble("Life"));
            enw_Data5.put(thisObject.getDouble("Credits"));
            enw_Data6.put(thisObject.getDouble("Debts"));
        }
        enw_Glob
                .put("labels", enw_Labels)
                .put("data", enw_Data)
                .put("data2", enw_Data2)
                .put("data3", enw_Data3)
                .put("data4", enw_Data4)
                .put("data5", enw_Data5)
                .put("data6", enw_Data6)
                .put("props", enw_Props);
        return enw_Glob;
    }
    
    private static JSONObject gasUse(JSONArray dataIn) {
        String ch_Name = "Gas Use by Month";
        JSONObject ch_Glob = new JSONObject();
        JSONObject ch_Props = new JSONObject();
        JSONArray ch_Labels = new JSONArray();
        JSONArray ch_Data = new JSONArray();
        ch_Props
                .put("dateFormat", "yyyy-MM")
                .put("chartName", ch_Name).put("chartFileName", "gasByMonth")
                .put("sName", "MCF").put("sColor", "White")
                .put("xLabel", "Month").put("yLabel", "MCF");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            ch_Labels.put(thisObject.getString("Month"));
            ch_Data.put(thisObject.getDouble("TotalMCF"));
        }
        ch_Glob
                .put("labels", ch_Labels)
                .put("data", ch_Data)
                .put("props", ch_Props);
        return ch_Glob;
    }
    
    private static JSONObject kWhUse(JSONArray dataIn) {
        String ch_Name = "kWh Use by Day";
        JSONObject kWh_Glob = new JSONObject();
        JSONObject kWh_Props = new JSONObject();
        JSONArray kWh_Labels = new JSONArray();
        JSONArray kWh_Data = new JSONArray();
        kWh_Props
                .put("dateFormat", "yyyy-MM-dd")
                .put("chartName", ch_Name).put("chartFileName", "kWhByDay")
                .put("sName", "kWh").put("sColor", "White")
                .put("xLabel", "Date").put("yLabel", "kWh");
        for (int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            kWh_Labels.put(thisObject.getString("Date"));
            kWh_Data.put(thisObject.getDouble("kWh"));
        }
        kWh_Glob
                .put("labels", kWh_Labels)
                .put("data", kWh_Data)
                .put("props", kWh_Props);
        return kWh_Glob;
    }

    private static JSONObject savingsOpt(JSONArray dataIn) {
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

    private static JSONObject webUse(JSONArray dataIn) {
        String ch_Name = "Web Use by Month";
        JSONObject ch_Glob = new JSONObject();
        JSONObject ch_Props = new JSONObject();
        JSONArray ch_Labels = new JSONArray();
        JSONArray ch_Data = new JSONArray();
        ch_Props
                .put("dateFormat", "yyyy-MM")
                .put("chartName", ch_Name).put("chartFileName", "webByMonth")
                .put("sName", "MB").put("sColor", "White")
                .put("xLabel", "Month").put("yLabel", "MB");
        for(int i = 0; i < dataIn.length(); i++) {
            JSONObject thisObject = dataIn.getJSONObject(i);
            ch_Labels.put(thisObject.getString("Month"));
            ch_Data.put(thisObject.getDouble("MBData"));
        }
        ch_Glob
                .put("labels", ch_Labels)
                .put("data", ch_Data)
                .put("props", ch_Props);
        return ch_Glob;
    }
    
    public static JSONObject getBillCh(JSONArray dataIn) { return billCh(dataIn); }
    public static JSONObject getFinEnw(JSONArray dataIn) { return finEnw(dataIn); }
    public static JSONObject getGasUse(JSONArray dataIn) { return gasUse(dataIn); }
    public static JSONObject getKwhUse(JSONArray dataIn) { return kWhUse(dataIn); }
    public static JSONObject getSavingsOpt(JSONArray dataIn) { return savingsOpt(dataIn); }
    public static JSONObject getWebUse(JSONArray dataIn) { return webUse(dataIn); }

}
