package TheGame;

import java.io.File;
import java.io.IOException;
import javax.sound.sampled.*;

import static Config.Constants.*;

public class AudioManager {

    private float currentVolume = 0.5f;
    private Clip backgroundClip;

    public void playSound(String fileName, boolean loop) {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                    new File(ASSETS_PATH + fileName)
            );
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            setClipVolume(clip);

            clip.start();
            if (loop) {
                clip.loop(Integer.MAX_VALUE);
            }
            clip.start();
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
        }
    }

    private void setClipVolume(Clip clip) {

        try {
            if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                // This logarithmic conversion is crucial
                float dB = (float) (Math.log(currentVolume) / Math.log(10.0) * 20.0);
                volumeControl.setValue(dB);
            } else {
                System.out.println("Volume control not supported");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void playCrashSound() {
        playSound(CRASH_SOUND_FILE, false);
    }

    public void playShotSound() {
        playSound(GUN_SHOT_SOUND_FILE, false);
    }

    public void playBGM() {
        try {
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(
                    new File(ASSETS_PATH + BGM_SOUND_FILE)
            );
            backgroundClip = AudioSystem.getClip();
            backgroundClip.open(audioStream);
            updateBackgroundVolume();
            backgroundClip.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (UnsupportedAudioFileException | LineUnavailableException | IOException e) {
            e.printStackTrace();
        }
    }

    private void updateBackgroundVolume() {
        if (backgroundClip != null) {
            try {
                if (backgroundClip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                    FloatControl volumeControl
                            = (FloatControl) backgroundClip.getControl(FloatControl.Type.MASTER_GAIN);

                    float dB = (float) (Math.log(currentVolume) / Math.log(10.0) * 20.0);

                    float min = volumeControl.getMinimum();
                    float max = volumeControl.getMaximum();
                    dB = Math.max(min, Math.min(max, dB));

                    volumeControl.setValue(dB);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void setVolume(float volume) {
        currentVolume = Math.max(0.0f, Math.min(1.0f, volume));

        updateBackgroundVolume();
    }

    public float getCurrentVolume() {
        return currentVolume;
    }
}
