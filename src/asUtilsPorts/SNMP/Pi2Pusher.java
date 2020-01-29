/*
SNMP Walk -> Database --- Raspberry Pi 2 class
Split off for v5 on 28 Apr 2019
Java created: 14 Aug 2017
Last updated: 29 Jan 2020
 */

package asUtilsPorts.SNMP;

import asUtilsPorts.Shares.JunkyBeans;
import asUtilsPorts.Shares.SSLHelper;
import asWebRest.secure.JunkyPrivate;
import asWebRest.shared.WebCommon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Pi2Pusher {
    
    public void snmpPi2() {

        JunkyBeans junkyBeans = new JunkyBeans();
        JunkyPrivate junkyPrivate = new JunkyPrivate();
        SSLHelper sslHelper = new SSLHelper();
        WebCommon wc = new WebCommon();

        final String ramPath = junkyBeans.getRamDrive().toString() + "/snmpPi2";
        final File ramPathF = new File(ramPath);
        final String zipOut = junkyBeans.getRamDrive().toString() + "/snmpPi2.zip";
        final String snmpPiPass = junkyPrivate.getSnmpPiPass();
        final String snmpPiUser = junkyPrivate.getSnmpPiUser();
        final File pi2WalkFile = new File(ramPath+"/snmpwalkPi2.txt");
        final File pioSerialMonFile2 = new File(ramPath+"/pioSerialMon2.log");
        pioSerialMonFile2.delete();
        final String snmpOnThis = "localhost";
        final String apiUpload = sslHelper.getApiUploadPi();
        
        if(!ramPathF.exists()) { ramPathF.mkdirs(); }

        String snmpWalk = "snmpwalk -v3 -l authPriv -u "+snmpPiUser+" -a MD5 -A "+snmpPiPass+" -x DES -X "+snmpPiPass+" "+snmpOnThis+" .";

        System.out.println("DEBUG: Starting SNMP walk...");
        try { wc.runProcessOutFile(snmpWalk, pi2WalkFile, false); } catch (FileNotFoundException fe) { fe.printStackTrace(); }
	
        System.out.println("DEBUG: SNMP walk done.");
    
        try { wc.zipThisFolder(ramPathF, new File(zipOut)); } catch (Exception e) { e.printStackTrace(); }
        
        FileInputStream zis = null;
        
        try { zis = new FileInputStream(zipOut); } catch (Exception e) { e.printStackTrace(); }
        
        System.out.println("DEBUG: Pushing to API (" + apiUpload + ")");
        try {
            SSLHelper.getConnection(apiUpload)
                .data("upfile", "snmpPi2.zip", zis)
                .post();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        File zipOutF = new File(zipOut);
        zipOutF.delete();
        
    }
    
    public static void main(String[] args) {
        Pi2Pusher pi2p = new Pi2Pusher();
        pi2p.snmpPi2();
    }
   
    
}
