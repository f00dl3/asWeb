/*
by Anthony Stump
Created: 1 Jul 2018
Ported to asWeb: 11 Feb 2019
Updated: 5 Dec 2019
 */

package asUtilsPorts.Tests;

import java.io.File;

import asUtils.Shares.JunkyBeans;
import asUtilsPorts.Cams.CamBeans;

public class TestStuff {
    
	public static void stupidTomcatSandboxing() {
		
		CamBeans camBeans = new CamBeans();
		File testFolder = new File(camBeans.getCamWebRoot() + "/TESTFOLDER");
		if(!testFolder.exists()) { try { testFolder.mkdirs(); } catch (Exception e) { e.printStackTrace(); } }
		
	}
	
    public static void main(String[] args) {
        JunkyBeans jb = new JunkyBeans();
        System.out.println(jb.getPathTomcatWebapps());
        
        System.out.println("\n\nTesting stuid Tomcat sandboxing issues.");
        stupidTomcatSandboxing();
        
    }
    
}
