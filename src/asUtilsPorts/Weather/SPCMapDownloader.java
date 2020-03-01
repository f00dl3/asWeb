/*
By Anthony Stump
Created: 29 Feb 2020
Updated: 1 Mar 2020
*/

package asUtilsPorts.Weather;

import java.io.File;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;

public class SPCMapDownloader {
	
	private final String baseUrl = "https://www.spc.noaa.gov/obswx/maps/";
	
	public String getByDay(String dateIn) {

		CommonBeans cb = new CommonBeans();
		WebCommon wc = new WebCommon();
		
		final String outPath = cb.getPersistTomcat()+"/Get/SPCMaps";
		final File outPathFO_sfc = new File(outPath+"/sfc");
		final File outPathFO_850 = new File(outPath+"/850");
		final File outPathFO_500 = new File(outPath+"/500");
		
		try {
			if(!outPathFO_sfc.exists()) { outPathFO_sfc.mkdirs(); } 
			if(!outPathFO_850.exists()) { outPathFO_850.mkdirs(); } 
			if(!outPathFO_500.exists()) { outPathFO_500.mkdirs(); }
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		final String file00z_sfc = "sfc_" + dateIn + "_00.gif";
		final String file12z_sfc = "sfc_" + dateIn + "_12.gif";
		final String file00z_850 = "850_" + dateIn + "_00.gif";
		final String file12z_850 = "850_" + dateIn + "_12.gif";
		final String file00z_500 = "500_" + dateIn + "_00.gif";
		final String file12z_500 = "500_" + dateIn + "_12.gif";
		
		final String url00z_sfc = baseUrl + file00z_sfc;
		final String url12z_sfc = baseUrl + file12z_sfc;
		final String url00z_850 = baseUrl + file00z_850;
		final String url12z_850 = baseUrl + file12z_850;
		final String url00z_500 = baseUrl + file00z_500;
		final String url12z_500 = baseUrl + file12z_500;

        try { wc.jsoupOutBinary(url00z_sfc, new File(outPathFO_sfc+"/"+file00z_sfc), 5.0); } catch (Exception e) { e.printStackTrace(); }
        try { wc.jsoupOutBinary(url12z_sfc, new File(outPathFO_sfc+"/"+file12z_sfc), 5.0); } catch (Exception e) { e.printStackTrace(); }
        try { wc.jsoupOutBinary(url00z_850, new File(outPathFO_850+"/"+file00z_850), 5.0); } catch (Exception e) { e.printStackTrace(); }
        try { wc.jsoupOutBinary(url12z_850, new File(outPathFO_850+"/"+file12z_850), 5.0); } catch (Exception e) { e.printStackTrace(); }
        try { wc.jsoupOutBinary(url00z_500, new File(outPathFO_500+"/"+file00z_500), 5.0); } catch (Exception e) { e.printStackTrace(); }
        try { wc.jsoupOutBinary(url12z_500, new File(outPathFO_500+"/"+file12z_500), 5.0); } catch (Exception e) { e.printStackTrace(); }

		String returnData = "DONE: " + url00z_sfc +
				"\nDONE: " + url12z_sfc +
				"\nDONE: " + url00z_850 +
				"\nDONE: " + url12z_850 +
				"\nDONE: " + url00z_500 +
				"\nDONE: " + url12z_500;
	
		return returnData;	
			
	}
	
	public String getToday() {

		DateTime tDateTime = new DateTime();
		final DateTimeFormatter tDtf = DateTimeFormat.forPattern("YYMMdd");
		final String dateString = tDtf.print(tDateTime);
		return getByDay(dateString);
		
	}

	public String getYesterday() {

		DateTime tDateTime = new DateTime().minusDays(1);
		final DateTimeFormatter tDtf = DateTimeFormat.forPattern("YYMMdd");
		final String dateString = tDtf.print(tDateTime);
		return getByDay(dateString);
		
	}
	
	public String goBackXdays(String days) {

		String rData = "";
		
		int daysToGoBack = Integer.valueOf(days);
		for(int i = 0; i < daysToGoBack; i++) {
			DateTime tDateTime = new DateTime().minusDays(i);
			final DateTimeFormatter tDtf = DateTimeFormat.forPattern("YYMMdd");
			final String dateString = tDtf.print(tDateTime);
			rData += getByDay(dateString);				
		}
		
		return rData;
		
	}
	
}
