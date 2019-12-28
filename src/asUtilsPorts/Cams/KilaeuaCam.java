/*
by Anthony Stump
Created: 17 May 2018
Updated: 28 Dec 2019
*/

package asUtilsPorts.Cams;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;

import java.io.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import asUtils.Shares.MyDBConnector;

public class KilaeuaCam {

	public static void doKilaeua() {

        CommonBeans cb = new CommonBeans();
        
		final String ramDrive = cb.getRamPath().toString()+"/kilaeua";
		final String kPath = cb.getPersistTomcat().toString()+"/Get/Kilaeua";
		final File kPathObj = new File(kPath);
		
		if(!kPathObj.exists()) {
			kPathObj.mkdirs();
		}
		
		DateFormat dateFormat = new SimpleDateFormat("yyMMddHHmmss");
		Date date = new Date();
		String thisTimestamp = dateFormat.format(date);

		String volCamUrlSQL = "SELECT URL FROM Core.WebLinks WHERE Bubble='KIVC';";

		try (
			Connection conn = MyDBConnector.getMyConnection();
			Statement stmt = conn.createStatement();
			ResultSet resultSet = stmt.executeQuery(volCamUrlSQL);
		) {	
                    
			while (resultSet.next()) {
				File thisPathObject = new File(ramDrive);
				File thisDestPathObject = new File(kPath+"/Archive");
				thisPathObject.mkdirs();
				thisDestPathObject.mkdirs();
                final String camURLReturn = resultSet.getString("URL");
				final File camOutFile = new File(ramDrive+"/KICam_Latest.jpg");
				WebCommon.jsoupOutBinary(camURLReturn, camOutFile, 5.0);
				WebCommon.copyFile(camOutFile.toString(), kPath+"/KICam_Latest.jpg");
				WebCommon.moveFile(camOutFile.toString(), kPath+"/Archive/KICam_"+thisTimestamp+".jpg");
			}

		} catch (Exception e) { e.printStackTrace(); }
			
	}

}
