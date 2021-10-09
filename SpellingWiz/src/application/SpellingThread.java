package application;

/**
 * This class creates a thread for festival to run on
 */
public class SpellingThread extends Thread{
	@Override
	public void run() {
		Controller speak = new Controller();
		String word = GamesModule.word;
		int wordCount = GamesModule.wordCount;
		double voiceSpeed = GamesModule.voiceSpeed;
		
		speak.spellingQuestion(word, wordCount, 0, 5, voiceSpeed);
	}
}