/*
by Anthony Stump
Created: 5 Mar 2020
Updated: on creation
 */

package asUtilsPorts.Jobs.Pi3;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import asUtilsPorts.Pi3.TwoMinute;

@DisallowConcurrentExecution	  

public class Pi3_Every2 implements Job {
  
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	String[] args = null;
    	TwoMinute.main(args);
    }
        
}
