/*
by Anthony Stump
Created: 5 Mar 2020
Updated: on creation
 */

package asUtilsPorts.Jobs.Pi3;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import asUtilsPorts.Jobs.CronBeans;

public class Crontabs_Pi3 {	

	public void scheduler() {
		
		CronBeans crb = new CronBeans();

		try {
			
			JobDetail pi3_2m = JobBuilder.newJob(Pi3_Every2.class).withIdentity("pi3_2m", "pi3Jobs").build();
			
			Trigger pi3_2m_Trigger = TriggerBuilder.newTrigger().withIdentity("cron_pi3_2m", "pi3Jobs").forJob(pi3_2m)
					.withSchedule(CronScheduleBuilder.cronSchedule(crb.get_int2m())).build();
			
			Scheduler sched_2m = new StdSchedulerFactory().getScheduler();
			sched_2m.start(); sched_2m.scheduleJob(pi3_2m, pi3_2m_Trigger);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}

	}
	
	public static void main(String[] args) {
		Crontabs_Pi3 cPi3 = new Crontabs_Pi3();
		cPi3.scheduler();
	}
	    
}
