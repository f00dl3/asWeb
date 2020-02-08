/*
by Anthony Stump
Created: 6 Feb 2020
Updated: 7 Feb 2020
 */

package asUtilsPorts.Jobs;

public class CronBeans {

    private String rapid = "0 0/1 * * * ?";
    private String int2m = "0 0/2 * * * ?";
    private String int5m = "0 0/10 * * * ?";
    private String int1h = "0 8 * * * ?";
    private String subHourly = "0 38 * * * ?";

    public String get_rapid() { return rapid; }
    public String get_int2m() { return int2m; }
    public String get_int5m() { return int5m; }
    public String get_int1h() { return int1h; }
    public String get_subHourly() { return subHourly; }
        
}
