/*
by Anthony Stump
Created: 30 Sep 2017
Ported to asWeb: 10 Feb 2019
Updated: 24 Apr 2021
*/

package asUtilsPorts;

import java.io.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import asUtilsPorts.GPSParse;
import asWebRest.shared.CommonBeans;

public class GPSBulk {

	public static void main(String[] args) {

		CommonBeans cb = new CommonBeans();
		final String archFlag = "no";
		final File dropLocation = new File(cb.getPathChartCache().toString());
		final File[] dirList = dropLocation.listFiles();

		String thisTrace = null;

		if (dirList != null) {
			for (File child : dirList) {
				String childPath = child.getPath();
				/* if(childPath.contains(".csv")) {
					Pattern p = Pattern.compile(dropLocation+"/(.*).csv");
					Matcher m = p.matcher(childPath);
					if (m.find()) {
						thisTrace = m.group(1);
						System.out.println(" --> Processing: "+thisTrace);
						String gpsProcArgs[] = { thisTrace, archFlag, "csv" };
						GPSParse.main(gpsProcArgs);
					}
				}
				if(childPath.contains(".json")) {
					Pattern p = Pattern.compile(dropLocation+"/(.*).json");
					Matcher m = p.matcher(childPath);
					if (m.find()) {
						thisTrace = m.group(1);
						System.out.println(" --> Processing: "+thisTrace);
						String gpsProcArgs[] = { thisTrace, archFlag, "json" };
						GPSParse.main(gpsProcArgs);
					}
				} */
				if(childPath.contains(".fit")) {
					Pattern p = Pattern.compile(dropLocation+"/(.*).fit");
					Matcher m = p.matcher(childPath);
					if (m.find()) {
						try { Thread.sleep(10*1000); } catch (Exception e) { }
						thisTrace = m.group(1);
						System.out.println(" --> Processing: "+thisTrace);
						String gpsProcArgs[] = { thisTrace, archFlag, "fit" };
						GPSParse.main(gpsProcArgs);
					}
				}
				/* if(childPath.contains(".gpx")) {
					Pattern p = Pattern.compile(dropLocation+"/(.*).gpx");
					Matcher m = p.matcher(childPath);
					if (m.find()) {
						thisTrace = m.group(1);
						System.out.println(" --> Processing: "+thisTrace);
						String gpsProcArgs[] = { thisTrace, archFlag, "gpx" };
						GPSParse.main(gpsProcArgs);
					}
				} */
			}
		}

	}

}
