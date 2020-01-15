/*
Stations - Core Process 
Created: 4 Sep 2016
Updated: 14 Jan 2020
*/

package asUtilsPorts;

import asUtils.Shares.JunkyBeans;
import asUtilsPorts.Weather.ModelBeans;
import asUtilsPorts.Weather.xsImageOp;
import asUtilsPorts.Weather.xsMETARAutoAdd;
import asUtilsPorts.Weather.xsWorkerBasic;
import asUtilsPorts.Weather.xsWorkerBouy;
import asUtilsPorts.Weather.xsWorkerFull;
import asUtilsPorts.Weather.xsWorkerHydro;
import asUtilsPorts.Weather.xsWorkerMETARStream;
import asUtilsPorts.Weather.xsWorkerWunder;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.MyDBConnector;
import asWebRest.shared.ThreadRipper;
import asWebRest.shared.WebCommon;

public class Stations {

	public String fetch(String fType) {
		
		String returnData = "";
            
        final boolean debugMode = false;

        CommonBeans cb = new CommonBeans();
        JunkyBeans junkyBeans = new JunkyBeans();
        ModelBeans modelBeans = new ModelBeans();
        MyDBConnector mdb = new MyDBConnector();
        ThreadRipper tr = new ThreadRipper();
        WebCommon wc = new WebCommon();        
        xsImageOp xsio = new xsImageOp();
        xsMETARAutoAdd xsmaa = new xsMETARAutoAdd();
        xsWorkerBasic xswb = new xsWorkerBasic();
        xsWorkerBouy xswy = new xsWorkerBouy();
        xsWorkerFull xswf = new xsWorkerFull();
        xsWorkerHydro xswh = new xsWorkerHydro();
        xsWorkerMETARStream xswm = new xsWorkerMETARStream();
        xsWorkerWunder xsww = new xsWorkerWunder();
        
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
    
		final long startTime = System.currentTimeMillis();

		final String xsTmp = modelBeans.getDiskSwap().toString();
		final String tFHour2D = "02";
		final String tSFHour2D = "03";
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
		final File logFile = new File(xsTmp+"/xs.log");
		final File metarsZipFile = new File(xsTmp+"/metars.xml.gz");
		final File nwsObsXMLzipFile = new File(xsTmp+"/index.zip");
		final File wwwOutObj = new File(cb.getPersistTomcat()+"/G2Out/xsOut");
		final File xsTmpObj = new File(xsTmp);
		final String gVarsSQL = "SELECT gVar FROM WxObs.gradsOutType WHERE Active=1;";
		final String gVarsHSQL = "SELECT gVar FROM WxObs.gradsOutType WHERE Active=1 AND HighRes=1;";
		final String gVarsLSQL = "SELECT gVar FROM WxObs.gradsOutType WHERE Active=1 AND HighRes=0;";
		final String resHigh = "13068x6600";
		final String resLow = "2904x1440";
		final String appPath = helpers.toString();
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

		if(fType.equals("Rapid")) { rapidRefresh = true; subHour = true; subHourFlag = "yes"; onlyWunder = false; }
		if(fType.equals("Wunder")) { rapidRefresh = true; subHour = false; subHourFlag = "no"; onlyWunder = true; }

		returnData += " -> DEBUG: (String) getHour = "+getHour+"\n" +
				" -> DEBUG: (String) getHourMin = "+getHourMin+"\n" +
				" -> DEBUG: (String) getDate = "+getDate;
                
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
				wc.deleteDir(xsTmpObj);
				xsTmpObj.mkdirs();
			}
		
            try ( ResultSet resultSetGVars = wc.q2rs1c(dbc, gVarsSQL, null); ) {
                    while (resultSetGVars.next()) { gVars.add(resultSetGVars.getString("gVar")); }
            } catch (Exception e) { e.printStackTrace(); }

            try ( ResultSet resultSetGVarsH = wc.q2rs1c(dbc, gVarsHSQL, null); ) {
                    while (resultSetGVarsH.next()) { gVarsH.add(resultSetGVarsH.getString("gVar")); }
            } catch (Exception e) { e.printStackTrace(); }

            try ( ResultSet resultSetGVarsL = wc.q2rs1c(dbc, gVarsLSQL, null); ) {
                    while (resultSetGVarsL.next()) { gVarsL.add(resultSetGVarsL.getString("gVar")); }
            } catch (Exception e) { e.printStackTrace(); }

            for (String thisGVar : gVars) {
                    File thisGVarPath = new File(gradsOutObj.getPath()+"/"+thisGVar);
                    File thisGVarWPath = new File(wwwOutObj.getPath()+"/"+thisGVar);
                    thisGVarPath.mkdirs();
                    thisGVarWPath.mkdirs();
            }
                        
			wc.jsoupOutBinary(hrrrGrib2URL, hrrrGrib2File, modelBeans.getDownloadTimeout());

