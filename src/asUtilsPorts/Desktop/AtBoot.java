/*
by Anthony Stump
Created on 7 Feb 2020
Updated on creation
 */

package asUtilsPorts.Desktop;

import asUtilsPorts.Jobs.Desktop.Crontabs_Desktop;
import asUtilsPorts.Shares.JunkyBeans;
import asWebRest.shared.WebCommon;

public class AtBoot {
    
	private void addedOperations() {
		
		JunkyBeans jb = new JunkyBeans();
		WebCommon wc = new WebCommon();
		
		final String ramDrive = jb.getRamDrive().toString();
		final String removeUvcLog = "rm -f /var/log/uvcdynctrl-udev.log";
		final String stopRenderd = "service renderd stop";
		final String unBootBundle = "cp " + ramDrive + "/asWeb/WEB-INF/BootBundle/* " + ramDrive;
		final String chBootBundle = "chmod +x " + ramDrive + "/*.sh";
		final String chHelpers = "chmod +x " + ramDrive + "/asWeb/WEB-INF/helpers/*";
	
		try { wc.runProcess(removeUvcLog); } catch (Exception e) { e.printStackTrace(); }
		try { wc.runProcess(stopRenderd); } catch (Exception e) { e.printStackTrace(); }
		try { wc.runProcess(unBootBundle); } catch (Exception e) { e.printStackTrace(); }
		try { wc.runProcess(chBootBundle); } catch (Exception e) { e.printStackTrace(); }
		try { wc.runProcess(chHelpers); } catch (Exception e) { e.printStackTrace(); }

	}

    private void doAtBootDesktop() {
		addedOperations();
        	Crontabs_Desktop cDt = new Crontabs_Desktop();
		cDt.scheduler();
 	}
      
    public static void main(String[] args) {
	AtBoot atBoot = new AtBoot();        
        atBoot.doAtBootDesktop();	        
    }

}
