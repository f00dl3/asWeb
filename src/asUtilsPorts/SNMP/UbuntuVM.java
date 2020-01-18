/*
SNMP Walk -> Database --- Ubuntu Virtual Machine class
Java created: 28 Apr 2019
Last updated: 18 Jan 2020
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

public class UbuntuVM {
    
    public void snmpUbuntuVM(Connection dbc) {
        
    	UbuntuVMWorker uvmWorker = new UbuntuVMWorker();
    	uvmWorker.main();
    	
    	CommonBeans cb = new CommonBeans();
        JunkyPrivate junkyPrivate = new JunkyPrivate();
        DeepRowCount drc = new DeepRowCount();
        WebCommon wc = new WebCommon();
        
        final String ramPath = cb.getPathChartCache();
        final DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        final Date date = new Date();
        final String thisTimestamp = dateFormat.format(date);
        
        final File dbSizeFile = new File(ramPath+"/snmpUnzip/Desktop/dbsizes.txt");
        final File duMySQLFile = new File(ramPath+"/UVM_duMySQL.txt");
        final File ns5File = new File(ramPath+"/UVM_ns5out.txt");
        
        final String snmpPass = junkyPrivate.getSnmpUvmPass();
        final String snmpUser = junkyPrivate.getSnmpUvmUser();
     
        final File theWalkFile = new File(ramPath+"/snmpwalkUVM2.txt");

        final String snmpWalkString = "snmpwalk -v3 -l authPriv -u "+snmpUser+" -a MD5 -A "+snmpPass+" -x DES -X "+snmpPass+" localhost .";
        System.out.println("DEBUG: " + snmpWalkString);
        try { wc.runProcessOutFile(snmpWalkString, theWalkFile, false); } catch (FileNotFoundException fe) { fe.printStackTrace(); }
        try { wc.runProcessOutFile("snmpwalk -m MYSQL-SERVER-MIB -v3 -l authPriv -u "+snmpUser+" -a MD5 -A "+snmpPass+" -x DES -X "+snmpPass+" localhost enterprises.20267", theWalkFile, true); } catch (FileNotFoundException fe) { fe.printStackTrace(); }           
        
        wc.sedFileReplace(ramPath+"/UVM_duMySQL.txt", "\\t", ",");
        wc.sedFileReplace(ramPath+"/UVM_duMySQL.txt", "\\t", ",");
        
        int numUsers = 0;
        int cpuLoad1 = 0;
        int cpuLoad2 = 0;
        int cpuLoad3 = 0;
        int cpuLoad4 = 0;
        int cpuLoad5 = 0;
        int cpuLoad6 = 0;
        int cpuLoad7 = 0;
        int cpuLoad8 = 0;
        double loadIndex1 = 0.0;
        double loadIndex5 = 0.0;
        double loadIndex15 = 0.0;
        int processes = 0;
        long octetsIn = 0;
        long octetsOut = 0;
        long kMemPhys = 0;
        long kMemVirt = 0;
        long kMemBuff = 0;
        long kMemCached = 0;
        long kMemShared = 0;
        long kSwap = 0;
        long k4Root = 0;
        long kMemPhysU = 0;
        long kMemVirtU = 0;
        long kMemBuffU = 0;
        long kMemCachedU = 0;
        long kMemSharedU = 0;
        long kSwapU = 0;
        long k4RootU = 0;
        long iNodeSDA1 = 0;
        long iNodeSHM = 0;
        long diskIOSysRead = 0;
        long diskIOSysWrite = 0;
        long mySQLUpdate = 0;
        long mySQLInsert = 0;
        long mySQLSelect = 0;
        long mySQLReplace = 0;
        long mySQLDelete = 0;

        Scanner walkFileScanner = null; try {		
                walkFileScanner = new Scanner(theWalkFile);
                while(walkFileScanner.hasNext()) {				
                        String line = walkFileScanner.nextLine();
                        if(line.contains("hrSystemNumUsers.0 =")) { Pattern p = Pattern.compile("Gauge32: (.*)"); Matcher m = p.matcher(line); if (m.find()) { numUsers = Integer.parseInt(m.group(1)); }}
                        if(line.contains("hrProcessorLoad.196608 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { cpuLoad1 = Integer.parseInt(m.group(1)); }}
                        if(line.contains("hrProcessorLoad.196609 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { cpuLoad2 = Integer.parseInt(m.group(1)); } }					
                        if(line.contains("hrProcessorLoad.196610 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { cpuLoad3 = Integer.parseInt(m.group(1)); } }					
                        if(line.contains("hrProcessorLoad.196611 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { cpuLoad4 = Integer.parseInt(m.group(1)); } }					
                        if(line.contains("hrProcessorLoad.196612 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { cpuLoad5 = Integer.parseInt(m.group(1)); } }					
                        if(line.contains("hrProcessorLoad.196613 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { cpuLoad6 = Integer.parseInt(m.group(1)); } }					
                        if(line.contains("hrProcessorLoad.196614 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { cpuLoad7 = Integer.parseInt(m.group(1)); } }					
                        if(line.contains("hrProcessorLoad.196615 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { cpuLoad8 = Integer.parseInt(m.group(1)); } }
                        if(line.contains("laLoad.1 =")) { Pattern p = Pattern.compile("STRING: (.*)"); Matcher m = p.matcher(line); if (m.find()) { loadIndex1 = Double.parseDouble(m.group(1)); } }
                        if(line.contains("laLoad.2 =")) { Pattern p = Pattern.compile("STRING: (.*)"); Matcher m = p.matcher(line); if (m.find()) { loadIndex5 = Double.parseDouble(m.group(1)); } }
                        if(line.contains("laLoad.3 =")) { Pattern p = Pattern.compile("STRING: (.*)"); Matcher m = p.matcher(line); if (m.find()) { loadIndex15 = Double.parseDouble(m.group(1)); } }
                        if(line.contains("hrSystemProcesses.0 =")) { Pattern p = Pattern.compile("Gauge32: (.*)"); Matcher m = p.matcher(line); if (m.find()) { processes = Integer.parseInt(m.group(1)); } }
                        if(line.contains("ifHCInOctets.2 =")) { Pattern p = Pattern.compile("Counter64: (.*)"); Matcher m = p.matcher(line); if (m.find()) { octetsIn = Long.parseLong(m.group(1)); } }
                        if(line.contains("ifHCOutOctets.2 =")) { Pattern p = Pattern.compile("Counter64: (.*)"); Matcher m = p.matcher(line); if (m.find()) { octetsOut = Long.parseLong(m.group(1)); } }
                        if(line.contains("hrStorageSize.1 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { kMemPhys = Long.parseLong(m.group(1)); } }
                        if(line.contains("hrStorageSize.3 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { kMemVirt = Long.parseLong(m.group(1)); } }
                        if(line.contains("hrStorageSize.6 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { kMemBuff = Long.parseLong(m.group(1)); } }
                        if(line.contains("hrStorageSize.7 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { kMemCached = Long.parseLong(m.group(1)); } }
                        if(line.contains("hrStorageSize.8 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { kMemShared = Long.parseLong(m.group(1)); } }
                        if(line.contains("hrStorageSize.10 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { kSwap = Long.parseLong(m.group(1)); } }
                        if(line.contains("hrStorageSize.31 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { k4Root = Long.parseLong(m.group(1)); } }
                        if(line.contains("hrStorageUsed.1 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { kMemPhysU = Long.parseLong(m.group(1)); } }
                        if(line.contains("hrStorageUsed.3 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { kMemVirtU = Long.parseLong(m.group(1)); } }
                        if(line.contains("hrStorageUsed.6 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { kMemBuffU = Long.parseLong(m.group(1)); } }
                        if(line.contains("hrStorageUsed.7 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { kMemCachedU = Long.parseLong(m.group(1)); } }
                        if(line.contains("hrStorageUsed.8 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { kMemSharedU = Long.parseLong(m.group(1)); } }
                        if(line.contains("hrStorageUsed.10 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { kSwapU = Long.parseLong(m.group(1)); } }
                        if(line.contains("hrStorageUsed.31 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { k4RootU = Long.parseLong(m.group(1)); } }
                        if(line.contains("dskPercentNode.3 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { iNodeSDA1 = Long.parseLong(m.group(1)); } }
                        if(line.contains("dskPercentNode.6 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { iNodeSHM = Long.parseLong(m.group(1)); } }
                        if(line.contains("ssIORawReceived.0 =")) { Pattern p = Pattern.compile("Counter32: (.*)"); Matcher m = p.matcher(line); if (m.find()) { diskIOSysRead = Long.parseLong(m.group(1)); } }
                        if(line.contains("ssIORawSent.0 =")) { Pattern p = Pattern.compile("Counter32: (.*)"); Matcher m = p.matcher(line); if (m.find()) { diskIOSysWrite = Long.parseLong(m.group(1)); } }
                        if(line.contains("myComUpdate.0 =")) { Pattern p = Pattern.compile("Counter32: (.*)"); Matcher m = p.matcher(line); if (m.find()) { mySQLUpdate = Long.parseLong(m.group(1)); } }
                        if(line.contains("myComInsert.0 =")) { Pattern p = Pattern.compile("Counter32: (.*)"); Matcher m = p.matcher(line); if (m.find()) { mySQLInsert = Long.parseLong(m.group(1)); } }
                        if(line.contains("myComSelect.0 =")) { Pattern p = Pattern.compile("Counter32: (.*)"); Matcher m = p.matcher(line); if (m.find()) { mySQLSelect = Long.parseLong(m.group(1)); } }
                        if(line.contains("myComReplace.0 =")) { Pattern p = Pattern.compile("Counter32: (.*)"); Matcher m = p.matcher(line); if (m.find()) { mySQLReplace = Long.parseLong(m.group(1)); } }
                        
                }
                
        } catch (FileNotFoundException e) { e.printStackTrace(); }

        long mySQLRowsCore = 0;
        long mySQLRowsFeeds = 0;
        long mySQLRowsNetSNMP = 0;
        long mySQLRowsWebCal = 0;
        long mySQLRowsWxObs = 0;
        double mySQLSizeCore = 0.0;
        double mySQLSizeFeeds = 0.0;
        double mySQLSizeNetSNMP = 0.0;
        double mySQLSizeWebCal = 0.0;
        double mySQLSizeWxObs = 0.0;

        Scanner dbSizeScanner = null; try {			
                dbSizeScanner = new Scanner(dbSizeFile);
                while(dbSizeScanner.hasNext()) {			
                        String line = dbSizeScanner.nextLine();
                        if(line.contains("Core,")) {
                                String[] lineTmp = line.split(",");
                                String strThisVal = lineTmp[1]; String strThisVa2 = lineTmp[2];
                                mySQLSizeCore = Double.parseDouble(strThisVal);
                                mySQLRowsCore = drc.totalRowCountFromDatabase(dbc, "Core");
                        }	
                        if(line.contains("Feeds,")) {
                                String[] lineTmp = line.split(",");
                                String strThisVal = lineTmp[1]; String strThisVa2 = lineTmp[2];
                                mySQLSizeFeeds = Double.parseDouble(strThisVal);
                                mySQLRowsFeeds = drc.totalRowCountFromDatabase(dbc, "Feeds");
                        }	
                        if(line.contains("net_snmp,")) {
                                String[] lineTmp = line.split(",");
                                String strThisVal = lineTmp[1]; String strThisVa2 = lineTmp[2];
                                mySQLSizeNetSNMP = Double.parseDouble(strThisVal);
                                mySQLRowsNetSNMP = drc.totalRowCountFromDatabase(dbc, "net_snmp");
                        }	
                        if(line.contains("WebCal,")) {
                                String[] lineTmp = line.split(",");
                                String strThisVal = lineTmp[1]; String strThisVa2 = lineTmp[2];
                                mySQLSizeWebCal = Double.parseDouble(strThisVal);
                                mySQLRowsWebCal = drc.totalRowCountFromDatabase(dbc, "WebCal");
                        }	
                        if(line.contains("WxObs,")) {
                                String[] lineTmp = line.split(",");
                                String strThisVal = lineTmp[1]; String strThisVa2 = lineTmp[2];
                                mySQLSizeWxObs = Double.parseDouble(strThisVal);
                                mySQLRowsWxObs = drc.totalRowCountFromDatabase(dbc, "WxObs");
                        }	
                }
        }
        catch (FileNotFoundException e) { e.printStackTrace(); }
        catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); }

        long duMySQLCore = 0;
        long duMySQLFeeds = 0;
        long duMySQLNetSNMP = 0;
        long duMySQLTotal = 0;
        long duMySQLWebCal = 0;
        long duMySQLWxObs = 0;

        Scanner duMySQLScanner = null; try {			
                duMySQLScanner = new Scanner(duMySQLFile);
                while(duMySQLScanner.hasNext()) {			
                        String line = duMySQLScanner.nextLine();
                        if(line.contains(",/var/lib/mysql/Core")) { String[] lineTmp = line.split(","); duMySQLCore = Long.parseLong(lineTmp[0]); }	
                        if(line.contains(",/var/lib/mysql/Feeds")) { String[] lineTmp = line.split(","); duMySQLFeeds = Long.parseLong(lineTmp[0]); }	
                        if(line.contains(",/var/lib/mysql/net_snmp")) { String[] lineTmp = line.split(","); duMySQLNetSNMP = Long.parseLong(lineTmp[0]); }	
                        if(line.contains(",/var/lib/mysql/WxObs")) { String[] lineTmp = line.split(","); duMySQLWxObs = Long.parseLong(lineTmp[0]); }	
                        if(line.contains(",/var/lib/mysql/WebCal")) { String[] lineTmp = line.split(","); duMySQLWebCal = Long.parseLong(lineTmp[0]); }	
                        if(line.contains(",/var/lib/mysql")) { String[] lineTmp = line.split(","); duMySQLTotal = Long.parseLong(lineTmp[0]); }
                }
        }
        catch (FileNotFoundException e) { e.printStackTrace(); }
        catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); }

        int ns5Active = 0;
        String ns5ActiveCon = "";

        Scanner ns5Scanner = null; try {			
                ns5Scanner = new Scanner(ns5File);
                while(ns5Scanner.hasNext()) {				
                        String line = ns5Scanner.nextLine();
                        ns5Active++;
                        String[] lineTmpA = line.split(","); String strThisValA = lineTmpA[4]; ns5ActiveCon = ns5ActiveCon + " " + strThisValA;
                }
        }
        catch (FileNotFoundException e) { e.printStackTrace(); }
        catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); }

        String thisSQLQuery = "INSERT IGNORE INTO net_snmp.UbuntuVM2 ("
                + "WalkTime, NumUsers,"
                + " CPULoad1, CPULoad2, CPULoad3, CPULoad4, CPULoad5, CPULoad6, CPULoad7, CPULoad8,"
                + " LoadIndex1, LoadIndex5, LoadIndex15, Processes,"
                + " OctetsIn, OctetsOut,"
                + " INodeSDA1, INodeSHM, DiskIOSysRead, DiskIOSysWrite,"
                + " KMemPhys, KMemVirt, KMemBuff, KMemCached, KMemShared, KSwap, K4Root,"
                + " KMemPhysU, KMemVirtU, KMemBuffU, KMemCachedU, KMemSharedU, KSwapU, K4RootU,"
                + " MySQLUpdate, MySQLInsert, MySQLSelect, MySQLReplace, MySQLDelete,"
                + " MySQLRowsCore, MySQLRowsNetSNMP, MySQLRowsWebCal, MySQLRowsWxObs, MySQLRowsFeeds,"
                + " MySQLSizeCore, MySQLSizeNetSNMP, MySQLSizeWebCal, MySQLSizeWxObs, MySQLSizeFeeds,"
                + " NS5Active, NS5ActiveCon,"
                + " duMySQLCore, duMySQLNetSNMP, duMySQLWebCal, duMySQLWxObs, duMySQLFeeds, duMySQLTotal) VALUES ("
                + "'"+thisTimestamp+"',"+numUsers+","
                + cpuLoad1+","+cpuLoad2+","+cpuLoad3+","+cpuLoad4+","+cpuLoad5+","+cpuLoad6+","+cpuLoad7+","+cpuLoad8+","
                + loadIndex1+","+loadIndex5+","+loadIndex15+","+processes+","
                + octetsIn+","+octetsOut+","
                + iNodeSDA1+","+iNodeSHM+","+diskIOSysRead+","+diskIOSysWrite+","
                + kMemPhys+","+kMemVirt+","+kMemBuff+","+kMemCached+","+kMemShared+","+kSwap+","+k4Root+","
                + kMemPhysU+","+kMemVirtU+","+kMemBuffU+","+kMemCachedU+","+kMemSharedU+","+kSwapU+","+k4RootU+","
                + mySQLUpdate+","+mySQLInsert+","+mySQLSelect+","+mySQLReplace+","+mySQLDelete+","
                + mySQLRowsCore+","+mySQLRowsNetSNMP+","+mySQLRowsWebCal+","+mySQLRowsWxObs+","+mySQLRowsFeeds+","
                + mySQLSizeCore+","+mySQLSizeNetSNMP+","+mySQLSizeWebCal+","+mySQLSizeWxObs+","+mySQLSizeFeeds+","
                + ns5Active+",'"+ns5ActiveCon+"',"
                + duMySQLCore+","+duMySQLNetSNMP+","+duMySQLWebCal+","+duMySQLWxObs+","+duMySQLFeeds+","+duMySQLTotal+");";

        System.out.println(thisSQLQuery);
    	try { wc.q2do1c(dbc, thisSQLQuery, null); } catch (Exception e) { e.printStackTrace(); }
                
    }
    
}
