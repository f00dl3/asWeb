/*
by Anthony Stump
Created: 10 Sep 2017
Updated: 28 Dec 2019
*/

package asUtilsPorts.Weather;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;


public class RadarNightly {

	public static void process(Connection dbc) {
		
		CommonBeans cb = new CommonBeans();
		WebCommon wc = new WebCommon();

		final DateTime dtYesterday = new DateTime().minusDays(1);
		final DateTimeFormatter dtFormat = DateTimeFormat.forPattern("yyMMdd");
		final String getYesterday = dtFormat.print(dtYesterday);

		final String ramDrive = cb.getRamPath() + "/nRadar";
		final String radPath = cb.getPersistTomcat()+"/Get/Radar";
		
		final File rdObj = new File(ramDrive);
		final File radPathObj = new File(radPath);
		
		if(!rdObj.exists()) { try { rdObj.mkdirs(); } catch (Exception e) { e.printStackTrace(); } }
		if(!radPathObj.exists()) { try { radPathObj.mkdirs(); } catch (Exception e) { e.printStackTrace(); } }

        final String queryRadarList = "SELECT Site FROM WxObs.RadarList WHERE Active=1 ORDER BY Site ASC;";
        
        try {
        	ResultSet resultSet = wc.q2rs1c(dbc, queryRadarList, null);
            while (resultSet.next()) {
                String thisSite = resultSet.getString("Site");
                final File tmpRadPath = new File(ramDrive + "/" + thisSite);
                final File sourceFolder = new File(radPath + "/" + thisSite + "/Archive");
                tmpRadPath.mkdirs();
                WebCommon.runProcess("mv " + sourceFolder.toString() + "/*.gif " + tmpRadPath.toString());
            }            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        try { 
        	wc.zipThisFolder(rdObj, new File(radPath + "/MP4/" + getYesterday + ".Archived.zip"));
    	} catch (Exception e) { 
    		e.printStackTrace();
		}
        
        WebCommon.runProcess("(ls "+radPath+"/MP4/* -t | head -n 14; ls "+radPath+"/MP4/*)|sort|uniq -u|xargs rm");
                
	}

}
