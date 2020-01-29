/*
by Anthony Stump
Created: 17 Sep 2017
Updated: 29 Jan 2020
*/

package asUtilsPorts.Shares;

import java.io.File;

import asUtilsPorts.Mailer;
import asWebRest.secure.JunkyPrivate;
import asWebRest.shared.WebCommon;


public class MailerTest {

	public static void main(String[] args) {

		Mailer mailer = new Mailer();
        JunkyPrivate junkyPrivate = new JunkyPrivate();
        JunkyBeans junkyBeans = new JunkyBeans();
        WebCommon wc = new WebCommon();
        
        final File sysLog = new File("/var/log/syslog");
        final File packedLog = new File(junkyBeans.getRamDrive().toString()+"/syslog.zip");
		final String thisSubject = junkyBeans.getApplicationName()+".Shares.MailerTest";
		final String myGmail = junkyPrivate.getGmailUser();
		final String thisMessage = "This is a Java test message.";
                
		System.out.println(thisSubject+" - send message with attachment to gmail.");
                
        wc.zipThisFile(sysLog, packedLog);
		mailer.sendMail(myGmail, thisSubject, thisMessage, packedLog);

	}

}
