/*
by Anthony Stump
Created: 8 May 2019
Updated: 30 Dec 2019
 */

package asUtilsPorts;

import asUtils.Secure.JunkyPrivate;
import asWebRest.shared.CommonBeans;
import asWebRest.shared.ThreadRipper;
import asWebRest.shared.WebCommon;

import java.io.File;


public class StartupNotify {

    private static void atBoot() {
        
        CommonBeans cb = new CommonBeans();
        JunkyPrivate junkyPrivate = new JunkyPrivate();
        ThreadRipper tr = new ThreadRipper();
        WebCommon wc = new WebCommon();
                
        final String ramDrive = cb.getRamPath();
        final String sharePath = cb.getPersistTomcat();
        final File hostWalkFile = new File(ramDrive+"/snmpwalk.txt");
        final File guestDestination = new File(sharePath+"/snmpwalk.txt");
        final File sysLog = new File("/var/log/syslog");
        final File packedLog = new File(ramDrive+"/syslog.zip");
        final String myCell = junkyPrivate.getSmsAddress();
        final String myGmail = junkyPrivate.getGmailUser();
        final String thisSubject = "asWeb started up!";
        final String thisMessage = "This means a system reboot likely occurred!\n" +
        		"Deployment: " + cb.getWarDeployBase() + "\n" +
        		"Threads: " + tr.getMaxThreads();

        wc.zipThisFile(sysLog, packedLog);
        Thread cm1 = new Thread(() -> { Mailer.sendMail(myCell, thisSubject, thisMessage, null); });
        Thread cm2 = new Thread(() -> { Mailer.sendMail(myGmail, thisSubject, thisMessage, packedLog); });
        Thread mailers[] = { cm1, cm2 };
        for (Thread thread : mailers) { thread.start(); }
        for (int i = 0; i < mailers.length; i++) { try { mailers[i].join(); } catch (InterruptedException nx) { nx.printStackTrace(); } }
        packedLog.delete();
                
        try { wc.copyFile(hostWalkFile.toString(), guestDestination.toString()); } catch (Exception e) { e.printStackTrace(); }
        
    }
    
    public static void getAtBoot() { atBoot(); }
    
    public static void main(String[] args) {
        getAtBoot();
    }
    
}
