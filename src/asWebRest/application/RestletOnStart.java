/*
by Anthony Stump
Created: 3 Dec 2019
Updated: 5 Dec 2019
 */

package asWebRest.application;

import asWebRest.shared.CommonBeans;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import asUtils.Shares.StumpJunk;
import asUtilsPorts.CamController;
import asUtilsPorts.Cams.CamBeans;

public class RestletOnStart {
    
    public static void ExecuteOnStartup() {    
    	
        CommonBeans cb = new CommonBeans();
        CamBeans camBeans = new CamBeans();
        
        final File cachePath = new File(cb.getPathChartCache().toString());
        if(!cachePath.exists()) { cachePath.mkdirs(); }
        
        final Path cachePath_Path = Paths.get(cachePath.toString());
        final Path linkLocationPath = Paths.get("/var/lib/tomcat9/webapps/asWeb#cache");
        try { Files.createSymbolicLink(linkLocationPath, cachePath_Path); } catch (Exception e) { e.printStackTrace(); }
        
        //final Path persistPath_Path = Paths.get(cb.getPersistTomcat());
        //final Path persistLinkLocationPath = Paths.get("/var/lib/tomcat9/webapps/asWeb#persist");
        //try { Files.createSymbolicLink(persistLinkLocationPath, persistPath_Path); } catch (Exception e) { e.printStackTrace(); }
        
        final File camPath = camBeans.getCamPath();
		final File pushTemp = camBeans.getPushTemp();

		if (!pushTemp.exists()) {
			camPath.mkdirs();
			pushTemp.mkdirs();
		}
		
		// To enable cam capture, forward USB to VM, uncomment below line, and change variable in /web/jsBase/Cams.js from oldRoot to persist
		// unless I can fix the stupid sandboxing issue Tomcat has w/ /var/www shared VM path.
		
        //try { CamController.initCams(); } catch (Exception e) { e.printStackTrace(); }
                
    }
        
}