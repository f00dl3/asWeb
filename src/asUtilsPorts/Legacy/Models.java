/*
by Anthony Stump
Models core class

Created: 22 Sep 2017
Updated: 8 Feb 2020
Status: Production
DECOM ASAP - LEGACY PORT

Completed: GFS, CMC, HRRR, HRWA, HRWN, NAM, SRFA, SRFN
*/

package asUtilsPorts.Legacy;

import java.io.*;
import java.sql.*;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import asUtilsPorts.Weather.ModelBeans;
import asUtilsPorts.Weather.ModelImageOps;
import asUtilsPorts.Weather.ModelShare;
import asUtilsPorts.Weather.ModelWorker;
import asWebRest.shared.MyDBConnector;
import asWebRest.shared.WebCommon;

public class Models {

	public void initiator(boolean sysProc, String getHour) {

        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
                
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
        ModelBeans modelBeans = new ModelBeans();
        ModelImageOps mio = new ModelImageOps();
        ModelShare ms = new ModelShare();
        ModelWorker mw = new ModelWorker();
        WebCommon wc = new WebCommon();                
        
		final DateTime tDateTime = new DateTime(DateTimeZone.UTC).minusHours(4);
		final DateTimeFormatter getDateFormat = DateTimeFormat.forPattern("yyyyMMdd");
		final String getDate = getDateFormat.print(tDateTime);
		final String modelRunString = getDate+"_"+getHour+"Z";
		final File imgOutPath = modelBeans.getImgOutPath();
		final File xml2Path = new File(ms.get_xsTmp(sysProc));
		final String wwwBase = ms.get_wwwBase(sysProc).toString();
		final File mSQLDebugDumpFile = new File(xml2Path.getPath()+"/mSQLDebugDump.json");

		System.out.println("DEBUG: Paths: \n" +
			"xml2Path: " + xml2Path.toString() + "\n" +
			"wwwBase: " + wwwBase);

		wc.deleteDir(xml2Path);
		imgOutPath.mkdirs();
		
		Thread mw01 = new Thread(() -> { mw.main(getHour, "1"); });
		Thread mw02 = new Thread(() -> { mw.main(getHour, "2"); });
		Thread mw03 = new Thread(() -> { mw.main(getHour, "3"); });
		Thread mw04 = new Thread(() -> { mw.main(getHour, "4"); });
		Thread mw05 = new Thread(() -> { mw.main(getHour, "5"); });
		Thread mw06 = new Thread(() -> { mw.main(getHour, "6"); });
		Thread mw07 = new Thread(() -> { mw.main(getHour, "7"); });
		Thread mw08 = new Thread(() -> { mw.main(getHour, "8"); });
		Thread mw09 = new Thread(() -> { mw.main(getHour, "9"); });
		Thread mw10 = new Thread(() -> { mw.main(getHour, "10"); });
		Thread mw11 = new Thread(() -> { mw.main(getHour, "11"); });
		Thread mw12 = new Thread(() -> { mw.main(getHour, "12"); });
		Thread mw13 = new Thread(() -> { mw.main(getHour, "13"); });
		Thread mw14 = new Thread(() -> { mw.main(getHour, "14"); });
		Thread mw15 = new Thread(() -> { mw.main(getHour, "15"); });
		Thread mw16 = new Thread(() -> { mw.main(getHour, "16"); });
		Thread mw17 = new Thread(() -> { mw.main(getHour, "17"); });
		Thread mw18 = new Thread(() -> { mw.main(getHour, "18"); });
		Thread mw19 = new Thread(() -> { mw.main(getHour, "19"); });
		Thread mw20 = new Thread(() -> { mw.main(getHour, "20"); });
		Thread mw21 = new Thread(() -> { mw.main(getHour, "21"); });
		Thread mw22 = new Thread(() -> { mw.main(getHour, "22"); });
		Thread mw23 = new Thread(() -> { mw.main(getHour, "23"); });
		Thread mw24 = new Thread(() -> { mw.main(getHour, "24"); });
		Thread mw25 = new Thread(() -> { mw.main(getHour, "25"); });
		Thread mw26 = new Thread(() -> { mw.main(getHour, "26"); });
		Thread mw27 = new Thread(() -> { mw.main(getHour, "27"); });
		Thread mwPool[] = { mw01, mw02, mw03, mw04, mw05, mw06, mw07, mw08, mw09, mw10, mw11, mw12, mw13, mw14, mw15, mw16, mw17, mw18, mw19, mw20, mw21, mw22, mw23, mw24, mw25, mw26, mw27 }; 
		for (Thread thread : mwPool) { thread.start(); }
		for (int i = 0; i < mwPool.length; i++) { try { mwPool[i].join(); } catch (InterruptedException nx) { nx.printStackTrace(); } }
		
		mio.main(getHour, xml2Path, wwwBase);
		
		String mSQLIndex = null;
		String mSQLQuery = null;
		
		String hrrrJSONString = ms.jsonMerge("hrrr");
		
		if(getHour.equals("03") || getHour.equals("09") || getHour.equals("15") || getHour.equals("21")) {
			String srfaJSONString = ms.jsonMerge("srfa");
			String srfnJSONString = ms.jsonMerge("srfn");
			mSQLIndex = "INSERT INTO WxObs.MOS_Index (RunString, GFS, NAM4KM, RAP, CMC, HRRR, HRWA, HRWN, SRFA, SRFN) VALUES ('"+modelRunString+"',0,0,0,0,1,0,0,1,1);";
			mSQLQuery = "INSERT INTO WxObs.KOJC_MFMD (RunString, HRRR, SRFA, SRFN) VALUES ('"+modelRunString+"','"+hrrrJSONString+"','"+srfaJSONString+"','"+srfnJSONString+"');";
		} else if(getHour.equals("00") || getHour.equals("06") || getHour.equals("12") || getHour.equals("18")) {
			String gfsJSONString = ms.jsonMerge("gfs");
			String namJSONString = ms.jsonMerge("nam");
			if(getHour.equals("00") || getHour.equals("12")) {
				String cmcJSONString = ms.jsonMerge("cmc");
				String hrwaJSONString = ms.jsonMerge("hrwa");
				String hrwnJSONString = ms.jsonMerge("hrwn");
				mSQLIndex = "INSERT INTO WxObs.MOS_Index (RunString, GFS, NAM4KM, RAP, CMC, HRRR, HRWA, HRWN, SRFA, SRFN) VALUES ('"+modelRunString+"',1,1,0,1,1,1,1,0,0);";
				mSQLQuery = "INSERT INTO WxObs.KOJC_MFMD (RunString, GFS, NAM, CMC, HRRR, HRWA, HRWN) VALUES ('"+modelRunString+"','"+gfsJSONString+"','"+namJSONString+"','"+cmcJSONString+"','"+hrrrJSONString+"','"+hrwaJSONString+"','"+hrwnJSONString+"');";
			} else {
				mSQLIndex = "INSERT INTO WxObs.MOS_Index (RunString, GFS, NAM4KM, RAP, CMC, HRRR, HRWA, HRWN, SRFA, SRFN) VALUES ('"+modelRunString+"',1,1,0,0,1,0,0,0,0);";
				mSQLQuery = "INSERT INTO WxObs.KOJC_MFMD (RunString, GFS, NAM, HRRR) VALUES ('"+modelRunString+"','"+gfsJSONString+"','"+namJSONString+"','"+hrrrJSONString+"');";
			}
		} else {
			mSQLIndex = "INSERT INTO WxObs.MOS_Index (RunString, GFS, NAM4KM, RAP, CMC, HRRR, HRWA, HRWN, SRFA, SRFN) VALUES ('"+modelRunString+"',0,0,0,0,1,0,0,0,0);";
			mSQLQuery = "INSERT INTO WxObs.KOJC_MFMD (RunString, HRRR) VALUES ('"+modelRunString+"','"+hrrrJSONString+"');";
		}
		
		String mSQLDumper = mSQLIndex + "\n" + mSQLQuery;
		
		try { wc.varToFile(mSQLDumper, mSQLDebugDumpFile, false); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
		
		try { wc.q2do1c(dbc, mSQLIndex, null); } catch (Exception e) { e.printStackTrace(); }
		try { wc.q2do1c(dbc, mSQLQuery, null); } catch (Exception e) { e.printStackTrace(); }
                
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
		
	}
	

	public static void main(String[] args) {
		Models models = new Models();
		String getHour = args[0];
		models.initiator(true, getHour);
	}

}
