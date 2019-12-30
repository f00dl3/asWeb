/*
by Anthony Stump
Created: 14 Dec 2019
Updated: 30 Dec 2019
 */

package asWebRest.hookers;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;

public class WeatherBot {
        
	public static void startBot() {

		CommonBeans cb = new CommonBeans();
		WebCommon wc = new WebCommon();
		
		String thisWorkingFolder = cb.getWarDeployBase();
		
		String sendMessage = "node bot.js 'Auto-start @ " + thisWorkingFolder + "'";
		String startBot = "node bot.js > /dev/null 2>&1 < /dev/null &";
		
		
		String commandToRun = "cd '" + thisWorkingFolder + "';" +
				" cd asWxBot;" +
				// sendMessage + ";" +
				startBot;
			
		Thread wxbot = new Thread(() -> { try { wc.runProcess(commandToRun); } catch (Exception e) { e.printStackTrace(); } });
		Thread bots[] = { wxbot };
		for (Thread bot : bots) { bot.start(); } 
				
	}
    
}


