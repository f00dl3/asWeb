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

import asUtilsPorts.Radar;

public class RadarJob implements Job {
        
    private static Logger _log = LoggerFactory.getLogger(RadarJob.class);
    
    public RadarJob() {}
    
    public void execute(JobExecutionContext context) throws JobExecutionException {
    	Radar radar = new Radar();
        _log.info("Executed asUtils.Radar @ " + new Date());
        radar.fetchRadars();
    }
        
}
