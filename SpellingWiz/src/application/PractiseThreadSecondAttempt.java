package application;

/**
 * The Practise Thread Second Attempt thread creates a thread for festival to run on 
 * when the user gets the first attempt wrong. This thread ensures the incorrect sound 
 * is played at the correct time before festival speaks. 
 * @author Group 22
 *
 */

public class PractiseThreadSecondAttempt extends Thread {
	@Override
	public void run() {
		Controller speak = new Controller();
		String word = PractiseModule.word;
		double speed = PractiseModule.voiceSpeed;
		
		speak.spellingQuestion(word, 1, speed);
	}
}
