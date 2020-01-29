/*
SNMP Walk -> Database --- Raspberry Pi class
Split off for v5 on 28 Apr 2019
Java created: 14 Aug 2017
Last updated: 29 Jan 2020
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

public class Pi {
    
    public void snmpPi(Connection dbc) {
        
    	CommonBeans cb = new CommonBeans();
        WebCommon wc = new WebCommon();
        
        final String ramPath = cb.getPathChartCache();
        final String ramPathUnzipped = ramPath + "/snmpUnzip/Pi";
        final File piSnmpZip = new File(ramPath+"/snmpPi.zip");
        final File ramPathUnzippedF = new File(ramPathUnzipped);
        final DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        final Date date = new Date();
        final String thisTimestamp = dateFormat.format(date);
        
        if(!ramPathUnzippedF.exists()) { ramPathUnzippedF.mkdirs(); }
        try { wc.unzipFile(piSnmpZip.toString(), ramPathUnzipped); } catch (Exception e) { e.printStackTrace(); }
        wc.runProcess("mv "+ramPathUnzipped+"/dev/shm/snmpPi/* "+ramPathUnzipped);

        final File piWalkFile = new File(ramPathUnzipped+"/snmpwalkPi.txt");
        final File pioSerialMonFile = new File(ramPathUnzipped+"/pioSerialMon.log");
        
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

        Scanner piWalkFileScanner = null; try {		
                piWalkFileScanner = new Scanner(piWalkFile);
                while(piWalkFileScanner.hasNext()) {				
                        String line = piWalkFileScanner.nextLine();
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

        try { wc.sedFileReplace(pioSerialMonFile.getPath(), "\\n\\n", "\n"); } catch (Exception e) { }

        double piExtTemp = 0.0;
        int piExtAmbLight = 0;
        int piExtNoise = 0;
        long piRunSec = 0;
        String piExtIRLEDs = "";

        try {
                FileInputStream inSerialMon = new FileInputStream(pioSerialMonFile);
                BufferedReader br = new BufferedReader(new InputStreamReader(inSerialMon));
                String serialLine = null, tmpLine = null;
                while ((tmpLine = br.readLine()) != null) { serialLine = tmpLine; }
                String line = serialLine;
                if(line.contains("IRLeds:")) {
                        System.out.println(line);
                        String[] lineTmp = line.split(",");
                        piExtTemp = Double.parseDouble(lineTmp[0].replace("TempF: ", ""));
                        piExtAmbLight = Integer.parseInt(lineTmp[1].replace(" AmbLight: ", ""));
                        piExtIRLEDs = lineTmp[2].replace(" IRLeds: ", "");
                        piExtNoise = Integer.parseInt(lineTmp[3].replace(" Noise: ", ""));
                        piRunSec = Long.parseLong(lineTmp[4].replace(" Seconds: ", ""));
                }
                inSerialMon.close();
        }
        catch (Exception e) { }

        String piSQLQuery = "INSERT IGNORE INTO net_snmp.RaspberryPi ("
                + "WalkTime, WalkTimeMatcher, NumUsers, Processes,"
                + " CPULoad, CPULoad2, CPULoad3, CPULoad4,"
                + " LoadIndex1, LoadIndex5, LoadIndex15,"
                + " OctetsIn, OctetsOut, WiFiOctetsIn, WiFiOctetsOut,"
                + " KMemPhys, KMemVirt, KMemBuff, KMemCached, KMemShared, KSwap, K4Root,"
                + " KMemPhysU, KMemVirtU, KMemBuffU, KMemCachedU, KMemSharedU, KSwapU, K4RootU,"
                + " ExtTemp, ExtAmbLight, ExtIRLEDs, ExtNoise, UptimeSec"
                + ") VALUES ("
                + "'"+thisTimestamp+"', (SELECT MAX(WalkTime) FROM net_snmp.Main), "+piNumUsers+","+piProcesses+","
                + piCPULoad1+","+piCPULoad2+","+piCPULoad3+","+piCPULoad4+","
                + piLoadIndex1+","+piLoadIndex5+","+piLoadIndex15+","
                + piEthIn+","+piEthOut+","+piWiFiIn+","+piWiFiOut+","
                + piKMemPhys+","+piKMemVirt+","+piKMemBuff+","+piKMemCached+","+piKMemShared+","+piKSwap+","+piK4Root+","
                + piKMemPhysU+","+piKMemVirtU+","+piKMemBuffU+","+piKMemCachedU+","+piKMemSharedU+","+piKSwapU+","+piK4RootU+","
                + piExtTemp+","+piExtAmbLight+",'"+piExtIRLEDs+"',"+piExtNoise+","+piRunSec
                + ");";		

        System.out.println(piSQLQuery);
		
        try { wc.q2do1c(dbc, piSQLQuery, null); } catch (Exception e) { e.printStackTrace(); }
                
    }
}
