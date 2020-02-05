/*
Created by Anthony Stump
Created: 20 Jan 2020
Updated: 4 Feb 2020
 */

package asUtilsPorts.Pi2;

import asUtilsPorts.Shares.JunkyBeans;
import asUtilsPorts.Shares.SSHTools;
import asWebRest.secure.JunkyPrivate;
import asWebRest.shared.WebCommon;

import java.io.File;

public class AtBoot {
    
    private static void doAtBoot() {       
        
        Thread ta = new Thread(() -> { pi2DesktopTunnel2(); });
        Thread tb = new Thread(() -> { startVideo(); });
        Thread procs[] = { ta , tb };
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
        
    	WebCommon wc = new WebCommon();
        JunkyBeans jb = new JunkyBeans();
        //final String basicCamCap = "ffmpeg -i /dev/video0 -update 1 /var/www/html/camLive.jpg > /dev/null 2>&1";
        final String start_v4l2ctl = "v4l2-ctl --set-fmt-video=width=1024,height=768 --set-ctrl=exposure_time_absolute=220";
 	final String start_v4l2loopback = "v4l2compress_omx /dev/video0 /dev/video20 &";
        final String start_h264server = "v4l2rtspserver -W 954 -H 540 -F 15 -P 8554 /dev/video20 &";
        try { wc.runProcess(start_v4l2ctl); } catch (Exception e) { e.printStackTrace(); }
        //try { wc.runProcess(basicCamCap); } catch (Exception e) { e.printStackTrace(); }
	try { wc.runProcess(start_v4l2loopback); } catch (Exception e) { e.printStackTrace(); }
        try { wc.runProcess(start_h264server); } catch (Exception e) { e.printStackTrace(); }
        
    }
    
}
