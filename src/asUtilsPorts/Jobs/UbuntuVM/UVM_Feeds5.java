/*
by Anthony Stump
Created: 6 Feb 2020
Updated: 7 Feb 2020
 */

package asUtilsPorts.Jobs.UbuntuVM;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import asUtilsPorts.UbuntuVM.Every5Minutes;

@DisallowConcurrentExecution	

public class UVM_Feeds5 implements Job {
    
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	Every5Minutes.execJobs5();
    }
        
}
