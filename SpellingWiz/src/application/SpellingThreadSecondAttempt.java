package application;

/**
 * This Spelling Thread Second Attempt class creates a thread for festival to run 
 * on when the user gets the first attempt wrong. There are two of these spelling threads 
 * for games module as the attempts variable didn't update correctly in the code, therefore 
 * has been hard coded. 
 * The thread is needed to ensure the timer doesn't momentarily stop.
 * @author Group 22
 *
 */
public class SpellingThreadSecondAttempt extends Thread{
	@Override
	public void run() {
		Controller speak = new Controller();
		String word = GamesModule.word;
		double voiceSpeed = GamesModule.voiceSpeed;
		
		speak.spellingQuestion(word, 1, voiceSpeed);
	}
}
