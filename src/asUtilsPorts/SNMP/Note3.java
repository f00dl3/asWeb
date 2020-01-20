/*
SNMP Walk -> Database --- Note 3 class
Split off for v5 on 28 Apr 2019
Java created: 14 Aug 2017
Last updated: 19 Jan 2020
 */

package asUtilsPorts.SNMP;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import org.json.JSONObject;

public class Note3 {
    
    public void snmpNote3(Connection dbc) {
        
        CommonBeans cb = new CommonBeans();
        WebCommon wc = new WebCommon();
        
        final String ramPath = cb.getPathChartCache();
        final DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        final Date date = new Date();
        final String thisTimestamp = dateFormat.format(date);        
          
        String aPayload = ramPath+"/aPayload.zip";
        File aPayloadFile = new File(aPayload);
        String uzPayload = ramPath+"/snmpUnzip/Note3";
        File uzPayloadF = new File(uzPayload);
        String uzPayloadFull = uzPayload;

        wc.unzipFile(aPayload, uzPayload);
        wc.sedFileReplace(uzPayloadFull+"/NetStatE.txt", " +", ",");
        wc.sedFileReplace(uzPayloadFull+"/IFStats.txt", " +", ",");
        wc.sedFileReplace(uzPayloadFull+"/DSCPU.txt", " +", ",");
        wc.sedFileReplace(uzPayloadFull+"/DSBattery.txt", " +", ",");
        wc.sedFileReplace(uzPayloadFull+"/DSConn.txt", " +", ",");
        wc.sedFileReplace(uzPayloadFull+"/DSGeo.txt", " +", ",");
        wc.sedFileReplace(uzPayloadFull+"/VMStat.txt", " +", ",");

        File aNetStatFile = new File(uzPayloadFull+"/NetStatE.txt");
        File aIfStatsFile = new File(uzPayloadFull+"/IFStats.txt");
        File aVMStatFile = new File(uzPayloadFull+"/VMStat.txt");
        File aDSCPUFile = new File(uzPayloadFull+"/DSCPU.txt");
        File aDSBatteryFile = new File(uzPayloadFull+"/DSBattery.txt");
        File aDSConnFile = new File(uzPayloadFull+"/DSConn.txt");
        File aDSGeoFile = new File(uzPayloadFull+"/DSGeo.txt");
        File aASLSLogFile = new File(uzPayloadFull+"/asls.log");
        File aASLSSensorLogFile = new File(uzPayloadFull+"/aslsSensors.log");

        int aActiveCon = 0;

        Scanner aNetStatScanner = null; try {		
                aNetStatScanner = new Scanner(aNetStatFile);
                while(aNetStatScanner.hasNext()) {				
                        String line = aNetStatScanner.nextLine();
                        if(line.contains("ESTAB")) { aActiveCon++; }						
                }
        } catch (FileNotFoundException e) { e.printStackTrace(); }

        long aRmnet0Rx = 0;
        long aRmnet0Tx = 0;
        long aWlan0Rx = 0;
        long aWlan0Tx = 0;

        Scanner aIfStatsScanner = null; try {			
                aIfStatsScanner = new Scanner(aIfStatsFile);
                while(aIfStatsScanner.hasNext()) {		
                        String line = aIfStatsScanner.nextLine();
                        if(line.contains("rmnet0:")) {
                                String[] lineTmp = line.split(",");
                                String strThisVal = lineTmp[1]; aRmnet0Rx = Long.parseLong(strThisVal);
                                String strThisVa2 = lineTmp[9]; aRmnet0Tx = Long.parseLong(strThisVa2);
                        }
                        if(line.contains("wlan0:")) {
                                String[] lineTmp = line.split(",");
                                String strThisVal = lineTmp[2]; aWlan0Rx = Long.parseLong(strThisVal);
                                String strThisVa2 = lineTmp[10]; aWlan0Tx = Long.parseLong(strThisVa2);
                        }
                }
        }
        catch (FileNotFoundException e) { e.printStackTrace(); }
        catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); }

        int aMemoryBuffers = 0;
        int aMemoryFree = 0;
        int aMemoryShared = 0;
        int aMemoryTotal = 0;
        int aMemoryUse = 0;

        Scanner aVMStatScanner = null; try {			
                aVMStatScanner = new Scanner(aVMStatFile);
                while(aVMStatScanner.hasNext()) {		
                        String line = aVMStatScanner.nextLine();
                        if(line.contains("Mem:")) {
                                String[] lineTmp = line.split(",");
                                String strThisVal = lineTmp[5]; aMemoryBuffers = Integer.parseInt(strThisVal);
                                String strThisVa2 = lineTmp[3]; aMemoryFree = Integer.parseInt(strThisVa2);
                                String strThisVa3 = lineTmp[4]; aMemoryShared = Integer.parseInt(strThisVa3);
                                String strThisVa4 = lineTmp[1]; aMemoryTotal = Integer.parseInt(strThisVa4);
                                String strThisVa5 = lineTmp[2]; aMemoryUse = Integer.parseInt(strThisVa5);
                        }
                }
        }
        catch (FileNotFoundException e) { e.printStackTrace(); }
        catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); }

        double aLoadNow = 0.0;
        double aLoad5 = 0.0;
        double aLoad15 = 0.0;
        double aCPUUse = 0.0;

        Scanner aDSCPUScanner = null; try {		
                aDSCPUScanner = new Scanner(aDSCPUFile);
                while(aDSCPUScanner.hasNext()) {				
                        String line = aDSCPUScanner.nextLine();					
                        if(line.contains("Load:")) {
                                String[] lineTmp = line.split(",");
                                String strThisVal = lineTmp[1]; aLoadNow = Double.parseDouble(strThisVal);
                                String strThisVa2 = lineTmp[3]; aLoad5 = Double.parseDouble(strThisVa2);
                                String strThisVa3 = lineTmp[5]; aLoad15 = Double.parseDouble(strThisVa3);
                        }
                        if(line.contains("TOTAL:")) { String[] lineTmp = line.split(","); String strThisVal = lineTmp[2].replaceAll("\\D+", ""); aCPUUse = Double.parseDouble(strThisVal); }
                }
        }
        catch (FileNotFoundException e) { e.printStackTrace(); }
        catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); }

        int aBattLevel = 0;
        int aBattVolt = 0;
        int aBattCurrent = 0;
        int aBattTemp = 0;
        String aBattPowered = null;
        String aBattPoweredU = null;
        int aBattHealth = 0;

        Scanner aDSBatteryScanner = null; try {			
                aDSBatteryScanner = new Scanner(aDSBatteryFile);
                while(aDSBatteryScanner.hasNext()) {				
                        String line = aDSBatteryScanner.nextLine();
                        if(line.contains(",level:")) { String[] lineTmp = line.split(","); String strThisVal = lineTmp[2]; aBattLevel = Integer.parseInt(strThisVal); }
                        if(line.contains(",voltage:")) { String[] lineTmp = line.split(","); String strThisVal = lineTmp[2]; aBattVolt = Integer.parseInt(strThisVal); }
                        if(line.contains(",current,now:")) { String[] lineTmp = line.split(","); String strThisVal = lineTmp[3]; aBattCurrent = Integer.parseInt(strThisVal); }
                        if(line.contains(",temperature:")) { String[] lineTmp = line.split(","); String strThisVal = lineTmp[2]; aBattTemp = Integer.parseInt(strThisVal); }
                        if(line.contains(",AC,powered:")) { String[] lineTmp = line.split(","); String strThisVal = lineTmp[3]; aBattPowered = strThisVal; }
                        if(line.contains(",USB,powered:")) { String[] lineTmp = line.split(","); String strThisVal = lineTmp[3]; aBattPoweredU = strThisVal; }
                        if(line.contains(",health:")) { String[] lineTmp = line.split(","); String strThisVal = lineTmp[2]; aBattHealth = Integer.parseInt(strThisVal); }
                }
        }
        catch (FileNotFoundException e) { e.printStackTrace(); }
        catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); }

        int aSigStrGSM = 0;
        int aSigStrCDMA = 0;
        int aSigStrEVDO = 0;
        int aSigStrLTE = 0;
        String aSigStrMode = null;
        int aCellIdentMCC = 0;
        int aCellIdentMNC = 0;
        int aCellIdentPCI = 0;

        Scanner aDSConnScanner = null; try {			
                aDSConnScanner = new Scanner(aDSConnFile);
                while(aDSConnScanner.hasNext()) {				
                        String line = aDSConnScanner.nextLine();
                        if(line.contains(",mSignalStrength=")) {
                                String[] lineTmp = line.split(",");
                                String strThisVal = lineTmp[2]; aSigStrGSM = Integer.parseInt(strThisVal);
                                String strThisVa2 = lineTmp[4]; aSigStrCDMA = Integer.parseInt(strThisVa2);
                                String strThisVa3 = lineTmp[7]; aSigStrEVDO = Integer.parseInt(strThisVa3);
                                String strThisVa4 = lineTmp[10]; aSigStrLTE = Integer.parseInt(strThisVa4);
                                String strThisVa5 = lineTmp[15]; aSigStrMode = strThisVa5;
                        }
                        if(line.contains(",mCellInfo=")) {
                                String[] lineTmp = line.split(",");
                                String strThisVal = lineTmp[5].replaceAll("\\D+", ""); aCellIdentMCC = Integer.parseInt(strThisVal);
                                String strThisVa2 = lineTmp[6].replaceAll("\\D+", ""); aCellIdentMNC = Integer.parseInt(strThisVa2);
                                String strThisVa3 = lineTmp[8].replaceAll("\\D+", ""); aCellIdentPCI = Integer.parseInt(strThisVa3);
                        }
                }
        }
        catch (FileNotFoundException e) { e.printStackTrace(); }
        catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); }

        String aLocationLon = null;
        String aLocationLat = null;

        Scanner aDSGeoScanner = null; try {			
                aDSGeoScanner = new Scanner(aDSGeoFile);
                while(aDSGeoScanner.hasNext()) {				
                        String line = aDSGeoScanner.nextLine();
                        if(line.contains(",passive:,Location")) {
                                String[] lineTmp = line.split(",");
                                String strThisVal = lineTmp[4]; aLocationLon = strThisVal;
                                String strThisVa2 = lineTmp[3]; aLocationLat = strThisVa2;
                        }
                }
        }
        catch (FileNotFoundException e) { e.printStackTrace(); }
        catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); }

        String aRapidUpdateNumber = "";
        String aRapidTime = "";
        String aRapidLocationLat = "";
        String aRapidLocationLon = "";
        String aRapidSource = "";
        String aRapidAltitude = "";
        String aRapidSpeed = "";
        String aRapidBearing = "";
        String rapidString = "";

        Scanner aASLSLogFileScanner = null; try {			
                aASLSLogFileScanner = new Scanner(aASLSLogFile);
                while(aASLSLogFileScanner.hasNext()) {				
                        String line = aASLSLogFileScanner.nextLine();
                        if(line.contains("EndLine")) {

                                String[] lineTmp = line.split(",");
                                String strThisVal = lineTmp[0].replaceAll("Update: ", ""); aRapidUpdateNumber = strThisVal;
                                String strThisVa2 = lineTmp[1].replaceAll(" Time: ", ""); aRapidTime = strThisVa2;
                                String strThisVa3 = lineTmp[2].replaceAll(" Latitude: ", ""); aRapidLocationLat = strThisVa3;
                                String strThisVa4 = lineTmp[3].replaceAll(" Longitude: ", ""); aRapidLocationLon = strThisVa4;
                                String strThisVa5 = lineTmp[4].replaceAll(" Source: ", ""); aRapidSource = strThisVa5;
                                String strThisVa6 = lineTmp[5].replaceAll(" Altitude: ", ""); aRapidAltitude = strThisVa6;
                                String strThisVa7 = lineTmp[6].replaceAll(" Speed: ", ""); aRapidSpeed = strThisVa7;
                                String strThisVa8 = lineTmp[7].replaceAll(" Bearing: ", ""); aRapidBearing = strThisVa8;

                                JSONObject jRapidObj = new JSONObject();
                                JSONObject jRapidData = new JSONObject();
                                if (wc.isSet(aRapidUpdateNumber)) {
                                		jRapidObj.put(aRapidUpdateNumber, jRapidData);
                                        if (wc.isSet(aRapidTime)) { jRapidData.put("Time", aRapidTime); }
                                        if (wc.isSet(aRapidLocationLat)) { jRapidData.put("Latitude", aRapidLocationLat); }
                                        if (wc.isSet(aRapidLocationLon)) { jRapidData.put("Longitude", aRapidLocationLon); }
                                        if (wc.isSet(aRapidSource)) { jRapidData.put("Source", aRapidSource); }
                                        if (wc.isSet(aRapidAltitude)) { jRapidData.put("AltitudeM", aRapidAltitude); }
                                        if (wc.isSet(aRapidSpeed)) { jRapidData.put("SpeedMPS", aRapidSpeed); }
                                        if (wc.isSet(aRapidBearing)) { jRapidData.put("Bearing", aRapidBearing); }
                                        String thisJSONstring = jRapidObj.toString().substring(1);
                                        thisJSONstring = thisJSONstring.substring(0, thisJSONstring.length()-1)+",";
                                        rapidString += thisJSONstring;
                                }
                        }
                }
        }
        catch (FileNotFoundException e) { e.printStackTrace(); }
        catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); }

        String aLocation = "["+aLocationLon+","+aLocationLat+"]";
        rapidString = ("{"+rapidString+"}").replace("\n","").replace(",}", "}");

        String aRapidSensorUpdateNumber = "";
        String aRapidSensorAmbientTemperature = "";
        String rapidSensorString = "";

        Scanner aASLSSensorLogFileScanner = null; try {			
                aASLSSensorLogFileScanner = new Scanner(aASLSSensorLogFile);
                while(aASLSSensorLogFileScanner.hasNext()) {				
                        String line = aASLSSensorLogFileScanner.nextLine();
                        if(line.contains("EndSensorData")) {

                                String[] lineTmp = line.split(",");
                                String strThisVal = lineTmp[0].replaceAll("Sensor Update: ", ""); aRapidSensorUpdateNumber = strThisVal;
                                String strThisVa2 = lineTmp[1].replaceAll(" AmbientTemperatureF: ", ""); aRapidSensorAmbientTemperature = strThisVa2;

                                JSONObject jRapidSensorObj = new JSONObject();
                                JSONObject jRapidSensorData = new JSONObject();
                                if (wc.isSet(aRapidSensorUpdateNumber)) { jRapidSensorObj.put(aRapidSensorUpdateNumber, jRapidSensorData);
                                        if (wc.isSet(aRapidSensorAmbientTemperature)) { jRapidSensorData.put("AmbientTemperatureF", aRapidSensorAmbientTemperature); }
                                        String thisJSONstring = jRapidSensorObj.toString().substring(1);
                                        thisJSONstring = thisJSONstring.substring(0, thisJSONstring.length()-1)+",";
                                        rapidSensorString += thisJSONstring;
                                }
                        }
                }
        }
        catch (FileNotFoundException e) { e.printStackTrace(); }
        catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); }

        rapidSensorString = ("{"+rapidSensorString+"}").replace("\n","").replace(",}", "}");

        String aSQLQuery = "INSERT IGNORE INTO net_snmp.Note3 ("
                + "WalkTime, WalkTimeMatcher, ActiveConn,"
                + " rmnet0Rx, rmnet0Tx, wlan0Rx, wlan0Tx,"
                + " MemoryBuffers, MemoryFree, MemoryShared, MemoryTotal, MemoryUse,"
                + " LoadNow, Load5, Load15, CPUUse,"
                + " BattLevel, BattVolt, BattCurrent, BattTemp, BattPowered, BattPoweredU, BattHealth,"
                + " SigStrGSM, SigStrCDMA, SigStrEVDO, SigStrLTE, SigStrMode,"
                + " CellIdentMCC, CellIdentMNC, CellIdentPCI,"
                + " Location, RapidLocation, SensorsRapid"
                + ") VALUES ("
                + "'"+thisTimestamp+"',(SELECT MAX(WalkTime) FROM net_snmp.Main),"+aActiveCon+","
                + aRmnet0Rx+","+aRmnet0Tx+","+aWlan0Rx+","+aWlan0Tx+","
                + aMemoryBuffers+","+aMemoryFree+","+aMemoryShared+","+aMemoryTotal+","+aMemoryUse+","
                + aLoadNow+","+aLoad5+","+aLoad15+","+aCPUUse+","
                + aBattLevel+","+aBattVolt+","+aBattCurrent+","+aBattTemp+",'"+aBattPowered+"','"+aBattPoweredU+"',"+aBattHealth+","
                + aSigStrGSM+","+aSigStrCDMA+","+aSigStrEVDO+","+aSigStrLTE+",'"+aSigStrMode+"',"
                + aCellIdentMCC+","+aCellIdentMNC+","+aCellIdentPCI+","
                + "'"+aLocation+"','"+rapidString+"','"+rapidSensorString+"'"
                + ");";

        System.out.println(aSQLQuery);
        
    	try { wc.q2do1c(dbc, aSQLQuery, null); } catch (Exception e) { e.printStackTrace(); }
        
        aPayloadFile.delete();
        wc.deleteDir(uzPayloadF);
                
    }
    
}
