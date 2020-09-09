/*
by Anthony Stump
Split from Parent: 4 Aug 2020
Updated: 9 Sep 2020
*/

package asWebRest.dao;

import asWebRest.secure.CheckbookBeans;
import asWebRest.secure.MortgageBeans;
import asWebRest.shared.CommonBeans;
import java.sql.ResultSet;
import asWebRest.shared.WebCommon;
import java.sql.Connection;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class StockDAO {
    
    CommonBeans wcb = new CommonBeans();
    CheckbookBeans ckBkBeans = new CheckbookBeans();
    WebCommon wc = new WebCommon();
    MortgageBeans mb = new MortgageBeans();

    private JSONArray eTradeBrokerageAccount(Connection dbc) {
        final String query_etba = "SELECT" +
        		" BTID, Date, Debit, Credit" +
        		" FROM Core.FB_ETIBXX" +
        		" ORDER BY DATE DESC LIMIT 5;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_etba, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Date", resultSet.getString("Date"))
                    .put("Debit", resultSet.getDouble("Debit"))
                    .put("Credit", resultSet.getDouble("Credit"))
                    .put("BTID",  resultSet.getInt("BTID"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    	
    }    

    private String eTradeBrokerageAccountAdd(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        String query_etbaAdd = "INSERT INTO Core.FB_ETIBXX (Date, Debit, Credit) VALUES (?,?,?);";
        try { returnData = wc.q2do1c(dbc, query_etbaAdd, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
    private JSONArray eTradeBalance(Connection dbc) {
        final String query_etb = "SELECT" +
        		" SUM(" +
        		"  (SELECT Value FROM Core.FB_Assets WHERE Description LIKE 'eTrade%') +" +
        		"  (SELECT SUM(Count*LastValue) FROM Core.StockShares WHERE Holder='eTrade')" +
        		") AS Balance," +
        		" (SELECT SUM(Credit-Debit) FROM Core.FB_ETIBXX) AS Contributions";		
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_etb, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Balance", resultSet.getDouble("Balance"))
                    .put("Contributions", resultSet.getDouble("Contributions"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    	
    }
        
    private JSONArray stockList(Connection dbc) { 
        final String query_GetStocks = "SELECT Symbol, Count, Holder," +
        		" LastValue, Description, PreviousClose," +
        		" LastBuy, LastSell, Invested, Managed," +
        		" EJTI15, EJRI23, EJRI07, LastComparedShares, Multiplier" +
        		" FROM Core.StockShares WHERE Active=1;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_GetStocks, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject
                	.put("Symbol", resultSet.getString("Symbol"))
                	.put("Count", resultSet.getDouble("Count"))
                	.put("Holder", resultSet.getString("Holder"))
                	.put("LastValue", resultSet.getString("LastValue"))
                	.put("Description", resultSet.getString("Description"))
                	.put("LastBuy", resultSet.getDouble("LastBuy"))
                	.put("LastSell", resultSet.getDouble("LastSell"))
                	.put("Invested", resultSet.getDouble("Invested"))
                	.put("Managed", resultSet.getInt("Managed"))
                	.put("EJTI15", resultSet.getDouble("EJTI15"))
                	.put("EJRI23", resultSet.getDouble("EJRI23"))
                	.put("EJRI07", resultSet.getDouble("EJRI07"))
                	.put("LastComparedShares", resultSet.getString("LastComparedShares"))
                	.put("Multiplier", resultSet.getDouble("Multiplier"))
                	.put("PreviousClose", resultSet.getString("PreviousClose"));
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }

    private String stockAdd(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        String query_AddStock = "INSERT INTO Core.StockShares " +
        		" (Symbol, Count, Active, Holder, Description, Managed)" +
        		" VALUES " +
        		" (?, ?, 1, ?, ?, ?);";
        try { returnData = wc.q2do1c(dbc, query_AddStock, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
    private JSONArray stockListPublic(Connection dbc) { 
        final String query_GetStocks = "SELECT Symbol, LastValue, Description, PreviousClose, FROM Core.StockShares WHERE Active=1;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_GetStocks, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject
                	.put("Symbol", resultSet.getString("Symbol"))
                	.put("LastValue", resultSet.getString("LastValue"))
                	.put("Description", resultSet.getString("Description"))
                	.put("PreviousClose", resultSet.getString("PreviousClose"));
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    private JSONArray stockHistory(Connection dbc) { 
        final String query_GetHistory = "SELECT AsOf, jsonData FROM Feeds.StockPrices ORDER BY AsOf DESC LIMIT 256;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_GetHistory, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject
                	.put("AsOf", resultSet.getString("AsOf"))
                	.put("jsonData", resultSet.getString("jsonData"));
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    private String stockIndex(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        String query_UpdateIndex = "INSERT INTO Feeds.StockPrices (jsonData) VALUES (?);";
        try { returnData = wc.q2do1c(dbc, query_UpdateIndex, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }

    private String stockShareUpdate(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        String query_ShareUpdate = "UPDATE Core.StockShares SET Count=?, Holder=? WHERE Symbol=?;";
        try { returnData = wc.q2do1c(dbc, query_ShareUpdate, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }

    private String stockUpdate(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        String query_UpdateStock = "UPDATE Core.StockShares SET LastValue=?, PreviousClose=? WHERE Symbol=?;";
        String query_UpdateStockB = "UPDATE Core.FB_Assets SET Value=(SELECT SUM(Count*LastValue) FROM Core.StockShares WHERE Managed=0), Checked=CURDATE() WHERE Description='Stocks';";
        String query_UpdateStockC = "UPDATE Core.FB_Assets SET Value=(SELECT SUM(Count*LastValue) FROM Core.StockShares WHERE Holder='EJones' AND Managed=1), Checked=CURDATE() WHERE Description='AE - Edward Jones';";
        String query_UpdateStockD = "UPDATE Core.FB_Assets SET Value=(SELECT SUM(Count*(LastValue*Multiplier)) FROM Core.StockShares WHERE Holder='FidelityA' AND Managed=1), Checked=CURDATE() WHERE Description='A - Fidelity Sprint 401k';";
        String query_UpdateStockE = "UPDATE Core.FB_Assets SET Value=(SELECT SUM(Count*LastValue) FROM Core.StockShares WHERE Holder='FidelityE' AND Managed=1), Checked=CURDATE() WHERE Description='E - Fidelity 401k CPFP';";
        try { returnData = wc.q2do1c(dbc, query_UpdateStock, qParams); } catch (Exception e) { e.printStackTrace(); }
        try { returnData += wc.q2do1c(dbc, query_UpdateStockB, null); } catch (Exception e) { e.printStackTrace(); }
        try { returnData += wc.q2do1c(dbc, query_UpdateStockC, null); } catch (Exception e) { e.printStackTrace(); }
        try { returnData += wc.q2do1c(dbc, query_UpdateStockD, null); } catch (Exception e) { e.printStackTrace(); }
        try { returnData += wc.q2do1c(dbc, query_UpdateStockE, null); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }

    public JSONArray getETradeBrokerageAccount(Connection dbc) { return eTradeBrokerageAccount(dbc); }
    public JSONArray getETradeBalance(Connection dbc) { return eTradeBalance(dbc); }
    public JSONArray getStockHistory(Connection dbc) { return stockHistory(dbc); }
    public JSONArray getStockList(Connection dbc) { return stockList(dbc); }
    public JSONArray getStockListPublic(Connection dbc) { return stockListPublic(dbc); }
    public String setETradeBrokerageAccountAdd(Connection dbc, List<String> qParams) { return eTradeBrokerageAccountAdd(dbc, qParams); }
    public String setStockAdd(Connection dbc, List<String> qParams) { return stockAdd(dbc, qParams); }
    public String setStockIndex(Connection dbc, List<String> qParams) { return stockIndex(dbc, qParams); }
    public String setStockShareUpdate(Connection dbc, List<String> qParams) { return stockShareUpdate(dbc, qParams); }
    public String setStockUpdate(Connection dbc, List<String> qParams) { return stockUpdate(dbc, qParams); }
    
}
