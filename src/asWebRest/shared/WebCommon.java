/*
by Anthony Stump
Created: 11 Feb 2018
Updated: 11 Jan 2020
*/

package asWebRest.shared;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.SocketTimeoutException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.compressors.gzip.GzipCompressorInputStream;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

public class WebCommon {
        
    public Double[] arrayDoubleFromJson(JSONArray inJsonArray) {
        Double[] newArray = new Double[inJsonArray.length()];
        for (int i = 0; i < inJsonArray.length(); i++) {
            Double tempDouble = 0.0;
            try { tempDouble = inJsonArray.getDouble(i); } catch (Exception e) { e.printStackTrace(); }
            newArray[i] = tempDouble;
        }
        return newArray;
    }
        
    public double[] arrayDoubleOldFromJson(JSONArray inJsonArray) {
        double[] newArray = new double[inJsonArray.length()];
        for (int i = 0; i < inJsonArray.length(); i++) {
            double tempDoubleOld = 0.0;
            try { tempDoubleOld = inJsonArray.getDouble(i); } catch (Exception e) { e.printStackTrace(); }
            newArray[i] = tempDoubleOld;
        }
        return newArray;
    }
    
    public float[] arrayFloatFromJson(JSONArray inJsonArray) {
        float[] newArray = new float[inJsonArray.length()];
        for (int i = 0; i < inJsonArray.length(); i++) {
            Float tempFloat = 0.0f;
            try { tempFloat = inJsonArray.getFloat(i); } catch (Exception e) { e.printStackTrace(); }
            newArray[i] = tempFloat;
        }
        return newArray;
    }
    
    public Integer[] arrayIntegerFromJson(JSONArray inJsonArray) {
        Integer[] newArray = new Integer[inJsonArray.length()];
        for (int i = 0; i < inJsonArray.length(); i++) {
            Integer tempInteger = 0;
            try { tempInteger = inJsonArray.getInt(i); } catch (Exception e) { e.printStackTrace(); }
            newArray[i] = tempInteger;
        }
        return newArray;
    }
    
