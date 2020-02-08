/*
by Anthony Stump
Created: 7 Feb 2020
Updated: on creation
 */

package asUtilsPorts.Jobs.Desktop;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import asUtilsPorts.Desktop.Every2Minutes;

@DisallowConcurrentExecution	    

public class Desktop_Every2 implements Job {
	
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	Every2Minutes.execJobs();
    }        
    
}
