/*
XML Stations 2019 - Core Process 
Created: 4 Sep 2016
Updated: 28 Dec 2019
*/

package asUtilsPorts;

import asUtils.Shares.StumpJunk;
import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import asUtils.Shares.MyDBConnector;
import asUtils.Shares.JunkyBeans;
import asUtils.Model.ModelBeans;
import asUtils.xs.xsImageOp;
import asUtils.xs.xsMETARAutoAdd;
import asUtils.xs.xsWorkerBasic;
import asUtils.xs.xsWorkerBouy;
import asUtils.xs.xsWorkerFull_19;
import asUtils.xs.xsWorkerHydro;
import asUtils.xs.xsWorkerMETARStream;
import asUtils.xs.xsWorkerWunder;
import asWebRest.shared.CommonBeans;

public class xs19 {

	public static void main(String[] args) {
            
        final boolean debugMode = false;

        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }

        CommonBeans cb = new CommonBeans();
        JunkyBeans junkyBeans = new JunkyBeans();
        ModelBeans modelBeans = new ModelBeans();
    
		final long startTime = System.currentTimeMillis();

		final File ramDrive = new File(cb.getRamPath());
		final String xsTmp = ramDrive.getPath()+"/xsTmpJ_19";
		final String tFHour2D = "02";
		final String tFHour4D = "0002";
		final String tSFHour2D = "03";
		final String tSFHour4D = "0003";
		final DateTime tDateTime = new DateTime(DateTimeZone.UTC).minusHours(2);
		final DateTimeFormatter getHourFormat = DateTimeFormat.forPattern("HH");
		final DateTimeFormatter getHourMinFormat = DateTimeFormat.forPattern("HHmm");
		final DateTimeFormatter getDateFormat = DateTimeFormat.forPattern("yyyyMMdd");
		final String getHour = getHourFormat.print(tDateTime);
		final String getHourMin = getHourMinFormat.print(tDateTime);
		final String getDate = getDateFormat.print(tDateTime);
		final File gradsOutObj = new File(xsTmp+"/grib2/iOut");
		final File helpers = junkyBeans.getHelpers();
		final File jsonDebugDumpFile = new File(xsTmp+"/dbgBigString.json");
		final File jsonDebugDumpRapidFile = new File(xsTmp+"/dbgRapidString.json");
		final File logFile = new File(xsTmp+"/xs19.log");
		final File metarsZipFile = new File(xsTmp+"/metars.xml.gz");
		final File nwsObsXMLzipFile = new File(xsTmp+"/index.zip");
		final File wwwOutObj = new File(cb.getPersistTomcat()+"/G2Out/xsOut");
		final File xsTmpObj = new File(xsTmp);
		final String gVarsSQL = "SELECT gVar FROM WxObs.gradsOutType WHERE Active=1;";
		final String gVarsHSQL = "SELECT gVar FROM WxObs.gradsOutType WHERE Active=1 AND HighRes=1;";
		final String gVarsLSQL = "SELECT gVar FROM WxObs.gradsOutType WHERE Active=1 AND HighRes=0;";
		final String resHigh = "13068x6600";
		final String resLow = "2904x1440";
		final String appPath = junkyBeans.getAppShareSys().toString();
		final String[] xwuWorkerArgs = { xsTmp, "None" };
		List<String> gVars = new ArrayList<String>();
		List<String> gVarsH = new ArrayList<String>();
		List<String> gVarsL = new ArrayList<String>();
		boolean rapidRefresh = false;
		boolean onlyWunder = false;
		boolean subHour = false;
		File hrrrCtlFile = new File(xsTmp+"/grib2/HRRR.ctl");
		File hrrrCtlSubFile = new File(xsTmp+"/grib2/HRRRsub.ctl");
		File hrrrGrib2File = new File(xsTmp+"/grib2/HRRR");
		File hrrrGrib2SubFile = new File(xsTmp+"/grib2/HRRRsub");
		String subHourFlag = "no";
		
        gradsOutObj.mkdirs();
        wwwOutObj.mkdirs();

		if(args.length > 0 && args[0].equals("Rapid")) { rapidRefresh = true; subHour = true; subHourFlag = "yes"; onlyWunder = false; }
		if(args.length > 0 && args[0].equals("Wunder")) { rapidRefresh = true; subHour = false; subHourFlag = "no"; onlyWunder = true; }

		System.out.println(" -> DEBUG: (String) getHour = "+getHour);
		System.out.println(" -> DEBUG: (String) getHourMin = "+getHourMin);
		System.out.println(" -> DEBUG: (String) getDate = "+getDate);
                
