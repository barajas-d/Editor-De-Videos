/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author edwin
 */
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;

import javax.imageio.ImageIO;

public class Video {

    public Video(String urlVideo,String urlFinal) throws Exception{
        
        File file = new File(urlVideo);

        FileInputStream fin = new FileInputStream(file);
        byte b[] = new byte[(int)file.length()];
        fin.read(b);

        File nf = new File(urlFinal);
        FileOutputStream fw = new FileOutputStream(nf);
        fw.write(b);
        fw.flush();
        fw.close();
        /*String videoFile = "sample.mp4";
        //path
        String videoPath = videoFolder + videoFile;
        //audio path
        String extractAudio=videoFolder+"videogen\\extraxAudio.mp3";
        try{
            //check the audio file exist or not ,remove it if exist
            File extractAudioFile = new File(extractAudio);
            if (extractAudioFile.exists()) {
                extractAudioFile.delete();
            }
            //audio recorder，extractAudio:audio path，2:channels 
            FFmpegFrameRecorder recorder = new FFmpegFrameRecorder(extractAudio, 2);
            recorder.setAudioOption("crf", "0");
            recorder.setAudioQuality(0);
            //bit rate
            recorder.setAudioBitrate(192000);
            //sample rate
            recorder.setSampleRate(44100);
            recorder.setAudioChannels(2);
            //encoder
            recorder.setAudioCodec(avcodec.AV_CODEC_ID_MP3);
            //start
            recorder.start();
            //load video
            FFmpegFrameGrabber grabber = FFmpegFrameGrabber.createDefault(videoPath);
            grabber.start();
            Frame f=null;
            //get audio sample and record it
            while ((f = grabber.grabSamples()) != null) {
            recorder.record(f);
            }
            // stop to save
            grabber.stop();
            recorder.close();
            //output audio path
            LOGGER.info(extractAudio);
        } catch (Exception e) {
            LOGGER.error("", e);
        }

        }
        */
    }
}
