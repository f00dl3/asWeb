/*
by Anthony Stump
Created: 1 Jul 2018
Updated: 2 Jan 2020
 */

package asUtilsPorts.Jobs;

import java.util.Date;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class XsRapidJob implements Job {
        
    private static Logger _log = LoggerFactory.getLogger(XsRapidJob.class);
    
    public XsRapidJob() {}
    
    public void execute(JobExecutionContext context) throws JobExecutionException {
        _log.info("Executed asUtils.xs7 (Rapid) @ " + new Date());
        String[] argsToPass = { "Rapid" };
        asUtils.xs19.main(argsToPass);
    }
        
}
