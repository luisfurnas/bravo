
package org.academiadecodigo.thunderstructs;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;

//Music handler
public class Music {

    private Clip clip;

    public void startMusic(String musicPath) {
        String pathStr = musicPath;
        URL soundURL;
        AudioInputStream audioInputStream = null;
        try {
            soundURL = getClass().getClassLoader().getResource(pathStr);
            if (soundURL == null) {
                pathStr = pathStr.substring(1);
                File file = new File(pathStr);
                soundURL = file.toURI().toURL();
            }
            audioInputStream = AudioSystem.getAudioInputStream(soundURL);
        } catch (UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
        }
        try {
            clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            clip.start();
            clip.loop(clip.LOOP_CONTINUOUSLY);
        } catch (LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    public void stopMusic() {
        clip.stop();
    }
}