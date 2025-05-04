
package com.example.game.gui.shared;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.io.IOException;
import java.net.URL;

public class MusicManager {
    private final MediaPlayer mediaPlayer;
    private boolean isPlaying = false;

    public MusicManager(String filename) throws IOException {
        URL resource = getClass().getResource(filename);
        System.out.println(resource);
        if (resource == null) {
            throw new IOException("Audio file not found: " + filename);
        }

        Media media = new Media(resource.toExternalForm());
        mediaPlayer = new MediaPlayer(media);
        mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE); // Loop forever
    }

    public void toggleMusic() {
        if (isPlaying) {
            mediaPlayer.stop();
        } else {
            mediaPlayer.seek(mediaPlayer.getStartTime());
            mediaPlayer.play();
        }
        isPlaying = !isPlaying;
    }

    public boolean isMusicPlaying() {
        return isPlaying;
    }
}
