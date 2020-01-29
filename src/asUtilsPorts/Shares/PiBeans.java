/*
by Anthony Stump
Created: 22 Aug 2019
Updated: 29 Jan 2020
*/

package asUtilsPorts.Shares;

import java.io.File;

public class PiBeans {
    
    private final File piHome = new File("/home/pi");
    
    private final String apiP = "https://localhost:9090/asWeb/r/Backend";
    private final String apiSmart = "https://localhost:9090/asWeb/r/Smarthome";
    private final File smartplugPythonScript = new File("/dev/shm/asUtils/helpers/tplink-smartplug/tplink_smartplug.py");
    
    public String getApiP() { return apiP; }
    public String getApiSmart() { return apiSmart; }
    public File getPiHome() { return piHome; }
    public File getSmartplugPythonScript() { return smartplugPythonScript; }
    
}
