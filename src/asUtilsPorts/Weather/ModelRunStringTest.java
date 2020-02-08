/*
Testing class for Model Run String
Created: 8 Feb 2020
Updated: on creation
 */

package asUtilsPorts.Weather;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import asUtilsPorts.Legacy.Models;

public class ModelRunStringTest {
    
    public static void main(String[] args) {

    	String doWhat = "Test";
    	
    	try { doWhat = args[0]; } catch (Exception e) { }
    	
    	final int hourOffset = 1;
		DateTime tDateTime = new DateTime(DateTimeZone.UTC).minusHours(hourOffset);
		final DateTimeFormatter tDtf = DateTimeFormat.forPattern("HH");
		final int runHourBF = Integer.parseInt(tDtf.print(tDateTime));
		final String runHour = String.format("%02d", runHourBF);
		System.out.println("DEBUG: runHour=" + runHour);
		
		if(doWhat.contentEquals("Run")) {
			String[] modelsArgs = { runHour };
	        Models.main(modelsArgs);
		} else {
			System.out.println("Nothing else to do!");
		}
        
    }
         
}
