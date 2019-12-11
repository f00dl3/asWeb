/*
by Anthony Stump
Created: 29 Nov 2019
Updated: 11 Dec 2019
 */

package asWebRest.hookers;

import java.io.File;

import asUtils.Secure.JunkyPrivate;
import asUtils.Shares.JunkyBeans;
import asUtils.Shares.SSHTools;
import asWebRest.secure.SshBeans;

public class SmartplugInterface {
        
	public static void setPlug(String command, String device) {
		
		SshBeans ssh = new SshBeans();
		JunkyBeans jb = new JunkyBeans();
		JunkyPrivate jp = new JunkyPrivate();
		
		String ip_controller = jp.getIpForDesktop();
		String ip_spRouter = jp.getIpForSmartplug3();
		String ip_spDesktop = jp.getIpForSmartplug1();
		String ipTarget = "";
		File keyFile = ssh.getSshKey_Pi2();
		
		switch(device.toLowerCase()) {
			case "desktop": ipTarget = ip_spDesktop; break;
			case "router": default: ipTarget = ip_spRouter; break;
		}

		String smartplugScript = "python " + jb.getHelpers().toString() + "/tplink-smartplug/tplink_smartplug.py -t " + ipTarget + " -c " + command;
		if(command.equals("cycle")) { 
			smartplugScript = "bash " + jb.getHelpers().toString() + "/tplink-smartplug/cycle.sh " + ipTarget;
		}
		
		String[] commandsToRun = { smartplugScript };
		
		System.out.println("DEBUG: ATTEMPTING TO RUN " + smartplugScript);
		
		try {
			System.out.println("DEBUG: Setting up tunnel...");
			SSHTools.sshRunCommands("pi", ip_controller, 39409, keyFile, commandsToRun);
			System.out.println("DEBUG: Commands ran successfully");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
    
}


