/*
by Anthony Stump
Created: 17 Oct 2017
Updated: 19 Dec 2019
*/

package asUtilsPorts.Weather;

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
import asUtils.Shares.JunkyBeans;

public class ModelWorkerSRFA_19 {
	
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
		final File jsonOutFile = new File(xml2Path.getPath()+"/srfaOut_"+round+".json");
		final String pointInputAsString = ModelShare.pointInputAsString("KOJC");				
		final String modelName = "SRFA";
		final String srefBase= wmb.getNomadsBase() + "/sref/prod/sref."+getDate+"/"+getHour+"/pgrb";
		
		final String sFHq = "SELECT FHour FROM WxObs.GFSFHA WHERE DoGet=1 AND SRFA=1 AND Round="+round+";";
		final String heightQ = "SELECT HeightMb FROM WxObs.ModelHeightLevels WHERE SRFA=1 ORDER BY HeightMB DESC;";

		final int gribSpot = 3;
		
		JSONObject srfa = new JSONObject();
		
		List<String> sFHList = new ArrayList<>();
		List<String> heightList = new ArrayList<>();
		
		try ( ResultSet resultSetSFH = mdb.q2rs1c(dbc, sFHq, null); ) { 
            while (resultSetSFH.next()) { sFHList.add(resultSetSFH.getString("FHour")); }
        } catch (Exception e) { e.printStackTrace(); }
		
		try ( ResultSet resultSetHeights = mdb.q2rs1c(dbc, heightQ, null); ) { 
            while (resultSetHeights.next()) { heightList.add(resultSetHeights.getString("HeightMb")); }
        } catch (Exception e) { e.printStackTrace(); }

		final String filters = ModelShare.filters("g2f")+"\\|"+ModelShare.filters("g2fr");

		for (String tFHourStr : sFHList) {

			final int tFHour = Integer.parseInt(tFHourStr);
                        final String enc = "_"+tFHour;
			final String tFHour2D = String.format("%02d", tFHour);
			final String tFHour3D = String.format("%03d", tFHour);
			final String tFHour4D = String.format("%04d", tFHour);
			final File sounding = new File(xml2Path.getPath()+"/outSRFA_"+tFHour4D+".csv");
			final File tFHGlob = new File(xml2Path.getPath()+"/fs"+tFHour4D);
			final File tFHCtlFile = new File(tFHGlob.getPath()+".ctl");
			final String tFHData = srefBase+"/sref_arw.t"+getHour+"z.pgrb132.n1.f"+tFHour2D+".grib2";

			System.out.println(" -> Processing "+modelName+" data - FH +"+tFHour4D+"!");

			StumpJunk.runProcess("(\""+helpers.getPath()+"/get_inv.pl\" "+tFHData+".idx | grep \""+filters+"\" | \""+helpers.getPath()+"/get_grib.pl\" "+tFHData+" "+tFHGlob.getPath()+")");
			try { StumpJunk.runProcessOutFile("\""+appPath.getPath()+"/wgrib2\" "+tFHGlob.getPath()+" "+pointInputAsString, sounding, false); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
			StumpJunk.sedFileReplace(sounding.getPath(), ":lon", ",lon");
			try { StumpJunk.runProcessOutFile("\""+appPath.getPath()+"/g2ctl\" "+tFHGlob, tFHCtlFile, false); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
			StumpJunk.runProcess("\""+appPath.getPath()+"/gribmap\" -v -i "+tFHCtlFile.getPath());

                        JSONObject md = new JSONObject();
                        
			Scanner srfaScanner = null; try {		
				srfaScanner = new Scanner(sounding);
				while(srfaScanner.hasNext()) {
					final String line = srfaScanner.nextLine();				
                    for(String sHeight : heightList) {
                            final int tHt = Integer.valueOf(sHeight);
                            final String tHe = tHt + enc;
                            if(line.startsWith(gi.srfX("TC"+tHe)+":")) { try { srfa.put("T"+tHe, gi.g2d(line, gribSpot, true)); } catch (Exception e) { e.printStackTrace(); srfa.put("T"+tHe, ddv); } }
                            if(line.startsWith(gi.srfX("RH"+tHe)+":")) { try { srfa.put("RH"+tHe, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); srfa.put("RH"+tHe, ddv); } }
                            if(line.startsWith(gi.srfX("WU"+tHe)+".1:")) { try { srfa.put("WU"+tHe, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); srfa.put("WU"+tHe, ddv); } }
                            if(line.startsWith(gi.srfX("WU"+tHe)+".2:")) { try { srfa.put("WV"+tHe, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); srfa.put("WV"+tHe, ddv); } }
                    }
                    if(line.startsWith(gi.srfX("CAPE"+enc)+":")) { try { srfa.put("CAPE"+enc, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); srfa.put("CAPE"+enc, ddv); } }
                    if(line.startsWith(gi.srfX("CIN"+enc)+":")) { try { srfa.put("CIN"+enc, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); srfa.put("CIN"+enc, ddv); } }
                    if(line.startsWith(gi.srfX("LI"+enc)+":")) { try { srfa.put("LI"+enc, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); srfa.put("LI"+enc, ddv); } }
                    if(line.startsWith(gi.srfX("PWAT"+enc)+":")) { try { srfa.put("PWAT"+enc, gi.g2d(line, gribSpot, false)*0.03937); } catch (Exception e) { e.printStackTrace(); srfa.put("PWAT"+enc, ddv); } }
                    if(line.startsWith(gi.srfX("HGT500"+enc)+":")) { try { srfa.put("HGT500"+enc, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); srfa.put("HGT500"+enc, ddv); } }    	
				}
				
			}
			catch (FileNotFoundException fnf) { fnf.printStackTrace(); }

            for(String sHeight : heightList) {
                final int tHt = Integer.valueOf(sHeight);
                final String tHe = tHt + enc;
                try { 
                    if(srfa.has("T"+tHe) && srfa.has("RH"+tHe)) { srfa.put("D"+tHe, wms.calcDwpt(srfa.getDouble("T"+tHe), srfa.getDouble("RH"+tHe))); } else { srfa.put("D"+tHe, ddv); }
                    if(srfa.has("WU"+tHe) && srfa.has("WV"+tHe)) { srfa.put("WD"+tHe, wms.windDirCalc(srfa.getDouble("WU"+tHe), srfa.getDouble("WV"+tHe))); } else { srfa.put("WD"+tHe, ddv); }
                    if(srfa.has("WU"+tHe) && srfa.has("WV"+tHe)) { srfa.put("WS"+tHe, wms.windSpdCalc(srfa.getDouble("WU"+tHe), srfa.getDouble("WV"+tHe))); } else { srfa.put("WS"+tHe, ddv); }
                } catch (Exception e) {
                    e.printStackTrace();
                }            
            }
            
            if(srfa.has("PWAT"+enc)) { srfa.put("PRATE"+enc, srfa.getDouble("PWAT"+enc)); }
            
            StumpJunk.runProcess("(echo \"run "+helpers.getPath()+"/ModelData.gs "+modelName+" "+tFHour4D+" "+getDate+" "+getHour+" "+xml2Path.getPath()+"\" | "+appPath.getPath()+"/grads -blc \"open "+tFHCtlFile.getPath()+"\")");								
			
			tFHGlob.delete();
			
		}

		String thisJSONstring = srfa.toString();
		thisJSONstring = thisJSONstring.substring(0, thisJSONstring.length()-1)+",";
		try { StumpJunk.varToFile(thisJSONstring, jsonOutFile, true); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
		System.out.println(" -> Completed: "+modelName+" - Round: "+round);
                
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
		
	}
}
