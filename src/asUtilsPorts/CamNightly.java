/* 
by Anthony Stump
Created: 10 Sep 2017
Updated: 16 Jan 2020
*/

package asUtilsPorts;

import asUtilsPorts.Cams.CamBeans;
import asWebRest.shared.CommonBeans;
import asWebRest.shared.GDrive;
import asWebRest.shared.ThreadRipper;
import asWebRest.shared.WebCommon;
import asUtils.Shares.JunkyBeans;
import java.io.*;
import java.sql.*;
import java.nio.file.*;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter; 

public class CamNightly {

	public void doJob(Connection dbc) {

        JunkyBeans junkyBeans = new JunkyBeans();
        CamBeans camBeans = new CamBeans();
        CommonBeans cb = new CommonBeans();
        ThreadRipper tr = new ThreadRipper();
        WebCommon wc = new WebCommon();
                
		final File helpers = junkyBeans.getHelpers();
		final DateTime dtYesterday = new DateTime().minusDays(1);
		final DateTimeFormatter dtFormat = DateTimeFormat.forPattern("yyMMdd");
		final String getYesterday = dtFormat.print(dtYesterday);
		final Path camPath = Paths.get(camBeans.getCamWebRoot().toString());
		final Path sourceFolder = Paths.get(camPath.toString()+"/Archive");
		final Path unpackFolder = Paths.get(camPath.toString()+"/mp4tmp");
		final Path cListing = Paths.get(unpackFolder.toString()+"/Listing.txt");
        final File mp4Log = new File(camPath.toString()+"/MakeMP4_GIF.log");
		final Path mp4OutFile = Paths.get(camPath.toString()+"/MP4/"+getYesterday+"JT.mp4");
 
		System.out.println("CNDEB: Deleting off Google Drive...");
        try { GDrive.deleteChildItemsFromFolder("CloudCams"); } catch (Exception ix) { ix.printStackTrace(); }
                
        System.out.println("CNDEB: Creating folder to unpack...");
		try { Files.createDirectories(unpackFolder); } catch (Exception ix) { ix.printStackTrace(); }

		System.out.println("CNDEB: Moving and sorting files...");
		wc.runProcess("mv "+sourceFolder.toString()+"/* "+unpackFolder.toString());
		wc.runProcess("bash "+helpers.getPath()+"/Sequence.sh "+unpackFolder.toString()+"/ mp4");
		List<String> camFiles = wc.fileSorter(unpackFolder, "*.mp4");
		
		System.out.println("CNDEB: Delete cListing...");
		try { Files.delete(cListing); } catch (Exception ix) { }

		System.out.println("CNDEB: Generating listing file...");
		for (String thisLoop : camFiles) {
			String fileListStr = "file '"+thisLoop+"'\n"; 
			try { wc.varToFile(fileListStr, cListing.toFile(), true); } catch (Exception fnf) { fnf.printStackTrace(); }
		}

		System.out.println("CNDEB: Starting FFMPEG...");
		wc.runProcess("(ffmpeg -threads "+tr.getMaxThreads()+" -safe 0 -f concat -i "+cListing.toString()+" -c copy "+mp4OutFile.toString()+" 2> "+mp4Log.toString()+")");

		int camImgQty = 0;

		long camMP4Size = 0;
		try { camMP4Size = (Files.size(mp4OutFile)/1024); } catch (Exception ix) { ix.printStackTrace(); }

		String camLogSQL = "INSERT INTO Core.Log_CamsMP4 (Date,ImgCount,MP4Size) VALUES (CURDATE()-1,'"+camImgQty+"',"+camMP4Size+");";

        try { wc.q2do1c(dbc, camLogSQL, null); } catch (Exception e) { e.printStackTrace(); }

        wc.runProcess("(ls "+camPath.toString()+"/MP4/*.mp4 -t | head -n 30; ls "+camPath.toString()+"/MP4/*.mp4)|sort|uniq -u|xargs rm");
        
        wc.deleteDir(unpackFolder.toFile());
        
	}

}

