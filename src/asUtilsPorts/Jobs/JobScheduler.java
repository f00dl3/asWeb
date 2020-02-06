/*
by Anthony Stump
Created: 1 Jul 2018
Updated: 5 Feb 2020

http://www.quartz-scheduler.org/documentation/quartz-2.3.0/tutorials/crontrigger.html#crontrigger-tutorial

 */

package asUtilsPorts.Jobs;

import static org.quartz.DateBuilder.evenMinuteDate;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

import java.util.Date;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JobScheduler {
    
    public void runCronJobs() throws Exception {
        
        Logger log = LoggerFactory.getLogger(Scheduler.class);
        log.info("Initializing Cron Jobs");
        
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();
        log.info("Initialization complete!");
        
        JobDetail radarJob = newJob(RadarJob.class)
                .withIdentity("job1", "group1")
                .build();
        
        CronTrigger t5m = newTrigger()
                .withIdentity("trigger1", "group1")
                .withSchedule(cronSchedule("* 0/5 * * * ?"))
                .build();
        
        Date ft = sched.scheduleJob(radarJob, t5m);
        log.info(radarJob.getKey() + " has been scheduled to run at: " + ft +
                " and repeated based on expression " + t5m.getCronExpression());
        
        sched.start();
        log.info("Cron Jobs running in background!");
        
    }
    
    public void runCronTest() throws Exception {
        
        Logger log = LoggerFactory.getLogger(Scheduler.class);
        log.info("Initializing Tests");
        
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();
        log.info("Initialization tests complete!");
        
        JobDetail radarJob = newJob(HelloJob.class)
                .withIdentity("job1", "group1")
                .build();
        
        CronTrigger trigger = newTrigger()
                .withIdentity("trigger1", "group1")
                .withSchedule(cronSchedule("0/20 * * * * ?"))
                .forJob(radarJob)
                .build();
        
        Date ft = sched.scheduleJob(radarJob, trigger);
        log.info(radarJob.getKey() + " has been scheduled to run at: " + ft +
                " and repeated based on expression " + trigger.getCronExpression());
        
        log.info("Starting jobs");
        sched.start();
        
    }
        
    public void runTest() throws Exception {
        Logger log = LoggerFactory.getLogger(Scheduler.class);
        log.info("Initializing Tests");
        
        SchedulerFactory sf = new StdSchedulerFactory();
        Scheduler sched = sf.getScheduler();
        log.info("Initialization tests complete!");
        
        Date runTime = evenMinuteDate(new Date());
        log.info("Scheduling test job");
        JobDetail job = newJob(HelloJob.class).withIdentity("job1", "group1").build();
        Trigger trigger = newTrigger().withIdentity("job1", "group1").startAt(runTime).build();
        sched.scheduleJob(job, trigger);
        log.info(job.getKey() + " will run at: " + runTime);
        sched.start();
        
        log.info("Test Scheduler started, waiting 65 seconds.");
        try { Thread.sleep(65L * 1000L); } catch (Exception e) { e.printStackTrace(); };
        
        log.info("Shutting down tests");
        sched.shutdown(true);
        log.info("Tests shutdown completed!");
        
    }
    
    public static void main(String[] args) throws Exception {
        JobScheduler scheduler = new JobScheduler();
        scheduler.runCronJobs();
    }
    
}
