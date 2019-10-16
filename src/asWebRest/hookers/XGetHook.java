/*
by Anthony Stump
Created: 16 Oct 2019
Updated: on Creation
 */

package asWebRest.hookers;

import java.io.File;

import asUtils.Shares.JunkyBeans;
import asUtils.Shares.StumpJunk;

public class XGetHook {
	
	public static void performXGetHook(String imageSet) throws Exception {
		
		JunkyBeans jb = new JunkyBeans();
		StumpJunk sj = new StumpJunk(); 
		
		String uHome = "/extra1/MediaServer";
		try {
			sj.runProcess("bash " + uHome + "/tmpTP/xGetDiff.sh " + imageSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
    
}
