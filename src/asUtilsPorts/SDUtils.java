/*
by Anthony Stump
Created: 14 Aug 2017
Updated: 28 Mar 2020
*/

package asUtilsPorts;

import java.io.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import asUtilsPorts.Shares.JunkyBeans;
import asUtilsPorts.Shares.SDUtilsVars;
import asWebRest.secure.JunkyPrivate;
import asWebRest.shared.MyDBConnector;
import asWebRest.shared.WebCommon;

public class SDUtils {

	public static void main (String[] args) {

		MyDBConnector mdb = new MyDBConnector();
		SDUtilsVars sduVars = new SDUtilsVars();
        JunkyBeans junkyBeans = new JunkyBeans();
        JunkyPrivate junkyPrivate = new JunkyPrivate();
        WebCommon wc = new WebCommon();
                
		sduVars.setBuild("SD Utils Java - Build 366");
		sduVars.setUpdated("28 MAR 2020 @ 12:10 CT");
        File usbDrivePath = junkyBeans.getSdCardPath();
        final String userHome = junkyBeans.getUserHome().toString();
        final String uName = junkyBeans.getUName();
                
		DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmm");
		Date date = new Date();
		String thisTimestamp = dateFormat.format(date);
		System.out.println(sduVars.getBuild()+"\n"+sduVars.getUpdated()+"\n"+usbDrivePath.toString()+"\nRun time: "+thisTimestamp+"\nby Anthony Stump\n\n");

		System.out.println("Requesting privilages and setting path...");
		wc.runProcess("sudo echo Done");

		System.setProperty("user.dir", usbDrivePath.toString());

		System.out.println("Cleaning crap off this MicroSD card...");
		
		String[] pathsToDelete = {
			"/.mmsyscache/",
			"/.the.pdfviewer3/",
			"/.Trash-1000/",
			"/albumthumbs/",
			"/Android/data/",
			"/AppGame/",
			"/DCIM/.thumbnails/",
			"/LOST.DIR/",
			"/ppy_cross/",
			"/tmp/" };
		
		for (String thisPathString : pathsToDelete) {
			File thisFolder = new File(usbDrivePath.toString()+"/"+thisPathString);
			wc.deleteDir(thisFolder);
		}

		String[] filesToDelete = { ".bugsense", "tapcontext" };
		for (String thisFileString : filesToDelete) {
			File thisFile = new File(usbDrivePath.toString()+"/"+thisFileString);
			thisFile.delete();
		}
		
        System.out.println("Creating encrypted backup of MySQL critical databases...");
		System.setProperty("user.dir", usbDrivePath.toString()+"/");
		new File(sduVars.getCachePath()+"/SQLDumps").mkdirs();
		
		String[] sqlTasks = { "Core", "Feeds", "WebCal" };
		for (String task : sqlTasks) {
			wc.runProcess("sudo -i mysqldump -h " + junkyPrivate.getAss() + " " +task+" --result-file="+sduVars.getCachePath()+"/SQLDumps/"+task+".sql");
		}

		wc.runProcess("tar -zcvf \""+usbDrivePath.toString()+"/[data]/Tools/SQL/Backup/"+thisTimestamp+"-Struct.tar.gz\" "+sduVars.getCachePath()+"/SQLDumps/*");
		File sqlCacheFolder = new File(sduVars.getCachePath()+"/SQLDumps");
		wc.deleteDir(sqlCacheFolder);		

		System.out.println("Backing up CODEX data...");
        File codexOutZip = new File(usbDrivePath.toString()+"/[data]/Tools/dev/codex.zip");
        File codexPath = new File(sduVars.getCodexPath());
        try { File asWebBuild = new File(codexPath.toString()+"/Java/asWeb/build"); wc.deleteDir(asWebBuild); } catch (Exception e) { e.printStackTrace(); }
        try { File asWebDist = new File(codexPath.toString()+"/Java/asWeb/dist"); wc.deleteDir(asWebDist); } catch (Exception e) { e.printStackTrace(); }
		wc.zipThisFolder(codexPath, codexOutZip);

		System.out.println("Backing up all SD Card data...");
        File thisZip = new File(sduVars.getUsbBackupPath()+"/"+thisTimestamp+".zip");
        wc.zipThisFolder(usbDrivePath, thisZip);
		wc.runProcess("(ls "+userHome+"/USB-Back/* -t | head -n 4; ls "+userHome+"/USB-Back/*)|sort|uniq -u|xargs rm");

		System.out.println("Writing log into database...");
		String usbBackSizeKB = Long.toString(new File(userHome+"/USB-Back/"+thisTimestamp+".zip").length()/1024);
		String updateQuery = "INSERT INTO Core.Log_SDUtils (Date,Time,Notes,ZIPSize) VALUES (CURDATE(),CURTIME(),'Ran "+sduVars.getBuild()+" Modified "+sduVars.getUpdated()+"',"+usbBackSizeKB+");";		
		try ( Connection conn = mdb.getMyConnection(); Statement stmt = conn.createStatement(); ) { stmt.executeUpdate(updateQuery); } catch (Exception e) { e.printStackTrace(); }
		
	}

}
