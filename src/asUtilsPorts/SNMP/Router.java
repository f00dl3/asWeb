/*
SNMP Walk -> Database --- Router class
Split off for v5 on 28 Apr 2019
Java created: 14 Aug 2017
Last updated: 26 Jan 2020
 */

package asUtilsPorts.SNMP;

import asUtils.Secure.JunkyPrivate;
import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Router {
    
    public void snmpRouter(Connection dbc) {
        
    	CommonBeans cb = new CommonBeans();
        JunkyPrivate junkyPrivate = new JunkyPrivate();
        WebCommon wc = new WebCommon();
        
        final String ramPath = cb.getPathChartCache();
        final DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        final Date date = new Date();
        final String thisTimestamp = dateFormat.format(date);
        
        final String snmpRtrUser = junkyPrivate.getSnmpRouterUser();
        final String snmpRtrPass = junkyPrivate.getSnmpRouterPass();
        final String ipForRouter = junkyPrivate.getIpForRouter();
        final File rtrWalkFile = new File(ramPath+"/snmpwalkR.txt");
        try { wc.runProcessOutFile( "snmpwalk -v3 -l authPriv -u "+snmpRtrUser+" -a MD5 -A "+snmpRtrPass+" -x DES -X "+snmpRtrPass+" "+ipForRouter+" .", rtrWalkFile, false); } catch (FileNotFoundException fe) { fe.printStackTrace(); }

        int rCPULoad1 = 0;
        int rCPULoad2 = 0;
        long rEth0Rx = 0;
        long rEth0Tx = 0;
        long rEth1Rx = 0;
        long rEth1Tx = 0;
        long rEth2Rx = 0;
        long rEth2Tx = 0;
        long rEth3Rx = 0;
        long rEth3Tx = 0;
        long rVlan1Rx = 0;
        long rVlan1Tx = 0;
        long rVlan2Rx = 0;
        long rVlan2Tx = 0;
        long rBr0Rx = 0;
        long rBr0Tx = 0;
        int rKMemPhys = 0;
        int rKMemVirt = 0;
        int rKMemBuff = 0;
        int rKMemCached = 0;
        int rKMemShared = 0;
        int rKSwap = 0;
        int rK4Root = 0;
        int rKMemPhysU = 0;
        int rKMemVirtU = 0;
        int rKMemBuffU = 0;
        int rKMemCachedU = 0;
        int rKMemSharedU = 0;
        int rKSwapU = 0;
        int rK4RootU = 0;

        Scanner rWalkFileScanner = null; try {		
                rWalkFileScanner = new Scanner(rtrWalkFile);
                while(rWalkFileScanner.hasNext()) {				
                        String line = rWalkFileScanner.nextLine();
                        if(line.contains("hrProcessorLoad.196608 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { rCPULoad1 = Integer.parseInt(m.group(1)); } }
                        if(line.contains("hrProcessorLoad.196609 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { rCPULoad2 = Integer.parseInt(m.group(1)); } }
                        if(line.contains("ifHCInOctets.4 =")) { Pattern p = Pattern.compile("Counter64: (.*)"); Matcher m = p.matcher(line); if (m.find()) { rEth0Rx = Long.parseLong(m.group(1)); } }
                        if(line.contains("ifHCOutOctets.4 =")) { Pattern p = Pattern.compile("Counter64: (.*)"); Matcher m = p.matcher(line); if (m.find()) { rEth0Tx = Long.parseLong(m.group(1)); } }	
                        if(line.contains("ifHCInOctets.11 =")) { Pattern p = Pattern.compile("Counter64: (.*)"); Matcher m = p.matcher(line); if (m.find()) { rEth1Rx = Long.parseLong(m.group(1)); } }
                        if(line.contains("ifHCOutOctets.11 =")) { Pattern p = Pattern.compile("Counter64: (.*)"); Matcher m = p.matcher(line); if (m.find()) { rEth1Tx = Long.parseLong(m.group(1)); } }	
                        if(line.contains("ifHCInOctets.12 =")) { Pattern p = Pattern.compile("Counter64: (.*)"); Matcher m = p.matcher(line); if (m.find()) { rEth2Rx = Long.parseLong(m.group(1)); } }
                        if(line.contains("ifHCOutOctets.12 =")) { Pattern p = Pattern.compile("Counter64: (.*)"); Matcher m = p.matcher(line); if (m.find()) { rEth2Tx = Long.parseLong(m.group(1)); } }	
                        if(line.contains("ifHCInOctets.13 =")) { Pattern p = Pattern.compile("Counter64: (.*)"); Matcher m = p.matcher(line); if (m.find()) { rEth3Rx = Long.parseLong(m.group(1)); } }
                        if(line.contains("ifHCOutOctets.13 =")) { Pattern p = Pattern.compile("Counter64: (.*)"); Matcher m = p.matcher(line); if (m.find()) { rEth3Tx = Long.parseLong(m.group(1)); } }	
                        if(line.contains("ifHCInOctets.8 =")) { Pattern p = Pattern.compile("Counter64: (.*)"); Matcher m = p.matcher(line); if (m.find()) { rVlan1Rx = Long.parseLong(m.group(1)); } }
                        if(line.contains("ifHCOutOctets.8 =")) { Pattern p = Pattern.compile("Counter64: (.*)"); Matcher m = p.matcher(line); if (m.find()) { rVlan1Tx = Long.parseLong(m.group(1)); } }	
                        if(line.contains("ifHCInOctets.9 =")) { Pattern p = Pattern.compile("Counter64: (.*)"); Matcher m = p.matcher(line); if (m.find()) { rVlan2Rx = Long.parseLong(m.group(1)); } }
                        if(line.contains("ifHCOutOctets.9 =")) { Pattern p = Pattern.compile("Counter64: (.*)"); Matcher m = p.matcher(line); if (m.find()) { rVlan2Tx = Long.parseLong(m.group(1)); } }
                        if(line.contains("ifHCInOctets.14 =")) { Pattern p = Pattern.compile("Counter64: (.*)"); Matcher m = p.matcher(line); if (m.find()) { rBr0Rx = Long.parseLong(m.group(1)); } }
                        if(line.contains("ifHCOutOctets.14 =")) { Pattern p = Pattern.compile("Counter64: (.*)"); Matcher m = p.matcher(line); if (m.find()) { rBr0Tx = Long.parseLong(m.group(1)); } }
                        if(line.contains("hrStorageSize.1 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { rKMemPhys = Integer.parseInt(m.group(1)); } }
                        if(line.contains("hrStorageSize.3 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { rKMemVirt = Integer.parseInt(m.group(1)); } }
                        if(line.contains("hrStorageSize.6 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { rKMemBuff = Integer.parseInt(m.group(1)); } }
                        if(line.contains("hrStorageSize.7 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { rKMemCached = Integer.parseInt(m.group(1)); } }
                        if(line.contains("hrStorageSize.8 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { rKMemShared = Integer.parseInt(m.group(1)); } }
                        if(line.contains("hrStorageSize.10 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { rKSwap = Integer.parseInt(m.group(1)); } }
                        if(line.contains("hrStorageSize.31 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { rK4Root = Integer.parseInt(m.group(1)); } }
                        if(line.contains("hrStorageUsed.1 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { rKMemPhysU = Integer.parseInt(m.group(1)); } }
                        if(line.contains("hrStorageUsed.3 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { rKMemVirtU = Integer.parseInt(m.group(1)); } }
                        if(line.contains("hrStorageUsed.6 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { rKMemBuffU = Integer.parseInt(m.group(1)); } }
                        if(line.contains("hrStorageUsed.7 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { rKMemCachedU = Integer.parseInt(m.group(1)); } }
                        if(line.contains("hrStorageUsed.8 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { rKMemSharedU = Integer.parseInt(m.group(1)); } }
                        if(line.contains("hrStorageUsed.10 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { rKSwapU = Integer.parseInt(m.group(1)); } }
                        if(line.contains("hrStorageUsed.31 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { rK4RootU = Integer.parseInt(m.group(1)); } }
                }
        } catch (FileNotFoundException e) { e.printStackTrace(); }

        String rSQLQuery = "INSERT IGNORE INTO net_snmp.Asus3200 ("
                + "WalkTime, WalkTimeMatcher, CPULoad1, CPULoad2,"
                + " eth0Rx, eth0Tx, eth1Rx, eth1Tx, eth2Rx, eth2Tx, eth3Rx, eth3Tx,"
                + " vlan1Rx, vlan1Tx, vlan2Rx, vlan2Tx, br0Rx, br0Tx,"
                + " KMemPhys, KMemVirt, KMemBuff, KMemCached, KMemShared, KSwap, K4Root,"
                + " KMemPhysU, KMemVirtU, KMemBuffU, KMemCachedU, KMemSharedU, KSwapU, K4RootU"
                + ") VALUES ("
                + "'"+thisTimestamp+"', (SELECT MAX(WalkTime) FROM net_snmp.Main), "+rCPULoad1+","+rCPULoad2+","
                + rEth0Rx+","+rEth0Tx+","+rEth1Rx+","+rEth1Tx+","+rEth2Rx+","+rEth2Tx+","+rEth3Rx+","+rEth3Tx+","
                + rVlan1Rx+","+rVlan1Tx+","+rVlan2Rx+","+rVlan2Tx+","+rBr0Rx+","+rBr0Tx+","
                + rKMemPhys+","+rKMemVirt+","+rKMemBuff+","+rKMemCached+","+rKMemShared+","+rKSwap+","+rK4Root+","
                + rKMemPhysU+","+rKMemVirtU+","+rKMemBuffU+","+rKMemCachedU+","+rKMemSharedU+","+rKSwapU+","+rK4RootU
                + ");";

        try { wc.q2do1c(dbc, rSQLQuery, null); } catch (Exception e) { e.printStackTrace(); }
         
    }
    
}
