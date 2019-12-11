/*
by Anthony Stump
Created: 24 Dec 2017
Updated: 4 Dec 2019
*/

package asUtilsPorts;

import asUtilsPorts.Cams.CamBeans;
import asWebRest.secure.GDriveAttribs;
import asWebRest.shared.GDrive;
import asWebRest.shared.WebCommon;
import asUtils.Shares.JunkyBeans;
import asUtils.Shares.StumpJunk;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CamPusher {
    
    public static String pushIt(Connection dbc) {
        
        GDriveAttribs gDriveAttribs = new GDriveAttribs();
        CamBeans camBeans = new CamBeans();
        JunkyBeans junkyBeans = new JunkyBeans();
        Date date = new Date();
        WebCommon wc = new WebCommon();
        
        final String parentFolder = gDriveAttribs.getCloudCamFolder();
        final DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        final String getLockTime = dateFormat.format(date);
        final File camWebRoot = camBeans.getCamWebRoot();
        final File dumpTemp = camBeans.getDumpTemp();
        final File helpers = junkyBeans.getHelpers();
        final File mp4Log = new File(camWebRoot.toString()+"/MakeMP4_GIF.log");
        final File mp4Out = new File(camWebRoot.toString()+"/Archive/push_"+getLockTime+".mp4");
        final File mp4OutAlt = new File(camWebRoot.toString()+"/Archive/push_"+getLockTime+"_b.mp4");
        final File pushTemp = camBeans.getPushTemp();
        final File tfOutCase = camBeans.getTfOutCase();
        final File tfOutCPU = camBeans.getTfOutCPU();
        final File tfOutGarage = camBeans.getTfOutGarage();
        final String ffParams = "\"scale=trunc(iw/2)*2:trunc(ih/2)*2\" -vcodec libx264 -pix_fmt yuv420p";
        final String tempGarage = "SELECT ExtTemp FROM net_snmp.RaspberryPi ORDER BY WalkTime DESC LIMIT 1;";
        final String tempSelects = "SELECT TempCase, TempCPU FROM net_snmp.Main ORDER BY WalkTime DESC LIMIT 1;";
        int tfCase = 0;
        int tfCPU = 0;
        int tfGarage = 0;
              
        if(!pushTemp.exists()) { pushTemp.mkdirs(); }
        
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
        
        try { StumpJunk.varToFile(Integer.toString(tfCase), tfOutCase, false); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
        try { StumpJunk.varToFile(Integer.toString(tfCPU), tfOutCPU, false); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
        try { StumpJunk.varToFile(Integer.toString(tfGarage), tfOutGarage, false); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
        
        if(!dumpTemp.exists()) { dumpTemp.mkdirs(); }
        if(mp4OutAlt.exists()) { mp4OutAlt.delete(); }
        if(mp4Out.exists()) { StumpJunk.moveFile(mp4Out.toString(), mp4OutAlt.toString()); }
        StumpJunk.runProcess("mv "+pushTemp.toString()+"/*.jpeg "+dumpTemp.toString());
        StumpJunk.runProcess("find "+dumpTemp.toString()+"/ -type f -size 0b -delete");
        StumpJunk.runProcess("bash "+helpers.toString()+"/Sequence.sh "+dumpTemp.toString()+"/ jpeg");
        try { StumpJunk.copyFile(dumpTemp.toString()+"/00001.jpeg", camWebRoot.toString()+"/_Latest.jpeg"); } catch (IOException ix) { ix.printStackTrace(); }
        StumpJunk.runProcess("(ffmpeg -threads 8 -framerate "+camBeans.getFrameRate()+" -i "+dumpTemp.toString()+"/%05d.jpeg -vf "+ffParams+" "+mp4Out.toString()+" 2> "+mp4Log.toString()+")");
        StumpJunk.runProcess("mogrify -resize 25% "+camWebRoot.toString()+"/_Latest.jpeg");
        try { StumpJunk.copyFile(mp4Out.toString(), camWebRoot.toString()+"/_Loop.mp4"); } catch (IOException ix) { ix.printStackTrace(); }
        StumpJunk.deleteDir(dumpTemp);
       
        try { GDrive.uploadFile(mp4Out, "video/mp4", parentFolder); } catch (IOException ix) { ix.printStackTrace(); }
        
        return "End of program!";
        
    }
    
}