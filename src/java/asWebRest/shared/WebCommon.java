/*
by Anthony Stump
Created: 11 Feb 2018
Updated: 24 Apr 2018
*/

package asWebRest.shared;

import asWebRest.shared.MyDBConnector;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.jsoup.Jsoup;

public class WebCommon {
            
    public static String basicInputFilter(String inString) {
        return inString.replace("\'", "\\\'").replace("\"", "\\\"").replace("\n", "\\n");
    }
    
    public static String cryptIt(String passwordIn) throws Exception {
        byte[] hash = hashIt(passwordIn);
        StringBuffer hexString = new StringBuffer();
        for(int i = 0; i< hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
        
    public static String getFileExtension(File thisFile) {
        String fileString = thisFile.toString();
        String fileExtension = null;
        int i = fileString.lastIndexOf('.');
        int p = Math.max(fileString.lastIndexOf('/'), fileString.lastIndexOf("\\"));
        if (i > p) { fileExtension = fileString.substring(i+1); }
        return fileExtension;
    }
    
    public void getFolderListing(String directoryName, ArrayList<File> files) {
        File directory = new File(directoryName);
        File[] fList = directory.listFiles();
        for (File file : fList) {
            if (file.isFile()) {
                try {
                    files.add(file);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (file.isDirectory()) {
                getFolderListing(file.getAbsolutePath(), files);
            }
        }
    }

    public static byte[] hashIt(String passwordIn) throws Exception {
        
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(passwordIn.getBytes(StandardCharsets.UTF_8));
        return encodedHash;
        
    }
    
    public static String htmlStripTease(String stringIn) {
        return Jsoup.parse(stringIn).text();
    }
    
    public static boolean isSet(String tStr) {
        if (tStr != null && !tStr.isEmpty()) {
            return true;
        } else { return false; }
    }

    public static boolean isSetNotZero(String tStr) {
        if (tStr != null && !tStr.isEmpty() && !tStr.equals("0")) {
            return true;
        } else { return false; }
    }

    public String q2do(String query, List<String> params) throws Exception {
        String messageBack = "Query has not ran yet or failed!";
        if(isSet(params.toString())) { messageBack += "PARAMS: " + params.toString() + "\n"; }
        try {
            MyDBConnector mdb = new MyDBConnector();
            Connection connection = mdb.getMyConnection();
            PreparedStatement pStatement = connection.prepareStatement(query);
            int pit = 1;
            if(params != null) {
                for (String param : params) {
                    if(param != null) {
                        if (param.equals("on")) { param = "1"; }
                        if (param.equals("off")) { param = "0"; }
                        try {
                            double paramAsDouble = Double.parseDouble(param);
                            try {
                                int paramAsInt = Integer.parseInt(param);
                                pStatement.setInt(pit, paramAsInt);
                            } catch (NumberFormatException e) {
                                pStatement.setDouble(pit, paramAsDouble);
                            }
                        } catch (NumberFormatException ex) {
                            pStatement.setString(pit, param);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        pStatement.setString(pit, null);
                    }
                    pit++;
                }
            }
            pStatement.execute();
            messageBack = "Query ran - Success!";
        } catch (Exception e) { e.printStackTrace(); }
        return messageBack;
    }
        
    public String q2do1c(Connection connection, String query, List<String> params) throws Exception {
        String messageBack = "Query has not ran yet or failed!";
        if(isSet(params.toString())) { messageBack += "PARAMS: " + params.toString() + "\n"; }
        try {
            PreparedStatement pStatement = connection.prepareStatement(query);
            int pit = 1;
            if(params != null) {
                for (String param : params) {
                    if(param != null) {
                        if (param.equals("on")) { param = "1"; }
                        if (param.equals("off")) { param = "0"; }
                        try {
                            double paramAsDouble = Double.parseDouble(param);
                            try {
                                int paramAsInt = Integer.parseInt(param);
                                pStatement.setInt(pit, paramAsInt);
                            } catch (NumberFormatException e) {
                                pStatement.setDouble(pit, paramAsDouble);
                            }
                        } catch (NumberFormatException ex) {
                            pStatement.setString(pit, param);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        pStatement.setString(pit, null);
                    }
                    pit++;
                }
            }
            pStatement.execute();
            messageBack = "Query ran - Success!";
        } catch (Exception e) { e.printStackTrace(); }
        return messageBack;
    }
    
    public static ResultSet q2rs(String query, List<String> params) throws Exception {
        MyDBConnector mdb = new MyDBConnector();
        Connection connection = mdb.getMyConnection();
        PreparedStatement pStatement = connection.prepareStatement(query);
        int pit = 1;
        if(params != null) {
            for (String param : params) {
                try {
                    double paramAsDouble = Double.parseDouble(param);
                    try {
                        int paramAsInt = Integer.parseInt(param);
                        pStatement.setInt(pit, paramAsInt);
                    } catch (NumberFormatException e) {
                        pStatement.setDouble(pit, paramAsDouble);
                    }
                } catch (NumberFormatException e) {
                    pStatement.setString(pit, param);
                }
                pit++;
            }
        }
        ResultSet resultSet = pStatement.executeQuery();
        return resultSet;
    }
    
    public static ResultSet q2rs1c(Connection connection, String query, List<String> params) throws Exception {
        PreparedStatement pStatement = connection.prepareStatement(query);
        int pit = 1;
        if(params != null) {
            for (String param : params) {
                try {
                    double paramAsDouble = Double.parseDouble(param);
                    try {
                        int paramAsInt = Integer.parseInt(param);
                        pStatement.setInt(pit, paramAsInt);
                    } catch (NumberFormatException e) {
                        pStatement.setDouble(pit, paramAsDouble);
                    }
                } catch (NumberFormatException e) {
                    pStatement.setString(pit, param);
                }
                pit++;
            }
        }
        ResultSet resultSet = pStatement.executeQuery();
        return resultSet;
    }
    
    public static String unzipFile(String zipFile, String outputFolder) {
        String resultsBack = "";
        byte[] buffer = new byte[4096];
        try {
            File folder = new File(outputFolder);
            if(!folder.exists()) { folder.mkdirs(); }
            ZipInputStream zis = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)));
            ZipEntry ze = zis.getNextEntry();
            while(ze != null) {
                String fileName = ze.getName();
                File newFile = new File(outputFolder+File.separator+fileName);
                resultsBack += "Unzip : " + newFile.getAbsoluteFile() + "\n";
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) { fos.write(buffer, 0, len); }
                fos.close();
                ze = zis.getNextEntry();
            }
            zis.closeEntry();
            zis.close();
            resultsBack += "Done!";
        } catch (IOException ex) { ex.printStackTrace(); }
        return resultsBack;
    }

    public static void varToFile(String thisVar, File outFile, boolean appendFlag) throws FileNotFoundException {
        try ( PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outFile, appendFlag))) ) {
                out.println(thisVar);
        } catch (IOException io) { io.printStackTrace(); }
    }
    
}
