/*
by Anthony Stump
Created: 21 Dec 2017
Updated: 1 Feb 2020
 */

package asUtilsPorts.Shares;

import asWebRest.secure.JunkyPrivate;
import asWebRest.shared.WebCommon;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class SSHTools {

    private static int globalSshTimeout = 5;
    
    public void backupPortForwardMethod(File keyfile, String user, String hostIP, int sshPort, int portL, int portR) {
        
    	WebCommon wc = new WebCommon();
        System.out.println("BACKUP METHOD");
        final String sshProcessCommand = "ssh -fNT -i " + keyfile.toString() + 
                " -p " + sshPort +
                " -L " + portL + ":localhost:" + portR +
                " " + user + "@" + hostIP+ " &";
        System.out.println("DEBUG Command: " + sshProcessCommand);
        wc.runProcess(sshProcessCommand);
        
    }
    
    public Session getDesktopSession(File keyfile, String whatIP) throws JSchException {
        JunkyPrivate junkyPrivate = new JunkyPrivate();
        SSHVars sshVars = new SSHVars();
        sshVars.setHostIP(whatIP);
        sshVars.setUser(junkyPrivate.getDesktopUser());
        sshVars.setPort(junkyPrivate.getDesktopSshPort());
        Session session = null;
        JSch jsch = new JSch();
        jsch.addIdentity(keyfile.toString());
        System.out.println("JSCH trying: " + sshVars.getUser() + "@" + sshVars.getHostIP() + ":" + sshVars.getPort());
        session = jsch.getSession(sshVars.getUser(), sshVars.getHostIP(), sshVars.getPort());
        session.setTimeout(1000*globalSshTimeout);
        java.util.Properties config = new java.util.Properties();
        config.put("StrictHostKeyChecking", "no");
        config.put("HashKnownHosts", "yes");
        config.put("trust", "true");
        session.setConfig(config);
        return session;
    }    
    
    public Session portForwardSessionDesktop(File keyfile, int portL, int portR) throws JSchException {
        JunkyPrivate jp = new JunkyPrivate();
        Session session = getDesktopSession(keyfile, jp.getIpForDesktop());
        session.setPortForwardingL(portL, "localhost", portR);
        return session;
    }
	
    public Session portForwardSessionDesktopLAN(File keyfile, int portL, int portR) throws JSchException {
        JunkyPrivate jp = new JunkyPrivate();
        Session session = getDesktopSession(keyfile, jp.getIpForDesktopLAN());   
        session.setPortForwardingL(portL, "localhost", portR);
        return session;
    }
    
    public void sshRunCommands(String user, String hostIP, int port, File hostKey, String[] commands) {
		
            SSHVars sshVars = new SSHVars();
            sshVars.setHostIP(hostIP);
            sshVars.setUser(user);
            sshVars.setPort(port);

            Session session = null;
			
            try {
                JSch jsch = new JSch();
                jsch.addIdentity(hostKey.toString());
                session = jsch.getSession(sshVars.getUser(), sshVars.getHostIP(), sshVars.getPort());
                java.util.Properties config = new java.util.Properties();
                config.put("StrictHostKeyChecking", "no");
                session.setConfig(config);
                session.connect();
                System.out.println("DEBUG: Connected to " + hostIP);
                ChannelExec chEx = (ChannelExec)session.openChannel("exec");
                for(String command : commands) {
                    try {
                        System.out.println("DEBUG: Attempting command " + command);
                        chEx.setCommand(command);
                        InputStream inStream = chEx.getInputStream();
                        chEx.connect();
                        InputStreamReader ir = new InputStreamReader(inStream);
                        BufferedReader br = new BufferedReader(ir);
                        String line = null;
                        int index = 0;
                        while((line = br.readLine()) != null) {
                                System.out.println(++index + " : " + line);
                        }
                        br.close();
                        ir.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                chEx.disconnect();
                session.disconnect();
            } catch (Exception e) {
                    e.printStackTrace();
            }

    }
        
    public void sftpUpload(String user, String hostIP, int port, File localFullPath, File uploadFullPath, File hostKey) {

            System.out.println("Starting SSH transaction - PUT "+localFullPath.toString()
                    +" TO "+uploadFullPath.toString()+" on "+user+"@"+hostIP+":"+port
                    +" using key "+hostKey.toString());
            
            SSHVars sshVars = new SSHVars();
            sshVars.setHostIP(hostIP);
            sshVars.setLocalFullPath(localFullPath);
            sshVars.setUser(user);
            sshVars.setUploadFullPath(uploadFullPath);
            sshVars.setPort(port);

            Session session = null;
            Channel channel = null;
            ChannelSftp channelSftp = null;

            try {
                JSch jsch = new JSch();
                jsch.addIdentity(hostKey.toString());
                session = jsch.getSession(sshVars.getUser(), sshVars.getHostIP(), sshVars.getPort());
                java.util.Properties config = new java.util.Properties();
                config.put("StrictHostKeyChecking", "no");
                session.setConfig(config);
                session.setTimeout(1000*globalSshTimeout);
                session.connect();
                channel = session.openChannel("sftp");
                channel.connect();
                channelSftp = (ChannelSftp) channel;
                channelSftp.cd(sshVars.getUploadFullPath().getParent());
                channelSftp.put(new FileInputStream(sshVars.getLocalFullPath()), sshVars.getLocalFullPath().getName());
                channelSftp.exit();
                session.disconnect();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

    }
    
    public void sftpDownload(String user, String hostIP, int port, File localFullPath, File downloadFullPath, File hostKey) {

            System.out.println("Starting SSH transaction - GET "+downloadFullPath.toString()
                    +" from "+user+"@"+hostIP+":"+port
                    +" using key "+hostKey.toString());
            
            SSHVars sshVars = new SSHVars();
            sshVars.setHostIP(hostIP);
            sshVars.setLocalFullPath(localFullPath);
            sshVars.setUser(user);
            sshVars.setDownloadFullPath(downloadFullPath);
            sshVars.setPort(port);

            Session session = null;
            Channel channel = null;
            ChannelSftp channelSftp = null;

            try {
                JSch jsch = new JSch();
                jsch.addIdentity(hostKey.toString());
                session = jsch.getSession(sshVars.getUser(), sshVars.getHostIP(), sshVars.getPort());
                java.util.Properties config = new java.util.Properties();
                config.put("StrictHostKeyChecking", "no");
                session.setConfig(config);
                session.setTimeout(1000*globalSshTimeout);
                session.connect();
                channel = session.openChannel("sftp");
                channel.connect();
                channelSftp = (ChannelSftp) channel;
                channelSftp.lcd(sshVars.getLocalFullPath().getParent());
                channelSftp.cd(sshVars.getDownloadFullPath().getParent());
                channelSftp.get(sshVars.getDownloadFullPath().getName(), sshVars.getLocalFullPath().getName(), null );
                channelSftp.exit();
                session.disconnect();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

    }
    
}
