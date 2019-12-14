/*
by Anthony Stump
Created: 1 Jul 2018
Ported to asWeb: 11 Feb 2019
Updated: 13 Dec 2019
 */

package asUtilsPorts.Tests;

import java.io.File;
import java.io.IOException;
import org.apache.commons.lang3.exception.*;

import asUtils.Shares.JunkyBeans;
import asUtils.Shares.StumpJunk;
import asUtilsPorts.Cams.CamBeans;

public class TestStuff {
    
	public static String stupidTomcatSandboxing() {
		
		String returnData = "";
		CamBeans camBeans = new CamBeans();
		JunkyBeans jb = new JunkyBeans();
		
		File camWebRoot = camBeans.getCamWebRoot();
		if(!camWebRoot.exists()) {
			try {
				System.out.println("DEBUG: Trying to create " + camWebRoot.toString());
				camWebRoot.mkdirs();
			} catch (Exception e) { e.printStackTrace(); }
		} else {
			System.out.println(camWebRoot.toString() + " exists!");
		}
		
		File newTestFile = new File("/media/sf_SharePoint/snmpwalk-Out.txt");

        	try { 
			StumpJunk.copyFile(jb.getRamDrive().toString()+"/tomcatShare/snmpwalk.txt", newTestFile.toString());
			returnData = "It was successful.";
		 } catch (Exception ix) {
			//ix.printStackTrace();
			returnData = ExceptionUtils.getStackTrace(ix);
		}
		
		return returnData;
	
	}
	
    public static void main(String[] args) {
        JunkyBeans jb = new JunkyBeans();
        System.out.println(jb.getPathTomcatWebapps());
        
        System.out.println("\n\nTesting stuid Tomcat sandboxing issues.");
        stupidTomcatSandboxing();
        
    }
    
}
