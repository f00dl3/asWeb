/*
by Anthony Stump
Created: 22 Feb 2018
Updated: 13 Dec 2019
 */

package asWebRest.shared;

public class CommonBeans {
    
	private String ramPath = "/dev/shm/tomcatShare";
    
    private String catalinaHome = System.getenv("CATALINA_HOME");
    private int chartMaxHeight = 1080;
    private int chartMaxWidth = 1920;
    private String defaultNotRanYet = "Query has not ran or has failed!";
    private double elecCost = 0.14;
    private String lastPhotoWar = "2017";
    private String pathApache = "/var/www";
    private String pathMediaServer = pathApache+"/MediaServ";
    private String pathTomcat = System.getProperty("catalina.base") + "/webapps";
    private String pathChartCache = ramPath + "/cache";
    private String persistTomcat = "/media/sf_SharePoint";
    private String query_SetOrder = "SET @OrderBy = ?;";
    private String query_SetRT0 = "SET @runtot := 0;";
    private String query_SetRT120K = "SET @runtot := 120000;";
    
    public String getCatalinaHome() { return catalinaHome; }
    public int getChartMaxHeight() { return chartMaxHeight; }
    public int getChartMaxWidth() { return chartMaxWidth; }
    public String getDefaultNotRanYet() { return defaultNotRanYet; }
    public double getElecCost() { return elecCost; }
    public String getLastPhotoWar() { return lastPhotoWar; }
    public String getPathApache() { return pathApache; }
    public String getPathChartCache() { return pathChartCache; }
    public String getPathMediaServer() { return pathMediaServer; }
    public String getPathTomcat() { return pathTomcat; }
    public String getPersistTomcat() { return persistTomcat; }
    public String getQSetOrder() { return query_SetOrder; }
    public String getQSetRT0() { return query_SetRT0; }
    public String getQSetRT120K() { return query_SetRT120K; }
    public String getRamPath() { return ramPath; }
    
}
