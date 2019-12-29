/*
by Anthony Stump
Created: 27 Dec 2017
Updated: 29 Dec 2019
utilizes:
    https://github.com/bramp/ffmpeg-cli-wrapper
    https://stackoverflow.com/questions/3571223/how-do-i-get-the-file-extension-of-a-file-in-java
 */

package asUtilsPorts;

import asUtils.Shares.JunkyBeans;
import asWebRest.shared.WebCommon;

import com.spikeify.ffmpeg.FFprobe;
import com.spikeify.ffmpeg.probe.FFmpegFormat;
import com.spikeify.ffmpeg.probe.FFmpegProbeResult;
import com.spikeify.ffmpeg.probe.FFmpegStream;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import org.apache.pdfbox.pdmodel.PDDocument;

public class MediaImporter {
	
	WebCommon wc = new WebCommon();
    
    public String getDesiredInput(String what, boolean required) {
        if(!required) { what = what+" (or Enter)"; }
        Scanner reader = new Scanner(System.in);
        System.out.println(" --> Enter desired "+what+" for the content: ");
        String desiredInput = reader.nextLine();
        if(required) { 
            if(!wc.isSet(desiredInput)) {
                System.out.println("You did not enter a "+what+"!");
                System.exit(0);
            }
        }
        return desiredInput;
    }
    
    public String getDesiredFolder(File msRoot) {
        final File[] msListing = msRoot.listFiles();
        String desiredFolder = null;
        Scanner reader = new Scanner(System.in);
        System.out.print("Folders: "); for (File folder : msListing) { System.out.print(folder.getName()+" "); }
        System.out.println("\n --> Entered desired folder under the Media Server: ");
        String subfolderInput = reader.nextLine();
        File desiredFolderObj = new File(msRoot.toString()+"/"+subfolderInput);
        if(desiredFolderObj.exists() && wc.isSet(subfolderInput)) {
            desiredFolder = desiredFolderObj.toString().replace(msRoot.toString(),"");
        } else {
            System.out.println("Desired folder path ("+desiredFolderObj.toString()+") is invalid!");
            System.exit(0);
        }
        return desiredFolder;
    }

