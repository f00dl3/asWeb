/*
by Anthony Stump
Created on 7 Feb 2020
Updated : 8 Feb 2020
 */

package asUtilsPorts.Desktop;

import asUtilsPorts.Jobs.Desktop.Crontabs_Desktop;
import asUtilsPorts.Shares.HelperPermissions;
import asUtilsPorts.Shares.JunkyBeans;
import asWebRest.shared.WebCommon;

public class AtBoot {
    
	private void addedOperations() {
		
		JunkyBeans jb = new JunkyBeans();
		WebCommon wc = new WebCommon();
		
		final String removeUvcLog = "rm -f /var/log/uvcdynctrl-udev.log";
		final String stopRenderd = "service renderd stop";
	
		try { wc.runProcess(removeUvcLog); } catch (Exception e) { e.printStackTrace(); }
		try { wc.runProcess(stopRenderd); } catch (Exception e) { e.printStackTrace(); }

	}

    private void doAtBootDesktop() {
    	
    	Crontabs_Desktop cDt = new Crontabs_Desktop();
		HelperPermissions hp = new HelperPermissions();
		
    	addedOperations();
    	hp.helperChmods();
    	cDt.scheduler();
		
 	}
      
    public static void main(String[] args) {
	AtBoot atBoot = new AtBoot();        
        atBoot.doAtBootDesktop();	        
    }

}
