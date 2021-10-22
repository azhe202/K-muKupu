package application;

import java.net.URL;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 * This class controls all sound in the game
 * @author Group 22
 *
 */
public class Sound {

	public static void playSound(String sound) {
	    URL file = CategorySelection.class.getClassLoader().getResource(sound);
	    final Media media = new Media(file.toString());
	    final MediaPlayer mediaPlayer = new MediaPlayer(media);
	    mediaPlayer.play();
	}
}
