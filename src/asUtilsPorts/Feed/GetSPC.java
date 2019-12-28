/* 
by Anthony Stump
Created: 17 Aug 2017
Updated: 28 Dec 2019
*/

package asUtilsPorts.Feed;

import asUtils.Shares.JunkyBeans;
import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;

import java.io.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.Scanner;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class GetSPC {

	public static void doGetSPC(Connection dbc) {

		CommonBeans cb = new CommonBeans();
		WebCommon wc = new WebCommon();
               
		final PrintStream console = System.out;
		System.setOut(console);
	
		Date nowDate = new Date();
		DateFormat nowDateFormat = new SimpleDateFormat("yyMMdd");
		DateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");
		DateFormat yearFormat = new SimpleDateFormat("yyyy");
		String spcDate = nowDateFormat.format(nowDate);
		String spcSQLDate = sqlDateFormat.format(nowDate);
		String spcYear = yearFormat.format(nowDate);
		
		final DateTime dtdspcdy = new DateTime().minusDays(1);
		final DateTimeFormatter dtfspcdy = DateTimeFormat.forPattern("yyMMdd");
		final DateTimeFormatter dtfspcsqldy = DateTimeFormat.forPattern("yyyy-MM-dd");
		String spcDateY = dtfspcdy.print(dtdspcdy);
		String spcSQLDateY = dtfspcsqldy.print(dtdspcdy);
		

		final String spcBaseURL = "http://www.spc.noaa.gov/climo/reports/";
		final String tmpPathStr = cb.getPathChartCache().toString();
		final String mysqlShare = tmpPathStr;
		final File tmpPath = new File(tmpPathStr);
		
		tmpPath.mkdirs();
		
		File spcReportsTFile = new File(tmpPath+"/SPCReportsT.csv");
		File spcReportsHFile = new File(tmpPath+"/SPCReportsH.csv");
		File spcReportsWFile = new File(tmpPath+"/SPCReportsW.csv");
		File spcReportsYTFile = new File(tmpPath+"/SPCReportsYT.csv");
		File spcReportsYHFile = new File(tmpPath+"/SPCReportsYH.csv");
		File spcReportsYWFile = new File(tmpPath+"/SPCReportsYW.csv");
		File spcWWkmzFile = new File(tmpPath+"/ActiveWW.kmz");
		File spcMDkmzFile = new File(tmpPath+"/ActiveMD.kmz");

		Thread thA1 = new Thread(() -> { WebCommon.jsoupOutBinary(spcBaseURL+spcDate+"_rpts_filtered_torn.csv", spcReportsTFile, 5.0); });
		Thread thA2 = new Thread(() -> { WebCommon.jsoupOutBinary(spcBaseURL+spcDate+"_rpts_filtered_hail.csv", spcReportsHFile, 5.0); });
		Thread thA3 = new Thread(() -> { WebCommon.jsoupOutBinary(spcBaseURL+spcDate+"_rpts_filtered_wind.csv", spcReportsWFile, 5.0); });
		Thread thA4 = new Thread(() -> { WebCommon.jsoupOutBinary(spcBaseURL+spcDateY+"_rpts_filtered_torn.csv", spcReportsYTFile, 5.0); });
		Thread thA5 = new Thread(() -> { WebCommon.jsoupOutBinary(spcBaseURL+spcDateY+"_rpts_filtered_hail.csv", spcReportsYHFile, 5.0); });
		Thread thA6 = new Thread(() -> { WebCommon.jsoupOutBinary(spcBaseURL+spcDateY+"_rpts_filtered_wind.csv", spcReportsYWFile, 5.0); });
		Thread thA7 = new Thread(() -> { WebCommon.jsoupOutBinary("http://www.spc.noaa.gov/products/watch/ActiveWW.kmz", spcWWkmzFile, 5.0); });
		Thread thA8 = new Thread(() -> { WebCommon.jsoupOutBinary("http://www.spc.noaa.gov/products/md/ActiveMD.kmz", spcMDkmzFile, 5.0); });
		Thread thListA[] = { thA1, thA2, thA3, thA4, thA5, thA6, thA7, thA8 };
		for (Thread thread : thListA) { thread.start(); } 
		for (int i = 0; i < thListA.length; i++) { try { thListA[i].join(); } catch (InterruptedException nx) { nx.printStackTrace(); } }

		Thread thB1 = new Thread(() -> { wc.unzipFile(tmpPathStr+"/ActiveWW.kmz", tmpPathStr); });
		Thread thB2 = new Thread(() -> { wc.unzipFile(tmpPathStr+"/ActiveMD.kmz", tmpPathStr); });
		Thread thListB[] = { thB1, thB2 };
		for (Thread thread : thListB) { thread.start(); } 
		for (int i = 0; i < thListB.length; i++) { try { thListB[i].join(); } catch (InterruptedException nx) { nx.printStackTrace(); } }

		Thread thC1 = new Thread(() -> { WebCommon.sedFileDeleteFirstLine(tmpPath+"/SPCReportsT.csv"); });
		Thread thC2 = new Thread(() -> { WebCommon.sedFileDeleteFirstLine(tmpPath+"/SPCReportsH.csv"); });
		Thread thC3 = new Thread(() -> { WebCommon.sedFileDeleteFirstLine(tmpPath+"/SPCReportsW.csv"); });
		Thread thC4 = new Thread(() -> { WebCommon.sedFileDeleteFirstLine(tmpPath+"/SPCReportsYT.csv"); });
		Thread thC5 = new Thread(() -> { WebCommon.sedFileDeleteFirstLine(tmpPath+"/SPCReportsYH.csv"); });
		Thread thC6 = new Thread(() -> { WebCommon.sedFileDeleteFirstLine(tmpPath+"/SPCReportsYW.csv"); });
		Thread thListC[] = { thC1, thC2, thC3, thC4, thC5, thC6 };
		for (Thread thread : thListC) { thread.start(); } 
		for (int i = 0; i < thListC.length; i++) { try { thListC[i].join(); } catch (InterruptedException nx) { nx.printStackTrace(); } }

		File spcMDkmlFile = new File(tmpPath+"/ActiveMD.kml");
		File spcWWkmlFile = new File(tmpPath+"/ActiveWW.kml");

		WebCommon.sedFileInsertEachLineNew(tmpPath+"/SPCReportsT.csv","T,"+spcSQLDate+",",tmpPath+"/SPCReportsLive.csv");
		WebCommon.sedFileInsertEachLineNew(tmpPath+"/SPCReportsH.csv","H,"+spcSQLDate+",",tmpPath+"/SPCReportsLive.csv");
		WebCommon.sedFileInsertEachLineNew(tmpPath+"/SPCReportsW.csv","W,"+spcSQLDate+",",tmpPath+"/SPCReportsLive.csv");
		WebCommon.sedFileInsertEachLineNew(tmpPath+"/SPCReportsYT.csv","T,"+spcSQLDate+",",tmpPath+"/SPCReportsLive.csv");
		WebCommon.sedFileInsertEachLineNew(tmpPath+"/SPCReportsYH.csv","H,"+spcSQLDateY+",",tmpPath+"/SPCReportsLive.csv");
		WebCommon.sedFileInsertEachLineNew(tmpPath+"/SPCReportsYW.csv","W,"+spcSQLDateY+",",tmpPath+"/SPCReportsLive.csv");
		WebCommon.moveFile(tmpPath+"/SPCReportsLive.csv", mysqlShare+"/jSPCReportsLive.csv");

		String spcReportsSQL = "LOAD DATA LOCAL INFILE '"+mysqlShare+"/jSPCReportsLive.csv' REPLACE INTO TABLE WxObs.SPCReportsLive FIELDS TERMINATED BY ',' LINES TERMINATED BY '\\\n' (Type,Date,Time,Magnitude,Location,County,State,Lat,Lon,Comments) SET AssocID = CONCAT(Date, Time, Type, Lat, Lon);";		

		System.out.println(spcReportsSQL);

		spcReportsTFile.delete();
		spcReportsHFile.delete();
		spcReportsWFile.delete();
		spcReportsYTFile.delete();
		spcReportsYHFile.delete();
		spcReportsYWFile.delete();

		String spcMDsubURL = null;
		String spcMesoSQL = null;
		Scanner spcMDScanner = null; try {		
			spcMDScanner = new Scanner(spcMDkmlFile);
			while(spcMDScanner.hasNext()) {				
				String line = spcMDScanner.nextLine();
				if(line.contains("<href>")) {
					Pattern p = Pattern.compile("<href>(.*)</href>"); Matcher m = p.matcher(line);
					if (m.find()) {
						spcMDsubURL = m.group(1);
						String spcMDsubFileName = spcMDsubURL.substring(spcMDsubURL.lastIndexOf("/")+1);
						String spcMDsubFileNoExt = spcMDsubFileName.substring(0, spcMDsubFileName.lastIndexOf("."));
						File spcMDsubFile = new File(tmpPath+"/"+spcMDsubFileName);
						WebCommon.jsoupOutBinary(spcMDsubURL, spcMDsubFile, 5.0);
						wc.unzipFile(tmpPath+"/"+spcMDsubFileName, tmpPathStr);
						String thisMDFileStr = tmpPath+"/"+spcMDsubFileNoExt+".kml";
						File thisMDFile = new File(thisMDFileStr);
						System.out.println(thisMDFileStr);
						WebCommon.sedFileReplace(thisMDFileStr, "<coordinates>\\\n", "<coordinates>");
						WebCommon.sedFileReplace(thisMDFileStr, "</coordinates>\\\n", "</coordinates>");
						WebCommon.sedFileReplace(thisMDFileStr, ",0\\\n", "],[");
						String thisMDid = null;
						String thisMDgeo = null;
						Scanner thisMDScanner = null; try {		
							thisMDScanner = new Scanner(thisMDFile);
							while(thisMDScanner.hasNext()) {				
								String subLine = thisMDScanner.nextLine();
								if(subLine.contains("<name>")) { Pattern p2 = Pattern.compile("<name>(.*)</name>"); Matcher m2 = p2.matcher(subLine); if (m2.find()) { thisMDid = m2.group(1); }}
								if(subLine.contains("<coordinates>")) {
									Pattern p2 = Pattern.compile("<coordinates>(.*)</coordinates>");
									Matcher m2 = p2.matcher(subLine);
									if (m2.find()) {
										thisMDgeo = m2.group(1);
										thisMDgeo = ("["+thisMDgeo+"]").replaceAll(",\\[\\]","");
									}
								}
							}
						} catch (FileNotFoundException esf) { esf.printStackTrace(); }
						spcMesoSQL = "INSERT IGNORE INTO WxObs.SPCMesoscaleShape (mdID, Bounds) VALUES ('"+spcYear+" "+thisMDid+"','["+thisMDgeo+"]');";
						System.out.println(spcMesoSQL);
						thisMDFile.delete();
					} else {
						System.out.print("No active SPC MDs!");
					}
				}
			}			
		} catch (FileNotFoundException e) { e.printStackTrace(); }

		String spcWWsubURL = null;
		String spcWatchSQL = null;
		Scanner spcWWScanner = null; try {		
			spcWWScanner = new Scanner(spcWWkmlFile);
			while(spcWWScanner.hasNext()) {				
				String line = spcWWScanner.nextLine();
				if(line.contains("<href>")) {
					Pattern p = Pattern.compile("<href>(.*)</href>"); Matcher m = p.matcher(line);
					if (m.find()) {
						spcWWsubURL = m.group(1);
						String spcWWsubFileName = spcWWsubURL.substring(spcWWsubURL.lastIndexOf("/")+1);
						String spcWWsubFileNoExt = spcWWsubFileName.substring(0, spcWWsubFileName.lastIndexOf("."));
						File spcWWsubFile = new File(tmpPath+"/"+spcWWsubFileName);
						WebCommon.jsoupOutBinary(spcWWsubURL, spcWWsubFile, 5.0);
						wc.unzipFile(tmpPath+"/"+spcWWsubFileName, tmpPathStr);
						String thisWWFileStr = tmpPath+"/"+spcWWsubFileNoExt+".kml";
						File thisWWFile = new File(thisWWFileStr);
						System.out.println(thisWWFileStr);
						WebCommon.sedFileReplace(thisWWFileStr, "<coordinates>\\\n", "<coordinates>");
						WebCommon.sedFileReplace(thisWWFileStr, "</coordinates>\\\n", "</coordinates>");
						WebCommon.sedFileReplace(thisWWFileStr, ",0\\\n", "],[");
						String thisWWid = null;
						String thisWWgeo = null;
						Scanner thisWWScanner = null; try {		
							thisWWScanner = new Scanner(thisWWFile);
							while(thisWWScanner.hasNext()) {				
								String subLine = thisWWScanner.nextLine();
								if(subLine.contains("<name>")) { Pattern p2 = Pattern.compile("<name>(.*)</name>"); Matcher m2 = p2.matcher(subLine); if (m2.find()) { thisWWid = m2.group(1); }}
								if(subLine.contains("<coordinates>")) {
									Pattern p2 = Pattern.compile("<coordinates>(.*)</coordinates>");
									Matcher m2 = p2.matcher(subLine);
									if (m2.find()) {
										thisWWgeo = m2.group(1);
										thisWWgeo = ("["+thisWWgeo+"]").replaceAll(",\\[\\]","");
									}
								}
							}
						} catch (FileNotFoundException esf) { esf.printStackTrace(); }
						spcWatchSQL = "INSERT IGNORE INTO WxObs.SPCWatchBoxes (WatchID, WatchBox) VALUES ('"+spcYear+" "+thisWWid+"','["+thisWWgeo+"]');";
						System.out.println(spcWatchSQL);
						thisWWFile.delete();				
					} else {
						System.out.print("No active SPC Watches!");
					}
				}
			}		
		} catch (FileNotFoundException e) { e.printStackTrace(); }

		String cleanReportsSQL = "DELETE FROM WxObs.SPCReportsLive WHERE AssocID LIKE '%satellite%';";
		String cleanWatchSQL = "DELETE FROM WxObs.SPCWatchBoxes WHERE WatchID = ' ';";
		String cleanMesoSQL = "DELETE FROM WxObs.SPCMesoscaleShape WHERE mdID = ' ';";

        try { wc.q2do1c(dbc, spcReportsSQL, null); } catch (Exception e) { e.printStackTrace(); }
		if ( spcMesoSQL != null ) { try { wc.q2do1c(dbc, spcMesoSQL, null); } catch (Exception e) { e.printStackTrace(); } }
		if ( spcWatchSQL != null ) { try { wc.q2do1c(dbc, spcWatchSQL, null); } catch (Exception e) { e.printStackTrace(); } }
        try { wc.q2do1c(dbc, cleanReportsSQL, null); } catch (Exception e) { e.printStackTrace(); }
        try { wc.q2do1c(dbc, cleanWatchSQL, null); } catch (Exception e) { e.printStackTrace(); }
        try { wc.q2do1c(dbc, cleanMesoSQL, null); } catch (Exception e) { e.printStackTrace(); }
        
		spcMDkmzFile.delete();
		spcWWkmzFile.delete();
		spcMDkmlFile.delete();
		spcWWkmlFile.delete();

	}

    public static void doGetSPCb(Connection dbc) {
        
    	CommonBeans cb = new CommonBeans();
    	WebCommon wc = new WebCommon();
    	JunkyBeans jb = new JunkyBeans();
    	
        final String mysqlShare = cb.getPathChartCache().toString();
        final String spcFeedBase = jb.getSpcFeedBase();

        final File spcMDFile = new File(mysqlShare+"/spcmdrss.xml");
        final File spcWWFile = new File(mysqlShare+"/spcwwrss.xml");
        
        DateFormat dateFormat = new SimpleDateFormat("yyyy");
        Date date = new Date();
        String tYear = dateFormat.format(date);
                
        WebCommon.jsoupOutFile(spcFeedBase+"spcmdrss.xml", spcMDFile);
        WebCommon.jsoupOutFile(spcFeedBase+"spcwwrss.xml", spcWWFile);			

        String loadSPCmdSQL = "LOAD DATA LOCAL INFILE '"+mysqlShare+"/spcmdrss.xml' IGNORE INTO TABLE WxObs.SPCMesoscale CHARACTER SET 'utf8' LINES STARTING BY '<item>' TERMINATED BY '</item>' (@tmp) SET title = CONCAT(ExtractValue(@tmp, '//title'),' "+tYear+"'), description = ExtractValue(@tmp, '//description'), pubDate = ExtractValue(@tmp, '//pubDate');";
        String loadSPCwwSQL = "LOAD DATA LOCAL INFILE '"+mysqlShare+"/spcwwrss.xml' IGNORE INTO TABLE WxObs.SPCWatches CHARACTER SET 'utf8' LINES STARTING BY '<item>' TERMINATED BY '</item>' (@tmp) SET title = CONCAT(ExtractValue(@tmp, '//title'),' "+tYear+"'), description = ExtractValue(@tmp, '//description'), pubDate = ExtractValue(@tmp, '//pubDate');";
        String cleanSPCmdSQL = "DELETE FROM WxObs.SPCMesoscale WHERE title LIKE 'SPC - No MDs are in effect as of %';";
        String cleanSPCwwSQL = "DELETE FROM WxObs.SPCWatches WHERE title LIKE 'SPC - No watches are valid as %';";

        try { wc.q2do1c(dbc, loadSPCmdSQL, null); } catch (Exception e) { e.printStackTrace(); }
        try { wc.q2do1c(dbc, loadSPCwwSQL, null); } catch (Exception e) { e.printStackTrace(); }
        try { wc.q2do1c(dbc, cleanSPCmdSQL, null); } catch (Exception e) { e.printStackTrace(); }
        try { wc.q2do1c(dbc, cleanSPCwwSQL, null); } catch (Exception e) { e.printStackTrace(); }
        
        spcMDFile.delete();
        spcWWFile.delete();
                        
    }    
    
    public static void doGetSPCHourly(Connection dbc) {

        CommonBeans cb = new CommonBeans();
    	WebCommon wc = new WebCommon();
    	JunkyBeans jb = new JunkyBeans();
    	
        final String mysqlShare = cb.getPathChartCache().toString();
        final String spcFeedBase = jb.getSpcFeedBase();
        
		File spcOutFileSrc = new File(mysqlShare+"/spcacrss.xml");
		WebCommon.jsoupOutFile(spcFeedBase+"spcacrss.xml", spcOutFileSrc);		
		String spcOutSQL = "LOAD DATA LOCAL INFILE '"+mysqlShare+"/spcacrss.xml' IGNORE INTO TABLE WxObs.SPCOutlooks CHARACTER SET 'utf8' LINES STARTING BY '<item>' TERMINATED BY '</item>' (@tmp) SET title = ExtractValue(@tmp, '//title'), description = ExtractValue(@tmp, '//description'), pubDate = ExtractValue(@tmp, '//pubDate');";
		
        try { wc.q2do1c(dbc, spcOutSQL, null); } catch (Exception e) { e.printStackTrace(); }

		new File(mysqlShare+"/spcacrss.xml").delete();
		
    }
    
}
