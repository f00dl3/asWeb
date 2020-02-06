/*
by Anthony Stump
Created: 5 May 2019
Updated: 31 Jan 2020
 */

package asUtilsPorts.UbuntuVM;

public class Every2Minutes {
    
    public static void execJobs() {
        
        String feedsArgs[] = { "TwoMinute" };        
        Feeds.main(feedsArgs);
        
    }
    
    public static void main(String[] args) {
        execJobs();
    }
    
}
