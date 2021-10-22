package application;

/**
 * The Repeat Thread class creates a thread for festival to run on when repeating the word 
 * The ensures that the timer is not momentarily interrupted
 * @author Group 22
 *
 */
public class RepeatThread extends Thread {
	@Override 
	public void run() {
		Controller repeat = new Controller();
		double voiceSpeed = GamesModule.voiceSpeed;
		String word = GamesModule.word;
		
		repeat.repeatWord(voiceSpeed, word);
	}
}

