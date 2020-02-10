/*
by Anthony Stump
Created: 6 Feb 2020
Updated: 9 Feb 2020
 */

package asUtilsPorts.Jobs.UbuntuVM;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import asUtilsPorts.UbuntuVM.AtBoot;
import asUtilsPorts.UbuntuVM.Feeds;

@DisallowConcurrentExecution	 

public class UVM_Feeds1H implements Job {
   
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	String args[] = { "Hour" };
    	Feeds.main(args);
    	AtBoot.pi2Tunnel();
    }

}
