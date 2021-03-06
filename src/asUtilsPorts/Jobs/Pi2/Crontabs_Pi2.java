/*
by Anthony Stump
Created: 6 Feb 2020
Updated: 9 Feb 2020
 */

package asUtilsPorts.Jobs.Pi2;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import asUtilsPorts.Jobs.CronBeans;

public class Crontabs_Pi2 {	

	public void scheduler() {
		
		CronBeans crb = new CronBeans();

		try {
			
			JobDetail pi2_2m = JobBuilder.newJob(Pi2_Every2.class).withIdentity("pi2_2m", "pi2Jobs").build();
			
			Trigger pi2_2m_Trigger = TriggerBuilder.newTrigger().withIdentity("cron_pi2_2m", "pi2Jobs").forJob(pi2_2m)
					.withSchedule(CronScheduleBuilder.cronSchedule(crb.get_int2m())).build();
			
			Scheduler sched_2m = new StdSchedulerFactory().getScheduler();
			sched_2m.start(); sched_2m.scheduleJob(pi2_2m, pi2_2m_Trigger);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}

		try {
			
			JobDetail pi2_1h = JobBuilder.newJob(Pi2_Every1H.class).withIdentity("pi2_1h", "pi2Jobs").build();
			
			Trigger pi2_1h_Trigger = TriggerBuilder.newTrigger().withIdentity("cron_pi2_1h", "pi2Jobs").forJob(pi2_1h)
					.withSchedule(CronScheduleBuilder.cronSchedule(crb.get_int1h())).build();
			
			Scheduler sched_1h = new StdSchedulerFactory().getScheduler();
			sched_1h.start(); sched_1h.scheduleJob(pi2_1h, pi2_1h_Trigger);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
	}
	
	public static void main(String[] args) {
		Crontabs_Pi2 cPi2 = new Crontabs_Pi2();
		cPi2.scheduler();
	}
	    
}
