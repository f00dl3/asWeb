/*
by Anhony Stump
Created: 14 Aug 2017
Updated: 28 Dec 2019
*/

package asUtilsPorts;

import asUtils.Secure.JunkyPrivate;
import asUtilsPorts.Feed.CF6Daily;
import asWebRest.shared.WebCommon;

import java.sql.*;

public class GetDaily {

	public static String getDaily(Connection dbc, int daysBack) {
		
		String returnData = "";
		
		returnData = CF6Daily.getCf6(dbc, daysBack);

        JunkyPrivate junkyPrivate = new JunkyPrivate();
        WebCommon wc = new WebCommon();
		
		String anwPrepSQLQuery = "SET @runtot := "+junkyPrivate.getMortBeginningBalance()+";";
		
		String autoNetWorthSQLQuery = "REPLACE INTO FB_ENWT ("
			+ "AsOf, AsLiq, AsFix, Life, Credits, Debts, Auto, AsLiqCA, AsLiqNV,"
			+ " AsFixHM, AsFixAU, AsFixDF, AsFixFT, AsFixEL, AsFixJC, AsFixKT, AsFixMD, AsFixTL,"
			+ " AsFixPT, AsFixUN, AsFixTR"
			+ ") VALUES ("
			+ "current_date,(SELECT SUM("
			+ "(SELECT FORMAT(SUM(Value)/1000,1) FROM FB_Assets WHERE Category IN ('NV','CA')) +"
			+ "(SELECT FORMAT(SUM(Credit-Debit)/1000,1) FROM FB_CFCK01 WHERE Date <= current_date) +"
			+ "(SELECT FORMAT(SUM(Credit-Debit)/1000,1) FROM FB_CFSV59 WHERE Date <= current_date))),"
			+ "(SELECT FORMAT(SUM(Value)/1000,1) FROM FB_Assets WHERE Type = 'F'),"
			+ "(SELECT FORMAT((SUM(Value)/1000),1) FROM FB_Assets WHERE Category = 'LI'),"
			+ "(SELECT FORMAT((SUM(Value)/1000),1) FROM FB_Assets WHERE Category = 'CR'),"
			+ "(SELECT FORMAT(MIN(@runtot := @runtot + (@runtot * (("+junkyPrivate.getMortIntRate()+"/12)/100)) - (Extra + "+junkyPrivate.getMortBaseMonthly()+"))/1000,1) AS MBal FROM FB_WFML35 WHERE DueDate < current_date + interval '30' day),1,"
			+ "(SELECT FORMAT((SUM(Value)/1000),1) FROM FB_Assets WHERE Category = 'CA'),"
			+ "(SELECT FORMAT((SUM(Value)/1000),1) FROM FB_Assets WHERE Category = 'NV'),"
			+ "(SELECT FORMAT((SUM(Value)/1000),1) FROM FB_Assets WHERE Category = 'HM'),"
			+ "(SELECT FORMAT((SUM(Value)/1000),1) FROM FB_Assets WHERE Category = 'AU'),"
			+ "(SELECT FORMAT((SUM(Value)/1000),1) FROM FB_Assets WHERE Category = 'DF'),"
			+ "(SELECT FORMAT((SUM(Value)/1000),1) FROM FB_Assets WHERE Category = 'FT'),"
			+ "(SELECT FORMAT((SUM(Value)/1000),1) FROM FB_Assets WHERE Category = 'EL'),"
			+ "(SELECT FORMAT((SUM(Value)/1000),1) FROM FB_Assets WHERE Category = 'JC'),"
			+ "(SELECT FORMAT((SUM(Value)/1000),1) FROM FB_Assets WHERE Category = 'KT'),"
			+ "(SELECT FORMAT((SUM(Value)/1000),1) FROM FB_Assets WHERE Category = 'MD'),"
			+ "(SELECT FORMAT((SUM(Value)/1000),1) FROM FB_Assets WHERE Category = 'TL'),"
			+ "(SELECT FORMAT((SUM(Value)/1000),1) FROM FB_Assets WHERE Category = 'PT'),"
			+ "(SELECT FORMAT((SUM(Value)/1000),1) FROM FB_Assets WHERE Category = 'UN'),"
			+ "(SELECT FORMAT((SUM(Value)/1000),1) FROM FB_Assets WHERE Category = 'TR')"
			+ ");";
		
        String ffxivGilQuery = "INSERT INTO FFXIV_GilByDate (Gil) VALUES " +
                    "((SELECT SUM(Value) FROM Core.FFXIV_Assets) + (SELECT Gil FROM Core.FFXIV_Gil ORDER BY AsOf DESC LIMIT 1));";
        
        try { wc.q2do1c(dbc, anwPrepSQLQuery, null); } catch (Exception e) { e.printStackTrace(); }
        try { wc.q2do1c(dbc, autoNetWorthSQLQuery, null); } catch (Exception e) { e.printStackTrace(); }
        try { wc.q2do1c(dbc, ffxivGilQuery, null); } catch (Exception e) { e.printStackTrace(); }

        return returnData;
        
	}

}
