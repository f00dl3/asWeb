/*
by Anthony Stump
Created: 22 Feb 2018
 */

package asWebRest.shared;

public class CommonBeans {
    
    private double elecCost = 0.14;
    private String lastPhotoWar = "2017";
    private String pathApache = "/var/www";
    private String pathMediaServer = pathApache+"/MediaServ";
    private String pathTomcat = "/var/lib/tomcat8/webapps";
    private String query_SetOrder = "SET @OrderBy = ?;";
    private String query_SetRT0 = "SET @runtot := 0;";
    private String query_SetRT120K = "SET @runtot := 120000;";
    
    public double getElecCost() { return elecCost; }
    public String getLastPhotoWar() { return lastPhotoWar; }
    public String getPathApache() { return pathApache; }
    public String getPathMediaServer() { return pathMediaServer; }
    public String getPathTomcat() { return pathTomcat; }
    public String getQSetOrder() { return query_SetOrder; }
    public String getQSetRT0() { return query_SetRT0; }
    public String getQSetRT120K() { return query_SetRT120K; }
    
}
