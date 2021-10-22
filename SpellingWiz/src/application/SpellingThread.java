package application;

/**
 * The Spelling Thread class creates a thread for festival to run on in the Games Module
 * This ensures the timer isn't momentarily interrupted
 * @author Group 22
 *
 */
public class SpellingThread extends Thread{
	@Override
	public void run() {
		Controller speak = new Controller();
		String word = GamesModule.word;
		double voiceSpeed = GamesModule.voiceSpeed;
		
		speak.spellingQuestion(word, 0, voiceSpeed);
	}
}