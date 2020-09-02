/*
by Anthony Stump
Created: 8 May 2019
Updated: 29 Aug 2020
 */

package asUtilsPorts;

import asWebRest.hookers.WeatherBot;
import asWebRest.secure.JunkyPrivate;
import asWebRest.shared.CommonBeans;
import asWebRest.shared.ThreadRipper;
import asWebRest.shared.WebCommon;

import java.io.File;
import java.util.ArrayList;


public class StartupNotify {

    private static void atBoot() {
        
        CommonBeans cb = new CommonBeans();
        JunkyPrivate junkyPrivate = new JunkyPrivate();
        Mailer mailer = new Mailer();
        ThreadRipper tr = new ThreadRipper();
        WeatherBot wxb = new WeatherBot();
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
	final String greetGif = "https://gph.is/2b2nzrE";

        wc.zipThisFile(sysLog, packedLog);
        

		ArrayList<Runnable> alerts = new ArrayList<Runnable>();
		alerts.add(() -> mailer.sendMail(myCell, thisSubject, thisMessage, null));
		alerts.add(() -> mailer.sendMail(myGmail, thisSubject, thisMessage, packedLog));
		alerts.add(() -> wxb.botBroadcastOnly(greetGif));
		tr.runProcesses(alerts, false, false);
		
        packedLog.delete();
                
        try { wc.copyFile(hostWalkFile.toString(), guestDestination.toString()); } catch (Exception e) { e.printStackTrace(); }
        
    }
    
    public static void getAtBoot() { atBoot(); }
    
    public static void main(String[] args) {
        getAtBoot();
    }
    
}
