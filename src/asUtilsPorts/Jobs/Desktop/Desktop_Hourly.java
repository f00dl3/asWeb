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

import asUtilsPorts.Desktop.Every1Hour;

@DisallowConcurrentExecution

public class Desktop_Hourly implements Job {	
	
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	Every1Hour.execJobs();
    }        
    
}
