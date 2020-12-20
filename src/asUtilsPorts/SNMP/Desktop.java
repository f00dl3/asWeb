/*
SNMP Walk -> Database --- Desktop class
Split off for v5 on 28 Apr 2019
Java created: 14 Aug 2017
Last updated: 20 Dec 2020
 */

package asUtilsPorts.SNMP;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.SNMPBeans;
import asWebRest.shared.WebCommon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONObject;
import org.json.JSONTokener;

import asUtilsPorts.Shares.JunkyBeans;

public class Desktop {
    
    public void snmpDesktop(Connection dbc) {
        
    	CommonBeans cb = new CommonBeans();
        JunkyBeans junkyBeans = new JunkyBeans();
        SNMPBeans snmpBeans = new SNMPBeans();
        //DeepRowCount drc = new DeepRowCount();
        WebCommon wc = new WebCommon();
        
        final double tA = snmpBeans.getTa();
        final int multFact = snmpBeans.getMultFact();
        final String ramPathPreUnzip = cb.getPathChartCache() + "/snmpDesktop.zip";
        final String ramPath = cb.getPathChartCache() + "/snmpUnzip/Desktop";
        final File ramPathF = new File(ramPath);
        final DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        final Date date = new Date();
        final String thisTimestamp = dateFormat.format(date);
        
        final File dtSnmpZip = new File(ramPathPreUnzip);
        
        if(!ramPathF.exists()) { ramPathF.mkdirs(); }
        try { wc.unzipFile(dtSnmpZip.toString(), ramPath); } catch (Exception e) { e.printStackTrace(); }
        wc.runProcess("mv "+ramPath+"/dev/shm/snmpDesktop/* "+ramPath);
        
        final File dbSizeFile = new File(ramPath+"/dbsizes.txt");
        final File duMySQLFileUVM = new File(ramPath+"/UVM_duMySQL.txt");
        final File ns5File = new File(ramPath+"/ns5out.txt");
        final File sensorFile = new File(ramPath+"/sensors.txt");
        final File theWalkFile = new File(ramPath+"/snmpwalk.txt");
        final File upsFile = new File(ramPath+"/upsstats.cgi");
        final File nvOutFile = new File(ramPath+"/nvidia-smi.txt");
        final File locJson = new File(ramPath+"/codeLines.json");

        String dbSizeQuery = "SELECT" +
                " table_schema 'Database'," +
                " ROUND(SUM(data_length + index_length)/1024/1024, 1) 'DBSizeMB'," +
                " GREATEST(SUM(TABLE_ROWS), SUM(AUTO_INCREMENT)) AS DBRows" +
                " FROM information_schema.tables" +
                " GROUP BY table_schema;";
        try {
            ResultSet resultSet = wc.q2rs1c(dbc, dbSizeQuery, null);
            System.setOut(new PrintStream(new FileOutputStream(dbSizeFile, false)));			
            while (resultSet.next()) {
                System.out.println(resultSet.getString("Database")+","+resultSet.getString("DBSizeMB")+","+resultSet.getString("DBRows"));
            }            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        try { wc.sedFileReplace(ramPath+"/sensors.txt", " +", ","); } catch (Exception e) { }
        try { wc.sedFileReplace(ramPath+"/ns5out.txt", " +", ","); } catch (Exception e) { }
        try { wc.sedFileReplace(ramPath+"/dbsizes.txt", "null", "0"); } catch (Exception e) { }
        try { wc.sedFileReplace(ramPath+"/duMySQL.txt", "\\t", ","); } catch (Exception e) { }
        try { wc.sedFileReplace(ramPath+"/duMySQL.txt", "\\t", ","); } catch (Exception e) { }
        //StumpJunk.sedFileReplace(nvOutFile.getPath(), "|", " ");
        try { wc.sedFileReplace(nvOutFile.getPath(), "\\/", " "); } catch (Exception e) { }
        try { wc.sedFileReplace(nvOutFile.getPath(), " +", ","); } catch (Exception e) { }
        
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
        long k4Extra0 = 0;
        long k4Extra1 = 0;
        long kMemPhysU = 0;
        long kMemVirtU = 0;
        long kMemBuffU = 0;
        long kMemCachedU = 0;
        long kMemSharedU = 0;
        long kSwapU = 0;
        long k4RootU = 0;
        long k4Extra0U = 0;
        long k4Extra1U = 0;
        long iNodeSDA1 = 0;
        long iNodeSHM = 0;
        long diskIOSysRead = 0;
        long diskIOSysWrite = 0;
        long mySQLUpdate = 0;
        long mySQLInsert = 0;
        long mySQLSelect = 0;
        long mySQLReplace = 0;
        long mySQLDelete = 0;
        long logApache2GET = 0;
        long logTomcatGET = 0;
        String extra0DiskID = "";
        String extra1DiskID = "";

        Scanner walkFileScanner = null; try {		
                walkFileScanner = new Scanner(theWalkFile);
                while(walkFileScanner.hasNext()) {				
                        String line = walkFileScanner.nextLine();
                        if(line.contains("hrStorage") && line.contains("/extra0")) { Pattern p = Pattern.compile("Descr.(.*) ="); Matcher m = p.matcher(line); if (m.find()) { extra0DiskID = m.group(1); }}
                        if(line.contains("hrStorage") && line.contains("/extra1")) { Pattern p = Pattern.compile("Descr.(.*) ="); Matcher m = p.matcher(line); if (m.find()) { extra1DiskID = m.group(1); }}
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
                        if(line.contains("hrStorageSize."+extra0DiskID+" =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { k4Extra0 = Long.parseLong(m.group(1)); } }
                        if(line.contains("hrStorageSize."+extra1DiskID+" =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { k4Extra1 = Long.parseLong(m.group(1)); } }
                        if(line.contains("hrStorageUsed.1 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { kMemPhysU = Long.parseLong(m.group(1)); } }
                        if(line.contains("hrStorageUsed.3 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { kMemVirtU = Long.parseLong(m.group(1)); } }
                        if(line.contains("hrStorageUsed.6 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { kMemBuffU = Long.parseLong(m.group(1)); } }
                        if(line.contains("hrStorageUsed.7 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { kMemCachedU = Long.parseLong(m.group(1)); } }
                        if(line.contains("hrStorageUsed.8 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { kMemSharedU = Long.parseLong(m.group(1)); } }
                        if(line.contains("hrStorageUsed.10 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { kSwapU = Long.parseLong(m.group(1)); } }
                        if(line.contains("hrStorageUsed.31 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { k4RootU = Long.parseLong(m.group(1)); } }
                        if(line.contains("hrStorageUsed."+extra0DiskID+" =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { k4Extra0U = Long.parseLong(m.group(1)); } }
                        if(line.contains("hrStorageUsed."+extra1DiskID+" =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { k4Extra1U = Long.parseLong(m.group(1)); } }
                        if(line.contains("dskPercentNode.3 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { iNodeSDA1 = Long.parseLong(m.group(1)); } }
                        if(line.contains("dskPercentNode.6 =")) { Pattern p = Pattern.compile("INTEGER: (.*)"); Matcher m = p.matcher(line); if (m.find()) { iNodeSHM = Long.parseLong(m.group(1)); } }
                        if(line.contains("ssIORawReceived.0 =")) { Pattern p = Pattern.compile("Counter32: (.*)"); Matcher m = p.matcher(line); if (m.find()) { diskIOSysRead = Long.parseLong(m.group(1)); } }
                        if(line.contains("ssIORawSent.0 =")) { Pattern p = Pattern.compile("Counter32: (.*)"); Matcher m = p.matcher(line); if (m.find()) { diskIOSysWrite = Long.parseLong(m.group(1)); } }
                        if(line.contains("myComUpdate.0 =")) { Pattern p = Pattern.compile("Counter32: (.*)"); Matcher m = p.matcher(line); if (m.find()) { mySQLUpdate = Long.parseLong(m.group(1)); } }
                        if(line.contains("myComInsert.0 =")) { Pattern p = Pattern.compile("Counter32: (.*)"); Matcher m = p.matcher(line); if (m.find()) { mySQLInsert = Long.parseLong(m.group(1)); } }
                        if(line.contains("myComSelect.0 =")) { Pattern p = Pattern.compile("Counter32: (.*)"); Matcher m = p.matcher(line); if (m.find()) { mySQLSelect = Long.parseLong(m.group(1)); } }
                        if(line.contains("myComReplace.0 =")) { Pattern p = Pattern.compile("Counter32: (.*)"); Matcher m = p.matcher(line); if (m.find()) { mySQLReplace = Long.parseLong(m.group(1)); } }
                        if(line.contains("logMatchCurrentCounter.1 =")) { Pattern p = Pattern.compile("Counter32: (.*)"); Matcher m = p.matcher(line); if (m.find()) { logApache2GET = Long.parseLong(m.group(1)); } }
                        if(line.contains("logMatchCurrentCounter.5 =")) { Pattern p = Pattern.compile("Counter32: (.*)"); Matcher m = p.matcher(line); if (m.find()) { logTomcatGET = Long.parseLong(m.group(1)); } }
                }			
        } catch (FileNotFoundException e) { e.printStackTrace(); }

        int tempCPU = 0;
        int tempCase = 0;
        int tempCore1 = 0;
        int tempCore2 = 0;
        int tempCore3 = 0;
        int tempCore4 = 0;
        int fan1 = 0;
        int fan2 = 0;
        int fan3 = 0;
        double voltCPU = 0.0;
        double voltCore1 = 0.0;
        double voltCore2 = 0.0;
        double voltCore3 = 0.0;
        double voltCore4 = 0.0;
        double voltPlus33 = 0.0;
        double voltPlus5 = 0.0;
        double voltPlus12 = 0.0;
        double voltBatt = 0.0;

        Scanner sensorFileScanner = null; try {		
                sensorFileScanner = new Scanner(sensorFile);
                while(sensorFileScanner.hasNext()) {				
                        String line = sensorFileScanner.nextLine();
                        if(line.contains("Package,id,0:,")) { String[] lineTmp = line.split(","); String strThisVal = lineTmp[3].replaceAll("\\D+", ""); Double dThisVal = Double.parseDouble(strThisVal); tempCPU = (int) (dThisVal * tA * multFact); }
                        if(line.contains("SYSTIN:,")) { String[] lineTmp = line.split(","); String strThisVal = lineTmp[1].replaceAll("\\D+", ""); Double dThisVal = Double.parseDouble(strThisVal); tempCase = (int) (dThisVal * tA * multFact);  }
                        if(line.contains("Core,0:,")) { String[] lineTmp = line.split(","); String strThisVal = lineTmp[2].replaceAll("\\D+", ""); Double dThisVal = Double.parseDouble(strThisVal); tempCore1 = (int) (dThisVal * tA * multFact); }
                        if(line.contains("Core,1:,")) { String[] lineTmp = line.split(","); String strThisVal = lineTmp[2].replaceAll("\\D+", ""); Double dThisVal = Double.parseDouble(strThisVal); tempCore2 = (int) (dThisVal * tA * multFact); }
                        if(line.contains("Core,2:,")) { String[] lineTmp = line.split(","); String strThisVal = lineTmp[2].replaceAll("\\D+", ""); Double dThisVal = Double.parseDouble(strThisVal); tempCore3 = (int) (dThisVal * tA * multFact); }
                        if(line.contains("Core,3:,")) { String[] lineTmp = line.split(","); String strThisVal = lineTmp[2].replaceAll("\\D+", ""); Double dThisVal = Double.parseDouble(strThisVal); tempCore4 = (int) (dThisVal * tA * multFact); }
                        if(line.contains("Fan,1:,")) { String[] lineTmp = line.split(","); String strThisVal = lineTmp[2].replaceAll("\\D+", ""); fan1 = Integer.parseInt(strThisVal); }
                        if(line.contains("Fan,2:,")) { String[] lineTmp = line.split(","); String strThisVal = lineTmp[2].replaceAll("\\D+", ""); fan2 = Integer.parseInt(strThisVal); }
                        if(line.contains("CPU,fan:,")) { String[] lineTmp = line.split(","); String strThisVal = lineTmp[2].replaceAll("\\D+", ""); fan3 = Integer.parseInt(strThisVal); }
                        if(line.contains("CPU,Vcc:,")) { String[] lineTmp = line.split(","); String strThisVal = lineTmp[2].replaceAll("\\D+", ""); Double dThisVal = Double.parseDouble(strThisVal); voltCPU = (dThisVal / multFact); }
                        if(line.contains("core,0:,+")) { String[] lineTmp = line.split(","); String strThisVal = lineTmp[2].replaceAll("\\D+", ""); Double dThisVal = Double.parseDouble(strThisVal); voltCore1 = (dThisVal / multFact); }
                        if(line.contains("core,1:,+")) { String[] lineTmp = line.split(","); String strThisVal = lineTmp[2].replaceAll("\\D+", ""); Double dThisVal = Double.parseDouble(strThisVal); voltCore2 = (dThisVal / multFact); }
                        if(line.contains("core,2:,+")) { String[] lineTmp = line.split(","); String strThisVal = lineTmp[2].replaceAll("\\D+", ""); Double dThisVal = Double.parseDouble(strThisVal); voltCore3 = (dThisVal / multFact); }
                        if(line.contains("core,3:,+")) { String[] lineTmp = line.split(","); String strThisVal = lineTmp[2].replaceAll("\\D+", ""); Double dThisVal = Double.parseDouble(strThisVal); voltCore4 = (dThisVal / multFact); }
                        if(line.contains("+3.3V:,+")) { String[] lineTmp = line.split(","); String strThisVal = lineTmp[1].replaceAll("\\D+", ""); Double dThisVal = Double.parseDouble(strThisVal); voltPlus33 = (dThisVal / multFact); }
                        if(line.contains("+5V:,+")) { String[] lineTmp = line.split(","); String strThisVal = lineTmp[1].replaceAll("\\D+", ""); Double dThisVal = Double.parseDouble(strThisVal); voltPlus5 = (dThisVal / multFact); }
                        if(line.contains("+12V:,+")) { String[] lineTmp = line.split(","); String strThisVal = lineTmp[1].replaceAll("\\D+", ""); Double dThisVal = Double.parseDouble(strThisVal); voltPlus12 = (dThisVal / multFact); }
                        if(line.contains("Vbat:,+")) { String[] lineTmp = line.split(","); String strThisVal = lineTmp[1].replaceAll("\\D+", ""); Double dThisVal = Double.parseDouble(strThisVal); voltBatt = (dThisVal / multFact); }				
                }					
        }
        catch (FileNotFoundException e) { e.printStackTrace(); }
        catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); }

        long mySQLRowsCore = 0;
        long mySQLRowsFeeds = 0;
        long mySQLRowsFinances = 0;
        long mySQLRowsNetSNMP = 0;
        long mySQLRowsWebCal = 0;
        long mySQLRowsWxObs = 0;
        double mySQLSizeCore = 0.0;
        double mySQLSizeFeeds = 0.0;
        double mySQLSizeFinances = 0.0;
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
                                try { mySQLSizeCore = Double.parseDouble(strThisVal); } catch (Exception e) { }
                                mySQLRowsCore = 0; //drc.totalRowCountFromDatabase(dbc, "Core");
                        }	
                        if(line.contains("Feeds,")) {
                                String[] lineTmp = line.split(",");
                                String strThisVal = lineTmp[1]; String strThisVa2 = lineTmp[2];
                                try { mySQLSizeFeeds = Double.parseDouble(strThisVal); } catch (Exception e) { }
                                mySQLRowsFeeds = 0; //drc.totalRowCountFromDatabase(dbc, "Feeds");
                        }	
                        if(line.contains("Finances,")) {
                            String[] lineTmp = line.split(",");
                            String strThisVal = lineTmp[1]; String strThisVa2 = lineTmp[2];
                            try { mySQLSizeFinances = Double.parseDouble(strThisVal); } catch (Exception e) { }
                            mySQLRowsFinances = 0; //drc.totalRowCountFromDatabase(dbc, "Feeds");
                    }	
                        if(line.contains("net_snmp,")) {
                                String[] lineTmp = line.split(",");
                                String strThisVal = lineTmp[1]; String strThisVa2 = lineTmp[2];
                                try { mySQLSizeNetSNMP = Double.parseDouble(strThisVal); } catch (Exception e) { }
                                mySQLRowsNetSNMP = 0; //drc.totalRowCountFromDatabase(dbc, "net_snmp");
                        }	
                        if(line.contains("WebCal,")) {
                                String[] lineTmp = line.split(",");
                                String strThisVal = lineTmp[1]; String strThisVa2 = lineTmp[2];
                                try { mySQLSizeWebCal = Double.parseDouble(strThisVal); } catch (Exception e) { }
                                mySQLRowsWebCal = 0; //drc.totalRowCountFromDatabase(dbc, "WebCal");
                        }	
                        if(line.contains("WxObs,")) {
                                String[] lineTmp = line.split(",");
                                String strThisVal = lineTmp[1]; String strThisVa2 = lineTmp[2];
                                try { mySQLSizeWxObs = Double.parseDouble(strThisVal); } catch (Exception e) { }
                                mySQLRowsWxObs = 0; //drc.totalRowCountFromDatabase(dbc, "WxObs");
                        }	
                }
        }
        catch (FileNotFoundException e) { e.printStackTrace(); }
        catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); }

        long duMySQLCore = 0;
        long duMySQLFeeds = 0;
        long duMySQLFinances = 0;
        long duMySQLNetSNMP = 0;
        long duMySQLTotal = 0;
        long duMySQLWebCal = 0;
        long duMySQLWxObs = 0;

        Scanner duMySQLScanner = null; try {			
                duMySQLScanner = new Scanner(duMySQLFileUVM); //duMySQLFile <== old
                while(duMySQLScanner.hasNext()) {			
                        String line = duMySQLScanner.nextLine();
                        if(line.contains(",/var/lib/mysql/Core")) { String[] lineTmp = line.split(","); duMySQLCore = Long.parseLong(lineTmp[0]); }	
                        if(line.contains(",/var/lib/mysql/Feeds")) { String[] lineTmp = line.split(","); duMySQLFeeds = Long.parseLong(lineTmp[0]); }
                        if(line.contains(",/var/lib/mysql/Finances")) { String[] lineTmp = line.split(","); duMySQLFinances = Long.parseLong(lineTmp[0]); }
                        if(line.contains(",/var/lib/mysql/net_snmp")) { String[] lineTmp = line.split(","); duMySQLNetSNMP = Long.parseLong(lineTmp[0]); }	
                        if(line.contains(",/var/lib/mysql/WxObs")) { String[] lineTmp = line.split(","); duMySQLWxObs = Long.parseLong(lineTmp[0]); }	
                        if(line.contains(",/var/lib/mysql/WebCal")) { String[] lineTmp = line.split(","); duMySQLWebCal = Long.parseLong(lineTmp[0]); }	
                        if(line.contains(",/var/lib/mysql")) { String[] lineTmp = line.split(","); duMySQLTotal = Long.parseLong(lineTmp[0]); }
                }
        }
        catch (FileNotFoundException e) { }
        catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); }

        String ns5ActiveSSHClient = "";
        int ns5Active = 0;
        String ns5ActiveCon = "";
        int ns5ActiveSSH = 0;

        Scanner ns5Scanner = null; try {			
                ns5Scanner = new Scanner(ns5File);
                while(ns5Scanner.hasNext()) {				
                        String line = ns5Scanner.nextLine();
                        ns5Active++;
                        String[] lineTmpA = line.split(","); String strThisValA = lineTmpA[4]; ns5ActiveCon = ns5ActiveCon + " " + strThisValA;
                        if(line.contains("39406")) { String[] lineTmp = line.split(","); String strThisVal = lineTmp[4]; ns5ActiveSSHClient = ns5ActiveSSHClient + " " + strThisVal; ns5ActiveSSH++; }
                }
        }
        catch (FileNotFoundException e) { e.printStackTrace(); }
        catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); }

        double upsLoad = 0.0;
        double upsTimeLeft = 0.0;

        Scanner upsScanner = null; try {			
                upsScanner = new Scanner(upsFile);
                while(upsScanner.hasNext()) {				
                        String line = upsScanner.nextLine();
                        if(line.contains("upsload")) { Pattern p = Pattern.compile("value=(.*)\" alt"); Matcher m = p.matcher(line); if (m.find()) { upsLoad = Double.parseDouble(m.group(1)); } }
                        if(line.contains("runtime")) { Pattern p = Pattern.compile("value=(.*)&amp"); Matcher m = p.matcher(line); if (m.find()) { upsTimeLeft = Double.parseDouble(m.group(1)); } }
                }
        } catch (FileNotFoundException e) { }

        int tomcatWars = 0;
        int tomcatDeploy = 0;
        int crashLogs = 0;

        try {
                tomcatWars = Integer.parseInt(wc.runProcessOutVar("ls "+junkyBeans.getTomcatWebapps().toString()+"/*.war | wc -l").replaceAll("\\D+", ""));
                tomcatDeploy = Integer.parseInt(wc.runProcessOutVar("ls "+junkyBeans.getTomcatWebapps().toString()+" -p | grep \"/\" | wc -l").replaceAll("\\D+", ""));
                crashLogs = Integer.parseInt(wc.runProcessOutVar("ls /var/crash/ | wc -l").replaceAll("\\D+", ""));
        } catch (IOException ix) { ix.printStackTrace(); }

        JSONObject expandedJson = new JSONObject();

        long aswjLocJava = 0;
        long aswjLocJsp = 0;
        long aswjLocJs = 0;
        long aswjLocCss = 0;
        long asUtilsLocJava = 0;

        try {

        	InputStream is = new FileInputStream(locJson);
        	JSONTokener jt = new JSONTokener(is);
        	JSONObject locJo = new JSONObject(jt);
        	aswjLocJava = locJo.getLong("aswjLocJava");
        	aswjLocJsp = locJo.getLong("aswjLocJsp");
        	aswjLocJs = locJo.getLong("aswjLocJs");
        	aswjLocCss = locJo.getLong("aswjLocCss");
        	asUtilsLocJava = locJo.getLong("asUtilsLocJava");

        } catch (Exception ix) { ix.printStackTrace(); }

        int nvFan = 0;
        int nvMemUse = 0;
        int nvMemTotal = 0;
        int nvTemp = 0;
        int nvPowerUse = 0;
        int nvPowerCap = 0;
        int nvUtilization = 0;
        String nvPerf = "";

        Scanner nVidiaScanner = null; try {		
                nVidiaScanner = new Scanner(nvOutFile);
                while(nVidiaScanner.hasNext()) {				
                        String line = nVidiaScanner.nextLine();
                        if(line.contains("2001MiB")) {
                            String[] lineTmp = line.split(",");
                            nvFan = Integer.parseInt(lineTmp[1].replace("%", ""));
                            nvTemp = Integer.parseInt(lineTmp[2].replace("C", ""));
                            nvPerf = lineTmp[3];
                            nvPowerUse = Integer.parseInt(lineTmp[4].replace("W", ""));
                            nvPowerCap = Integer.parseInt(lineTmp[5].replace("W", ""));
                            nvMemUse = Integer.parseInt(lineTmp[7].replace("MiB", ""));
                            nvMemTotal = Integer.parseInt(lineTmp[8].replace("MiB", ""));
                            nvUtilization = Integer.parseInt(lineTmp[10].replace("%", ""));
                        }
                }					
        }
        catch (FileNotFoundException e) { e.printStackTrace(); }
        catch (ArrayIndexOutOfBoundsException aix) { aix.printStackTrace(); }

        expandedJson
            .put("LOC_aswjJava", aswjLocJava)
            .put("LOC_aswjJsp", aswjLocJsp)
            .put("LOC_aswjJs", aswjLocJs)
            .put("LOC_aswjCss", aswjLocCss)
            .put("LOC_asUtilsJava", asUtilsLocJava)
            //.put("LOC_asUtilsJavaSh", asUtilsLocJavaSh)
            .put("k4Extra0", k4Extra0)
            .put("k4Extra0U", k4Extra0U)
            .put("k4Extra1", k4Extra1)
            .put("k4Extra1U", k4Extra1U)
            .put("nvFan", nvFan)
            .put("nvMemUse", nvMemUse)
            .put("nvMemTotal", nvMemTotal)
            .put("nvTemp", nvTemp)
            .put("nvPerf", nvPerf)
            .put("nvPowerUse", nvPowerUse) 
            .put("nvPowerCap", nvPowerCap)
            .put("nvUtilization", nvUtilization)
            .put("mySQLRowsFinances", mySQLRowsFinances)
            .put("mySQLSizeFinances", mySQLSizeFinances)
            .put("duMySQLFinances", duMySQLFinances);

        String thisSQLQuery = "INSERT IGNORE INTO net_snmp.Main ("
                + "WalkTime, NumUsers,"
                + " CPULoad1, CPULoad2, CPULoad3, CPULoad4, CPULoad5, CPULoad6, CPULoad7, CPULoad8,"
                + " LoadIndex1, LoadIndex5, LoadIndex15, Processes,"
                + " OctetsIn, OctetsOut, LogApache2GET, TomcatGET,"
                + " INodeSDA1, INodeSHM, DiskIOSysRead, DiskIOSysWrite,"
                + " KMemPhys, KMemVirt, KMemBuff, KMemCached, KMemShared, KSwap, K4Root,"
                + " KMemPhysU, KMemVirtU, KMemBuffU, KMemCachedU, KMemSharedU, KSwapU, K4RootU,"
                + " MySQLUpdate, MySQLInsert, MySQLSelect, MySQLReplace, MySQLDelete,"
                + " TempCPU, TempCase, TempCore1, TempCore2, TempCore3, TempCore4,"
                + " Fan1, Fan2, Fan3, VoltCPU, VoltCore1, VoltCore2, VoltCore3, VoltCore4,"
                + " VoltPlus33, VoltPlus5, VoltPlus12, VoltBatt,"
                + " MySQLRowsCore, MySQLRowsNetSNMP, MySQLRowsWebCal, MySQLRowsWxObs, MySQLRowsFeeds,"
                + " MySQLSizeCore, MySQLSizeNetSNMP, MySQLSizeWebCal, MySQLSizeWxObs, MySQLSizeFeeds,"
                + " NS5Active, NS5ActiveSSH, SSHClientIP, NS5ActiveCon,"
                + " UPSLoad, UPSTimeLeft, TomcatWARs, TomcatDeploy, CrashLogs,"
                + " duMySQLCore, duMySQLNetSNMP, duMySQLWebCal, duMySQLWxObs, duMySQLFeeds, duMySQLTotal,"
                + " ExpandedJSONData) VALUES ("
                + "'"+thisTimestamp+"',"+numUsers+","
                + cpuLoad1+","+cpuLoad2+","+cpuLoad3+","+cpuLoad4+","+cpuLoad5+","+cpuLoad6+","+cpuLoad7+","+cpuLoad8+","
                + loadIndex1+","+loadIndex5+","+loadIndex15+","+processes+","
                + octetsIn+","+octetsOut+","+logApache2GET+","+logTomcatGET+","
                + iNodeSDA1+","+iNodeSHM+","+diskIOSysRead+","+diskIOSysWrite+","
                + kMemPhys+","+kMemVirt+","+kMemBuff+","+kMemCached+","+kMemShared+","+kSwap+","+k4Root+","
                + kMemPhysU+","+kMemVirtU+","+kMemBuffU+","+kMemCachedU+","+kMemSharedU+","+kSwapU+","+k4RootU+","
                + mySQLUpdate+","+mySQLInsert+","+mySQLSelect+","+mySQLReplace+","+mySQLDelete+","
                + tempCPU+","+tempCase+","+tempCore1+","+tempCore2+","+tempCore3+","+tempCore4+","
                + fan1+","+fan2+","+fan3+","+voltCPU+","+voltCore1+","+voltCore2+","+voltCore3+","+voltCore4+","
                + voltPlus33+","+voltPlus5+","+voltPlus12+","+voltBatt+","
                + mySQLRowsCore+","+mySQLRowsNetSNMP+","+mySQLRowsWebCal+","+mySQLRowsWxObs+","+mySQLRowsFeeds+","
                + mySQLSizeCore+","+mySQLSizeNetSNMP+","+mySQLSizeWebCal+","+mySQLSizeWxObs+","+mySQLSizeFeeds+","
                + ns5Active+","+ns5ActiveSSH+",'"+ns5ActiveSSHClient+"','"+ns5ActiveCon+"',"
                + upsLoad+","+upsTimeLeft+","+tomcatWars+","+tomcatDeploy+","+crashLogs+","
                + duMySQLCore+","+duMySQLNetSNMP+","+duMySQLWebCal+","+duMySQLWxObs+","+duMySQLFeeds+","+duMySQLTotal+","
                + "'" + expandedJson.toString() + "');";

    	try { wc.q2do1c(dbc, thisSQLQuery, null); } catch (Exception e) { e.printStackTrace(); }
                        
    }    
    
}
