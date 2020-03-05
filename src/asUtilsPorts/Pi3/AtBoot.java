/*
Created by Anthony Stump
Created: 5 Mar 2020
Updated: on creation
 */

package asUtilsPorts.Pi3;

import asUtilsPorts.Jobs.Pi3.Crontabs_Pi3;
import asUtilsPorts.Shares.HelperPermissions;

public class AtBoot {
    
    private static void doAtBoot() {       
        
    	Crontabs_Pi3 cPi3 = new Crontabs_Pi3();
    	HelperPermissions hp = new HelperPermissions();
    	
    	hp.helperChmods();
    	
        Thread ta = new Thread(() -> { cPi3.scheduler(); });
        Thread procs[] = { ta };
        for (Thread thread : procs) { thread.start(); }
        
    }
    
    public static void main(String[] args) {
        
        System.out.println("Raspberry Pi 3 @Boot");
        doAtBoot();
        
    }
    
}
