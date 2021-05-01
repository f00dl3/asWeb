/*
by Anthony Stump
Split from Parent: 4 Aug 2020
Updated: 1 May 2021
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


    private JSONArray CryptoAccount(Connection dbc) {
        final String query_etba = "SELECT" +
        		" BTID, Date, Debit, Credit" +
        		" FROM Finances.FB_CBCRYP" +
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

    private String CryptoAccountAdd(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        String query_cryAdd = "INSERT INTO Finances.FB_CBCRYP (Date, Debit, Credit) VALUES (?,?,?);";
        try { returnData = wc.q2do1c(dbc, query_cryAdd, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
    private JSONArray CryptoBalance(Connection dbc) {
        final String query_etb = "SELECT" +
        		" SUM(" +
        		"  (SELECT SUM(Count*LastValue) FROM Finances.StockShares WHERE Holder='Crypto')" +
        		") AS Balance," +
        		" (SELECT SUM(Credit-Debit) FROM Finances.FB_CBCRYP) AS Contributions";		
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
    
    private JSONArray eTradeBrokerageAccount(Connection dbc) {
        final String query_etba = "SELECT" +
        		" BTID, Date, Debit, Credit" +
        		" FROM Finances.FB_ETIBXX" +
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
        String query_etbaAdd = "INSERT INTO Finances.FB_ETIBXX (Date, Debit, Credit) VALUES (?,?,?);";
        try { returnData = wc.q2do1c(dbc, query_etbaAdd, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
    private JSONArray eTradeBalance(Connection dbc) {
        final String query_etb = "SELECT" +
        		" SUM(" +
        		"  (SELECT Value FROM Finances.FB_Assets WHERE Description LIKE 'Brokerage%') +" +
        		"  (SELECT SUM((FIIBAN-Unvested)*LastValue) FROM Finances.StockShares WHERE FIIBAN != 0)" +
        		") AS Balance," +
        		" (SELECT SUM(Credit-Debit) FROM Finances.FB_ETIBXX) AS Contributions";		
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
        		" LastBuy, LastBuyFIRIAN, LastBuyFI4KAN, LastBuyEJTI15, LastBuyEJRI07," +
        		" LastSell, Invested, Managed, SpilloverSavings," +
        		" EJTI15, EJRI23, EJRI07, LastComparedShares, Multiplier, LastUpdated," +
        		" FI4KAN, FIRIAN, FIIBAN, Unvested" +
        		" FROM Finances.StockShares WHERE Active=1;";
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
                	.put("LastBuyFIRIAN", resultSet.getDouble("LastBuyFIRIAN"))
                	.put("LastBuyFI4KAN", resultSet.getDouble("LastBuyFI4KAN"))
                	.put("LastBuyEJTI15", resultSet.getDouble("LastBuyEJTI15"))
                	.put("LastBuyEJRI07", resultSet.getDouble("LastBuyEJRI07"))
                	.put("LastSell", resultSet.getDouble("LastSell"))
                	.put("Invested", resultSet.getDouble("Invested"))
                	.put("Managed", resultSet.getInt("Managed"))
                	.put("EJTI15", resultSet.getDouble("EJTI15"))
                	.put("EJRI23", resultSet.getDouble("EJRI23"))
                	.put("EJRI07", resultSet.getDouble("EJRI07"))
                	.put("FI4KAN", resultSet.getDouble("FI4KAN"))
                	.put("FIRIAN", resultSet.getDouble("FIRIAN"))
                	.put("FIIBAN", resultSet.getDouble("FIIBAN"))
                	.put("LastComparedShares", resultSet.getString("LastComparedShares"))
                	.put("LastUpdated",  resultSet.getString("LastUpdated"))
                	.put("Multiplier", resultSet.getDouble("Multiplier"))
			.put("SpilloverSavings", resultSet.getInt("SpilloverSavings"))
			.put("Unvested", resultSet.getDouble("Unvested"))
                	.put("PreviousClose", resultSet.getString("PreviousClose"));
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }

    private String stockAdd(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        String query_AddStock = "INSERT INTO Finances.StockShares " +
        		" (Symbol, Count, Active, Holder, Description, Managed, " +
        		" LastBuy, LastBuyFIRIAN, LastBuyFI4KAN, LastBuyEJTI15, LastBuyEJRI07)" +
        		" VALUES " +
        		" (?, ?, 1, ?, ?, ?, ?, ?, ?, ?, ?);";
        try { returnData = wc.q2do1c(dbc, query_AddStock, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
    private JSONArray stockListPublic(Connection dbc) { 
        final String query_GetStocks = "SELECT Symbol, LastValue, Description, PreviousClose, FROM Finances.StockShares WHERE Active=1;";
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
        String query_ShareUpdate = "UPDATE Finances.StockShares SET Count=?, Holder=?, EJTI15=?, EJRI07=?, FI4KAN=?, FIRIAN=?, FIIBAN=?, " +
        		" LastBuy=?, LastBuyFIRIAN=?, LastBuyFI4KAN=?, LastBuyEJTI15=?, LastBuyEJRI07=? WHERE Symbol=?;";
        String query_ShareUpdateB = "UPDATE Finances.StockShares SET Count=EJTI15+EJRI07 WHERE Holder='EJones';";
        try { returnData = wc.q2do1c(dbc, query_ShareUpdate, qParams); } catch (Exception e) { e.printStackTrace(); }
        try { returnData = wc.q2do1c(dbc, query_ShareUpdateB, null); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }

    private String stockUpdate(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        String query_UpdateStock = "UPDATE Finances.StockShares SET LastValue=?, PreviousClose=? WHERE Symbol=?;";
        String query_UpdateStockB = "UPDATE Finances.FB_Assets SET Value=((SELECT SUM(FIIBAN*LastValue) FROM Finances.StockShares WHERE Managed=0)+(SELECT SUM(Count*LastValue) FROM Finances.StockShares WHERE Holder='Crypto')), Checked=CURDATE() WHERE Description='Stocks';";
        String query_UpdateStockC = "UPDATE Finances.FB_Assets SET Value=(SELECT SUM(Count*LastValue) FROM Finances.StockShares WHERE Holder='EJones' AND Managed=1), Checked=CURDATE() WHERE Description='AE - Edward Jones';";
        String query_UpdateStockD = "UPDATE Finances.FB_Assets SET Value=(SELECT SUM(FI4KAN*(LastValue*Multiplier)) FROM Finances.StockShares WHERE FI4KAN != 0), Checked=CURDATE() WHERE Description='A - Fidelity Sprint 401k';";
        String query_UpdateStockE = "UPDATE Finances.FB_Assets SET Value=(SELECT SUM(Count*LastValue) FROM Finances.StockShares WHERE Holder='FidelityE' AND Managed=1), Checked=CURDATE() WHERE Description='E - Fidelity 401k CPFP';";
        String query_UpdateStockF = "UPDATE Finances.FB_Assets SET Value=(SELECT SUM(FIRIAN*(LastValue*Multiplier)) FROM Finances.StockShares WHERE FIRIAN != 0), Checked=CURDATE() WHERE Description='A - Fidelity Roth IRA';";
        try { returnData = wc.q2do1c(dbc, query_UpdateStock, qParams); } catch (Exception e) { e.printStackTrace(); }
        try { returnData += wc.q2do1c(dbc, query_UpdateStockB, null); } catch (Exception e) { e.printStackTrace(); }
        try { returnData += wc.q2do1c(dbc, query_UpdateStockC, null); } catch (Exception e) { e.printStackTrace(); }
        try { returnData += wc.q2do1c(dbc, query_UpdateStockD, null); } catch (Exception e) { e.printStackTrace(); }
        try { returnData += wc.q2do1c(dbc, query_UpdateStockE, null); } catch (Exception e) { e.printStackTrace(); }
	try { returnData += wc.q2do1c(dbc, query_UpdateStockF, null); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }

    public JSONArray getCryptoAccount(Connection dbc) { return CryptoAccount(dbc); }
    public JSONArray getCryptoBalance(Connection dbc) { return CryptoBalance(dbc); }
    public JSONArray getETradeBrokerageAccount(Connection dbc) { return eTradeBrokerageAccount(dbc); }
    public JSONArray getETradeBalance(Connection dbc) { return eTradeBalance(dbc); }
    public JSONArray getStockHistory(Connection dbc) { return stockHistory(dbc); }
    public JSONArray getStockList(Connection dbc) { return stockList(dbc); }
    public JSONArray getStockListPublic(Connection dbc) { return stockListPublic(dbc); }
    public String setCryptoAccountAdd(Connection dbc, List<String> qParams) { return CryptoAccountAdd(dbc, qParams); }
    public String setETradeBrokerageAccountAdd(Connection dbc, List<String> qParams) { return eTradeBrokerageAccountAdd(dbc, qParams); }
    public String setStockAdd(Connection dbc, List<String> qParams) { return stockAdd(dbc, qParams); }
    public String setStockIndex(Connection dbc, List<String> qParams) { return stockIndex(dbc, qParams); }
    public String setStockShareUpdate(Connection dbc, List<String> qParams) { return stockShareUpdate(dbc, qParams); }
    public String setStockUpdate(Connection dbc, List<String> qParams) { return stockUpdate(dbc, qParams); }
    
}
