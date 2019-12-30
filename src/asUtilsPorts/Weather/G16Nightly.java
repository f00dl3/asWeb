/*
by Anthony Stump
Created: 7 SEP 2017
Updated: 30 Dec 2019
*/

package asUtilsPorts.Weather;

import asUtils.Shares.JunkyBeans;
import asWebRest.shared.CommonBeans;
import asWebRest.shared.MyDBConnector;
import asWebRest.shared.WebCommon;

import java.sql.*;
import java.nio.file.*;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;


public class G16Nightly {

	public static void main(String[] args) {

		CommonBeans cb = new CommonBeans();
		MyDBConnector mdb = new MyDBConnector();		
        JunkyBeans junkyBeans = new JunkyBeans();
        WebCommon wc = new WebCommon();
        
		final DateTime dtYesterday = new DateTime().minusDays(1);
		final DateTimeFormatter dtFormat = DateTimeFormat.forPattern("yyMMdd");
		final String dateStamp = dtFormat.print(dtYesterday);
		final String g16Base = cb.getPersistTomcat()+"/Get/G16";

		System.out.println(dateStamp);

		String getRadarSetSQL = "SELECT Sector FROM WxObs.GOES16SectorsRDO WHERE Active=1 ORDER BY Sector ASC;";

		try (
			Connection conn = mdb.getMyConnection();
			Statement stmt = conn.createStatement();
			ResultSet resultSet = stmt.executeQuery(getRadarSetSQL);
		) {		
			while (resultSet.next()) {

				String thisSector = resultSet.getString("Sector");
				Path tG16Arch = Paths.get(g16Base+"/"+thisSector+"/Archive");
				Path tMemTmpA = Paths.get(junkyBeans.getRamDrive().toString()+"/G16MP4/Archive/"+thisSector);
				Path tArchDay = Paths.get(g16Base+"/ArchiveDay/"+thisSector);
				Path tListing = Paths.get(tMemTmpA+"/Listing.txt");

				wc.deleteDir(tMemTmpA.toFile());
				Files.createDirectories(tMemTmpA);
				Files.createDirectories(tArchDay);
				if (Files.exists(tListing)) { Files.delete(tListing); }

				wc.runProcess("mv "+tG16Arch+"/*.mp4 "+tMemTmpA);
				wc.runProcess("bash "+junkyBeans.getHelpers().toString()+"/Sequence.sh "+tMemTmpA+"/ mp4");

				String fileListingString = "";	

				List<String> aOfFiles = wc.fileSorter(tMemTmpA, "*.mp4");

				for (String thisFileStr : aOfFiles) {
					fileListingString += "file '"+thisFileStr+"'\n";
				}

				wc.varToFile(fileListingString, tListing.toFile(), false);

				System.out.println("Creating MP4 file...");
				wc.runProcess("ffmpeg -threads 8 -safe 0 -f concat -i "+tListing.toString()+" -c copy "+tArchDay.toString()+"/"+dateStamp+"A.mp4");
				wc.runProcess("(ls "+tArchDay.toString()+"/*.mp4 -t | head -n 31; ls "+tArchDay.toString()+"/*.mp4)|sort|uniq -u|xargs rm");
				//WebCommon.runProcess("chown -R "+junkyBeans.getWebUser()+" "+tArchDay.toString());
				System.out.println(thisSector+" completed!");

			}

		} catch (Exception e) { e.printStackTrace(); }
                
        //String kilaeuaNArgs[] = { "Empty" }; try { KilaeuaNightly.main(kilaeuaNArgs); } catch (Exception e) { e.printStackTrace(); }
                
	}

}
