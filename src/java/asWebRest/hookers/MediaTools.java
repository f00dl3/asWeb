/*
by Anthony Stump
Created: 15 Jul 2018
*/

package asWebRest.hookers;

import asUtils.Shares.JunkyBeans;
import com.spikeify.ffmpeg.FFprobe;
import com.spikeify.ffmpeg.probe.FFmpegFormat;
import com.spikeify.ffmpeg.probe.FFmpegProbeResult;
import com.spikeify.ffmpeg.probe.FFmpegStream;
import java.io.IOException;
import org.json.JSONObject;

public class MediaTools {
    
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
    
    public JSONObject getImageInfo(String fileToWorkWith) { return imageInfo(fileToWorkWith); }
    
}
