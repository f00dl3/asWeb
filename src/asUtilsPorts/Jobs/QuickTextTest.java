/*
by Anthony Stump
Created: 6 Feb 2020
Updated: on creation
 */

package asUtilsPorts.Jobs;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import asUtilsPorts.Mailer;

public class QuickTextTest implements Job {
	    
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	Mailer mailer = new Mailer();
    	mailer.sendQuickText("IT WORKED!");
    }
        
}
