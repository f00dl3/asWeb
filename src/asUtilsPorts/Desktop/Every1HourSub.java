/*
by Anthony Stump
Created: 7 Feb 2020
Updated: 8 Feb 2020
 */

package asUtilsPorts.Desktop;

import asUtilsPorts.Mailer;
import asUtilsPorts.Legacy.xs19;

public class Every1HourSub {

    public static void execJobs() {

    	Mailer mailer = new Mailer();
    	mailer.sendQuickEmail("asWeb INFO: Sub-hourly job started");
    	
		String[] xs19Args = { "Rapid" };
	    xs19.main(xs19Args);
	            
    }
    
    public static void main(String[] args) {
        execJobs();
    }
    
}
