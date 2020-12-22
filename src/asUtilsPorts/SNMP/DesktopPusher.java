/*
SNMP Walk -> Database --- Desktop class
Split off for v5 on 28 Apr 2019
Java created: 14 Aug 2017
Last updated: 20 Dec 2020
 */

package asUtilsPorts.SNMP;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.json.JSONObject;

import asUtilsPorts.Shares.JunkyBeans;
import asUtilsPorts.Shares.SSLHelper;
import asWebRest.shared.WebCommon;

public class DesktopPusher {
    
    public void snmpDesktopPusher() {
        
        JunkyBeans junkyBeans = new JunkyBeans();
        SSLHelper sslHelper = new SSLHelper();
        WebCommon wc = new WebCommon();
        
        final String ramPath = junkyBeans.getRamDrive().toString() + "/snmpDesktop";
        final File ramPathF = new File(ramPath);
        final String zipOut = ramPath + "/snmpDesktop.zip";
        final String apiUpload = sslHelper.getApiUpload();
        
        final File duMySQLFile = new File(ramPath+"/duMySQL.txt");
        final File ns5File = new File(ramPath+"/ns5out.txt");
        final File sensorFile = new File(ramPath+"/sensors.txt");
        final File theWalkFile = new File(ramPath+"/snmpwalk.txt");
        final File upsFile = new File(ramPath+"/upsstats.cgi");
        final File nvOutFile = new File(ramPath+"/nvidia-smi.txt");
        final File codeFile = new File(ramPath+"/codeLines.json");
        final File lbaFile = new File(ramPath+"/lba_sdb1.txt");
        final File asWebJavaPath = new File(junkyBeans.getUserHome().toString()+"/src/codex/Java/asWeb");
        
        final String aPayload = junkyBeans.getUserHome().toString()+"/aPayload.zip";
        final File aPayloadFile = new File(aPayload);
                
        if(!ramPathF.exists()) { ramPathF.mkdirs(); }
			
        Thread s1t1 = new Thread(new Runnable() { public void run() { try { wc.runProcessOutFile("sensors", sensorFile, false); } catch (FileNotFoundException fe) { fe.printStackTrace(); } }});
        Thread s1t2 = new Thread(new Runnable() { public void run() { try { wc.runProcessOutFile("snmpwalk localhost .", theWalkFile, false); } catch (FileNotFoundException fe) { fe.printStackTrace(); } }});
        Thread s1t3 = new Thread(new Runnable() { public void run() { try { wc.runProcessOutFile("netstat -W | grep \"astump-Desktop\" | grep \"ESTAB\"", ns5File, false); } catch (FileNotFoundException fe) { fe.printStackTrace(); } }});
        Thread s1t4 = new Thread(new Runnable() { public void run() { try { wc.jsoupOutFile("http://127.0.0.1/cgi-bin/apcupsd/upsstats.cgi", upsFile); } catch (Exception e) { } }});		
        Thread s1t5 = new Thread(new Runnable() { public void run() { try { wc.runProcessOutFile("du /var/lib/mysql", duMySQLFile, false); } catch (FileNotFoundException fe) { fe.printStackTrace(); } }});
        Thread s1t6 = new Thread(new Runnable() { public void run() { try { wc.runProcessOutFile("nvidia-smi", nvOutFile, false); } catch (FileNotFoundException fe) { fe.printStackTrace(); } }});
        Thread s1t7 = new Thread(new Runnable() { public void run() { try { wc.runProcessOutFile("smartctl -t short -a /dev/sdb1 | grep 241", lbaFile, false); } catch (Exception e) { } }});
        Thread thListA[] = { s1t1, s1t2, s1t3, /* s1t4, */ s1t5, s1t6, s1t7 };
        for (Thread thread : thListA) { thread.start(); try { thread.join(); } catch (InterruptedException nx) { nx.printStackTrace(); } }
        /* for (int i = 0; i < thListA.length; i++) { try { thListA[i].join(); } catch (InterruptedException nx) { nx.printStackTrace(); } } */

        try { wc.runProcessOutFile("snmpwalk -m MYSQL-SERVER-MIB localhost enterprises.20267", theWalkFile, true); } catch (FileNotFoundException fe) { fe.printStackTrace(); }
        
        String asUtilsJava = "";
        String aswjJava = "";
        String aswjJsp = "";
        String aswjJs = "";
        String aswjCss = "";
        long aswjLocJava = 0;
        long aswjLocJsp = 0;
        long aswjLocJs = 0;
        long aswjLocCss = 0;
        long asUtilsLocJava = 0;

        try {

            aswjJava = wc.runProcessOutVar("(find '" + asWebJavaPath.toString() + "' -name '*.java' | xargs wc -l)");
            aswjJsp = wc.runProcessOutVar("(find '" + asWebJavaPath.toString() + "/web' -name '*.jsp' | xargs wc -l)");
            aswjJs = wc.runProcessOutVar("(find '" + asWebJavaPath.toString() + "/web/jsBase' -name '*.js' | xargs wc -l)");
            aswjCss = wc.runProcessOutVar("(find '" + asWebJavaPath.toString() + "/web/css' -name '*.css' | xargs wc -l)");                           
            asUtilsJava = wc.runProcessOutVar("(find '" + junkyBeans.getCodeBase().toString() + "' -name '*.java' | xargs wc -l)");                   

            Scanner aswjJavaScanner = new Scanner(aswjJava);
            while (aswjJavaScanner.hasNextLine()) {
                String line = aswjJavaScanner.nextLine();
                if(line.contains(" total")) { Pattern p = Pattern.compile("(.*) total"); Matcher m = p.matcher(line); if (m.find()) { aswjLocJava = Long.parseLong(m.group(1).replaceAll(" +","")); } }
            }
            aswjJavaScanner.close();

            Scanner aswjJspScanner = new Scanner(aswjJsp);
            while (aswjJspScanner.hasNextLine()) {
                String line = aswjJspScanner.nextLine();
                if(line.contains(" total")) { Pattern p = Pattern.compile("(.*) total"); Matcher m = p.matcher(line); if (m.find()) { aswjLocJsp = Long.parseLong(m.group(1).replaceAll(" +","")); } }
            }
            aswjJspScanner.close();

            Scanner aswjJsScanner = new Scanner(aswjJs);
            while (aswjJsScanner.hasNextLine()) {
                String line = aswjJsScanner.nextLine();
                if(line.contains(" total")) { Pattern p = Pattern.compile("(.*) total"); Matcher m = p.matcher(line); if (m.find()) { aswjLocJs = Long.parseLong(m.group(1).replaceAll(" +","")); } }
            }
            aswjJsScanner.close();

            Scanner aswjCssScanner = new Scanner(aswjCss);
            while (aswjCssScanner.hasNextLine()) {
                String line = aswjCssScanner.nextLine();
                if(line.contains(" total")) { Pattern p = Pattern.compile("(.*) total"); Matcher m = p.matcher(line); if (m.find()) { aswjLocCss = Long.parseLong(m.group(1).replaceAll(" +","")); } }
            }
            aswjCssScanner.close();

            Scanner asUtilsJavaScanner = new Scanner(asUtilsJava);
            while (asUtilsJavaScanner.hasNextLine()) {
                String line = asUtilsJavaScanner.nextLine();
                if(line.contains(" total")) { Pattern p = Pattern.compile("(.*) total"); Matcher m = p.matcher(line); if (m.find()) { asUtilsLocJava = Long.parseLong(m.group(1).replaceAll(" +","")); } }
            }
            asUtilsJavaScanner.close();
            
        } catch (IOException ix) { ix.printStackTrace(); }

        JSONObject codeLines = new JSONObject();
        codeLines
                .put("aswjLocJava", aswjLocJava)
                .put("aswjLocJsp", aswjLocJsp)
                .put("aswjLocJs", aswjLocJs)
                .put("aswjLocCss", aswjLocCss)
                .put("asUtilsLocJava", asUtilsLocJava);
        try { wc.varToFile(codeLines.toString(), codeFile, false); } catch (Exception e) { e.printStackTrace(); }
        
        // create zip and upload to API here.
        try { wc.zipThisFolder(ramPathF, new File(zipOut)); } catch (Exception e) { e.printStackTrace(); }
        
        FileInputStream zis = null;
        
        try { zis = new FileInputStream(zipOut); } catch (Exception e) { e.printStackTrace(); }
        
        try {
            SSLHelper.getConnection(apiUpload)
                .data("upfile", "snmpDesktop.zip", zis)
                .post();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        File zipOutF = new File(zipOut);
        zipOutF.delete();
       
        if(aPayloadFile.exists()) { 
            
            FileInputStream zis2 = null;            
            try { zis2 = new FileInputStream(aPayloadFile); } catch (Exception e) { e.printStackTrace(); }
            try {
                SSLHelper.getConnection(apiUpload)
                    .data("upfile", "aPayload.zip", zis2)
                    .post();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }    
    
    public static void main(String[] args) {
        DesktopPusher dp = new DesktopPusher();
        try { dp.snmpDesktopPusher(); } catch (Exception e) { }
    }
    
    
}
