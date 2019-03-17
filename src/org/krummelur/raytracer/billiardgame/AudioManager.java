package org.krummelur.raytracer.billiardgame;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class AudioManager {
    long milliTimeSincePlayed = 0;
    private static AudioManager instance = null;
    HashMap<String, Clip>  clips;

    public static AudioManager getInstance() {
        if(instance == null)
            instance = new AudioManager();
        return instance;
    }
    void loadIfNeededAndPlay(String filename){
        loadIfNeededAndPlay(filename, 1f);
    }
    void loadIfNeededAndPlay(String filename, float gain){


        if(System.currentTimeMillis() - milliTimeSincePlayed < 20)
            return;
            Clip clip = null;
        if(clips.containsKey(filename)){
        milliTimeSincePlayed = System.currentTimeMillis();
                clip = clips.get(filename);
        }
        else
        try {
            // Open an audio input stream.
            URL url = new File(filename).toURI().toURL();
            AudioInputStream audioIn = AudioSystem.getAudioInputStream(url);
            // Get a sound clip resource.
            clip = AudioSystem.getClip();
            // Open audio clip and load samples from the audio input stream.
            clip.open(audioIn);
            clip.start();
            clips.put(filename, clip);
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }
        if (clip != null) {
            clip.stop();
            clip.setFramePosition(0);
            FloatControl gainControl = (FloatControl) clip
                    .getControl(FloatControl.Type.MASTER_GAIN);
            gain = Math.min(Math.max(gain, 0), 1); // number between 0 and 1 (loudest)
            float dB = (float) (Math.log(gain) / Math.log(10.0) * 20.0);
            gainControl.setValue(dB);
            clip.start();
        }

    }

    private AudioManager() {
        clips = new HashMap<>();
    }
}
