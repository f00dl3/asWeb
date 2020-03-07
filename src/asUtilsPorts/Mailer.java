/* 
by Anthony Stump
Created: 17 Sep 2017
Updated: 5 Mar 2020
*/

package asUtilsPorts;

import asWebRest.dao.NewsFeedDAO;
import asWebRest.dao.WebUIserAuthDAO;
import asWebRest.hookers.WeatherBot;
import asWebRest.secure.JunkyPrivate;
import asWebRest.shared.MyDBConnector;
import asWebRest.shared.ThreadRipper;
import asWebRest.shared.WebCommon;

import com.jcraft.jsch.JSchException;

import asUtilsPorts.Shares.JunkyBeans;

import java.sql.*;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Store;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

import org.json.JSONArray;
import org.json.JSONObject;

public class Mailer {
    
        public static Session getMailSession() {
            
	        JunkyPrivate junkyPrivate = new JunkyPrivate();
	        JunkyBeans junkyBeans = new JunkyBeans();
	        final String username = junkyPrivate.getGmailUser();
			final String password = mailAuth();
	
			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", junkyBeans.getGmailSmtpServer());
			props.put("mail.smtp.port", "587");
	
			Session session = Session.getInstance(props,
			  new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});
	                
            return session;
                
        }
        
        public static Session getMailSessionSsh(File keyfile) throws JSchException {
            
            JunkyPrivate junkyPrivate = new JunkyPrivate();
            JunkyBeans junkyBeans = new JunkyBeans();
            final String username = junkyPrivate.getGmailUser();
            final String password = mailAuthSsh(keyfile);

			Properties props = new Properties();
			props.put("mail.smtp.auth", "true");
			props.put("mail.smtp.starttls.enable", "true");
			props.put("mail.smtp.host", junkyBeans.getGmailSmtpServer());
			props.put("mail.smtp.port", "587");
		
			Session session = Session.getInstance(props,
			  new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(username, password);
				}
			});
		            
            return session;
                
        }
        
        public static Store getMailStore() throws NoSuchProviderException, MessagingException {
            
            JunkyPrivate junkyPrivate = new JunkyPrivate();
            JunkyBeans junkyBeans = new JunkyBeans();
            Session session = getMailSession();
            Store store = session.getStore("imaps");
            store.connect(junkyBeans.getGmailSmtpServer(), junkyPrivate.getGmailUser(), mailAuth());
            return store;
            
        }

	public static String mailAuth() {
            
        MyDBConnector mdb = new MyDBConnector();
        WebCommon wc = new WebCommon();
        
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
                
		final String getAuthSQL = "SELECT Value FROM JavaSex WHERE Item='gmpa' LIMIT 1;";
		String password = null;
		try ( ResultSet resultSetGetAuth = wc.q2rs1c(dbc, getAuthSQL, null); ) {
			while (resultSetGetAuth.next()) { password = resultSetGetAuth.getString("Value"); }
		} catch (Exception e) { e.printStackTrace(); }
                
                try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
                
		return password;
	}
        
	public static String mailAuthSsh(File keyfile) throws JSchException {
        String password = "";
        MyDBConnector mdb = new MyDBConnector();
		WebUIserAuthDAO auth = new WebUIserAuthDAO();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        try { password = auth.getExternalPassword(dbc, "gmpa"); } catch (Exception e) { e.printStackTrace(); }
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
		return password;
	}
        
    public String mailForSQL(Connection dbc) {
    	
    	WebCommon wc = new WebCommon();

        String sqlStatementAppending = "";
        String messageId = "";
        String received = "";
        String fromAddress = "";
        String subject ="";
        String body = "";
        
        JSONObject allMailJSON = readMailToJSON();
        Iterator<?> keys = allMailJSON.keys();
        while(keys.hasNext()) {
            String theKey = (String)keys.next();
            messageId = wc.jsonSanitize(theKey);
            JSONObject messageObj = (JSONObject) allMailJSON.get(theKey);
            received = wc.jsonSanitize(messageObj.getString("Received"));
            fromAddress = wc.jsonSanitize(messageObj.getString("From"));
            subject = wc.jsonSanitize(messageObj.getString("Subject"));
            body = wc.jsonSanitize(messageObj.getString("Body"));
            sqlStatementAppending += "('"+messageId+"','"+received+"','"+fromAddress+"','"+subject+"','"+body+"'),";
        }
        
        sqlStatementAppending = (sqlStatementAppending+";").replace(",;",";");
        
        String mailUpdateSQL = "INSERT IGNORE INTO Feeds.Messages ("
                + "MessageId, Received, FromAddress, Subject, Body"
                + ") VALUES "+sqlStatementAppending;

		
        try { wc.q2do1c(dbc, mailUpdateSQL, null); } catch (Exception e) { }
        try { runMailActions(dbc); } catch (Exception e) { }
        
        return mailUpdateSQL;
        
    }

    public static JSONObject readMailToJSON() {
        JSONObject allMail = new JSONObject();
        try {
            Store store = getMailStore();
            Folder inbox = store.getFolder("inbox");
            inbox.open(Folder.READ_ONLY);
            String messageContent = "";
            int messageCount = inbox.getMessageCount();
            System.out.println("Total messages in Inbox: "+messageCount+"\n");
            Message[] messages = inbox.getMessages();
            for (int i=0; i<messages.length; i++) {
                Message message = messages[i];
                String senderAddress = InternetAddress.toString(message.getFrom());
                JSONObject mailMessage = new JSONObject();
                String messageKey = message.getReceivedDate().toString()+message.getSubject();
                mailMessage.put("Received", message.getReceivedDate().toString());
                mailMessage.put("From", senderAddress);
                mailMessage.put("Subject", message.getSubject());
                if(message.getContent() instanceof Multipart) {
                    Multipart multipart = (Multipart) message.getContent();
                    for(int j=0; j < multipart.getCount(); j++) {
                        BodyPart bodyPart = multipart.getBodyPart(j);
                        String disposition = bodyPart.getDisposition();
                        if(disposition != null && (disposition.equalsIgnoreCase("ATTACHMENT"))) {
                            System.out.println("Mail has an attachment");
                            DataHandler handler = bodyPart.getDataHandler();
                            System.out.println("File name: "+handler.getName());
                        } else {
                            messageContent += bodyPart.getContent().toString();
                        }
                    }
                } else {
                    messageContent = message.getContent().toString();
                }
                mailMessage.put("Body", messageContent);
                allMail.put(messageKey, mailMessage);
            }
            inbox.close(true);
            store.close();
        } catch (NoSuchProviderException nsp) {
            nsp.printStackTrace();              
        } catch (MessagingException mex) {
            mex.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return allMail;
    }
    
    public void runMailActions(Connection dbc) {

    	JunkyPrivate jp = new JunkyPrivate();
    	NewsFeedDAO newsFeedDAO = new NewsFeedDAO();
    	JSONArray messageStore = newsFeedDAO.getRecentEmail(dbc);
    	for(int i = 0; i < messageStore.length(); i++) {
    		JSONObject thisMessage = messageStore.getJSONObject(i);
    		String thisSubject = thisMessage.getString("Subject");
    		if(thisSubject.contains("asWeb:")) {
    	        String messageRecipient = jp.getSmsAddress();
    	        String messageSubject = "asWeb Command Detected!";
    	        String messageBody = thisMessage.getString("Subject");
    	        List<String> sqParams = new ArrayList<String>();
    	        sqParams.add(0, thisMessage.getString("MessageID"));
    	        sendMail(messageRecipient, messageSubject, messageBody, null);
    	        newsFeedDAO.setMessageActionTaken(dbc, sqParams);
    		}
    	}
    	
    }
    
	public void sendMail(String sendTo, String messageSubject, String messageContent, File attachment) {

                JunkyPrivate junkyPrivate = new JunkyPrivate();
                final String username = junkyPrivate.getGmailUser();
                
                Session session = getMailSession();

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(sendTo));
			message.setSubject(messageSubject);
                        if(attachment != null) {
                            DataSource source = new FileDataSource(attachment.toString());
                            Multipart multipart = new MimeMultipart();
                            BodyPart messageBodyPart1 = new MimeBodyPart();
                            BodyPart messageBodyPart2 = new MimeBodyPart();
                            messageBodyPart1.setText(messageContent);
                            messageBodyPart2.setDataHandler(new DataHandler(source));
                            messageBodyPart2.setFileName(attachment.toString());
                            multipart.addBodyPart(messageBodyPart1);
                            multipart.addBodyPart(messageBodyPart2);
                            message.setContent(multipart);
                        } else {
                            message.setText(messageContent);
                        }
			Transport.send(message);
			System.out.println(" -> Mail sent!");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
 
	public static void sendMailSsh(File keyfile, String sendTo, String messageSubject, String messageContent, File attachment) {

                JunkyPrivate junkyPrivate = new JunkyPrivate();
                final String username = junkyPrivate.getGmailUser();
                System.out.println(" -> Started request to Mailer.sendMailSsh");
                
                Session session = null;
                try {
                    session = getMailSessionSsh(keyfile);
                    System.out.println(" -> Successfully started SSH mail session!");
                } catch (Exception e) {
                    e.printStackTrace();
                }

		try {

			Message message = new MimeMessage(session);
			message.setFrom(new InternetAddress(username));
			message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(sendTo));
			message.setSubject(messageSubject);
                        if(attachment != null) {
                            DataSource source = new FileDataSource(attachment.toString());
                            Multipart multipart = new MimeMultipart();
                            BodyPart messageBodyPart1 = new MimeBodyPart();
                            BodyPart messageBodyPart2 = new MimeBodyPart();
                            messageBodyPart1.setText(messageContent);
                            messageBodyPart2.setDataHandler(new DataHandler(source));
                            messageBodyPart2.setFileName(attachment.toString());
                            multipart.addBodyPart(messageBodyPart1);
                            multipart.addBodyPart(messageBodyPart2);
                            message.setContent(multipart);
                        } else {
                            message.setText(messageContent);
                        }
			Transport.send(message);
			System.out.println(" -> SSH Mail sent!");

		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
	}
	
	public void sendMultiAlert(String message, boolean testing) {
		
		JunkyPrivate junkyPrivate = new JunkyPrivate();
		ThreadRipper tr = new ThreadRipper();
		WeatherBot wxBot = new WeatherBot();
		
		final String eaSubject = "asWeb Broadcast";
        final String myCell = junkyPrivate.getSmsAddress();
        final String myGmail = junkyPrivate.getGmailUser();
        
		ArrayList<Runnable> alerts = new ArrayList<Runnable>();
		alerts.add(() -> wxBot.botBroadcastOnly(message));
		alerts.add(() -> sendMail(myCell, eaSubject, message, null));
		alerts.add(() -> sendMail(myGmail, eaSubject, message, null));
		tr.runProcesses(alerts, false, false);
		
	}
	
	public void sendQuickText(String message) {
		JunkyPrivate junkyPrivate = new JunkyPrivate();
		final String eaSubject = "asWeb Alert";
        final String myCell = junkyPrivate.getSmsAddress();
        sendMail(myCell, eaSubject, message, null);		
	}

	public void sendQuickEmail(String message) {
		JunkyPrivate junkyPrivate = new JunkyPrivate();
		final String eaSubject = "asWeb Alert";
        final String myGmail = junkyPrivate.getGmailUser();
        sendMail(myGmail, eaSubject, message, null);		
	}

	public void sendQuickEmailTo(String recipient, String message) {
		JunkyPrivate junkyPrivate = new JunkyPrivate();
		final String eaSubject = "asWeb Alert";
        final String myGmail = junkyPrivate.getGmailUser();
        sendMail(recipient, eaSubject, message, null);		
	}

	public void sendQuickEmailAttachmentTo(String recipient, String message, File attachment) {
		JunkyPrivate junkyPrivate = new JunkyPrivate();
		final String eaSubject = "asWeb Alert";
        final String myGmail = junkyPrivate.getGmailUser();
        sendMail(recipient, eaSubject, message, attachment);		
	}
	
}
