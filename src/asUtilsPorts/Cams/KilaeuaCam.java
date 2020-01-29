/*
by Anthony Stump
Created: 17 May 2018
Updated: 29 Jan 2020
*/

package asUtilsPorts.Cams;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.MyDBConnector;
import asWebRest.shared.WebCommon;

import java.io.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class KilaeuaCam {

	public void doKilaeua() {

        CommonBeans cb = new CommonBeans();
        MyDBConnector mdb = new MyDBConnector();
        WebCommon wc = new WebCommon();
        
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
			Connection conn = mdb.getMyConnection();
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
				wc.jsoupOutBinary(camURLReturn, camOutFile, 5.0);
				wc.copyFile(camOutFile.toString(), kPath+"/KICam_Latest.jpg");
				wc.moveFile(camOutFile.toString(), kPath+"/Archive/KICam_"+thisTimestamp+".jpg");
			}

		} catch (Exception e) { e.printStackTrace(); }
			
	}

}
