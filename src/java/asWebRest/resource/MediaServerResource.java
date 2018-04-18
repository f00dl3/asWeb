/*
by Anthony Stump
Created: 18 Feb 2018
Updated: 18 Apr 2018
 */

package asWebRest.resource;

import asWebRest.action.GetMediaServerAction;
import asWebRest.action.UpdateMediaServerAction;
import asWebRest.dao.MediaServerDAO;
import asWebRest.shared.MyDBConnector;
import asWebRest.shared.WebCommon;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import org.json.JSONArray;
import org.json.JSONObject;
import org.restlet.data.Form;
import org.restlet.representation.Representation;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;

public class MediaServerResource extends ServerResource {
    
    @Post
    public String doPost(Representation argsIn) {
        
        WebCommon wc = new WebCommon();
        
        MyDBConnector mdb = new MyDBConnector();
        Connection dbc = null;
        try { dbc = mdb.getMyConnection(); } catch (Exception e) { e.printStackTrace(); }
        
        GetMediaServerAction getMediaServerAction = new GetMediaServerAction(new MediaServerDAO());
        UpdateMediaServerAction updateMediaServerAction = new UpdateMediaServerAction(new MediaServerDAO());
                        
        JSONObject mergedResults = new JSONObject();
        List<String> qParams = new ArrayList<>();
        final Form argsInForm = new Form(argsIn);
        
        String doWhat = null;
        String returnData = "";
         
        try {
            doWhat = argsInForm.getFirstValue("doWhat");
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        if(doWhat != null) {
            switch(doWhat) {
                    
                case "getDbx":
                    String xsMobile = argsInForm.getFirstValue("isMobile");
                    qParams.add(0, "1");
                    qParams.add(1, "0");
                    qParams.add(2, "0");
                    qParams.add(3, "/DBX/%");
                    JSONArray msx = getMediaServerAction.getIndexed(dbc, qParams, xsMobile);
                    returnData += msx.toString();
                    break;
                    
                case "getFileListing":
                    File folderToList = new File(argsInForm.getFirstValue("folderPath"));
                    File[] folderListing = wc.getFolderListing(folderToList);
                    for (File file : folderListing) {
                        String name = file.getName();
                        String extension = wc.getFileExtension(file);
                        String path = file.getPath();
                        long size = file.length();
                        JSONObject thisObject = new JSONObject();
                        thisObject
                            .put("Path", path)
                            .put("Size", size)
                            .put("Type", extension);
                        /* if(extension.equals("jpg")) {
                            try {
                                BufferedImage bimg = ImageIO.read(file);
                                int iHeight = bimg.getHeight();
                                int iWidth = bimg.getWidth();
                                thisObject
                                    .put("Height", iHeight)
                                    .put("Width", iWidth);
                            } catch (IOException ix) {
                                ix.printStackTrace();
                            }
                        } */
                        mergedResults.put(name, thisObject);
                    }
                    returnData += mergedResults.toString();                    
                    break;
                    
                case "getIndexed":
                    String aTest = "0";
                    String isMobile = argsInForm.getFirstValue("isMobile");
                    String aContent = argsInForm.getFirstValue("aContent");
                    if(aContent.equals("1")) { aTest = "1"; }
                    qParams.add(0, aTest);
                    qParams.add(1, aContent);
                    qParams.add(2, "1");
                    qParams.add(3, "0");
                    JSONArray mso = getMediaServerAction.getOverview(dbc);
                    JSONArray msi = getMediaServerAction.getIndexed(dbc, qParams, isMobile);
                    mergedResults
                        .put("Overview", mso)
                        .put("Index", msi);
                    returnData += mergedResults.toString();
                    break;
                  
                case "setPlayed":
                    qParams.add(0, argsInForm.getFirstValue("FileName"));
                    returnData += updateMediaServerAction.setLastPlayed(dbc, qParams);
                    break;
                    
            }
        }    
        
        try { dbc.close(); } catch (Exception e) { e.printStackTrace(); }
       
        return returnData;
        
    }
    
}
