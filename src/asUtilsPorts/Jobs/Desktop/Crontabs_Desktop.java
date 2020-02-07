/*
by Anthony Stump
Created: 6 Feb 2020
Updated: on creation
 */

package asUtilsPorts.Jobs.Desktop;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import asUtilsPorts.Jobs.CronBeans;

public class Crontabs_Desktop {	

	public void scheduler() {
		
		CronBeans crb = new CronBeans();

		try {
			
			JobDetail dt_sh = JobBuilder.newJob(Desktop_SubHourly.class)
					.withIdentity("dt_sh", "dtJobs").build();
			
			Trigger dt_sh_Trigger = TriggerBuilder.newTrigger()
					.withIdentity("cron_dt_sh", "dtJobs")
					.forJob(dt_sh)
					.withSchedule(CronScheduleBuilder.cronSchedule(crb.get_subHourly()))
					.build();
			
			Scheduler sched_sh = new StdSchedulerFactory().getScheduler();
			sched_sh.start();
			sched_sh.scheduleJob(dt_sh, dt_sh_Trigger);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
	}
	
	public static void main(String[] args) {
		Crontabs_Desktop cDt = new Crontabs_Desktop();
		cDt.scheduler();
	}
	    
}
