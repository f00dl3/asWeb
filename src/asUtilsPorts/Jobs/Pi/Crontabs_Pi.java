/*
by Anthony Stump
Created: 6 Feb 2020
Updated: on creation
 */

package asUtilsPorts.Jobs.Pi;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

import asUtilsPorts.Jobs.CronBeans;
import asUtilsPorts.Jobs.Pi2.Pi2_Every2;

public class Crontabs_Pi {	

	public void scheduler() {
		
		CronBeans crb = new CronBeans();

		try {
			
			JobDetail pi_2m = JobBuilder.newJob(Pi2_Every2.class)
					.withIdentity("pi_2m", "piJobs").build();
			
			Trigger pi_2m_Trigger = TriggerBuilder.newTrigger()
					.withIdentity("cron_pi_2m", "piJobs")
					.forJob(pi_2m)
					.withSchedule(CronScheduleBuilder.cronSchedule(crb.get_int2m()))
					.build();
			
			Scheduler sched_2m = new StdSchedulerFactory().getScheduler();
			sched_2m.start();
			sched_2m.scheduleJob(pi_2m, pi_2m_Trigger);
			
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		
	}
	
	public static void main(String[] args) {
		Crontabs_Pi cPi = new Crontabs_Pi();
		cPi.scheduler();
	}
	    
}
