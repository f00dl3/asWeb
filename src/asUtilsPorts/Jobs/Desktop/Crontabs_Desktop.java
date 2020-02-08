/*
by Anthony Stump
Created: 6 Feb 2020
Updated: 8 Feb 2020
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
			
			JobDetail dt_2m = JobBuilder.newJob(Desktop_Every2.class).withIdentity("dt_sh", "dtJobs").build();
			
			Trigger dt_2m_Trigger = TriggerBuilder.newTrigger().withIdentity("cron_dt_2m", "dtJobs").forJob(dt_2m)
					.withSchedule(CronScheduleBuilder.cronSchedule(crb.get_int2m())).build();
			
			Scheduler sched_2m = new StdSchedulerFactory().getScheduler();
			sched_2m.start();
			sched_2m.scheduleJob(dt_2m, dt_2m_Trigger);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}

		try {
			
			JobDetail dt_sh = JobBuilder.newJob(Desktop_SubHourly.class).withIdentity("dt_sh", "dtJobs").build();
			
			Trigger dt_sh_Trigger = TriggerBuilder.newTrigger().withIdentity("cron_dt_sh", "dtJobs").forJob(dt_sh)
					.withSchedule(CronScheduleBuilder.cronSchedule(crb.get_subHourly())).build();
			
			Scheduler sched_sh = new StdSchedulerFactory().getScheduler();
			sched_sh.start();
			sched_sh.scheduleJob(dt_sh, dt_sh_Trigger);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}

		try {
			JobDetail dt_1h = JobBuilder.newJob(Desktop_Hourly.class).withIdentity("dt_1h", "dtJobs").build();
			
			Trigger dt_1h_Trigger = TriggerBuilder.newTrigger().withIdentity("cron_dt_1h", "dtJobs").forJob(dt_1h)
					.withSchedule(CronScheduleBuilder.cronSchedule(crb.get_int1h())).build();
			
			Scheduler sched_1h = new StdSchedulerFactory().getScheduler();
			sched_1h.start();
			sched_1h.scheduleJob(dt_1h, dt_1h_Trigger);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
	}
	
	public static void main(String[] args) {
		Crontabs_Desktop cDt = new Crontabs_Desktop();
		cDt.scheduler();
	}
	    
}
