/*
by Anthony Stump
Created: 13 May 2019
Updated: on Creation
 */

package asWebRest.resource;

import java.io.*;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.io.IOUtils;
import org.restlet.data.MediaType;
import org.restlet.data.Status;
import org.restlet.ext.fileupload.RestletFileUpload;
import org.restlet.representation.Representation;
import org.restlet.representation.StringRepresentation;
import org.restlet.resource.Options;
import org.restlet.resource.Post;
import org.restlet.resource.ResourceException;
import org.restlet.resource.ServerResource;

import asWebRest.shared.CommonBeans;
import asWebRest.shared.WebCommon;

public class UploadResource extends ServerResource {

    private Representation doHandleUpload(Representation entity, String pathOverride) throws FileUploadException {
        
    	WebCommon wc = new WebCommon();
    	CommonBeans cb = new CommonBeans();
    	
        File cacherFolder = new File(cb.getPathChartCache());
        if(wc.isSet(pathOverride)) { cacherFolder = new File(pathOverride); }
        if(!cacherFolder.exists()) { cacherFolder.mkdirs(); }
        
        Representation result = null;
        int sizeThreshold = 1000240;
        if(entity != null & MediaType.MULTIPART_FORM_DATA.equals(
                entity.getMediaType(), true)) {
            DiskFileItemFactory factory = new DiskFileItemFactory();
            factory.setSizeThreshold(sizeThreshold);
            RestletFileUpload upload = new RestletFileUpload(factory);
            try {
                FileItemIterator fileIterator = upload.getItemIterator(entity);
                boolean found = false;
                while(fileIterator.hasNext()) {
                    FileItemStream fi = fileIterator.next();
                    if(fi.getFieldName().equals("upfile")) {
                        found = true;
                        StringBuilder sb = new StringBuilder("media type: ");
                        String fileName = fi.getName();
                        File outFile = new File(cacherFolder.toString() + "/" + fileName);
                        sb
                               .append(fi.getContentType()).append("\n")
                               .append("file name : ").append(fileName).append("\n");
                        try(OutputStream outputStream = new FileOutputStream(outFile)) {
                            IOUtils.copy(fi.openStream(), outputStream);
                            sb.append("\n\nFile succesfully wrote - " + outFile.toString()); 
                        } catch (Exception e) { 
                            e.printStackTrace();
                        }
                        result = new StringRepresentation(sb.toString(), MediaType.TEXT_PLAIN);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
           setStatus(Status.CLIENT_ERROR_BAD_REQUEST);
           throw new ResourceException(Status.CLIENT_ERROR_UNSUPPORTED_MEDIA_TYPE);
        }
        System.out.println("This was reached");
        return result;
    }
    
    @Post @Options
    public Representation handleUpload(Representation entity) {
        
       Representation result = null;       
       try { result = doHandleUpload(entity, null); } catch (Exception e) { e.printStackTrace(); }
       return result; 
       
    }
    
}
