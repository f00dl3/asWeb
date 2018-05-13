/*
by Anthony Stump
Created: 19 Feb 2018
Updated: 13 May 2018
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

public class FinanceDAO {
    
    CommonBeans wcb = new CommonBeans();
    CheckbookBeans ckBkBeans = new CheckbookBeans();
    WebCommon wc = new WebCommon();
    MortgageBeans mb = new MortgageBeans();
    
    public JSONArray get3NetWorth(Connection dbc) {
        final String query_FBook_3NW = "SELECT" +
        " ((AsLiq + AsFix + Life + Credits) - Debts) AS Worth" +
        " FROM Core.FB_ENWT" +
        " WHERE AsOf = (SELECT max(AsOf) FROM Core.FB_ENWT);";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_FBook_3NW, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("Worth", resultSet.getString("Worth"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getAmSch(Connection dbc) {
        final String query_AmSch = "SELECT" +
            " DueDate, "+mb.getMortPayment()+" AS Payment, Extra, Planned," +
            " CAST((@runtot * (("+mb.getMortRate()+"/12)/100)) AS DECIMAL(5,2)) AS Interest," +
            " CAST((@runtot := @runtot + (@runtot * (("+mb.getMortRate()+"/12)/100)) - (Extra + Planned + "+mb.getMortPayment()+")) AS DECIMAL(10,2)) AS Balance" +
            " FROM Core.FB_WFML35;";
        JSONArray tContainer = new JSONArray();
        try { ResultSet rsA = wc.q2rs1c(dbc, wcb.getQSetRT120K(), null); rsA.close(); } catch (Exception e) { e.printStackTrace(); }
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_AmSch, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject
                    .put("DueDate", resultSet.getString("DueDate"))
                    .put("Payment", resultSet.getDouble("Payment"))
                    .put("Extra", resultSet.getDouble("Extra"))
                    .put("Planned", resultSet.getDouble("Planned"))
                    .put("Interest", resultSet.getDouble("Interest"))
                    .put("Balance", resultSet.getDouble("Balance"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getAutoBillSum(Connection dbc) {
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
    
    public JSONArray getAutoMaint(Connection dbc) {
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
    
    public JSONArray getAutoMpg(Connection dbc) {
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
    
    public JSONArray getAutoMpgAverage(Connection dbc) {
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
    
    public JSONArray getAssetTrack(Connection dbc) {
        final String query_FBook_ATrackPrep1 = "UPDATE Core.FB_Assets SET Value=(SELECT sum(Quantity)*7 FROM Core.BGames), Checked=CURDATE() WHERE Related='BGames';";
        final String query_FBook_ATrackPrep2 = "UPDATE Core.FB_Assets SET Value=(SELECT sum(Quantity)*7 FROM Core.Books), Checked=CURDATE() WHERE Related='Books';";
        final String query_FBook_ATrackPrep3 = "UPDATE Core.FB_Assets SET Value=(SELECT sum(Quantity)*7 FROM Core.DecorTools), Checked=CURDATE() WHERE Related='DecorTools';";
        final String query_FBook_ATrackPrep4 = "UPDATE Core.FB_Assets SET Value=(SELECT sum(Count)*7 FROM Core.Licenses), Checked=CURDATE() WHERE Related='Licenses';";
        final String query_FBook_ATrack = "SELECT Description, Type, Category, Value, Checked," +
                " Serial, UPC, Related, Location, Notes FROM Core.FB_Assets ORDER BY Type, Category, Description;";
        JSONArray tContainer = new JSONArray();
        try { String rsA = wc.q2do1c(dbc, query_FBook_ATrackPrep1, null); } catch (Exception e) { e.printStackTrace(); }
        try { String rsB = wc.q2do1c(dbc, query_FBook_ATrackPrep2, null); } catch (Exception e) { e.printStackTrace(); }
        try { String rsC = wc.q2do1c(dbc, query_FBook_ATrackPrep3, null); } catch (Exception e) { e.printStackTrace(); }
        try { String rsD = wc.q2do1c(dbc, query_FBook_ATrackPrep4, null); } catch (Exception e) { e.printStackTrace(); }
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_FBook_ATrack, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Description", resultSet.getString("Description"))
                    .put("Type", resultSet.getString("Type"))
                    .put("Category", resultSet.getString("Category"))
                    .put("Value", resultSet.getDouble("Value"))
                    .put("Checked", resultSet.getString("Checked"))
                    .put("Serial", resultSet.getString("Serial"))
                    .put("UPC", resultSet.getString("UPC"))
                    .put("Related", resultSet.getString("Related"))
                    .put("Locaiton", resultSet.getString("Location"))
                    .put("Notes", resultSet.getString("Notes"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getBGames(Connection dbc) {
        final String query_FBook_BGames = "SELECT Title, Quantity FROM Core.BGames ORDER BY Title;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_FBook_BGames, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Title", resultSet.getString("Title"))
                    .put("Quantity", resultSet.getDouble("Quantity"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getBills(Connection dbc) {
        final String query_FBook_Bills = "SELECT" +
            " Month, ELE, GAS, WAT, SWR, TRA, WEB, PHO, Gym, Other," +
            " (ELE+GAS+WAT+SWR+TRA+WEB+PHO+Gym+Other) AS Total" +
            " FROM Core.Bills" +
            " ORDER BY Month DESC;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_FBook_Bills, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Month", resultSet.getString("Month"))
                    .put("ELE", resultSet.getDouble("ELE"))
                    .put("GAS", resultSet.getDouble("GAS"))
                    .put("WAT", resultSet.getDouble("WAT"))
                    .put("SWR", resultSet.getDouble("SWR"))
                    .put("TRA", resultSet.getDouble("TRA"))
                    .put("WEB", resultSet.getDouble("WEB"))
                    .put("PHO", resultSet.getDouble("PHO"))
                    .put("Gym", resultSet.getDouble("Gym"))
                    .put("Other", resultSet.getDouble("Other"))
                    .put("Total", resultSet.getDouble("Total"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
   
    public JSONArray getBooks(Connection dbc) {
        final String query_FBook_Books = "SELECT Title, Quantity FROM Core.Books ORDER BY Title;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_FBook_Books, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Title", resultSet.getString("Title"))
                    .put("Quantity", resultSet.getDouble("Quantity"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getChecking(Connection dbc) { 
        final String query_FBook_Checking = "SELECT SUM(Credit-Debit) AS Balance FROM Core.FB_CFCK01 WHERE Date <= current_date;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_FBook_Checking, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject.put("Balance", resultSet.getDouble("Balance"));
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getCkBk(Connection dbc) {
        final String query_FBook_CkBk = "SELECT" + 
                " CTID, Bank, Date, Description, Debit, Credit," +
                " (@runtot := @runtot - Debit + Credit) AS Balance" +
                " FROM Core.FB_CFCK01" +
                " WHERE Date >= '" + ckBkBeans.getCkBkEomDate() + "'" +
                " ORDER BY Date, CTID";
        JSONArray tContainer = new JSONArray();
        try { ResultSet rsA = wc.q2rs1c(dbc, ckBkBeans.getCkBkPrep(), null); rsA.close(); } catch (Exception e) { e.printStackTrace(); }
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_FBook_CkBk, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject
                    .put("CTID", resultSet.getInt("CTID"))
                    .put("Bank", resultSet.getString("Bank"))
                    .put("Date", resultSet.getString("Date"))
                    .put("Description", resultSet.getString("Description"))
                    .put("Debit", resultSet.getDouble("Debit"))
                    .put("Credit", resultSet.getDouble("Credit"))
                    .put("Balance", resultSet.getDouble("Balance"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getCkBkComb(Connection dbc) {
        final String query_FBook_CkBkComb = "SELECT" +
                " Card, CTID, Bank, Date, Description, Debit, Credit FROM (" +
                " SELECT 'Checking' AS Card, CTID, Bank, Date, Description, Debit, Credit FROM Core.FB_CFCK01 UNION ALL" +
                " SELECT 'Savings' AS Card, CONCAT('S', STID) AS CTID, Date AS Bank, Date, Description, Debit, Credit FROM Core.FB_CFSV59 UNION ALL" +
                " SELECT 'Discover' AS Card, CONCAT('D', CTID) AS CTID, Date AS Bank, Date, Description, Debit, Credit FROM Core.FB_DICC45 UNION ALL" +
                " SELECT 'OldNavy' AS Card, CONCAT('O', CTID) AS CTID, Date AS Bank, Date, Description, Debit, Credit FROM Core.FB_ONCCXX" +
                " ) as tmp" +
                " ORDER BY Date, CTID DESC;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_FBook_CkBkComb, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject
                    .put("CTID", resultSet.getString("CTID"))
                    .put("Card", resultSet.getString("Card"))
                    .put("Bank", resultSet.getString("Bank"))
                    .put("Date", resultSet.getString("Date"))
                    .put("Description", resultSet.getString("Description"))
                    .put("Debit", resultSet.getDouble("Debit"))
                    .put("Credit", resultSet.getDouble("Credit"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getDecorTools(Connection dbc) {
        final String query_FBook_DecorTools = "SELECT Description, Quantity, Location, Checked FROM Core.DecorTools ORDER BY Description;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_FBook_DecorTools, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Description", resultSet.getString("Description"))
                    .put("Quantity", resultSet.getDouble("Quantity"))
                    .put("Location", resultSet.getString("Location"))
                    .put("Checked", resultSet.getString("Checked"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getEnw(Connection dbc) {
        final String query_FBook_ENW = "SELECT SUM(Assets) AS NetWorth FROM (" +
                " SELECT SUM(Value) AS Assets FROM Core.FB_Assets UNION ALL" +
                " SELECT -(min(CAST((@runtot := @runtot + (@runtot * (("+mb.getMortRate()+"/12)/100)) - (Extra + "+mb.getMortPayment()+")) AS DECIMAL(10, 1)))) AS Assets FROM Core.FB_WFML35 WHERE DueDate < current_date + interval '30' day UNION ALL" +
                " SELECT SUM(Credit-Debit) AS Assets FROM Core.FB_CFCK01 WHERE Date <= current_date UNION ALL" +
                " SELECT SUM(Credit-Debit) AS Assets FROM Core.FB_CFSV59 WHERE Date <= current_date" +
                ") as tmp;";
        JSONArray tContainer = new JSONArray();
        try { ResultSet rsA = wc.q2rs1c(dbc, wcb.getQSetRT120K(), null); rsA.close(); } catch (Exception e) { e.printStackTrace(); }
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_FBook_ENW, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("NetWorth", resultSet.getDouble("NetWorth"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getEnwt(Connection dbc) {
        final String query_FBook_ENWT = "SELECT" +
                " AsOf, ((AsLiq + AsFix + Life + Credits) - Debts) AS Worth," +
                " AsLiq, AsFix, Life, Credits, Debts, Growth" +
                " FROM Core.FB_ENWT" +
                " WHERE" +
                " AsOf > CURRENT_DATE - INTERVAL '365' day" +
                " AND (AsOf LIKE '%03-01%' OR AsOf LIKE '%06-01%' OR AsOf LIKE '%09-01%' OR AsOf LIKE '%12-01%')" +
                " ORDER BY AsOf DESC LIMIT 20;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_FBook_ENWT, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("AsOf", resultSet.getString("AsOf"))
                    .put("Worth", resultSet.getDouble("Worth"))
                    .put("AsLiq", resultSet.getDouble("AsLiq"))
                    .put("AsFix", resultSet.getDouble("AsFix"))
                    .put("Life", resultSet.getDouble("Life"))
                    .put("Credits", resultSet.getDouble("Credits"))
                    .put("Debts", resultSet.getDouble("Debts"))
                    .put("Growth", resultSet.getDouble("Growth"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getLicenses(Connection dbc) {
        final String query_FBook_Licenses = "SELECT Title, Type FROM Core.Licenses ORDER BY Title;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_FBook_Licenses, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Title", resultSet.getString("Title"))
                    .put("Type", resultSet.getString("Type"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getMort(Connection dbc) {
        final String query_Mort = "SELECT" +
            " MIN(@runtot := @runtot + (@runtot * ("+mb.getMortRate()+"/12)/100) - (Extra + "+mb.getMortPayment()+")) AS MBal" +
            " FROM Core.FB_WFML35" +
            " WHERE DueDate < current_date + interval '30' day;";
        JSONArray tContainer = new JSONArray();
        try { ResultSet rsA = wc.q2rs1c(dbc, wcb.getQSetRT120K(), null); rsA.close(); } catch (Exception e) { e.printStackTrace(); }
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_Mort, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject.put("MBal", resultSet.getDouble("MBal"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getNwga(Connection dbc) {
        final String query_FBook_NWGA = "SELECT FORMAT((AVG(Growth)),2) AS GrowthAvg FROM Core.FB_ENWT;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_FBook_NWGA, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject.put("GrowthAvg", resultSet.getDouble("GrowthAvg"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getQMerged(Connection dbc) {
        final String query_FBook_QMerged = "SELECT" +
                " (SELECT SUM(Quantity) FROM Core.BGames) AS qBGames," +
                " (SELECT SUM(Quantity) FROM Core.Books) as qBooks," +
                " (SELECT SUM(Quantity) FROM Core.DecorTools) as qDTools," +
                " (SELECT SUM(Count) FROM Core.Licenses) as qLicenses," +
                " (SELECT SUM(Asset) FROM Core.MediaServer WHERE Asset != 0) as qMedia" +
                " FROM Core.BGames" +
                " LIMIT 1;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_FBook_QMerged, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject
                    .put("qBGames", resultSet.getInt("qBGames"))
                    .put("qBooks", resultSet.getInt("qBooks"))
                    .put("qDTools", resultSet.getInt("qDTools"))
                    .put("qLicenses", resultSet.getInt("qLicenses"))
                    .put("qMedia", resultSet.getInt("qMedia"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getSaving(Connection dbc) {
        final String query_FBook_Saving = "SELECT (SUM(Credit-Debit)) AS SBal FROM Core.FB_CFSV59 WHERE Date <= current_date;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_FBook_Saving, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject.put("SBal", resultSet.getDouble("SBal"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getSavingChart(Connection dbc, List<String> qParams) { // ?  = end date
        final String query_ch_Saving_Opt = "SELECT SUM(Credit-Debit) AS Value, ? AS Date FROM FB_CFSV59 WHERE Date <= ?;";        
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_ch_Saving_Opt, qParams);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Date", resultSet.getString("Date"))
                    .put("Value", resultSet.getDouble("Value"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getSettingC(Connection dbc) {
        final String query_FBook_SettingC = "SELECT Day, Time, Temp FROM Core.TH_Cool;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_FBook_SettingC, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Day", resultSet.getString("Day"))
                    .put("Time", resultSet.getInt("Time"))
                    .put("Temp", resultSet.getInt("Temp"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getSettingH(Connection dbc) {
        final String query_FBook_SettingH = "SELECT Day, Time, Temp FROM Core.TH_Heat;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_FBook_SettingH, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Day", resultSet.getString("Day"))
                    .put("Time", resultSet.getInt("Time"))
                    .put("Temp", resultSet.getInt("Temp"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getSvBk(Connection dbc) {
        final String query_FBook_SvBk = "SELECT" +
                " STID, Date, Description, Debit, Credit" +
                " FROM Core.FB_CFSV59" +
                " WHERE Date BETWEEN (current_date - interval '180' day) AND current_date" +
                " ORDER BY Date DESC, STID DESC;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_FBook_SvBk, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject
                    .put("STID", resultSet.getInt("STID"))
                    .put("Date", resultSet.getString("Date"))
                    .put("Description", resultSet.getString("Description"))
                    .put("Debit", resultSet.getDouble("Debit"))
                    .put("Credit", resultSet.getDouble("Credit"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
     
    public String setAssetTrackUpdate(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        String query_FBook_ATrackUp = "UPDATE Core.FB_Assets SET Value=?, Notes=?, Checked=CURDATE() WHERE Description=?;";
        try { returnData = wc.q2do1c(dbc, query_FBook_ATrackUp, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
    public String setAutoMpgAdd(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        String query_AddAutoMpg = "INSERT IGNORE INTO Core.Auto_MPG VALUES (?,?,?,?,0,0);";
        try { returnData = wc.q2do1c(dbc, query_AddAutoMpg, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
    public String setCheckbookAdd(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        String query_FBook_CkBkAdd = "INSERT INTO Core.FB_CFCK01 (CTID, Bank, Date, Description, Debit, Credit) VALUES (Null,?,?,?,?,?);";
        try { returnData = wc.q2do1c(dbc, query_FBook_CkBkAdd, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
    public String setCheckbookUpdate(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        String query_FBook_CkBkUpdate = "UPDATE Core.FB_CFCK01 SET Bank=?, Date=?, Description=?, Debit=?, Credit=? where CTID=?;";
        try { returnData = wc.q2do1c(dbc, query_FBook_CkBkUpdate, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
     
    public String setDecorToolsUpdate(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        String query_FBook_DecorToolsUpdate = "UPDATE Core.DecorTools SET Quantity=?, Location=?, Checked=CURDATE() WHERE Description=?;";
        try { returnData = wc.q2do1c(dbc, query_FBook_DecorToolsUpdate, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
    public String setSavingsAdd(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        String query_FBook_SvBkAdd = "INSERT INTO FB_CFSV59 VALUES (Null,?,?,?,?);";
        try { returnData = wc.q2do1c(dbc, query_FBook_SvBkAdd, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
        
    
}
