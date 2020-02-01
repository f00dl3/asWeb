/*
by Anthony Stump
Created: 3 SEP 2017
Updated: 1 Feb 2020
*/

package asUtilsPorts;

import java.io.*;
import java.util.Scanner;

import asUtilsPorts.Shares.JunkyBeans;
import asWebRest.shared.WebCommon;

public class Vid2GIF {

	public static void main(String[] args) {

        JunkyBeans junkyBeans = new JunkyBeans();
        WebCommon wc = new WebCommon();
                
		final String sdCardPath = junkyBeans.getSdCardPath().toString();
		final String tempFolder = junkyBeans.getRamDrive().toString()+"/xTemp";
		final String dropPath = junkyBeans.getDesktopPath().toString();
		final String v2gVersion = "10.0 (Java via API)";
		final String v2gUpdated = "2020-02-01";
		final File tempFolderObj = new File(tempFolder);
		final File xOutObj = new File(sdCardPath+"/ASWebUI/Images/X");
                
		System.out.println("Vid2GIF Self-installing\nVersion "+v2gVersion+" (GIFVer 3)\nUpdated: "+v2gUpdated);

		Scanner inputReader = new Scanner(System.in);
	
		System.out.printf("File name: ");
		String getFileName = inputReader.nextLine();

		System.out.printf("File type: ");
		String getFileType = inputReader.nextLine();

		System.out.printf("Cut (HH:MM:SS): ");
		String getCutTime = inputReader.nextLine();

		inputReader.close();

		System.out.println("\n -> Parameters input: [ "+getFileName+" ], [ "+getFileType+" ], [ "+getCutTime+" ]");

		tempFolderObj.mkdirs();
		xOutObj.mkdirs();
		
		wc.runProcess("ffmpeg -ss "+getCutTime+" -i \""+dropPath+"/"+getFileName+"."+getFileType+"\" -pix_fmt rgb24 -r 10 -s 96x96 -t 00:00:03.000 "+tempFolder+"/"+getFileName+".%03d.png");
		wc.runProcess("convert -verbose -delay 5 -loop 0 "+tempFolder+"/"+getFileName+".*.png "+tempFolder+"/"+getFileName+".gif");
		wc.moveFile(tempFolder+"/"+getFileName+".gif", sdCardPath+"/ASWebUI/Images/X/"+getFileName+".pnx");
		wc.deleteDir(tempFolderObj);
		wc.runProcess("bash "+sdCardPath+"/ASWebUI/Install.sh");

		System.out.println("Done!");

	}

}
