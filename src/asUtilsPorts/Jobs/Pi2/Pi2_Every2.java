/*
by Anthony Stump
Created: 6 Feb 2020
Updated: on creation
 */

package asUtilsPorts.Jobs.Pi2;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import asUtilsPorts.Pi2.TwoMinute;

public class Pi2_Every2 implements Job {
	    
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	String[] args = null;
    	TwoMinute.main(args);
    }
        
}