		if(!onlyWunder) {
			
			/* URLs! */
			String hrrrGrib2URL = modelBeans.getNomadsBase() + "/hrrr/prod/hrrr."+getDate+"/conus/hrrr.t"+getHour+"z.wrfprsf"+tFHour2D+".grib2";
			if(subHour) {
				hrrrCtlFile = hrrrCtlSubFile;
				hrrrGrib2URL = modelBeans.getNomadsBase() + "/hrrr/prod/hrrr."+getDate+"/conus/hrrr.t"+getHour+"z.wrfsubhf"+tSFHour2D+".grib2";
				hrrrGrib2File = hrrrGrib2SubFile;
			}
			final String metarsURL = "http://aviationweather.gov/adds/dataserver_current/current/metars.cache.xml.gz";
			final String xmlObsURL = "http://w1.weather.gov/xml/current_obs/all_xml.zip";

			if(!rapidRefresh) {
				StumpJunk.deleteDir(xsTmpObj);
				xsTmpObj.mkdirs();
			}
		
            try ( ResultSet resultSetGVars = mdb.q2rs1c(dbc, gVarsSQL, null); ) {
                    while (resultSetGVars.next()) { gVars.add(resultSetGVars.getString("gVar")); }
            } catch (Exception e) { e.printStackTrace(); }

            try ( ResultSet resultSetGVarsH = mdb.q2rs1c(dbc, gVarsHSQL, null); ) {
                    while (resultSetGVarsH.next()) { gVarsH.add(resultSetGVarsH.getString("gVar")); }
            } catch (Exception e) { e.printStackTrace(); }

            try ( ResultSet resultSetGVarsL = mdb.q2rs1c(dbc, gVarsLSQL, null); ) {
                    while (resultSetGVarsL.next()) { gVarsL.add(resultSetGVarsL.getString("gVar")); }
            } catch (Exception e) { e.printStackTrace(); }

            for (String thisGVar : gVars) {
                    File thisGVarPath = new File(gradsOutObj.getPath()+"/"+thisGVar);
                    File thisGVarWPath = new File(wwwOutObj.getPath()+"/"+thisGVar);
                    thisGVarPath.mkdirs();
                    thisGVarWPath.mkdirs();
            }
                        
			StumpJunk.jsoupOutBinary(hrrrGrib2URL, hrrrGrib2File, modelBeans.getDownloadTimeout());

			StumpJunk.runProcess("(\""+appPath+"/wgrib2\" "+hrrrGrib2File.getPath()+" -pdt | egrep -v \"^600:\" | \""+appPath+"/wgrib2\" -i "+hrrrGrib2File.getPath()+" -grib "+hrrrGrib2File.getPath()+")");
			StumpJunk.runProcess("(\""+appPath+"/g2ctl\" "+hrrrGrib2File.getPath()+" > "+hrrrCtlFile.getPath()+")");
			StumpJunk.runProcess("\""+appPath+"/gribmap\" -v -i "+hrrrCtlFile.getPath());
			
			Thread d1a = new Thread(() -> { StumpJunk.jsoupOutBinary(xmlObsURL, nwsObsXMLzipFile, 15.0); });
			Thread d1b = new Thread(() -> { StumpJunk.jsoupOutBinary(metarsURL, metarsZipFile, 15.0); });
			Thread dList1[] = { d1a, d1b };
			for (Thread thread : dList1) { thread.start(); }
			for (int i = 0; i < dList1.length; i++) { try { dList1[i].join(); } catch (InterruptedException nx) { nx.printStackTrace(); } }

			Thread d2a = new Thread(() -> { StumpJunk.unzipFile(nwsObsXMLzipFile.getPath(), xsTmp); });
			Thread d2b = new Thread(() -> { StumpJunk.runProcess("gunzip \""+metarsZipFile.getPath()+"\""); });
			Thread dList2[] = { d2a, d2b };
			for (Thread thread : dList2) { thread.start(); }
			for (int i = 0; i < dList2.length; i++) { try { dList2[i].join(); } catch (InterruptedException nx) { nx.printStackTrace(); } }
	 
			final String[] addMETARStationArgs = { xsTmp };
			final String[] xfWorker1Args = { xsTmp, "USC" }; 
			final String[] xfWorker2Args = { xsTmp, "USE" }; 
			final String[] xfWorker3Args = { xsTmp, "USW" };
			final String[] xbWorkerArgs = { xsTmp, "None" };
			final String[] xmWorkerSArgs = { xsTmp, "None" }; 
			final String[] xwbWorkerArgs = { xsTmp, "None" };
			final String[] xwhWorkerArgs = { xsTmp, "None" };
                        
            if(debugMode) {

                System.out.println("DEBUG MODE ON!");
                xsWorkerFull_19.main(xfWorker1Args, true);

            } else {

                StumpJunk.runProcess("(echo \"run "+helpers.getPath()+"/xsGraphics.gs "+getDate+" "+getHourMin+" "+gradsOutObj.getPath()+" "+subHourFlag+"\" | \""+appPath+"/grads\" -blc \"open "+hrrrCtlFile.getPath()+"\" &>> "+logFile.getPath()+")");
                for (String gVar : gVarsH) { StumpJunk.runProcess("convert \""+gradsOutObj.getPath()+"/"+gVar+"/"+getDate+"_"+getHourMin+"_"+gVar+".png\" -gravity Center -crop "+resHigh+"+0+0 "+gradsOutObj.getPath()+"/"+gVar+"/"+getDate+"_"+getHourMin+"_"+gVar+".png"); }
                for (String gVar : gVarsL) { StumpJunk.runProcess("convert \""+gradsOutObj.getPath()+"/"+gVar+"/"+getDate+"_"+getHourMin+"_"+gVar+".png\" -gravity Center -crop "+resLow+"+0+0 "+gradsOutObj.getPath()+"/"+gVar+"/"+getDate+"_"+getHourMin+"_"+gVar+".png"); }

                final String[] xsImageOpArgs = { xsTmp }; xsImageOp.main(xsImageOpArgs, gVars, gVarsH, gVarsL);

                xsMETARAutoAdd.main(addMETARStationArgs);

                Thread xs1 = new Thread(() -> { xsWorkerFull_19.main(xfWorker1Args, false); });
                Thread xs2 = new Thread(() -> { xsWorkerFull_19.main(xfWorker2Args, false); });
                Thread xs3 = new Thread(() -> { xsWorkerFull_19.main(xfWorker3Args, false); });
                Thread xs4 = new Thread(() -> { xsWorkerBasic.main(xbWorkerArgs); });
                Thread xs5 = new Thread(() -> { xsWorkerMETARStream.main(xmWorkerSArgs); });
                Thread xs6 = new Thread(() -> { xsWorkerBouy.main(xwbWorkerArgs); });
                Thread xs7 = new Thread(() -> { xsWorkerHydro.main(xwhWorkerArgs); });
                Thread xs8 = new Thread(() -> { xsWorkerWunder.main(xwuWorkerArgs); });
                Thread xsPool[] = { xs1 /*, xs2, xs3, xs4, xs5, xs6, xs7, xs8 */};
                for (Thread thread : xsPool) { thread.start(); }
                for (int i = 0; i < xsPool.length; i++) { try { xsPool[i].join(); } catch (InterruptedException nx) { nx.printStackTrace(); } }
                
                String jsonBigString = null;
                try { jsonBigString = StumpJunk.runProcessOutVar("cat "+xsTmp+"/output_*.json"); } catch (IOException ix) { ix.printStackTrace(); }
                jsonBigString = ("{"+jsonBigString+"}").replace("\n","").replace(",}", "}");
                try { StumpJunk.varToFile(jsonBigString, jsonDebugDumpFile, false); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
                String jsonBigSQLQuery = "INSERT INTO WxObs.StationDataIndexed (jsonData) VALUES ('"+jsonBigString+"');";
                try { mdb.q2do1c(dbc, jsonBigSQLQuery, null); } catch (Exception e) { e.printStackTrace(); }

            }

		} else {
			
			xsWorkerWunder.main(xwuWorkerArgs);
			
		}
		
		String jsonRapidString = null;
		try { jsonRapidString = StumpJunk.runProcessOutVar("cat "+xsTmp+"/rapid_*.json"); } catch (IOException ix) { ix.printStackTrace(); }
		jsonRapidString = ("{"+jsonRapidString+"}").replace("\n","").replace(",}", "}");
		try { StumpJunk.varToFile(jsonRapidString, jsonDebugDumpRapidFile, false); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
		String jsonRapidSQLQuery = "INSERT INTO WxObs.RapidSDI (jsonData) VALUES ('"+jsonRapidString+"');";
		try { mdb.q2do1c(dbc, jsonRapidSQLQuery, null); } catch (Exception e) { e.printStackTrace(); }

		final long endTime = System.currentTimeMillis();
	
		long totalRunTime = (endTime - startTime)/1000;

		String xsRuntime = "INSERT INTO WxObs.Logs VALUES (Null,"+totalRunTime+");";
		try { StumpJunk.varToFile(xsRuntime, logFile, true); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
		try { mdb.q2do1c(dbc, xsRuntime, null); } catch (Exception e) { e.printStackTrace(); }

		System.out.println("Updates completed! Runtime: "+totalRunTime); 
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
		
	}

}
