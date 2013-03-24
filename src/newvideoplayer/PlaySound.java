/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hvideoplayer;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.Timer;

/**
 *
 * @author Vaishnavi
 */
public class PlaySound extends Thread{
    
    public static int audioCounter = 0;
    //private static int BUFFER_SIZE, frameNumber;
    private static File soundFile=null;
    //private static String audioFileName=null;
    private static AudioInputStream audioStream=null;
    private static AudioFormat audioFormat;
    private static SourceDataLine sourceLine=null;
    //private static long n;
    public static boolean pauseFlag=false; 
    public static Timer audioTimer;

    public PlaySound(String readPath, int startFrame) throws IOException{
        readPath = readPath.split(".rgb")[0]+".wav";
        audioCounter = startFrame;
        
        try {
            soundFile = new File(readPath);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }

        try {
            if(audioStream!=null)
                audioStream = null;
                
            audioStream = AudioSystem.getAudioInputStream(soundFile);
        } catch (Exception e){
            e.printStackTrace();
           System.exit(1);
        }
        
        //System.out.println(audioStream.getFrameLength()+"file size "+soundFile.length());
        
        /*Long n = audioStream.getFrameLength();
        n = n*startFrame*
        System.out.println(audioStream.getFrameLength()+" "+);*/
                
        
        Long n = soundFile.length()/720;
        n = n*startFrame;       
        audioStream.skip(n);    //skip to the startFrame
        audioFormat = audioStream.getFormat();
        
        DataLine.Info info = new DataLine.Info(SourceDataLine.class, audioFormat);
        
        try {
            if(sourceLine!=null)
                sourceLine.drain();
            sourceLine = (SourceDataLine) AudioSystem.getLine(info);
            sourceLine.open(audioFormat);
        } catch (LineUnavailableException e) {
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
        sourceLine.start();
    }
    
    @Override
    public void run(){
        
        if(audioTimer==null){
        audioTimer = new Timer((1000/24), new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    PlayAudio();
                } catch (UnsupportedAudioFileException ex) {
                    Logger.getLogger(PlaySound.class.getName()).log(Level.SEVERE, null, ex);
                } catch (IOException ex) {
                    Logger.getLogger(PlaySound.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
        audioTimer.start();
        }
    }
    
    
    public void PlayAudio() throws UnsupportedAudioFileException, IOException{
        
        int nBytesRead = 0;
        
        byte[] abData = new byte[(int)(soundFile.length()/720)];
        if(audioCounter<720){
            try {
                nBytesRead = audioStream.read(abData, 0, (int)(soundFile.length()/720));
            } catch (IOException exc) {
                exc.printStackTrace();
            }
            if (nBytesRead >= 0) {
                @SuppressWarnings("unused")
                int nBytesWritten = sourceLine.write(abData, 0, nBytesRead);
                audioCounter++;
            }
        }
        else{
            sourceLine.drain();
            audioStream = AudioSystem.getAudioInputStream(soundFile);
            audioCounter=0;
        }
    }
}
