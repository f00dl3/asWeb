/*
by Anthony Stump
Created: 30 Sep 2017
Updated: 10 Feb 2019
*/

package asUtilsPorts;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import asUtilsPorts.GPSParse;
import asUtils.Shares.JunkyBeans;

public class GPSBulk {

	public static void main(String[] args) {

                JunkyBeans junkyBeans = new JunkyBeans();
		final String archFlag = "no";
		final File dropLocation = new File(junkyBeans.getDesktopPath().toString());
		final File[] dirList = dropLocation.listFiles();

		String thisTrace = null;

		if (dirList != null) {
			for (File child : dirList) {
				String childPath = child.getPath();
				if(childPath.contains(".csv")) {
					Pattern p = Pattern.compile("Desktop/(.*).csv");
					Matcher m = p.matcher(childPath);
					if (m.find()) {
						thisTrace = m.group(1);
						System.out.println(" --> Processing: "+thisTrace);
						String gpsProcArgs[] = { thisTrace, archFlag, "csv" };
						GPSParse.main(gpsProcArgs);
					}
				}
				if(childPath.contains(".json")) {
					Pattern p = Pattern.compile("Desktop/(.*).json");
					Matcher m = p.matcher(childPath);
					if (m.find()) {
						thisTrace = m.group(1);
						System.out.println(" --> Processing: "+thisTrace);
						String gpsProcArgs[] = { thisTrace, archFlag, "json" };
						GPSParse.main(gpsProcArgs);
					}
				}
				if(childPath.contains(".fit")) {
					Pattern p = Pattern.compile("Desktop/(.*).fit");
					Matcher m = p.matcher(childPath);
					if (m.find()) {
						thisTrace = m.group(1);
						System.out.println(" --> Processing: "+thisTrace);
						String gpsProcArgs[] = { thisTrace, archFlag, "fit" };
						GPSParse.main(gpsProcArgs);
					}
				}
				if(childPath.contains(".gpx")) {
					Pattern p = Pattern.compile("Desktop/(.*).gpx");
					Matcher m = p.matcher(childPath);
					if (m.find()) {
						thisTrace = m.group(1);
						System.out.println(" --> Processing: "+thisTrace);
						String gpsProcArgs[] = { thisTrace, archFlag, "gpx" };
						GPSParse.main(gpsProcArgs);
					}
				}
			}
		}

	}

}
