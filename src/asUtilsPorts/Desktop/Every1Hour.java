/*
by Anthony Stump
Created: 7 Feb 2020
Updated: 8 Feb 2020
 */

package asUtilsPorts.Desktop;

import asUtilsPorts.Legacy.xs19;
import asUtilsPorts.Legacy.Models;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Every1Hour {

    public static void execJobs() {

    	final int hourOffset = 0;
		DateTime tDateTime = new DateTime(DateTimeZone.UTC).minusHours(hourOffset);
		final DateTimeFormatter tDtf = DateTimeFormat.forPattern("HH");
		final int runHourBF = Integer.parseInt(tDtf.print(tDateTime));
		final String runHour = String.format("%02d", runHourBF);
		System.out.println("DEBUG: runHour=" + runHour);
		String[] xs19Args = { };
		String[] modelsArgs = { runHour };
        xs19.main(xs19Args);
        Models.main(modelsArgs);
        
    }
    
    public static void main(String[] args) {
        execJobs();
    }
    
}
