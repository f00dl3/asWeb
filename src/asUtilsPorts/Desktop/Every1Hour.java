/*
by Anthony Stump
Created: 7 Feb 2020
Updated: on creation
 */

package asUtilsPorts.Desktop;

import asUtilsPorts.Legacy.xs19;
import asUtilsPorts.Legacy.Models;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

public class Every1Hour {

    public static void execJobs() {

	DateTime tDateTime = new DateTime().minusHours(5);
	final DateTimeFormatter tDtf = DateTimeFormat.forPattern("HH");
	final String runHour = tDtf.print(tDateTime);
	String[] xs19Args = { };
	String[] modelsArgs = { runHour };
        xs19.main(xs19Args);
	Models.main(modelsArgs);
        
    }
    
    public static void main(String[] args) {
        execJobs();
    }
    
}
