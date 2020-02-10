/*
Created by Anthony Stump
Created: 20 Jan 2020
Updated: 9 Feb 2020
 */

package asUtilsPorts.Pi2;

import asUtilsPorts.Jobs.Pi2.Crontabs_Pi2;
import asUtilsPorts.Shares.SSHTools;
import asWebRest.secure.JunkyPrivate;
import asWebRest.shared.ThreadRipper;
import asWebRest.shared.WebCommon;

import java.io.File;
import java.util.ArrayList;

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
        Thread procs[] = { ta /*, tb*/, tc };
        for (Thread thread : procs) { thread.start(); }
        
    }
    
    public static void main(String[] args) {
        System.out.println("Raspberry Pi 2 @Boot");
        doAtBoot();        
    }
    
    public static void pi2DesktopTunnel2() {        
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
    
    
    public static void startVideo() {
    	
    	// Feb 9 2020 - Does not work due to something?
    	
    	ThreadRipper tr = new ThreadRipper();
    	WebCommon wc = new WebCommon();
    	
		ArrayList<String> prereqs = new ArrayList<String>();
		prereqs.add("modprobe v4l2loopback video_nr=20");
		prereqs.add("v4l2-ctl --set-fmt-video=width=1024,height=768 --set-ctrl=exposure_time_absolute=220");
		prereqs.add("v4l2compress_omx /dev/video0 /dev/video20 &");
		prereqs.add("v4l2rtspserver -W 954 -H 540 -F 15 -P 8554 /dev/video20 &");
        
		ArrayList<Runnable> threads = new ArrayList<Runnable>();
		for(int i = 0; i < prereqs.size(); i++) {
			final int tpi = i;
			threads.add(() -> wc.runProcess(prereqs.get(tpi)));	
			try { Thread.sleep(1000); } catch (Exception e) { e.printStackTrace(); }
		}
		tr.runProcesses(threads, true, true);
        
    }
    
}
