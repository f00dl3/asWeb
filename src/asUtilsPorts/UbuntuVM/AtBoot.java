/*
by Anthony Stump
Created on 8 May 2019
Updated on 6 Feb 2020
 */

package asUtilsPorts.UbuntuVM;

import asUtilsPorts.Jobs.Crontabs_UVM;
import asUtilsPorts.Shares.JunkyBeans;
import asUtilsPorts.Shares.UVMBeans;
import asUtilsPorts.Shares.SSHTools;
import asWebRest.secure.JunkyPrivate;
import asWebRest.shared.WebCommon;
import java.io.File;

public class AtBoot {
    
    private static void doAtBootUvm() {
        
    	Crontabs_UVM cUVM = new Crontabs_UVM();
        JunkyBeans junkyBeans = new JunkyBeans();
        UVMBeans uvmBeans = new UVMBeans();
        WebCommon wc = new WebCommon();

        final String ramDrive = junkyBeans.getRamDrive().toString();
        final String sharePath = junkyBeans.getWebRoot().toString();
        final File tomcatShare = new File(uvmBeans.getTomcatShare());
        final File hostWalkFile = new File(sharePath+"/snmpwalk.txt");
        final File guestDestination = new File(ramDrive+"/snmpwalk.txt");
        
        try { wc.copyFile(hostWalkFile.toString(), guestDestination.toString()); } catch (Exception e) { e.printStackTrace(); }
        
        try { 
            tomcatShare.mkdirs();
            wc.runProcess("chown -R tomcat " + tomcatShare.toString());
        } catch (Exception e) { }
        
		Thread ta = new Thread(() -> { pi2Tunnel(); });
		Thread procs[] = { ta };
		for (Thread thread : procs) { thread.start(); }

    }
    
    public static void getAtBootUvm() { doAtBootUvm(); }
    
    public static void main(String[] args) {        
        doAtBootUvm();	        
    }

	public static void pi2Tunnel() {
		SSHTools sshTools = new SSHTools();
		JunkyPrivate jp = new JunkyPrivate();
		final File keyfile = new File("/home/astump/.ssh/r4pi2");
		final String user = jp.getPiUser();
		final String host = jp.getIpForRaspPi2();
		final int sshPort = jp.getPi2SshPort();
		final int portL = 8555;
		final int portR = 8554;
		sshTools.backupPortForwardMethod(keyfile, user, host, sshPort, portL, portR);
	}
    
}
