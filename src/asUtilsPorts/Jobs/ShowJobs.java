/*
by Anthony Stump
Created: 8 Feb 2020
Updated: on creation
 */

package asUtilsPorts.Jobs;

import java.util.Date;
import java.util.List;

import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;

public class ShowJobs {
        
    public static void main(String[] args) {
    	
    	try { 
    		
    		Scheduler scheduler = new StdSchedulerFactory().getScheduler();
    		
    		for(String groupName : scheduler.getJobGroupNames()) {
    			
    			for(JobKey jobKey : scheduler.getJobKeys(GroupMatcher.jobGroupEquals(groupName))) {    		
    				
    				String jobName = jobKey.getName();
    				String jobGroup = jobKey.getGroup();    			
    				
    				@SuppressWarnings("unchecked")
					List<Trigger> triggers = (List<Trigger>) scheduler.getTriggersOfJob(jobKey);
    				Date nextFireTime = triggers.get(0).getNextFireTime();    				
    				
    				System.out.println("[jobName] : " + jobName + " [groupName] : " + jobGroup + " - " + nextFireTime);    		
    				
    			}
    			
    		}    		
    		
    	} catch (Exception e) {
    		
    		e.printStackTrace();
    		
    	}
    	
    }    
        
}
