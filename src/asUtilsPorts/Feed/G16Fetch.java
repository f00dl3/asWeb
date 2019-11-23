/*
by Anthony Stump
Created: 7 Sep 2017
Updated: 23 Nov 2019
*/

package asUtilsPorts.Feed;

/* import asUtils.Shares.JunkyBeans;
import asUtils.Shares.StumpJunk;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date; */

public class G16Fetch {

	public static void main(String[] args) {

		/* DISABLED MAY 2019 - STUB CODE FOR REUSE 

        JunkyBeans junkyBeans = new JunkyBeans();
            
		final DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
		final Date date = new Date();
		final String imgTimestamp = dateFormat.format(date);
		final String baseURL = "http://weather.cod.edu/data/satellite";
		final String g16Base = junkyBeans.getWebRoot().toString()+"/Get/G16";
                
		final File objCentPlains = new File(g16Base+"/codCentPlainsWV");
		final File objCentPlainsLatest = new File(objCentPlains.getPath()+"/Latest");
		final File objCentPlainsVis = new File(g16Base+"/codCentPlainsVI");
		final File objCentPlainsVisLatest = new File(objCentPlainsVis.getPath()+"/Latest");
		final File objNamer = new File(g16Base+"/codNamerWV");
		final File objNamerLatest = new File(objNamer.getPath()+"/Latest");

		File fileCentPlains = new File(objCentPlains.getPath()+"/"+imgTimestamp+".jpg");
		File fileCentPlainsVis = new File(objCentPlainsVis.getPath()+"/"+imgTimestamp+".jpg");
		File fileNamer = new File(objNamer.getPath()+"/"+imgTimestamp+".jpg");

		objCentPlainsLatest.mkdirs();
		objCentPlainsVisLatest.mkdirs();
                objNamer.mkdirs();
                
		Thread s1a = new Thread(() -> { StumpJunk.jsoupOutBinary(baseURL+"/regional/northcentral/current/northcentral.10.jpg", fileCentPlains, 5.0); });
		Thread s1b = new Thread(() -> { StumpJunk.jsoupOutBinary(baseURL+"/subregional/Cen_Plains/current/Cen_Plains.02.jpg", fileCentPlainsVis, 5.0); });
		Thread s1c = new Thread(() -> { StumpJunk.jsoupOutBinary(baseURL+"/global/northamerica/current/northamerica.10.jpg", fileNamer, 5.0); });
		Thread downs[] = { s1a, s1b, s1c };
		for (Thread thread : downs) { thread.start(); }
		for (int i = 0; i < downs.length; i++) { try { downs[i].join(); } catch (InterruptedException nx) { nx.printStackTrace(); } }

                // Threads causing problems 5/4/19 << FIX THIS SHIT!
		Thread s2a = new Thread(() -> { try { StumpJunk.copyFile(fileCentPlains.getPath(), objCentPlainsLatest.getPath()+"/Latest.jpg"); } catch (IOException ix) { ix.printStackTrace(); }  });
		Thread s2b = new Thread(() -> { try { StumpJunk.copyFile(fileCentPlainsVis.getPath(), objCentPlainsVisLatest.getPath()+"/Latest.jpg"); } catch (IOException ix) { ix.printStackTrace(); }  });
		Thread s2c = new Thread(() -> { try { StumpJunk.copyFile(fileNamer.getPath(), objNamerLatest.getPath()+"/Latest.jpg"); } catch (IOException ix) { ix.printStackTrace(); }  });
		Thread s2d = new Thread(() -> { StumpJunk.runProcess("convert -delay 7 -loop 0 "+objCentPlains.getPath()+"/*.jpg "+objCentPlainsLatest.getPath()+"/Loop.gif"); });
		Thread s2e = new Thread(() -> { StumpJunk.runProcess("convert -delay 7 -loop 0 "+objCentPlainsVis.getPath()+"/*.jpg "+objCentPlainsVisLatest.getPath()+"/Loop.gif"); });
		Thread s2f = new Thread(() -> { StumpJunk.runProcess("convert -delay 7 -loop 0 "+objNamer.getPath()+"/*.jpg "+objNamerLatest.getPath()+"/Loop.gif"); });
		Thread latest[] = { s2a, s2b, s2c, s2d, s2e, s2f };
		for (Thread thread : latest) { thread.start(); }
		for (int i = 0; i < latest.length; i++) { try { latest[i].join(); } catch (InterruptedException nx) { nx.printStackTrace(); } }

		*/
		
	}

}
