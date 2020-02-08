/*
by Anthony Stump
Created: 13 Oct 2017
Updated: 29 Jan 2020
*/

package asUtilsPorts.Weather.Experimental;

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

public class ModelWorkerHRWN {
	
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
		final File jsonOutFile = new File(xml2Path+"/hrwnOut_"+round+".json");
		final String pointInputAsString = wms.pointInputAsString("KOJC");				
		final String modelName = "HRWN";
		final String hrwnBase= wmb.getNomadsBase() + "/hiresw/prod/hiresw."+getDate;
		final String bFHq = "SELECT FHour FROM WxObs.GFSFHA WHERE DoGet=1 AND HRWA=1 AND Round="+round+";";
		final String heightQ = "SELECT HeightMb FROM WxObs.ModelHeightLevels WHERE HRWA=1 ORDER BY HeightMB DESC;";

		final int gribSpot = 3;
		
		JSONObject hrwn = new JSONObject();
		
		List<String> aFHList = new ArrayList<>();
		List<String> heightList = new ArrayList<>();
		
		try ( ResultSet resultSetAFH = wc.q2rs1c(dbc, bFHq, null); ) { 
            while (resultSetAFH.next()) { aFHList.add(resultSetAFH.getString("FHour")); }
        } catch (Exception e) { e.printStackTrace(); }
		
		try ( ResultSet resultSetHeights = wc.q2rs1c(dbc, heightQ, null); ) { 
            while (resultSetHeights.next()) { heightList.add(resultSetHeights.getString("HeightMb")); }
        } catch (Exception e) { e.printStackTrace(); }

		final String filters = wms.filters("g2f")+"\\|"+wms.filters("g2fr");

		for (String tFHourStr : aFHList) {

			final int tFHour = Integer.parseInt(tFHourStr);
            final String enc = "_" + tFHour;
			final String tFHour2D = String.format("%02d", tFHour);
			final String tFHour4D = String.format("%04d", tFHour);
			final File sounding = new File(xml2Path.getPath()+"/outHRWN_"+tFHour4D+".csv");
			final File tFHGlob = new File(xml2Path.getPath()+"/fb"+tFHour4D);
			final File tFHCtlFile = new File(tFHGlob.getPath()+".ctl");
			final String tFHData = hrwnBase+"/hiresw.t"+getHour+"z.nmmb_5km.f"+tFHour2D+".conus.grib2";

			System.out.println(" -> Processing "+modelName+" data - FH +"+tFHour4D+"!");

			wc.runProcess("(\""+helpers.getPath()+"/get_inv.pl\" "+tFHData+".idx | grep \""+filters+"\" | \""+helpers.getPath()+"/get_grib.pl\" "+tFHData+" "+tFHGlob.getPath()+")");
			try { wc.runProcessOutFile("\""+helpers.getPath()+"/wgrib2\" "+tFHGlob.getPath()+" "+pointInputAsString, sounding, false); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
			wc.sedFileReplace(sounding.getPath(), ":lon", ",lon");
			try { wc.runProcessOutFile("\""+helpers.getPath()+"/g2ctl\" "+tFHGlob.getPath(), tFHCtlFile, false); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
			wc.runProcess("\""+helpers.getPath()+"/gribmap\" -v -i "+tFHCtlFile.getPath());

            JSONObject md = new JSONObject();
                        
			Scanner hrwnScanner = null; try {		
				hrwnScanner = new Scanner(sounding);
				while(hrwnScanner.hasNext()) {
					final String line = hrwnScanner.nextLine();
                    for(String sHeight : heightList) {
                            final int tHt = Integer.valueOf(sHeight);
                            final String tHe = tHt + enc;
                            if(line.startsWith(gi.hrwX("TC"+tHe)+":")) { try { hrwn.put("T"+tHe, gi.g2d(line, gribSpot, true)); } catch (Exception e) { e.printStackTrace(); hrwn.put("T"+tHe, ddv); } }
                            if(line.startsWith(gi.hrwX("RH"+tHe)+":")) { try { hrwn.put("RH"+tHe, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); hrwn.put("RH"+tHe, ddv); } }
                            if(line.startsWith(gi.hrwX("WU"+tHe)+".1:")) { try { hrwn.put("WU"+tHe, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); hrwn.put("WU"+tHe, ddv); } }
                            if(line.startsWith(gi.hrwX("WU"+tHe)+".2:")) { try { hrwn.put("WV"+tHe, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); hrwn.put("WV"+tHe, ddv); } }
                    }
                    if(line.startsWith(gi.hrwX("CAPE"+enc)+":")) { try { hrwn.put("CAPE"+enc, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); hrwn.put("CAPE"+enc, ddv); } }
                    if(line.startsWith(gi.hrwX("CIN"+enc)+":")) { try { hrwn.put("CIN"+enc, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); hrwn.put("CIN"+enc, ddv); } }
                    if(line.startsWith(gi.hrwX("LI"+enc)+":")) { try { hrwn.put("LI"+enc, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); hrwn.put("LI"+enc, ddv); } }
                    if(line.startsWith(gi.hrwX("PWAT"+enc)+":")) { try { hrwn.put("PWAT"+enc, gi.g2d(line, gribSpot, false)*0.03937); } catch (Exception e) { e.printStackTrace(); hrwn.put("PWAT"+enc, ddv); } }
                    if(line.startsWith(gi.hrwX("HGT500"+enc)+":")) { try { hrwn.put("HGT500"+enc, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); hrwn.put("HGT500"+enc, ddv); } } 
    			}			
			}
			catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
			
            for(String sHeight : heightList) {
                final int tHt = Integer.valueOf(sHeight);
                final String tHe = tHt + enc;
                try { 
                    if(hrwn.has("T"+tHe) && hrwn.has("RH"+tHe)) { hrwn.put("D"+tHe, wms.calcDwpt(hrwn.getDouble("T"+tHe), hrwn.getDouble("RH"+tHe))); } else { hrwn.put("D"+tHe, ddv); }
                    if(hrwn.has("WU"+tHe) && hrwn.has("WV"+tHe)) { hrwn.put("WD"+tHe, wms.windDirCalc(hrwn.getDouble("WU"+tHe), hrwn.getDouble("WV"+tHe))); } else { hrwn.put("WD"+tHe, ddv); }
                    if(hrwn.has("WU"+tHe) && hrwn.has("WV"+tHe)) { hrwn.put("WS"+tHe, wms.windSpdCalc(hrwn.getDouble("WU"+tHe), hrwn.getDouble("WV"+tHe))); } else { hrwn.put("WS"+tHe, ddv); }
                } catch (Exception e) {
                    e.printStackTrace();
                }            
            }
            
            if(hrwn.has("PWAT"+enc)) { hrwn.put("PRATE"+enc, hrwn.getDouble("PWAT"+enc)); }
            
			wc.runProcess("(echo \"run "+helpers.getPath()+"/ModelData.gs "+modelName+" "+tFHour4D+" "+getDate+" "+getHour+" "+xml2Path.getPath()+"\" | "+helpers.getPath()+"/grads -blc \"open "+tFHCtlFile.getPath()+"\")");								
			
			tFHGlob.delete();
			
		}

		String thisJSONstring = hrwn.toString();
		thisJSONstring = thisJSONstring.substring(0, thisJSONstring.length()-1)+",";
		try { wc.varToFile(thisJSONstring, jsonOutFile, true); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
		System.out.println(" -> Completed: "+modelName+" - Round: "+round);
                
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
                
	}
}
