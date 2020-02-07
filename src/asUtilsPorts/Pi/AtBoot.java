/*
Created by Anthony Stump
Created: 22 May 2019
Updated: 6 Jan 2020
 */

package asUtilsPorts.Pi;

import asUtilsPorts.Jobs.Pi.Crontabs_Pi;
import asUtilsPorts.Pi.SendAPICall;
import asWebRest.secure.JunkyPrivate;
import asWebRest.shared.WebCommon;
import asUtilsPorts.Shares.JunkyBeans;
import asUtilsPorts.Shares.SSHTools;
import java.io.File;

public class AtBoot {
    
    private static void doAtBoot() {       
        
    	Crontabs_Pi cPi = new Crontabs_Pi();
        Thread ta = new Thread(() -> { startHomeSeer(); });
        Thread tb = new Thread(() -> { startVideo(); });
        Thread tc = new Thread(() -> { serialMonitor(); });
        Thread td = new Thread(() -> { pi2DesktopTunnel(); });
        Thread te = new Thread(() -> { cPi.scheduler(); });
        Thread procs[] = { ta, tb, tc, td, te };
        for (Thread thread : procs) { thread.start(); }
        
    }
    
    public static void main(String[] args) {
        
        System.out.println("Raspberry Pi @Boot");
        doAtBoot();
        
    }
    
    private static void pi2DesktopTunnel() {
        
    	SendAPICall sapi = new SendAPICall();
        SSHTools sshTools = new SSHTools();
        JunkyPrivate jp = new JunkyPrivate();
        final File keyfile = jp.getPiDesktopKey();
        final String user = jp.getDesktopUser();
        final String host = jp.getIpForDesktopLAN();
        final int sshPort = jp.getDesktopSshPort();
        final int portL = 9090;
        final int portR = 8444;
        sshTools.backupPortForwardMethod(keyfile, user, host, sshPort, portL, portR);
        sapi.smarthomeDoorEvent("TEST");
        
    }

    private static void serialMonitor() {
            WebCommon wc = new WebCommon();
            String processString = "cat /dev/ttyACM0 > /dev/shm/pioSerialMon.log &";
            try { wc.runProcess(processString); } catch (Exception e) { e.printStackTrace(); }
    }
    
    private static void startHomeSeer() {
        
        WebCommon wc = new WebCommon();
        final String start_HomeSeer = "cd /home/pi/HomeSeer; ./go &";
        wc.runProcess(start_HomeSeer);
        
    }
    
    private static void startVideo() {
        
        WebCommon wc = new WebCommon();
        JunkyBeans jb = new JunkyBeans();
        final String start_v4l2ctl = "v4l2-ctl --set-ctrl=vertical_flip=1 --set-ctrl=horizontal_flip=1 --set-ctrl=exposure_time_absolute=220";
        final String start_h264server = jb.getHelpers().toString() + "/h264_v4l2_rtspserver /dev/video0 -W 954 -H 540 -F 15 -P 8554";
        try { wc.runProcess(start_v4l2ctl); } catch (Exception e) { e.printStackTrace(); }
        try { wc.runProcess(start_h264server); } catch (Exception e) { e.printStackTrace(); }
        
    }
    
}
