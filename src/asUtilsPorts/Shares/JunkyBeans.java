/*
By Anthony Stump
Created: 19 Dec 2017
Updated: 31 Dec 2020
 */

package asUtilsPorts.Shares;

import java.io.File;
import java.nio.file.*;

public class JunkyBeans {
    
        private final String api = "https://localhost:8444/asWeb/r/Backend";
	private final String apiByIp = "https://127.0.0.1:8444/asWeb/r/Backend";
        private final String applicationName = "asUtils";
        private final int applicationMajorVersion = 4;
        private final File appShareSys = new File("/usr/local/bin");
        private final File appSys = new File("/usr/bin");
        private final String bicycle = "A16";
        private final int downloadTimeout = 10;
        private final String gmailSmtpServer = "smtp.gmail.com";
        private final File mySqlShare = new File("/var/lib/mysql-files");
		private final File ramDrive = new File("/dev/shm");
		private final File helpers = new File(ramDrive.getPath()+"/asUtils/helpers");
        private final String pathTomcatWebapps = System.getProperty("catalina.base") + "/webapps";
        private final String pathWebappCache = pathTomcatWebapps + "/asWeb#cache";
        private final File sdCardPath = new File("/media/astump/PHONE");
        private final String spcFeedBase = "http://www.spc.noaa.gov/products/";
        private final File sshKeyFolder = new File("/root/.ssh");
        private final File sshKeyFolderPi = new File("/home/pi/.ssh");
        private final File sshKeyFolderUser = new File("/home/astump/.ssh");
        private final File tomcatWebapps = new File("/var/lib/tomcat8/webapps");
        private final String uName = "astump";
        private final File userHome = new File("/home/astump");
        private final File vdiPath = new File("/extra1/VDI/");
        private final File webRoot = new File("/var/www");
        private final File webRoot2 = new File("/home/astump/SharePoint");
        private final String webUser = "www-data";
        
        private final File codeBase = new File(userHome.toString()+"/src/codex/Java/asUtils/");
        private final Path desktopPath = Paths.get(userHome+"/Desktop");
        private final File dbBackupPath = new File(userHome+"/Scripts/dbBackup");
        private final File dbBackupPath2 = new File("/extra1/dbBackup");
        private final File mediaServerRoot = new File(webRoot.toString()+"/MediaServ");
        
        public String getApi() { return api; }
	public String getApiByIp() { return apiByIp; }
        public String getApplicationName() { return applicationName; }
        public int getApplicationMajorVersion() { return applicationMajorVersion; }
        public File getAppShareSys() { return appShareSys; }
        public File getAppSys() { return appSys; }
        public String getBicycle() { return bicycle; }
        public File getDbBackupPath() { return dbBackupPath; }
        public File getDbBackupPath2() { return dbBackupPath2; }
        public File getCodeBase() { return codeBase; }
        public Path getDesktopPath() { return desktopPath; }
        public int getDownloadTimeout() { return downloadTimeout; }
        public String getGmailSmtpServer() { return gmailSmtpServer; }
        public File getHelpers() { return helpers; }
        public File getMediaServerRoot() { return mediaServerRoot; }
        public File getMySqlShare() { return mySqlShare; }
        public String getPathTomcatWebapps() { return pathTomcatWebapps; }
        public String getPathWebappCache() { return pathWebappCache; }
        public File getRamDrive() { return ramDrive; }
        public File getSdCardPath() { return sdCardPath; }
        public String getSpcFeedBase() { return spcFeedBase; }
        public File getSshKeyFolder() { return sshKeyFolder; }
		public File getSshKeyFolderPi() { return sshKeyFolderPi; }
        public File getSshKeyFolderUser() { return sshKeyFolderUser; }
        public File getTomcatWebapps() { return tomcatWebapps; }
        public String getUName() { return uName; }
        public File getUserHome() { return userHome; }
        public File getVdiPath() { return vdiPath; }
        public File getWebRoot() { return webRoot; }
        public File getWebRoot2() { return webRoot2; }
        public String getWebUser() { return webUser; }
        
}
