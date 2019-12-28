/*
by Anthony Stump
Created: 27 Aug 2017
Updated: 28 Dec 2019
*/

package asUtilsPorts.Weather;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.MyDBConnector;
import asWebRest.shared.WebCommon;

import java.io.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class RadarWorker {

	public static void fetch(String thisRound) {
        
		MyDBConnector mdb = new MyDBConnector();
		
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
		CommonBeans cb = new CommonBeans();
		WebCommon wc = new WebCommon();
		
		final String ramDrive = cb.getRamPath()+"/wxRadarJ";
		final String radPath = cb.getPersistTomcat()+"/Get/Radar";
		
		final File rdObj = new File(ramDrive);
		final File radPathObj = new File(radPath);
		
		if(!rdObj.exists()) { try { rdObj.mkdirs(); } catch (Exception e) { e.printStackTrace(); } }
		if(!radPathObj.exists()) { try { radPathObj.mkdirs(); } catch (Exception e) { e.printStackTrace(); } }

		DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
		Date date = new Date();
		String thisTimestamp = dateFormat.format(date);

		String getRadarSetSQL = "SELECT Site FROM WxObs.RadarList WHERE Active=1 AND Round='"+thisRound+"' ORDER BY Site ASC;";

        try (
        	ResultSet resultSet = wc.q2rs1c(dbc, getRadarSetSQL, null);
		) {		
			while (resultSet.next()) {
				
				final String thisRad = resultSet.getString("Site");
				File thisPathObject = new File(ramDrive+"/"+thisRad);
				File thisDestPathObject = new File(radPath+"/"+thisRad+"/Archive");
				thisPathObject.mkdirs();
				thisDestPathObject.mkdirs();

				final File radAoutFile = new File(ramDrive+"/"+thisRad+"/radTmpB_"+thisRad+".gif");
				final File radBoutFile = new File(ramDrive+"/"+thisRad+"/radTmpV_"+thisRad+".gif");
				
				Thread gr1a = new Thread(() -> {
                    String radarURLa = null;
                    if(thisRad.equals("XXX")) { radarURLa = "https://radar.weather.gov/ridge/Conus/RadarImg/latest_radaronly.gif"; }
                    else { radarURLa = "https://radar.weather.gov/ridge/RadarImg/N0R/"+thisRad+"_N0R_0.gif"; }
                    WebCommon.jsoupOutBinary(radarURLa, radAoutFile, 5.0);
                });
				
				Thread gr1b = new Thread(() -> {
                    String radarURLb = null;
                    if(thisRad.equals("XXX")) { radarURLb = "https://radar.weather.gov/ridge/RadarImg/N0S/EAX_N0S_0.gif"; }
                    else { radarURLb = "https://radar.weather.gov/ridge/RadarImg/N0S/"+thisRad+"_N0S_0.gif"; }
                    WebCommon.jsoupOutBinary(radarURLb, radBoutFile, 5.0);
                });

				Thread grListA[] = { gr1a, gr1b };
				for (Thread thread : grListA) { thread.start(); }
				for (int i = 0; i < grListA.length; i++) { try { grListA[i].join(); } catch (InterruptedException nx) { nx.printStackTrace(); } }

				System.out.println("Processing: K"+thisRad);
				
				Thread gr2a = new Thread(() -> { WebCommon.runProcess("convert "+ramDrive+"/"+thisRad+"/radTmpB_"+thisRad+".gif -fill \"#576464\" -opaque \"#04E9E7\" "+ramDrive+"/"+thisRad+"/radTmpB_"+thisRad+".gif"); });
				Thread gr2b = new Thread(() -> { WebCommon.runProcess("convert "+ramDrive+"/"+thisRad+"/radTmpV_"+thisRad+".gif -fill \"#000000\" -opaque \"#9000A0\" "+ramDrive+"/"+thisRad+"/radTmpV_"+thisRad+".gif"); });
				Thread gr2c = new Thread(() -> { WebCommon.runProcess("convert "+ramDrive+"/"+thisRad+"/radTmpV_"+thisRad+".gif -fill \"#201B1E\" -opaque \"#7C977B\" "+ramDrive+"/"+thisRad+"/radTmpV_"+thisRad+".gif"); });
				Thread gr2d = new Thread(() -> { WebCommon.runProcess("convert "+ramDrive+"/"+thisRad+"/radTmpV_"+thisRad+".gif -fill \"#551616\" -opaque \"#987777\" "+ramDrive+"/"+thisRad+"/radTmpV_"+thisRad+".gif"); });
				Thread grListB[] = { gr2a, gr2b, gr2c, gr2d };
				for (Thread thread : grListB) { thread.start(); }
				for (int i = 0; i < grListB.length; i++) { try { grListB[i].join(); } catch (InterruptedException nx) { nx.printStackTrace(); } }

				WebCommon.copyFile(ramDrive+"/"+thisRad+"/radTmpB_"+thisRad+".gif", radPath+"/"+thisRad+"/_BLatest.gif");
				WebCommon.moveFile(ramDrive+"/"+thisRad+"/radTmpB_"+thisRad+".gif", radPath+"/"+thisRad+"/B"+thisTimestamp+".gif");
				WebCommon.moveFile(ramDrive+"/"+thisRad+"/radTmpV_"+thisRad+".gif", radPath+"/"+thisRad+"/V"+thisTimestamp+".gif");

				WebCommon.runProcess("(ls "+radPath+"/"+thisRad+"/B*.gif -t | head -n 16; ls "+radPath+"/"+thisRad+"/B*.gif)|sort|uniq -u| xargs -I '{}' mv '{}' "+radPath+"/"+thisRad+"/Archive");
				WebCommon.runProcess("(ls "+radPath+"/"+thisRad+"/V*.gif -t | head -n 16; ls "+radPath+"/"+thisRad+"/V*.gif)|sort|uniq -u| xargs -I '{}' mv '{}' "+radPath+"/"+thisRad+"/Archive");
				WebCommon.runProcess("find "+radPath+"/"+thisRad+"/ -size 0 -print0 |xargs -0 rm");
				
                if(thisRad.equals("EAX")) { 
                	WebCommon.runProcess("convert -delay 18 -loop 0 -dispose previous "+radPath+"/"+thisRad+"/B*.gif "+radPath+"/"+thisRad+"/_BLoop.gif");
                	WebCommon.runProcess("convert -delay 18 -loop 0 -dispose previous "+radPath+"/"+thisRad+"/V*.gif "+radPath+"/"+thisRad+"/_VLoop.gif");
                }			


			}

		} catch (Exception e) { }

		try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
		
	}

}
