/*
by Anthony Stump
Created: 12 Oct 2017
Updated: 29 Jan 2020
*/

package asUtilsPorts.Weather;

import asWebRest.shared.MyDBConnector;
import asWebRest.shared.WebCommon;

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

public class ModelWorkerHRWA {
	
	public void main(String getHour, String round) {

        ModelBeans wmb = new ModelBeans();
        ModelShare wms = new ModelShare();
		MyDBConnector mdb = new MyDBConnector();
        Grib2Iterators gi = new Grib2Iterators();
        JunkyBeans junkyBeans = new JunkyBeans();
        WebCommon wc = new WebCommon();
        
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
      
        final double ddv = wmb.getDefaultDataValue();  
		final DateTime tDateTime = new DateTime(DateTimeZone.UTC).minusHours(4);
		final DateTimeFormatter getDateFormat = DateTimeFormat.forPattern("yyyyMMdd");
		final String getDate = getDateFormat.print(tDateTime);
		final File xml2Path = wmb.getXml2Path();
		final File helpers = junkyBeans.getHelpers();
		final File jsonOutFile = new File(xml2Path.getPath()+"/hrwaOut_"+round+".json");
		final String pointInputAsString = wms.pointInputAsString("KOJC");				
		final String modelName = "HRWA";
		final String hrwaBase= wmb.getNomadsBase()+"/hiresw/prod/hiresw."+getDate;
		final String aFHq = "SELECT FHour FROM WxObs.GFSFHA WHERE DoGet=1 AND HRWA=1 AND Round="+round+";";
		final String heightQ = "SELECT HeightMb FROM WxObs.ModelHeightLevels WHERE HRWA=1 ORDER BY HeightMB DESC;";

		final int gribSpot = 3;
		
		JSONObject hrwa = new JSONObject();
		
		List<String> aFHList = new ArrayList<>();
		List<String> heightList = new ArrayList<>();
		
		
		try ( ResultSet resultSetAFH = wc.q2rs1c(dbc, aFHq, null); ) { 
            while (resultSetAFH.next()) { aFHList.add(resultSetAFH.getString("FHour")); }
        } catch (Exception e) { e.printStackTrace(); }
		
		try ( ResultSet resultSetHeights = wc.q2rs1c(dbc, heightQ, null); ) { 
            while (resultSetHeights.next()) { heightList.add(resultSetHeights.getString("HeightMb")); }
        } catch (Exception e) { e.printStackTrace(); }
                
		final String filters = wms.filters("g2f")+"\\|"+wms.filters("g2fr");

		for (String tFHourStr : aFHList) {

			final int tFHour = Integer.parseInt(tFHourStr);
            final String enc = "_"+tFHour;
			final String tFHour2D = String.format("%02d", tFHour);
			final String tFHour3D = String.format("%03d", tFHour);
			final String tFHour4D = String.format("%04d", tFHour);
			final File sounding = new File(xml2Path.getPath()+"/outHRWA_"+tFHour4D+".csv");
			final File tFHGlob = new File(xml2Path.getPath()+"/fa"+tFHour4D);
			final File tFHCtlFile = new File(tFHGlob.getPath()+".ctl");
			final String tFHData = hrwaBase+"/hiresw.t"+getHour+"z.arw_5km.f"+tFHour2D+".conus.grib2";

			System.out.println(" -> Processing "+modelName+" data - FH +"+tFHour4D+"!");

			wc.runProcess("(\""+helpers.getPath()+"/get_inv.pl\" "+tFHData+".idx | grep \""+filters+"\" | \""+helpers.getPath()+"/get_grib.pl\" "+tFHData+" "+tFHGlob.getPath()+")");
			try { wc.runProcessOutFile("\""+helpers.getPath()+"/wgrib2\" "+tFHGlob.getPath()+" "+pointInputAsString, sounding, false); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
			wc.sedFileReplace(sounding.getPath(), ":lon", ",lon");
			try { wc.runProcessOutFile("\""+helpers.getPath()+"/g2ctl\" "+tFHGlob, tFHCtlFile, false); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
			wc.runProcess("\""+helpers.getPath()+"/gribmap\" -v -i "+tFHCtlFile.getPath()); 
                        
			Scanner hrwaScanner = null; try {		
				hrwaScanner = new Scanner(sounding);
				while(hrwaScanner.hasNext()) {
					final String line = hrwaScanner.nextLine();
                    for(String sHeight : heightList) {
                            final int tHt = Integer.valueOf(sHeight);
                            final String tHe = tHt + enc;
                            if(line.startsWith(gi.hrwX("TC"+tHe)+":")) { try { hrwa.put("T"+tHe, gi.g2d(line, gribSpot, true)); } catch (Exception e) { e.printStackTrace(); hrwa.put("T"+tHe, ddv); } }
                            if(line.startsWith(gi.hrwX("RH"+tHe)+":")) { try { hrwa.put("RH"+tHe, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); hrwa.put("RH"+tHe, ddv); } }
                            if(line.startsWith(gi.hrwX("WU"+tHe)+".1:")) { try { hrwa.put("WU"+tHe, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); hrwa.put("WU"+tHe, ddv); } }
                            if(line.startsWith(gi.hrwX("WU"+tHe)+".2:")) { try { hrwa.put("WV"+tHe, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); hrwa.put("WV"+tHe, ddv); } }
                    }
                    if(line.startsWith(gi.hrwX("CAPE"+enc)+":")) { try { hrwa.put("CAPE"+enc, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); hrwa.put("CAPE"+enc, ddv); } }
                    if(line.startsWith(gi.hrwX("CIN"+enc)+":")) { try { hrwa.put("CIN"+enc, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); hrwa.put("CIN"+enc, ddv); } }
                    if(line.startsWith(gi.hrwX("LI"+enc)+":")) { try { hrwa.put("LI"+enc, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); hrwa.put("LI"+enc, ddv); } }
                    if(line.startsWith(gi.hrwX("PWAT"+enc)+":")) { try { hrwa.put("PWAT"+enc, gi.g2d(line, gribSpot, false)*0.03937); } catch (Exception e) { e.printStackTrace(); hrwa.put("PWAT"+enc, ddv); } }
                    if(line.startsWith(gi.hrwX("HGT500"+enc)+":")) { try { hrwa.put("HGT500"+enc, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); hrwa.put("HGT500"+enc, ddv); } }                              
				}
			}
			catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
			
            for(String sHeight : heightList) {
                final int tHt = Integer.valueOf(sHeight);
                final String tHe = tHt + enc;
                try { 
                    if(hrwa.has("T"+tHe) && hrwa.has("RH"+tHe)) { hrwa.put("D"+tHe, wms.calcDwpt(hrwa.getDouble("T"+tHe), hrwa.getDouble("RH"+tHe))); } else { hrwa.put("D"+tHe, ddv); }
                    if(hrwa.has("WU"+tHe) && hrwa.has("WV"+tHe)) { hrwa.put("WD"+tHe, wms.windDirCalc(hrwa.getDouble("WU"+tHe), hrwa.getDouble("WV"+tHe))); } else { hrwa.put("WD"+tHe, ddv); }
                    if(hrwa.has("WU"+tHe) && hrwa.has("WV"+tHe)) { hrwa.put("WS"+tHe, wms.windSpdCalc(hrwa.getDouble("WU"+tHe), hrwa.getDouble("WV"+tHe))); } else { hrwa.put("WS"+tHe, ddv); }
                } catch (Exception e) {
                    e.printStackTrace();
                }            
            }
            
            if(hrwa.has("PWAT"+enc)) { hrwa.put("PRATE"+enc, hrwa.getDouble("PWAT"+enc)); }
                        
			wc.runProcess("(echo \"run "+helpers.getPath()+"/ModelData.gs "+modelName+" "+tFHour4D+" "+getDate+" "+getHour+" "+xml2Path.getPath()+"\" | "+helpers.getPath()+"/grads -blc \"open "+tFHCtlFile.getPath()+"\")");								
			
			tFHGlob.delete();
			
		}

		String thisJSONstring = hrwa.toString();
		thisJSONstring = thisJSONstring.substring(0, thisJSONstring.length()-1)+",";
		try { wc.varToFile(thisJSONstring, jsonOutFile, true); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
		System.out.println(" -> Completed: "+modelName+" - Round: "+round);
		
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
                
	}
}
