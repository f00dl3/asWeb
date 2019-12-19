/*
by Anthony Stump
Created: 4 Oct 2017
Updated: 19 Dec 2019
*/

package asUtilsPorts.Weather;

import asUtils.Shares.JunkyBeans;
import asUtils.Shares.StumpJunk;
import asUtils.Shares.MyDBConnector;
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

public class ModelWorkerGFS_19 {
	
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
		final String pointInputAsString = ModelShare.pointInputAsString("KOJC");
		final String modelName = "GFS";
		final File jsonOutFile = new File(xml2Path+"/gfsOut_"+round+".json");
		final String gfsBase = wmb.getNomadsBase() + "/gfs/prod/gfs."+getDate+getHour;
		final String gFHq = "SELECT FHour FROM WxObs.GFSFHA WHERE DoGet=1 AND GFS=1 AND Round="+round+";";
		final String heightQ = "SELECT HeightMb FROM WxObs.ModelHeightLevels WHERE GFS=1 ORDER BY HeightMB DESC;";
		final int gribSpot = 3;
		
		JSONObject gfs = new JSONObject();
		
		List<String> gFHList = new ArrayList<>();
		List<String> heightList = new ArrayList<>();
		
		try ( ResultSet resultSetGFH = mdb.q2rs1c(dbc, gFHq, null); ) {
            while (resultSetGFH.next()) { gFHList.add(resultSetGFH.getString("FHour")); } 
        } catch (Exception e) { e.printStackTrace(); }
                
		try ( ResultSet resultSetHeights = mdb.q2rs1c(dbc, heightQ, null); ) { 
            while (resultSetHeights.next()) { heightList.add(resultSetHeights.getString("HeightMb")); }
        } catch (Exception e) { e.printStackTrace(); }


		final String filters = ModelShare.filters("g2f");

		System.out.println("DEBUG pointInputAsString: "+pointInputAsString);

		for (String tFHourStr : gFHList) {

			final int tFHour = Integer.parseInt(tFHourStr);
            final String enc = "_"+tFHour;
			final String tFHour3D = String.format("%03d", tFHour);
			final String tFHour4D = String.format("%04d", tFHour);
			final File sounding = new File(xml2Path.getPath()+"/outGFS_"+tFHour4D+".csv");
			final File tFHGlob = new File(xml2Path.getPath()+"/fh"+tFHour4D);
			final File tFHCtlFile = new File(tFHGlob.getPath()+".ctl");
			final String tFHData = gfsBase+"/gfs.t"+getHour+"z.pgrb2.0p25.f"+tFHour3D;

			StumpJunk.runProcess("(\""+helpers.getPath()+"/get_inv.pl\" "+tFHData+".idx | grep \""+filters+"\" | \""+helpers.getPath()+"/get_grib.pl\" "+tFHData+" "+tFHGlob.getPath()+")");
			try { StumpJunk.runProcessOutFile("\""+appPath.getPath()+"/wgrib2\" "+tFHGlob.getPath()+" "+pointInputAsString, sounding, false); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
			StumpJunk.sedFileReplace(sounding.getPath(), ":lon", ",lon");
			try { StumpJunk.runProcessOutFile("\""+appPath.getPath()+"/g2ctl\" "+tFHGlob.getPath(), tFHCtlFile, false); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
			StumpJunk.runProcess("\""+appPath.getPath()+"/gribmap\" -v -i "+tFHCtlFile.getPath());
                        
			Scanner gfsScanner = null; try {		
				gfsScanner = new Scanner(sounding);
				while(gfsScanner.hasNext()) {
					final String line = gfsScanner.nextLine();
                    for(String sHeight : heightList) {
                            final int height = Integer.valueOf(sHeight);
                            final String tHe = height + enc;
                            if(line.startsWith(gi.gfs("TC"+tHe)+":")) { try { gfs.put("T"+tHe, gi.g2d(line, gribSpot, true)); } catch (Exception e) { e.printStackTrace(); gfs.put("T"+tHe, ddv); } }
                            if(line.startsWith(gi.gfs("RH"+tHe)+":")) { try { gfs.put("RH"+tHe, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); gfs.put("RH"+tHe, ddv); } }
                            if(line.startsWith(gi.gfs("WU"+tHe)+":")) { try { gfs.put("WU"+tHe, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); gfs.put("WU"+tHe, ddv); } }
                            if(line.startsWith(gi.gfs("WV"+tHe)+":")) { try { gfs.put("WV"+tHe, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); gfs.put("WV"+tHe, ddv); } }
                    }
                    if(line.startsWith(gi.gfs("CAPE"+enc)+":")) { try { gfs.put("CAPE"+enc, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); gfs.put("CAPE"+enc, ddv); } }
                    if(line.startsWith(gi.gfs("CIN"+enc)+":")) { try { gfs.put("CIN"+enc, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); gfs.put("CIN"+enc, ddv); } }
                    if(line.startsWith(gi.gfs("LI"+enc)+":")) { try { gfs.put("LI"+enc, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); gfs.put("LI"+enc, ddv); } }
                    if(line.startsWith(gi.gfs("PWAT"+enc)+":")) { try { gfs.put("PWAT"+enc, gi.g2d(line, gribSpot, false)*0.03937); } catch (Exception e) { e.printStackTrace(); gfs.put("PWAT"+enc, ddv); } }
                    if(line.startsWith(gi.gfs("HGT500"+enc)+":")) { try { gfs.put("HGT500"+enc, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); gfs.put("HGT500"+enc, ddv); } }        
				}
			}
			catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
				
            for(String sHeight : heightList) {
                final int tHt = Integer.valueOf(sHeight);
                final String tHe = tHt + enc;
                try { 
                    if(gfs.has("T"+tHe) && gfs.has("RH"+tHe)) { gfs.put("D"+tHe, wms.calcDwpt(gfs.getDouble("T"+tHe), gfs.getDouble("RH"+tHe))); } else { gfs.put("D"+tHe, ddv); }
                    if(gfs.has("WU"+tHe) && gfs.has("WV"+tHe)) { gfs.put("WD"+tHe, wms.windDirCalc(gfs.getDouble("WU"+tHe), gfs.getDouble("WV"+tHe))); } else { gfs.put("WD"+tHe, ddv); }
                    if(gfs.has("WU"+tHe) && gfs.has("WV"+tHe)) { gfs.put("WS"+tHe, wms.windSpdCalc(gfs.getDouble("WU"+tHe), gfs.getDouble("WV"+tHe))); } else { gfs.put("WS"+tHe, ddv); }
                } catch (Exception e) {
                    e.printStackTrace();
                }            
            }
                        
			StumpJunk.runProcess("(echo \"run "+helpers.getPath()+"/ModelData.gs "+modelName+" "+tFHour4D+" "+getDate+" "+getHour+" "+xml2Path.getPath()+"\" | "+appPath.getPath()+"/grads -blc \"open "+tFHCtlFile.getPath()+"\")");
			
			tFHGlob.delete();
			
		}
			
		String thisJSONstring = gfs.toString();
		thisJSONstring = thisJSONstring.substring(0, thisJSONstring.length()-1)+",";
		try { StumpJunk.varToFile(thisJSONstring, jsonOutFile, true); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
		System.out.println(" -> Completed: "+modelName+" - Round: "+round);

        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
                
	}
	
}
