/*
by Anthony Stump
Created: 3 Dec 2019
Updated: 30 Dec 2019
 */

package asWebRest.application;

import asWebRest.hookers.WeatherBot;
import asWebRest.shared.CommonBeans;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import asUtilsPorts.CamController;
import asUtilsPorts.StartupNotify;
import asUtilsPorts.Cams.CamBeans;

public class RestletOnStart {
    
    public static void ExecuteOnStartup() {    
    	
        CommonBeans cb = new CommonBeans();
        CamBeans camBeans = new CamBeans();        
        
        StartupNotify.getAtBoot();
        
        try { WeatherBot.startBot(); } catch (Exception e) { e.printStackTrace(); }
        
        final File cachePath = new File(cb.getPathChartCache().toString());
        if(!cachePath.exists()) { cachePath.mkdirs(); }
        
        final Path cachePath_Path = Paths.get(cachePath.toString());
        final Path linkLocationPath = Paths.get("/var/lib/tomcat9/webapps/asWeb#cache");
        try { Files.createSymbolicLink(linkLocationPath, cachePath_Path); } catch (Exception e) { e.printStackTrace(); }
        
        final Path persistPath_Path = Paths.get(cb.getPersistTomcat().toString() + "/Get");
        final Path persistLinkLocationPath = Paths.get("/var/lib/tomcat9/webapps/asWeb#Get2");
        try { Files.createSymbolicLink(persistLinkLocationPath, persistPath_Path); } catch (Exception e) { e.printStackTrace(); }
        
        final File camPath = camBeans.getCamPath();
		final File pushTemp = camBeans.getPushTemp();

		if (!pushTemp.exists()) {
			camPath.mkdirs();
			pushTemp.mkdirs();
		} 
		
        try { CamController.initCams(); } catch (Exception e) { e.printStackTrace(); }
                
    }
        
}
