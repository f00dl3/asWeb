/*
by Anthony Stump
Created: 29 Jun 2018
Split off from WebCommon 1 Jul 2018
Updated: 28 Dec 2019
 */

package asWebRest.hookers;

import asWebRest.shared.WebCommon;
import java.io.File;
import java.util.ArrayList;
import org.json.JSONArray;
import org.json.JSONObject;

public class FolderTools {
    
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
    
    public JSONObject lukePathWalker(String folderToWalk) {
    	WebCommon wc = new WebCommon();
        String scanFolder = "";
        JSONObject resultSet = new JSONObject();
        JSONArray fullPathsFiles = new JSONArray();
        JSONArray shortNameFiles = new JSONArray();
        JSONArray fullPathsFolders = new JSONArray();
        JSONArray shortNameFolders = new JSONArray();
        JSONArray sizesOfFiles = new JSONArray();
        try {
            File folderToScan = new File(folderToWalk);
            scanFolder = folderToScan.toString();
            File[] fileListing = folderToScan.listFiles();        
            for (int i = 0; i < fileListing.length; i++) {
                String fileName = fileListing[i].getName();
                String fullPath = fileListing[i].toString();
                long fileSize = fileListing[i].length();
                if (fileListing[i].isDirectory()) {
                    if(wc.isSet(fullPath)) { fullPathsFolders.put(fullPath); }
                    if(wc.isSet(fileName)) { shortNameFolders.put(fileName); }
                } else if (fileListing[i].isFile()) {
                    if(wc.isSet(fullPath)) { fullPathsFiles.put(fullPath); }
                    if(wc.isSet(fileName)) { shortNameFiles.put(fileName); }
                    sizesOfFiles.put(fileSize);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        resultSet
                .put("ScanFolder", scanFolder)
                .put("ShortNameFiles", shortNameFiles)
                .put("FullPathsFiles", fullPathsFiles)
                .put("ShortNameFolders", shortNameFolders)
                .put("FullPathsFolders", fullPathsFolders)
                .put("Size", sizesOfFiles);
        return resultSet;
    }
    
    public JSONObject lukePathWalker2(String folderToWalk) {
    	WebCommon wc = new WebCommon();
        String scanFolder = "";
        JSONObject resultSet = new JSONObject();
        JSONObject fContainer = new JSONObject();
        try {
            File folderToScan = new File(folderToWalk);
            scanFolder = folderToScan.toString();
            File[] fileListing = folderToScan.listFiles();        
            for (int i = 0; i < fileListing.length; i++) {
                JSONObject fObject = new JSONObject();
                String fileName = fileListing[i].getName();
                String fullPath = fileListing[i].toString();
                long fileSize = fileListing[i].length();
                if (fileListing[i].isDirectory()) {
                    fObject.put("type", "folder");
                    if(wc.isSet(fullPath)) { fObject.put("path", fullPath); }
                    if(wc.isSet(fileName)) { fObject.put("name", fileName); }
                } else if (fileListing[i].isFile()) {
                    fObject.put("type", "file");
                    if(wc.isSet(fullPath)) { fObject.put("path", fullPath); }
                    if(wc.isSet(fileName)) { fObject.put("name", fileName); }
                    fObject.put("size", fileSize);
                }
                fContainer.put(fileName, fObject);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        resultSet
                .put("Folder", scanFolder)
                .put("InnerChildren", fContainer);
        return resultSet;
    }
    
    public void rebuildFolder(File folderIn) {
    	folderIn.delete();
    	folderIn.mkdirs();
    }
    
}
