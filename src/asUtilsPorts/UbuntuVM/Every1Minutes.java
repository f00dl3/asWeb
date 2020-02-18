/*
by Anthony Stump
Created: 17 Feb 2020
Updated: on creation
 */

package asUtilsPorts.UbuntuVM;

public class Every1Minutes {
    
    public static void execJobs() {
        
        String feedsArgs[] = { "OneMinute" };        
        Feeds.main(feedsArgs);
        
    }
    
    public static void main(String[] args) {
        execJobs();
    }
    
}
