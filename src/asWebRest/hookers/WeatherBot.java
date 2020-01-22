/*
by Anthony Stump
Created: 14 Dec 2019
Updated: 21 Jan 2020
 */

package asWebRest.hookers;

import java.util.ArrayList;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.ThreadRipper;
import asWebRest.shared.WebCommon;

public class WeatherBot {
	
	public void botBroadcastOnly(String message) {

		CommonBeans cb = new CommonBeans();
		ThreadRipper tr = new ThreadRipper();
		WebCommon wc = new WebCommon();
		
		String thisWorkingFolder = cb.getWarDeployBase();
		String sendMessage = "node bot.js '" +message + "'";		
		
		String commandToRun = "cd '" + thisWorkingFolder + "';" +
				" cd asWxBot;" + 
				sendMessage;
		
		ArrayList<Runnable> bots = new ArrayList<Runnable>();
		bots.add(() -> wc.runProcess(commandToRun));
		tr.runProcesses(bots, false, false);
		
	}
        
	public void startBot() {

		CommonBeans cb = new CommonBeans();
		ThreadRipper tr = new ThreadRipper();
		WebCommon wc = new WebCommon();
		
		String thisWorkingFolder = cb.getWarDeployBase();		
		String startBot = "node bot.js > /dev/null 2>&1 < /dev/null &";		
		
		String commandToRun = "cd '" + thisWorkingFolder + "';" +
				" cd asWxBot;" + 
				startBot;
		
		ArrayList<Runnable> bots = new ArrayList<Runnable>();
		bots.add(() -> wc.runProcess(commandToRun));
		tr.runProcesses(bots, false, true);
		
				
	}
    
}


