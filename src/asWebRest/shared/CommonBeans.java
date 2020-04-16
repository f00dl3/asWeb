/*
by Anthony Stump
Created: 22 Feb 2018
Updated: 16 Apr 2020
 */

package asWebRest.shared;

import asWebRest.hookers.WeatherBot;

public class CommonBeans {
    
	private String warDeployBase() {
        String thisWorkingFolder = WeatherBot.class.getProtectionDomain().getCodeSource().getLocation().getPath();
        thisWorkingFolder = thisWorkingFolder.replace("%23","#").replace("%23","#");
        thisWorkingFolder = thisWorkingFolder.replace("/WEB-INF/classes",  "");
        return thisWorkingFolder;        
	}
	
	private String ramPath = "/dev/shm/tomcatShare";
    
    private String catalinaHome = System.getenv("CATALINA_HOME");
    private int chartMaxHeight = 2160;
    private int chartMaxWidth = 3840;
    private String defaultNotRanYet = "Query has not ran or has failed!";
    private double elecCost = 0.14;
    private String fakeUserAgent = "Mozilla/5.0 (X11; Ubuntu; Linux x86_64; rv:72.0) Gecko/20100101 Firefox/72.0";
    private String lastPhotoWar = "2017";
    private String pathApache = "/var/www";
    private String pathMediaServer = pathApache+"/MediaServ";
    private String pathTomcat = System.getProperty("catalina.base") + "/webapps";
    private String pathChartCache = ramPath + "/cache";
    private String persistTomcat = "/media/sf_SharePoint";
    private String query_SetOrder = "SET @OrderBy = ?;";
    private String query_SetRT0 = "SET @runtot := 0;";
    private String query_SetRT120K = "SET @runtot := 120000;";
    private String tomcatUserHome = "/home/tomcat";
    
    public String getCatalinaHome() { return catalinaHome; }
    public int getChartMaxHeight() { return chartMaxHeight; }
    public int getChartMaxWidth() { return chartMaxWidth; }
    public String getDefaultNotRanYet() { return defaultNotRanYet; }
    public double getElecCost() { return elecCost; }
    public String getFakeUserAgent() { return fakeUserAgent; }
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
    public String getTomcatUserHome() { return tomcatUserHome; }
    public String getWarDeployBase() { return warDeployBase(); }
    
}
