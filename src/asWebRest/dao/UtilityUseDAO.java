/*
by Anthony Stump
Created: 19 Feb 2018
Updated: 9 Jan 2021
*/

package asWebRest.dao;

import java.sql.ResultSet;
import java.util.List;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;
import java.sql.Connection;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.JSONArray;
import org.json.JSONObject;

public class UtilityUseDAO {
    
    WebCommon wc = new WebCommon();
    CommonBeans wcb = new CommonBeans();
    
    private JSONArray chCellUse(Connection dbc) {
        final String query_ch_CellUse = "SELECT A.Bill AS Bill," +
                " (A.Texts+E.Texts) AS Texts," +
                " (A.MBData+E.MBData) AS MBData," +
                " (A.Minutes+E.Minutes_L500+E.Minutes_Free) AS Minutes," +
                " (A.MMS+E.MMS) AS MMS" +
                " FROM Core.UseSprintA A" +
                " LEFT OUTER JOIN Core.UseSprintE E ON A.Bill = E.Bill" +
                " WHERE A.Bill >= '2012-08';";      
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_ch_CellUse, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Bill", resultSet.getString("Bill"))
                    .put("Texts", resultSet.getInt("Texts"))
                    .put("MBData", resultSet.getInt("MBData"))
                    .put("Minutes", resultSet.getInt("Minutes"))
                    .put("MMS", resultSet.getInt("MMS"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getChCellUse(Connection dbc) { return chCellUse(dbc); }
    
    public JSONArray getChUseElecD(Connection dbc) {
        final String query_ch_UseElecD = "SELECT Date, kWh FROM Core.UseElecD WHERE Date > CURRENT_DATE - INTERVAL '365' DAY ORDER BY Date;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_ch_UseElecD, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Date", resultSet.getString("Date"))
                    .put("kWh", Double.parseDouble(resultSet.getString("kWh")));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getChUseGas(Connection dbc) {
        final String query_ch_UseGas = "SELECT Month, TotalMCF, BilledAmount, BilledDays" +
        		" FROM Core.UseGas ORDER BY Month;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_ch_UseGas, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Month", resultSet.getString("Month"))
                    .put("TotalMCF", resultSet.getDouble("TotalMCF"))
                    .put("BilledAmount", resultSet.getDouble("BilledAmount"))
                    .put("BilledDays", resultSet.getInt("BilledDays"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getChWebData(Connection dbc) {
        final String query_ch_WebData = "SELECT (MBUpload + MBDown) AS MBData, Month FROM Core.UseInternet ORDER BY Month ASC;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_ch_WebData, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Month", resultSet.getString("Month"))
                    .put("MBData", resultSet.getDouble("MBData"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONObject getCombinedUtilityUseByMonth(Connection dbc, String month) {
        JSONObject tEncapsulator = new JSONObject();
        DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM");
        DateTime baseTime = dtf.parseDateTime(month);
        for(int i = 0; i <= 24; i++) {
            DateTime thisMonth = baseTime.minusMonths(i);
            String tMonth = dtf.print(thisMonth);
            final String query_FBook_UU_Comb = "SELECT" +
                " (SELECT TotalMCF FROM Core.UseGas WHERE Month LIKE '%"+tMonth+"%') AS TotalMCF," +
                " (SELECT FORMAT((kWh_AVG),1) FROM (" +
                "   SELECT kWh_AVG FROM Core.UseElecM WHERE Month LIKE '%"+tMonth+"%' UNION ALL" +
                "   SELECT AVG(kWh) FROM Core.UseElecD WHERE Date LIKE '%"+tMonth+"%') as tmp" +
                " ) AS kWh_Avg," +
                " (SELECT SUM(MBUpload+MBDown) FROM Core.UseInternet WHERE Month LIKE '%"+tMonth+"%') as WData," +
                " (SELECT (A.Minutes+E.Minutes_L500+E.Minutes_Free) FROM Core.UseSprintA A LEFT OUTER JOIN Core.UseSprintE E on A.Bill = E.Bill WHERE A.Bill LIKE '%"+tMonth+"%') AS Minutes," +
                " (SELECT (A.Texts+E.Texts) FROM Core.UseSprintA A LEFT OUTER JOIN Core.UseSprintE E on A.Bill = E.Bill WHERE A.Bill LIKE '%"+tMonth+"%') AS Texts," +
                " (SELECT (A.MMS+E.MMS) FROM Core.UseSprintA A LEFT OUTER JOIN Core.UseSprintE E on A.Bill = E.Bill WHERE A.Bill LIKE '%"+tMonth+"%') AS MMS," +
                " (SELECT (A.MBData+E.MBData) FROM Core.UseSprintA A LEFT OUTER JOIN Core.UseSprintE E on A.Bill = E.Bill WHERE A.Bill LIKE '%"+tMonth+"%') AS CData," +
                " (SELECT AVG(Average) FROM WxObs.CF6MCI WHERE Date LIKE '%"+tMonth+"%') AS wxAvgTF" +
                " FROM Core.UseGas" +
                " LIMIT 1;";
            try {
                ResultSet resultSet = wc.q2rs1c(dbc, query_FBook_UU_Comb, null);
                while (resultSet.next()) {
                    JSONObject tObject = new JSONObject();
                    tObject
                        .put("TotalMCF", resultSet.getDouble("TotalMCF"))
                        .put("kWh_Avg", resultSet.getDouble("kWh_Avg"))
                        .put("WData", resultSet.getLong("WData"))
                        .put("Minutes", resultSet.getInt("Minutes"))
                        .put("MMS", resultSet.getInt("MMS"))
                        .put("Texts", resultSet.getInt("Texts"))
                        .put("CData", resultSet.getInt("CData"))
                        .put("wxAvgTF", resultSet.getDouble("wxAvgTF"));
                    tEncapsulator.put(tMonth, tObject);
                }
                resultSet.close();
            } catch (Exception e) { e.printStackTrace(); }
        }
        return tEncapsulator;
    }
    
    public JSONArray getUsePhone(Connection dbc, String month) {
        final String query_FBook_UU_P = "SELECT (A.Minutes+E.Minutes_L500+E.Minutes_Free) AS Minutes," +
                " (A.Texts+E.Texts) AS Texts, (A.MMS+E.MMS) as MMS, (A.MBData+E.MBData) AS MBData" +
                " FROM Core.UseSprintA A LEFT OUTER JOIN Core.UseSprintE E ON A.Bill = E.Bill" +
                " WHERE A.Bill LIKE "+month+";";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_FBook_UU_P, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Minutes", resultSet.getInt("Minutes"))
                    .put("MMS", resultSet.getInt("MMS"))
                    .put("Texts", resultSet.getInt("Texts"))
                    .put("MBData", resultSet.getInt("MBData"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }

    public String setElectricityUse(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        String query_kWhUse = "REPLACE INTO Core.UseElecD" +
        		" (Date, kWh, kWh_Demand, kWh_PeakDemand, PeakDemandTime, Cost)" +
        		" VALUES (?,?,?,?,?,?);";
        try { returnData = wc.q2do1c(dbc, query_kWhUse, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }

    public String setGasUse(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        String query_Gas = "REPLACE INTO Core.UseGas" +
        		" (Month, TotalMCF, BilledAmount, BilledDays)" +
        		" VALUES (?,?,?,?) ON DUPLICATE KEY UPDATE" +
        		" TotalMCF=?, BilledAmount=?, BilledDays=?;";
        try { returnData = wc.q2do1c(dbc, query_Gas, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
  
  
}
