/*
by Anthony Stump
Created: 17 Oct 2017
Updated: 8 Feb 2020
*/

package asUtilsPorts.Weather.OldModelWorkers;

import java.io.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.json.*;

import asUtilsPorts.Shares.JunkyBeans;
import asUtilsPorts.Weather.ModelBeans;
import asUtilsPorts.Weather.ModelShare;
import asWebRest.shared.MyDBConnector;
import asWebRest.shared.WebCommon;

public class ModelWorkerSRFA {
	
	public void main(String getHour, String round) {
		
		MyDBConnector mdb = new MyDBConnector();
                Connection dbc = null;
                try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
                
                ModelBeans modelBeans = new ModelBeans();
                JunkyBeans junkyBeans = new JunkyBeans();
                ModelShare ms = new ModelShare();
                WebCommon wc = new WebCommon();
                
		final DateTime tDateTime = new DateTime(DateTimeZone.UTC).minusHours(4);
		final DateTimeFormatter getDateFormat = DateTimeFormat.forPattern("yyyyMMdd");
		final String getDate = getDateFormat.print(tDateTime);
		final File appPath = junkyBeans.getAppShareSys();
		final File xml2Path = modelBeans.getXml2Path();
		final File helpers = junkyBeans.getHelpers();
		final File jsonOutFile = new File(xml2Path.getPath()+"/srfaOut_"+round+".json");
		final String pointInputAsString = ms.pointInputAsString("KOJC");				
		final String modelName = "SRFA";
		final String srefBase= modelBeans.getNomadsBase() + "/sref/prod/sref."+getDate+"/"+getHour+"/pgrb";
		
		final String sFHq = "SELECT FHour FROM WxObs.GFSFHA WHERE DoGet=1 AND SRFA=1 AND Round="+round+";";
		final String heightQ = "SELECT HeightMb FROM WxObs.ModelHeightLevels WHERE SRFA=1 ORDER BY HeightMB DESC;";

		int gribSpot = 3;
		int iSx = 10;
		int iSs = 3;
		
		JSONObject srfaJSON = new JSONObject();
		
		List<String> sFHList = new ArrayList<>();
		List<String> heightList = new ArrayList<>();
		
		try ( ResultSet resultSetSFH = wc.q2rs1c(dbc, sFHq, null); ) { 
                    while (resultSetSFH.next()) { sFHList.add(resultSetSFH.getString("FHour")); }
                } catch (Exception e) { e.printStackTrace(); }
		
		try ( ResultSet resultSetHeights = wc.q2rs1c(dbc, heightQ, null); ) { 
                    while (resultSetHeights.next()) { heightList.add(resultSetHeights.getString("HeightMb")); }
                } catch (Exception e) { e.printStackTrace(); }

		final String filters = ms.filters("g2f")+"\\|"+ms.filters("g2fr");

		for (String tFHourStr : sFHList) {

			int tFHour = Integer.parseInt(tFHourStr);
			String tFHour2D = String.format("%02d", tFHour);
			String tFHour3D = String.format("%03d", tFHour);
			String tFHour4D = String.format("%04d", tFHour);
			File sounding = new File(xml2Path.getPath()+"/outSRFA_"+tFHour4D+".csv");
			File tFHGlob = new File(xml2Path.getPath()+"/fs"+tFHour4D);
			File tFHCtlFile = new File(tFHGlob.getPath()+".ctl");
			String tFHData = srefBase+"/sref_arw.t"+getHour+"z.pgrb132.n1.f"+tFHour2D+".grib2";

			System.out.println(" -> Processing "+modelName+" data - FH +"+tFHour4D+"!");

			wc.runProcess("(\""+helpers.getPath()+"/get_inv.pl\" "+tFHData+".idx | grep \""+filters+"\" | \""+helpers.getPath()+"/get_grib.pl\" "+tFHData+" "+tFHGlob.getPath()+")");
			try { wc.runProcessOutFile("\""+appPath.getPath()+"/wgrib2\" "+tFHGlob.getPath()+" "+pointInputAsString, sounding, false); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
			wc.sedFileReplace(sounding.getPath(), ":lon", ",lon");
			try { wc.runProcessOutFile("\""+appPath.getPath()+"/g2ctl\" "+tFHGlob, tFHCtlFile, false); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
			wc.runProcess("\""+appPath.getPath()+"/gribmap\" -v -i "+tFHCtlFile.getPath());

			double tTC100 = 0.001; double tTC150 = 0.001;
			double tTC200 = 0.001; double tTC250 = 0.001;
			double tTC300 = 0.001; double tTC350 = 0.001;
			double tTC400 = 0.001; double tTC450 = 0.001;
			double tTC500 = 0.001; double tTC550 = 0.001;
			double tTC600 = 0.001; double tTC650 = 0.001;
			double tTC700 = 0.001; double tTC750 = 0.001;
			double tTC800 = 0.001; double tTC850 = 0.001;
			double tTC900 = 0.001; double tTC950 = 0.001;
			double tTC1000 = 0.001; double tTC0 = 0.001;

			double tRH100 = 0.001; double tRH150 = 0.001;
			double tRH200 = 0.001; double tRH250 = 0.001;
			double tRH300 = 0.001; double tRH350 = 0.001;
			double tRH400 = 0.001; double tRH450 = 0.001;
			double tRH500 = 0.001; double tRH550 = 0.001;
			double tRH600 = 0.001; double tRH650 = 0.001;
			double tRH700 = 0.001; double tRH750 = 0.001;
			double tRH800 = 0.001; double tRH850 = 0.001;
			double tRH900 = 0.001; double tRH950 = 0.001;
			double tRH1000 = 0.001; double tRH0 = 0.001;

			double tWU100 = 0.001; double tWU150 = 0.001;
			double tWU200 = 0.001; double tWU250 = 0.001;
			double tWU300 = 0.001; double tWU350 = 0.001;
			double tWU400 = 0.001; double tWU450 = 0.001;
			double tWU500 = 0.001; double tWU550 = 0.001;
			double tWU600 = 0.001; double tWU650 = 0.001;
			double tWU700 = 0.001; double tWU750 = 0.001;
			double tWU800 = 0.001; double tWU850 = 0.001;
			double tWU900 = 0.001; double tWU950 = 0.001;
			double tWU1000 = 0.001; double tWU0 = 0.001;
			
			double tWV100 = 0.001; double tWV150 = 0.001;
			double tWV200 = 0.001; double tWV250 = 0.001;
			double tWV300 = 0.001; double tWV350 = 0.001;
			double tWV400 = 0.001; double tWV450 = 0.001;
			double tWV500 = 0.001; double tWV550 = 0.001;
			double tWV600 = 0.001; double tWV650 = 0.001;
			double tWV700 = 0.001; double tWV750 = 0.001;
			double tWV800 = 0.001; double tWV850 = 0.001;
			double tWV900 = 0.001; double tWV950 = 0.001;
			double tWV1000 = 0.001; double tWV0 = 0.001;
		
			double tCAPE = 0.001; double tCIN = 0.001;
			double tPRATE = 0.001; double tHGT500 = 0.001;
			double tPWAT = 0.001;
			double tLI = 0.001;

			Scanner srfaScanner = null; try {		
				srfaScanner = new Scanner(sounding);
				while(srfaScanner.hasNext()) {
					String line = srfaScanner.nextLine();				
					
					if(line.startsWith(((iSx+0)+(iSs*0))+":")) { String[] lineTmp = line.split(","); try { tTC100 = Double.parseDouble(lineTmp[gribSpot].replace("val=", ""))-273.15; } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+0)+(iSs*1))+":")) { String[] lineTmp = line.split(","); try { tTC150 = Double.parseDouble(lineTmp[gribSpot].replace("val=", ""))-273.15; } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+0)+(iSs*2))+":")) { String[] lineTmp = line.split(","); try { tTC200 = Double.parseDouble(lineTmp[gribSpot].replace("val=", ""))-273.15; } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+0)+(iSs*3))+":")) { String[] lineTmp = line.split(","); try { tTC250 = Double.parseDouble(lineTmp[gribSpot].replace("val=", ""))-273.15; } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+0)+(iSs*4))+":")) { String[] lineTmp = line.split(","); try { tTC300 = Double.parseDouble(lineTmp[gribSpot].replace("val=", ""))-273.15; } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+0)+(iSs*5))+":")) { String[] lineTmp = line.split(","); try { tTC350 = Double.parseDouble(lineTmp[gribSpot].replace("val=", ""))-273.15; } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+0)+(iSs*6))+":")) { String[] lineTmp = line.split(","); try { tTC400 = Double.parseDouble(lineTmp[gribSpot].replace("val=", ""))-273.15; } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+0)+(iSs*7))+":")) { String[] lineTmp = line.split(","); try { tTC450 = Double.parseDouble(lineTmp[gribSpot].replace("val=", ""))-273.15; } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+0)+(iSs*8)+1)+":")) { String[] lineTmp = line.split(","); try { tTC500 = Double.parseDouble(lineTmp[gribSpot].replace("val=", ""))-273.15; } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+0)+(iSs*9)+1)+":")) { String[] lineTmp = line.split(","); try { tTC550 = Double.parseDouble(lineTmp[gribSpot].replace("val=", ""))-273.15; } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+0)+(iSs*10)+1)+":")) { String[] lineTmp = line.split(","); try { tTC600 = Double.parseDouble(lineTmp[gribSpot].replace("val=", ""))-273.15; } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+0)+(iSs*11)+1)+":")) { String[] lineTmp = line.split(","); try { tTC650 = Double.parseDouble(lineTmp[gribSpot].replace("val=", ""))-273.15; } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+0)+(iSs*12)+1)+":")) { String[] lineTmp = line.split(","); try { tTC700 = Double.parseDouble(lineTmp[gribSpot].replace("val=", ""))-273.15; } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+0)+(iSs*13)+1)+":")) { String[] lineTmp = line.split(","); try { tTC750 = Double.parseDouble(lineTmp[gribSpot].replace("val=", ""))-273.15; } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+0)+(iSs*14)+1)+":")) { String[] lineTmp = line.split(","); try { tTC800 = Double.parseDouble(lineTmp[gribSpot].replace("val=", ""))-273.15; } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+0)+(iSs*15)+1)+":")) { String[] lineTmp = line.split(","); try { tTC850 = Double.parseDouble(lineTmp[gribSpot].replace("val=", ""))-273.15; } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+0)+(iSs*16)+1)+":")) { String[] lineTmp = line.split(","); try { tTC900 = Double.parseDouble(lineTmp[gribSpot].replace("val=", ""))-273.15; } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+0)+(iSs*17)+1)+":")) { String[] lineTmp = line.split(","); try { tTC950 = Double.parseDouble(lineTmp[gribSpot].replace("val=", ""))-273.15; } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+0)+(iSs*18)+1)+":")) { String[] lineTmp = line.split(","); try { tTC1000 = Double.parseDouble(lineTmp[gribSpot].replace("val=", ""))-273.15; } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith("1:")) { String[] lineTmp = line.split(","); try { tTC0 = Double.parseDouble(lineTmp[gribSpot].replace("val=", ""))-273.15; } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
			
					if(line.startsWith(((iSx+1)+(iSs*0))+":")) { String[] lineTmp = line.split(","); try { tRH100 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+1)+(iSs*1))+":")) { String[] lineTmp = line.split(","); try { tRH150 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+1)+(iSs*2))+":")) { String[] lineTmp = line.split(","); try { tRH200 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+1)+(iSs*3))+":")) { String[] lineTmp = line.split(","); try { tRH250 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+1)+(iSs*4))+":")) { String[] lineTmp = line.split(","); try { tRH300 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+1)+(iSs*5))+":")) { String[] lineTmp = line.split(","); try { tRH350 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+1)+(iSs*6))+":")) { String[] lineTmp = line.split(","); try { tRH400 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+1)+(iSs*7))+":")) { String[] lineTmp = line.split(","); try { tRH450 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+1)+(iSs*8)+1)+":")) { String[] lineTmp = line.split(","); try { tRH500 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+1)+(iSs*9)+1)+":")) { String[] lineTmp = line.split(","); try { tRH550 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+1)+(iSs*10)+1)+":")) { String[] lineTmp = line.split(","); try { tRH600 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+1)+(iSs*11)+1)+":")) { String[] lineTmp = line.split(","); try { tRH650 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+1)+(iSs*12)+1)+":")) { String[] lineTmp = line.split(","); try { tRH700 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+1)+(iSs*13)+1)+":")) { String[] lineTmp = line.split(","); try { tRH750 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+1)+(iSs*14)+1)+":")) { String[] lineTmp = line.split(","); try { tRH800 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+1)+(iSs*15)+1)+":")) { String[] lineTmp = line.split(","); try { tRH850 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+1)+(iSs*16)+1)+":")) { String[] lineTmp = line.split(","); try { tRH900 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+1)+(iSs*17)+1)+":")) { String[] lineTmp = line.split(","); try { tRH950 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+1)+(iSs*18)+1)+":")) { String[] lineTmp = line.split(","); try { tRH1000 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith("2:")) { String[] lineTmp = line.split(","); try { tRH0 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }

					if(line.startsWith(((iSx+2)+(iSs*0))+".1:")) { String[] lineTmp = line.split(","); try { tWU100 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+2)+(iSs*1))+".1:")) { String[] lineTmp = line.split(","); try { tWU150 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+2)+(iSs*2))+".1:")) { String[] lineTmp = line.split(","); try { tWU200 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+2)+(iSs*3))+".1:")) { String[] lineTmp = line.split(","); try { tWU250 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+2)+(iSs*4))+".1:")) { String[] lineTmp = line.split(","); try { tWU300 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+2)+(iSs*5))+".1:")) { String[] lineTmp = line.split(","); try { tWU350 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+2)+(iSs*6))+".1:")) { String[] lineTmp = line.split(","); try { tWU400 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+2)+(iSs*7))+".1:")) { String[] lineTmp = line.split(","); try { tWU450 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+2)+(iSs*8)+1)+".1:")) { String[] lineTmp = line.split(","); try { tWU500 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+2)+(iSs*9)+1)+".1:")) { String[] lineTmp = line.split(","); try { tWU550 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+2)+(iSs*10)+1)+".1:")) { String[] lineTmp = line.split(","); try { tWU600 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+2)+(iSs*11)+1)+".1:")) { String[] lineTmp = line.split(","); try { tWU650 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+2)+(iSs*12)+1)+".1:")) { String[] lineTmp = line.split(","); try { tWU700 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+2)+(iSs*13)+1)+".1:")) { String[] lineTmp = line.split(","); try { tWU750 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+2)+(iSs*14)+1)+".1:")) { String[] lineTmp = line.split(","); try { tWU800 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+2)+(iSs*15)+1)+".1:")) { String[] lineTmp = line.split(","); try { tWU850 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+2)+(iSs*16)+1)+".1:")) { String[] lineTmp = line.split(","); try { tWU900 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+2)+(iSs*17)+1)+".1:")) { String[] lineTmp = line.split(","); try { tWU950 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+2)+(iSs*18)+1)+".1:")) { String[] lineTmp = line.split(","); try { tWU1000 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith("3.1:")) { String[] lineTmp = line.split(","); try { tWU0 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }

					if(line.startsWith(((iSx+2)+(iSs*0))+".2:")) { String[] lineTmp = line.split(","); try { tWV100 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+2)+(iSs*1))+".2:")) { String[] lineTmp = line.split(","); try { tWV150 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+2)+(iSs*2))+".2:")) { String[] lineTmp = line.split(","); try { tWV200 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+2)+(iSs*3))+".2:")) { String[] lineTmp = line.split(","); try { tWV250 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+2)+(iSs*4))+".2:")) { String[] lineTmp = line.split(","); try { tWV300 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+2)+(iSs*5))+".2:")) { String[] lineTmp = line.split(","); try { tWV350 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+2)+(iSs*6))+".2:")) { String[] lineTmp = line.split(","); try { tWV400 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+2)+(iSs*7))+".2:")) { String[] lineTmp = line.split(","); try { tWV450 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+2)+(iSs*8)+1)+".2:")) { String[] lineTmp = line.split(","); try { tWV500 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+2)+(iSs*9)+1)+".2:")) { String[] lineTmp = line.split(","); try { tWV550 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+2)+(iSs*10)+1)+".2:")) { String[] lineTmp = line.split(","); try { tWV600 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+2)+(iSs*11)+1)+".2:")) { String[] lineTmp = line.split(","); try { tWV650 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+2)+(iSs*12)+1)+".2:")) { String[] lineTmp = line.split(","); try { tWV700 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+2)+(iSs*13)+1)+".2:")) { String[] lineTmp = line.split(","); try { tWV750 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+2)+(iSs*14)+1)+".2:")) { String[] lineTmp = line.split(","); try { tWV800 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+2)+(iSs*15)+1)+".2:")) { String[] lineTmp = line.split(","); try { tWV850 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+2)+(iSs*16)+1)+".2:")) { String[] lineTmp = line.split(","); try { tWV900 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+2)+(iSs*17)+1)+".2:")) { String[] lineTmp = line.split(","); try { tWV950 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith(((iSx+2)+(iSs*18)+1)+".2:")) { String[] lineTmp = line.split(","); try { tWV1000 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith("3.2:")) { String[] lineTmp = line.split(","); try { tWV0 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
			
					if(line.startsWith("5:")) { String[] lineTmp = line.split(","); try { tCAPE = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith("6:")) { String[] lineTmp = line.split(","); try { tCIN = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith("68:")) { String[] lineTmp = line.split(","); try { tPRATE = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith("7:")) { String[] lineTmp = line.split(","); try { tPWAT = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith("34:")) { String[] lineTmp = line.split(","); try { tHGT500 = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
					if(line.startsWith("9:")) { String[] lineTmp = line.split(","); try { tLI = Double.parseDouble(lineTmp[gribSpot].replace("val=", "")); } catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); } }
			
				}
				
			}
			catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
			
			double tTD100 = ms.calcDwpt(tTC100, tRH100); double tTD150 = ms.calcDwpt(tTC150, tRH150); 
			double tTD200 = ms.calcDwpt(tTC200, tRH200); double tTD250 = ms.calcDwpt(tTC250, tRH250); 
			double tTD300 = ms.calcDwpt(tTC300, tRH300); double tTD350 = ms.calcDwpt(tTC350, tRH350); 
			double tTD400 = ms.calcDwpt(tTC400, tRH400); double tTD450 = ms.calcDwpt(tTC450, tRH450); 
			double tTD500 = ms.calcDwpt(tTC500, tRH500); double tTD550 = ms.calcDwpt(tTC550, tRH550); 
			double tTD600 = ms.calcDwpt(tTC600, tRH600); double tTD650 = ms.calcDwpt(tTC650, tRH650); 
			double tTD700 = ms.calcDwpt(tTC700, tRH700); double tTD750 = ms.calcDwpt(tTC750, tRH750); 
			double tTD800 = ms.calcDwpt(tTC800, tRH800); double tTD850 = ms.calcDwpt(tTC850, tRH850); 
			double tTD900 = ms.calcDwpt(tTC900, tRH900); double tTD950 = ms.calcDwpt(tTC950, tRH950); 
			double tTD1000 = ms.calcDwpt(tTC1000, tRH1000); double tTD0 = ms.calcDwpt(tTC0, tRH0); 
			
			double tWD100 = ms.windDirCalc(tWU100, tWV100); double tWD150 = ms.windDirCalc(tWU150, tWV150);
			double tWS100 = ms.windSpdCalc(tWU100, tWV100); double tWS150 = ms.windSpdCalc(tWU150, tWV150);
			double tWD200 = ms.windDirCalc(tWU200, tWV200); double tWD250 = ms.windDirCalc(tWU250, tWV250);
			double tWS200 = ms.windSpdCalc(tWU200, tWV200); double tWS250 = ms.windSpdCalc(tWU250, tWV250);
			double tWD300 = ms.windDirCalc(tWU300, tWV300); double tWD350 = ms.windDirCalc(tWU350, tWV350);
			double tWS300 = ms.windSpdCalc(tWU300, tWV300); double tWS350 = ms.windSpdCalc(tWU350, tWV350);
			double tWD400 = ms.windDirCalc(tWU400, tWV400); double tWD450 = ms.windDirCalc(tWU450, tWV450);
			double tWS400 = ms.windSpdCalc(tWU400, tWV400); double tWS450 = ms.windSpdCalc(tWU450, tWV450);
			double tWD500 = ms.windDirCalc(tWU500, tWV500); double tWD550 = ms.windDirCalc(tWU550, tWV550);
			double tWS500 = ms.windSpdCalc(tWU500, tWV500); double tWS550 = ms.windSpdCalc(tWU550, tWV550);
			double tWD600 = ms.windDirCalc(tWU600, tWV600); double tWD650 = ms.windDirCalc(tWU650, tWV650);
			double tWS600 = ms.windSpdCalc(tWU600, tWV600); double tWS650 = ms.windSpdCalc(tWU650, tWV650);
			double tWD700 = ms.windDirCalc(tWU700, tWV700); double tWD750 = ms.windDirCalc(tWU750, tWV750);
			double tWS700 = ms.windSpdCalc(tWU700, tWV700); double tWS750 = ms.windSpdCalc(tWU750, tWV750);
			double tWD800 = ms.windDirCalc(tWU800, tWV800); double tWD850 = ms.windDirCalc(tWU850, tWV850);
			double tWS800 = ms.windSpdCalc(tWU800, tWV800); double tWS850 = ms.windSpdCalc(tWU850, tWV850);
			double tWD900 = ms.windDirCalc(tWU900, tWV900); double tWD950 = ms.windDirCalc(tWU950, tWV950);
			double tWS900 = ms.windSpdCalc(tWU900, tWV900); double tWS950 = ms.windSpdCalc(tWU950, tWV950);
			double tWD1000 = ms.windDirCalc(tWU1000, tWV1000); double tWD0 = ms.windDirCalc(tWU0, tWV0); 
			double tWS1000 = ms.windSpdCalc(tWU1000, tWV1000); double tWS0 = ms.windSpdCalc(tWU0, tWV0);
	
			if(tTC100 != 0.0001) { srfaJSON.put("T100_"+tFHour, tTC100); }
			if(tTC150 != 0.0001) { srfaJSON.put("T150_"+tFHour, tTC150); }
			if(tTC200 != 0.0001) { srfaJSON.put("T200_"+tFHour, tTC200); }
			if(tTC250 != 0.0001) { srfaJSON.put("T250_"+tFHour, tTC250); }
			if(tTC300 != 0.0001) { srfaJSON.put("T300_"+tFHour, tTC300); }
			if(tTC350 != 0.0001) { srfaJSON.put("T350_"+tFHour, tTC350); }
			if(tTC400 != 0.0001) { srfaJSON.put("T400_"+tFHour, tTC400); }
			if(tTC450 != 0.0001) { srfaJSON.put("T450_"+tFHour, tTC450); }
			if(tTC500 != 0.0001) { srfaJSON.put("T500_"+tFHour, tTC500); }
			if(tTC550 != 0.0001) { srfaJSON.put("T550_"+tFHour, tTC550); }
			if(tTC600 != 0.0001) { srfaJSON.put("T600_"+tFHour, tTC600); }
			if(tTC650 != 0.0001) { srfaJSON.put("T650_"+tFHour, tTC650); }
			if(tTC700 != 0.0001) { srfaJSON.put("T700_"+tFHour, tTC700); }
			if(tTC750 != 0.0001) { srfaJSON.put("T750_"+tFHour, tTC750); }
			if(tTC800 != 0.0001) { srfaJSON.put("T800_"+tFHour, tTC800); }
			if(tTC850 != 0.0001) { srfaJSON.put("T850_"+tFHour, tTC850); }
			if(tTC900 != 0.0001) { srfaJSON.put("T900_"+tFHour, tTC900); }
			if(tTC950 != 0.0001) { srfaJSON.put("T950_"+tFHour, tTC950); }
			if(tTC1000 != 0.0001) { srfaJSON.put("T1000_"+tFHour, tTC1000); }
			if(tTC0 != 0.0001) { srfaJSON.put("T0_"+tFHour, tTC0); }
			
			if(tTD100 != 0.0001) { srfaJSON.put("D100_"+tFHour, tTD100); }
			if(tTD150 != 0.0001) { srfaJSON.put("D150_"+tFHour, tTD150); }
			if(tTD200 != 0.0001) { srfaJSON.put("D200_"+tFHour, tTD200); }
			if(tTD250 != 0.0001) { srfaJSON.put("D250_"+tFHour, tTD250); }
			if(tTD300 != 0.0001) { srfaJSON.put("D300_"+tFHour, tTD300); }
			if(tTD350 != 0.0001) { srfaJSON.put("D350_"+tFHour, tTD350); }
			if(tTD400 != 0.0001) { srfaJSON.put("D400_"+tFHour, tTD400); }
			if(tTD450 != 0.0001) { srfaJSON.put("D450_"+tFHour, tTD450); }
			if(tTD500 != 0.0001) { srfaJSON.put("D500_"+tFHour, tTD500); }
			if(tTD550 != 0.0001) { srfaJSON.put("D550_"+tFHour, tTD550); }
			if(tTD600 != 0.0001) { srfaJSON.put("D600_"+tFHour, tTD600); }
			if(tTD650 != 0.0001) { srfaJSON.put("D650_"+tFHour, tTD650); }
			if(tTD700 != 0.0001) { srfaJSON.put("D700_"+tFHour, tTD700); }
			if(tTD750 != 0.0001) { srfaJSON.put("D750_"+tFHour, tTD750); }
			if(tTD800 != 0.0001) { srfaJSON.put("D800_"+tFHour, tTD800); }
			if(tTD850 != 0.0001) { srfaJSON.put("D850_"+tFHour, tTD850); }
			if(tTD900 != 0.0001) { srfaJSON.put("D900_"+tFHour, tTD900); }
			if(tTD950 != 0.0001) { srfaJSON.put("D950_"+tFHour, tTD950); }
			if(tTD1000 != 0.0001) { srfaJSON.put("D1000_"+tFHour, tTD1000); }
			if(tTD0 != 0.0001) { srfaJSON.put("D0_"+tFHour, tTD0); }
			
			if(tWD100 != 0.0001) { srfaJSON.put("WD100_"+tFHour, tWD100); }
			if(tWD150 != 0.0001) { srfaJSON.put("WD150_"+tFHour, tWD150); }
			if(tWD200 != 0.0001) { srfaJSON.put("WD200_"+tFHour, tWD200); }
			if(tWD250 != 0.0001) { srfaJSON.put("WD250_"+tFHour, tWD250); }
			if(tWD300 != 0.0001) { srfaJSON.put("WD300_"+tFHour, tWD300); }
			if(tWD350 != 0.0001) { srfaJSON.put("WD350_"+tFHour, tWD350); }
			if(tWD400 != 0.0001) { srfaJSON.put("WD400_"+tFHour, tWD400); }
			if(tWD450 != 0.0001) { srfaJSON.put("WD450_"+tFHour, tWD450); }
			if(tWD500 != 0.0001) { srfaJSON.put("WD500_"+tFHour, tWD500); }
			if(tWD550 != 0.0001) { srfaJSON.put("WD550_"+tFHour, tWD550); }
			if(tWD600 != 0.0001) { srfaJSON.put("WD600_"+tFHour, tWD600); }
			if(tWD650 != 0.0001) { srfaJSON.put("WD650_"+tFHour, tWD650); }
			if(tWD700 != 0.0001) { srfaJSON.put("WD700_"+tFHour, tWD700); }
			if(tWD750 != 0.0001) { srfaJSON.put("WD750_"+tFHour, tWD750); }
			if(tWD800 != 0.0001) { srfaJSON.put("WD800_"+tFHour, tWD800); }
			if(tWD850 != 0.0001) { srfaJSON.put("WD850_"+tFHour, tWD850); }
			if(tWD900 != 0.0001) { srfaJSON.put("WD900_"+tFHour, tWD900); }
			if(tWD950 != 0.0001) { srfaJSON.put("WD950_"+tFHour, tWD950); }
			if(tWD1000 != 0.0001) { srfaJSON.put("WD1000_"+tFHour, tWD1000); }
			if(tWD0 != 0.0001) { srfaJSON.put("WD0_"+tFHour, tWD0); }
			
			if(tWS100 != 0.0001) { srfaJSON.put("WS100_"+tFHour, tWS100); }
			if(tWS150 != 0.0001) { srfaJSON.put("WS150_"+tFHour, tWS150); }
			if(tWS200 != 0.0001) { srfaJSON.put("WS200_"+tFHour, tWS200); }
			if(tWS250 != 0.0001) { srfaJSON.put("WS250_"+tFHour, tWS250); }
			if(tWS300 != 0.0001) { srfaJSON.put("WS300_"+tFHour, tWS300); }
			if(tWS350 != 0.0001) { srfaJSON.put("WS350_"+tFHour, tWS350); }
			if(tWS400 != 0.0001) { srfaJSON.put("WS400_"+tFHour, tWS400); }
			if(tWS450 != 0.0001) { srfaJSON.put("WS450_"+tFHour, tWS450); }
			if(tWS500 != 0.0001) { srfaJSON.put("WS500_"+tFHour, tWS500); }
			if(tWS550 != 0.0001) { srfaJSON.put("WS550_"+tFHour, tWS550); }
			if(tWS600 != 0.0001) { srfaJSON.put("WS600_"+tFHour, tWS600); }
			if(tWS650 != 0.0001) { srfaJSON.put("WS650_"+tFHour, tWS650); }
			if(tWS700 != 0.0001) { srfaJSON.put("WS700_"+tFHour, tWS700); }
			if(tWS750 != 0.0001) { srfaJSON.put("WS750_"+tFHour, tWS750); }
			if(tWS800 != 0.0001) { srfaJSON.put("WS800_"+tFHour, tWS800); }
			if(tWS850 != 0.0001) { srfaJSON.put("WS850_"+tFHour, tWS850); }
			if(tWS900 != 0.0001) { srfaJSON.put("WS900_"+tFHour, tWS900); }
			if(tWS950 != 0.0001) { srfaJSON.put("WS950_"+tFHour, tWS950); }
			if(tWS1000 != 0.0001) { srfaJSON.put("WS1000_"+tFHour, tWS1000); }
			if(tWS0 != 0.0001) { srfaJSON.put("WS0_"+tFHour, tWS0); }

			if(tCIN != 0.0001) { srfaJSON.put("CIN_"+tFHour, tCIN); }
			if(tLI != 0.0001) { srfaJSON.put("LI_"+tFHour, tLI); }
			if(tPRATE != 0.0001) { srfaJSON.put("PRATE_"+tFHour, tPRATE*0.03937); }
			if(tPWAT != 0.0001) { srfaJSON.put("PWAT_"+tFHour, tPRATE*0.03937); }
			if(tHGT500 != 0.0001) { srfaJSON.put("HGT500_"+tFHour, tHGT500); }
			
			
			wc.runProcess("(echo \"run "+helpers.getPath()+"/ModelData.gs "+modelName+" "+tFHour4D+" "+getDate+" "+getHour+" "+xml2Path.getPath()+"\" | "+appPath.getPath()+"/grads -blc \"open "+tFHCtlFile.getPath()+"\")");								
			
			tFHGlob.delete();
			
		}

		String thisJSONstring = srfaJSON.toString();
		thisJSONstring = thisJSONstring.substring(0, thisJSONstring.length()-1)+",";
		try { wc.varToFile(thisJSONstring, jsonOutFile, true); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
		System.out.println(" -> Completed: "+modelName+" - Round: "+round);
                
                try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
		
	}
}
