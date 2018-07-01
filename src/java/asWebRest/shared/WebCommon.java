/*
by Anthony Stump
Created: 11 Feb 2018
Updated: 1 Jul 2018
*/

package asWebRest.shared;

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
import java.sql.ResultSetMetaData;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

public class WebCommon {
        
    public static Double[] arrayDoubleFromJson(JSONArray inJsonArray) {
        Double[] newArray = new Double[inJsonArray.length()];
        for (int i = 0; i < inJsonArray.length(); i++) {
            Double tempDouble = 0.0;
            try { tempDouble = inJsonArray.getDouble(i); } catch (Exception e) { e.printStackTrace(); }
            newArray[i] = tempDouble;
        }
        return newArray;
    }
        
    public static double[] arrayDoubleOldFromJson(JSONArray inJsonArray) {
        double[] newArray = new double[inJsonArray.length()];
        for (int i = 0; i < inJsonArray.length(); i++) {
            double tempDoubleOld = 0.0;
            try { tempDoubleOld = inJsonArray.getDouble(i); } catch (Exception e) { e.printStackTrace(); }
            newArray[i] = tempDoubleOld;
        }
        return newArray;
    }
    
    public static float[] arrayFloatFromJson(JSONArray inJsonArray) {
        float[] newArray = new float[inJsonArray.length()];
        for (int i = 0; i < inJsonArray.length(); i++) {
            Float tempFloat = 0.0f;
            try { tempFloat = inJsonArray.getFloat(i); } catch (Exception e) { e.printStackTrace(); }
            newArray[i] = tempFloat;
        }
        return newArray;
    }
    
    public static Integer[] arrayIntegerFromJson(JSONArray inJsonArray) {
        Integer[] newArray = new Integer[inJsonArray.length()];
        for (int i = 0; i < inJsonArray.length(); i++) {
            Integer tempInteger = 0;
            try { tempInteger = inJsonArray.getInt(i); } catch (Exception e) { e.printStackTrace(); }
            newArray[i] = tempInteger;
        }
        return newArray;
    }
    
    public static ArrayList<Date> arrayListDateFromJson(JSONArray inJsonArray, String formatPattern) {
        DateFormat outFormat = new SimpleDateFormat(formatPattern);
        ArrayList<Date> newArrayList = new ArrayList<>();
        for (int i = 0; i < inJsonArray.length(); i++) {
            try {
                Date tDate = outFormat.parse(inJsonArray.getString(i));
                newArrayList.add(tDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return newArrayList;
    }
    
    public static ArrayList<Float> arrayListFloatFromJson(JSONArray inJsonArray) {
        ArrayList<Float> newArrayList = new ArrayList<>();
        for (int i = 0; i < inJsonArray.length(); i++) {
            Float tempFloat = 0.0f;
            try { tempFloat = inJsonArray.getFloat(i); } catch (Exception e) { e.printStackTrace(); }
            newArrayList.add(tempFloat);
        }
        return newArrayList;
    }        
    
    public static ArrayList<Integer> arrayListIntegerFromJson(JSONArray inJsonArray) {
        ArrayList<Integer> newArrayList = new ArrayList<>();
        for (int i = 0; i < inJsonArray.length(); i++) {
            Integer tempInteger = 0;
            try { tempInteger = inJsonArray.getInt(i); } catch (Exception e) { e.printStackTrace(); }
            newArrayList.add(tempInteger);
        }
        return newArrayList;
    }        
    
    public static String[] arrayStringFromJson(JSONArray inJsonArray) {
        String[] newArray = new String[inJsonArray.length()];
        for (int i = 0; i < inJsonArray.length(); i++) {
            String tempString = "";
            try { tempString = inJsonArray.getString(i); } catch (Exception e) { e.printStackTrace(); }
            newArray[i] = tempString;
        }
        return newArray;
    }
    
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
        
    public static void deleteDir(File file) {
            File[] contents = file.listFiles();
            if (contents != null) {
                    for (File f : contents) { deleteDir(f); }
            }
            file.delete();
    }

    public static String getFileExtension(File thisFile) {
        String fileString = thisFile.toString();
        String fileExtension = null;
        int i = fileString.lastIndexOf('.');
        int p = Math.max(fileString.lastIndexOf('/'), fileString.lastIndexOf("\\"));
        if (i > p) { fileExtension = fileString.substring(i+1); }
        return fileExtension;
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
    
    private String nowDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now();  
        String nowTime = dtf.format(now);
        return nowTime;
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
        try {
            PreparedStatement pStatement = connection.prepareStatement(query);
            int pit = 1;
            if(params != null) {
                if(isSet(params.toString())) { messageBack += "PARAMS: " + params.toString() + "\n"; }
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
    
    public static JSONArray query2json(Connection connection, String query, List<String> params) throws Exception {
        JSONArray tContainer = new JSONArray();
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
        ResultSetMetaData rsmd = resultSet.getMetaData();
        try {
            int colLength = rsmd.getColumnCount();
            while (resultSet.next()) {
                JSONObject tObject = new JSONObject();
                for(int i = 1; i <= colLength; i++) {
                    resultSet.getString(i);
                    String keyName = rsmd.getColumnName(i);
                    tObject.put(keyName, resultSet.getString(i));
                }
                tContainer.put(tObject);
            }
            resultSet.close();
        } catch (Exception e) { e.printStackTrace(); }
        return tContainer;
    }

    public static double tempC2F(double tempC) { return tempC * 9/5 + 32; }
        
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
    
    /* Public accessors */
    public String getNowDate() { return nowDate(); }
    
}
