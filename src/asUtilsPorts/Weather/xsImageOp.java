/*
by Anthony Stump
Created: 13 Sep 2017
Updated: 1 Feb 2020
*/

package asUtilsPorts.Weather;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;

import java.io.*;
import java.util.List;

import asUtilsPorts.Shares.EmptyImageCleaner;
import asUtilsPorts.Shares.JunkyBeans;

public class xsImageOp {

	public void main(boolean sysProc, List<String> gVars, List<String> gVarsH, List<String> gVarsL) {

		CommonBeans cb = new CommonBeans();
        JunkyBeans junkyBeans = new JunkyBeans();
        ModelShare ms = new ModelShare();
        WebCommon wc = new WebCommon();
                
		final int iCyc = 192;
		final String xsTmp = ms.get_xsTmp(sysProc);
		final String gradsOut = xsTmp+"/grib2/iOut";
		final File gradsOutObj = new File(xsTmp+"/grib2/iOut");
		final File wwwOutObj = ms.get_xsOut(sysProc);
		
		wc.runProcess("cp -Rv "+gradsOutObj.getPath()+"/* "+wwwOutObj.getPath());

		for (String gVar : gVarsL) { wc.runProcess("(ls "+wwwOutObj.toString()+"/"+gVar+"/*.png -t | head -n "+iCyc+"; ls "+wwwOutObj.toString()+"/"+gVar+"/*.png)|sort|uniq -u|xargs rm"); }
		for (String gVar : gVarsH) { wc.runProcess("(ls "+wwwOutObj.toString()+"/"+gVar+"/*.png -t | head -n 12; ls "+wwwOutObj.toString()+"/"+gVar+"/*.png)|sort|uniq -u|xargs rm"); }

		wc.runProcess("cp -R "+wwwOutObj.toString()+"/* "+gradsOut+"/");

		for (String gVar : gVarsL) {
			String thisPathString = gradsOut+"/"+gVar;
			String[] imageCleanerArgs = { thisPathString };
			EmptyImageCleaner.main(imageCleanerArgs);
			wc.runProcess("bash "+junkyBeans.getHelpers().toString()+"/Sequence.sh "+thisPathString+" png");
			wc.runProcess("ffmpeg -threads 8 -r 10 -i "+thisPathString+"/%05d.png -vcodec libx264 -pix_fmt yuv420p "+xsTmp+"/_HRRRLoop_"+gVar+".mp4");
		}

		wc.runProcess("mv "+xsTmp+"/_HRRRLoop_* "+wwwOutObj.toString()+"/");
		wc.deleteDir(gradsOutObj);

	}

}
