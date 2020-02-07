/*
by Anthony Stump
Created: 6 Feb 2020
Updated: on creation
 */

package asUtilsPorts.Jobs.Desktop;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import asUtilsPorts.Legacy.xs19;

public class Desktop_SubHourly implements Job {
	    
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	String[] args = { "Rapid" };
    	xs19.main(args);
    }
        
}
