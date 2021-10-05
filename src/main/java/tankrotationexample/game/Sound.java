package tankrotationexample.game;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public interface Sound {

    default void smallExp() {
            try {
                Clip smallExp;
                AudioInputStream music1 = AudioSystem.getAudioInputStream(TRE.class.getClassLoader().getResource("Explosion_small.wav"));
                smallExp = AudioSystem.getClip();
                smallExp.open(music1);
                smallExp.start();
            } catch (UnsupportedAudioFileException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (LineUnavailableException e) {
                e.printStackTrace();
            }

    }

    default void largeExp() {
        try {
            Clip smallExp;
            AudioInputStream music1 = AudioSystem.getAudioInputStream(TRE.class.getClassLoader().getResource("Explosion_large.wav"));
            smallExp = AudioSystem.getClip();
            smallExp.open(music1);
            smallExp.start();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }

    }

}
