/*
by Anthony Stump
Created: 6 Feb 2020
Updated: on creation
 */

package asUtilsPorts.Jobs.UbuntuVM;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import asUtilsPorts.UbuntuVM.Feeds;

public class UVM_Feeds1H implements Job {
	    
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	String args[] = { "Hour" };
    	Feeds.main(args);
    }
        
}
