/*
by Anthony Stump
Created: 26 Dec 2017
Updated: 30 Dec 2019
*/

package asUtilsPorts.Feed;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.util.Scanner;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;

public class CF6Daily {
    
    public String getCf6(Connection dbc, int daysBack) {

        CommonBeans cb = new CommonBeans();
        WebCommon wc = new WebCommon();
                
    	String rData = "";
    	
    	final String cf6DataURL_181004 = "http://w2.weather.gov/climate/getclimate.php?wfo=eax&pil=CF6&sid=mci";
		final String cf6DataURL = "https://w2.weather.gov/climate/getclimate.php?wfo=eax&pil=CF6&sid=mci";
		final File cf6File = new File(cb.getPathChartCache().toString()+"/cf6.txt");
		final DateTime tdt = new DateTime().minusDays(daysBack);
		final DateTimeFormatter tdtf = DateTimeFormat.forPattern("dd");
		final DateTimeFormatter sqlf = DateTimeFormat.forPattern("yyyy-MM-dd");
		
		String dom = tdtf.print(tdt);
		String dSQL = sqlf.print(tdt);
		String domSp = dom.replaceAll("\\G0", " ");
		
		wc.jsoupOutBinary(cf6DataURL, cf6File, 30.0);
		
		String processedLine = null;
		
		int tHigh = 0;
		int tLow = 0;
		int tAverage = 0;
		int tDFNorm = 0;
		int hdd = 0;
		int cdd = 0;
		int pFlag = 0;
		double liquid = 0.0;
		double snow = 0.0;
		double sDepth = 0.0;
		double wAvg = 0;
		int wMax = 0;
		int clouds = 0;
		String weather = null;
		
		Scanner cf6FileScanner = null; try {		
			cf6FileScanner = new Scanner(cf6File);
			while(cf6FileScanner.hasNext()) {				
				String line = cf6FileScanner.nextLine();
				if(line.startsWith(domSp)) {
					processedLine = line.replaceFirst(domSp, dSQL).replaceAll("       ", ",-").replaceAll(" +", ",");
					String[] lineTmp = processedLine.split(",");
					tHigh = Integer.parseInt(lineTmp[1]);
					tLow = Integer.parseInt(lineTmp[2]);
					tAverage = Integer.parseInt(lineTmp[3]);
					tDFNorm = Integer.parseInt(lineTmp[4]);
					hdd = Integer.parseInt(lineTmp[5]);
					cdd = Integer.parseInt(lineTmp[6]);
					liquid = Double.parseDouble(lineTmp[7].replaceAll("T","0.01").replaceAll("M","0.00"));
					if (liquid != 0.00) { pFlag = 1; }
					snow = Double.parseDouble(lineTmp[8].replaceAll("T","0.1").replaceAll("M","0.0"));
					sDepth = Double.parseDouble(lineTmp[9].replaceAll("T","0.1").replaceAll("M","0.0"));
					wAvg = Double.parseDouble(lineTmp[10]);
					wMax = Integer.parseInt(lineTmp[11]);
					clouds = Integer.parseInt(lineTmp[15]);
					weather = lineTmp[16].replaceAll("-","");
				}
			}
		} catch (FileNotFoundException e) { e.printStackTrace(); }
		
		System.out.println(processedLine+"\n\n");
		
		String cf6SQLQuery = "INSERT IGNORE INTO WxObs.CF6MCI ("
			+ "Date, High, Low, Average, DFNorm, HDD, CDD, PFlag,"
			+ " Liquid, Snow, SDepth, WAvg, WMax, Clouds, Weather, Auto, AutoAge"
			+ ") VALUES ("
			+ "'"+dSQL+"',"+tHigh+","+tLow+","+tAverage+","+tDFNorm+","+hdd+","+cdd+","+pFlag+","
			+ liquid+","+snow+","+sDepth+","+wAvg+","+wMax+","+clouds+",'"+weather+"',1,"+daysBack
			+ ");";
			
		System.out.println(cf6SQLQuery);

        try { wc.q2do1c(dbc, cf6SQLQuery, null); } catch (Exception e) { e.printStackTrace(); }
        
    	return cf6SQLQuery;
        
    }
    
}