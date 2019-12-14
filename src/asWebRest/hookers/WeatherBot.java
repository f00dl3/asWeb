/*
by Anthony Stump
Created: 14 Dec 2019
Updated: on creation
 */

package asWebRest.hookers;

import asUtils.Shares.StumpJunk;


public class WeatherBot {
        
	public static void startBot() {
		
		String commandToRun = "cd ../../../../asWxBot; node bot.js 'Auto-start on VirtualBox Tomcat WAR deployment.' &";
		StumpJunk sj = new StumpJunk();
		try { sj.runProcess(commandToRun); } catch (Exception e) { e.printStackTrace(); }
		
	}
    
}


