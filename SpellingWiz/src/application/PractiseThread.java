package application;

/**
 * The Practise Thread class creates a thread for festival to run on in the Practise Module
 * This ensures the timer isn't momentarily interrupted
 * @author Group 22
 *
 */
public class PractiseThread extends Thread {
	@Override
	public void run() {
		Controller speak = new Controller();
		String word = PractiseModule.word;
		double speed = PractiseModule.voiceSpeed;
		
		speak.spellingQuestion(word, 0, speed);
	}
}