    public void main(String[] args) {
  
        JunkyBeans junkyBeans = new JunkyBeans();
        Date date = new Date();
        
        String inMpaa = null;
        String inMpaaDesc= null;
        String inXTags = null;
               
        final DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        final String currentDate = dateFormat.format(date);
        //final File fileToWorkWith = new File(junkyBeans.getCodeBase().toString()+"/testOut/Selena Gomez Marshmello - Wolves.mp3");
        final File fileToWorkWith = new File(args[0]);
        final File sysAppPath = junkyBeans.getAppSys();
        final File msRoot = junkyBeans.getMediaServerRoot();
        final String fileExtension = wc.getFileExtension(fileToWorkWith);
        final String thisFileSize = Long.toString(fileToWorkWith.length()/1024);
        final String inDesiredFolder = getDesiredFolder(msRoot);
        final int inAsset = Integer.parseInt(getDesiredInput("Asset? (0/1)", true));
        final String inArtist = getDesiredInput("Artist", true);
        final String inDescription = getDesiredInput("Description", true);
        final String inContentDate = getDesiredInput("Content date yyyy-mm-dd", false);
        final String inAlbumArt = getDesiredInput("Album Art file", false);
        final String inGeoData = getDesiredInput("GeoData [lon,lat]", false);
        final int inAdult = Integer.parseInt(getDesiredInput("Adult? (0/1)", true));
        if(inDesiredFolder.contains("Movies")) {
            inMpaa = getDesiredInput("MPAA Rating", false);
            inMpaaDesc = getDesiredInput("MPAA Rating Description", false);
        }
        if(inDesiredFolder.contains("Adult") || inDesiredFolder.contains("DBX")) {
            inXTags = getDesiredInput("xTags", true);
        }
        final int gifVer = 3;
        
        int mediaDuration = 0;
        long mediaBitRate = 0;
        int mediaHeight = 0;
        int mediaWidth = 0;
        int mediaSampleRate = 0;
        int mediaChannels = 0;

        if(args.length == 0) {
            System.out.println("No file entered! Exiting!");
            System.exit(0);
        }
           
        try {
            FFprobe ffProbe = new FFprobe(sysAppPath+"/ffprobe");
            FFmpegProbeResult probeResult = null;
            probeResult = ffProbe.probe(fileToWorkWith.toString());
            FFmpegFormat format = probeResult.getFormat();
            FFmpegStream stream = probeResult.getStreams().get(0);
            mediaDuration = (int) Math.round(format.duration);
            mediaBitRate = format.bit_rate;
            mediaHeight = stream.height;
            mediaWidth = stream.width;
            //mediaSampleRate = stream.sample_rate;
            //mediaChannels = stream.channels;
        } catch (IOException ix) {
            ix.printStackTrace();
        }
        
	int mediaBitRateKbps = Math.round(mediaBitRate / 1000);
        String mediaResolution = mediaHeight+"x"+mediaWidth;
        
        int itemPages = 0;
               
        String qs1 = "INSERT IGNORE INTO Core.MediaServer ("
                + "Path, Size, File, Description, Artist,"
                + "LastSelected, Asset, OffDisk, Working,"
                + "Archived, WarDeploy, DateIndexed, Adult";
        
        String qs2 = ") VALUES ("
                + "'"+inDesiredFolder+"',"+thisFileSize+",'"+fileToWorkWith.getName()+"','"+inDescription+"','"+inArtist+"',"
                + "0,"+inAsset+",0,1,"
                + "0,0,'"+currentDate+"',"+inAdult;
        
        if(wc.isSet(inContentDate)) { qs1 += ", ContentDate"; qs2 += ",'"+inContentDate+"'"; }
        if(wc.isSet(inGeoData)) { qs1 += ", GeoData"; qs2 += ", '"+inGeoData+"'"; }
        if(wc.isSet(inAlbumArt)) { qs1 += ", AlbumArt"; qs2 += ", '"+inAlbumArt+"'"; }
        if(!mediaResolution.equals("0x0")) { qs1 += ", Resolution"; qs2 += ", '"+mediaResolution+"'"; }
        
        switch(fileExtension) {
            case "mp3":
            case "wav":
            case "ogg":
                qs1 += ", BitRate, DurSec, Hz, Channels";
                qs2 += ","+mediaBitRateKbps+","+mediaDuration+","+mediaSampleRate+","+mediaChannels;
                break;
            case "mp4":
            case "flv":
            case "wmv":
                if(wc.isSet(inMpaa)) { qs1 += ", MPAA"; qs2 += ", '"+inMpaa+"'"; }
                if(wc.isSet(inMpaaDesc)) { qs1 += ", MPAAContent"; qs2 += ", '"+inMpaaDesc+"'"; }
                if(wc.isSet(inXTags)) {
                    qs1 += ", GIFVer, XTags";
                    qs2 += ","+gifVer+",'"+inXTags+"'";
                }
                qs1 += ", BitRate, DurSec, Hz, Channels";
                qs2 += ","+mediaBitRateKbps+","+mediaDuration+","+mediaSampleRate+","+mediaChannels;
                break;
            case "pdf":
                try {
                    PDDocument tDoc = PDDocument.load(fileToWorkWith);
                    itemPages = tDoc.getNumberOfPages();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                qs1 += ", Pages"; qs2 += ","+itemPages;
                break;
            case "txt":
                itemPages = 1;
                qs1 += ", Pages"; qs2 += ","+itemPages;
        }
        
        final String joinedQueryString = (qs1 + qs2 + ");");
        
        System.out.println(joinedQueryString);
        
    }
    
}