/*
by Anthony Stump
Created: 6 Feb 2020
Updated: 7 Feb 2020
 */

package asUtilsPorts.Jobs.Desktop;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import asUtilsPorts.Desktop.Every1HourSub;

@DisallowConcurrentExecution

public class Desktop_SubHourly implements Job {
	
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	Every1HourSub.execJobs();
    }
    
}
