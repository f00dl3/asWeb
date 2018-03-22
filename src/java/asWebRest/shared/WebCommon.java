/*
by Anthony Stump
Created: 11 Feb 2018
Updated: 22 Mar 2018
*/

package asWebRest.shared;

import asWebRest.shared.MyDBConnector;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public class WebCommon {
    
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

    public static byte[] hashIt(String passwordIn) throws Exception {
        
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(passwordIn.getBytes(StandardCharsets.UTF_8));
        return encodedHash;
        
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
        String messageBack = "";
        if(isSet(params.toString())) { messageBack += "PARAMS: " + params.toString() + "\n"; }
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
        pStatement.execute();
        messageBack += "Query successfull!";
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
      
}
