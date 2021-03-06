/*
By Anthony Stump
Created: 20 Dec 2017
Updated: 7 Feb 2020
*/

package asUtilsPorts.Weather;

import java.io.File;

import asUtilsPorts.Shares.JunkyBeans;
import asWebRest.shared.CommonBeans;

public class ModelBeans {

		CommonBeans cb = new CommonBeans();
		JunkyBeans jb = new JunkyBeans();
    
        //final private File ramDrive = new File(cb.getRamPath().toString());
		final private File ramDrive = jb.getRamDrive();
        final private File diskSwap = new File(cb.getPersistTomcat()+"/wxSwap");
        final private String canadianBase = "https://dd.weather.gc.ca";
        final private String imageResStd = "2904x1440";
        final private String nomadsBase = "https://nomads.ncep.noaa.gov/pub/data/nccf/com";
        final private String wgrib2Path = "/home/astump/src/grib2/wgrib2";
       
		//final private File xml2Path = diskSwap;
        final private File xml2Path = new File(ramDrive.getPath()+"/Weather2020");
        final private double defaultDataValue = 0.001;
        final private double downloadTimeout = 15.0;
		final private File imgOutPath = new File(xml2Path.getPath()+"/tmpic");
		final private File pointDump = new File(xml2Path.getPath()+"/pointDump.txt");
        
        public double getDefaultDataValue() { return defaultDataValue; }
        public File getDiskSwap() { return diskSwap; }
        public double getDownloadTimeout() { return downloadTimeout; }
        public String getCanadianBase() { return canadianBase; }
        public String getImageResStd() { return imageResStd; }
        public String getNomadsBase() { return nomadsBase; }
        public File getXml2Path() { return xml2Path; }
        public File getImgOutPath() { return imgOutPath; }
        public File getPointDump() { return pointDump; }
        public String getWgrib2Path() { return wgrib2Path; }

    
}
