/*
by Anthony Stump
Created: 19 Feb 2018
Updated: 22 Feb 2018
*/

package asWebRest.dao;

import java.sql.ResultSet;
import asWebRest.shared.WebCommon;
import org.json.JSONArray;
import org.json.JSONObject;

public class UtilityUseDAO {
    
    WebCommon wc = new WebCommon(); 
    
    public JSONArray getChUseElecD() {
        final String query_ch_UseElecD = "SELECT Date, kWh FROM Core.UseElecD WHERE Date > CURRENT_DATE - '120' DAY ORDER BY Date;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_ch_UseElecD, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Date", resultSet.getString("Date"))
                    .put("kWh", resultSet.getDouble("kWh"));
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getChUseGas() {
        final String query_ch_UseGas = "SELECT Month, TotalMCF FROM Core.UseGas ORDER BY Month;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_ch_UseGas, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Month", resultSet.getString("Month"))
                    .put("TotalMCF", resultSet.getDouble("TotalMCF"));
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getChWebData() {
        final String query_ch_WebData = "SELECT (MBUpload + MBDown) AS MBData, Month FROM Core.UseInternet ORDER BY Month ASC;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_ch_WebData, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Month", resultSet.getString("Month"))
                    .put("MBData", resultSet.getDouble("MBData"));
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getUseGas(String month) {
        final String query_FBook_UU_G = "SELECT TotalMCF FROM Core.UseGas WHERE Month LIKE "+month+";";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_FBook_UU_G, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("TotalMCF", resultSet.getDouble("TotalMCF"));
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getUseElectricity(String month) {
        final String query_FBook_UU_E = "SELECT FORMAT((kWh_AVG),1) AS kWh_AVG FROM" +
        " (SELECT kWh_AVG FROM UseElecM WHERE Month LIKE "+month+" UNION ALL" +
        " SELECT AVG(kWh) FROM UseElecD WHERE Date LIKE "+month+") AS tmp;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_FBook_UU_E, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("kWh_AVG", resultSet.getDouble("kWh_AVG"));
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
       
    public JSONArray getUseInternet(String month) {
        final String query_FBook_UU_W = "SELECT SUM(MBUpload+MBDown) AS MBData FROM Core.UseInternet WHERE Month LIKE "+month+";";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_FBook_UU_W, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("MBData", resultSet.getInt("MBData"));
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getUsePhone(String month) {
        final String query_FBook_UU_P = "SELECT (A.Minutes+E.Minutes_L500+E.Minutes_Free) AS Minutes," +
                " (A.Texts+E.Texts) AS Texts, (A.MMS+E.MMS) as MMS, (A.MBData+E.MBData) AS MBData" +
                " FROM Core.UseSprintA A LEFT OUTER JOIN Core.UseSprintE E ON A.Bill = E.Bill" +
                " WHERE A.Bill LIKE "+month+";";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_FBook_UU_P, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Minutes", resultSet.getInt("Minutes"))
                    .put("MMS", resultSet.getInt("MMS"))
                    .put("Texts", resultSet.getInt("Texts"))
                    .put("MBData", resultSet.getInt("MBData"));
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }

  
}
