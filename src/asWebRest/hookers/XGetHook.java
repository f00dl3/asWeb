/*
by Anthony Stump
Created: 16 Oct 2019
Updated: 17 Oct 2019
 */

package asWebRest.hookers;

import java.io.File;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;

import asUtils.Shares.JunkyBeans;
import asUtils.Shares.StumpJunk;
import asWebRest.action.GetTpAction;
import asWebRest.action.UpdateTpAction;
import asWebRest.dao.TpDAO;
import asWebRest.secure.XBeans;
import asWebRest.shared.MyDBConnector;
import asWebRest.shared.WebCommon;

public class XGetHook {

	public static void doUpdateTpiData(String imageSet) { updateTpiData(imageSet); }
	
	public static void performXGetHook(String imageSet) throws Exception {
		
		// Does not work due to security permissions of Tomcat user.
		XBeans xb = new XBeans();
		JunkyBeans jb = new JunkyBeans();
		StumpJunk sj = new StumpJunk(); 
		
		String uHome = xb.getTpGalleryBasePath();		
		try {
			sj.runProcess("bash " + uHome + "/xGetDiff.sh " + imageSet);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
    
	private static void updateTpiData(String imageSet) {
		
		XBeans xb = new XBeans();
		
		String tpBase = xb.getTpGalleryBasePath();		
		String tpImageBase = xb.getTpGalleryImageBasePath();
		File tpSwap = new File(xb.getTpSwapFolder().toString() + "/xSwap.htm");
		String hashPath = "";
		
		tpBase = tpBase + imageSet + ".html";

		try { StumpJunk.jsoupOutBinary(tpBase, tpSwap, 30); } catch (Exception e) { e.printStackTrace(); }
		
		Scanner tpSwapScanner = null; try {
			tpSwapScanner = new Scanner(tpSwap);
			boolean hashFound = false;
			while(tpSwapScanner.hasNext()) {
				String line = tpSwapScanner.nextLine();
				if(line.contains("galleries") && line.contains("thumbs")) {
					if(hashFound) { break; }
					Pattern p = Pattern.compile("galleries/(.*)/thumbs");
					Matcher m = p.matcher(line);
					if (m.find()) {
						hashPath = m.group(1);
						hashFound = true;
						break;
					}
				}
			}
		} catch (FileNotFoundException fnf) {
			fnf.printStackTrace();
		} catch (Error e) {
			e.printStackTrace();
		}
		
		hashPath = hashPath.replaceAll(" ", "");
		tpImageBase = tpImageBase + hashPath + "/";
		
		System.out.println("DEBUG: HashPath: " + hashPath);
		
		JSONArray hackedImageArray = new JSONArray();
		
		Scanner tpSwapScanner2 = null; try {
			tpSwapScanner2 = new Scanner(tpSwap);
			while(tpSwapScanner2.hasNext()) {
				String line = tpSwapScanner2.nextLine();
				if(line.contains(hashPath+"/thumbs/") && line.contains("\" alt=") ) {
					Pattern p = Pattern.compile(hashPath+"/thumbs/(.*)\" alt=");
					Matcher m = p.matcher(line);
					if(m.find()) {
						String hackedImage = m.group(1);
						hackedImageArray.put(hackedImage);
					}
				}
			}
		} catch (FileNotFoundException fnf) {
			fnf.printStackTrace();
		} catch (Error e) {
			e.printStackTrace();
		}
		
		System.out.println("DEBUG: hackedImageArray: " + hackedImageArray.toString());
		
		JSONArray inventory = new JSONArray();
		
		File vaultPathF = new File(xb.getTpVaultPath() + hashPath + "/full");
		File vaultPathT = new File(xb.getTpVaultPath() + hashPath + "/thumb");
		
		if(vaultPathF.isDirectory()) {
			File[] listFiles = vaultPathF.listFiles();
			for(File file : listFiles) {
				if(file.isFile()) {
					inventory.put(file.getName());
				}
			}
		}
		
		System.out.println("DEBUG: inventory: " + inventory.toString());

        for (int i = 0; i < hackedImageArray.length(); i++) {
        	boolean haveIt = false;
			String thisImage = hackedImageArray.getString(i);
			for (int j = 0; j < inventory.length(); j++) { 
				String inventoryImage = inventory.getString(j);
				if(inventoryImage.equals(thisImage)) { haveIt = true; break; }
			}
			if(haveIt) {
				System.out.println("DEBUG: [ " + thisImage + " ] - Skipping. We have it!");				
			} else {
				System.out.println("DEBUG: [ " + thisImage + " ] - Downloading...");
				String fileToGet = tpImageBase + thisImage;
				File tImageFile = new File(xb.getTpSwapFolder().toString() + "/" + thisImage);
				try { StumpJunk.jsoupOutBinary(fileToGet, tImageFile, 30); } catch (Exception e) { e.printStackTrace(); }
			}
		}
		
		try { 
			StumpJunk.runProcess("cp " + xb.getTpSwapFolder().toString() + "/*.jpg " + vaultPathF.toString());
			StumpJunk.runProcess("mogrify -resize 64x64! " + xb.getTpSwapFolder().toString() + "/*.jpg");
			StumpJunk.runProcess("mv " + xb.getTpSwapFolder().toString() + "/*.jpg " + vaultPathT.toString());			
		} catch (Exception e) { 
			e.printStackTrace();
		}
		
		MyDBConnector mdb = new MyDBConnector();
	    Connection dbc = null;
	    try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
	    UpdateTpAction updateTpAction = new UpdateTpAction(new TpDAO());	      
        List<String> qParams = new ArrayList<>();
		qParams.add(hashPath);
    	try { updateTpAction.setTpCheck2019(dbc, qParams).toString(); } catch (Exception e) { e.printStackTrace(); }
		
    	System.out.println("DEBUG: Completed " + hashPath + "!");
		
	}	
    
}
