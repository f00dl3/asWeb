/*
by Anthony Stump
Created: 15 Jul 2018
Updated: 4 Jan 2020
*/

package asWebRest.hookers;

import asUtils.Shares.JunkyBeans;
//import asWebRest.shared.WebCommon;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import com.spikeify.ffmpeg.FFprobe;
import com.spikeify.ffmpeg.probe.FFmpegFormat;
import com.spikeify.ffmpeg.probe.FFmpegProbeResult;
import com.spikeify.ffmpeg.probe.FFmpegStream;

import java.io.File;
import java.io.IOException;
import org.json.JSONObject;

public class MediaTools {
	
	private void playMediaOnServer(String mp3File) {
		
		Media hit = new Media(new File(mp3File).toURI().toString());
		MediaPlayer mediaPlayer = new MediaPlayer(hit);
		mediaPlayer.play();
		
		/* WebCommon wc = new WebCommon();
		wc.runProcess("ffplay -nodisp \"" + mp3File + "\""); */
		
	}
    
    private JSONObject imageInfo(String fileToWorkWith) {
        JunkyBeans jb = new JunkyBeans();
        final String sysAppPath = jb.getAppSys().toString();
        JSONObject tContainer = new JSONObject();
        int mediaHeight = 0;
        int mediaWidth = 0;
        try {
            FFprobe ffProbe = new FFprobe(sysAppPath+"/ffprobe");
            FFmpegProbeResult probeResult = null;
            probeResult = ffProbe.probe(fileToWorkWith);
            FFmpegFormat format = probeResult.getFormat();
            FFmpegStream stream = probeResult.getStreams().get(0);
            mediaHeight = stream.height;
            mediaWidth = stream.width;
        } catch (IOException ix) {
            ix.printStackTrace();
        }
        tContainer
                .put("imageHeight", mediaHeight)
                .put("imageWidth", mediaWidth);
        return tContainer;
    }

    public void doPlayMediaOnServer(String mp3File) { playMediaOnServer(mp3File); }
    public void doPlayMediaOnServer_TEST() { playMediaOnServer("/extra1/MediaServer/Games/U-Z/Unreal Gold - Foundry.mp3"); }
    public JSONObject getImageInfo(String fileToWorkWith) { return imageInfo(fileToWorkWith); }
    
}
