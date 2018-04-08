/*
by Anthony Stump
Created: 19 Feb 2018
Updated: 7 Apr 2018
*/

package asWebRest.dao;

import asWebRest.secure.CheckbookBeans;
import asWebRest.secure.MortgageBeans;
import asWebRest.shared.CommonBeans;
import java.sql.ResultSet;
import asWebRest.shared.WebCommon;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;

public class FinanceDAO {
    
    CommonBeans wcb = new CommonBeans();
    CheckbookBeans ckBkBeans = new CheckbookBeans();
    WebCommon wc = new WebCommon();
    MortgageBeans mb = new MortgageBeans();
    
    public JSONArray get3NetWorth() {
        final String query_FBook_3NW = "SELECT" +
        " ((AsLiq + AsFix + Life + Credits) - Debits) AS Worth" +
        " FROM Core.FB_ENWT" +
        " WHERE AsOf = (SELECT max(AsOf) FROM Core.FB_ENWT);";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_FBook_3NW, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject.put("Worth", resultSet.getString("Worth"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getAmSch() {
        final String query_AmSch = "SELECT" +
            " DueDate, "+mb.getMortPayment()+" AS Payment, Extra, Planned," +
            " CAST((@runtot * (("+mb.getMortRate()+"/12)/100)) AS DECIMAL(5,2)) AS Interest," +
            " CAST((@runtot := @runtot + (@runtot * (("+mb.getMortRate()+"/12)/100)) - (Extra + Planned + )) AS DECIMAL(10,2)) AS Balance" +
            " FROM Core.FB_WFLM35;";
        JSONArray tContainer = new JSONArray();
        try { ResultSet rsA = wc.q2rs(wcb.getQSetRT120K(), null); rsA.close(); } catch (Exception e) { e.printStackTrace(); }
        try {
            ResultSet resultSet = wc.q2rs(query_AmSch, null);
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
    
    public JSONArray getAutoBillSum() {
        final String query_AutoBillSum = "SELECT SUM(Bill) AS BillSum from Core.AutoMaint_MAZ6;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_AutoBillSum, null);
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
    
    public JSONArray getAutoMaint() {
        final String query_AutoMaint = "SELECT Invoice, Miles, Date, Location, Services, Bill, OilCh, TireRotate" +
                " FROM Core.AutoMaint_MAZ6 ORDER BY Date DESC LIMIT 10;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_AutoMaint, null);
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
    
    public JSONArray getAutoMpg() {
        final String query_AutoMPG = "SELECT Date, TotMiles, CostPG, Gallons FROM Auto_MPG ORDER BY Date DESC LIMIT 10;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_AutoMPG, null);
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
    
    public JSONArray getAutoMpgAverage() {
        final String query_AutoMPG_Average = "SELECT MAX(TotMiles) AS EndMiles, MIN(TotMiles) AS StartMiles, SUM(Gallons) AS Gallons, AVG(CostPG) AS AvgCost FROM Core.Auto_MPG;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_AutoMPG_Average, null);
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
    
    public JSONArray getAssetTrack() {
        final String query_FBook_ATrackPrep1 = "UPDATE Core.FB_Assets SET Value=(SELECT sum(Quantity)*7 FROM Core.BGames), Checked=CURDATE() WHERE Related='BGames';";
        final String query_FBook_ATrackPrep2 = "UPDATE Core.FB_Assets SET Value=(SELECT sum(Quantity)*7 FROM Core.Books), Checked=CURDATE() WHERE Related='Books';";
        final String query_FBook_ATrackPrep3 = "UPDATE Core.FB_Assets SET Value=(SELECT sum(Quantity)*7 FROM Core.DecorTools), Checked=CURDATE() WHERE Related='DecorTools';";
        final String query_FBook_ATrackPrep4 = "UPDATE Core.FB_Assets SET Value=(SELECT sum(Count)*7 FROM Core.Licenses), Checked=CURDATE() WHERE Related='Licenses';";
        final String query_FBook_ATrack = "SELECT Description, Type, Category, Value, Checked," +
                " Serial, UPC, Related, Location, Notes FROM Core.FB_Assets ORDER BY Type, Category, Description;";
        JSONArray tContainer = new JSONArray();
        try { ResultSet rsA = wc.q2rs(query_FBook_ATrackPrep1, null); rsA.close(); } catch (Exception e) { e.printStackTrace(); }
        try { ResultSet rsB = wc.q2rs(query_FBook_ATrackPrep2, null); rsB.close(); } catch (Exception e) { e.printStackTrace(); }
        try { ResultSet rsC = wc.q2rs(query_FBook_ATrackPrep3, null); rsC.close(); } catch (Exception e) { e.printStackTrace(); }
        try { ResultSet rsD = wc.q2rs(query_FBook_ATrackPrep4, null); rsD.close(); } catch (Exception e) { e.printStackTrace(); }
        try {
            ResultSet resultSet = wc.q2rs(query_FBook_ATrack, null);
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
    
    public JSONArray getBGames() {
        final String query_FBook_BGames = "SELECT Title, Quantity FROM Core.BGames ORDER BY Title;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_FBook_BGames, null);
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
    
    public JSONArray getBills() {
        final String query_FBook_Bills = "SELECT" +
            " Month, ELE, GAS, WAT, SWR, TRA, WEB, PHO, Gym, Other" +
            " (ELE+GAS+WAT+SWR+TRA+WEB+PHO+Gym+Other) as Total" +
            " FROM Core.Bills" +
            " ORDER BY Month DESC;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_FBook_Bills, null);
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
   
    public JSONArray getBooks() {
        final String query_FBook_Books = "SELECT Title, Quantity FROM Core.Books ORDER BY Title;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_FBook_Books, null);
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
    
    public JSONArray getChecking() { 
        final String query_FBook_Checking = "SELECT FORMAT((SUM(Credit-Debit)),2) AS Balance FROM Core.FB_CFCK01 WHERE Date <= current_date;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_FBook_Checking, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject.put("Balance", resultSet.getDouble("Balance"));
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getCkBk() {
        final String query_FBook_CkBk = "SELECT" + 
                " CTID, Bank, Date, Description, Debit, Credit," +
                " (@runtot := @runtot - Debit + Credit) AS Balance" +
                " FROM Core.FB_CFCK01" +
                " WHERE Date >= " + ckBkBeans.getCkBkEomDate() +
                " ORDER BY Date, CTID";
        JSONArray tContainer = new JSONArray();
        try { ResultSet rsA = wc.q2rs(ckBkBeans.getCkBkPrep(), null); rsA.close(); } catch (Exception e) { e.printStackTrace(); }
        try {
            ResultSet resultSet = wc.q2rs(query_FBook_CkBk, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject
                    .put("CTID", resultSet.getInt("CTID"))
                    .put("Bank", resultSet.getString("Bank"))
                    .put("Date", resultSet.getString("Date"))
                    .put("Description", resultSet.getString("Description"))
                    .put("Deibt", resultSet.getDouble("Debit"))
                    .put("Credit", resultSet.getDouble("Credit"))
                    .put("Balance", resultSet.getDouble("Balance"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getCkBkRange() {
        final String query_FBook_CkBkRange = "SELECT" +
                " Card, CTID, Bank, Date, Description, Debit, Credit FROM (" +
                " SELECT 'Checking Check Book' AS Card, CTID, Bank, Date, Description, Debit, Credit FROM Core.FB_CFCK01 UNION ALL" +
                " SELECT 'Savings' AS Card, CONCAT('S', STID) AS CTID, Date AS Bank, Date, Description, Debit, Credit FROM Core.FB_CFSV59 UNION ALL" +
                " SELECT 'Discover' AS Card, CONCAT('D', CTID) AS CTID, Date AS Bank, Date, Description, Debit, Credit FROM Core.FB_DICC45 UNION ALL" +
                " SELECT 'Old Navy' AS Card, CONCAT('O', CTID) AS CTID, Date AS Bank, Date, Description, Debit, Credit FROM Core.FB_ONCCXX" +
                " ) as tmp" +
                " ORDER BY Date, CTID DESC;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_FBook_CkBkRange, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject
                    .put("CTID", resultSet.getInt("CTID"))
                    .put("Bank", resultSet.getString("Bank"))
                    .put("Date", resultSet.getString("Date"))
                    .put("Description", resultSet.getString("Description"))
                    .put("Deibt", resultSet.getDouble("Debit"))
                    .put("Credit", resultSet.getDouble("Credit"))
                    .put("Balance", resultSet.getDouble("Balance"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getDecorTools() {
        final String query_FBook_DecorTools = "SELECT Description, Quantity, Location, Checked FROM Core.DecorTools ORDER BY Description;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_FBook_DecorTools, null);
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
    
    public JSONArray getEnw() {
        
        final String query_FBook_ENW = "SELECT" +
                " SUM(Assets) AS NetWorth FROM Core.FB_Assets UNION ALL" +
                " SELECT -(min(CAST((@runtot := @runtot + (@runtot * (("+mb.getMortRate()+"/12)/100)) - (Extra + "+mb.getMortPayment()+")) AS DECIMAL(10, 1)))) AS Balance FROM Core.FB_WFML35 WHERE DueDate < current_date + interval '30' day UNION ALL" +
                " SELECT sum(Credit-Debit) AS Assets FROM Core.FB_CFCK01 WHERE Date <= current_date UNION ALL" +
                " SELECT sum(Credit-Debit) AS Assets FROM Core.FB_CFSV59 WHERE Date <= current_date) as tmp;";
        
        JSONArray tContainer = new JSONArray();
        try { ResultSet rsA = wc.q2rs(wcb.getQSetRT120K(), null); rsA.close(); } catch (Exception e) { e.printStackTrace(); }
        try {
            ResultSet resultSet = wc.q2rs(query_FBook_ENW, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Assets", resultSet.getDouble("Assets"))
                    .put("Balance", resultSet.getDouble("Balance"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getEnwt() {
        final String query_FBook_ENWT = "SELECT" +
                " AsOf, ((AsLiq + AsFix + Life + Credits) - Debits) AS Worth," +
                " AsLiq, AsFix, Life, Credits, Debits, Growth" +
                " FROM Core.FB_ENWT" +
                " WHERE" +
                " AsOf > CURRENT_DATE - INTERVAL '365' day'" +
                " (AsOf LIKE '%03-01%' OR AsOf LIKE '%06-01%' OR AsOf LIKE '%09-01%' OR AsOf LIKE '%12-01%')" +
                " ORDER BY AsOf DESC LIMIT 20;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_FBook_ENWT, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                    .put("AsOf", resultSet.getString("AsOf"))
                    .put("Worth", resultSet.getDouble("Worth"))
                    .put("AsLiq", resultSet.getDouble("AsLiq"))
                    .put("AsFix", resultSet.getDouble("AsFix"))
                    .put("Life", resultSet.getDouble("Life"))
                    .put("Credits", resultSet.getDouble("Credits"))
                    .put("Debits", resultSet.getDouble("Debits"))
                    .put("Growth", resultSet.getDouble("Growth"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getLicenses() {
        final String query_FBook_Licenses = "SELECT Title, Type FROM Core.Licenses ORDER BY Title;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_FBook_Licenses, null);
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
    
    public JSONArray getMort() {
        final String query_Mort = "SELECT" +
            " min(@runtot := @runtot + (@runtot * ("+mb.getMortRate()+"/12)/100)) - (Extra + "+mb.getMortPayment()+")) AS MBal" +
            " FROM Core.FB_WFML35" +
            " WHERE DueDate < current_date + interval '30' day;";
        
        JSONArray tContainer = new JSONArray();
        try { ResultSet rsA = wc.q2rs(wcb.getQSetRT120K(), null); } catch (Exception e) { e.printStackTrace(); }
        try {
            ResultSet resultSet = wc.q2rs(query_Mort, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject.put("MBal", resultSet.getDouble("MBal"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getNwga() {
        final String query_FBook_NWGA = "SELECT FORMAT((AVG(Growth)),2) AS GrowthAvg FROM Core.FB_ENWT;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_FBook_NWGA, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject.put("GrowthAvg", resultSet.getDouble("GrowthAvg"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getQMerged() {
        final String query_FBook_QMerged = "SELECT\n" +
                " (SELECT SUM(Quantity) FROM Core.BGames) AS qBGames," +
                " (SELECT SUM(Quantity) FROM Core.Books) as qBooks," +
                " (SELECT SUM(Quantity) FROM Core.DecorTools) as qDTools," +
                " (SELECT SUM(Count) FROM Core.Licenses) as qLicenses," +
                " (SELECT SUM(Asset) FROM Core.MediaServer WHERE Asset != 0) as qMedia" +
                " FROM Core.BGames" +
                " LIMIT 1;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_FBook_QMerged, null);
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
    
    public JSONArray getSaving() {
        final String query_FBook_Saving = "SELECT FORMAT((SUM(Credit-Debit)), 0) AS SBal FROM Core.FB_CFSV59 WHERE Date <= current_date;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_FBook_Saving, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject.put("SBal", resultSet.getDouble("SBal"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    public JSONArray getSettingC() {
        final String query_FBook_SettingC = "SELECT Day, Time, Temp FROM Core.TH_Cool;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_FBook_SettingC, null);
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
    
    public JSONArray getSettingH() {
        final String query_FBook_SettingH = "SELECT Day, Time, Temp FROM Core.TH_Heat;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_FBook_SettingH, null);
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
    
    public JSONArray getSvBk() {
        final String query_FBook_SvBk = "SELECT" +
                " STID, Date, Description, Debit, Credit" +
                " FORM Core.FB_CFSV59" +
                " WHERE Date BETWEEN CURDATE()-interval 180 day AND CURDATE()" +
                " ORDER BY Date DESC;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_FBook_SvBk, null);
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
    
    public JSONArray getUURel() {
        final String query_FBook_UURel = "SELECT URL FROM Core.WebLinks WHERE Master='FBook.php-UU';";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs(query_FBook_UURel, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject.put("URL", resultSet.getString("URL"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
     
    public String setAssetTrackUpdate(List<String> qParams) {
        String returnData = "Query has not ran yet or failed!";
        String query_FBook_ATrackUp = "UPDATE FB_Assets SET Value=?, Notes=?, Checked=CURDATE() WHERE Description=?;";
        try { returnData = wc.q2do(query_FBook_ATrackUp, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
     
    public String setDecorToolsUpdate(List<String> qParams) {
        String returnData = "Query has not ran yet or failed!";
        String query_FBook_DecorToolsUpdate = "UPDATE DecorTools SET Quantity=?, Location=?, Checked=CURDATE() WHERE Description=?;";
        try { returnData = wc.q2do(query_FBook_DecorToolsUpdate, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
    
    
}
