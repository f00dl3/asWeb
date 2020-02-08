/*
Created by Anthony Stump
Created: 20 Jan 2020
Updated: 8 Feb 2020
 */

package asUtilsPorts.Pi2;

import asUtilsPorts.Jobs.Pi2.Crontabs_Pi2;
import asUtilsPorts.Shares.SSHTools;
import asWebRest.secure.JunkyPrivate;
import asWebRest.shared.WebCommon;

import java.io.File;

import asUtilsPorts.Shares.HelperPermissions;
import asUtilsPorts.Shares.JunkyBeans;

public class AtBoot {
    
    private static void doAtBoot() {
    	
		Crontabs_Pi2 cPi2 = new Crontabs_Pi2();
		HelperPermissions hp = new HelperPermissions();
		
		hp.helperChmods();
		
        Thread ta = new Thread(() -> { pi2DesktopTunnel2(); });
        Thread tb = new Thread(() -> { startVideo(); });
        Thread tc = new Thread(() -> { cPi2.scheduler(); });
        Thread procs[] = { ta , tb, tc };
        for (Thread thread : procs) { thread.start(); }
        
    }
    
    public static void main(String[] args) {
        System.out.println("Raspberry Pi 2 @Boot");
        doAtBoot();        
    }
    
    private static void pi2DesktopTunnel2() {        
        SSHTools sshTools = new SSHTools();
        JunkyPrivate jp = new JunkyPrivate();
        final File keyfile = jp.getPi2DesktopKey();
        final String user = jp.getDesktopUser();
        final String host = jp.getIpForDesktop();
        final int sshPort = jp.getDesktopSshPort();
        final int portL = 9090;
        final int portR = 8444;
        sshTools.backupPortForwardMethod(keyfile, user, host, sshPort, portL, portR);        
    }
    
    
    private static void startVideo() {        
    	JunkyBeans jb = new JunkyBeans();
    	WebCommon wc = new WebCommon();
    	final String startVideo = "bash " + jb.getHelpers().toString() + "/startPi2Video.sh";
        System.out.println("DEBUG: " + startVideo); 
        Thread ta = new Thread(() -> { wc.runProcess(startVideo); });
        Thread procs[] = { ta };
        for(Thread thread : procs) { thread.start(); }        
    }
    
}
