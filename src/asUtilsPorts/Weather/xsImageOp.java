/*
by Anthony Stump
Created: 13 Sep 2017
Updated: 30 Dec 2019
*/

package asUtilsPorts.Weather;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;

import java.io.*;
import java.util.List;

import asUtils.Shares.EmptyImageCleaner;
import asUtils.Shares.JunkyBeans;

public class xsImageOp {

	public static void main(String[] args, List<String> gVars, List<String> gVarsH, List<String> gVarsL) {

		CommonBeans cb = new CommonBeans();
        JunkyBeans junkyBeans = new JunkyBeans();
        WebCommon wc = new WebCommon();
                
		final int iCyc = 192;
		final String xsTmp = args[0];
		final String gradsOut = xsTmp+"/grib2/iOut";
		final String wwwOut = cb.getPersistTomcat()+"/G2Out";
		final File gradsOutObj = new File(xsTmp+"/grib2/iOut");
		final File wwwOutObj = new File(wwwOut+"/xsOut");
		
		wc.runProcess("cp -Rv "+gradsOutObj.getPath()+"/* "+wwwOutObj.getPath());

		for (String gVar : gVarsL) { wc.runProcess("(ls "+wwwOut+"/xsOut/"+gVar+"/*.png -t | head -n "+iCyc+"; ls "+wwwOut+"/xsOut/"+gVar+"/*.png)|sort|uniq -u|xargs rm"); }
		for (String gVar : gVarsH) { wc.runProcess("(ls "+wwwOut+"/xsOut/"+gVar+"/*.png -t | head -n 12; ls "+wwwOut+"/xsOut/"+gVar+"/*.png)|sort|uniq -u|xargs rm"); }

		wc.runProcess("cp -R "+wwwOut+"/xsOut/* "+gradsOut+"/");

		for (String gVar : gVarsL) {
			String thisPathString = gradsOut+"/"+gVar;
			String[] imageCleanerArgs = { thisPathString };
			EmptyImageCleaner.main(imageCleanerArgs);
			wc.runProcess("bash "+junkyBeans.getHelpers().toString()+"/Sequence.sh "+thisPathString+" png");
			wc.runProcess("ffmpeg -threads 8 -r 10 -i "+thisPathString+"/%05d.png -vcodec libx264 -pix_fmt yuv420p "+xsTmp+"/_HRRRLoop_"+gVar+".mp4");
		}

		wc.runProcess("mv "+xsTmp+"/_HRRRLoop_* "+wwwOut+"/");
		//StumpJunk.runProcess("chown -R "+junkyBeans.getWebUser()+" "+wwwOut+"/");
		wc.deleteDir(gradsOutObj);

	}

}
