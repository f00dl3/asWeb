/*
by Anthony Stump
Created: 8 Feb 2020
Updated: on creation
*/

package asUtilsPorts.Shares;

import asWebRest.shared.WebCommon;

public class HelperPermissions {

	public void helperChmods() {

		JunkyBeans jb = new JunkyBeans();
		WebCommon wc = new WebCommon();
		
		final String ramDrive = jb.getRamDrive().toString();
		final String unBootBundle = "cp " + ramDrive + "/asWeb/WEB-INF/BootBundle/* " + ramDrive;
		final String chBootBundle = "chmod +x " + ramDrive + "/*.sh";
		final String chHelpers = "chmod +x " + ramDrive + "/asWeb/WEB-INF/helpers/*";
		
		try { wc.runProcess(unBootBundle); } catch (Exception e) { e.printStackTrace(); }
		try { wc.runProcess(chBootBundle); } catch (Exception e) { e.printStackTrace(); }
		try { wc.runProcess(chHelpers); } catch (Exception e) { e.printStackTrace(); }
		
	}
	
	public static void main(String[] args) {
		
		HelperPermissions hp = new HelperPermissions();
		hp.helperChmods();

	}

}
