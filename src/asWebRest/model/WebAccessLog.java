/*
by Anthony Stump
Created: 15 Feb 2018
Updated: 7 Jun 2018
 */

package asWebRest.model;

public class WebAccessLog {
    
    private String browser;
    private String loginTime;
    private String remoteIp;
    private int tomcat;
    private String user;
    
    public String getBrowser() { return browser; }
    public String getLoginTime() { return loginTime; }
    public String getRemoteIp() { return remoteIp; }
    public int getTomcat() { return tomcat; }
    public String getUser() { return user; }
    
    public void setBrowser(String browser) { this.browser = browser; }
    public void setLoginTime(String loginTime) { this.loginTime = loginTime; }
    public void setRemoteIp(String remoteIp) { this.remoteIp = remoteIp; }
    public void setTomcat(int tomcat) { this.tomcat = tomcat; }
    public void setUser(String user) { this.user = user; }
    
}