    public ArrayList<Date> arrayListDateFromJson(JSONArray inJsonArray, String formatPattern) {
        DateFormat outFormat = new SimpleDateFormat(formatPattern);
        ArrayList<Date> newArrayList = new ArrayList<>();
        for (int i = 0; i < inJsonArray.length(); i++) {
            try {
                String tDateString = inJsonArray.getString(i);
                int tDateStringLength = tDateString.length();
                if(tDateStringLength != 14) {
                    switch(tDateStringLength) {
                        case 12: tDateString = tDateString + "00"; break;
                    }
                }
                Date tDate = outFormat.parse(tDateString);
                newArrayList.add(tDate);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return newArrayList;
    }
    
    public ArrayList<Float> arrayListFloatFromJson(JSONArray inJsonArray) {
        ArrayList<Float> newArrayList = new ArrayList<>();
        for (int i = 0; i < inJsonArray.length(); i++) {
            Float tempFloat = 0.0f;
            try { tempFloat = inJsonArray.getFloat(i); } catch (Exception e) { e.printStackTrace(); }
            newArrayList.add(tempFloat);
        }
        return newArrayList;
    }        
    
    public ArrayList<Integer> arrayListIntegerFromJson(JSONArray inJsonArray) {
        ArrayList<Integer> newArrayList = new ArrayList<>();
        for (int i = 0; i < inJsonArray.length(); i++) {
            Integer tempInteger = 0;
            try { tempInteger = inJsonArray.getInt(i); } catch (Exception e) { e.printStackTrace(); }
            newArrayList.add(tempInteger);
        }
        return newArrayList;
    }        
    
    public String[] arrayStringFromJson(JSONArray inJsonArray) {
        String[] newArray = new String[inJsonArray.length()];
        for (int i = 0; i < inJsonArray.length(); i++) {
            String tempString = "";
            try { tempString = inJsonArray.getString(i); } catch (Exception e) { e.printStackTrace(); }
            newArray[i] = tempString;
        }
        return newArray;
    }
    
    public String basicInputFilter(String inString) {
        return inString.replace("\'", "\\\'").replace("\"", "\\\"").replace("\n", "\\n");
    }
    
    public String basicInputFilterICS(String inString) {
        return inString
        		.replace("\'", "\\\'")
        		.replace("\"", "\\\"")
        		.replace("\n", "\\n")
        		.replace(",",  "\\,");
    }

	public void copyFile(String sourceFile, String destFile) throws IOException {
		try { 
			Files.copy(Paths.get(sourceFile),
				Paths.get(destFile),
				StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException ix) { ix.printStackTrace(); }
	}

	public void copyFileSilently(String sourceFile, String destFile) {
		try { Files.copy(Paths.get(sourceFile), Paths.get(destFile), StandardCopyOption.REPLACE_EXISTING); } catch (Exception e) { }
	}
	
    public String cryptIt(String passwordIn) throws Exception {
        byte[] hash = hashIt(passwordIn);
        StringBuffer hexString = new StringBuffer();
        for(int i = 0; i< hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }
        
    public void deleteDir(File file) {
            File[] contents = file.listFiles();
            if (contents != null) {
                    for (File f : contents) { deleteDir(f); }
            }
            file.delete();
    }

	public String fileScanner(String path, Charset encoding) throws IOException {
		byte[] encoded = Files.readAllBytes(Paths.get(path));
		return new String(encoded, encoding);
	}

	public List<String> fileSorter(Path folderPath, String objectsToSort) {	
		List<String> sorterList = new ArrayList<>();
		try {
			DirectoryStream<Path> stream = Files.newDirectoryStream(folderPath, objectsToSort);
			for (Path path : stream) { sorterList.add(path.toString()); }
		}
		catch (IOException ix) { ix.printStackTrace(); }
		Collections.sort(sorterList);
		return sorterList;
	}
	
    public String getFileExtension(File thisFile) {
        String fileString = thisFile.toString();
        String fileExtension = null;
        int i = fileString.lastIndexOf('.');
        int p = Math.max(fileString.lastIndexOf('/'), fileString.lastIndexOf("\\"));
        if (i > p) { fileExtension = fileString.substring(i+1); }
        return fileExtension;
    }

    public byte[] hashIt(String passwordIn) throws Exception {
        
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodedHash = digest.digest(passwordIn.getBytes(StandardCharsets.UTF_8));
        return encodedHash;
        
    }
    
    public String htmlStripTease(String stringIn) {
        return Jsoup.parse(stringIn).text();
    }
    
    public boolean isSet(String tStr) {
        if (tStr != null && !tStr.isEmpty()) {
            return true;
        } else { return false; }
    }

    public boolean isSetNotZero(String tStr) {
        if (tStr != null && !tStr.isEmpty() && !tStr.equals("0")) {
            return true;
        } else { return false; }
    }
    
    public String jsonSanitize(String inString) {
        return inString.replace("\'", "\\\'").replace("\"", "\\\"").replace("\n", "\\n");
    }
    
    public String jsonSanitizeStrict(String inString) {
        return inString.replace("\'", "").replace("\"", "").replace("\n", "").replace("\\(", "").replace("\\(", "");
    }

	public void jsoupOutBinary(String thisUrl, File outFile, double toS) {
		int toLength = (int) (1000.0*toS*60);
		String stripPath = outFile.getPath();
		File cacheFile = new File(stripPath+".tmp");
                System.out.println(" --> Downloading [ "+thisUrl+" ] NO MIME AS BINARY");
		try {
			FileOutputStream out = new FileOutputStream(cacheFile);
			org.jsoup.Connection.Response binaryResult = Jsoup.connect(thisUrl)
				.ignoreContentType(true)
				.maxBodySize(1024*1024*1024*100)
				.timeout(toLength)
				.execute();
			out.write(binaryResult.bodyAsBytes());
			out.close();
		}
        catch (SocketTimeoutException stx) { stx.printStackTrace(); }
		catch (Exception e) { e.printStackTrace(); }
		if (cacheFile.length() > 0) {
			moveFile(cacheFile.getPath(), outFile.getPath());
		} else { System.out.println("0 byte download!"); }
		cacheFile.delete();
		System.out.flush();
	}

	public void jsoupOutBinaryNoCache(String thisUrl, File outFile, double toS) {
		int toLength = (int) (1000.0*toS*60);
		String stripPath = outFile.getPath();
		File cacheFile = outFile;
		System.out.println(" --> Downloading [ "+thisUrl+" ] NO MIME AS BINARY - NO CACHE");
		try {
			FileOutputStream out = new FileOutputStream(cacheFile);
			org.jsoup.Connection.Response binaryResult = Jsoup.connect(thisUrl)
				.ignoreContentType(true)
				.maxBodySize(1024*1024*1024*100)
				.timeout(toLength)
				.execute();
			out.write(binaryResult.bodyAsBytes());
			out.close();
		}
                catch (SocketTimeoutException stx) { stx.printStackTrace(); }
		catch (Exception e) { e.printStackTrace(); }
		System.out.flush();
	}

	public void jsoupOutFile(String thisUrl, File outFile) {
		System.out.println(" --> Downloading [ "+thisUrl+" ] NO MIME");
		PrintStream console = System.out;
		try {
			org.jsoup.Connection.Response html = Jsoup.connect(thisUrl).ignoreContentType(true).execute();
			System.setOut(new PrintStream(new FileOutputStream(outFile, false)));
			System.out.println(html.body());
		}
                catch (SocketTimeoutException stx) { stx.printStackTrace(); }
		catch (Exception e) { e.printStackTrace(); }
		System.out.flush();
		System.setOut(console);
	}
    
    public String lastModifiedFile(String path2Check) {
        File fl = new File(path2Check);
        File[] files = fl.listFiles(new FileFilter() {
            public boolean accept(File file) {
                return file.isFile();
            }
        });
        long lastMod = Long.MIN_VALUE;
        File choice = null;
        for (File file : files) {
            if(file.lastModified() > lastMod) {
                choice = file;
                lastMod = file.lastModified();
            }
        }
        return choice.toString();
    }

	public double meters2Feet(double metersIn) { return metersIn*3.28; }
	
    public void moveFile(String oldFileName, String newFileName) {
		System.out.println(" --> Moving [ "+oldFileName+" ] to [ "+newFileName+" ]");
		Path oldFileFile = Paths.get(oldFileName);
		Path newFileFile = Paths.get(newFileName);
		try { 
			Files.move(oldFileFile, newFileFile, StandardCopyOption.REPLACE_EXISTING);
		}
		catch (IOException io) { io.printStackTrace(); }
	}

	public void moveFileSilently(String oldFileName, String newFileName) {
		Path oldFileFile = Paths.get(oldFileName);
		Path newFileFile = Paths.get(newFileName);
		try { 
			Files.move(oldFileFile, newFileFile, StandardCopyOption.REPLACE_EXISTING);
		}
		catch (IOException io) { }
	} 
        
    private String[] multiWordSearchArray(String searchStringIn) {
    	String[] searchArray = searchStringIn.split(" ");
    	return searchArray;
    }
    
    private String nowDate() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
        LocalDateTime now = LocalDateTime.now();  
        String nowTime = dtf.format(now);
        return nowTime;
    }

    public String q2do(String query, List<String> params) throws Exception {
        String messageBack = "Query has not ran yet or failed!";
        try {
            MyDBConnector mdb = new MyDBConnector();
            Connection connection = mdb.getMyConnection();
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
    
    public ResultSet q2rs(String query, List<String> params) throws Exception {
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
    
    public ResultSet q2rs1c(Connection connection, String query, List<String> params) throws Exception {
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
    
    public JSONArray query2json(Connection connection, String query, List<String> params) throws Exception {
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
    
	public void runProcess(String pString) {
		System.out.println(" --> Running [ "+pString+" ]");
		String s = null;
		String[] pArray = { "bash", "-c", pString };
		try { 
			Process p = new ProcessBuilder(pArray).start();
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			while ((s = stdInput.readLine()) != null) { System.out.println(s); }
			while ((s = stdError.readLine()) != null) { System.out.println(s); }
			p.destroy();
		}
		catch (IOException e) { e.printStackTrace(); }
		System.out.flush();
	}
        
	public void runProcessAsynch(String pString) {
		System.out.println(" --> Running asynchronously [ "+pString+" ]");
		String[] pArray = { "bash", "-c", pString };
		try { 
			Process p = Runtime.getRuntime().exec(pArray);
		}
		catch (IOException e) { e.printStackTrace(); }
		System.out.flush();
	}
     
    public void runProcessAsynchNoFlush(String pString) {
        pString = "nohup " + pString;
        System.out.println(" ---> Running asynchronously no flush [ " + pString + " ]");
        String[] pArray = { "bash", "-c", pString };
        try {
            Process p = Runtime.getRuntime().exec(pArray);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
	public void runProcessSilently(String pString) {
		String s = null;
		String[] pArray = { "bash", "-c", pString };
		try { 
			Process p = new ProcessBuilder(pArray).start();
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));
			while ((s = stdInput.readLine()) != null) { System.out.println(s); }
			while ((s = stdError.readLine()) != null) { System.out.println(s); }
			p.destroy();
		}
		catch (IOException e) { e.printStackTrace(); }
		System.out.flush();
	}

	public void runProcessOutFile(String pString, File outFile, boolean appendFlag) throws FileNotFoundException {
		System.out.println(" --> (Output following result to file: "+outFile.getPath()+")");
		String tmpVar = null;
		try { tmpVar = runProcessOutVar(pString); } catch (IOException ix) { ix.printStackTrace(); }
		varToFile(tmpVar, outFile, appendFlag);
	}

	public String runProcessOutVar(String pString) throws java.io.IOException {
		String[] pArray = { "bash", "-c", pString };
		Process proc = new ProcessBuilder(pArray).start();
		InputStream is = proc.getInputStream();
		Scanner co = new Scanner(is).useDelimiter("\\A");
		String val = "";
		if (co.hasNext()) { val = co.next(); } else { val = ""; }
		return val;
	}

	public void sedFileDeleteFirstLine(String fileName) {
		try {
			File thisFileObject = new File(fileName);
			Scanner fileScanner = new Scanner(thisFileObject);
			if (fileScanner.hasNextLine()) {
				fileScanner.nextLine();
				FileWriter fileStream = new FileWriter(thisFileObject);
				BufferedWriter out = new BufferedWriter(fileStream);
				while (fileScanner.hasNextLine()) {
					String next = fileScanner.nextLine();
					if(next.equals("\n")) { out.newLine(); } else { out.write(next); }
					out.newLine();
				}
				out.close();
				fileStream.close();
			}
		} catch (IOException ix) { ix.printStackTrace(); }
	}

	public void sedFileInsertEachLineNew(String subjectFile, String toInsert, String targetFile) {
		try {
			FileInputStream fstream = new FileInputStream(subjectFile);
			BufferedReader br = new BufferedReader(new InputStreamReader(fstream));
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(targetFile), true));
			String strLine = null;
			while ((strLine = br.readLine()) != null) {
				if (strLine.equals("")) {
					System.out.println(" --> Skipping a line...");
				} else {
					System.out.println(" --> Processing: \""+strLine+"\"");		
					String upLine = toInsert+strLine;
					bw.write(upLine);
					bw.newLine();
				}
			}
			bw.close();
			br.close();
		}
		catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
		catch (IOException iox) { iox.printStackTrace(); }
		System.out.flush();
	}

	public void sedFileReplace(String fileName, String toFind, String replaceTo) {
		Path path = Paths.get(fileName);
		Charset charset = StandardCharsets.UTF_8;
		try {		
			String content = new String(Files.readAllBytes(path), charset);
			content = content.trim().replaceAll(toFind, replaceTo);
			Files.write(path, content.getBytes(charset));
		}
		catch (IOException io) { io.printStackTrace(); }
	}

	public double sumListDouble(List<Double> dList) {
		if (dList == null || dList.size() < 1) { return 0; }
		double sum = 0.0;
		for (double tVar : dList) { sum = sum+tVar; }
		return sum;		
	}

	public double sumListInteger(List<Integer> iList) {
		if (iList == null || iList.size() < 1) { return 0; }
		int sum = 0;
		for (int tVar : iList) { sum = sum+tVar; }
		return sum;		
	}
	
    public double tempC2F(double tempC) { return tempC * 9/5 + 32; }

	public void unTarGz(String tarFileStr, String destStr) {
		File tarFile = new File(tarFileStr);
		File dest = new File(destStr);
		dest.mkdirs();
		TarArchiveInputStream tarIn = null;
		try {
			tarIn = new TarArchiveInputStream(new GzipCompressorInputStream(new BufferedInputStream(new FileInputStream(tarFile))));
			TarArchiveEntry tarEntry = tarIn.getNextTarEntry();
			while (tarEntry != null) {
				File destPath = new File(dest, tarEntry.getName());
				System.out.println("Working: " + destPath.getCanonicalPath());
				if (tarEntry.isDirectory()) { destPath.mkdirs(); }
				else {
					if(!destPath.getParentFile().exists()) { destPath.getParentFile().mkdirs(); }					
					destPath.createNewFile();
					byte [] btoRead = new byte[4096];
					BufferedOutputStream bout = new BufferedOutputStream(new FileOutputStream(destPath));
					int len = 0;
					while ((len = tarIn.read(btoRead)) != -1) { bout.write(btoRead, 0, len); }
					bout.close();
					btoRead = null;
				}
				tarEntry = tarIn.getNextTarEntry();
			}
			tarIn.close();
		}
		catch (FileNotFoundException fnf) { fnf.printStackTrace(); }
		catch (IOException iox) { iox.printStackTrace(); }
	}
	
    public String unzipFile(String zipFile, String outputFolder) {
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

    public void varToFile(String thisVar, File outFile, boolean appendFlag) throws FileNotFoundException {
        try ( PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(outFile, appendFlag))) ) {
                out.println(thisVar);
        } catch (IOException io) { io.printStackTrace(); }
    }

    public void zipThisFolder(File sourceFolder, File outputZipFile) {
        String zipFile = outputZipFile.toString();
        File sourceDir = sourceFolder;
        try {
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);
            System.out.println(" --> Creating [ "+outputZipFile.toString()+"]");
            zipThisFolderWorker(sourceDir, zos);
            zos.close();
            System.out.println(" --> Packed all files in [ "+sourceFolder.toString()+" ] to [ "+outputZipFile.toString()+"]. Final size: "+outputZipFile.length());
        }
        catch (IOException ioe) { ioe.printStackTrace(); }
    }

    private void zipThisFolderWorker(File dirObj, ZipOutputStream zos) throws IOException {
        File[] files = dirObj.listFiles();
        byte[] buffer = new byte[8192];
        for(int i = 0; i < files.length; i++) {
            if(files[i].isDirectory()) { zipThisFolderWorker(files[i], zos); continue; }
            FileInputStream fis = new FileInputStream(files[i].getAbsolutePath());
            System.out.print(" --> Compressing: "+files[i].getAbsolutePath()+" ("+files[i].length()+" bytes) -- ");
            zos.putNextEntry(new ZipEntry(files[i].getAbsolutePath()));
            int len;
            while ((len = fis.read(buffer)) > 0) {
                zos.write(buffer, 0, len);
            }
            System.out.print(" DONE!\n");
            zos.closeEntry();
            fis.close();
        }
    }
    
    public void zipThisFile(File fileToZip, File outputZipFile) {
        String zipFile = outputZipFile.toString();
        String sourceFile = fileToZip.toString();
        try {
            byte[] buffer = new byte[4096];
            FileOutputStream fos = new FileOutputStream(zipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);
            File thisFile = new File(sourceFile);
            FileInputStream fis = new FileInputStream(thisFile);
            zos.putNextEntry(new ZipEntry(thisFile.getName()));
            int length;
            while ((length = fis.read(buffer)) > 0) { zos.write(buffer, 0, length); }
            zos.closeEntry();
            fis.close();
            zos.close();
            System.out.println(" --> Packed [ "+fileToZip.toString()+" ] into [ "+outputZipFile.toString()+"]");
        }
        catch (IOException ioe) { ioe.printStackTrace(); }
    }
    
    /* Public accessors */
    public String[] getMultiWordSearchArray(String searchStringIn) { return multiWordSearchArray(searchStringIn); }
    public String getNowDate() { return nowDate(); }
    
}
