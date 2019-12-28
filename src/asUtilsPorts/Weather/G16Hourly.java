/*
by Anthony Stump
Created: 7 Sep 2017
Updated: 28 Dec 2019
*/

package asUtilsPorts.Weather;

import asUtils.Shares.JunkyBeans;
import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;

import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;


public class G16Hourly {

	public static void main(String[] args) {
            
		CommonBeans cb = new CommonBeans();
        JunkyBeans junkyBeans = new JunkyBeans();

		final String animName = args[0];
		final DateFormat dateFormat = new SimpleDateFormat("yyMMddHH");
		final Date date = new Date();
		final String mp4Timestamp = dateFormat.format(date);
		final String g16Source = cb.getPersistTomcat()+"/Get/G16/"+animName;
		final File memTemp = new File(cb.getRamPath()+"/G16MP4/"+animName);
		final File g16Archive = new File(g16Source+"/Archive");

		memTemp.mkdirs();
		g16Archive.mkdirs();

		WebCommon.runProcess("mv "+g16Source+"/*.jpg "+memTemp+"/");
		WebCommon.runProcess("bash "+junkyBeans.getHelpers().toString()+"/Sequence.sh "+memTemp+"/ jpg");
		//WebCommon.runProcess("mogrify -format jpg "+memTemp+"/*.jpg");
		WebCommon.runProcess("ffmpeg -threads 8 -framerate 24 -i "+memTemp+"/%05d.jpg -vf \"scale=trunc(iw/2)*2:trunc(ih/2)*2\" -vcodec libx264 -pix_fmt yuv420p "+g16Archive.getPath()+"/H"+mp4Timestamp+".mp4");
		//WebCommon.runProcess("chown -R "+junkyBeans.getWebUser()+" "+g16Archive.getPath());
		WebCommon.deleteDir(memTemp);

	}

}
