/*
by Anthony Stump
Created: 4 Oct 2017
Updated: 2 Jan 2020
*/

package asUtilsPorts.Weather;

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

import asUtils.Shares.JunkyBeans;
import asWebRest.shared.MyDBConnector;
import asWebRest.shared.WebCommon;

public class ModelWorkerHRRR {
	
	public void main(String getHour, String round) {
		
		MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
        ModelBeans wmb = new ModelBeans();
        ModelShare wms = new ModelShare();
        Grib2Iterators gi = new Grib2Iterators();
        JunkyBeans junkyBeans = new JunkyBeans();
        WebCommon wc = new WebCommon();
        
        final double ddv = wmb.getDefaultDataValue();
		final DateTime tDateTime = new DateTime(DateTimeZone.UTC).minusHours(4);
		final DateTimeFormatter getDateFormat = DateTimeFormat.forPattern("yyyyMMdd");
		final String getDate = getDateFormat.print(tDateTime);
		final File xml2Path = wmb.getXml2Path();
		final File helpers = junkyBeans.getHelpers();
		final File jsonOutFile = new File(xml2Path+"/hrrrOut_"+round+".json");
		final String pointInputAsString = wms.pointInputAsString("KOJC");
		final String modelName = "HRRR";
		final String hrrrBase = wmb.getNomadsBase()+"/hrrr/prod/hrrr."+getDate+"/conus";
		final String hFHq = "SELECT FHour FROM WxObs.GFSFHA WHERE DoGet=1 AND HRRR=1 AND Round="+round+";";
		final String heightQ = "SELECT HeightMb FROM WxObs.ModelHeightLevels WHERE GFS=1 ORDER BY HeightMB DESC;";

		final int gribSpot = 3;
		
		JSONObject hrrr = new JSONObject();
		
		List<String> hFHList = new ArrayList<>();
		List<String> heightList = new ArrayList<>();
				
		try ( ResultSet resultSetHFH = wc.q2rs1c(dbc, hFHq, null); ) {
            while (resultSetHFH.next()) { hFHList.add(resultSetHFH.getString("FHour")); } 
        } catch (Exception e) { e.printStackTrace(); }
				
		try ( ResultSet resultSetHeights = wc.q2rs1c(dbc, heightQ, null); ) { 
            while (resultSetHeights.next()) { heightList.add(resultSetHeights.getString("HeightMb")); }
        } catch (Exception e) { e.printStackTrace(); }

		final String filters = wms.filters("g2fd")+"\\|"+wms.filters("g2fr");

		System.out.println("DEBUG pointInputAsString: "+pointInputAsString);

		for (String tFHourStr : hFHList) { 
		
			final int tFHour = Integer.parseInt(tFHourStr);
            final String enc = "_"+tFHour;
        	final String tFHour2D = String.format("%02d", tFHour);
			final String tFHour4D = String.format("%04d", tFHour);
			final File sounding = new File(xml2Path.getPath()+"/outHRRR"+tFHour4D+".csv");
			final File tFHGlob = new File(xml2Path.getPath()+"/fx"+tFHour4D);
			final File tFXCtlFile = new File(tFHGlob.getPath()+".ctl");
			final String tFHData = hrrrBase+"/hrrr.t"+getHour+"z.wrfprsf"+tFHour2D+".grib2";

			System.out.println(" -> Processing "+modelName+" data - FH +"+tFHour4D+"!");
			
			wc.runProcess("(\""+helpers.getPath()+"/get_inv.pl\" "+tFHData+".idx | grep \""+filters+"\" | \""+helpers.getPath()+"/get_grib.pl\" "+tFHData+" "+tFHGlob.getPath()+")");
                        
            try { wc.runProcessOutFile("\""+helpers.getPath()+"/wgrib2\" "+tFHGlob.getPath()+" "+pointInputAsString, sounding, false); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
			wc.sedFileReplace(sounding.getPath(), ":lon", ",lon");
			try { wc.runProcessOutFile("\""+helpers.getPath()+"/g2ctl\" "+tFHGlob.getPath(), tFXCtlFile, false); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
			wc.runProcess("\""+helpers.getPath()+"/gribmap\" -v -i "+tFXCtlFile.getPath());
                        
            Scanner hrrrScanner = null; try {		
                    hrrrScanner = new Scanner(sounding);
                    while(hrrrScanner.hasNext()) {
	                    final String line = hrrrScanner.nextLine();
	                    for(String sHeight : heightList) {
	                        final int tHt = Integer.valueOf(sHeight);
	                        final String tHe = tHt + enc;
	                        if(line.startsWith(gi.hrrrB("TC"+tHe)+":")) { try { hrrr.put("T"+tHe, gi.g2d(line, gribSpot, true)); } catch (Exception e) { e.printStackTrace(); hrrr.put("T"+tHe, ddv); } }
	                        if(line.startsWith(gi.hrrrB("RH"+tHe)+":")) { try { hrrr.put("RH"+tHe, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); hrrr.put("RH"+tHe, ddv); } }
	                        if(line.startsWith(gi.hrrrB("WU"+tHe)+":")) { try { hrrr.put("WU"+tHe, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); hrrr.put("WU"+tHe, ddv); } }
	                        if(line.startsWith(gi.hrrrB("WV"+tHe)+":")) { try { hrrr.put("WV"+tHe, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); hrrr.put("WV"+tHe, ddv); } }
	                    }
	                    if(line.startsWith(gi.hrrrB("CAPE"+enc)+":")) { try { hrrr.put("CAPE"+enc, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); hrrr.put("CAPE"+enc, ddv); } }
	                    if(line.startsWith(gi.hrrrB("CIN"+enc)+":")) { try { hrrr.put("CIN"+enc, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); hrrr.put("CIN"+enc, ddv); } }
	                    if(line.startsWith(gi.hrrrB("LI"+enc)+":")) { try { hrrr.put("LI"+enc, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); hrrr.put("LI"+enc, ddv); } }
	                    if(line.startsWith(gi.hrrrB("PWAT"+enc)+":")) { try { hrrr.put("PWAT"+enc, gi.g2d(line, gribSpot, false)*0.03937); } catch (Exception e) { e.printStackTrace(); hrrr.put("PWAT"+enc, ddv); } }
	                    if(line.startsWith(gi.hrrrB("HGT500"+enc)+":")) { try { hrrr.put("HGT500"+enc, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); hrrr.put("HGT500"+enc, ddv); } }
	            }
            }
            catch (FileNotFoundException fnf) { fnf.printStackTrace(); }

            for(String sHeight : heightList) {
                final int tHt = Integer.valueOf(sHeight);
                final String tHe = tHt + enc;
                try { 
                    if(hrrr.has("T"+tHe) && hrrr.has("RH"+tHe)) { hrrr.put("D"+tHe, wms.calcDwpt(hrrr.getDouble("T"+tHe), hrrr.getDouble("RH"+tHe))); } else { hrrr.put("D"+tHe, ddv); }
                    if(hrrr.has("WU"+tHe) && hrrr.has("WV"+tHe)) { hrrr.put("WD"+tHe, wms.windDirCalc(hrrr.getDouble("WU"+tHe), hrrr.getDouble("WV"+tHe))); } else { hrrr.put("WD"+tHe, ddv); }
                    if(hrrr.has("WU"+tHe) && hrrr.has("WV"+tHe)) { hrrr.put("WS"+tHe, wms.windSpdCalc(hrrr.getDouble("WU"+tHe), hrrr.getDouble("WV"+tHe))); } else { hrrr.put("WS"+tHe, ddv); }
                } catch (Exception e) {
                    e.printStackTrace();
                }            
            }
            
            if(hrrr.has("PWAT"+enc)) { hrrr.put("PRATE"+enc, hrrr.getDouble("PWAT"+enc)); }
            
			wc.runProcess("(echo \"run "+helpers.getPath()+"/ModelData.gs "+modelName+" "+tFHour4D+" "+getDate+" "+getHour+" "+xml2Path.getPath()+"\" | "+helpers.getPath()+"/grads -blc \"open "+tFXCtlFile.getPath()+"\")");
		
			tFHGlob.delete();
		
		}
			
		String thisJSONstring = hrrr.toString();
		thisJSONstring = thisJSONstring.substring(0, thisJSONstring.length()-1)+",";
		try { wc.varToFile(thisJSONstring, jsonOutFile, true); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
		System.out.println(" -> Completed: "+modelName+" - Round: "+round);
			
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
                
	}
	
}
