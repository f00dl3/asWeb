/*
by Anthony Stump
Models 2020 core class Tomcat Port

Created: 22 Sep 2017
Updated: 8 Jan 2020
Status: UNTESTED on Tomcat
*/

package asUtilsPorts;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import asUtilsPorts.Weather.ModelBeans;
import asUtilsPorts.Weather.ModelImageOps;
import asUtilsPorts.Weather.ModelShare;
import asUtilsPorts.Weather.ModelWorker;
import asWebRest.shared.MyDBConnector;
import asWebRest.shared.ThreadRipper;
import asWebRest.shared.WebCommon;

public class Models {

	public static void main(String[] args) {

        ModelBeans modelBeans = new ModelBeans();
        ModelShare wms = new ModelShare();
        ModelImageOps mio = new ModelImageOps();
        ModelWorker mw = new ModelWorker();
        MyDBConnector mdb = new MyDBConnector();
        ThreadRipper tr = new ThreadRipper();
        WebCommon wc = new WebCommon();
        
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
		final DateTime tDateTime = new DateTime().minusHours(2);
		final DateTimeFormatter getDateFormat = DateTimeFormat.forPattern("yyyyMMdd");
		final String getHour = args[0];
		final String getDate = getDateFormat.print(tDateTime);
		final String modelRunString = getDate+"_"+getHour+"Z";
		final File imgOutPath = modelBeans.getImgOutPath();
		final File xml2Path = modelBeans.getXml2Path();
		final String wwwBase = wms.get_wwwBase(true).toString();
		final File mSQLDebugDumpFile = new File(xml2Path.getPath()+"/mSQLDebugDump.json");

		wc.deleteDir(xml2Path);
		imgOutPath.mkdirs();

		ArrayList<Runnable> pool = new ArrayList<Runnable>();
		pool.add(() -> mw.main(getHour, "1"));
		pool.add(() -> mw.main(getHour, "2"));
		pool.add(() -> mw.main(getHour, "3"));
		pool.add(() -> mw.main(getHour, "4"));
		pool.add(() -> mw.main(getHour, "5"));
		pool.add(() -> mw.main(getHour, "6"));
		pool.add(() -> mw.main(getHour, "7"));
		pool.add(() -> mw.main(getHour, "8"));
		pool.add(() -> mw.main(getHour, "9"));
		pool.add(() -> mw.main(getHour, "10"));
		pool.add(() -> mw.main(getHour, "11"));
		pool.add(() -> mw.main(getHour, "12"));
		pool.add(() -> mw.main(getHour, "13"));
		pool.add(() -> mw.main(getHour, "14"));
		pool.add(() -> mw.main(getHour, "15"));
		pool.add(() -> mw.main(getHour, "16"));
		pool.add(() -> mw.main(getHour, "17"));
		pool.add(() -> mw.main(getHour, "18"));
		pool.add(() -> mw.main(getHour, "19"));
		pool.add(() -> mw.main(getHour, "20"));
		pool.add(() -> mw.main(getHour, "21"));
		pool.add(() -> mw.main(getHour, "22"));
		pool.add(() -> mw.main(getHour, "23"));
		pool.add(() -> mw.main(getHour, "24"));
		pool.add(() -> mw.main(getHour, "25"));
		pool.add(() -> mw.main(getHour, "26"));
		pool.add(() -> mw.main(getHour, "27"));
		tr.runProcesses(pool, false, false);		

		mio.main(getHour, xml2Path, wwwBase);
		
		String mSQLIndex = null;
		String mSQLQuery = null;
		
		String hrrrJSONString = wms.jsonMerge("hrrr");
		
		if(getHour.equals("03") || getHour.equals("09") || getHour.equals("15") || getHour.equals("21")) {
			String srfaJSONString = wms.jsonMerge("srfa");
			String srfnJSONString = wms.jsonMerge("srfn");
			mSQLIndex = "INSERT INTO WxObs.MOS_Index (RunString, GFS, NAM4KM, RAP, CMC, HRRR, HRWA, HRWN, SRFA, SRFN) VALUES ('"+modelRunString+"',0,0,0,0,1,0,0,1,1);";
			mSQLQuery = "INSERT INTO WxObs.KOJC_MFMD (RunString, HRRR, SRFA, SRFN) VALUES ('"+modelRunString+"','"+hrrrJSONString+"','"+srfaJSONString+"','"+srfnJSONString+"');";
		} else if(getHour.equals("00") || getHour.equals("06") || getHour.equals("12") || getHour.equals("18")) {
			String gfsJSONString = wms.jsonMerge("gfs");
			String namJSONString = wms.jsonMerge("nam");
			if(getHour.equals("00") || getHour.equals("12")) {
				String cmcJSONString = wms.jsonMerge("cmc");
				String hrwaJSONString = wms.jsonMerge("hrwa");
				String hrwnJSONString = wms.jsonMerge("hrwn");
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

}
