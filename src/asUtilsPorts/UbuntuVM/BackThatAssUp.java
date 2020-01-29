/*
by Anthony Stump
Created: 2 May 2019
Updated: 28 Jan 2020
 */

package asUtilsPorts.UbuntuVM;

import asUtils.Secure.JunkyPrivate;
import asUtils.Shares.JunkyBeans;
import asUtils.Shares.Mailer;
import asWebRest.shared.WebCommon;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class BackThatAssUp {
	
	public void reminder() {
		
		JunkyPrivate junkyPrivate = new JunkyPrivate();
    
	    final String myCell = junkyPrivate.getSmsAddress();
	    final String myGmail = junkyPrivate.getGmailUser();
	    
        String thisSubject = "asUtilsPorts.UbuntuVM.BackThatAssUp onEvent";
        String thisMessage = thisSubject;
        
	    thisSubject = "asWeb - BackThatAssUp Reminder";
        thisMessage = "ass_VM backup VDI disabled.\n" +
                " Please run backup manually via command line. \n" +
                " Estimated file size not available from API.";
        
        Mailer.sendMail(myCell, thisSubject, thisMessage, null);
        Mailer.sendMail(myGmail, thisSubject, thisMessage, null);
        
	}
    
    public static void sendEmail(String onEvent, long fileSize) {
        
        JunkyPrivate junkyPrivate = new JunkyPrivate();
        
        final String myCell = junkyPrivate.getSmsAddress();
        final String myGmail = junkyPrivate.getGmailUser();
        
        String thisSubject = "asUtils.BackThatAssUp onEvent";
        String thisMessage = thisSubject;
        
        switch(onEvent) {
            
            case "Disabled":
                thisSubject = "asUtils.BackThatAssUp - Disabled";
                thisMessage = "ass_VM backup VDI disabled.\n" +
                        " Please run backup manually via command line. \n" +
                        " Estimated file size " + fileSize + " bytes.";
                break;
                
            case "Start":
                thisSubject = "asUtils.BackThatAssUp - Started";
                thisMessage = "ass_VM backup VDI started.\n" +
                        " Database and web interfaces unavailable. \n" +
                        " Estimated file size " + fileSize + " bytes.";
                break;
                
            case "Finish":
                thisSubject = "asUtils.BackThatAssUp - Finished";
                thisMessage = "ass_VM backup VDI completed.\n" +
                        " Database and web interfaces are now available. \n" +
                        " Please copy to USB drive ASAP to free space up. \n" +
                        " Final file size " + fileSize + " bytes.";
                break;
                
        }
                
        Mailer.sendMail(myCell, thisSubject, thisMessage, null);
        Mailer.sendMail(myGmail, thisSubject, thisMessage, null);
        
    }
    
    public static void doBackupEnabled() {
       
        JunkyBeans junkyBeans = new JunkyBeans();
        WebCommon wc = new WebCommon();
        
        final int sleepSetting = 120;
        final File uvmVdi = new File(junkyBeans.getVdiPath().toString() + "/ass_VM.vdi");
        final File uvmVdiBackup = new File(uvmVdi.toString() + ".bak");
        
        long uvmVdiSize = 0;
        long uvmVdiBackupSize = 0;
        
        try { uvmVdiSize = uvmVdi.length(); } catch (Exception ix) { ix.printStackTrace(); }
        sendEmail("Start", uvmVdiSize);
        
        wc.runProcess("sudo -H -u astump VBoxManage controlvm assVM poweroff");
        
        try {
            wc.copyFile(uvmVdi.toString(), uvmVdiBackup.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        wc.runProcessAsynch("sudo -H -u astump VBoxHeadless -s assVM &");
        
        System.out.println("Waiting " + sleepSetting + " seconds for the machine to come up...");
        try { TimeUnit.SECONDS.sleep(sleepSetting); } catch (Exception e) { e.printStackTrace(); }
        
        try { uvmVdiBackupSize = uvmVdiBackup.length(); } catch (Exception ix) { ix.printStackTrace(); }
        sendEmail("Finish", uvmVdiBackupSize);
        
        
    }
    
    public static void doBackupDisabled() {
        
        JunkyBeans junkyBeans = new JunkyBeans();
       
        final File uvmVdi = new File(junkyBeans.getVdiPath().toString() + "/ass_VM.vdi");
        long uvmVdiSize = 0;
        
        try { uvmVdiSize = uvmVdi.length(); } catch (Exception ix) { ix.printStackTrace(); }
        
        sendEmail("Disabled", uvmVdiSize);        
        
    }
    
    public static void main(String[] args) {
        
        //doBackupEnabled();
        doBackupDisabled();
        
    }
        
}
