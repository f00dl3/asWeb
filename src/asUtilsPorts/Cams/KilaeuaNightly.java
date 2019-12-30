/*
by Anthony Stump
Created: 17 May 2018
Updated: 30 Dec 2019
*/

package asUtilsPorts.Cams;

import asUtils.Shares.JunkyBeans;
import asWebRest.shared.CommonBeans;
import asWebRest.shared.ThreadRipper;
import asWebRest.shared.WebCommon;

import java.nio.file.*;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class KilaeuaNightly {

	public static void main(String[] args) {
		
		CommonBeans cb = new CommonBeans();
        JunkyBeans junkyBeans = new JunkyBeans();
        ThreadRipper tr = new ThreadRipper();
        WebCommon wc = new WebCommon();
        
		final DateTime dtYesterday = new DateTime().minusDays(1);
		final DateTimeFormatter dtFormat = DateTimeFormat.forPattern("yyMMdd");
		final String dateStamp = dtFormat.print(dtYesterday);
		final String kilaeuaBase = cb.getPersistTomcat()+"/Get/Kilaeua";

		System.out.println(dateStamp);

                final Path kMP4Arch = Paths.get(kilaeuaBase+"/DailyMP4s");
                final Path tMemTmp = Paths.get(cb.getRamPath()+"/kMP4tmp");
                final Path tArchDay = Paths.get(kilaeuaBase+"/Archive/");
                
                try {
                    wc.deleteDir(tMemTmp.toFile());
                    Files.createDirectories(kMP4Arch);
                    Files.createDirectories(tMemTmp);
                    Files.createDirectories(tArchDay);
                    
                    wc.runProcess("mv "+tArchDay+"/*.jpg "+tMemTmp);
                    wc.runProcess("bash "+junkyBeans.getHelpers().toString()+"/Sequence.sh "+tMemTmp+"/ jpg");

                    System.out.println("Creating MP4 file...");
                    wc.runProcess("ffmpeg -threads "+tr.getMaxThreads()+" -framerate 96 -i "+tMemTmp+"/%05d.jpg -vf \"scale=trunc(iw/2)*2:trunc(ih/2)*2\" -vcodec libx264 -pix_fmt yuv420p "+kMP4Arch.toString()+"/"+dateStamp+".mp4");
                    System.out.println("Volcam Archive MP4 completed!");

		} catch (Exception e) { e.printStackTrace(); }
	}

}
