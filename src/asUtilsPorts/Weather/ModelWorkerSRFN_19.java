/*
by Anthony Stump
Created: 17 Oct 2017
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

public class ModelWorkerSRFN_19 {
	
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
		final File jsonOutFile = new File(xml2Path.getPath()+"/srfnOut_"+round+".json");
		final String pointInputAsString = ModelShare.pointInputAsString("KOJC");				
		final String modelName = "SRFN";
		final String srefBase= wmb.getNomadsBase() + "/sref/prod/sref."+getDate+"/"+getHour+"/pgrb";
		final String sFHq = "SELECT FHour FROM WxObs.GFSFHA WHERE DoGet=1 AND SRFA=1 AND Round="+round+";";
		final String heightQ = "SELECT HeightMb FROM WxObs.ModelHeightLevels WHERE SRFA=1 ORDER BY HeightMB DESC;";
		final int gribSpot = 3;
		
		JSONObject srfn = new JSONObject();
		
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
			final File sounding = new File(xml2Path.getPath()+"/outSRFN_"+tFHour4D+".csv");
			final File tFHGlob = new File(xml2Path.getPath()+"/ft"+tFHour4D);
			final File tFHCtlFile = new File(tFHGlob.getPath()+".ctl");
			final String tFHData = srefBase+"/sref_nmb.t"+getHour+"z.pgrb132.n1.f"+tFHour2D+".grib2";

			System.out.println(" -> Processing "+modelName+" data - FH +"+tFHour4D+"!");

			StumpJunk.runProcess("(\""+helpers.getPath()+"/get_inv.pl\" "+tFHData+".idx | grep \""+filters+"\" | \""+helpers.getPath()+"/get_grib.pl\" "+tFHData+" "+tFHGlob.getPath()+")");
			try { StumpJunk.runProcessOutFile("\""+appPath.getPath()+"/wgrib2\" "+tFHGlob.getPath()+" "+pointInputAsString, sounding, false); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
			StumpJunk.sedFileReplace(sounding.getPath(), ":lon", ",lon");
			try { StumpJunk.runProcessOutFile("\""+appPath.getPath()+"/g2ctl\" "+tFHGlob, tFHCtlFile, false); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
			StumpJunk.runProcess("\""+appPath.getPath()+"/gribmap\" -v -i "+tFHCtlFile.getPath());
                        
			Scanner srfnScanner = null; try {		
				srfnScanner = new Scanner(sounding);
				while(srfnScanner.hasNext()) {
					final String line = srfnScanner.nextLine();
                    for(String sHeight : heightList) {
                            final int tHt = Integer.valueOf(sHeight);
                            final String tHe = tHt + enc;
                            if(line.startsWith(gi.srfX("TC"+tHe)+":")) { try { srfn.put("T"+tHe, gi.g2d(line, gribSpot, true)); } catch (Exception e) { e.printStackTrace(); srfn.put("T"+tHe, ddv); } }
                            if(line.startsWith(gi.srfX("RH"+tHe)+":")) { try { srfn.put("RH"+tHe, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); srfn.put("RH"+tHe, ddv); } }
                            if(line.startsWith(gi.srfX("WU"+tHe)+".1:")) { try { srfn.put("WU"+tHe, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); srfn.put("WU"+tHe, ddv); } }
                            if(line.startsWith(gi.srfX("WU"+tHe)+".2:")) { try { srfn.put("WV"+tHe, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); srfn.put("WV"+tHe, ddv); } }
                    }
                    if(line.startsWith(gi.srfX("CAPE"+enc)+":")) { try { srfn.put("CAPE"+enc, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); srfn.put("CAPE"+enc, ddv); } }
                    if(line.startsWith(gi.srfX("CIN"+enc)+":")) { try { srfn.put("CIN"+enc, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); srfn.put("CIN"+enc, ddv); } }
                    if(line.startsWith(gi.srfX("LI"+enc)+":")) { try { srfn.put("LI"+enc, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); srfn.put("LI"+enc, ddv); } }
                    if(line.startsWith(gi.srfX("PWAT"+enc)+":")) { try { srfn.put("PWAT"+enc, gi.g2d(line, gribSpot, false)*0.03937); } catch (Exception e) { e.printStackTrace(); srfn.put("PWAT"+enc, ddv); } }
                    if(line.startsWith(gi.srfX("HGT500"+enc)+":")) { try { srfn.put("HGT500"+enc, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); srfn.put("HGT500"+enc, ddv); } } 				
				}
				
			}
			catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
		
            for(String sHeight : heightList) {
                final int tHt = Integer.valueOf(sHeight);
                final String tHe = tHt + enc;
                try { 
                    if(srfn.has("T"+tHe) && srfn.has("RH"+tHe)) { srfn.put("D"+tHe, wms.calcDwpt(srfn.getDouble("T"+tHe), srfn.getDouble("RH"+tHe))); } else { srfn.put("D"+tHe, ddv); }
                    if(srfn.has("WU"+tHe) && srfn.has("WV"+tHe)) { srfn.put("WD"+tHe, wms.windDirCalc(srfn.getDouble("WU"+tHe), srfn.getDouble("WV"+tHe))); } else { srfn.put("WD"+tHe, ddv); }
                    if(srfn.has("WU"+tHe) && srfn.has("WV"+tHe)) { srfn.put("WS"+tHe, wms.windSpdCalc(srfn.getDouble("WU"+tHe), srfn.getDouble("WV"+tHe))); } else { srfn.put("WS"+tHe, ddv); }
                } catch (Exception e) {
                    e.printStackTrace();
                }            
            }
            
            if(srfn.has("PWAT"+enc)) { srfn.put("PRATE"+enc, srfn.getDouble("PWAT"+enc)); }
            
            StumpJunk.runProcess("(echo \"run "+helpers.getPath()+"/ModelData.gs "+modelName+" "+tFHour4D+" "+getDate+" "+getHour+" "+xml2Path.getPath()+"\" | "+appPath.getPath()+"/grads -blc \"open "+tFHCtlFile.getPath()+"\")");								

			tFHGlob.delete();
			
		}

		String thisJSONstring = srfn.toString();
		thisJSONstring = thisJSONstring.substring(0, thisJSONstring.length()-1)+",";
		try { StumpJunk.varToFile(thisJSONstring, jsonOutFile, true); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
		System.out.println(" -> Completed: "+modelName+" - Round: "+round);
                
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
		
	}
}
