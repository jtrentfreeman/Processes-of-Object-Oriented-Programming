package pong;

import java.io.File;
import java.util.ArrayList;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.FloatControl;

public class Audio {

    private ArrayList<String> musicFiles;
    private int currentSongIndex;

    public Audio(String... files){
        musicFiles = new ArrayList<String>();
        
        String OS = System.getProperty("os.name");
        Boolean Windows = false;
        
        if( OS.length() > 8 )
            Windows = "Windows".equals(OS.substring(0,7));
        
        // Specify proper file path and have .wav audio file.
        if( Windows )
            for(String file : files)
                musicFiles.add("./src/pong/" + file + ".wav");
        else
            for(String file : files)
                musicFiles.add("./pong/" + file + ".wav");
    }

    private void playBGM(String fileName){

        try{
            File soundFile = new File(fileName);
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);
            AudioFormat format = ais.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip clip = (Clip)AudioSystem.getLine(info);
            clip.open(ais);

            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-20);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            Thread.sleep(1000);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    private void playSound(String fileName) {
        try{
            File soundFile = new File(fileName);
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);
            AudioFormat format = ais.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            Clip clip = (Clip)AudioSystem.getLine(info);
            clip.open(ais);

            FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-20);
            clip.start();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void runSound() { playSound(musicFiles.get(currentSongIndex));}

    public void runBGM() {
        playBGM(musicFiles.get(currentSongIndex));
    }
}
