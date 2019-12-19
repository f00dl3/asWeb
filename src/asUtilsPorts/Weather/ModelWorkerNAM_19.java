/*
by Anthony Stump
Created: 4 Oct 2017
Updated: 19 Dec 2019
*/

package asUtilsPorts.Weather;

import asUtils.Shares.JunkyBeans;
import asUtils.Shares.StumpJunk;
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

import asUtils.Shares.MyDBConnector;

public class ModelWorkerNAM_19 {
	
	public static void main(String[] args) {
				
		MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
        ModelBeans wmb = new ModelBeans();
        ModelShare wms = new ModelShare();
        Grib2Iterators gi = new Grib2Iterators();
        JunkyBeans junkyBeans = new JunkyBeans();
        
        final double ddv = wmb.getDefaultDataValue();
		final String getHour = args[0];
		final String round = args[1];
		final DateTime tDateTime = new DateTime(DateTimeZone.UTC).minusHours(4);
		final DateTimeFormatter getDateFormat = DateTimeFormat.forPattern("yyyyMMdd");
		final String getDate = getDateFormat.print(tDateTime);
		final File appPath = junkyBeans.getAppShareSys();
		final File xml2Path = wmb.getXml2Path();
		final File helpers = junkyBeans.getHelpers();
		final File jsonOutFile = new File(xml2Path+"/namOut_"+round+".json");
		final String pointInputAsString = ModelShare.pointInputAsString("KOJC");				
		final String modelName = "NAM";
		final String namBase = wmb.getNomadsBase() + "/nam/prod/nam."+getDate;
		final String nFHq = "SELECT FHour FROM WxObs.GFSFHA WHERE DoGet=1 AND NAM=1 AND Round="+round+";";
		final String heightQ = "SELECT HeightMb FROM WxObs.ModelHeightLevels WHERE NAM=1 ORDER BY HeightMB DESC;";

		final int gribSpot = 3;
		
		JSONObject nam = new JSONObject();
		
		List<String> nFHList = new ArrayList<>();
		List<String> heightList = new ArrayList<>();
		
		try ( ResultSet resultSetNFH = mdb.q2rs1c(dbc, nFHq, null); ) { 
            while (resultSetNFH.next()) { nFHList.add(resultSetNFH.getString("FHour")); }
        } catch (Exception e) { e.printStackTrace(); }
		
		try ( ResultSet resultSetHeights = mdb.q2rs1c(dbc, heightQ, null); ) { 
            while (resultSetHeights.next()) { heightList.add(resultSetHeights.getString("HeightMb")); }
        } catch (Exception e) { e.printStackTrace(); }

		final String filters = ModelShare.filters("g2f")+"\\|"+ModelShare.filters("g2fr");

		for (String tFHourStr : nFHList) {

			final int tFHour = Integer.parseInt(tFHourStr);
            final String enc = "_"+tFHour;
			final String tFHour2D = String.format("%02d", tFHour);
			final String tFHour4D = String.format("%04d", tFHour);
			final File sounding = new File(xml2Path.getPath()+"/outNAM_"+tFHour4D+".csv");
			final File tFHGlob = new File(xml2Path.getPath()+"/fn"+tFHour4D);
			final File tFHCtlFile = new File(tFHGlob.getPath()+".ctl");
			final String tFHData = namBase+"/nam.t"+getHour+"z.conusnest.hiresf"+tFHour2D+".tm00.grib2";

			System.out.println(" -> Processing "+modelName+" data - FH +"+tFHour4D+"!");

			StumpJunk.runProcess("(\""+helpers.getPath()+"/get_inv.pl\" "+tFHData+".idx | grep \""+filters+"\" | \""+helpers.getPath()+"/get_grib.pl\" "+tFHData+" "+tFHGlob.getPath()+")");
			try { StumpJunk.runProcessOutFile("\""+appPath.getPath()+"/wgrib2\" "+tFHGlob.getPath()+" "+pointInputAsString, sounding, false); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
			StumpJunk.sedFileReplace(sounding.getPath(), ":lon", ",lon");
			try { StumpJunk.runProcessOutFile("\""+appPath.getPath()+"/g2ctl\" "+tFHGlob.getPath(), tFHCtlFile, false); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
			StumpJunk.runProcess("\""+appPath.getPath()+"/gribmap\" -v -i "+tFHCtlFile.getPath());
		
			Scanner namScanner = null; try {		
				namScanner = new Scanner(sounding);
				while(namScanner.hasNext()) {
					final String line = namScanner.nextLine();
                    for(String sHeight : heightList) {
                            final int tHt = Integer.valueOf(sHeight);
                            final String tHe = tHt + enc;
                            if(line.startsWith(gi.nam("TC"+tHe)+":")) { try { nam.put("T"+tHe, gi.g2d(line, gribSpot, true)); } catch (Exception e) { e.printStackTrace(); nam.put("T"+tHe, ddv); } }
                            if(line.startsWith(gi.nam("RH"+tHe)+":")) { try { nam.put("RH"+tHe, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); nam.put("RH"+tHe, ddv); } }
                            if(line.startsWith(gi.nam("WU"+tHe)+".1:")) { try { nam.put("WU"+tHe, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); nam.put("WU"+tHe, ddv); } }
                            if(line.startsWith(gi.nam("WU"+tHe)+".2:")) { try { nam.put("WV"+tHe, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); nam.put("WV"+tHe, ddv); } }
                    }
                    if(line.startsWith(gi.nam("CAPE"+enc)+":")) { try { nam.put("CAPE"+enc, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); nam.put("CAPE"+enc, ddv); } }
                    if(line.startsWith(gi.nam("CIN"+enc)+":")) { try { nam.put("CIN"+enc, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); nam.put("CIN"+enc, ddv); } }
                    if(line.startsWith(gi.nam("LI"+enc)+":")) { try { nam.put("LI"+enc, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); nam.put("LI"+enc, ddv); } }
                    if(line.startsWith(gi.nam("PWAT"+enc)+":")) { try { nam.put("PWAT"+enc, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); nam.put("PWAT"+enc, ddv); } }
                    if(line.startsWith(gi.nam("HGT500"+enc)+":")) { try { nam.put("HGT500"+enc, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); nam.put("HGT500"+enc, ddv); } }      
				}
				
			}
			catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
			
            for(String sHeight : heightList) {
                final int tHt = Integer.valueOf(sHeight);
                final String tHe = tHt + enc;
                try { 
                    if(nam.has("T"+tHe) && nam.has("RH"+tHe)) { nam.put("D"+tHe, wms.calcDwpt(nam.getDouble("T"+tHe), nam.getDouble("RH"+tHe))); } else { nam.put("D"+tHe, ddv); }
                    if(nam.has("WU"+tHe) && nam.has("WV"+tHe)) { nam.put("WD"+tHe, wms.windDirCalc(nam.getDouble("WU"+tHe), nam.getDouble("WV"+tHe))); } else { nam.put("WD"+tHe, ddv); }
                    if(nam.has("WU"+tHe) && nam.has("WV"+tHe)) { nam.put("WS"+tHe, wms.windSpdCalc(nam.getDouble("WU"+tHe), nam.getDouble("WV"+tHe))); } else { nam.put("WS"+tHe, ddv); }
                } catch (Exception e) {
                    e.printStackTrace();
                }            
            }
            
            if(nam.has("PWAT"+enc)) { nam.put("PRATE"+enc, nam.getDouble("PWAT"+enc)); }
            
			StumpJunk.runProcess("(echo \"run "+helpers.getPath()+"/ModelData.gs "+modelName+" "+tFHour4D+" "+getDate+" "+getHour+" "+xml2Path.getPath()+"\" | "+appPath.getPath()+"/grads -blc \"open "+tFHCtlFile.getPath()+"\")");								
			
			tFHGlob.delete();
			
		}

		String thisJSONstring = nam.toString();
		thisJSONstring = thisJSONstring.substring(0, thisJSONstring.length()-1)+",";
		try { StumpJunk.varToFile(thisJSONstring, jsonOutFile, true); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
		System.out.println(" -> Completed: "+modelName+" - Round: "+round);
                
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
                
	}
}
