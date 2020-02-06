/*
by Anthony Stump
Created: 3 Dec 2019
Updated: 6 Feb 2020
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
import asUtilsPorts.Jobs.Crontabs_UVM;

public class RestletOnStart {
    
    public static void ExecuteOnStartup() {    

        CamBeans camBeans = new CamBeans();
        CommonBeans cb = new CommonBeans();
        Crontabs_UVM cUVM = new Crontabs_UVM();
        WeatherBot wxb = new WeatherBot();
        
        StartupNotify.getAtBoot();
        
        try { wxb.startBot(); } catch (Exception e) { e.printStackTrace(); }
        
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
        try { cUVM.scheduler(); } catch (Exception e) { e.printStackTrace(); }
                
    }
        
}
