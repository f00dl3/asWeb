/*
by Anthony Stump
Created: 16 Dec 2018
Updated: 23 Nov 2019
 */

package asUtilsPorts.Feed;

import asUtils.Shares.StumpJunk;
import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class MHPFetch {
    
    public static void doMHP(Connection dbc, String troop) {
   
    	CommonBeans cb = new CommonBeans();
    	WebCommon wc = new WebCommon();
        
        final File mhpFile = new File(cb.getPathChartCache().toString()+"/mhpOut.txt");
        final String tURL = "https://www.mshp.dps.missouri.gov/HP68/SearchAction?searchTroop=" + troop;
        final String rURLPrefix = "https://www.mshp.dps.missouri.gov/HP68/AccidentDetailsAction?ACC_RPT_NUM=";
        final DateTimeFormatter inDateFormat = DateTimeFormat.forPattern("MM/dd/yyyy");
        final DateTimeFormatter outDateFormat = DateTimeFormat.forPattern("yyyy-MM-dd");
        final DateTimeFormatter inTimeFormat = DateTimeFormat.forPattern("hh:mmaa");
        final DateTimeFormatter outTimeFormat = DateTimeFormat.forPattern("hhmm");
        final String vehHeader = "Veh. # Damage Driver Name Driver Age Driver City/State Vehicle Direction";
        final String vehHeader2 = "Vehicle Description Disposition Driver Gender Safety Device Driver Insurance";
        final String injHeader = "Veh. # Gender Injury Type City/State Disposition";
        final String injHeader2 = "Name Age Safety Device Involvement";
        StumpJunk.jsoupOutBinary(tURL, mhpFile, 30.0);
        
        String query_AccidentReport = "INSERT IGNORE INTO Core.Crash_HPMO " +
                                        "(Incident, Lat, Lon, County, Location," +
                                        " Synopsis, Date, Time, Vehicles, Damage," +
                                        " Injuries, InjuryTypes)" +
                                        " VALUES ";
        
        Scanner mhpFileScanner = null; try {		
            mhpFileScanner = new Scanner(mhpFile);
            String previousId = "";
            int fileNumber = 0;
            while(mhpFileScanner.hasNext()) {				
                String line = mhpFileScanner.nextLine();
                if(line.contains("ACC_RPT_NUM=")) {
                    Pattern pattern = Pattern.compile("ACC_RPT_NUM=(.*)\">");
                    Matcher matcher = pattern.matcher(line);
                    while (matcher.find()) {
                        String reportId = matcher.group(1);
                        String rURL = rURLPrefix + reportId;
                        if(!reportId.equals(previousId)) {
                            if(fileNumber < 99999) {
                                String textFull = "";
                                String aLatitude = "";
                                String aLongitude = "";
                                String aCounty = "";
                                String aLocation = "";
                                String aSynopsis = "";
                                String aDateString = "";
                                String aTimeString = "";
                                int iteratedThisFile = 0;
                                final File arFile = new File(cb.getPathChartCache().toString()+"/mhpOut-"+reportId+".txt");
                                StumpJunk.jsoupOutBinary(rURL, arFile, 30.0);
                                StumpJunk.sedFileReplace(arFile.getPath(), "<td class=\"infoCell3\">", "\"");
                                StumpJunk.sedFileReplace(arFile.getPath(), "</td>", "\"");
                                StumpJunk.sedFileReplace(arFile.getPath(), "\\t", "");
                                StumpJunk.sedFileReplace(arFile.getPath(), "<tr>", "-TRS-");
                                StumpJunk.sedFileReplace(arFile.getPath(), "</tr>", "-TRE-");
                                StumpJunk.sedFileReplace(arFile.getPath(), "\\*", "-");
                                Scanner arFileScanner = null; try {		
                                    arFileScanner = new Scanner(arFile);
                                    while(arFileScanner.hasNext()) {				
                                        String arLine = arFileScanner.nextLine();
                                        if(arLine.contains(reportId)) {
                                            aLatitude = StumpJunk.jsonSanitizeStrict(arFileScanner.nextLine().replaceAll("\"", ""));
                                            aLongitude = StumpJunk.jsonSanitizeStrict(arFileScanner.nextLine().replaceAll("\"", ""));
                                            String dateString = StumpJunk.jsonSanitizeStrict(arFileScanner.nextLine().replaceAll("\"", ""));
                                            DateTime readDateIn = inDateFormat.parseDateTime(dateString);
                                            aDateString = StumpJunk.jsonSanitizeStrict(outDateFormat.print(readDateIn));
                                            String timeString = StumpJunk.jsonSanitizeStrict(arFileScanner.nextLine().replaceAll("\"", ""));
                                            DateTime readTimeIn = inTimeFormat.parseDateTime(timeString);
                                            aTimeString = StumpJunk.jsonSanitizeStrict(outTimeFormat.print(readTimeIn));
                                            aCounty = StumpJunk.jsonSanitizeStrict(arFileScanner.nextLine().replaceAll("\"", ""));
                                            aLocation = StumpJunk.jsonSanitizeStrict(arFileScanner.nextLine().replaceAll("\"", ""));
                                            
                                        }
                                        if(arLine.contains("<span id=\"Misc\">")) {                         
                                            Pattern synPat = Pattern.compile("<span id=\"Misc\">(.*)</span>");
                                            Matcher synMat = synPat.matcher(arLine);
                                            while(synMat.find()) {
                                                aSynopsis = StumpJunk.jsonSanitizeStrict(synMat.group(1));
                                            }
                                        }
                                    }
                                } catch (Exception e) { e.printStackTrace(); }
                                Scanner arFileScannerC = null; try {		
                                    arFileScannerC = new Scanner(arFile);
                                    while(arFileScannerC.hasNext()) {				
                                        String arLineC = arFileScannerC.nextLine();
                                        if(arLineC.contains("Vehicle Information") && iteratedThisFile == 0) {
                                            while(arFileScannerC.hasNext()) {
                                                String textLine = arFileScannerC.nextLine();
                                                textLine = StumpJunk.htmlStripTease(textLine);
                                                textFull += textLine + " ";
                                                if(arFileScannerC.nextLine().equals("</table>")) { break; }
                                            }
                                            iteratedThisFile = 1;
                                        }
                                    }
                                } catch (Exception e) { e.printStackTrace(); }
                                //System.out.println(textFull);
                                try {
                                    textFull = textFull.replaceAll("\\n", ",").replaceAll(" +", " ").replaceAll("  -TRE-","").replaceAll(" -TRE- -TRS- ", ";");
                                    String carsFull = StumpJunk.jsonSanitizeStrict(textFull.replaceAll(vehHeader, "").replaceAll(vehHeader2, "").split(injHeader2)[0]);
                                    String injFull = StumpJunk.jsonSanitizeStrict(textFull.replaceAll(injHeader, "").replaceAll(aSynopsis, "").split(injHeader2)[1]);
                                    int totCars = carsFull.length() - carsFull.replaceAll(";", "").length() + 1;
                                    int totInj = injFull.length() - injFull.replaceAll(";", "").length() + 1;
                                    if(!aLocation.equals("")) {
                                        query_AccidentReport += " ('" + reportId + "', '" + aLatitude + "', '" + aLongitude + "', '" + aCounty + "', '" + aLocation + "'," +
                                            " '" + aSynopsis + "', '" + aDateString + "', '" + aTimeString + "', '" + totCars + "', '" + carsFull + "'," +
                                            " '" + totInj + "', '" + injFull + "'),";
                                    }
                                } catch (Exception e) { e.printStackTrace(); }
                                arFile.delete();
                            }
                            fileNumber++;
                        }
                        previousId = reportId;
                    }
                }
            }
            query_AccidentReport = query_AccidentReport.substring(0, query_AccidentReport.length() - 1) + ";";
            
            final File sqlDebugFile = new File(cb.getPathChartCache().toString()+"/mhp.sql");
            try { StumpJunk.varToFile(query_AccidentReport, sqlDebugFile, false); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
            //System.out.println(query_AccidentReport);

            try { wc.q2do1c(dbc, query_AccidentReport, null); } catch (Exception e) { e.printStackTrace(); }
            
        } catch (Exception e) {
            e.printStackTrace();
        }            
        mhpFile.delete();        
    }    
}
