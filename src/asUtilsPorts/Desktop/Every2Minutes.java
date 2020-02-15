/*
by Anthony Stump
Created: 7 Feb 2020
Updated: 13 Feb 2020
 */

package asUtilsPorts.Desktop;

import asUtilsPorts.Walk2DBv5;
import asUtilsPorts.Feed.Reddit;

public class Every2Minutes {

    public static void execJobs() {
        
		double tA = 0.97;
		final String[] w2dbArgs = { "Desktop" };
		Walk2DBv5.main(w2dbArgs);
		Reddit reddit = new Reddit();
		reddit.actualClass();	        
    }
    
    public static void main(String[] args) {
        execJobs();
    }
    
}
