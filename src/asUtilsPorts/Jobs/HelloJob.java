/*
by Anthony Stump
Created: 1 Jul 2018
Updated: 29 Dec 2019
 */

package asUtilsPorts.Jobs;

import java.util.Date;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelloJob implements Job {
        
    private static Logger _log = LoggerFactory.getLogger(HelloJob.class);
    
    public HelloJob() {}
    
    public void execute(JobExecutionContext context) throws JobExecutionException {
        _log.info("Hello world! - " + new Date());
    }
        
}
