/*
by Anthony Stump
Created: 19 Feb 2018
Split from FinanceDAO 1 Mar 2020
Updated: 25 Oct 2020
*/

package asWebRest.dao;

import asWebRest.shared.CommonBeans;
import java.sql.ResultSet;
import asWebRest.shared.WebCommon;
import java.sql.Connection;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class AutomotiveDAO {
    
    CommonBeans wcb = new CommonBeans();
    WebCommon wc = new WebCommon();
    
    private JSONArray autoBillSum(Connection dbc) {
        final String query_AutoBillSum = "SELECT SUM(Bill) AS BillSum from Core.AutoMaint_MAZ6;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_AutoBillSum, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject
                    .put("BillSum", resultSet.getDouble("BillSum"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }   

    private JSONArray autoBillSumHondaCivic(Connection dbc) {
        final String query_AutoBillSum = "SELECT SUM(Bill) AS BillSum from Core.AutoMaint_HONCIV;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_AutoBillSum, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject
                    .put("BillSum", resultSet.getDouble("BillSum"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }   

    private JSONArray autoBillSumNewCar20(Connection dbc) {
        final String query_AutoBillSum = "SELECT SUM(Bill) AS BillSum from Core.AutoMaint_KiaSorrento17;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_AutoBillSum, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject
                    .put("BillSum", resultSet.getDouble("BillSum"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }   
    
    private JSONArray autoMaint(Connection dbc) {
        final String query_AutoMaint = "SELECT Invoice, Miles, Date, Location, Services, Bill, OilCh, TireRotate" +
                " FROM Core.AutoMaint_MAZ6 ORDER BY Date DESC LIMIT 10;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_AutoMaint, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Invoice", resultSet.getInt("Invoice"))
                    .put("Miles", resultSet.getInt("Miles"))
                    .put("Date", resultSet.getString("Date"))
                    .put("Location", resultSet.getString("Location"))
                    .put("Services", resultSet.getString("Services"))
                    .put("Bill", resultSet.getDouble("Bill"))
                    .put("OilCh", resultSet.getInt("OilCh"))
                    .put("TireRotate", resultSet.getInt("TireRotate"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }   
      
    private JSONArray autoMaintHondaCivic(Connection dbc) {
        final String query_AutoMaint = "SELECT Invoice, Miles, Date, Location, Services, Bill, OilCh, TireRotate" +
                " FROM Core.AutoMaint_HONCIV ORDER BY Date DESC LIMIT 10;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_AutoMaint, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Invoice", resultSet.getInt("Invoice"))
                    .put("Miles", resultSet.getInt("Miles"))
                    .put("Date", resultSet.getString("Date"))
                    .put("Location", resultSet.getString("Location"))
                    .put("Services", resultSet.getString("Services"))
                    .put("Bill", resultSet.getDouble("Bill"))
                    .put("OilCh", resultSet.getInt("OilCh"))
                    .put("TireRotate", resultSet.getInt("TireRotate"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }   
    
  private JSONArray autoMaintNewCar20(Connection dbc) {
      final String query_AutoMaint = "SELECT Invoice, Miles, Date, Location, Services, Bill, OilCh, TireRotate" +
              " FROM Core.AutoMaint_KiaSorrento17 ORDER BY Date DESC LIMIT 10;";
      JSONArray tContainer = new JSONArray();
      try {
          ResultSet resultSet = wc.q2rs1c(dbc, query_AutoMaint, null);
          while (resultSet.next()) { 
              JSONObject tObject = new JSONObject();
              tObject
                  .put("Invoice", resultSet.getInt("Invoice"))
                  .put("Miles", resultSet.getInt("Miles"))
                  .put("Date", resultSet.getString("Date"))
                  .put("Location", resultSet.getString("Location"))
                  .put("Services", resultSet.getString("Services"))
                  .put("Bill", resultSet.getDouble("Bill"))
                  .put("OilCh", resultSet.getInt("OilCh"))
                  .put("TireRotate", resultSet.getInt("TireRotate"));
              tContainer.put(tObject);
          }
          resultSet.close();
      } catch (Exception e) { e.printStackTrace(); }
      return tContainer;
  }   
    
    private JSONArray autoMpg(Connection dbc) {
        final String query_AutoMPG = "SELECT Date, TotMiles, CostPG, Gallons FROM Auto_MPG ORDER BY Date DESC LIMIT 10;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_AutoMPG, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Date", resultSet.getString("Date"))
                    .put("TotMiles", resultSet.getInt("TotMiles"))
                    .put("Gallons", resultSet.getDouble("Gallons"))
                    .put("CostPG", resultSet.getDouble("CostPG"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }    

    private JSONArray autoMpgHondaCivic(Connection dbc) {
        final String query_AutoMPG = "SELECT Date, TotMiles, CostPG, Gallons FROM Auto_MPG_HONCIV ORDER BY Date DESC LIMIT 10;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_AutoMPG, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Date", resultSet.getString("Date"))
                    .put("TotMiles", resultSet.getInt("TotMiles"))
                    .put("Gallons", resultSet.getDouble("Gallons"))
                    .put("CostPG", resultSet.getDouble("CostPG"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }    

    private JSONArray autoMpgNewCar20(Connection dbc) {
        final String query_AutoMPG = "SELECT Date, TotMiles, CostPG, Gallons FROM Auto_MPG_KiaSorrento17 ORDER BY Date DESC LIMIT 10;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_AutoMPG, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Date", resultSet.getString("Date"))
                    .put("TotMiles", resultSet.getInt("TotMiles"))
                    .put("Gallons", resultSet.getDouble("Gallons"))
                    .put("CostPG", resultSet.getDouble("CostPG"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }    
    
    private String autoMpgAdd(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        String query_AddAutoMpg = "INSERT IGNORE INTO Core.Auto_MPG VALUES (?,?,?,?,0,0);";
        try { returnData = wc.q2do1c(dbc, query_AddAutoMpg, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }

    private String autoMpgAddHondaCivic(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        String query_AddAutoMpg = "INSERT IGNORE INTO Core.Auto_MPG_HONCIV VALUES (?,?,?,?,0,0);";
        try { returnData = wc.q2do1c(dbc, query_AddAutoMpg, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }

    private String autoMpgAddNewCar20(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        String query_AddAutoMpg = "INSERT IGNORE INTO Core.Auto_MPG_KiaSorrento17 VALUES (?,?,?,?,0,0);";
        try { returnData = wc.q2do1c(dbc, query_AddAutoMpg, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
        
    private JSONArray autoMpgAverage(Connection dbc) {
        final String query_AutoMPG_Average = "SELECT MAX(TotMiles) AS EndMiles, MIN(TotMiles) AS StartMiles, SUM(Gallons) AS Gallons, AVG(CostPG) AS AvgCost FROM Core.Auto_MPG;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_AutoMPG_Average, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject
                    .put("EndMiles", resultSet.getInt("EndMiles"))
                    .put("StartMiles", resultSet.getInt("StartMiles"))
                    .put("Gallons", resultSet.getDouble("Gallons"))
                    .put("AvgCost", resultSet.getDouble("AvgCost"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }    

    private JSONArray autoMpgAverageHondaCivic(Connection dbc) {
        final String query_AutoMPG_Average = "SELECT MAX(TotMiles) AS EndMiles, MIN(TotMiles) AS StartMiles, SUM(Gallons) AS Gallons, AVG(CostPG) AS AvgCost FROM Core.Auto_MPG_HONCIV;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_AutoMPG_Average, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject
                    .put("EndMiles", resultSet.getInt("EndMiles"))
                    .put("StartMiles", resultSet.getInt("StartMiles"))
                    .put("Gallons", resultSet.getDouble("Gallons"))
                    .put("AvgCost", resultSet.getDouble("AvgCost"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }    
    
    private JSONArray autoMpgAverageNewCar20(Connection dbc) {
        final String query_AutoMPG_Average = "SELECT MAX(TotMiles) AS EndMiles, MIN(TotMiles) AS StartMiles, SUM(Gallons) AS Gallons, " +
    			" AVG(CostPG) AS AvgCost FROM Core.Auto_MPG_KiaSorrento17;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_AutoMPG_Average, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject
                    .put("EndMiles", resultSet.getInt("EndMiles"))
                    .put("StartMiles", resultSet.getInt("StartMiles"))
                    .put("Gallons", resultSet.getDouble("Gallons"))
                    .put("AvgCost", resultSet.getDouble("AvgCost"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }    
    
    public JSONArray getAutoBillSum(Connection dbc) { return autoBillSum(dbc); }
    public JSONArray getAutoBillSumHondaCivic(Connection dbc) { return autoBillSumHondaCivic(dbc); }
    public JSONArray getAutoBillSumNewCar20(Connection dbc) { return autoBillSumNewCar20(dbc); }
    public JSONArray getAutoMaint(Connection dbc) { return autoMaint(dbc); }
    public JSONArray getAutoMaintHondaCivic(Connection dbc) { return autoMaintHondaCivic(dbc); }
    public JSONArray getAutoMaintNewCar20(Connection dbc) { return autoMaintNewCar20(dbc); }
    public JSONArray getAutoMpg(Connection dbc) { return autoMpg(dbc); }
    public JSONArray getAutoMpgHondaCivic(Connection dbc) { return autoMpgHondaCivic(dbc); }
    public JSONArray getAutoMpgNewCar20(Connection dbc) { return autoMpgNewCar20(dbc); }
    public JSONArray getAutoMpgAverage(Connection dbc) { return autoMpgAverage(dbc); }
    public JSONArray getAutoMpgAverageHondaCivic(Connection dbc) { return autoMpgAverageHondaCivic(dbc); }
    public JSONArray getAutoMpgAverageNewCar20(Connection dbc) { return autoMpgAverageNewCar20(dbc); }
    public String setAutoMpgAdd(Connection dbc, List<String> qParams) { return autoMpgAdd(dbc, qParams); }
    public String setAutoMpgAddHondaCivic(Connection dbc, List<String> qParams) { return autoMpgAddHondaCivic(dbc, qParams); }
    public String setAutoMpgAddNewCar20(Connection dbc, List<String> qParams) { return autoMpgAddNewCar20(dbc, qParams); }
    
}
