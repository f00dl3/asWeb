/*
by Anthony Stump
Created: 14 Dec 2019
Updated: 16 Dec 2019
 */

package asWebRest.hookers;

import asUtils.Shares.StumpJunk;
import asWebRest.shared.CommonBeans;

public class WeatherBot {
        
	public static void startBot() {

		CommonBeans cb = new CommonBeans();
		StumpJunk sj = new StumpJunk();
		String thisWorkingFolder = cb.getWarDeployBase();
		
		String sendMessage = "node bot.js 'Auto-start @ " + thisWorkingFolder + "'";
		String startBot = "node bot.js > /dev/null 2>&1 < /dev/null &";
		
		
		String commandToRun = "cd '" + thisWorkingFolder + "';" +
				" cd asWxBot;" +
				// sendMessage + ";" +
				startBot;
			
		Thread wxbot = new Thread(() -> { try { sj.runProcess(commandToRun); } catch (Exception e) { e.printStackTrace(); } });
		Thread bots[] = { wxbot };
		for (Thread bot : bots) { bot.start(); } 
				
	}
    
}


