/*
by Anthony Stump
Created: 19 Feb 2018
Updated: 2 Apr 2020
*/

package asWebRest.dao;

import asWebRest.secure.CheckbookBeans;
import asWebRest.secure.MortgageBeans;
import asWebRest.shared.CommonBeans;
import java.sql.ResultSet;
import asWebRest.shared.WebCommon;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.Months;
import org.json.JSONArray;
import org.json.JSONObject;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class FinanceDAO {
    
    CommonBeans wcb = new CommonBeans();
    CheckbookBeans ckBkBeans = new CheckbookBeans();
    WebCommon wc = new WebCommon();
    MortgageBeans mb = new MortgageBeans();
    
    private JSONArray amSch(Connection dbc) {
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
    
    private String assetTrackUpdate(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        String query_FBook_ATrackUp = "UPDATE Core.FB_Assets SET Value=?, Notes=?, Checked=CURDATE() WHERE Description=?;";
        try { returnData = wc.q2do1c(dbc, query_FBook_ATrackUp, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
    private JSONArray assetTrack(Connection dbc) {
        final String query_FBook_ATrackPrep1 = "UPDATE Core.FB_Assets SET Value=(SELECT sum(Quantity)*7 FROM Core.BGames), Checked=CURDATE() WHERE Related='BGames';";
        final String query_FBook_ATrackPrep2 = "UPDATE Core.FB_Assets SET Value=(SELECT sum(Quantity)*7 FROM Core.Books), Checked=CURDATE() WHERE Related='Books';";
        final String query_FBook_ATrackPrep3 = "UPDATE Core.FB_Assets SET Value=(SELECT sum(Quantity)*7 FROM Core.DecorTools), Checked=CURDATE() WHERE Related='DecorTools';";
        final String query_FBook_ATrackPrep4 = "UPDATE Core.FB_Assets SET Value=(SELECT sum(Count)*7 FROM Core.Licenses), Checked=CURDATE() WHERE Related='Licenses';";
        final String query_FBook_ATrackPrep5 = "UPDATE Core.FB_Assets SET Value=(SELECT sum(Asset)*0.99 FROM Core.MediaServer), Checked=CURDATE() WHERE Related='Media';";
        final String query_FBook_ATrackPrep6 = "UPDATE Core.FB_Assets " +
        		" SET Value=(((SELECT SUM(Value) FROM Core.FFXIV_Assets) + (SELECT Gil FROM Core.FFXIV_Gil ORDER BY AsOf DESC LIMIT 1))" +
        		" * (SELECT ValueUSD FROM Core.FFXIV_GilExchangeRate ORDER BY Date DESC LIMIT 1)/1000000)," +
        		" Notes=CONCAT('Exchange rate ',(SELECT ValueUSD FROM Core.FFXIV_GilExchangeRate ORDER BY Date DESC LIMIT 1),' per milion')," +
        		" Checked=CURDATE()" +
        		" WHERE Related='FFXIV';";
        final String query_FBook_ATrack = "SELECT Description, Type, Category, Value, Checked, PendingDonation," +
                " Serial, UPC, Related, Location, Notes, NetWorthType FROM Core.FB_Assets ORDER BY Type, Category, Description;";
        JSONArray tContainer = new JSONArray();
        try { String rsA = wc.q2do1c(dbc, query_FBook_ATrackPrep1, null); } catch (Exception e) { e.printStackTrace(); }
        try { String rsB = wc.q2do1c(dbc, query_FBook_ATrackPrep2, null); } catch (Exception e) { e.printStackTrace(); }
        try { String rsC = wc.q2do1c(dbc, query_FBook_ATrackPrep3, null); } catch (Exception e) { e.printStackTrace(); }
        try { String rsD = wc.q2do1c(dbc, query_FBook_ATrackPrep4, null); } catch (Exception e) { e.printStackTrace(); }
        try { String rsE = wc.q2do1c(dbc, query_FBook_ATrackPrep5, null); } catch (Exception e) { e.printStackTrace(); }
        try { String rsF = wc.q2do1c(dbc, query_FBook_ATrackPrep6, null); } catch (Exception e) { e.printStackTrace(); }
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
                    .put("Notes", resultSet.getString("Notes"))
                    .put("NetWorthType", resultSet.getString("NetWorthType"))
                    .put("PendingDonation", resultSet.getInt("PendingDonation"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    private JSONArray bGames(Connection dbc) {
        final String query_FBook_BGames = "SELECT Title, Quantity, PendingDonation FROM Core.BGames ORDER BY Title;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_FBook_BGames, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Title", resultSet.getString("Title"))
                    .put("Quantity", resultSet.getDouble("Quantity"))
                    .put("PendingDonation", resultSet.getInt("PendingDonation"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    private JSONArray bills(Connection dbc) {
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
   
    private JSONArray books(Connection dbc) {
        final String query_FBook_Books = "SELECT Title, Quantity, PendingDonation FROM Core.Books ORDER BY Title;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_FBook_Books, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Title", resultSet.getString("Title"))
                    .put("Quantity", resultSet.getDouble("Quantity"))
                    .put("PendingDonation", resultSet.getInt("PendingDonation"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    private String checkbookAdd(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        String query_FBook_CkBkAdd = "INSERT INTO Core.FB_CFCK01 (CTID, Bank, Date, Description, Debit, Credit) VALUES (Null,?,?,?,?,?);";
        try { returnData = wc.q2do1c(dbc, query_FBook_CkBkAdd, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
    private String checkbookUpdate(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        String query_FBook_CkBkUpdate = "UPDATE Core.FB_CFCK01 SET Bank=?, Date=?, Description=?, Debit=?, Credit=? where CTID=?;";
        try { returnData = wc.q2do1c(dbc, query_FBook_CkBkUpdate, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
     
    private JSONArray checking(Connection dbc) { 
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
    
    private JSONArray ckBk(Connection dbc) {
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
    
    private JSONArray ckBkComb(Connection dbc) {
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
        
    private int countyHomeValue(Connection dbc) {
    	int returnData = 0;
        final String query_AutoMaint = "SELECT Value FROM Core.FB_Assets WHERE Description='# House Assessed Value';";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_AutoMaint, null);
            while (resultSet.next()) { 
                returnData = resultSet.getInt("Value");
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }   
    
    private JSONArray decorTools(Connection dbc) {
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
    
    private String decorToolsUpdate(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        String query_FBook_DecorToolsUpdate = "UPDATE Core.DecorTools SET Quantity=?, Location=?, Checked=CURDATE() WHERE Description=?;";
        try { returnData = wc.q2do1c(dbc, query_FBook_DecorToolsUpdate, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
    private JSONArray enw(Connection dbc) {
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
    
    private JSONArray enwChart(Connection dbc, String periodLength) {
        String query_ch_ENW = "SELECT " +
                " AsOf, AsLiq, AsFix, Life, Credits, Debts," +
                " ((AsFix + AsLiq + Life + Credits) - Debts) AS Worth" +
                " FROM Core.FB_ENWT";
        switch(periodLength) {
        	case "Year": 
        		query_ch_ENW += " WHERE AsOf BETWEEN CURDATE() - INTERVAL 730 DAY AND CURDATE()" + 
        				" ORDER BY AsOf LIMIT 730;";
        		break;
        	case "All": default:
        		query_ch_ENW += " WHERE (AsOf LIKE '%1'" +
		                " OR AsOf LIKE '%6')" +
		                " ORDER BY AsOf;";
	        	break;
        }
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_ch_ENW, null);
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                tObject
                        .put("AsOf", resultSet.getString("AsOf"))
                        .put("Worth", resultSet.getDouble("Worth"))
                        .put("AsLiq", resultSet.getDouble("AsLiq"))
                        .put("AsFix", resultSet.getDouble("AsFix"))
                        .put("Life", resultSet.getDouble("Life"))
                        .put("Credits", resultSet.getDouble("Credits"))
                        .put("Debts", resultSet.getDouble("Debts"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    private JSONArray enwt(Connection dbc) {
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
    
    private JSONArray eTradeBalance(Connection dbc) {
        final String query_etb = "SELECT SUM((SELECT Value FROM Core.FB_Assets WHERE Description LIKE 'eTrade%') + (SELECT SUM(Count*LastValue) FROM Core.StockShares WHERE Holder='eTrade')) AS Balance;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_etb, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject
                    .put("Balance", resultSet.getDouble("Balance"));
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    	
    }
    
    private JSONArray licenses(Connection dbc) {
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
    
    private JSONArray mort(Connection dbc) {
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
    
    private JSONArray mortDumpFund(Connection dbc) { 
        final String query_FBook_Checking = "SELECT SUM(Value) AS Value FROM Core.FB_Assets WHERE Description LIKE '%CD%';";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_FBook_Checking, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject.put("Value", resultSet.getInt("Value"));
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }
    
    private JSONArray netWorth3(Connection dbc) {
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
    
    private JSONArray nwga(Connection dbc) {
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
    
    private JSONArray qMerged(Connection dbc) {
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
    
    private JSONArray saving(Connection dbc) {
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
    
    private JSONArray savingChart(Connection dbc, List<String> qParams) {
        final LocalDate nowDate = LocalDate.parse(wc.getNowDate(), java.time.format.DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss"));
        final LocalDate eomDate = nowDate.withDayOfMonth(nowDate.getMonth().length(nowDate.isLeapYear()));
        final java.time.format.DateTimeFormatter parseFormat = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd");
        final String finalEomString = eomDate.format(parseFormat);
        final DateTimeFormatter dtFormat = DateTimeFormat.forPattern("yyyy-MM-dd");
        final String startDateString = "2013-01-01";
        final DateTime startDateTime = dtFormat.parseDateTime(startDateString);
        final DateTime endDateTime = dtFormat.parseDateTime(finalEomString);
        final Months monthPeriod = Months.monthsBetween(startDateTime, endDateTime);
        JSONArray tContainer = new JSONArray();
        for(int i = 0; i < monthPeriod.getMonths(); i++) {
            List<String> intQParams = new ArrayList<>();
            final DateTime thisMonthEnd = startDateTime.plusMonths((i+1));
            final String thisMonthEndString = dtFormat.print(thisMonthEnd);
            intQParams.add(0, thisMonthEndString);
            intQParams.add(1, thisMonthEndString);
            final String query_ch_Saving_Opt = "SELECT SUM(Credit-Debit) AS Value, ? AS Date FROM FB_CFSV59 WHERE Date <= ?;";        
            try {
                ResultSet resultSet = wc.q2rs1c(dbc, query_ch_Saving_Opt, intQParams);
                while (resultSet.next()) { 
                    JSONObject tObject = new JSONObject();
                    tObject
                        .put("Date", resultSet.getString("Date"))
                        .put("Value", resultSet.getDouble("Value"));
                    tContainer.put(tObject);
                }
                resultSet.close();
            } catch (Exception e) { e.printStackTrace(); }
        }
        return tContainer;
    }
    
    private String savingsAdd(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        String query_FBook_SvBkAdd = "INSERT INTO FB_CFSV59 VALUES (Null,?,?,?,?);";
        try { returnData = wc.q2do1c(dbc, query_FBook_SvBkAdd, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
    private JSONArray settingC(Connection dbc) {
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
    
    private JSONArray settingH(Connection dbc) {
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
    
    private JSONArray svBk(Connection dbc) {
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

    private String zillowDailyUpdate(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        String query_ZillowUpdate = "INSERT INTO Feeds.ZillowEstimates (Date, jsonData) VALUES (CURDATE(), ?);";
        try { returnData = wc.q2do1c(dbc, query_ZillowUpdate, qParams); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
    private String zillowHomeValue(Connection dbc, String zestimate) {
    	int assessedValue = 0;
    	int zestimateDifference = 0;
    	try { assessedValue = countyHomeValue(dbc); } catch (Exception e) { e.printStackTrace(); }
    	try { zestimateDifference = Integer.parseInt(zestimate) - assessedValue; } catch (Exception e) { e.printStackTrace(); }
        String returnData = wcb.getDefaultNotRanYet();
        String query_ZillowHomeUpdate = "UPDATE Core.FB_Assets SET Value="+zestimateDifference+", Checked=CURDATE() WHERE Description='# Zillow House Adjustment';";
        try { returnData = wc.q2do1c(dbc, query_ZillowHomeUpdate, null); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
    private JSONArray stockList(Connection dbc) { 
        final String query_GetStocks = "SELECT Symbol, Count, Holder, LastValue, Description, PreviousClose FROM Core.StockShares WHERE Active=1;";
        JSONArray tContainer = new JSONArray();
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, query_GetStocks, null);
            while (resultSet.next()) { 
                JSONObject tObject = new JSONObject();
                tObject
                	.put("Symbol", resultSet.getString("Symbol"))
                	.put("Count", resultSet.getInt("Count"))
                	.put("Holder", resultSet.getString("Holder"))
                	.put("LastValue", resultSet.getString("LastValue"))
                	.put("Description", resultSet.getString("Description"))
                	.put("PreviousClose", resultSet.getString("PreviousClose"));
                tContainer.put(tObject);
            }
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }

    private JSONArray stockListPublic(Connection dbc) { 
        final String query_GetStocks = "SELECT Symbol, LastValue, Description, PreviousClose FROM Core.StockShares WHERE Active=1;";
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

    private String stockUpdate(Connection dbc, List<String> qParams) {
        String returnData = wcb.getDefaultNotRanYet();
        String query_UpdateStock = "UPDATE Core.StockShares SET LastValue=?, PreviousClose=? WHERE Symbol=?;";
        String query_UpdateStockB = "UPDATE Core.FB_Assets SET Value=(SELECT SUM(Count*LastValue) FROM Core.StockShares), Checked=CURDATE() WHERE Description='Stocks';";
        try { returnData = wc.q2do1c(dbc, query_UpdateStock, qParams); } catch (Exception e) { e.printStackTrace(); }
        try { returnData = wc.q2do1c(dbc, query_UpdateStockB, null); } catch (Exception e) { e.printStackTrace(); }
        return returnData;
    }
    
	private JSONArray zillowPIDs(Connection dbc) {
		final String query_GetZPIDs = "SELECT Who, Address, ZPID, MyAsset FROM Core.FB_CompHomeVals WHERE ZPID IS NOT null;";
		JSONArray tContainer = new JSONArray();
		try {
			ResultSet resultSet = wc.q2rs1c(dbc, query_GetZPIDs, null);
			while(resultSet.next()) {
				JSONObject tObject = new JSONObject();
				tObject
					.put("Who", resultSet.getString("Who"))
					.put("Address", resultSet.getString("Address"))
					.put("ZPID", resultSet.getString("ZPID"))
					.put("MyAsset", resultSet.getInt("MyAsset"));
				tContainer.put(tObject);
			}
			resultSet.close();
		} catch (Exception e) { e.printStackTrace(); }
		return tContainer;
	}				
     
    public JSONArray get3NetWorth(Connection dbc) { return netWorth3(dbc); }
    public JSONArray getAmSch(Connection dbc) { return amSch(dbc); }
    public JSONArray getAssetTrack(Connection dbc) { return assetTrack(dbc); }
    public JSONArray getBGames(Connection dbc) { return bGames(dbc); }
    public JSONArray getBills(Connection dbc) { return bills(dbc); }
    public JSONArray getBooks(Connection dbc) { return books(dbc); }
    public JSONArray getChecking(Connection dbc) { return checking(dbc); }
    public JSONArray getCkBk(Connection dbc) { return ckBk(dbc); }
    public JSONArray getCkBkComb(Connection dbc) { return ckBkComb(dbc); }
    public int getCountyHomeValue(Connection dbc) { return countyHomeValue(dbc); }
    public JSONArray getDecorTools(Connection dbc) { return decorTools(dbc); }
    public JSONArray getEnw(Connection dbc) { return enw(dbc); }
    public JSONArray getEnwChart(Connection dbc, String periodLength) { return enwChart(dbc, periodLength); }
    public JSONArray getEnwt(Connection dbc) { return enwt(dbc); }
    public JSONArray getETradeBalance(Connection dbc) { return eTradeBalance(dbc); }
    public JSONArray getLicenses(Connection dbc) { return licenses(dbc); }
    public JSONArray getMort(Connection dbc) { return mort(dbc); }
    public JSONArray getMortDumpFund(Connection dbc) { return mortDumpFund(dbc); }
    public JSONArray getNwga(Connection dbc) { return nwga(dbc); }
    public JSONArray getQMerged(Connection dbc) { return qMerged(dbc); }
    public JSONArray getSaving(Connection dbc) { return saving(dbc); }
    public JSONArray getSavingChart(Connection dbc, List<String> qParams) { return savingChart(dbc, qParams); }
    public JSONArray getSettingC(Connection dbc) { return settingC(dbc); }
    public JSONArray getSettingH(Connection dbc) { return settingH(dbc); }
    public JSONArray getStockHistory(Connection dbc) { return stockHistory(dbc); }
    public JSONArray getStockList(Connection dbc) { return stockList(dbc); }
    public JSONArray getStockListPublic(Connection dbc) { return stockListPublic(dbc); }
    public JSONArray getSvBk(Connection dbc) { return svBk(dbc); }
	public JSONArray getZillowPIDs(Connection dbc) { return zillowPIDs(dbc); }
	public String setAssetTrackUpdate(Connection dbc, List<String> qParams) { return assetTrackUpdate(dbc, qParams); }
    public String setCheckbookAdd(Connection dbc, List<String> qParams) { return checkbookAdd(dbc, qParams); }
    public String setCheckbookUpdate(Connection dbc, List<String> qParams) { return checkbookUpdate(dbc, qParams); }
    public String setDecorToolsUpdate(Connection dbc, List<String> qParams) { return decorToolsUpdate(dbc, qParams); }
    public String setSavingsAdd(Connection dbc, List<String> qParams) { return savingsAdd(dbc, qParams); }
    public String setStockIndex(Connection dbc, List<String> qParams) { return stockIndex(dbc, qParams); }
    public String setStockUpdate(Connection dbc, List<String> qParams) { return stockUpdate(dbc, qParams); }
    public String setZillowDailyUpdate(Connection dbc, List<String> qParams) { return zillowDailyUpdate(dbc, qParams); }
    public String setZillowHomeValue(Connection dbc, String zestimate) { return zillowHomeValue(dbc, zestimate); }
    
}
