/*
by Anthony Stump
Created: 24 Dec 2017
Updated: 29 Jan 2020
*/

package asUtilsPorts;

import asUtilsPorts.Cams.CamBeans;
import asUtilsPorts.Shares.JunkyBeans;
import asWebRest.secure.GDriveAttribs;
import asWebRest.shared.CommonBeans;
import asWebRest.shared.GDrive;
import asWebRest.shared.ThreadRipper;
import asWebRest.shared.WebCommon;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CamPusher {
    
    public String pushIt(Connection dbc) {
        
        GDriveAttribs gDriveAttribs = new GDriveAttribs();
        CommonBeans cb = new CommonBeans();
        CamBeans camBeans = new CamBeans();
        JunkyBeans junkyBeans = new JunkyBeans();
        ThreadRipper tr = new ThreadRipper();
        WebCommon wc = new WebCommon();
        
        Date date = new Date();

		final String cachePath = cb.getPathChartCache();
        final String parentFolder = gDriveAttribs.getCloudCamFolder();
        final DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        final String getLockTime = dateFormat.format(date);
        final File camWebRoot = camBeans.getCamWebRoot();
        final File dumpTemp = camBeans.getDumpTemp();
        final File helpers = junkyBeans.getHelpers();
        final File mp4Log = new File(camWebRoot.toString()+"/MakeMP4_GIF.log");
        final File mp4LogPub = new File(cb.getPathChartCache()+"/MakeMP4Pub_GIF.log");
        final File mp4Out = new File(camWebRoot.toString()+"/Archive/push_"+getLockTime+".mp4");
        final File mp4OutAlt = new File(camWebRoot.toString()+"/Archive/push_"+getLockTime+"_b.mp4");
        final File mp4OutPub = new File(cb.getPathChartCache()+"/CamLoopPublic.mp4");
        final File pushTemp = camBeans.getPushTemp();
        final File pushTempPub = camBeans.getPushTempPub();
        final File tfOutCase = camBeans.getTfOutCase();
        final File tfOutCPU = camBeans.getTfOutCPU();
        final File tfOutGarage = camBeans.getTfOutGarage();
        final String ffParams = "\"scale=trunc(iw/2)*2:trunc(ih/2)*2\" -vcodec libx264 -pix_fmt yuv420p";
        final String tempGarage = "SELECT ExtTemp FROM net_snmp.RaspberryPi ORDER BY WalkTime DESC LIMIT 1;";
        final String tempSelects = "SELECT TempCase, TempCPU FROM net_snmp.Main ORDER BY WalkTime DESC LIMIT 1;";
        int tfCase = 0;
        int tfCPU = 0;
        int tfGarage = 0;

        
        if(!pushTemp.exists()) { pushTemp.mkdirs(); pushTempPub.mkdirs(); }
        if(!camWebRoot.exists()) { camWebRoot.mkdirs(); }
        if(dumpTemp.exists()) { wc.deleteDir(dumpTemp); }
        
        try {
        	ResultSet resultSet = wc.q2rs1c(dbc, tempSelects, null);
        	while (resultSet.next()) {
                tfCase = (Integer.parseInt(resultSet.getString("TempCase"))/1000)*9/5+(32-6);
                tfCPU = (Integer.parseInt(resultSet.getString("TempCPU"))/1000)*9/5+(32-6);
            }
        } catch (Exception e) { e.printStackTrace(); }
        
        try {
        	ResultSet resultSet = wc.q2rs1c(dbc, tempGarage, null);
        	while (resultSet.next()) {
                tfGarage = (int) Math.round(Double.parseDouble(resultSet.getString("ExtTemp")));
            }
        } catch (Exception e) { e.printStackTrace(); }
        
        System.out.println("[DEBUG] TempCase: "+tfCase+"; TempCPU: "+tfCPU+"; TempGar: "+tfGarage);
        
        try { wc.varToFile(Integer.toString(tfCase), tfOutCase, false); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
        try { wc.varToFile(Integer.toString(tfCPU), tfOutCPU, false); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
        try { wc.varToFile(Integer.toString(tfGarage), tfOutGarage, false); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
        
        dumpTemp.mkdirs();
        if(mp4OutAlt.exists()) { mp4OutAlt.delete(); }
        if(mp4Out.exists()) { wc.moveFile(mp4Out.toString(), mp4OutAlt.toString()); }
        wc.runProcess("mv "+pushTemp.toString()+"/*.jpeg "+dumpTemp.toString());
        wc.runProcess("find "+dumpTemp.toString()+"/ -type f -size 0b -delete");
        wc.runProcess("bash "+helpers.toString()+"/Sequence.sh "+dumpTemp.toString()+"/ jpeg");
        try { wc.copyFile(dumpTemp.toString()+"/00001.jpeg", camWebRoot.toString()+"/_Latest.jpeg"); } catch (IOException ix) { ix.printStackTrace(); }
        wc.runProcess("(ffmpeg -threads "+tr.getMaxThreads()+" -framerate "+camBeans.getFrameRate()+" -i "+dumpTemp.toString()+"/%05d.jpeg -vf "+ffParams+" "+mp4Out.toString()+" 2> "+mp4Log.toString()+")");
        wc.runProcess("mogrify -resize 25% "+camWebRoot.toString()+"/_Latest.jpeg");
        try { wc.copyFile(mp4Out.toString(), camWebRoot.toString()+"/_Loop.mp4"); } catch (IOException ix) { ix.printStackTrace(); }
        wc.deleteDir(dumpTemp);
       
        try { GDrive.uploadFile(mp4Out, "video/mp4", parentFolder); } catch (Exception ix) { ix.printStackTrace(); }

        dumpTemp.mkdirs();
        wc.runProcess("mv "+pushTempPub.toString()+"/*.jpeg "+dumpTemp.toString());
        wc.runProcess("find "+dumpTemp.toString()+"/ -type f -size 0b -delete");
        wc.runProcess("bash "+helpers.toString()+"/Sequence.sh "+dumpTemp.toString()+"/ jpeg");
        wc.runProcess("(ffmpeg -y -threads "+tr.getMaxThreads()+" -framerate "+camBeans.getFrameRate()+" -i "+dumpTemp.toString()+"/%05d.jpeg -vf "+ffParams+" "+mp4OutPub.toString()+ " 2> "+mp4LogPub.toString()+")");
        wc.deleteDir(dumpTemp);
        
        return "End of program!";
        
    }
    
}
