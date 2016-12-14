package colorfight;

import java.io.BufferedInputStream;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.io.InputStream;

/**
 * @author Nisarg Tike
 */

public class Sound {
    
    public void playSound(final String url) {
                    new Thread(new Runnable() {
            public void run() {
              try {
                Clip clip = AudioSystem.getClip();
                InputStream audioSrc = getClass().getResourceAsStream("/sound/" + url);
                InputStream bufferedIn = new BufferedInputStream(audioSrc);
                AudioInputStream inputStream = AudioSystem.getAudioInputStream(bufferedIn);
                clip.open(inputStream);
               clip.start();
                
              } catch (Exception e) {
                System.err.println(e.getMessage());
              }
            }
          }).start();
}
}