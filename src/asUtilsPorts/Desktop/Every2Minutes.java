/*
by Anthony Stump
Created: 7 Feb 2020
Updated: on creation
 */

package asUtilsPorts.Desktop;

import asUtilsPorts.Walk2DBv5;

public class Every2Minutes {

    public static void execJobs() {
        
	double tA = 0.97;
	final String[] w2dbArgs = { "Desktop" };
	Walk2DBv5.main(w2dbArgs);
        
    }
    
    public static void main(String[] args) {
        execJobs();
    }
    
}