			wc.runProcess("(\""+appPath+"/wgrib2\" "+hrrrGrib2File.getPath()+" -pdt | egrep -v \"^600:\" | \""+appPath+"/wgrib2\" -i "+hrrrGrib2File.getPath()+" -grib "+hrrrGrib2File.getPath()+")");
			wc.runProcess("(\""+appPath+"/g2ctl\" "+hrrrGrib2File.getPath()+" > "+hrrrCtlFile.getPath()+")");
			wc.runProcess("\""+appPath+"/gribmap\" -v -i "+hrrrCtlFile.getPath());

			ArrayList<Runnable> stp1 = new ArrayList<Runnable>();
			stp1.add(() -> wc.jsoupOutBinary(xmlObsURL, nwsObsXMLzipFile, 15.0));
			stp1.add(() -> wc.jsoupOutBinary(metarsURL, metarsZipFile, 15.0));
			tr.runProcesses(stp1, false, false);		

			ArrayList<Runnable> stp2 = new ArrayList<Runnable>();
			stp2.add(() -> wc.unzipFile(nwsObsXMLzipFile.getPath(), xsTmp));
			stp2.add(() -> wc.runProcess("gunzip \""+metarsZipFile.getPath()+"\""));
			tr.runProcesses(stp2, false, false);
			
            if(debugMode) {

                System.out.println("DEBUG MODE ON!");
                xswf.stations(true, xsTmp, "USC");

            } else {

    			wc.runProcess("(echo \"run "+helpers.getPath()+"/xsGraphics.gs "+getDate+" "+getHourMin+" "+gradsOutObj.getPath()+" "+subHourFlag+"\" | \""+appPath+"/grads\" -blc \"open "+hrrrCtlFile.getPath()+"\" &>> "+logFile.getPath()+")");
                for (String gVar : gVarsH) { wc.runProcess("convert \""+gradsOutObj.getPath()+"/"+gVar+"/"+getDate+"_"+getHourMin+"_"+gVar+".png\" -gravity Center -crop "+resHigh+"+0+0 "+gradsOutObj.getPath()+"/"+gVar+"/"+getDate+"_"+getHourMin+"_"+gVar+".png"); }
                for (String gVar : gVarsL) { wc.runProcess("convert \""+gradsOutObj.getPath()+"/"+gVar+"/"+getDate+"_"+getHourMin+"_"+gVar+".png\" -gravity Center -crop "+resLow+"+0+0 "+gradsOutObj.getPath()+"/"+gVar+"/"+getDate+"_"+getHourMin+"_"+gVar+".png"); }

                final String[] xsImageOpArgs = { xsTmp }; xsio.main(xsImageOpArgs, gVars, gVarsH, gVarsL);

                xsmaa.main(xsTmp);

    			ArrayList<Runnable> stp3 = new ArrayList<Runnable>();
    			stp3.add(() -> xswf.stations(false, xsTmp, "USC"));
    			stp3.add(() -> xswf.stations(false, xsTmp, "USE"));
    			stp3.add(() -> xswf.stations(false, xsTmp, "USW"));
    			stp3.add(() -> xswb.stations(xsTmp, "None"));    			
    			stp3.add(() -> xswm.main(xsTmp));
    			stp3.add(() -> xswy.main(xsTmp, "None"));
    			stp3.add(() -> xswh.main(xsTmp, "None"));
    			stp3.add(() -> xsww.main(xsTmp, "None"));
    			tr.runProcesses(stp3, false, false);
    			
                String jsonBigString = null;
                try { jsonBigString = wc.runProcessOutVar("cat "+xsTmp+"/output_*.json"); } catch (IOException ix) { ix.printStackTrace(); }
                jsonBigString = ("{"+jsonBigString+"}").replace("\n","").replace(",}", "}");
                try { wc.varToFile(jsonBigString, jsonDebugDumpFile, false); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
                String jsonBigSQLQuery = "INSERT INTO WxObs.StationDataIndexed (jsonData) VALUES ('"+jsonBigString+"');";
                try { wc.q2do1c(dbc, jsonBigSQLQuery, null); } catch (Exception e) { e.printStackTrace(); }

            }

		} else {
			
			xsww.main(xsTmp, "None");
			
		}
		
		String jsonRapidString = null;
		try { jsonRapidString = wc.runProcessOutVar("cat "+xsTmp+"/rapid_*.json"); } catch (IOException ix) { ix.printStackTrace(); }
		jsonRapidString = ("{"+jsonRapidString+"}").replace("\n","").replace(",}", "}");
		try { wc.varToFile(jsonRapidString, jsonDebugDumpRapidFile, false); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
		String jsonRapidSQLQuery = "INSERT INTO WxObs.RapidSDI (jsonData) VALUES ('"+jsonRapidString+"');";
		try { wc.q2do1c(dbc, jsonRapidSQLQuery, null); } catch (Exception e) { e.printStackTrace(); }

		final long endTime = System.currentTimeMillis();
	
		long totalRunTime = (endTime - startTime)/1000;

		String xsRuntime = "INSERT INTO WxObs.Logs VALUES (Null,"+totalRunTime+");";
		try { wc.varToFile(xsRuntime, logFile, true); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
		try { wc.q2do1c(dbc, xsRuntime, null); } catch (Exception e) { e.printStackTrace(); }

		returnData += "Updates completed! Runtime: "+totalRunTime; 
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
        
        return returnData;
		
	}

}
