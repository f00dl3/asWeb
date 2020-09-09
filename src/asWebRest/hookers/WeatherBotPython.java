/*
by Anthony Stump
Created: 5 Sep 2020
Updated: 5 Sep 2020
 */

package asWebRest.hookers;

import java.io.File;
import java.util.ArrayList;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.ThreadRipper;
import asWebRest.shared.WebCommon;

public class WeatherBotPython {

	public void startBot() {

		CommonBeans cb = new CommonBeans();
		ThreadRipper tr = new ThreadRipper();
		WebCommon wc = new WebCommon();

		String thisWorkingFolder = cb.getWarDeployBase();		
		String startPythonBot = "python3 bot.py &";
		
		String commandToRun = "cd '" + thisWorkingFolder + "';" +
				" cd asWxBotPy;" + 
				startPythonBot;
		
		ArrayList<Runnable> bots = new ArrayList<Runnable>();
		bots.add(() -> wc.runProcess(commandToRun));
		tr.runProcesses(bots, false, true);
		
				
	}
   
}


