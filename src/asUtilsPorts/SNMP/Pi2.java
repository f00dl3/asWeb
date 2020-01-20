/*
SNMP Walk -> Database --- Raspberry Pi 2 class
Split off for v5 on 28 Apr 2019
Java created: 14 Aug 2017
Last updated: 20 Jan 2020
 */

package asUtilsPorts.SNMP;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Pi2 {
    
    public void snmpPi2(Connection dbc) {

        	CommonBeans cb = new CommonBeans();
            WebCommon wc = new WebCommon();
            
            final String ramPath = cb.getPathChartCache();
            final String ramPathUnzipped = ramPath + "/snmpUnzip/Pi2";
            final File piSnmpZip = new File(ramPath+"/snmpPi2.zip");
            final File ramPathUnzippedF = new File(ramPathUnzipped);
            final DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
            final Date date = new Date();
            final String thisTimestamp = dateFormat.format(date);
            
            if(!ramPathUnzippedF.exists()) { ramPathUnzippedF.mkdirs(); }
            try { wc.unzipFile(piSnmpZip.toString(), ramPathUnzipped); } catch (Exception e) { e.printStackTrace(); }
            wc.runProcess("mv "+ramPathUnzipped+"/dev/shm/snmpPi2/* "+ramPathUnzipped);

            final File pi2WalkFile = new File(ramPathUnzipped+"/snmpwalkPi2.txt");
            final File pioSerialMonFile2 = new File(ramPathUnzipped+"/pioSerialMon2.log");

            int piNumUsers = 0;
            int piCPULoad1 = 0;
            int piCPULoad2 = 0;
            int piCPULoad3 = 0;
            int piCPULoad4 = 0;
            double piLoadIndex1 = 0;
            double piLoadIndex5 = 0;
            double piLoadIndex15 = 0;
            int piProcesses = 0;
            long piEthIn = 0;
            long piEthOut = 0;
            long piWiFiIn = 0;
            long piWiFiOut = 0;
            long piKMemPhys = 0;
            long piKMemVirt = 0;
            long piKMemBuff = 0;
            long piKMemCached = 0;
            long piKMemShared = 0;
            long piKSwap = 0;
            long piK4Root = 0;
            long piKMemPhysU = 0;
            long piKMemVirtU = 0;
            long piKMemBuffU = 0;
            long piKMemCachedU = 0;
            long piKMemSharedU = 0;
            long piKSwapU = 0;
            long piK4RootU = 0;
			
            Scanner pi2WalkFileScanner = null; try {		
                    pi2WalkFileScanner = new Scanner(pi2WalkFile);
                    while(pi2WalkFileScanner.hasNext()) {				
                            String line = pi2WalkFileScanner.nextLine();
                            if(line.contains("hrSystemNumUsers.0 =")) { Pattern p = Pattern.compile("Gauge32: (.*)"); Matcher m = p.matcher(line); if (m.find()) { piNumUsers = Integer.parseInt(m.group(1)); }}
                            if(line.contains("hrProcessorLoad.196608 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { piCPULoad1 = Integer.parseInt(m.group(1)); }}
                            if(line.contains("hrProcessorLoad.196609 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { piCPULoad2 = Integer.parseInt(m.group(1)); } }					
                            if(line.contains("hrProcessorLoad.196610 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { piCPULoad3 = Integer.parseInt(m.group(1)); } }					
                            if(line.contains("hrProcessorLoad.196611 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { piCPULoad4 = Integer.parseInt(m.group(1)); } }					
                            if(line.contains("laLoad.1 =")) { Pattern p = Pattern.compile("STRING: (.*)"); Matcher m = p.matcher(line); if (m.find()) { piLoadIndex1 = Double.parseDouble(m.group(1)); } }
                            if(line.contains("laLoad.2 =")) { Pattern p = Pattern.compile("STRING: (.*)"); Matcher m = p.matcher(line); if (m.find()) { piLoadIndex5 = Double.parseDouble(m.group(1)); } }
                            if(line.contains("laLoad.3 =")) { Pattern p = Pattern.compile("STRING: (.*)"); Matcher m = p.matcher(line); if (m.find()) { piLoadIndex15 = Double.parseDouble(m.group(1)); } }
                            if(line.contains("hrSystemProcesses.0 =")) { Pattern p = Pattern.compile("Gauge32: (.*)"); Matcher m = p.matcher(line); if (m.find()) { piProcesses = Integer.parseInt(m.group(1)); } }
                            if(line.contains("ifHCInOctets.2 =")) { Pattern p = Pattern.compile("Counter64: (.*)"); Matcher m = p.matcher(line); if (m.find()) { piEthIn = Long.parseLong(m.group(1)); } }
                            if(line.contains("ifHCOutOctets.2 =")) { Pattern p = Pattern.compile("Counter64: (.*)"); Matcher m = p.matcher(line); if (m.find()) { piEthOut = Long.parseLong(m.group(1)); } }
                            if(line.contains("ifHCInOctets.3 =")) { Pattern p = Pattern.compile("Counter64: (.*)"); Matcher m = p.matcher(line); if (m.find()) { piWiFiIn = Long.parseLong(m.group(1)); } }
                            if(line.contains("ifHCOutOctets.3 =")) { Pattern p = Pattern.compile("Counter64: (.*)"); Matcher m = p.matcher(line); if (m.find()) { piWiFiOut = Long.parseLong(m.group(1)); } }
                            if(line.contains("hrStorageSize.1 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { piKMemPhys = Long.parseLong(m.group(1)); } }
                            if(line.contains("hrStorageSize.3 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { piKMemVirt = Long.parseLong(m.group(1)); } }
                            if(line.contains("hrStorageSize.6 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { piKMemBuff = Long.parseLong(m.group(1)); } }
                            if(line.contains("hrStorageSize.7 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { piKMemCached = Long.parseLong(m.group(1)); } }
                            if(line.contains("hrStorageSize.8 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { piKMemShared = Long.parseLong(m.group(1)); } }
                            if(line.contains("hrStorageSize.10 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { piKSwap = Long.parseLong(m.group(1)); } }
                            if(line.contains("hrStorageSize.31 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { piK4Root = Long.parseLong(m.group(1)); } }
                            if(line.contains("hrStorageUsed.1 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { piKMemPhysU = Long.parseLong(m.group(1)); } }
                            if(line.contains("hrStorageUsed.3 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { piKMemVirtU = Long.parseLong(m.group(1)); } }
                            if(line.contains("hrStorageUsed.6 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { piKMemBuffU = Long.parseLong(m.group(1)); } }
                            if(line.contains("hrStorageUsed.7 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { piKMemCachedU = Long.parseLong(m.group(1)); } }
                            if(line.contains("hrStorageUsed.8 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { piKMemSharedU = Long.parseLong(m.group(1)); } }
                            if(line.contains("hrStorageUsed.10 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { piKSwapU = Long.parseLong(m.group(1)); } }
                            if(line.contains("hrStorageUsed.31 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { piK4RootU = Long.parseLong(m.group(1)); } }

                    }
            } catch (Exception e) { e.printStackTrace(); }

            try { wc.sedFileReplace(pioSerialMonFile2.getPath(), "\\n\\n", "\n"); } catch (Exception e) { }

            double piExtTemp = 0.0;
            long piRunSec = 0;
            double piGPSLat = 0.0;
            double piGPSLon = 0.0;
            int piGPSAge = 0;
            int piGPSCourse = 0;
            int piGPSAlti = 0;
            double piGPSSpeed = 0.0;
            long piGPSChars = 0;
            long piGPSFails = 0;
            int piHallAState = 0;
            int piHallBState = 0;
            float piGPSEstDist = 0.0f;
            int piLightLevel = 0;
            int piSwitchState = 0;

            try {
                    FileInputStream inSerialMon = new FileInputStream(pioSerialMonFile2);
                    BufferedReader br = new BufferedReader(new InputStreamReader(inSerialMon));
                    String serialLine = null, tmpLine = null;
                    while ((tmpLine = br.readLine()) != null) { serialLine = tmpLine; }
                    String line = serialLine;
                    System.out.println(line);
                    if(line.contains("TempF:")) {
                            String[] lineTmp = line.split(",");
                            piRunSec = Long.parseLong(lineTmp[0].replace("Seconds: ", ""));
                            piExtTemp = Double.parseDouble(lineTmp[1].replace(" TempF: ", ""));
                            piGPSLat = Double.parseDouble(lineTmp[2].replace(" Lat: ", ""));
                            piGPSLon = Double.parseDouble(lineTmp[3].replace(" Lon: ", ""));
                            piGPSAge = Integer.parseInt(lineTmp[4].replace(" FixAgeMS: ", ""));
                            piGPSAlti = Integer.parseInt(lineTmp[7].replace(" AltitCM: ", ""));
                            piGPSCourse = Integer.parseInt(lineTmp[8].replace(" Course: ", ""));
                            piGPSSpeed = Double.parseDouble(lineTmp[9].replace(" SpeedMPH: ", ""));
                            piGPSChars = Long.parseLong(lineTmp[10].replace(" GPSDataUseChar: ", ""));
                            piGPSFails = Long.parseLong(lineTmp[11].replace(" GPSCheckSumFail: ", ""));
                            piHallAState = Integer.parseInt(lineTmp[12].replace(" HallAState: ",""));
                            piHallBState = Integer.parseInt(lineTmp[13].replace(" HallBState: ",""));
                            piGPSEstDist = Float.parseFloat(lineTmp[14].replace(" EstDistMI: ", ""));
                            piLightLevel = Integer.parseInt(lineTmp[15].replace(" LightLevel: ", ""));
                            piSwitchState = Integer.parseInt(lineTmp[16].replace(" SwitchState: ", ""));
                    }
                    inSerialMon.close();
            }
            catch (Exception ix) { }

            String pi2SQLQuery = "INSERT IGNORE INTO net_snmp.RaspberryPi2 ("
                    + "WalkTime, WalkTimeMatcher, NumUsers, Processes,"
                    + " CPULoad, CPULoad2, CPULoad3, CPULoad4,"
                    + " LoadIndex1, LoadIndex5, LoadIndex15,"
                    + " OctetsIn, OctetsOut, WiFiOctetsIn, WiFiOctetsOut,"
                    + " KMemPhys, KMemVirt, KMemBuff, KMemCached, KMemShared, KSwap, K4Root,"
                    + " KMemPhysU, KMemVirtU, KMemBuffU, KMemCachedU, KMemSharedU, KSwapU, K4RootU,"
                    + " UptimeSec, ExtTemp, GPSCoords, GPSAgeMS, GPSCourse, GPSAltiCM, GPSSpeedMPH,"
                    + " GPSDataChars, GPSChecksumFails, HallAState, HallBState, GPSEstDist,"
                    + " LightLevel, SwitchState"
                    + ") VALUES ("
                    + "'"+thisTimestamp+"', (SELECT MAX(WalkTime) FROM net_snmp.Main), "+piNumUsers+","+piProcesses+","
                    + piCPULoad1+","+piCPULoad2+","+piCPULoad3+","+piCPULoad4+","
                    + piLoadIndex1+","+piLoadIndex5+","+piLoadIndex15+","
                    + piEthIn+","+piEthOut+","+piWiFiIn+","+piWiFiOut+","
                    + piKMemPhys+","+piKMemVirt+","+piKMemBuff+","+piKMemCached+","+piKMemShared+","+piKSwap+","+piK4Root+","
                    + piKMemPhysU+","+piKMemVirtU+","+piKMemBuffU+","+piKMemCachedU+","+piKMemSharedU+","+piKSwapU+","+piK4RootU+","
                    + piRunSec+","+piExtTemp+",'["+piGPSLon+","+piGPSLat+"]',"+piGPSAge+","+piGPSCourse+","+piGPSAlti+","+piGPSSpeed+","
                    + piGPSChars+","+piGPSFails+","+piHallAState+","+piHallBState+",'"+piGPSEstDist+"',"
                    + piLightLevel+","+piSwitchState
                    + ");";

            System.out.println(pi2SQLQuery);

            try { pioSerialMonFile2.delete(); } catch (Exception e) { }

            try { wc.q2do1c(dbc, pi2SQLQuery, null); } catch (Exception e) { e.printStackTrace(); }
                    
    }
    
}
