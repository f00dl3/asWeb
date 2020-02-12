/*
by Anthony Stump
Created: 7 Feb 2020
Updated: 12 Feb 2020
 */

package asUtilsPorts.Desktop;

import asUtilsPorts.Walk2DBv5;
import asUtilsPorts.Feed.Reddit;

public class Every2Minutes {

    public static void execJobs() {
        
    	Reddit reddit = new Reddit();
		double tA = 0.97;
		final String[] w2dbArgs = { "Desktop" };
		Walk2DBv5.main(w2dbArgs);
		reddit.actualClass();	
	        
    }
    
    public static void main(String[] args) {
        execJobs();
    }
    
}
