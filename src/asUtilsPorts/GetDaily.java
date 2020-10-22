/*
by Anhony Stump
Created: 14 Aug 2017
Updated: 22 Oct 2020
*/

package asUtilsPorts;

import asUtilsPorts.Feed.CF6Daily;
import asUtilsPorts.UbuntuVM.BackThatAssUp;
import asUtilsPorts.Weather.SPCMapDownloader;
import asWebRest.action.UpdateFfxivAction;
import asWebRest.dao.FfxivDAO;
import asWebRest.hookers.EvergyAPIHook;
import asWebRest.hookers.KansasGasHook;
import asWebRest.hookers.ZillowAPIHook;
import asWebRest.secure.JunkyPrivate;
import asWebRest.secure.MortgageBeans;
import asWebRest.shared.WebCommon;

import java.sql.*;

import org.joda.time.DateTime;

public class GetDaily {

	public static String getDaily(Connection dbc, int daysBack) {

		DateTime rightNow = new DateTime();
		MortgageBeans mb = new MortgageBeans();
		
		CF6Daily cf6 = new CF6Daily();
		EvergyAPIHook evergy = new EvergyAPIHook();
        JunkyPrivate junkyPrivate = new JunkyPrivate();
        SPCMapDownloader smd = new SPCMapDownloader();
		UpdateFfxivAction updateFfxivAction = new UpdateFfxivAction(new FfxivDAO());
        WebCommon wc = new WebCommon();
		ZillowAPIHook zapi = new ZillowAPIHook();
		
		String returnData = "";
		
		try { returnData += cf6.getCf6(dbc, daysBack); } catch (Exception e) { e.printStackTrace(); }
		
		String anwPrepSQLQuery = "SET @runtot := "+junkyPrivate.getMortBeginningBalance()+";";
		
		String autoNetWorthSQLQuery = "REPLACE INTO Core.FB_ENWT ("
			+ "AsOf, AsLiq, AsFix, Life, Credits, Debts, Auto, AsLiqCA, AsLiqNV,"
			+ " AsFixHM, AsFixAU, AsFixDF, AsFixFT, AsFixEL, AsFixJC, AsFixKT, AsFixMD, AsFixTL,"
			+ " AsFixPT, AsFixUN, AsFixTR"
			+ ") VALUES ("
			+ "(current_date),(SELECT SUM("
			+ "(SELECT FORMAT(SUM(Value)/1000,1) FROM Core.FB_Assets WHERE Category IN ('NV','CA')) +"
			+ "(SELECT FORMAT(SUM(Credit-Debit)/1000,1) FROM Core.FB_CFCK01 WHERE Date <= current_date) +"
			+ "(SELECT FORMAT(SUM(Credit-Debit)/1000,1) FROM Core.FB_CFSV59 WHERE Date <= current_date))),"
			+ "(SELECT FORMAT(SUM(Value)/1000,1) FROM Core.FB_Assets WHERE Type = 'F'),"
			+ "(SELECT FORMAT((SUM(Value)/1000),1) FROM Core.FB_Assets WHERE Category = 'LI'),"
			+ "(SELECT FORMAT((SUM(Value)/1000),1) FROM Core.FB_Assets WHERE Category = 'CR'),"
			+ "(";
		if(mb.getPayed() == 0) {
			autoNetWorthSQLQuery += "(SELECT FORMAT(MIN(@runtot := @runtot + (@runtot * (("+junkyPrivate.getMortIntRate()+"/12)/100)) - (Extra + "+junkyPrivate.getMortBaseMonthly()+"))/1000,1) AS MBal FROM FB_WFML35 WHERE DueDate < current_date + interval '30' day)";
		} else {
			autoNetWorthSQLQuery += "0.0";
		}
		autoNetWorthSQLQuery += " + (SELECT SUM(ABS(Value/1000)) FROM Core.FB_Assets WHERE Category='DB')),"
			+ " 1,"
			+ "(SELECT FORMAT((SUM(Value)/1000),1) FROM Core.FB_Assets WHERE Category = 'CA'),"
			+ "(SELECT FORMAT((SUM(Value)/1000),1) FROM Core.FB_Assets WHERE Category = 'NV'),"
			+ "(SELECT FORMAT((SUM(Value)/1000),1) FROM Core.FB_Assets WHERE Category = 'HM'),"
			+ "(SELECT FORMAT((SUM(Value)/1000),1) FROM Core.FB_Assets WHERE Category = 'AU'),"
			+ "(SELECT FORMAT((SUM(Value)/1000),1) FROM Core.FB_Assets WHERE Category = 'DF'),"
			+ "(SELECT FORMAT((SUM(Value)/1000),1) FROM Core.FB_Assets WHERE Category = 'FT'),"
			+ "(SELECT FORMAT((SUM(Value)/1000),1) FROM Core.FB_Assets WHERE Category = 'EL'),"
			+ "(SELECT FORMAT((SUM(Value)/1000),1) FROM Core.FB_Assets WHERE Category = 'JC'),"
			+ "(SELECT FORMAT((SUM(Value)/1000),1) FROM Core.FB_Assets WHERE Category = 'KT'),"
			+ "(SELECT FORMAT((SUM(Value)/1000),1) FROM Core.FB_Assets WHERE Category = 'MD'),"
			+ "(SELECT FORMAT((SUM(Value)/1000),1) FROM Core.FB_Assets WHERE Category = 'TL'),"
			+ "(SELECT FORMAT((SUM(Value)/1000),1) FROM Core.FB_Assets WHERE Category = 'PT'),"
			+ "(SELECT FORMAT((SUM(Value)/1000),1) FROM Core.FB_Assets WHERE Category = 'UN'),"
			+ "(SELECT FORMAT((SUM(Value)/1000),1) FROM Core.FB_Assets WHERE Category = 'TR')"
			+ ");";        

        try { zapi.autoZestimates(dbc); } catch (Exception e) { e.printStackTrace(); }
        if(mb.getPayed() == 0) { try { wc.q2do1c(dbc, anwPrepSQLQuery, null); } catch (Exception e) { e.printStackTrace(); } }
        try { wc.q2do1c(dbc, autoNetWorthSQLQuery, null); } catch (Exception e) { e.printStackTrace(); }
        try { updateFfxivAction.setFfxivGilAuto(dbc); } catch (Exception e) { e.printStackTrace(); }        
        try { returnData += evergy.dailyJob(dbc); } catch (Exception e) { e.printStackTrace(); }
	try { returnData += evergy.dailyJob(dbc); } catch (Exception e) { }
        try { smd.getYesterday(); } catch (Exception e) { e.printStackTrace(); }
        
        if(rightNow.dayOfWeek().get() == 1) {
        	KansasGasHook kgs = new KansasGasHook();
        	kgs.writeToDatabase(dbc);
        }
        
        if(rightNow.dayOfMonth().get() == 1 || rightNow.dayOfMonth().get() == 15) {
        	BackThatAssUp btau = new BackThatAssUp();
        	btau.reminder();
        }

        return returnData;
        
	}

}
