/*
by Anthony Stump
Created: 4 Oct 2017
Updated: 2 Jan 2020
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

import asUtils.Shares.JunkyBeans;

public class ModelWorkerCMC {
	
	public void main(String getHour, String round) {
		
        MyDBConnector mdb = new MyDBConnector();
        WebCommon wc = new WebCommon();
        
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
        JunkyBeans junkyBeans = new JunkyBeans();
        ModelShare wms = new ModelShare();
        Grib2Iterators gi = new Grib2Iterators();
        ModelBeans wmb = new ModelBeans();
    
        final double ddv = wmb.getDefaultDataValue();
		final DateTime tDateTime = new DateTime(DateTimeZone.UTC).minusHours(4);
		final DateTimeFormatter getDateFormat = DateTimeFormat.forPattern("yyyyMMdd");
		final String getDate = getDateFormat.print(tDateTime);
		final File appPath = junkyBeans.getAppShareSys();
		final File xml2Path = wmb.getXml2Path();
		final File helpers = junkyBeans.getHelpers();
		final String pointInputAsString = wms.pointInputAsString("KOJC");
		final File jsonOutFile = new File(xml2Path+"/cmcOut_"+round+".json");
		final String modelName = "CMC";
		final String cmcBase = wmb.getCanadianBase()+"/model_gem_global/25km/grib2/lat_lon/"+getHour;
		final String cFHq = "SELECT FHour FROM WxObs.GFSFHA WHERE DoGet=1 AND CMC=1 AND Round="+round+";";
		final String vCMCq = "SELECT VarName FROM WxObs.CMCModelVarsV2 WHERE HeightLoop=0;";
		final String vlCMCq = "SELECT VarName FROM WxObs.CMCModelVarsV2 WHERE HeightLoop=1;";
		final String heightQ = "SELECT HeightMb FROM WxObs.ModelHeightLevels WHERE CMC=1 ORDER BY HeightMB DESC;";

		final int gribSpot = 3;
		
		JSONObject cmc = new JSONObject();
		
		List<String> cFHList = new ArrayList<>();
		List<String> vCMC = new ArrayList<>();
		List<String> vlCMC = new ArrayList<>();
		List<String> heightList = new ArrayList<>();
		
		try ( ResultSet resultSetCFH = wc.q2rs1c(dbc, cFHq, null); ) {
            while (resultSetCFH.next()) { cFHList.add(resultSetCFH.getString("FHour")); } 
        } catch (Exception e) { e.printStackTrace(); }

		try ( ResultSet resultSetVcmc = wc.q2rs1c(dbc, vCMCq, null); ) {
            while (resultSetVcmc.next()) { vCMC.add(resultSetVcmc.getString("VarName")); } 
        } catch (Exception e) { e.printStackTrace(); }

		try ( ResultSet resultSetVLcmc = wc.q2rs1c(dbc, vlCMCq, null); ) { 
            while (resultSetVLcmc.next()) { vlCMC.add(resultSetVLcmc.getString("VarName")); }
        } catch (Exception e) { e.printStackTrace(); }
				
		try ( ResultSet resultSetHeights = wc.q2rs1c(dbc, heightQ, null); ) { 
            while (resultSetHeights.next()) { heightList.add(resultSetHeights.getString("HeightMb")); }
        } catch (Exception e) { e.printStackTrace(); }

		for (String tFHourStr : cFHList) {
				
			final int tFHour = Integer.parseInt(tFHourStr);
            final String enc = "_"+tFHour;
			final String tFHour2D = String.format("%02d", tFHour);
			final String tFHour3D = String.format("%03d", tFHour);
			final String tFHour4D = String.format("%04d", tFHour);
			final File sounding = new File(xml2Path.getPath()+"/outCMC_"+tFHour4D+".csv");
			final File tFHGlob = new File(xml2Path.getPath()+"/fc"+tFHour4D);
			final File tFHCtlFile = new File(tFHGlob.getPath()+".ctl");

			for(String tVar : vlCMC) {
				for(String tHeight : heightList) {
					final String thisFHData = cmcBase+"/"+tFHour3D+"/CMC_glb_"+tVar+"_"+tHeight+"_latlon.24x.24_"+getDate+getHour+"_P"+tFHour3D+".grib2";
					final File thisFHFile = new File(tFHGlob.getPath()+"_"+tVar+"_"+tHeight+".part");
					System.out.println(thisFHData);
					wc.jsoupOutBinary(thisFHData, thisFHFile, wmb.getDownloadTimeout());
				}
			}
			
			for(String tVar : vCMC) {
				String thisFHData = cmcBase+"/"+tFHour3D+"/CMC_glb_"+tVar+"_latlon.24x.24_"+getDate+getHour+"_P"+tFHour3D+".grib2";
				File thisFHFile = new File(tFHGlob.getPath()+"_"+tVar+"_SFC_0.part");
				System.out.println(thisFHData);
				wc.jsoupOutBinary(thisFHData, thisFHFile, wmb.getDownloadTimeout());
			}
			
			wc.runProcess("(cat "+tFHGlob.getPath()+"*.part > "+tFHGlob.getPath()+")");
			wc.runProcess("rm "+tFHGlob.getPath()+"*.part");
			
			try { wc.runProcessOutFile("\""+appPath.getPath()+"/wgrib2\" "+tFHGlob.getPath()+" "+pointInputAsString, sounding, false); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
			wc.sedFileReplace(sounding.getPath(), ":lon", ",lon");
			try { wc.runProcessOutFile("\""+appPath.getPath()+"/g2ctl\" "+tFHGlob.getPath(), tFHCtlFile, false); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
			wc.runProcess("\""+appPath.getPath()+"/gribmap\" -v -i "+tFHCtlFile.getPath());
		        
			Scanner cmcScanner = null; try {		
				cmcScanner = new Scanner(sounding);
				while(cmcScanner.hasNext()) {
					final String line = cmcScanner.nextLine();
                    for(String sHeight : heightList) {
                            final int tHt = Integer.valueOf(sHeight);
                            final String tHe = tHt + enc;
                            if(line.startsWith(gi.cmc("TC"+tHe)+":")) { try { cmc.put("T"+tHe, gi.g2d(line, gribSpot, true)); } catch (Exception e) { e.printStackTrace(); cmc.put("tTC"+tHe, ddv); } }
                            if(line.startsWith(gi.cmc("RH"+tHe)+":")) { try { cmc.put("RH"+tHe, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); cmc.put("tRH"+tHe, ddv); } }
                            if(line.startsWith(gi.cmc("WU"+tHe)+":")) { try { cmc.put("WU"+tHe, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); cmc.put("tWU"+tHe, ddv); } }
                            if(line.startsWith(gi.cmc("WV"+tHe)+":")) { try { cmc.put("WV"+tHe, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); cmc.put("tWV"+tHe, ddv); } }
                    }
                    if(line.startsWith(gi.cmc("PRATE"+enc)+":")) { try { cmc.put("PRATE"+enc, gi.g2d(line, gribSpot, false)*0.03937); } catch (Exception e) { e.printStackTrace(); cmc.put("PWAT"+enc, ddv); } }
                    if(line.startsWith(gi.cmc("HGT500"+enc)+":")) { try { cmc.put("HGT500"+enc, gi.g2d(line, gribSpot, false)); } catch (Exception e) { e.printStackTrace(); cmc.put("HGT500"+enc, ddv); } }                              
				}
			} catch (Exception e) { e.printStackTrace(); }
                        
            
            for(String sHeight : heightList) {
                final int tHt = Integer.valueOf(sHeight);
                final String tHe = tHt + enc;
                try { 
                    if(cmc.has("T"+tHe) && cmc.has("RH"+tHe)) { cmc.put("D"+tHe, wms.calcDwpt(cmc.getDouble("T"+tHe), cmc.getDouble("RH"+tHe))); } else { cmc.put("D"+tHe, ddv); }
                    if(cmc.has("WU"+tHe) && cmc.has("WV"+tHe)) { cmc.put("WD"+tHe, wms.windDirCalc(cmc.getDouble("WU"+tHe), cmc.getDouble("WV"+tHe))); } else { cmc.put("WD"+tHe, ddv); }
                    if(cmc.has("WU"+tHe) && cmc.has("WV"+tHe)) { cmc.put("WS"+tHe, wms.windSpdCalc(cmc.getDouble("WU"+tHe), cmc.getDouble("WV"+tHe))); } else { cmc.put("WS"+tHe, ddv); }
                } catch (Exception e) {
                    e.printStackTrace();
                }            
            }
            
			wc.runProcess("(echo \"run "+helpers.getPath()+"/ModelData.gs "+modelName+" "+tFHour4D+" "+getDate+" "+getHour+" "+xml2Path.getPath()+"\" | "+appPath.getPath()+"/grads -blc \"open "+tFHCtlFile.getPath()+"\")");
			tFHGlob.delete();
		}

		String thisJSONstring = cmc.toString();
		thisJSONstring = thisJSONstring.substring(0, thisJSONstring.length()-1)+",";
		try { wc.varToFile(thisJSONstring, jsonOutFile, true); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
		System.out.println(" -> Completed: "+modelName+" - Round: "+round);

        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
                
	}
}
