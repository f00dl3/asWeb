/*
by Anthony Stump
Created: 6 Feb 2020
Updated: 17 Feb 2020
 */

package asUtilsPorts.Jobs.UbuntuVM;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import asUtilsPorts.Jobs.CronBeans;

public class Crontabs_UVM {	

	public void scheduler() {
		
		CronBeans crb = new CronBeans();
		
		try {
			
			JobDetail uvmFeeds_1m = JobBuilder.newJob(UVM_Feeds1.class).withIdentity("uvmFeeds_1m", "uvmJobs").build();
			
			Trigger uvmFeeds_1m_Trigger = TriggerBuilder.newTrigger().withIdentity("cron_1m", "uvmJobs").forJob(uvmFeeds_1m)
					.withSchedule(CronScheduleBuilder.cronSchedule(crb.get_rapid())).build();
			
			Scheduler sched_1m = new StdSchedulerFactory().getScheduler();
			sched_1m.start(); sched_1m.scheduleJob(uvmFeeds_1m, uvmFeeds_1m_Trigger);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}

		try {
			JobDetail uvmFeeds_2m = JobBuilder.newJob(UVM_Feeds2.class).withIdentity("uvmFeeds_2m", "uvmJobs").build();
			
			Trigger uvmFeeds_2m_Trigger = TriggerBuilder.newTrigger().withIdentity("cron_2m", "uvmJobs").forJob(uvmFeeds_2m)
					.withSchedule(CronScheduleBuilder.cronSchedule(crb.get_int2m())).build();
			
			Scheduler sched_2m = new StdSchedulerFactory().getScheduler();
			sched_2m.start(); sched_2m.scheduleJob(uvmFeeds_2m, uvmFeeds_2m_Trigger);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}

		try {
			
			JobDetail uvmFeeds_5m = JobBuilder.newJob(UVM_Feeds5.class).withIdentity("uvmFeeds_5m", "uvmJobs").build();
			
			Trigger uvmFeeds_5m_Trigger = TriggerBuilder.newTrigger().withIdentity("cron_5m", "uvmJobs").forJob(uvmFeeds_5m)
					.withSchedule(CronScheduleBuilder.cronSchedule(crb.get_int5m())).build();
			
			Scheduler sched_5m = new StdSchedulerFactory().getScheduler();
			sched_5m.start();
			sched_5m.scheduleJob(uvmFeeds_5m, uvmFeeds_5m_Trigger);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}

		try {
			
			JobDetail uvmFeeds_1h = JobBuilder.newJob(UVM_Feeds1H.class).withIdentity("uvmFeeds_1h", "uvmJobs").build();
			
			Trigger uvmFeeds_1h_Trigger = TriggerBuilder.newTrigger().withIdentity("cron_1h", "uvmJobs").forJob(uvmFeeds_1h)
					.withSchedule(CronScheduleBuilder.cronSchedule(crb.get_int1h())).build();
			
			Scheduler sched_1h = new StdSchedulerFactory().getScheduler();
			sched_1h.start(); sched_1h.scheduleJob(uvmFeeds_1h, uvmFeeds_1h_Trigger);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
	}
	
	public static void main(String[] args) {
		Crontabs_UVM cUVM = new Crontabs_UVM();
		cUVM.scheduler();
	}
	    
}
