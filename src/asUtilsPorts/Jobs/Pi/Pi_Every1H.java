/*
by Anthony Stump
Created: 9 Feb 2020
Updated: on creation
 */

package asUtilsPorts.Jobs.Pi;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import asUtilsPorts.Pi.AtBoot;


@DisallowConcurrentExecution

public class Pi_Every1H implements Job {
	    
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	AtBoot.pi2DesktopTunnel();
    }
        
}
