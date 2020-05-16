/*
SNMP Walk -> Database --- Raspberry Pi 3 class
Java created: 5 Mar 2020
Last updated: 16 May 2020
 */

package asUtilsPorts.SNMP;

import asUtilsPorts.Shares.JunkyBeans;
import asUtilsPorts.Shares.SSLHelper;
import asWebRest.secure.JunkyPrivate;
import asWebRest.shared.WebCommon;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class Pi3Pusher {
    
    public void snmpPi3() {

        JunkyBeans junkyBeans = new JunkyBeans();
        JunkyPrivate junkyPrivate = new JunkyPrivate();
        SSLHelper sslHelper = new SSLHelper();
        WebCommon wc = new WebCommon();

        final String ramPath = junkyBeans.getRamDrive().toString() + "/snmpPi3";
        final File ramPathF = new File(ramPath);
        final String zipOut = junkyBeans.getRamDrive().toString() + "/snmpPi3.zip";
        final String snmpPiPass = junkyPrivate.getSnmpPiPass();
        final String snmpPiUser = junkyPrivate.getSnmpPiUser();
        final File pi3WalkFile = new File(ramPath+"/snmpwalkPi3.txt");
        final File pioSerialMonFile3 = new File(ramPath+"/pioSerialMon3.log");
        pioSerialMonFile3.delete();
        final String snmpOnThis = "localhost";
        final String apiUpload = sslHelper.getApiUploadPi();
        
        if(!ramPathF.exists()) { ramPathF.mkdirs(); }

        String snmpWalk = "snmpwalk -v3 -l authPriv -u "+snmpPiUser+" -a MD5 -A "+snmpPiPass+" -x DES -X "+snmpPiPass+" "+snmpOnThis+" .";

        System.out.println("DEBUG: Starting SNMP walk...");
        try { wc.runProcessOutFile(snmpWalk, pi3WalkFile, false); } catch (FileNotFoundException fe) { fe.printStackTrace(); }
	
        System.out.println("DEBUG: SNMP walk done.");
    
        try { wc.zipThisFolder(ramPathF, new File(zipOut)); } catch (Exception e) { e.printStackTrace(); }
        
        FileInputStream zis = null;
        
        try { zis = new FileInputStream(zipOut); } catch (Exception e) { e.printStackTrace(); }
        
        System.out.println("DEBUG: Pushing to API (" + apiUpload + ")");
        try {
            SSLHelper.getConnection(apiUpload)
                .data("upfile", "snmpPi3.zip", zis)
                .post();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        File zipOutF = new File(zipOut);
        zipOutF.delete();
        
    }
    
    public static void main(String[] args) {
        Pi3Pusher pi3p = new Pi3Pusher();
        try { pi3p.snmpPi3(); } catch (Exception e) { }
    }
   
    
}
