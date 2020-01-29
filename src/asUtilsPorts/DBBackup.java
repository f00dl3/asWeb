/*
by Anthony Stump
Created 24 Dec 2017
Updated 28 Jan 2020
 */

package asUtilsPorts;

import asUtils.UbuntuVM.BackThatAssUp;
import asWebRest.secure.GDriveAttribs;
import asWebRest.shared.GDrive;
import asWebRest.shared.WebCommon;
import asUtils.Secure.JunkyPrivate;
import asUtils.Shares.JunkyBeans;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DBBackup {
    
    public static void main(String[] args) {
           
        GDriveAttribs gDriveAttribs = new GDriveAttribs();
        JunkyBeans junkyBeans = new JunkyBeans();
        JunkyPrivate junkyPrivate = new JunkyPrivate();
        WebCommon wc = new WebCommon();
        DateFormat dateFormat = new SimpleDateFormat("yyMMdd");
        Date date = new Date();
        
        final String gdParentFolder = gDriveAttribs.getDbBackupFolder();
        final String gpgKeyRecipient = junkyPrivate.getGpgKeyRecipient();
        final String backupType = args[0];
        final String uName = junkyBeans.getUName();
        final File dbbPath = junkyBeans.getDbBackupPath();
        final File dbb2Path = junkyBeans.getDbBackupPath2();
        final File googleFlagFile = new File(dbb2Path, "GoogleFlag.tmp");
        final int keepCount = 2;
        File backupFinalFile = null;
  
        final String today = dateFormat.format(date);
        
        if(!dbbPath.exists()) { dbbPath.mkdirs(); }
        
        switch(backupType) {
            
            case "Auto":
                
                System.out.println("Determining type of backup to run...");
                if(googleFlagFile.exists()) {
                    googleFlagFile.delete();
                    System.out.println("Clone VM");
                    String[] pArgs = { "Clone" };
                    DBBackup.main(pArgs);
                } else {
                    String dumpToFile = "Not my week to do a backup!";
                    try { wc.varToFile(dumpToFile, googleFlagFile, false); } catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
                    //String dumpToFile = "WxObs -> Google Drive";
                    System.out.println(dumpToFile);
                    //String[] pArgs = { "WxObs" };
                    //DBBackup.main(pArgs);
                }
                break;
                
            case "Clone":
                String[] args2 = { };
                BackThatAssUp.main(args2);
                break;
            
            case "Full":
                System.out.println("Performing a Full Compressed MySQL backup to /extra1");
                backupFinalFile = new File(dbb2Path.toString()+"/FullBackup-"+today+".sql.gz");
                wc.runProcess("(mysqldump -h " + junkyPrivate.getAss() + " --all-databases | gzip > "+backupFinalFile.toString()+")");
                wc.runProcess("chown "+uName+" "+backupFinalFile.toString());
                wc.runProcess("((ls "+dbb2Path.toString()+"/FullBackup-??????.sql.gz -t | head -n "+keepCount+"; ls "+dbb2Path.toString()+"/FullBackup-??????.sql.gz)|sort|uniq -u|xargs rm)");
                if(backupFinalFile != null) {
                    try { GDrive.deleteChildItemsFromFolder("DBBackup"); } catch (IOException ix) { ix.printStackTrace(); }
                    try { GDrive.uploadFile(backupFinalFile, "application/zip", gdParentFolder); } catch (IOException ix) { ix.printStackTrace(); }
                } else {
                    System.out.println("Backup failed, skipping Google Drive upload.");
                }
                break;
                
            case "WxObs":
                System.out.println("Performing a WxObs Encrypted Compressed MySQL backup");
                backupFinalFile = new File(dbb2Path.toString()+"/WxObs-"+today+".sql.gz.gpg");
                wc.runProcess("(mysqldump -h 127.0.0.1 WxObs | gzip | sudo -H -u astump gpg --output "+backupFinalFile.toString()+" --encrypt --recipient "+gpgKeyRecipient+" -)");
                wc.runProcess("chown "+uName+" "+backupFinalFile.toString());
                wc.runProcess("((ls "+dbb2Path.toString()+"/WxObs-??????.sql.gz.gpg -t | head -n "+keepCount+"; ls "+dbb2Path.toString()+"/WxObs-??????.sql.gz.gpg)|sort|uniq -u|xargs rm)");
                wc.runProcess("chown -R "+dbb2Path.toString());
                if(backupFinalFile != null) {
                    try { GDrive.deleteChildItemsFromFolder("DBBackup"); } catch (IOException ix) { ix.printStackTrace(); }
                    try { GDrive.uploadFile(backupFinalFile, "application/zip", gdParentFolder); } catch (IOException ix) { ix.printStackTrace(); }
                } else {
                    System.out.println("Backup failed, skipping Google Drive upload.");
                }
                break;
                
            default:
                System.out.println("Backup type not specified. Please specify either Auto, Full, or WxObs!");
                break;
                
        }
        
        
    }
}
