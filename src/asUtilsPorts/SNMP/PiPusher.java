/*
SNMP Walk -> Database --- Raspberry Pi class
Split off for v5 on 28 Apr 2019
Java created: 14 Aug 2017
Last updated: 31 Jan 2020
 */

package asUtilsPorts.SNMP;

import asWebRest.secure.JunkyPrivate;
import asUtilsPorts.Shares.JunkyBeans;
import asUtilsPorts.Shares.SSLHelper;
import asWebRest.shared.WebCommon;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class PiPusher {
    
    public void snmpPi() {
        
        JunkyBeans junkyBeans = new JunkyBeans();
        JunkyPrivate junkyPrivate = new JunkyPrivate();
        SSLHelper sslHelper = new SSLHelper();
        WebCommon wc = new WebCommon();
        
        final String ramPath = junkyBeans.getRamDrive().toString() + "/snmpPi";
        final File ramPathF = new File(ramPath);
        
        final String snmpPiPass = junkyPrivate.getSnmpPiPass();
        final String snmpPiUser = junkyPrivate.getSnmpPiUser();
        final File piWalkFile = new File(ramPath+"/snmpwalkPi.txt");
        final File pioSerialMonFile = new File(ramPath+"/pioSerialMon.log");
        final String zipOut = junkyBeans.getRamDrive().toString() + "/snmpPi.zip";
        pioSerialMonFile.delete();
        final String snmpOnThis = "localhost";
        final String apiUpload = sslHelper.getApiUploadPi();
        
        if(!ramPathF.exists()) { ramPathF.mkdirs(); }
        
        String snmpWalk = "snmpwalk -v3 -l authPriv -u "+snmpPiUser+" -a MD5 -A "+snmpPiPass+" -x DES -X "+snmpPiPass+" "+snmpOnThis+" .";

        System.out.println("DEBUG: Starting SNMP walk...");
        try { wc.runProcessOutFile(snmpWalk, piWalkFile, false); } catch (FileNotFoundException fe) { fe.printStackTrace(); }
	
        System.out.println("DEBUG: SNMP walk done.");

        try { wc.zipThisFolder(ramPathF, new File(zipOut)); } catch (Exception e) { e.printStackTrace(); }
        
        FileInputStream zis = null;
        
        try { zis = new FileInputStream(zipOut); } catch (Exception e) { e.printStackTrace(); }
        
        System.out.println("DEBUG: Pushing to API (" + apiUpload + ")");
        try {
            SSLHelper.getConnection(apiUpload)
                .data("upfile", "snmpPi.zip", zis)
                .post();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        File zipOutF = new File(zipOut);
        zipOutF.delete();
        
    }
    
    public static void main(String[] args) {
        PiPusher pip = new PiPusher();
        pip.snmpPi();
    }
    
}
